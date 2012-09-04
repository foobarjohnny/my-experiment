/**
 * 
 */
package com.telenav.cserver.proto.util;

import java.util.Date;

public class RevHistory 
{
	public long poiId=-1;
	public long basePOIId=-1;
	public Date updateTime= new Date();
	public String fieldName="";
	public String actionName="";
	public String oldValue="";
	public String newValue="";
	public long userId=-1;
	
	public String firstName;
	public String lastName;
	
}