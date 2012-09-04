<%@page import="java.util.PropertyResourceBundle"%>
<%@page import="java.util.Collection"%>
<%
	// Common layout information.
	//MoviesLayout layout = (MoviesLayout) request.getAttribute("MoviesLayout");
	// Common layout information.
	Collection layoutSupported = (Collection) request
			.getAttribute("MoviesLayout");

	// Host information
	PropertyResourceBundle serverBundle = (PropertyResourceBundle) PropertyResourceBundle
			.getBundle("config.movie");
	//Try if we can get the host name and port by java API
	String host = serverBundle.getString("movie.server.url");
%>