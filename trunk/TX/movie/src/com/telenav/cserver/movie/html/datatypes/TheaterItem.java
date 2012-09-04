package com.telenav.cserver.movie.html.datatypes;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.telenav.j2me.datatypes.Stop;

public class TheaterItem {
	private long id;
	private String name;
	private Stop address;
	private String phoneNo;
	private String vendor;
	private String distance;
	private String distanceUnit;
	
	//schedule and movieList is only used when search from movie
	private ScheduleItem scheduleItem;
	private List<MovieItem> movieList = new ArrayList<MovieItem>();
    
	private String 	addressDisplay;//address detail
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Stop getAddress() {
		return address;
	}
	public void setAddress(Stop address) {
		this.address = address;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getDistanceUnit() {
		return distanceUnit;
	}
	public void setDistanceUnit(String distanceUnit) {
		this.distanceUnit = distanceUnit;
	}

	public List<MovieItem> getMovieList() {
		return movieList;
	}
	public void setMovieList(List<MovieItem> movieList) {
		this.movieList = movieList;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public ScheduleItem getScheduleItem() {
		return scheduleItem;
	}
	public void setScheduleItem(ScheduleItem scheduleItem) {
		this.scheduleItem = scheduleItem;
	}
	public String getAddressDisplay() {
		return addressDisplay;
	}
	public void setAddressDisplay(String addressDisplay) {
		this.addressDisplay = addressDisplay;
	}
}
