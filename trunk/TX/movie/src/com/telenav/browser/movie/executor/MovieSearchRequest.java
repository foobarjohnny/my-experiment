package com.telenav.browser.movie.executor;

import com.telenav.browser.movie.Constant;
import com.telenav.browser.movie.datatypes.Address;
import com.telenav.cserver.framework.executor.ExecutorRequest;

public class MovieSearchRequest extends ExecutorRequest{
	private String searchString;
	private boolean sortByName = true;
	private String newSortBy;
	private String searchDate;
	private int batchNumber = 1; // default value for ARM9000
	private int batchSize = 20; // default value for ARM9000
	private Address address;
	private int distanceUnit=Constant.DUNIT_MILES;
	
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	public boolean isSortByName() {
		return sortByName;
	}
	public void setSortByName(boolean sortByName) {
		this.sortByName = sortByName;
	}
	public String getNewSortBy() {
		return newSortBy;
	}
	public void setNewSortBy(String newSortBy) {
		this.newSortBy = newSortBy;
	}
	public String getSearchDate() {
		return searchDate;
	}
	public void setSearchDate(String searchDate) {
		this.searchDate = searchDate;
	}
	public int getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(int batchNumber) {
		this.batchNumber = batchNumber;
	}
	public int getBatchSize() {
		return batchSize;
	}
	
	public void setDistanceUnit(int dUnit) {
		this.distanceUnit = dUnit;
	}
	public int getDistanceUnit() {
		return distanceUnit;
	}
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public String toString() {
		String addressStr = address != null ? address.toString() : "";
		return "[searchString] = " + searchString + "; [sortByName] = "
				+ sortByName + "; [newSortBy] = " + newSortBy + "; [searchDate] = " + searchDate
				+ "; [batchNumber] = " + batchNumber + "; [batchSize] = "
				+ batchSize + "; [address] = " + addressStr
				+ "; [distanceUnit] = " + distanceUnit;
	}
}
