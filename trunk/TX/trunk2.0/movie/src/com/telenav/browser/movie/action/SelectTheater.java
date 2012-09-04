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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
import com.telenav.browser.movie.datatype.MovieImage;
import com.telenav.browser.movie.datatype.Theater;
import com.telenav.browser.movie.exception.MovieException;
import com.telenav.browser.movie.service.ManagerFactory;
import com.telenav.browser.movie.service.MoviesManager;
import com.telenav.browser.movie.util.MovieThunderUtil;
import com.telenav.browser.movie.util.MovieUtil;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * @author lwei (lwei@telenav.cn) 4:59:19 PM, Oct 15, 2008
 */
public class SelectTheater extends BrowserBaseAction {

    protected ActionForward doAction(ActionMapping mapping,
            DataHandler handler, HttpServletRequest request,
            HttpServletResponse response) throws MovieException {
        TxNode idNode = (TxNode) handler.getParameter("movieId");
        long id = idNode.valueAt(0);

        // Get movie id and its detail.
        MoviesManager moviesManager = ManagerFactory.getMoviesManager();
        List movies = moviesManager.getMoviesByIds(new long[] { id });
        Movie movie = (Movie) movies.get(0);
        request.setAttribute("movie", movie);

        // Get movie small image [FOR NARMAL CASE & THUNDER]
        if (movie != null) {
        	/*
            String smallImageString = movie.getSmallImage();
            MovieImage smallImage;
            if (smallImageString != null) {
                String height = handler.getClientInfo(DataHandler.KEY_HEIGHT);
                String width = handler.getClientInfo(DataHandler.KEY_WIDTH);
                smallImage = moviesManager.getMovieImage(smallImageString,
                        width, height);
                if (smallImage != null) {
                    request.setAttribute("smallImage", smallImage);
                }
            }*/
        	MovieImage smallImage = getMovieImage(movie.getSmallImage(),handler,request);
            if (smallImage != null) {
                request.setAttribute("smallImage", smallImage);
            }
        }
        // Set multilayout images into request [FOR ANDROID]
		if (movie != null) {
			setMultiMovieImage(movie.getSmallImage(), handler, request, "small");
		}
        // Where(Theater)
        TxNode theaterIdArrayNode = (TxNode) handler
                .getShareData(Constants.THEATER_ID_ARRAY);
        long[] theaterId = new long[theaterIdArrayNode.msgsSize()];
        for (int i = 0; i < theaterId.length; i++) {
            theaterId[i] = Long.parseLong(theaterIdArrayNode.msgAt(i));
        }

        // Where(UserLocation)
        TxNode circleNode = (TxNode) handler
                .getShareData(Constants.SEARCH_CIRCLE);
        GeoCircle circle = GeoCircle.fromTxNode(circleNode);

        // When
        TxNode dateNode = (TxNode) handler.getShareData(Constants.MOVIES_DATE);
        Date date = new Date(Long.parseLong(dateNode.msgAt(1)));
        request.setAttribute("date", Constants.DATE_FORMAT.format(date));
        // Store shortDate
        request.setAttribute("shortDate", Constants.SHORT_DATE_FORMAT
                .format(date));
        // Get show time.
        Map schedule = moviesManager.searchScheduleByMovie(id, theaterId, date);
        Set keySet = schedule.keySet();
        List list = new ArrayList(keySet);
        // Set distance
        if (list != null) {
            Theater theater;
            for (int i = 0; i < list.size(); i++) {
                theater = (Theater) list.get(i);
                theater.setReferencePoint(circle.getLat(), circle.getLon());
            }
        }
        // Choose theaters and scheduleMap for ticketing
        List ticketTheaters = new ArrayList(list);
        Map ticketScheduleMap = new HashMap(schedule);

        // Remove unticketable theater and related schedule list
        MovieUtil.removeUnticket(ticketTheaters, ticketScheduleMap);

        // Sort theater list by distance
        list = MovieUtil.sortTheaters(list);
        ticketTheaters = MovieUtil.sortTheaters(ticketTheaters);

        request.setAttribute("ticketTheaters", ticketTheaters);
        request.setAttribute("ticketScheduleMap", ticketScheduleMap);

        request.setAttribute("scheduleMap", schedule);
        request.setAttribute("theaterList", list);
        if(MovieThunderUtil.isThunder(handler))
        {
        	setPagingInfo(handler,request,id,list);
            // Store shortDate
            request.setAttribute("shortDate", MovieThunderUtil.getShortDate(date));
        }
        return mapping.findForward("success");
    }
    
    /**
     * 
     * @param request
     * @param id
     */
    private void setPagingInfo(DataHandler handler,HttpServletRequest request,long id,List list)
    {
    	TxNode idNode = (TxNode) handler.getParameter("movieIds");
    	if(idNode == null) return ;
    	
    	String ids = idNode.msgAt(0);
    	String[] movieIds = ids.split(",");
    	List movieList = Arrays.asList(movieIds);

    	String nextId = "";
    	String previousId = "";
        int index = movieList.indexOf(id+"");
        if(index != 0)
        {
        	previousId = movieList.get(index-1) + "";
        }
        
        if(index != movieList.size()-1)
        {
        	nextId = movieList.get(index+1) + "";
        }
        
        StringBuffer sb = new StringBuffer();
        Iterator i = list.iterator();
        while(i.hasNext()) 
        {
        	Theater theater = (Theater) i.next();
        	sb.append("," + theater.getId());
        }
        if(sb.length() >=0)
        {
        	sb.deleteCharAt(0);
        }
    	//check the next/previous flag 
    	request.setAttribute("nextId", nextId);
    	request.setAttribute("previousId",previousId);
    	request.setAttribute("movieIds",ids);
    	request.setAttribute("theaterIds",sb.toString());
    	
    }
}
