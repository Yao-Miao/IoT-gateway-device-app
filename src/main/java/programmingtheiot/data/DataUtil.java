/**
 * This class is part of the Programming the Internet of Things project.
 * 
 * It is provided as a simple shell to guide the student and assist with
 * implementation for the Programming the Internet of Things exercises,
 * and designed to be modified by the student as needed.
 */ 

package programmingtheiot.data;

import java.nio.file.FileSystems;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.google.gson.Gson;

/**
 * Shell representation of class for student implementation.
 *
 */
public class DataUtil
{
	// static
	
	private static final DataUtil _Instance = new DataUtil();

	/**
	 * Returns the Singleton instance of this class.
	 * 
	 * @return ConfigUtil
	 */
	public static final DataUtil getInstance()
	{
		return _Instance;
	}
	
	
	// private var's
	private Gson gson;
	
	// constructors: initializing the class
	
	/**
	 * Default (private).
	 * 
	 */
	private DataUtil()
	{
		super();
		this.gson = new Gson();
	}
	
	
	// public methods
	
	//  Convert ActuatorData to JSON
	public String actuatorDataToJson(ActuatorData actuatorData)
	{
		String jsonData = this.gson.toJson(actuatorData);
		return jsonData;
	}
	
	//  Convert SensorData to JSON
	public String sensorDataToJson(SensorData sensorData)
	{
		String jsonData = this.gson.toJson(sensorData);
		return jsonData;
	}
	
	//  Convert SystemPerformanceData to JSON
	public String systemPerformanceDataToJson(SystemPerformanceData sysPerfData)
	{
		String jsonData = this.gson.toJson(sysPerfData);
		return jsonData;
	}
	
	//  Convert SystemStateData to JSON
	public String systemStateDataToJson(SystemStateData sysStateData)
	{
		String jsonData = this.gson.toJson(sysStateData);
		return jsonData;
	}
	
	//Convert JSON to an ActuatorData instance
	public ActuatorData jsonToActuatorData(String jsonData)
	{
		ActuatorData actuatorData = gson.fromJson(jsonData, ActuatorData.class);
		return actuatorData;
	}
	
	//Convert JSON to an SensorData instance
	public SensorData jsonToSensorData(String jsonData)
	{
		SensorData sensorData = gson.fromJson(jsonData, SensorData.class);
		return sensorData;
	}
	
	//Convert JSON to an SystemPerformanceData instance
	public SystemPerformanceData jsonToSystemPerformanceData(String jsonData)
	{
		SystemPerformanceData systemPerformanceData = gson.fromJson(jsonData, SystemPerformanceData.class);
		return systemPerformanceData;
	}
	
	//Convert JSON to an SystemStateData instance
	public SystemStateData jsonToSystemStateData(String jsonData)
	{
		SystemStateData systemStateData = gson.fromJson(jsonData, SystemStateData.class);
		return systemStateData;
	}
	
}
