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
	
	private IPubSubClient mqttClient = null;
	private IPubSubClient cloudClient = null;
	private IPersistenceClient persistenceClient = null;
	private IRequestResponseClient smtpClient = null;
	private CoapServerGateway coapServer = null;
	
	private SystemPerformanceManager sysPerfManager = null;
	
	// constructors: initializing the class
	public DeviceDataManager()
	{
		super();
		
		this.sysPerfManager = new SystemPerformanceManager();
		ConfigUtil configUtil = ConfigUtil.getInstance();
		this.enableMqttClient  = configUtil.getBoolean(ConfigConst.GATEWAY_DEVICE, ConfigConst.ENABLE_MQTT_CLIENT_KEY);
		this.enableCoapServer  = configUtil.getBoolean(ConfigConst.GATEWAY_DEVICE, ConfigConst.ENABLE_COAP_SERVER_KEY);
		this.enableCloudClient = configUtil.getBoolean(ConfigConst.GATEWAY_DEVICE, ConfigConst.ENABLE_CLOUD_CLIENT_KEY);
		this.enableSmtpClient  = configUtil.getBoolean(ConfigConst.GATEWAY_DEVICE, ConfigConst.ENABLE_SMTP_CLIENT_KEY);
		this.enablePersistenceClient = configUtil.getBoolean(ConfigConst.GATEWAY_DEVICE, ConfigConst.ENABLE_PERSISTENCE_CLIENT_KEY);
		
		
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
		_Logger.info("The function handleActuatorCommandResponse is called");
		return false;
	}

	// these will be used as callback methods and the implement the IDataMessageListener interface you imported.
	@Override
	public boolean handleIncomingMessage(ResourceNameEnum resourceName, String msg)
	{
		_Logger.info("The function handleIncomingMessage is called");
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
		_Logger.info("The function handleSensorMessage is called");
		String sdJson = dataUtil.sensorDataToJson(data);
		handleUpstreamTransmission(resourceName,sdJson, ConfigConst.DEFAULT_QOS);
		handleSensorDataAnalysis(data);
		return false;
	}

	// these will be used as callback methods and the implement the IDataMessageListener interface you imported.
	@Override
	public boolean handleSystemPerformanceMessage(ResourceNameEnum resourceName, SystemPerformanceData data)
	{
		_Logger.info("The function handleSystemPerformanceMessage is called");
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
			this.mqttClient = new MqttClientConnector();
		}
		//add by miaoyao@11/16/2020: in order to test python coap connect
		if(this.enableCoapServer) {
			this.coapServer = new CoapServerGateway();
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
	private boolean handleUpstreamTransmission(ResourceNameEnum resourceName, String jsonData, int qos)
	{
		_Logger.info("The function handleUpstreamTransmission is called");
		return false;
	}
	
	/**
	 * Call this from handleSensorMessage()
	 * @param data
	 */
	private void handleSensorDataAnalysis(SensorData data) 
	{
		float val = data.getValue();
		float type = data.getSensorType();
		boolean useThreshold = false; //now we don't have threshold
		if(useThreshold) {
			ActuatorData ad = new ActuatorData();
			String adJson = dataUtil.actuatorDataToJson(ad);
			handleUpstreamTransmission(ResourceNameEnum.CDA_ACTUATOR_CMD_RESOURCE, adJson, ConfigConst.DEFAULT_QOS);
		}
		
	}
	
}
