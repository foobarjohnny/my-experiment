package com.telenav.cserver.framework.html.util;

import java.util.PropertyResourceBundle;
import org.apache.log4j.Logger;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;

/**
 * 
 * @author mmli
 * @version 1.0 2011-09-20
 * 
 * 	Some HTML pages couldn't display well in several HTC devices in landscape, while they works well in other same resolution devices.
 * 	This should be device issue, but we can't wait the device manufacture to fix it. To avoid affect other devices, more important, to 
 *  keep consistent of all devices, we add special device judgment logic here. If a device is special, you can add configuration under
 *  HtmlClientHelper.SPECIAL_DEVICE_FOLDER_NAME + "/" + programCode + "/" + platform + "/" + version  + "/" + product  + "/" + device, 
 *  to control it. 
 *  
 *  This also can be used to handle some other special device issue later if necessary. 
 *   
 */
public class HtmlSpecialDeviceHelper {
	private static String fName = "device";
	private static String resolutionSupport = "resolutionSupport";
	private static HtmlSpecialDeviceHelper instance = new HtmlSpecialDeviceHelper(); 
	private static Logger logger = Logger.getLogger(HtmlSpecialDeviceHelper.class);
	/**
	 * 
	 * @return
	 */
	public static HtmlSpecialDeviceHelper getInstance()
	{
		return instance;
	}
	
	
	public static boolean isSpecialDevice(HtmlClientInfo clientInfo,String key)
	{
    	String programCode = clientInfo.getProgramCode();
        String platform = clientInfo.getPlatform();
        String version = HtmlClientHelper.getVersion(clientInfo.getVersion());
        String device = clientInfo.getDevice();
        String product = HtmlClientHelper.getProduct(clientInfo.getProduct());
        
    	String filePath = HtmlClientHelper.SPECIAL_DEVICE_FOLDER_NAME + "/" + 
    									programCode + "/" + 
    									platform + "/" + 
    									version  + "/" + 
    									product  + "/" + 
    									device + "/" + 
    									fName;
    	logger.debug("special file path : " + filePath);     
    	
	  	boolean isSpecialDevice = false;
		boolean isSupportResolution = false;
    	
    	try
        {
            PropertyResourceBundle bundle = (PropertyResourceBundle) PropertyResourceBundle.getBundle(filePath);  
            String resolutionSupportList = HtmlCommonUtil.getString(bundle.getString(resolutionSupport));
            if(resolutionSupportList != null){
            	String width = clientInfo.getWidth();
            	String height = clientInfo.getHeight();
                String[] resolutionSupportArray = resolutionSupportList.split("#");	
                for(int i = 0; i < resolutionSupportArray.length; i++){
                	String[] res = resolutionSupportArray[i].split("x");
                	if(res.length > 1){
                		if((width.equalsIgnoreCase(res[0]) && height.equalsIgnoreCase(res[1])) || (height.equalsIgnoreCase(res[0]) && width.equalsIgnoreCase(res[1]))){
                			isSupportResolution = true;
                			break;
                		}
                	}
                }
            }
            
            if(!isSupportResolution){
            	return isSpecialDevice;
            }
        	    
            String value = HtmlCommonUtil.getString(bundle.getString(key));
            if(value != null && value.equalsIgnoreCase("true"))
            {
            	isSpecialDevice = true;
            }
        }
        catch(Exception e)
        {
        	logger.debug("error during read special device configuration:" + e.getMessage());
        }
        return isSpecialDevice;
	}
}
