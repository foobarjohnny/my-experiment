package com.telenav.cserver.browser.datatype;

public class DeviceConfig {
	
	private String cachedKey = "";
	private boolean deviceLevelConfigExist = true;
	private boolean screenSizeLevelConfigExist = true;
	
	private String layoutKeyWithDevice = "";
	private String layoutKeyWithProduct = "";
	private String imageKeyWithoutSize = "";
	private String messageI18NKey = "";
	private String imageKey = "";
	//such as "Nexusone"
	private String realDeviceName = "";
	//such as "854x480_480x854"
	private String defaultDeviceName = "";
	//such as "800x480_480x800"
	private String closestDeviceName = "";

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\ncachedKey:");
		sb.append(cachedKey);
		sb.append("\nrealDeviceName:");
		sb.append(realDeviceName);
		sb.append("\ndefaultDeviceName:");
		sb.append(defaultDeviceName);
		sb.append("\nclosestDeviceName:");
		sb.append(closestDeviceName);
		sb.append("\ndeviceLevelConfigExist:");
		sb.append(deviceLevelConfigExist);
		sb.append("\nscreenSizeLevelConfigExist:");
		sb.append(screenSizeLevelConfigExist);
		sb.append("\nlayoutKeyWithDevice:");
		sb.append(layoutKeyWithDevice);
		sb.append("\nimageKeyWithoutSize:");
		sb.append(imageKeyWithoutSize);
		sb.append("\nmessageI18NKey:");
		sb.append(messageI18NKey);
		sb.append("\nlayoutKeyWithDevice:");
		sb.append(layoutKeyWithDevice);
		sb.append("\nlayoutKeyWithProduct:");
		sb.append(layoutKeyWithProduct);
		
		return sb.toString();
	}


	public String getLayoutKeyWithDevice() {
		return layoutKeyWithDevice;
	}

	public void setLayoutKeyWithDevice(String layoutKeyWithDevice) {
		this.layoutKeyWithDevice = layoutKeyWithDevice;
	}
	
	public String getLayoutKeyWithProduct() {
		return layoutKeyWithProduct;
	}

	public void setLayoutKeyWithProduct(String layoutKeyWithProduct) {
		this.layoutKeyWithProduct = layoutKeyWithProduct;
	}

	public String getImageKeyWithoutSize() {
		return imageKeyWithoutSize;
	}

	public void setImageKeyWithoutSize(String imageKeyWithoutSize) {
		this.imageKeyWithoutSize = imageKeyWithoutSize;
	}

	public String getMessageI18NKey() {
		return messageI18NKey;
	}

	public void setMessageI18NKey(String messageI18NKey) {
		this.messageI18NKey = messageI18NKey;
	}

	public String getImageKey() {
		return imageKey;
	}

	public void setImageKey(String imageKey) {
		this.imageKey = imageKey;
	}


	public boolean isDeviceLevelConfigExist() {
		return deviceLevelConfigExist;
	}


	public void setDeviceLevelConfigExist(boolean deviceLevelConfigExist) {
		this.deviceLevelConfigExist = deviceLevelConfigExist;
	}


	public boolean isScreenSizeLevelConfigExist() {
		return screenSizeLevelConfigExist;
	}


	public void setScreenSizeLevelConfigExist(boolean screenSizeLevelConfigExist) {
		this.screenSizeLevelConfigExist = screenSizeLevelConfigExist;
	}

	public boolean isUsingClosingResulotion()
	{
		boolean isUsing = false;
		if(!this.isDeviceLevelConfigExist() && !this.isScreenSizeLevelConfigExist())
		{
			isUsing = true;
		}
		
		return isUsing;
	}


	public String getRealDeviceName() {
		return realDeviceName;
	}


	public void setRealDeviceName(String realDeviceName) {
		this.realDeviceName = realDeviceName;
	}


	public String getClosestDeviceName() {
		return closestDeviceName;
	}


	public void setClosestDeviceName(String closestDeviceName) {
		this.closestDeviceName = closestDeviceName;
	}


	public String getDefaultDeviceName() {
		return defaultDeviceName;
	}


	public void setDefaultDeviceName(String defaultDeviceName) {
		this.defaultDeviceName = defaultDeviceName;
	}


	public String getCachedKey() {
		return cachedKey;
	}


	public void setCachedKey(String cachedKey) {
		this.cachedKey = cachedKey;
	}
}
