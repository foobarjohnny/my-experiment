package com.telenav.cserver.backend.cose;

public class PoiDetailsResponse 
{
	private long poiId;
	private String businessHours;
	private String businessHoursNote;
	private String description;
	private String priceRange;
	private String olsonTimezone;
	private String logoId;
	private String mediaServerKey;
	private int status;
	private String poiLogo;
	private String brandLogo;
	private String categoryLogo;
	private boolean hasPoiMenu;
	private boolean hasPoiExtraAttributes;
	private boolean isRatingEnabled;
    
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
    public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setPoiLogo(String poiLogo) {
		this.poiLogo = poiLogo;
	}
	public String getPoiLogo() {
		return poiLogo;
	}
	public void setBrandLogo(String brandLogo) {
		this.brandLogo = brandLogo;
	}
	public String getBrandLogo() {
		return brandLogo;
	}
	public void setCategoryLogo(String categoryLogo) {
		this.categoryLogo = categoryLogo;
	}
	public String getCategoryLogo() {
		return categoryLogo;
	}
	public void setHasPoiMenu(boolean hasPoiMenu) {
		this.hasPoiMenu = hasPoiMenu;
	}
	public boolean isHasPoiMenu() {
		return hasPoiMenu;
	}
	public void setHasPoiExtraAttributes(boolean hasPoiExtraAttributes) {
		this.hasPoiExtraAttributes = hasPoiExtraAttributes;
	}
	public boolean isHasPoiExtraAttributes() {
		return hasPoiExtraAttributes;
	}
	public void setRatingEnabled(boolean isRatingEnabled) {
		this.isRatingEnabled = isRatingEnabled;
	}
	public boolean isRatingEnabled() {
		return isRatingEnabled;
	}
	
	public String toString()
	{
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("poiId:" + poiId).append(",businessHours:"+businessHours).append(",businessHoursNote:"+businessHoursNote)
	    .append(",description"+description).append(",priceRange:"+priceRange).append(",olsonTimezone:"+olsonTimezone).append(",logoId:"+logoId)
	    .append(",mediaServerKey:"+mediaServerKey).append(",status"+status);
	    return buffer.toString();
	}
}
