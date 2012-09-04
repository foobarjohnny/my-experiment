/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.browser.movie.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.browser.movie.Constants;
import com.telenav.browser.movie.exception.MovieException;
import com.telenav.browser.movie.util.MovieThunderUtil;
import com.telenav.browser.movie.util.MovieUtil;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * @author pzhang
 *
 * @version 1.0, 2009-3-17
 */
public class NewSearch extends BrowserBaseAction{
	
    protected ActionForward doAction(ActionMapping mapping,
            DataHandler handler, HttpServletRequest request,
            HttpServletResponse response) throws MovieException {
    	
    	String action = MovieThunderUtil.getString(request.getParameter("action"));
    	action = MovieUtil.filterLastPara(action);
    	String path = "";

    	if("edit".equals(action) || "newsearch".equals(action))
    	{
    		//from
    		String from = MovieThunderUtil.getString(request.getParameter("from"));
    		from = MovieUtil.filterLastPara(from);
    		request.setAttribute("from", from);
            // When
            TxNode dateNode = (TxNode) handler.getShareData(Constants.MOVIES_DATE);
            Date date = new Date(Long.parseLong(dateNode.msgAt(1)));
            request.setAttribute("date", MovieThunderUtil.getLongDate(date));
            
            // Where
            TxNode location;
            Stop stop;
            
            Object o = handler.getShareData(Constants.SEARCH_LOCATION);
            if(o instanceof TxNode)
            {
            	location = (TxNode)o;
            	stop = new Stop();
    			stop.lat = (int) location.valueAt(1);
    			stop.lon = (int) location.valueAt(2);
            }
            else
            {
            	stop = (Stop)o;
            	location = stop.toTxNode();
            }
            

            handler.setParameter("addrFromAC", location);
            request.setAttribute("location", getStopText(stop,request));
    		
    		path = action;
    	}

    	return mapping.findForward(path);
    }
}
