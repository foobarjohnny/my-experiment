/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.autosuggest;

/**
 * AutoSuggestListRequest.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Apr 13, 2011
 *
 */
public class GetSuggestListRequest
{
    
    private int count;
    private String queryString;
    private String lat;
    private String lon;
    private String ptn;
    private String transactionId;
    private String userId;
    private String mapDataSet;
    private String extendParams = "";
    
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("count="+count)
          .append(",queryString="+queryString)
          .append(",lat="+lat)
          .append(",lon="+lon)
          .append(",ptn="+ptn)
          .append(",transactionId="+transactionId)
          .append(",userId="+userId)
          .append(",mapDataSet="+mapDataSet)
          .append(",extendParams="+extendParams);
        return sb.toString();
    }
    
    public int getCount()
    {
        return count;
    }
    public void setCount(int count)
    {
        this.count = count;
    }
    public String getQueryString()
    {
        return queryString;
    }
    public void setQueryString(String queryString)
    {
        this.queryString = queryString;
    }
    public String getLat()
    {
        return lat;
    }
    public void setLat(String lat)
    {
        this.lat = lat;
    }
    public String getLon()
    {
        return lon;
    }
    public void setLon(String lon)
    {
        this.lon = lon;
    }
    public String getPtn()
    {
        return ptn;
    }
    public void setPtn(String ptn)
    {
        this.ptn = ptn;
    }
    public String getTransactionId()
    {
        return transactionId;
    }
    public void setTransactionId(String tid)
    {
        this.transactionId = tid;
    }
    public String getUserId()
    {
        return userId;
    }
    public void setUserId(String uid)
    {
        this.userId = uid;
    }
    public String getMapDataSet()
    {
        return mapDataSet;
    }
    public void setMapDataSet(String mapDataSet)
    {
        this.mapDataSet = mapDataSet;
    }
    public String getExtendParams()
    {
        return extendParams;
    }
    public void setExtendParams(String extendParams)
    {
        this.extendParams = extendParams;
    }

}
