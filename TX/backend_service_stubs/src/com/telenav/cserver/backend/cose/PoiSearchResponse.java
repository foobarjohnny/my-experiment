/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.cose;

import java.util.List;

import com.telenav.cserver.backend.datatypes.TnPoi;

/**
 * PoiSearchResponse.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-10
 */
public class PoiSearchResponse
{
	private int poiSearchType;
	private int totalMatchedPoiCount;
	private List<TnPoi> pois;
	private List<TnPoi> sponsorPois;
	//TODO:[HB] not used?
	//private List<Address> geoCodedAddresses;
	private int poiSearchStatus;
	
	public String toString() {
	    StringBuilder sb=new StringBuilder();
	    sb.append("PoiSearchResponse=[");
	    sb.append("poiSearchType=");
	    sb.append(this.poiSearchType);
	    sb.append(", totalMatchedPoiCount=");
	    sb.append(this.totalMatchedPoiCount);
	    sb.append(", poiSearchStatus=");
	    sb.append(this.poiSearchStatus);
	    sb.append(", pois=");
	    if(this.pois!=null) {
	        for(int i=0;i<this.pois.size();i++) {
	            sb.append("\n");
	            sb.append(this.pois.get(i).toString());
	        }
	    }
	    sb.append(", sponsorPois=");
        if(this.sponsorPois!=null) {
            for(int j=0;j<this.sponsorPois.size();j++) {
                sb.append("\n");
                sb.append(this.sponsorPois.get(j).toString());
            }
        }
	    return sb.toString();
	}
	
	/**
	 * @param poiSearchType the poiSearchType to set
	 */
	public void setPoiSearchType(int poiSearchType)
	{
		this.poiSearchType = poiSearchType;
	}
	/**
	 * @return the poiSearchType
	 */
	public int getPoiSearchType()
	{
		return poiSearchType;
	}
	/**
	 * @param totalMatchedPoiCount the totalMatchedPoiCount to set
	 */
	public void setTotalMatchedPoiCount(int totalMatchedPoiCount)
	{
		this.totalMatchedPoiCount = totalMatchedPoiCount;
	}
	/**
	 * @return the totalMatchedPoiCount
	 */
	public int getTotalMatchedPoiCount()
	{
		return totalMatchedPoiCount;
	}

	/**
	 * @param poiSearchStatus the poiSearchStatus to set
	 */
	public void setPoiSearchStatus(int poiSearchStatus)
	{
		this.poiSearchStatus = poiSearchStatus;
	}
	/**
	 * @return the poiSearchStatus
	 */
	public int getPoiSearchStatus()
	{
		return poiSearchStatus;
	}
	/**
	 * @param pois the pois to set
	 */
	public void setPois(List<TnPoi> pois)
	{
		this.pois = pois;
	}
	/**
	 * @return the pois
	 */
	public List<TnPoi> getPois()
	{
		return pois;
	}
	/**
	 * @param sponsorPois the sponsorPois to set
	 */
	public void setSponsorPois(List<TnPoi> sponsorPois)
	{
		this.sponsorPois = sponsorPois;
	}
	/**
	 * @return the sponsorPois
	 */
	public List<TnPoi> getSponsorPois()
	{
		return sponsorPois;
	}
	
}
