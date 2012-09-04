<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@page import="com.telenav.browser.movie.Constant" %>

<%@ include file="Header.jsp"%>
<%
	String pageURL = "{movie.http}/goToJsp.do?pageRegion=" + region + "&amp;jsp=StartUp";
	String sMovie = getPage + "SearchMovie";
%>

<tml:TML outputMode="TxNode">
	<tml:script language="fscript" version="1">
		<![CDATA[
				func preLoad()  
			        String saveKey="<%=Constant.StorageKeyForJSON.JSON_OBJECT_SEARCH_POI_ADDESS%>"
			        Cache.deleteFromTempCache(saveKey)
			        saveKey="<%=Constant.StorageKey.MOVIE_SORT_TYPE%>"
			        Cache.deleteFromTempCache(saveKey)
			        saveKey="<%=Constant.StorageKey.MOVIE_NEW_SORT_BY%>"
			        Cache.deleteFromTempCache(saveKey)
			        saveKey="<%= Constant.StorageKey.MOVIE_PAGE_DATE_ID%>"
				    Cache.deleteFromTempCache(saveKey)
			        saveKey="<%= Constant.StorageKey.MOVIE_PAGE_DATE_LABEL%>"
				    Cache.deleteFromTempCache(saveKey)
			        saveKey="<%= Constant.StorageKey.MOVIE_PAGE_KEYWORD%>"
				    Cache.deleteFromTempCache(saveKey)
					System.doAction("startup")
				endfunc
			]]>
	</tml:script>

	<tml:menuItem name="startup" pageURL="<%=sMovie%>" />
	
	<tml:page url="<%=pageURL%>" type="static" supportback="false" groupId="<%=MOVIE_GROUP_ID%>">
		<tml:title>
		</tml:title>
	</tml:page>
</tml:TML>