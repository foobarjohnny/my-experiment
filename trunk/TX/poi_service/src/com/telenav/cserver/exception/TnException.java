package com.telenav.cserver.exception;

public class TnException extends Exception{
    private static final long serialVersionUID = 4064025224609131891L;
    public static final int ERROR_INVALID_REQUEST = 1;
    public static final int ERROR_CANNOT_CONNECTTO_WS = 2;
    public static final int ERROR_WS_NULL_RESPONSE = 3;
    public static final int ERROR_WS_UNKNOWN = 4;
    public static final int ERROR_GENERAL = 9;
    private int code = ERROR_GENERAL;
    
    public TnException() {
        super();
    }

    public TnException(String string) {
        super(string);
    }

    public TnException(Throwable t) {
        super(t);
    }
    
    public TnException(int code)
    {
        super();
        this.code = code;
    }

    public TnException(String message, int code, Throwable cause)
    {
        super(message, cause);
        this.code = code;
    }

    public TnException(String message, int code)
    {
        super(message);
        this.code = code;
    }

    public TnException(int code, Throwable cause)
    {
        super(cause);
        this.code = code;
    }

    public int getCode()
    {
        return code;
    }
}
