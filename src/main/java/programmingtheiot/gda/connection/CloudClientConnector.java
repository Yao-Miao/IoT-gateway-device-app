/**
 * This class is part of the Programming the Internet of Things project.
 * 
 * It is provided as a simple shell to guide the student and assist with
 * implementation for the Programming the Internet of Things exercises,
 * and designed to be modified by the student as needed.
 */ 

package programmingtheiot.gda.connection;


import java.util.Properties;
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

/**
 * Shell representation of class for student implementation.
 *
 */
public class CloudClientConnector implements ICloudClient
{
	// static
	
	private static final Logger _Logger =
		Logger.getLogger(CloudClientConnector.class.getName());
	
	// private var's
	
	private String topicPrefix = "";
	private MqttClientConnector mqttClient = null;
	private IDataMessageListener dataMsgListener = null;
	private int qosLevel;
	
	// constructors
	
	/**
	 * Default.
	 * 
	 */
	public CloudClientConnector()
	{
		ConfigUtil configUtil = ConfigUtil.getInstance();
		
		this.topicPrefix =
			configUtil.getProperty(ConfigConst.CLOUD_GATEWAY_SERVICE, ConfigConst.BASE_TOPIC_KEY);
		
		this.qosLevel =  ConfigConst.DEFAULT_QOS;
		// Depending on the cloud service, the topic names may or may not begin with a "/", so this code
		// should be updated according to the cloud service provider's topic naming conventions
		if (topicPrefix == null) {
			topicPrefix = "/";
		} else {
			if (! topicPrefix.endsWith("/")) {
				topicPrefix += "/";
			}
		}
	}
	
	
	// public methods
	
	public boolean isMqttClientConnected() {
		if (this.mqttClient == null) {
			return false;
		}
		
		return this.mqttClient.isConnected();
	}
	//Create the mqtt client and connenct
	@Override
	public boolean connectClient()
	{
		if (this.mqttClient == null) {
			this.mqttClient = new MqttClientConnector(true);
		}
		this.mqttClient.setDataMessageListener(this.dataMsgListener);
		return this.mqttClient.connectClient();
	}
	
    //disconnenct the client
	@Override
	public boolean disconnectClient()
	{
		if (this.mqttClient != null) {
			return this.mqttClient.disconnectClient();
		}
		return false;
		
		
	}
	
	
	// set DataMessageListener
	@Override
	public boolean setDataMessageListener(IDataMessageListener listener)
	{
		this.dataMsgListener = listener;
		return true;
	}

	//send data to cloud service
	@Override
	public boolean sendEdgeDataToCloud(ResourceNameEnum resource, SensorData data)
	{
		if (resource != null && data != null) {
			String payload = DataUtil.getInstance().sensorDataToJson(data);
			
			return publishMessageToCloud(resource, data.getName(), payload);
		}
		
		return false;
	}
	
	//send data to cloud service
	@Override
	public boolean sendEdgeDataToCloud(ResourceNameEnum resource, ActuatorData data)
	{
		if (resource != null && data != null) {
			SensorData actuatorData = new SensorData();
			if(data.getActuatorType() == 3) {
				actuatorData.setName(ConfigConst.SPR_ACTUATOR_NAME);
			}else if(data.getActuatorType() == 4) {
				actuatorData.setName(ConfigConst.SPR_CTRL_ACTUATOR_NAME);
			}
			actuatorData.setValue(data.getCommand());
			
			boolean actuatorDataSuccess = sendEdgeDataToCloud(resource, actuatorData);
			
			if (! actuatorDataSuccess) {
				_Logger.warning("Failed to send CPU Actuator data to cloud service.");
			}
			return actuatorDataSuccess;
		}
		
		return false;
	}
	
	//send data to cloud service
	@Override
	public boolean sendEdgeDataToCloud(ResourceNameEnum resource, SystemPerformanceData data)
	{
		if (resource != null && data != null) {
			SensorData cpuData = new SensorData();
			cpuData.setName(ConfigConst.CPU_UTIL_NAME);
			cpuData.setValue(data.getCpuUtilization());
			
			boolean cpuDataSuccess = sendEdgeDataToCloud(resource, cpuData);
			
			if (! cpuDataSuccess) {
				_Logger.warning("Failed to send CPU utilization data to cloud service.");
			}
			
			SensorData memData = new SensorData();
			memData.setName(ConfigConst.MEM_UTIL_NAME);
			memData.setValue(data.getMemoryUtilization());
			
			boolean memDataSuccess = sendEdgeDataToCloud(resource, memData);
			
			if (! memDataSuccess) {
				_Logger.warning("Failed to send memory utilization data to cloud service.");
			}
			
			return (cpuDataSuccess == memDataSuccess);
		}
		
		return false;
	}

	//subscribe cloud topic
	@Override
	public boolean subscribeToEdgeEvents(ResourceNameEnum resource)
	{
		boolean success = false;
		
		String topicName = null;
		
		if (isMqttClientConnected()) {
			topicName = createTopicName(resource);
			
			_Logger.info("[TEST]:topicName->" + topicName );
			this.mqttClient.subscribeToTopic(topicName + "/lv", this.qosLevel);
			
			success = true;
		} else {
			_Logger.warning("Subscription methods only available for MQTT. No MQTT connection to broker. Ignoring. Topic: " + topicName);
		}
		
		return success;
	}
	
	//subscribe cloud topic
	public boolean subscribeToEdgeEvents(String topicName)
	{
		boolean success = false;
		
		if (isMqttClientConnected()) {
			
			_Logger.info("[TEST]:topicName->" + topicName );
			this.mqttClient.subscribeToTopic(topicName + "lv", this.qosLevel);
			
			success = true;
		} else {
			_Logger.warning("Subscription methods only available for MQTT. No MQTT connection to broker. Ignoring. Topic: " + topicName);
		}
		
		return success;
	}

	//unsubscribe cloud topic
	@Override
	public boolean unsubscribeFromEdgeEvents(ResourceNameEnum resource)
	{
		boolean success = false;
		
		String topicName = null;
		
		if (isMqttClientConnected()) {
			topicName = createTopicName(resource);
			
			this.mqttClient.unsubscribeFromTopic(topicName);
			
			success = true;
		} else {
			_Logger.warning("Unsubscribe method only available for MQTT. No MQTT connection to broker. Ignoring. Topic: " + topicName);
		}
		
		return success;
	}
	
	
	
	// private methods
	// create cloud topic name
	private String createTopicName(ResourceNameEnum resource)
	{
		return this.topicPrefix + resource.getDeviceName() + "/" + resource.getResourceType();
	}
	
	
	// publish message to cloud service
	private boolean publishMessageToCloud(ResourceNameEnum resource, String itemName, String payload)
	{
		String topicName = createTopicName(resource) + "-" + itemName;
		
		try {
			_Logger.finest("Publishing payload value(s) to Ubidots: " + topicName);
			
			this.mqttClient.publishMessage(topicName, payload.getBytes(), this.qosLevel);
			
			return true;
		} catch (Exception e) {
			_Logger.warning("Failed to publish message to Ubidots: " + topicName);
		}
		
		return false;
	}
	
	
}