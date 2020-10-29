/**
 * This class is part of the Programming the Internet of Things project.
 * 
 * It is provided as a simple shell to guide the student and assist with
 * implementation for the Programming the Internet of Things exercises,
 * and designed to be modified by the student as needed.
 */ 

package programmingtheiot.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import programmingtheiot.common.ConfigConst;

/**
 * Convenience wrapper to store system state data, including location
 * information, action command, state data and a list of the following
 * data items:
 * <p>SystemPerformanceData
 * <p>SensorData
 * 
 */
public class SystemStateData extends BaseIotData implements Serializable
{
	// static
	
	public static final int NO_ACTION = 0;
	public static final int REBOOT_SYSTEM_ACTION = 1;
	public static final int GET_SYSTEM_STATE_ACTION = 2;
	
	public static final String DEFAULT_LOCATION = ConfigConst.NOT_SET;
	
	// private var's
	
    private String location = DEFAULT_LOCATION;
    
    private int actionCmd = NO_ACTION;
    
    private List<SystemPerformanceData> sysPerfDataList = null;
    private List<SensorData> sensorDataList = null;
    
    
    
    
	// constructors: initializing the class
	
	public SystemStateData()
	{
		super();
		sysPerfDataList = new ArrayList<>();
		sensorDataList = new ArrayList<>();
	}
	
	
	// public methods
	
	// add SensorData instance into the sensorDataList
	public boolean addSensorData(SensorData data)
	{
		sensorDataList.add(data);
		return true;
	}
	
	// add SystemPerformanceData instance into the sysPerfDataList
	public boolean addSystemPerformanceData(SystemPerformanceData data)
	{
		sysPerfDataList.add(data);
		return true;
	}
	
	// get the actionCmd 
	public int getActionCommand()
	{
		return this.actionCmd;
	}
	
	// get the location 
	public String getLocation()
	{
		return this.location;
	}
	
	// get the sensorDataList 
	public List<SensorData> getSensorDataList()
	{
		return this.sensorDataList;
	}
	
	// get the sysPerfDataList 
	public List<SystemPerformanceData> getSystemPerformanceDataList()
	{
		return this.sysPerfDataList;
	}
	
	// set the actionCmd 
	public void setActionCommand(int actionCmd)
	{
		this.actionCmd = actionCmd;
	}
	
	// set the location 
	public void setLocation(String location)
	{
		this.location = location;
	}
	
	
	// protected methods
	
	/* (non-Javadoc)
	 * @see programmingtheiot.data.BaseIotData#handleToString()
	 */
	protected String handleToString()
	{
		return "";
	}
	
	/* (non-Javadoc)
	 * @see programmingtheiot.data.BaseIotData#handleUpdateData(programmingtheiot.data.BaseIotData)
	 */
	protected void handleUpdateData(BaseIotData data)
	{
		SystemStateData ssd = (SystemStateData) data;
		this.actionCmd = ssd.getActionCommand();
		this.location = ssd.getLocation();
		this.sensorDataList = new ArrayList<SensorData>(ssd.getSensorDataList());
		this.sysPerfDataList = new ArrayList<SystemPerformanceData>(ssd.getSystemPerformanceDataList());
	}
	
}
