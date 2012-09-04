/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.datatypes;

import java.util.List;

import com.telenav.j2me.datatypes.Stop;

/**
 * @TODO	define data type
 * @author  
 * @version 1.0 
 */
public class AdsDetail {
	
	private long adId;
	private String adSource  = "";
    private String brandName;
	private String tagline;
    private boolean hasPoiMenu;
    private boolean hasDeal;
    private String logoName;
    private String logoImage;    
	//menu
	//private String menuText;
    private MenuItem menu;
    
	private List<AdsOffer> offerList;
	//for display only
	private String addressDisplay;
	private Stop address;
	private String phoneNo;
    private String distanceWithUnit;
    //data for mis log
    private String poiId = "";
    private String poiType = "";
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\nbrandName:");
		sb.append(this.getBrandName());
		sb.append("\nlogoName:");
		sb.append(this.getLogoName());
		sb.append("\ntagline:");
		sb.append(this.getTagline());
		sb.append("\nadSource:");
		sb.append(this.getAdSource());		
		sb.append("\npoiId:");
		sb.append(this.getPoiId());
		sb.append("\npoiType:");
		sb.append(this.getPoiType());	
		return sb.toString();
	}
	
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getTagline() {
		return tagline;
	}
	public void setTagline(String tagline) {
		this.tagline = tagline;
	}
	public Stop getAddress() {
		return address;
	}
	public void setAddress(Stop address) {
		this.address = address;
	}

	public String getAddressDisplay() {
		return addressDisplay;
	}

	public void setAddressDisplay(String addressDisplay) {
		this.addressDisplay = addressDisplay;
	}

	public long getAdId() {
		return adId;
	}

	public void setAdId(long adId) {
		this.adId = adId;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public List<AdsOffer> getOfferList() {
		return offerList;
	}

	public void setOfferList(List<AdsOffer> offerList) {
		this.offerList = offerList;
	}
	
	public String getDistanceWithUnit(){
	    return distanceWithUnit;
	}

	public void setDistanceWithUnit(String distanceWithUnit){
	    this.distanceWithUnit = distanceWithUnit;
	}


    public String getLogoImage()
    {
        return logoImage;
    }

    public void setLogoImage(String logoImage)
    {
        this.logoImage = logoImage;
    }

	public String getLogoName() {
		return logoName;
	}

	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}

	public boolean isHasPoiMenu() {
		return hasPoiMenu;
	}

	public void setHasPoiMenu(boolean hasPoiMenu) {
		this.hasPoiMenu = hasPoiMenu;
	}

	public boolean isHasDeal() {
		return hasDeal;
	}

	public void setHasDeal(boolean hasDeal) {
		this.hasDeal = hasDeal;
	}

	public String getAdSource() {
		return adSource;
	}

	public void setAdSource(String adSource) {
		this.adSource = adSource;
	}

	public MenuItem getMenu() {
		return menu;
	}

	public void setMenu(MenuItem menu) {
		this.menu = menu;
	}

	public String getPoiId() {
		return poiId;
	}

	public void setPoiId(String poiId) {
		this.poiId = poiId;
	}

	public String getPoiType() {
		return poiType;
	}

	public void setPoiType(String poiType) {
		this.poiType = poiType;
	}
}
