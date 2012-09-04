package com.telenav.cserver.framework.html.datatype;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author panzhang
 * @version 1.0 2010-12-09
 */

public class HtmlDeviceConfig {
	
	private String cachedKey = "";
	//private boolean deviceLevelConfigExist = true;
	//private boolean screenSizeLevelConfigExist = true;
	
	private String layoutKey = "";
	private String layoutKeyWithDevice = "";
	//private String imageKeyWithoutSize = "";
	private String messageI18NKey = "";
	private String imageKey = "";
	//such as "Nexusone"
	private String realDeviceName = "";
	//such as "854x480_480x854"
	//private String defaultDeviceName = "";
	//such as "800x480_480x800"
	private String closestDeviceName = "";
	//the device name actually used
	//private String usedDeviceName = "";
	private String cssKey = "";
	//private String manifestKey = "";
	private String sharedImageKey="";
	private String sharedCssKey="";
	//
	private String cssDeviceCommonKey = "";
	private String sharedCssDeviceCommonKey = "";
	private String audioKey = "";
	//resource mapping
	private Map msgMapping = new HashMap();
	private Map sharedImageMapping = new HashMap();
	private Map sharedCssMapping = new HashMap();
	private Map specficMapping = new HashMap();
	private Map audioMapping = new HashMap();
	//private Map manifestMapping = new HashMap();
	private Map configMap = new HashMap();
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\ncachedKey:");
		sb.append(cachedKey);
		sb.append("\nrealDeviceName:");
		sb.append(realDeviceName);
		sb.append("\nclosestDeviceName:");
		sb.append(closestDeviceName);
		sb.append("\nlayoutKeyWithDevice:");
		sb.append(layoutKeyWithDevice);
		sb.append("\nmessageI18NKey:");
		sb.append(messageI18NKey);
		sb.append("\ncssKey:");
		sb.append(cssKey);	
		sb.append("\nsharedImageKey:");
		sb.append(sharedImageKey);	
		sb.append("\nsharedCssKey:");
		sb.append(sharedCssKey);
		sb.append("\ncssDeviceCommonKey:");
		sb.append(cssDeviceCommonKey);	
		sb.append("\nsharedCssDeviceCommonKey:");
		sb.append(sharedCssDeviceCommonKey);
		sb.append("\naudioKey:");
		sb.append(audioKey);
		return sb.toString();
	}


	public String getLayoutKeyWithDevice() {
		return layoutKeyWithDevice;
	}

	public void setLayoutKeyWithDevice(String layoutKeyWithDevice) {
		this.layoutKeyWithDevice = layoutKeyWithDevice;
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


	public String getCachedKey() {
		return cachedKey;
	}


	public void setCachedKey(String cachedKey) {
		this.cachedKey = cachedKey;
	}


	public String getCssKey() {
		return cssKey;
	}


	public void setCssKey(String cssKey) {
		this.cssKey = cssKey;
	}

	public String getSharedImageKey() {
		return sharedImageKey;
	}


	public void setSharedImageKey(String sharedImageKey) {
		this.sharedImageKey = sharedImageKey;
	}


	public Map getSharedImageMapping() {
		return sharedImageMapping;
	}


	public void setSharedImageMapping(Map sharedImageMapping) {
		this.sharedImageMapping = sharedImageMapping;
	}


	public String getSharedCssKey() {
		return sharedCssKey;
	}


	public void setSharedCssKey(String sharedCssKey) {
		this.sharedCssKey = sharedCssKey;
	}


	public Map getSharedCssMapping() {
		return sharedCssMapping;
	}


	public void setSharedCssMapping(Map sharedCssMapping) {
		this.sharedCssMapping = sharedCssMapping;
	}


	public Map getMsgMapping() {
		return msgMapping;
	}


	public void setMsgMapping(Map msgMapping) {
		this.msgMapping = msgMapping;
	}


	public Map getSpecficMapping() {
		return specficMapping;
	}


	public void setSpecficMapping(Map specficMapping) {
		this.specficMapping = specficMapping;
	}


	public Map getConfigMap() {
		return configMap;
	}


	public void setConfigMap(Map configMap) {
		this.configMap = configMap;
	}


	public String getCssDeviceCommonKey() {
		return cssDeviceCommonKey;
	}


	public void setCssDeviceCommonKey(String cssDeviceCommonKey) {
		this.cssDeviceCommonKey = cssDeviceCommonKey;
	}


	public String getSharedCssDeviceCommonKey() {
		return sharedCssDeviceCommonKey;
	}


	public void setSharedCssDeviceCommonKey(String sharedCssDeviceCommonKey) {
		this.sharedCssDeviceCommonKey = sharedCssDeviceCommonKey;
	}


	public String getLayoutKey() {
		return layoutKey;
	}


	public void setLayoutKey(String layoutKey) {
		this.layoutKey = layoutKey;
	}


	public Map getAudioMapping()
	{
		return audioMapping;
	}


	public void setAudioMapping(Map audioMapping)
	{
		this.audioMapping = audioMapping;
	}


	public String getAudioKey()
	{
		return audioKey;
	}


	public void setAudioKey(String audioKey)
	{
		this.audioKey = audioKey;
	}
}
