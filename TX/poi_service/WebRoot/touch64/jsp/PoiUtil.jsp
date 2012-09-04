<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<tml:script language="fscript" version="1">
	<%@ include file="StopUtil.jsp"%>
	<![CDATA[
	   func PoiUtil_convertToNodeForResentSearch(JSONObject jo)
            return convertToPoi(jo)
        endfunc
        
        func PoiUtil_replaceString(String wholeString, String oldString, String newString)
            int index = String.find(wholeString, 0, oldString)
       	    if index > 0
       	       int length = String.getLength(wholeString)
       	       String firstPart = String.at(wholeString,0, index)
       	       
       	       int secondIndex = index + String.getLength(oldString)
		       int lastPartLength = length - secondIndex
		       String lastLine = String.at(wholeString, secondIndex, lastPartLength)
		       
		       wholeString = firstPart + newString + lastLine
       	    endif
       	    
       	    return wholeString
        endfunc
        
        func PoiUtil_replaceAllString(String wholeString, String oldString, String newString)
            int index = String.find(wholeString, 0, oldString)
            int length = String.getLength(wholeString)
            String firstPart
            String lastLine
            int secondIndex
            int lastPartLength
       	    while index > 0
       	       length = String.getLength(wholeString)
       	       firstPart = String.at(wholeString,0, index)       	       
       	       secondIndex = index + String.getLength(oldString)
		       lastPartLength = length - secondIndex
		       lastLine = String.at(wholeString, secondIndex, lastPartLength)
		       
		       wholeString = firstPart + newString + lastLine
		       index = String.find(wholeString, 0, oldString)
       	    endwhile
       	    
       	    return wholeString
        endfunc
        
        func PoiUtil_encodeURL(String wholeString)
            
            wholeString = PoiUtil_replaceAllString(wholeString, "%", "%25")
        	wholeString = PoiUtil_replaceAllString(wholeString, ";", "%3B")
        	wholeString = PoiUtil_replaceAllString(wholeString, "?", "%3F")
        	wholeString = PoiUtil_replaceAllString(wholeString, "/", "%2F")
        	wholeString = PoiUtil_replaceAllString(wholeString, ":", "%3A")
        	wholeString = PoiUtil_replaceAllString(wholeString, "#", "%23")
        	wholeString = PoiUtil_replaceAllString(wholeString, "&", "%26")
        	wholeString = PoiUtil_replaceAllString(wholeString, "=", "%3D")
        	wholeString = PoiUtil_replaceAllString(wholeString, "+", "%2B")
        	wholeString = PoiUtil_replaceAllString(wholeString, "$", "%24")
        	wholeString = PoiUtil_replaceAllString(wholeString, ",", "%2C")
        	wholeString = PoiUtil_replaceAllString(wholeString, " ", "%20")
        	wholeString = PoiUtil_replaceAllString(wholeString, "<", "%3C")
        	wholeString = PoiUtil_replaceAllString(wholeString, ">", "%3E")
        	wholeString = PoiUtil_replaceAllString(wholeString, "~", "%7E")
            
            return wholeString
        endfunc
	]]>
</tml:script>