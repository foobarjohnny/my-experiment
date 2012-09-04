<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@include file="../model/AddressCaptureTemplateModel.jsp"%>
<tml:script language="fscript" version="1">
		<![CDATA[
			# Template
		    func AddressCaptureTemplate_C_saveTemplates(JSONObject jo)
		        AddressCaptureTemplate_M_saveTemplates(jo)
		    endfunc
		]]>
</tml:script>


