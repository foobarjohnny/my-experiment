/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.onebox.executor;

import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.poi.executor.POISearchResponse_WS;
import com.telenav.services.search.onebox.v10.QuerySuggestion;
import com.telenav.ws.datatypes.address.Address;

import java.util.List;

/**
 * Response type for One Box Search
 * 
 * @author weiw
 * @date Mar 25, 2010
 */

public class OneBoxResponse extends ExecutorResponse {

	public static final String STATUS_NOT_EXACT_MATCH = "not_exact_match";
	public static final String STATUS_EXACT_MATCH = "exact_match";
	private POISearchResponse_WS poiResp;
	private int resultType = -1;
	private List<Address> addressList;
	private QuerySuggestion[] suggestions;
	private String exactMatchStatus = STATUS_EXACT_MATCH;
	private String poiDetailUrl;

	/**
	 * @return the poiResp
	 */
	public POISearchResponse_WS getPoiResp() {
		return poiResp;
	}

	/**
	 * @param poiResp
	 *            the poiResp to set
	 */
	public void setPoiResp(POISearchResponse_WS poiResp) {
		this.poiResp = poiResp;
	}

	/**
	 * @return the resultType
	 */
	public int getResultType() {
		return resultType;
	}

	/**
	 * @param resultType
	 *            the resultType to set
	 */
	public void setResultType(int resultType) {
		this.resultType = resultType;
	}

	/**
	 * @return the addressList
	 */
	public List<Address> getAddressList() {
		return addressList;
	}

	/**
	 * @param addressList
	 *            the addressList to set
	 */
	public void setAddressList(List<Address> addressList) {
		this.addressList = addressList;
	}

	/**
	 * @return the suggestions
	 */
	public QuerySuggestion[] getSuggestions() {
		return suggestions;
	}

	/**
	 * @param suggestions the suggestions to set
	 */
	public void setSuggestions(QuerySuggestion[] suggestions) {
		this.suggestions = suggestions;
	}

	public String getExactMatchStatus() {
		return exactMatchStatus;
	}

	public void setExactMatchStatus(String exactMatchStatus) {
		this.exactMatchStatus = exactMatchStatus;
	}

    public String getPoiDetailUrl()
    {
        return poiDetailUrl;
    }

    public void setPoiDetailUrl(String poiDetailUrl)
    {
        this.poiDetailUrl = poiDetailUrl;
    }
	

}
