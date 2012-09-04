/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.impl.interceptor;
import org.apache.log4j.Logger;

import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.Interceptor;
import com.telenav.cserver.framework.livestat.AppManager;
import com.telenav.cserver.framework.util.LiveStatsLogger;

/**
 * LiveStatInterceptor This interceptor is for live stat.
 * 
 * @author jzhu@telenav.cn
 * @version 1.0 2010-2-23
 * 
 */
public class LiveStatInterceptor implements Interceptor
{

    Logger log = Logger.getLogger(LiveStatInterceptor.class);

    String app = "";
    
    /**
     * @return
     */
    public String getApp()
    {
        return app;
    }

    /**
     * 
     * @param enableLiveStat
     */
    public void setApp(String app)
    {
        this.app = app;
    }
    
    private static final String[] tripList = {"Dynamic_Route",
                                              "Chunked_Dynamic_Route", 
                                              "Static_Route", 
                                              "Reverse_Route", 
                                              "Decimated_MultiRoute", 
                                              "Traffic_Avoid_Selected_Seg_Reroute", 
                                              "Traffic_Min_Delay_Rerout", 
                                              "Traffic_Avoid_Incidents_Reroute", 
                                              "Traffic_Static_Min_Delay_Reroute", 
                                              "Traffic_Avoid_Selected_Seg_Deci_Reroute",
                                              "Traffic_Min_Delay_Deci_Reroute",
                                              "Traffic_Static_Avoid_Selected_Seg_Deci_Reroute",
                                              "Traffic_Static_Min_Delay_Deci_Reroute"};
    
    private static final String[] devList = {"Check_Deviation", 
                                             "CheckSelected_Deviation", 
                                             "Chunk_CheckSelected_Deviation"};
    public InterceptResult intercept(ExecutorRequest request,
            ExecutorResponse response, ExecutorContext context) 
    {
        
        try
        {
        	/*
            LiveStatsLogger ls = createLiveStatsLogger();
            ls.setApp(app);
            if( "routing".equals(app) )
            {
                String executeType = request.getExecutorType();
                if( hasElement(tripList,executeType) )
                {
                    ls.setEx1("trip");
                }
                else if( hasElement(devList,executeType) )
                {
                    ls.setEx1("dev");
                }
                else
                {
                    ls.setEx1("more");
                }
            }
            if (response.getStatus() == ExecutorResponse.STATUS_EXCEPTION)
                ls.setSuccessCount(ls.getDefaultCount());
            
            ls.send();
            */
        	String appName = app;
        	String extName = "";
        	boolean isSuccess = true;
        	if("routing".equals(app))
        	{
        		 String executeType = request.getExecutorType();
                 if( hasElement(tripList,executeType) )
                 {
                	 extName = "trip";
                 }
                 else if( hasElement(devList,executeType) )
                 {
                	 extName = "dev";
                 }
                 else
                 {
                	 extName = "more";
                 }
        	}
            if(response.getStatus() == ExecutorResponse.STATUS_EXCEPTION)
            	isSuccess = false;
            AppManager.getInstance().setLiveStat(appName, extName, isSuccess);      	
        }
        catch(Exception e)
        {
            log.error("LiveStatInterceptor", e);
        }
        return InterceptResult.PROCEED;
    }
    
    protected LiveStatsLogger createLiveStatsLogger()
    {
    	return new LiveStatsLogger();
    }
    
    private boolean hasElement(String[] array, String testElement)
    {
        for(String element : array)
        {
            if( element.equals(testElement))
                return true;
        }
        return false;
    }

}