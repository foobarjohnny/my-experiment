/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import javax.servlet.http.HttpServletRequest;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolRequestParser;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.html.executor.HtmlNickNameRequest;
import com.telenav.cserver.html.util.HtmlConstants;

/**
 * @TODO	Define executor type
 * 			Parse the browser request
 * @author  chfzhang@telenav.cn
 * @version 1.0 Feb 18, 2011
 */
public class HtmlNickNameParser extends HtmlProtocolRequestParser{

	@Override
	public String getExecutorType() {
		// TODO Auto-generated method stub
		return "nickName";
	}

	@Override
	public ExecutorRequest parseBrowserRequest(HttpServletRequest httpServletRequest)
			throws Exception {
		// TODO Auto-generated method stub
		HtmlNickNameRequest nickNameRequest = new HtmlNickNameRequest();
		
		String operateType = httpServletRequest.getParameter("operateType");
		String ssoTokenStr = httpServletRequest.getParameter("ssoToken");
		
		long userId = -1;
		try{
		    userId = Long.parseLong(HtmlCommonUtil.getUserId(ssoTokenStr));
			//userId = 5683206;
		}catch(NumberFormatException e)
        {
        }
		
		// if is check operation
		if(HtmlConstants.OPERATE_NICKNAME_CHECK.equals(operateType)){
			String nickNameStr = httpServletRequest.getParameter("nickName");
			nickNameRequest.setNickName(nickNameStr);
		}else if(HtmlConstants.OPERATE_NICKNAME_ADD.equals(operateType)){
			String nickNameStr = httpServletRequest.getParameter("nickName");
			nickNameRequest.setNickName(nickNameStr);
			nickNameRequest.setUserId(userId);
		}else if(HtmlConstants.OPERATE_NICKNAME_CHECKANDUPDATE.equals(operateType)){
			String nickNameStr = httpServletRequest.getParameter("nickName");
			nickNameRequest.setNickName(nickNameStr);
			nickNameRequest.setUserId(userId);
		}
		else{
			nickNameRequest.setUserId(userId);
		}
		nickNameRequest.setOperateType(operateType);
		return nickNameRequest;
	}

}
