<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.browser.framework.BrowserFrameworkConstants"%>
<%@page import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>
<%@ include file="/touch62/jsp/Header.jsp"%>
<%@ include file="/touch62/jsp/ac/model/SelectAddressModel.jsp"%>	
<tml:script language="fscript" version="1">
	<![CDATA[
        func SelectAddress_C_SelectAddress(JSONObject jo)
			string from = JSONObject.getString(jo,"from")
	        if from==NULL || from ==""
	        	from = "Common"
	        	JSONObject.put(jo,"from",from)
	        endif
        	SelectAddress_M_saveParameter(jo)	        
			string pageUrl = "<%=getAcPageCallBack%>" + "SelectAddress#" + from
			MenuItem.setAttribute("selectAddress","url",pageUrl)
            System.doAction("selectAddress")
            return FAIL
        endfunc
        
        func SelectAddress_C_reSaveParameterWhenBack(JSONObject jo)
			string from = JSONObject.getString(jo,"from")
	        if from==NULL || from ==""
	        	from = "Common"
	        	JSONObject.put(jo,"from",from)
	        endif
        	SelectAddress_M_saveParameter(jo)	        
        endfunc
        
        func SelectAddress_C_getMaskForFavorite()
	    	return SelectAddress_M_getMaskForFavorite()
	    endfunc
	    
        func SelectAddress_C_isReturnAsIs()
	    	return SelectAddress_M_isReturnAsIs()
	    endfunc
        
        func SelectAddress_C_favoriteInitial()
            AddressCapture_M_deleteFavoritesStatus()
        endfunc
        
        func SelectAddress_C_SetNoCoverage(int flag)
        	SelectAddress_M_SetNoCoverage(flag)
        endfunc
	]]>
</tml:script>
<tml:menuItem name="selectAddress" pageURL=""/>