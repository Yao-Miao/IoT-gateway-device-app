/**
 * This class is part of the Programming the Internet of Things project.
 * 
 * It is provided as a simple shell to guide the student and assist with
 * implementation for the Programming the Internet of Things exercises,
 * and designed to be modified by the student as needed.
 */ 

package programmingtheiot.gda.system;

import java.util.logging.Logger;

import programmingtheiot.data.SensorData;

/**
 *
 */
public abstract class BaseSystemUtilTask
{
	// static
	
	private static final Logger _Logger =
		Logger.getLogger(BaseSystemUtilTask.class.getName());
	
	
	// private
	private SensorData latestSensorData = null;
	
	// constructors: initializing the class
	
	public BaseSystemUtilTask()
	{
		super();
		
		
	}
	
	
	// public methods
	
	/**
	 * Generate The Telemetry
	 * @return SensorData
	 */
	public SensorData generateTelemetry()
	{
		this.latestSensorData = new SensorData();
		float val = getSystemUtil();
		this.latestSensorData.setValue(val);
		return this.latestSensorData;
	}
	
	/**
	 * Get The Telemetry Value
	 * @return float
	 */
	public float getTelemetryValue()
	{	
		if(this.latestSensorData == null) {
			this.latestSensorData = this.generateTelemetry();
		}
		float val = this.latestSensorData.getValue();
		//_Logger.info("The method <getSystemUtil> get a value: " + val);
		return val;
	}
	
	
	// protected methods
	
	/**
	 * Template method definition. Sub-class will implement this to retrieve
	 * the system utilization measure.
	 * 
	 * @return float
	 */
	protected abstract float getSystemUtil();
	
}
