package com.telenav.browser.movie.test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;


public class MockHttpServletRequest extends
		org.apache.struts.mock.MockHttpServletRequest {
	private byte[] inputStreamData;
	private Hashtable<String, String> headers=new Hashtable<String, String>();
	
	public MockHttpServletRequest(byte[] inputStreamData){
		this.inputStreamData=inputStreamData;
	}

	@Override
	public ServletInputStream getInputStream() {
		final ByteArrayInputStream bais=new ByteArrayInputStream(inputStreamData);
		ServletInputStream is=new ServletInputStream() {
			
			@Override
			public int read() throws IOException {
				return bais.read();
			}

			@Override
			public int available() throws IOException {
				return bais.available();
			}
		};
		return is;
	}
		
	@Override
	public int getContentLength() {
		return inputStreamData.length;
	}

	public BufferedReader getReader(){
		InputStream is=new ByteArrayInputStream(inputStreamData);
		Reader in=new InputStreamReader(is);
		BufferedReader reader=new BufferedReader(in);
		return reader;
	}


    @SuppressWarnings("rawtypes")
	public Enumeration getHeaderNames() {    	
        return headers.keys();
    }

	@Override
	public String getHeader(String name) {
		return (String) headers.get(name);
	}
	
	public void addHeader(String key, String value){
		headers.put(key, value);
	}

	@Override
	public StringBuffer getRequestURL() {
		return new StringBuffer("testurl");
	}

	@Override
	public String getRemoteAddr() {
		return "127.0.0.1";
	}

	@Override
	public String getRemoteHost() {
		return "test";
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		try {
			Class<?> cls=Class.forName("com.telenav.cserver.datatest.MockRequestDispatcher");
			Constructor<?> con = cls.getConstructor(String.class);
			return (RequestDispatcher) con.newInstance(path);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void removeParameter(String name){
		this.parameters.remove(name);
	}
	
	
	

}
