<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp"%>

<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>

<%@page import="com.telenav.browser.movie.Constant" %>

<%
	
	// shows no movies message for theater choosen in POI_theaters category
    String imageBg = imageUrl + "/background_telenav.png";
    String pageURL = host + "/NoMovies.jsp";
    
	String theaterId = (String)request.getAttribute("theaterId");
	String dateDisp =  " (" + (String)request.getAttribute("dateDisplay") + ")";
%>

<tml:TML outputMode="TxNode">

	<jsp:include page="controller/SelectDateController.jsp" />
	
	<tml:script language="fscript" version="1">
		<![CDATA[
			func onLoad()
				#set flag for New Search action to return to original search
				TxNode flag
		    	TxNode.addMsg(flag, "theater")
		        String saveKey="<%=Constant.StorageKey.MOVIE_FROM_SEARCH%>"
		    	Cache.saveToTempCache(saveKey, flag)
			endfunc
				
			func OnDateChange()
				String url = "<%=host + "/ShowMovies.do"%>"
				<% if(theaterId == null) { %>
					SelectDate_C_showDateListInterface(url, NULL, NULL, NULL)
				<% }else{ %>
					SelectDate_C_showDateListInterface(url, NULL, <%=theaterId%>, NULL)
				<% }%>
			endfunc
			
			func back()
				System.back()
			endfunc
			]]>
	</tml:script>

		<tml:menuItem name="goBack" onClick="back"/>
		<tml:menuItem name="ButtonCD" onClick="OnDateChange"/>
		
	<tml:page id="NoMovies" url="<%=pageURL%>"	background="<%=imageBg%>" type="net" groupId="<%=MOVIE_GROUP_ID%>">
		
		<tml:title id="title" align="center|middle"
			fontWeight="bold|system_large" fontColor="white">
			<%=msg.get("noMovies.title") + dateDisp%>
		</tml:title>
		
		<tml:label id="sorry" fontSize="20"	align="center|middle" textWrap="wrap">
			<%=msg.get("noMovies.msg")%>
		</tml:label>
		
		<tml:button id="backB" text="<%=msg.get("menu.go.back")%>"
			fontWeight="system_large"
			imageClick="<%=imageUrl + "button_small_on.png"%>"
			imageUnclick="<%=imageUrl + "button_small_off.png"%>">
			<tml:menuRef name="goBack" />
		</tml:button>
		
		<tml:button id="dateButton" text="<%=msg.get("menu.change.date")%>"
			fontWeight="system_large"
			imageClick="<%=imageUrl + "button_small_on.png"%>"
			imageUnclick="<%=imageUrl + "button_small_off.png"%>">
			<tml:menuRef name="ButtonCD" />
		</tml:button>
		<cserver:outputLayout />
	</tml:page>
</tml:TML>