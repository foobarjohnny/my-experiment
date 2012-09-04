/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.Iterator;
import java.util.Map.Entry;

/**
 * TextDisplayFormat.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-10-28
 *
 */
class TextDisplayFormat implements DisplayFormat
{
    private final static String NEW_LINE_FLAG = "\n";
    
    ResourceCacheManagement cacheManagement = null;
    
    public TextDisplayFormat(ResourceCacheManagement cacheManagement)
    {
        this.cacheManagement = cacheManagement;
    }

    public String statistic()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(statisticString());

        sb.append(NEW_LINE_FLAG);
        sb.append(HOLDER).append(NEW_LINE_FLAG);
        Iterator<ResourceHolder> holders = cacheManagement.getHolderSet().iterator();
        while (holders.hasNext())
        {
            ResourceHolder holder = holders.next();
            sb.append(
                holder.getName() + "[" + cacheManagement.getCountOfCacheObject(holder) + "] = " +
                cacheManagement.getSizeOfObject().humanReadable(cacheManagement.getCacheSize(holder)))
                    .append(NEW_LINE_FLAG);
        }
        sb.append("-----------------end------------------").append(NEW_LINE_FLAG);
        return sb.toString();
    }

    public String details()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(statisticString());

        sb.append(NEW_LINE_FLAG);
        sb.append(HOLDER).append(NEW_LINE_FLAG);
        Iterator<ResourceHolder> holders = cacheManagement.getHolderSet().iterator();
        ResourceHolder holder = null;
        while (holders.hasNext())
        {
            holder = holders.next();
            Iterator cacheObjects = ((AbstractResourceHolder) holder).getMap().entrySet().iterator();
            while (cacheObjects.hasNext())
            {
                Entry pair = (Entry) cacheObjects.next();
                Object cacheObject = pair.getValue();
                long sizeOfCacheObject = cacheManagement.getSizeOfCacheObject(cacheObject);
                sb.append(pair.getKey() + " = " + cacheManagement.getSizeOfObject().humanReadable(sizeOfCacheObject));
            }
            sb.append("--------------------------------------").append(NEW_LINE_FLAG);
        }
        sb.append("-----------------end------------------").append(NEW_LINE_FLAG);
        return sb.toString();
    }

    private StringBuffer statisticString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("-----------CacheManagement------------").append(NEW_LINE_FLAG);
        sb.append(STATISTIC).append(NEW_LINE_FLAG);
        sb.append(COUNT_OF_HOLDER_TYPE + " = " + cacheManagement.getCountOfHolderType()).append(NEW_LINE_FLAG);
        sb.append(COUNT_OF_HOLDER + " = " + cacheManagement.getCounterOfHolder()).append(NEW_LINE_FLAG);
        sb.append(TOTAL_MEMORY_OF_CACHE + " = " + cacheManagement.getSizeOfObject().humanReadable(
            cacheManagement.getTotalCacheSize())).append(NEW_LINE_FLAG);
        return sb;
    }

    @Override
    public String details(String name)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String contents(String holderName, String key)
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String viewCachedClient(String holderName, String key) {
    	 // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

}

