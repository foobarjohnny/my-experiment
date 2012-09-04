<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@include file="/WEB-INF/jsp/misc/model/SendUpdateModel.jsp"%>
<jsp:include page="/WEB-INF/jsp/ac/controller/SelectContactController.jsp"/>

<tml:script language="fscript" version="1">
	<![CDATA[
        func SendUpdate_C_show(JSONObject jo)
        	SendUpdate_M_setParameter(jo)
        	
        	JSONObject joContact
        	JSONObject.put(joContact,"callbackpageurl","<%=getPageCallBack + "SendUpdate"%>")
        	JSONObject.put(joContact,"callbackfunction","CallBack_SelectContact")
        	JSONObject.put(joContact,"from","SendUpdate")
        	JSONObject.put(joContact,"contactType","SendUpdate")
        	JSONObject.put(joContact,"title","Send ETA To")
			SelectContact_C_show(joContact)
        endfunc
        
        func SendUpdate_C_saveContactNode(TxNode contactNode)
	        SendUpdate_M_saveContactNode(contactNode)
	    endfunc
	]]>
</tml:script>
