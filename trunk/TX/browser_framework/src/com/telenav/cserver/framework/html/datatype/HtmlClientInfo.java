package com.telenav.cserver.framework.html.datatype;

/**
 * 
 * @author panzhang
 * @version 1.0 2010-12-09
 */
public class HtmlClientInfo {
	private String programCode="";
	private String deviceCarrier = "";
	private String carrier="";
	private String platform="";
	private String version="";
	private String product="";
	private String device="";
	private String locale="";
	private String width="";
	private String height="";
	private String region="";
	private String userId="";
	private String buildNo="";
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\ncarrier:");
		sb.append(carrier);
		sb.append("\nprogramCode:");
		sb.append(programCode);
		sb.append("\nplatform:");
		sb.append(platform);
		sb.append("\nversion:");
		sb.append(version);
		sb.append("\nproduct:");
		sb.append(product);
		sb.append("\ndevice:");
		sb.append(device);
		sb.append("\nlocale:");
		sb.append(locale);
		sb.append("\nwidth:");
		sb.append(width);
		sb.append("\nheight:");
		sb.append(height);
		sb.append("\nregion:");
		sb.append(region);
		sb.append("\nuserId:");
		sb.append(userId);
		
		return sb.toString();
	}
	
	public String getCarrier() {
		return carrier;
	}
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getBuildNo() {
		return buildNo;
	}

	public void setBuildNo(String buildNo) {
		this.buildNo = buildNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProgramCode() {
		return programCode;
	}

	public void setProgramCode(String programCode) {
		this.programCode = programCode;
	}

	public String getDeviceCarrier() {
		return deviceCarrier;
	}

	public void setDeviceCarrier(String deviceCarrier) {
		this.deviceCarrier = deviceCarrier;
	}
	
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
}
