/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.movie.html.executor;

/**
 * LoadImageRequest.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Dec 28, 2010
 *
 */
public class LoadImageRequest extends MovieCommonRequest
{
    private int height;
    private int width;
    private String[] movieIds = new String[0];

    public String[] getMovieIds()
    {
        return movieIds;
    }

    public void setMovieIds(String[] movieIds)
    {
        this.movieIds = movieIds;
    }

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
    
    

}
