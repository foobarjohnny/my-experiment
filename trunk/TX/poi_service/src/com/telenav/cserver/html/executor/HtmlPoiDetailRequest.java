/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import com.telenav.cserver.framework.executor.ExecutorRequest;

/**
 * @TODO	Define the request Object
 * @author panzhang@telenav.cn
 * @version 1.0 3-3, 2011
 *
 */
public class HtmlPoiDetailRequest extends ExecutorRequest
{
    private String operateType;
    private long poiId;
    private String categoryId;
    private long adsId;
    
    private String width;
    private String height;
    private String menuWidth;
    private String menuHeight;
    private boolean isAdsRequest;
    private boolean isPoiRequest;
    
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\npoiId:");
		sb.append(this.getPoiId());
		return sb.toString();
	}

	public String getOperateType() {
		return operateType;
	}


	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}


	public long getPoiId() {
		return poiId;
	}


	public void setPoiId(long poiId) {
		this.poiId = poiId;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getMenuWidth() {
		return menuWidth;
	}

	public void setMenuWidth(String menuWidth) {
		this.menuWidth = menuWidth;
	}

	public String getMenuHeight() {
		return menuHeight;
	}

	public void setMenuHeight(String menuHeight) {
		this.menuHeight = menuHeight;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public long getAdsId() {
		return adsId;
	}

	public void setAdsId(long adsId) {
		this.adsId = adsId;
	}

	public boolean isAdsRequest() {
		return isAdsRequest;
	}

	public void setAdsRequest(boolean isAdsRequest) {
		this.isAdsRequest = isAdsRequest;
	}

	public boolean isPoiRequest() {
		return isPoiRequest;
	}

	public void setPoiRequest(boolean isPoiRequest) {
		this.isPoiRequest = isPoiRequest;
	}
}
