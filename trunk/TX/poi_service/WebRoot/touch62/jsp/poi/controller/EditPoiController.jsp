<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
	
<%@ include file="../model/EditPoiModel.jsp"%>
<tml:script language="fscript" version="1" feature="<%=FeatureConstant.UGC_EDIT%>">
		<![CDATA[
		      func EditPoi_C_savePoiInformation(JSONObject poiInformationJo)
	             EditPoi_M_savePoiInformation(poiInformationJo)
	          endfunc
		]]>
</tml:script>