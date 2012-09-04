package com.telenav.cserver.dsr.util;

import java.util.Properties;
import java.util.Locale;
import java.io.InputStream;
import java.io.IOException;

/**
 * User: llzhang
 * Date: 2009-6-18
 * Time: 15:10:07
 */
public class StateConverter {

    private Properties properties = null;

    private static StateConverter instance = new StateConverter();

    private static final String propFile = "/states/us.properties";

    public static StateConverter getInstance(){
        return instance;
    }

    private StateConverter(){
        loadProp();
    }

    private void loadProp() {
        InputStream in = StateConverter.class.getResourceAsStream(propFile);
        properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException ignored) {
        }
    }

    public String convert(String stateAbbrev){
        if (stateAbbrev == null)
            return "";
        return properties.getProperty(stateAbbrev.toUpperCase(), stateAbbrev);        
    }
}
