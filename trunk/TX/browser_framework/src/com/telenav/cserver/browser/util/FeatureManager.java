package com.telenav.cserver.browser.util;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PropertyResourceBundle;

import com.telenav.tnbrowser.util.DataHandler;
import com.telenav.tnbrowser.util.IFeatureManager;
import com.televigation.log.TVCategory;

/**
 * The feature is an entity that can be turn off/on. Feature availability is defined by set of parameters. 
 * For now it is region, carrier, probably client type(free user or paying customer) and platform
 * 
 * Feature manager loads feature availability flags from property file. Location of this property file 
 * is based on parameter values. Example: for region NA, carrier ATT, file location is 
 * WEB-INF/classes/features/NA/ATT/feature_list.properties. 
 *  
 * @author sergeyz
 */

 /* Implementation notes: 
  For now property file contains features that are not available for given set of parameters. If file is absent 
  that means all features are available. Thought warning is going to be logged in log file.
  */
public class FeatureManager implements IFeatureManager {
	
    private static final TVCategory logger = (TVCategory) TVCategory.getInstance(FeatureManager.class);
    
    /** Convenience key for DSR feature */
	public static final String DSR = "DSR";
	
	/* Keeps all managers as a singleton per set of parameters */
	private static HashMap<String, IFeatureManager> managerList = new HashMap<String, IFeatureManager>();
	private static final String[] defaultParams = {DataHandler.KEY_REGION, DataHandler.KEY_CARRIER};
	private static final String fDir = "features/"; 
	private static final String fName = "feature_list.properties";
	private static Object mutex = new Object();
	
	/**
	 * Returns feature manager defined by client information. Default parameter list is used (REGION, CARRIER)
	 * @param handler client information container
	 */
	public static IFeatureManager getManager(DataHandler handler){
		return getManager(handler, defaultParams);
	}
	/**
	 * Returns feature manager defined by client information 
	 * @param handler client information container 
	 * @param paramNames list of parameters that are used to "calculate" feature availability. 
	 */
	public static IFeatureManager getManager(DataHandler handler, String[] paramNames){
		String fManagerKey = calculateParamString(handler, paramNames);
		if (!managerList.containsKey(fManagerKey)){ //load property file
			synchronized (mutex) { 
				if (managerList.containsKey(fManagerKey)) return managerList.get(fManagerKey); // if other thread already initialized it
				ClassLoader cl = Thread.currentThread().getContextClassLoader();
	        	InputStream stream = cl.getResourceAsStream(fDir + fManagerKey + fName);
	        	PropertyResourceBundle bundle = null;
	        	FeatureManager mgr = new FeatureManager();
            	mgr.featureList = new HashSet<String>();
	            if (stream != null){
	            	try{
		            	bundle = new PropertyResourceBundle(stream);
		            	Enumeration<String> features = bundle.getKeys();
		            	while(features.hasMoreElements()) {
		            		String feature = features.nextElement(); 
		            		mgr.featureList.add(feature);
		            	}
	            	}catch(Exception ex){
	                	logger.warn("All features are available for parameters <" + fManagerKey + ">", ex);
	            	}
	            }else{
	            	logger.warn("All features are available for parameters <" + fManagerKey + ">");
	            }
        		if (logger.isDebugEnabled())
        			logger.debug("Features unavailable for parameters <" + fManagerKey + "> :" + mgr.featureList);
	        	managerList.put(fManagerKey, mgr);
			}
		}
		return managerList.get(fManagerKey);
	}
	
	private static String calculateParamString(DataHandler handler, String[] paramNames){
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < paramNames.length; i++) {
			buff.append(handler.getClientInfo(paramNames[i]));
			buff.append("/");
		}
		return buff.toString();
	}
	
	private HashSet<String> featureList;
	
	/**
	 * Checks if feature is available.
	 */
	public boolean isEnabled(String feature) {
		return !featureList.contains(feature);
	}
	
}
