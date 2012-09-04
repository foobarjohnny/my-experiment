/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Oct 31, 2008
 * File name: DaoUtil.java
 * Package name: com.telenav.j2me.browser.db.impl
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 10:28:53 AM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.db.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.telenav.browser.movie.datatype.Schedule;
import com.telenav.browser.movie.util.MovieUtil;

/**
 * The Class DaoUtil provides utility for DAO class.
 * 
 * @author dysong (dysong@telenav.cn) 10:28:53 AM, Oct 31, 2008
 */
public class DaoUtil {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd");

    /**
     * @param array
     * @return sql string :array[]{1,2,3,...}--> in (1,2,3,....)
     */
    public static String arrayToSql(long[] array) {
        if (array == null || array.length < 1) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            if (buffer.length() > 0) {
                buffer.append(",");
            }

            buffer.append(array[i]);

        }
        return buffer.toString();

    }

    /**
     * Get sort sql string for sort type.
     * 
     * @param sortType
     * @return Sort sql String
     */
    public static String sortTypeToSql(int sortType) {
        String sortString;
        if (sortType == 2) {
            sortString = "ORDER BY MOVIE_RATING DESC, MOVIE_NAME ASC";
        } else if (sortType == 3){
            sortString = "ORDER BY MOVIE_RELEASE_DATE DESC,MOVIE_NAME ASC";
        } else {
            sortString = "ORDER BY MOVIE_NAME ASC";
        }
        return sortString;

    }

    /**
     * Get sort sql string for where id in(?,?,?,....).
     * 
     * 
     * 
     * @param idArrayString
     * @return
     */
    public static String arrayToSortSql(String idArrayString) {

        return null;

    }

    /**
     * Get date sql string
     * 
     * @param date
     * @return Date sql string
     */
    public static String dateToSql(Date date) {
        String dateString;
        if (date == null) {
            dateString = null;
        } else {
            dateString = dateFormat.format(date);
        }
        return dateString;

    }

    /**
     * Divide raw schedules into detail schedules by splitting of showTime
     * 19:35_20:45-->19:35 20:45
     * 
     * @param shedules List of raw schedules to divide
     * @return List<code>Schedule</code>
     */

    public static List divideRawSchedule(List schedules) {
        if (schedules == null || schedules.size() < 1)
            return null;
        ArrayList newSchedules = new ArrayList();
        Schedule newSchedule;
        Schedule schedule;
        String[] showtimes;
        String showtime;
        int i;
        int j;
        schedules = MovieUtil.sortRawSchedules(schedules);
        for (i = 0; i < schedules.size(); i++) {
            schedule = (Schedule) schedules.get(i);
            showtime = schedule.getShowTime();
            // Split show time(19:30_21:45...-->19:30 21:45 ...)
            if (showtime.indexOf("_") > -1) {
                showtimes = showtime.split("_");
                for (j = 0; j < showtimes.length; j++) {
                    newSchedule = (Schedule) schedule.clone();
                    newSchedule.setShowTime(showtimes[j]);
                    newSchedules.add(newSchedule);
                }
            } else {
                newSchedule = schedule;
                newSchedules.add(newSchedule);
            }
        }
        return newSchedules;

    }

    /**
     * Divide raw schedule into detail schedules by splitting of showTime
     * 19:35_20:45-->19:35 20:45
     * 
     * @param schedule
     * @return List<code>Schedule</code>
     */
    public static List divideRawSchedule(Schedule schedule) {
        if (schedule == null)
            return null;
        ArrayList schedules = new ArrayList();
        Schedule newSchedule;
        String[] showtimes;
        String showtime;

        showtime = schedule.getShowTime();
        // Split show time(19:30_21:45...-->19:30 21:45 ...)
        if (showtime.indexOf("_") > -1) {
            showtimes = showtime.split("_");
            for (int j = 0; j < showtimes.length; j++) {
                newSchedule = (Schedule) schedule.clone();
                newSchedule.setShowTime(showtimes[j]);
                schedules.add(newSchedule);
            }
        } else {
            newSchedule = schedule;
            schedules.add(newSchedule);
        }
        return schedules;

    }

    /**
     * Clone schedule.
     * 
     * @param schedule
     * @return new schedule
     */

    public static Schedule copySchedule(Schedule schedule) {
        if (schedule == null)
            return null;

        Schedule newSchedule = new Schedule();

        newSchedule.setDate(schedule.getDate());
        newSchedule.setId(schedule.getId());
        newSchedule.setMovieId(schedule.getMovieId());
        newSchedule.setMovieVendorId(schedule.getMovieVendorId());
        newSchedule.setMovie(schedule.getMovie());
        newSchedule.setTheater(schedule.getTheater());
        newSchedule.setTheaterId(schedule.getTheaterId());
        newSchedule.setTheaterVendorId(schedule.getTheaterVendorId());
        newSchedule.setShowTime(schedule.getShowTime());
        newSchedule.setVendorName(schedule.getVendorName());
        newSchedule.setTicketURI(schedule.getTicketURI());
        newSchedule.setQuals(schedule.getQuals());

        return newSchedule;

    }
}
