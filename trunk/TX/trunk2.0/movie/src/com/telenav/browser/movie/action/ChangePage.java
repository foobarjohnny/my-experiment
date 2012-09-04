/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Nov 3, 2008
 * File name: ChangePage.java
 * Package name: com.telenav.j2me.browser.action
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 7:08:25 PM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.browser.movie.Constants;
import com.telenav.browser.movie.datatype.GeoCircle;
import com.telenav.browser.movie.datatype.Movie;
import com.telenav.browser.movie.datatype.Schedule;
import com.telenav.browser.movie.datatype.Theater;
import com.telenav.browser.movie.db.impl.DaoUtil;
import com.telenav.browser.movie.exception.MovieException;
import com.telenav.browser.movie.service.ManagerFactory;
import com.telenav.browser.movie.service.MoviesManager;
import com.telenav.browser.movie.util.MovieThunderUtil;
import com.telenav.browser.movie.util.MovieUtil;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * @author lwei (lwei@telenav.cn) 7:08:25 PM, Nov 3, 2008
 */
public class ChangePage extends BrowserBaseAction {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.telenav.j2me.browser.action.BrowserBaseAction#doAction(org.apache
     * .struts.action.ActionMapping, com.telenav.tnbrowser.util.DataHandler,
     * javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */

    protected ActionForward doAction(ActionMapping mapping,
            DataHandler handler, HttpServletRequest request,
            HttpServletResponse response) throws MovieException {

        int sortBy;
        long[] movieIdArray;
        int pageNumber;
        String timestamp;
        int pageSize = MovieUtil.getMoviePageSize(handler);
        
        // Check whether this page is from wrap.
        if (!"true".equals(request.getParameter("fromWrap"))) {
            // Movie id array.
			int sortType = Integer.parseInt(request.getParameter("sortBy"));
			TxNode movieId;
			if (MoviesManager.SORT_BY_NAME == sortType) {
				movieId = (TxNode) handler
						.getShareData(Constants.MOVIES_ID_ARRAY_SORT_BY_NAME);
			} else if (MoviesManager.SORT_BY_RATING == sortType) {
				movieId = (TxNode) handler
						.getShareData(Constants.MOVIES_ID_ARRAY_SORT_BY_RATING);
			} else {
				movieId = (TxNode) handler
						.getShareData(Constants.MOVIES_ID_ARRAY_SORT_BY_NEWOPENING);
			}
            
            StringBuffer sb = new StringBuffer();
            sb.append(movieId.msgAt(0));
            for (int i = 1; i < movieId.msgsSize(); i++) {
                sb.append(",").append(movieId.msgAt(i));
            }
            request.setAttribute("movieIds", sb.toString());

            // Set total size.
            request.setAttribute("moviePagesAmount", new Integer(MovieUtil
                    .pageAmount(movieId.msgsSize(),pageSize )));
            request
                    .setAttribute("shortDate", request
                            .getParameter("shortDate"));
            request
                    .setAttribute("timestamp", request
                            .getParameter("timestamp"));
            request.setAttribute("sortBy", request.getParameter("sortBy"));
			request.setAttribute("startPage", new Integer(MovieUtil
					.filterLastPara(request.getParameter("pageNumber"))));
            //
            return mapping.findForward("wrap");
        }

        // Get parameters.
        sortBy = Integer.parseInt(request.getParameter("sortBy"));
		pageNumber = Integer.parseInt(MovieUtil.filterLastPara(request
				.getParameter("pageNumber")));
        timestamp = request.getParameter("timestamp");
        String[] split = request.getParameter("movieIds").split(",");
        movieIdArray = new long[split.length];
        for (int i = 0; i < split.length; i++) {
            movieIdArray[i] = Long.parseLong(split[i]);
        }

        request.setAttribute("timestamp", timestamp);
        request.setAttribute("sortBy", new Integer(sortBy));
        request.setAttribute("pageNumber", pageNumber + "");
        // Store short date
        request.setAttribute("shortDate", request.getParameter("shortDate"));

        // Set pagination information.
        request.setAttribute("currentPage", new Integer(pageNumber));
        if (pageNumber > 0) {
            request.setAttribute("previousPage", new Boolean(true));
        } else {
            request.setAttribute("previousPage", new Boolean(false));
        }

        int movieSize = pageSize;
        if (movieSize >= movieIdArray.length - pageNumber * pageSize) {
            movieSize = movieIdArray.length - pageNumber * pageSize;
            request.setAttribute("nextPage", new Boolean(false));
        } else {
            request.setAttribute("nextPage", new Boolean(true));
        }

        // Get movies
        long[] idArray = new long[movieSize];
        for (int i = 0; i < movieSize; i++) {
            idArray[i] = movieIdArray[i + pageNumber * pageSize];
        }

        // Set amount of movie pages
        request.setAttribute("moviePagesAmount", new Integer(MovieUtil
                .pageAmount(movieIdArray.length, pageSize)));

        MoviesManager moviesManager = ManagerFactory.getMoviesManager();
        List movies = moviesManager.getMoviesByIds(idArray);
        // sort movies

        movies = MovieUtil.sortMovies(movies, sortBy);
        if(MovieUtil.needNearestTheater(handler))
        {
	        //get nearesttheater
	        getNearestTheater(request,movies);
        }
        //
        request.setAttribute("movies", movies);
        return mapping.findForward("success");
    }
    
    /**
     * 
     * @param handler
     * @param movies
     * @throws MovieException
     */
    private void getNearestTheater(HttpServletRequest request,List movies) throws MovieException 
    {
        // Theater Ids
    	String[] split = request.getParameter("theaterIds").split(",");
        long[] theaterId = new long[split.length];
        for (int i = 0; i < split.length; i++) {
        	theaterId[i] = Long.parseLong(split[i]);
        }
        
        long[] movieIds = new long[movies.size()];
    	StringBuffer sb = new StringBuffer();
        for (int i = 0; i < movies.size(); i++) {
    		//get movie
    		Movie movie = (Movie)movies.get(i); 
    		sb.append(",").append(movie.getId());
        	movieIds[i] = movie.getId();
        }
    	//get the movie list by string
	if(sb.length() >=0)
	{
    		sb.deleteCharAt(0);
    	}
    	request.setAttribute("movieIds",sb.toString());
    	
        //Location
        String[] splitLocation = request.getParameter("circle").split(",");
        GeoCircle circle = new GeoCircle();
        circle.setLat(Double.parseDouble(splitLocation[0]));
        circle.setLon(Double.parseDouble(splitLocation[1]));
        circle.setRadius(Integer.parseInt(splitLocation[2]));

        //date
        String strDate = request.getParameter("date");
        Date date = new Date(Long.parseLong(strDate));
    	// Get show time.
        //System.out.println("getNearestTheater Start:" + System.currentTimeMillis());
    	MoviesManager moviesManager = ManagerFactory.getMoviesManager();
    	//first get all the possible theater detail base on the location
    	List possibleTheaterList = moviesManager.getTheatersByIds(theaterId);
    	//put possibleTheaterList to a Map
    	Map theaterMap = new HashMap();
    	Iterator it0 = possibleTheaterList.iterator();
		while(it0.hasNext())
		{
			Theater t0 = (Theater)it0.next();
			t0.setReferencePoint(circle.getLat(), circle.getLon());
			theaterMap.put(t0.getId()+"", t0);
		}
		
    	//get all the possible schedule detisl base on theater ids and movies ids
    	List possibleScheduleList = moviesManager.searchScheduleByMovie(movieIds,theaterId,date);
    	//put possibleScheduleList to a Map
    	Iterator itS = possibleScheduleList.iterator();
    	Map scheduleMap = new HashMap();
		while(itS.hasNext())
		{
			Schedule tempSchedule = (Schedule)itS.next();
			scheduleMap.put(getTempScheduleKey(tempSchedule), tempSchedule);
		}
		
		/**
		 * loop all the movies list to setup the nearest theater
		 */
    	for(int j=0;j<movies.size();j++)
    	{
    		Movie movie = (Movie)movies.get(j); 
    		
    		//get the theater list 
    		List tempTheaterList = new ArrayList();
    		Iterator it1 = possibleScheduleList.iterator();
    		while(it1.hasNext())
    		{
    			Schedule s1 = (Schedule)it1.next();
    			if(s1.getMovieId() == movieIds[j])
    			{
    				Theater t1 = (Theater)theaterMap.get(s1.getTheaterId()+"");
    				tempTheaterList.add(t1);
    			}
    		}
    		
	        // Sort theater list by distance
    		tempTheaterList = MovieUtil.sortTheaters(tempTheaterList);
	        Theater nearTheater = new Theater();
	        if(tempTheaterList.size() >0)
	        {
	        	nearTheater = (Theater)tempTheaterList.get(0);
	        }
	        movie.setNearestTheater(nearTheater);
	        
	        //get the schedule
	        // Schedule
	        Schedule nearSchedule = (Schedule)scheduleMap.get(getTempScheduleKey(movie.getId(),nearTheater.getId(),date));
	        if(nearSchedule != null)
	        {
		        List searchSchedule = DaoUtil.divideRawSchedule(nearSchedule);
		        //List searchSchedule = moviesManager.searchSchedule(movie.getId(), nearTheater.getId(),date);
		        String showTime = MovieThunderUtil.composeShowTimeAm(searchSchedule,nearTheater.getTimeZone());
		        movie.setNearestTheaterShowTime(showTime);
	        }
    	}
    	//System.out.println("getNearestTheater End  :" + System.currentTimeMillis());
    	
    	//get search title
		StringBuffer title = new StringBuffer();
		title.append("Where:");
		title.append((String)request.getParameter("location"));
		title.append("\n");
		title.append("When:");
		title.append(MovieThunderUtil.getShortDate(date));
		//
		request.setAttribute("title",title.toString());
    }
    
    /**
     * 
     * @param s
     * @return
     * @throws MovieException 
     */
    private String getTempScheduleKey(Schedule s) throws MovieException
    {
    	String key = "";
    	
    	if(s != null)
    	{
    		key = getTempScheduleKey(s.getMovieId(),s.getTheaterId(),s.getDate());
    	}
    	
    	return key;
    }
    
    /**
     * 
     * @param movieId
     * @param theaterId
     * @param scheduleDate
     * @return
     * @throws MovieException
     */
    private String getTempScheduleKey(long movieId,long theaterId, Date scheduleDate) throws MovieException
    {
    	String key = "";
    	try
    	{
	    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.US);

	    	key = movieId + "_" + theaterId + "_" + dateFormat.format(scheduleDate);
    	}
    	catch(Exception e)
    	{
    		throw new MovieException(e);
    	}
    	
    	return key;
    }
}
