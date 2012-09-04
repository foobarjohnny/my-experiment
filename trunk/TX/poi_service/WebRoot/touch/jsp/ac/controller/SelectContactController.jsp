<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="/touch/jsp/Header.jsp"%>	
<%@include file="../model/SelectContactModel.jsp"%>	
<tml:script language="fscript" version="1">
	<![CDATA[
        func SelectContact_C_show(JSONObject jo)
        	string fromPage = "contact"
			string type = JSONObject.getString(jo,"type")
			if type == NULL
				type = ""
			endif
			
			if type == "1"
				fromPage = "ac"     
			elsif type == "2"
				fromPage = "contactback"	     	
            endif
            
            JSONObject.put(jo,"fromPage",fromPage)
            SelectContact_M_saveParameter(jo)
            if fromPage == "contact"
            	SelectContact_M_clear()
            endif
                    	
            string pageUrl = "<%=getPageCallBack%>" + "SelectContact#" + fromPage
			MenuItem.setAttribute("selectContact","url",pageUrl)
            System.doAction("selectContact")
            return FAIL
        endfunc
        
        func SelectContact_C_showWithDefault(JSONObject jo,TxNode contact)
        	string fromPage = "contact"
			string type = JSONObject.getString(jo,"type")
			if type == NULL
				type = ""
			endif
			
			if type == "1"
				fromPage = "ac"     
			elsif type == "2"
				fromPage = "contactback"	     	
            endif
            JSONObject.put(jo,"fromPage",fromPage)
            SelectContact_M_saveParameter(jo)
			SelectContact_M_setContact(contact)
                    	
            string pageUrl = "<%=getPageCallBack%>" + "SelectContact#" + fromPage
			MenuItem.setAttribute("selectContact","url",pageUrl)
            System.doAction("selectContact")
            return FAIL
        endfunc
	]]>
</tml:script>
<tml:menuItem name="selectContact" pageURL=""/>
