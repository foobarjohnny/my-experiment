/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Oct 15, 2008
 * File name: Startup.java
 * Package name: com.telenav.j2me.browser.action
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 4:59:19 PM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.action;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.browser.movie.Constants;
import com.telenav.browser.movie.datatype.GeoCircle;
import com.telenav.browser.movie.datatype.Theater;
import com.telenav.browser.movie.exception.MovieException;
import com.telenav.browser.movie.service.ManagerFactory;
import com.telenav.browser.movie.service.MoviesManager;
import com.telenav.browser.movie.util.MovieUtil;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * @author lwei (lwei@telenav.cn) 4:59:19 PM, Oct 15, 2008
 */
public class SearchTheaters extends Search {
    
    private final static String errorMsg = "No theaters found for your search criteria.";

    protected ActionForward doAction(ActionMapping mapping,
            DataHandler handler, HttpServletRequest request,
            HttpServletResponse response) throws MovieException {

    	Map crietria = this.getSearchCriteria(handler, request);
        // Get date.
        Date date = (Date)crietria.get("date");
        //
        GeoCircle circle = (GeoCircle)crietria.get("circle");
        // Get theaters.
        MoviesManager moviesManager = ManagerFactory.getMoviesManager();
        long[] theaterIds = moviesManager.searchTheaterIds(circle, date);
        // if theaterIds null ,return no result
        if (theaterIds == null || theaterIds.length < 1) {
            request.setAttribute("errorMsg", errorMsg);
            return mapping.findForward("fail");
        }

        List theaters = moviesManager.getTheatersByIds(theaterIds);
        // Compute distance
        for (Iterator iterator = theaters.iterator(); iterator.hasNext();) {
            Theater theater = (Theater) iterator.next();
            theater.setReferencePoint(circle.getLat(), circle.getLon());
        }
        // Sort theaters by distance
        theaters = MovieUtil.sortTheaters(theaters);
        // Set
        request.setAttribute("theaterList", theaters);
        // Store theaters id after sort
        TxNode idNode = new TxNode();
        for (int i = 0; i < theaters.size(); i++) {
            long l = ((Theater) theaters.get(i)).getId();
            idNode.addMsg(l + "");
        }
        handler.addShareData(Constants.THEATER_ID_ARRAY, idNode);

        // Save the attribute.
        StringBuffer sb = new StringBuffer();
        sb.append("" + ((Theater) theaters.get(0)).getId());
        for (int i = 1; i < theaters.size(); i++) {
            long l = ((Theater) theaters.get(i)).getId();
            sb.append(",");
            sb.append(l);
        }

        request.setAttribute("theaterTimestamp", System.currentTimeMillis()
                + "");
        request.setAttribute("theaterIds", sb.toString());
        request.setAttribute("theaterStartPage", new Integer(0));
        request.setAttribute("theaterPagesAmount", new Integer(MovieUtil
                .pageAmount(theaterIds.length, Constants.PAGE_SIZE)));
        request.setAttribute("lon", new Long(
                (long) (circle.getLon() * GeoCircle.MULTIPLIER)));
        request.setAttribute("lat", new Long(
                (long) (circle.getLat() * GeoCircle.MULTIPLIER)));
        request.setAttribute("radius", new Integer(circle.getRadius()));

        //if(MovieThunderUtil.isThunder(handler))
        //{
            //Add for thunder, pass date/location/theater id to screen.
            request.setAttribute("date", date.getTime()+"");
            request.setAttribute("circle", circle.getLat() + "," + circle.getLon()+ "," + circle.getRadius());
        //}
        
        return mapping.findForward("success");
    }
}
