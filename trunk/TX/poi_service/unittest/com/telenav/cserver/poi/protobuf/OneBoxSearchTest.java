package com.telenav.cserver.poi.protobuf;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

import com.telenav.cserver.framework.util.ProtoUtil;
import com.telenav.cserver.util.TestUtil;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoOneBoxSearchReq;
import com.telenav.j2me.framework.protocol.ProtoOneBoxSearchResp;
import com.telenav.j2me.framework.protocol.ProtoSearchItem;
import com.telenav.j2me.framework.protocol.ProtoUserProfile;
import com.telenav.j2me.framework.util.ProtocolBufferUtil;
import com.telenav.protocol.constants.NodeTypeDefinitions;

public class OneBoxSearchTest 
{
	@Test
	public void testCase(){
		System.out.println("do nothing, just for passing the test, otherwise it will throw No runnable methods Error!!!");
	}

	public static void testOneBoxSearch() throws Exception
	{
		ProtoOneBoxSearchReq.Builder request = ProtoOneBoxSearchReq.newBuilder();
		request.setDistanceUnit(1);
		request.setMaxResult(9);
		request.setPageNumber(2);
		ProtoSearchItem.Builder item = ProtoSearchItem.newBuilder();
		item.setType(NodeTypeDefinitions.TYPE_ONE_BOX_ADDRESS_SHARING_ORGIN_NODE);
		item.setStop(ProtoUtil.convertStopToProtoBuf(TestUtil.getStop(1)));
		request.addElementSearchItems(item.build());
		request.setSearchString("Coffee");
		request.setSearchType(5);
		ProtoUserProfile profile = TestUtil.getProtoMandatory();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		TestUtil.buildUserProfile(baos, profile);
		
		byte[] objType = new String("OneBox").getBytes();
		byte[] objTypeLen = new byte[4];
		com.telenav.j2me.datatypes.DataUtil.writeInt(objTypeLen, objType.length, 0);
		baos.write(objTypeLen);
		baos.write(objType);		
		byte[] objLen = new byte[4];
		com.telenav.j2me.datatypes.DataUtil.writeInt(objLen, request.build().toByteArray().length, 0);
		baos.write(objLen);
		baos.write(request.build().toByteArray());
		
		URL url = new URL(TestUtil.poiCServerUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		System.out.println(conn);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		//conn.setRequestProperty("flag", "probe");
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
		long start = System.currentTimeMillis();
		byte[] binData = rBaos.toByteArray();
		
		ProtocolBuffer[] buffer = ProtocolBufferUtil.toBufArray(binData);
		for(int i = 0; i < buffer.length; i++)
		{
			if(buffer[i].getObjType().equalsIgnoreCase("OneBox"))
			{
			    ProtoOneBoxSearchResp response =  ProtoOneBoxSearchResp.parseFrom(buffer[i].getBufferData());
			    System.out.println(response.toString());
			}
		}
	}
	
/*	public static void main(String[] args) throws Exception
	{
		OneBoxSearchTest.testOneBoxSearch();
	}*/
}
