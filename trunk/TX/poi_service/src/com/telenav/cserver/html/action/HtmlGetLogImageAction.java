/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.html.executor.HtmlGetLogImageResponse;

/**
 * HtmlGetLogImageAction.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Feb 22, 2011
 *
 */
public class HtmlGetLogImageAction extends HtmlPoiBaseAction 
{
    private static final Logger logger = Logger.getLogger(HtmlGetLogImageAction.class);
    
    @Override
	/**
	 * 1) parse paramter from httprequest
	 * 2) run logic in execute
	 * 3) format httprequest to front end
	 */
    protected ActionForward doAction(ActionMapping mapping, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception
    {
        try {
            ExecutorRequest[] requests = this.requestParser.parse(httpRequest);
            HtmlGetLogImageResponse response = new HtmlGetLogImageResponse();
            if (requests != null && requests.length > 0) 
            {
                ExecutorDispatcher.getInstance().execute(requests[0], response,new ExecutorContext());
                
            } 
            if( response.getStatus() == ExecutorResponse.STATUS_FAIL )
            {
                httpRequest.setAttribute("errorCode", new Long(-1));
                httpRequest.setAttribute("errorMsg", response.getErrorMessage());
                return mapping.findForward("failure");
            }
            this.responseFormatter.format(httpRequest, new ExecutorResponse[]{response});
            return mapping.findForward("success");
            
        }
        catch (Exception e) 
        {
            logger.error("HtmlGetLogImageAction#doAction",e);
            ActionMessages msgs = new ActionMessages();
            msgs.add("getlogimage", new ActionMessage("errors.poi.failed"));
            addErrors(httpRequest, msgs);
            return mapping.findForward("failure");
        }
    }
    
}
