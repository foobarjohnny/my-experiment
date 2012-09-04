package com.telenav.cserver.proto.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import net.jarlehansen.protobuf.javame.ByteString;

import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.j2me.datatypes.FavoriteCategory;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.framework.protocol.ProtoBasePoi;
import com.telenav.j2me.framework.protocol.ProtoBasePoiExtraInfo;
import com.telenav.j2me.framework.protocol.ProtoBizPoi;
import com.telenav.j2me.framework.protocol.ProtoFavoriteCategory;
import com.telenav.j2me.framework.protocol.ProtoProperty;
import com.telenav.j2me.framework.protocol.ProtoStop;
import com.telenav.kernel.util.datatypes.TnContext;

public class ProtoUtil 
{
    /**
     * convert ProtoStop to regular Stop object
     * @param protoStop
     * @return
     */
	public static Stop convertProtoBufToStop(ProtoStop protoStop)
	{
	    if (protoStop == null)
	        return null;
	    
		Stop stop = new Stop();;
		stop.lat = protoStop.getLat();
		stop.lon = protoStop.getLon();
		stop.stopType = (byte) protoStop.getStopType();
		stop.stopStatus = (byte) protoStop.getStopStatus();
		stop.isGeocoded = protoStop.getIsGeocoded();
		stop.hashCode = protoStop.getHashCode();
		stop.isShareAddress = protoStop.getIsShareAddress();
		stop.label = protoStop.getLabel();
		stop.firstLine = protoStop.getFirstLine();
		stop.city = protoStop.getCity();
		stop.state = protoStop.getState();
		stop.stopId = protoStop.getStopId();
		stop.zip = protoStop.getZip();
		stop.country = protoStop.getCountry();
		stop.county = protoStop.getCounty();
		return stop;
	}
	
	public static ProtoStop convertStopToProtoBuf(Stop stop)
	{
	    if (stop == null)
	        return null;
	    
		ProtoStop.Builder builder = ProtoStop.newBuilder();
		builder.setLat(stop.lat);
		builder.setLon(stop.lon);
		builder.setStopType(stop.stopType);
		builder.setStopStatus(stop.stopStatus);
		builder.setIsGeocoded(stop.isGeocoded);
		builder.setHashCode(stop.hashCode);
		builder.setIsShareAddress(stop.isShareAddress);
		builder.setLabel(stop.label);
		builder.setFirstLine(stop.firstLine);
		builder.setCity(stop.city);
		builder.setState(stop.state);
		builder.setStopId(stop.stopId);
		builder.setZip(stop.zip);
		builder.setCountry(stop.country);
		builder.setCounty(stop.county);
		return builder.build();
	}
	
	public static BizPOI convertProtoBufToBizPOI(ProtoBizPoi protoBizPoi)
	{
	    if (protoBizPoi == null)
	        return null;
	    
	    BizPOI bizPoi = new BizPOI();
	    
	    Vector vSupportInfo = protoBizPoi.getSupportInfos();
	    if (vSupportInfo != null)
	    {
	        bizPoi.supportInfo = new String[vSupportInfo.size()];
	        for (int i=0; i<vSupportInfo.size(); i++)
	        	bizPoi.supportInfo[i] = (String) vSupportInfo.get(i);
	    }
	    
	    bizPoi.brand = protoBizPoi.getBrand();
	    bizPoi.phoneNumber = protoBizPoi.getPhoneNumber();
	    bizPoi.navigable = protoBizPoi.getNavigable();
	    bizPoi.distance = protoBizPoi.getDistance();
	    
	    Vector vSupplementalInfo = protoBizPoi.getSupplementalInfos();
        if (vSupplementalInfo != null)
        {
	        bizPoi.supplementalInfo = new String[vSupplementalInfo.size()];
	        for (int i=0; i<vSupplementalInfo.size(); i++)
	        	bizPoi.supplementalInfo[i] = (String) vSupplementalInfo.get(i);
        }
	    
	    bizPoi.parentCatName = protoBizPoi.getParentCatName();
	    bizPoi.price = protoBizPoi.getPrice();
	    bizPoi.groupID = protoBizPoi.getGroupID();
	    bizPoi.address = convertProtoBufToStop(protoBizPoi.getAddress());
	    bizPoi.layout = protoBizPoi.getLayout();
	    bizPoi.vendorCode = protoBizPoi.getVendorCode();
	    
	    return bizPoi;
	}
	
	public static ProtoBizPoi convertBizPOIToProtoBuf(BizPOI bizPoi)
    {
        if (bizPoi == null)
            return null;
        
        ProtoBizPoi.Builder builder = ProtoBizPoi.newBuilder();
        if (bizPoi.supportInfo != null)
        {
            Vector v = new Vector();
            v.add(bizPoi.supportInfo);
            builder.setSupportInfos(v);
        }
        
        if (bizPoi.supplementalInfo != null)
        {
            Vector v = new Vector();
            v.add(bizPoi.supplementalInfo);
            builder.setSupplementalInfos(v);
        }
        
        builder.setBrand(bizPoi.brand);
        builder.setPhoneNumber(bizPoi.phoneNumber);
        builder.setNavigable(bizPoi.navigable);
        builder.setDistance(bizPoi.distance);
        
        builder.setParentCatName(bizPoi.parentCatName);
        builder.setPrice(bizPoi.price);
        builder.setGroupID(bizPoi.groupID);
        ProtoStop pStop = convertStopToProtoBuf(bizPoi.address);
        if (pStop != null)
        {
            builder.setAddress(pStop);
        }
        builder.setLayout(bizPoi.layout);
        builder.setVendorCode(bizPoi.vendorCode);
        
        return builder.build();
    }
	
	public static POI convertProtoBufToPoi(ProtoBasePoi protoPoi)
	{
	    if (protoPoi == null)
	        return null;
	    
	    POI poi = new POI();
	    
	    poi.bizPoi = convertProtoBufToBizPOI(protoPoi.getBizPoi());
	    poi.avgRating = protoPoi.getAvgRating();
	    poi.popularity = protoPoi.getPopularity();
	    poi.isRatingEnable = protoPoi.getIsRatingEnable();
	    poi.hasUserRatedThisPoi = protoPoi.getHasUserRatedThisPoi();
	    poi.poiId = protoPoi.getPoiId();
	    
	    if (protoPoi.getBasePoiExtraInfo() != null)
	    {
    	    Vector<ProtoProperty> protoPropertyVector = protoPoi.getBasePoiExtraInfo().getExtraProperties();
    	    if (protoPropertyVector != null)
    	    {
        	    for(ProtoProperty protoProperty: protoPropertyVector)
        	    {
        	        poi.extraProperties.put(protoProperty.getKey(), protoProperty.getValue());
        	    }
    	    }
    	    poi.setAudioPoiName(convertProtoBufToByteArray(protoPoi.getBasePoiExtraInfo().getPoiNameAudio()));
	    }
	    
	    
	    return poi;
	}
	
	public static ProtoBasePoi convertTnPoiToProtoBuf(TnPoi tnpoi, TnContext context)
	{
		POI poi = PoiDataConverter.convertTnPoiToPOI(tnpoi, false, 0, context, false);
		return convertPoiToProtoBuf(poi);
	}
	
	public static ProtoBasePoi convertPoiToProtoBuf(POI poi)
    {
        if (poi == null)
            return null;
        
        ProtoBasePoi.Builder builder = ProtoBasePoi.newBuilder();
        
        ProtoBizPoi pBizPoi = convertBizPOIToProtoBuf(poi.bizPoi);
        if (pBizPoi != null)
        {
            builder.setBizPoi(pBizPoi);
        }
        builder.setAvgRating(poi.avgRating);
        builder.setPopularity(poi.popularity);
        builder.setIsRatingEnable(poi.isRatingEnable);
        builder.setHasUserRatedThisPoi(poi.hasUserRatedThisPoi);
        builder.setPoiId(poi.poiId);
        
//        Vector<ProtoProperty> protoPropertyVector = protoPoi.extraProperties;
//        for(ProtoProperty protoProperty: protoPropertyVector)
//        {
//            poi.setExtraProperty(protoProperty.getKey(), protoProperty.getValue());
//        }
        
        ProtoBasePoiExtraInfo.Builder pBasePoiExtraInfo = ProtoBasePoiExtraInfo.newBuilder();
        pBasePoiExtraInfo.setPoiNameAudio(convertByteArrayToProtoBuf(poi.getAudioPoiName()));
        builder.setBasePoiExtraInfo(pBasePoiExtraInfo.build());
        
        return builder.build();
    }
	
	public static HashMap convertProtoBufToHashMap(Vector<ProtoProperty> protoPropertyVector)
	{
	    if (protoPropertyVector == null)
	        return null;
	    
	    HashMap hashMap = new HashMap();
	    for(ProtoProperty protoProperty: protoPropertyVector)
	    {
	        hashMap.put(protoProperty.getKey(), protoProperty.getValue());
	    }
	    
	    return hashMap;
	}
	
	public static Map convertProtoBufToMap(Vector<ProtoProperty> protoPropertyVector)
    {
	    
        return convertProtoBufToHashMap(protoPropertyVector);
    }
	
    public static Vector<ProtoProperty> convertMapToProtoBuf(Map<String, String> map)
    {
        if (map == null)
            return null;
        
        Iterator<String> iterator = map.keySet().iterator();
        Vector<ProtoProperty> vector = new Vector<ProtoProperty>();
        while (iterator.hasNext())
        {
            String key = iterator.next();
            String value = map.get(key);
            vector.add(ProtoProperty.newBuilder().setKey(key).setValue(value).build());
        }
        
        return vector;
    }
	
	public static byte[] convertProtoBufToByteArray(ByteString byteString)
	{
	    if (byteString == null)
	        return null;
	    else
	        return byteString.toByteArray();
	}
	
	public static ByteString convertByteArrayToProtoBuf(byte[] buff)
    {
        if (buff == null)
            return ByteString.copyFromUtf8(" ");
        else
            return ByteString.copyFrom(buff);
    }
	
	public static FavoriteCategory convertProtoBufToFavoriteCategory(ProtoFavoriteCategory pFavoriteCategory)
	{
	    if (pFavoriteCategory == null)
	        return null;
	    
	    FavoriteCategory favoriteCategory = new FavoriteCategory();
	    favoriteCategory.id = pFavoriteCategory.getId();
	    favoriteCategory.catType = (byte)pFavoriteCategory.getCatType();
	    favoriteCategory.name = pFavoriteCategory.getName();
	    favoriteCategory.status = (byte)pFavoriteCategory.getStatus();
	    
	    return favoriteCategory;
	}


	public static ProtoFavoriteCategory convertFavoriteCategoryToProtoBuf(FavoriteCategory favoriteCategory)
    {
        if (favoriteCategory == null)
            return null;
        
        ProtoFavoriteCategory.Builder builder = ProtoFavoriteCategory.newBuilder();
        builder.setId(favoriteCategory.id);
        builder.setCatType(favoriteCategory.catType);
        builder.setName(favoriteCategory.name);
        builder.setStatus(favoriteCategory.status);
        
        return builder.build();
    }
	
	
	public static long[] convertProtoBufToLong(Vector<Long> longVector)
    {
        if (longVector == null)
            return null;
        
        long[] longArray = new long[longVector.size()];
        for(int i=0; i<longVector.size(); i++)
        {
            longArray[i] = longVector.get(i);
        }
        return longArray;
    }
	
	
	public static Vector<Long> convertLongToProtoBuf(long[] longArray)
    {
        if (longArray == null)
            return null;
        
        Vector<Long> longVector = new Vector<Long>();
        for(long longValue: longArray)
        {
            longVector.add(longValue);
        }
        return longVector;

    }
    
	public static Vector<ProtoFavoriteCategory> toFavoriteCategoryVector(Vector<FavoriteCategory> favoriteCategoryVector)
    {
        if (favoriteCategoryVector == null)
            return null;
        
        
        Vector<ProtoFavoriteCategory> pFavoriteCategory = new Vector<ProtoFavoriteCategory>();
        for(FavoriteCategory favoriteCategory : favoriteCategoryVector)
        {
            pFavoriteCategory.add(ProtoUtil.convertFavoriteCategoryToProtoBuf(favoriteCategory));
        }
        
        return pFavoriteCategory;
    }
	
	
	
	
	
}
