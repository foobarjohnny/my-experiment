<%@ include file="../Header.jsp"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%@page import="com.telenav.cserver.util.TnConstants"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%
	Map screenParameter = (Map)request.getAttribute(TnConstants.PARAMETER_SCREEN);
	String pin = (String)screenParameter.get("pin");
%>
<tml:TML outputMode="TxNode">
	<tml:page id="AboutPin" url="<%=host + "/About.do?pageRegion=" + region + "&amp;action=aboutPin"%>" type="net"   background="" helpMsg="$//$yourpin">
		
		<tml:title id="title" fontWeight="bold|system_large" align="center" fontColor="white">
			<%=TnUtil.amend(msg.get("about.pin.title"))%>
		</tml:title>
    	<tml:multiline id="pinLabel" fontWeight="system_large">
			<%=TnUtil.amend(msg.get("about.pin.pinLabel")+"<bold> "+pin+"</bold>. \n"+msg.get("about.pin.hint")+" "+msg.get("about.pin.hint2"))%> 
		</tml:multiline>
		<tml:image id="titleShadow" url="" align="left|top"/>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>

