/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.commutealert;

import com.telenav.cserver.framework.ServerException;

/**
 * CommuteAlertException
 * 
 * @author yqchen
 * @version 1.0 2007-11-22 10:14:13
 */
public class CommuteAlertException extends ServerException {

    private int code;

    public CommuteAlertException(int code, String message)
    {
        super(message);
        this.code = code;
    }

    public CommuteAlertException(int code)
    {
        super(Integer.toString(code));
        this.code = code;
    }

    public CommuteAlertException()
    {
    }

    public CommuteAlertException(Throwable cause)
    {
        super(cause);
    }

    public CommuteAlertException(int code, Throwable cause)
    {
        super(Integer.toString(code), cause);
        this.code = code;
    }

    public CommuteAlertException(String message)
    {
        super(message);
    }

    public CommuteAlertException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public CommuteAlertException(int code, String message, Throwable cause)
    {
        super(message, cause);
        this.code = code;
    }

    /**
     * return error code
     *
     * @return
     */
    public int getCode()
    {
        return code;
    }

}
