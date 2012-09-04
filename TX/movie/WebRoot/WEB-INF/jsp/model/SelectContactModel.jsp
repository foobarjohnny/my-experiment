<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.browser.movie.Constant" %>

<tml:script language="fscript" version="1">
	<![CDATA[

	    func SelectContact_M_getMandatoryFlag()
	    	return "Y"
	    endfunc

	    func SelectContact_M_getContact() 
	        TxNode node = Cache.getFromTempCache("<%=Constant.StorageKey.SHARE_MOVIE_CONTACT_DEFAULT%>")
			return node
	    endfunc
	    
	    func SelectContact_M_setContact(TxNode contact)
	        Cache.saveToTempCache("<%=Constant.StorageKey.SHARE_MOVIE_CONTACT_DEFAULT%>",contact)
	    endfunc
	    
	]]>
</tml:script>
	