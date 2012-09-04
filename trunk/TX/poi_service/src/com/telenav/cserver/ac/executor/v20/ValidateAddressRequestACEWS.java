/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.ac.executor.v20;

import com.telenav.cserver.framework.executor.ExecutorRequest;

/**
 * ValidateAddressRequestACEWS.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-7
 * copy and update by xfliu at 2011/12/6
 */
public class ValidateAddressRequestACEWS extends ExecutorRequest
{
	private String firstLine;
	private String lastLine;
	private String street1;
	private String street2;
	private String city;
	private String state;
	private String country;
	private String zip;
	private String label;
	private boolean maitai;
	private String transactionId;
	private String addressSearchId;
	private String county;
	private String door;
	private String neighborhood;
	private String suite;
	private String sublocality;
	private String locality;
	private String locale;
	private String subStreet;
	private String buildingName;
	private String addressId;
	private String cityCountyOrPostalCode;
	
	public String getSuite() {
		return suite;
	}
	public void setSuite(String suite) {
		this.suite = suite;
	}
	public String getSublocality() {
		return sublocality;
	}
	public void setSublocality(String sublocality) {
		this.sublocality = sublocality;
	}
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getSubStreet() {
		return subStreet;
	}
	public void setSubStreet(String subStreet) {
		this.subStreet = subStreet;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	public String getNeighborhood() {
		return neighborhood;
	}
	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}
	public String getCityCountyOrPostalCode() {
		return cityCountyOrPostalCode;
	}
	public void setCityCountyOrPostalCode(String cityCountyOrPostalCode) {
		this.cityCountyOrPostalCode = cityCountyOrPostalCode;
	}
	public String getDoor() {
		return door;
	}
	public void setDoor(String door) {
		this.door = door;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getAddressSearchId() {
		return addressSearchId;
	}
	public void setAddressSearchId(String addressSearchId) {
		this.addressSearchId = addressSearchId;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	/**
	 * @param firstLine the firstLine to set
	 */
	public void setFirstLine(String firstLine)
	{
		this.firstLine = firstLine;
	}
	/**
	 * @return the firstLine
	 */
	public String getFirstLine()
	{
		return firstLine;
	}
	/**
	 * @param lastLine the lastLine to set
	 */
	public void setLastLine(String lastLine)
	{
		this.lastLine = lastLine;
	}
	/**
	 * @return the lastLine
	 */
	public String getLastLine()
	{
		return lastLine;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country)
	{
		this.country = country;
	}
	/**
	 * @return the country
	 */
	public String getCountry()
	{
		return country;
	}
	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip)
	{
		this.zip = zip;
	}
	/**
	 * @return the zip
	 */
	public String getZip()
	{
		return zip;
	}
	/**
	 * @param street1 the street1 to set
	 */
	public void setStreet1(String street1)
	{
		this.street1 = street1;
	}
	/**
	 * @return the street1
	 */
	public String getStreet1()
	{
		return street1;
	}
	/**
	 * @param street2 the street2 to set
	 */
	public void setStreet2(String street2)
	{
		this.street2 = street2;
	}
	/**
	 * @return the street2
	 */
	public String getStreet2()
	{
		return street2;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city)
	{
		this.city = city;
	}
	/**
	 * @return the city
	 */
	public String getCity()
	{
		return city;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state)
	{
		this.state = state;
	}
	/**
	 * @return the state
	 */
	public String getState()
	{
		return state;
	}
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public boolean isMaitai() {
        return maitai;
    }
    public void setMaitai(boolean maitai) {
        this.maitai = maitai;
    }
}
