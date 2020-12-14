/**
 * This class is part of the Programming the Internet of Things project.
 * 
 * It is provided as a simple shell to guide the student and assist with
 * implementation for the Programming the Internet of Things exercises,
 * and designed to be modified by the student as needed.
 */ 

package programmingtheiot.gda.app;

import java.util.logging.Level;
import java.util.logging.Logger;

import programmingtheiot.common.ConfigConst;
import programmingtheiot.common.ConfigUtil;
import programmingtheiot.common.IDataMessageListener;
import programmingtheiot.common.ResourceNameEnum;

import programmingtheiot.data.ActuatorData;
import programmingtheiot.data.DataUtil;
import programmingtheiot.data.SensorData;
import programmingtheiot.data.SystemPerformanceData;
import programmingtheiot.data.SystemStateData;

import programmingtheiot.gda.connection.CloudClientConnector;
import programmingtheiot.gda.connection.CoapClientConnector;
import programmingtheiot.gda.connection.CoapServerGateway;
import programmingtheiot.gda.connection.IPersistenceClient;
import programmingtheiot.gda.connection.IPubSubClient;
import programmingtheiot.gda.connection.IRequestResponseClient;
import programmingtheiot.gda.connection.MqttClientConnector;
import programmingtheiot.gda.connection.RedisPersistenceAdapter;
import programmingtheiot.gda.connection.SmtpClientConnector;

import programmingtheiot.gda.system.SystemPerformanceManager;

/**
 * Shell representation of class for student implementation.
 *
 */
public class DeviceDataManager implements IDataMessageListener
{
	// static
	
	private static final Logger _Logger =
		Logger.getLogger(DeviceDataManager.class.getName());
	
	// private var's
	
	private boolean enableMqttClient = false;
	private boolean enableCoapServer = false;
	private boolean enableCloudClient = false;
	private boolean enableSmtpClient = false;
	private boolean enablePersistenceClient = false;
	private DataUtil dataUtil = DataUtil.getInstance();
	private ConfigUtil configUtil;
	
	private IPubSubClient mqttClient = null;
	//private IPubSubClient cloudClient = null;
	private RedisPersistenceAdapter persistenceClient = null;
	private IRequestResponseClient smtpClient = null;
	private CoapServerGateway coapServer = null;
	private CoapClientConnector coapClient = null;
	private CloudClientConnector cloudClient = null;
	
	private SystemPerformanceManager sysPerfManager = null;
	
	private boolean enableHandleTempChangeOnDevice = false;
	private int sensorDatacount = 0;
	private float tempVal;
	
	// constructors: initializing the class
	public DeviceDataManager()
	{
		
		super();
		this.sysPerfManager = new SystemPerformanceManager();
		this.sysPerfManager.setDataMessageListener(this);
		this.configUtil = ConfigUtil.getInstance();
		this.enableMqttClient  = configUtil.getBoolean(ConfigConst.GATEWAY_DEVICE, ConfigConst.ENABLE_MQTT_CLIENT_KEY);
		this.enableCoapServer  = configUtil.getBoolean(ConfigConst.GATEWAY_DEVICE, ConfigConst.ENABLE_COAP_SERVER_KEY);
		this.enableCloudClient = configUtil.getBoolean(ConfigConst.GATEWAY_DEVICE, ConfigConst.ENABLE_CLOUD_CLIENT_KEY);
		this.enableSmtpClient  = configUtil.getBoolean(ConfigConst.GATEWAY_DEVICE, ConfigConst.ENABLE_SMTP_CLIENT_KEY);
		this.enablePersistenceClient = configUtil.getBoolean(ConfigConst.GATEWAY_DEVICE, ConfigConst.ENABLE_PERSISTENCE_CLIENT_KEY);
		
		
		this.enableHandleTempChangeOnDevice = configUtil.getBoolean(ConfigConst.GATEWAY_DEVICE, ConfigConst.ENABLE_HANDLE_TEMP_CHANGE_KEY);
		
		
		
		
		initConnections();
	}
	
	// constructors: initializing the class
	public DeviceDataManager(
		boolean enableMqttClient,
		boolean enableCoapClient,
		boolean enableCloudClient,
		boolean enableSmtpClient,
		boolean enablePersistenceClient)
	{
		super();
		this.enableMqttClient  = enableMqttClient;
		this.enableCoapServer  = enableCoapClient;
		this.enableCloudClient = enableCloudClient;
		this.enableSmtpClient  = enableSmtpClient;
		this.enablePersistenceClient = enablePersistenceClient;
		
		initConnections();
	}
	
	
	// public methods
	
	// these will be used as callback methods and the implement the IDataMessageListener interface you imported.
	@Override
	public boolean handleActuatorCommandResponse(ResourceNameEnum resourceName, ActuatorData data)
	{
		_Logger.info("[GDA_CALLBACK]The function handleActuatorCommandResponse is called");
		this.cloudClient.sendEdgeDataToCloud(resourceName, data);
		return false;
	}

	// these will be used as callback methods and the implement the IDataMessageListener interface you imported.
	@Override
	public boolean handleIncomingMessage(ResourceNameEnum resourceName, String msg)
	{
		_Logger.info("[GDA_CALLBACK]The function handleIncomingMessage is called");
		if(resourceName == ResourceNameEnum.CDA_ACTUATOR_CMD_RESOURCE) {
			ActuatorData ad = dataUtil.jsonToActuatorData(msg);
			handleIncomingDataAnalysis(resourceName, ad);
		}else {
			SystemStateData ssd = dataUtil.jsonToSystemStateData(msg);
			handleIncomingDataAnalysis(resourceName, ssd);
		}
		
		return false;
	}

	// these will be used as callback methods and the implement the IDataMessageListener interface you imported.
	@Override
	public boolean handleSensorMessage(ResourceNameEnum resourceName, SensorData data)
	{
		_Logger.info("[GDA_CALLBACK]The function handleSensorMessage is called");
		this.cloudClient.sendEdgeDataToCloud(resourceName, data);
		String topic = resourceName.getResourceName() + "/" + data.getName();
		this.persistenceClient.storeData(topic, 0, data);
		handleSensorDataAnalysis(data);
		return false;
	}

	// these will be used as callback methods and the implement the IDataMessageListener interface you imported.
	@Override
	public boolean handleSystemPerformanceMessage(ResourceNameEnum resourceName, SystemPerformanceData data)
	{
		_Logger.info("[GDA_CALLBACK]The function handleSystemPerformanceMessage is called");
		this.cloudClient.sendEdgeDataToCloud(resourceName, data);
		return false;
	}
	
	// start the Device Data Manager
	public void startManager()
	{
		_Logger.info("Starting DeviceDataManager...");
		
		this.sysPerfManager.startManager();
		
		if(this.enableCoapServer) {
			this.coapServer.startServer();
		}
		
		if(this.enableMqttClient) {
			this.mqttClient.connectClient();
		}
		
		if(this.enableCloudClient) {
			this.cloudClient.connectClient();
			//this.cloudClient.subscribeToEdgeEvents(ResourceNameEnum.CDA_ACTUATOR_RESPONSE_RESOURCE);
			this.cloudClient.subscribeToEdgeEvents("/v1.6/devices/gatewaydevice/spractuator-flag");
			
		}
		
		
	}
	
	// stop the Device Data Manager
	public void stopManager()
	{
		
		this.sysPerfManager.stopManager();
		
		if(this.enableCoapServer) {
			this.coapServer.stopServer();
		}
		
		if(this.enableMqttClient) {
			this.mqttClient.disconnectClient();
		}
		
		if(this.enableCloudClient) {
			this.cloudClient.disconnectClient();
		}
		
		
		_Logger.info("Stopping DeviceDataManager...");
	}

	
	// private methods
	
	/**
	 * Initializes the enabled connections. This will NOT start them, but only create the
	 * instances that will be used in the {@link #startManager() and #stopManager()) methods.
	 * 
	 */
	private void initConnections()
	{
		if(this.enableMqttClient) {
			_Logger.info(">>>>>>>>>>>>>>>MQTT server is initilizing.......");
			this.mqttClient = new MqttClientConnector();
			this.mqttClient.setDataMessageListener(this);
		}
		
		//add by miaoyao@11/16/2020: in order to test python coap connect
		if(this.enableCoapServer) {
			_Logger.info(">>>>>>>>>>>>>>>COAP server is initilizing.......");
			this.coapServer = new CoapServerGateway();
			this.coapServer.setDataMessageListener(this);
			this.coapClient = new CoapClientConnector();
			this.coapClient.setDataMessageListener(this);
		}
		
		if(this.enableCloudClient) {
			_Logger.info(">>>>>>>>>>>>>>>Cloud server is initilizing.......");
			this.cloudClient = new CloudClientConnector();
			this.cloudClient.setDataMessageListener(this);
			
		}
		if(this.enablePersistenceClient) {
			_Logger.info(">>>>>>>>>>>>>>>Redis server is initilizing.......");
			this.persistenceClient = new RedisPersistenceAdapter();
		}
		
	}
	
	/**
	 * Call this from handleIncomeMessage()
	 * @param resourceName
	 * @param data
	 */
	private void handleIncomingDataAnalysis(ResourceNameEnum resourceName, ActuatorData data) 
	{
		_Logger.info("The function handleIncomingDataAnalysis is called");
	}
	
	/**
	 * Call this from handleIncomeMessage() 
	 * @param resourceName
	 * @param data
	 */
	private void handleIncomingDataAnalysis(ResourceNameEnum resourceName, SystemStateData data) 
	{
		_Logger.info("The function handleIncomingDataAnalysis is called");
	}
	
	/**
	 * Call this from handleActuatorCommandResponse(), handlesensorMessage()
	 * @param resourceName
	 * @param jsonData
	 * @param qos
	 * @return
	 */
	private synchronized boolean handleUpstreamTransmission(ResourceNameEnum resourceName, String jsonData, int qos)
	{
		_Logger.info("The function handleUpstreamTransmission is called");
		this.coapClient.sendPostRequest(resourceName, false, jsonData, 5);
		//this.mqttClient.publishMessage(resourceName, jsonData, qos);
		return true;
	}
	
	/**
	 * Call this from handleSensorMessage()
	 * @param data
	 */
	private void handleSensorDataAnalysis(SensorData data) 
	{

		if(data.getSensorType() == 3 && enableHandleTempChangeOnDevice) {
			this.sensorDatacount++;
			this.tempVal += data.getValue();
			
			_Logger.info("[TEST] sensorDatacount:" + sensorDatacount);
			if(this.sensorDatacount == 10) {
				float avg = tempVal / sensorDatacount;
				int triggerCtrlTempLevel1 = configUtil.getInteger(ConfigConst.GATEWAY_DEVICE, ConfigConst.TRIGGER_CTRL_TEMP_LEVEL1_KEY);
				int triggerCtrlTempLevel2 = configUtil.getInteger(ConfigConst.GATEWAY_DEVICE, ConfigConst.TRIGGER_CTRL_TEMP_LEVEL2_KEY);
				int triggerCtrlTempLevel3 = configUtil.getInteger(ConfigConst.GATEWAY_DEVICE, ConfigConst.TRIGGER_CTRL_TEMP_LEVEL3_KEY);
				ActuatorData ad = new ActuatorData(4);
				if (avg <= triggerCtrlTempLevel1){
					ad.setCommand(0);
					ad.setValue(0);
				}else if(avg > triggerCtrlTempLevel1 && avg <= triggerCtrlTempLevel2) {
					ad.setCommand(1);
					ad.setValue(1);
				}else if(avg > triggerCtrlTempLevel2 && avg <=  triggerCtrlTempLevel3) {
					ad.setCommand(1);
					ad.setValue(2);
				}else {
					ad.setCommand(1);
					ad.setValue(3);
				}
				String adJson = dataUtil.actuatorDataToJson(ad);
				handleUpstreamTransmission(ResourceNameEnum.CDA_ACTUATOR_CMD_RESOURCE, adJson, ConfigConst.DEFAULT_QOS);
				this.sensorDatacount = 0;
				this.tempVal = 0;
				
			}
		}else if(data.getSensorType() == 2 && enableHandleTempChangeOnDevice) {
			float pressureVal = data.getValue();
			ActuatorData ad = new ActuatorData(3);
			if(pressureVal > 1000 || pressureVal < 200) {
				ad.setCommand(0);
				String adJson = dataUtil.actuatorDataToJson(ad);
				handleUpstreamTransmission(ResourceNameEnum.CDA_ACTUATOR_CMD_RESOURCE, adJson, ConfigConst.DEFAULT_QOS);
			}
		}
	}
	
	/*private void handleSensorDataAnalysis(String topic) 
	{
		SensorData[] sds= this.persistenceClient.getSensorData(topic, null, null);
		float val = 0;
		int count = sds.length;
		int sensorType = sds[0].getSensorType();
		for(SensorData sd : sds) {
			val += sd.getValue();
		}
		float avg = val / count;
		
		if(sensorType == 3 && enableHandleTempChangeOnDevice) {
			int triggerCtrlTempLevel1 = configUtil.getInteger(ConfigConst.GATEWAY_DEVICE, ConfigConst.TRIGGER_CTRL_TEMP_LEVEL1_KEY);
			int triggerCtrlTempLevel2 = configUtil.getInteger(ConfigConst.GATEWAY_DEVICE, ConfigConst.TRIGGER_CTRL_TEMP_LEVEL2_KEY);
			int triggerCtrlTempLevel3 = configUtil.getInteger(ConfigConst.GATEWAY_DEVICE, ConfigConst.TRIGGER_CTRL_TEMP_LEVEL3_KEY);
			
			ActuatorData ad = new ActuatorData(4);
			
			if (avg <= triggerCtrlTempLevel1){
				ad.setCommand(0);
				ad.setValue(0);
			}else if(avg > triggerCtrlTempLevel1 && avg <= triggerCtrlTempLevel2) {
				ad.setCommand(1);
				ad.setValue(1);
			}else if(avg > triggerCtrlTempLevel2 && avg <=  triggerCtrlTempLevel3) {
				ad.setCommand(1);
				ad.setValue(2);
			}else {
				ad.setCommand(1);
				ad.setValue(3);
			}
			String adJson = dataUtil.actuatorDataToJson(ad);
			handleUpstreamTransmission(ResourceNameEnum.CDA_ACTUATOR_CMD_RESOURCE, adJson, ConfigConst.DEFAULT_QOS);
		}
		

		
	}*/
	
	
}
