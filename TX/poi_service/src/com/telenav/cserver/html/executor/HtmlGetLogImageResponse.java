/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import com.telenav.cserver.framework.executor.ExecutorResponse;

/**
 * @TODO	Define the response Object
 * @author jhjin@telenav.cn
 * @version 1.0 Feb 22, 2011
 */
public class HtmlGetLogImageResponse extends ExecutorResponse
{
	private String imageName;
    private String image;

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
    
    

}
