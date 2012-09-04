package com.telenav.cserver.dsr.ds;

import com.telenav.j2me.datatypes.Stop;
import com.telenav.xnav.teleaddress.AddressStatusTypeDef;

import java.util.List;
import java.util.ArrayList;

/**
 * User: llzhang
 * Date: 2009-9-18
 * Time: 10:25:12
 */
public class AddressValidation {
    private List<Stop> validatedStop = new ArrayList<Stop>();
    
    private int status = 0;

    public static final int DEFAULT = 0;
    public static final int IS_EXACT = 1;

    public AddressValidation() {
    }

    public AddressValidation(List<Stop> validatedStop, int status) {
        this.validatedStop = validatedStop;
        this.status = status;
    }

    public List<Stop> getValidatedStop() {
        return validatedStop;
    }

    public void addStop(Stop stop){
        validatedStop.add(stop);
    }

    public int getStatus() {
        return status;
    }

    public boolean isExact(){
        return status == IS_EXACT;
    }

    public boolean isSuccess(){
        return !validatedStop.isEmpty();
    }
}
