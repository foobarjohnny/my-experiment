 /**
     * (c) Copyright 2009 TeleNav.
     *  All Rights Reserved.
     */
package com.telenav.cserver.backend.addresssharing;

import java.util.List;

import com.telenav.cserver.backend.datatypes.addresssharing.ContactInfo;
import com.telenav.j2me.datatypes.Stop;

/**
 * The request aid to get trigger action for share address.
 * @author pzhang
 * 2010-08-03
 */
public class LocationSharingRequest
{   

	private long userId;
	private String ptn;
	private List<ContactInfo> contactList;
	private Stop address;
	private String brandName;
	private String movieName;
    private String userClient;
    private String userClientVersion;
    private String contextString;
    
   public String toString() {
       StringBuilder sb = new StringBuilder();
       sb.append("LocationSharingRequest=[");
       sb.append(", ptn=").append(this.ptn);
       sb.append(", userId=").append(this.userId);
       sb.append(", contactList=").append(this.contactList.toString());
       sb.append(", address=").append(this.address.toString());
       sb.append(", brandName=").append(this.brandName);
       sb.append(", movieName=").append(this.movieName);
       sb.append(", userClient=").append(this.userClient);
       sb.append(", userClientVersion=").append(this.userClientVersion);
       sb.append("]");
       return sb.toString();
   }

    /**
     * @return the userClient
     */
    public String getUserClient()
    {
        return userClient;
    }
    /**
     * @param userClient the userClient to set
     */
    public void setUserClient(String userClient)
    {
        this.userClient = userClient;
    }
    /**
     * @return the userClientVersion
     */
    public String getUserClientVersion()
    {
        return userClientVersion;
    }
    /**
     * @param userClientVersion the userClientVersion to set
     */
    public void setUserClientVersion(String userClientVersion)
    {
        this.userClientVersion = userClientVersion;
    }

	public String getPtn() {
		return ptn;
	}

	public void setPtn(String ptn) {
		this.ptn = ptn;
	}

	public String getContextString() {
		return contextString;
	}

	public void setContextString(String contextString) {
		this.contextString = contextString;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Stop getAddress() {
		return address;
	}

	public void setAddress(Stop address) {
		this.address = address;
	}

	public List<ContactInfo> getContactList() {
		return contactList;
	}

	public void setContactList(List<ContactInfo> contactList) {
		this.contactList = contactList;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
   
}
