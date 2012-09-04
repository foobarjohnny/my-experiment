/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.management.external_service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.cserver.matcher.MatchBox;
import com.telenav.cserver.unittestutil.UnittestUtil;

/**
 * TestExternalServiceManager.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-20
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ExternalServiceManager.class})
@SuppressStaticInitializationFor({"com.telenav.cserver.framework.management.external_service.ExternalServiceManager"})
public class TestExternalServiceManager extends TestCase{
	private ExternalServiceManager externalServiceManager = new ExternalServiceManager();
	private String serviceKey = "serviceKey";
	private String serviceValue = "serviceValue";
	
	@Override
	protected void setUp() throws Exception {
		ExternalServiceManager.props = new Properties();
		ExternalServiceManager.props.put("0", "false");
		ExternalServiceManager.props.put("1", "true");
		
		Whitebox.setInternalState(ExternalServiceManager.class, "logger", Logger.getLogger(ExternalServiceManager.class));
	}
	public void testMarkStatus() throws Exception{
		boolean b;
		b = Boolean.parseBoolean(Whitebox.invokeMethod(ExternalServiceManager.class, "markStatus" ,"3",false).toString());
		assertFalse(b);
		
		b = Boolean.parseBoolean(Whitebox.invokeMethod(ExternalServiceManager.class, "markStatus" ,"0",false).toString());
		assertTrue(b);
		
		b = Boolean.parseBoolean(Whitebox.invokeMethod(ExternalServiceManager.class, "markStatus" ,"0",true).toString());
		assertTrue(b);
	}
	
	public void testGetStatus(){
		//for exception
		try{
			ExternalServiceManager.getStatus(null);
		}catch(Exception e){
			UnittestUtil.printExceptionMsg(e);
		}
		try{
			ExternalServiceManager.getStatus("");
		}catch(Exception e){
			UnittestUtil.printExceptionMsg(e);
		}
		//for fail
		assertFalse(ExternalServiceManager.getStatus(serviceKey));
		
		//for success
		
		assertFalse(ExternalServiceManager.getStatus("0"));
		assertTrue(ExternalServiceManager.getStatus("1"));
		
	}
	public void testStore4fail() throws Exception{
		Object nullObj = null;
		//for fail
		Whitebox.setInternalState(ExternalServiceManager.class, "absoluteFilePath", nullObj);
		Whitebox.invokeMethod(ExternalServiceManager.class, "store");
	}
	public void testStore4success() throws Exception{
		//define variable
		Object nullObj = null;
		Properties props = PowerMock.createMock(Properties.class);
		FileOutputStream out = PowerMock.createMock(FileOutputStream.class);
		String absPath = UnittestUtil.getAbsURLPath("resource/test.xml");
		//prepare and replay
		Whitebox.setInternalState(ExternalServiceManager.class, "absoluteFilePath", absPath);
		Whitebox.setInternalState(ExternalServiceManager.class, "header", "");
		ExternalServiceManager.props = props;
		
		PowerMock.expectNew(FileOutputStream.class, absPath).andReturn(out);
		props.store(MatchBox.outputStreamEquals(out), EasyMock.eq(""));
		out.close();
		
		PowerMock.replayAll();
		
		//invoke and verify
		Whitebox.invokeMethod(ExternalServiceManager.class, "store");
		PowerMock.verifyAll();
		//assert
		//no exception is ok.
		Whitebox.setInternalState(ExternalServiceManager.class, "absoluteFilePath", nullObj);
	}
	public void testSimple(){
		try{
			ExternalServiceManager.markDown("");
		}catch(Exception e){
			UnittestUtil.printExceptionMsg(e);
		}
		try{
			ExternalServiceManager.markUp("");
		}catch(Exception e){
			UnittestUtil.printExceptionMsg(e);
		}
	}

}
