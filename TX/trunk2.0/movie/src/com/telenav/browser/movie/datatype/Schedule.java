/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Oct 23, 2008
 * File name: Schedule.java
 * Package name: com.telenav.j2me.browser.datatype
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 1:14:51 PM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.datatype;

import java.util.Date;

/**
 * @author lwei (lwei@telenav.cn) 1:14:51 PM, Oct 23, 2008
 */
public class Schedule implements Cloneable {
    /** Fields */
    private Theater theater;

    private Movie movie;

    private String showTime;

    private String movieVendorId;

    private String theaterVendorId;

    private long movieId;

    private long theaterId;

    private long id;

    private Date date;

    private String vendorName;

    private String quals;

    private String ticketURI;

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMovieVendorId() {
        return movieVendorId;
    }

    public void setMovieVendorId(String movieVendorId) {
        this.movieVendorId = movieVendorId;
    }

    public String getTheaterVendorId() {
        return theaterVendorId;
    }

    public void setTheaterVendorId(String theaterVendorId) {
        this.theaterVendorId = theaterVendorId;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public long getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(long theaterId) {
        this.theaterId = theaterId;
    }

    /**
     * @return the theater
     */
    public Theater getTheater() {
        return theater;
    }

    /**
     * @param theater the theater to set
     */
    public void setTheater(Theater theater) {
        this.theater = theater;
    }

    /**
     * @return the movie
     */
    public Movie getMovie() {
        return movie;
    }

    /**
     * @param movie the movie to set
     */
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    /**
     * @return the showTime
     */
    public String getShowTime() {
        return showTime;
    }

    /**
     * @param showTime the showTime to set
     */
    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    /**
     * Low clone because there're no need in MOVIES project.
     * 
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        Schedule o = null;
        try {
            o = (Schedule) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    public String toString() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("Schedule================START\n");

        buffer.append("id:" + this.getId() + "\n");
        buffer.append("VendorName:" + this.getVendorName() + "\n");
        buffer.append("MovieVendorId:" + this.getMovieVendorId() + "\n");
        buffer.append("TheaterVendorId:" + this.getTheaterVendorId() + "\n");
        buffer.append("MovieId:" + this.getMovieId() + "\n");
        buffer.append("TheaterId:" + this.getTheaterId() + "\n");
        buffer.append("ShowTime:" + this.getShowTime() + "\n");
        buffer.append("Date:" + this.getDate().toString() + "\n");
        buffer.append("TicketURI:" + this.getTicketURI() + "\n");
        buffer.append("Quals:" + this.getQuals() + "\n");
        buffer.append("Movie:"
                + (this.getMovie() == null ? "null" : this.getMovie()
                        .toString()) + "\n");
        buffer.append("Theater:"
                + (this.getTheater() == null ? "null" : this.getTheater()
                        .toString()) + "\n");

        buffer.append("Schedule================OVER\n ");

        return buffer.toString();

    }

    /**
     * @return the quals
     */
    public String getQuals() {
        return quals;
    }

    /**
     * @param quals the quals to set
     */
    public void setQuals(String quals) {
        this.quals = quals;
    }

    /**
     * @return the ticketURI
     */
    public String getTicketURI() {
        return ticketURI;
    }

    /**
     * @param ticketURI the ticketURI to set
     */
    public void setTicketURI(String ticketURI) {
        this.ticketURI = ticketURI;
    }

}
