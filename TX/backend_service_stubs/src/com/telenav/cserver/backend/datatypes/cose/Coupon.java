/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.cose;

import java.util.Date;

/**
 * Coupon.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-13
 */
public class Coupon
{
	private String id;
	private String name;
	private String text;
	private String description;
	private String url;
	private double urlPPE;
	private Date startDate;
	private Date endDate;
	private byte[] image;
	/**
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}
	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text)
	{
		this.text = text;
	}
	/**
	 * @return the text
	 */
	public String getText()
	{
		return text;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url)
	{
		this.url = url;
	}
	/**
	 * @return the url
	 */
	public String getUrl()
	{
		return url;
	}
	/**
	 * @param urlPPE the urlPPE to set
	 */
	public void setUrlPPE(double urlPPE)
	{
		this.urlPPE = urlPPE;
	}
	/**
	 * @return the urlPPE
	 */
	public double getUrlPPE()
	{
		return urlPPE;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}
	/**
	 * @return the startDate
	 */
	public Date getStartDate()
	{
		return startDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate()
	{
		return endDate;
	}
	/**
	 * @param image the image to set
	 */
	public void setImage(byte[] image)
	{
		this.image = image;
	}
	/**
	 * @return the image
	 */
	public byte[] getImage()
	{
		return image;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Coupon[");
		sb.append("id=");
		sb.append(this.id);
		sb.append(", name=");
		sb.append(this.name);
		sb.append(", text=");
		sb.append(this.text);
		sb.append(", description=");
		sb.append(this.description);
		sb.append(", url=");
		sb.append(this.url);
		sb.append(", urlPPE=");
		sb.append(this.urlPPE);
		sb.append(", endDate=");
		sb.append(this.endDate);
		sb.append(", imageSize=");
		sb.append(this.image == null ? 0 : this.image.length);
		sb.append("]>");
		return sb.toString();
	}
}
