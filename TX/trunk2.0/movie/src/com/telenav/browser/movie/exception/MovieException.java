/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Oct 31, 2008
 * File name: MovieException.java
 * Package name: com.telenav.j2me.browser.exception
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 2:58:05 PM, Oct 31, 2008
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.exception;

/**
 * @author dysong (dysong@telenav.cn) 2:58:05 PM
 */
public class MovieException extends Exception {

    private static final long serialVersionUID = 4064025224609131891L;

    public MovieException() {
        super();
    }

    public MovieException(String string) {
        super(string);
    }

    public MovieException(Throwable t) {
        super(t);
    }
}
