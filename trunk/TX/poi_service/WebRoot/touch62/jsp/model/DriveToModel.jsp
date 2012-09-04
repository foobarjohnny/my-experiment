<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<tml:script language="fscript" version="1">
	<![CDATA[
	    func DriveTo_M_saveStopType(int stopType)
	        String saveKey="<%=Constant.StorageKey.STOP_TYPE%>"
	        TxNode stopTypeNode
	        TxNode.addValue(stopTypeNode,stopType)
	        Cache.saveToTempCache(saveKey,stopTypeNode)
	    endfunc
	    
	    func DriveTo_M_getStopType()
	        String saveKey="<%=Constant.StorageKey.STOP_TYPE%>"
	        int stopType = <%=Constant.STOP_GENERIC%>
	        TxNode stopTypeNode = Cache.getFromTempCache(saveKey)
	        if NULL != stopTypeNode
	            stopType = TxNode.valueAt(stopTypeNode,0)
	        endif
	        return stopType
	    endfunc	     

		func DriveTo_M_isDoingNav()
			int doing = 0
	        TxNode node = Cache.getFromTempCache("<%=Constant.StorageKey.DRIVETO_DOING_STATUS%>")
	        if NULL != node
	            doing = TxNode.valueAt(node,0)
	        endif
	        return doing
		endfunc
		
		func DriveTo_M_setDoingNav(int showing)
			TxNode node
		    TxNode.addValue(node,showing)
		    Cache.saveToTempCache("<%=Constant.StorageKey.DRIVETO_DOING_STATUS%>",node)
		endfunc

		func DriveTo_M_isFromDSR()
			int fromDSR = 0
	        TxNode node = Cache.getFromTempCache("<%=Constant.StorageKey.DRIVETO_FROM_DSR%>")
	        if NULL != node
	            fromDSR = TxNode.valueAt(node,0)
	        endif
	        return fromDSR
		endfunc
		
		func DriveTo_M_setFromDSR(int fromDSR)
			TxNode node
		    TxNode.addValue(node,fromDSR)
		    Cache.saveToTempCache("<%=Constant.StorageKey.DRIVETO_FROM_DSR%>",node)
		endfunc
		
	    func DriveTo_M_saveAddress(JSONObject jo)
	        Cache.saveToTempCache("<%=Constant.StorageKey.DRIVETO_ADDRESS%>",jo)
	    endfunc

	    func DriveTo_M_getAddress()
	        JSONObject jo = Cache.getJSONObjectFromTempCache("<%=Constant.StorageKey.DRIVETO_ADDRESS%>")
	        return jo
	    endfunc			
	    
        func DriveTo_M_convertStopToNodeForResentSearch(JSONObject jo)
            TxNode node
            TxNode.addMsg(node,"stop")
            
            TxNode stopNode = convertToStop(jo)
            TxNode.addChild(node,stopNode)
            
            return node
        endfunc    
        
        func DriveTo_M_saveNavType(String navType)
            TxNode navTypeNode
            TxNode.addMsg(navTypeNode,navType)
            Cache.saveToTempCache("<%=Constant.StorageKey.NAV_TYPE%>",navTypeNode) 
        endfunc  
        
        func DriveTo_M_getNavType()
            String navType = ""
            TxNode navTypeNode = Cache.getFromTempCache("<%=Constant.StorageKey.NAV_TYPE%>") 
            if NULL != navTypeNode
               navType = TxNode.msgAt(navTypeNode,0)
            endif
            return navType
        endfunc 

        func DriveTo_M_saveAudio(TxNode node)
            Cache.saveToTempCache("<%=Constant.StorageKey.DRIVETO_AUDIO%>",node) 
        endfunc  
        
        func DriveTo_M_getAudio()
            return Cache.getFromTempCache("<%=Constant.StorageKey.DRIVETO_AUDIO%>") 
        endfunc 
        
        func DriveTo_M_saveBackAction(String backAction)
           TxNode backActionNode
           TxNode.addMsg(backActionNode,backAction)
           String saveKey = "<%=Constant.StorageKey.BACK_ACTION_DRIVETO%>"
		   Cache.saveToTempCache(saveKey,backActionNode)
        endfunc
        
        func DriveTo_M_getBackAction()
           String backAction = ""
           String saveKey = "<%=Constant.StorageKey.BACK_ACTION_DRIVETO%>"
           TxNode backActionNode = Cache.getFromTempCache(saveKey)
           if NULL != backActionNode
              backAction = TxNode.msgAt(backActionNode,0)
           endif
           return backAction
        endfunc
        
        func DriveTo_M_deleteBackAction()
           String saveKey = "<%=Constant.StorageKey.BACK_ACTION_DRIVETO%>"
           Cache.deleteFromTempCache(saveKey)
        endfunc
        
        func DriveTo_M_getBackActionForMovie()
	        String backAction = ""
	        String saveKey = "<%=Constant.StorageKey.BACK_ACTION_MOVIE%>"
	        TxNode backActionNode = Cache.getFromTempCache(saveKey)
	        if NULL != backActionNode
	           backAction = TxNode.msgAt(backActionNode,0)
	        endif
	        return backAction
	    endfunc
	     
	    func DriveTo_M_deleteBackActionForMovie()
	        String saveKey = "<%=Constant.StorageKey.BACK_ACTION_MOVIE%>"
	        Cache.deleteFromTempCache(saveKey)
	    endfunc
	]]>
</tml:script>