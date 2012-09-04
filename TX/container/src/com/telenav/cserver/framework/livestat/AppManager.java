package com.telenav.cserver.framework.livestat;

import java.util.HashMap;
import java.util.Map;

import com.telenav.cserver.framework.threadpool.Job;
import com.telenav.cserver.framework.threadpool.ThreadPool;
import com.telenav.cserver.framework.util.LiveStatsLogger;

/**
 * 
 * @author donghengz
 *
 */

public class AppManager 
{
	private static final int MAX_PACKET_SIZE = 200;//100;
	private static final int MAX_THREAD_SIZE = 3;
	
    //private static Map<String, App> appMap = Collections.synchronizedMap(new HashMap<String, App>());//Performance
	private static Map<String, App> appMap = new HashMap<String, App>();
    
    private static AppManager manager;
    
    private static ThreadPool pool = new ThreadPool(MAX_THREAD_SIZE);
    
    public static AppManager getInstance()
    {
    	if(manager == null)
    		manager = new AppManager();
    	return manager;
    }
    
    private App getApp(String app, String ext)
    {
    	App oneApp = null;
        String key = this.getKey(app, ext);
        
        oneApp = appMap.get(key);
        if(oneApp == null)
        {
        	synchronized(appMap)
        	{
        		oneApp = appMap.get(key);
        		if(oneApp == null)
        		{
        			oneApp = new App(app, ext);
        			appMap.put(key, oneApp);
        		}
        	}
        }
    	return oneApp;
    }
    
    private String getKey(String app, String ext)
    {
    	String key = app;
    	if(ext != null && ext.length() > 0)
    		key = app + "_" + ext;
    	return key;
    }
    
    public void setLiveStat(String app, String ext, boolean isSuccess)
    {
    	App oneApp = this.getApp(app, ext);
    	synchronized(oneApp)
    	{
    		oneApp.incrementRequest();
    		if(isSuccess)
    		{
    			oneApp.incrementSuccess();
    		}
    		if(oneApp.getRequestCount() == MAX_PACKET_SIZE)
    		{
    			LiveStatsLogger ls = new LiveStatsLogger();
    			ls.setApp(oneApp.getAppName());
    			if(ext != null && ext.length() > 0)
    				ls.setEx1(oneApp.getExtName());
    			ls.setRequestCount(""+oneApp.getRequestCount());
    			ls.setSuccessCount(""+oneApp.getSuccessCount());
    			LiveStatJob job = new LiveStatJob(ls);
    			pool.addJob(job);
    			oneApp.resetCount();
    		}
    	}
    }
    
    /*
    public static void main(String[] args) throws Exception
    {
    	ThreadPool testPool = new ThreadPool(50);
    	for(int i = 0 ; i < 1009; i++)
    	{
    		TestJob job = new TestJob("Route", "trip", true);
    		testPool.addJob(job);
    		TestJob job2 = new TestJob("Route", "more", true);
    		testPool.addJob(job2);
    		TestJob job3 = new TestJob("Route", "dev", true);
    		testPool.addJob(job3);
    		TestJob job4 = new TestJob("Resource", "", true);
    		testPool.addJob(job4);
    	}	
//    	for(int i = 0 ; i < 1008; i++)
//    	{
//    		TestJob job = new TestJob("Route", "more", true);
//    		testPool.addJob(job);
//    	}
//    	for(int i = 0 ; i < 1007; i++)
//    	{
//    		TestJob job = new TestJob("Route", "dev", true);
//    		testPool.addJob(job);
//    	}
//    	for(int i = 0 ; i < 1006; i++)
//    	{
//    		TestJob job = new TestJob("Resource", "", true);
//    		testPool.addJob(job);
//    	}
    	Thread.currentThread().sleep(10000);
    	System.out.println("Route trip : " + AppManager.getInstance().getApp("Route", "trip").requestCount + "/" + AppManager.getInstance().getApp("Route", "trip").successCount);
    	System.out.println("Route more : " + AppManager.getInstance().getApp("Route", "more").requestCount + "/" + AppManager.getInstance().getApp("Route", "more").successCount);
    	System.out.println("Route dev : " + AppManager.getInstance().getApp("Route", "dev").requestCount + "/" + AppManager.getInstance().getApp("Route", "dev").successCount);
    	System.out.println("Resource : " + AppManager.getInstance().getApp("Resource", "").requestCount + "/" + AppManager.getInstance().getApp("Resource", "").successCount);
    }
    */
}

/*
class TestJob implements Job 
{
	String app;
	String ext;
	boolean isSuccess;
	
	TestJob(String app, String ext, boolean isSuccess)
	{
		this.app = app;
		this.ext = ext;
		this.isSuccess = isSuccess;
	}
	
	public void doIt(int handlerID) throws Exception
	{
		AppManager.getInstance().setLiveStat(app, ext, isSuccess);
	}
}
*/

class App
{
	String appName;
	String extName;

	int requestCount = 0;
	int successCount = 0;
	
	App(String appName, String extName)
	{
	    this.appName = appName;	
	    this.extName = extName;
	}
	
	void incrementRequest()
	{
		this.requestCount++;
	}
    
    void incrementSuccess()
    {
    	this.successCount++;
    }
    
	public int getRequestCount() {
		return requestCount;
	}

	public void setRequestCount(int requestCount) {
		this.requestCount = requestCount;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}
	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getExtName() {
		return extName;
	}

	public void setExtName(String extName) {
		this.extName = extName;
	}
    
    void resetCount()
    {
    	this.requestCount = 0;
    	this.successCount = 0;
    }
}
