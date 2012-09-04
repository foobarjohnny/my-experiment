<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
        func convertToStop(JSONObject jo)
        	TxNode node
        	if JSONObject.getInt(jo,"typeStop") != NULL
        		TxNode.addValue(node,JSONObject.getInt(jo,"typeStop"))
        	else
        		TxNode.addValue(node,0)
        	endif	

        	if JSONObject.getInt(jo,"lat") != NULL
        		TxNode.addValue(node,JSONObject.getInt(jo,"lat"))
        	else
        		TxNode.addValue(node,0)
        	endif	

        	if JSONObject.getInt(jo,"lon") != NULL
        		TxNode.addValue(node,JSONObject.getInt(jo,"lon"))
        	else
        		TxNode.addValue(node,0)
        	endif

        	if JSONObject.getInt(jo,"type") != NULL
        		TxNode.addValue(node,JSONObject.getInt(jo,"type"))
        	else
        		TxNode.addValue(node,0)
        	endif
        	
        	if JSONObject.getInt(jo,"stopStatus") != NULL
        		TxNode.addValue(node,JSONObject.getInt(jo,"stopStatus"))
        	else
        		TxNode.addValue(node,0)
        	endif

        	if JSONObject.getInt(jo,"isGeocoded") != NULL
        		TxNode.addValue(node,JSONObject.getInt(jo,"isGeocoded"))
        	else
        		TxNode.addValue(node,0)
        	endif
        	if JSONObject.getInt(jo,"hashCode") != NULL
        		TxNode.addValue(node,JSONObject.getInt(jo,"hashCode"))
        	else
        		TxNode.addValue(node,0)
        	endif
        	if JSONObject.getString(jo,"label") != NULL
        		TxNode.addMsg(node,JSONObject.getString(jo,"label"))
        	else
        		TxNode.addMsg(node,"")
        	endif
        	        	
        	if JSONObject.getString(jo,"firstLine") != NULL
        		TxNode.addMsg(node,JSONObject.getString(jo,"firstLine"))
        	else
        		TxNode.addMsg(node,"")
        	endif
        	if JSONObject.getString(jo,"city") != NULL
        		TxNode.addMsg(node,JSONObject.getString(jo,"city"))
        	else
        		TxNode.addMsg(node,"")
        	endif      	
        	if JSONObject.getString(jo,"state") != NULL
        		TxNode.addMsg(node,JSONObject.getString(jo,"state"))
        	else
        		TxNode.addMsg(node,"")
        	endif

        	if JSONObject.getString(jo,"stopId") != NULL
        		TxNode.addMsg(node,JSONObject.getString(jo,"stopId"))
        	else
        		TxNode.addMsg(node,"")
        	endif
        	if JSONObject.getString(jo,"zip") != NULL
        		TxNode.addMsg(node,JSONObject.getString(jo,"zip"))
        	else
        		TxNode.addMsg(node,"")
        	endif
        	if JSONObject.getString(jo,"country") != NULL
        		TxNode.addMsg(node,JSONObject.getString(jo,"country"))
        	else
        		TxNode.addMsg(node,"")
        	endif
        	return node
        endfunc
	        
 		func convertStopToJSON(TxNode node)
 		    if NULL == node
 		       return NULL
 		    endif
 		    
			JSONObject jo
			
			JSONObject.put(jo,"typeStop",TxNode.valueAt(node,0))
			JSONObject.put(jo,"lat",TxNode.valueAt(node,1))
			JSONObject.put(jo,"lon",TxNode.valueAt(node,2))
			JSONObject.put(jo,"type",TxNode.valueAt(node,3))
			JSONObject.put(jo,"stopStatus",TxNode.valueAt(node,4))
			JSONObject.put(jo,"isGeocoded",TxNode.valueAt(node,5))
			JSONObject.put(jo,"hashCode",TxNode.valueAt(node,6))
			
			if TxNode.getStringSize(node) > 0	
				JSONObject.put(jo,"label",checkNULL(TxNode.msgAt(node,0)))
				JSONObject.put(jo,"firstLine",checkNULL(TxNode.msgAt(node,1)))
				JSONObject.put(jo,"city",checkNULL(TxNode.msgAt(node,2)))
				JSONObject.put(jo,"state",checkNULL(TxNode.msgAt(node,3)))
				JSONObject.put(jo,"stopId",checkNULL(TxNode.msgAt(node,4)))
				JSONObject.put(jo,"zip",checkNULL(TxNode.msgAt(node,5)))
				JSONObject.put(jo,"country",checkNULL(TxNode.msgAt(node,6)))
				JSONObject.put(jo,"lastLine","")
			else
				JSONObject.put(jo,"label","")
				JSONObject.put(jo,"firstLine","")
				JSONObject.put(jo,"city","")
				JSONObject.put(jo,"state","")
				JSONObject.put(jo,"stopId","")
				JSONObject.put(jo,"zip","")
				JSONObject.put(jo,"country","")
				JSONObject.put(jo,"lastLine","")					
			endif
        	return jo			
		endfunc
		
		func checkNULL(string s)
        	if s== NULL
        		return ""
        	else
        		return s	
        	endif
        endfunc   

		func isCurrentStop(JSONObject jo)
			if jo == NULL
				return TRUE
			endif
			int type = JSONObject.getInt(jo,"type")
			if 6==type
				return TRUE
			else
				return FALSE
			endif
		endfunc

        func RecentPlace_saveAddress(JSONObject jo)
            TxNode node
            TxNode.addMsg(node,"stop")
            
            TxNode stopNode = convertToStop(jo)
            TxNode.addChild(node,stopNode)
            
            RecentPlaces.saveAddress(node)
        endfunc
		]]>
	</tml:script>
