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
	
	
	// constructors
	
	public BaseSystemUtilTask()
	{
		super();
	}
	
	
	// public methods
	
	public SensorData generateTelemetry()
	{
		return null;
	}
	
	public float getTelemetryValue()
	{	
		float val = getSystemUtil();
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
