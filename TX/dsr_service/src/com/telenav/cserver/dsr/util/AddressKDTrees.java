package com.telenav.cserver.dsr.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;

import com.telenav.address.engine.index.*;
import com.telenav.address.util.KDTreeUtil;


/**
 * @author xiaochuann@telenav.com, 2010/07
 * 
 * This is an extension of the KDTreeUtil utility. We'd make a container
 * that can construct multiple KDTrees from address files and
 * perform search on any of them by specifying the key.
 * 
 */
public class AddressKDTrees {
	
    private static Logger logger = Logger.getLogger(AddressKDTrees.class.getCanonicalName());
	
    private static AddressKDTrees instance = null;
    
    private static HashMap<String, KDTree> nameToRoot = null;
    
    private static Properties configProps = null;
    
    private static final String configFile = "/config/kdtree.properties";

	/**
	 * make it singleton
	 */
	private AddressKDTrees() {
		try {
			configProps = new Properties();
			configProps.load(AddressKDTrees.class.getResourceAsStream(configFile));
			init();
		} catch (IOException e) {
			logger.severe("Cannot load configuration file:"+configFile);
		}
	}

	/**
	 * get the instance
	 */
    public static synchronized AddressKDTrees getInstance() {
    	if (instance == null)
    		instance = new AddressKDTrees();
    	return instance;
    }

	/**
	 * Initialize the KDTrees
	 */
    private void init() throws IOException {
    	nameToRoot = new HashMap<String, KDTree>(10);
    	Set<String> propKeys = configProps.stringPropertyNames();
    	Iterator<String> it = propKeys.iterator();
    	while ( it.hasNext() ) {
    		 String propName = it.next();
    		 String treeName = propName.substring(0, propName.indexOf("_file"));
    		 if ( treeName != null && !"test".equals(treeName) ) {
    			 String dataFile = configProps.getProperty(propName);
    			 logger.fine("Construct KDTree from " + dataFile + " for key " + treeName);
    			 KDTree root;
    			 try {
    				 root = KDTreeUtil.constructKDTree(dataFile);
        			 nameToRoot.put(treeName, root);
    			 } catch (IOException e) {
    				 logger.severe("IOException when contruct kd tree.");
    			 }
    		 }
    	}
    }
    
	/**
	 * Search the nearest N cities besides the target.  
	 * @param lat
	 * @param lon
	 * @param amount
	 * @param groupName
	 * @return The Destination array order by the distance. The nearest destination is index 0
	 */
	public Destination[] searchNearbyCities(String addressSet,double lat,double lon,int amount){
		KDTree root = nameToRoot.get(addressSet);
		return KDTreeUtil.searchNearbyCities(root,lat,lon,amount);
	}


}
