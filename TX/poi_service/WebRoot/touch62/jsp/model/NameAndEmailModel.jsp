<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<tml:script language="fscript" version="1">
	<![CDATA[
	    func NameAndEmail_M_saveCallBackUrl(String url)
	        TxNode urlNode
	        TxNode.addMsg(urlNode,url)
	        
	        String saveKey = "<%=Constant.StorageKey.NAMEANDEMAIL_CALLBACK_URL%>"
			Cache.saveToTempCache(saveKey,urlNode)
	    endfunc
	    
	    func NameAndEmail_M_getCallBackUrl()
	        String saveKey = "<%=Constant.StorageKey.NAMEANDEMAIL_CALLBACK_URL%>"
			TxNode urlNode = Cache.getFromTempCache(saveKey)
			
			String url = ""
			if NULL != urlNode
			   url = TxNode.msgAt(urlNode,0)
			endif
			  
			return url
	    endfunc
	    
	    func NameAndEmail_M_saveCallBackFunc(String backFunc)
	        TxNode backFuncNode
	        TxNode.addMsg(backFuncNode,backFunc)
	        
	        String saveKey = "<%=Constant.StorageKey.NAMEANDEMAIL_CALLBACK_FUNC%>"
			Cache.saveToTempCache(saveKey,backFuncNode)
	    endfunc
	    
	    func NameAndEmail_M_getCallBackFunc()
	        String saveKey = "<%=Constant.StorageKey.NAMEANDEMAIL_CALLBACK_FUNC%>"
			TxNode funcNode = Cache.getFromTempCache(saveKey)
			
			String funcName = ""
			if NULL != funcNode
			   funcName = TxNode.msgAt(funcNode,0)
			endif
			
			return funcName
	    endfunc
	    
	    func NameAndEmail_M_saveDonotAskAgain(String type)
	        TxNode node
	        TxNode.addValue(node,1)
	        String saveKey = "<%=Constant.StorageKey.DONOT_ASK_AGAIN%>" + "_" + type
	        Cache.saveCookie(saveKey,node)
	    endfunc
	    
	    func NameAndEmail_M_getDonotAskAgain(String type)
	        int flag = 0
	        String saveKey = "<%=Constant.StorageKey.DONOT_ASK_AGAIN%>" + "_" + type
	        TxNode node = Cache.getCookie(saveKey)
	        if NULL != node
	           flag = TxNode.valueAt(node,0);
	        endif
	        
	        return flag
	    endfunc
	    
	    func NameAndEmail_M_saveSource(String source)
	        TxNode node
	        TxNode.addMsg(node,source)
	        String saveKey = "<%=Constant.StorageKey.SOURCE_FOR_NAMEANDEMAIL%>"
	        Cache.saveCookie(saveKey,node)
	    endfunc
	    
	    func NameAndEmail_M_getSource()
	        String source = ""
	        String saveKey = "<%=Constant.StorageKey.SOURCE_FOR_NAMEANDEMAIL%>"
	        TxNode node = Cache.getCookie(saveKey)
	        if NULL != node
	           source = TxNode.msgAt(node,0);
	        endif
	        
	        return source
	    endfunc
	    
	    func NameAndEmail_M_goBack()
	        String url = NameAndEmail_M_getCallBackUrl()
	        String funcName = NameAndEmail_M_getCallBackFunc()
	        TxNode funcNameNode
	        TxNode.addMsg(funcNameNode,funcName)
	        
	        MenuItem.setBean("goBack","callFunction",funcNameNode)
	        MenuItem.setAttribute("goBack","url",url)
		    System.doAction("goBack")
	    endfunc
	    
	    
	]]>
</tml:script>

<tml:menuItem name="goBack" />
