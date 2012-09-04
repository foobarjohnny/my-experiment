<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>

<tml:script language="fscript" version="1">
	<![CDATA[
	func AccountTypeForBoost_IsLiteUser()
	    int serviceLevel = AccountTypeForBoost_GetServiceLevel()
	    if 0 == serviceLevel
	       return FALSE
	    endif
        
        if <%=Constant.SERVICE_LEVEL_LITE%> <= serviceLevel && serviceLevel <  <%=Constant.SERVICE_LEVEL_BOUNDLE%>
	       println("isLiteUser..................")
	       return TRUE
	    endif
	    
	    return FALSE
	endfunc
	
	func AccountTypeForBoost_IsBundleUser()
	    int serviceLevel = AccountTypeForBoost_GetServiceLevel()
	    if 0 == serviceLevel
	       return FALSE
	    endif
        
        if <%=Constant.SERVICE_LEVEL_BOUNDLE%> <= serviceLevel && serviceLevel <  <%=Constant.SERVICE_LEVEL_PREMIUM_FREE%>
	       println("isBundleUser..................")
	       return TRUE
	    endif
	    
	    return FALSE
	endfunc
	
	func AccountTypeForBoost_IsFreeTrial()
	    int serviceLevel = AccountTypeForBoost_GetServiceLevel()
	    if 0 == serviceLevel
	       return FALSE
	    endif
        
        if <%=Constant.SERVICE_LEVEL_PREMIUM_FREE%> <= serviceLevel && serviceLevel <  <%=Constant.SERVICE_LEVEL_PREMIUM_PPD%>
	       println("isFreeTrial..................")
	       return TRUE
	    endif
	    
	    return FALSE
	endfunc
	
	func AccountTypeForBoost_GetServiceLevel()
	    TxNode serviceLevelNode = UserInfo.getAccountInfo()
	    #println("get user info from...........")
	    #println(serviceLevelNode)
        int value = 0
        if NULL != serviceLevelNode && TxNode.getValueSize(serviceLevelNode) > 1
           value = TxNode.valueAt(serviceLevelNode, 1)
        endif
       
        #println("service level................"+value)
        return value
	endfunc
	]]>
</tml:script>
