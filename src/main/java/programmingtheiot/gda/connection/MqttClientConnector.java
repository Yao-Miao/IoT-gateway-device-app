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

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import programmingtheiot.common.ConfigConst;
import programmingtheiot.common.ConfigUtil;
import programmingtheiot.common.IDataMessageListener;
import programmingtheiot.common.ResourceNameEnum;

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
	
	// constructors
	
	/**
	 * 
	 * Default constructor. This will set remote broker information and client connection information 
	 * based on the default configuration file contents.
	 * 
	 */
	public MqttClientConnector()
	{
		super();
		ConfigUtil configUtil = ConfigUtil.getInstance();

		this.host =
		    configUtil.getProperty(
		        ConfigConst.MQTT_GATEWAY_SERVICE, ConfigConst.HOST_KEY, ConfigConst.DEFAULT_HOST);

		this.port =
		    configUtil.getInteger(
		        ConfigConst.MQTT_GATEWAY_SERVICE, ConfigConst.PORT_KEY, ConfigConst.DEFAULT_MQTT_PORT);

		this.brokerKeepAlive =
		    configUtil.getInteger(
		        ConfigConst.MQTT_GATEWAY_SERVICE, ConfigConst.KEEP_ALIVE_KEY, ConfigConst.DEFAULT_KEEP_ALIVE);

		// paho Java client requires a client ID
		this.clientID = MqttClient.generateClientId();

		// these are specific to the MQTT connection which will be used during connect
		this.persistence = new MemoryPersistence();
		this.connOpts = new MqttConnectOptions();

		this.connOpts.setKeepAliveInterval(this.brokerKeepAlive);
		this.connOpts.setCleanSession(false);
		this.connOpts.setAutomaticReconnect(true);

		// NOTE: URL does not have a protocol handler for "tcp",
		// so we need to construct the URL manually
		this.brokerAddr = this.protocol + "://" + this.host + ":" + this.port;
		
		
	}
	
	
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
		return false;
	}
	
	@Override
	/**
	 * Publishes a message to a topic on the server
	 * @param topicName to deliver the message to
	 * @param msg the actual message to send
	 * @param qos the quality of service level to use.
	 */
	public boolean publishMessage(ResourceNameEnum topicName, String msg, int qos)
	{	_Logger.info("The function publishMessage is called");
		String topic = topicName.getResourceName();
		byte[] payload = msg.getBytes();
		
		if (qos < 0 || qos > 2) {
			qos = ConfigConst.DEFAULT_QOS;
		}
					
		try {
			this.mqttClient.publish(topic, payload, qos, true);
		} catch (MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	/**
	 * Subscribe to a topic
	 * @param topicName the topic to subscribe to
	 * @param qos the quality of service level to use.
	 */
	public boolean subscribeToTopic(ResourceNameEnum topicName, int qos)
	{
		_Logger.info("The function subscribeToTopic is called");
		String topic = topicName.getResourceName();
		
		if (qos < 0 || qos > 2) {
			qos = ConfigConst.DEFAULT_QOS;
		}
					
		try {
			this.mqttClient.subscribe(topic, qos);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	/**
	 * Requests the server unsubscribe the client from a topic.
	 * @param topicName the topic to unsubscribe from
	 */
	public boolean unsubscribeFromTopic(ResourceNameEnum topicName)
	{
		_Logger.info("The function unsubscribeFromTopic is called");
		String topic = topicName.getResourceName();
		
		try {
			this.mqttClient.unsubscribe(topic);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	
	public boolean setDataMessageListener(IDataMessageListener listener)
	{
		_Logger.info("The function setDataMessageListener is called");
		return false;
	}
	
	// callbacks
	
	@Override
	/**
	 *  callback methods to handle connection notification events
	 */
	public void connectComplete(boolean reconnect, String serverURI)
	{
		_Logger.info("The function connectComplete is called");
	}

	@Override
	/**
	 *  callback methods to handle connection notification events
	 */
	public void connectionLost(Throwable t)
	{
		_Logger.info("The function connectionLost is called");
	}
	
	@Override
	/**
	 *  callback method to handle publish notification events
	 */
	public void deliveryComplete(IMqttDeliveryToken token)
	{
		_Logger.info("The function deliveryComplete is called");
	}
	
	@Override
	/**
	 * callback method - this will be called whenever a message is received on the topic for which your client has subscribed
	 */
	public void messageArrived(String topic, MqttMessage msg) throws Exception
	{
		_Logger.info("The function messageArrived is called");
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
		// TODO: implement this
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
		// TODO: implement this
	}
}
