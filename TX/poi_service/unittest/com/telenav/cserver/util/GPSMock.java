/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.util;

import java.util.Vector;

import com.telenav.j2me.framework.protocol.ProtoGpsFix;
import com.telenav.j2me.framework.protocol.ProtoGpsList;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.datatypes.TxNode;

/**
 * GPSMock.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-5-7
 */
public class GPSMock
{/*
	 Values:
	        [0]=51
	        binary exists ? = false
	        ------------ children -----------
	                ------------ TxNode -----------
	                Values:
	                [0]=124168189132
	                [1]=3737501
	                [2]=-12199770
	                [3]=0
	                [4]=0
	                [5]=0
	                [6]=15
	                binary exists ? = false
	                ------------ end of TxNode ------

	                ------------ TxNode -----------
	                Values:
	                [0]=124168189028
	                [1]=3737501
	                [2]=-12199770
	                [3]=0
	                [4]=0
	                [5]=0
	                [6]=15
	                binary exists ? = false
	                ------------ end of TxNode ------

	                ------------ TxNode -----------
	                Values:
	                [0]=124168188928
	                [1]=3737501
	                [2]=-12199770
	                [3]=0
	                [4]=0
	                [5]=0
	                [6]=15
	                binary exists ? = false
	                ------------ end of TxNode ------

	        ------------ end of TxNode ------
*/
	public static TxNode getGPSStopNode()
	{
		Stop stop = new Stop();
		

		stop.lat = 3763214;
		stop.lon = -12243689;
		return stop.toTxNode();
		
	}
	
	public static TxNode getGPSNode_CN()
	{

        TxNode gpsNode = new TxNode();
        gpsNode.addMsg("gps");
        TxNode gps1 = new TxNode();
        gps1.addValue(System.currentTimeMillis());
        gps1.addValue(3128419);
        gps1.addValue(12143220);
       
        gps1.addValue(0);
        gps1.addValue(0);
        gps1.addValue(0);
        gps1.addValue(1);
        
        TxNode gps2 = new TxNode();
        gps2.addValue(System.currentTimeMillis());
        //gps2.addValue(3737890);
        //gps2.addValue(-12201074);
        gps1.addValue(3128419);
        gps1.addValue(12143220);
        gps2.addValue(0);
        gps2.addValue(0);
        gps2.addValue(0);
        gps2.addValue(1);
        
        TxNode gps3 = new TxNode();
        gps3.addValue(System.currentTimeMillis());
        gps1.addValue(3128419);
        gps1.addValue(12143220);
        gps3.addValue(0);
        gps3.addValue(0);
        gps3.addValue(0);
        gps3.addValue(1);
        
        gpsNode.addChild(gps1);
        gpsNode.addChild(gps2);
        gpsNode.addChild(gps3);
        
        return gpsNode;
    
	}
	
	/*
	public static TxNode getGPSNode()
	{
		TxNode gpsNode = new TxNode();
		gpsNode.addMsg("gps");
		TxNode gps1 = new TxNode();
		gps1.addValue(System.currentTimeMillis());
		gps1.addValue(3763214);
		gps1.addValue(-12243689);
       
		gps1.addValue(0);
		gps1.addValue(0);
		gps1.addValue(0);
		gps1.addValue(1);
		
		TxNode gps2 = new TxNode();
		gps2.addValue(System.currentTimeMillis());
		//gps2.addValue(3737890);
		//gps2.addValue(-12201074);
		gps2.addValue(3763214);
		gps2.addValue(-12243689);
		gps2.addValue(0);
		gps2.addValue(0);
		gps2.addValue(0);
		gps2.addValue(1);
		
		TxNode gps3 = new TxNode();
		gps3.addValue(System.currentTimeMillis());
		gps3.addValue(3763214);
		gps3.addValue(-12243689);
		gps3.addValue(0);
		gps3.addValue(0);
		gps3.addValue(0);
		gps3.addValue(1);
		
		gpsNode.addChild(gps1);
		gpsNode.addChild(gps2);
		gpsNode.addChild(gps3);
		
		return gpsNode;
	}
	*/
	
	
	//3775935,-12240598//4064448, -737932
	public static TxNode getTestGPSNode()
	{
		TxNode gpsNode = new TxNode();
		gpsNode.addMsg("gps");
		TxNode gps1 = new TxNode();
		gps1.addValue(System.currentTimeMillis());
		gps1.addValue(3775935);
		gps1.addValue(-12240598);
       
		gps1.addValue(0);
		gps1.addValue(0);
		gps1.addValue(0);
		gps1.addValue(1);
		
		TxNode gps2 = new TxNode();
		gps2.addValue(System.currentTimeMillis());
		//gps2.addValue(3737890);
		//gps2.addValue(-12201074);
		gps2.addValue(3775935);
		gps2.addValue(-12240598);
		gps2.addValue(0);
		gps2.addValue(0);
		gps2.addValue(0);
		gps2.addValue(1);
		
		TxNode gps3 = new TxNode();
		gps3.addValue(System.currentTimeMillis());
		gps3.addValue(3775935);
		gps3.addValue(-12240598);
		gps3.addValue(0);
		gps3.addValue(0);
		gps3.addValue(0);
		gps3.addValue(1);
		
		gpsNode.addChild(gps1);
		gpsNode.addChild(gps2);
		gpsNode.addChild(gps3);
		
		return gpsNode;
	}
	
	public static ProtoGpsList getProtoGPS()
	{
		ProtoGpsList.Builder gpsBuilder = ProtoGpsList.newBuilder();
		Vector vGps = new Vector();
		ProtoGpsFix.Builder fixBuilder = ProtoGpsFix.newBuilder();
		long timeStamp = System.currentTimeMillis();
		fixBuilder.setTimeTag(timeStamp);
		if(timeStamp > 0L)
		{
			fixBuilder.setLatitude(3737370);
			fixBuilder.setLontitude(-12200386);
			fixBuilder.setSpeed(0);
			fixBuilder.setHeading(0);
			fixBuilder.setType(0);
			fixBuilder.setErrorSize(1);
		}
		vGps.add(fixBuilder.build());
		vGps.add(fixBuilder.build());
		vGps.add(fixBuilder.build());
		gpsBuilder.setGpsfix(vGps);
		
		return gpsBuilder.build();
	}
	
	public static TxNode getGPSNode()
	{
		TxNode gpsNode = new TxNode();
		gpsNode.addMsg("gps");
		TxNode gps1 = new TxNode();
		gps1.addValue(System.currentTimeMillis());
		gps1.addValue(3737370);
		gps1.addValue(-12200386);
       
		gps1.addValue(0);
		gps1.addValue(0);
		gps1.addValue(0);
		gps1.addValue(1);
		
		TxNode gps2 = new TxNode();
		gps2.addValue(System.currentTimeMillis());
		//gps2.addValue(3737890);
		//gps2.addValue(-12201074);
		gps2.addValue(3737370);
		gps2.addValue(-12200386);
		gps2.addValue(0);
		gps2.addValue(0);
		gps2.addValue(0);
		gps2.addValue(1);
		
		TxNode gps3 = new TxNode();
		gps3.addValue(System.currentTimeMillis());
		gps3.addValue(3737370);
		gps3.addValue(-12200386);
		gps3.addValue(0);
		gps3.addValue(0);
		gps3.addValue(0);
		gps3.addValue(1);
		
		gpsNode.addChild(gps1);
		gpsNode.addChild(gps2);
		gpsNode.addChild(gps3);
		
		return gpsNode;
	}
	
	/* public static TxNode getGPSNode()
	{
		TxNode gpsNode = new TxNode();
		gpsNode.addMsg("gps");
		TxNode gps1 = new TxNode();
		gps1.addValue(System.currentTimeMillis());
		gps1.addValue(3582447);
		gps1.addValue(-12076136);
		gps1.addValue(0);
		gps1.addValue(0);
		gps1.addValue(0);
		gps1.addValue(1);
		
		TxNode gps2 = new TxNode();
		gps2.addValue(System.currentTimeMillis());
		gps1.addValue(3582447);
		gps1.addValue(-12076136);
		gps2.addValue(0);
		gps2.addValue(0);
		gps2.addValue(0);
		gps2.addValue(1);
		
		TxNode gps3 = new TxNode();
		gps3.addValue(System.currentTimeMillis());
		gps1.addValue(3582447);
		gps1.addValue(-12076136);
		gps3.addValue(0);
		gps3.addValue(0);
		gps3.addValue(0);
		gps3.addValue(1);
		
		gpsNode.addChild(gps1);
		gpsNode.addChild(gps2);
		gpsNode.addChild(gps3);
		
		return gpsNode;
	}*/
}
