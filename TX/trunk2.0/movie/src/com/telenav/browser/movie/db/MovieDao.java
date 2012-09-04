/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Oct 29, 2008
 * File name: MovieDao.java
 * Package name: com.telenav.j2me.browser.db
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 1:42:29 PM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.db;

import java.sql.SQLException;
import java.util.List;

import com.telenav.browser.movie.datatype.Movie;

/**
 * Interface of Movie Dao
 * 
 * @author lwei (lwei@telenav.cn) 1:42:29 PM, Oct 29, 2008
 */
public interface MovieDao {

    public static final String TABLE_MOVIE = "movie";
    /**
     * Get array of movies from movie table according array of ids
     * 
     * @param movieIdArray Ids of movies to get.If movieIdArray is null , return
     *            all movies.
     * 
     * 
     * @return List<code>Movie</code>
     * 
     * @throws SQLException
     */

    public List getMoviesByIds(long[] movieIdArray) throws SQLException;;

    /**
     * Truncate movie table
     * 
     * @param tableName
     * @throws SQLException
     */

    public void truncate(String tableName) throws SQLException;

    /**
     * Insert movie data into database
     * 
     * @param movie movie to insert
     * @return Id auto id in database
     * @throws SQLException
     */

    public long insert(Movie movie) throws SQLException;

    /**
     * Get movie from database with movie id
     * 
     * @param id movie id
     * @return Movie
     * 
     * 
     * @throws SQLException
     */
    public Movie getMovieById(long id) throws SQLException;

    /**
     * Get and check array of movie ids from movie table by sort type order.
     * 
     * @param movieIdArray Ids of movies to get.If movieIdArray is null , return
     *            null
     * @param sortType Type of sort. 2 :sort by movie rating desc;1 or other:
     *            sort by movie name asc.
     * 
     * @return List<code>Long</code>
     */

    public List getMovieIdsInOrder(long[] movieIdArray, int sortType)
            throws SQLException;
    public List getMovieIdsByName(long[] movieIdArray, int sortType, String movieName)
	throws SQLException;
    /**
     * Get array of movies from movie table according array of ids by sort type
     * order.
     * 
     * @param movieIdArray Ids of movies to get.If movieIdArray is null , return
     *            all movies.
     * @param sortType Type of sort. 2 :sort by movie rating desc;1 or other:
     *            sort by movie name asc.
     * 
     * @return List<code>Movie</code>
     * 
     * @throws SQLException
     */
    public List getMoviesByIds(long[] movieIdArray, int sortType)
            throws SQLException;

    /**
     * Create temporary table for store latest data.
     * 
     * @throws SQLException
     */
    public void createTemp() throws SQLException;

    /**
     * When the process of getting latest data is completed , copy temporary
     * table into primary table and backup table with date suffix name.
     * 
     * @param date
     * @throws SQLException
     */
    public void apply() throws SQLException;

    /**
     * Copy temporary table into new table of table name with date suffix
     * 
     * @throws SQLException
     */
    //public void backUp() throws SQLException;

}
