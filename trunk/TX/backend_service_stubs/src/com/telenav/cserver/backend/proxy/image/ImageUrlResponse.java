/*
 * (c) Copyright 2012 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.image;

import com.telenav.cserver.backend.proxy.HttpClientResponse;

/**
 * 
 * @author huishen
 *
 */
public class ImageUrlResponse extends HttpClientResponse 
{
	private String url;

	public String getUrl() 
	{
		return url;
	}

	public void setUrl(String url) 
	{
		this.url = url;
	}
}
