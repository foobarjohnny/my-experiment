/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Oct 21, 2008
 * File name: MoviesManager.java
 * Package name: com.telenav.j2me.browser.service
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 10:37:36 AM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.telenav.browser.movie.datatype.GeoCircle;
import com.telenav.browser.movie.datatype.MovieImage;
import com.telenav.browser.movie.datatype.Schedule;
import com.telenav.browser.movie.exception.MovieException;

/**
 * The movie manager is the interface to get the movies and theaters
 * information.
 * 
 * @author lwei (lwei@telenav.cn) 10:37:36 AM, Oct 21, 2008
 */
public interface MoviesManager {

    /** Sort types. */
    public static final int SORT_BY_NAME = 1;

    public static final int SORT_BY_DEFAULT = SORT_BY_NAME;

    public static final int SORT_BY_RATING = 2;
    
    public static final int SORT_BY_NEWOPENING = 3;

    /**
     * Search the movies displayed in the theaters.
     * 
     * @param theaterIdArray The theater id array.
     * @param date The movie date.
     * @param sortType The sort type.
     * @return Movie id array.
     */
    public long[] searchMovieIds(long[] theaterIdArray, Date date, int sortType)
            throws MovieException;

    /**
     * Get the movies according the the id array.
     * 
     * @param movieIdArray The id array.
     * @return the <code>Movie</code> List.
     * @throws MovieException
     */
    public List getMoviesByIds(long[] movieIdArray) throws MovieException;

    /**
     * Get the theaters according the the id array.
     * 
     * @param theaterIdArray The id array.
     * @return the <code>Theater</code> List.
     * @throws MovieException
     */
    public List getTheatersByIds(long[] theaterIdArray) throws MovieException;

    /**
     * Search for the theaters around a certain location.
     * 
     * @param geoCircle The search area.
     * @param date The date to search.
     * @return The theater id match.
     * @throws MovieException
     */
    public long[] searchTheaterIds(GeoCircle geoCircle, Date date)
            throws MovieException;

    /**
     * Search for the schedule of a movie in a certain theater.
     * 
     * @param movieId The movie Id.
     * @param theaterId The theater id.
     * @param date The date.
     * @return The <code>Schedule</code> List.
     * @throws MovieException
     */
    public List searchSchedule(long movieId, long theaterId, Date date)
            throws MovieException;

    /**
     * Search for the schedule of a certain movie in the theaters.
     * 
     * @param movieId The movie id.
     * @param theaterIdArray The theaters to search.
     * @param date The date to search.
     * @return The map of schedule. The key is the <code>Theater</code> and
     *         value is the <code>Schedule </code>List.
     * @throws MovieException
     */
    public Map searchScheduleByMovie(long movieId, long[] theaterIdArray,
            Date date) throws MovieException;

    /**
     * Search for schedule in the theater.
     * 
     * @param theaterId The schedule id.
     * @param date The date.
     * @param sortType The sort type.
     * @return The map of schedule. The key is the <code>Movie</code> and value
     *         is the <code>Schedule </code>List.
     * @throws MovieException
     */
    public Map searchScheduleByTheater(long theaterId, Date date, int sortType)
            throws MovieException;

    /**
     * Get movie image by image key
     * 
     * @param key movie image key
     * @param height Screen height.
     * @param width Screen width.
     * @return MovieImage movie image selected
     * @throws MovieException
     */
    public MovieImage getMovieImage(String key, String width, String height)
            throws MovieException;
    
    public long[] getMoviesIdsByName(long[] movieIdArray,int sortType, String movieName) throws MovieException;
    public Schedule getScheduleById(long id) throws MovieException;
    public List searchScheduleByMovie(long[] movieIdArray, long[] theaterIdArray,
            Date date) throws MovieException;
}
