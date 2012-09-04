<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="../model/ReferFriendModel.jsp"%>
<jsp:include page="/WEB-INF/jsp/ac/controller/SelectContactController.jsp"/>	
<%@ include file="../GetClientInfo.jsp"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<tml:script language="fscript" version="1">
	<![CDATA[
	       func ReferFriend_C_initalAndGoTo()
	            #ReferFriend_M_deleteContactListNode()
	            #ReferFriend_M_deletePhoneInfoForBackShow()
	            #System.doAction("goToReferFriend")
	            String title = "Refer TeleNav"
	            <% 
				    if(!TnUtil.isTN(product)){
	                    %>
				          title = "<%=msg.get("common.refer.title")%>"
				        <%
				    }
				%>
	            
	            JSONObject joContact
	        	JSONObject.put(joContact,"callbackpageurl","<%=getPageCallBack + "ReferFriend"%>")
			    JSONObject.put(joContact,"callbackfunction","CallBack_SelectContact")
	        	JSONObject.put(joContact,"contactType","ReferFriend")
	        	JSONObject.put(joContact,"title",title)
	            SelectContact_C_show(joContact)
	       endfunc
		]]>
</tml:script>

<tml:menuItem name="goToReferFriend"
	pageURL="<%=getPage + "ReferFriend"%>">
</tml:menuItem>