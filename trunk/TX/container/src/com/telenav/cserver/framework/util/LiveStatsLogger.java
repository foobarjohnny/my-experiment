package com.telenav.cserver.framework.util;

import java.io.IOException;
import java.util.PropertyResourceBundle;

import com.telenav.livestats.client.LiveStatsPacket;
import com.telenav.livestats.client.LiveStatsPacketFactory;
import com.telenav.livestats.client.LiveStatsPacketFactoryBuilder;
import com.televigation.log.TVCategory;

public class LiveStatsLogger
{
    protected static TVCategory logger = (TVCategory) TVCategory
            .getInstance(LiveStatsLogger.class);

    private static String HOST_ADDRESS = "s-livestat-01";

    private static int HOST_PORT = 8088;

    private String requestCount = "1";

    private String failedCount = "0";

    private String successCount = "1";

    
    private String app = "";
    
    private String ex1 = null;
    
    private static final String TYPE = "tn60";

    private static LiveStatsPacketFactory factory;

    static
    {
        // read liveStats URL from configuration
        String fileName = "config.liveStats";
        PropertyResourceBundle bundle = (PropertyResourceBundle) PropertyResourceBundle
                .getBundle(fileName);
        HOST_ADDRESS = bundle.getString("LIVE_STATE_HOST");
        HOST_PORT = Integer.parseInt(bundle.getString("LIVE_STATE_PORT"));
        try
        {
            factory = LiveStatsPacketFactoryBuilder.buildPacketFactory(
                    HOST_ADDRESS, HOST_PORT);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            logger.fatal("LiveStats Server error: " + e.getMessage());
        }
        logger.info("LIVE_STATE_HOST:" + HOST_ADDRESS);
        logger.info("LIVE_STATE_PORT:" + HOST_PORT);

    }

    public String getLiveStatsHost()
    {
        return HOST_ADDRESS;
    }

    public int getLiveStatsPort()
    {
        return HOST_PORT;
    }

    public String getRequestCount()
    {
        return requestCount;
    }
    
    public void setRequestCount(String requestCount)
    {
    	this.requestCount = requestCount;
    }

    public String getSuccessCount()
    {
        return successCount;
    }

    public void setSuccessCount(String successCount)
    {
        this.successCount = successCount;
    }

    public String getDefaultCount()
    {
        return failedCount;
    }

    public void setApp(String app)
    {
        this.app = app;
    }
    
    public void setEx1(String ex1)
    {
        this.ex1 = ex1;
    }

    public void send()
    {
        try
        {   //never send a bad packet out
            //if packetFactory server can't be initialized , don't send the liveStats info.
            if (factory == null || app.equalsIgnoreCase("")) 
            {
                logger.fatal("LiveStats log can't been sent out!!!");
                return;
            }
            LiveStatsPacket packet = factory.createPacket();
            packet.add("type", TYPE)
                  .add("app", app);
            if( ex1 != null )
            {
                packet.add("ex1", ex1);
            }
            packet.add("requestCount", getRequestCount())
                  .add("successCount",getSuccessCount())
                  .send();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
