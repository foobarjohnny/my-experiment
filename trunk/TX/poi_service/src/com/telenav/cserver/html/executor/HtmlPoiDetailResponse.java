/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import java.util.List;

import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.html.datatypes.AdsOffer;
import com.telenav.cserver.html.datatypes.ExtraProperty;
import com.telenav.cserver.html.datatypes.GasPrice;
import com.telenav.cserver.html.datatypes.MenuItem;
import com.telenav.cserver.html.datatypes.PoiDetail;

/**
 * @TODO	Define the response Object
 * @author panzhang@telenav.cn
 * @version 1.0 Jan 26, 2011
 */
public class HtmlPoiDetailResponse extends ExecutorResponse
{
	private String operateType;
	
	//main tab
	private PoiDetail poiDetail;
	//extra tab
	private List<ExtraProperty> extras;
	//menu
	private MenuItem menu;
	//private String menuText;
	//private String menuImage;
	//Offer
	private List<AdsOffer> offerList;
	//gas price
	//extra tab
	private List<GasPrice> gasPriceList;
	private boolean isAdsRequest;
	private boolean isPoiRequest;
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\npoiDetail:");
		if(poiDetail != null)
		{
			sb.append(this.getPoiDetail().toString());
		}
		
		return sb.toString();
	}
	
	
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}


	public PoiDetail getPoiDetail() {
		return poiDetail;
	}


	public void setPoiDetail(PoiDetail poiDetail) {
		this.poiDetail = poiDetail;
	}

	public List<AdsOffer> getOfferList() {
		return offerList;
	}


	public void setOfferList(List<AdsOffer> offerList) {
		this.offerList = offerList;
	}


	public List<ExtraProperty> getExtras() {
		return extras;
	}


	public void setExtras(List<ExtraProperty> extras) {
		this.extras = extras;
	}

	public List<GasPrice> getGasPriceList() {
		return gasPriceList;
	}


	public void setGasPriceList(List<GasPrice> gasPriceList) {
		this.gasPriceList = gasPriceList;
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


	public MenuItem getMenu() {
		return menu;
	}


	public void setMenu(MenuItem menu) {
		this.menu = menu;
	}
}
