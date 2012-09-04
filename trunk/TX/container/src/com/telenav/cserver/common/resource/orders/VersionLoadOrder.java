/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.orders;

import com.telenav.cserver.common.resource.LoadOrder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.util.CSStringUtil;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * VersionOrder.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-5-6
 *
 */
public class VersionLoadOrder extends LoadOrder 
{
	{
		setType("version");
	}
	
	/**
	 * get the attribute value from UserProfile
	 * 
	 * @param profile
	 * @return
	 */
	public String getAttributeValue(UserProfile profile, TnContext tnContext)
	{
		if(CSStringUtil.isNotNil(profile.getVersion()))
		{
		    String version = profile.getVersion();
		    //if( version.startsWith("7.1.") || version.startsWith("7_1_") ){
		    //    version = "7.1.0";
		    //}
			return version.replaceAll("\\.", "_");
		}
		return "";
	}
	
	/**
	 * get version value from UserProfile which ignore the subversion from client.
	 * The different with  getAttributeValue's method is that the version number need format from "_" to ".". 
	 * 
	 * @param profile
	 * @param tnContext
	 * @return
	 */
	public String getAttributeValueIgnoreSubversion(UserProfile profile)
	{
		if(CSStringUtil.isNotNil(profile.getVersion()))
		{
		    String version = profile.getVersion();
		    //if( version.startsWith("7.1.") || version.startsWith("7_1_") ){
		    //    version = "7.1.0";
		    //}
			return version.replaceAll("_", "\\.");
		}
		return "";
	}
	
}
