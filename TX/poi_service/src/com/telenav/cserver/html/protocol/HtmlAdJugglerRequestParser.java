/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONObject;

import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolRequestParser;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.framework.html.util.HtmlFrameworkConstants;
import com.telenav.cserver.html.executor.HtmlAdJugglerRequest;
import com.telenav.cserver.html.util.HtmlAdjugglerUtil;
/**
 * @TODO	Define executor type
 * 			Parse the browser request
 * @author  
 * @version 1.0 
 */
public class HtmlAdJugglerRequestParser extends HtmlProtocolRequestParser {
	@Override
	public String getExecutorType() {
		return "HtmlAdJuggler";
	}

	@Override
	public ExecutorRequest parseBrowserRequest(HttpServletRequest object)
			throws Exception {
//		HtmlAdJugglerRequest request = new HtmlAdJugglerRequest();
//		HttpServletRequest httpRequest = (HttpServletRequest) object;
//
//		String jsonString = httpRequest.getParameter("jsonStr");
//		JSONObject json = new JSONObject(jsonString);
//		request.setActionJO(json);
//		String imageKey = (String) httpRequest
//				.getAttribute(BrowserFrameworkConstants.IMAGE_KEY);
//		request.setImageKey(imageKey);
//
//		HtmlClientInfo clientInfo = (HtmlClientInfo) httpRequest
//				.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
//		request.setClientInfo(clientInfo);
//		
//		String ssoToken = (String) httpRequest.getParameter("ssoToken");
//		String userId = HtmlCommonUtil.getUserId(ssoToken);
//		
//		if("".equals(userId)){
//			userId = "9826225";
//		}
//		request.setUserId(userId);
//		
//		String hostUrl = (String) httpRequest.getAttribute("Host_url");
//		request.setHostUrl(hostUrl);
//		
//		JSONObject paramJSON = new JSONObject();
//		paramJSON.put("ssoToken", ssoToken);
//		String appCode = httpRequest.getParameter("appCode");
//		paramJSON.put("appCode", appCode);
//		String clientInfoStr = httpRequest.getParameter("clientInfo");
//		paramJSON.put("clientInfoStr", clientInfoStr);
//		String width = httpRequest.getParameter("width");
//		paramJSON.put("width", width);
//		String height = httpRequest.getParameter("height");
//		paramJSON.put("height", height);
//		String loginLocator = HtmlAdjugglerUtil.getSearviceLocator(hostUrl,
//				"LOGIN");
//		paramJSON.put("loginLocator", loginLocator);
//		String movieLocator = HtmlAdjugglerUtil.getSearviceLocator(hostUrl,
//				"MOVIE");
//		paramJSON.put("movieLocator", movieLocator);
//		request.setParamJSON(paramJSON);
//
//		return request;
		return null;
	}
}
