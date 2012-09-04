<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%
	String host = "http://172.16.10.87:8080/poi_service";
	String pageType = "net";
	String getPage = host + "/goToJsp.do?jsp=";
	String pageURL = host + "/TestForAndroid.jsp";
%>
<tml:TML outputMode="TxNode">

	<tml:page id="mainPageMapPage" url="<%=pageURL%>" type="<%=pageType%>"
		x="0" y="0" width="200" height="320" showLeftArrow="true"
		showRightArrow="true" helpMsg="">

		<tml:label id="testLabel" x="0" y="50" width="200" height="100" textWrap="wrap" fontSize="22" align="left|top">
		    Hi, this is a label for Android test
		</tml:label>
	</tml:page>
</tml:TML>
