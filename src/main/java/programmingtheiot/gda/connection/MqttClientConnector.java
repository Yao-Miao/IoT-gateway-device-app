/**
 * This class is part of the Programming the Internet of Things project.
 * 
 * It is provided as a simple shell to guide the student and assist with
 * implementation for the Programming the Internet of Things exercises,
 * and designed to be modified by the student as needed.
 */ 

package programmingtheiot.gda.connection;

import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.SubnodeConfiguration;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import programmingtheiot.common.ConfigCloud;
import programmingtheiot.common.ConfigConst;
import programmingtheiot.common.ConfigUtil;
import programmingtheiot.common.IDataMessageListener;
import programmingtheiot.common.ResourceNameEnum;

import javax.net.ssl.SSLSocketFactory;
import programmingtheiot.common.SimpleCertManagementUtil;

import programmingtheiot.data.DataUtil;
import programmingtheiot.data.SensorData;
import programmingtheiot.data.SystemPerformanceData;
import programmingtheiot.data.ActuatorData;

/**
 * Shell representation of class for student implementation.
 * 
 */
public class MqttClientConnector implements IPubSubClient, MqttCallbackExtended
{
	// static
	
	private static final Logger _Logger =
		Logger.getLogger(MqttClientConnector.class.getName());
	
	// private params
	private String host;
	private int port;
	private int brokerKeepAlive;
	private String clientID;
	private MemoryPersistence persistence;
	private MqttConnectOptions connOpts;
	private String brokerAddr;
	private String protocol = "tcp";
	private MqttClient mqttClient;
	private IDataMessageListener dataMsgListener;
	
	private boolean enableEncryption;
	private boolean useCleanSession;
	private boolean enableAutoReconnect;
	private String pemFileName;
	
	private boolean useCloudGatewayConfig = false;
	
	private String userToken;
	
	private DataUtil dataUtil = DataUtil.getInstance();
	
	// constructors
	
	/**
	 * 
	 * Default constructor. This will set remote broker information and client connection information 
	 * based on the default configuration file contents.
	 * 
	 */
	
	public MqttClientConnector()
	{
		this(false);
	}
	
	public MqttClientConnector(boolean useCloudGatewayConfig)
	{
		super();
		
		this.useCloudGatewayConfig = useCloudGatewayConfig;
		
		if (useCloudGatewayConfig) {
			initClientParameters(ConfigConst.CLOUD_GATEWAY_SERVICE);
		} else {
			initClientParameters(ConfigConst.MQTT_GATEWAY_SERVICE);
		}
	}
	
	/*public MqttClientConnector()
	{
		super();
		
		initClientParameters(ConfigConst.MQTT_GATEWAY_SERVICE);
		
		
	}*/
	
	
	// public methods
	
	@Override
	/**
	 * Connects to an MQTT server
	 */
	public boolean connectClient()
	{
		if (this.mqttClient == null) {
			try {
				this.mqttClient = new MqttClient(this.brokerAddr, this.clientID, this.persistence);
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    this.mqttClient.setCallback(this);
		}
		if (! this.mqttClient.isConnected()) {
		    try {
				this.mqttClient.connect(this.connOpts);
				return true;
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		_Logger.info("Mqtt is already connected");
		return false;
	}

	@Override
	/**
	 * Disconnects from the server.
	 */
	public boolean disconnectClient()
	{
		if (this.mqttClient.isConnected()) {
			try {
				this.mqttClient.disconnect();
				return true;
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		_Logger.info("Mqtt is already disconnected");
		return false;
	}
	
	/**
	 * Check the connection status
	 * @return
	 */
	public boolean isConnected()
	{
		return this.mqttClient.isConnected();
	}
	
	@Override
	/**
	 * Publishes a message to a topic on the server
	 * @param topicName to deliver the message to
	 * @param msg the actual message to send
	 * @param qos the quality of service level to use.
	 */
	public boolean publishMessage(ResourceNameEnum topicName, String msg, int qos)
	{	

		String topic = topicName.getResourceName();
		byte[] payload = msg.getBytes();
		return publishMessage(topic, payload, qos);
	}

	@Override
	/**
	 * Subscribe to a topic
	 * @param topicName the topic to subscribe to
	 * @param qos the quality of service level to use.
	 */
	public boolean subscribeToTopic(ResourceNameEnum topicName, int qos)
	{
		String topic = topicName.getResourceName();
		return subscribeToTopic(topic, qos);
	}

	@Override
	/**
	 * Requests the server unsubscribe the client from a topic.
	 * @param topicName the topic to unsubscribe from
	 */
	public boolean unsubscribeFromTopic(ResourceNameEnum topicName)
	{
		String topic = topicName.getResourceName();
		return unsubscribeFromTopic(topic);
	}

	
	@Override
	public boolean setDataMessageListener(IDataMessageListener listener)
	{
		_Logger.info("The function setDataMessageListener is called");
	    if (listener != null) {
	    	this.dataMsgListener = listener;
	        return true;
	    }
		
	    return false;
	}
	
	//protected method implementations for publish
	protected boolean publishMessage(String topic, byte[] payload, int qos)
	{
		MqttMessage message = new MqttMessage(payload);
		
		if (qos < 0 || qos > 2) {
			qos = 0;
		}
		
		message.setQos(qos);
		
		// NOTE: you may want to log the exception stack trace if the call fails
		try {
			//_Logger.info("Publishing message to topic: " + topic);
			
			this.mqttClient.publish(topic, message);
			
			return true;
		} catch (MqttPersistenceException e) {
			_Logger.warning("Persistence exception thrown when publishing to topic: " + topic);
		} catch (MqttException e) {
			_Logger.warning("MQTT exception thrown when publishing to topic: " + topic);
		}
		
		return false;
	}
	
	//protected method implementations for subscribe
	protected boolean subscribeToTopic(String topic, int qos)
	{
		// NOTE: you may want to log the exception stack trace if the call fails
		try {
			this.mqttClient.subscribe(topic, qos);
			
			return true;
		} catch (MqttException e) {
			_Logger.warning("Failed to subscribe to topic: " + topic);
		}
		
		return false;
	}
	
	//protected method implementations for unsubscribe
	protected boolean unsubscribeFromTopic(String topic)
	{
		// NOTE: you may want to log the exception stack trace if the call fails
		try {
			this.mqttClient.unsubscribe(topic);
			
			return true;
		} catch (MqttException e) {
			_Logger.warning("Failed to unsubscribe from topic: " + topic);
		}
		
		return false;
	}
	
	
	
	
	
	
	
	
	// callbacks
	
	@Override
	/**
	 *  callback methods to handle connection notification events
	 */
	public void connectComplete(boolean reconnect, String serverURI)
	{
		_Logger.info("[GDA_MQTT_CALLBACK]MQTT connection successful (is reconnect = " + reconnect + "). Broker: " + serverURI);
		
		int qos = 1;
		
		this.subscribeToTopic(ResourceNameEnum.CDA_ACTUATOR_RESPONSE_RESOURCE, qos);
		this.subscribeToTopic(ResourceNameEnum.CDA_SENSOR_MSG_RESOURCE, qos);
		this.subscribeToTopic(ResourceNameEnum.CDA_SYSTEM_PERF_MSG_RESOURCE, qos);
		
		// Option 2
		/*try {
			this.mqttClient.subscribe(
				ResourceNameEnum.CDA_ACTUATOR_RESPONSE_RESOURCE.getResourceName(),
				qos,
				new ActuatorResponseMessageListener(ResourceNameEnum.CDA_ACTUATOR_RESPONSE_RESOURCE, this.dataMsgListener));
		} catch (MqttException e) {
			_Logger.warning("Failed to subscribe to CDA actuator response topic.");
		}*/
		
	}

	@Override
	/**
	 *  callback methods to handle connection notification events
	 */
	public void connectionLost(Throwable t)
	{
		_Logger.info("[GDA_MQTT_CALLBACK]The function connectionLost is called");
	}
	
	@Override
	/**
	 *  callback method to handle publish notification events
	 */
	public void deliveryComplete(IMqttDeliveryToken token)
	{
		_Logger.info("[GDA_MQTT_CALLBACK]The function deliveryComplete is called");
	}
	
	@Override
	/**
	 * callback method - this will be called whenever a message is received on the topic for which your client has subscribed
	 */
	public void messageArrived(String topic, MqttMessage msg) throws Exception
	{
		ConfigUtil configUtil = ConfigUtil.getInstance();
		String arriveMsg = msg.toString();
		String sensorTopic = ResourceNameEnum.CDA_SENSOR_MSG_RESOURCE.getResourceName();
		String sysTopic = ResourceNameEnum.CDA_SYSTEM_PERF_MSG_RESOURCE.getResourceName();
		String actuatorTopic = ResourceNameEnum.CDA_ACTUATOR_RESPONSE_RESOURCE.getResourceName();
		/*String cloudTopic = ResourceNameEnum.GDA_MGMT_STATUS_CMD_RESOURCE.getResourceName();*/
		String cloudTopic = configUtil.getProperty(ConfigConst.CLOUD_GATEWAY_SERVICE, ConfigConst.BASE_TOPIC_KEY) + ResourceNameEnum.GDA_MGMT_STATUS_CMD_RESOURCE.getDeviceName() + "/" + ResourceNameEnum.GDA_MGMT_STATUS_CMD_RESOURCE.getResourceType() + "/lv";
		cloudTopic = cloudTopic.toLowerCase();
		
		//_Logger.info("[GDA_MQTT_CALLBACK] The function messageArrived is called + [topic]:" + topic +"&[MSG]:" + msg + ((this.dataMsgListener != null) ? "->true" : "->false"));
		
		_Logger.info("[GDA_MQTT_CALLBACK] The function messageArrived is called + [topic]:" + topic);
		if(this.dataMsgListener != null) {
			
			if(topic.equals(sensorTopic)) {
				SensorData sd = DataUtil.getInstance().jsonToSensorData(arriveMsg);
				this.dataMsgListener.handleSensorMessage(ResourceNameEnum.CDA_SENSOR_MSG_RESOURCE, sd);	
			}else if(topic.equals(sysTopic)) {
				SystemPerformanceData spd = DataUtil.getInstance().jsonToSystemPerformanceData(arriveMsg);
				this.dataMsgListener.handleSystemPerformanceMessage(ResourceNameEnum.CDA_SYSTEM_PERF_MSG_RESOURCE, spd);
			}else if(topic.equals(actuatorTopic)) {
				ActuatorData ad = DataUtil.getInstance().jsonToActuatorData(arriveMsg);
				this.dataMsgListener.handleActuatorCommandResponse(ResourceNameEnum.CDA_ACTUATOR_RESPONSE_RESOURCE, ad);
			}else if(topic.equals(cloudTopic)){
				
				this.dataMsgListener.handleIncomingMessage(ResourceNameEnum.GDA_MGMT_STATUS_CMD_RESOURCE, arriveMsg);
				
				//ActuatorData ad = DataUtil.getInstance().jsonToActuatorData(arriveMsg);
				//this.dataMsgListener.handleIncomingMessage(ResourceNameEnum.GDA_MGMT_STATUS_CMD_RESOURCE, msg);
			}
					
		}
		
	}

	
	// private methods
	
	/**
	 * Called by the constructor to set the MQTT client parameters to be used for the connection.
	 * 
	 * @param configSectionName The name of the configuration section to use for
	 * the MQTT client configuration parameters.
	 */
	private void initClientParameters(String configSectionName)
	{
		ConfigUtil configUtil = ConfigUtil.getInstance();
		ConfigCloud configCloud = ConfigCloud.getInstance();
		
		this.host =
			configUtil.getProperty(
				configSectionName, ConfigConst.HOST_KEY, ConfigConst.DEFAULT_HOST);
		this.port =
			configUtil.getInteger(
				configSectionName, ConfigConst.PORT_KEY, ConfigConst.DEFAULT_MQTT_PORT);
		this.brokerKeepAlive =
			configUtil.getInteger(
				configSectionName, ConfigConst.KEEP_ALIVE_KEY, ConfigConst.DEFAULT_KEEP_ALIVE);
		this.enableEncryption =
			configUtil.getBoolean(
				configSectionName, ConfigConst.ENABLE_CRYPT_KEY);
		this.pemFileName =
			configUtil.getProperty(
				configSectionName, ConfigConst.CERT_FILE_KEY);
		
		if(configSectionName == ConfigConst.CLOUD_GATEWAY_SERVICE) {
			this.userToken = configCloud.getProperty("Cloud", "userToken");
		}
		
		
		
		
		// Paho Java client requires a client ID
		this.clientID = MqttClient.generateClientId();
		
		// these are specific to the MQTT connection which will be used during connect
		this.persistence = new MemoryPersistence();
		this.connOpts    = new MqttConnectOptions();
		
		this.connOpts.setKeepAliveInterval(this.brokerKeepAlive);
		this.connOpts.setCleanSession(this.useCleanSession);
		
		if(configSectionName == ConfigConst.CLOUD_GATEWAY_SERVICE) {
			this.userToken = configCloud.getProperty("Cloud", "userToken");
			this.connOpts.setUserName(this.userToken);
		}
		this.connOpts.setAutomaticReconnect(this.enableAutoReconnect);
		
		// if encryption is enabled, try to load and apply the cert(s)
		if (this.enableEncryption) {
			initSecureConnectionParameters(configSectionName);
		}
		
		// if there's a credential file, try to load and apply them
		if (configUtil.hasProperty(configSectionName, ConfigConst.CRED_FILE_KEY)) {
			initCredentialConnectionParameters(configSectionName);
		}
		
		// NOTE: URL does not have a protocol handler for "tcp" or "ssl",
		// so construct the URL manually
		this.brokerAddr  = this.protocol + "://" + this.host + ":" + this.port;
		
		_Logger.info("Using URL for broker conn: " + this.brokerAddr);
	}
	
	
	/**
	 * Called by {@link #initClientParameters(String)} to load credentials.
	 * 
	 * @param configSectionName The name of the configuration section to use for
	 * the MQTT client configuration parameters.
	 */
	private void initCredentialConnectionParameters(String configSectionName)
	{
		// TODO: implement this
	}
	
	/**
	 * Called by {@link #initClientParameters(String)} to enable encryption.
	 * 
	 * @param configSectionName The name of the configuration section to use for
	 * the MQTT client configuration parameters.
	 */
	private void initSecureConnectionParameters(String configSectionName)
	{
		ConfigUtil configUtil = ConfigUtil.getInstance();
		
		try {
			_Logger.info("Configuring TLS...");
			
			if (this.pemFileName != null) {
				File file = new File(this.pemFileName);
				
				if (file.exists()) {
					_Logger.info("PEM file valid. Using secure connection: " + this.pemFileName);
				} else {
					this.enableEncryption = false;
					
					_Logger.log(Level.WARNING, "PEM file invalid. Using insecure connection: " + pemFileName, new Exception());
					
					return;
				}
			}
			
			SSLSocketFactory sslFactory =
				SimpleCertManagementUtil.getInstance().loadCertificate(this.pemFileName);
			
			this.connOpts.setSocketFactory(sslFactory);
			
			// override current config parameters
			this.port =
				configUtil.getInteger(
					configSectionName, ConfigConst.SECURE_PORT_KEY, ConfigConst.DEFAULT_MQTT_SECURE_PORT);
			
			this.protocol = ConfigConst.DEFAULT_MQTT_SECURE_PROTOCOL;
			
			_Logger.info("TLS enabled.");
		} catch (Exception e) {
			_Logger.log(Level.SEVERE, "Failed to initialize secure MQTT connection. Using insecure connection.", e);
			
			this.enableEncryption = false;
		}
	}
	
	
	private class ActuatorResponseMessageListener implements IMqttMessageListener
	{
		private ResourceNameEnum resource = null;
		private IDataMessageListener dataMsgListener = null;
		
		ActuatorResponseMessageListener(ResourceNameEnum resource, IDataMessageListener dataMsgListener)
		{
			this.resource = resource;
			this.dataMsgListener = dataMsgListener;
		}
		
		@Override
		public void messageArrived(String topic, MqttMessage message) throws Exception
		{
			try {
				ActuatorData actuatorData =
					DataUtil.getInstance().jsonToActuatorData(new String(message.getPayload()));
				
				if (this.dataMsgListener != null) {
					this.dataMsgListener.handleActuatorCommandResponse(resource, actuatorData);
				}
			} catch (Exception e) {
				_Logger.warning("Failed to convert message payload to ActuatorData.");
			}
		}
		
	}
	
	
}

