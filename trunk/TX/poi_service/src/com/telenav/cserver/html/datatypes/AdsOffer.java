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
public class AdsOffer {
	private String name;
	private String description;
	private String dealImage;
	public String getDealImage() {
		return dealImage;
	}
	public void setDealImage(String dealImage) {
		this.dealImage = dealImage;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
