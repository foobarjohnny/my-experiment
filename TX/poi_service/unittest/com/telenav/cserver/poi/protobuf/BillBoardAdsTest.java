package com.telenav.cserver.poi.protobuf;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

import com.telenav.cserver.util.GPSMock;
import com.telenav.cserver.util.TestUtil;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoBillBoardAdsReq;
import com.telenav.j2me.framework.protocol.ProtoBillBoardAdsResp;
import com.telenav.j2me.framework.protocol.ProtoGpsList;
import com.telenav.j2me.framework.protocol.ProtoUserProfile;
import com.telenav.j2me.framework.util.ProtocolBufferUtil;


public class BillBoardAdsTest 
{
	@Test
	public void testCase(){
		System.out.println("do nothing, just for passing the test, otherwise it will throw No runnable methods Error!!!");
	}
	
	public static long test()throws Exception
	{
	    
		ProtoBillBoardAdsReq.Builder builder = ProtoBillBoardAdsReq.newBuilder();
		
        builder.setRouteId(12345);
        
        ProtoGpsList gpsList = GPSMock.getProtoGPS();
        
        
		ProtoUserProfile profile = TestUtil.getProtoMandatory();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		TestUtil.buildUserProfile(baos, profile);
		TestUtil.buildGpsList(baos, gpsList);
		
		byte[] objType = new String("BillBoardAds").getBytes();
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
			if(buffer[i].getObjType().equalsIgnoreCase("BillBoardAds"))
			{
			    ProtoBillBoardAdsResp resp = ProtoBillBoardAdsResp.parseFrom(buffer[i].getBufferData());
			    status = resp.getStatus();
			}
		}
		return status;
	}
	

	
/*	public static void main(String[] args) throws Exception
	{
		long status = BillBoardAdsTest.test();
		System.out.println("status = " + status);
		
	}*/
}
