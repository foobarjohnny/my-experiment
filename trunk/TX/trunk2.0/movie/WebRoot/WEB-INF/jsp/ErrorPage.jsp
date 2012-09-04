<%@ include file="header.jsp"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>

<tml:TML outputMode="TxNode">
	<tml:page id="errorPage" url="/ErrorPage.jsp"
		background="<%=imagePath + "/background_blank.png"%>" x="0" y="0"
		width="<%=lScreenWidth%>" height="<%=lScreenHeight%>" type="net">
		<%
		    int y = 0;
		            int labelHeight = 100;
		%>
		<tml:title id="errortitle" x="0" y="<%=y%>" width="<%=lScreenWidth%>"
			height="<%=lTitleHeight%>" fontWeight="bold|system_large"
			align="center" fontColor="<%=lTitleFontColor%>">
			<bean:message key="movies.sorry" />
		</tml:title>
		<%
		    y += lTitleHeight;
		%>
		<tml:label id="errorLabel" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="<%=labelHeight%>"
			fontWeight="bold|system_large" align="center">
			<bean:message key="movies.not.available" />
		</tml:label>
		<%
		    y += labelHeight;
		// it needs another label for showing properly.
		%>
		<tml:label id="errorLabel2" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="30" fontWeight="bold|system_huge"
			align="center">
		</tml:label>
	</tml:page>
</tml:TML>
