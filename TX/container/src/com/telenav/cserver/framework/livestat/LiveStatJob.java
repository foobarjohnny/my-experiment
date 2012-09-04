package com.telenav.cserver.framework.livestat;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.threadpool.Job;
import com.telenav.cserver.framework.util.LiveStatsLogger;

public class LiveStatJob implements Job 
{
	Logger log = Logger.getLogger(LiveStatJob.class);
	LiveStatsLogger logger = null;
	
	public LiveStatJob(LiveStatsLogger logger)
	{
        this.logger = logger;		
	}
	
	@Override
	public void doIt(int handlerID) throws Exception
	{
		try
		{
			if(log.isDebugEnabled())
			{
				log.debug("Default Count : " + this.logger.getDefaultCount() + " Request Count : "
						+ this.logger.getRequestCount() + " Success Count : " + this.logger.getSuccessCount()
						+ " Live Host : " + this.logger.getLiveStatsHost());
			}
			this.logger.send();
		}
		catch(Exception ex)
		{
			log.error("LiveStatInterceptor", ex);
		}			
	}

}
