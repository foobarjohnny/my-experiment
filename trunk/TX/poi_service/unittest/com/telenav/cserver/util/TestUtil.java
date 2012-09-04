/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.j2me.framework.protocol.ProtoGpsList;
import com.telenav.j2me.framework.protocol.ProtoMultiRouteResp;
import com.telenav.j2me.framework.protocol.ProtoUserProfile;
import com.telenav.j2me.framework.util.ProtocolBufferUtil;
import com.telenav.j2me.datatypes.DataConstants;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.sso.SsoToken;
import com.telenav.kernel.sso.SsoTokenManager;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * TestUtil.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-5-11
 */
public class TestUtil
{
	public static final String poiCServerUrl = "http://s-tn60-rim-poi.telenav.com:8080/poi_service/telenav-server-pb";
	//public static final String poiCServerUrl = "http://172.16.10.15:8680/poi_service/telenav-server-pb";
	
	public static ProtoUserProfile getUserProfile()
	{
		ProtoUserProfile.Builder builder = ProtoUserProfile.newBuilder();
		builder.setMin("4085070020");
		builder.setPassword("0020");
		builder.setUserId("3599697");
		builder.setEqpin("eq_pin");
		builder.setLocale("en_US");
		builder.setRegion("NA");
		
		builder.setProgramCode("ATT");
		builder.setPlatform("RIM");
		builder.setVersion("6.0.01");
		builder.setDevice("9000");
		builder.setBuildNumber("2259");
		builder.setGpsType("AGPS");
		builder.setProduct("ATT_NAV");
		
		builder.setAudioFormat("mp3");
		builder.setImageType("PNG");
		builder.setAudioLevel("3");
		builder.setScreenWidth("480");
		builder.setScreenHeight("800");
		return builder.build();
	}
	
	public static TxNode getMandantoryNode(String ptn, String pin)
	{
		TxNode profileNode = new TxNode();
		
		String userId="28088363";
		
		profileNode.addMsg("profile");
		TxNode clientInfoNode = new TxNode();
		profileNode.addChild(clientInfoNode);
		clientInfoNode.addValue(DataConstants.USER_INFO);
		clientInfoNode.addMsg(ptn);
		clientInfoNode.addMsg(pin);
		clientInfoNode.addMsg(userId);
		clientInfoNode.addMsg("eq_pin");
		clientInfoNode.addMsg("en_US");
		clientInfoNode.addMsg("NA");
        clientInfoNode.addMsg(createSsoToken(userId));
		
		TxNode deviceInfoNode = new TxNode();
		profileNode.addChild(deviceInfoNode);
		deviceInfoNode.addValue(DataConstants.CLIENT_VERSION);
		deviceInfoNode.addMsg("ATT");
		deviceInfoNode.addMsg("ANDROID");
		deviceInfoNode.addMsg("6.2.01");
		deviceInfoNode.addMsg("passion");
		deviceInfoNode.addMsg("2259");
		deviceInfoNode.addMsg("AGPS");
		deviceInfoNode.addMsg("ATT_NAV");
		
		TxNode userPrefsNode = new TxNode();
		profileNode.addChild(userPrefsNode);
		userPrefsNode.addValue(DataConstants.USER_PREFS);
		userPrefsNode.addMsg("mp3");
		userPrefsNode.addMsg("PNG");
		userPrefsNode.addMsg("3");
		return profileNode;
		
	}
	
	public static ProtoUserProfile getProtoMandatory()
	{
		String userId = "28088363";
		ProtoUserProfile.Builder profileBuilder = ProtoUserProfile.newBuilder();
		profileBuilder.setMin("4085054859");
		profileBuilder.setPassword("4859");
		profileBuilder.setUserId(userId);
		profileBuilder.setEqpin("eq_pin");
		profileBuilder.setLocale("en_US");
		profileBuilder.setRegion("NA");
		profileBuilder.setSsoToken(createSsoToken(userId));
		
		profileBuilder.setProgramCode("ATT");
		profileBuilder.setPlatform("ANDROID");
		profileBuilder.setVersion("7.0.01");
		profileBuilder.setDevice("passion");
		profileBuilder.setBuildNumber("2259");
		profileBuilder.setGpsType("AGPS");
		profileBuilder.setProduct("ATT_NAV");
		
		profileBuilder.setAudioFormat("mp3");
		profileBuilder.setImageType("PNG");
		profileBuilder.setAudioLevel("3");
		profileBuilder.setScreenWidth("800");
		profileBuilder.setScreenHeight("480");
		return profileBuilder.build();
	}
	
	public static TnContext getTnContext() {
		TnContext tnContext = new TnContext();
		tnContext.addProperty(TnContext.PROP_CARRIER, "ATT");
		tnContext.addProperty(TnContext.PROP_DEVICE, "8900");
		tnContext.addProperty(TnContext.PROP_PRODUCT, "RIM");
		tnContext.addProperty(TnContext.PROP_VERSION, "6.0.01");
		tnContext.addProperty("application", "ATT_NAV");
		tnContext.addProperty("login", "4085057537");
		tnContext.addProperty("userid", "9826225");

		return tnContext;
	}
	
	public static UserProfile getProfile() {
		UserProfile userProfile = new UserProfile();
		userProfile.setMin("4085057537");
		userProfile.setPassword("7537");

		userProfile.setVersion("6.0.01");
		userProfile.setPlatform("RIM");
		userProfile.setDevice("8900");
		userProfile.setLocale("en_US");
		userProfile.setCarrier("ATT");
		userProfile.setProduct("ATT");
		userProfile.setRegion("");
		userProfile.setScreenWidth("480");
		userProfile.setScreenHeight("360");
		userProfile.setProgramCode("ATT");
		userProfile.setAudioFormat("amr");
		userProfile.setUserId("9826225");

		return userProfile;
	}
	
	public static TxNode getMandantoryNode()
	{
		TxNode profileNode = new TxNode();		
		String userId="28088363";
		profileNode.addMsg("profile");
		TxNode clientInfoNode = new TxNode();
		profileNode.addChild(clientInfoNode);
		clientInfoNode.addValue(DataConstants.USER_INFO);
		clientInfoNode.addMsg("4085054859");
		clientInfoNode.addMsg("4859");
		clientInfoNode.addMsg(userId);
		clientInfoNode.addMsg("eq_pin");
		clientInfoNode.addMsg("en_US");
		clientInfoNode.addMsg("NA");
        clientInfoNode.addMsg(createSsoToken(userId));
		
		TxNode deviceInfoNode = new TxNode();
		profileNode.addChild(deviceInfoNode);
		deviceInfoNode.addValue(DataConstants.CLIENT_VERSION);
		deviceInfoNode.addMsg("ATT");
		deviceInfoNode.addMsg("ANDROID");
		deviceInfoNode.addMsg("6.2.01");
		deviceInfoNode.addMsg("passion");
		deviceInfoNode.addMsg("2259");
		deviceInfoNode.addMsg("AGPS");
		deviceInfoNode.addMsg("ATT_NAV");
		
		TxNode userPrefsNode = new TxNode();
		profileNode.addChild(userPrefsNode);
		userPrefsNode.addValue(DataConstants.USER_PREFS);
		userPrefsNode.addMsg("mp3");
		userPrefsNode.addMsg("PNG");
		userPrefsNode.addMsg("3");
		userPrefsNode.addMsg("");
		userPrefsNode.addMsg("en_US");
		userPrefsNode.addMsg("NA");
		return profileNode;
		
	}
	
	private static String createSsoToken(String userId)
	{  	
	   SsoToken ssoToken=SsoTokenManager.createToken(Long.parseLong(userId), System.currentTimeMillis()+(long)1000*60*60*15);
	   return ssoToken==null?"":ssoToken.toString();
    }
	
	public static Stop getStop_CN()
	{
	    Stop stop = new Stop();

        stop.lat =3125724;
        stop.lon = 12142224;
        //
        stop.stopType = 1;
        stop.stopStatus = 0;
        stop.isGeocoded = true;
        stop.hashCode = 0;
        stop.isShareAddress = true;
        stop.label = "shanghai";
        stop.firstLine ="";
        stop.city = "SHANGHAI";
        stop.state = "";
        stop.stopId = "0";
        stop.country = "CN";
        return stop;
	}
	public static Stop getStopByType(int stopType)
	{
	    Stop stop = new Stop();
	    switch(stopType)
	    {
	    case Stop.STOP_GENERIC:
	        stop.stopType = Stop.STOP_GENERIC;
	        stop.firstLine = "1130 KIFER RD";
	        stop.city = "SYNNYVALE";
	        stop.state = "ca";
	        stop.lat = 3736564;
	        stop.lon = -12177790;
	        break;
	    case Stop.STOP_FAVORITE:
	        stop.stopType = Stop.STOP_FAVORITE;
	        stop.label = "telenav office";
	        stop.firstLine = "1130 KIFER RD";
            stop.city = "SYNNYVALE";
            stop.state = "ca";
            stop.lat = 3737391;
            stop.lon = -12199930;
	        break;
	    case Stop.STOP_RECENT:
	        stop.stopType = Stop.STOP_RECENT;
	        stop.label = "telenav office";
            stop.firstLine = "1130 KIFER RD";
            stop.city = "SYNNYVALE";
            stop.state = "ca";
            stop.lat = 3737391;
            stop.lon = -12199930;
	        break;
	    case Stop.STOP_AIRPORT:
	        stop.stopType = Stop.STOP_AIRPORT;
	        stop.label = "SFO";
	        stop.firstLine = "SFO";
	        stop.lat = 3761386;
	        stop.lon = -12239382;
	        
	        break;
	    case Stop.STOP_CITY:
	        stop.stopType = Stop.STOP_CITY;
	        stop.lat = 3737159;
	        stop.lon = -12203824;
	        stop.city = "SUNNYVALE";
	        stop.state = "ca";
	        break;
	    case 7: //poi
	        stop.stopType = 7;
	        stop.label = "KFC";
	        stop.firstLine = "1695 HOLLENBECK AVE";
	        stop.lat = 3733792;
	        stop.lon = -12204151;
	        break;
	    }
	    return stop;
	}
	public static Stop getStop(int id)
	{
		Stop stop = new Stop();
		if(id == 1)
		{
			/*
			 ------------ TxNode -----------
	        Values:
	        [0]=28
	        [1]=3735548
	        [2]=-12195426
	        [3]=5
	        [4]=0
	        [5]=1
	        [6]=0
	        [7]=0
	        binary exists ? = false
	        ------------ messages -----------
	        SANTA CLARA

	        SANTA CLARA
	        CA
	        0

	        US
	        ------------ end of TxNode ------
			*/
			//stop.lat = 3735548;
			
			//stop.lon = -12195426;
			stop.lat =3737158;
			stop.lon = -12203824;
			//
			stop.stopType = 1;
			stop.stopStatus = 0;
			stop.isGeocoded = true;
			stop.hashCode = 0;
			stop.isShareAddress = true;
			stop.label = "SANTA CLARA";
			stop.firstLine ="";
			stop.city = "SANTA CLARA";
			stop.state = "CA";
			stop.stopId = "0";
			stop.country = "US";
		}
		else if(id == 2)
		{
			/* ------------ TxNode -----------
	        Values:
	        [0]=28
	        [1]=3761386
	        [2]=-12239382
	        [3]=1
	        [4]=0
	        [5]=1
	        [6]=0
	        [7]=1
	        binary exists ? = false
	        ------------ messages -----------
	        SFO
	        SAN FRANCISCO INTERNATIONAL AIRPORT
	        BURLINGAME
	        CA
	        0
	        94128

	        ------------ end of TxNode ------
	 */
			
			stop.lat = 3761386;
			stop.lon = -12239382;
			stop.stopType = 1;
			stop.stopStatus = 0;
			stop.isGeocoded = true;
			stop.hashCode = 0;
			stop.isShareAddress = true;
			stop.label = "SFO";//SFO
			stop.firstLine = "SAN FRANCISCO INTERNATIONAL AIRPORT";// "SAN FRANCISCO INTERNATIONAL AIRPORT";
			stop.city = "BURLINGAME";//"BURLINGAME";
			stop.state = "CA";//"CA";
			stop.stopId = "0";
			stop.zip = "94128";
		}
		else if(id == 3)
		{

			stop.lat = 4071453;
			
			stop.lon = -7400713;
			
			stop.stopType = 1;
			stop.stopStatus = 0;
			stop.isGeocoded = true;
			stop.hashCode = 0;
			stop.isShareAddress = true;
//			stop.label = "SANTA CLARA";
//			stop.firstLine ="";
//			stop.city = "SANTA CLARA";
			stop.state = "NY";
			stop.stopId = "0";
			stop.country = "US";
		}
		else if(id == 4)
		{//37.78028,-122.399025
			stop.lat = 3778028;
			stop.lon = -12239902;
			stop.stopType = 1;
			stop.stopStatus = 0;
			stop.isGeocoded = true;
			stop.hashCode = 0;
			stop.isShareAddress = true;
			stop.label = "";
			stop.firstLine ="";
			stop.city = "";
			stop.state = "CA";
			stop.stopId = "0";
			stop.country = "US";
		}
		else if(id == 5)
		{
			stop.lat =4064615;
			stop.lon = -737869;
			stop.stopType = 1;
			stop.stopStatus = 0;
			stop.isGeocoded = true;
			stop.hashCode = 0;
			stop.isShareAddress = true;
			stop.label = "";
			stop.firstLine ="";
			stop.city = "";
			stop.state = "NY";
			stop.stopId = "0";
			stop.country = "US";
		}
		if(id == 6)//37.548351,-121.988557, Fremont, CA//37.859545,-122.487091, Sausalito, CA//33.938682,-118.093257, Santa Fe Springs, CA//37.844669,-122.297705, Emeryville, CA 
		{
			stop.lat = 3784466;
			stop.lon = -12229770;
			//
			stop.stopType = 1;
			stop.stopStatus = 0;
			stop.isGeocoded = true;
			stop.hashCode = 0;
			stop.isShareAddress = true;
			stop.label = "Emeryville";
			stop.firstLine ="";
			stop.city = "Emeryville";
			stop.state = "CA";
			stop.stopId = "0";
			stop.country = "US";
		}
		if(id == 7)
		{
			stop.lat = 3737890;
			stop.lon = -12201074;
			stop.stopType = 1;
			stop.stopStatus = 0;
			stop.isGeocoded = true;
			stop.hashCode = 0;
			stop.isShareAddress = true;
			stop.label = "Sunnyvale";
			stop.firstLine ="";
			stop.city = "Sunnyvale";
			stop.state = "CA";
			stop.stopId = "0";
			stop.country = "US";
			
		}
		return stop;
	}
	
    public static final int MAX_CHUNK = 4096 * 8; //32k
    
    public static int readBytes(InputStream is, byte[] buff, int offset, int len) throws Exception
    {
    	int bytesRead = 0;

    	while (bytesRead < len)
    	{
    		int nextChunk = len - bytesRead;
    		if (nextChunk > MAX_CHUNK)
    			nextChunk = MAX_CHUNK;
    		int count = is.read(buff, offset, nextChunk);

    		if (count < 0)
    			break;
    		bytesRead += count;
    		offset += count;
    	}

    	return bytesRead;
    }
    
    public static final int RESPONSE_STREAMING = 983276845;
    public static final int RESPONSE_STREAMING_COMPRESSED = 983276846;
    public static boolean readHttpChunkedContent(InputStream is, int[] routePathIds) throws Exception
    {
        byte[] lenBuf = new byte[4];
        while (true)
        {	
        	// read the length of chunk
	        int nRet = readBytes(is, lenBuf, 0, 4);
	        if (nRet != lenBuf.length)
	        { 	
	        	throw new Exception("read http streaming :: chunk length error [nRet = " + nRet + "]");
	        } 	
	        
	        // check length of this chunk, if length is zero, it means the end of the response 
	        //int len = DataUtil.readInt(lenBuf, 0);
	        int len = com.telenav.j2me.datatypes.DataUtil.readInt(lenBuf, 0);
	        if (len <= 0) return true;
	        
	        // read response mode
	        nRet = readBytes(is, lenBuf, 0, 4);
	        if (nRet != lenBuf.length)
	        { 	
	        	throw new Exception("read http streaming :: chunk response code error [nRet = " + nRet + "]");
	        }
	        
	        int dataLen = len - 4;
	        int chunkRespMode = com.telenav.j2me.datatypes.DataUtil.readInt(lenBuf, 0);
	        
	        if (chunkRespMode == RESPONSE_STREAMING || chunkRespMode == RESPONSE_STREAMING_COMPRESSED)
	        {
	            // read chunk n content
                byte[] data = new byte[dataLen];
                nRet = readBytes(is, data, 0, dataLen);
                if (nRet != dataLen)
                {	
                	throw new Exception("read http streaming chunk data failed [nRet = " + nRet + "]");
                }
                TxNode response = TxNode.fromByteArray(data, 0);
                System.out.println(response.toString());
                
                for(int i=0; i<response.childrenSize(); i++)
                {
                    TxNode child = response.childAt(i);
                    int type =(int) child.valueAt(0);
                    if(type == 0)
                    {
                        for(int j=0; j<child.childrenSize(); j++)
                        {
                            TxNode child1 = child.childAt(j);
                            int type1 = (int) child1.valueAt(0);
                            if(type1 == DataConstants.TYPE_MULTI_ROUTES)
                            {
                            	routePathIds[0] = (int)child1.valueAt(1);
                                System.out.println("Route Path Id : " + routePathIds[0]);
                            }
                        }
                    }
                }
 	        }
	        else
	        {
	            throw new Exception("chunk response code is wrong :: " + chunkRespMode);
	        }
	    }
    }
    
    public static boolean readProtoHttpChunkedContent(InputStream is, int[] routePathIds) throws Exception
    {
        byte[] lenBuf = new byte[4];
        while (true)
        {	
        	// read the length of chunk
	        int nRet = readBytes(is, lenBuf, 0, 4);
	        if (nRet != lenBuf.length)
	        { 	
	        	throw new Exception("read http streaming :: chunk length error [nRet = " + nRet + "]");
	        } 	
	        
	        // check length of this chunk, if length is zero, it means the end of the response 
	        //int len = DataUtil.readInt(lenBuf, 0);
	        int len = com.telenav.j2me.datatypes.DataUtil.readInt(lenBuf, 0);
	        if (len <= 0) return true;
	        
	        // read response mode
	        nRet = readBytes(is, lenBuf, 0, 4);
	        if (nRet != lenBuf.length)
	        { 	
	        	throw new Exception("read http streaming :: chunk response code error [nRet = " + nRet + "]");
	        }
	        
	        int dataLen = len - 4;
	        int chunkRespMode = com.telenav.j2me.datatypes.DataUtil.readInt(lenBuf, 0);
	        
	        if (chunkRespMode == RESPONSE_STREAMING || chunkRespMode == RESPONSE_STREAMING_COMPRESSED)
	        {
	            // read chunk n content
                byte[] data = new byte[dataLen];
                nRet = readBytes(is, data, 0, dataLen);
                if (nRet != dataLen)
                {	
                	throw new Exception("read http streaming chunk data failed [nRet = " + nRet + "]");
                }
                
                ProtocolBuffer[] buffer = ProtocolBufferUtil.toBufArray(data);
                for(int i = 0; i < buffer.length; i++)
                {
                	String type = buffer[i].getObjType();
                	byte[] bufferData = buffer[i].getBufferData();
                	if(type.equalsIgnoreCase("Proto_Multi_Route"))
                	{
                		ProtoMultiRouteResp mp = ProtoMultiRouteResp.parseFrom(bufferData);
                		System.out.println("ProtoMultiRouteResp : " + mp.toString());
                	}
                	else if(type.equalsIgnoreCase("Proto_Route_Audio"))
                	{
                		//ProtoRouteAudioResp ap = ProtoRouteAudioResp.parseFrom(bufferData);
                		//System.out.println("ProtoRouteAudioResp : " + ap.toString());
                	}
                	System.out.println("type : " + type + "/size : " + bufferData.length);
                }
 	        }
	        else
	        {
	            throw new Exception("chunk response code is wrong :: " + chunkRespMode);
	        }
	    }
    }
    
    public static void buildUserProfile(ByteArrayOutputStream baos, ProtoUserProfile profile) throws Exception
    {	
		byte[] objType = new String("profile").getBytes();
		byte[] objTypeLen = new byte[4];
		com.telenav.j2me.datatypes.DataUtil.writeInt(objTypeLen, objType.length, 0);
		baos.write(objTypeLen);
		baos.write(objType);		
		byte[] objLen = new byte[4];
		com.telenav.j2me.datatypes.DataUtil.writeInt(objLen, profile.toByteArray().length, 0);
		baos.write(objLen);
		baos.write(profile.toByteArray());
    }
    
    public static void buildGpsList(ByteArrayOutputStream baos, ProtoGpsList list) throws Exception
    {
		byte[] objType = new String("gps").getBytes();
		byte[] objTypeLen = new byte[4];
		com.telenav.j2me.datatypes.DataUtil.writeInt(objTypeLen, objType.length, 0);
		baos.write(objTypeLen);
		baos.write(objType);		
		byte[] objLen = new byte[4];
		com.telenav.j2me.datatypes.DataUtil.writeInt(objLen, list.toByteArray().length, 0);
		baos.write(objLen);
		baos.write(list.toByteArray());
    }
}
