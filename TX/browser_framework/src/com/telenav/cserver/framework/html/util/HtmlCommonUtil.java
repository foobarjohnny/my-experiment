package com.telenav.cserver.framework.html.util;

import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.util.threadpool.ThreadPool;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.telenav.client.dsm.ContextMgrService;
import com.telenav.client.dsm.ContextMgrStatus;
import com.telenav.client.dsm.Error;
import com.telenav.cserver.framework.cli.LogDeploymentInfo;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.kernel.sso.SsoToken;
import com.telenav.kernel.sso.SsoTokenManager;
import com.telenav.kernel.util.datatypes.TnContext;

public class HtmlCommonUtil {
	
	public static final double DEGREE_MULTIPLIER = 1.e5; // 1e-5 deg units
	private final static String STATICRESOURCE_URL = "STATICRESOURCE_URL";
	private final static String APPSTORE_URL = "APPSTORE_URL";
	private final static String FACEBOOK_URL = "FACEBOOK_URL";
	
	private static Logger logger = Logger.getLogger(HtmlCommonUtil.class);
	
	/**
	 * Get URL for static resource server
	 * @param hostUrl
	 * @return
	 */
	public static String getStaticResourceServerURL(String hostUrl){
		return getServiceLocatorUrl(hostUrl, STATICRESOURCE_URL);
	}
	/**
	 * getCheckOutURL
	 * @param hostUrl
	 * @return
	 */
	public static String getCheckOutURL(String hostUrl)
	{
		return getServiceLocatorUrl(hostUrl, APPSTORE_URL);
	}
	/**
	 * getFacebookUrl
	 * @param hostUrl
	 * @return
	 */
	public static String getFacebookUrl(String hostUrl)
	{
		return getServiceLocatorUrl(hostUrl, FACEBOOK_URL);
	}
	/**
	 * Get URL from service locator based on URL key
	 * @param hostUrl
	 * @param key
	 * @return URL
	 */
	private static String getServiceLocatorUrl(String hostUrl, String key){
		String url = HtmlServiceLocator.getInstance().getServiceUrl(
				hostUrl, key);
		return url;	
	}
	
	/**
	 * Prepend version to static content URL.
	 * @param url
	 * @return
	 */
	public static String addVersionToStaticContentUrl(String url){
		if(StringUtils.isEmpty(url)){ return "";}
		//read in static resource host from resource configuration and prepend it to the url.
		
		String version = LogDeploymentInfo.getVersion();
		String versionPathEle = (StringUtils.isEmpty(version)?"":"/" + version.replace(".", "_")) ;
		if(!"/".equalsIgnoreCase(url.charAt(0)+"")){
			versionPathEle = versionPathEle + "/";
		}
		StringBuilder sb = new StringBuilder();
		return sb.append(versionPathEle).append(url).toString();
	}
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String getString(String string)
	{
		if(string == null) return "";
		
		return string.trim();
	}
    /**
     * 
     * @param s
     * @return
     */
    public static String filterLastPara(String s)
    {
    	if("".equals(getString(s))) return "";
    	
    	String[] list = s.split(";");
    	if(list != null)
    	{
    		return list[0];
    	}
    	else
    	{
    		return "";
    	}
    }
    
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static int convertToInt(String string)
	{
		int i = 0;
		if(!"".equals(getString(string)))
		{
			try{
				i = Integer.parseInt(string);
			}catch(NumberFormatException e){
				logger.error("Parsing error when converting to int: [" + string + "]");
			}
		}
		return i;
	}
	
    public static int convertToDM5(double degree) {
        return (int) (degree * DEGREE_MULTIPLIER);
    }
    
    public static ConfigurationContext getWSContext() throws AxisFault {
        int soTimeout = 30000;
        int connectionTimeout = 4000;
        int poolSizePerHost = 10;
        int maximumPoolSize = 30;
        int minimumPoolSize = 10;
    	
        ConfigurationContext configContext = ConfigurationContextFactory
            .createConfigurationContextFromFileSystem(null, null);

        // create the multi-threaded HTTP manager & the client with it
        MultiThreadedHttpConnectionManager mgr = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams connectionManagerParams = new HttpConnectionManagerParams();
         connectionManagerParams.setDefaultMaxConnectionsPerHost(poolSizePerHost);
        connectionManagerParams.setTcpNoDelay(true);
        connectionManagerParams.setStaleCheckingEnabled(true);
        connectionManagerParams.setLinger(0);
        
        // set the timeouts
         connectionManagerParams.setConnectionTimeout(connectionTimeout); // in milliseconds
         connectionManagerParams.setSoTimeout(soTimeout); // in milliseconds     

        mgr.setParams(connectionManagerParams);
        HttpClient client = new HttpClient(mgr);
        // cache the HTTP client
        configContext.setProperty(HTTPConstants.REUSE_HTTP_CLIENT, Boolean.TRUE);
        configContext.setProperty(HTTPConstants.CACHED_HTTP_CLIENT, client);
        // set the thread pool
        configContext.setThreadPool(new ThreadPool(minimumPoolSize, maximumPoolSize));
        return configContext;
    }
	
    public static String getUrlString(String s)
    {
    	String temp =s;
		temp = temp.replaceAll("%", "%25");
		temp = temp.replaceAll("&", "%26");
		temp = temp.replaceAll("=", "%3D");
		temp = temp.replaceAll(" ", "%20");
		temp = temp.replaceAll("#", "%23");
    	return temp;
    }
    
	public static String geJSString(String input)
	{
		if (input == null) {
		   return input;
		  }
		StringBuilder filtered = new StringBuilder(input.length());
		  char prevChar = '\u0000';
		  char c;
		  for (int i = 0; i < input.length(); i++) {
		   c = input.charAt(i);
		   if (c == '"') {
		filtered.append("\\\"");
		   }
		   else if (c == '\'') {
		filtered.append("\\'");
		   }
		   else if (c == '\\') {
		filtered.append("\\\\");
		   }
		   else if (c == '\t') {
		filtered.append("\\t");
		   }
		   else if (c == '\n') {
		if (prevChar != '\r') {
		 filtered.append("\\n");
		    }
		   }
		   else if (c == '\r') {
		filtered.append("\\n");
		   } else if (c == '\f') {
		filtered.append("\\f");
		  } else if (c == '/') {
		filtered.append("\\/");
		            }
		   else {
		    filtered.append(c);
		   }
		   prevChar = c;
		  }
	  return filtered.toString();
	}
	
	public static String getStringFromMapKey(Map map, String key, String defaultValue){
		String str = (String)map.get(key);
    	str = HtmlCommonUtil.getString(str);
    	
    	if("".equals(str))
    	{
    		str = defaultValue;
    	}
    	
    	return str;
	}
	
	/**
	 * long[0] = userId
	 * long[1] = expiredTime
	 * @param userToken
	 * @return
	 */
	public static long[] getUserIdAndExpired(String userToken)
	{
		SsoToken ssoToken = SsoTokenManager.parseToken(userToken);
		if(ssoToken != null)
		{
			long userId = ssoToken.getUserId();
			long expiredTime = ssoToken.getExpireTime();
			return new long[]{userId, expiredTime};
		}
		return new long[0];
	}
	
	/**
	 * @Deprecated
	 * @param userToken
	 * @return
	 */
	@Deprecated
	public static String getUserId(String userToken)
	{
		String userId = "";
		long luserid = SsoTokenManager.extractUserIdFromToken(userToken);
		userId = String.valueOf(luserid);
		logger.info("get user id:" + userId + " from ssoToken: " + userToken);
		return userId;
	}
	
	public static String getSsoToken(long userId)
	{
		String ssoToken = SsoTokenManager.createToken(userId).toString();
		logger.info("ssoToken:" + ssoToken);
		return ssoToken;
	}
	
    public static String getMapProvider(HtmlClientInfo clientInfo)
    {
        TnContext tc = getTnContext(clientInfo);
        String mapDataSet = getString(tc.getProperty(TnContext.PROP_MAP_DATASET));
        if("".equals(mapDataSet))
        {
        	mapDataSet = "Navteq";
        }
        return mapDataSet;
    }
    
	/**
     * 
     * @param handler
     * @return
     */
    public static TnContext getTnContext(HtmlClientInfo clientInfo)
    {
        TnContext tc = new TnContext();
        tc.addProperty(TnContext.PROP_CARRIER , clientInfo.getCarrier());
        tc.addProperty(TnContext.PROP_DEVICE , clientInfo.getDevice());
        tc.addProperty(TnContext.PROP_PRODUCT , clientInfo.getPlatform());
        tc.addProperty(TnContext.PROP_VERSION , clientInfo.getVersion());
        tc.addProperty("program_code" , clientInfo.getProgramCode());
        tc.addProperty("locale" , clientInfo.getLocale());
        tc.addProperty("region" , clientInfo.getRegion());

        ContextMgrService cms = new ContextMgrService();
        cms.updateContext(tc);
        return tc;
    }
    
	/**
     * 
     * @param handler
     * @return
     */
    public static TnContext getTnContext(HtmlClientInfo clientInfo, String userToken )
    {
        TnContext tc = new TnContext();
        String mapDataSet = "Navteq";   
       
        long lUserID = Long.parseLong(getUserId(userToken));

        tc.addProperty(TnContext.PROP_CARRIER , clientInfo.getCarrier());
        tc.addProperty(TnContext.PROP_DEVICE , clientInfo.getDevice());
        tc.addProperty(TnContext.PROP_PRODUCT , clientInfo.getPlatform());
        tc.addProperty(TnContext.PROP_VERSION , clientInfo.getVersion());
        tc.addProperty("program_code" , clientInfo.getProgramCode());
        tc.addProperty("locale" , clientInfo.getLocale());
        tc.addProperty("region" , clientInfo.getRegion());

        ContextMgrService cms = new ContextMgrService();
        ContextMgrStatus myStatus = cms.registerContext(lUserID, "BROWSER-SERVER", tc);

        if(myStatus == null || myStatus.getStatusCode() != Error.NO_ERROR)
        {
            tc.addProperty(TnContext.PROP_MAP_DATASET, mapDataSet);
        }
        
        return tc;
    }
    
	public static String useNativeBrowser(String url)
	{
		String newUrl = "";
		if("" != url)
		{
			if(url.indexOf("?") == -1)
			{
				newUrl = url + "?nativebrowser=true";
			}
			else
			{
				newUrl = url + "&nativebrowser=true";
			}
		}
		
		return newUrl;
	}
	
	
	/**
	 * 
	 * @param platform
	 * @return
	 */
	public static boolean isAndroid(String platform)
	{
		if("ANDROID".equalsIgnoreCase(platform))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 
	 * @param platform
	 * @return
	 */
	public static boolean isIphone(String platform)
	{
		if("IPHONE".equalsIgnoreCase(platform))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
