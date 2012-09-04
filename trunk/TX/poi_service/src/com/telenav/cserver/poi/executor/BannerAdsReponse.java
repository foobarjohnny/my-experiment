/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.poi.executor;

import com.telenav.cserver.framework.executor.ExecutorResponse;

import java.util.Arrays;

/**Response type for banner ads.
 * @author weiw
 * @date Mar 25, 2010
 */

public class BannerAdsReponse extends ExecutorResponse {

    /** link for the wap/web site of the ad. */
    private String clickUrl;

    /** url for the ads image. */
    private String imageUrl;

    /** actual height for the image. */
    private int imageHeight;

    /** actual width for the image. */
    private int imageWidth;

    private byte [] imgData;
    public final String getClickUrl() {
        return clickUrl;
    }

    public final void setClickUrl(final String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public final String getImageUrl() {
        return imageUrl;
    }

    public final void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public final int getImageHeight() {
        return imageHeight;
    }

    public final void setImageHeight(final int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public final int getImageWidth() {
        return imageWidth;
    }

    public final void setImageWidth(final int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public byte[] getImgData() {
        return imgData;
    }

    public void setImgData(byte[] imgData) {
        this.imgData = imgData;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BannerAdsReponse [clickUrl=" + clickUrl + ", imageHeight="
				+ imageHeight + ", imageUrl=" + imageUrl + ", imageWidth="
				+ imageWidth + ", imgData=" + Arrays.toString(imgData) + "]";
	}


}
