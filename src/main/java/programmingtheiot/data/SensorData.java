/**
 * This class is part of the Programming the Internet of Things project.
 * 
 * It is provided as a simple shell to guide the student and assist with
 * implementation for the Programming the Internet of Things exercises,
 * and designed to be modified by the student as needed.
 */ 

package programmingtheiot.data;

import java.io.Serializable;

/**
 * Shell representation of class for student implementation.
 * The class that restores the Sensor Data
 */
public class SensorData extends BaseIotData implements Serializable
{
	// static
	
	public static final int DEFAULT_SENSOR_TYPE = 0;
	

	// private var's
	
	private float value;
	private int sensorType = DEFAULT_SENSOR_TYPE;
    
	// constructors: initializing the class
	
	public SensorData()
	{
		super();
		
	}
	
	public SensorData(int sensorType)
	{
		super();
		this.sensorType = sensorType;
	}
	
	
	// public methods
	
	//get the value variable
	public float getValue()
	{
		return this.value;
	}
	
	//get the sensorType variable
	public int getSensorType() 
	{
		return this.sensorType;
	}
	
	//set the value variable
	public void setValue(float val)
	{
		this.value = val;
	}
	
	
	// protected methods
	
	/* (non-Javadoc)
	 * @see programmingtheiot.data.BaseIotData#handleUpdateData(programmingtheiot.data.BaseIotData)
	 */
	// handle the update action
	protected void handleUpdateData(BaseIotData data)
	{	
		SensorData sd = (SensorData)data;
		this.value = sd.getValue();
		this.sensorType = sd.getSensorType();
	}
	
}
