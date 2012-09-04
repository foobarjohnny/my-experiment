<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="../model/NameAndEmailModel.jsp"%>
<%@ include file="../GetClientInfo.jsp"%>
<%@ include file="../Header.jsp"%>
<%@ include file="../model/PrefModel.jsp"%>
<tml:script language="fscript" version="1">
	<![CDATA[
	    func NameAndEmail_C_saveCallBackUrl(String url)
	        NameAndEmail_M_saveCallBackUrl(url)
	    endfunc
	    
	    func NameAndEmail_C_getCallBackUrl()
	        return NameAndEmail_M_getCallBackUrl()
	    endfunc
	    
	    func NameAndEmail_C_getDonotAskAgain(String source)
	        return NameAndEmail_M_getDonotAskAgain(source)
	    endfunc
	    
	    func NameAndEmail_C_initialAndGoToJsp(String backUrl, String backFunc,String source)
	        NameAndEmail_M_saveCallBackUrl(backUrl)
	        NameAndEmail_M_saveCallBackFunc(backFunc)
            NameAndEmail_M_saveSource(source)
            String url = "<%=getPageWithLocale1%>" + Pref_M_getLocale() + "<%=getPageWithLocale2%>" + "NameAndEmail"
            MenuItem.setAttribute("nameAndEmail","url",url)
            System.doAction("nameAndEmail")
            return FAIL
	    endfunc
	]]>
</tml:script>

<tml:menuItem name="nameAndEmail"
	pageURL="<%=getPageWithLocale + "NameAndEmail"%>">
</tml:menuItem>