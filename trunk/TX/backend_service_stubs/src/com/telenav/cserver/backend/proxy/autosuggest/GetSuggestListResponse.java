/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.autosuggest;

import java.util.ArrayList;
import java.util.List;

import com.telenav.cserver.backend.proxy.HttpClientResponse;

/**
 * AutoSuggestListResponse.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Apr 13, 2011
 *
 */
public class GetSuggestListResponse extends HttpClientResponse
{
 
    private List<SuggestItem> suggestList = new ArrayList<SuggestItem>();


    public List<SuggestItem> getSuggestList()
    {
        return suggestList;
    }

    public void setSuggestList(List<SuggestItem> suggestList)
    {
        this.suggestList = suggestList;
    }
    
    public void addSuggestItem(SuggestItem item)
    {
        suggestList.add(item);
    }

    @Override
    public String toString()
    {
        return suggestList.toString();
    }
    
    
  
}
