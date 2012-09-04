<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="Header.jsp"%>	

<%
	
	String errorCode = (String)request.getAttribute("ErrorCode");
    String errorMsg = (String)request.getAttribute("ErrorMessage");
	String errMsg = "\r\n" + msg.get(errorMsg);
	if ("EXCEPTION".equals(errorCode)) errMsg = msg.get("error");
%>


<tml:TML outputMode="TxNode">

	<tml:page id="ErrorPage" url="ErrorPage.jsp" type="<%=pageType%>" x="0" groupId="<%=MOVIE_GROUP_ID%>"
		y="0" width="480" height="320">
		<tml:label id="text1" fontWeight="bold|system_large" align="center|top" 
			x="0" y="10" width="480" height="320">
				<![CDATA[ <%=msg.get("err.page")%>: <%=errMsg%> ]]>
		</tml:label>
		<cserver:outputLayout />
	</tml:page>
</tml:TML>