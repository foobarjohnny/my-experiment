/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.telenav.cserver.dsr.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DsrConfigure {

    private static String propFile = "/config/server.properties";

    private static Properties props = new Properties();
    private static Map<String, String> stateIpMap = new HashMap<String, String>();
    private static Map<String, String> dsrIpMap = new HashMap<String, String>();
    private static AtomicInteger ipIndex = new AtomicInteger();
    private static int counter = 0 ;
    private static Map<Integer, Integer> typePortMapClusterA = new HashMap<Integer, Integer>();
    private static List<Integer> clusterAPortList = new ArrayList<Integer>();
    private static final String CLUSTER_A_IP = "CLUSTER_A_IP_";
    private static final String CLUSTER_A_PORT = "CLUSTER_A_PORT_";
    private static Map<Integer, Integer> typePortMapClusterB = new HashMap<Integer, Integer>();
    private static List<Integer> clusterBPortList = new ArrayList<Integer>();
    private static final String CLUSTER_B_IP = "CLUSTER_B_IP_";
    private static final String CLUSTER_B_PORT = "CLUSTER_B_PORT_";
    private static int abCounter = 0;
    public static int abSwitchPoint;
    private static final String AB_COUNTER = "AB_COUNTER";
    
    static{
        InputStream is = null;
        Logger logger = Logger.getLogger(DsrConfigure.class.getName());
        try{
            is = DsrConfigure.class.getResourceAsStream(propFile);
            props.load(is);
            Iterator ite = props.keySet().iterator();
            while(ite.hasNext()){
            	String key = ite.next().toString();
            	if(key.startsWith(CLUSTER_A_IP)){
            		dsrIpMap.put(CLUSTER_A_IP, key.substring(CLUSTER_A_IP.length()));
            	}else if(key.startsWith(CLUSTER_A_PORT)){
            		String[] types = props.getProperty(key).split(",");
            		for(String type:types){
            			typePortMapClusterA.put(Integer.parseInt(type), Integer.parseInt(key.substring(CLUSTER_A_PORT.length())));
            		}
            		clusterAPortList.add(Integer.parseInt(key.substring(CLUSTER_A_PORT.length())));
            	}else if(key.equals(AB_COUNTER)){
            		abSwitchPoint = Integer.parseInt(props.getProperty(key));
            	}else if(key.startsWith(CLUSTER_B_IP)){
            		dsrIpMap.put(CLUSTER_B_IP, key.substring(CLUSTER_B_IP.length()));
            	}else if(key.startsWith(CLUSTER_B_PORT)){
            		String[] types = props.getProperty(key).split(",");
            		for(String type:types){
            			typePortMapClusterB.put(Integer.parseInt(type), Integer.parseInt(key.substring(CLUSTER_B_PORT.length())));
            		}
            		clusterBPortList.add(Integer.parseInt(key.substring(CLUSTER_B_PORT.length())));
            	}
            }
            
            String configInfo = "Reading properties::" +
            					"AB_SWITCH: "+abSwitchPoint +
            					"dsrIPMap:"+dsrIpMap +
            					",typePortMapClusterA:"+typePortMapClusterA+",clusterAPortList:"+clusterAPortList +
            					",typePortMapClusterB:"+typePortMapClusterB+",clusterBPortList:"+clusterBPortList;
            
            //System.out.println(configInfo);
            logger.info(configInfo);
            
        }catch(Exception e){
            logger.log(Level.SEVERE, "Read Properties Failed", e);
        }finally{
            if(is != null){
                try{
                    is.close();
                }catch(Exception ignored){}
            }
        }
    }


	public static List<String> getDSRConfig(int type){
		List<String> config = new ArrayList<String>();
		
		int i = ipIndex.getAndIncrement();
		
		//Reset
		if(i == 99)
			ipIndex.set(0);
		
		if(i < abSwitchPoint){
        	config.add(dsrIpMap.get(CLUSTER_B_IP));
        	Integer port = typePortMapClusterB.get(type);
        	if(port == null){
        		if(counter >= clusterBPortList.size())
        			counter = 0;
        		port = clusterBPortList.get(counter++);
        	}
        	config.add(port.toString());
		}else{
        	config.add(dsrIpMap.get(CLUSTER_A_IP));
        	Integer port = typePortMapClusterA.get(type);
        	if(port == null){
        		if(counter >= clusterAPortList.size())
        			counter = 0;
        		port = clusterAPortList.get(counter++);
        	}
        	config.add(port.toString());
		}
				
		return config;
	}
	
	public static void main(String[] args)
	{
		List<String> config;
		for(int i=0; i<200; i++){
			config = DsrConfigure.getDSRConfig(6);
			System.out.println("IP: "+config.get(0)+", PORT: "+config.get(1));
		}
	}


}
