package programmingtheiot.gda.connection.handlers;


import java.util.logging.Logger;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Response;

import programmingtheiot.common.IDataMessageListener;

public class GenericCoapResponseHandler implements CoapHandler{
	
	// static

	private static final Logger _Logger =
		Logger.getLogger(GenericCoapResponseHandler.class.getName());
		
	// params
	private IDataMessageListener dataMsgListener = null;
	
	// constructors
	/*public GenericCoapResponseHandler(IDataMessageListener dataMsgListener)
	{
		this.dataMsgListener = dataMsgListener;
	}*/
	/**
	 * Default.
	 * 
	 */
	public GenericCoapResponseHandler()
	{
		this((IDataMessageListener) null);
	}
	
	/**
	 * Constructor.
	 * 
	 */
	public GenericCoapResponseHandler(IDataMessageListener listener)
	{
		super();
		
		dataMsgListener = listener;
		
		_Logger.fine("Response handler created. IDataMessageListener is " + (listener != null ? "set" : "not set"));
	}
	
	
	// public methods
	/**
	 * Invoked when a CoAP response or notification has arrived.
	 *
	 * @param response the response
	 */
	@Override
	public void onLoad(CoapResponse response)
	{
		if (response != null) {
			OptionSet options = response.getOptions();
			
			_Logger.info("Processing CoAP response. Options: " + options);
			_Logger.info("Processing CoAP response. MID: " + response.advanced().getMID());
			_Logger.info("Processing CoAP response. Token: " + response.advanced().getTokenString());
			_Logger.info("Processing CoAP response. Code: " + response.getCode());
			
			// TODO: parse payload and notify listener
			_Logger.info(" --> Payload: " + response.getResponseText());
			
			if (this.dataMsgListener != null) {
				// TODO: send listener the response
			}
		} else {
			_Logger.warning("No CoAP response to process. Response is null.");
		}
	}
	
	/**
	 * Invoked when a request timeouts or has been rejected by the server.
	 */
	@Override
	public void onError()
	{
		_Logger.warning("Error processing CoAP response. Ignoring.");
	}
	
}
