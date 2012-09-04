/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.ext;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.telenav.cserver.common.resource.ResourceUtil;

/**
 * Overwrite ResourceBundler 
 * 
 * @author yqchen
 * @version 1.0 2007-3-23 15:45:27
 */
public class TelenavResourceBundler
{
	private static Logger logger =  Logger.getLogger(TelenavResourceBundler.class);
	public TelenavResourceBundler(String path)
	{
		this(path, Locale.getDefault(), null);
	}
	
	private Map map = null;
	
	public TelenavResourceBundler(String path, Locale locale, String mapDataSet)
	{
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		
        boolean getByHTTP = path.startsWith("http");
        
      
//		if(locale == null)
//		{
//			locale = Locale.getDefault();
//		}
		String filePath = null;
		if(locale != null && mapDataSet != null)
		{
			filePath = path + "_" + mapDataSet + "_" + locale + ".properties";
		}
        else if(locale != null && mapDataSet == null)
        {
            filePath = path + "_" + locale + ".properties";
        }
        else if(locale == null && mapDataSet != null)
        {
            filePath = path + "_" + mapDataSet + ".properties";
        }
		else
		{
			filePath = path + ".properties";
		}
		
		InputStream is = null;
		
		if(logger.isDebugEnabled())
		{
			logger.debug("loading " + filePath);
		}
        
        if (getByHTTP)
        {
            try
            {
                HttpURLConnection connection = (HttpURLConnection) (new URL(filePath)).openConnection();
                is = new BufferedInputStream(connection.getInputStream());
            }
            catch(IOException e)
            {
                logger.warn("Exception when reading:" + path + "," + e.getMessage());
            }
        }
        else
        {
//            is = cl.getResourceAsStream(filePath);
        	URL url = cl.getResource(filePath);
			if(url == null)
			{
				logger.warn("File not found:" + filePath);
				return;
			}
			
			String absoluteFilePath = url.getPath();
			
        	if(logger.isDebugEnabled())
    		{
    			logger.debug("absoluteFilePath:" + absoluteFilePath);
    		}
        	try {
				FileInputStream fis = new FileInputStream(absoluteFilePath);
				is = new BufferedInputStream(fis);
			} catch (FileNotFoundException e) {				
			}
        }
        
		
		if(is == null)
		{
            logger.warn("No resource for:" + filePath);
			return;
		}
		
		if (logger.isDebugEnabled())
        {
            logger.debug("AbsoluteFilePath:" + filePath);
        }
		
		Properties props = new Properties();
		try
		{
			props.load(is);
			map = ResourceUtil.getResource(props);
			map = trimKeyAndValues(map);
			if( logger.isDebugEnabled() ){
			    logger.debug("map:" + map);
			}
		} catch (Exception e)
		{
			logger.warn("No resource for:" + path + ",locale:" + locale);
//			throw new RuntimeException("No resource for:" + path + ",locale:" + locale, e);
		}
        finally
        {
            try
            {
                if (is != null)
                {
                    is.close();
                }
            } catch (Exception e)
            {
                logger.warn("file close failed.", e);
            }
        }
		
	}
	
	public Map getProperties()
	{
		return map;
	}
	
	public Object get(Object key)
	{
		return map != null ? map.get(key): null;
	}
	
	/**
	 * don't change parameter, return a new map which trim keys and value of map
	 * @param map
	 * @return
	 */
	private Map<String,String> trimKeyAndValues(Map<String,String> map){
	    if( logger.isDebugEnabled() ){
	        logger.debug("invoke trim() on key and values of map.");
	    }
	    if( map == null )
	        return new HashMap<String,String>();
	    Map<String,String> newMap = new HashMap<String,String>();
	    Iterator<String> keys = map.keySet().iterator();
	    while(keys.hasNext()){
	        String key = keys.next();
	        String value = map.get(key);
	        if( key != null ){
	            key = key.trim();
	        }
	        if( value != null ){
	            value = value.trim();
	        }
	        newMap.put(key, value);
	    }
	    return newMap;
	}
	
//	public static void main(String[] args)
//	{
//		TelenavResourceBundler trb = new TelenavResourceBundler("image/icons_color_winmobile_new");
//		
//		System.out.println(trb.getProperties().size());
//		System.out.println(trb.getProperties());
//		
//		trb = new TelenavResourceBundler("image/icons_color_winmobile_new", Locale.US, null);
//		
//		System.out.println(trb.getProperties().size());
//		System.out.println(trb.getProperties());
//	}
}
