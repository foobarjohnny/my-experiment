<%@ include file="Header.jsp"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@page import="com.telenav.browser.movie.Constant" %>
<%@page import="com.telenav.browser.movie.Util" %>
<%@page import="com.telenav.cserver.browser.util.ClientHelper"%>
<%
	String poiModuleName = ClientHelper.getModuleNameForPoi(handlerGloble);
	String POI_host = "{poi.http}/" + poiModuleName + "/goToJsp.do?jsp=SearchPoi";
%>
	<tml:script language="fscript" version="1">
		<![CDATA[
			func newSearch()
				#set flag for New Search action to return to original search
		        String saveKey="<%=Constant.StorageKey.MOVIE_FROM_SEARCH%>"
		    	TxNode node = Cache.getFromTempCache(saveKey)
		    	String flag = TxNode.msgAt(node, 0)
			    System.doAction(flag)
			endfunc
			 
		]]>
	</tml:script>

	<%--change date--%>
	<%-- function OnDateChange should be implemented in each including file --%>
	<tml:menuItem name="ChangeDate" onClick="OnDateChange" text="<%=msg.get("menu.change.date")%>" trigger="KEY_CONTEXT_MENU || KEY_MENU">
	</tml:menuItem>
	
	<%--new search--%>
	<tml:menuItem name="NewSearch" onClick="newSearch" text="<%=msg.get("menu.new.search")%>"  trigger="KEY_CONTEXT_MENU || KEY_MENU">
	</tml:menuItem>
	
	<tml:menuItem name="theater" pageURL="<%=POI_host%>">
	</tml:menuItem>
	
	<tml:menuItem name="movie" pageURL="<%=getPage + "StartUp"%>">
	</tml:menuItem>