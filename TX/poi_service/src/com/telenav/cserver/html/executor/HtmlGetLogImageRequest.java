/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import com.telenav.cserver.framework.executor.ExecutorRequest;

/**
 * @TODO	Define the request Object
 * @author jhjin@telenav.cn
 * @version 1.0 Feb 22, 2011
 *
 */
public class HtmlGetLogImageRequest extends ExecutorRequest
{
    private String operateType;
    private String imageName;
    private String width;
    private String height;
    private String center;
    private String markers;
    private String mapImageUrl;
    private String theme = "";

    public String getImageName()
    {
        return imageName;
    }

    public void setImageName(String imageName)
    {
        this.imageName = imageName;
    }

    public String getWidth()
    {
        return width;
    }

    public void setWidth(String width)
    {
        this.width = width;
    }

    public String getHeight()
    {
        return height;
    }

    public void setHeight(String height)
    {
        this.height = height;
    }

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public String getMarkers() {
		return markers;
	}

	public void setMarkers(String markers) {
		this.markers = markers;
	}

	public String getMapImageUrl() {
		return mapImageUrl;
	}

	public void setMapImageUrl(String mapImageUrl) {
		this.mapImageUrl = mapImageUrl;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}
    
    
}
