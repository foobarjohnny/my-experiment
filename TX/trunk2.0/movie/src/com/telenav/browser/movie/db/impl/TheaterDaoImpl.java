/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Oct 29, 2008
 * File name: TheaterDaoImpl.java
 * Package name: com.telenav.j2me.browser.db.impl
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 3:09:51 PM, Oct 29, 2008
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.db.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.telenav.browser.movie.datatype.BoundingBox;
import com.telenav.browser.movie.datatype.GeoCircle;
import com.telenav.browser.movie.datatype.Theater;
import com.telenav.browser.movie.db.TheaterDao;

/**
 * @author dysong (dysong@telenav.cn) 3:09:51 PM
 */
public class TheaterDaoImpl implements TheaterDao {

    /**
     * SqlMapClient
     */
    private SqlMapClient sqlMap;

    private static String nameSpace = "Theater.";

    public TheaterDaoImpl(SqlMapClient sqlMap) {
        this.sqlMap = sqlMap;
    }

    /**
     * Insert one theater item into theater table
     * 
     * @return auto id in database
     * 
     * @see com.telenav.j2me.browser.db.TheaterDao#insert(com.telenav.j2me.browser.db.TheaterDao)
     */
    public long insert(Theater theater) throws SQLException {
        return ((Long) sqlMap.insert(nameSpace + "insertTheater", theater))
                .longValue();
    }

    /**
     * Truncate theater table
     * 
     * @param tableName
     * 
     * @see com.telenav.j2me.browser.db.TheaterDao#truncate()
     */
    public void truncate(String tableName) throws SQLException {
        sqlMap.delete(nameSpace + "truncateTheaterTable", tableName);
    }

    /**
     * Get one theater from theater table
     * 
     * @param id Id of theater to get
     * @return Theater
     */

    public Theater getTheaterById(long id) throws SQLException {
        if (id <= 0) {
            return null;
        }

        return (Theater) sqlMap.queryForObject(nameSpace + "getTheaterById",
                new Long(id));

    }

    /**
     * Get array of theaters from movie table according array of ids
     * 
     * @param theaterIdArray Ids of theaters to get
     * @return List<code>Theater</code>
     * 
     * @see com.telenav.browser.movie.db.TheaterDao#getTheatersByIds(long[])
     */
    public List getTheatersByIds(long[] theaterIdArray) throws SQLException {
        // Array to sqlString
        String theaterIdString = DaoUtil.arrayToSql(theaterIdArray);
        if (theaterIdString == null) {
            return null;
        }
        // Get sort sql string for where id in(?,?,?,....)
        String sortString = DaoUtil.arrayToSortSql(theaterIdString);

        HashMap map = new HashMap();
        map.put("sortType", sortString);
        map.put("theaterIds", theaterIdString);

        return sqlMap.queryForList(nameSpace + "getTheaters", map);

    }

    /**
     * Search theater by GeoCircle(lat,lon,radius),return theaters which is in
     * the radius of selected point(lat,lon).
     * 
     * @param geoCircle Circle which the theaters are in.
     * @return List<code>Theater</code>
     * 
     * @see com.telenav.browser.movie.db.TheaterDao#searchTheatersInCircle(com.telenav.browser.movie.datatype.GeoCircle)
     */
    public List searchTheatersInCircle(GeoCircle geoCircle) throws SQLException {
        if (geoCircle == null) {
            return null;
        }
        BoundingBox boundingBox = BoundingBox.getBoundingBox(
                geoCircle.getLat(), geoCircle.getLon(), geoCircle.getRadius());
        List boundTheaters = this.searchBoundTheaters(boundingBox);
        List theaters = cullTheatersInCircle(boundTheaters, geoCircle);
        return theaters;
    }

    /**
     * Get array of theaters from theater table in certain bounding box
     * 
     * @param boundingBox Certain bounding box which the theaters are in.
     * @return List<code>Theater</code>
     * @throws SQLException
     */
    private List searchBoundTheaters(BoundingBox boundingBox)
            throws SQLException {
        if (boundingBox == null) {
            return null;
        }
        return sqlMap.queryForList(nameSpace + "searchBoundTheaters",
                boundingBox);
    }

    /**
     * Remove the theater is not in the certain geoCircle
     * 
     * @param boundTheaters
     * @param geoCircle
     * @return List<code>Theaters</code>
     */

    private List cullTheatersInCircle(List boundTheaters, GeoCircle geoCircle) {

        if (boundTheaters == null || boundTheaters.size() < 1) {
            return boundTheaters;
        }
        Theater theater = null;
        for (int i = 0; i < boundTheaters.size(); i++) {
            theater = (Theater) boundTheaters.get(i);
            if (!theater.inCircle(geoCircle)) {
                boundTheaters.remove(i);
                i--;
            }
        }
        return boundTheaters;

    }

    /**
     * Create temporary table for store latest data.
     * 
     * @throws SQLException
     * 
     * @see com.telenav.browser.movie.db.TheaterDao#createTemp()
     */
    public void createTemp() throws SQLException {
        drop("theater_temp");
        create("theater_temp");

    }

    /**
     * When the process of getting latest data is completed , copy temporary
     * table into primary table and backup table with date suffix name.
     * 
     * @throws SQLException
     * 
     * @see com.telenav.browser.movie.db.TheaterDao#apply()
     */
    public void apply() throws SQLException {
        deleteData(TABLE_THEATER);
        copy();
    }

    /**
     * Rename temporary table to primary table
     * 
     * @throws SQLException
     */
    private void copy() throws SQLException {
        sqlMap.insert(nameSpace + "copyTheaterTable", TABLE_THEATER);

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
//                + "checkTheaterTable", tableName));
//        if (!tableName.equalsIgnoreCase(s)) {
//            return false;
//        }
//        return true;
//    }

    /**
     * Copy temporary table into new table of table name with date suffix
     * 
     * @param date
     * 
     * @throws SQLException
     * 
     * @see com.telenav.browser.movie.db.TheaterDao#backUp()
     */
//    public void backUp() throws SQLException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
//        String dateString = sdf.format(new Date());
//        drop("theater_" + dateString);
//        create("theater_" + dateString);
//        sqlMap.insert(nameSpace + "copyTheaterTable", "theater_" + dateString);
//
//    }

    /**
     * Create new table with structure of primary movie table
     * 
     * @param tableName
     * @throws SQLException
     */
    private void create(String tableName) throws SQLException {
        sqlMap.update(nameSpace + "newTheaterTable", tableName);
    }

    /**
     * Drop table
     * 
     * @param tableName
     * @throws SQLException
     */
    private void drop(String tableName) throws SQLException {
        sqlMap.delete(nameSpace + "dropTheaterTable", tableName);
    }

    private void deleteData(String tableName) throws SQLException {
        sqlMap.delete(nameSpace + "deleteTheaterData", tableName);
    }
    
}
