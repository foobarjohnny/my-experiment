package com.telenav.cserver.backend.datatypes.addresssharing;

import java.util.Date;
import java.util.List;

public class SharedAddressItem {
	private long id;
	private Date createTime;
	private String label;
	private String street;
	private String city;
	private String province;
	private String postalCode;
	private String country;
	private String displayCityText;
	private List<ReceiverUserInfo> receiverList;
	
   public String toString() {
       StringBuilder sb = new StringBuilder();
       sb.append("SharedAddressItem=[");
       sb.append(", id=").append(this.id);
       sb.append(", label=").append(this.label);
       sb.append(", street=").append(this.street);
       sb.append(", city=").append(this.city);
       sb.append(", province=").append(this.province);
       sb.append(", postalCode=").append(this.postalCode);
       sb.append(", country=").append(this.country);
       for(ReceiverUserInfo info:receiverList)
       {
    	   sb.append(", receiver=").append(info.toString());
       }
       sb.append("]");
       return sb.toString();
   }
	   
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getDisplayCityText() {
		return displayCityText;
	}
	public void setDisplayCityText(String displayCityText) {
		this.displayCityText = displayCityText;
	}
	public List<ReceiverUserInfo> getReceiverList() {
		return receiverList;
	}
	public void setReceiverList(List<ReceiverUserInfo> receiverList) {
		this.receiverList = receiverList;
	}
}
