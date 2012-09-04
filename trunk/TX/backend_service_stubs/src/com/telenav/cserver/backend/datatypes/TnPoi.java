/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.telenav.cserver.backend.datatypes.cose.Advertisement;
import com.telenav.cserver.backend.datatypes.cose.Coupon;
import com.telenav.cserver.backend.datatypes.cose.GasPriceInfo;
import com.telenav.cserver.backend.datatypes.cose.OpenTableInfo;
import com.telenav.cserver.backend.datatypes.cose.TnPoiReviewSummary;

/**
 * TnPoi.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-13
 */
public class TnPoi
{
	private long poiId;
	private long basePoiId;
	private String brandName;
	private Address address;
	private String phoneNumber;
	private String featureName;
	private int poiType;
	private String vendor;
	private String editor;
	private String userFlag;
	private byte[] logo;
	private String[] poiImage;
	private Calendar createdTime;
	private Calendar updatedTime;
	private boolean isNavigable;
	private boolean isRatingEnable;
	private List<GasPriceInfo> gasPriceInfos = new ArrayList<GasPriceInfo>();
	private List<Coupon> couponList = new ArrayList<Coupon>();
	private TnPoiReviewSummary reviewSummary;
	private boolean isUgPoi;
	private String flagInfo;
	private Advertisement ad;
	private String businessHour;
	private double distanceInMeter;
	private double distanceToUserInMeter;
	private String categoryLabel;
	private String categoryId;
	private String menu;
	private double priceRange;
	private OpenTableInfo openTableInfo;
	private List<LocalAppInfo> localAppInfos=new ArrayList<LocalAppInfo>();
	public Map<String, String> poiExtraInfo = new HashMap();
	
	public void setLocalAppInfos(List<LocalAppInfo> localAppInfos) {
		this.localAppInfos = localAppInfos;
	}

	public List<LocalAppInfo> getLocalAppInfos() {
		return localAppInfos;
	}

	/**
	 * 
	 * @return featureName
	 */
	public String getFeatureName() {
		return featureName;
	}
	
	/**
	 * featureName the featureName to set
	 * @param featureName
	 */
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	/**
     * @return the categoryId
     */
    public String getCategoryId()
    {
        return categoryId;
    }
    /**
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(String categoryId)
    {
        this.categoryId = categoryId;
    }
    /**
	 * @param poiId the poiId to set
	 */
	public void setPoiId(long poiId)
	{
		this.poiId = poiId;
	}
	/**
	 * @return the poiId
	 */
	public long getPoiId()
	{
		return poiId;
	}
	/**
	 * @param basePoiId the basePoiId to set
	 */
	public void setBasePoiId(long basePoiId)
	{
		this.basePoiId = basePoiId;
	}
	/**
	 * @return the basePoiId
	 */
	public long getBasePoiId()
	{
		return basePoiId;
	}
	/**
	 * @param brandName the brandName to set
	 */
	public void setBrandName(String brandName)
	{
		this.brandName = brandName;
	}
	/**
	 * @return the brandName
	 */
	public String getBrandName()
	{
		return brandName;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address)
	{
		this.address = address;
	}
	/**
	 * @return the address
	 */
	public Address getAddress()
	{
		return address;
	}
	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber()
	{
		return phoneNumber;
	}
	/**
	 * @param poiType the poiType to set
	 */
	public void setPoiType(int poiType)
	{
		this.poiType = poiType;
	}
	/**
	 * @return the poiType
	 */
	public int getPoiType()
	{
		return poiType;
	}
	/**
	 * @param vendor the vendor to set
	 */
	public void setVendor(String vendor)
	{
		this.vendor = vendor;
	}
	/**
	 * @return the vendor
	 */
	public String getVendor()
	{
		return vendor;
	}
	/**
	 * @param editor the editor to set
	 */
	public void setEditor(String editor)
	{
		this.editor = editor;
	}
	/**
	 * @return the editor
	 */
	public String getEditor()
	{
		return editor;
	}
	/**
	 * @param userFlag the userFlag to set
	 */
	public void setUserFlag(String userFlag)
	{
		this.userFlag = userFlag;
	}
	/**
	 * @return the userFlag
	 */
	public String getUserFlag()
	{
		return userFlag;
	}
	/**
	 * @param logo the logo to set
	 */
	public void setLogo(byte[] logo)
	{
		this.logo = logo;
	}
	/**
	 * @return the logo
	 */
	public byte[] getLogo()
	{
		return logo;
	}
	public void setPoiImage(String[] poiImage) {
		this.poiImage = poiImage;
	}

	public String[] getPoiImage() {
		return poiImage;
	}

	public void setCreatedTime(Calendar createdTime) {
		this.createdTime = createdTime;
	}

	public Calendar getCreatedTime() {
		return createdTime;
	}

	public void setUpdatedTime(Calendar updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Calendar getUpdatedTime() {
		return updatedTime;
	}

	/**
	 * @param isNavigable the isNavigable to set
	 */
	public void setNavigable(boolean isNavigable)
	{
		this.isNavigable = isNavigable;
	}
	/**
	 * @return the isNavigable
	 */
	public boolean isNavigable()
	{
		return isNavigable;
	}
	/**
	 * @param isRatingEnable the isRatingEnable to set
	 */
	public void setRatingEnable(boolean isRatingEnable)
	{
		this.isRatingEnable = isRatingEnable;
	}
	/**
	 * @return the isRatingEnable
	 */
	public boolean isRatingEnable()
	{
		return isRatingEnable;
	}

	/**
	 * @param reviewSummary the reviewSummary to set
	 */
	public void setReviewSummary(TnPoiReviewSummary reviewSummary)
	{
		this.reviewSummary = reviewSummary;
	}
	/**
	 * @return the reviewSummary
	 */
	public TnPoiReviewSummary getReviewSummary()
	{
		return reviewSummary;
	}
	/**
	 * @param isUgPoi the isUgPoi to set
	 */
	public void setUgPoi(boolean isUgPoi)
	{
		this.isUgPoi = isUgPoi;
	}
	/**
	 * @return the isUgPoi
	 */
	public boolean isUgPoi()
	{
		return isUgPoi;
	}
	/**
	 * @param flagInfo the flagInfo to set
	 */
	public void setFlagInfo(String flagInfo)
	{
		this.flagInfo = flagInfo;
	}
	/**
	 * @return the flagInfo
	 */
	public String getFlagInfo()
	{
		return flagInfo;
	}

	/**
	 * @param ad the ad to set
	 */
	public void setAd(Advertisement ad)
	{
		this.ad = ad;
	}
	/**
	 * @return the ad
	 */
	public Advertisement getAd()
	{
		return ad;
	}
	/**
	 * @param coupon the coupon to set
	 */
	public void setCouponList(List<Coupon> coupon)
	{
		this.couponList = coupon;
	}
	/**
	 * @return the coupon
	 */
	public List<Coupon> getCouponList()
	{
		return couponList;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("poiId=");
		sb.append(this.poiId);
		sb.append(", basePoiId=");
		sb.append(this.basePoiId);
		sb.append(", brandName=");
		sb.append(this.brandName);
		sb.append(", poiType=");
		sb.append(this.poiType);
		sb.append(", vendor=");
		sb.append(this.vendor);
		sb.append(", editor=");
		sb.append(this.editor);
		sb.append(", isNavigable=");
		sb.append(this.isNavigable);
		sb.append(", Address=");
		sb.append(this.address);
		sb.append(", coupon=");
		sb.append(this.couponList);
		sb.append(", ad.=");
		sb.append(this.ad);
		sb.append(", distanceInMeter=");
		sb.append(this.distanceInMeter);
		sb.append(", distanceToUserInMeter=");
		sb.append(this.distanceToUserInMeter);
		sb.append(", phoneNumber=");
		sb.append(this.phoneNumber);
		sb.append(", priceRange=");
		sb.append(this.priceRange);
		sb.append(", review summary=");
		sb.append(this.reviewSummary);
		sb.append(", GasPriceInfo=");
		sb.append(this.getGasPriceInfos());
		if(poiExtraInfo.size() > 0)
		{
			sb.append(",extraInfo=");
			Iterator<String> it = poiExtraInfo.keySet().iterator();
			while(it.hasNext())
			{
				String key = it.next();String value = poiExtraInfo.get(key);
				sb.append("key:"+key+"|value:"+value+",");
			}
		}
		
		return sb.toString();
	}
	/**
	 * @param distanceInMeter the distanceInMeter to set
	 */
	public void setDistanceInMeter(double distanceInMeter)
	{
		this.distanceInMeter = distanceInMeter;
	}
	/**
	 * @return the distanceInMeter
	 */
	public double getDistanceInMeter()
	{
		return distanceInMeter;
	}
	/**
	 * @param gasPriceInfos the gasPriceInfos to set
	 */
	public void setGasPriceInfos(List<GasPriceInfo> gasPriceInfos)
	{
		this.gasPriceInfos = gasPriceInfos;
	}
	/**
	 * @return the gasPriceInfos
	 */
	public List<GasPriceInfo> getGasPriceInfos()
	{
		return gasPriceInfos;
	}
	/**
	 * @param categoryLabel the categoryLabel to set
	 */
	public void setCategoryLabel(String categoryLabel)
	{
		this.categoryLabel = categoryLabel;
	}
	/**
	 * @return the categoryLabel
	 */
	public String getCategoryLabel()
	{
		return categoryLabel;
	}
	/**
	 * @param menu the menu to set
	 */
	public void setMenu(String menu)
	{
		this.menu = menu;
	}
	/**
	 * @return the menu
	 */
	public String getMenu()
	{
		return menu;
	}
	/**
	 * @param businessHour the businessHour to set
	 */
	public void setBusinessHour(String businessHour)
	{
		this.businessHour = businessHour;
	}
	/**
	 * @return the businessHour
	 */
	public String getBusinessHour()
	{
		return businessHour;
	}
	/**
	 * @param priceRange the priceRange to set
	 */
	public void setPriceRange(double priceRange)
	{
		this.priceRange = priceRange;
	}
	/**
	 * @return the priceRange
	 */
	public double getPriceRange()
	{
		return priceRange;
	}
	/**
	 * 
	 * @return OpenTableInfo 
	 */
	public OpenTableInfo getOpenTableInfo()
	{
		return openTableInfo;
	}
    /**
     * 
     * @param openTableInfo
     */
	public void setOpenTableInfo(OpenTableInfo openTableInfo)
	{
		this.openTableInfo = openTableInfo;
	}

	public double getDistanceToUserInMeter() {
		return distanceToUserInMeter;
	}

	public void setDistanceToUserInMeter(double distanceToUserInMeter) {
		this.distanceToUserInMeter = distanceToUserInMeter;
	}
	
	public Map<String, String> getPoiExtraInfo() {
		return poiExtraInfo;
	}

	public void setPoiExtraInfo(Map<String, String> poiExtraInfo) {
		this.poiExtraInfo = poiExtraInfo;
	}
}
