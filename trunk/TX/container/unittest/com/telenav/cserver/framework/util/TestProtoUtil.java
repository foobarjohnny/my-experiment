/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import junit.framework.TestCase;
import net.jarlehansen.protobuf.javame.ByteString;

import com.telenav.cserver.unittestutil.UTConstant;
import com.telenav.cserver.unittestutil.UnittestUtil;
import com.telenav.j2me.datatypes.BizPOI;
import com.telenav.j2me.datatypes.FavoriteCategory;
import com.telenav.j2me.datatypes.POI;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.datatypes.StopPoiWrapper;
import com.telenav.j2me.framework.protocol.ProtoBasePoi;
import com.telenav.j2me.framework.protocol.ProtoBasePoiExtraInfo;
import com.telenav.j2me.framework.protocol.ProtoBizPoi;
import com.telenav.j2me.framework.protocol.ProtoFavoriteCategory;
import com.telenav.j2me.framework.protocol.ProtoProperty;
import com.telenav.j2me.framework.protocol.ProtoStop;
import com.telenav.j2me.framework.protocol.ProtoStopPoiWrapper;

/**
 * TestProtoUtil.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-26
 */
public class TestProtoUtil extends TestCase{
	private ProtoUtil protoUtil = new ProtoUtil();//for coverage
	
	public void testConvertProtoBufToStop(){
		//prepare
		ProtoStop arg = UnittestUtil.createProtoStop();
		assertNull(ProtoUtil.convertProtoBufToStop(null));
		
		//invoke
		Stop result = ProtoUtil.convertProtoBufToStop(arg);
		
		//assert
		assertEquals(UTConstant.PROTOSTOP_LAT, result.lat);
		assertEquals(UTConstant.PROTOSTOP_ISSHAREADDRESS, result.isShareAddress);
		assertEquals(UTConstant.PROTOSTOP_STOPID, result.stopId);
	}
	
	public void testConvertStopToProtoBuf(){
		assertNull(ProtoUtil.convertStopToProtoBuf(null));
		
		//prepare
		
		Stop arg = UnittestUtil.createStop();
		
		//invoke
		ProtoStop result = ProtoUtil.convertStopToProtoBuf(arg);
		
		//assert
		assertEquals(UTConstant.STOP_LAT, result.getLat());
		assertEquals(UTConstant.STOP_ISSHAREADDRESS, result.getIsShareAddress());
		assertEquals(UTConstant.STOP_STOPID, result.getStopId());
	}
	/**
	 * (protoBizPoi.getSupportInfos() != null && protoBizPoi.getSupportInfos().size() > 0)
	 * (protoBizPoi.getSupplementalInfos() != null && protoBizPoi.getSupplementalInfos().size() > 0)
	 */
	public void testConvertProtoBufToBizPOI(){
		assertNull(ProtoUtil.convertProtoBufToBizPOI(null));
		
		//prepare
		
		ProtoBizPoi arg = UnittestUtil.createProtoBizPoi();
		
		//invoke
		BizPOI result = ProtoUtil.convertProtoBufToBizPOI(arg);
		
		//assert
		assertEquals(UTConstant.PROTOBIZPOI_BRAND, result.brand);
		assertEquals(UTConstant.PROTOBIZPOI_PHONENUMBER, result.phoneNumber);
		assertEquals(UTConstant.PROTOBIZPOI_NAVIGABLE, result.navigable);
		assertEquals(UTConstant.PROTOBIZPOI_DISTANCE, result.distance );
		 
		assertEquals(UTConstant.PROTOBIZPOI_SUPPORTINFO, result.supportInfo);
		assertEquals(UTConstant.PROTOBIZPOI_SUPPLEMENTALINFO, result.supplementalInfo);
	}
	/**
	 * (protoBizPoi.getSupportInfos() != null && protoBizPoi.getSupportInfos().size() == 0)
	 * (protoBizPoi.getSupplementalInfos() != null && protoBizPoi.getSupplementalInfos().size() == 0)
	 */
	public void testConvertProtoBufToBizPOI1(){
		
		//prepare
		ProtoBizPoi.Builder builder = ProtoBizPoi.newBuilder();
		ProtoBizPoi arg = builder.build();

		//invoke
		BizPOI result = ProtoUtil.convertProtoBufToBizPOI(arg);
		
		 //no exception is ok
	}
	/**
	 * (protoBizPoi.getSupportInfos() == null 
	 * (protoBizPoi.getSupplementalInfos() == null
	 */
	public void testConvertProtoBufToBizPOI2(){
		//prepare
		ProtoBizPoi.Builder builder = ProtoBizPoi.newBuilder();
		builder.setSupportInfos(null);
		builder.setSupplementalInfos(null);
		
		ProtoBizPoi arg = builder.build();
		
		//invoke
		BizPOI result = ProtoUtil.convertProtoBufToBizPOI(arg);
		
		 //no exception is ok
	}
	
	
	
	
	
	/**
	 * bizPoi.supportInfo != null
	 * bizPoi.supplementalInfo != null
	 * pStop != null
	 */
	public void testConvertBizPOIToProtoBuf(){
		assertNull(ProtoUtil.convertBizPOIToProtoBuf(null));
		
		//prepare
		BizPOI arg = UnittestUtil.createBizPOI();
		
		//invoke
		ProtoBizPoi result = ProtoUtil.convertBizPOIToProtoBuf(arg);
		
		//assert
		assertEquals(arg.brand, result.getBrand());
		assertEquals(arg.phoneNumber, result.getPhoneNumber());
		assertEquals(arg.navigable, result.getNavigable());
		assertEquals(arg.distance, result.getDistance());
		
		assertEquals(arg.supportInfo, result.getSupportInfos().get(0).toString());
		assertEquals(arg.supplementalInfo, result.getSupplementalInfos().get(0).toString());
		assertNotNull(result.getAddress());
	}
	/**
	 * bizPoi.supportInfo == null
	 * bizPoi.supplementalInfo == null
	 * pStop == null
	 */
	public void testConvertBizPOIToProtoBuf1(){

		//prepare
		BizPOI arg = UnittestUtil.createBizPOI();
		arg.supportInfo = null;
		arg.supplementalInfo = null;
		arg.address = null;
		
		//invoke
		ProtoBizPoi result = ProtoUtil.convertBizPOIToProtoBuf(arg);
		
		//assert
		assertEquals(arg.brand, result.getBrand());
		assertEquals(arg.phoneNumber, result.getPhoneNumber());
		assertEquals(arg.navigable, result.getNavigable());
		assertEquals(arg.distance, result.getDistance());
		
		assertNull(result.getAddress());
	}
	/**
	 * protoPoi.getBasePoiExtraInfo() != null
	 * 		protoPoi.getBasePoiExtraInfo().getExtraProperties() != null
	 */
	public void testConvertProtoBufToPoi(){
		assertNull(ProtoUtil.convertProtoBufToPoi(null));
		//prepare
		ProtoProperty property = UnittestUtil.createProtoProperty();
		
		ProtoBasePoiExtraInfo.Builder extraInfobuilder = UnittestUtil.getProtoBasePoiExtraInfo_bulder();
		extraInfobuilder.addElementExtraProperties(property);
		
		ProtoBasePoi arg = null;
		ProtoBasePoi.Builder builder = UnittestUtil.getProtoBasePoi_builder();
		builder.setBasePoiExtraInfo(extraInfobuilder.build());
		
		arg = builder.build();

		
		//invoke
		POI result = ProtoUtil.convertProtoBufToPoi(arg);
		
		//assert
		assertEquals(UTConstant.PROTOBASEPOI_AVGRATING, result.avgRating);
		assertEquals(UTConstant.PROTOBASEPOI_POPULARITY, result.popularity);
		assertEquals(UTConstant.PROTOPROPERTY_BUILDER_VALUE,result.getExtraProperty(UTConstant.PROTOPROPERTY_BUILDER_KEY));
	}
	/**
	 * protoPoi.getBasePoiExtraInfo() != null
	 * 		protoPoi.getBasePoiExtraInfo().getExtraProperties() == null
	 */
	public void testConvertProtoBufToPoi1(){
		//prepare
		ProtoBasePoiExtraInfo.Builder extraInfobuilder = UnittestUtil.getProtoBasePoiExtraInfo_bulder();
		extraInfobuilder.setExtraProperties(null);
		
		ProtoBasePoi arg = null;
		ProtoBasePoi.Builder builder = UnittestUtil.getProtoBasePoi_builder();
		builder.setBasePoiExtraInfo(extraInfobuilder.build());
		
		arg = builder.build();

		
		//invoke
		POI result = ProtoUtil.convertProtoBufToPoi(arg);
		
		//assert
		assertEquals(UTConstant.PROTOBASEPOI_AVGRATING, result.avgRating);
		assertEquals(UTConstant.PROTOBASEPOI_POPULARITY, result.popularity);
		assertNull(result.getExtraProperty(UTConstant.PROTOPROPERTY_BUILDER_KEY));
	}
	/**
	 * protoPoi.getBasePoiExtraInfo() == null
	 * 		protoPoi.getBasePoiExtraInfo().getExtraProperties() == null
	 */
	public void testConvertProtoBufToPoi2(){
		//prepare
		ProtoBasePoi arg = null;
		ProtoBasePoi.Builder builder = UnittestUtil.getProtoBasePoi_builder();
		builder.setBasePoiExtraInfo(null);
		
		arg = builder.build();
		
		//invoke
		POI result = ProtoUtil.convertProtoBufToPoi(arg);
		
		//assert
		assertEquals(UTConstant.PROTOBASEPOI_AVGRATING, result.avgRating);
		assertEquals(UTConstant.PROTOBASEPOI_POPULARITY, result.popularity);
		assertNull(result.getExtraProperty(UTConstant.PROTOPROPERTY_BUILDER_KEY));
	}
	
	/**
	 * poi.bizPoi != null
	 */
	public void testConvertPoiToProtoBuf(){
		assertNull(ProtoUtil.convertPoiToProtoBuf(null));
		
		//prepare
		POI arg = UnittestUtil.createPOI();
		arg.bizPoi = UnittestUtil.createBizPOI();
		
		//invoke
		ProtoBasePoi result = ProtoUtil.convertPoiToProtoBuf(arg);
		
		//assert
		assertEquals(UTConstant.POI_AVGRATING, result.getAvgRating());
		assertEquals(UTConstant.POI_POIID, result.getPoiId());
		assertEquals(UTConstant.POI_POPULARITY, result.getPopularity());
		assertEquals(UTConstant.BIZPOI_BRAND, result.getBizPoi().getBrand());
	}
	/**
	 * poi.bizPoi == null
	 */
	public void testConvertPoiToProtoBuf1(){
		assertNull(ProtoUtil.convertPoiToProtoBuf(null));
		
		//prepare
		POI arg = UnittestUtil.createPOI();
		arg.bizPoi = null;
		
		//invoke
		ProtoBasePoi result = ProtoUtil.convertPoiToProtoBuf(arg);
		
		//assert
		assertEquals(UTConstant.POI_AVGRATING, result.getAvgRating());
		assertEquals(UTConstant.POI_POIID, result.getPoiId());
		assertEquals(UTConstant.POI_POPULARITY, result.getPopularity());
		assertNull(result.getBizPoi());
	}
	
	
	public void testConvertProtoBufToHashMap(){
		assertNull(ProtoUtil.convertProtoBufToHashMap(null));
		//prepare
		ProtoProperty.Builder pb0 = UnittestUtil.getProtoProperty_builder();
		ProtoProperty.Builder pb1 = UnittestUtil.getProtoProperty_builder();
		ProtoProperty.Builder pb2 = UnittestUtil.getProtoProperty_builder();
		
		pb0.setKey("0");
		pb0.setValue("zero");
		pb1.setKey("1");
		pb1.setValue("one");
		pb2.setKey("2");
		pb2.setValue("two");
		
		Vector<ProtoProperty> arg = new Vector<ProtoProperty>();
		arg.add(pb0.build());
		arg.add(pb1.build());
		arg.add(pb2.build());
		
		//invoke
		HashMap result = ProtoUtil.convertProtoBufToHashMap(arg);
		
		//assert
		assertEquals(3, result.size());
		assertTrue(result.containsKey("0"));
		assertTrue(result.containsKey("1"));
		assertTrue(result.containsKey("2"));
		assertTrue(result.containsValue("zero"));
		assertTrue(result.containsValue("one"));
		assertTrue(result.containsValue("two"));
		
		ProtoUtil.convertProtoBufToMap(arg);//just for coverage
	}
	
	public void testConvertMapToProtoBuf(){
		assertNull(ProtoUtil.convertMapToProtoBuf(null));
		//prepare
		Map<String, String> map = new HashMap<String, String>();
		map.put("0", "zero");
		map.put("1", "one");
		map.put("2", "two");
		
		//invoke
		Vector<ProtoProperty> result = ProtoUtil.convertMapToProtoBuf(map);
		
		//assert
		assertEquals(3, result.size());
		String key = "";
		for(ProtoProperty p : result){
			if(p.getKey() .equals("1")){
				key = p.getKey();
			}
		}
		assertEquals("1", key);
	}
	
	public void testConvertProtoBufToByteArray() throws UnsupportedEncodingException{
		assertNull(ProtoUtil.convertProtoBufToByteArray(null));
		
		//prepare
		ByteString arg = ByteString.copyFrom("ABC","UTF-8");
		
		//invoke
		byte[] result = ProtoUtil.convertProtoBufToByteArray(arg);
		
		//assert
		assertEquals(3, result.length);
		assertEquals(65, result[0]);
		assertEquals(66, result[1]);
		assertEquals(67, result[2]);
	}
	
	public void testConvertByteArrayToProtoBuf(){
		assertNull(ProtoUtil.convertByteArrayToProtoBuf(null));
		
		//prepare
		byte[] arg = new byte[]{'a','b','c','d'};
		
		//invoke
		ByteString result = ProtoUtil.convertByteArrayToProtoBuf(arg);
		
		//assert
		assertEquals(4, result.size());
		assertEquals(97, result.byteAt(0));
		assertEquals(98, result.byteAt(1));
		assertEquals(99, result.byteAt(2));
		assertEquals(100, result.byteAt(3));
	}
	
	public void testConvertProtoBufToStopPoiWrapper(){
		assertNull(ProtoUtil.convertProtoBufToStopPoiWrapper(null));
		
		//prepare
		ProtoStopPoiWrapper.Builder builder = UnittestUtil.getProtoStopPoiWrapper_builder();
		builder.setStop(UnittestUtil.createProtoStop());
		builder.setPoi(UnittestUtil.createProtoBasePoi());
		
		//invoke
		StopPoiWrapper result = ProtoUtil.convertProtoBufToStopPoiWrapper(builder.build());
		
		//assert
		assertEquals(UTConstant.PROTOSTOPPOIWRAPPER_LABEL, result.label);
		assertEquals(UTConstant.PROTOSTOPPOIWRAPPER_TYPE, result.type);
		assertEquals(UTConstant.PROTOSTOPPOIWRAPPER_SHAREDFROMUSER, result.sharedFromUser);
		assertEquals(UTConstant.PROTOSTOP_CITY, result.stop.city);
		assertEquals(UTConstant.PROTOBASEPOI_AVGRATING, result.poi.avgRating);
		
	}
	
	/**
	 * arg.stop != null
	 * arg.poi != null
	 */
	public void testConvertStopPoiWrapperToProtoBuf(){
		assertNull(ProtoUtil.convertStopPoiWrapperToProtoBuf(null));
		
		//prepare
		StopPoiWrapper arg = UnittestUtil.createStopPoiWrapper();
		arg.stop = UnittestUtil.createStop();
		arg.poi = UnittestUtil.createPOI();
		//invoke
		ProtoStopPoiWrapper result = ProtoUtil.convertStopPoiWrapperToProtoBuf(arg);
		
		//assert
		assertEquals(UTConstant.STOPPOIWRAPPER_LABEL, result.getLabel());
		assertEquals(UTConstant.STOPPOIWRAPPER_ID, result.getId());
		assertEquals(UTConstant.STOPPOIWRAPPER_SHAREDFROMPTN, result.getSharedFromPTN());
		assertEquals(UTConstant.STOP_COUNTRY, result.getStop().getCountry());
		assertEquals(UTConstant.POI_POPULARITY, result.getPoi().getPopularity());
	}
	/**
	 * arg.stop == null
	 * arg.poi == null
	 */
	public void testConvertStopPoiWrapperToProtoBuf1(){
		assertNull(ProtoUtil.convertStopPoiWrapperToProtoBuf(null));
		
		//prepare
		StopPoiWrapper arg = UnittestUtil.createStopPoiWrapper();
		arg.stop = null;
		arg.poi = null;
		//invoke
		ProtoStopPoiWrapper result = ProtoUtil.convertStopPoiWrapperToProtoBuf(arg);
		
		//assert
		assertEquals(UTConstant.STOPPOIWRAPPER_LABEL, result.getLabel());
		assertEquals(UTConstant.STOPPOIWRAPPER_ID, result.getId());
		assertEquals(UTConstant.STOPPOIWRAPPER_SHAREDFROMPTN, result.getSharedFromPTN());
		assertNull(result.getStop());
		assertNull(result.getPoi());
	}
	
	public void testConvertProtoBufToFavoriteCategory(){
		assertNull(ProtoUtil.convertProtoBufToFavoriteCategory(null));
		
		//prepare
		ProtoFavoriteCategory arg = UnittestUtil.createProtoFavoriteCategory();
		//invoke
		FavoriteCategory result = ProtoUtil.convertProtoBufToFavoriteCategory(arg);
		
		//assert
		assertEquals(UTConstant.PROTOFAVORITECATEGORY_CATTYPE, result.catType);
		assertEquals(UTConstant.PROTOFAVORITECATEGORY_NAME, result.name);
		assertEquals(UTConstant.PROTOFAVORITECATEGORY_STATUS, result.status);
	}
	
	public void testConvertFavoriteCategoryToProtoBuf(){
		assertNull(ProtoUtil.convertFavoriteCategoryToProtoBuf(null));
		
		//prepare
		FavoriteCategory arg = UnittestUtil.createFavoriteCategory();
		//invoke
		ProtoFavoriteCategory result = ProtoUtil.convertFavoriteCategoryToProtoBuf(arg);
		
		//assert
		assertEquals(UTConstant.FAVORITECATEGORY_ID, result.getId());
		assertEquals(UTConstant.FAVORITECATEGORY_CATTYPE, result.getCatType());
		assertEquals(UTConstant.FAVORITECATEGORY_NAME, result.getName());
		assertEquals(UTConstant.FAVORITECATEGORY_STATUS, result.getStatus());
	}
	
	public void testConvertProtoBufToLong(){
		assertNull(ProtoUtil.convertProtoBufToLong(null));
		
		//prepare
		Vector<Long> arg = new Vector<Long>();
		arg.add(new Long(0));
		arg.add(new Long(1));
		arg.add(new Long(2));
		//invoke
		long[] result = ProtoUtil.convertProtoBufToLong(arg);
		
		//assert
		assertEquals(3, result.length);
		assertEquals(0, result[0]);
		assertEquals(1, result[1]);
		assertEquals(2, result[2]);
	}
	
	public void testConvertLongToProtoBuf(){
		assertNull(ProtoUtil.convertLongToProtoBuf(null));
		
		//prepare
		long[] arg = new long[]{0,1,2};
		//invoke
		Vector<Long>  result = ProtoUtil.convertLongToProtoBuf(arg);
		
		//assert
		assertEquals(3, result.size());
		assertEquals(0, result.get(0).intValue());
		assertEquals(1, result.get(1).intValue());
		assertEquals(2, result.get(2).intValue());
	}
	
	public void testToStopPoiWrapperVector(){
		assertNull(ProtoUtil.toStopPoiWrapperVector(null));
		
		//prepare
		Vector<StopPoiWrapper> arg = new Vector<StopPoiWrapper>();
		arg.add(UnittestUtil.createStopPoiWrapper());
		
		//invoke
		Vector<ProtoStopPoiWrapper> result = ProtoUtil.toStopPoiWrapperVector(arg);
		
		//assert
		assertEquals(1, result.size());
		assertEquals(UTConstant.STOPPOIWRAPPER_TYPE, result.get(0).getType());
		assertEquals(UTConstant.STOPPOIWRAPPER_STATUS, result.get(0).getStatus());
		assertEquals(UTConstant.STOPPOIWRAPPER_SHAREDFROMPTN, result.get(0).getSharedFromPTN());
	}
	
	public void testToFavoriteCategoryVector(){
		assertNull(ProtoUtil.toFavoriteCategoryVector(null));
		
		//prepare
		Vector<FavoriteCategory> arg = new Vector<FavoriteCategory>();
		arg.add(UnittestUtil.createFavoriteCategory());
		
		//invoke
		Vector<ProtoFavoriteCategory> result = ProtoUtil.toFavoriteCategoryVector(arg);
		
		//assert
		assertEquals(1, result.size());
		assertEquals(UTConstant.FAVORITECATEGORY_CATTYPE, result.get(0).getCatType());
		assertEquals(UTConstant.FAVORITECATEGORY_STATUS, result.get(0).getStatus());
		assertEquals(UTConstant.FAVORITECATEGORY_NAME, result.get(0).getName());
	}
	
}
