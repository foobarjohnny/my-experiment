/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on May 8, 2009
 * File name: ValidateAddressResponse.java
 * Package name: com.telenav.cserver.ac.executor
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 5:13:58 PM
 *  Update:
 *******************************************************************************/
package com.telenav.cserver.ac.executor.v20;

import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.j2me.datatypes.Stop;

/**
 * @author dysong (dysong@telenav.cn) 5:13:58 PM, May 8, 2009
 * copy and update by xfliu at 2011/12/6
 */
public class ShareAddressResponse extends ExecutorResponse {
    private int status;
    private boolean isRGC = false;
    private Stop address;
    
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isRGC() {
        return isRGC;
    }

    public void setRGC(boolean isRGC) {
        this.isRGC = isRGC;
    }

    public Stop getAddress() {
        return address;
    }

    public void setAddress(Stop address) {
        this.address = address;
    }
    
    public String toString() {
		String addressStr = address != null ? address.toString() : "";
		return "[status] = " + status + "; [isRGC] = " + isRGC
				+ "; [address] = " + addressStr;
	}
}
