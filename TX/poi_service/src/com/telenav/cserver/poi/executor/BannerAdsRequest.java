/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.poi.executor;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.datatypes.content.ads.v10.ImageSizeType;
import com.telenav.datatypes.content.cose.v10.CategoryParam;
import com.telenav.ws.datatypes.address.Location;



/**Request for banner ads from client.
 * @author weiw
 */

public class BannerAdsRequest extends ExecutorRequest {
	//TODO: category version should be configurable
    public static final String CATEGORY_VERSION = "YP50";
    public static final long HIERARCHY_FULL = 1L;
    
    private String publicIP;
    private String searchId;
    
	private Location loc;
	private Location curLoc;

    private ImageSizeType minSize;
    private ImageSizeType maxSize;
    private String pageId;
    private int pageIndex;
    private String keyWord;
    private CategoryParam category;

    public CategoryParam getCategory() {
        return category;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public Location getLoc() {
        return loc;
    }

    public ImageSizeType getMaxSize() {
        return maxSize;
    }

    public ImageSizeType getMinSize() {
        return minSize;
    }

    public String getPageId() {
        return pageId;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public String getPublicIP() {
        return publicIP;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setCategory(long categoryId) {
        CategoryParam category = new CategoryParam();
        category.setCategoryHierarchyId(HIERARCHY_FULL);
        category.setCategoryVersion(CATEGORY_VERSION);
        category.setCategoryId(new long[] { categoryId });
        this.category = category;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public void setMaxSize(ImageSizeType maxSize) {
        this.maxSize = maxSize;
    }

    public void setMinSize(ImageSizeType minSize) {
        this.minSize = minSize;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setPublicIP(String publicIP) {
        this.publicIP = publicIP;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    
    
	/**
	 * @return the curLoc
	 */
	public Location getCurLoc() {
		return curLoc;
	}

	/**
	 * @param curLoc the curLoc to set
	 */
	public void setCurLoc(Location curLoc) {
		this.curLoc = curLoc;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BannerAdsRequest [category=").append(category).append(
				", keyWord=").append(keyWord).append(", loc=").append(loc)
				.append(", maxSize=").append(maxSize).append(", minSize=")
				.append(minSize).append(", pageId=").append(pageId).append(
						", pageIndex=").append(pageIndex).append(", publicIP=")
				.append(publicIP).append(", searchId=").append(searchId)
				.append("]");
		return builder.toString();
	}
    
}
