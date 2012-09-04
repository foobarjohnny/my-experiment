/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.impl.interceptor;

import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.client.dsm.ContextMgrService;
import com.telenav.client.dsm.ContextMgrStatus;
import com.telenav.client.dsm.Error;
import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.common.resource.constant.HolderType;
import com.telenav.cserver.common.resource.device.DeviceProperties;
import com.telenav.cserver.common.resource.device.DevicePropertiesHolder;
import com.telenav.cserver.common.resource.holder.impl.DsmRuleHolder;
import com.telenav.cserver.framework.Constants;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.Interceptor;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * TnContextInterceptor.java
 *
 * @author yqchen@telenav.cn
 * @version 1.0 2009-5-5
 *
 */
public class TnContextInterceptor implements Interceptor 
{ 
    public static final String DEFAULT_MAP_DATA = "Navteq";
    public static final String DEFAULT_POI_DATASET = "TA";
    

    public static final String PROP_DSM_SERVER_STATUS = "dsm_server_status";
    public static final String DSM_SERVER_DOWN = "down";
    
    private static final String GENERATE_LANE_INFO="GENERATE_LANE_INFO";
    private static final String NAV_ENABLE_LANE_ASSIST="NAV_ENABLE_LANE_ASSIST";
    
    public static final String NAV_FLOW_DATA_SRC_KEY="flow_data_src";
    public static final String NAV_ALERT_DATA_SRC_KEY="alert_data_src";
    
    public static final String PROP_REGION = "region";
    public static final String PROP_PROGRAM_CODE = "program_code";
    
	Logger logger = Logger.getLogger(TnContextInterceptor.class);
	
	boolean needRegister = false;
	
	
	/**
	 * @return the needRegister
	 */
	public boolean isNeedRegister() {
		return needRegister;
	}


	/**
	 * @param needRegister the needRegister to set
	 */
	public void setNeedRegister(boolean needRegister) {
		this.needRegister = needRegister;
	}


	public InterceptResult intercept(ExecutorRequest request,
			ExecutorResponse response, ExecutorContext context)
	{
		TnContext tnContext = context.getTnContext();

		if (tnContext != null && tnContext.size() > 0)
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Re-use tnContext:" + tnContext);
				logger.debug("Returned.");
			}
			return InterceptResult.PROCEED;
		}

		tnContext = getTnContext(request.getUserProfile(), context);
		context.setTnContext(tnContext);
		return InterceptResult.PROCEED;
	}

    
	public TnContext getTnContext(UserProfile userProfile, ExecutorContext context)
	{
		
		String userID = userProfile.getUserId(); 	
		
		long lUserID = -1;
		if (userID != null && userID.length() > 0) {
			lUserID = Long.parseLong(userID);
		}
	
		ContextMgrService cms = new ContextMgrService();
        ContextMgrStatus myStatus = null;
        
        if(logger.isDebugEnabled())
        {
        	logger.debug("needRegister:" + needRegister);
        }
        
        TnContext tc = new TnContext();
        try
        {
	        if(needRegister)
	        {
	            //get the tncontext from DSM db
	            //tc = getTnContextFromDsmDB(userProfile.getMin());
	            //set user profile into tncontext
	            setUserProfileToTnContext(tc, userProfile, context);
	            //set the need register dsm rule into tncontext
	            setNeedRegisterDsmRuleToTnContext(userProfile, tc);
	            if( logger.isDebugEnabled() )
	            {
	                logger.debug("tc -->" + tc);
	                logger.debug("tc.length = "+tc.toContextString().length());
	            }
	            myStatus = cms.registerContext(lUserID, Constants.CALLER_CSERVER, tc);
	            
	        }
	        else
	        {
	            //set user profile into tncontext
	            setUserProfileToTnContext(tc, userProfile, context);
	            myStatus = cms.updateContext(tc);
	        }
        }
        catch(Exception e)
        {
        	logger.fatal(e, e);
        }
        
        //set all the dsm rules in to tncontext
        setAllDsmRuleToTnContext(userProfile, tc);
            
        DevicePropertiesHolder deviceHolder = (DevicePropertiesHolder)ResourceHolderManager.getResourceHolder(HolderType.DEVICE_TYPE); 
        if( deviceHolder != null )
        {
            DeviceProperties deviceProperties = deviceHolder.getDeviceProperties(userProfile, tc);
            if( deviceProperties != null )
            {
                tc.addProperty(GENERATE_LANE_INFO, String.valueOf(deviceProperties.getBooleanDefFalse(NAV_ENABLE_LANE_ASSIST)));
            }
        }
        
        if(myStatus == null || myStatus.getStatusCode() != Error.NO_ERROR)
        {
            CliTransaction cli = com.telenav.cserver.framework.cli.CliTransactionFactory.getInstance(CliConstants.TYPE_MODULE);
            cli.setFunctionName("getTnContext");
            cli.addData("cms_status(has issue)", myStatus != null ? myStatus.getStatusCode() + "" : "null");
            
            tc = getDefaultTnContext(tc);
            if( logger.isDebugEnabled() )
            {
                logger.debug("tnContext has issue.");
            }
            
            cli.complete();
        }      
        logger.debug("final tc:>>" + tc);
		return tc;
	}
	
	/*private TnContext getTnContextFromDsmDB(String loginName)
	{
	    TnContext tc = new TnContext();
	    tc.addProperty(TnContext.PROP_LOGIN_NAME , loginName);
	    
	    ContextMgrService cms = new ContextMgrService();
	    ContextMgrStatus myStatus = null;
	    try
        {
	        myStatus = cms.updateContext(tc);
	        
	        if(myStatus == null || myStatus.getStatusCode() != Error.NO_ERROR)
	        {
	            CliTransaction cli = com.telenav.cserver.framework.cli.CliTransactionFactory.getInstance(CliConstants.TYPE_MODULE);
	            cli.setFunctionName("getTnContextFromDsmDB");
	            cli.addData("cms_status", myStatus != null ? myStatus.getStatusCode() + "" : "null");
	            cli.complete();
	            
	            if( logger.isDebugEnabled() )
	            {
	                logger.debug("getTnContextFromDsmDB() error: " + (myStatus != null ? myStatus.getStatusCode() + "" : "null"));
	            }
	        }
        }
	    catch(Exception e)
	    {
	        logger.fatal(e, e);
	    }
	    
	    return tc;
	}*/
	
	private void setUserProfileToTnContext(TnContext tc, UserProfile userProfile, ExecutorContext context)
	{
        String loginName = userProfile.getMin();
        String carrier = userProfile.getCarrier();
        String device = userProfile.getDevice();
        String product = userProfile.getPlatform();
        String version = userProfile.getVersion();
        String userID = userProfile.getUserId();    
        String applicationName = userProfile.getProduct();
        String mapDpi = userProfile.getMapDpi();
        String locale = userProfile.getLocale();
        String programCode = userProfile.getProgramCode();
      
        tc.addProperty(TnContext.PROP_LOGIN_NAME , loginName);
        tc.addProperty(TnContext.PROP_CARRIER , carrier);
        tc.addProperty(TnContext.PROP_DEVICE , device);
        tc.addProperty(TnContext.PROP_PRODUCT , product);
        tc.addProperty(TnContext.PROP_VERSION , version);
//      tc.addProperty("user_family", Long.toString(ResourceLoader.getInstance().getUserFamily(userProfile)));
        tc.addProperty("application", applicationName);//"application" should defined in TnContext
        tc.addProperty("c-server class", Constants.CSERVER_CLASS);
        tc.addProperty("c-server url", context.getServerUrl());
        tc.addProperty("map_dpi", mapDpi);
        tc.addProperty("locale", locale);
        tc.addProperty(PROP_PROGRAM_CODE, programCode);
        
        tc.addProperty(TnContext.PROP_REQUESTOR, TnContext.TT_REQUESTOR_TNCLIENT);
        
        tc.addProperty(PROP_REGION, userProfile.getRegion());
        
	}
	
	public void setNeedRegisterDsmRuleToTnContext(UserProfile userProfile, TnContext tc)
	{
	    setDsmRuleToTnContext(userProfile, tc, false);
	}
	
	public void setAllDsmRuleToTnContext(UserProfile userProfile, TnContext tc)
	{
	    setDsmRuleToTnContext(userProfile, tc, true);
	}
	
	private void setDsmRuleToTnContext(UserProfile userProfile, TnContext tc, boolean isAddAllDsmRule)
    {
        DsmRuleHolder dsmHolder = ResourceHolderManager.getResourceHolder(HolderType.DSM_TYPE);
        if (dsmHolder != null)
        {
            Map map = dsmHolder.getDsmResponses(userProfile, tc);
            if (map != null)
            {
                Iterator it = map.keySet().iterator();
                while (it.hasNext())
                {
                    String key = (String) it.next();
                    if( isAddAllDsmRule )
                    {
                        tc.addProperty(key, (String) map.get(key));
                    }
                    else
                    {
                        if( isDsmRuleNeedRegister(key) )
                        {
                            tc.addProperty(key, (String) map.get(key));
                        }
                    }
                }
            }
        }
    }
	
	private final String[] NEED_RIGISTER_DSM_RULE_KEYS = {NAV_FLOW_DATA_SRC_KEY, NAV_ALERT_DATA_SRC_KEY};
	//private final String[] NEED_RIGISTER_DSM_RULE_KEYS = {};
	private boolean isDsmRuleNeedRegister(String key)
	{
	    for(int i=0; i<NEED_RIGISTER_DSM_RULE_KEYS.length; i++)
	    {
	        if( NEED_RIGISTER_DSM_RULE_KEYS[i].equals(key) )
	            return true;
	    }
	    return false;
	}
	
	
	 /**
     * If DSM server failed, then no choice to use the default one
     * @return
     */
    private TnContext getDefaultTnContext(TnContext tc)
    {
    	if(tc == null)
    	{
    		tc = new TnContext();
    	}
        tc.addProperty(TnContext.PROP_MAP_DATASET , DEFAULT_MAP_DATA);
        tc.addProperty(TnContext.PROP_POI_DATASET , DEFAULT_POI_DATASET);
        //Add this property to indicate this is a default dsm config
        tc.addProperty(PROP_DSM_SERVER_STATUS, DSM_SERVER_DOWN);
        
        logger.debug("getDefaultTnContext>>" + tc.getProperty(TnContext.PROP_MAP_DATASET));
        return tc;
    }
}