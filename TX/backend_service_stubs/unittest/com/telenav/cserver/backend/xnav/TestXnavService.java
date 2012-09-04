/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.xnav;

import java.util.List;

import junit.framework.TestCase;

import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * TestAddressSharingService
 * @author pzhang
 * @date 2010-8-9
 */
public class TestXnavService extends TestCase
{
    private TnContext tnContext;
	private String ptn;
	private String[] ptns;
	private long userId;
	private String country;
	private int count;

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
        
        ptns = new String[2];
        ptns[0] = "3817799999";
        ptns[1] = "3795251284"; 
        
        country = "USA";
        count = 100;
    }

    
    /**
     * 
     * @throws ThrottlingException
     */
    public void testResendPin() throws ThrottlingException
    {
    	ResendPinRequest request = new ResendPinRequest();
    	request.setPtn(ptn);
    	request.setContextString(tnContext.toContextString());
    	
        ResendPinResponse response = XnavServiceProxy.getInstance().resendPin(request,tnContext);
    	String statusCode = response.getStatusCode();
        
        assertEquals("OK", statusCode);
        
    }
    
    /**
     * 
     * @throws ThrottlingException
     */
    public void testReferToFriends() throws ThrottlingException
    {
    	ReferToFriendRequest request = new ReferToFriendRequest();
    	request.setUserId(userId);
    	request.setPtns(ptns);
    	request.setContextString(tnContext.toContextString());
    	
    	ReferToFriendResponse response = XnavServiceProxy.getInstance().refer2Friends(request,tnContext);
    	String statusCode = response.getStatusCode();
        
        assertEquals("OK", statusCode);
        
    }
    
    /**
     * 
     * @throws ThrottlingException
     */
    public void testFetchBrandNames() throws ThrottlingException
    {
    	FetchBrandNamesRequest request = new FetchBrandNamesRequest();
    	request.setCount(count);
    	request.setCountry(country);
    	
    	FetchBrandNamesResponse response = XnavServiceProxy.getInstance().fetchBrandNames(request,tnContext);
    	
    	String statusCode = response.getStatusCode();
    	List<String> nameList = response.getBrandNames();
    	System.out.println(nameList.toString());
    	
        assertEquals("OK", statusCode);
        
    }
    
    public void testETA()
    {
    	SendETARequest req = new SendETARequest();
    	req.setUserId(userId);
    	req.setPtns(ptns);
    	req.setContextString(tnContext.toContextString());
    	req.setMessage("Test sendEta()");
    	SendETAResponse resp = null;
    	try
		{
			resp = XnavServiceProxy.getInstance().sendEta(req, tnContext);
		}
		catch (ThrottlingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	assertEquals("OK", resp.getStatusCode());
    	assertEquals("OK", resp.getStatusMessage());
    }
}
