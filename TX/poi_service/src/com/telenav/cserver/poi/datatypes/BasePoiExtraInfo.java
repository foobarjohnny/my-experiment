package com.telenav.cserver.poi.datatypes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.jarlehansen.protobuf.javame.ByteString;

import com.telenav.j2me.framework.protocol.ProtoBasePoiExtraInfo;
import com.telenav.j2me.framework.protocol.ProtoProperty;
/*
 * Separate POI related to different part
 * POI BASE [COSE]
 * POI REVIEW [CONTENT]
 * POI DETAIL [COSE/ADS/IMAGE]
 * DEALS [ADS]
 * POI EXTRA [COSE]
 */
public class BasePoiExtraInfo 
{
    public int userPreviousRating;
    public int ratingNumber;
    public byte[] poiNameAudio;
    public String shortMessage;
    public String message;
    public String adSource;
    public String sourceAdId;
    public boolean isReservable;
    public String partnerPoiId;
    public String partner;     
    public Map<String, String> extraProperties = new HashMap();

    
    public BasePoiExtraInfo()
    {}
    
    public ProtoBasePoiExtraInfo toProtobuf()
    {
        ProtoBasePoiExtraInfo.Builder poiExtraInfoBuilder = ProtoBasePoiExtraInfo.newBuilder();
        poiExtraInfoBuilder.setUserPreviousRating(this.userPreviousRating);
        if(this.poiNameAudio != null && this.poiNameAudio.length > 0)
        {
            poiExtraInfoBuilder.setPoiNameAudio(ByteString.copyFrom(this.poiNameAudio));
        }
        poiExtraInfoBuilder.setShortMessage(shortMessage);
        poiExtraInfoBuilder.setMessage(message);
        poiExtraInfoBuilder.setAdSource(adSource);
        poiExtraInfoBuilder.setSourceAdId(sourceAdId);
        poiExtraInfoBuilder.setIsReservable(isReservable);
        poiExtraInfoBuilder.setPartnerPoiId(partnerPoiId);
        poiExtraInfoBuilder.setPartner(partner);
        if(this.extraProperties != null && this.extraProperties.size() > 0)
        {
            Iterator<String> keySet = this.extraProperties.keySet().iterator();
            while(keySet.hasNext())
            {
                String strKey = keySet.next();
                ProtoProperty pProperty = ProtoProperty.newBuilder().setKey(strKey).setValue(this.extraProperties.get(strKey)).build();
                poiExtraInfoBuilder.addElementExtraProperties(pProperty);
            }
        }
        poiExtraInfoBuilder.setRatingNumber(ratingNumber);
        
        
        return poiExtraInfoBuilder.build();
    }
    

}
