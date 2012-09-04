package com.telenav.cserver.poi.protobuf;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

import com.telenav.cserver.util.TestUtil;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoSentAddressReq;
import com.telenav.j2me.framework.protocol.ProtoSentAddressResp;
import com.telenav.j2me.framework.protocol.ProtoUserProfile;
import com.telenav.j2me.framework.util.ProtocolBufferUtil;


public class SentAddressTest 
{
	@Test
	public void testCase(){
		System.out.println("do nothing, just for passing the test, otherwise it will throw No runnable methods Error!!!");
	}
	
	public static long testConfirmRecentStops()throws Exception
	{
	    
		ProtoSentAddressReq.Builder builder = ProtoSentAddressReq.newBuilder();
        builder.setAction("1");
        builder.setId("12344");
//        builder.setUserId(123543453);
			
		ProtoUserProfile profile = TestUtil.getProtoMandatory();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		TestUtil.buildUserProfile(baos, profile);
		
		byte[] objType = new String("SentAddress").getBytes();
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
			if(buffer[i].getObjType().equalsIgnoreCase("SentAddress"))
			{
			    ProtoSentAddressResp pSentAddressResp = ProtoSentAddressResp.parseFrom(buffer[i].getBufferData());
			    status = pSentAddressResp.getStatus();
			}
		}
		return status;
	}
	
	
/*	public static void main(String[] args) throws Exception
	{
		long status = SentAddressTest.testConfirmRecentStops();
		System.out.println("status = " + status);
		
	}*/
}
