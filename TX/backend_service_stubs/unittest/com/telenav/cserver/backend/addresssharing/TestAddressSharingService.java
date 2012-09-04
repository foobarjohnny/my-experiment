/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.addresssharing;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.telenav.cserver.backend.datatypes.addresssharing.ContactInfo;
import com.telenav.cserver.backend.datatypes.addresssharing.SharedAddressItem;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * TestAddressSharingService
 * @author pzhang
 * @date 2010-8-9
 */
public class TestAddressSharingService extends TestCase
{
    private TnContext tnContext;
	private long userId;
	private String ptn;
	private List<ContactInfo> contactList;
	private Stop address;
	private String brandName;
	private String movieName;

    @Override
    protected void setUp() throws Exception
    {
        tnContext = new TnContext();
        tnContext.addProperty(TnContext.PROP_CARRIER, "ATT");
        tnContext.addProperty(TnContext.PROP_DEVICE, "9000");
        tnContext.addProperty(TnContext.PROP_PRODUCT, "RIM");
        tnContext.addProperty(TnContext.PROP_VERSION, "6.0.01");
        tnContext.addProperty("application", "ATT_NAV");
        tnContext.addProperty("login", "3817799999");
        tnContext.addProperty("userid", "3707312");
        
        userId = 3707312;
        ptn = "3817799999";
        
        address = new Stop();
        address.firstLine="San Francisco International Airport";
        address.label="SFO";
        address.city = "BURLINGAME";
        address.state="CA";
        address.country = "US";
        address.zip = "94128";
        address.lat = 3761386;
        address.lon = -12239382;
        
        contactList = new ArrayList<ContactInfo>();
        ContactInfo info = new ContactInfo();
        info.setPtn("3817799999");
        info.setName("Pan Zhang");
        contactList.add(info);
        
        info = new ContactInfo();
        info.setPtn("3795251284");
        info.setName("Test");
        contactList.add(info);
        
        brandName = "AMC Mercado 20";
        movieName = "Aisha";
    }

    /**
     * 
     * @throws ThrottlingException
     */
    public void testShareAddress() throws ThrottlingException
    {
    	AddressSharingRequest request = new AddressSharingRequest();
    	request.setUserId(userId);
    	request.setPtn(ptn);
    	request.setContextString(tnContext.toContextString());
    	request.setAddress(address);
    	request.setContactList(contactList);
        
        AddressSharingResponse response = AddressSharingServiceProxy.getInstance().shareAddress(request, tnContext);
        String statusCode = response.getStatusCode();
        
        assertEquals("OK", statusCode);
        
    }
    
    /**
     * 
     * @throws ThrottlingException
     */
    public void testFeachAddress() throws ThrottlingException
    {
    	FetchSharedAddressRequest request = new FetchSharedAddressRequest();
    	request.setContextString(tnContext.toContextString());
    	request.setUserId(userId);
    	
    	FetchSharedAddressResponse response = AddressSharingServiceProxy.getInstance().fetchSharedAddress(request, tnContext);
    	List<SharedAddressItem> sharedAddressList = response.getSharedAddressList();
    	StringBuilder sb = new StringBuilder();
    	for(SharedAddressItem info:sharedAddressList)
        {
     	   sb.append("\nSharedAddressItem=").append(info.toString());
        }
        System.out.println(sb.toString());
    	
    	String statusCode = response.getStatusCode();
        
        assertEquals("OK", statusCode);
        
    }
    
    /**
     * 
     * @throws ThrottlingException
     */
    public void testShareMovie() throws ThrottlingException
    {
    	LocationSharingRequest request = new LocationSharingRequest();
    	request.setUserId(userId);
    	request.setPtn(ptn);
    	request.setContextString(tnContext.toContextString());
    	request.setAddress(address);
    	request.setContactList(contactList);
    	request.setMovieName(movieName);
    	request.setBrandName(brandName);
        
        AddressSharingResponse response = AddressSharingServiceProxy.getInstance().shareMovie(request, tnContext);
        String statusCode = response.getStatusCode();
        
        assertEquals("OK", statusCode);
        
    }
}
