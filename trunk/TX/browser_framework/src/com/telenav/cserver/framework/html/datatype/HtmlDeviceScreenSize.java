package com.telenav.cserver.framework.html.datatype;

/**
 * 
 * @author panzhang
 * @version 1.0 2010-12-09
 */
public class HtmlDeviceScreenSize implements Comparable<HtmlDeviceScreenSize>{
	private String deviceName;
	private int width;
	private int height;
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
    /**
     * 
     * 800x480_480x800
     * first get 800x480
     * then set the min value as width, it will be 480
     * and set the max value as height, it will be 800
     * @param deviceName
     * @param deviceName
     * @return
     */
	public void setDeviceScreenSize()
	{
		String[] screenSize = this.getDeviceName().split("_");
    	String[] width = screenSize[0].split("x");
    	
    	int width1 = Integer.parseInt(width[0]);
    	int width2 = Integer.parseInt(width[1]);
    	int screenWith = Math.min(width1, width2);
    	int screenHeight = Math.max(width1, width2);

    	this.setWidth(screenWith);
    	this.setHeight(screenHeight);
	}

	/**
	 * 
	 */
	public int compareTo(HtmlDeviceScreenSize o2) {
		int result = 0;
		if(this.getWidth() >= o2.getWidth() && this.getHeight() >= o2.getHeight())
		{
			result = 1;
		}
		else if(this.getWidth() < o2.getWidth() && this.getHeight() < o2.getHeight())
		{
			result = -1;
		}
		return result;
	}
}
