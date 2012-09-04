/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Oct 29, 2008
 * File name: DaoManager.java
 * Package name: com.telenav.j2me.browser.db
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 1:43:59 PM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.db;

import java.io.Reader;

import org.apache.log4j.Logger;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.telenav.browser.movie.db.impl.MovieDaoImpl;
import com.telenav.browser.movie.db.impl.MovieImageDaoImpl;
import com.telenav.browser.movie.db.impl.ScheduleDaoImpl;
import com.telenav.browser.movie.db.impl.TheaterDaoImpl;

/**
 * DaoManager manages the different Dao
 * 
 * @author lwei (lwei@telenav.cn) 1:43:59 PM, Oct 29, 2008
 */
public class DaoManager {
    /**
     * log4j
     */
    private static Logger log = Logger.getLogger(DaoManager.class);

    /**
     * sqlMapClient
     */
    private static SqlMapClient sqlMap;

    static {
        try {
            String resource = "com/telenav/browser/movie/db/config/sql-map-config.xml";
            Reader reader = Resources.getResourceAsReader(resource);
            sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);

        } catch (Exception e) {
            log.fatal("Sql error", e);
        }
    }

    /**
     * Get a Movie Dao with a sqlMapClient
     * 
     * @return Movie Dao
     */

    public static MovieDao getMovieDao() {
        return new MovieDaoImpl(sqlMap);
    }

    /**
     * Get a Schedule Dao with a sqlMapClient
     * 
     * @return Schedule Dao
     */

    public static ScheduleDao getScheduleDao() {
        return new ScheduleDaoImpl(sqlMap);
    }

    /**
     * Get a Theater Dao with a sqlMapClient
     * 
     * @return TheaterDao
     */

    public static TheaterDao getTheaterDao() {
        return new TheaterDaoImpl(sqlMap);
    }

    /**
     * Get a MovieImage Dao with a sqlMapClient
     * 
     * @return MovieImageDao
     */

    public static MovieImageDao getMovieImageDao() {
        return new MovieImageDaoImpl(sqlMap);
    }

}
