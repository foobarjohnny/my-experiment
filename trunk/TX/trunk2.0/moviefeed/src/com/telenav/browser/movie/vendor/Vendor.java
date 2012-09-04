/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Nov 28, 2008
 * File name: Vendor.java
 * Package name: com.telenav.j2me.browser.vendor
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 10:32:02 AM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.vendor;

import java.util.List;

/**
 * @author lwei (lwei@telenav.cn) 10:32:02 AM, Nov 28, 2008
 */
public interface Vendor {

    /**
     * Get the vendor's name. This name should be unique.
     * 
     * @return The vender name.
     */
    public String getVenderName();

    /**
     * Initialized the vendor.
     * 
     * @return Succeed or not.
     */
    public boolean initialize();

    /**
     * Load the feed file. Before getting other information, please load feed
     * first.
     * 
     * @return succeed or not.
     * @throws IllegalAccessException If the vendor is not initialized
     *             correctly.
     */
    public boolean loadFeed() throws IllegalAccessException;

    /**
     * Get the movie list from this vendor. The element in this list is
     * <code>Movie</code>.
     * 
     * @return The movie list.
     * @throws IllegalAccessException If the feed is not finished successfully.
     */
    public List getMovieList() throws IllegalAccessException;

    /**
     * Get the theater list from this vendor. The element in this list is
     * <code>Theater</code>.
     * 
     * @return The theater list.
     * @throws IllegalAccessException If the feed is not finished successfully.
     */
    public List getTheaterList() throws IllegalAccessException;

    /**
     * Get the schedule list from this vendor. The element in this list is
     * <code>Schedule</code>.
     * 
     * @return The schedule list.
     * @throws IllegalAccessException If the feed is not finished successfully.
     */
    public List getScheduleList() throws IllegalAccessException;

    /**
     * Download the pictures for movie. If the picture is not downloaded
     * successfully, set the picture information to <code>Null</code> of the
     * movie.<br>
     * The element in movie list is the <code>Movie</code>.<br/>
     * The returned value is a list of <code>String</code> indicating the
     * picture file name.
     * 
     * @param movieList The movie list.
     * @return The file name list.
     * @throws IllegalAccessException If the vendor is not initialized
     *             correctly.
     */
    public List downloadImage(List movieList) throws IllegalAccessException;

    /**
     * Generate the WAP link according to the given information. TODO Rain
     * concrete the parm.
     * 
     * @return The WAP link.
     * @throws IllegalAccessException If the vendor is not initialized
     *             correctly.
     */
    public String generateWAPLink() throws IllegalAccessException;
}
