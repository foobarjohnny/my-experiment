package com.telenav.cserver.framework.html.util;

import org.apache.log4j.Logger;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;

/**
 * 
 * @author panzhang
 * @version 1.0 2010-12-09
 */
public class HtmlClientInfoFactory {
	private static HtmlClientInfoFactory instance = new HtmlClientInfoFactory(); 
	private Logger logger = Logger.getLogger(HtmlClientInfoFactory.class);
	/**
	 * 
	 * @return
	 */
	public static HtmlClientInfoFactory getInstance()
	{
		return instance;
	}
	
	public String buildClientInfoString(HtmlClientInfo info)
	{
		JSONObject jo = new JSONObject();
    	try{
    		
    		jo.put(HtmlFrameworkConstants.CLIENT_INFO_KEY_PROGRAMECODE, info.getProgramCode());
        	//jo.put(HtmlFrameworkConstants.CLIENT_INFO_KEY_DEVICECARRIER, HtmlCommonUtil.getUrlString(info.getCarrier()));
        	jo.put(HtmlFrameworkConstants.CLIENT_INFO_KEY_PLATFORM, info.getPlatform());
        	jo.put(HtmlFrameworkConstants.CLIENT_INFO_KEY_VERSION, info.getVersion());
        	jo.put(HtmlFrameworkConstants.CLIENT_INFO_KEY_PRODUCTTYPE, info.getProduct());
        	//jo.put(HtmlFrameworkConstants.CLIENT_INFO_KEY_DEVICE, info.getDevice());
        	jo.put(HtmlFrameworkConstants.CLIENT_INFO_KEY_LOCALE, info.getLocale());
        	//jo.put(HtmlFrameworkConstants.CLIENT_INFO_KEY_BUILDNUMBER, info.getBuildNo());
    	}catch (JSONException e) {
    		e.printStackTrace();
		}
    	
    	StringBuilder sb = new StringBuilder();
    	sb.append(jo.toString())
    		.append("&" + HtmlFrameworkConstants.CLIENT_INFO_KEY_WIDTH)
    		.append("=" + info.getWidth())
    		.append("&" + HtmlFrameworkConstants.CLIENT_INFO_KEY_HEIGHT)
    		.append("=" + info.getHeight());
    	
    	String str = sb.toString();
    	return str;
	}

	/**
	 * input=ATT,ANDROID,6.2.01,ATT,480x320_320x480,320,480,en_US
	 * @param info
	 * @return
	 */
	public HtmlClientInfo build(String info,String width, String height, String ssoToken)
	{
		HtmlClientInfo clientInfo = new HtmlClientInfo();
		if(info.equals(""))
		{
			clientInfo.setProgramCode("SNNAVPROG");
			clientInfo.setCarrier("SprintPCS");
			clientInfo.setPlatform("ANDROID");
			clientInfo.setVersion("7.1.0");
			clientInfo.setProduct("SN_prem");
			clientInfo.setDevice("genericTest");
			clientInfo.setWidth("480");
			clientInfo.setHeight("800");
			clientInfo.setBuildNo("7101213");
			clientInfo.setLocale("en_US");
			clientInfo.setRegion("US");
			//clientInfo.setPtn("");
			clientInfo.setUserId("3707312");
			logger.debug("Used default value, should pass value from client");
		}
		else
		{
			try {
				JSONObject jo = new JSONObject(info);
				clientInfo.setProgramCode(jo.optString(HtmlFrameworkConstants.CLIENT_INFO_KEY_PROGRAMECODE));
				clientInfo.setDeviceCarrier(jo.optString(HtmlFrameworkConstants.CLIENT_INFO_KEY_DEVICECARRIER));
				clientInfo.setPlatform(jo.optString(HtmlFrameworkConstants.CLIENT_INFO_KEY_PLATFORM));
				clientInfo.setVersion(jo.optString(HtmlFrameworkConstants.CLIENT_INFO_KEY_VERSION));
				clientInfo.setProduct(jo.optString(HtmlFrameworkConstants.CLIENT_INFO_KEY_PRODUCTTYPE));
				clientInfo.setDevice(jo.optString(HtmlFrameworkConstants.CLIENT_INFO_KEY_DEVICE));
				clientInfo.setBuildNo(jo.optString(HtmlFrameworkConstants.CLIENT_INFO_KEY_BUILDNUMBER));
				String locale = HtmlCommonUtil.getString(jo.optString(HtmlFrameworkConstants.CLIENT_INFO_KEY_LOCALE));
				String region = HtmlCommonUtil.getString(jo.optString(HtmlFrameworkConstants.CLIENT_INFO_KEY_REGION));
				if("".equalsIgnoreCase(locale))
				{
					locale = "en_US";
				}
				if("".equalsIgnoreCase(region))
				{
					region = "US";
				}
				if("".equalsIgnoreCase(width))
				{
					width = "480";
				}
				if("".equalsIgnoreCase(height))
				{
					height = "800";
				}
				clientInfo.setLocale(locale);
				clientInfo.setRegion(region);
				//clientInfo.setPtn("");
				clientInfo.setWidth(width);
				clientInfo.setHeight(height);
				//get deviceCarrier mapping here
				clientInfo.setCarrier(HtmlClientHelper.getDeviceCarrierMapping(clientInfo));
				clientInfo.setUserId(HtmlCommonUtil.getUserId(ssoToken));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//set supported device
//		int iRealWidth = 800;
//		int iRealHeight = 480;
//		try
//		{
//			int iWidth = Integer.parseInt(clientInfo.getWidth());
//			int iHeight = Integer.parseInt(clientInfo.getHeight());
//			iRealWidth = Math.max(iWidth, iHeight);
//			iRealHeight = Math.min(iWidth, iHeight);
//		}
//		catch(Exception e)
//		{
//			
//		}
//		
//		String supportedScreenWidth = iRealWidth + "-" + iRealHeight;
//		String supportedScreenHeight = iRealHeight + "-" + iRealWidth;
//		clientInfo.setSupportedScreenWidth(supportedScreenWidth);
//		clientInfo.setSupportedScreenHeight(supportedScreenHeight);
		
		return clientInfo;
	}
}
