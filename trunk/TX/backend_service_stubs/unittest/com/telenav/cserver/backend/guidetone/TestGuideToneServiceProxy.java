/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.guidetone;

import java.lang.reflect.Method;
import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import com.telenav.billing.ws.datatypes.common.RequestSource;
import com.telenav.billing.ws.datatypes.userprofile.CreateGuideToneOrderRequest;
import com.telenav.billing.ws.datatypes.userprofile.CreateGuideToneOrderResponse;
import com.telenav.billing.ws.datatypes.userprofile.GuideToneOrder;
import com.telenav.billing.ws.datatypes.userprofile.UpdateGuideToneOrderRequest;
import com.telenav.billing.ws.datatypes.userprofile.UpdateGuideToneOrderResponse;
import com.telenav.billing.ws.userprofilemanagementservice.UserProfileManagementServiceStub;
import com.telenav.cserver.backend.StatusConstants;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.framework.throttling.ThrottlingException;

import junit.framework.TestCase;

/**
 * GuideToneServiceProxy.java
 * 
 * jhjin@telenav.cn
 * 
 * @version 1.0 2010-9-6
 * 
 */
public class TestGuideToneServiceProxy extends TestCase
{
    private static Logger logger = Logger.getLogger(TestGuideToneServiceProxy.class);

    private GuideToneServiceProxy proxy = GuideToneServiceProxy.getInstance();

    private long userId = 11045322;

    private String guideToneName = "104";

    public void testGetUserGuideToneInfo4Success() throws ThrottlingException, RemoteException
    {
        /*long guideToneId = createGuideToneForUser();
        if (logger.isDebugEnabled())
        {
            logger.debug("guideToneId = " + guideToneId);
        }
        if (guideToneId < 0)
        {
           // fail();
        }*/
        GetUserGuideToneInfoRequest request = new GetUserGuideToneInfoRequest();
        request.setUserId(userId);
        GetUserGuideToneInfoResponse response = proxy.getUserGuideToneInfo(request, null);
        assertEquals(StatusConstants.SUCCESS, response.getStatusCode());
        /*clearGuideToneForUser();
        proxy.getUserGuideToneInfo(request, null);
        assertEquals(StatusConstants.SUCCESS, response.getStatusCode());*/
    }


    public void testGetUserGuideToneInfo4InvalidUserId() throws ThrottlingException
    {
        GetUserGuideToneInfoRequest request = new GetUserGuideToneInfoRequest();
        request.setUserId(Integer.MIN_VALUE);
        GetUserGuideToneInfoResponse response = proxy.getUserGuideToneInfo(request, null);
        assertEquals(StatusConstants.SUCCESS, response.getStatusCode());

    }
    
    public void testGetUserGuideToneInfo4NullRequest() throws ThrottlingException
    {
        GetUserGuideToneInfoRequest request = null;
        GetUserGuideToneInfoResponse response = proxy.getUserGuideToneInfo(request, null);
        assertEquals(StatusConstants.FAIL, response.getStatusCode());

    }

    private long createGuideToneForUser() throws RemoteException
    {
        UserProfileManagementServiceStub stub = createUserProfileManagementServiceStub();
        CreateGuideToneOrderRequest request = new CreateGuideToneOrderRequest();
        GuideToneOrder order = new GuideToneOrder();
        order.setUserId(userId);
        order.setGuideToneName(guideToneName);
        order.setDefaultTone(0);
        request.setGuideToneOrder(order);
        request.setRequestSource(RequestSource.TelenavXnav);
        CreateGuideToneOrderResponse response = stub.createGuideToneOrder(request);
        WebServiceUtils.cleanupStub(stub);
        return response.getGuideToneId();
    }
    
    private void clearGuideToneForUser() throws RemoteException
    {
        UserProfileManagementServiceStub stub = createUserProfileManagementServiceStub();
        UpdateGuideToneOrderRequest request = new UpdateGuideToneOrderRequest();
        GuideToneOrder order = new GuideToneOrder();
        order.setUserId(userId);
        order.setGuideToneName(guideToneName);
        order.setDefaultTone(0);
        order.setGuideToneId(6902);
        request.setGuideToneOrder(order);
        request.setRequestSource(RequestSource.TelenavXnav);
        UpdateGuideToneOrderResponse response = stub.updateGuideToneOrder(request);
       
    }

    private UserProfileManagementServiceStub createUserProfileManagementServiceStub()
    {
        UserProfileManagementServiceStub stub = null;
        try
        {
            Method method = GuideToneServiceProxy.class.getDeclaredMethod("createUserProfileManagementServiceStub");
            method.setAccessible(true);
            stub = (UserProfileManagementServiceStub)method.invoke(proxy);
        }
        catch (Exception ex)
        {
            logger.fatal("createUserProfileManagementServiceStub", ex);
            fail();
        }
        return stub;
    }

}
