/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Oct 29, 2008
 * File name: ScheduleDao.java
 * Package name: com.telenav.j2me.browser.db
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 1:41:22 PM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.db;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.telenav.browser.movie.datatype.Schedule;

/**
 * Interface of Schedule Dao
 * 
 * @author lwei (lwei@telenav.cn) 1:41:22 PM, Oct 29, 2008
 */
public interface ScheduleDao {

    public static final String TABLE_SCHEDULE = "schedule";
    /**
     * Search schedules list by movie id , theater id array and date.
     * 
     * @param movieId
     * @param theaterIdArray
     * @param date
     * @return List<code>Schedule</code>
     * @throws SQLException
     */
    public List searchSchedulesList(long movieId, long[] theaterIdArray,
            Date date) throws SQLException;

    /**
     * Search schedules list by movie id , theater id array and date.
     * 
     * @param movieId
     * @param theaterIdArray
     * @param date
     * @return List<code>Schedule</code>
     * @throws SQLException
     */
    public List searchSchedulesList(long movieIdArray[], long[] theaterIdArray,
            Date date) throws SQLException;
    
    /**
     * Search schedules by theater id and date ,and both can't be null.
     * 
     * @param theaterId Theater id of schedules to search
     * @param date Date of schedules
     * @return List<code>Schedule</code>
     */

    public List searchScheduleByTheater(long theaterId, Date date)
            throws SQLException;

    /**
     * Search schedule list by movie id, theater id in the order by sort type.
     * It can display movie in different ways such as: IMAX, THX, OPEN CAPTION,
     * so there are different show tables(or schedules).
     * 
     * @param movieId
     * @param theaterId
     * @param date
     * @return List<code>Schedule</code>
     * @throws SQLException
     */
    public List searchScheduleList(long movieId, long theaterId, Date date)
            throws SQLException;

    /**
     * 
     * Get and check ids of theaters from schedule table according date.
     * 
     * @param theaterIdArray check theaters exist in schedule table
     * @param date
     * @return List<code>Long</code>
     * @throws SQLException
     * 
     */

    public List searchTheaterIds(long[] theaterIdArray, Date date)
            throws SQLException;

    /**
     * 
     * Get ids of movies from schedule table according array of theater ids by
     * sort type order.
     * 
     * @param theaterIdArray
     * @param date
     * @return List<code>Long</code>
     * @throws SQLException
     */
    public List searchMovieIds(long[] theaterIdArray, Date date)
            throws SQLException;

    /**
     * Get one raw schedule item from schedule table. *
     * 
     * @param id Id of raw schedule to select
     * @return List<code>Schedule</code>
     */

    public Schedule getScheduleById(long id) throws SQLException;

    /**
     * Truncate schedule table
     * 
     * @param tableName
     * @throws SQLException
     */
    public void truncate(String tableName) throws SQLException;

    /**
     * Insert one schedule item into schedule table
     * 
     * @return auto id in database
     * 
     * @throws SQLException
     */
    public long insert(Schedule schedule) throws SQLException;

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
     * @throws SQLException
     */
    //public void backUp() throws SQLException;
}
