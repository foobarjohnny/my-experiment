/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.recentstop;
/**
 * The construction of RecentPoiProx is similar with POI and the subclass recent POI
 * 
 * @author zhjdou
 * 
 */
public class RecentPoi
{

    // POI rating currently
    private double poiRating;

    // POI rating on current user
    private int poiRatingOfCurrentUser;
    
    private long id;

    private String name;// poi name useless

    private String brandName;// brand name

    private String label;
    //suppport information
    private String message; 

    private String phoneNumber;

    private String faxNumber;

    private String email;
    
    private String priceRange;
    
    private String otherDescription;
    
    private com.telenav.cserver.backend.datatypes.recentstop.RecentAddress address;

    private String imageUrl;// watch out the image url

    private String wapUrl;///

    private String webUrl;//

    private String hours;//

    private String acceptedPaymentMethods;///

    private byte[] logo;//
    
    /**
     * @return the poiRating
     */
    public double getPoiRating()
    {
        return poiRating;
    }

    /**
     * @param poiRating the poiRating to set
     */
    public void setPoiRating(double poiRating)
    {
        this.poiRating = poiRating;
    }

    /**
     * @return the poiRatingOfCurrentUser
     */
    public int getPoiRatingOfCurrentUser()
    {
        return poiRatingOfCurrentUser;
    }

    /**
     * @param poiRatingOfCurrentUser the poiRatingOfCurrentUser to set
     */
    public void setPoiRatingOfCurrentUser(int poiRatingOfCurrentUser)
    {
        this.poiRatingOfCurrentUser = poiRatingOfCurrentUser;
    }

   /**
     * @return the id
     */
    public long getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id)
    {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the brandName
     */
    public String getBrandName()
    {
        return brandName;
    }

    /**
     * @param brandName the brandName to set
     */
    public void setBrandName(String brandName)
    {
        this.brandName = brandName;
    }

    /**
     * @return the address
     */
    public RecentAddress getAddress()
    {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(RecentAddress address)
    {
        this.address = address;
    }

    /**
     * @return the label
     */
    public String getLabel()
    {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label)
    {
        this.label = label;
    }

    /**
     * @return the message
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message)
    {
        this.message = message;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the faxNumber
     */
    public String getFaxNumber()
    {
        return faxNumber;
    }

    /**
     * @param faxNumber the faxNumber to set
     */
    public void setFaxNumber(String faxNumber)
    {
        this.faxNumber = faxNumber;
    }

    /**
     * @return the email
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * @return the imageUrl
     */
    public String getImageUrl()
    {
        return imageUrl;
    }

    /**
     * @param imageUrl the imageUrl to set
     */
    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    /**
     * @return the wapUrl
     */
    public String getWapUrl()
    {
        return wapUrl;
    }

    /**
     * @param wapUrl the wapUrl to set
     */
    public void setWapUrl(String wapUrl)
    {
        this.wapUrl = wapUrl;
    }

    /**
     * @return the webUrl
     */
    public String getWebUrl()
    {
        return webUrl;
    }

    /**
     * @param webUrl the webUrl to set
     */
    public void setWebUrl(String webUrl)
    {
        this.webUrl = webUrl;
    }

    /**
     * @return the hours
     */
    public String getHours()
    {
        return hours;
    }

    /**
     * @param hours the hours to set
     */
    public void setHours(String hours)
    {
        this.hours = hours;
    }

    /**
     * @return the acceptedPaymentMethods
     */
    public String getAcceptedPaymentMethods()
    {
        return acceptedPaymentMethods;
    }

    /**
     * @param acceptedPaymentMethods the acceptedPaymentMethods to set
     */
    public void setAcceptedPaymentMethods(String acceptedPaymentMethods)
    {
        this.acceptedPaymentMethods = acceptedPaymentMethods;
    }

    /**
     * @return the priceRange
     */
    public String getPriceRange()
    {
        return priceRange;
    }

    /**
     * @param priceRange the priceRange to set
     */
    public void setPriceRange(String priceRange)
    {
        this.priceRange = priceRange;
    }

    /**
     * @return the logo
     */
    public byte[] getLogo()
    {
        return logo;
    }

    /**
     * @param logo the logo to set
     */
    public void setLogo(byte[] logo)
    {
        this.logo = logo;
    }

    /**
     * @return the otherDescription
     */
    public String getOtherDescription()
    {
        return otherDescription;
    }

    /**
     * @param otherDescription the otherDescription to set
     */
    public void setOtherDescription(String otherDescription)
    {
        this.otherDescription = otherDescription;
    }
    
    /**
     * override toString
     */
    public String toString() {
        StringBuffer bf=new StringBuffer();
        bf.append("recent poi[");
        bf.append("id=");
        bf.append(this.id);
        bf.append(", name=");
        bf.append(this.name);
        bf.append(", brandName=");
        bf.append(this.brandName);
        bf.append(", poiRating=");
        bf.append(this.poiRating);
        bf.append(", poiRatingOfCurrentUser=");
        bf.append(this.poiRatingOfCurrentUser);
        bf.append(", label=");
        bf.append(this.label);
        bf.append(", message=");
        bf.append(this.message);
        bf.append(", phoneNumber=");
        bf.append(this.phoneNumber);
        bf.append(", faxNumber=");
        bf.append(this.faxNumber);
        bf.append(", email=");
        bf.append(this.email);
        bf.append(", otherDescription=");
        bf.append(this.otherDescription);
        bf.append(", address=");
        bf.append(this.address);
        bf.append("]");
        return bf.toString();
    }
}