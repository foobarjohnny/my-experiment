/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.browser.movie.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.browser.movie.Constants;
import com.telenav.browser.movie.datatype.Schedule;
import com.telenav.browser.movie.exception.MovieException;
import com.telenav.browser.movie.service.ManagerFactory;
import com.telenav.browser.movie.service.MoviesManager;
import com.telenav.browser.movie.util.MovieThunderUtil;
import com.telenav.browser.movie.util.MovieUtil;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * @author pzhang
 *
 * @version 1.0, 2009-4-13
 */
public class SelectShowTime extends BrowserBaseAction{
    protected ActionForward doAction(ActionMapping mapping,
            DataHandler handler, HttpServletRequest request,
            HttpServletResponse response) throws MovieException {
    	
    	String action = MovieThunderUtil.getString(request.getParameter("action"));
    	action = MovieUtil.filterLastPara(action);
    	String path = "";
 
    	if("buyticket".equals(action))
    	{
    		Map info = new HashMap();
    		TxNode node = null;
    		//
    		node = (TxNode) handler.getParameter("movieName");
		    info.put("movieName", node.msgAt(0));
		    //
		    node = (TxNode) handler.getParameter("theaterName");
		    info.put("theaterName", node.msgAt(0));		    
    		//
		    //
		    node = (TxNode) handler.getParameter("scheduleId");
		    long id = Long.parseLong(node.msgAt(0));
		    MoviesManager moviesManager = ManagerFactory.getMoviesManager();
		    Schedule s= moviesManager.getScheduleById(id);
		    //
		    //show time
		    node = (TxNode) handler.getParameter("showTime");
		    String showTime = node.msgAt(0);
		    
    		node = (TxNode) handler.getParameter("theaterVendorId");
		    String theaterVendorId = node.msgAt(0);
		    

		    String trueDateString = "";
		    int marginDay = MovieUtil.getMarginDay(showTime);
            if (marginDay == 0) {
                trueDateString = Constants.DATE_FORMAT
                .format(s.getDate());
            } else {
                trueDateString = Constants.DATE_FORMAT
                        .format(MovieUtil.getTrueDate(s.getDate(),
                                showTime));
            }
		    info.put("showDate", trueDateString);
		    info.put("showTime", showTime);
		    
            String trueShowTime = MovieUtil.formatShowTime(showTime);
            String numberDateString = Constants.NUMBER_DATE_FORMAT
                    .format(MovieUtil.getTrueDate(s.getDate(),
                            trueShowTime));
            String wapLink = MovieUtil.revise(MovieUtil.getWapLink(s
                    .getVendorName(), trueShowTime, s
                    .getTicketURI(), numberDateString,theaterVendorId));
            
            info.put("wapLink",wapLink);
		    request.setAttribute("info", info);
    		path = "buyticket";
    	}
    	else if("chooseschedule".equals(action))
    	{
    		Map info = new HashMap();
    		TxNode node = null;
    		//
    		node = (TxNode) handler.getParameter("movieName");
		    info.put("movieName", node.msgAt(0));
		    //
		    node = (TxNode) handler.getParameter("theaterName");
		    info.put("theaterName", node.msgAt(0));		    
    		//
		    node = (TxNode) handler.getParameter("scheduleId");
		    info.put("scheduleId", node.msgAt(0));
		    //
    		node = (TxNode) handler.getParameter("theaterVendorId");
    		info.put("theaterVendorId", node.msgAt(0));
		    //
		    //show time
		    node = (TxNode) handler.getParameter("BuyTicketShowTime");
		    String showTimes = node.msgAt(0);
		    // Theater Ids
	    	String[] split = showTimes.split(",");
	        List scheduleList = new ArrayList();
	        for (int i = 0; i < split.length; i++) {
	        	scheduleList.add(split[i]);
	        }
	        info.put("BuyTicketShowTime", scheduleList);
		    
		    request.setAttribute("info", info);
    		path = "chooseschedule";
    	}
    	
    	return mapping.findForward(path);
    }
}
