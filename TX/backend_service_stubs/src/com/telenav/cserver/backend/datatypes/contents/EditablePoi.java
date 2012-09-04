/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.contents;

import com.telenav.cserver.backend.datatypes.contents.Stop;

;

/**
 * <p>
 * About the difference between POIid and basePOIid
 * </p>
 * poiId is the unique identifier of a POI. The poiId means the same thing as it always did. basePoiId is a link to an
 * original poi. When a poi is edited, it is given a new, unique poiId, and also is given a basePoiId which represents
 * the original POI on which the revision is based. An original poi (vendor poi, or a poi added by a user) can be
 * considered to have poiId == basePoiId.<br>
 * For example, we have vendor poi with poiId 1234. It's basePoiId is 1234 also.<br>
 * <ul>
 * <li>User A edits poi 1234. A new poi with poiId 2374 is created. Its basePoiId is 1234 since it is based on the
 * original poi with poiId 1234. <br>
 * <li>User B edits poi 2374. A new poi with poiId 7364 is created. Its basePoiId is 1234 since it is based on the
 * original poi with poiId 1234. <br>
 * <li>User C adds poi 8273. It's basePoiId is 8273, since it is a new poi. <br>
 * <li>User D edits poi 8273. A new poi with poiId 8438 is created. Its basePoiId is 8273 since it is based on the
 * original poi with poiId 8273.
 * <ul>
 * 
 * @author zhjdou 2009-07-14
 * 
 */
public class EditablePoi
{
    private long poiId;

    private long userId;

    private Stop address;

    private String brandName;

    private String phone;

    private double priceRange ;

    private String businessHours;

    private float rating;

    // review content
    private String review;

    // review author
    private String reviewerName;

    /**
     * @return the poiId
     */
    public long getPoiId()
    {
        return poiId;
    }

    /**
     * @param poiId the poiId to set
     */
    public void setPoiId(long poiId)
    {
        this.poiId = poiId;
    }

    /**
     * @return the userId
     */
    public long getUserId()
    {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    /**
     * @return the address
     */
    public Stop getAddress()
    {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(Stop address)
    {
        this.address = address;
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
     * @return the phone
     */
    public String getPhone()
    {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    /**
     * @return the priceRange
     */
    public double getPriceRange()
    {
        return priceRange;
    }

    /**
     * @param priceRange the priceRange to set
     */
    public void setPriceRange(double priceRange)
    {
        this.priceRange = priceRange;
    }

    /**
     * @return the bnusinessHours
     */
    public String getBusinessHours()
    {
        return businessHours;
    }

    /**
     * @param bnusinessHours the bnusinessHours to set
     */
    public void setBusinessHours(String businessHours)
    {
        this.businessHours = businessHours;
    }

    /**
     * @return the rating
     */
    public float getRating()
    {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(float rating)
    {
        this.rating = rating;
    }

    /**
     * @return the review
     */
    public String getReview()
    {
        return review;
    }

    /**
     * @param review the review to set
     */
    public void setReview(String review)
    {
        this.review = review;
    }

    /**
     * @return the reviewerName
     */
    public String getReviewerName()
    {
        return reviewerName;
    }

    /**
     * @param reviewerName the reviewerName to set
     */
    public void setReviewerName(String reviewerName)
    {
        this.reviewerName = reviewerName;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("EditablePoi[");
        sb.append("poiId=");
        sb.append(this.poiId);
        sb.append(", userId=");
        sb.append(this.userId);
        sb.append(", brandName=");
        sb.append(this.brandName);
        sb.append(", phone=");
        sb.append(this.phone);
        sb.append(", priceRange=");
        sb.append(this.priceRange);
        sb.append(", businessHours");
        sb.append(this.businessHours);
        sb.append(", rating=");
        sb.append(this.rating);
        sb.append(", review=");
        sb.append(this.review);
        sb.append(", reviewerName=");
        sb.append(this.reviewerName);
        sb.append(",address=");
        if (address != null)
        {
            sb.append(this.address.toString());
        }
        return sb.toString();
    }
}
