/**
 * This class is part of the Programming the Internet of Things project.
 * 
 * It is provided as a simple shell to guide the student and assist with
 * implementation for the Programming the Internet of Things exercises,
 * and designed to be modified by the student as needed.
 */ 

package programmingtheiot.gda.connection.handlers;

import java.util.HashMap;
import java.util.logging.Logger;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;

import programmingtheiot.common.ConfigConst;
import programmingtheiot.common.ConfigUtil;
import programmingtheiot.common.IDataMessageListener;
import programmingtheiot.common.ResourceNameEnum;


/**
 * Shell representation of class for student implementation.
 *
 */
public class GenericCoapResourceHandler extends CoapResource
{
	// static
	
	private static final Logger _Logger =
		Logger.getLogger(GenericCoapResourceHandler.class.getName());
	
	// params
	private IDataMessageListener dataMsgListener = null;
	
	private HashMap<String, String> map;
	
	// constructors
	
	/**
	 * Constructor.
	 * 
	 * @param resource Basically, the path (or topic)
	 */
	public GenericCoapResourceHandler(ResourceNameEnum resource)
	{
		this(resource.getResourceName());
		map = new HashMap<>();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param resourceName The name of the resource.
	 */
	public GenericCoapResourceHandler(String resourceName)
	{
		super(resourceName);
		map = new HashMap<>();
	}
	
	
	// public methods
	
	@Override
	public void handleDELETE(CoapExchange context)
	{
		
	}
	
	@Override
	public void handleGET(CoapExchange context)
	{
		// TODO: validate 'context'
	    
	    // accept the request
	    context.accept();
	    
	    // TODO: retrieve the requested data and generate a response message: 'msg'
	    String key = this.getURI();
	    String msg = map.containsKey(key) ? map.get(key) : "";
	    // send an appropriate response
	    context.respond(ResponseCode.CONTENT, msg);
	    
	}
	
	@Override
	public void handlePOST(CoapExchange context)
	{
		// TODO: validate 'context'
	    
	    // accept the request
	    context.accept();
	    String key = this.getURI();
	    
	    
	    
	    // TODO: create (or update) the resource with the payload
	    String payload = new String(context.getRequestPayload());
	    if(payload != null) {
	    	map.put(key, payload);
	    }
	    
	    
	    // TODO: generate a response message: 'msg'
	    String msg = "Success"; // fill this in
	    
	    // send an appropriate response
	    context.respond(ResponseCode.CREATED, msg);
	}
	
	@Override
	public void handlePUT(CoapExchange context)
	{
	}
	
	public void setDataMessageListener(IDataMessageListener listener)
	{
	}
	
}
