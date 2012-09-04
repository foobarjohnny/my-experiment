<%@page import="com.telenav.cserver.framework.html.util.HtmlMessageHelper"%>
<%@page import="com.telenav.cserver.framework.html.datatype.HtmlClientInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
	String locale="",ret="",key="";
	if(request.getParameter("locale") != null && request.getParameter("key")!=null){
		locale = request.getParameter("locale").toString();
		key = request.getParameter("key").toString();
		if(locale.equals("")) locale = "en_US";
		HtmlClientInfo hci = new HtmlClientInfo();
		hci.setProgramCode("ATTNAVPROG");
		hci.setPlatform("ANDROID");
		hci.setVersion("7.2.0");
		hci.setProduct("ATT_NAV");
		hci.setLocale(locale);
		hci.setWidth("480");
		hci.setHeight("800");
		ret = HtmlMessageHelper.getInstance().getMessageValue(hci,key);
	}
	
%>
<html>
	<head>
		<meta name="viewport" content="width=device-width" />
		<style type="text/css">
			html {
				font-size: 16pt;
			}
		</style>
	</head>
	<script language="javascript">
		function reset1(f){
			document.getElementById("locale").value = "en_US";
			document.getElementById("key").value = "";
			document.getElementById("resultDiv").innerHTML = " ";
		}
		function go(form){
			document.getElementById("resultDiv").innerHTML = "";
			form.action = form.action + "?locale="+encodeURI(form.locale.value)+"&key="+form.key.value;
			location.href = form.action;
		}
		window.onload = function(){
			var ret = '<%=ret%>',
				locale = '<%=locale%>',
				key = '<%=key%>';
			document.getElementById("locale").value = locale;
			document.getElementById("key").value = key;
			if(ret){
				ret = "<font color='red'>"+ret+"</font>";
				document.getElementById("resultDiv").innerHTML = key+" = "+ret;
			}
		}
	</script>
	<body>
		<form id="form1" action="testMsg.jsp" method="get">
			<select id="locale">
				<option value="en_US" selected="true">en_US</option>
				<option value="es_MX">es_MX</option>
				<option value="de_DE">de_DE</option>
				<option value="en_GB">en_GB</option>
				<option value="en_IN">en_IN</option>
				<option value="en_US">en_US</option>
				<option value="es_ES">es_ES</option>
				<option value="fr_FR">fr_FR</option>
				<option value="it_IT">it_IT</option>
				<option value="nl_NL">nl_NL</option>
				<option value="pt_BR">pt_BR</option>
			</select>
			<br>
			input key: <br>
			<input type="search" id="key"  style="width:100%">
			<br>
			<input type="button" value="Reset" onclick="reset1(this.form)">
			<input type="button" value="Go!" onclick="go(this.form)">
		</form>
		<div id="resultDiv"></div>
	</body>
</html>