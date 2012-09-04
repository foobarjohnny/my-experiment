/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Oct 29, 2008
 * File name: ScheduleDaoImpl.java
 * Package name: com.telenav.j2me.browser.db.impl
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 3:26:17 PM, Oct 29, 2008
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.db.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.telenav.browser.movie.datatype.Schedule;
import com.telenav.browser.movie.db.ScheduleDao;

/**
 * 
 * The class ScheduleDaoImpl takes charge of manipulating schedule table
 * 
 * @author dysong (dysong@telenav.cn) 3:26:17 PM
 */
public class ScheduleDaoImpl implements ScheduleDao {
    /**
     * SqlMapClient
     */
    private SqlMapClient sqlMap;

    private static String nameSpace = "Schedule.";

    public ScheduleDaoImpl(SqlMapClient sqlMap) {
        this.sqlMap = sqlMap;
    }

    /**
     * Insert one schedule item into schedule table
     * 
     * @return auto id in database
     * 
     * @see com.telenav.j2me.browser.db.ScheduleDao#insert(com.telenav.j2me.browser.datatype.Schedule)
     */
    public long insert(Schedule schedule) throws SQLException {

        if (schedule == null) {
            return -1;
        }
        return ((Long) sqlMap.insert(nameSpace + "insertSchedule", schedule))
                .longValue();
    }

    /**
     * Truncate schedule table
     * 
     * @param tableName
     * 
     * @see com.telenav.j2me.browser.db.ScheduleDao#truncate()
     */
    public void truncate(String tableName) throws SQLException {
        sqlMap.delete(nameSpace + "truncateScheduleTable", tableName);

    }

    /**
     * Search raw schedule list by movie id, theater id in the order by sort
     * type. It can display movie in different ways such as: IMAX, THX, OPEN
     * CAPTION, so there are different show tables(or schedules).
     * 
     * @param movieId
     * @param theaterId
     * @param date
     * @return List<code>Schedule</code>
     * @throws SQLException
     * @see com.telenav.j2me.browser.db.ScheduleDao#searchScheduleList(long,
     *      long, java.util.Date)
     */
    public List searchScheduleList(long movieId, long theaterId, Date date)
            throws SQLException {
        if (movieId <= 0 && theaterId <= 0 && date == null) {
            return null;
        }
        // Date to sqlString
        String dateString = DaoUtil.dateToSql(date);
        // theaterId to sqlStirng
        String theaterIdString = null;
        if (theaterId > 0) {
            theaterIdString = "" + theaterId;
        }
        // movieId to sqlStirng
        String movieIdString = null;
        if (movieId > 0) {
            movieIdString = "" + movieId;
        }

        HashMap map = new HashMap();
        map.put("date", dateString);
        map.put("theaterId", theaterIdString);
        map.put("movieId", movieIdString);

        return sqlMap.queryForList(nameSpace + "searchSchedule", map);
    }

    /**
     * Get one raw schedule item from schedule table. *
     * 
     * @param id Id of raw schedule to select
     * @return List<code>Schedule</code>
     * @see com.telenav.j2me.browser.db.ScheduleDao#getScheduleById(long)
     */
    public Schedule getScheduleById(long id) throws SQLException {
        if (id <= 0) {
            return null;
        }
        return (Schedule) sqlMap.queryForObject(nameSpace + "getSchedule",
                new Long(id));

    }

    /**
     * Search schedules by theater id and date ,and both can't be null.
     * 
     * @param theaterId Theater id of schedules to search
     * @param date Date of schedules
     * @return List<code>Schedule</code>
     * @see com.telenav.j2me.browser.db.ScheduleDao#searchScheduleByTheater(long[],
     *      java.util.Date)
     */
    public List searchScheduleByTheater(long theaterId, Date date)
            throws SQLException {
        if (theaterId <= 0 || date == null) {
            return null;
        }
        // Date to sqlString
        String dateString = DaoUtil.dateToSql(date);
        // Array to sqlString
        String theaterIdString = "" + theaterId;

        HashMap map = new HashMap();
        map.put("date", dateString);
        map.put("theaterId", theaterIdString);

        return sqlMap.queryForList(nameSpace + "searchScheduleByTheater", map);
    }

    /**
     * 
     * Get ids of movies from schedule table according array of theater ids by
     * sort type order.
     * 
     * @param theaterIdArray
     * @param date
     * @return List<code>Long</code>
     * @throws SQLException
     * @see com.telenav.j2me.browser.db.ScheduleDao#searchMovie(long[],
     *      java.util.Date, int)
     */
    public List searchMovieIds(long[] theaterIdArray, Date date)
            throws SQLException {
        if ((theaterIdArray == null || theaterIdArray.length < 1)
                || date == null) {
            return null;
        }
        // Date to sqlString
        String dateString = DaoUtil.dateToSql(date);
        // Array to sqlString
        String theaterIdString = DaoUtil.arrayToSql(theaterIdArray);

        HashMap map = new HashMap();
        map.put("date", dateString);
        map.put("theaterId", theaterIdString);

        List movieIdList = sqlMap.queryForList(nameSpace + "searchMovieIds",
                map);

        return movieIdList;
    }

    /**
     * 
     * Get and check ids of theaters from schedule table according date.
     * 
     * @param theaterIdArray check theaters exist in schedule table
     * @param date
     * @return List<code>Long</code>
     * @throws SQLException
     * @see com.telenav.j2me.browser.db.ScheduleDao#searchTheaterIds(long[],
     *      java.util.Date)
     */
    public List searchTheaterIds(long[] theaterIdArray, Date date)
            throws SQLException {
        if (date == null || theaterIdArray == null || theaterIdArray.length < 1) {
            return null;
        }
        // Date to sqlString
        String dateString = DaoUtil.dateToSql(date);
        // Array to sqlString
        String theaterIdString = DaoUtil.arrayToSql(theaterIdArray);

        HashMap map = new HashMap();
        map.put("date", dateString);
        map.put("theaterId", theaterIdString);

        List theaterIdList = sqlMap.queryForList(
                nameSpace + "searchTheaterIds", map);

        return theaterIdList;
    }

    /**
     * Search raw schedules list by movie id, theater id array and date.
     * 
     * @param movieId
     * @param theaterIdArray
     * @param date
     * @return List<code>Schedule</code>
     * @throws SQLException
     * 
     * @see com.telenav.j2me.browser.db.ScheduleDao#searchSchedulesList(long,
     *      long[], java.util.Date)
     */
    public List searchSchedulesList(long movieId, long[] theaterIdArray,
            Date date) throws SQLException {

        if (movieId <= 0
                || (theaterIdArray == null || theaterIdArray.length < 1)
                || date == null) {
            return null;
        }
        // Date to sqlString
        String dateString = DaoUtil.dateToSql(date);
        // theaterIdArray to sqlString
        String theaterIdString = DaoUtil.arrayToSql(theaterIdArray);
        // movie to sqlString
        String movieIdString = "" + movieId;

        HashMap map = new HashMap();
        map.put("date", dateString);
        map.put("theaterId", theaterIdString);
        map.put("movieId", movieIdString);
        // Get schedules
        List rawSchedules = sqlMap.queryForList(nameSpace + "searchSchedules",
                map);

        return rawSchedules;
    }

    /**
     * Search raw schedules list by movie id, theater id array and date.
     * 
     * @param movieId
     * @param theaterIdArray
     * @param date
     * @return List<code>Schedule</code>
     * @throws SQLException
     * 
     * @see com.telenav.j2me.browser.db.ScheduleDao#searchSchedulesList(long,
     *      long[], java.util.Date)
     */
    public List searchSchedulesList(long[] movieIdArray, long[] theaterIdArray,
            Date date) throws SQLException {

        if (movieIdArray == null || movieIdArray.length < 1
                || (theaterIdArray == null || theaterIdArray.length < 1)
                || date == null) {
            return null;
        }
        // Date to sqlString
        String dateString = DaoUtil.dateToSql(date);
        // theaterIdArray to sqlString
        String theaterIdString = DaoUtil.arrayToSql(theaterIdArray);
        // movie to sqlString
        String movieIdString = DaoUtil.arrayToSql(movieIdArray);

        HashMap map = new HashMap();
        map.put("date", dateString);
        map.put("theaterId", theaterIdString);
        map.put("movieId", movieIdString);
        // Get schedules
        List rawSchedules = sqlMap.queryForList(nameSpace + "searchSchedules",
                map);

        return rawSchedules;
    }
    
    /**
     * Create temporary table for store latest data.
     * 
     * @throws SQLException
     * 
     * @see com.telenav.browser.movie.db.ScheduleDao#createTemp()
     */
    public void createTemp() throws SQLException {
        drop("schedule_temp");
        create("schedule_temp");

    }

    /**
     * When the process of getting latest data is completed , copy temporary
     * table into primary table and backup table with date suffix name.
     * 
     * @throws SQLException
     * 
     * @see com.telenav.browser.movie.db.ScheduleDao#apply()
     */
    public void apply() throws SQLException {
        deleteData(TABLE_SCHEDULE);
        copy();

    }

    /**
     * Rename temporary table to primary table
     * 
     * @throws SQLException
     */
    private void copy() throws SQLException {
        sqlMap.insert(nameSpace + "copyScheduleTable", TABLE_SCHEDULE);
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
//                + "checkScheduleTable", tableName));
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
     * @see com.telenav.browser.movie.db.ScheduleDao#backUp()
     */
//    public void backUp() throws SQLException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
//        String dateString = sdf.format(new Date());
//        drop("schedule_" + dateString);
//        create("schedule_" + dateString);
//
//        sqlMap
//                .insert(nameSpace + "copyScheduleTable", "schedule_"
//                        + dateString);
//
//    }

    /**
     * Create new table with structure of primary movie table
     * 
     * @param tableName
     * @throws SQLException
     */
    private void create(String tableName) throws SQLException {
        sqlMap.update(nameSpace + "newScheduleTable", tableName);
    }

    /**
     * Drop table
     * 
     * @param tableName
     * @throws SQLException
     */
    private void drop(String tableName) throws SQLException {
        sqlMap.delete(nameSpace + "dropScheduleTable", tableName);
    }

    private void deleteData(String tableName) throws SQLException {
        sqlMap.delete(nameSpace + "deleteScheduleData", tableName);
    }
    
}
