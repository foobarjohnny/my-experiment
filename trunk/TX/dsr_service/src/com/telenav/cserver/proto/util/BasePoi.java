package com.telenav.cserver.proto.util;

import com.telenav.j2me.framework.protocol.ProtoBasePoi;
/*
 * Separate POI related to different part
 * POI BASE [COSE]
 * POI REVIEW [CONTENT]
 * POI DETAIL [COSE/ADS/IMAGE]
 * DEALS [ADS]
 * POI EXTRA [COSE]
 */
public class BasePoi 
{
    

    public int avgRating;
    public boolean isRatingEnable;
    public boolean hasUserRatedThisPoi;
    public long poiId;  
    public int popularity;
    public BizPOI bizPOI;
    public BasePoiExtraInfo basePoiExtraInfo;
    
 
    public BasePoi()
    {}
    
    
    public ProtoBasePoi toProtobuf()
    {
        ProtoBasePoi.Builder poiBuilder = ProtoBasePoi.newBuilder();
        poiBuilder.setPoiId(this.poiId);
        poiBuilder.setPopularity(this.popularity);
        poiBuilder.setIsRatingEnable(this.isRatingEnable);
        
        //poiBuilder.setRatingNumber(this.ratingNumber);
        poiBuilder.setHasUserRatedThisPoi(this.hasUserRatedThisPoi);
        poiBuilder.setAvgRating(this.avgRating);
        
        
        if(this.bizPOI != null)
        {
        	poiBuilder.setBizPoi(ProtoDataConvert.toProtobuf(this.bizPOI));
        }
        
        if (this.basePoiExtraInfo != null)
        {
            poiBuilder.setBasePoiExtraInfo(basePoiExtraInfo.toProtobuf());
        }
        
        return poiBuilder.build();
    }
}
