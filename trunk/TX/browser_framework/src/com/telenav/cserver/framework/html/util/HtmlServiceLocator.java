package com.telenav.cserver.framework.html.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.json.me.JSONException;
import org.json.me.JSONObject;


public class HtmlServiceLocator {
	private static HtmlServiceLocator instance = new HtmlServiceLocator(); 
	private Logger logger = Logger.getLogger(HtmlServiceLocator.class);
	private Map<String,String> serviceMap = null;
	/**
	 * 
	 * @return
	 */
	public static HtmlServiceLocator getInstance()
	{
		return instance;
	}
	
	private Map<String,String> getServiceMap()
	{
    	if(serviceMap == null)
    	{
    		serviceMap = new HashMap<String,String>();
    	  	String fielPath = "config.serviceLocator";
        	try
            {
                ResourceBundle serverBundle = ResourceBundle.getBundle(fielPath);
                Enumeration<String> enumeration = serverBundle.getKeys();

                String key;
                while(enumeration.hasMoreElements())
                {
                	key = enumeration.nextElement();
                	serviceMap.put(key, serverBundle.getString(key));
                }
                //
            }
            catch(Exception e)
            {
            	logger.debug("error during read config.serviceLocator file:" + e.getMessage());
            }
    	}

        return serviceMap;
	}
	
	public String getServiceUrl(String hostUrl,String key)
	{
		Map<String,String> serviceMap = getServiceMap();
		String urlMapping = serviceMap.get(key);
		String tempkey = "";
		String url = "";
		hostUrl = trimHttpHead(hostUrl);
		try {
			JSONObject json = new JSONObject(urlMapping);
			Enumeration em = json.keys();
			while(em.hasMoreElements())
			{
				tempkey = (String)em.nextElement();
				if(hostUrl.startsWith(trimHttpHead(tempkey)))
				{
					url = json.getString(tempkey);
					break;
				}
			}
			if("".equals(url))
			{
				//default is Production url
				url = json.getString("");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("serviceKey:" + key + "hostUrl:" + hostUrl + ",url:" + url);
		return url;
	}
	
	/**
	 * when do the compare, remove the http(s)://
	 */
	private String trimHttpHead(String hostUrl)
	{
		String tempStr;
		//remove http(s):
		String[] array = hostUrl.split("://");
		if(array.length > 1)
		{
			tempStr = array[1];
		}
		else
		{
			tempStr = array[0];
		}
		
		return tempStr;
	}
}
