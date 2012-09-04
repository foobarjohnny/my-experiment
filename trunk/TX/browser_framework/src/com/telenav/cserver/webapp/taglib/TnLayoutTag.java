
/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.webapp.taglib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;

import com.telenav.cserver.browser.datatype.DeviceConfig;
import com.telenav.cserver.browser.datatype.DeviceScreenSize;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.browser.util.ClientHelper;
import com.telenav.cserver.browser.util.DeviceManager;
import com.telenav.cserver.browser.util.ReloadablePropertyResourceBundle;
import com.telenav.tnbrowser.util.DataHandler;
import java.util.MissingResourceException;

/**
 * @author pzhang
 *
 * @version 1.0, 2009-5-19
 */
public class TnLayoutTag extends BodyTagSupport{
    /**
     * "Y" = support local confige
     */
	private static Logger logger = Logger.getLogger(TnLayoutTag.class);
    private String supportLocal;
    private String supportDual;
    private static final long serialVersionUID = 1L;
    public StringBuffer outputText = new StringBuffer();
    public static final String COMMON_FILE = "BrowserCommon";
    public static final String CLIENT_ID_MAPPING_FILE = "ClientIdMapping";
    public static final String DEVICE_FOLDER = "device";
    public static final String COMMON_INAGE_INDICATOR = "$";
    public static final String SCREEN_LEVEL_COMMON_LAYOUT_FOLDER = "Common";
    public static final String SCREEN_LEVEL_IMAGE_PREFIX = "ScreenLevelImage:";
    
    private String imageUrl;
    private String imageCommonUrl;
    private String screenLevelImageUrl;
    private String screenLevelImageCommonUrl;
	private Map<String,String> tempCacheForLayouts = new HashMap<String,String>();
    private String locale = "en_US";
    private boolean isMissingLayoutFile;
    /**
     * 
     * @return
     */
    private String getLayoutFileName()
    {
        String layoutFile = "";
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        //get pageName from pageContext
        
        String servletPath = request.getServletPath();
        //String contextPath = request.getContextPath();
        String pageName = getJspName(servletPath);
        String pageFolderName = getJspFolderName(servletPath);
        if(!"".equals(pageFolderName))
        {
            layoutFile += pageFolderName + "." ;
        }
        layoutFile +=pageName;            
        return layoutFile;
    }
    
    /**
     * 
     */
    public int doStartTag() throws JspException
    {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        //DataHandler handler = new DataHandler(request);
        DataHandler handler = (DataHandler) request
        .getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
        //
        String filePath = "";
        String screenLevelCommonFile = "";
        String commonFile = COMMON_FILE;
        String layoutFile = getLayoutFileName();
 
        //get pageName from pageContext
        String requestURL = request.getRequestURL().toString();
        String servletPath = request.getServletPath();

        String hostURL = ClientHelper.getImageHost();
        if("".equals(hostURL))
        {
        	hostURL = ClientHelper.getHostURL(requestURL,servletPath);
        }
        
        String imageUrl = "";
        String imageCommonUrl = "";
        String screenLevelImageUrl = "";
        String screenLevelImageCommonUrl = "";
        
        String[] screenSize = getScreenKey(handler);
        String filePathScreenSize = "";
        
        setCurrentLocale(handler.getClientInfo(DataHandler.KEY_LOCALE));
        
        for(int i=0; i< screenSize.length; i++)
        {
        	filePathScreenSize = screenSize[i];
        	//check if we are using closest resulotion
        	DeviceConfig config = DeviceManager.getInstance().getDeviceConfig(handler);
        	if(config.isUsingClosingResulotion())
        	{
        		DeviceScreenSize device = new DeviceScreenSize();
        		device.setDeviceName(config.getClosestDeviceName());
        		device.setDeviceScreenSize();
        		
        		if(i==0)
        		{
        			filePathScreenSize = device.getWidth() + "x" + device.getHeight();
        		}
        		else
        		{
        			filePathScreenSize = device.getHeight() + "x" + device.getWidth();
        		}
        	}
            filePath = DEVICE_FOLDER + "." + getEnvironmentPathWithDevice(handler,filePathScreenSize,layoutFile, false);
            screenLevelCommonFile = DEVICE_FOLDER + "." + getEnvironmentPathWithDevice(handler,filePathScreenSize, layoutFile, true);
            String[] screenWidthHeight = screenSize[i].split("x");
            //imageUrl = hostURL + ClientHelper.getImageKeyWithoutSize(handler) + screenWidthHeight[0] + "x" + screenWidthHeight[1] + "/image/";
            //imageCommonUrl = hostURL + ClientHelper.getImageKeyWithoutSize(handler) + ClientHelper.getCommonImageFloder(handler) + "/image/";
            
            imageUrl = hostURL + config.getImageKeyWithoutSize() + filePathScreenSize + "/image/";
            imageCommonUrl = hostURL + config.getImageKeyWithoutSize() + ClientHelper.getCommonImageFloder(handler) + "/image/";
            screenLevelImageUrl = hostURL + coverKeyToPath(generateScreenLevelPath(handler, filePathScreenSize, false))+ "image/";
            screenLevelImageCommonUrl = hostURL + coverKeyToPath(generateScreenLevelPath(handler, filePathScreenSize, true))+ ClientHelper.getCommonImageFloder(handler) + "/image/";
            this.setImageUrl(imageUrl);
            this.setImageCommonUrl(imageCommonUrl);
            this.setScreenLevelImageUrl(screenLevelImageUrl);
            this.setScreenLevelImageCommonUrl(screenLevelImageCommonUrl);
            generateWholeLayout(screenWidthHeight[0],screenWidthHeight[1],filePath,layoutFile,commonFile,screenLevelCommonFile);
        }
        //
    
        try {
            pageContext.getOut().println(outputText.toString());
        } catch (IOException e) {
        }
        return EVAL_BODY_INCLUDE;
    }
    

    /**
     * get host from config file.
     * e.g for poi_service, it's {poi.http}
     * @return
     */
    private String getHost()
    {
    	return ClientHelper.getImageHost();
    }
    
    
    /**
     * get jsp name base on /WEB-INF/jsp/dsr/SpeakSearch1.jsp
     * @param servletPath
     * @return SpeakSearch1
     */
    private String getJspName(String servletPath)
    {
        String pageName = "";
        int position1 = servletPath.lastIndexOf("/");
        int position2 = servletPath.lastIndexOf(".");
        
        pageName = servletPath.substring(position1+1, position2);
        return pageName;
    }
    
    
    /**
     * get jsp name base on /WEB-INF/jsp/dsr/SpeakSearch1.jsp
     * @param servletPath
     * @return dsr
     * 
     * if foler like /WEB-INF/jsp/SearchSearch1.jsp return ""
     */
    public String getJspFolderName(String servletPath)
    {
        String pageFolderName = "";
        String[] temp = servletPath.split("/");
        if(temp == null || temp.length <= 4)
        {
        	pageFolderName = "";
        }
        else
        {
        	pageFolderName = temp[temp.length - 2];
        }
        
        return pageFolderName;
    }
    
    /**
     * 
     * @param handler
     * @param screenSize
     * @return
     * @throws JspException
     */
    private String getEnvironmentPathWithDevice(DataHandler handler,String screenSize, String layoutFile, boolean isCommonLayoutFile) throws JspException
    {
        String locale = handler.getClientInfo(DataHandler.KEY_LOCALE);
        //String key = ClientHelper.getLayoutKeyWithDevice(handler) + "." + screenSize; 
        String key = DeviceManager.getInstance().getDeviceConfig(handler).getLayoutKeyWithDevice() + "." + screenSize;
        if(isCommonLayoutFile)
        {
        	key = generateScreenLevelPath(handler, screenSize ,false);
        	
        }
        if("Y".equalsIgnoreCase(this.getSupportLocal()))
        {
        	key += "." + locale;
        }
        
        return key;
    }

    /**
     * generate layout files base on layout config
     * @param filePath
     * @param layoutFile
     */
    public void getSpecificLayout(String filePath,String layoutFile,boolean isScreenLevelImage)
    {
        //System.out.println("layoutFile name:" +  layoutFile);
        //generate layout file base on configure file
        try
        {
        	String locale[] = getCurrentLocale();
        	
        	ResourceBundle serverBundle =  ReloadablePropertyResourceBundle.getResourceBundle(filePath + "." + layoutFile,locale);
        	//ResourceBundle serverBundle =  ResourceBundle.getBundle(filePath + "." + layoutFile);
        	//no log or exception when BrowserCommon.properties is missing.
        	if(null == serverBundle && !layoutFile.startsWith(COMMON_FILE))
        	{
        		if(!isMissingLayoutFile)
        		{
        			//Output a warn log when common folder resource is missing
        			logger.warn("Can't find resource, " + filePath + "." + layoutFile + ", locale " + locale[0] + "_" + locale[1]);
        			isMissingLayoutFile = true;
        			return;
        			
        		}
        		else
        		{
        			//Throw an exception when both Common and specific device layout files are missing
        			throw new MissingResourceException("The resource is missing in both Common and specific device folder, name " + layoutFile + ", locale " + locale[0] + "_" + locale[1], this.getClass().toString(), layoutFile);
        		}
        	}
        	else if(null == serverBundle && layoutFile.startsWith(COMMON_FILE))
        	{
        		logger.warn("Can't find resource, " + filePath + "." + layoutFile + ", locale " + locale[0] + "_" + locale[1]);
        		return;
        	}
            Enumeration<String> enumeration = serverBundle.getKeys();
            String itemKey = "";
            String itemValue = "";
            String uiId = "";
            while(enumeration.hasMoreElements())
            {
            	itemKey = enumeration.nextElement();
            	itemValue = serverBundle.getString(itemKey).trim();
            	uiId = getUIId(itemKey);
            	if(uiId.equals("@include"))
                {
            		getSpecificLayout(filePath, itemValue, isScreenLevelImage);
                }
            	if(isScreenLevelImage && isImageAttribute(itemValue))
            	{
            		itemValue = SCREEN_LEVEL_IMAGE_PREFIX + itemValue;
            	}
            	tempCacheForLayouts.put(itemKey, itemValue);
            }
            

        }
        catch(Exception e)
        {	
			//add debug log if can't find reource
        	logger.debug(e.getMessage(),e);
        }
    }   
    
    /**
     * generate layout files base on layout config
     * @param filePath
     * @param layoutFile
     */
	public void generateLayout(Map<String,String> clientIdMaping)
    {
        //System.out.println("layoutFile name:" +  layoutFile);
        //generate layout file base on configure file
        try
        {
            String itemKey = "";
            List<String> keyList = new ArrayList<String>();
            
            keyList.addAll(tempCacheForLayouts.keySet());
            //sort the key
            Collections.sort(keyList);
            
            String uiId = "";
            String attributeId = "";
            String previousId = "";
            String itemValue = "";
            for(int i=0;i<keyList.size();i++)
            {
                itemKey = keyList.get(i);
                itemValue = tempCacheForLayouts.get(itemKey);
                uiId = getUIId(itemKey);
                attributeId = getAttributeId(itemKey);
                //System.out.println("ID:" + uiId + ",itemKey" + itemKey);
                
                if(!uiId.equals("") && !attributeId.equals(""))
                {
                    if(!uiId.equals(previousId))
                    {
                        //generate uicontrol
                        if(!previousId.equals(""))
                        {
                            outputText.append("</uicontrol>\n");
                        }
                        outputText.append("<uicontrol id=\"");
                        outputText.append(uiId);
                        outputText.append("\">\n");
                        //
                        previousId = uiId;
                    }
                    
                    if(isImageAttribute(itemValue))
                    {
                    	String clientId = clientIdMaping.get(getImageName(itemValue));
                    	if(clientId == null || "".equals(clientId.trim()))
                    	{
                    		itemValue = getImageFullUrl(itemValue);
                    	}
                    	else
                        {
                    		itemValue = clientId;
                        }
                    }
                    //generate uiattribute for uicontrol
                    outputText.append("<uiattribute key=\"");
                    outputText.append(attributeId);
                    outputText.append("\">");
                    outputText.append(itemValue);
                    outputText.append("</uiattribute>\n");
                    
                    if(i==keyList.size()-1)
                    {
                        outputText.append("</uicontrol>\n");
                    }
                    //System.out.println("" + itemKey + ":" + serverBundle.getString(itemKey));
                }
                    
            }
        }
        catch(Exception e)
        {	
			//add debug log if can't find reource
        	logger.debug(e.getMessage(),e);
        }
    }
    
    /**
     * 
     * @param itemValue
     * @return
     */
    private String getImageFullUrl(String itemValue)
    {
    	String fullUrl = "";
    	
		if(itemValue.startsWith(SCREEN_LEVEL_IMAGE_PREFIX))
		{
			itemValue = itemValue.substring(SCREEN_LEVEL_IMAGE_PREFIX.length());
			String commonImageName = getCommonImage(itemValue);
	    	if("".equals(commonImageName))
	    	{
	    		fullUrl = this.getScreenLevelImageUrl() + itemValue;
	    	}
	    	else
	    	{
	    		fullUrl = this.getScreenLevelImageCommonUrl() + commonImageName;
	    	}
		}
		else
		{
			String commonImageName = getCommonImage(itemValue);
	    	if("".equals(commonImageName))
	    	{
	    		fullUrl = this.getImageUrl() + itemValue;
	    	}
	    	else
	    	{
	    		fullUrl = this.getImageCommonUrl() + commonImageName;
	    	}
		}
    	return fullUrl;
    }
    
    /**
     * If it's common image, return the common image name, otherwise return ""
     * @param itemValue
     * @return
     */
    private String getCommonImage(String itemValue)
    {
    	String commonImage = "";
    	if(itemValue.startsWith(COMMON_INAGE_INDICATOR))
    	{
    		commonImage = itemValue.substring(COMMON_INAGE_INDICATOR.length());
    	}
    	
    	return commonImage;
    }

    /**
     * If it's common image, return the common image name, otherwise return ""
     * @param itemValue
     * @return
     */
    private String getImageName(String itemValue)
    {
    	String commonImage = itemValue;
    	if(itemValue.startsWith(COMMON_INAGE_INDICATOR))
    	{
    		commonImage = itemValue.substring(COMMON_INAGE_INDICATOR.length());
    	}
    	
    	return commonImage;
    }
    
    /**
     * check if the attribute is used for image
     * @param attribute
     * @return
     */
    private boolean isImageAttribute(String attribute)
    {
       boolean isImage = false;
       
       if(attribute.endsWith(".png"))
       {
           isImage = true;
       }
       return isImage;
    }
    
    /**
     * generate layout for one jsp screen
     * @param width:        screen widht
     * @param height        screen height
     * @param filePath      layout file path
     * @param layoutFile    layout file name
     * @param commonFile    common layout file name
     */
    public void generateWholeLayout(String width,String height,String filePath,String layoutFile,String commonFile,String screenLevelCommonFile)
    {
        String layoutWidth = width;
        String layoutHeight = height;
        
        //getting client id mapping
        Map<String,String> clientIdMaping = getClientImageIdMapping(filePath + "." + CLIENT_ID_MAPPING_FILE);
        //layout tag
        outputText.append("<layout height=\"");
        outputText.append(layoutHeight);
        outputText.append("\" width=\"");
        outputText.append(layoutWidth);
        outputText.append("\">\n");
        isMissingLayoutFile = false;
       	//get screen size level common layout out
        getSpecificLayout(screenLevelCommonFile,commonFile,true);
        getSpecificLayout(screenLevelCommonFile,layoutFile,true);
        //get device level common layout
        getSpecificLayout(filePath,commonFile,false);
        //get layout
        getSpecificLayout(filePath,layoutFile,false);
    	if(tempCacheForLayouts.isEmpty())
    	{
    		
    		logger.error("There is no layout file for this specific device!");
    		outputText.append("</layout>\n");
    		return;
    	}
    	else
    	{
    		//generate response TxNode
            generateLayout(clientIdMaping);
            outputText.append("</layout>\n");
    	}
       
        
        tempCacheForLayouts.clear();
        
        //System.out.println(outputText.toString());
    }
    
    private Map<String,String> getClientImageIdMapping(String mappingName)
    {
    	Map<String,String> map = new HashMap<String,String>();
    	
		try
		{
			ResourceBundle serverBundle = ReloadablePropertyResourceBundle.getBundle(mappingName);
			
			Enumeration<String> enumeration = serverBundle.getKeys();
            String itemKey = "";
            String itemValue = "";
            while(enumeration.hasMoreElements())
            {
            	itemKey = enumeration.nextElement();
            	itemValue = serverBundle.getString(itemKey);
            	
            	map.put(itemKey, itemValue);
            }
		     
		}
		catch(Exception e)
		{
			 
		}
    	return map;   	
    }
    
    /**
     * get the screen widthxheight key
     * if it's dual layout, return the widthxheight list
     * @param handler
     * @return
     * @throws JspException
     */
    private String[] getScreenKey(DataHandler handler) throws JspException
    {
        String width = "";
        String height = "";
        
        String width1 = handler.getClientInfo(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH);
        String height1 = handler.getClientInfo(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT);
        String width2 = handler.getClientInfo(DataHandler.KEY_WIDTH);
        String height2 = handler.getClientInfo(DataHandler.KEY_HEIGHT);
        
        if(width1 == null || width1.equals(""))
        {
            width = width2;
            height = height2;
        }
        else
        {
            width = width1;
            height = height1;
        }
        
        String[] key;
        String[] widths = width.split("-");
        String[] heights = height.split("-");

        if((widths.length != heights.length) || (widths.length <1))
        {
            throw new JspException("the screen width and height is not correct.");
        }
        
        int length = widths.length;
        if(widths.length == 4){
        	if(!"Y".equalsIgnoreCase(supportDual)){
        		length = 2;
        	}
        }
        key = new String[length];
        
        if(length == 2)
        {
        	int tempWidth = Integer.parseInt(widths[0]);
        	int tempHeight = Integer.parseInt(heights[0]);
        	
        	if(tempWidth <= tempHeight)
        	{
        		key[0] = widths[0] + "x" + widths[1];
        		key[1] = heights[0] + "x" + heights[1];
        	}
        	else
        	{
        		key[0] = heights[0] + "x" + heights[1];
        		key[1] = widths[0] + "x" + widths[1];       		
        	}
        }else if(length == 4){
        	int tempWidth1 = Integer.parseInt(widths[0]);
        	int tempHeight1 = Integer.parseInt(heights[0]);
        	int tempWidth2 = Integer.parseInt(widths[0]);
        	int tempHeight2 = Integer.parseInt(heights[0]);
        	if(tempWidth1 <= tempHeight1)
        	{
        		key[0] = widths[0] + "x" + heights[0];
        		key[1] = widths[1] + "x" + heights[1];
        	}
        	else
        	{
        		key[0] = widths[1] + "x" + heights[1];
        		key[1] = widths[0] + "x" + heights[0];       		
        	}
        	
        	if(tempWidth2 <= tempHeight2)
        	{
        		key[2] = widths[2] + "x" + heights[2];
        		key[3] = widths[3] + "x" + heights[3];
        	}
        	else
        	{
        		key[2] = widths[3] + "x" + heights[3];
        		key[3] = widths[2] + "x" + heights[2];
        	}
        }
        else
        {
	        for(int i=0; i<widths.length ; i++)
	        {
	            key[i] = widths[i] + "x" + heights[i];
	        }
        }
        
        return key;
    }
    
    /**
     * get the UI's id
     * @param itemKey
     * @return
     */
    private String getUIId(String itemKey)
    {
        String id= "";
        String[] temp = itemKey.split("\\.");

        if(temp != null && temp.length >=1)
        {
            id = temp[0];
        }
        return id;
    }
    
    /**
     * get UI's attribute:
     * e.g
     * x
     * y
     * width
     * height
     * fontSize
     * ...
     * @param itemKey
     * @return
     */
    private String getAttributeId(String itemKey)
    {
        String attribute= "";
        String[] temp = itemKey.split("\\.");

        if(temp != null && temp.length >=2)
        {
            attribute = temp[1];
        }
        return attribute;
    }
    
    public int doEndTag() throws JspException 
    {   
        this.clean();
        return EVAL_PAGE;
    }
    
    public int doAfterBody() throws JspException
    {
        return SKIP_BODY;
    }    
    
    public void clean()
    {
        outputText = new StringBuffer();
        setSupportLocal("");
        setSupportDual("");
        setImageUrl("");
        setImageCommonUrl("");
    }
    
    /**
     * 
     * @param args
     */
    public static void main(String args[]) {
    	String servletPath = "/WEB-INF/jsp/dsr/SpeakSearch1.jsp";
        String[] temp = servletPath.split("/");
        for(int i=0;i< temp.length;i++)
        {
        	System.out.println(temp[i]);
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSupportLocal() {
        return supportLocal;
    }

    public void setSupportLocal(String supportLocal) {
        this.supportLocal = supportLocal;
    }

	public String getSupportDual() {
		return supportDual;
	}

	public void setSupportDual(String supportDual) {
		this.supportDual = supportDual;
	}

	public String getImageCommonUrl() {
		return imageCommonUrl;
	}

	public void setImageCommonUrl(String imageCommonUrl) {
		this.imageCommonUrl = imageCommonUrl;
	}
	
	private void setCurrentLocale(String locale)
	{
		if(null != locale && locale.length() != 0)
		{
			this.locale = locale;
		}
	}
	
	
	private String[] getCurrentLocale()
	{
		String[] localeArray;
		if(this.locale.indexOf("_") != -1 && !this.locale.endsWith("_"))
		{
			localeArray = this.locale.split("_");
		}
		else
		{
			localeArray = new String[]{"en","US"};
		}
		
		return localeArray;
	}
	
    public String getScreenLevelImageUrl() {
		return screenLevelImageUrl;
	}

	public void setScreenLevelImageUrl(String screenLevelImageUrl) {
		this.screenLevelImageUrl = screenLevelImageUrl;
	}

	public String getScreenLevelImageCommonUrl() {
		return screenLevelImageCommonUrl;
	}

	public void setScreenLevelImageCommonUrl(String screenLevelImageCommonUrl) {
		this.screenLevelImageCommonUrl = screenLevelImageCommonUrl;
	}
	
	private String generateScreenLevelPath(DataHandler handler, String screenSize, boolean isCommonImageFolder)
	{
		String strPath = SCREEN_LEVEL_COMMON_LAYOUT_FOLDER + "." + handler.getClientInfo(DataHandler.KEY_PLATFORM) + "." + handler.getClientInfo(DataHandler.KEY_VERSION).replace('.', '_') + "." + ClientHelper.getDefaultDeviceName(handler);
		if(!isCommonImageFolder)
		{
			strPath += "." + screenSize;
		}
		return strPath;
	}
	
	private String coverKeyToPath(String key)
	{
		return "/" + key.replace('.', '/') + "/";
	}
}
