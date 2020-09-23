package programmingtheiot.gda.system;

public class SystemRtMemUtilTask extends BaseSystemUtilTask {
	
	// constructors
	
	/**
	 * Default.
	 * 
	 */
	public SystemRtMemUtilTask()
	{
		super();
	}
	
	
	@Override
	protected float getSystemUtil() {
		
		double valt = Runtime.getRuntime().totalMemory();
		double valf = Runtime.getRuntime().freeMemory();
		double val = (valt - valf) / valt;
		
		return (float)val;
	}

}
