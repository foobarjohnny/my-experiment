/**
 * (c) Copyright 2008 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Category;

import com.telenav.cserver.common.resource.ptn.PtnProperties;

/**
 * for cache the /device/ptn_resouce/ptn.properties
 * @author jzhu@telenav.cn
 *
 */
public class GeneralPtnPropertyManager
{
    private static Category logger = Category.getInstance(GeneralPtnPropertyManager.class);
    
    private static final String PTN_RESOURCE_ROOT_PATH = "/device/ptn_resource/";
    private static final String fileName = "/device/ptn_resource/ptn.properties";
    
    private static Properties ptnProperties = initGeneralPtnProperty();

    private static Properties initGeneralPtnProperty()
    {
        Properties prop = new Properties();
        InputStream is = null;
        try
        {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if(is == null)
            {
                logger.warn("File not found:" + fileName);
                return prop;
            }
            prop.load(is);
        }
        catch(Exception e)
        {
            logger.warn("GeneralPtnPropertyManager.getGeneralPtnProperty()" + e.getMessage());
        }
        finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {

                }
            }
        }
        
        return prop;
    }
    
    
    public static void clear()
    {
        ptnProperties = initGeneralPtnProperty();
    }
    
    
    public static Map getGeneralPtnMap()
    {
        return (Map)ptnProperties;
    }
    
    
    /**
     * get the ptn resource path 
     * @param ptn
     * @return
     */
    public static String getPtnResourcePath(String ptn)
    {
        String ptnFolder = PtnProperties.get((Map)ptnProperties, ptn);  
            
        if (ptnFolder == null)
        {
            return "";
        }
        else
        {
            return PTN_RESOURCE_ROOT_PATH + ptnFolder + "/";
        }
    }
    
}
