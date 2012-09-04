package com.telenav.cserver.poi.protobuf;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

import com.telenav.cserver.util.TestUtil;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoBannerAdsReq;
import com.telenav.j2me.framework.protocol.ProtoShareAddressResp;
import com.telenav.j2me.framework.protocol.ProtoUserProfile;
import com.telenav.j2me.framework.util.ProtocolBufferUtil;


public class BannerAdsTest 
{
	@Test
	public void testCase(){
		System.out.println("do nothing, just for passing the test, otherwise it will throw No runnable methods Error!!!");
	}
    
	public static long test()throws Exception
	{
	    
		ProtoBannerAdsReq.Builder builder = ProtoBannerAdsReq.newBuilder();
        builder.setAddressType(1);
        builder.setCategoryId(123456);
        builder.setHeight(480);
        builder.setKeyWord("coffee");
        builder.setLat(3212345);
        builder.setLon(-12345676);
        builder.setPageId(1);
        builder.setPageIndex(1);
        builder.setSearchId("back");
        builder.setWidth(320);
        	
		ProtoUserProfile profile = TestUtil.getProtoMandatory();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		TestUtil.buildUserProfile(baos, profile);
		
		byte[] objType = new String("bannerAds").getBytes();
		byte[] objTypeLen = new byte[4];
		com.telenav.j2me.datatypes.DataUtil.writeInt(objTypeLen, objType.length, 0);
		baos.write(objTypeLen);
		baos.write(objType);		
		byte[] objLen = new byte[4];
		com.telenav.j2me.datatypes.DataUtil.writeInt(objLen, builder.build().toByteArray().length, 0);
		baos.write(objLen);
		baos.write(builder.build().toByteArray());
		
		URL url = new URL(TestUtil.poiCServerUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		System.out.println(conn);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		OutputStream output = conn.getOutputStream();
		output.write(baos.toByteArray());
		output.flush();
		output.close();
		
		System.out.println("try to get input stream");
		BufferedInputStream is = new BufferedInputStream(conn
				.getInputStream());

		ByteArrayOutputStream rBaos = new ByteArrayOutputStream();
		int ch = -1;
		while((ch = is.read()) != -1)
		{
			rBaos.write(ch);
		}

		byte[] binData = rBaos.toByteArray();
		
		ProtocolBuffer[] buffer = ProtocolBufferUtil.toBufArray(binData);
		
		long status = 0;
		for(int i = 0; i < buffer.length; i++)
		{
			if(buffer[i].getObjType().equalsIgnoreCase("bannerAds"))
			{
			    ProtoShareAddressResp pShareAddressResp = ProtoShareAddressResp.parseFrom(buffer[i].getBufferData());
			    status = pShareAddressResp.getStatus();
			}
		}
		return status;
	}
	

	
/*	public static void main(String[] args) throws Exception
	{
		long status = BannerAdsTest.test();
		System.out.println("status = " + status);
		
	}*/
}
