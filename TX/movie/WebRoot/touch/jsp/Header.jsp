
<%@page import="com.telenav.cserver.browser.util.MessageHelper"%>
<%@page import="com.telenav.cserver.browser.datatype.MessageWrap"%>
<%@page import="com.telenav.cserver.browser.framework.BrowserFrameworkConstants"%>
<%@page import="java.util.PropertyResourceBundle"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>
<%@page import="com.telenav.cserver.browser.util.ClientHelper"%>

<%
    //String host = (String) request.getAttribute("Host_url"); 
	//String pageType = "net";    
	
	String host = "{movie.http}";
	String hostImage = host;
	String pageType = "static";
	
	host = host + "/touch";
    
    String imageKey = (String)request.getAttribute(BrowserFrameworkConstants.IMAGE_KEY); 
    String msgKey = (String)request.getAttribute(BrowserFrameworkConstants.MESSAGE_KEY); 
    DataHandler handlerGloble = (DataHandler)request.getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
    String locale = handlerGloble.getClientInfo(DataHandler.KEY_LOCALE);
    String region = handlerGloble.getClientInfo(DataHandler.KEY_REGION);
    
    if("".equals(locale))
    {
        locale = "en_US"; 
    }

    if("".equals(region))
    {
        region = "NA";
    }
    
    //MessageHelper.getInstance().setReadFromServer(true);
    MessageWrap msg = MessageHelper.getInstance(false).getMessageWrap(msgKey);

    String getPage = host + "/goToJsp.do?pageRegion=" + region + "&amp;jsp=";
    String getPageCallBack = host + "/goToJsp.do?pageRegion=" + region + "&jsp=";
    
	String imageHost = ClientHelper.getImageHost();
    if("".equals(imageHost))
    {
    	imageHost = hostImage;
    }
    String imageUrl = imageHost + imageKey + "/image/";
	
	String MOVIE_GROUP_ID = "Movie";

%>
