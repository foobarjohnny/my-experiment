/*
 *  @file MoviesLayout.java	
 *  @copyright (c) 2008 Telenav, Inc.
 */
package com.telenav.browser.movie.layout;

import java.util.Map;

/**
 * 
 * @author zywang
 * @version 1.0 2009-1-14
 */

public class MoviesLayout
{
    private Map layoutProp;

    public MoviesLayout(Map map)
    {
        this.layoutProp = map;
    }
    
    /**
     * @return Returns the layoutProp.
     */
    public Map getLayoutProp()
    {
        return layoutProp;
    }

    public int getIntProperty(String key)
    {
        return Integer.parseInt((String)layoutProp.get(key));
    }
     
    public String getStringProperty(String key)
    {
        return (String)layoutProp.get(key);
    }
    
}
