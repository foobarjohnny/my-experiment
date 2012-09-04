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
			JSONObject jo
			
			JSONObject.put(jo,"typeStop",TxNode.valueAt(node,0))
			JSONObject.put(jo,"lat",TxNode.valueAt(node,1))
			JSONObject.put(jo,"lon",TxNode.valueAt(node,2))
			JSONObject.put(jo,"type",TxNode.valueAt(node,3))
			JSONObject.put(jo,"stopStatus",TxNode.valueAt(node,4))
			JSONObject.put(jo,"isGeocoded",TxNode.valueAt(node,5))
			JSONObject.put(jo,"hashCode",TxNode.valueAt(node,6))
		    JSONObject.put(jo,"poiOrStop", "stop")
			
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
<% if (version > 6.0){ %>		
		func convertToAddress(JSONObject jo, int stopType)
   		    TxNode address
   		    # IDataConstants.TYPE_STOP_POI_WRAPPER = 199				
            TxNode.addValue(address,199)
   		    # poi
   		    if isJsonPoi(jo)
				String label = ""
				String poiName = ""
			    poiName = JSONObject.get(jo,"name")
			    JSONObject stopTmp = JSONObject.get(jo,"stop")
			    if JSONObject.has(stopTmp,"label")
			      label = JSONObject.get(stopTmp,"label")
			    endif
			    if "" == label && "" != poiName
			      JSONObject.put(stopTmp,"label",poiName)
			    endif
        		JSONObject.put(stopTmp,"type",stopType)
				TxNode poi = convertToPoi(jo)
	       		# recent poi - 2
	       		TxNode.addValue(address,2)
				TxNode.addChild(address, poi)
				# lable
				TxNode.addMsg(address, poiName)
			else
        		JSONObject.put(jo,"type",stopType)
				TxNode stop = convertToStop(jo)
	       		# recent stop - 3
	       		TxNode.addValue(address,3)
				TxNode.addChild(address, stop)
			    if JSONObject.has(jo,"label")
			      TxNode.addMsg(address, JSONObject.getString(jo,"label"))
			    endif
   		    endif
   		    
			# status 0 - unchanged
       		TxNode.addValue(address,0)
       		# id
       		TxNode.addValue(address,0)
       		# category
       		TxNode.addValue(address,0)
			return address
		endfunc
<%}else{%>
		func convertToAddress(JSONObject jo, int stopType)
   		    if isJsonPoi(jo)
   		     	String label = ""
				String poiName = JSONObject.get(jo,"name")
				
   		    	jo = JSONObject.get(jo,"stop")
			    if JSONObject.has(jo,"label")
			      label = JSONObject.get(jo,"label")
			    endif
			    
			    if "" == label && "" != poiName
			      JSONObject.put(jo,"label",poiName)
			    endif
			endif
			JSONObject.put(jo,"type",stopType)
			
			return convertToStop(jo)			
		endfunc				
<%}%>		
		func convertToPoi(JSONObject jo)
	        TxNode node
            TxNode.addMsg(node,"supportInfo")
            TxNode.addMsg(node,JSONObject.getString(jo,"name"))
            String phoneNumber = JSONObject.getString(jo,"phoneNumber")
            if NULL == phoneNumber
               phoneNumber = ""
            endif
            TxNode.addMsg(node,phoneNumber)
            
            String distance = JSONObject.get(jo,"distance") + ""
            TxNode.addMsg(node,distance)
            TxNode.addMsg(node,JSONObject.getString(jo,"category"))
            TxNode.addMsg(node,"price")
            TxNode.addMsg(node,"vendorCode")

            # TYPE_POI = 196
            TxNode.addValue(node,196)
            TxNode.addValue(node,0)
            JSONObject stop =  JSONObject.get(jo,"stop")
            TxNode stopNode = convertToStop(stop)
            TxNode.addChild(node,stopNode)
             
            TxNode poiNode
            TxNode.addValue(poiNode,196)
            TxNode.addValue(poiNode,0)
            #fix bug
            int isRatingEnable = 1
			if JSONObject.has(jo,"isRatingEnable")
            	isRatingEnable = JSONObject.get(jo,"isRatingEnable")
            endif
            TxNode.addValue(poiNode,isRatingEnable)
            TxNode.addValue(poiNode,0)
     
            int poiId = JSONObject.get(jo,"poiId")
            TxNode.addValue(poiNode,poiId)
            TxNode.addValue(poiNode,0)
            TxNode.addMsg(poiNode,"poi")
            # add biz poi
            TxNode.addChild(poiNode,node)
            # add extras
        	if JSONObject.has(jo,"ad")
        		JSONObject ad = JSONObject.get(jo,"ad")
        		if (JSONObject.has(ad,"adID") && JSONObject.has(ad,"adSource"))
	        		TxNode adId
	        		# protocol msg0 - key, msg1 - value, val0 - type text 3 
	        		TxNode.addMsg(adId,"adID")
	        		TxNode.addValue(adId, 3)
	        		TxNode.addMsg(adId,JSONObject.getString(ad,"adID"))
	        		TxNode adSource
	        		TxNode.addMsg(adSource,"adSource")
	        		TxNode.addValue(adSource, 3)
	        		TxNode.addMsg(adSource,JSONObject.getString(ad,"adSource"))
	        		TxNode extras
	        		# TYPE_POI_EXTRA_INFO_TAG = 252
	        		TxNode.addValue(extras, 252)
		            TxNode.addChild(extras,adId)
		            TxNode.addChild(extras,adSource)
		            TxNode.addChild(poiNode,extras)
        		endif
        	endif
			return poiNode
		endfunc
		
        func convertPoiToJSON(TxNode poiNode)
            int avgRating = TxNode.valueAt(poiNode,1)
		    int poiId = TxNode.valueAt(poiNode,4)
		    TxNode locationNode = TxNode.childAt(poiNode,0)
		    int distance = TxNode.valueAt(locationNode,2)
		    String distanceStr = distance + ""   
		    
		    String brand = TxNode.msgAt(locationNode,1)
		    String phoneNumber = TxNode.msgAt(locationNode,2)
		    String parentCatName = TxNode.msgAt(locationNode,4)
		      
		    TxNode stopNode = TxNode.childAt(locationNode,0)
		    JSONObject stopJo = convertStopToJSON(stopNode)
		    
	    	JSONObject ad
		    if (TxNode.getChildSize(poiNode) > 1)
			    TxNode extras = TxNode.childAt(poiNode,1)
			    String adID
			    String adSource
			    if TxNode.valueAt(extras,0) == 252
			    	TxNode tmp = TxNode.childAt(extras,0)
			    	setExtrasValue(tmp, ad)
			    	tmp = TxNode.childAt(extras,1)
			    	setExtrasValue(tmp, ad)
			    endif
			endif
		    JSONObject poiJo
		    JSONObject.put(poiJo,"distance",distanceStr)
		    JSONObject.put(poiJo,"rating",avgRating)
		    JSONObject.put(poiJo,"phoneNumber",phoneNumber)
		    JSONObject.put(poiJo,"name",brand)
		    JSONObject.put(poiJo,"poiId",poiId)
		    JSONObject.put(poiJo,"category",parentCatName)
		    JSONObject.put(poiJo,"stop",stopJo)
		    JSONObject.put(poiJo,"ad",ad)
		    JSONObject.put(poiJo,"poiOrStop","poi")
		    
		    return poiJo
        endfunc
        
        func setExtrasValue(TxNode extra, JSONObject jo)
        	String key = TxNode.msgAt(extra,0)
        	String value = TxNode.msgAt(extra,1)
        	JSONObject.put(jo, key, value)
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
   		    if isJsonPoi(jo)
   		    	node = convertToPoi(jo)
   		    else
	            TxNode.addMsg(node,"stop")
	            TxNode stopNode = convertToStop(jo)
            	TxNode.addChild(node,stopNode)
            endif
            RecentPlaces.saveAddress(node)
        endfunc
        
        func isTxNodePoi(TxNode node)
   		    if TxNode.valueAt(node,0) == 196
   		    	return TRUE
			endif
			return FALSE        	
        endfunc
        
        func isJsonPoi(JSONObject jo)
   		    if (NULL != JSONObject.get(jo,"poiOrStop") && "poi" == JSONObject.get(jo,"poiOrStop")) || NULL != JSONObject.get(jo,"poiId")
   		    	return TRUE
			endif
			return FALSE        	
        endfunc
		]]>
	</tml:script>
