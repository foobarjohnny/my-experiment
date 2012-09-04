<%@ include file="header.jsp"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%
	int acMask = 0x1 | 0x2 | 0x4 | 0x8 | 0x10 | 0x20 | 0x40 | 0x100
			| 0x200 | 0x0400 | 0x0800;
%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Locale"%>
<%@page import="com.telenav.j2me.datatypes.Stop"%>
<%@page import="com.telenav.browser.movie.Constants"%>
<%@page import="com.telenav.browser.movie.util.MovieUtil"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>


<%
	int lButtonInterval = layout
			.getIntProperty("movies.button.b.interval");
	int lButtonWidth = layout.getIntProperty("movies.button.b.width");
	int lButtonHeight = layout.getIntProperty("movies.button.b.height");
	int lButtonMargin = layout.getIntProperty("movies.button.b.margin");
	int lSearchMoviesButtonX = (lScreenWidth - lButtonWidth * 2 - lButtonInterval) / 2;
	int lSearchTheatersButtonX = lSearchMoviesButtonX + lButtonWidth
			+ lButtonInterval;
%>

<tml:TML outputMode="TxNode">
	<tml:script language="fscript" version="1">
		<![CDATA[
		func setWhere()
			TxNode node
			node=ParameterSet.getParam("addrFromAC")

			if NULL!=node
				if isCurrentStop(node)
					Page.setComponentAttribute("where","text","<bean:message key="movies.where" />\n\r<bean:message key="movies.current.location"/>")
					MenuItem.setActionItemValid("movies::searchmovies",0,1);
					MenuItem.setActionItemValid("movies::searchtheaters",0,1);
					
					MenuItem.setBean("movies::searchmovies","addrFromAC",NULL)
					MenuItem.setBean("movies::searchtheaters","addrFromAC",NULL)
				else
					Page.setComponentAttribute("where","text","<bean:message key="movies.where" />\n\r"+getLabelFromStop(node))
					MenuItem.setActionItemValid("movies::searchmovies",0,0);
					MenuItem.setActionItemValid("movies::searchtheaters",0,0);
					
					MenuItem.setBean("movies::searchmovies","addrFromAC",node)
					MenuItem.setBean("movies::searchtheaters","addrFromAC",node)
				endif
			else
				Page.setComponentAttribute("where","text","<bean:message key="movies.where" />\n\r<bean:message key="movies.current.location"/>")
				MenuItem.setActionItemValid("movies::searchmovies",0,1);
				MenuItem.setActionItemValid("movies::searchtheaters",0,1);
			endif
		endfunc

		func getLabelFromStop(TxNode node)
			string firstline,city,state,label	
			label=TxNode.msgAt(node,0)
			firstline=TxNode.msgAt(node,1)
			city=TxNode.msgAt(node,2)
			if firstline!=NULL && firstline!=""
				return firstline
			elseif city!=NULL && city!=""
				return city
			else
			  	return label
			endif	
		endfunc

		func isCurrentStop(TxNode node)
			int type
			type= TxNode.valueAt(node,3)
			if 6==type
				return TRUE
			else
				return FALSE
		endif
	endfunc

	func setDate()
		TxNode node
		node=ParameterSet.getParam("dateStr")
		if NULL!=node
			Page.setComponentAttribute("when","text","<bean:message key="movies.when" />\n\r"+TxNode.msgAt(node,0))
			node = ParameterSet.getParam("dateLong")
			MenuItem.setBean("movies::searchmovies","date",node)
			MenuItem.setBean("movies::searchtheaters","date",node)
		endif
	endfunc
	
	func checkGPS()
		TxNode addrFromACNode
		addrFromACNode = ParameterSet.getParam("addrFromAC")
		if NULL == addrFromACNode
			TxNode node
			node = ParameterSet.getParam("currentLocation")		
			if NULL == node
				System.showErrorMsg("No GPS, Please move to open area.")
				return FAIL
			endif
		endif
	endfunc
	
		]]>
	</tml:script>

	<tml:page id="inputConditionPage"
		url="<%=host + "/InputCondition.do"%>"
		background="<%=imagePath + "/background_telenav.png"%>" x="0" y="0"
		width="<%=lScreenWidth%>" height="<%=lScreenHeight%>" type="prefetch"
		helpMsg="##RES_MENU_HELP_COMMUTE_ALERTS" supportback="true">
		<%--input address--%>
		<tml:actionItem name="getAddrFromAC" action="getAddrFromAC">

			<tml:output name="addrFromAC" />
			<tml:input>
				<tml:bean name="title" valueType="string" value="Movies Near" />
			</tml:input>
			<tml:input>
				<tml:bean name="mask" valueType="int" value="<%=acMask + ""%>" />
			</tml:input>
		</tml:actionItem>
		<%--get GPS of current position--%>
		<tml:actionItem name="getGPS" action="getGPS">
			<tml:output name="currentLocation" />
			<tml:input>
				<tml:bean name="needAccurateFix" valueType="int" value="0" />
			</tml:input>
		</tml:actionItem>






		<%--title--%>
		<tml:title id="inputConditionTitle" x="0" y="0"
			width="<%=lScreenWidth%>" height="<%=lTitleHeight%>" fontSize="18"
			fontWeight="bold|system_large" align="center"
			fontColor="<%=lTitleFontColor%>">
			<bean:message key="movies.title" />
		</tml:title>
		<%
			int y = lTitleHeight;
		%>
		<%--where--%>
		<tml:menuItem name="movies::where"
			trigger="KEY_RIGHT | TRACKBALL_CLICK" actionRef="getAddrFromAC"
			onClick="setWhere" />
		<tml:urlImageLabel id="where" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="<%=lHeight%>" fontSize="16"
			fontWeight="bold|system" align="left" multLine="true">
			<bean:message key="movies.where" /><%="\n\r"%><bean:message
				key="movies.current.location" />
			<tml:menuRef name="movies::where" />
		</tml:urlImageLabel>
		<%
			y += lHeight + lSpace;
		%>
		<%--when--%>
		<tml:menuItem name="movies::when"
			pageURL="<%=host + "/SelectDate.do"%>"
			trigger="KEY_RIGHT | TRACKBALL_CLICK">
		</tml:menuItem>
		<tml:urlImageLabel id="when" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="<%=lHeight%>" fontSize="16"
			fontWeight="bold|system" align="left" multLine="true">
			<bean:message key="movies.when" /><%=MovieUtil.amend("\n\r"
										+ request.getAttribute("date"))%>
			<tml:menuRef name="movies::when" />
		</tml:urlImageLabel>
		<%
			y += lHeight + lSpace + lButtonMargin * 3;
		%>
		<%--search movie--%>

		<tml:menuItem name="movies::searchmovies"
			pageURL="<%=host + "/SearchMovies.do"%>"
			progressBarText="Searching Movies..."
			trigger="KEY_RIGHT | TRACKBALL_CLICK" onClick="checkGPS"
			actionRef="getGPS">
			<tml:bean name="date" valueType="String"
				value="<%=System.currentTimeMillis() + ""%>" />
		</tml:menuItem>
		<tml:button id="searchMovies" x="<%=lSearchMoviesButtonX%>" y="<%=y%>"
			width="<%=lButtonWidth%>" height="<%=lButtonHeight%>"
			text="Search Movies" textVisible="true"
			imageUnclick="<%=imagePath + "/button-b_off.png"%>"
			imageClick="<%=imagePath + "/button-b_on.png"%>" isFocusable="true"
			getFocus="true" fontWeight="bold|system_median">
			<tml:menuRef name="movies::searchmovies" />
		</tml:button>
		<%--	<tml:urlImageLabel x="0" y="<%=y%>" width="<%=lScreenWidth%>"
			height="<%=lHeight%>" fontSize="16" fontWeight="system" align="left"
			multLine="true">
			<bean:message key="movies.searchmovies" />
			<tml:menuRef name="movies::searchmovies" />
		</tml:urlImageLabel> --%>

		<%--search theater--%>
		<tml:menuItem name="movies::searchtheaters"
			pageURL="<%=host + "/SearchTheaters.do"%>"
			progressBarText="Searching Theaters..."
			trigger="KEY_RIGHT | TRACKBALL_CLICK" onClick="checkGPS"
			actionRef="getGPS">
			<tml:bean name="date" valueType="String"
				value="<%=System.currentTimeMillis() + ""%>" />
		</tml:menuItem>
		<tml:button id="searchTheaters" x="<%=lSearchTheatersButtonX%>"
			y="<%=y%>" width="<%=lButtonWidth%>" height="<%=lButtonHeight%>"
			text="Search Theaters" textVisible="true"
			imageUnclick="<%=imagePath + "/button-b_off.png"%>"
			imageClick="<%=imagePath + "/button-b_on.png"%>" isFocusable="true"
			fontWeight="bold|system_median">
			<tml:menuRef name="movies::searchtheaters" />
		</tml:button>
		<%--<tml:urlImageLabel x="0" y="<%=y%>" width="<%=lScreenWidth%>"
			height="<%=lHeight%>" fontSize="16" fontWeight="system" align="left"
			multLine="true">
			<bean:message key="movies.searchtheaters" />
			<tml:menuRef name="movies::searchtheaters" />
		</tml:urlImageLabel>--%>



		<%
			DataHandler handler = (DataHandler) request
							.getAttribute("DataHandler");
					handler.toXML(out);
		%>
		<%--prefetched select date jsps--%>
		<tml:bundlepage>
			<c:import url="<%=host + "/SelectDate.do"%>">
				<c:param name="<%=DataHandler.KEY_LOCALE%>"
					value="<%=request.getParameter(DataHandler.KEY_LOCALE)%>" />
				<c:param name="<%=DataHandler.KEY_REGION%>"
					value="<%=request.getParameter(DataHandler.KEY_REGION)%>" />
				<c:param name="<%=DataHandler.KEY_CARRIER%>"
					value="<%=request.getParameter(DataHandler.KEY_CARRIER)%>" />
				<c:param name="<%=DataHandler.KEY_PLATFORM%>"
					value="<%=request.getParameter(DataHandler.KEY_PLATFORM)%>" />
				<c:param name="<%=DataHandler.KEY_DEVICEMODEL%>"
					value="<%=request.getParameter(DataHandler.KEY_DEVICEMODEL)%>" />
				<c:param name="<%=DataHandler.KEY_WIDTH%>"
					value="<%=request.getParameter(DataHandler.KEY_WIDTH)%>" />
				<c:param name="<%=DataHandler.KEY_HEIGHT%>"
					value="<%=request.getParameter(DataHandler.KEY_HEIGHT)%>" />
				<c:param name="<%=DataHandler.KEY_PRODUCTTYPE%>"
					value="<%=request.getParameter(DataHandler.KEY_PRODUCTTYPE)%>" />
				<c:param name="<%=DataHandler.KEY_VERSION%>"
					value="<%=request.getParameter(DataHandler.KEY_VERSION)%>" />
			</c:import>
		</tml:bundlepage>

	</tml:page>
</tml:TML>
