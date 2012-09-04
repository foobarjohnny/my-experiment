<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.browser.framework.BrowserFrameworkConstants"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>	
<tml:script language="fscript" version="1">
	<![CDATA[
		    func AddressCapture_M_getInstance()
		        String saveKey = "<%=Constant.StorageKey.ADDRESS_CAPTURE_TYPE%>"
				return AddressCapture_M_getString(saveKey)
		    endfunc
		    
		    func AddressCapture_M_getLocation()
		        String saveKey = "<%=Constant.StorageKey.ADDRESS_CAPTURE_LOCATION%>" + "-" + AddressCapture_M_getInstance()
				TxNode node = Cache.getFromTempCache(saveKey)
				return node
		    endfunc
		    
		    func AddressCapture_M_getCountry()
		        String saveKey = "<%=Constant.StorageKey.ADDRESS_CAPTURE_COUNTRY%>" + "-" + AddressCapture_M_getInstance()
				return AddressCapture_M_getString(saveKey)
		    endfunc
		    
		    func AddressCapture_M_getErrorMsg()
			    String saveKey = "<%=Constant.StorageKey.ADDRESS_CAPTURE_NOT_FOUND%>" + "-" + AddressCapture_M_getInstance()
				return AddressCapture_M_getString(saveKey)
			endfunc
			
			func AddressCapture_M_saveInvoker(String s)
				 String saveKey="<%=Constant.StorageKey.ADDRESS_CAPTURE_INVOKER%>"
				 AddressCapture_M_saveString(saveKey,s)
			endfunc
			
			func AddressCapture_M_saveCallbackFunc(String s)
				 String saveKey="<%=Constant.StorageKey.ADDRESS_CAPTURE_CALLBACK_FUNC%>"
			   	 AddressCapture_M_saveString(saveKey,s)
			endfunc
			
			func AddressCapture_M_initType(String s)
			    String saveKey="<%=Constant.StorageKey.ADDRESS_CAPTURE_TYPE%>"
			    AddressCapture_M_saveString(saveKey,s)
			endfunc
			
			func AddressCapture_M_saveLocation(TxNode location)
		    	if NULL != location
			        String saveKey = "<%=Constant.StorageKey.ADDRESS_CAPTURE_LOCATION%>" + "-" + AddressCapture_M_getType()
					Cache.saveToTempCache(saveKey,location)
			    endif
			endfunc
			
			func AddressCapture_M_saveCountry(String s)
			    String saveKey="<%=Constant.StorageKey.ADDRESS_CAPTURE_COUNTRY%>" + "-" + AddressCapture_M_getType()
			    AddressCapture_M_saveString(saveKey,s)
			endfunc
			
			func AddressCapture_M_saveStreet1(String s)
				String saveKey="<%=Constant.StorageKey.ADDRESS_CAPTURE_STREET1%>" + "-" + AddressCapture_M_getType()
			    AddressCapture_M_saveString(saveKey,s)
			endfunc
			
			func AddressCapture_M_saveStreet2(String s)
				String saveKey="<%=Constant.StorageKey.ADDRESS_CAPTURE_STREET2%>" + "-" + AddressCapture_M_getType()
			    AddressCapture_M_saveString(saveKey,s)
			endfunc
			
			func AddressCapture_M_saveCity(String s)
				String saveKey="<%=Constant.StorageKey.ADDRESS_CAPTURE_CITY%>" + "-" + AddressCapture_M_getType()
			    AddressCapture_M_saveString(saveKey,s)   
			endfunc
			
			func AddressCapture_M_saveState(String s)
			    String saveKey="<%=Constant.StorageKey.ADDRESS_CAPTURE_STATE%>" + "-" + AddressCapture_M_getType()
			    AddressCapture_M_saveString(saveKey,s)
			endfunc
			
			func AddressCapture_M_saveZip(String s)
				String saveKey="<%=Constant.StorageKey.ADDRESS_CAPTURE_ZIP%>" + "-" + AddressCapture_M_getType()
				AddressCapture_M_saveString(saveKey,s)
			endfunc
			
			func AddressCapture_M_saveErrorMsg()
				String saveKey = "<%=Constant.StorageKey.ADDRESS_CAPTURE_NOT_FOUND%>" + "-" + AddressCapture_M_getType()
				AddressCapture_M_saveString(saveKey,"Address Not Found")
			endfunc
			
			# No prefix indicate that it's a private method
			func AddressCapture_M_getString(String saveKey)
				TxNode node = Cache.getFromTempCache(saveKey)
				String s = ""
				if NULL != node
					int size = TxNode.getStringSize(node)
					if 0 < size
						s = TxNode.msgAt(node,0)
					endif
				endif
				return s
			endfunc
			
			# No prefix indicate that it's a private method
			func AddressCapture_M_getType()
				String saveKey = "<%=Constant.StorageKey.ADDRESS_CAPTURE_TYPE%>"
				TxNode node = Cache.getFromTempCache(saveKey)
				String s = ""
				if NULL != node
					int size = TxNode.getStringSize(node)
					if 0 < size
						s = TxNode.msgAt(node,0)
					endif
				endif
				return s
			endfunc
		    
		    func AddressCapture_M_getInvoker()
		    	String s = ""
		    	JSONObject jo = AddressCapture_M_getParameter()
		    	if jo != NULL
		    		s = JSONObject.getString(jo,"callbackpageurl")
				endif
				return s
			endfunc
			
			func AddressCapture_M_getCallbackFunc()
				String s = ""
				JSONObject jo = AddressCapture_M_getParameter()
				if jo != NULL
					s = JSONObject.getString(jo,"callbackfunction")
				endif
				
				return s
			endfunc
			
		    func AddressCapture_M_isReturnAsIs()
		        JSONObject jo = AddressCapture_M_getParameter()
				if JSONObject.has(jo, "returnAsIs")
					return TRUE
				endif
				return FALSE	    
		    endfunc
			
			func AddressCapture_M_saveString(String saveKey,String s)
			    if NULL != s
			        if "" != s
			           TxNode node
			           TxNode.addMsg(node,s)
			           Cache.saveToTempCache(saveKey,node)
			        endif
			    endif
			endfunc
			
			func AddressCapture_M_init(String invokerPageURL, String callbackFunction, String from)
				 AddressCapture_M_initAsIs(invokerPageURL, callbackFunction, from, 0)
			endfunc
			
			func AddressCapture_M_initAsIs(String invokerPageURL, String callbackFunction, String from, int asIs)
				if from == ""
					from = "Common"
				endif
				JSONObject joTemp
				JSONObject joTemp1 = Cache.getJSONObjectFromTempCache("<%=Constant.StorageKey.ADDRESS_CAPTURE_PARAMETER%>")
				if joTemp1 != NULL
					joTemp = joTemp1
				endif
	
				JSONObject jo
				JSONObject.put(jo,"callbackfunction",callbackFunction)
				JSONObject.put(jo,"callbackpageurl",invokerPageURL)
				if asIs > 0
					JSONObject.put(jo,"returnAsIs","1")
				endif
		    	JSONObject.put(joTemp,from,jo)
		    	
		        Cache.saveToTempCache("<%=Constant.StorageKey.ADDRESS_CAPTURE_PARAMETER%>",joTemp)
			endfunc
			
			func AddressCapture_M_getParameter()
		        JSONObject joTemp = Cache.getJSONObjectFromTempCache("<%=Constant.StorageKey.ADDRESS_CAPTURE_PARAMETER%>")
		        if joTemp != NULL
			        JSONObject jo = JSONObject.get(joTemp,AddressCapture_M_getFrom())
			    	return jo
		    	else
		    		return NULL
		    	endif			
			endfunc
			
			func AddressCapture_M_getFrom()
				string from = Page.getControlProperty("page","url_flag")
				if from == NULL || from ==""
					from = "Common"
				endif
		    	return from
			endfunc
			
			func AddressCapture_M_city(JSONObject jo)
				string invokerPageURL = JSONObject.getString(jo,"callbackpageurl")
				string callbackFunction = JSONObject.getString(jo,"callbackfunction")
				string from = JSONObject.getString(jo,"from")
		        if from==NULL || from ==""
		        	from = "Common"
		        endif
				string pageUrl = "<%=getAcPageCallBack%>" + "TypeCity#" + from
				MenuItem.setAttribute("AddressCapture_Action_ac","url",pageUrl)	        
				AddressCapture_M_init(invokerPageURL,callbackFunction,from)
				System.doAction("AddressCapture_Action_ac")
			endfunc
			
			func AddressCapture_M_address(JSONObject jo)
				string invokerPageURL = JSONObject.getString(jo,"callbackpageurl")
				string callbackFunction = JSONObject.getString(jo,"callbackfunction")
				string from = JSONObject.getString(jo,"from")
		        if from==NULL || from ==""
		        	from = "Common"
		        endif
				string pageUrl = "<%=getAcPageCallBack%>" + "TypeAddress#" + from
				MenuItem.setAttribute("AddressCapture_Action_ac","url",pageUrl)
				AddressCapture_M_init(invokerPageURL,callbackFunction,from)
				System.doAction("AddressCapture_Action_ac")
			endfunc
			
			func AddressCapture_M_intersection(JSONObject jo)
				string invokerPageURL = JSONObject.getString(jo,"callbackpageurl")
				string callbackFunction = JSONObject.getString(jo,"callbackfunction")
				string from = JSONObject.getString(jo,"from")
		        if from==NULL || from ==""
		        	from = "Common"
		        endif
				string pageUrl = "<%=getAcPageCallBack%>" + "TypeIntersection#" + from
				MenuItem.setAttribute("AddressCapture_Action_ac","url",pageUrl)
				AddressCapture_M_init(invokerPageURL,callbackFunction,from)
				System.doAction("AddressCapture_Action_ac")
			endfunc
			
			func AddressCapture_M_airport(JSONObject jo)
				string invokerPageURL = JSONObject.getString(jo,"callbackpageurl")
				string callbackFunction = JSONObject.getString(jo,"callbackfunction")
				string from = JSONObject.getString(jo,"from")
		        if from==NULL || from ==""
		        	from = "Common"
		        endif
				string pageUrl = "<%=host + "/FindAirPort.do#"%>" + from
				MenuItem.setAttribute("AddressCapture_Action_ac","url",pageUrl)
				AddressCapture_M_init(invokerPageURL,callbackFunction,from)
				System.doAction("AddressCapture_Action_ac")
			endfunc
			
			func AddressCapture_M_recentPlace(JSONObject jo)
				string invokerPageURL = JSONObject.getString(jo,"callbackpageurl")
				string callbackFunction = JSONObject.getString(jo,"callbackfunction")
				string from = JSONObject.getString(jo,"from")
				if JSONObject.has(jo, "returnAsIs")
					AddressCapture_M_initAsIs(invokerPageURL,callbackFunction,from,1)
				else
					AddressCapture_M_init(invokerPageURL,callbackFunction,from)
				endif
				
				string pageUrl = "<%=getPageCallBack%>" + "RecentPlaces#" + from
			    MenuItem.setAttribute("AddressCapture_Action_recentPlace","url",pageUrl)
				System.doAction("AddressCapture_Action_recentPlace")
			endfunc
			
			func AddressCapture_M_favorites(JSONObject jo)
				string invokerPageURL = JSONObject.getString(jo,"callbackpageurl")
				string callbackFunction = JSONObject.getString(jo,"callbackfunction")
				string from = JSONObject.getString(jo,"from")
        	
			    AddressCapture_M_deleteFavoritesStatus()
				if JSONObject.has(jo, "returnAsIs")
					AddressCapture_M_initAsIs(invokerPageURL,callbackFunction,from,1)
				else
					AddressCapture_M_init(invokerPageURL,callbackFunction,from)
				endif
			    AddressCapture_M_saveFavoriteType("")
			    
			    string pageUrl = "<%=getPageCallBack%>" + "MyFavorites#" + from
			    MenuItem.setAttribute("AddressCapture_Action_favorites","url",pageUrl)
				System.doAction("AddressCapture_Action_favorites")
			endfunc
						
			func AddressCapture_M_showAddressCapturePage()
				MenuItem.setAttribute("showAddressCapturePage","url",AddressCapture_M_getBackUrlForSelectCountry())
				System.doAction("showAddressCapturePage")
			endfunc
			
			func AddressCapture_M_returnAddressToInvokerPage(JSONObject jo)
				if NULL != jo
					#RecentPlace_saveAddress(jo)
					
					AddressCapture_M_returnAddressToInvokerPageWithoutSaveToRecentPlace(jo)
				endif
			endfunc

			func AddressCapture_M_returnAddressToInvokerPageWithoutSaveToRecentPlace(JSONObject jo)
				if NULL != jo
					String invoker = AddressCapture_M_getInvoker()
					String callbackFunc = AddressCapture_M_getCallbackFunc()
					TxNode node
					TxNode.addMsg(node,callbackFunc)
					TxNode addressNode
					TxNode.addMsg(addressNode,JSONObject.toString(jo))
					
					MenuItem.setAttribute("returnAddressToInvokerPage","url",invoker)
					MenuItem.setBean("returnAddressToInvokerPage","callFunction",node)
					MenuItem.setBean("returnAddressToInvokerPage","returnAddress",addressNode)
					System.doAction("returnAddressToInvokerPage")
				endif
			endfunc
						
			func AddressCapture_M_returnAirportToInvokerPage(JSONObject jo)
			    if NULL != jo
			        String invoker = AddressCapture_M_getInvoker()
					String callbackFunc = AddressCapture_M_getCallbackFunc()
					
					JSONObject.put(jo,"type",2)
					#RecentPlace_saveAddress(jo)
					
					TxNode node
					TxNode.addMsg(node,callbackFunc)
					TxNode addressNode
					TxNode.addMsg(addressNode,JSONObject.toString(jo))
					MenuItem.setAttribute("returnAirportToInvokerPage","url",invoker)
					MenuItem.setBean("returnAirportToInvokerPage","callFunction",node)
					MenuItem.setBean("returnAirportToInvokerPage","returnAddress",addressNode)
					
					System.doAction("returnAirportToInvokerPage")
			    endif
			endfunc
			
			func AddressCapture_M_saveMultiMatchAirport(TxNode node)
			    String saveKey = "<%=Constant.StorageKey.MUTLI_MATCHES_AIRPORT_LIST%>"
		        Cache.saveToTempCache(saveKey,node)
			endfunc
			
			func AddressCapture_M_getMultiMatchAirport()
			    String saveKey = "<%=Constant.StorageKey.MUTLI_MATCHES_AIRPORT_LIST%>"
		        TxNode node =  Cache.getFromTempCache(saveKey)
		        return node
			endfunc
			
			func AddressCapture_M_saveReturnToLocalService(String serviceName)
			    TxNode serviceNode
			    TxNode.addMsg(serviceNode,serviceName)
			    String saveKey = "<%=Constant.StorageKey.RETURN_TO_LOCALSERVICE%>"
			    Cache.saveToTempCache(saveKey,serviceNode)
			endfunc
			
			func AddressCapture_M_getReturnToLocalService()
			    TxNode serviceNode
			    String saveKey = "<%=Constant.StorageKey.RETURN_TO_LOCALSERVICE%>"
			    serviceNode = Cache.getFromTempCache(saveKey)
			    String serviceName = ""
			    if NULL != serviceNode
			       serviceName = TxNode.msgAt(serviceNode,0)
			    endif
			    
			    return serviceName
			endfunc
			
			func AddressCapture_M_saveRecentPlacesStatus(int status)
			    TxNode statusNode
			    TxNode.addValue(statusNode,status)
			    
			    String saveKey = "<%=Constant.StorageKey.RECENTPLACE_LOCALSERVICE_STATUS%>"
			    Cache.saveToTempCache(saveKey,statusNode)
			endfunc
			
			func AddressCapture_M_getRecentPlacesStatus()
			    String saveKey = "<%=Constant.StorageKey.RECENTPLACE_LOCALSERVICE_STATUS%>"
			    TxNode statusNode = Cache.getFromTempCache(saveKey)
			    int status = 0
			    if NULL != statusNode
			       if 1 == TxNode.valueAt(statusNode,0)
			          status = 1
			       endif
			    endif
			    return status
			endfunc
			
			func AddressCapture_M_deleteRecentPlacesStatus()
			    String saveKey = "<%=Constant.StorageKey.RECENTPLACE_LOCALSERVICE_STATUS%>"
			    Cache.deleteFromTempCache(saveKey)
			endfunc
			
			###########
			func AddressCapture_M_saveFavoritesStatus(int status)
			    TxNode statusNode
			    TxNode.addValue(statusNode,status)
			    
			    String saveKey = "<%=Constant.StorageKey.FAVORITES_LOCALSERVICE_STATUS%>"
			    Cache.saveToTempCache(saveKey,statusNode)
			endfunc
			
			func AddressCapture_M_getFavoritesStatus()
			    String saveKey = "<%=Constant.StorageKey.FAVORITES_LOCALSERVICE_STATUS%>"
			    TxNode statusNode = Cache.getFromTempCache(saveKey)
			    int status = 0
			    if NULL != statusNode
			       if 1 == TxNode.valueAt(statusNode,0)
			          status = 1
			       endif
			    endif
			    return status
			endfunc
			
			func AddressCapture_M_deleteFavoritesStatus()
			    String saveKey = "<%=Constant.StorageKey.FAVORITES_LOCALSERVICE_STATUS%>"
			    Cache.deleteFromTempCache(saveKey)
			endfunc

# Comment useless code 			
#			func AddressCapture_M_saveFavoritesBackURL(String url)
#			    TxNode urlNode
#			    TxNode.addMsg(urlNode,url)
#			    
#			    String saveKey = "<%=Constant.StorageKey.FAVORITE_BACK_URL%>"
#			    Cache.saveToTempCache(saveKey,statusNode)
#			endfunc
			func AddressCapture_M_getFavoritesBackURL()
			    String saveKey = "<%=Constant.StorageKey.FAVORITE_BACK_URL%>"
			    TxNode urlNode = Cache.getFromTempCache(saveKey)
			    String url = ""
			    if NULL != urlNode
			       url = TxNode.msgAt(urlNode,0)
			    endif
			    
			    return url
			endfunc
			
			func AddressCapture_M_deleteFavoritesBackURL()
			    String saveKey = "<%=Constant.StorageKey.FAVORITE_BACK_URL%>"
			    Cache.deleteFromTempCache(saveKey)
			endfunc
			
			func AddressCapture_M_saveCreateFavoritesStatus(int status)
			    TxNode statusNode
			    TxNode.addValue(statusNode,status)
			    
			    String saveKey = "<%=Constant.StorageKey.CREATE_FAVORITES_LOCALSERVICE_STATUS%>"
			    Cache.saveToTempCache(saveKey,statusNode)
			endfunc
			
			func AddressCapture_M_getCreateFavoritesStatus()
			    String saveKey = "<%=Constant.StorageKey.CREATE_FAVORITES_LOCALSERVICE_STATUS%>"
			    TxNode statusNode = Cache.getFromTempCache(saveKey)
			    int status = 0
			    if NULL != statusNode
			       if 1 == TxNode.valueAt(statusNode,0)
			          status = 1
			       endif
			    endif
			    return status
			endfunc
			
			func AddressCapture_M_deleteCreateFavoritesStatus()
			    String saveKey = "<%=Constant.StorageKey.CREATE_FAVORITES_LOCALSERVICE_STATUS%>"
			    Cache.deleteFromTempCache(saveKey)
			endfunc
			
		    func AddressCapture_M_clearDefaultAddress()
		        Cache.deleteFromTempCache("<%=Constant.StorageKey.ADDRESS_CAPTURE_DEFAULT_ADDRESS%>")
		    endfunc	    
		    		    
		    func AddressCapture_M_saveDefaultAddress(JSONObject jo)
		    	Cache.saveToTempCache("<%=Constant.StorageKey.ADDRESS_CAPTURE_DEFAULT_ADDRESS%>",jo)
		    endfunc
		    
		    func AddressCapture_M_getDefaultAddress()
		    	JSONObject jo = Cache.getJSONObjectFromTempCache("<%=Constant.StorageKey.ADDRESS_CAPTURE_DEFAULT_ADDRESS%>")
		    	return jo
		    endfunc	 

			func AddressCapture_M_saveFavoriteType(String type)
			    TxNode urlNode
			    TxNode.addMsg(urlNode,type)
			    
			    String saveKey = "<%=Constant.StorageKey.ADDRESS_CAPTURE_FAVORITE_TYPE%>"
			    Cache.saveToTempCache(saveKey,urlNode)
			endfunc
			
			func AddressCapture_M_getFavoriteType()
			    String saveKey = "<%=Constant.StorageKey.ADDRESS_CAPTURE_FAVORITE_TYPE%>"
			    TxNode urlNode = Cache.getFromTempCache(saveKey)
			    String type = ""
			    if NULL != urlNode
			       type = TxNode.msgAt(urlNode,0)
			    endif
			    return type
			endfunc
			
			func AddressCapture_M_saveCreateFavoriteReturnUrl(String url)
			    TxNode urlNode
			    TxNode.addMsg(urlNode,url)
			    
			    String saveKey = "<%=Constant.StorageKey.CREATE_FAVORITE_RETURN_URL%>"
			    Cache.saveToTempCache(saveKey,urlNode)
			endfunc
			
			func AddressCapture_M_getCreateFavoriteReturnUrl()
			    String saveKey = "<%=Constant.StorageKey.CREATE_FAVORITE_RETURN_URL%>"
			    TxNode urlNode = Cache.getFromTempCache(saveKey)
			    String url = ""
			    if NULL != urlNode
			       url = TxNode.msgAt(urlNode,0)
			    endif
			    return url
			endfunc

			func AddressCapture_M_saveBackUrlForSelectCountry(String url)
			    TxNode urlNode
			    TxNode.addMsg(urlNode,url)
			    
			    String saveKey = "<%=Constant.StorageKey.BACK_URL_FOR_SELECT_COUNTRY%>"
			    Cache.saveToTempCache(saveKey,urlNode)
			endfunc
			
			func AddressCapture_M_getBackUrlForSelectCountry()
			    String saveKey = "<%=Constant.StorageKey.BACK_URL_FOR_SELECT_COUNTRY%>"
			    TxNode urlNode = Cache.getFromTempCache(saveKey)
			    String url = ""
			    if NULL != urlNode
			       url = TxNode.msgAt(urlNode,0)
			    endif
			    return url
			endfunc
			
			func AddressCapture_M_country(string callbackpageurl)
				AddressCapture_M_saveBackUrlForSelectCountry(callbackpageurl)
				System.doAction("AddressCapture_Action_country")
			endfunc
			
			func AddressCapture_M_saveAddressJSONForSearch(JSONObject jo)
			    String saveKey = "<%=Constant.StorageKeyForJSON.ADDRESS_JSON_FOR_SEARCH%>"
			    Cache.saveToTempCache(saveKey,jo)
			endfunc
			
			func AddressCapture_M_getAddressJSONForSearch()
			    String saveKey = "<%=Constant.StorageKeyForJSON.ADDRESS_JSON_FOR_SEARCH%>"
			    JSONObject jo = Cache.getJSONObjectFromTempCache(saveKey)
			    return jo
			endfunc
			
			func AddressCapture_M_saveCityName(TxNode cityNode)
			    String saveKey = "<%=Constant.StorageKey.CITY_NAME_NODE%>"
			    Cache.saveCookie(saveKey,cityNode)
			endfunc
			
			func AddressCapture_M_getCityName()
			    String saveKey = "<%=Constant.StorageKey.CITY_NAME_NODE%>"
			    TxNode cityNode = Cache.getCookie(saveKey)
			    return cityNode
			endfunc
			
			func AddressCapture_M_saveCacheCity(TxNode cacheCityNode)
			    String saveKey = "<%=Constant.StorageKey.CACHE_CITY_NODE%>"
			    Cache.saveCookie(saveKey,cacheCityNode)
			endfunc
			
			func AddressCapture_M_getCacheCity()
			    String saveKey = "<%=Constant.StorageKey.CACHE_CITY_NODE%>"
			    TxNode cityNode = Cache.getCookie(saveKey)
			    return cityNode
			endfunc
			
			func AddressCapture_M_saveNavAddressNode(TxNode addressNode)
			    String saveKey = "<%=Constant.StorageKey.NAV_ADDRESS_NODE%>"
			    Cache.saveToTempCache(saveKey,addressNode)
			endfunc
			
			func AddressCapture_M_getNavAddressNode()
			    String saveKey = "<%=Constant.StorageKey.NAV_ADDRESS_NODE%>"
			    String addressStr = ""
			    TxNode addressNode = Cache.getFromTempCache(saveKey)
			    if NULL != addressNode
			       addressStr = TxNode.msgAt(addressNode,0)
			    endif
			    return addressStr
			endfunc
			
			func AddressCapture_M_saveAddressForCreateOrEdit(String createOrEdit)
			    TxNode createOrEditNode
			    TxNode.addMsg(createOrEditNode,createOrEdit)
			    String saveKey = "<%=Constant.StorageKey.CREATE_OR_EDIT_FAVORITE%>"
			    Cache.saveToTempCache(saveKey,createOrEditNode)
			endfunc
			
			func AddressCapture_M_getAddressForCreateOrEdit()
			    String saveKey = "<%=Constant.StorageKey.CREATE_OR_EDIT_FAVORITE%>"
			    TxNode createOrEditNode = Cache.getFromTempCache(saveKey)
			    String createOrEdit = "create"
			    if NULL != createOrEditNode
			        createOrEdit = TxNode.msgAt(createOrEditNode,0)
			    endif
			    return createOrEdit
			endfunc

			func AddressCapture_M_isDoingCreateFavorites()
				int doing = 0
		        TxNode node = Cache.getFromTempCache("<%=Constant.StorageKey.CREATE_FAV_DOING_STATUS%>")
		        if NULL != node
		            doing = TxNode.valueAt(node,0)
		        endif
		        return doing
			endfunc
			
			func AddressCapture_M_setDoingCreateFavorites(int showing)
				TxNode node
			    TxNode.addValue(node,showing)
			    Cache.saveToTempCache("<%=Constant.StorageKey.CREATE_FAV_DOING_STATUS%>",node)
			endfunc
			
			func AddressCapture_M_setCacheCity()
				TxNode nearCityNode = AddressCapture_M_getCacheCity()
				TxNode node = AddressCapture_M_getCityName()
				
				TxNode FlagInfoNode = Startup.getFlagInfos()
				String isCityChange = "0"
				if NULL != FlagInfoNode
				    isCityChange = TxNode.msgAt(FlagInfoNode,7)
				endif
				
				if NULL == nearCityNode || "1" == isCityChange
					AddressCapture_M_getCityString()
					nearCityNode = AddressCapture_M_getCacheCity()
					node = AddressCapture_M_getCityName()
				endif
				
				if NULL != nearCityNode
				   if NULL != node
					  int citySize = TxNode.getStringSize(node)
					  int i = 0
					  String cityName = ""
					  while i < citySize
					     cityName = TxNode.msgAt(node,i)
					     TxNode.addMsg(nearCityNode,cityName)
					     i = i + 1
					  endwhile
				   endif
				endif
				return nearCityNode
			endfunc
			
			func AddressCapture_M_saveCacheAddress(JSONObject jo)
			    String saveKey = "<%=Constant.StorageKeyForJSON.CACHE_ADDRESS_JSONOBJECT%>"
			    Cache.saveToTempCache(saveKey,jo)
			endfunc
			
			func AddressCapture_M_getCacheAddress()
			    String saveKey = "<%=Constant.StorageKeyForJSON.CACHE_ADDRESS_JSONOBJECT%>"
			    JSONObject jo = Cache.getJSONObjectFromTempCache(saveKey)
			    return jo
			endfunc
			
			func AddressCapture_M_deleteCacheAddress()
			    String saveKey = "<%=Constant.StorageKeyForJSON.CACHE_ADDRESS_JSONOBJECT%>"
			    Cache.deleteFromTempCache(saveKey)
			endfunc
			
			func AddressCapture_M_saveInputAddress(JSONObject jo)
			    Cache.saveToTempCache("<%=Constant.StorageKeyForJSON.INPUT_ADDRESS_JSONOBJECT%>",jo)
			endfunc
			
			func AddressCapture_M_getInputAddress()
			    JSONObject jo = Cache.getJSONObjectFromTempCache("<%=Constant.StorageKeyForJSON.INPUT_ADDRESS_JSONOBJECT%>")
			    return jo
			endfunc
			
			func AddressCapture_M_deleteInputAddress()
			    String saveKey = "<%=Constant.StorageKeyForJSON.INPUT_ADDRESS_JSONOBJECT%>"
			    Cache.deleteFromTempCache(saveKey)
			endfunc

			func AddressCapture_M_getCityString()
			    TxNode nearCityMsgNode = AddressCapture_M_getCityName()
			    TxNode node = Startup.getFlagInfos()
		    	if node != NULL
		    	   String isCityChange = ""
		    	   isCityChange = TxNode.msgAt(node,7)
		    	   if "1" == isCityChange || NULL == nearCityMsgNode
		    	      TxNode nearCityNode = Startup.getNearCities()
					  if NULL != nearCityNode
					     AddressCapture_M_saveCacheCity(nearCityNode)
					     int childSize = TxNode.getChildSize(nearCityNode)
					     int i = 0
					     String cityMsg = ""
					     String statesStr = ""
					     TxNode childNode
					     TxNode newNearCityMsgNode
					     while i < childSize
					        childNode = TxNode.childAt(nearCityNode,i)
					        cityMsg = TxNode.msgAt(childNode,2)
					        statesStr = TxNode.msgAt(childNode,3)
					        if NULL != cityMsg && "" != cityMsg
					            if NULL != statesStr && "" != statesStr
					               cityMsg = cityMsg + ", " + statesStr
					            endif
					            TxNode.addMsg(newNearCityMsgNode,cityMsg)
					        endif
					        i = i + 1
					     endwhile
					     AddressCapture_M_saveCityName(newNearCityMsgNode)
					  endif
		    	   endif
		    	endif
			endfunc
			
			func AddressCapture_M_saveAddressListTitleForMaiTai(TxNode node)
			    String saveKey = "<%=Constant.StorageKey.ADDRESS_LIST_TITLE_FOR_MAITAI%>"
			    Cache.saveToTempCache(saveKey,node)
			endfunc
			
			func AddressCapture_M_getAddressListTitleForMaiTai()
			    String saveKey = "<%=Constant.StorageKey.ADDRESS_LIST_TITLE_FOR_MAITAI%>"
			    TxNode node = Cache.getFromTempCache(saveKey)
			    int titleFlag = 0
			    if NULL != node
			       titleFlag = TxNode.valueAt(node,0)
			    endif
			    return titleFlag
			endfunc
			
			func AddressCapture_M_deleteAddressListTitleForMaiTai()
			    String saveKey = "<%=Constant.StorageKey.ADDRESS_LIST_TITLE_FOR_MAITAI%>"
			    Cache.deleteFromTempCache(saveKey)
			endfunc
			
			
			func AddressCapture_M_saveBackAction(String backAction)
	           TxNode backActionNode
	           TxNode.addMsg(backActionNode,backAction)
	           String saveKey = "<%=Constant.StorageKey.BACK_ACTION_ADDRESS_LIST%>"
			   Cache.saveToTempCache(saveKey,backActionNode)
	        endfunc
			
			func AddressCapture_M_getBackAction()
			    String backAction = ""
	            String saveKey = "<%=Constant.StorageKey.BACK_ACTION_ADDRESS_LIST%>"
	            TxNode backActionNode = Cache.getFromTempCache(saveKey)
	            if NULL != backActionNode
	               backAction = TxNode.msgAt(backActionNode,0)
	            endif
	            return backAction
			endfunc
			
			func AddressCapture_M_deleteBackAction()
	           String saveKey = "<%=Constant.StorageKey.BACK_ACTION_ADDRESS_LIST%>"
	           Cache.deleteFromTempCache(saveKey)
	        endfunc
		]]>
</tml:script>

<tml:menuItem name="AddressCapture_Action_favorites" pageURL="">
</tml:menuItem>

<tml:menuItem name="AddressCapture_Action_recentPlace" pageURL="">
</tml:menuItem>

<tml:menuItem name="showAddressCapturePage" pageURL="">
</tml:menuItem>

<tml:menuItem name="returnAddressToInvokerPage" pageURL="">
</tml:menuItem>

<tml:menuItem name="returnAirportToInvokerPage" pageURL="">
</tml:menuItem>

<tml:menuItem name="AddressCapture_Action_country" pageURL="<%=getPage + "SelectCountry"%>" />
<tml:menuItem name="AddressCapture_Action_ac" pageURL="" />
<tml:menuItem name="AddressCapture_Action_airport" pageURL="<%=host + "/FindAirPort.do"%>"/>
