<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>

<jsp:include page="/WEB-INF/jsp/ac/controller/SelectContactController.jsp"/>	
<%@include file="/WEB-INF/jsp/ac/model/ShareAddressModel.jsp"%>	

<tml:script language="fscript" version="1" feature="<%=FeatureConstant.SHARE_ADDRESS%>">
	<![CDATA[
        func ShareAddress_C_show(JSONObject jo)
        	ShareAddress_M_clear()
        	ShareAddress_M_saveParameter(jo)
        	
        	JSONObject joContact
        	JSONObject.put(joContact,"callbackpageurl","<%=getPageCallBack + "ShareAddress"%>")
		    JSONObject.put(joContact,"callbackfunction","CallBack_SelectContact")
        	JSONObject.put(joContact,"contactType","ShareAddress")
        	JSONObject.put(joContact,"title","<%=msg.get("selectcontact.title.shareaddress")%>")
            SelectContact_C_show(joContact)
        endfunc
	]]>
</tml:script>
