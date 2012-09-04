/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * @TODO	tool class
 * @author zhhyan
 * @version 1.0 
 */
public class HtmlPoiExtraHelper {
	private static final Logger logger = Logger.getLogger(HtmlPoiExtraHelper.class);
	
	private static final String FILENAME = "text/poi_extra_attribute_key.txt";
	private static ArrayList<String> extraList = new ArrayList<String>();
	private static HtmlPoiExtraHelper instance = null;
	
	public static HtmlPoiExtraHelper getInstance(){
		if (instance == null) {
			instance = new HtmlPoiExtraHelper();
			extraList = instance.initExtraList();
		}
		return instance;
	}
	/**
	 * TODO initialize the extra list
	 * @return
	 */
	private ArrayList<String> initExtraList(){
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream stream = cl.getResourceAsStream(FILENAME);
		if (stream != null) {
			BufferedReader br=new BufferedReader(new InputStreamReader(stream));
			String key=null; 
			try {
				while((key=br.readLine())!=null) 
				{ 
					extraList.add(key.toLowerCase()); 
				}
			} catch (IOException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		return extraList;
	}
	
	public ArrayList<String> getExtraList(){
		return extraList;
	}
	
}
