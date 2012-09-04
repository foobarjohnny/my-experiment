/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.unittestutil;

import net.jarlehansen.protobuf.javame.ByteString;

import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.framework.protocol.ProtoStop;

/**
 * UTConstant.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-26
 */
public class UTConstant {

	public static final String DO_NOT_WORRY_EXCEPTION = "If you see me, shows that the code is right! Don't worry.\n";
	
	public static final String EXECUTORTYPE = "TESTEXECUTORTYPE";
	public static final String JSONARRAY_EXECUTORTYPE_KEY = "JE_KEY";
	public static final Object nullObj = null;
	
	public static final int PROTOGPSFIX_ERRORSIZE = 50;
	public static final int PROTOGPSFIX_HEADING = 10;
	public static final int PROTOGPSFIX_LATITUDE = 100;
	public static final int PROTOGPSFIX_LONTITUDE = 150;
	public static final int PROTOGPSFIX_SPEED = 80;
	public static final long PROTOGPSFIX_TIMETAG = 1;
	public static final int PROTOGPSFIX_TYPE = 5;
	
	public static final String PROTOSTOP_CITY = "Chicago";
	public static final String PROTOSTOP_COUNTRY = "US";
	public static final String PROTOSTOP_COUNTY = "county";
	public static final String PROTOSTOP_CROSSSTREETNAME = "CROSSSTREETNAME";
	public static final String PROTOSTOP_DOOR = "206";
	public static final String PROTOSTOP_FIRSTLINE = "FIRSTLINE";
	public static final int PROTOSTOP_HASHCODE = 123456;
	public static final boolean PROTOSTOP_ISGEOCODED = true;
	public static final boolean PROTOSTOP_ISSHAREADDRESS = false;
	public static final String PROTOSTOP_LABEL = "label";
	public static final String PROTOSTOP_LASTLINE = "LASTLINE";
	public static final int PROTOSTOP_LAT = 1;
	public static final int PROTOSTOP_LON = 2;
	public static final String PROTOSTOP_STATE = "Illinois";
	public static final String PROTOSTOP_STOPID = "Sre357";
	public static final int PROTOSTOP_STOPSTATUS = 66;
	public static final int PROTOSTOP_STOPTYPE = 65;
	public static final String PROTOSTOP_STREETNAME = "second street";
	public static final String PROTOSTOP_ZIP = "zip_zip";
	
	public static final String TnContext_PROP_MAP_DATASET = "TnContext_PROP_MAP_DATASET";
	
	public static final String USERPROFILE_ATTRBUTE_ACCOUNT_TYPE = "USERPROFILE_ATTRBUTE_ACCOUNT_TYPE";
	public static final String USERPROFILE_AUDIOFORMAT = "mp3";
	public static final String USERPROFILE_AUDIOLEVEL = "5";
	public static final String USERPROFILE_BUILDNUMBER = "21";
	public static final String USERPROFILE_CARRIER = "CMCC";
	public static final String USERPROFILE_DATAPROCESS = "ds";
	public static final String USERPROFILE_DEVICE = "9000";
	public static final String USERPROFILE_EQPIN = "EQPIN";
	public static final String USERPROFILE_GPSTYPE = "MO-12X-r45";
	public static final String USERPROFILE_GUIDETONE = "English";
	public static final String USERPROFILE_IMAGETYPE = "jpg";
	public static final String USERPROFILE_LOCALE = "CHINA";
	public static final String USERPROFILE_MAPDPI = "MAPDPI";
	public static final String USERPROFILE_MIN = "TESTMIN";
	public static final String USERPROFILE_PASSWORD = "abcd";
	public static final String USERPROFILE_PLATFORM = "Android";
	public static final String USERPROFILE_PRODUCT = "FREE";
	public static final String USERPROFILE_PROGRAMCODE = "123456";
	public static final int USERPROFILE_PTNSOURCE = 10;
	public static final String USERPROFILE_REGION = "SHANGHAI";
	public static final String USERPROFILE_RESOLUTION = "320x480_480x320";
	public static final String USERPROFILE_SCREENHEIGHT = "600";
	public static final String USERPROFILE_SCREENWIDTH = "800";
	public static final String USERPROFILE_SSOTOKEN = "**";
	public static final String USERPROFILE_USERID = "12321";
	public static final String USERPROFILE_VERSION = "6_0_01";
	public static final String DEFAULT = "default";
	
	
	public static final int STOP_LAT = 12;
	public static final int STOP_LON = 13;
	public static final byte STOP_STOPTYPE = 97;
	public static final byte STOP_STOPSTATUS = 98;
	public static final boolean STOP_ISGEOCODED = false;
	public static final int STOP_HASHCODE = 7890123;
	public static final boolean STOP_ISSHAREADDRESS = true;
	public static final String STOP_LABEL = "stop_label";
	public static final String STOP_FIRSTLINE = "stop_firstLine";
	public static final String STOP_CITY = "ShenYang";
	public static final String STOP_STATE = "LiangNing";
	public static final String STOP_STOPID = "w124";
	public static final String STOP_ZIP = "stop_zip";
	public static final String STOP_COUNTRY = "CN";
	public static final String STOP_COUNTY = "stop_county";
	
    public static final String BIZPOI_SUPPORTINFO = "BIZPOINO_SUPPORTINFO";
    public static final String BIZPOI_BRAND = "SIMENS";
    public static final String BIZPOI_PHONENUMBER = "13800138000";
    public static final boolean BIZPOI_NAVIGABLE = true;
    public static final int BIZPOI_DISTANCE = 500;
    public static final String BIZPOI_SUPPLEMENTALINFO = "BIZPOINO_SUPPLEMENTALINFO";
    public static final String BIZPOI_PARENTCATNAME = "abc";
    public static final String BIZPOI_PRICE = "10.2";
    public static final int BIZPOI_GROUPID = -1;
    public static final Stop BIZPOI_ADDRESS = UnittestUtil.createStop();
    public static final int BIZPOI_LAYOUT = 3;
    public static final String BIZPOI_VENDORCODE = "210092";
    
    public static final String PROTOBIZPOI_BRAND = "nokia";
    public static final String PROTOBIZPOI_PHONENUMBER = "110";
    public static final String PROTOBIZPOI_CATEGORYID = "555";
    public static final int PROTOBIZPOI_GROUPID = 434;
    public static final boolean PROTOBIZPOI_NAVIGABLE = false;
    public static final int PROTOBIZPOI_DISTANCE = 600;
	public static final String PROTOBIZPOI_PARENTCATNAME = "name..";
	public static final String PROTOBIZPOI_PRICE = "23.1";
	public static final int PROTOBIZPOI_LAYOUT = 1;
	public static final String PROTOBIZPOI_VENDORCODE = "249";
	public static final ProtoStop PROTOBIZPOI_ADDRESS = UnittestUtil.createProtoStop();
	public static final String PROTOBIZPOI_SUPPORTINFO = "aaa";
	public static final String PROTOBIZPOI_SUPPLEMENTALINFO = "bbbb";
	
	public static final int PROTOBASEPOIEXTRAINFO_USERPREVIOUSRATING = 22;
	public static final ByteString PROTOBASEPOIEXTRAINFO_POINAMEAUDIO = ByteString.copyFrom(new byte[]{65,66,67});
	public static final String PROTOBASEPOIEXTRAINFO_SHORTMESSAGE = "shortMessage";
	public static final String PROTOBASEPOIEXTRAINFO_MESSAGE= "message";
	public static final String PROTOBASEPOIEXTRAINFO_ADSOURCE = "http";
	public static final String PROTOBASEPOIEXTRAINFO_SOURCEADID = "http123";
	public static final boolean PROTOBASEPOIEXTRAINFO_ISRESERVABLE = false;
	public static final String PROTOBASEPOIEXTRAINFO_PARTNERPOIID = "partnerPoiId";
	public static final String PROTOBASEPOIEXTRAINFO_PARTNER = "car";
	public static final int PROTOBASEPOIEXTRAINFO_RATINGNUMBER = 456;
	
	public static final String PROTOPROPERTY_BUILDER_KEY = "PROTOPROPERTY_BUILDER_KEY";
	public static final String PROTOPROPERTY_BUILDER_VALUE = "PROTOPROPERTY_BUILDER_VALUE";
	
	public static final int PROTOBASEPOI_AVGRATING = 333;
	public static final boolean PROTOBASEPOI_ISRATINGENABLE = true;
	public static final boolean PROTOBASEPOI_HASUSERRATEDTHISPOI = false;
	public static final long PROTOBASEPOI_POIID = 4L;
	public static final int PROTOBASEPOI_POPULARITY = 6;
	
	public static final int POI_AVGRATING = 3;
	public static final int  POI_POPULARITY = 8;
	public static final boolean  POI_ISRATINGENABLE = true;
	public static final boolean  POI_HASUSERRATEDTHISPOI = false;
	public static final long  POI_POIID = 0L;
	
	public static final int PROTOSTOPPOIWRAPPER_TYPE = 7;
	public static final int PROTOSTOPPOIWRAPPER_STATUS = 46;
	public static final long PROTOSTOPPOIWRAPPER_ID = 9L;
	public static final String PROTOSTOPPOIWRAPPER_LABEL = "PROTOSTOPPOIWRAPPER_LABEL";
	public static final String PROTOSTOPPOIWRAPPER_SHAREDFROMUSER = "sharedFromUser";
	public static final String PROTOSTOPPOIWRAPPER_SHAREDFROMPTN = "sharedFromPTN";
	public static final long PROTOSTOPPOIWRAPPER_CREATETIME = 123456;
	public static final long PROTOSTOPPOIWRAPPER_UPDATETIME = 123459;
	
	public static final byte STOPPOIWRAPPER_TYPE = 65;
	public static final byte STOPPOIWRAPPER_STATUS = 50;
	public static final String STOPPOIWRAPPER_LABEL = "stopPoiWrapper_label";
	public static final long STOPPOIWRAPPER_ID = 11L;
	public static final String STOPPOIWRAPPER_SHAREDFROMUSER = "stopPoiWrapper_sharedFromUser";
	public static final String STOPPOIWRAPPER_SHAREDFROMPTN = "stopPoiWrapper_sharedFromPTN";
	
	public static final long PROTOFAVORITECATEGORY_ID = 55L;
	public static final int PROTOFAVORITECATEGORY_CATTYPE = 35;
	public static final String PROTOFAVORITECATEGORY_NAME = "protofavoritecategory_name";
	public static final int PROTOFAVORITECATEGORY_STATUS = 56;
	public static final long PROTOFAVORITECATEGORY_CREATETIME = 324;
	public static final long PROTOFAVORITECATEGORY_UPDATETIME = 424;
	
	public static final long FAVORITECATEGORY_ID = 66L;
	public static final byte FAVORITECATEGORY_CATTYPE = 2;
	public static final String FAVORITECATEGORY_NAME = "fff";
	public static final byte FAVORITECATEGORY_STATUS = 0;
}
