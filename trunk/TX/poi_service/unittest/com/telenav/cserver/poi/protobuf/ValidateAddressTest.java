package com.telenav.cserver.poi.protobuf;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

import com.telenav.cserver.util.TestUtil;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoUserProfile;
import com.telenav.j2me.framework.protocol.ProtoValidateAddressReq;
import com.telenav.j2me.framework.protocol.ProtoValidateAddressResp;
import com.telenav.j2me.framework.util.ProtocolBufferUtil;


public class ValidateAddressTest 
{
	@Test
	public void testCase(){
		System.out.println("do nothing, just for passing the test, otherwise it will throw No runnable methods Error!!!");
	}
	
	public static long testConfirmRecentStops()throws Exception
	{
	    
		ProtoValidateAddressReq.Builder builder = ProtoValidateAddressReq.newBuilder();
        builder.setCity("sunnyvale");
        builder.setFirstLine("1130, kifer");
        builder.setCountry("USA");
        builder.setState("CA");
        builder.setZip("94086");
        builder.setLastLine("");
        builder.setStreet1("1130, kifer");
        builder.setStreet2("");
			
		ProtoUserProfile profile = TestUtil.getProtoMandatory();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		TestUtil.buildUserProfile(baos, profile);
		
		byte[] objType = new String("ValidateAddress").getBytes();
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
			if(buffer[i].getObjType().equalsIgnoreCase("ValidateAddress"))
			{
			    ProtoValidateAddressResp pValidateAddressResp = ProtoValidateAddressResp.parseFrom(buffer[i].getBufferData());
			    status = pValidateAddressResp.getStatus();
			}
		}
		return status;
	}
	
	
/*	public static void main(String[] args) throws Exception
	{
		long status = ValidateAddressTest.testConfirmRecentStops();
		System.out.println("status = " + status);
		
	}*/
}
