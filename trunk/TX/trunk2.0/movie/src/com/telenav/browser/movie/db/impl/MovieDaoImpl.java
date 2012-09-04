/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Oct 29, 2008
 * File name: MovieDaoImpl.java
 * Package name: com.telenav.j2me.browser.db.impl
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong (dysong@telenav.cn) 1:46:42 PM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.db.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.telenav.browser.movie.datatype.Movie;
import com.telenav.browser.movie.db.MovieDao;

/**
 * The class MovieDaoImpl takes charge of manipulating movie table
 * 
 * @author dysong (dysong@telenav.cn) 1:46:42 PM, Oct 29, 2008
 */
public class MovieDaoImpl implements MovieDao {
    /**
     * SqlMapClent
     */
    private SqlMapClient sqlMap;

    private static String nameSpace = "Movie.";

    public MovieDaoImpl(SqlMapClient sqlMap) {
        this.sqlMap = sqlMap;
    }

    /**
     * 
     * Insert one movie item into movie table
     * 
     * @param movie movie to insert
     * @return auto id in database
     * @see com.telenav.j2me.browser.db.MovieDao#insert(com.telenav.j2me.browser.datatype.Movie)
     */

    public long insert(Movie movie) throws SQLException {
        return ((Long) sqlMap.insert(nameSpace + "insertMovie", movie))
                .longValue();
    }

    /**
     * Truncate movie table
     * 
     * @param tableName
     * @see com.telenav.j2me.browser.db.MovieDao#truncate()
     */
    public void truncate(String talbeName) throws SQLException {
        sqlMap.delete(nameSpace + "truncateMovieTable", talbeName);
    }

    /**
     * Get one movie item from movie table
     * 
     * @param Id Id of movie to select
     * @return Movie
     */

    public Movie getMovieById(long id) throws SQLException {
        if (id <= 0) {
            return null;
        }
        return (Movie) sqlMap.queryForObject(nameSpace + "getMovie", new Long(
                id));
    }

    /**
     * Get array of movies from movie table according array of ids
     * 
     * @param movieIdArray Ids of movies to get.If movieIdArray is null , return
     *            all movies.
     * @return List<code>Movie</code>
     * @see com.telenav.j2me.browser.db.MovieDao#getMoviesByIds(long[])
     */
    public List getMoviesByIds(long[] movieIdArray) throws SQLException {
        // Array to sqlString
        String movieIdString = DaoUtil.arrayToSql(movieIdArray);
        if (movieIdString == null) {
            return null;
        }
        // Get sort sql string for where id in(?,?,?,....)
        String sortString = DaoUtil.arrayToSortSql(movieIdString);

        HashMap map = new HashMap();
        map.put("sortType", sortString);
        map.put("movieIds", movieIdString);

        return sqlMap.queryForList(nameSpace + "getMovies", map);
    }

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
     */

    public List getMoviesByIds(long[] movieIdArray, int sortType)
            throws SQLException {
        // Array to sqlString
        String movieIdString = DaoUtil.arrayToSql(movieIdArray);
        if (movieIdString == null) {
            return null;
        }
        // sortType to sqlString
        String sortString = DaoUtil.sortTypeToSql(sortType);

        HashMap map = new HashMap();
        map.put("sortType", sortString);
        map.put("movieIds", movieIdString);

        return sqlMap.queryForList(nameSpace + "getMovies", map);
    }

    /**
     * Get and check array of movie ids from movie table by sort type order.
     * 
     * @param movieIdArray Ids of movies to get.If movieIdArray is null , return
     *            null
     * @param sortType Type of sort. 2 :sort by movie rating desc;1 or other:
     *            sort by movie name asc.
     * 
     * @return List<code>Long</code>
     * @see com.telenav.j2me.browser.db.MovieDao#getMovieIdsInOrder(long[], int)
     */
    public List getMovieIdsInOrder(long[] movieIdArray, int sortType)
            throws SQLException {

        // Array to sqlString
        String movieIdString = DaoUtil.arrayToSql(movieIdArray);
        // sortType to sqlString
        String sortString = DaoUtil.sortTypeToSql(sortType);

        HashMap map = new HashMap();
        map.put("sortType", sortString);
        map.put("movieIds", movieIdString);

        return sqlMap.queryForList(nameSpace + "getMovieIdsInOrder", map);
    }

    /**
     * 
     * @param movieIdArray
     * @param sortType
     * @param movieName
     * @return
     * @throws SQLException
     */
    public List getMovieIdsByName(long[] movieIdArray, int sortType, String movieName)
    	throws SQLException {

		// Array to sqlString
		String movieIdString = DaoUtil.arrayToSql(movieIdArray);
		// sortType to sqlString
		String sortString = DaoUtil.sortTypeToSql(sortType);
		
		HashMap map = new HashMap();

		map.put("movieIds", movieIdString);
		map.put("movieName", movieName);
		map.put("sortType", sortString);
		
		return sqlMap.queryForList(nameSpace + "getMovieIdsByName", map);
	}
	
    /**
     * Create temporary table for store latest data.
     * 
     * @throws SQLException
     * 
     * @see com.telenav.browser.movie.db.MovieDao#createTemp()
     */
    public void createTemp() throws SQLException {
        drop("movie_temp");
        create("movie_temp");

    }

    /**
     * When the process of getting latest data is completed , copy temporary
     * table into primary table and backup table with date suffix name.
     * 
     * @param date
     * @throws SQLException
     * 
     * @see com.telenav.browser.movie.db.MovieDao#apply(java.lang.String)
     */
    public void apply() throws SQLException {
        deleteData(TABLE_MOVIE);
        copy();

    }

    /**
     * Rename temporary table to primary table
     * 
     * @throws SQLException
     */
    private void copy() throws SQLException {
        sqlMap.insert(nameSpace + "copyMovieTable", TABLE_MOVIE);

    }

    /**
     * Check whether the table exists
     * 
     * @param tableName
     * @return
     * @throws SQLException
     */
//    private boolean check(String tableName) throws SQLException {
//        if (tableName == null || tableName.length() < 1) {
//            return false;
//        }
//        String s = ((String) sqlMap.queryForObject(nameSpace
//                + "checkMovieTable", tableName));
//        if (!tableName.equalsIgnoreCase(s)) {
//            return false;
//        }
//        return true;
//    }

    /**
     * Copy temporary table into new table of table name with date suffix
     * 
     * @throws SQLException
     * 
     * @see com.telenav.browser.movie.db.MovieDao#backUp()
     */
//    public void backUp() throws SQLException {
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
//        String dateString = sdf.format(new Date());
//        drop("movie_" + dateString);
//        create("movie_" + dateString);
//
//        sqlMap.insert(nameSpace + "copyMovieTable", "movie_" + dateString);
//
//    }

    /**
     * Create new table with structure of primary movie table
     * 
     * @param tableName
     * @throws SQLException
     */
    private void create(String tableName) throws SQLException {
        sqlMap.update(nameSpace + "newMovieTable", tableName);
    }

    /**
     * Drop table
     * 
     * @param tableName
     * @throws SQLException
     */
    private void drop(String tableName) throws SQLException {
        sqlMap.delete(nameSpace + "dropMovieTable", tableName);
    }   
    
    
    
    private void deleteData(String tableName) throws SQLException {
        sqlMap.delete(nameSpace + "deleteMovieData", tableName);
    }

}
