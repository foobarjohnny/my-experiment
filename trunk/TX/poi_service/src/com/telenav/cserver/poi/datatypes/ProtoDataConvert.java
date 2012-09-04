package com.telenav.cserver.poi.datatypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.jarlehansen.protobuf.javame.ByteString;

import com.telenav.cserver.backend.datatypes.LocalAppInfo;
import com.telenav.cserver.backend.datatypes.contents.ReviewServicePOIReview;
import com.telenav.cserver.backend.datatypes.cose.Advertisement;
import com.telenav.cserver.backend.datatypes.cose.Coupon;
import com.telenav.cserver.backend.datatypes.cose.OpenTableInfo;
import com.telenav.j2me.framework.protocol.ProtoAdvertisement;
import com.telenav.j2me.framework.protocol.ProtoBasePoi;
import com.telenav.j2me.framework.protocol.ProtoBasePoiExtraInfo;
import com.telenav.j2me.framework.protocol.ProtoBizPoi;
import com.telenav.j2me.framework.protocol.ProtoCoupon;
import com.telenav.j2me.framework.protocol.ProtoLocalAppInfo;
import com.telenav.j2me.framework.protocol.ProtoOpenTableInfo;
import com.telenav.j2me.framework.protocol.ProtoPoiDetail;
import com.telenav.j2me.framework.protocol.ProtoPoiReview;
import com.telenav.j2me.framework.protocol.ProtoPoiReviewDetail;
import com.telenav.j2me.framework.protocol.ProtoPoiReviewSummary;
import com.telenav.j2me.framework.protocol.ProtoProperty;


public class ProtoDataConvert
{
    public static Vector<ProtoPoiDetail> handlePOI(List<POI> pois)
    {
        Vector<ProtoPoiDetail> vc = new Vector<ProtoPoiDetail>();
        if (pois != null && pois.size() > 0)
        {
            for (POI poi : pois)
            {
                vc.add(toProtobuf(poi));

            }
        }
        return vc;
    } 
    
    public static Vector<ProtoBasePoi> handleBasePoi(List<BasePoi> pois)
    {
        Vector<ProtoBasePoi> vc = new Vector<ProtoBasePoi>();
        if (pois != null && pois.size() > 0)
        {
            for (BasePoi poi : pois)
            {
                vc.add(toProtobuf(poi));

            }
        }
        return vc;
    }
    
    public static ProtoPoiReview toProtobuf(Review review)
    {
        ProtoPoiReview.Builder builder = ProtoPoiReview.newBuilder();
        builder.setFirstName(review.username).setReviewStarRating(review.reviewStarRating)
                .setReviewString(review.reviewString).setUserId(review.userid);
        
        return builder.build();
    }
    
    public static ProtoPoiReviewSummary toProtobuf(PoiReviewSummary summary)
    {
        ProtoPoiReviewSummary.Builder builder = ProtoPoiReviewSummary.newBuilder();
        builder.setAverageRating(summary.getAverageRating()).setBasePoiId(summary.getBasePoiId()).setPopularity(summary.getPopularity())
                .setPriceRange(summary.getPriceRange()).setRatingNumber(summary.getRatingNumber()).setReviewAveragePrice(summary.getReviewAveragePrice())
                .setReviewCategoryPath(summary.getReviewCategoryPath()).setReviewNumber(summary.getReviewNumber()).setReviewText(summary.getReviewText())
                .setReviewType(summary.getReviewType()).setUserPreviousRating(summary.getUserPreviousRating()).setPoiId(summary.getPoiId());
        
        return builder.build();
        
    }
    
    public static ProtoBizPoi toProtobuf(BizPOI bizPoi)
    {
        ProtoBizPoi.Builder bizBuilder = ProtoBizPoi.newBuilder();
        bizBuilder.setBrand(bizPoi.brand);
        bizBuilder.setPhoneNumber(bizPoi.phoneNumber);
        bizBuilder.setNavigable(bizPoi.navigable);
        bizBuilder.setDistance(bizPoi.distance);
        bizBuilder.setParentCatName(bizPoi.parentCatName);
        bizBuilder.setCategoryId(bizPoi.categoryId);
        bizBuilder.setPrice(bizPoi.price);
        bizBuilder.setLayout(bizPoi.layout);
        bizBuilder.setVendorCode(bizPoi.vendorCode);
        bizBuilder.setGroupID(bizPoi.groupID);
        
        if (bizPoi.supplementalInfo != null)
        {
            for(String str:bizPoi.supplementalInfo)
            {
                bizBuilder.addElementSupplementalInfos(str);
            }
        }
        if (bizPoi.supportInfo != null)
        {
            for(String str:bizPoi.supportInfo)
            {
                bizBuilder.addElementSupportInfos(str);
            }
        }
        
        if (bizPoi.address != null)
            bizBuilder.setAddress(bizPoi.address.toProtocol());
        
        return bizBuilder.build();
    }
    
    public static ProtoAdvertisement toProtobuf(Advertisement ad)
    {
        ProtoAdvertisement.Builder builder = ProtoAdvertisement.newBuilder();
        builder.setAdPageUrl(ad.getAdPageUrl()).setAdSource(ad.getAdSource()).setAdType(ad.getAdType()).setBuyerName(ad.getBuyerName())
                .setEventPrice(ad.getEventPrice()).setMessage(ad.getMessage()).setPayPerCall(ad.getPayPerCall())
                .setPayPerClick(ad.getPayPerClick()).setPoundEnabled(ad.getPoundEnabled()).setRanking(ad.getRanking())
                .setShortMessage(ad.getShortMessage()).setSourceAdId(ad.getSourceAdId()).setStarEnabled(ad.isStarEnabled());
        
        if (ad.getStartTime() != null)
            builder.setStartTime(ad.getStartTime().getTime());
        
        if (ad.getEndTime() != null)
            builder.setEndTime(ad.getEndTime().getTime());
        
        return builder.build();
        
        
    }
    
    public static ProtoCoupon toProtoBuf(Coupon coupon)
    {
        ProtoCoupon.Builder builder = ProtoCoupon.newBuilder();
        builder.setId(coupon.getId()).setDescription(coupon.getDescription())
                .setName(coupon.getName()).setText(coupon.getText()).setUrl(coupon.getUrl()).setUrlPPE(coupon.getUrlPPE());
        
        if (coupon.getStartDate() != null)
            builder.setStartDate(coupon.getStartDate().getTime());
        
        if (coupon.getEndDate() != null)
            builder.setEndDate(coupon.getEndDate().getTime());
        
        if (coupon.getImage() != null)
            builder.setImage(ByteString.copyFrom(coupon.getImage()));

        return builder.build();
    }
    
    
    public static ProtoOpenTableInfo toProtobuf(OpenTableInfo openTableInfo)
    {
        ProtoOpenTableInfo.Builder builder = ProtoOpenTableInfo.newBuilder();
        builder.setIsReservable(openTableInfo.isReservable())
                .setPartner(openTableInfo.getPartner())
                .setPartnerPoiId(openTableInfo.getPartnerPoiId());
        
        return builder.build();
    }
    
    public static ProtoPoiReviewDetail toProtoBuf(ReviewServicePOIReview detail)
    {
        ProtoPoiReviewDetail.Builder builder = ProtoPoiReviewDetail.newBuilder();
        builder.setPoiId(detail.getPoiId()).setRating(detail.getRating()).setRatingCount(detail.getRatingCount())
                .setReviewerName(detail.getReviewerName()).setReviewId(detail.getReviewId()).setReviewSource(detail.getReviewSource())
                .setReviewText(detail.getReviewText()).setUserId(detail.getUserId());
        
        if (detail.getCreateTime() != null)
            builder.setCreateTime(detail.getCreateTime().getTime());
        
        if (detail.getUpdateTime() != null)         
            builder.setUpdateTime(detail.getUpdateTime().getTime());
        
        return builder.build();
    }
    
    
    
    public static ProtoPoiDetail toProtobuf(POI poi)
    {
        ProtoPoiDetail.Builder builder = ProtoPoiDetail.newBuilder();
        builder.setAvgRating(poi.avgRating).setBasePoiId(poi.basePoiId).setBusinessHour(poi.businessHour)
                .setHasUserRatedThisPoi(poi.hasUserRatedThisPoi).setIsRatingEnable(poi.isRatingEnable)
                .setMenu(poi.menu).setPoiId(poi.poiId).setPopularity(poi.popularity)
                .setPriceRange(poi.priceRange).setRatingNumber(poi.ratingNumber)
                .setUserPreviousRating(poi.userPreviousRating);
        
        if (poi.openTableInfo != null)
            builder.setOpenTableInfo(toProtobuf(poi.openTableInfo));
        
        
        if (poi.bizPoi != null)
            builder.setBizPoi(toProtobuf(poi.bizPoi));
        
        if (poi.ad != null)
            builder.setAd(toProtobuf(poi.ad));
        
        if (poi.reviewSummary != null)
            builder.setReviewSummary(toProtobuf(poi.reviewSummary));
        
        if (poi.reviews != null)
        {
            for(Review review : poi.reviews)
            {
                builder.addElementReviews(toProtobuf(review));
            }
        }
        
        if (poi.getReviewResponse != null && poi.getReviewResponse.getReview() != null)
        {
            ReviewServicePOIReview[] reviewDetail = poi.getReviewResponse.getReview();
            for(int i=0; i<reviewDetail.length; i++)
            {
                builder.addElementReviewDetail(toProtoBuf(reviewDetail[i]));
            }
        }
        
        if (poi.poiNameAudio != null)
            builder.setPoiNameAudio(ByteString.copyFrom(poi.poiNameAudio));

        if (poi.couponList != null)
        {
            for (Coupon coupon :poi.couponList)
            {
                builder.addElementCouponList(toProtoBuf(coupon));
            }
        }
        
        if (poi.extraProperties != null)
        {
            List<ProtoProperty> list =  toProtobuf(poi.extraProperties);
            for(ProtoProperty pProperty :list)
            {
                builder.addElementExtraProperties(pProperty);
            }
        }
        
        builder.setLocalAppInfos(convert2ProtoLocalAppInfoFromPOI(poi));
        
        return builder.build();
        
    }
    
    public static Vector<ProtoLocalAppInfo> convert2ProtoLocalAppInfoFromBasePOI(BasePoi poi)
    {
    	Vector<ProtoLocalAppInfo> localAppInfos=new Vector<ProtoLocalAppInfo>();
    	
    	for(LocalAppInfo appInfo:poi.getLocalAppInfos())
    	{
    		ProtoLocalAppInfo.Builder protoLocalAppInfo=ProtoLocalAppInfo.newBuilder();
    		protoLocalAppInfo.setName(appInfo.getName());
    		for(String key: appInfo.getProps().keySet())
    		{
    			ProtoProperty.Builder propBuilder=ProtoProperty.newBuilder();
    			propBuilder.setKey(key);
    			propBuilder.setValue(appInfo.getProps().get(key));
    			protoLocalAppInfo.addElementProps(propBuilder.build());
    		}
    		
    		localAppInfos.add(protoLocalAppInfo.build());
    	}
    	return localAppInfos;
    }
    
    public static Vector<ProtoLocalAppInfo> convert2ProtoLocalAppInfoFromPOI(POI poi)
    {
    	Vector<ProtoLocalAppInfo> localAppInfos=new Vector<ProtoLocalAppInfo>();
    	
    	for(LocalAppInfo appInfo:poi.getLocalAppInfos())
    	{
    		ProtoLocalAppInfo.Builder protoLocalAppInfo=ProtoLocalAppInfo.newBuilder();
    		protoLocalAppInfo.setName(appInfo.getName());
    		for(String key: appInfo.getProps().keySet())
    		{
    			ProtoProperty.Builder propBuilder=ProtoProperty.newBuilder();
    			propBuilder.setKey(key);
    			propBuilder.setValue(appInfo.getProps().get(key));
    			protoLocalAppInfo.addElementProps(propBuilder.build());
    		}
    		
    		localAppInfos.add(protoLocalAppInfo.build());
    	}
    	return localAppInfos;
    }
    
    public static ProtoBasePoi toProtobuf(BasePoi basePoi)
    {
        ProtoBasePoi.Builder poiBuilder = ProtoBasePoi.newBuilder();
        poiBuilder.setPoiId(basePoi.poiId);
        poiBuilder.setPopularity(basePoi.popularity);
        poiBuilder.setIsRatingEnable(basePoi.isRatingEnable);
        
        //poiBuilder.setRatingNumber(this.ratingNumber);
        poiBuilder.setHasUserRatedThisPoi(basePoi.hasUserRatedThisPoi);
        poiBuilder.setAvgRating(basePoi.avgRating);
        
        
        if(basePoi.bizPOI != null)
        {
            poiBuilder.setBizPoi(ProtoDataConvert.toProtobuf(basePoi.bizPOI));
        }
        
        if (basePoi.basePoiExtraInfo != null)
        {
            poiBuilder.setBasePoiExtraInfo(toProtobuf(basePoi.basePoiExtraInfo));
        }
        
        poiBuilder.setLocalAppInfos(convert2ProtoLocalAppInfoFromBasePOI(basePoi));
        
        return poiBuilder.build();
    }
    
    public static ProtoBasePoiExtraInfo toProtobuf(BasePoiExtraInfo basePoiExtraInfo)
    {
        ProtoBasePoiExtraInfo.Builder poiExtraInfoBuilder = ProtoBasePoiExtraInfo.newBuilder();
        poiExtraInfoBuilder.setUserPreviousRating(basePoiExtraInfo.userPreviousRating);
        if(basePoiExtraInfo.poiNameAudio != null && basePoiExtraInfo.poiNameAudio.length > 0)
        {
            poiExtraInfoBuilder.setPoiNameAudio(ByteString.copyFrom(basePoiExtraInfo.poiNameAudio));
        }
        poiExtraInfoBuilder.setShortMessage(basePoiExtraInfo.shortMessage);
        poiExtraInfoBuilder.setMessage(basePoiExtraInfo.message);
        poiExtraInfoBuilder.setAdSource(basePoiExtraInfo.adSource);
        poiExtraInfoBuilder.setSourceAdId(basePoiExtraInfo.sourceAdId);
        poiExtraInfoBuilder.setIsReservable(basePoiExtraInfo.isReservable);
        poiExtraInfoBuilder.setPartnerPoiId(basePoiExtraInfo.partnerPoiId);
        poiExtraInfoBuilder.setPartner(basePoiExtraInfo.partner);
        
        
        if(basePoiExtraInfo.extraProperties != null && basePoiExtraInfo.extraProperties.size() > 0)
        {
            List<ProtoProperty> list =  toProtobuf(basePoiExtraInfo.extraProperties);
            for(ProtoProperty pProperty :list)
            {
                poiExtraInfoBuilder.addElementExtraProperties(pProperty);
            }
        }
        
        
        poiExtraInfoBuilder.setRatingNumber(basePoiExtraInfo.ratingNumber);
        
        
        return poiExtraInfoBuilder.build();
    }
    
    public static List<ProtoProperty> toProtobuf(Map<String, String> map)
    {
        List<ProtoProperty> list = new ArrayList<ProtoProperty>();
        Iterator<String> keySet = map.keySet().iterator();
        while(keySet.hasNext())
        {
            String strKey = keySet.next();
            ProtoProperty pProperty = ProtoProperty.newBuilder().setKey(strKey).setValue(map.get(strKey)).build();
            list.add(pProperty);
        }
        
        return list;
    }

}
