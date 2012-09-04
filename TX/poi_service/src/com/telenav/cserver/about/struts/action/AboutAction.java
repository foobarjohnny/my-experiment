/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.about.struts.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.cserver.PoiBaseAction;
import com.telenav.cserver.backend.telepersonalize.TelepersonalizationFacade;
import com.telenav.cserver.backend.telepersonalize.UserStatus;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.browser.util.MessageHelper;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.cserver.util.TnConstants;
import com.telenav.cserver.util.TnUtil;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * @author pzhang
 *
 * @version 1.0, 2009-3-2
 */
public class AboutAction extends PoiBaseAction{
	
	private static Logger log = Logger.getLogger(AboutAction.class);
	
	protected ActionForward doAction(ActionMapping mapping,
			HttpServletRequest request, HttpServletResponse response) { 
		DataHandler handler = (DataHandler) request
        .getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
		
		String action = TnUtil.getString(request.getParameter("action"));
		action = TnUtil.filterLastPara(action);
		log.info("AboutAction:action is:" + action);
		
		String path = "";
		Map screenParameter = new HashMap();
		if("".equals(action))
		{
			path = "aboutMenu";
		}
		else
		{
			path = action;
		}
		
		if("aboutPin".equals(action))
		{
			//String pin = handler.getClientInfo(DataHandler.KEY_USERPIN);
			String account = handler.getClientInfo(DataHandler.KEY_USERACCOUNT);
			String userID = handler.getClientInfo(DataHandler.KEY_USERID);
			String pin = fetchPin(account,userID,handler);
			
			pin = TnUtil.getString(pin);
			
		     if ("".equals(pin))
		     {
		    	 //TODO should get a assigned string for this case
		 		 pin = MessageHelper.getInstance().getMessageValue(handler, "about.pin.unknown");
		 	 }

		     screenParameter.put("pin", pin);
		}
		request.setAttribute(TnConstants.PARAMETER_SCREEN, screenParameter);
		request.setAttribute("handler", handler);
		
		return mapping.findForward(path);
	}
	
	/**
	 * fetch pin with userId, if userId null, query userId by ptn first.
	 * @param ptn
	 * @param userId
	 * @return
	 */
	public String fetchPin(String ptn, String userId,DataHandler handler)
	{
		String pin = "";
		TnContext tncontext = PoiUtil.getTnContext(handler);
		String tempUserId = userId;
		if("".equals(PoiUtil.getString(tempUserId)))
		{
			UserStatus status;
			try {
				status = TelepersonalizationFacade.getUserProfileViaPTN(ptn, tncontext);
				tempUserId = String.valueOf(status.getUserId());
			} catch (ThrottlingException e) {
				// TODO Auto-generated catch block
				log.error("error occured when get user profile via PTN!");
				log.error("cause:"+e.getCause()+",message:"+e.getMessage());
				e.printStackTrace();
			}
			log.debug(" ----- Fetch userid : " + tempUserId);
		}

		if(!"".equals(PoiUtil.getString(tempUserId)))
		{
			UserStatus status;
			try {
				status = TelepersonalizationFacade.getUserProfile(tempUserId, tncontext);
				pin = status.getPassword();
			} catch (ThrottlingException e) {
				// TODO Auto-generated catch block
				log.error("error occured when get user profile via PTN!");
				log.error("cause:"+e.getCause()+",message:"+e.getMessage());
				e.printStackTrace();
			}
			log.debug(" ----- Fetch password : " + pin);	
		}

		System.out.println("-----pin:" + pin);
		return pin;
	}
	
	
}
