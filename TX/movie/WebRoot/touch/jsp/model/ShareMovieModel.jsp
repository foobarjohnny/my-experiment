<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ include file="../Header.jsp"%>
<%@page import="com.telenav.browser.movie.Constant" %>
<%@page import="com.telenav.cserver.framework.executor.ExecutorResponse" %>

<tml:script language="fscript" version="1">
		<![CDATA[
		    func ShareMovie_M_saveParams(JSONObject param)
		        String saveKey="<%= Constant.StorageKeyForJSON.MOVIE_SHARE_PARAMS_JSON%>"
			    Cache.saveToTempCache(saveKey,param)
		    endfunc
		    
		    func ShareMovie_M_getParams()
		        String saveKey="<%= Constant.StorageKeyForJSON.MOVIE_SHARE_PARAMS_JSON%>"
				JSONObject jo = Cache.getJSONObjectFromTempCache(saveKey)
				return jo		    
		    endfunc
			
			func ShareMovie_M_savePhone(TxNode phone)
		        String saveKey="<%=Constant.StorageKey.SHARE_MOVIE_PHONE%>"
			    Cache.saveToTempCache(saveKey,phone)
			endfunc
					    
			func ShareMovie_M_getPhone()
		        String saveKey="<%=Constant.StorageKey.SHARE_MOVIE_PHONE%>"
			    TxNode nodePhone = Cache.getFromTempCache(saveKey)
			    String phone = ""
			    if nodePhone != NULL
			    	phone = TxNode.msgAt(nodePhone,0)
			    endif
			    return phone
			endfunc
			
		    func ShareMovie_M_removePhone()
		        String saveKey="<%= Constant.StorageKey.SHARE_MOVIE_PHONE%>"
				Cache.deleteFromTempCache(saveKey)
		    endfunc
			
		    func ShareMovie_M_saveRecipients(JSONObject param)
		        String saveKey="<%= Constant.StorageKeyForJSON.MOVIE_SHARE_RECIPIENTS_JSON%>"
			    Cache.saveToTempCache(saveKey,param)
		    endfunc
		    
		    func ShareMovie_M_getRecipients()
		        String saveKey="<%= Constant.StorageKeyForJSON.MOVIE_SHARE_RECIPIENTS_JSON%>"
				JSONObject jo = Cache.getJSONObjectFromTempCache(saveKey)
				return jo		    
		    endfunc
		    
		    func ShareMovie_M_removeRecipients()
		        String saveKey="<%= Constant.StorageKeyForJSON.MOVIE_SHARE_RECIPIENTS_JSON%>"
				Cache.deleteFromTempCache(saveKey)
		    endfunc
		    
		    func ShareMovie_M_removeContact()
		        Cache.deleteFromTempCache("<%=Constant.StorageKey.SHARE_MOVIE_CONTACT_DEFAULT%>")
		    endfunc

		    func ShareMovie_M_getContact()
		        TxNode node = Cache.getFromTempCache("<%=Constant.StorageKey.SHARE_MOVIE_CONTACT_DEFAULT%>")
		        return node
		    endfunc
		    
		    func ShareMovie_M_setContact(TxNode contact)
		        Cache.saveToTempCache("<%=Constant.StorageKey.SHARE_MOVIE_CONTACT_DEFAULT%>",contact)
		    endfunc
	    		    
		    func ShareMovie_M_sendSms(String phoneNumber)
		        String saveKey="<%= Constant.StorageKeyForJSON.MOVIE_SHARE_PARAMS_JSON%>"
				JSONObject jo = Cache.getJSONObjectFromTempCache(saveKey)
				JSONObject recipients
				if "" != phoneNumber
					JSONObject.put(recipients, "<%=Constant.RRKey.SM_RECIPIENT%>", phoneNumber)
				else
					recipients = ShareMovie_M_getRecipients()
				endif
				TxNode node
				TxNode.addMsg(node, JSONObject.toString(jo))
				TxNode.addMsg(node, JSONObject.toString(recipients))
				TxRequest req
				
				TxNode langNode = Preference.getPreferenceValue(9)
				String language
				String send_sms
				if NULL != langNode
				   	language = TxNode.msgAt(langNode,0)	
				endif
				if language == "es_MX"
					send_sms = "Mandando Mensaje de texto..."
				else
					send_sms = "Sending SMS..."
				endif
				String url="<%=host + "/SendShareMovieSMS.do"%>"
				String scriptName="shareMovieCallback"
				TxRequest.open(req,url)
				TxRequest.setRequestData(req,node)
				TxRequest.onStateChange(req,scriptName)
				TxRequest.setProgressTitle(req,send_sms)
				TxRequest.send(req)	
		    endfunc
		    
		    func ShareMovie_M_clear()
	            ShareMovie_M_removePhone()
	            ShareMovie_M_removeRecipients()
	            ShareMovie_M_removeContact()
	            # if you use it in the way back it kills the pop-up window
	            # so we have to do it here
				Page.setComponentAttribute("sentToDisplay","text", "") 
				Page.setComponentAttribute("sentTo","text", "") 
				Page.setComponentAttribute("sentTo","visible", "1")
				Page.setComponentAttribute("sentToDisplay","visible", "0")		    
		    endfunc
		    
		    func shareMovieCallback(TxNode node,int status)
	            if status == 0
	                System.showErrorMsg("<%=msg.get("err.internal")%>")
				    return FAIL
				endif
				int statusCode = TxNode.valueAt(node, 0)
				if statusCode == <%=ExecutorResponse.STATUS_OK%>
					ShareMovie_M_clear()
                	System.showGeneralMsg(NULL,"<%=msg.get("SMS.success")%>",NULL,NULL,3,"Callback_PopopTimeOut")
			    	return FAIL
			    else
			    	System.showErrorMsg("<%=msg.get("SMS.notAvailable")%>")
			    	return FAIL
				endif		    
		    endfunc
		    
		    func Callback_PopopTimeOut(int param)
       			System.back()
				return FAIL		    	
		    endfunc
		    
		]]>
</tml:script>