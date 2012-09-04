/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.xnav;

import java.util.List;

/**
 * The response aid to get trigger action for resend pin.
 * @author pzhang
 * 2010-08-03
 */
public class FetchBrandNamesResponse
{
    private String statusCode;
    private String statusMessage;
    private List<String> brandNames;

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("FetchBrandNamesResponse=[");
        sb.append(", statusCode=").append(this.statusCode);
        sb.append(", statusMessage=").append(this.statusMessage);
        sb.append("]");
        return sb.toString();
    }

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public List<String> getBrandNames() {
		return brandNames;
	}

	public void setBrandNames(List<String> brandNames) {
		this.brandNames = brandNames;
	}


}
