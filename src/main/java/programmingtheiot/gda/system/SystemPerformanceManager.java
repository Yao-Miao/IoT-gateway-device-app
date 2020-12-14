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
import programmingtheiot.common.ResourceNameEnum;
import programmingtheiot.data.SystemPerformanceData;
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
	//private SystemRtMemUtilTask rtMemUtilTask = null;  -- delete by yao miao. this syntax is used to get runtime memory utilization

	private Runnable taskRunner = null;
	private boolean isStarted = false;
	private IDataMessageListener dataMsgListener = null;
	
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
		//this.rtMemUtilTask = new SystemRtMemUtilTask(); -- delete by yao miao. this syntax is to get runtime memory utilization
		

		this.taskRunner = () -> {
		    this.handleTelemetry();
		};
	}
	
	
	// public methods
	
	//Call getTelemetryValue() method to get the CPU utilization and memory utilization. Logs the values of self.cpuUtilPct and self.memUtilPct
	
	public void handleTelemetry()
	{
		float cpuUtilPct = this.cpuUtilTask.getTelemetryValue();
		float memUtilPct = this.memUtilTask.getTelemetryValue();
		//float rtMemUtilPct = this.rtMemUtilTask.getTelemetryValue(); -- delete by yao miao. this syntax is to get runtime memory utilization
		
		SystemPerformanceData spd = new SystemPerformanceData();
		spd.setCpuUtilization(cpuUtilPct);
		spd.setMemoryUtilization(memUtilPct);
		
		if(this.dataMsgListener != null) {
			this.dataMsgListener.handleSystemPerformanceMessage(ResourceNameEnum.GDA_SYSTEM_PERF_MSG_RESOURCE, spd);
		}
		
		_Logger.info("CPU utilization is " + cpuUtilPct +  " percent, and memory utilization is " + memUtilPct + " percent.");
		//_Logger.info("Handle telemetry results: cpuUtil=" + cpuUtilPct + ", memUtil=" + memUtilPct + "%");
	}
	
	public void setDataMessageListener(IDataMessageListener listener)
	{
		this.dataMsgListener = listener;
	}
	
	//Start the System Performance Manager. Start ScheduledExecutorService. Switch isStarted to true.
	public void startManager()
	{
		_Logger.info("SystemPerformanceManager is starting");
		if (! this.isStarted) {
		    ScheduledFuture<?> futureTask = this.schedExecSvc.scheduleAtFixedRate(this.taskRunner, 0L, this.pollSecs, TimeUnit.SECONDS);

		    this.isStarted = true;
		}
	}
	//Start the System Performance Manager. Shutdown ScheduledExecutorService.
	public void stopManager()
	{
		this.schedExecSvc.shutdown();
		_Logger.info("SystemPerformanceManager is stopping");
	}
	
}
