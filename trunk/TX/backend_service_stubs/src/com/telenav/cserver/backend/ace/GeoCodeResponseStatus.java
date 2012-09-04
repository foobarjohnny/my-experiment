package com.telenav.cserver.backend.ace;

import com.telenav.ws.datatypes.services.ResponseStatus;

/**
 * @author: rapu
 * @date: 2009-5-12
 * @time: 10:46:39
 * TeleNav Inc.
 */
public class GeoCodeResponseStatus
{
    public static final int UNKNOWN_STATUS = -1;

    public static final int EXACT_FOUND = 0;

    public static final int MULTIPLE_FOUND = 1;

    public static final int NOT_FOUND = 3;

    public static final int CITY_NAME_MODIFIED = 12;


    public static final int EXCEPTION_BASE = 10000;

    public static final int UNKOWN_EXCEPTION = EXCEPTION_BASE + 1;

    public static final int TOO_MANY_MATCH_EXCEPTION = EXCEPTION_BASE + 2;

    public static final int ERROR_BASE = 100000;

    public static final int INVALID_INPUT_ERROR = ERROR_BASE + 2;

    public static final int INVALID_REQUEST_TYPE = ERROR_BASE + 3;

    public static final int NO_RESPONSE = ERROR_BASE + 4;

    public static final int TIMED_OUT = ERROR_BASE + 5;

    public static final int OUT_OF_MEMORY = ERROR_BASE + 6;

    private ResponseStatus status = null;

    private final static String UNKNOWN_STATUS_MSG = "Unknown status.";

    public GeoCodeResponseStatus(ResponseStatus status)
    {
        this.status = status;
    }

    public int getStatusCode()
    {
        if (null == status) return UNKNOWN_STATUS;
        try
        {
            return Integer.parseInt(this.status.getStatusCode());
        }
        catch (Exception e)
        {
            return UNKNOWN_STATUS;
        }
    }

    public String getDescription()
    {
        if (null == status) return UNKNOWN_STATUS_MSG;
        return status.getStatusMessage();
    }

    public boolean isSuccessful()
    {
        final int status = getStatusCode();
        switch (status)
        {
            case GeoCodeResponseStatus.EXACT_FOUND:
            case GeoCodeResponseStatus.MULTIPLE_FOUND:
            case GeoCodeResponseStatus.CITY_NAME_MODIFIED:
            case GeoCodeResponseStatus.TOO_MANY_MATCH_EXCEPTION:
                return true;
            default:
                return false;
        }
    }
}




