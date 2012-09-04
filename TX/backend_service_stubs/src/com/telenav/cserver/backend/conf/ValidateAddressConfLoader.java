package com.telenav.cserver.backend.conf;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ValidateAddressConfLoader
{
    private static final Logger logger = Logger.getLogger(ValidateAddressConfLoader.class);
    
    private static ValidateAddressConfLoader conf= new ValidateAddressConfLoader();
    
    private Properties pros=new Properties();
    
    private ValidateAddressConfLoader()
    {
        try
        {
            InputStream is =ValidateAddressConfLoader.class.getResourceAsStream("validateAddress.properties");
            pros.load(is);
        }catch(Exception e)
        {
            logger.fatal("can't load resource validateAddress.properties",e);
        }
    }
    
    public static ValidateAddressConfLoader getInstance()
    {
        return conf;
    }
    
    public String getString(String key)
    {
        return pros.getProperty(key);
    }
}
