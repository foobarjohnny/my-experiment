/**
 * chbZhang
 * Copy from com.telenav.j2me.datatypes.Stop.java 
 * We use this class as a bean and we can change it as we want
 */
package com.telenav.cserver.poi.datatypes;

import com.telenav.j2me.datatypes.DataConstants;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.j2me.framework.protocol.ProtoStop;

public class Stop
{
    public String firstLine = "";
    public String street = "";
    public String street2 = "";
    public String city = "";
    public String state = "";
    public String zip = "";
    public String label = "";
    public String type = "";
    public String country = "";
    public String county = "";
    public int lat;
    public int lon;
    public String stopId = "";
    public boolean isGeocoded = false;
    public int hashCode;

    
    private byte[] firstLineAudio;
    private byte[] lastLineAudio;
    
    
    public boolean isShareAddress = false;
    
    // Added by Radha for UGC
    
   

    /**
     * default constructor
     */
    public Stop()
    {
        super();
    }
    
    //for DSR begin
    public byte[] getFirstLineAudio()
    {
        return this.firstLineAudio;
    }
    
    public void setFirstLineAudio(byte[] firstLineAudio)
    {
        this.firstLineAudio = firstLineAudio;
    }

    public byte[] getLastLineAudio()
    {
        return this.lastLineAudio;
    }
    
    public void setLastLineAudio(byte[] lastLineAudio)
    {
        this.lastLineAudio = lastLineAudio;
    }
    
    public ProtoStop toProtocol()
    {	    
		ProtoStop.Builder builder = ProtoStop.newBuilder();
		builder.setLat(lat);
		builder.setLon(lon);
		builder.setIsGeocoded(isGeocoded);
		builder.setHashCode(hashCode);
		builder.setIsShareAddress(isShareAddress);
		builder.setLabel(label);
		builder.setFirstLine(firstLine);
		builder.setCity(city);
		builder.setState(state);
		builder.setStopId(stopId);
		builder.setZip(zip);
		builder.setCountry(country);
		builder.setCounty(county);
		return builder.build();
    }
    
    public TxNode toTxNode()
    {
        TxNode node = new TxNode();
        node.addValue(DataConstants.TYPE_STOP);//0
        node.addValue(lat);//1
        node.addValue(lon);//2
        node.addValue(0);//3
        node.addValue(0); //4
        node.addValue(isGeocoded ? 1:0);//5
        node.addValue(hashCode); //6
        
        node.addValue(isShareAddress ? 1:0); //7
        
        node.addMsg(label);//0
        node.addMsg(firstLine);//1
        node.addMsg(city);//2
        node.addMsg(state);//3
        node.addMsg(stopId);//4
        node.addMsg(zip);//5
        
        // Guoyuan: added country and county to support EU countries 
        if (country != null)
            node.addMsg(country); //6
        else
            node.addMsg(""); //6
        if(county != null && !county.equals(""))
            node.addMsg(county); //7
        else
            node.addMsg(""); //6
    
        return node;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
    
}
