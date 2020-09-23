/**
 * This class is part of the Programming the Internet of Things project.
 * 
 * It is provided as a simple shell to guide the student and assist with
 * implementation for the Programming the Internet of Things exercises,
 * and designed to be modified by the student as needed.
 */ 

package programmingtheiot.gda.system;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import programmingtheiot.common.ConfigConst;
import programmingtheiot.common.IDataMessageListener;
import programmingtheiot.gda.app.GatewayDeviceApp;

/**
 * Shell representation of class for student implementation.
 * 
 */
public class SystemPerformanceManager
{
	//static
	private static final Logger _Logger = Logger.getLogger(SystemPerformanceManager.class.getName());
	
	// private var's
	private int pollSecs = 30;
	private ScheduledExecutorService schedExecSvc = null;
	private SystemCpuUtilTask cpuUtilTask = null;
	private SystemMemUtilTask memUtilTask = null;

	private Runnable taskRunner = null;
	private boolean isStarted = false;
	
	
	// constructors
	
	/**
	 * Default.
	 * 
	 */
	public SystemPerformanceManager()
	{	
		this(ConfigConst.DEFAULT_POLL_CYCLES);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param pollSecs The number of seconds between each scheduled task poll.
	 */
	public SystemPerformanceManager(int pollSecs)
	{	
		if(pollSecs > 1 && pollSecs < Integer.MAX_VALUE) {
			this.pollSecs = pollSecs;
		}
		this.schedExecSvc = Executors.newScheduledThreadPool(1);
		this.cpuUtilTask = new SystemCpuUtilTask();
		this.memUtilTask = new SystemMemUtilTask();

		this.taskRunner = () -> {
		    this.handleTelemetry();
		};
	}
	
	
	// public methods
	
	public void handleTelemetry()
	{
		float cpuUtilPct = this.cpuUtilTask.getTelemetryValue();
		float memUtilPct = this.memUtilTask.getTelemetryValue();
		
		//_Logger.info("CPU utilization is " + cpuUtilPct +  " percent, and memory utilization is " + memUtilPct + " percent.");
		_Logger.info("Handle telemetry results: cpuUtil=" + cpuUtilPct + ", memUtil=" + memUtilPct);
	}
	
	public void setDataMessageListener(IDataMessageListener listener)
	{
	}
	
	public void startManager()
	{
		_Logger.info("SystemPerformanceManager is starting");
		if (! this.isStarted) {
		    ScheduledFuture<?> futureTask = this.schedExecSvc.scheduleAtFixedRate(this.taskRunner, 0L, this.pollSecs, TimeUnit.SECONDS);

		    this.isStarted = true;
		}
	}
	
	public void stopManager()
	{
		this.schedExecSvc.shutdown();
		_Logger.info("SystemPerformanceManager is stopping");
	}
	
}
