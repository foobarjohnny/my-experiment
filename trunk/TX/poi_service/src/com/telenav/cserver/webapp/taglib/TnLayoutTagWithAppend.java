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

import com.telenav.cserver.browser.datatype.DeviceConfig;
import com.telenav.cserver.browser.datatype.DeviceScreenSize;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.browser.util.ClientHelper;
import com.telenav.cserver.browser.util.DeviceManager;
import com.telenav.cserver.browser.util.ReloadablePropertyResourceBundle;
import com.telenav.tnbrowser.util.DataHandler;

public class TnLayoutTagWithAppend extends BodyTagSupport {

	  /**
     * "Y" = support local confige
     */
    private String supportLocal;
    private String supportDual;
    private static final long serialVersionUID = 1L;
    public StringBuffer outputText = new StringBuffer();
    public static final String COMMON_FILE = "BrowserCommon";
    public static final String CLIENT_ID_MAPPING_FILE = "ClientIdMapping";
    public static final String DEVICE_FOLDER = "device";
    public static final String COMMON_INAGE_INDICATOR = "$";
    private String imageUrl;
    private String imageCommonUrl;
    private String includeFiles;
    //add new locale support
    private String locale = "en_US";
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
        System.out.println("layoutFile " + layoutFile);
        System.out.println("servletPath " + servletPath);
        return layoutFile;
    }
    
    /**
     * 
     * @return
     */
    private String getIncludeFileName(String includeFilePath)
    {
        String includeFileName = "";
        includeFileName= includeFilePath.replace('/', '.');
        return includeFileName;
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
            filePath = DEVICE_FOLDER + "." + getEnvironmentPathWithDevice(handler,filePathScreenSize);
            String[] screenWidthHeight = screenSize[i].split("x");
            //imageUrl = hostURL + ClientHelper.getImageKeyWithoutSize(handler) + screenWidthHeight[0] + "x" + screenWidthHeight[1] + "/image/";
            //imageCommonUrl = hostURL + ClientHelper.getImageKeyWithoutSize(handler) + ClientHelper.getCommonImageFloder(handler) + "/image/";
            
            imageUrl = hostURL + config.getImageKeyWithoutSize() + filePathScreenSize + "/image/";
            imageCommonUrl = hostURL + config.getImageKeyWithoutSize() + ClientHelper.getCommonImageFloder(handler) + "/image/";
            this.setImageUrl(imageUrl);
            this.setImageCommonUrl(imageCommonUrl);
            generateWholeLayout(screenWidthHeight[0],screenWidthHeight[1],filePath,layoutFile,commonFile);           
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
    private String getEnvironmentPathWithDevice(DataHandler handler,String screenSize) throws JspException
    {
        String locale = handler.getClientInfo(DataHandler.KEY_LOCALE);
        //String key = ClientHelper.getLayoutKeyWithDevice(handler) + "." + screenSize; 
        String key = DeviceManager.getInstance().getDeviceConfig(handler).getLayoutKeyWithDevice() + "." + screenSize;
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
    public void generateLayout(String filePath,String layoutFile,Map<String,String> clientIdMaping)
    {
        //generate layout file base on configure file
        try
        {
        	String locale[] = getCurrentLocale();
        	ResourceBundle serverBundle =  ReloadablePropertyResourceBundle.getResourceBundle(filePath + "." + layoutFile, locale);
        	//ResourceBundle serverBundle =  ResourceBundle.getBundle(filePath + "." + layoutFile);
            Enumeration<String> enumeration = serverBundle.getKeys();
            String itemKey = "";
            List<String> keyList = new ArrayList<String>();
            while(enumeration.hasMoreElements())
            {
                keyList.add(enumeration.nextElement());
            }
            
            //sort the key
            Collections.sort(keyList);
            
            String uiId = "";
            String attributeId = "";
            String previousId = "";
            String itemValue = "";
            for(int i=0;i<keyList.size();i++)
            {
                itemKey = keyList.get(i);
                itemValue = serverBundle.getString(itemKey).trim();
                uiId = getUIId(itemKey);
                attributeId = getAttributeId(itemKey);
                //System.out.println("ID:" + uiId + ",itemKey" + itemKey);
                
                if(uiId.equals("@include"))
                {
                    //if have include file, read the include file layout
                    generateLayout(filePath,itemValue,clientIdMaping);
                }
                else if(!uiId.equals("") && !attributeId.equals(""))
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
            e.printStackTrace();
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
    	String commonImageName = getCommonImage(itemValue);
    	if("".equals(commonImageName))
    	{
    		fullUrl = this.getImageUrl() + itemValue;
    	}
    	else
    	{
    		fullUrl = this.getImageCommonUrl() + commonImageName;
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
    public void generateWholeLayout(String width,String height,String filePath,String layoutFile,String commonFile)
    {
        String layoutWidth = width;
        String layoutHeight = height;
        
        String includeFile = getIncludeFileName(includeFiles);
        
        //getting client id mapping
        Map<String,String> clientIdMaping = getClientImageIdMapping(filePath + "." + CLIENT_ID_MAPPING_FILE);
        //layout tag
        outputText.append("<layout height=\"");
        outputText.append(layoutHeight);
        outputText.append("\" width=\"");
        outputText.append(layoutWidth);
        outputText.append("\">\n");
        
        //get common layout
        generateLayout(filePath,commonFile,clientIdMaping);
        //get layout
        generateLayout(filePath,layoutFile,clientIdMaping);
        //get include layouts
        generateLayout(filePath,includeFile,clientIdMaping);
        
        outputText.append("</layout>\n");
           
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
        		key[0] = widths[0] + "x" + heights[0];
        		key[1] = widths[1] + "x" + heights[1];
        	}
        	else
        	{
        		key[0] = widths[1] + "x" + heights[1];
        		key[1] = widths[0] + "x" + heights[0];       		
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

	public String getIncludeFiles() {
		return includeFiles;
	}

	public void setIncludeFiles(String includeFiles) {
		this.includeFiles = includeFiles;
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
}
