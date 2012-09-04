 /**
     * (c) Copyright 2009 TeleNav.
     *  All Rights Reserved.
     */
package com.telenav.cserver.backend.addresssharing;

import java.util.List;

import com.telenav.cserver.backend.datatypes.addresssharing.ContactInfo;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.datatype.backend.telenavfinder.IPoi;

/**
 * The request aid to get trigger action for share address.
 * @author pzhang
 * 2010-08-03
 */
public class AddressSharingRequest
{   
	private long userId;
	private String ptn;
	private List<ContactInfo> contactList;
	private Stop address;
    private String userClient;
    private String userClientVersion;
    private String contextString;
    private IPoi poi;
   

public String toString() {
       StringBuilder sb = new StringBuilder();
       sb.append("ResendPinRequest=[");
       sb.append(", ptn=").append(this.ptn);
       sb.append(", userId=").append(this.userId);
       sb.append(", contactList=").append(this.contactList);
       sb.append(", address=").append(this.address);
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
	
	public IPoi getPoi() {
		return poi;
	}

	public void setPoi(IPoi poi) {
		this.poi = poi;
	}
   
}
