<%@ include file="header.jsp"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%
    String errorMsg = (String) request.getAttribute("errorMsg");
%>
<tml:TML outputMode="TxNode">


	<tml:page id="noResultPage" url="/NoResult.jsp"
		background="<%=imagePath + "/background_blank.png"%>" x="0" y="0"
		width="<%=lScreenWidth%>" height="<%=lScreenHeight%>" type="net">
		<tml:error text="<%=errorMsg%>">
		</tml:error>
	</tml:page>
</tml:TML>
