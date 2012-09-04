/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.username;

import junit.framework.TestCase;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.telenav.cserver.backend.StatusConstants;
import com.telenav.cserver.backend.proxy.username.SaveRegisterInfoRequest;
import com.telenav.cserver.backend.proxy.username.SaveRegisterInfoResponse;
import com.telenav.cserver.backend.proxy.username.SaveUserNameResponse;
import com.telenav.cserver.backend.proxy.username.UserNameServiceHelper;
import com.telenav.cserver.backend.proxy.username.UserNameServiceProxy;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.user.management.v11.GetUserResponseDTO;
import com.telenav.services.user.management.v11.IsUniqueUsernameResponseDTO;

/**
 * UserNameServiceProxyTest.java
 * 
 * jhjin@telenav.cn
 * 
 * @version 1.0 Mar 9, 2011
 * 
 */
public class TestUserNameServiceProxy extends TestCase
{

    public void testGetUser()
    {
        long userId = 9894832;
        GetUserResponseDTO response = proxy.getUser(UserNameServiceHelper.newGetUserRequestDTO(userId, tc), tc);
        assertNotNull(response);
        assertNotNull(response.getUser());
        assertNotNull(response.getUser().getUserName());

    }

    public void testSaveUserName()
    {
        long userId = 3698214;
        String userName = "jhjin";
        SaveUserNameResponse response = proxy.saveUserName(UserNameServiceHelper.newSaveUserNameRequest(userId, userName), tc);
        assertEquals(StatusConstants.SUCCESS, response.getStatusCode());
        assertEquals(userName, proxy.getUser(UserNameServiceHelper.newGetUserRequestDTO(userId, tc), tc).getUser().getUserName());

        response = proxy.saveUserName(UserNameServiceHelper.newSaveUserNameRequest(userId, null), tc);
        assertEquals(StatusConstants.SUCCESS, response.getStatusCode());
    }

    public void testCheckUserName()
    {
        long userId = 3698214;
        String userName = "abcd";

        IsUniqueUsernameResponseDTO dto = proxy.checkUserName(UserNameServiceHelper.newIsUniqueUsernameRequestDTO(userName, tc), tc);
        assertEquals(false, dto.getUnique());

        SaveUserNameResponse response = proxy.saveUserName(UserNameServiceHelper.newSaveUserNameRequest(userId, userName), tc);
        assertEquals(StatusConstants.SUCCESS, response.getStatusCode());

        dto = proxy.checkUserName(UserNameServiceHelper.newIsUniqueUsernameRequestDTO(userName, tc), tc);
        assertEquals(false, dto.getUnique());
        
        response = proxy.saveUserName(UserNameServiceHelper.newSaveUserNameRequest(userId, null), tc);
        assertEquals(StatusConstants.SUCCESS, response.getStatusCode());
    }
    
    public void testRegisterId()
    {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId("9985413");
        userProfile.setProgramCode("ATTNAVPROG");
        userProfile.setPlatform("ANDROID");
        userProfile.setVersion("7.2.0");
        userProfile.setDeviceID("asdfasdfasdfasdf72983487192836");
        
        SaveRegisterInfoRequest req=new SaveRegisterInfoRequest(userProfile, "abcd");
        SaveRegisterInfoResponse resp=proxy.saveRegisterInfo(req, tc);
        System.out.println(ReflectionToStringBuilder.toString(resp,ToStringStyle.MULTI_LINE_STYLE));
    }

    private UserNameServiceProxy proxy = null;

    private TnContext tc = null;

    @Override
    protected void setUp() throws Exception
    {
        proxy = new UserNameServiceProxy()
        {
//            public UserManagementServiceStub getStub()
//            {
//                if (stub == null)
//                {
//                    try
//                    {
//                        stub = new UserManagementServiceStub("http://10.224.74.33:8080/tnws/services/UserManagementService");
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//                return stub;
//            }

        };
        tc = new TnContext();
    }
}
