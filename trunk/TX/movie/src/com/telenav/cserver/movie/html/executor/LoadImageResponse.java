/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.movie.html.executor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LoadImageResponse.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Dec 28, 2010
 *
 */
public class LoadImageResponse extends MovieCommonResponse
{
    private Map<String,String> images = new LinkedHashMap<String, String>();

    public Map<String, String> getImages()
    {
        return images;
    }

    public void setImages(Map<String, String> images)
    {
        this.images = images;
    }
}
