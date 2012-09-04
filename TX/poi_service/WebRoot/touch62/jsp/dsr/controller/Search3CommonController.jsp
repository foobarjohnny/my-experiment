<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>	
<%@ include file="/touch62/jsp/dsr/model/Search3CommonModel.jsp"%>
<tml:script language="fscript" version="1" feature="<%=FeatureConstant.DSR%>">
	<![CDATA[
	        func Search3Common_C_show(string cancelUrl)
	        	Search3Common_M_SaveCancelUrl(cancelUrl)
				System.doAction("search3Menu")
				return FAIL
	        endfunc
		]]>
</tml:script>

<tml:block feature="<%=FeatureConstant.DSR%>">
	<tml:menuItem name="search3Menu" pageURL="<%=getDsrPage + "SpeakFlow3"%>"/>
</tml:block>