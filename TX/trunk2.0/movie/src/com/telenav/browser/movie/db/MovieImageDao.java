/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Nov 26, 2008
 * File name: ImageDao.java
 * Package name: com.telenav.j2me.browser.db
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 2:25:20 PM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.db;

import java.sql.SQLException;
import java.util.Set;

import com.telenav.browser.movie.datatype.MovieImage;

/**
 * Interface of MovieImage Dao
 * 
 * @author dysong (dysong@telenav.cn) 2:25:20 PM, Nov 26, 2008
 */
public interface MovieImageDao {

    /**
     * Truncate movie image table
     * 
     * @throws SQLException
     */
    public void truncate() throws SQLException;

    /**
     * Insert movie image data into database
     * 
     * @param movieImage
     * @return
     * @throws SQLException
     */
    public void insert(MovieImage movieImage) throws SQLException;

    /**
     * Get movie image from database with movie image key
     * 
     * @param key
     * @return
     * @throws SQLException
     */
    public MovieImage getMovieImageByKey(String key) throws SQLException;

    /**
     * Get all of movie image keys
     * 
     * @return HashSet<key>
     * @throws SQLException
     */
    public Set getAllImageKeys() throws SQLException;

}
