package com.telenav.cserver.dsr.ds;

import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.dsr.util.ResourceConst;

public class RecContext {
    public byte recType;
    public byte audioFormat;
    public Stop location;
    public UserProfile user;
    public TnContext tnContext;
    public String ttsFormat;
    
    public boolean needsGeocoding = false;

    public RecContext(byte recType, byte audioFormat, Stop locationContext, UserProfile user, TnContext tnContext, String ttsFormat) {
        this.recType = recType;
        this.audioFormat = audioFormat;
        this.location = locationContext;
        this.user = user;
        this.tnContext = tnContext;
        this.ttsFormat = ttsFormat;

        if (recType == ResourceConst.DSR_RECOGNIZE_CITY_STATE_GEOCODING) {
            this.recType = ResourceConst.DSR_RECOGNIZE_CITY_STATE;
            needsGeocoding = true;
        }
    }
}
