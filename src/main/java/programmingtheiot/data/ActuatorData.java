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
 * The class that restores the Actuator Data
 */
public class ActuatorData extends BaseIotData implements Serializable
{
	// static
	
	public static final int DEFAULT_ACTUATOR_TYPE = 0;
	public static final int DEFAULT_COMMAND = 0;
	public static final int COMMAND_OFF = DEFAULT_COMMAND;
	public static final int COMMAND_ON = 1;
	
	
	// private var's
	
	private float value;
	private int command;
	private int actuatorType = DEFAULT_ACTUATOR_TYPE;
    
	
	// constructors: initializing the class
	/**
	 * Default.
	 * 
	 */
	public ActuatorData()
	{
		super(); 
	}
	
	public ActuatorData(int actuatorType)
	{
		super();
		this.actuatorType = actuatorType;
	}
	
	
	// public methods
	
	//get the command variable
	public int getCommand()
	{
		return this.command;
	}
	
	//get the value variable
	public float getValue()
	{
		return this.value;
	}
	
	//get the actuatorType variable
	public int getActuatorType() 
	{
		return this.actuatorType;
	}
	
	//set the command variable
	public void setCommand(int command)
	{
		this.command = command;
	}
	
	//set the value variable
	public void setValue(float val)
	{
		this.value = val;
	}
	public void setActuatorType(int actuatorType) {
		this.actuatorType = actuatorType;
	}
	
	
	// protected methods
	
	/* (non-Javadoc)
	 * @see programmingtheiot.data.BaseIotData#handleUpdateData(programmingtheiot.data.BaseIotData)
	 */
	// handle the update action
	protected void handleUpdateData(BaseIotData data)
	{
		ActuatorData ad = (ActuatorData) data;
		this.command = ad.getCommand();
		this.value = ad.getValue();
		this.actuatorType = ad.getActuatorType();
	}
	
}
