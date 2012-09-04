/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.datatypes;
/**
 * @TODO	define data type
 * @author  
 * @version 1.0 
 */
public class PoiDetail {
	//private String businessHoursNote;
	private String description  = "";
	private String priceRange  = "";
	//
	private String businessHours  = "";
	private String olsonTimezone  = "";
	//
    private String adSource  = "";
    private String adID  = "";
    //poi logo name
    private String logoName  = "";
    //poi tab flag
    private boolean hasPoiMenu;
    private boolean hasPoiExtraAttributes;
    private boolean hasGasPrice;
    private boolean hasHotel;
    private boolean hasOpenTable;
    private boolean hasTheater;
    private boolean hasDeal;
    private boolean hasReview;
    //
    private String vendorId = "";
    private String vendorCode = "";
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\ndescriptions:");
		sb.append(this.getDescription());
		sb.append("\npriceRange:");
		sb.append(this.getPriceRange());
		sb.append("\nbusinessHours:");
		sb.append(this.getBusinessHours());
		sb.append("\nolsonTimezone:");
		sb.append(this.getOlsonTimezone());
		sb.append("\nadSource:");
		sb.append(this.getAdSource());
		sb.append("\nadID:");
		sb.append(this.getAdID());
		sb.append("\nlogoName:");
		sb.append(this.getLogoName());
		sb.append("\nhasPoiMenu:");
		sb.append(this.isHasPoiMenu());
		sb.append("\nhasPoiExtraAttributes:");
		sb.append(this.isHasPoiExtraAttributes());
		sb.append("\nhasGasPrice:");
		sb.append(this.isHasGasPrice());
		sb.append("\nhasHotel:");
		sb.append(this.isHasHotel());
		sb.append("\nhasOpenTable:");
		sb.append(this.isHasOpenTable());
		sb.append("\nhasTheater:");
		sb.append(this.isHasTheater());
		sb.append("\nhasDeal:");
		sb.append(this.isHasDeal());
		sb.append("\nhasReview:");
		sb.append(this.isHasReview());
		sb.append("\nvendorId:");
		sb.append(this.getVendorId());
		sb.append("\nvendorCode:");
		sb.append(this.getVendorCode());
		
		return sb.toString();
	}
	
	
	public String getBusinessHours() {
		return businessHours;
	}
	public void setBusinessHours(String businessHours) {
		this.businessHours = businessHours;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPriceRange() {
		return priceRange;
	}
	public void setPriceRange(String priceRange) {
		this.priceRange = priceRange;
	}
	public String getOlsonTimezone() {
		return olsonTimezone;
	}
	public void setOlsonTimezone(String olsonTimezone) {
		this.olsonTimezone = olsonTimezone;
	}

	public String getAdSource() {
		return adSource;
	}


	public void setAdSource(String adSource) {
		this.adSource = adSource;
	}


	public String getAdID() {
		return adID;
	}


	public void setAdID(String adID) {
		this.adID = adID;
	}


	public boolean isHasPoiMenu() {
		return hasPoiMenu;
	}


	public void setHasPoiMenu(boolean hasPoiMenu) {
		this.hasPoiMenu = hasPoiMenu;
	}


	public boolean isHasPoiExtraAttributes() {
		return hasPoiExtraAttributes;
	}


	public void setHasPoiExtraAttributes(boolean hasPoiExtraAttributes) {
		this.hasPoiExtraAttributes = hasPoiExtraAttributes;
	}


	public String getLogoName() {
		return logoName;
	}


	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}


	public boolean isHasGasPrice() {
		return hasGasPrice;
	}


	public void setHasGasPrice(boolean hasGasPrice) {
		this.hasGasPrice = hasGasPrice;
	}


	public boolean isHasHotel() {
		return hasHotel;
	}


	public void setHasHotel(boolean hasHotel) {
		this.hasHotel = hasHotel;
	}


	public boolean isHasOpenTable() {
		return hasOpenTable;
	}


	public void setHasOpenTable(boolean hasOpenTable) {
		this.hasOpenTable = hasOpenTable;
	}


	public boolean isHasTheater() {
		return hasTheater;
	}


	public void setHasTheater(boolean hasTheater) {
		this.hasTheater = hasTheater;
	}


	public String getVendorId() {
		return vendorId;
	}


	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}


	public boolean isHasDeal() {
		return hasDeal;
	}


	public void setHasDeal(boolean hasDeal) {
		this.hasDeal = hasDeal;
	}


	public boolean isHasReview() {
		return hasReview;
	}


	public void setHasReview(boolean hasReview) {
		this.hasReview = hasReview;
	}


	public String getVendorCode() {
		return vendorCode;
	}


	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
}
