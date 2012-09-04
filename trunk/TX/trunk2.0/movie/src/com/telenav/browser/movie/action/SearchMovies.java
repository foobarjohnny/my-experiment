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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.telenav.browser.movie.Constants;
import com.telenav.browser.movie.datatype.GeoCircle;
import com.telenav.browser.movie.exception.MovieException;
import com.telenav.browser.movie.service.ManagerFactory;
import com.telenav.browser.movie.service.MoviesManager;
import com.telenav.browser.movie.util.MovieUtil;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * @author lwei (lwei@telenav.cn) 4:59:19 PM, Oct 15, 2008
 */
public class SearchMovies extends Search {

    private final static String errorMsg = "No movies found for your search criteria.";

    protected ActionForward doAction(ActionMapping mapping,
            DataHandler handler, HttpServletRequest request,
            HttpServletResponse response) throws MovieException {

        Map crietria = this.getSearchCriteria(handler, request);
        // Get date.
        Date date = (Date) crietria.get("date");
        // Store shortDate
        request.setAttribute("shortDate", Constants.SHORT_DATE_FORMAT.format(
                date).replaceAll("\\s", "_"));

        GeoCircle circle = (GeoCircle) crietria.get("circle");

        if (circle == null) {
            // can't get GPS, forward to retry screen
            request.setAttribute("gpsFail", "Y");
            return mapping.findForward("retry");
        }

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
        // get sort from dropdown list
        else {
            TxNode sortSelectNode = (TxNode) handler
                    .getParameter("sortBySelect");
            if (sortSelectNode != null) {
                int selectIndex = (int) sortSelectNode.valueAt(0);

                switch (selectIndex) {
                case 0:
                    sortType = MoviesManager.SORT_BY_NAME;
                    break;
                case 1:
                    sortType = MoviesManager.SORT_BY_RATING;
                    break;
                case 2:
                    sortType = MoviesManager.SORT_BY_NEWOPENING;
                    break;
                }
            }
        }
        // Get the input location if available.
        MoviesManager moviesManager = ManagerFactory.getMoviesManager();
        long[] searchTheaters;
        // New search.
        if (circle != null) {
            circle.setRadius(Constants.SEARCH_RADIUS);
            searchTheaters = moviesManager.searchTheaterIds(circle, date);
            // if searchTheaters null ,return no result
            if (searchTheaters == null || searchTheaters.length < 1) {
                request.setAttribute("errorMsg", errorMsg);
                return mapping.findForward("fail");
            }
            TxNode theaterIdNode = new TxNode();
            for (int i = 0; i < searchTheaters.length; i++) {
                long l = searchTheaters[i];
                theaterIdNode.addMsg(l + "");
            }
            handler.addShareData(Constants.THEATER_ID_ARRAY, theaterIdNode);
            handler.addShareData(Constants.SEARCH_CIRCLE, circle.toTxNode());
        } else {
            // Load theater find before for only sorting.
            TxNode theaterIdNode = (TxNode) handler
                    .getShareData(Constants.THEATER_ID_ARRAY);
            searchTheaters = new long[theaterIdNode.msgsSize()];
            for (int i = 0; i < searchTheaters.length; i++) {
                searchTheaters[i] = Long.parseLong(theaterIdNode.msgAt(i));
            }
            // if searchTheaters null ,return no result
            if (searchTheaters == null || searchTheaters.length < 1) {
                request.setAttribute("errorMsg", errorMsg);
                return mapping.findForward("fail");
            }
        }

        // Get movies available.
        long[] searchMovie = moviesManager.searchMovieIds(searchTheaters, date,
                sortType);
        // if(MovieThunderUtil.isThunder(handler))
        // {
        String movieName = (String) crietria.get("movieName");
        // filter base on movie name
        if (movieName != null && !"".equals(movieName)) {
            // support %
            movieName = movieName.toUpperCase() + "%";
            //
            searchMovie = moviesManager.getMoviesIdsByName(searchMovie,
                    sortType, movieName);
        }
        // }
        // if searchMovie null ,return no result
        if (searchMovie == null || searchMovie.length < 1) {
            request.setAttribute("errorMsg", errorMsg);
            return mapping.findForward("fail");
        }
        TxNode idNode = new TxNode();
        for (int i = 0; i < searchMovie.length; i++) {
            long l = searchMovie[i];
            idNode.addMsg(l + "");
        }
        
		if (MoviesManager.SORT_BY_NAME == sortType) {
			handler
					.addShareData(Constants.MOVIES_ID_ARRAY_SORT_BY_NAME,
							idNode);
		} else if (MoviesManager.SORT_BY_RATING == sortType) {
			handler.addShareData(Constants.MOVIES_ID_ARRAY_SORT_BY_RATING,
					idNode);
		} else {
			handler.addShareData(Constants.MOVIES_ID_ARRAY_SORT_BY_NEWOPENING,
					idNode);
		}
        

        // Save the attribute.
        StringBuffer sb = new StringBuffer();
        sb.append("" + searchMovie[0]);
        for (int i = 1; i < searchMovie.length; i++) {
            long l = searchMovie[i];
            sb.append(",");
            sb.append(l);
        }

        request.setAttribute("timestamp", System.currentTimeMillis() + "");
        request.setAttribute("sortBy", new Integer(sortType));
        request.setAttribute("movieIds", sb.toString());
        request.setAttribute("startPage", new Integer(0));
        request.setAttribute("moviePagesAmount", new Integer(MovieUtil
                .pageAmount(searchMovie.length, MovieUtil
                        .getMoviePageSize(handler))));

        // if(MovieThunderUtil.isThunder(handler))
        // {
        // Add for thunder, pass date/location/theater id to screen.
        request.setAttribute("date", date.getTime() + "");
        request.setAttribute("circle", circle.getLat() + "," + circle.getLon()
                + "," + circle.getRadius());
        StringBuffer sbTheater = new StringBuffer();
        sbTheater.append(searchTheaters[0]);
        for (int i = 1; i < searchTheaters.length; i++) {
            sbTheater.append(",").append(searchTheaters[i]);
        }
        // Pass the theaterIds, it will be used in ChangePage later.
        // at ChangePage, we will get the movies theaters, to do this sevice, we
        // need
        // get the neaterby theater first, then filter those theater belong to
        // that movie
        request.setAttribute("theaterIds", sbTheater.toString());
        // }
        return mapping.findForward("success");
    }
}
