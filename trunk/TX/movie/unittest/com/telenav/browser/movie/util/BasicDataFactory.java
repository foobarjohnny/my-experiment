package com.telenav.browser.movie.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.PropertyResourceBundle;

import org.apache.log4j.Logger;
import org.json.me.JSONObject;
import com.telenav.browser.movie.datatypes.Address;

public class BasicDataFactory {
	private static Logger logger = Logger.getLogger(BasicDataFactory.class);
	
	private static String initFilePath = "config/unittest/PropertyFilePathMap.properties";
	private static HashMap<String,PropertyResourceBundle> resourceBundleMap = new HashMap<String,PropertyResourceBundle>();
	private static HashMap<String,String> propertyFilePathMap = new HashMap<String,String>();
	
	static{
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream stream = cl.getResourceAsStream(initFilePath);
		try {
			PropertyResourceBundle propertyResourceBundle = new PropertyResourceBundle(stream);
			for(String key:propertyResourceBundle.keySet()){
				propertyFilePathMap.put(key, propertyResourceBundle.getString(key));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private PropertyResourceBundle getPropertyResourceBundle(String propertyFileType){
		PropertyResourceBundle propertyResourceBundle = null;
		if(resourceBundleMap.containsKey(propertyFileType)){
			propertyResourceBundle = resourceBundleMap.get(propertyFileType);
		}else{
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			InputStream stream = cl.getResourceAsStream(propertyFilePathMap.get(propertyFileType));
			try {
				propertyResourceBundle = new PropertyResourceBundle(stream);
				resourceBundleMap.put(propertyFileType, propertyResourceBundle);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("your propertyFileType:"+propertyFileType+ " is not register in file "+initFilePath);
			}
		}
		return propertyResourceBundle;
	}
	
	private String getStringFromConfig(String propertyFileType,String key) throws Exception{
		PropertyResourceBundle propertyResourceBundle = getPropertyResourceBundle(propertyFileType);
		if(propertyResourceBundle!=null && propertyResourceBundle.containsKey(key)){
			return propertyResourceBundle.getString(key);
		}else{
			throw new Exception("can not get String from property file,the propertyFileType is "+propertyFileType);
		}
	}
	
	public Object getObjectFromConfig(String propertyFileType,String key, Class clazz) throws Exception{	
		String objectString = getStringFromConfig(propertyFileType,key);
		if(clazz.equals(Address.class)){
			JSONObject jsonObject = new JSONObject(objectString);
			Address address = new Address();
			address.makeFrom(jsonObject);
			return address;
		}else if(clazz.equals(Integer.class)){
			return Integer.parseInt(objectString);
		}else if(clazz.equals(Boolean.class)){
			return Boolean.parseBoolean(objectString);
		}else if(clazz.equals(String.class)){
			return objectString;
		}else{
			return null;
		}
	}
}
