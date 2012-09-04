/**
 * chbZhang
 * Copy from com.telenav.j2me.datatypes.POI.java 
 * We use this class as a bean and we can change it as we want
 */
package com.telenav.cserver.poi.datatypes;

/**
 * POI result
 */
public class BizPOI
{
    public static final int NO_GROUP = -1;
    
    public String brand = "";
    public String phoneNumber ="";
    public boolean navigable;
    public int distance;
    public String parentCatName;
    public String categoryId;
    public String price;
    public int groupID = NO_GROUP;
    public Stop address;
    public int layout;
    public String vendorCode;
    
    // gas price supporting info
    // grade and price
    public String[] supplementalInfo;
    // update time
    public String[] supportInfo;
    
    // Added by Radha for UGC
    
	// set new field for ACE 4.0, we need assembling "first_line" form "StreetName" and "HouseNumber"
	private String houseNumber;
	private String streetName;

	public String getHouseNumber()
	{
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber)
	{
		this.houseNumber = houseNumber;
	}

	public String getStreetName()
	{
		return streetName;
	}

	public void setStreetName(String streetName)
	{
		this.streetName = streetName;
	}
    
    /** constructor */
    public BizPOI()
    {}
    
    
}
