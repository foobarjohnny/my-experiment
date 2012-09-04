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
 *  Intial: lwei(lwei@telenav.cn) 4:59:19 PM, Oct 15, 2008
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.action;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.browser.movie.Constants;
import com.telenav.browser.movie.datatype.GeoCircle;
import com.telenav.browser.movie.datatype.Movie;
import com.telenav.browser.movie.datatype.Theater;
import com.telenav.browser.movie.exception.MovieException;
import com.telenav.browser.movie.service.ManagerFactory;
import com.telenav.browser.movie.service.MoviesManager;
import com.telenav.browser.movie.util.MovieThunderUtil;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * @author lwei (lwei@telenav.cn) 4:59:19 PM, Oct 15, 2008
 */
public class SelectMovie extends BrowserBaseAction {

    protected ActionForward doAction(ActionMapping mapping,
            DataHandler handler, HttpServletRequest request,
            HttpServletResponse response) throws MovieException {
        // Get theater id.
        TxNode idNode = (TxNode) handler.getParameter("theaterId");
        long theaterId = idNode.valueAt(0);
        request.setAttribute("theaterId", new Long(theaterId));

        // Get date.
        TxNode dateNode = (TxNode) handler.getShareData(Constants.MOVIES_DATE);
        Date date = new Date(Long.parseLong(dateNode.msgAt(1)));
        // Store shortDate
        request.setAttribute("shortDate", Constants.SHORT_DATE_FORMAT
                .format(date));
        // Get sort type.
        int sortType = MoviesManager.SORT_BY_DEFAULT;
        TxNode sortNode = (TxNode) handler.getParameter("sortBy");
        if (sortNode != null && sortNode.getValuesCount() > 0) {
            sortType = (int) sortNode.valueAt(0);
            if (sortType != MoviesManager.SORT_BY_NAME
                    && sortType != MoviesManager.SORT_BY_RATING) {
                sortType = MoviesManager.SORT_BY_DEFAULT;
            }
        }
        request.setAttribute("sortBy", new Integer(sortType));

        // Get movies.
        MoviesManager moviesManager = ManagerFactory.getMoviesManager();
        Map searchScheduleByTheater = moviesManager.searchScheduleByTheater(
                theaterId, date, sortType);
        request.setAttribute("movies", searchScheduleByTheater);

        if(MovieThunderUtil.isThunder(handler))
        {
        	setPagingInfo(handler,request,theaterId,searchScheduleByTheater.keySet());
        	request.setAttribute("shortDate", MovieThunderUtil.getShortDate(date));
        }
        return mapping.findForward("success");
    }
    
    /**
     * 
     * @param request
     * @param id
     * @throws MovieException 
     */
    private void setPagingInfo(DataHandler handler,HttpServletRequest request,long id,Set list) throws MovieException
    {
    	TxNode idNode = (TxNode) handler.getParameter("theaterIds");
    	if(idNode == null) return ;
    	
    	String ids = idNode.msgAt(0);
    	String[] theaterIds = ids.split(",");
    	List List = Arrays.asList(theaterIds);

    	String nextId = "";
    	String previousId = "";
        int index = List.indexOf(id+"");
        if(index != 0)
        {
        	previousId = List.get(index-1) + "";
        }
        
        if(index != List.size()-1)
        {
        	nextId = List.get(index+1) + "";
        }
        
        StringBuffer sb = new StringBuffer();
        Iterator i = list.iterator();
        while(i.hasNext()) 
        {
        	Movie movie = (Movie) i.next();
        	sb.append("," + movie.getId());
        }
        if(sb.length() >=0)
        {
        	sb.deleteCharAt(0);
        }
    	//check the next/previous flag 
    	request.setAttribute("nextId", nextId);
    	request.setAttribute("previousId",previousId);
    	request.setAttribute("theaterIds",ids);
    	request.setAttribute("movieIds",sb.toString());
    	
        // Get movie id and its detail.
        MoviesManager moviesManager = ManagerFactory.getMoviesManager();
        List theaters = moviesManager.getTheatersByIds(new long[] { id });
        Theater theater = (Theater) theaters.get(0);
        TxNode circleNode = (TxNode) handler.getShareData(Constants.SEARCH_CIRCLE);
        GeoCircle circle = GeoCircle.fromTxNode(circleNode);
        theater.setReferencePoint(circle.getLat(), circle.getLon());
        request.setAttribute("theater", theater);
        
    	//get Theater Location
    	TxNode theaterLocation = new TxNode();
    	theaterLocation.addValue(0);
    	theaterLocation.addValue((long)(theater.getLat() * Constants.LATLON_MULTIPLY));
    	theaterLocation.addValue((long)(theater.getLon() * Constants.LATLON_MULTIPLY));
    	
    	theaterLocation.addMsg("");
    	theaterLocation.addMsg(theater.getStreet());
    	theaterLocation.addMsg(theater.getCity());
    	theaterLocation.addMsg(theater.getState());
    	request.setAttribute("driveToLocation", theaterLocation);
    }

}
