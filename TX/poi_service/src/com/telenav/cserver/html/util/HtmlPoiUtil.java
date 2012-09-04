/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
/**
 * @TODO	Tools class for POI.
 * @author  
 * @version 1.0 
 */
public class HtmlPoiUtil {
	/**
	 * get Version No.
	 * @param clientInfo
	 * @return
	 */
	public static String getVersionNo(HtmlClientInfo clientInfo)
	{
		String programCode = clientInfo.getProgramCode();
		String versionNo = clientInfo.getVersion();
		
		if("ATTNAVPROG".equals(programCode)||"USCCNAVPROG".equals(programCode))
		{
			if(versionNo.startsWith("7")){
				versionNo = "3" + versionNo.substring(1);
			}
		} 
	
		return versionNo;
	}
	
	/**
	 * get Version No.
	 * @param clientInfo
	 * @return
	 */
	public static boolean hideGeneralFeedbackTitle(HtmlClientInfo clientInfo)
	{
		boolean display = true;
		if(HtmlCommonUtil.isIphone(clientInfo.getPlatform()) && clientInfo.getVersion().startsWith("7.3"))
		{
			display = false;
		} 
	
		return display;
	}
	
    
	/**
	 * get Trx Id
	 * @return
	 */
	public static String getTrxId()
	{
		Date today = new Date();
		//using date + ptn as trxn id
		String trxnId = String.valueOf(today.getTime());
		return trxnId;
	}
	
	/**
	 * safe JSON Put String
	 * @param jo
	 * @param key
	 * @param str
	 * @throws JSONException
	 */
	public static void safeJSONPutString(JSONObject jo, String key, String str) throws JSONException
	{
		// System.out.println("safe put " + str);
		if(str!=null)
			jo.put(key, str);
		else
			jo.put(key, "");
		
	}
	/**
	 * format Description
	 * @param desc
	 * @return
	 */
	public static String formatDesc(String desc)
	{
		String desc1 =  desc.replaceAll("\r\n", "<br>");
		String desc2 =  desc1.replaceAll("\n\n", "<br>");
		String desc3 =  desc2.replaceAll("\n", "<br>");
		return desc3;
	}
	/**
	 * filter AdSource
	 * @param adSource
	 * @return
	 */
	public static String filterAdSource(String adSource)
	{
		if(null == adSource || "".equals(adSource))
		{
			return "";
		}
		
		List<String> supporedAdSource = new ArrayList<String>();
		supporedAdSource.add("CS");
		supporedAdSource.add("TN");
		supporedAdSource.add("ATTi");
		supporedAdSource.add("Groupon");
		
		if(supporedAdSource.contains(adSource))
		{
			return adSource;
		}
		else
		{
			return "";
		}
	}
	
	/**
	 * 
	 * @param categoryId
	 * @return
	 */
	public static boolean isTheater(String categoryId)
	{
		boolean isTheater = false;
		
		if("181".equals(categoryId))
		{
			isTheater = true;
		}
		
		return isTheater;
		
	}
	
	/**
	 * 
	 * @param categoryId
	 * @return
	 */
	public static boolean isHotel(String categoryId)
	{
		boolean isHotel = false;
		if ("595".equals(categoryId)) {
			isHotel = true;

		}
		return isHotel;
	}
}
