/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.ext;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Category;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.telenav.cserver.common.resource.ResourceLoader;

/**
 * XML config Loader, to load attributes and return as org.w3c.dom.Element Object
 * 
 * @author yqchen
 * @version 1.0 2007-2-6 9:42:59
 */
public class XmlResourceLoader implements ResourceLoader
{
	Category logger = Category.getInstance(getClass());
	
	/**
	 * load resource 
	 * 
	 * @param path
	 * @param objectName
	 * @return Object, Map for properties, Element for XML
	 */
	public Object loadResource(String path, String objectName)
	{
		
		if(!path.endsWith(".xml"))
		{
			//append file suffix automatically
			path = path.concat(".xml");
		}
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Loading resource:" + path );
		}
		
		
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream is = null;
		try
		{
			//		is = XmlResourceLoader.class.getResourceAsStream(path);	           
//			is = cl.getResourceAsStream(path);
			URL url = cl.getResource(path);
			if(url == null)
			{
				logger.warn("File not found:" + path);
				return null;
			}
			
			if (logger.isDebugEnabled())
	        {
	            logger.debug("AbsoluteFilePath:" + path);
	        }
			
			String absoluteFilePath = url.getPath();
			
			try {
				FileInputStream fis = new FileInputStream(absoluteFilePath);
				is = new BufferedInputStream(fis);
			} catch (FileNotFoundException e) {				
			}
			
			
			Document doc = factory.newDocumentBuilder().parse(is);

			Element root = doc.getDocumentElement();

			return root;
		} catch (Exception e)
		{
			// config file not existes or invalid format
//			System.out.println("config file not existes or invalid format:" + path);
//			e.printStackTrace();
			//logger.error("config file not existes or invalid format:" + path, e);
			logger.warn("config file not existes or invalid format:" + path 
					+ ", " + e.getMessage());
		}
		finally
		{
			if(is != null)
        	{
	            try
	            {
	                is.close();
	            }
	            catch (Exception e)
	            {
	            }
        	}
		}

		return null;

	}
}