/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.html.executor.HtmlAdsResponse;
import com.telenav.cserver.html.util.HtmlConstants;


/**
 * get ads screen action class
 * @author  panzhang@telenav.cn
 * @version 1.0 2011-3-3
 */
public class HtmlAdsAction extends HtmlPoiBaseAction{
	
	@Override
	/**
	 * 1) parse paramter from httprequest
	 * 2) run logic in execute
	 * 3) format httprequest to front end
	 */
    protected ActionForward doAction(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            ExecutorRequest[] executorRequests = requestParser.parse(request);
            HtmlAdsResponse executorResponse = new HtmlAdsResponse();

            if (executorRequests != null && executorRequests.length > 0) {
                ExecutorDispatcher ac = ExecutorDispatcher.getInstance();
                ac.execute(executorRequests[0], executorResponse,
                        new ExecutorContext());
            }
//            if (executorResponse.getStatus() == ExecutorResponse.STATUS_FAIL) {
//                request.setAttribute("errorCode", new Long(-1));
//                request.setAttribute("errorMsg", executorResponse.getErrorMessage());
//                return mapping.findForward("failure");
//            }
            
            responseFormatter.format(request,
                    new ExecutorResponse[] { executorResponse });
            
            String forwardName = "success";
            if(HtmlConstants.OPERATE_ADSVIEW_BASIC.equals(executorResponse.getOperateType()))
            {
                forwardName = "basic";
            }else if(HtmlConstants.OPERATE_ADSVIEW_DETAIL.equals(executorResponse.getOperateType())){
                forwardName = "detail";
            }else if(HtmlConstants. OPERATE_ADSVIEW_FETCH_DETAIL_DATA.equals(executorResponse.getOperateType())){
                forwardName = "success";
            }
            
            return mapping.findForward(forwardName);
        } catch (Exception e) {
            return mapping.findForward("Globe_Exception");
        } 
    }
   
}
