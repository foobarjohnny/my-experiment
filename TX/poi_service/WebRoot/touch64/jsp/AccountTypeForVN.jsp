<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>

<tml:script language="fscript" version="1">
	<![CDATA[
	func AccountTypeForSprint_IsLiteUser()
	    int serviceLevel = AccountTypeForSprint_GetServiceLevel()
	    if 0 == serviceLevel
	       return FALSE
	    endif
        
        if <%=Constant.SERVICE_LEVEL_LITE_WITHOUT_FREE_TMO%> == serviceLevel || <%=Constant.SERVICE_LEVEL_LITE_WITH_FREE_TMO%> == serviceLevel
	       return TRUE
	    endif
	    
	    return FALSE
	endfunc

	func AccountTypeForSprint_IsBundleUser()	    
	    return FALSE
	endfunc
	
	func AccountTypeForSprint_IsFreeTrial()
	    return FALSE
	endfunc
	
	func AccountTypeForSprint_IsPremUser()
	    int serviceLevel = AccountTypeForSprint_GetServiceLevel()
	    if 0 == serviceLevel
	       return FALSE
	    endif
        
        if <%=Constant.SERVICE_LEVEL_PREMIUM_TMO%> == serviceLevel
	       return TRUE
	    endif
	    
	    return FALSE
	endfunc
	
	func AccountTypeForSprint_GetServiceLevel()
	    TxNode serviceLevelNode = UserInfo.getAccountInfo()
        int value = 0
        if NULL != serviceLevelNode && TxNode.getValueSize(serviceLevelNode) > 1
           value = TxNode.valueAt(serviceLevelNode, 1)
        endif       
        return value
	endfunc
	]]>
</tml:script>
