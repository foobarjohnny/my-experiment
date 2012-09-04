package com.telenav.cserver.dsr.util;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchConfig
{
	private static Logger logger = Logger.getLogger(SearchConfig.class.getName());
	
	private static String propFile = "/search_proxy.properties";
	
	private String url;
	private String key ;
	private int connectionTime = 2000;
    private int soTimeOut = 0;
    private int maxConnectionsPerHost = 10;
    private int maxTotalConnections = 30;

	private static final String URL = "url";
	private static final String KEY = "key";
	
	private static SearchConfig instance = new SearchConfig();
	
	public static SearchConfig getInstance()
	{
		return instance ;
	}
	
	private SearchConfig()
	{
		init() ;
	}
	
	private void init()
	{
		InputStream is = null;
		
		try
		{
			is = SearchConfig.class.getResourceAsStream(propFile);
			Properties props = new Properties();
			props.load(is);

			Iterator ite = props.keySet().iterator();
			while (ite.hasNext())
			{
				String key = ite.next().toString();
				if (key.equalsIgnoreCase(URL))
					url = props.getProperty(key);
				else if (key.equalsIgnoreCase(KEY))
					this.key = props.getProperty(key);
			}
		}
		catch (Exception e)
		{
			logger.log(Level.SEVERE, "Read Properties Failed", e);
		}
		finally
		{
			if (is != null)
			{
				try
				{
					is.close();
				}
				catch (Exception ignored)
				{}
			}
		}
	}
	
	public  String getUrl()
	{
		return url ;
	}

	public  String getKey()
	{
		return key ;
	}
	
	/**
	 * @return the connectionTime
	 */
	public int getConnectionTime() {
		return connectionTime;
	}

	/**
	 * @return the soTimeOut
	 */
	public int getSoTimeOut() {
		return soTimeOut;
	}

	/**
	 * @return the maxConnectionsPerHost
	 */
	public int getMaxConnectionsPerHost() {
		return maxConnectionsPerHost;
	}

	/**
	 * @return the maxTotalConnections
	 */
	public int getMaxTotalConnections() {
		return maxTotalConnections;
	}
}
