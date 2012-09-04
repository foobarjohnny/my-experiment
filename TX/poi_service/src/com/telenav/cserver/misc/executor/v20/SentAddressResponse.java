/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.misc.executor.v20;

import java.util.List;

import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.misc.struts.datatype.Address;

/**
 * @author pzhang
 *
 * @version 1.0, 2009-5-25
 * copy and update by xfliu at 2011/12/6
 */
public class SentAddressResponse extends ExecutorResponse{
    private List<Address> addressList = null;
    private String path = "";


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    public String toString() {
        StringBuffer addressBuffer = new StringBuffer("");
    	if(addressList!=null && addressList.size()!=0){
			for(Address address:addressList){
				if("".equals(addressBuffer)){
					addressBuffer.append(address.toString());
				}else{
					addressBuffer.append(", " + address.toString());
				}
			}
		}
		return "[addressList] = " + addressBuffer.toString() + "; [path] = " + path;
	}
}
