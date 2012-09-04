/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.Map;


/**
 * ServletHTMLDisplayFormat.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Mar 16, 2011
 *
 */
public class ServletHTMLDisplayFormat extends HTMLDisplayFormat
{
    
    public ServletHTMLDisplayFormat(ResourceCacheManagement cacheManagement)
    {
        super(cacheManagement);
    }
    
    protected String getHttpMethod()
    {
        return "GET";
    }

    @Override
    protected String getContentsAction()
    {
        return "resource-management";
    }

    @Override
    protected StringBuffer getForm(String action, String tips, Map<String, String> args)
    {
    	if(!args.containsKey("operation"))
    		args.put("operation", "contents");
        return super.getForm(action, tips, args);
    }

    @Override
    protected String getInputHiddenItem(String key, String value)
    {
        return "<input type=\"hidden\" name=\"" + key + "\" value=\"" + value + "\">" + "</input>";
    }

    @Override
    public String contents(String holderName, String key)
    {
        String result = super.contents(holderName, key);
        return htmlPage(result);
    }

    @Override
    public String details()
    {
        String result = super.details();
        return htmlPage(result);
    }
    
    private String htmlPage(String contents)
    {
        StringBuffer sb = new StringBuffer("<html>");
        sb.append("<body>");
        sb.append(contents);
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }


    

}
