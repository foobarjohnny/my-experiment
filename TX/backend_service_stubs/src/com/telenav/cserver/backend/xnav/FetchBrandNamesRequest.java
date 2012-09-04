 /**
     * (c) Copyright 2009 TeleNav.
     *  All Rights Reserved.
     */
package com.telenav.cserver.backend.xnav;

/**
 * The request aid to get trigger action for resend pin.
 * @author pzhang
 * 2010-08-03
 */
public class FetchBrandNamesRequest
{   
	private String country;
	private int count;
    
   public String toString() {
       StringBuilder sb = new StringBuilder();
       sb.append("FetchBrandNamesRequest=[");
       sb.append(", country=").append(this.country);
       sb.append(", count=").append(this.count);
       sb.append("]");
       return sb.toString();
   }

public String getCountry() {
	return country;
}

public void setCountry(String country) {
	this.country = country;
}

public int getCount() {
	return count;
}

public void setCount(int count) {
	this.count = count;
}


   
}
