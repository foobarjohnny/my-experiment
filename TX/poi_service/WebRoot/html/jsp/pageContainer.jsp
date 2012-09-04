<%@page import="com.telenav.cserver.framework.html.util.HtmlFrameworkConstants"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlServiceLocator"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlClientInfoFactory"%>
<%
	HtmlClientInfo clientInfo = (HtmlClientInfo)request.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
	String manifestName = "manifest.manifest?clientInfo=" + HtmlClientInfoFactory.getInstance().buildClientInfoString(clientInfo);
	String targetUrl = (String)request.getParameter("target");
	String exitWebView = (String)request.getParameter("exitWebView");
	String title = (String)request.getParameter("title");
%>
<html>
<title>Page Container </title>
<head>
<%@ include file="/html/jsp/Header.jsp"%>
</head>
<script type="text/javascript">	
	
$(document).ready(function() {
	var target = decodeURIComponent('<%=targetUrl %>');
	var exitWebView = '<%=exitWebView %>';
	var title = '<%=title %>';
	if(!title){
		title = "";
	}

	document.getElementById("pageFrame").src=target;
	document.getElementById("title").innerHTML = title;
	
	if(CommonUtil.isIphone()){
		CommonUtil.addBackButtonForIphone("backButtonDiv",'<%=msg.get("iphone.back")%>',exitWebView);
	}

});

</script>
<body bgcolor="#C3DAE6" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >

	<div id="titleBarDiv" class="clsTitleFrame clsTitleBg">
		<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
		    <tr>
		    	<td width="19%" id="backButtonDiv">&nbsp;</td>
		    	<td><p class="clsTitleContent" id="title"></p></td>
		    	<td width="19%" >
			    	&nbsp;
			  	</td>
			</tr>
	  	</table>
	</div>
<iframe id="pageFrame" src="" style="width:100%;height:100%;" frameborder="0"></iframe>
	
<input type="hidden" id="programCode"  value="<%=clientInfo.getProgramCode()%>">
<input type="hidden" id="deviceCarrier"  value="<%=HtmlCommonUtil.getUrlString(clientInfo.getCarrier())%>">
<input type="hidden" id="platform"  value="<%=clientInfo.getPlatform()%>">
<input type="hidden" id="version"  value="<%=clientInfo.getVersion()%>">
<input type="hidden" id="productType"  value="<%=clientInfo.getProduct()%>">
<input type="hidden" id="device"  value="<%=clientInfo.getDevice()%>">
<input type="hidden" id="locale"  value="<%=clientInfo.getLocale()%>">
<input type="hidden" id="buildNumber"  value="<%=clientInfo.getBuildNo()%>">
<input type="hidden" id="width"  value="<%=clientInfo.getWidth()%>">
<input type="hidden" id="height"  value="<%=clientInfo.getHeight()%>">
</body>
</html>
