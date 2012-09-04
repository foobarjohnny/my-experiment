 /**
     * (c) Copyright 2009 TeleNav.
     *  All Rights Reserved.
     */
package com.telenav.cserver.backend.addresssharing;

import com.telenav.cserver.backend.datatypes.TnPoi;

/**
 * The request aid to get trigger action for share address.
 * @author pzhang
 * 2010-08-03
 */
public class AddressSharingRequestV20 extends AddressSharingRequest
{   
	private TnPoi tnPoi;

	public void setTnPoi(TnPoi tnPoi) {
		this.tnPoi = tnPoi;
	}

	public TnPoi getTnPoi() {
		return tnPoi;
	}
   
   
}
