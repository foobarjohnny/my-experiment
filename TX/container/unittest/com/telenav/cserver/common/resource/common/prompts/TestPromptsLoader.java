/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource.common.prompts;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.cserver.resource.common.prompts.BinaryHolder;
import com.telenav.cserver.resource.common.prompts.PromptConfKeys;
import com.telenav.cserver.resource.common.prompts.PromptsLoader;
import com.telenav.cserver.unittestutil.UTConstant;

/**
 * TestPromptsLoader.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-26
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({PromptsLoader.class})
public class TestPromptsLoader extends TestCase{
	private PromptsLoader promptsLoader = PromptsLoader.getInstance();
	
	public void testGetIntValue(){
		assertEquals(12, promptsLoader.getIntValue(PromptConfKeys.CAR_ICON_PROMPT_SERVER));
		assertEquals(0, promptsLoader.getIntValue(PromptConfKeys.MAX_CONNECTION));
	}
	
	public void testGetHttpClient_client_notNull() throws Exception{
		//define variables
		HttpClient client = PowerMock.createMock(HttpClient.class);
		Whitebox.setInternalState(PromptsLoader.class, "client", client);
		//prepare and replay
		
		PowerMock.replayAll();
		
		//invoke and verify
		HttpClient result = Whitebox.invokeMethod(promptsLoader, "getHttpClient");
		PowerMock.verifyAll();

		//assert
		assertEquals("the two object should be same.",client, result);
		Whitebox.setInternalState(PromptsLoader.class, "client", UTConstant.nullObj);
	}
	public void testGetHttpClient_client_ISNull() throws Exception{
		//define variables
		PromptsLoader promptsLoader = PowerMock.createPartialMock(PromptsLoader.class, "getIntValue");
		Whitebox.setInternalState(PromptsLoader.class, "client", UTConstant.nullObj);
		//prepare and replay
		PowerMock.expectPrivate(promptsLoader, "getIntValue", PromptConfKeys.MAX_CONNECTION).andReturn(11);
		PowerMock.expectPrivate(promptsLoader, "getIntValue", PromptConfKeys.CONNECTION_TIMEOUT).andReturn(22);
		PowerMock.expectPrivate(promptsLoader, "getIntValue", PromptConfKeys.SO_TIMEOUT).andReturn(33);
		PowerMock.replayAll();
		
		//invoke and verify
		HttpClient result = Whitebox.invokeMethod(promptsLoader, "getHttpClient");
		PowerMock.verifyAll();

		//assert
		assertEquals(11, result.getHttpConnectionManager().getParams().getDefaultMaxConnectionsPerHost());
		assertEquals(22, result.getHttpConnectionManager().getParams().getConnectionTimeout());
		assertEquals(33, result.getHttpConnectionManager().getParams().getSoTimeout());
	}
	/**
	 * promptsCache.containsKey(fileName) == false
	 * @throws Exception
	 */
	public void testRetrieveBinaryDataVia() throws Exception {
		//define variables
		PromptsLoader promptsLoader = PowerMock.createPartialMock(PromptsLoader.class, "getPromptResourceFromRemote");
		BinaryHolder bh = new BinaryHolder("myName", "myName".getBytes());
		Whitebox.setInternalState(promptsLoader, "logger", Logger.getLogger(PromptsLoader.class));
		//prepare and replay
		PowerMock.expectPrivate(promptsLoader, "getPromptResourceFromRemote","fileName",
				PromptConfKeys.CAR_ICON_PROMPT_SERVER).andReturn(bh);
		PowerMock.replayAll();

		//invoke and verify
		BinaryHolder result = promptsLoader.retrieveBinaryDataVia("fileName", 
				PromptConfKeys.CAR_ICON_PROMPT_SERVER);
		PowerMock.verifyAll();

		//assert
		assertEquals("The two objects should be same.", bh, result);
	}
	/**
	 * promptsCache.containsKey(fileName) == true
	 * @throws Exception
	 */
	public void testRetrieveBinaryDataVia1() throws Exception {
		//define variables
		Map<String, BinaryHolder> promptsCache = new HashMap<String, BinaryHolder>();
		BinaryHolder bh = new BinaryHolder("myName", "myName".getBytes());
		promptsCache.put("key", bh);
		Whitebox.setInternalState(PromptsLoader.class, "promptsCache",promptsCache);

		//invoke
		BinaryHolder result = promptsLoader.retrieveBinaryDataVia("key",null);
		//assert
		assertEquals("The two objects should be same.", bh, result);
		promptsLoader.refresh();
	}
	
	
	/**
	 * StringUtils.isEmpty(baseUrl) == false
	 * 200 != method.getStatusCode()
	 * @throws Exception 
	 * 
	 */
	public void testGetPromptResourceFromRemote() throws Exception{
		//define variables
		PromptsLoader promptsLoader = PowerMock.createPartialMock(PromptsLoader.class, 
				"getValue", "getHttpClient");
		Whitebox.setInternalState(promptsLoader, "logger", Logger.getLogger(PromptsLoader.class));
		PromptConfKeys urlType = PromptConfKeys.CAR_ICON_PROMPT_SERVER;
		String fileName = "fileName";
		
		GetMethod method = PowerMock.createMock(GetMethod.class);
		HttpClient httpClient = PowerMock.createMock(HttpClient.class);
		
		//prepare and replay
		EasyMock.expect(promptsLoader.getValue(urlType)).andReturn("1");
		PowerMock.expectNew(GetMethod.class, "1/"+fileName).andReturn(method);
		
		PowerMock.expectPrivate(promptsLoader, "getHttpClient").andReturn(httpClient);
		EasyMock.expect(httpClient.executeMethod(method)).andReturn(0);
		EasyMock.expect(method.getStatusCode()).andReturn(190);
		PowerMock.replayAll();

		//invoke and verify
		BinaryHolder result = Whitebox.invokeMethod(promptsLoader, "getPromptResourceFromRemote", 
				fileName, urlType);
		PowerMock.verifyAll();

		//assert
		assertEquals(fileName, result.getName());
	}
	
	/**
	 * StringUtils.isEmpty(baseUrl) == false
	 * 200 == method.getStatusCode()
	 * @throws Exception 
	 * 
	 */
	public void testGetPromptResourceFromRemote1() throws Exception{
		//define variables
		PromptsLoader promptsLoader = PowerMock.createPartialMock(PromptsLoader.class, 
				"getValue", "getHttpClient");
		Whitebox.setInternalState(promptsLoader, "logger", Logger.getLogger(PromptsLoader.class));
		PromptConfKeys urlType = PromptConfKeys.CAR_ICON_PROMPT_SERVER;
		String fileName = "fileName";
		
		GetMethod method = PowerMock.createMock(GetMethod.class);
		HttpClient httpClient = PowerMock.createMock(HttpClient.class);
		
		//prepare and replay
		EasyMock.expect(promptsLoader.getValue(urlType)).andReturn("1");
		PowerMock.expectNew(GetMethod.class, "1/"+fileName).andReturn(method);
		
		PowerMock.expectPrivate(promptsLoader, "getHttpClient").andReturn(httpClient);
		EasyMock.expect(httpClient.executeMethod(method)).andReturn(0);
		EasyMock.expect(method.getStatusCode()).andReturn(200);
		EasyMock.expect(method.getResponseBody()).andReturn(new byte[]{65,66,67});
		PowerMock.replayAll();

		//invoke and verify
		BinaryHolder result = Whitebox.invokeMethod(promptsLoader, "getPromptResourceFromRemote", 
				fileName, urlType);
		PowerMock.verifyAll();

		//assert
		assertEquals(fileName, result.getName());
		assertEquals(3, result.getContent().length);
		assertEquals(65, result.getContent()[0]);
		assertEquals(66, result.getContent()[1]);
		assertEquals(67, result.getContent()[2]);
	}
	
	/**
	 * StringUtils.isEmpty(baseUrl) == true
	 * @throws Exception 
	 * 
	 */
	public void testGetPromptResourceFromRemote2() throws Exception{
		//define variables
		PromptsLoader promptsLoader = PowerMock.createPartialMock(PromptsLoader.class, 
				"getValue", "getHttpClient");
		Whitebox.setInternalState(promptsLoader, "logger", Logger.getLogger(PromptsLoader.class));
		PromptConfKeys urlType = PromptConfKeys.CAR_ICON_PROMPT_SERVER;
		String fileName = "fileName";
		
		//prepare and replay
		EasyMock.expect(promptsLoader.getValue(urlType)).andReturn("");
		PowerMock.replayAll();

		//invoke and verify
		BinaryHolder result = Whitebox.invokeMethod(promptsLoader, "getPromptResourceFromRemote", 
				fileName, urlType);
		PowerMock.verifyAll();

		//assert
		assertEquals(fileName, result.getName());
	}
}
