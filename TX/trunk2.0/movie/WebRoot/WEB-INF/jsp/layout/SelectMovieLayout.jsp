<%@ include file="layout.jsp"%>
<%@page import="java.util.Collection"%>
<%@page
	import="com.telenav.browser.common.resource.holder.LayoutProperties"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>


<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.telenav.browser.movie.datatype.Movie"%>
<%
	for (java.util.Iterator iterator = layoutSupported.iterator(); iterator
			.hasNext();) {
		LayoutProperties layout = (LayoutProperties) iterator.next();
		int lHeight = layout.getIntProperty("button.height");
		int lSpace = layout.getIntProperty("button..margin");
		int lTitleHeight = layout.getIntProperty("title.height");
		int lScreenWidth = layout.getIntProperty("screen.width");
		int lScreenHeight = layout.getIntProperty("screen.height");
		String lTitleFontColor = layout.getStringProperty("title.font.color");
		String imagePath = host + "/images/" + lScreenWidth + "x"
				+ lScreenHeight;

		int y = 0;
		int x = 0;

%>
<tml:layout height="<%=lScreenHeight%>" width="<%=lScreenWidth%>">
	<tml:uicontrol id="selectMoviePage">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lScreenHeight%></tml:uiattribute>
		<tml:uiattribute key="background"><%=imagePath + "/background_blank.png"%></tml:uiattribute>
	</tml:uicontrol>
	<%-- Title --%>
	<tml:uicontrol id="selectMovieTitle">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lTitleHeight%></tml:uiattribute>
		<tml:uiattribute key="fontColor"><%=lTitleFontColor%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y = lTitleHeight;
	%>
	<%
		Map movieMap = (Map) request.getAttribute("movies");
				Set key = movieMap.keySet();
				int index = 0;
				String ratingImageUrl;
				for (Iterator it = key.iterator(); it.hasNext(); index++) {
					Movie movie = (Movie) it.next();
					ratingImageUrl = imagePath + "/small_"
							+ ((int) (movie.getRating() * 2)) + ".png";
	%>
	<tml:uicontrol id="<%="movie"+index%>">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lHeight%></tml:uiattribute>
		<tml:uiattribute key="imageUrl"><%=ratingImageUrl%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lHeight + lSpace;
				}
	%>

</tml:layout>
<%
	}
%>


