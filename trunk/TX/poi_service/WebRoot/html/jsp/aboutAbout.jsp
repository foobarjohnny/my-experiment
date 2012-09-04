<%@page import="java.util.List"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlCommonUtil" %>
<%@page import="com.telenav.cserver.framework.html.util.HtmlFeatureHelper" %>
<%@page import="com.telenav.cserver.framework.html.util.HtmlClientInfoFactory"%>
<%@page import="com.telenav.cserver.html.util.HtmlPoiUtil" %>
<%
	HtmlClientInfo clientInfo = (HtmlClientInfo)request.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
	String manifestName = "manifest.manifest?module=about&clientInfo=" + HtmlClientInfoFactory.getInstance().buildClientInfoString(clientInfo);
%>
<html manifest='<%=manifestName%>'>
<title>About</title>
<head>
<%@ include file="/html/jsp/Header.jsp"%>
<%-- <link href="<%=cssDeviceCommonPath + "aboutAbout.css"%>" rel="stylesheet"> --%>
<link href="<%=cssProgUrl + "aboutAbout_compressed.css"%>" rel="stylesheet">
</head>
<%
	String programCode = clientInfo.getProgramCode();
	String buildNo = clientInfo.getBuildNo();
	//String ssoToken = request.getParameter("ssoToken");
	String dataSetKey = HtmlCommonUtil.getMapProvider(clientInfo);
	String dataSet = msg.get(dataSetKey);
	String versionNo = HtmlPoiUtil.getVersionNo(clientInfo);
	
	boolean backButton = HtmlCommonUtil.isIphone(clientInfo.getPlatform());
	boolean movie = HtmlFeatureHelper.getInstance().supportFeature(clientInfo, HtmlFrameworkConstants.FEATURE.FEATURE_MOVIE);
 %>
<body>
<div id="container_div" class="clsPageBackground">
	<%if(!backButton){%>
	<div id="titleBarDiv" class="clsTitleFrame clsTitleBg">
		<div class="div_table">
			<div class="div_cell clsTitleContent">
				<html:msg key='<%="about.title." + programCode%>'/>
			</div>
		</div>
	</div>
	<%}%>
	<div class="about fs_large fc_gray fw_bold ">
		<div class="line"><html:msg key='<%="about.product." + programCode%>'/></div>
		<div class="line" ><html:msg key='about.version'/>
			<span id="versionNo">[<%=versionNo+"."+buildNo%>]</span>
		</div>
		<div class="line"  id="poweredBy"><html:msg key='about.poweredBy'/></div>
		<div class="line">
			<html:msg key='about.mapData'/>
			<span id="dataSet"><%=dataSet %></span>
		</div>
		
		<aside id="copyRight" style="display:none">
			<html:msg key='about.copyRight.notice' />
		</aside>
		
		<%if(movie){ %>
		<div class="line"><html:msg key='about.movieList'/></div>
		<%}%>
		<div >
			<html:msg key='<%="about.tosText." + programCode%>'/><br/>
			<a href="<%=msg.get("about.termUrl1." + programCode) %>">
				<html:msg key='about.term.text'/>
			</a>
		</div>
	</div>
</div>
<%@ include file="/html/jsp/Footer.jsp"%>
<script src="<%=contextPath + "js/about.js"%>" ></script>
</body>
</html>
