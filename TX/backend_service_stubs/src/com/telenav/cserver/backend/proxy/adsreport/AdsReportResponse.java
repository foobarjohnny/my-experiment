package com.telenav.cserver.backend.proxy.adsreport;

import com.telenav.cserver.backend.proxy.HttpClientResponse;

public class AdsReportResponse extends HttpClientResponse 
{
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Status Code=" + this.getStatusCode());
        return buffer.toString();
    }
}
