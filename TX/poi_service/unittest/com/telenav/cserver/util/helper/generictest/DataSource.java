package com.telenav.cserver.util.helper.generictest;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.powermock.api.easymock.PowerMock;

import com.telenav.tnbrowser.util.DataHandler;

public class DataSource {
	
	public int assertLevel;
	public String logLevel;
	
	HttpServletRequest httpServletRequest = null;
	DataHandler dataHandler = null;
	
	public HashMap<String, Object> dataHub = new HashMap();
	
	
	public static void createData(Class c, Object... objects){
		Object dispatcher = dispatcher(c);
		
	}
	
	
	private static Object dispatcher(Class c){
		
		return new Object();
	}
	
	public HttpServletRequest getHttpServletRequest(){
		if(httpServletRequest == null){
			httpServletRequest = PowerMock.createMock(HttpServletRequest.class);
		}
		return httpServletRequest;
	}
	
	public DataHandler getDataHandler(boolean isHandleAJAXRequest){
		if(dataHandler == null){
			dataHandler = PowerMock.createMock(DataHandler.class, getHttpServletRequest(), isHandleAJAXRequest);
		}
		
		return dataHandler;
	}
	
	
	public void addData(String name, Object o){
		if(null == dataHub){
			dataHub = new HashMap();
		}
		
		dataHub.put(name, o);
	}
	
	public void clear(){
		
		dataHub.clear();
		dataHub = null;
	}
	
	public void setAssertLevel(int level){
		this.assertLevel = level;
	}
	
	public int getAssertLevel(){
		return this.assertLevel;
	}

	public void setLogLevel(String level){
		this.logLevel = level;
	}
	
	public String getLogLevel(){
		return this.logLevel;
	}
	
	public Object lookup(Class<?> c){
		Object o = dataHub.get(c.getName());
		return o;
	}
	
	public Object[] lookup(Class<?>[] c, Object[] o){
		
		if(null == c || null == o){
			return null;
		}
		
		if(o.length < c.length){
			o = new Object[c.length];
		}
		
		for(int i = 0; i < c.length; i++){
			o[i] = lookup(c[i]);
		}		
		return o;
	}
}
