/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Nov 26, 2008
 * File name: MovieImageDaoImpl.java
 * Package name: com.telenav.j2me.browser.db.impl
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 2:49:20 PM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.db.impl;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.telenav.browser.movie.datatype.MovieImage;
import com.telenav.browser.movie.db.MovieImageDao;

/**
 * The class MovieImageDaoImpl takes charge of manipulating movieimage table
 * 
 * @author dysong (dysong@telenav.cn) 2:49:20 PM, Nov 26, 2008
 */
public class MovieImageDaoImpl implements MovieImageDao {
    /**
     * SqlMapClent
     */
    private SqlMapClient sqlMap;

    private static String nameSpace = "MovieImage.";

    public MovieImageDaoImpl(SqlMapClient sqlMap) {
        this.sqlMap = sqlMap;
    }

    /**
     * 
     * Get one movie image item from movie image table
     * 
     * @param key key of movie image to select
     * @return MovieImage
     * 
     * @see com.telenav.j2me.browser.db.MovieImageDao#getMovieImageByKey(java.lang.String)
     */
    public MovieImage getMovieImageByKey(String key) throws SQLException {

        if (key == null || key.length() < 1) {
            return null;
        }
        MovieImage movieImage = (MovieImage) sqlMap.queryForObject(nameSpace
                + "getMovieImage", key);
        return movieImage;
    }

    /**
     * Insert one movie image item into movie image table
     * 
     * @param movie image movie image to insert
     * @see com.telenav.j2me.browser.db.MovieImageDao#insert(com.telenav.j2me.browser.datatype.MovieImage)
     */
    public void insert(MovieImage movieImage) throws SQLException {

        sqlMap.insert(nameSpace + "insertMovieImage", movieImage);
    }

    /**
     * Truncate movie image table
     * 
     * @see com.telenav.j2me.browser.db.MovieImageDao#truncate()
     */
    public void truncate() throws SQLException {
        sqlMap.delete(nameSpace + "truncateMovieImage", null);
    }

    /**
     * Get all of movie image keys
     * 
     * @return HashSet<key>
     * @throws SQLException
     */
    public Set getAllImageKeys() throws SQLException {

        return new HashSet(sqlMap.queryForList(nameSpace + "getAllImageKeys"));
    }

}
