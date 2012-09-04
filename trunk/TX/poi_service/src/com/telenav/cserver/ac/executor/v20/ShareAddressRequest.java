/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on May 8, 2009
 * File name: ValidateAddressRequest.java
 * Package name: com.telenav.cserver.ac.executor
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 5:13:03 PM
 *  Update:
 *******************************************************************************/
package com.telenav.cserver.ac.executor.v20;

import java.util.List;

import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.backend.datatypes.addresssharing.ContactInfo;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.datatype.backend.telenavfinder.IPoi;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * @author pzhang (pzhang@telenav.cn) 5:13:03 PM, May 8, 2009
 * copy and update by xfliu at 2011/12/6
 */
public class ShareAddressRequest extends ExecutorRequest {

    private long userId;
    private String label;
    private List<String> groupList;
    private List<ContactInfo> contactList;
    private Stop address;
    private TnContext tnContext;
    private TnPoi poi;
    private boolean isRGC = false;
    private String senderPTN = "";
    
    
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    public Stop getAddress() {
        return address;
    }
    public void setAddress(Stop address) {
        this.address = address;
    }
    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public TnContext getTnContext() {
        return tnContext;
    }
    public void setTnContext(TnContext tnContext) {
        this.tnContext = tnContext;
    }
    public List<String> getGroupList() {
        return groupList;
    }
    public void setGroupList(List<String> groupList) {
        this.groupList = groupList;
    }

    public TnPoi getPoi() {
        return poi;
    }
    public void setPoi(TnPoi poi) {
        this.poi = poi;
    }
    public boolean isRGC() {
        return isRGC;
    }
    public void setRGC(boolean isRGC) {
        this.isRGC = isRGC;
    }
    
    public String toString() {
		String addressStr = address != null ? address.toString() : "";
		StringBuffer groupBuffer = new StringBuffer("");
		if (groupList != null && groupList.size() != 0) {
			for (String group : groupList) {
				if ("".equals(groupBuffer.toString())) {
					groupBuffer.append(group);
				} else {
					groupBuffer.append(", " + group);
				}
			}
		}
		StringBuffer contactBuffer = new StringBuffer("");
		if (contactList != null && contactList.size() != 0) {
			for (ContactInfo contact : contactList) {
				if ("".equals(contactBuffer.toString())) {
					contactBuffer.append(contact.getPtn());
				} else {
					contactBuffer.append(", " + contact.getPtn());
				}
			}
		}

		return "[userId] = " + userId + "; [label] = " + label
				+ "; [groupList] = " + groupBuffer.toString()
				+ "; [contactNumberList] = " + contactBuffer.toString()
				+ "; [address] = " + addressStr + "; [isRGC] = " + isRGC;
	}
	public String getSenderPTN() {
		return senderPTN;
	}
	public void setSenderPTN(String senderPTN) {
		this.senderPTN = senderPTN;
	}
	public List<ContactInfo> getContactList() {
		return contactList;
	}
	public void setContactList(List<ContactInfo> contactList) {
		this.contactList = contactList;
	}

}
