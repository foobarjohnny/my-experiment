/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Oct 28, 2008
 * File name: ManagerFactory.java
 * Package name: com.telenav.j2me.browser.service
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 5:53:44 PM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.service;

import com.telenav.browser.movie.service.impl.MoviesManagerImpl;

/**
 * @author lwei (lwei@telenav.cn) 5:53:44 PM, Oct 28, 2008
 */
public class ManagerFactory {
    private static MoviesManager moviesManager = new MoviesManagerImpl();

    public static MoviesManager getMoviesManager() {
        return moviesManager;
    }
}
