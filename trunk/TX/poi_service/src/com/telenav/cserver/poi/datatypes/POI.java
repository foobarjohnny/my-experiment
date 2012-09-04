/**
 * chbZhang
 * Copy from com.telenav.j2me.datatypes.POI.java 
 * We use this class as a bean and we can change it as we want
 */
package com.telenav.cserver.poi.datatypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.telenav.cserver.backend.contents.GetReviewResponse;
import com.telenav.cserver.backend.datatypes.LocalAppInfo;
import com.telenav.cserver.backend.datatypes.cose.Advertisement;
import com.telenav.cserver.backend.datatypes.cose.Coupon;
import com.telenav.cserver.backend.datatypes.cose.OpenTableInfo;

public class POI 
{
    //member
    public BizPOI bizPoi = new BizPOI();        // old BizPoi
    public int avgRating;                       // average rating
 
    // Added by Radha - Do we need this apart from something called price
    public double priceRange=-1; // More expensive, cheap, les expensive, very cheap etc.
    
    public int popularity;                      // popularity points
    public boolean isRatingEnable;
    public boolean hasUserRatedThisPoi;
    public long poiId = 0;     //poiId must be long.
    
    //for DSR
    public byte[] poiNameAudio;
    
    
    // Added by Radha for UGC
      public List<RevHistory> revHistory=new ArrayList<RevHistory>();
      public List<Review> reviews = new ArrayList<Review>();
      public List<BusinessHour> hours = new ArrayList<BusinessHour>();
      public List<MenuEntry> menuEntries = new ArrayList<MenuEntry>();
      
      public String businessHourString;
      
      //Add by HuBo
      public long basePoiId = -1; //base poiId
      public PoiReviewSummary reviewSummary;
      public Advertisement ad;
      public List<Coupon> couponList = new ArrayList<Coupon>();
      public String businessHour;
      public String menu;
      
      //Add by chbzhang, for poi reviews
      public GetReviewResponse getReviewResponse;
      public int ratingNumber;
      public int userPreviousRating;
      public OpenTableInfo openTableInfo; 
      private List<LocalAppInfo> localAppInfos=new ArrayList<LocalAppInfo>();
      public Map<String, String> extraProperties = new HashMap<String, String>(); 
      
      
      public void setLocalAppInfos(List<LocalAppInfo> localAppInfos) {
  		this.localAppInfos = localAppInfos;
  	}

  	public List<LocalAppInfo> getLocalAppInfos() {
  		return localAppInfos;
  	}

      /**
     * default constructor
     */
    public POI()
    {
        super();
    }
    
    //for DSR
    public byte[] getAudioPoiName()
    {
        return poiNameAudio;
    }
    
    //for DSR
    public void setAudioPoiName(byte[] buf)
    {
        this.poiNameAudio = buf;
    }
    
    public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
	
	public void addReview(Review review)
	{
		reviews.add(review);
	}

}