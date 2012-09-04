<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<tml:script language="fscript" version="1">
	<![CDATA[
	    func FreeTrial_serverDriven_GetValue(String key)
	    	println("~~~~~~serverDriven_GetValue(key)~~~~~~~"+key)
			int isSupport = -1
			TxNode node = System.getServerParam(key)
			 if node != NULL
			    if NULL != TxNode.msgAt(node,0) 
			    	if "" != TxNode.msgAt(node,0)
			    		isSupport = String.convertToNumber(TxNode.msgAt(node,0))
			    	endif
			    endif
			 endif
			   	
			println(key + ".................."+isSupport)
			return isSupport
		endfunc
		
	    func FreeTrial_serverDriven_FreeTrialOfferInterval_Min()
	   		return FreeTrial_serverDriven_GetValue("<%=Constant.FREE_TRIAL_OFFER_INTERVAL_MIN%>")
		endfunc
		
		func FreeTrial_serverDriven_FreeTrialDays()
			return FreeTrial_serverDriven_GetValue("<%=Constant.FREE_TRIAL_DAYS%>")
		endfunc
		
		func FreeTrial_serverDriven_FreeTrialReminderDay()
			return FreeTrial_serverDriven_GetValue("<%=Constant.FREE_TRIAL_REMINDER_DAY%>")
		endfunc
		
		func FreeTrial_serverDriven_FreeTrialReminderInterval_Min()
			return FreeTrial_serverDriven_GetValue("<%=Constant.FREE_TRIAL_REMINDER_INTERVAL_MIN%>")
		endfunc
	]]>
</tml:script>