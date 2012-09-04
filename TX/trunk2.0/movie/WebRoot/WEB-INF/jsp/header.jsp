<%@page import="com.telenav.browser.movie.layout.MoviesLayout"%>
<%@page import="java.util.PropertyResourceBundle"%>
<%@page import="java.util.Collection"%>
<%@page import="com.telenav.browser.common.resource.holder.LayoutProperties"%>
<%
    // Common layout information.
    //MoviesLayout layout = (MoviesLayout) request.getAttribute("MoviesLayout");
	// Common layout information.
	Collection layoutSupported = (Collection) request.getAttribute("MoviesLayout");
	//set screen default value
	java.util.Iterator iterator = layoutSupported.iterator();
	
	LayoutProperties layout = (LayoutProperties) iterator.next();

    int lHeight = layout.getIntProperty("button.height");
    int lSpace = layout.getIntProperty("button..margin");
    int lTitleHeight = layout.getIntProperty("title.height");
    int lScreenWidth = layout.getIntProperty("screen.width");
    int lScreenHeight = layout.getIntProperty("screen.height");
    String lTitleFontColor = layout.getStringProperty("title.font.color");

    // Host information
    PropertyResourceBundle serverBundle = (PropertyResourceBundle) PropertyResourceBundle
            .getBundle("config.movie");
    //Try if we can get the host name and port by java API
    String host = serverBundle.getString("movie.server.url");
    String imagePath = host + "/images/" + lScreenWidth + "x"
            + lScreenHeight;
    
%>
