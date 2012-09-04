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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.browser.movie.Constants;
import com.telenav.browser.movie.datatype.GeoCircle;
import com.telenav.browser.movie.datatype.Movie;
import com.telenav.browser.movie.datatype.MovieImage;
import com.telenav.browser.movie.datatype.Schedule;
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
public class SelectSchedule extends BrowserBaseAction {

    protected ActionForward doAction(ActionMapping mapping,
            DataHandler handler, HttpServletRequest request,
            HttpServletResponse response) throws MovieException {
        MoviesManager moviesManager = ManagerFactory.getMoviesManager();

        // What
        TxNode movieIdNode = (TxNode) handler.getParameter("movieId");
        long movieId = movieIdNode.valueAt(0);
        List movies = moviesManager.getMoviesByIds(new long[] { movieId });
        Movie movie = (Movie) movies.get(0);
        request.setAttribute("movie", movie);

        // Get movie small image [FOR NORMAL CASE & THUNDER]
        if (movie != null) {
        	/*
            String smallImageString = movie.getSmallImage();
            MovieImage smallImage;
            if (smallImageString != null) {
                String height = handler.getClientInfo(DataHandler.KEY_HEIGHT);
                String width = handler.getClientInfo(DataHandler.KEY_WIDTH);
                smallImage = moviesManager.getMovieImage(smallImageString,
                        width, height);

            }*/
        	MovieImage smallImage = getMovieImage(movie.getSmallImage(),handler,request);
            if (smallImage != null) {
                request.setAttribute("smallImage", smallImage);
            }
        }
        
        // Set multilayout images into request [FRO ANDROID]
		if (movie != null) {
			setMultiMovieImage(movie.getSmallImage(), handler, request, "small");
		}

        // Where
        TxNode theaterIdNode = (TxNode) handler.getParameter("theaterId");
        long theaterId = theaterIdNode.valueAt(0);
        List theaters = moviesManager
                .getTheatersByIds(new long[] { theaterId });
        request.setAttribute("theater", theaters.get(0));

        // When
        TxNode dateNode = (TxNode) handler.getShareData(Constants.MOVIES_DATE);
        Date date = new Date(Long.parseLong(dateNode.msgAt(1)));
        request.setAttribute("date", Constants.DATE_FORMAT.format(date));

        // Schedule
        List searchSchedule = moviesManager.searchSchedule(movieId, theaterId,
                date);
        String path = "";
        // Check the theater is ticketing
		boolean theaterTicketing = MovieUtil
				.checkTheaterTicketing(searchSchedule)
				&& !MovieUtil.isDisableLinkDevice(handler);
        if (!theaterTicketing) {
            // Store shortDate
            request.setAttribute("shortDate", Constants.SHORT_DATE_FORMAT
                    .format(date));
            // Get showTime
            String showTime = MovieUtil.composeShowTimeAm(searchSchedule);
            request.setAttribute("showTime", showTime);
            path = "noticket";
        } else {
            request.setAttribute("scheduleList", searchSchedule);
            path = "success";
        }
        
        if(MovieThunderUtil.isThunder(handler))
        {
        	path = "success";
        	Theater theater = (Theater)theaters.get(0);
            request.setAttribute("shortDate", MovieThunderUtil.getShortDate(date));
        	request.setAttribute("showTime", MovieThunderUtil.composeShowTimeAm(searchSchedule,theater.getTimeZone()));
        	
        	List scheduleList = MovieThunderUtil.filterShowTime(searchSchedule,theater.getTimeZone());
        	request.setAttribute("scheduleList",scheduleList);
        	
        	if(scheduleList != null)
        	{
        		StringBuffer sb = new StringBuffer();
        		for(int i=0;i<scheduleList.size();i++)
        		{
        			Schedule s = (Schedule)scheduleList.get(i);
        			sb.append("," + s.getShowTime());
        		}
        		if(sb.length() >=0)
        		{
        			sb.deleteCharAt(0);
        		}
        		request.setAttribute("BuyTicketShowTime",sb.toString());
        	}
        	setPagingInfo(handler,request,movieId,theater);
        }
        
        return mapping.findForward(path);
    }

    /**
     * 
     * @param handler
     * @param request
     * @param movieId
     * @param theaterId
     */
    private void setPagingInfo(DataHandler handler,HttpServletRequest request,long movieId,Theater theater)
    {
    	String[] ids = null;
    	long currentId = 0;
    	TxNode theaterIdNode = (TxNode) handler.getParameter("theaterIds");
    	TxNode movieIdNode = (TxNode) handler.getParameter("movieIds");
    	if(movieIdNode == null && theaterIdNode == null)
    	{
    		return ;
    	}
    	
    	//come from "MoviesInfoShowTimes.jsp"
    	if(theaterIdNode != null)
    	{
    		request.setAttribute("theaterIds",theaterIdNode.msgAt(0));
    		ids = theaterIdNode.msgAt(0).split(",");
    		currentId = theater.getId();
    	}
    	

    	//come from "SelectMovie.jsp"
    	if(movieIdNode != null)
    	{
    		request.setAttribute("movieIds",movieIdNode.msgAt(0));
    		ids = movieIdNode.msgAt(0).split(",");
    		currentId = movieId;
    	}
    		
    	List idList = Arrays.asList(ids);

    	String nextId = "";
    	String previousId = "";
        int index = idList.indexOf(currentId +"");
        if(index != 0)
        {
        	previousId = idList.get(index-1) + "";
        }
        
        if(index != idList.size()-1)
        {
        	nextId = idList.get(index+1) + "";
        }
        
        //TxNode distanceNode = (TxNode) handler.getParameter("distance");
        TxNode circleNode = (TxNode) handler.getShareData(Constants.SEARCH_CIRCLE);
        GeoCircle circle = GeoCircle.fromTxNode(circleNode);
        theater.setReferencePoint(circle.getLat(), circle.getLon());
    	//check the next/previous flag
        request.setAttribute("theater", theater);
    	request.setAttribute("nextId", nextId);
    	request.setAttribute("previousId",previousId);
    	
    	String indexText = " " + (index + 1) + " of " + idList.size() + " ";
    	request.setAttribute("indexText",indexText);
    	
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
