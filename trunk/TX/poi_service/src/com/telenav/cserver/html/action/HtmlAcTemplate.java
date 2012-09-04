package com.telenav.cserver.html.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.me.JSONObject;

import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.framework.html.util.HtmlFrameworkConstants;
import com.telenav.cserver.html.util.HtmlAcTemplateConfig;
import com.telenav.cserver.html.util.HtmlAcTemplateField;

public class HtmlAcTemplate extends HtmlPoiBaseAction
{
	public ActionForward doAction(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		String forwardName = HtmlCommonUtil.getString((String) request.getParameter("module"));
		HtmlClientInfo clientInfo = (HtmlClientInfo) request.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
		String region = clientInfo.getRegion();
		List<List<HtmlAcTemplateField>> acTemplate = HtmlAcTemplateConfig.getAcTemplate(region);
		JSONObject regionAutoSuggest = HtmlAcTemplateConfig.getRegionAutoSuggest(region);
		if ("".equals(forwardName)) {
			forwardName = "success";
		}
		request.setAttribute("acTemplate", acTemplate);
		request.setAttribute("regionAutoSuggest", regionAutoSuggest);
		return mapping.findForward(forwardName);
	}
}
