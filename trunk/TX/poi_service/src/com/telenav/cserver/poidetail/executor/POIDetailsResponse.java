package com.telenav.cserver.poidetail.executor;

import com.telenav.cserver.framework.executor.ExecutorResponse;

public class POIDetailsResponse extends ExecutorResponse 
{
	private long poiId;
	private String businessHours;
	private String businessHoursNote;
	private String description;
	private String priceRange;
	private String olsonTimezone;
	private String logoId;
	private String mediaServerKey;
	
	public long getPoiId() {
		return poiId;
	}
	public void setPoiId(long poiId) {
		this.poiId = poiId;
	}
	public String getBusinessHours() {
		return businessHours;
	}
	public void setBusinessHours(String businessHours) {
		this.businessHours = businessHours;
	}
	public String getBusinessHoursNote() {
		return businessHoursNote;
	}
	public void setBusinessHoursNote(String businessHoursNote) {
		this.businessHoursNote = businessHoursNote;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPriceRange() {
		return priceRange;
	}
	public void setPriceRange(String priceRange) {
		this.priceRange = priceRange;
	}
	public String getOlsonTimezone() {
		return olsonTimezone;
	}
	public void setOlsonTimezone(String olsonTimezone) {
		this.olsonTimezone = olsonTimezone;
	}
	public String getLogoId() {
		return logoId;
	}
	public void setLogoId(String logoId) {
		this.logoId = logoId;
	}
	public String getMediaServerKey() {
		return mediaServerKey;
	}
	public void setMediaServerKey(String mediaServerKey) {
		this.mediaServerKey = mediaServerKey;
	}
}
