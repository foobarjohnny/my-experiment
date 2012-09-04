/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.ac.executor;

import java.util.List;

import com.telenav.cserver.backend.ace.GeoCodeResponseStatus;
import com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress;
import com.telenav.cserver.framework.executor.ExecutorResponse;

/**
 * ValidateAddressResponseACEWS.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-7
 */
public class ValidateAddressResponseACEWS extends ExecutorResponse
{
    /** Validate Address response. */
    private long totalCount;
    private List<GeoCodedAddress> addresses;
    private String label;
    private boolean maitai;
	
	private int geoCodeStatusCode= GeoCodeResponseStatus.EXACT_FOUND; // Default
	
	public List<GeoCodedAddress> getAddresses() 
	{
		return addresses;
	}
	
	/**
     * @return the totalCount
     */
    public long getTotalCount() {
        return totalCount;
    }

    /**
     * @param totalCount the totalCount to set
     */
    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

	public void setAddressList(List<GeoCodedAddress> addresses) 
	{
		this.addresses = addresses;
	}

	public int getGeoCodeStatusCode() {
		return geoCodeStatusCode;
	}

	public void setGeoCodeStatusCode(int geoCodeStatusCode) {
		this.geoCodeStatusCode = geoCodeStatusCode;
	}

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isMaitai() {
        return maitai;
    }

    public void setMaitai(boolean maitai) {
        this.maitai = maitai;
    }

	
}
