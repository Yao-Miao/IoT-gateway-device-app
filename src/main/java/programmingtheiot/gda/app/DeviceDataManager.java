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
	
	@Override
	public boolean handleActuatorCommandResponse(ResourceNameEnum resourceName, ActuatorData data)
	{
		_Logger.info("The function handleActuatorCommandResponse is called");
		return false;
	}

	@Override
	public boolean handleIncomingMessage(ResourceNameEnum resourceName, String msg)
	{
		_Logger.info("The function handleIncomingMessage is called");
		return false;
	}

	@Override
	public boolean handleSensorMessage(ResourceNameEnum resourceName, SensorData data)
	{
		_Logger.info("The function handleSensorMessage is called");
		return false;
	}

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
	}
	
	// stop the Device Data Manager
	public void stopManager()
	{
		this.sysPerfManager.stopManager();
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
	}
	
}
