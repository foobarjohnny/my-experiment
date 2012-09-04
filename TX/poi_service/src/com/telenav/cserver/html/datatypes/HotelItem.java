/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.datatypes;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.telenav.j2me.datatypes.Stop;

/**
 * @TODO	define data type
 * @author  xljiang@telenav.cn
 * @version 1.0 
 */
public class HotelItem {

	private long poiId;
	private long partnerPoiId;
	private String name;
	private Stop stop;
	private String address;
	private String distance;
	private String phoneNumber;
	private String brandName;
	private String hotelType;
	private int starRating;
	private String logo;
	private List<Map.Entry<String, Integer>> photos;//we must avoid unnecessary duplicate items and sort them by display_order
	private String description;
	private int startPrice = 0;
	private String locationDesc;
	private Set<Integer> amenities;//we must avoid unnecessary duplicate items
	private int totalRooms;
	private int totalFloors;
	private String yearBuilt;
	private String yearOfLastRenovation;

	
	public long getPoiId() {
		return poiId;
	}

	public void setPoiId(long poiId) {
		this.poiId = poiId;
	}
	
	public long getPartnerPoiId() {
		return partnerPoiId;
	}

	public void setPartnerPoiId(long partnerPoiId) {
		this.partnerPoiId = partnerPoiId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Stop getStop() {
		return stop;
	}

	public void setStop(Stop stop) {
		this.stop = stop;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getHotelType() {
		return hotelType;
	}

	public void setHotelType(String hotelType) {
		this.hotelType = hotelType;
	}

	public int getStarRating() {
		return starRating;
	}

	public void setStarRating(int starRating) {
		this.starRating = starRating;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public List<Map.Entry<String, Integer>> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Map.Entry<String, Integer>> photos) {
		this.photos = photos;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(int startPrice) {
		this.startPrice = startPrice;
	}

	public String getLocationDesc() {
		return locationDesc;
	}

	public void setLocationDesc(String locationDesc) {
		this.locationDesc = locationDesc;
	}

	public int getTotalRooms() {
		return totalRooms;
	}

	public void setTotalRooms(int totalRooms) {
		this.totalRooms = totalRooms;
	}

	public int getTotalFloors() {
		return totalFloors;
	}

	public void setTotalFloors(int totalFloors) {
		this.totalFloors = totalFloors;
	}

	public String getYearBuilt() {
		return yearBuilt;
	}

	public void setYearBuilt(String yearBuilt) {
		this.yearBuilt = yearBuilt;
	}

	public String getYearOfLastRenovation() {
		return yearOfLastRenovation;
	}

	public void setYearOfLastRenovation(String yearOfLastRenovation) {
		this.yearOfLastRenovation = yearOfLastRenovation;
	}

	public Set<Integer> getAmenities() {
		return amenities;
	}

	public void setAmenities(Set<Integer> amenities) {
		this.amenities = amenities;
	}
}
