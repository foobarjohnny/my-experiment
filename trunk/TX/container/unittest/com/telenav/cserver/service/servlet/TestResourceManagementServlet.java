/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.service.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.cserver.common.resource.ResourceFactory;
import com.telenav.cserver.common.resource.ResourceHolder;
import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.common.resource.ServletHTMLDisplayFormat;
import com.telenav.cserver.common.resource.holder.impl.BillingConfHolder;
import com.telenav.cserver.common.resource.message.MessagesHolder;
import com.telenav.cserver.framework.transportation.ServletUtil;
import com.telenav.cserver.framework.trump.TrumpRunnable;
import com.telenav.cserver.matcher.MatchBox;
import com.telenav.cserver.resource.common.prompts.PromptsLoader;

/**
 * TestResourceManagementServlet.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-27
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ResourceManagementServlet.class,ResourceFactory.class,TrumpRunnable.class,PromptsLoader.class,ServletUtil.class,ResourceHolderManager.class})
@SuppressStaticInitializationFor({"com.telenav.cserver.common.resource.ResourceFactory",
	"com.telenav.cserver.common.resource.ResourceHolderManager"})
public class TestResourceManagementServlet extends TestCase{
	private ResourceManagementServlet resourceManagementServlet = new ResourceManagementServlet();
	private HttpServletRequest req = PowerMock.createMock(HttpServletRequest.class);
	private HttpServletResponse res = PowerMock.createMock(HttpServletResponse.class);
	public void testDoGet() throws ServletException, IOException{
		//define variables
		ResourceManagementServlet rms = PowerMock.createPartialMock(ResourceManagementServlet.class, "doPost");
		
		//prepare and replay
		rms.doPost(req, res);
		PowerMock.replayAll();

		//invoke and verify
		rms.doGet(req, res);
		PowerMock.verifyAll();

		//assert
		//no Exception is OK.
	}
	/**
	 *  req.getParameter("action") == "refresh"
	 *  req.getParameter("untrump") == true
	 *  	req.getParameter("fileName") == "fileName"
	 *  action.equalsIgnoreCase("refresh") == true
	 * @throws Exception 
	 */
	public void testDoPost() throws Exception {
		//define variables
		ResourceManagementServlet rms = PowerMock.createPartialMock(ResourceManagementServlet.class, "handleView");
		String action = "refresh";
		
		Map<String, ResourceHolder> map = new HashMap<String, ResourceHolder>();
		map.put("0", new BillingConfHolder());
		map.put("2", new MessagesHolder());
		Collection<ResourceHolder> allHolders = map.values();
		String fileName = "fileName";
		
		TrumpRunnable trumpRunnable = PowerMock.createMock(TrumpRunnable.class);
		PromptsLoader promptsLoader = PowerMock.createMock(PromptsLoader.class);
		
		//prepare and replay
		PowerMock.mockStatic(ResourceFactory.class);
		EasyMock.expect(ResourceFactory.getInstance()).andReturn(null).anyTimes();
		EasyMock.expect( req.getParameter("action")).andReturn(action);
		
		PowerMock.mockStatic(ResourceHolderManager.class);
		EasyMock.expect(ResourceHolderManager.getAllResourceHolder()).andReturn(allHolders);
		
		EasyMock.expect( req.getParameter("untrump")).andReturn("true");
		EasyMock.expect(req.getParameter("fileName")).andReturn(fileName);
		
		PowerMock.mockStatic(TrumpRunnable.class);
		EasyMock.expect(TrumpRunnable.getTrumpRunnable()).andReturn(trumpRunnable);
		trumpRunnable.unzipByName(fileName);
		
		PowerMock.mockStatic(PromptsLoader.class);
		EasyMock.expect(PromptsLoader.getInstance()).andReturn(promptsLoader);
		promptsLoader.refresh();
		
		PowerMock.mockStatic(ServletUtil.class);
		ServletUtil.sendResponse(MatchBox.httpServletResponseEquals(res), 
				EasyMock.aryEq("Refresh OK\r\n<br/>\r\n<br/>".getBytes()));
		
		PowerMock.expectPrivate(rms, "handleView", req,res);
		PowerMock.replayAll();

		//invoke and verify
		rms.doPost(req, res);
		PowerMock.verifyAll();

		//assert

		//no Exception is OK.
	}
	
	/**
	 *  req.getParameter("action") == null
	 *  req.getParameter("untrump") == false
	 *  action.equalsIgnoreCase("refresh") == false
	 * @throws Exception 
	 */
	public void testDoPost1() throws Exception {
		//define variables
		ResourceManagementServlet rms = PowerMock.createPartialMock(ResourceManagementServlet.class, "handleView");
		
		Map<String, ResourceHolder> map = new HashMap<String, ResourceHolder>();
		map.put("0", new BillingConfHolder());
		map.put("2", new MessagesHolder());
		Collection<ResourceHolder> allHolders = map.values();
		//prepare and replay
		PowerMock.mockStatic(ResourceFactory.class);
		EasyMock.expect(ResourceFactory.getInstance()).andReturn(null).anyTimes();
		EasyMock.expect( req.getParameter("action")).andReturn(null);
		
		PowerMock.mockStatic(ResourceHolderManager.class);
		EasyMock.expect(ResourceHolderManager.getAllResourceHolder()).andReturn(allHolders);
		
		EasyMock.expect( req.getParameter("untrump")).andReturn("false");
		
		PowerMock.expectPrivate(rms, "handleView", req,res);
		PowerMock.replayAll();

		//invoke and verify
		rms.doPost(req, res);
		PowerMock.verifyAll();

		//assert

		//no Exception is OK.
	}
	/**
	 *  req.getParameter("action").length() == 0
	 *  req.getParameter("untrump") == false
	 *  action.equalsIgnoreCase("refresh") == false
	 * @throws Exception 
	 */
	public void testDoPost2() throws Exception {
		//define variables
		ResourceManagementServlet rms = PowerMock.createPartialMock(ResourceManagementServlet.class, "handleView");
		Map<String, ResourceHolder> map = new HashMap<String, ResourceHolder>();
		map.put("0", new BillingConfHolder());
		map.put("2", new MessagesHolder());
		Collection<ResourceHolder> allHolders = map.values();
		//prepare and replay
		PowerMock.mockStatic(ResourceFactory.class);
		EasyMock.expect(ResourceFactory.getInstance()).andReturn(null).anyTimes();
		EasyMock.expect( req.getParameter("action")).andReturn("");
		
		PowerMock.mockStatic(ResourceHolderManager.class);
		EasyMock.expect(ResourceHolderManager.getAllResourceHolder()).andReturn(allHolders);
		
		EasyMock.expect( req.getParameter("untrump")).andReturn("false");
		
		PowerMock.expectPrivate(rms, "handleView", req,res);
		PowerMock.replayAll();

		//invoke and verify
		rms.doPost(req, res);
		PowerMock.verifyAll();

		//assert

		//no Exception is OK.
	}
	
	/**
	 *  req.getParameter("action") == ""
	 *  req.getParameter("untrump") == true
	 *  	req.getParameter("fileName") == null
	 *  action.equalsIgnoreCase("refresh") == false
	 * @throws Exception 
	 */
	public void testDoPost3() throws Exception {
		//define variables
		ResourceManagementServlet rms = PowerMock.createPartialMock(ResourceManagementServlet.class, "handleView");
		String action = "";
		
		
		Map<String, ResourceHolder> map = new HashMap<String, ResourceHolder>();
		map.put("0", new BillingConfHolder());
		map.put("2", new MessagesHolder());
		Collection<ResourceHolder> allHolders = map.values();
		
		TrumpRunnable trumpRunnable = PowerMock.createMock(TrumpRunnable.class);
		
		//prepare and replay
		PowerMock.mockStatic(ResourceFactory.class);
		EasyMock.expect(ResourceFactory.getInstance()).andReturn(null).anyTimes();
		EasyMock.expect( req.getParameter("action")).andReturn(action);
		
		PowerMock.mockStatic(ResourceHolderManager.class);
		EasyMock.expect(ResourceHolderManager.getAllResourceHolder()).andReturn(allHolders);
		
		EasyMock.expect( req.getParameter("untrump")).andReturn("true");
		EasyMock.expect(req.getParameter("fileName")).andReturn(null);
		
		PowerMock.mockStatic(TrumpRunnable.class);
		EasyMock.expect(TrumpRunnable.getTrumpRunnable()).andReturn(trumpRunnable);
		trumpRunnable.unzipAll();
		
		PowerMock.expectPrivate(rms, "handleView", req,res);
		PowerMock.replayAll();

		//invoke and verify
		rms.doPost(req, res);
		PowerMock.verifyAll();

		//assert

		//no Exception is OK.
	}
	/**
	 *  req.getParameter("action") == ""
	 *  req.getParameter("untrump") == true
	 *  	req.getParameter("fileName") == ""
	 *  action.equalsIgnoreCase("refresh") == false
	 * @throws Exception 
	 */
	public void testDoPost4() throws Exception {
		//define variables
		ResourceManagementServlet rms = PowerMock.createPartialMock(ResourceManagementServlet.class, "handleView");
		String action = "";
		
		
		Map<String, ResourceHolder> map = new HashMap<String, ResourceHolder>();
		map.put("0", new BillingConfHolder());
		map.put("2", new MessagesHolder());
		Collection<ResourceHolder> allHolders = map.values();
		
		TrumpRunnable trumpRunnable = PowerMock.createMock(TrumpRunnable.class);
		
		//prepare and replay
		PowerMock.mockStatic(ResourceFactory.class);
		EasyMock.expect(ResourceFactory.getInstance()).andReturn(null).anyTimes();
		EasyMock.expect( req.getParameter("action")).andReturn(action);
		
		PowerMock.mockStatic(ResourceHolderManager.class);
		EasyMock.expect(ResourceHolderManager.getAllResourceHolder()).andReturn(allHolders);
		
		EasyMock.expect( req.getParameter("untrump")).andReturn("true");
		EasyMock.expect(req.getParameter("fileName")).andReturn("");
		
		PowerMock.mockStatic(TrumpRunnable.class);
		EasyMock.expect(TrumpRunnable.getTrumpRunnable()).andReturn(trumpRunnable);
		trumpRunnable.unzipAll();
		
		PowerMock.expectPrivate(rms, "handleView", req,res);
		PowerMock.replayAll();

		//invoke and verify
		rms.doPost(req, res);
		PowerMock.verifyAll();

		//assert

		//no Exception is OK.
	}
	/**
	 * req.getParameter(KEY_OPERATION) != "contents"
	 * result != null
	 * @throws Exception 
	 */
	public void testHandleView() throws Exception{
		//define variables
		String operation = "xxx";
		ServletHTMLDisplayFormat display = PowerMock.createMock(ServletHTMLDisplayFormat.class);
		Whitebox.setInternalState(resourceManagementServlet, "display", display);
		String display_details_result = "a returned string.";
		//prepare and replay
		EasyMock.expect( req.getParameter("operation")).andReturn(operation);
		EasyMock.expect(display.details()).andReturn(display_details_result);
		
		PowerMock.mockStatic(ServletUtil.class);
		ServletUtil.sendResponse(MatchBox.httpServletResponseEquals(res), 
				EasyMock.aryEq(display_details_result.getBytes()));
		
		PowerMock.replayAll();

		//invoke and verify
		Whitebox.invokeMethod(resourceManagementServlet, "handleView", req, res);
		PowerMock.verifyAll();

		//assert
		//no Exception is OK.
	}
	
	/**
	 * req.getParameter(KEY_OPERATION) != "contents"
	 * result == null
	 * @throws Exception 
	 */
	public void testHandleView1() throws Exception{
		//define variables
		String operation = "xxx";
		ServletHTMLDisplayFormat display = PowerMock.createMock(ServletHTMLDisplayFormat.class);
		Whitebox.setInternalState(resourceManagementServlet, "display", display);
		String display_details_result = null;
		//prepare and replay
		EasyMock.expect( req.getParameter("operation")).andReturn(operation);
		EasyMock.expect(display.details()).andReturn(display_details_result);
		
		PowerMock.mockStatic(ServletUtil.class);
		ServletUtil.sendResponse(MatchBox.httpServletResponseEquals(res), 
				EasyMock.aryEq("".getBytes()));
		
		PowerMock.replayAll();

		//invoke and verify
		Whitebox.invokeMethod(resourceManagementServlet, "handleView", req, res);
		PowerMock.verifyAll();

		//assert
		//no Exception is OK.
	}
	
	/**
	 * req.getParameter(KEY_OPERATION) == "contents"
	 * result != null
	 * @throws Exception 
	 */
	public void testHandleView2() throws Exception{
		//define variables
		String operation = "contents";
		ServletHTMLDisplayFormat display = PowerMock.createMock(ServletHTMLDisplayFormat.class);
		Whitebox.setInternalState(resourceManagementServlet, "display", display);
		String holderName = "h";
		 String key = "k";
		String display_details_result = "a returned string.";
		//prepare and replay
		EasyMock.expect( req.getParameter("operation")).andReturn(operation);
		EasyMock.expect(req.getParameter("holderName")).andReturn(holderName);
		EasyMock.expect(req.getParameter("key")).andReturn(key);
		EasyMock.expect(display.contents(holderName,key)).andReturn(display_details_result);
		
		PowerMock.mockStatic(ServletUtil.class);
		ServletUtil.sendResponse(MatchBox.httpServletResponseEquals(res), 
				EasyMock.aryEq(display_details_result.getBytes()));
		
		PowerMock.replayAll();

		//invoke and verify
		Whitebox.invokeMethod(resourceManagementServlet, "handleView", req, res);
		PowerMock.verifyAll();

		//assert
		//no Exception is OK.
	}
}
