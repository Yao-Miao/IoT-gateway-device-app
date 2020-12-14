/**
 * 
 * This class is part of the Programming the Internet of Things
 * project, and is available via the MIT License, which can be
 * found in the LICENSE file at the top level of this repository.
 * 
 * Copyright (c) 2020 by Andrew D. King
 */ 

package programmingtheiot.part03.integration.connection;

import static org.junit.Assert.*;

import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import programmingtheiot.common.ConfigConst;
import programmingtheiot.common.ConfigUtil;
import programmingtheiot.common.DefaultDataMessageListener;
import programmingtheiot.common.IDataMessageListener;
import programmingtheiot.common.ResourceNameEnum;
import programmingtheiot.data.DataUtil;
import programmingtheiot.data.SystemStateData;
import programmingtheiot.gda.connection.*;

/**
 * This test case class contains very basic integration tests for
 * CoapClientConnector. It should not be considered complete,
 * but serve as a starting point for the student implementing
 * additional functionality within their Programming the IoT
 * environment.
 * 
 * NOTE: The CoAP server must be running before executing these tests.
 */
public class CoapClientConnectorTest
{
	// static
	
	public static final int DEFAULT_TIMEOUT = 5;
	public static final boolean USE_DEFAULT_RESOURCES = true;
	
	private static final Logger _Logger =
		Logger.getLogger(CoapClientConnectorTest.class.getName());
	
	// member var's
	
	private CoapClientConnector coapClient = null;
	private IDataMessageListener dataMsgListener = null;
	
	
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
		this.coapClient = new CoapClientConnector();
		this.dataMsgListener = new DefaultDataMessageListener();
		
		this.coapClient.setDataMessageListener(this.dataMsgListener);
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}
	
	// test methods
	
	/***************************************************************
	 * test Connect And Discover :add by miaoyao
	 ***************************************************************/
	
	//@Test
	public void testConnectAndDiscover()
	{
		assertTrue(this.coapClient.sendDiscoveryRequest(DEFAULT_TIMEOUT));

		// NOTE: If you are using a custom asynchronous discovery, include a brief wait here
		try {
			Thread.sleep(2000L);
			System.out.println("--------->testConnectAndDiscover");
		} catch (InterruptedException e) {
			// ignore
		}
	}
	
	
	/***************************************************************
	 * test Get with CON:add by miaoyao
	 ***************************************************************/
	
	//@Test
	public void testGetRequestCon()
	{
		assertTrue(this.coapClient.sendGetRequest(ResourceNameEnum.GDA_MGMT_STATUS_MSG_RESOURCE, true, DEFAULT_TIMEOUT));

		// NOTE: If you are using a custom asynchronous discovery, include a brief wait here
		try {
			Thread.sleep(2000L);
			System.out.println("--------->testGetRequestCon");
		} catch (InterruptedException e) {
			// ignore
		}
	}
	
	/***************************************************************
	 * test Get with NON:add by miaoyao
	 ***************************************************************/
	@Test
	public void testGetRequestNon()
	{
		assertTrue(this.coapClient.sendGetRequest(ResourceNameEnum.GDA_MGMT_STATUS_MSG_RESOURCE, false, DEFAULT_TIMEOUT));
		
		// NOTE: If you are using a custom asynchronous discovery, include a brief wait here
		try {
			Thread.sleep(2000L);
			System.out.println("--------->testGetRequestNon");
		} catch (InterruptedException e) {
			// ignore
		}
	}
	

	
	/***************************************************************
	 * test Post with CON:add by miaoyao
	 ***************************************************************/

	//@Test
	public void testPostRequestCon()
	{
		int actionCmd = 2;
		
		SystemStateData ssd = new SystemStateData();
		ssd.setActionCommand(actionCmd);
		
		String ssdJson = DataUtil.getInstance().systemStateDataToJson(ssd);
		assertTrue(this.coapClient.sendPostRequest(ResourceNameEnum.GDA_MGMT_STATUS_MSG_RESOURCE, true, ssdJson, DEFAULT_TIMEOUT));
		
		
		// NOTE: If you are using a custom asynchronous discovery, include a brief wait here
		try {
			Thread.sleep(2000L);
			System.out.println("--------->testPostRequestCon");
		} catch (InterruptedException e) {
			// ignore
		}
	}
	/***************************************************************
	 * test Post with NON:add by miaoyao
	 ***************************************************************/	
	//@Test
	public void testPostRequestNon()
	{
		int actionCmd = 2;
		
		SystemStateData ssd = new SystemStateData();
		ssd.setActionCommand(actionCmd);
		
		String ssdJson = DataUtil.getInstance().systemStateDataToJson(ssd);
		assertTrue(this.coapClient.sendPostRequest(ResourceNameEnum.GDA_MGMT_STATUS_MSG_RESOURCE, false, ssdJson, DEFAULT_TIMEOUT));

		// NOTE: If you are using a custom asynchronous discovery, include a brief wait here
		try {
			Thread.sleep(2000L);
			System.out.println("--------->testPostRequestNon");
		} catch (InterruptedException e) {
			// ignore
		}
		
	}
	
	
	/***************************************************************
	 * test Put WITH CON:add by miaoyao
	 ***************************************************************/
	//@Test
	public void testPutRequestCon()
	{
		int actionCmd = 2;
		
		SystemStateData ssd = new SystemStateData();
		ssd.setActionCommand(actionCmd);
		
		String ssdJson = DataUtil.getInstance().systemStateDataToJson(ssd);
		System.out.println(ssdJson);
		assertTrue(this.coapClient.sendPutRequest(ResourceNameEnum.GDA_MGMT_STATUS_MSG_RESOURCE, true, ssdJson, DEFAULT_TIMEOUT));

		// NOTE: If you are using a custom asynchronous discovery, include a brief wait here
		try {
			Thread.sleep(2000L);
			System.out.println("--------->testPutRequestCon");
		} catch (InterruptedException e) {
			// ignore
		}
	}
	/***************************************************************
	 * test Put WITH NON:add by miaoyao
	 ***************************************************************/	
	@Test
	public void testPutRequestNon()
	{
		int actionCmd = 2;
		
		SystemStateData ssd = new SystemStateData();
		ssd.setActionCommand(actionCmd);
		
		String ssdJson = DataUtil.getInstance().systemStateDataToJson(ssd);
		System.out.print(ssdJson);
		assertTrue(this.coapClient.sendPutRequest(ResourceNameEnum.GDA_MGMT_STATUS_MSG_RESOURCE, false, ssdJson, DEFAULT_TIMEOUT));
		
		// NOTE: If you are using a custom asynchronous discovery, include a brief wait here
		try {
			Thread.sleep(2000L);
			System.out.println("--------->testPutRequestNon");
		} catch (InterruptedException e) {
			// ignore
		}
	}
	
	/***************************************************************
	 * test Delete WITH CON:add by miaoyao
	 ***************************************************************/

	//@Test
	public void testDeleteRequestCon()
	{
		assertTrue(this.coapClient.sendDeleteRequest(ResourceNameEnum.GDA_MGMT_STATUS_MSG_RESOURCE, true, DEFAULT_TIMEOUT));
		
		// NOTE: If you are using a custom asynchronous discovery, include a brief wait here
		try {
			Thread.sleep(2000L);
			System.out.println("--------->testDeleteRequestNon");
		} catch (InterruptedException e) {
			// ignore
		}

	}
	/***************************************************************
	 * test Delete WITH NON:add by miaoyao
	 ***************************************************************/
	//@Test
	public void testDeleteRequestNon()
	{
		assertTrue(this.coapClient.sendDeleteRequest(ResourceNameEnum.GDA_MGMT_STATUS_MSG_RESOURCE, false, DEFAULT_TIMEOUT));
		
		// NOTE: If you are using a custom asynchronous discovery, include a brief wait here
		try {
			Thread.sleep(2000L);
			System.out.println("--------->testDeleteRequestNon");
		} catch (InterruptedException e) {
			// ignore
		}
	}
	
}

