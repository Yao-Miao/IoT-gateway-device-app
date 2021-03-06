/**
 * This class is part of the Programming the Internet of Things project.
 * 
 * It is provided as a simple shell to guide the student and assist with
 * implementation for the Programming the Internet of Things exercises,
 * and designed to be modified by the student as needed.
 */ 

package programmingtheiot.gda.connection;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import programmingtheiot.common.ConfigConst;
import programmingtheiot.common.ConfigUtil;
import programmingtheiot.data.ActuatorData;
import programmingtheiot.data.DataUtil;
import programmingtheiot.data.SensorData;
import programmingtheiot.data.SystemPerformanceData;
import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Shell representation of class for student implementation.
 * 
 */
public class RedisPersistenceAdapter implements IPersistenceClient
{
	// static
	
	private static final Logger _Logger =
		Logger.getLogger(RedisPersistenceAdapter.class.getName());
	
	// private var's
	private String host;
	private int port;
	//private Client jedisClient;
	private Jedis jedisClient;
	private DataUtil dataUtil;
	
	// constructors
	
	/**
	 * Default.
	 * 
	 */
	public RedisPersistenceAdapter()
	{
		super();
		ConfigUtil configUtil = ConfigUtil.getInstance();
		this.host = configUtil.getProperty(ConfigConst.DATA_GATEWAY_SERVICE, ConfigConst.HOST_KEY);
		this.port = configUtil.getInteger(ConfigConst.DATA_GATEWAY_SERVICE, ConfigConst.PORT_KEY);
		this.jedisClient = new Jedis(host, port);
		this.dataUtil = DataUtil.getInstance();
		
		initClient();
	}
	
	
	// public methods
	
	@Override
	public boolean connectClient()
	{
		boolean isConnected = jedisClient.isConnected();
		if(isConnected) {
			_Logger.info("The jedis is already connected");
		}else {
			_Logger.info("Connecting.....");
			jedisClient.connect();
			_Logger.info("The jedis is already connected");

		}
		return true;
	}

	@Override
	public boolean disconnectClient()
	{
		boolean isConnected = jedisClient.isConnected();
		if(!isConnected) {
			_Logger.info("The jedis is already disconnected");
		}else {
			_Logger.info("Disconnecting.....");
			jedisClient.disconnect();
			_Logger.info("The jedis is already disconnected");
			
		}
		return true;
	}

	@Override
	public ActuatorData[] getActuatorData(String topic, Date startDate, Date endDate)
	{
		List<ActuatorData> list = new ArrayList<>();
		List<String> adJsons = jedisClient.lrange(topic, 0, -1);
		for(String adJson : adJsons) {
			ActuatorData ad = dataUtil.jsonToActuatorData(adJson);
			list.add(ad);
		}
		
		ActuatorData[] ActuatorDataArr = new ActuatorData[list.size()];
		ActuatorDataArr = list.toArray(ActuatorDataArr);
		return ActuatorDataArr;
	}

	@Override
	public SensorData[] getSensorData(String topic, Date startDate, Date endDate)
	{
		List<SensorData> list = new ArrayList<>();
		List<String> sdJsons = jedisClient.lrange(topic, 0, 10);
		for(String sdJson : sdJsons) {
			SensorData sd = dataUtil.jsonToSensorData(sdJson);
			list.add(sd);
		}
		SensorData[] SensorDataArr = new SensorData[list.size()];
		SensorDataArr = list.toArray(SensorDataArr);
		return SensorDataArr;
	}

	@Override
	public void registerDataStorageListener(Class cType, IPersistenceListener listener, String... topics)
	{
	}

	@Override
	public boolean storeData(String topic, int qos, ActuatorData... data)
	{
		for(ActuatorData ad : data) {
			String adJson = this.dataUtil.actuatorDataToJson(ad);
			this.jedisClient.lpush(topic, adJson);
		}
		return true;
	}

	@Override
	public boolean storeData(String topic, int qos, SensorData... data)
	{
		for(SensorData sd : data) {
			String sdJson = this.dataUtil.sensorDataToJson(sd);
			this.jedisClient.lpush(topic, sdJson);
		}
		return true;
	}

	/**
	 *
	 */
	@Override
	public boolean storeData(String topic, int qos, SystemPerformanceData... data)
	{
		for(SystemPerformanceData spd : data) {
			String spdJson = this.dataUtil.systemPerformanceDataToJson(spd);
			this.jedisClient.lpush(topic, spdJson);
		}
		return true;
	}
	
	public long getListLen(String topic) {
		return this.jedisClient.llen(topic);
	}
	
	
	// private methods
	
	/**
	 * Generates a listener key map from the class type and topic.
	 * The format will be as follows:
	 * <br>'simple class name' + "_" + 'topic name'
	 * <br>e.g. ActuatorData_localhost/fan
	 * <br>e.g. SensorData_localhost/temperature
	 * <p>
	 * If the class type is null, it will simply be dropped and
	 * only the topic name will be used in the key. If the topic
	 * name is also null or invalid (e.g. empty), the 'all' keyword
	 * will be used instead.
	 * 
	 * @param cType The class type to use in the key.
	 * @param topic The topic name to use in the key.
	 * @return String The key derived from cType and topic, as per above.
	 */
	private String getListenerMapKey(Class cType, String topic)
	{
		StringBuilder buf = new StringBuilder();
		
		if (cType != null) {
			buf.append(cType.getSimpleName()).append("_");
		}
		
		if (topic != null && topic.trim().length() > 0) {
			buf.append(topic.trim());
		} else {
			buf.append("all");
		}
		
		String key = buf.toString();
		
		_Logger.info("Generated listener map lookup key from '" + cType + "' and '" + topic + "': " + key);
		
		return key;
	}
	
	private void initClient()
	{
	}
	
	private Long updateRedisDataElement(String topic, double score, String payload)
	{
		return 0L;
	}


}
