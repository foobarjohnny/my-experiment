/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.ArrayList;
import java.util.List;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * the order for loading
 * 
 * @author yqchen
 * @version 2.0 2009-5-6 17:46
 */
public abstract class LoadOrder
{	
	private String type;
		
	public String getType()
	{
		return type;
	}
	
	public void setType(String type)
	{
		this.type = type;
		
		LoadOrderManager.register(this);
	}
	
	/**
	 * get the attribute value from UserProfile
	 * 
	 * @param profile
	 * @param tnContext TODO
	 * @return
	 */
	public abstract String getAttributeValue(UserProfile profile, TnContext tnContext);
    
    /**
     * get attribute value list from UserProfile
     * because some attribute value represents multiple values
     * e.g. the "device" represents both "device" and "resolution"
     * @param profile
     * @param tnContext
     * @return
     */
    public List<String> getAttributeValueList(UserProfile profile, TnContext tnContext)
    {
        List<String> list = new ArrayList<String>();
        list.add(getAttributeValue(profile, tnContext));
        return list;
    }
    
    /**
     * some attribute need map to others if there is no exactly match
     * e.g. if there is no resolution 845x320, we will use 850x320 instead 
     * @param fullPath
     * @return
     */
    public List<String> getResemblanceFullPath(String fullPath, String configName, UserProfile profile, TnContext tnContext)
    {
        return new ArrayList<String>();
    }
	
	public String toString()
	{
		return "type:" + type;
	}
}
