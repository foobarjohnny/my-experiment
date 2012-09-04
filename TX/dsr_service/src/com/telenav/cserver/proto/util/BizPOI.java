/**
 * chbZhang
 * Copy from com.telenav.j2me.datatypes.POI.java 
 * We use this class as a bean and we can change it as we want
 */
package com.telenav.cserver.proto.util;

import com.telenav.j2me.datatypes.Stop;

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
    
    
    /** constructor */
    public BizPOI()
    {}
    
    
}
