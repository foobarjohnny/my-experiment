/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html;

import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.html.executor.HtmlNickNameRequest;
import com.telenav.cserver.html.executor.HtmlNickNameResponse;
import com.telenav.cserver.html.executor.HtmlNickNameServiceProxy;
import com.telenav.kernel.util.datatypes.TnContext;

import junit.framework.TestCase;


public class TestUserService extends TestCase
{
	long userId = 0;
	TnContext tnContext;
	@Override
    protected void setUp() throws Exception
    {
		tnContext = new TnContext();
		String userToken ="AAAAAAA4kbAAAAEuxtdAfCytu5fV7hiBaKSDLIQPCJ8=";
		String strUserId = HtmlCommonUtil.getUserId(userToken);
		userId = Long.parseLong(strUserId);
    }
	
	public void testGetAdsBasic()
	{
		HtmlNickNameRequest nickNameRequest = new HtmlNickNameRequest();
		nickNameRequest.setUserId(userId);
		HtmlNickNameResponse nickNameResponse = new HtmlNickNameResponse();
		
		HtmlNickNameServiceProxy.getInstance().queryNickName(nickNameRequest, nickNameResponse, tnContext);
		
		System.out.println("nickName:" + nickNameResponse.getNickName());
	}
	
	public void testCheckNickName()
	{
		HtmlNickNameRequest nickNameRequest = new HtmlNickNameRequest();
		nickNameRequest.setNickName("zhang pan");
		HtmlNickNameResponse nickNameResponse = new HtmlNickNameResponse();
		
		HtmlNickNameServiceProxy.getInstance().checkNickName(nickNameRequest, nickNameResponse, tnContext);
		
		System.out.println("IsUniqueNickName:" + nickNameResponse.getIsUniqueNickName());
	}
	
	public void testCheckAndaddNickName()
	{
		HtmlNickNameRequest nickNameRequest = new HtmlNickNameRequest();
		nickNameRequest.setNickName("zhang pan");
		nickNameRequest.setUserId(userId);
		HtmlNickNameResponse nickNameResponse = new HtmlNickNameResponse();
		
		HtmlNickNameServiceProxy.getInstance().checkAndaddNickName(nickNameRequest, nickNameResponse, tnContext);
		
		System.out.println("IsUniqueNickName:" + nickNameResponse.getIsUniqueNickName());
	}
	
	public void testAddNickName()
	{
		HtmlNickNameRequest nickNameRequest = new HtmlNickNameRequest();
		nickNameRequest.setNickName("zhang pan");
		nickNameRequest.setUserId(userId);
		HtmlNickNameResponse nickNameResponse = new HtmlNickNameResponse();
		
		HtmlNickNameServiceProxy.getInstance().addNickName(nickNameRequest, nickNameResponse, tnContext);
	}
}
