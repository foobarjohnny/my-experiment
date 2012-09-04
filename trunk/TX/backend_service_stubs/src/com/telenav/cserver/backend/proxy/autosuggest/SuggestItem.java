package com.telenav.cserver.backend.proxy.autosuggest;

import java.util.StringTokenizer;

import com.telenav.j2me.datatypes.TxNode;
 
public class SuggestItem 
{
    String showterm;
	
    String searchTerm;

	String lat;
	
	String lon;

	public String getSearchTerm() 
	{
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) 
	{
		this.searchTerm = searchTerm;
	}

	public String getShowterm()
    {
		return showterm;
	}

	public void setShowterm(String showterm)
	{
		this.showterm = showterm;
	}
	
    public String getLat() 
    {
		return lat;
	}
    
    public String getLon()
    {
    	return lon;
    }

	public void setLatLon(String latLon) 
	{
		 StringTokenizer st = new StringTokenizer(latLon, ",");
         if(st.hasMoreTokens())
         {
        	 this.lat = st.nextToken();
         }
         if(st.hasMoreTokens())
         {
        	 this.lon = st.nextToken();
         }
	}
	
	public String toString()
	{
		return "showterm : " + showterm + " lat/lon : " + lat + "/" + lon + " searchTerm : " + searchTerm;
	}
	
	public TxNode toTxNode()
	{
		TxNode node = new TxNode();
		node.addValue(400);
		node.addMsg(showterm);
		node.addMsg(lat);
		node.addMsg(lon);
		node.addMsg(searchTerm);
		return node;
	}
	/*
	public ProtoSuggestItem toProtoBuff()
    {
        ProtoSuggestItem protoSuggestItem = ProtoSuggestItem.newBuilder().
                                            setLat(Double.parseDouble(lat)).
                                            setLon(Double.parseDouble(lon)).
                                            setSearchTerm(searchTerm).
                                            setShowterm(showterm).build();
       
        return protoSuggestItem;
    }*/

}
