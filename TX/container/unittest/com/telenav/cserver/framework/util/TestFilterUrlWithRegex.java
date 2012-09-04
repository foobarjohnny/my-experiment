/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * TestFilterUrlWithRegex.java
 * @author njiang
 * @version 1.0 Jan 16, 2012
 */
public class TestFilterUrlWithRegex extends TestCase{
	
//	private Pattern urlPattern=Pattern.compile("https?://[^/]*(?::\\d+)?");
	
	private Pattern pattern = Pattern.compile(".+://[^/]*");
	
	
	public void testUrlRegex(){
		String[] url = new String[13];
		url[0] = "http://63.237.220.118//login_startup_service";
		url[1] = "http://63.237.220.118";
		url[2] = "http://63.237.220.118:8080/";
		url[3] = "http://63.237.220.118:8080";
		url[4] = "http://63.237.220.118:80";
		url[5] = "http://localhost:80/login_startup_service";
		url[6] = "http://localhost:80";
		url[7] = "http://localhost/login_startup_service";
		url[8] = "http://localhost/";
		url[9] = "http://localhost";
		url[10] = "https://localhost";
		url[11] = "socket://localhost:8080/login_startup_service";
		url[12] = "socket://localhost:8080/";
		
		int i = 0;
		
		for(String s : url){
			Matcher matcher=pattern.matcher(s);
			if(matcher.find()){
				if(i == 0 || i == 1){
					Assert.assertEquals("http://63.237.220.118", matcher.group(0));
				}else if(i == 2 || i == 3){
					Assert.assertEquals("http://63.237.220.118:8080", matcher.group(0));
				}else if(i == 4){
					Assert.assertEquals("http://63.237.220.118:80", matcher.group(0));
				}else if(i == 5 || i == 6){
					Assert.assertEquals("http://localhost:80", matcher.group(0));
				}else if(i == 7 || i == 8 || i == 9){
					Assert.assertEquals("http://localhost", matcher.group(0));
				}else if(i == 10){
					Assert.assertEquals("https://localhost", matcher.group(0));
				}else{
					Assert.assertEquals("socket://localhost:8080", matcher.group(0));
				}
				i++;
			}
			
		}
		
	}
}
