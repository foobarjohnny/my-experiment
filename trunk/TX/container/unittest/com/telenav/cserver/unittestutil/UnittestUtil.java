/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.unittestutil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.powermock.reflect.Whitebox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.telenav.cserver.common.resource.LoadOrders;
import com.telenav.cserver.common.resource.MockTestHolder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.j2me.datatypes.BizPOI;
import com.telenav.j2me.datatypes.FavoriteCategory;
import com.telenav.j2me.datatypes.POI;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.datatypes.StopPoiWrapper;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.j2me.framework.protocol.ProtoBasePoi;
import com.telenav.j2me.framework.protocol.ProtoBasePoiExtraInfo;
import com.telenav.j2me.framework.protocol.ProtoBizPoi;
import com.telenav.j2me.framework.protocol.ProtoFavoriteCategory;
import com.telenav.j2me.framework.protocol.ProtoGpsFix;
import com.telenav.j2me.framework.protocol.ProtoGpsList;
import com.telenav.j2me.framework.protocol.ProtoProperty;
import com.telenav.j2me.framework.protocol.ProtoStop;
import com.telenav.j2me.framework.protocol.ProtoStopPoiWrapper;
import com.telenav.j2me.framework.protocol.ProtoUserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * ConstructorUtil.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-28
 */
public class UnittestUtil {
	/**============================================================================
	 * ===============================  used very frequently  ============================
	 * ============================================================================
	 */
//	public static Element createElementRoot(String xmlPath) throws SAXException, IOException, ParserConfigurationException{
//		XMLfileSerializer x = new XMLfileSerializer();
//		return x.read(xmlPath);
//		//return createElementRoot1(xmlPath);
//	}
	public static void main(String[] args) throws Exception, IOException, ParserConfigurationException {
		//UnittestUtil.createElementRoot("resource/testAbstractRuleMatchHolder.xml");
	}
//	public static Element createElementRoot1 (String xmlPath) throws SAXException, IOException, ParserConfigurationException{
//		ClassLoader cl = Thread.currentThread().getContextClassLoader();
//		InputStream stream =  cl.getResourceAsStream(xmlPath);
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		factory.setValidating(false);
//		Document doc = factory.newDocumentBuilder().parse(stream); 
//		Element root = doc.getDocumentElement(); 
//		return root;
//	}
	public static TnContext createTnContext(){
		TnContext tnContext = new TnContext();
		tnContext.addProperty(TnContext.PROP_MAP_DATASET, UTConstant.TnContext_PROP_MAP_DATASET);
		return tnContext;
	}
	public static UserProfile createUserProfile(){
		UserProfile userProfile = new UserProfile();
		//set properties
		userProfile.setCarrier(UTConstant.USERPROFILE_CARRIER);
		userProfile.setPlatform(UTConstant.USERPROFILE_PLATFORM);
		userProfile.setVersion(UTConstant.USERPROFILE_VERSION);
		userProfile.setProduct(UTConstant.USERPROFILE_PRODUCT);
		userProfile.setDevice(UTConstant.USERPROFILE_DEVICE);
		userProfile.setLocale(UTConstant.USERPROFILE_LOCALE);
		userProfile.setRegion(UTConstant.USERPROFILE_REGION);
		userProfile.setMin(UTConstant.USERPROFILE_MIN);
		userProfile.setUserId(UTConstant.USERPROFILE_USERID);
		userProfile.setMapDpi(UTConstant.USERPROFILE_MAPDPI);
		userProfile.setProgramCode(UTConstant.USERPROFILE_PROGRAMCODE);
		//set attribute map
		userProfile.setAttribute(UserProfile.ARRT_RESOLUTION,UTConstant.USERPROFILE_RESOLUTION);
		return userProfile;
	}
	
	public static UserProfile createATT70UserProfile(){
		UserProfile userProfile = new UserProfile();
		//set properties
		userProfile.setProgramCode("ATTNAVPROG");
		userProfile.setPlatform("IPHONE");
		userProfile.setVersion("7.0.01");
		userProfile.setDevice("9800");
		userProfile.setProduct("ATT_NAV");
		//set attribute map
		userProfile.setAttribute(UserProfile.ARRT_RESOLUTION,UTConstant.USERPROFILE_RESOLUTION);
		return userProfile;
	}
	
	
	public static MockTestHolder createMockTestHolder()
	{
		MockTestHolder holder=new MockTestHolder();
		holder.setConfigPath("test");
		holder.setName("mockTestHolder");
		LoadOrders orders=new LoadOrders();
		orders.addOrderString("programcode,platform,version");
		holder.setPrefixStructureOrders(orders);
		orders=new LoadOrders();
		orders.addOrderString("product,device_resolution");
		holder.setFloatingStructureOrders(orders);
		holder.setSuffixStructureOrders(new LoadOrders());
		holder.setType("resource_bundle");
		return holder;
	}
	
	
	
	/**============================================================================
	 * ===============================  About protocol  ============================
	 * ============================================================================
	 */
	public static ProtoStop.Builder getProtoStop_builder(){
		ProtoStop.Builder builder = ProtoStop.newBuilder();
		
		builder.setLat(UTConstant.PROTOSTOP_LAT);
		builder.setLon(UTConstant.PROTOSTOP_LON);
		builder.setStopType(UTConstant.PROTOSTOP_STOPTYPE);
		builder.setStopStatus(UTConstant.PROTOSTOP_STOPSTATUS);
		builder.setIsGeocoded(UTConstant.PROTOSTOP_ISGEOCODED);
		builder.setHashCode(UTConstant.PROTOSTOP_HASHCODE);
		builder.setIsShareAddress(UTConstant.PROTOSTOP_ISSHAREADDRESS);
		builder.setLabel(UTConstant.PROTOSTOP_LABEL);
		builder.setFirstLine(UTConstant.PROTOSTOP_FIRSTLINE);
		builder.setCity(UTConstant.PROTOSTOP_CITY);
		builder.setState(UTConstant.PROTOSTOP_STATE);
		builder.setStopId(UTConstant.PROTOSTOP_STOPID);
		builder.setZip(UTConstant.PROTOSTOP_ZIP);
		builder.setCountry(UTConstant.PROTOSTOP_COUNTRY);
		builder.setCounty(UTConstant.PROTOSTOP_COUNTY);
		builder.setLastLine(UTConstant.PROTOSTOP_LASTLINE);
		builder.setStreetName(UTConstant.PROTOSTOP_STREETNAME);
		builder.setCrossStreetName(UTConstant.PROTOSTOP_CROSSSTREETNAME);
		builder.setDoor(UTConstant.PROTOSTOP_DOOR);
		
		return builder;
	}
	public static ProtoStop createProtoStop(){
		return getProtoStop_builder().build();
	}
	public static ProtoUserProfile createProtoUserProfile() throws Exception{
		com.telenav.j2me.framework.protocol.ProtoUserProfile.Builder builder = 
			Whitebox.invokeConstructor(com.telenav.j2me.framework.protocol.ProtoUserProfile.Builder.class);
		builder.setMin(UTConstant.USERPROFILE_MIN);
		builder.setUserId(UTConstant.USERPROFILE_USERID);
		builder.setPassword(UTConstant.USERPROFILE_PASSWORD);
		builder.setEqpin(UTConstant.USERPROFILE_EQPIN);
		builder.setLocale(UTConstant.USERPROFILE_LOCALE);
		builder.setRegion(UTConstant.USERPROFILE_REGION);
		builder.setSsoToken(UTConstant.USERPROFILE_SSOTOKEN);
		builder.setGuideTone(UTConstant.USERPROFILE_GUIDETONE);
		builder.setProgramCode(UTConstant.USERPROFILE_PROGRAMCODE);
		builder.setProduct(UTConstant.USERPROFILE_PRODUCT);
		builder.setPlatform(UTConstant.USERPROFILE_PLATFORM);
		builder.setDevice(UTConstant.USERPROFILE_DEVICE);
		builder.setVersion(UTConstant.USERPROFILE_VERSION);
		builder.setBuildNumber(UTConstant.USERPROFILE_BUILDNUMBER);
		builder.setGpsType(UTConstant.USERPROFILE_GPSTYPE);
		builder.setAudioFormat(UTConstant.USERPROFILE_AUDIOFORMAT);
		builder.setImageType(UTConstant.USERPROFILE_IMAGETYPE);
		builder.setAudioLevel(UTConstant.USERPROFILE_AUDIOLEVEL);
		builder.setDataProcessType(UTConstant.USERPROFILE_DATAPROCESS);
		builder.setScreenWidth(UTConstant.USERPROFILE_SCREENWIDTH);
		builder.setScreenHeight(UTConstant.USERPROFILE_SCREENHEIGHT);
		builder.setPtnSource(UTConstant.USERPROFILE_PTNSOURCE);
		builder.setDeviceCarrier(UTConstant.USERPROFILE_CARRIER);
		
		Constructor<?>[] cms = com.telenav.j2me.framework.protocol.ProtoUserProfile.class.getDeclaredConstructors();
		Constructor cm = cms[0];
		cm.setAccessible(true);
		ProtoUserProfile pUserProfile = (ProtoUserProfile)cm.newInstance(builder);
		return pUserProfile;
	}
	
	public static ProtoGpsList createProtoGpsList() throws Exception{
		com.telenav.j2me.framework.protocol.ProtoGpsList.Builder builder =  
			Whitebox.invokeConstructor(com.telenav.j2me.framework.protocol.ProtoGpsList.Builder.class);
		ProtoGpsFix protoGpsFix0 = createProtoGpsFix(1);
		ProtoGpsFix protoGpsFix1 = createProtoGpsFix(-1);
		
		builder.addElementGpsfix(protoGpsFix0);
		builder.addElementGpsfix(protoGpsFix1);
		Constructor<?>[] cms = com.telenav.j2me.framework.protocol.ProtoGpsList.class.getDeclaredConstructors();
		Constructor cm = cms[0];
		cm.setAccessible(true);
		ProtoGpsList protoGpsList = (ProtoGpsList)cm.newInstance(builder);
		return protoGpsList;
	}
	
	public static ProtoGpsFix createProtoGpsFix(long timeLag) throws Exception{
		com.telenav.j2me.framework.protocol.ProtoGpsFix.Builder builder = 
			Whitebox.invokeConstructor(com.telenav.j2me.framework.protocol.ProtoGpsFix.Builder.class);
		builder.setTimeTag(UTConstant.PROTOGPSFIX_TIMETAG);
		if(timeLag <= 0 ) builder.setTimeTag(timeLag);
		builder.setLatitude(UTConstant.PROTOGPSFIX_LATITUDE);
		builder.setLontitude(UTConstant.PROTOGPSFIX_LONTITUDE);
		builder.setSpeed(UTConstant.PROTOGPSFIX_SPEED);
		builder.setHeading(UTConstant.PROTOGPSFIX_HEADING);
		builder.setType(UTConstant.PROTOGPSFIX_TYPE);
		builder.setErrorSize(UTConstant.PROTOGPSFIX_ERRORSIZE);
		
		
		Constructor<?>[] cms = com.telenav.j2me.framework.protocol.ProtoGpsFix.class.getDeclaredConstructors();
		Constructor cm = cms[0];
		cm.setAccessible(true);
		ProtoGpsFix protoGpsFix = (ProtoGpsFix)cm.newInstance(builder);
		return protoGpsFix;
	}
	public static Stop createStop(){
		Stop stop = new Stop();;
		stop.lat = UTConstant.STOP_LAT;
		stop.lon = UTConstant.STOP_LON;
		stop.stopType = UTConstant.STOP_STOPTYPE;
		stop.stopStatus = UTConstant.STOP_STOPSTATUS;
		stop.isGeocoded = UTConstant.STOP_ISGEOCODED;
		stop.hashCode = UTConstant.STOP_HASHCODE;
		stop.isShareAddress = UTConstant.STOP_ISSHAREADDRESS;
		stop.label = UTConstant.STOP_LABEL;
		stop.firstLine = UTConstant.STOP_FIRSTLINE;
		stop.city = UTConstant.STOP_CITY;
		stop.state = UTConstant.STOP_STATE;
		stop.stopId = UTConstant.STOP_STOPID;
		stop.zip = UTConstant.STOP_ZIP;
		stop.country = UTConstant.STOP_COUNTRY;
		stop.county = UTConstant.STOP_COUNTY;
		return stop;
	} 
	public static BizPOI createBizPOI(){
		BizPOI bizPOI = new BizPOI();
		bizPOI.supportInfo = UTConstant.BIZPOI_SUPPORTINFO;
		bizPOI.brand = UTConstant.BIZPOI_BRAND;
	    bizPOI.phoneNumber = UTConstant.BIZPOI_PHONENUMBER;
	    bizPOI.navigable = UTConstant.BIZPOI_NAVIGABLE;
	    bizPOI.distance = UTConstant.BIZPOI_DISTANCE;
	    bizPOI.supplementalInfo = UTConstant.BIZPOI_SUPPLEMENTALINFO;
	    bizPOI.parentCatName = UTConstant.BIZPOI_PARENTCATNAME;
	    bizPOI.price = UTConstant.BIZPOI_PRICE;
	    bizPOI.groupID = UTConstant.BIZPOI_GROUPID;
	    bizPOI.address = UTConstant.BIZPOI_ADDRESS;
	    bizPOI.layout = UTConstant.BIZPOI_LAYOUT;
	    bizPOI.vendorCode = UTConstant.BIZPOI_VENDORCODE;
	    
	    return bizPOI;
	}
	public static POI createPOI(){
		POI poi = new POI();
		poi.avgRating = UTConstant.POI_AVGRATING;
		poi.popularity = UTConstant.POI_POPULARITY;
		poi.isRatingEnable = UTConstant.POI_ISRATINGENABLE;
		poi.hasUserRatedThisPoi = UTConstant.POI_HASUSERRATEDTHISPOI;
		poi.poiId = UTConstant.POI_POIID;
		
		return poi;
	}
	public static StopPoiWrapper createStopPoiWrapper(){
		StopPoiWrapper stopPoiWrapper = new StopPoiWrapper();
		 stopPoiWrapper.type = UTConstant.STOPPOIWRAPPER_TYPE;
	     stopPoiWrapper.status = UTConstant.STOPPOIWRAPPER_STATUS;
	     stopPoiWrapper.label = UTConstant.STOPPOIWRAPPER_LABEL;
	     stopPoiWrapper.id = UTConstant.STOPPOIWRAPPER_ID;
	     stopPoiWrapper.sharedFromUser = UTConstant.STOPPOIWRAPPER_SHAREDFROMUSER;
	     stopPoiWrapper.sharedFromPTN = UTConstant.STOPPOIWRAPPER_SHAREDFROMPTN;
	     
	     return stopPoiWrapper;
	}
	public static FavoriteCategory createFavoriteCategory(){
		FavoriteCategory f = new FavoriteCategory();
		f.id = UTConstant.FAVORITECATEGORY_ID;
	    f.catType = UTConstant.FAVORITECATEGORY_CATTYPE;
	    f.name = UTConstant.FAVORITECATEGORY_NAME;
	    f.status = UTConstant.FAVORITECATEGORY_STATUS;
		return f;
	}
	public static ProtoBizPoi.Builder getProtoBizPoi_builder(){
		ProtoBizPoi.Builder builder = ProtoBizPoi.newBuilder();
		builder.setBrand(UTConstant.PROTOBIZPOI_BRAND);
		builder.setPhoneNumber(UTConstant.PROTOBIZPOI_PHONENUMBER);
		builder.setCategoryId(UTConstant.PROTOBIZPOI_CATEGORYID);
		builder.setGroupID(UTConstant.PROTOBIZPOI_GROUPID);
		builder.setNavigable(UTConstant.PROTOBIZPOI_NAVIGABLE);
		builder.setDistance(UTConstant.PROTOBIZPOI_DISTANCE);
		builder.setParentCatName(UTConstant.PROTOBIZPOI_PARENTCATNAME);
		builder.setPrice(UTConstant.PROTOBIZPOI_PRICE);
		builder.setLayout(UTConstant.PROTOBIZPOI_LAYOUT);
		builder.setVendorCode(UTConstant.PROTOBIZPOI_VENDORCODE);
		builder.setAddress(UTConstant.PROTOBIZPOI_ADDRESS);
		builder.addElementSupportInfos(UTConstant.PROTOBIZPOI_SUPPORTINFO);
		builder.addElementSupplementalInfos(UTConstant.PROTOBIZPOI_SUPPLEMENTALINFO);
		
		return builder;
	}
	public static ProtoBizPoi createProtoBizPoi(){
		return getProtoBizPoi_builder().build();
	}
	
	public static ProtoBasePoiExtraInfo.Builder getProtoBasePoiExtraInfo_bulder(){
		ProtoBasePoiExtraInfo.Builder builder = ProtoBasePoiExtraInfo.newBuilder();
		builder.setUserPreviousRating(UTConstant.PROTOBASEPOIEXTRAINFO_USERPREVIOUSRATING);
		builder.setPoiNameAudio(UTConstant.PROTOBASEPOIEXTRAINFO_POINAMEAUDIO);
		builder.setShortMessage(UTConstant.PROTOBASEPOIEXTRAINFO_SHORTMESSAGE);
		builder.setMessage(UTConstant.PROTOBASEPOIEXTRAINFO_MESSAGE);
		builder.setAdSource(UTConstant.PROTOBASEPOIEXTRAINFO_ADSOURCE);
		builder.setSourceAdId(UTConstant.PROTOBASEPOIEXTRAINFO_SOURCEADID);
		builder.setIsReservable(UTConstant.PROTOBASEPOIEXTRAINFO_ISRESERVABLE);
		builder.setPartnerPoiId(UTConstant.PROTOBASEPOIEXTRAINFO_PARTNERPOIID);
		builder.setPartner(UTConstant.PROTOBASEPOIEXTRAINFO_PARTNER);
		builder.setRatingNumber(UTConstant.PROTOBASEPOIEXTRAINFO_RATINGNUMBER);
		
		return builder;
	}
	
	public static ProtoBasePoiExtraInfo createProtoBasePoiExtraInfo(){
		return getProtoBasePoiExtraInfo_bulder().build();
	}
	
	
	public static ProtoProperty.Builder getProtoProperty_builder(){
		ProtoProperty.Builder builder = ProtoProperty.newBuilder();
		builder.setKey(UTConstant.PROTOPROPERTY_BUILDER_KEY);
		builder.setValue(UTConstant.PROTOPROPERTY_BUILDER_VALUE);
		return builder;
	}
	public static ProtoProperty createProtoProperty(){
		return getProtoProperty_builder().build();
	}
	
	public static ProtoBasePoi.Builder getProtoBasePoi_builder(){
		ProtoBasePoi.Builder builder = ProtoBasePoi.newBuilder();
		builder.setAvgRating(UTConstant.PROTOBASEPOI_AVGRATING);
		builder.setIsRatingEnable(UTConstant.PROTOBASEPOI_ISRATINGENABLE);
		builder.setHasUserRatedThisPoi(UTConstant.PROTOBASEPOI_HASUSERRATEDTHISPOI);
		builder.setPoiId(UTConstant.PROTOBASEPOI_POIID);
		builder.setPopularity(UTConstant.PROTOBASEPOI_POPULARITY);
		return builder;
	}
	public static ProtoBasePoi createProtoBasePoi(){
		return getProtoBasePoi_builder().build();
	}
	
	public static ProtoStopPoiWrapper.Builder getProtoStopPoiWrapper_builder(){
		ProtoStopPoiWrapper.Builder builder = ProtoStopPoiWrapper.newBuilder();
		builder.setType(UTConstant.PROTOSTOPPOIWRAPPER_TYPE);
		builder.setStatus(UTConstant.PROTOSTOPPOIWRAPPER_STATUS);
		builder.setId(UTConstant.PROTOSTOPPOIWRAPPER_ID);
		builder.setLabel(UTConstant.PROTOSTOPPOIWRAPPER_LABEL);
		builder.setSharedFromUser(UTConstant.PROTOSTOPPOIWRAPPER_SHAREDFROMUSER);
		builder.setSharedFromPTN(UTConstant.PROTOSTOPPOIWRAPPER_SHAREDFROMPTN);
		builder.setCreateTime(UTConstant.PROTOSTOPPOIWRAPPER_CREATETIME);
		builder.setUpdateTime(UTConstant.PROTOSTOPPOIWRAPPER_UPDATETIME);
		return builder;
	}
	public static ProtoStopPoiWrapper createProtoStopPoiWrapper(){
		return getProtoStopPoiWrapper_builder().build();
	}
	
	public static ProtoFavoriteCategory.Builder getProtoFavoriteCategory_builder(){
		ProtoFavoriteCategory.Builder builder = ProtoFavoriteCategory.newBuilder();
		builder.setId(UTConstant.PROTOFAVORITECATEGORY_ID);
		builder.setCatType(UTConstant.PROTOFAVORITECATEGORY_CATTYPE);
		builder.setName(UTConstant.PROTOFAVORITECATEGORY_NAME);
		builder.setStatus(UTConstant.PROTOFAVORITECATEGORY_STATUS);
		builder.setCreateTime(UTConstant.PROTOFAVORITECATEGORY_CREATETIME);
		builder.setUpdateTime(UTConstant.PROTOFAVORITECATEGORY_UPDATETIME);
		return builder;
	}
	public static ProtoFavoriteCategory createProtoFavoriteCategory(){
		return getProtoFavoriteCategory_builder().build();
	}
	/**============================================================================
	 * ===============================  About resource path  ============================
	 * ============================================================================
	 */
	public static String getAbsURLPath(String relativePath){
		URL url = Thread.currentThread().getContextClassLoader().getResource(relativePath);
        if (url == null) return "";
        return url.getFile();
	}
	public static String getAbsPath(String relativePath){
		String s = getAbsURLPath(relativePath);
		if(s.startsWith("/")){
			s = s.substring(1);
		}
		//s = s.replaceAll("/", "\\\\");
		// "\\" has something wrong on Linux or Unix
		s = s.replace('/', File.separatorChar);
		return s;
		
	}
	
	
	/**============================================================================
	 * ===============================  About JSON  ============================
	 * ============================================================================
	 */
	public static String getJSONRequestJsonArray(){
		String ret=
			"{JSONRequest:[  { "+UTConstant.JSONARRAY_EXECUTORTYPE_KEY+":"+UTConstant.EXECUTORTYPE+"," +
							//====================profile data==========
							  "profile:" +
							  			"{	Min: " + UTConstant.USERPROFILE_MIN + ", " +
							  			   "Password: " + UTConstant.USERPROFILE_PASSWORD + ", " +
							  			   "UserId: " + UTConstant.USERPROFILE_USERID + ", " +
							  			   "Eqpin: " + UTConstant.USERPROFILE_EQPIN + ", " +
							  			   "Locale: " + UTConstant.USERPROFILE_LOCALE + ", " +
							  			   "Region: " + UTConstant.USERPROFILE_REGION + ", " +
							  			   "SsoToken: " + UTConstant.USERPROFILE_SSOTOKEN + ", " +
							  			   "GuideTone: " + UTConstant.USERPROFILE_GUIDETONE + ", " +
							  			   "Carrier: " + UTConstant.USERPROFILE_CARRIER + ", " +
							  			   "Platform: " + UTConstant.USERPROFILE_PLATFORM + ", " +
							  			   "Version: " + UTConstant.USERPROFILE_VERSION + ", " +
							  			   "Device: " + UTConstant.USERPROFILE_DEVICE + ", " +
							  			   "BuildNumber: " + UTConstant.USERPROFILE_BUILDNUMBER + ", " +
							  			   "GpsType: " + UTConstant.USERPROFILE_GPSTYPE + ", " +
							  			   "Product: " + UTConstant.USERPROFILE_PRODUCT + ", " +
							  			   "AudioFormat: " + UTConstant.USERPROFILE_AUDIOFORMAT + ", " +
							  			   "ImageType: " + UTConstant.USERPROFILE_IMAGETYPE + ", " +
							  			   "AudioLevel: " + UTConstant.USERPROFILE_AUDIOLEVEL + ", " +
							  			   "DataProcess: " + UTConstant.USERPROFILE_DATAPROCESS + ", " +
							  			   "ScreenWidth: " + UTConstant.USERPROFILE_SCREENWIDTH + ", " +
							  			   "ScreenHeight: " + UTConstant.USERPROFILE_SCREENHEIGHT +
							  			"}" +
							  		  "," +
							  //==================gps data============
							  "gps:[" +
							  		  "{TimeTag:1,  Lat:2, Lon:3, Speed:4, Heading:5, Type: 65, ErrorSize:7}," +
									  "{TimeTag:-1, Lat:2, Lon:3, Speed:4, Heading:5, Type: 65, ErrorSize:7}" +
								  "]" +
						    "}" +
						 "]"+
	        "}";
		
		return ret;
	}
	public static String getSampleJsonString(){
		String ret=
			"{	 name: Jim," +
				"age : 20," + 
				"gender : male," +
				"hobby: football," +
				"weight : 65(KG)," +
				"height : 175(cm)" +
	        "} ";
		
		return ret;
	}
	public static JSONObject getSampleJSONObject() throws JSONException{
		JSONObject obj = new JSONObject(getSampleJsonString());
		return obj;
	}
	public static String getSampleJsonArray(){
		String ret=
			"{ programmers: [ { firstName: Brett, " +
								   "lastName:McLaughlin, " +
								   "email: aaaa " +"}, " +
									  
								   "{ firstName: Jason, " +
									 "lastName:Hunter, " +
									 "email: bbbb }," +
									  
								   "{ firstName: Elliotte, " +
								     "lastName:Harold, " +
								     "email: cccc }]," +
								     
	          "authors: [{firstName: Isaac, " +
	                               "lastName: Asimov, " +
	                               "genre: science fiction }," +
	                               
	                             "{ firstName: Tad, " +
	                               "lastName: Williams, " +
	                               "genre: fantasy }, " +
	                               
	                             "{ firstName: Frank, " +
	                               "lastName: Peretti, " +
	                               "genre: christian fiction }]," +
	                               
	          "musicians: [{ firstName: Eric, " +
	                               "lastName: Clapton, " +
	                               "instrument: guitar }, " +
	                               
	                             "{ firstName: Sergei, " +
	                               "lastName: Rachmaninoff, " +
	                               "instrument: piano }]" +
	        "} ";
		
		return ret;
	}
	
	
	/**============================================================================
	 * ===============================  About Exception  ============================
	 * ============================================================================
	 */
	private static String printExceptionMsg(String msg,Exception e){
		String ret = "\n========== Catched ";
		ret = ret.concat(e.getClass().getName()).concat(" in Unit Test. ==========\n")
					.concat(msg).concat("\n")
					.concat("Error Message is: ").concat(e.getMessage()==null?"":e.getMessage())
					.concat("\n====================================================================================================");
		System.out.println(ret);
		return ret;
	}
	public static String printExceptionMsg(Exception e){
		return printExceptionMsg(UTConstant.DO_NOT_WORRY_EXCEPTION,e);
	}
	
	
	/**============================================================================
	 * ===============================  About TxNode  ============================
	 * ============================================================================
	 */
	public static TxNode createTxNode(boolean moreThanOneChild){
		TxNode root = new TxNode();
		root.addChild(getChildNode());
		if(moreThanOneChild){
			root.addChild(getChildNode());
		}
		return root;
	}
	private static TxNode getChildNode(){
		TxNode child = new TxNode();
		child.addMsg("1");
		child.addMsg("2");
		child.addMsg("3");
		child.addMsg("4");
		child.addMsg("5");
		child.addMsg("6");
		child.addMsg("204");
		child.addMsg("8");
		child.addMsg("9");
		
		return child;
	}
	public static byte[] getTxNodeBytes(boolean moreThanOneChild){
		TxNode root = createTxNode(moreThanOneChild);
		return TxNode.toByteArray(root);
	}

}
