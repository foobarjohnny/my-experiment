<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>

<tml:script language="fscript" version="1">
	<![CDATA[
	    func Pref_M_getPrefValue(int key)
	    	int value = 0
			TxNode node = Preference.getPreferenceValue(key)

			if node != NULL
				value = TxNode.valueAt(node,0) 
			endif
			
	    	return value	        
	    endfunc	

<%if(featureMgr.isEnabled(FeatureConstant.DSR)){%>
	    func Pref_M_getAddressInput()
	    	int type = Pref_M_getPrefValue(<%=PreferenceConstants.KEY_SPEECHINPUT_ADDRESSINPUT%>)
	    	return type
	    endfunc
	   
	   	func Pref_M_getSearchInput()
	    	int type = Pref_M_getPrefValue(<%=PreferenceConstants.KEY_SPEECHINPUT_SEARCHINPUT%>)
	    	return type
	    endfunc	 	    

	    func Pref_M_getSpeechInput()
	    	int type = Pref_M_getPrefValue(<%=PreferenceConstants.KEY_SPEECHINPUT_SPEECHINPUT%>)
	    	return type
	    endfunc

	    func Pref_M_getAnounceSearchResult()
	    	int type = Pref_M_getPrefValue(<%=PreferenceConstants.KEY_SPEECHINPUT_ANOUNCESEARCHRESULT%>)
	    	return type
	    endfunc
<%}else{%>
	    func Pref_M_getAddressInput()
	    	return <%=PreferenceConstants.VALUE_NOT_AVAIL%>
	    endfunc
	   
	   	func Pref_M_getSearchInput()
	    	return <%=PreferenceConstants.VALUE_NOT_AVAIL%>
	    endfunc	 	    

	    func Pref_M_getSpeechInput()
	    	return <%=PreferenceConstants.VALUE_NOT_AVAIL%>
	    endfunc

	    func Pref_M_getAnounceSearchResult()
	    	return <%=PreferenceConstants.VALUE_NOT_AVAIL%>
	    endfunc
<%}%>


	   	func Pref_M_getRouteStyle()
	    	int type = Pref_M_getPrefValue(<%=PreferenceConstants.KEY_NAVIGATION_ROUTESTYLE%>)
	    	return type
	    endfunc

        func Pref_M_getLocale()
        	string locale = ""
        	TxNode node = Preference.getPreferenceValue(9)
			if node != NULL
				if TxNode.getStringSize(node) > 0
					locale = TxNode.msgAt(node,0) 
				endif
			endif
			if locale == NULL || locale==""
				locale = "en_US"
			endif
			return locale
        endfunc
	]]>
</tml:script>