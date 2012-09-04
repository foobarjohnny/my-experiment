<%@page import="java.util.PropertyResourceBundle"%>
<%@page import="com.telenav.cserver.browser.datatype.MessageWrap"%>
<%@page import="com.telenav.cserver.browser.util.MessageHelper"%>
<%@page import="com.telenav.cserver.browser.util.FeatureManager"%>
<%@page import="com.telenav.cserver.browser.framework.BrowserFrameworkConstants"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>
<%@page import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>
<%@page import="com.telenav.tnbrowser.util.IFeatureManager"%>
<%@page import="com.telenav.cserver.browser.util.ClientHelper"%>
<%
    //String host = (String) request.getAttribute("Host_url"); 
    //String pageType = "net";
	
    String host = "{poi.http}";
    String pageType = "static";
    String hostImage = host;
    host = host + "/touch";
	
    String prefetchType = "prefetch|static";
    String br = "\r\n";
    
    String imageKey = (String)request.getAttribute(BrowserFrameworkConstants.IMAGE_KEY);
    String imageCommonKey =  (String)request.getAttribute(BrowserFrameworkConstants.IMAGE_KEY_WITHOUT_SIZE) + "common";
    String msgKey = (String)request.getAttribute(BrowserFrameworkConstants.MESSAGE_KEY); 
    DataHandler handlerGloble = (DataHandler)request.getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
    String locale = handlerGloble.getClientInfo(DataHandler.KEY_LOCALE);
    String region = handlerGloble.getClientInfo(DataHandler.KEY_REGION);
    
    if("".equals(locale))
    {
        locale = PreferenceConstants.VALUE_LANGUAGE_ENGLISH;
    }

    if("".equals(region))
    {
        region = PreferenceConstants.VALUE_REGION_AMERICA;
    }
    
    MessageWrap msg = MessageHelper.getInstance(false).getMessageWrap(msgKey);
    // feature manager 
    String[] defaultParams = {DataHandler.KEY_REGION, DataHandler.KEY_CARRIER, DataHandler.KEY_PRODUCTTYPE, DataHandler.KEY_DEVICEMODEL};
    IFeatureManager featureMgr = FeatureManager.getManager(handlerGloble, defaultParams);
    pageContext.setAttribute(IFeatureManager.MANAGER_KEY, featureMgr); 
    
    String getPage = host + "/goToJsp.do?pageRegion=" + region + "&amp;jsp=";
    String getPageCallBack = host + "/goToJsp.do?pageRegion=" + region + "&jsp=";
    String getDsrPage = host + "/runDSR.do?pageRegion=" + region +"&amp;action=";
    String getDsrPageCallBack = host + "/runDSR.do?pageRegion=" + region + "&action=";
    String getAcPage = host + "/selectAddress.do?pageRegion=" + region + "&amp;jsp=";
    String getAcPageCallBack = host + "/selectAddress.do?pageRegion=" + region + "&jsp=";
    String getPageWithLocale = host + "/goToJsp.do?pageLocale=" + locale + "&amp;pageRegion=" + region + "&amp;jsp=";
    String getPageCallBackWithLocale = host + "/goToJsp.do?pageLocale=" + locale + "&pageRegion=" + region + "&jsp=";
    String cAlertURL = "{commuteAlert.http}/" + ClientHelper.getModuleNameForCommute(handlerGloble) + "/goToJsp.do?pageRegion=" + region + "&amp;jsp=Startup";
	String movieURL = "{movie.http}/" + ClientHelper.getModuleNameForMovie(handlerGloble) + "/goToJsp.do?pageRegion=" + region + "&amp;jsp=StartUp";

    String imageHost = ClientHelper.getImageHost();
    if("".equals(imageHost))
    {
    	imageHost = hostImage;
    }
    
    String imageUrl = imageHost + imageKey + "/image/";
    String imageCommonUrl = imageHost + imageCommonKey + "/image/";
    
    // Group ID
    String GROUP_ID_COMMOM = "Common";
    String GROUP_ID_MISC = "MISC";
    String GROUP_ID_POI = "POI";
    String GROUP_ID_AC= "AC";
    String GROUP_ID_DSR = "DSR";
    
    String versionStr = handlerGloble.getClientInfo(DataHandler.KEY_VERSION);
    double version = Double.parseDouble(versionStr.substring(0,3));
%>
