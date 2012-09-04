<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ include file="../Header.jsp"%>
<%@page import="com.telenav.browser.movie.Constant" %>

<tml:script language="fscript" version="1">
		<![CDATA[
		    func ShareMovie_C_ShareMovieInterface(TxNode movieName, TxNode theaterName, TxNode theaterAddress)
				JSONObject jo
			    String tmp = TxNode.msgAt(movieName, 0)
		    	JSONObject.put(jo,"<%= Constant.RRKey.M_NAME%>",tmp)
		    	tmp = TxNode.msgAt(theaterName, 0)
			    JSONObject.put(jo,"<%= Constant.RRKey.M_THEATER_NAME%>",tmp)
		    	tmp = TxNode.msgAt(theaterAddress, 0)
		    	JSONObject.put(jo,"<%= Constant.RRKey.M_THEATER_ADDRESS%>",tmp)
		    	ShareMovie_M_saveParams(jo)
	            ShareMovie_M_removePhone()
	            ShareMovie_M_removeRecipients()
	            ShareMovie_M_removeContact()
		    	System.doAction("shareMovie")
		    endfunc
		]]>
</tml:script>

<tml:menuItem name="shareMovie"	pageURL="<%=getPage + "ShareMovie"%>">
</tml:menuItem>
