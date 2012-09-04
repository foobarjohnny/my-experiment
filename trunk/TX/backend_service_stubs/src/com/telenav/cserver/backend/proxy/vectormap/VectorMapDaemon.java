/*
 * (c) Copyright 2012 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.vectormap;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import com.telenav.cserver.backend.proxy.BackendProxyManager;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * @author huishen
 *
 */
public class VectorMapDaemon implements Runnable
{
	private final static String PROGRAM_MMINAVPROG = "MMINAVPROG";
	private final static String KEY_MMINAVPROG = "IN_MMI";
	
	private final static String PROGRAM_SCOUTEUPROG = "SCOUTEUPROG";
	private final static String KEY_SCOUTEUPROG = "EU_NT";
	
	private final static String PROGRAM_TMOUKNAVPROG = "TMOUKNAVPROG";
	private final static String KEY_TMOUKNAVPROG = "EU_NT";
	
	private final static String PROGRAM_TELCELNAVPROG = "TELCELNAVPROG";
	private final static String KEY_TELCELNAVPROG = "MX_NT";
	
	private final static String DEFAULT_KEY = "US_TA";
	
	private static final VectorMapDaemon vectorMapDaemon = new VectorMapDaemon();
	
	private Map<String,String> mapURLsMap = new HashMap<String, String>();
	
	private Category logger = Logger.getLogger(VectorMapDaemon.class);
	
	private VectorMapDaemon(){}
	
	public static VectorMapDaemon getInstance()
	{
		return vectorMapDaemon;
	}
	
	public String getMapURL(UserProfile profile, TnContext tnContext)
	{
		String key = generateKey(profile, tnContext);
		
		String url = mapURLsMap.get(key);
		//retrieve blank ones
		if (StringUtils.isEmpty(url))
		{
			synchronized(mapURLsMap)
			{
				url = mapURLsMap.get(key);
				if (StringUtils.isEmpty(url))
				{
					url = BackendProxyManager.getBackendProxyFactory().getBackendProxy(
							VectorMapHttpClient.class).getVectorMapURL(key);
					if (!StringUtils.isEmpty(url))
					{
						logger.debug("Vector map url from backend: [" + key + "] " + url);
						mapURLsMap.put(key, url);
					}
				}
			}
		}
		logger.debug("Vector map: [" + key + "] " + url);
		return url == null ? "" : url;
	}
	
	/**
	 * The key should be "{region}_{data set}".
	 * Because we cannot get the detailed region info from UserProfile, 
	 * we choose to map program code to region.
	 * Currently the region for every program is certain.
	 * @param profile
	 * @param tnContext
	 * @return
	 */
	private String generateKey(UserProfile profile, TnContext tnContext)
	{
		String key = DEFAULT_KEY;
		String programCode = profile.getProgramCode();
		if (PROGRAM_MMINAVPROG.equals(programCode))
		{
			key = KEY_MMINAVPROG;
		}
		else if (PROGRAM_SCOUTEUPROG.equals(programCode))
		{
			key = KEY_SCOUTEUPROG;
		}
		else if (PROGRAM_TMOUKNAVPROG.equals(programCode))
		{
			key = KEY_TMOUKNAVPROG;
		}
		else if (PROGRAM_TELCELNAVPROG.equals(programCode))
		{
			key = KEY_TELCELNAVPROG;
		}
		
		return key;
	}
	
	public void run()
	{
		try 
		{
			Set<Entry<String, String>> entries = mapURLsMap.entrySet();
			String url;
			String key;
			for (Entry<String, String> entry : entries)
			{
				key = entry.getKey();
				url = BackendProxyManager.getBackendProxyFactory().getBackendProxy(
						VectorMapHttpClient.class).getVectorMapURL(key);
				if (!StringUtils.isEmpty(url))
				{
					logger.debug("Vector map url from backend: [" + key + "] " + url);
					mapURLsMap.put(key, url);
				}
			}
		} 
		catch (Exception e) 
		{
			logger.error(e.getStackTrace());
		}
	}
	
//	ATTNAVPROG\ANDROID\7_0_01  US
//	ATTNAVPROG\ANDROID\7_1	
//	ATTNAVPROG\ANDROID\7_2_0
//	ATTNAVPROG\IPHONE\7_0_01
//	ATTNAVPROG\IPHONE\7_2_0
//	BELLNAVPROG\ANDROID\7_2_0
//	MMINAVPROG\ANDROID\7_1     IN_MMI
//	SCOUTEUPROG\ANDROID\7_2_0	EU_NT
//	SCOUTPROG\IPHONE\7_0_01
//	SCOUTPROG\IPHONE\7_1
//	SCOUTPROG\IPHONE\7_1_0
//	SCOUTPROG\IPHONE\7_2_0
//	SCOUTUSPROG\ANDROID\7_2_0
//	SNNAVPROG\ANDROID\7_1
//	SNNAVPROG\ANDROID\7_1
//	TELCELNAVPROG\ANDROID\7_2_0
//	TMOUKNAVPROG\ANDROID\7_1	EU_NT
//	TMOUSNAVPROG\ANDROID\7_1
//	TNNAVPLUSPROG\IPHONE\7_2_0
//	TNNAVPROG\IPHONE\7_0_01
//	TNNAVPROG\IPHONE\7_2_0
//	USCCNAVPROG\ANDROID\7_1
//	USCCNAVPROG\ANDROID\7_2_0
//	VIVONAVPROG\ANDROID\7_1
//	VIVONAVPROG\ANDROID\7_2_0

}
