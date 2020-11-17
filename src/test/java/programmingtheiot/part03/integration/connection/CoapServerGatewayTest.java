package programmingtheiot.part03.integration.connection;

import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.WebLink;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import programmingtheiot.common.DefaultDataMessageListener;
import programmingtheiot.common.IDataMessageListener;
import programmingtheiot.gda.connection.CoapServerGateway;
import programmingtheiot.gda.connection.SmtpClientConnector;

public class CoapServerGatewayTest {
	// static
	
	public static final int DEFAULT_TIMEOUT = 120000;
		
	private static final Logger _Logger =
	Logger.getLogger(CoapServerGatewayTest.class.getName());
		
	// member var's
		
	private CoapServerGateway csg = null;
		
		
	// test setup methods
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		
	}
	

	@Test
	public void testRunSimpleCoapServerGatewayIntegration()
	{
		try {
			String url = "coap://localhost:5683";
			
			this.csg = new CoapServerGateway(); // assumes the no-arg constructor will create all resources internally
			this.csg.startServer();
			
			CoapClient clientConn = new CoapClient(url);
			
			Set<WebLink> wlSet = clientConn.discover();
				
			if (wlSet != null) {
				for (WebLink wl : wlSet) {
					_Logger.info(" --> WebLink: " + wl.getURI() + ". Attributes: " + wl.getAttributes());
				}
			}
			
			Thread.sleep(DEFAULT_TIMEOUT); // DEFAULT_TIMEOUT is in milliseconds - for instance, 120000 (2 minutes)
			
			this.csg.stopServer();
		} catch (Exception e) {
			// log a message!
		}
	}
}
