/**
 * This class is part of the Programming the Internet of Things project.
 * 
 * It is provided as a simple shell to guide the student and assist with
 * implementation for the Programming the Internet of Things exercises,
 * and designed to be modified by the student as needed.
 */ 

package programmingtheiot.data;

import java.io.Serializable;
import programmingtheiot.common.ConfigConst;

/**
 * Shell representation of class for student implementation.
 * The class that restores the System Performance Data
 */
public class SystemPerformanceData extends BaseIotData implements Serializable
{
	// static
	
	
	// private var's
	private float cpuUtil;
	private float diskUtil;
	private float memUtil;
		
    
	// constructors: initializing the class
	
	public SystemPerformanceData()
	{
		super();
		
		super.setName(ConfigConst.SYS_PERF_DATA);
	}
	
	
	// public methods
	
	//get the cpuUtil: CPU utilization
	public float getCpuUtilization()
	{
		return this.cpuUtil;
	}
	
	//get the diskUtil: Disk utilization
	public float getDiskUtilization()
	{
		return this.diskUtil;
	}
	
	//get the memoryUtil: Memory utilization
	public float getMemoryUtilization()
	{
		return this.memUtil;
	}
	
	//set the cpuUtil: CPU utilization
	public void setCpuUtilization(float val)
	{
		this.cpuUtil = val;
	}
	
	//set the diskUtil: Disk utilization
	public void setDiskUtilization(float val)
	{
		this.diskUtil = val;
	}
	
	//set the memoryUtil: Memory utilization
	public void setMemoryUtilization(float val)
	{
		this.memUtil = val;
	}
	
	
	// protected methods
	
	/* (non-Javadoc)
	 * @see programmingtheiot.data.BaseIotData#handleToString()
	 */
	protected String handleToString()
	{
		return null;
	}
	
	/* (non-Javadoc)
	 * @see programmingtheiot.data.BaseIotData#handleUpdateData(programmingtheiot.data.BaseIotData)
	 */
	protected void handleUpdateData(BaseIotData data)
	{
		SystemPerformanceData sysd = (SystemPerformanceData) data;
		this.cpuUtil = sysd.getCpuUtilization();
		this.diskUtil = sysd.getDiskUtilization();
		this.memUtil= sysd.getMemoryUtilization();
	}
	
}
