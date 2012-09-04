<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<tml:script language="fscript" version="1">
	<![CDATA[
	    func StartUp_M_setFreeAlertFlag()
	        TxNode valueNode
	        TxNode.addValue(valueNode, 1)
	        Cache.saveCookie("STARTUP_FREE_ALERT_FLAG",valueNode)
	    endfunc
	    
	    func StartUp_M_getFreeAlertFlag()
	        int value = 0
	        TxNode valueNode = Cache.getCookie("STARTUP_FREE_ALERT_FLAG")
	        if NULL != valueNode
	           value = TxNode.valueAt(valueNode, 0)
	        endif
	        
	        return value
	    endfunc
	    
	    func StartUp_M_setExpireAlertFlag()
	        TxNode valueNode
	        TxNode.addValue(valueNode, 1)
	        Cache.saveCookie("STARTUP_EXPIRE_ALERT_FLAG",valueNode)
	    endfunc
	    
	    func StartUp_M_getExpireAlertFlag()
	        int value = 0
	        TxNode valueNode = Cache.getCookie("STARTUP_EXPIRE_ALERT_FLAG")
	        if NULL != valueNode
	           value = TxNode.valueAt(valueNode, 0)
	        endif
	        
	        return value
	    endfunc
	    
	    func StartUp_M_getSyncResourceFlag()
	        TxNode data = Cache.getCookie("syncResouceFlag")
	        return data
	    endfunc
	    
	    func StartUp_M_saveSyncResourceFlag(TxNode data)
	        Cache.saveCookie("syncResouceFlag",data)
	    endfunc
	    
	    func StartUp_M_getAppNewFlag()
	        int flag = 0
	    	TxNode node = Startup.getFlagInfos()
	    	if node != NULL
	    		int size = TxNode.getStringSize(node)
	    		if size > 6
	    			string currentAppVersion = StartUp_M_checkNULL(TxNode.msgAt(node,5))
	    			TxNode newAppVersionNode = System.getServerParam("NEW_APP_VERSION")
	    			string newAppVersion = ""
	    			if newAppVersionNode != NULL
		    			newAppVersion = StartUp_M_checkNULL(TxNode.msgAt(newAppVersionNode,0))  
		    		endif
	    			
	    			if newAppVersion != currentAppVersion
	    				flag = 1
	    			endif 
	    		endif  
	    	endif
	        return flag
	    endfunc
	    
	    func StartUp_M_clearAppNewFlag()
			TxNode node = Startup.getFlagInfos()
	    	if node != NULL
	    		int size = TxNode.getStringSize(node)
	    		if size > 6
	    			TxNode newAppVersionNode = System.getServerParam("NEW_APP_VERSION")
	    			string newAppVersion = ""
	    			if newAppVersionNode != NULL
		    			newAppVersion = StartUp_M_checkNULL(TxNode.msgAt(newAppVersionNode,0))  
		    		endif
	    			Startup.setAppVersion(newAppVersion)
	    		endif 
	    	endif	    	
	    endfunc  	    

	    func StartUp_M_getUpgradeFlag()
	    	int flag = 0
	    	
    		TxNode node = Startup.getFlagInfos()
	    	if node != NULL
	    		int size = TxNode.getStringSize(node)
	    		if size > 3
	    			string value = TxNode.msgAt(node,3)
	    			if "OPTIONAL" == value
	    				flag = 1
	    			endif
	    		endif  
	    	endif
   			return flag
	    endfunc

	    func StartUp_M_getUpgradeUrl()
	    	string url = ""
    		TxNode node = Startup.getFlagInfos()
	    	if node != NULL
	    		int size = TxNode.getStringSize(node)
	    		if size > 4
	    			url = TxNode.msgAt(node,4)
	    		endif  
	    	endif   	
	    	return url
	    endfunc
	    	    
		func StartUp_M_checkNULL(string s)
        	if s== NULL
        		return ""
        	else
        		return s	
        	endif
        endfunc
         
	    func StartUp_M_isTurnOnTraffic()
	    	int isSupport = 1
	    	TxNode node = System.getServerParam("TRAFFIC")
	    	if node != NULL
		    	if "0" == TxNode.msgAt(node,0)  
		    		isSupport = 0
		    	endif
	    	endif
	    	return isSupport
	    endfunc        
	]]>
</tml:script>