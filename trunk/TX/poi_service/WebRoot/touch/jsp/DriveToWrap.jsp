<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="Header.jsp"%>
<tml:TML outputMode="TxNode">
	<jsp:include page="/touch/jsp/DriveToCommonInclude.jsp" />
	<tml:script language="fscript" version="1">
		<![CDATA[
		func CallBack_SelectAddress()
			DriveTo_M_setDoingNav(0)
			DriveTo_M_setFromDSR(0)
        	TxNode node
        	node = ParameterSet.getParam("returnAddress")
        	if NULL != node
        	    int size = TxNode.getStringSize(node)
        	    JSONObject jo
        	    if size > 1
        	       jo = JSONObject.fromString(TxNode.msgAt(node,1))
        	    else
        	       jo = JSONObject.fromString(TxNode.msgAt(node,0))
        	    endif
       		    DriveTo_M_saveAddress(jo)
        	endif
        endfunc

        func doBack()
        	System.back()
			return FAIL
        endfunc
        
        func BackToNav()
        	System.doAction("backToDriveToMenu")
        	return FAIL
        endfunc
        
        func goToRestoreNav()
            System.doAction("goToRestoreNav")
            return FAIL
        endfunc
	]]>
	</tml:script>
	<tml:page id="DriveToWrap" url="<%=getPage+ "DriveToWrap"%>"
		type="<%=pageType%>" helpMsg="" groupId="<%=GROUP_ID_COMMOM%>">
	</tml:page>
</tml:TML>
