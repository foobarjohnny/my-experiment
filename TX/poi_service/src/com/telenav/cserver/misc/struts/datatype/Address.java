/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.misc.struts.datatype;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * @author pzhang
 *
 * @version 1.0, 2009-2-27
 */
public class Address implements Serializable{
	/**
	 * 
	 */
	private Logger logger = Logger.getLogger(Address.class);
	
	private static final long serialVersionUID = 6454488017592836742L;
	private String sentOn;
	private String sentAt;
	private long id;
	private Date createTime;
	private String label;
	private String street;
	private String city;
	private String province;
	private String postalCode;
	private String country;
	private String displayCityText;
	private List<String> receiverList;
	
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getSentOn() {
		return sentOn;
	}

	public void setSentOn(String sentOn) {
		this.sentOn = sentOn;
	}

	public String getSentAt() {
		return sentAt;
	}

	public void setSentAt(String sentAt) {
		this.sentAt = sentAt;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getDisplayCityText() {
		return displayCityText;
	}

	public void setDisplayCityText(String displayCityText) {
		this.displayCityText = displayCityText;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

    public List<String> getReceiverList() {
        return receiverList;
    }

    public void setReceiverList(List<String> receiverList) {
        this.receiverList = receiverList;
    }
    
    public JSONObject toJSON()
    {
        JSONObject jo = new JSONObject();
        try {
            jo.put("sentOn", this.getSentOn());
            jo.put("sentAt", this.getSentAt());
            jo.put("label", this.getLabel());
            jo.put("street", this.getStreet());
            jo.put("city", this.getDisplayCityText());
            jo.put("country", this.getCountry());
            jo.put("id", String.valueOf(this.getId()));
            
            JSONArray ja = new JSONArray();
            
            List<String> list = this.getReceiverList();
            if(list != null)
            {
                for(String s:list)
                {
                    ja.put(s);
                }
            }
            //jo.put("recipients",ja.toString());
            jo.put("recipients",ja);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
        	logger.error(" JSONException occured when convert address to Jsonobject");
            e.printStackTrace();
        }        
        return jo;
    }
}
