<%@ include file="layout.jsp"%>
<%@page import="java.util.Collection"%>
<%@page
	import="com.telenav.browser.common.resource.holder.LayoutProperties"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>

<%@page import="java.util.List"%>
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

		int lButtonInterval = layout
				.getIntProperty("movies.button.c.interval");
		int lButtonWidth = layout
				.getIntProperty("movies.button.c.width");
		int lButtonHeight = layout
				.getIntProperty("movies.button.c.height");
		int lButtonMargin = layout
				.getIntProperty("movies.button.c.margin");
		int lPreviousButtonX = (lScreenWidth - lButtonWidth * 2 - lButtonInterval) / 2;
		int lNextButtonX = lPreviousButtonX + lButtonWidth
				+ lButtonInterval;

		Object o = request.getAttribute("nextPage");
		boolean nextPage = false;
		if (o != null) {
			nextPage = ((Boolean) o).booleanValue();
		}
		boolean previousPage = false;
		o = request.getAttribute("previousPage");
		if (o != null) {
			previousPage = ((Boolean) o).booleanValue();
		}

		List list = (List) request.getAttribute("pageTheaters");
		int theatersCount = 0;
		if (null != list) {
			theatersCount = list.size();
		}
%>
<tml:layout height="<%=lScreenHeight%>" width="<%=lScreenWidth%>">
	<tml:uicontrol id="searchTheatersPage">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lScreenHeight%></tml:uiattribute>
		<tml:uiattribute key="background"><%=imagePath + "/background_blank.png"%></tml:uiattribute>
	</tml:uicontrol>

	<%-- Title --%>
	<tml:uicontrol id="searchTheatersTitle">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lTitleHeight%></tml:uiattribute>
		<tml:uiattribute key="fontColor"><%=lTitleFontColor%></tml:uiattribute>
	</tml:uicontrol>

	<%
		y = lTitleHeight;

				for (int i = 0; i < theatersCount; i++) {
	%>
	<tml:uicontrol id="<%="theater"+i%>">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lHeight + lSpace;
				}
	%>
	<%
		y += lButtonMargin;
	%>
	<%--previous and next button--%>
	<%
		if (previousPage) {
	%>
	<tml:uicontrol id="previousButton">
		<tml:uiattribute key="x"><%=lPreviousButtonX%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lButtonWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lButtonHeight%></tml:uiattribute>
		<tml:uiattribute key="imageUnclick"><%=imagePath + "/button-c_off.png"%></tml:uiattribute>
		<tml:uiattribute key="imageClick"><%=imagePath + "/button-c_on.png"%></tml:uiattribute>
	</tml:uicontrol>
	<%
		}
				if (nextPage) {
	%>
	<tml:uicontrol id="nextButton">
		<tml:uiattribute key="x"><%=lNextButtonX%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lButtonWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lButtonHeight%></tml:uiattribute>
		<tml:uiattribute key="imageUnclick"><%=imagePath + "/button-c_off.png"%></tml:uiattribute>
		<tml:uiattribute key="imageClick"><%=imagePath + "/button-c_on.png"%></tml:uiattribute>
	</tml:uicontrol>
	<%
		}
	%>
	<%
		if (nextPage || previousPage) {
					y += lButtonHeight * 2;
	%>
	<tml:uicontrol id="nullLabel">
		<tml:uiattribute key="x"><%=0%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=1%></tml:uiattribute>
	</tml:uicontrol>
	<%
		}
	%>

</tml:layout>
<%
	}
%>








