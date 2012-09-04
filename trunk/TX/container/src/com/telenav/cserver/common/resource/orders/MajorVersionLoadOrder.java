/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.orders;

import com.telenav.cserver.common.resource.LoadOrder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * MajorVersionLoadOrder.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-5-6
 *
 */
public class MajorVersionLoadOrder extends LoadOrder 
{
	{
		setType("major_version");
	}
	
	/**
	 * get the attribute value from UserProfile
	 * 
	 * @param profile
	 * @return
	 */
	public String getAttributeValue(UserProfile profile, TnContext tnContext)
	{
		String version = profile.getVersion();
		if(version != null)
		{
			int k = version.indexOf('.', 2);   
			if(k != -1)
			{
				version = version.substring(0, k);
			}
		}
		if(version != null)
		{
			version = version.replace('.', '_');
		}
		return version;
	}
}
