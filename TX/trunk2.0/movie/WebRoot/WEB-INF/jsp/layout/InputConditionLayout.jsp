<%@ include file="layout.jsp"%>
<%@page import="java.util.Collection"%>
<%@page
	import="com.telenav.browser.common.resource.holder.LayoutProperties"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
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

		int lButtonInterval = layout
				.getIntProperty("movies.button.b.interval");
		int lButtonWidth = layout
				.getIntProperty("movies.button.b.width");
		int lButtonHeight = layout
				.getIntProperty("movies.button.b.height");
		int lButtonMargin = layout
				.getIntProperty("movies.button.b.margin");
		int lSearchMoviesButtonX = (lScreenWidth - lButtonWidth * 2 - lButtonInterval) / 2;
		int lSearchTheatersButtonX = lSearchMoviesButtonX
				+ lButtonWidth + lButtonInterval;

		int y = 0;
		int x = 0;
%>
<tml:layout height="<%=lScreenHeight%>" width="<%=lScreenWidth%>">
	<tml:uicontrol id="inputConditionPage">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lScreenHeight%></tml:uiattribute>
		<tml:uiattribute key="background"><%=imagePath
									+ "/background_telenav.png"%></tml:uiattribute>
	</tml:uicontrol>
	<tml:uicontrol id="inputConditionTitle">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lTitleHeight%></tml:uiattribute>
		<tml:uiattribute key="fontColor"><%=lTitleFontColor%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lTitleHeight;
	%>
	<tml:uicontrol id="where">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lHeight + lSpace;
	%>
	<tml:uicontrol id="when">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lHeight + lSpace + lButtonMargin * 3;
	%>
	<tml:uicontrol id="searchMovies">
		<tml:uiattribute key="x"><%=lSearchMoviesButtonX%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lButtonWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lButtonHeight%></tml:uiattribute>
		<tml:uiattribute key="imageUnclick"><%=imagePath + "/button-b_off.png"%></tml:uiattribute>
		<tml:uiattribute key="imageClick"><%=imagePath + "/button-b_on.png"%></tml:uiattribute>
		<tml:uiattribute key="fontWeight"><%="system_small"%></tml:uiattribute>
	</tml:uicontrol>
	<tml:uicontrol id="searchTheaters">
		<tml:uiattribute key="x"><%=lSearchTheatersButtonX%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lButtonWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lButtonHeight%></tml:uiattribute>
		<tml:uiattribute key="imageUnclick"><%=imagePath + "/button-b_off.png"%></tml:uiattribute>
		<tml:uiattribute key="imageClick"><%=imagePath + "/button-b_on.png"%></tml:uiattribute>
	</tml:uicontrol>
</tml:layout>
<%
	}
%>
