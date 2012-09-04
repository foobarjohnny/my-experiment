package com.telenav.cserver.unittestutil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.ServletInputStream;

import org.apache.xalan.xsltc.runtime.Hashtable;

public class MockHttpServletRequest extends
		org.apache.struts.mock.MockHttpServletRequest {
	private byte[] inputStreamData;
	private Vector<String> headerNames=new Vector<String>();
	private Hashtable headers=new Hashtable();
	
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
        return headerNames.elements();
    }
    
    public void setHeaderNames(Enumeration<String> headerNames){
    	while(headerNames.hasMoreElements()){
    		this.headerNames.add(headerNames.nextElement());
    	}
    }

	@Override
	public String getHeader(String name) {
		return (String) headers.get(name);
	}

	@Override
	public StringBuffer getRequestURL() {
		return new StringBuffer("testurl");
	}

	@Override
	public String getServletPath() {
		return "junittest";
	}

	@Override
	public String getRemoteUser() {
		return "test";
	}

	@Override
	public String getRemoteAddr() {
		return "127.0.0.1";
	}

	@Override
	public String getRemoteHost() {
		return "test";
	}
	
	
	

}
