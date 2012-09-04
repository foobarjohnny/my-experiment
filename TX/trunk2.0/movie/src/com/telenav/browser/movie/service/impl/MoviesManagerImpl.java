/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Oct 30, 2008
 * File name: MoviesManagerImpl.java
 * Package name: com.telenav.j2me.browser.service
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 9:28:44 AM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.telenav.browser.movie.datatype.GeoCircle;
import com.telenav.browser.movie.datatype.Movie;
import com.telenav.browser.movie.datatype.MovieImage;
import com.telenav.browser.movie.datatype.Schedule;
import com.telenav.browser.movie.datatype.Theater;
import com.telenav.browser.movie.db.DaoManager;
import com.telenav.browser.movie.db.MovieDao;
import com.telenav.browser.movie.db.MovieImageDao;
import com.telenav.browser.movie.db.ScheduleDao;
import com.telenav.browser.movie.db.TheaterDao;
import com.telenav.browser.movie.db.impl.DaoUtil;
import com.telenav.browser.movie.exception.MovieException;
import com.telenav.browser.movie.service.MoviesManager;

/**
 * The MoviesManagerImpl provides services to get the movies , theaters and
 * schedules information.
 * 
 * @author dysong (dysong@telenav.cn) 9:28:44 AM, Oct 30, 2008
 */
public class MoviesManagerImpl implements MoviesManager {
    private static Logger log = Logger.getLogger(MoviesManagerImpl.class);

    private static MovieDao movieDaoImpl = DaoManager.getMovieDao();

    private static TheaterDao theaterDaoImpl = DaoManager.getTheaterDao();

    private static ScheduleDao scheduleDaoImpl = DaoManager.getScheduleDao();

    private static MovieImageDao movieImageDaoImpl = DaoManager
            .getMovieImageDao();

    /**
     * Get the movies according the the id array.
     * 
     * @param movieIdArray The id array.
     * @return the <code>Movie</code> List.
     * @throws MovieException
     * @see com.telenav.j2me.browser.service.MoviesManager#getMoviesByIds(long[])
     */

    public List getMoviesByIds(long[] movieIdArray) throws MovieException {
        // CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        // cli.setFunctionName("getMovies");
        // cli.addData(CliConstants.LABEL_CLIENT_INTO,"min=" + account +
        // "&pageNum=" + pageNumber + "&pageSize=" + pageSize);
        try {
            if (movieIdArray == null || movieIdArray.length < 1) {
                return new ArrayList();
            }

            return movieDaoImpl.getMoviesByIds(movieIdArray);
        } catch (SQLException e) {
            log.error("Sql error", e);
            throw new MovieException("Get Movies failed");
        }
        // finally{
        // cli.complete();
        // }
    }

    public long[] getMoviesIdsByName(long[] movieIdArray,int sortType, String movieName) throws MovieException {
        // CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        // cli.setFunctionName("getMovies");
        // cli.addData(CliConstants.LABEL_CLIENT_INTO,"min=" + account +
        // "&pageNum=" + pageNumber + "&pageSize=" + pageSize);
        try {
            if (movieIdArray == null || movieIdArray.length < 1) {
                return new long[0];
            }

            List idList =  movieDaoImpl.getMovieIdsByName(movieIdArray,sortType,movieName);
            
            // List to long[]
            long[] movieIds = new long[idList.size()];
            for (int i = 0; i < idList.size(); i++) {
            	movieIds[i] = ((Long) idList.get(i)).longValue();
            }
            return movieIds;
        } catch (SQLException e) {
            log.error("Sql error", e);
            e.printStackTrace();
            throw new MovieException("Get Movies failed");
        }
        // finally{
        // cli.complete();
        // }
    }
    /**
     * Get the theaters according the the id array.
     * 
     * @param theaterIdArray The id array.
     * @return the <code>Theater</code> List.
     * @throws MovieException
     * @see com.telenav.j2me.browser.service.MoviesManager#getTheatersByIds(long[])
     */
    public List getTheatersByIds(long[] theaterIdArray) throws MovieException {
        try {
            if (theaterIdArray == null || theaterIdArray.length < 1) {
                return new ArrayList();
            }
            return theaterDaoImpl.getTheatersByIds(theaterIdArray);
        } catch (SQLException e) {
            log.error("Sql error", e);
            throw new MovieException("Get Theaters failed");
        }
    }

    /**
     * Search the movies displayed in the theaters.
     * 
     * @param theaterIdArray The theater id array.
     * @param date The movie date.
     * @param sortType The sort type.
     * @return Movie id array.
     * @throws MovieException
     * 
     * @see com.telenav.j2me.browser.service.MoviesManager#searchMovie(long[],
     *      java.util.Date, int)
     */
    public long[] searchMovieIds(long[] theaterIdArray, Date date, int sortType)
            throws MovieException {

        try {
            if ((theaterIdArray == null || theaterIdArray.length < 1)
                    && date == null) {
                return new long[0];
            }

            // Get movies being displayed.
            List movieIdList = scheduleDaoImpl.searchMovieIds(theaterIdArray,
                    date);
            if (movieIdList == null || movieIdList.size() < 1) {
                return new long[0];
            }

            // List to long[]
            long[] movieIdArray = new long[movieIdList.size()];
            for (int i = 0; i < movieIdList.size(); i++) {
                movieIdArray[i] = ((Long) movieIdList.get(i)).longValue();
            }

            // Sort movie ids
            movieIdList = movieDaoImpl.getMovieIdsInOrder(movieIdArray,
                    sortType);

            // List to long[]
            if (movieIdList == null || movieIdList.size() < 1) {
                return new long[0];
            }
            movieIdArray = new long[movieIdList.size()];
            for (int i = 0; i < movieIdList.size(); i++) {
                movieIdArray[i] = ((Long) movieIdList.get(i)).longValue();
            }

            return movieIdArray;
        } catch (SQLException e) {
            log.error("Sql error", e);
            throw new MovieException("search Movie failed");
        }

    }

    /**
     * Search for the theaters around a certain location.
     * 
     * @param geoCircle The search area.
     * @param date The date to search.
     * @return The theater id match.
     * @throws MovieException
     * @see com.telenav.j2me.browser.service.MoviesManager#searchTheaters(com.telenav.j2me.browser.datatype.GeoCircle,
     *      java.util.Date)
     */
    public long[] searchTheaterIds(GeoCircle geoCircle, Date date)
            throws MovieException {
        try {
            if (geoCircle == null && date == null) {
                return new long[0];
            }
            // Get theaters
            List theaters = theaterDaoImpl.searchTheatersInCircle(geoCircle);

            long[] theaterIdArray = null;
            if (theaters != null) {
                theaterIdArray = new long[theaters.size()];
                for (int i = 0; i < theaters.size(); i++) {
                    theaterIdArray[i] = ((Theater) theaters.get(i)).getId();
                }
            }

            // Get theaterIdList in these theaters and date
            List theaterIdList = scheduleDaoImpl.searchTheaterIds(
                    theaterIdArray, date);

            // List to Long[]
            if (theaterIdList == null || theaterIdList.size() < 1) {
                return new long[0];
            } else {

                theaterIdArray = new long[theaterIdList.size()];
                for (int i = 0; i < theaterIdList.size(); i++) {
                    theaterIdArray[i] = ((Long) theaterIdList.get(i))
                            .longValue();
                }
            }

            return theaterIdArray;
        } catch (SQLException e) {
            log.error("Sql error", e);
            throw new MovieException("search Theater failed");
        }

    }

    /**
     * Search for the schedule of a movie in a certain theater.
     * 
     * @param movieId The movie Id.
     * @param theaterId The theater id.
     * @param date The date.
     * @return The <code>Schedule</code> List.
     * @throws MovieException
     * @see com.telenav.j2me.browser.service.MoviesManager#searchScheduleList(long,
     *      long, java.util.Date)
     */
    public List searchSchedule(long movieId, long theaterId, Date date)
            throws MovieException {
        try {
            if (movieId <= 0 || theaterId <= 0) {
                return new ArrayList();
            }

            // Load data.
            List schedules = scheduleDaoImpl.searchScheduleList(movieId,
                    theaterId, date);
            Movie movie = movieDaoImpl.getMovieById(movieId);
            Theater theater = theaterDaoImpl.getTheaterById(theaterId);

            // All null ,return null
            if (schedules != null && schedules.size() > 0 && movie != null
                    && theater != null) {
                for (Iterator iterator = schedules.iterator(); iterator
                        .hasNext();) {
                    Schedule schedule = (Schedule) iterator.next();
                    schedule.setMovie(movie);
                    schedule.setTheater(theater);
                }
                return DaoUtil.divideRawSchedule(schedules);
            } else {
                return new ArrayList();
            }
        } catch (SQLException e) {
            log.error("Sql error", e);
            throw new MovieException("search Schedule failed");
        }

    }

    /**
     * Search for schedule in the theater. First get ids of movies in certain
     * theater and certain date,then get detail schedules about the movies.
     * 
     * @param theaterId The schedule id.
     * @param date The date.
     * @param sortType The sort type.
     * @return The map of schedule. The key is the <code>Movie</code> and value
     *         is the <code>Schedule </code>List.
     * @throws MovieException
     * @see com.telenav.j2me.browser.service.MoviesManager#searchScheduleByTheater(long,
     *      java.util.Date, int)
     */
    public Map searchScheduleByTheater(long theaterId, Date date, int sortType)
            throws MovieException {
        try {
            if (theaterId <= 0 || date == null) {
                return new HashMap();
            }

            // Get the theater
            List theaters = getTheatersByIds(new long[] { theaterId });
            Theater theater;
            if (theaters == null || theaters.isEmpty()) {
                return new HashMap();
            } else {
                theater = (Theater) theaters.get(0);
            }

            // Get schedule List
            List scheduleList = scheduleDaoImpl.searchScheduleByTheater(
                    theaterId, date);
            if (scheduleList == null || scheduleList.isEmpty()) {
                return new HashMap();
            }

            // Get the movie list.
            long[] movieIdArray = new long[scheduleList.size()];
            for (int i = 0; i < movieIdArray.length; i++) {
                movieIdArray[i] = ((Schedule) scheduleList.get(i)).getMovieId();
            }
            List movies = movieDaoImpl.getMoviesByIds(movieIdArray, sortType);
            if (movies == null || movies.size() < 1) {
                return new HashMap();
            }

            // Create the result map.
            LinkedHashMap result = new LinkedHashMap();
            for (Iterator iterator = movies.iterator(); iterator.hasNext();) {
                Movie movie = (Movie) iterator.next();
                result.put(movie, new ArrayList());
            }

            // Organize the schedule.
            Map map = new HashMap();
            ArrayList schedules;
            Long movieIdLong;
            for (Iterator iterator = scheduleList.iterator(); iterator
                    .hasNext();) {
                Schedule schedule = (Schedule) iterator.next();
                movieIdLong = new Long(schedule.getMovieId());
                if (map.containsKey(movieIdLong)) {
                    schedules = (ArrayList) map.get(movieIdLong);
                    schedules.add(schedule);
                } else {
                    schedules = new ArrayList();
                    schedules.add(schedule);
                    map.put(movieIdLong, schedules);
                }
            }

            Set keySet = result.keySet();
            Set noSchedule = new HashSet();
            for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
                Movie movie = (Movie) iterator.next();
                List resultList = (List) result.get(movie);
                schedules = (ArrayList) map.get(new Long(movie.getId()));
                if (schedules == null || schedules.size() < 1) {
                    // No schedule for this movie in this theater.
                    noSchedule.add(movie);
                    continue;
                }
                // Set movie and theater
                for (Iterator it = schedules.iterator(); it.hasNext();) {
                    Schedule schedule = (Schedule) it.next();
                    schedule.setMovie(movie);
                    schedule.setTheater(theater);
                }
                // divide raw schedules into detail schedules with splitting
                // show time
                resultList.addAll(DaoUtil.divideRawSchedule(schedules));

            }
            // Remove the no schedules.
            for (Iterator iterator = noSchedule.iterator(); iterator.hasNext();) {
                Movie m = (Movie) iterator.next();
                result.remove(m);
            }

            return result;
        } catch (SQLException e) {
            log.error("Sql error", e);
            throw new MovieException("search Schedule By Theater failed");
        }

    }

    /**
     * Search for the schedule of a certain movie in the theaters.
     * 
     * @param movieId The movie id.
     * @param theaterIdArray The theaters to search.
     * @param date The date to search.
     * @return The map of schedule. The key is the <code>Theater</code> and
     *         value is the <code>Schedule </code>List.
     * @throws MovieException
     * @see com.telenav.j2me.browser.service.MoviesManager#searchScheduleByMovie(long,
     *      long[], java.util.Date)
     */
    public Map searchScheduleByMovie(long movieId, long[] theaterIdArray,
            Date date) throws MovieException {
        try {
            if (movieId <= 0 || theaterIdArray == null
                    || theaterIdArray.length == 0 || date == null) {
                return new HashMap();
            }

            // Get movies.
            List movies = getMoviesByIds(new long[] { movieId });
            Movie movie;
            if (movies == null || movies.isEmpty()) {
                return new HashMap();
            } else {
                movie = (Movie) movies.get(0);
            }

            // Get theaters
            List theaters = theaterDaoImpl.getTheatersByIds(theaterIdArray);
            if (theaters == null || theaters.size() < 1) {
                return new HashMap();
            }

            // Get raw schedules (not split show time)
            List scheduleList = scheduleDaoImpl.searchSchedulesList(movieId,
                    theaterIdArray, date);
            // Schedule table has no schedules ,return null
            if (scheduleList == null || scheduleList.size() < 1) {
                return new HashMap();
            }

            // Create result map.
            Map result = new LinkedHashMap();
            for (int i = 0; theaters != null && i < theaters.size(); i++) {
                Theater theater = (Theater) theaters.get(i);
                result.put(theater, new ArrayList());
            }

            // Organize the schedule.
            HashMap map = new HashMap();
            ArrayList schedules;
            Long theaterIdLong;

            for (Iterator iterator = scheduleList.iterator(); iterator
                    .hasNext();) {
                Schedule schedule = (Schedule) iterator.next();
                theaterIdLong = new Long(schedule.getTheaterId());
                if (map.containsKey(theaterIdLong)) {
                    schedules = (ArrayList) map.get(theaterIdLong);
                    schedules.add(schedule);
                } else {
                    schedules = new ArrayList();
                    schedules.add(schedule);
                    map.put(theaterIdLong, schedules);
                }

            }

            Set keySet = result.keySet();
            Set noSchedule = new HashSet();
            for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {

                Theater theater = (Theater) iterator.next();
                List resultList = (List) result.get(theater);
                schedules = (ArrayList) map.get(new Long(theater.getId()));

                if (schedules == null || schedules.size() < 1) {
                    // No schedule for this movie in this theater.
                    noSchedule.add(theater);
                    continue;
                }
                // Set movie and theater
                for (Iterator it = schedules.iterator(); it.hasNext();) {
                    Schedule schedule = (Schedule) it.next();
                    schedule.setMovie(movie);
                    schedule.setTheater(theater);
                }
                // divide raw schedules into detail schedules with splitting
                // show time
                resultList.addAll(DaoUtil.divideRawSchedule(schedules));

            }

            // Remove the no schedules.
            for (Iterator iterator = noSchedule.iterator(); iterator.hasNext();) {
                Theater t = (Theater) iterator.next();
                result.remove(t);
            }

            return result;
        } catch (SQLException e) {
            log.error("Sql error", e);
            throw new MovieException("search Schedule By Movie failed");
        }

    }

    
    /**
     * 
     * @param movieIdArray
     * @param theaterIdArray
     * @param date
     * @return
     * @throws MovieException
     */
    public List searchScheduleByMovie(long[] movieIdArray, long[] theaterIdArray,
            Date date) throws MovieException {
        try {
            if (movieIdArray == null
                    || movieIdArray.length == 0 || theaterIdArray == null
                    || theaterIdArray.length == 0 || date == null) {
                return new ArrayList();
            }

 

            // Get raw schedules (not split show time)
            List scheduleList = scheduleDaoImpl.searchSchedulesList(movieIdArray,
                    theaterIdArray, date);
            
            return scheduleList;
        } catch (SQLException e) {
            log.error("Sql error", e);
            throw new MovieException("search Schedule By Movies failed");
        }

    }
    
    /**
     * Get movie image by image key
     * 
     * @param key movie image key
     * @return MovieImage movie image selected
     * @throws MovieException
     * 
     * @see com.telenav.j2me.browser.service.MoviesManager#getMovieImageByKey(java.lang.String)
     */
    public MovieImage getMovieImage(String key, String width, String height)
            throws MovieException {
        MovieImage movieImage = null;
        try {
            movieImage = movieImageDaoImpl.getMovieImageByKey(width + "x"
                    + height + "_" + key);
            if (movieImage == null || movieImage.getData() == null
                    || movieImage.getData().length < 1) {
                log.debug("Get movie image null");
                return null;
            }
            log.debug("Get movie image success");
            return movieImage;
        } catch (SQLException e) {
            log.error("Sql error", e);
            throw new MovieException(
                    "search MovieImage By Movie Image key failed");
        }

    }
    
    public Schedule getScheduleById(long id) throws MovieException 
    {
    	Schedule s;
    	try {
			s = scheduleDaoImpl.getScheduleById(id);
		} catch (SQLException e) {
			log.error("Sql error", e);
            throw new MovieException(
                    "getScheduleById failed");
		}
		
		return s;
    }
}
