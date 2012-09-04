/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Oct 29, 2008
 * File name: Theater.java
 * Package name: com.telenav.j2me.browser.db
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 1:43:21 PM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.db;

import java.sql.SQLException;
import java.util.List;

import com.telenav.browser.movie.datatype.GeoCircle;
import com.telenav.browser.movie.datatype.Theater;

/**
 * Interface of Theater Dao
 * 
 * @author lwei (lwei@telenav.cn) 1:43:21 PM, Oct 29, 2008
 */

public interface TheaterDao {

    public static final String TABLE_THEATER = "theater";
    /**
     * Truncate theater table
     * 
     * @param tableName
     * @throws SQLException
     */
    public void truncate(String tableName) throws SQLException;

    /**
     * Insert one theater item into theater table
     * 
     * @return auto id in database
     * 
     */

    public long insert(Theater theater) throws SQLException;

    /**
     * /** Get one theater from theater table
     * 
     * @param theaterId Id of theater to get
     * @return Theater
     * 
     * 
     * @throws SQLException
     */
    public Theater getTheaterById(long theaterId) throws SQLException;

    /**
     * Get array of theaters from theater table according array of ids
     * 
     * @param theaterIdArray Ids of theaters to get
     * @return List<code>Theater</code>
     * 
     * @throws SQLException
     */
    public List getTheatersByIds(long[] theaterIdArray) throws SQLException;

    /**
     * Search theater by GeoCircle(lat,lon,radius),return theaters which is in
     * the radius of selected point(lat,lon).
     * 
     * @param circle Circle which the theaters are in.
     * @return List<code>Theater</code>
     */

    public List searchTheatersInCircle(GeoCircle circle) throws SQLException;

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
     * @throws SQLException
     */
    public void apply() throws SQLException;

    /**
     * Copy temporary table into new table of table name with date suffix
     * 
     * @param date
     * 
     * @throws SQLException
     */
    //public void backUp() throws SQLException;
}
