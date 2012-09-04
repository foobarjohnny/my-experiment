/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolResponseFormatter;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.html.executor.HtmlNickNameResponse;
import com.telenav.cserver.html.util.HtmlConstants;
/**
 * @TODO	format the data returned from browser server and put it into request
 * @author  chfzhang@telenav.cn
 * @version 1.0 Feb 18, 2011
 */
public class HtmlNickNameFormatter extends HtmlProtocolResponseFormatter{

	@Override
	public void parseBrowserResponse(HttpServletRequest httpServletRequest,
			ExecutorResponse response) throws Exception {
		
		HtmlNickNameResponse nickNameResponse = (HtmlNickNameResponse)response;
		String operateType = nickNameResponse.getOperateType();
		
		if(HtmlConstants.OPERATE_NICKNAME_GET.equals(operateType)||HtmlConstants.OPERATE_NICKNAME_ADD.equals(operateType)){
			httpServletRequest.setAttribute("ajaxResponse", toJSONString(nickNameResponse));
		}
		else if(HtmlConstants.OPERATE_NICKNAME_CHECK.equals(operateType)){
			httpServletRequest.setAttribute("ajaxResponse", toJSONStringFromCheck(nickNameResponse));
		}
		else if(HtmlConstants.OPERATE_NICKNAME_CHECKANDUPDATE.equals(operateType)){
			httpServletRequest.setAttribute("ajaxResponse", toJSONStringFromCheck(nickNameResponse));
		}
	}
	
	private String toJSONString(HtmlNickNameResponse nickNameResponse) throws JSONException{
		JSONObject json = new JSONObject();
		json.put("nickName", HtmlCommonUtil.getString(nickNameResponse.getNickName()));
		
		return json.toString();
	}

	private String toJSONStringFromCheck(HtmlNickNameResponse nickNameResponse) throws JSONException{
		JSONObject json = new JSONObject();
		json.put("isUniqueNickName", nickNameResponse.getIsUniqueNickName());
		json.put("nickName", HtmlCommonUtil.getString(nickNameResponse.getNickName()));
		
		return json.toString();
	}
}
