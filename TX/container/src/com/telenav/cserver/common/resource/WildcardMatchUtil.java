/**
 * (c) Copyright 2008 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.List;

import org.apache.log4j.Category;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * WildcardMatchUtil
 * 
 * @author yqchen@telenav.cn
 * @version 1.0 2008-3-25
 *
 */
public class WildcardMatchUtil
{
	protected static Category logger = Category.getInstance(WildcardMatchUtil.class);
	
	public final static char ANY_CHARACTER = '?';
	public final static String ASTERISK = "*";
	
	/**
	 * string match
	 * 
	 * @param origString
	 * @param destString
	 * @return
	 */
	public static boolean matchString(String origString, String destString)
	{
		if(origString == null)
		{
			origString = "";
		}
		
		//destString has been UpperCase already.
		origString = origString.toUpperCase();	
    	
    	if(destString.equals(ASTERISK))
    	{
    		return true;
    	}
    	
    	if(origString.length() == destString.length())
    	{
    		if(destString.equals(origString))
        	{
    			return true;
        	}
    		
        	//compare '?'
        	int k = destString.indexOf(ANY_CHARACTER);
        	if(k > -1)
        	{
        		for(int j = 0; j < origString.length(); j ++)
        		{
        			char ch1 = origString.charAt(j);
        			char ch2 = destString.charAt(j);
        			if(ch2 != ANY_CHARACTER && ch1 != ch2)
        			{
        				return false;
        			}
        		}
        		return true;
        	}
    	}
    	
    	int posOfAsterisk = destString.indexOf(ASTERISK);
    	if(posOfAsterisk != -1)
    	{
    		//destString like: "5.2.*" or "*.2.01"
    		//Currently, we don't support multipe asterisk
    		if(posOfAsterisk == 0)
    		{
    			//*.2.01
    			//origString: 5.2.01, 6.2.01, 5.2
    			if(origString.length() >= destString.length())
    			{
    				int offset = origString.length() - destString.length();
    				for(int j = origString.length() - 1; j >= origString.length() - offset; j --)
            		{
            			char ch1 = origString.charAt(j);
            			char ch2 = destString.charAt(j - offset);
            			if(ch1 != ch2)
            			{
            				return false;
            			}
            		}
        			return true;
    			}    			
    		}
    		
    		
    		if(posOfAsterisk == destString.length() - 1)
    		{
    			//5.2.*    			
    			for(int j = 0; j < origString.length() && j < posOfAsterisk; j ++)
        		{
        			char ch1 = origString.charAt(j);
        			char ch2 = destString.charAt(j);
        			if(ch1 != ch2)
        			{
        				return false;
        			}
        		}
    			return true;
    		}
    	}
    	return false;
	}

	/**
	 * get matched string by wildcard
	 * 
	 * @param wildcardOrders
	 * @param wrapper
	 * @param list
	 * @return
	 */
	public static String match(LoadOrders wildcardOrders,
			UserProfile profile, TnContext tnContext, List list)
	{
		if(wildcardOrders == null || profile == null || list == null)
		{
			return null;
		}
		List<LoadOrder> orderList = wildcardOrders.getOrders();
		//platform, version, carrier, device
		int size = orderList.size();		
		
        for(int index = 0 ; index < list.size(); index ++)
        {
        	MatchObject mo = (MatchObject)list.get(index);
        	String[] keyStrings = mo.keyStrings;
            String value = mo.value;
            
            boolean isMatched = false;
            int i = 0;
            for(; i < size; i ++)
			{		
            	String destString = keyStrings[i];
            	String origString = null;
            	
            	LoadOrder order = orderList.get(i);
            	origString = order.getAttributeValue(profile, tnContext);
//            	replace "_" in locale, "en_US" -> "en-US", to avoid confliction with key string
            	
            	origString = origString.replaceAll("_", "-");
            	
    			
            	isMatched = matchString(origString, destString);     
            	
            	if(!isMatched)
            	{
            		//NOT matched, break
            		break;
            	}
			}
            //double judge to get out of double loop
            if(!isMatched)
        	{
        		//NOT matched, continue;
        		continue;
        	}
            
			
			
            if(logger.isDebugEnabled())
            {
            	StringBuffer sb = new StringBuffer("Matched:");
            	for(int k = 0; k < keyStrings.length; k ++)
            	{
            		sb.append(keyStrings[k]).append("_");
            	}
            	logger.debug(sb.toString());
                
            }
            return value;
        }       
        
        if(logger.isDebugEnabled())
        {
        	logger.debug("No matched.");
        }
		return null;
	}
	
	
//	public static void main(String[] args)
//	{
//		System.out.println(matchString("5.2.01-free", "5.2.*"));
//		System.out.println(matchString("5.2.01-free", "*-FREE"));
//		System.out.println(matchString("5.2.01-free", "*0FREE"));
//		System.out.println(matchString("5.2.01-free", "5.2..*"));
//	}
}
