<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ include file="Header.jsp"%>	

<%
	
	String errorCode = (String)request.getAttribute("ErrorCode");
    String errorMsg = (String)request.getAttribute("ErrorMessage");
	String errMsg = "\r\n" + msg.get(errorMsg);
	if ("EXCEPTION".equals(errorCode)) errMsg = msg.get("error");
%>


<tml:TML outputMode="TxNode">

	<tml:page id="ErrorPage" url="ErrorPage.jsp" type="<%=pageType%>" groupId="<%=MOVIE_GROUP_ID%>">
		<tml:label id="text1" fontWeight="bold|system_large" align="center|top">
				<![CDATA[ <%=msg.get("err.page")%>: <%=errMsg%> ]]>
		</tml:label>
	</tml:page>
</tml:TML>