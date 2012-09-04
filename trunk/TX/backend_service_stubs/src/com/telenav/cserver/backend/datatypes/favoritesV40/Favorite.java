/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.favoritesV40;

import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.ws.datatypes.common.LifeCycle;

/**
 * 
 * @author zhjdou 2010-2-20
 */
public class Favorite {
	private long favoriteId;
	private long userId;
	private String senderName;
	private String senderPtn;
	private TnPoi favoritePoi;
	private Address favoriteAddress;
	private boolean isPOI=false;
	
	/*
	 * extended data field
	 */
	
	private String label;
	private String sourceFlag;
	private LifeCycle lifeCycle;
	private String favoriteType;
	private long eventId;
	

	/**
	 * @return the isPOI
	 */
	public boolean isPOI() {
		return isPOI;
	}

	/**
	 * @param isPOI the isPOI to set
	 */
	public void setPOI(boolean isPOI) {
		this.isPOI = isPOI;
	}

	/**
	 * @return the favoriteId
	 */
	public long getFavoriteId() {
		return favoriteId;
	}

	/**
	 * @param favoriteId
	 *            the favoriteId to set
	 */
	public void setFavoriteId(long favoriteId) {
		this.favoriteId = favoriteId;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return the favoritePoi
	 */
	public TnPoi getFavoritePoi() {
		return favoritePoi;
	}

	/**
	 * @param favoritePoi
	 *            the favoritePoi to set
	 */
	public void setFavoritePoi(TnPoi favoritePoi) {
		this.favoritePoi = favoritePoi;
	}

	/**
	 * @return the favoriteAddress
	 */
	public Address getFavoriteAddress() {
		return favoriteAddress;
	}

	/**
	 * @param favoriteAddress
	 *            the favoriteAddress to set
	 */
	public void setFavoriteAddress(Address favoriteAddress) {
		this.favoriteAddress = favoriteAddress;
	}

	/**
	 * @return the senderName
	 */
	public String getSenderName() {
		return senderName;
	}

	/**
	 * @param senderName
	 *            the senderName to set
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	/**
	 * @return the senderPtn
	 */
	public String getSenderPtn() {
		return senderPtn;
	}

	/**
	 * @param senderPtn
	 *            the senderPtn to set
	 */
	public void setSenderPtn(String senderPtn) {
		this.senderPtn = senderPtn;
	}

	/*
	 * extended methods
	 */
	
	public void setLabel(String label){
		this.label = label;
	}
	
	public String getLabel(){
		return this.label;
	}
	
	public void setSourceFlag(String sourceFlag){
		this.sourceFlag = sourceFlag;
	}
	
	public String getSourceFlag(){
		return this.sourceFlag;
	}
	
	public void setLifeCycle(LifeCycle lifeCycle){
		this.lifeCycle = lifeCycle;
	}
	
	public LifeCycle getLifeCycle(){
		return this.lifeCycle;
	}
	
	public void setFavoriteType(String favoriteType){
		this.favoriteType = favoriteType;
	}
	
	public String gettFavoriteType(){
		return this.favoriteType;
	}
	
	public void setEventId(long eventId){
		this.eventId = eventId;
	}
	
	public long getEventId(){
		return this.eventId;
	}
	

}
