func AdJuggler_doActionForPremClick(JSONObject adJugglerJO)
    if NULL == adJugglerJO
       return FAIL
    endif
    JSONObject actionJO = JSONObject.get(adJugglerJO,"action")
    if NULL == actionJO
       return FAIL
    endif

    String key = JSONObject.get(actionJO,"type")
    if NULL != key && "" != key
       String campaignID = ""
       if JSONObject.has(adJugglerJO,"campaignID")
          campaignID = JSONObject.get(adJugglerJO,"campaignID")
       endif
       String pageName = JSONObject.get(adJugglerJO,"pageName")
       String accountType = AdJuggler_getAccoutType()
       JSONObject textJSON = JSONObject.get(adJugglerJO,"text")
       String text = ""
       String locale = AdJuggler_getLocale()
       if NULL != textJSON
          text = JSONObject.get(textJSON,locale)
       endif
       AdJuggler_logForAdJuggler(<%=EventTypes.AD_BANNER_CLICK%>, pageName, campaignID, accountType, text)
       AdJuggler_setJSONForPurchase(campaignID, pageName)
       
       if "<%=Constant.KEY_ADJUGGLER_URL%>" == key
          String url = JSONObject.get(actionJO,"url")
		  MenuItem.setAttribute("dealWithURL","url",url)
	            TxNode lastKnownLocationNode = Gps.getLastKnownLocation()
	            JSONObject locationJO = convertStopToJSON(lastKnownLocationNode)
	            if NULL != locationJO
	               String locationJOStr = JSONObject.toString(locationJO)
		       TxNode tmpNode
	               TxNode.addMsg(tmpNode,locationJOStr)
		       MenuItem.setBean("dealWithURL","locationJO",tmpNode)
	            endif

          System.doAction("dealWithURL")
       elsif "<%=Constant.KEY_ADJUGGLER_SEARCH%>" == key
          SearchPoi_C_initial(5)
         
          String categoryId = "-1"
          if JSONObject.has(actionJO,"searchCategory")
             categoryId = JSONObject.get(actionJO,"searchCategory")
          endif
          <%=PoiListModel.setCategoryId("categoryId")%>
          
          String searchName = ""
          if JSONObject.has(actionJO,"searchName")
             searchName = JSONObject.get(actionJO,"searchName")
          endif
          <%=PoiListModel.setKeyWord("searchName")%>
          
          String isMostPopular = "0"
          if JSONObject.has(actionJO,"isMostPopular")
             isMostPopular = JSONObject.get(actionJO,"isMostPopular")
          endif
          <%=PoiListModel.setMostPopular("isMostPopular")%>
         
          getCurrentLocation(<%=Constant.CurrentLocation.CURRENT_LOCATION%>,<%=gpsValidTime%>,<%=cellIdValidTime%>,<%=gpsTimeout%>)	
       elsif "<%=Constant.KEY_ADJUGGLER_WEATHER%>" == key
          Weather_C_showCurrent()
      	  return FAIL
       elsif "<%=Constant.KEY_ADJUGGLER_MOVIE%>" == key
          SearchMovie_C_initMovie()
          
          if JSONObject.has(actionJO,"searchName")
             String movieName = JSONObject.get(actionJO,"searchName")
             TxNode inputNode
             TxNode.addMsg(inputNode, movieName)
		     SearchMovie_C_saveInput(inputNode)
          endif
          
          if JSONObject.has(actionJO,"searchDate")
             String searchDate = JSONObject.get(actionJO,"searchDate")
             if !String.isNumberString(searchDate)
                searchDate = "0"
             endif
             MovieList_C_saveMovieDate(searchDate)
          endif

          if JSONObject.has(actionJO,"sortBy")
             String newSortBy = JSONObject.get(actionJO,"sortBy")
             MovieList_C_saveNewSortBy(newSortBy)
          endif
		  
          AdJuggler_searchLocationForMovie()
          return FAIL
       elsif "<%=Constant.KEY_ADJUGGLER_BROWSER%>" == key
          String browserUrl = JSONObject.get(actionJO,"url")
		  if NULL != browserUrl && "" != browserUrl
			 TxNode node
			 TxNode.addMsg(node,browserUrl)
			 #TxNode.addMsg(node,"TRUE")
			 MenuItem.setBean("callLocalBrowserMenu", "url", node)
			 System.doAction("callLocalBrowserMenu")
		  endif
	   elsif "<%=Constant.KEY_POPUP%>" == key
	      JSONObject textJO = JSONObject.get(actionJO,"text")
	      JSONObject button1JO = JSONObject.get(actionJO,"button1")
	      JSONObject button2JO = JSONObject.get(actionJO,"button2")
	      String showMessage = JSONObject.get(textJO,locale)
	      String buttonStr1 =  JSONObject.get(button1JO,locale)
	      String buttonStr2 =  JSONObject.get(button2JO,locale)
	      
	      Cache.saveToTempCache("ACTION_JSON_POPUP",actionJO)
	      System.showConfirm(showMessage,buttonStr1,buttonStr2,"popupCallBack")
       endif
    endif
    return FAIL
endfunc

func popupCallBack(int selected)
	JSONObject actionJO = Cache.getJSONObjectFromTempCache("ACTION_JSON_POPUP")
	if NULL == actionJO
	   return FAIL
	endif
	println(actionJO)
	
	JSONObject buttonActionJO
	if selected==1
	   buttonActionJO =  JSONObject.get(actionJO,"buttonAction1")
	elsif selected==0
	   buttonActionJO =  JSONObject.get(actionJO,"buttonAction2")
	endif
	
	doActionForPopup(buttonActionJO)
endfunc

func doActionForPopup(JSONObject buttonActionJO)
    if NULL == buttonActionJO
       return FAIL
    endif
    
    if !JSONObject.has(buttonActionJO,"action")
       return FAIL
    endif
    
    String action = JSONObject.get(buttonActionJO,"action")
    if "" == action
       return FAIL
    endif
    
    if "<%=Constant.KEY_ADJUGGLER_URL%>" == action
    	 String url = JSONObject.get(buttonActionJO,"url")
    	 MenuItem.setAttribute("dealWithURL","url",url)
         System.doAction("dealWithURL")
    elsif "<%=Constant.KEY_ADJUGGLER_BROWSER%>" == action
         String browserUrl = JSONObject.get(buttonActionJO,"url")
		 if NULL != browserUrl && "" != browserUrl
			 TxNode node
			 TxNode.addMsg(node,browserUrl)
			 MenuItem.setBean("callLocalBrowserMenu", "url", node)
			 System.doAction("callLocalBrowserMenu")
		 endif
	 endif
	 
	 return FAIL
endfunc

func setCurrentLocation(JSONObject jo)
	SearchPoi_C_saveLocation(jo)
	AdJuggler_doSearchWithAjaxForPOI()
endfunc
      
func AdJuggler_doSearchWithAjaxForPOI()
    PoiList_C_deleteSortTypeTemp()
    <%=PoiListModel.setPageIndexTemp("0")%>
    
    String categoryId = <%=PoiListModel.getCategoryId()%>
    String isMostPopular = <%=PoiListModel.getMostPopular()%>
	PoiList_C_saveSpecialSortForSpare(categoryId,isMostPopular)
    PoiList_C_searchPoiWithAjax()
endfunc

func AdJuggler_searchLocationForMovie()
    JSONObject locParm
	JSONObject.put(locParm,"LocationType",1)
	JSONObject.put(locParm,"GpsLocationValidTime",240000)
	JSONObject.put(locParm,"NetworkLocationValidTime",1860000)
	JSONObject.put(locParm,"Timeout",12000)
	TxNode locParmNode
	TxNode.addMsg(locParmNode,""+locParm)
	MenuItem.setBean("doGetGpsForMovie","locParam",locParmNode)
	System.doAction("doGetGpsForMovie")
endfunc
      
func AdJuggler_getLocationForMovieForMovie()
    TxNode currentLocationNode = ParameterSet.getParam("currentLocation")
	
    JSONObject stop 
	String joStr =TxNode.msgAt(currentLocationNode,0)
	JSONObject joStop = JSONObject.fromString(joStr)
	String callbackId = JSONObject.get(joStop,"CallbackID")
	 
	if(callbackId!="Success")
		System.showErrorMsg("<%=msg.get("mSearch.gps.error")%>")
		return FAIL
	endif
	
	String location = JSONObject.getString(joStop,"Location")
	JSONObject gps= JSONObject.fromString(location)
      
    JSONObject jo
	JSONObject.put(jo,"lat",JSONObject.get(gps,"Lat"))
	JSONObject.put(jo,"lon",JSONObject.get(gps,"Lon"))
	JSONObject.put(jo,"type",6)
	Movie_C_saveCurrentLocation(jo)
	
	int sortByName = Movie_C_getSortType()
    String newSortBy = MovieList_C_getNewSortBy()
    MovieList_C_searchMovieWithAjax(sortByName, newSortBy)
endfunc

func AdJuggler_logForAdJuggler(int type,String pageName, String campaignID, String accountType,String message)
    JSONObject logJSON
	JSONObject.put(logJSON, "<%=AttributeID.PAGE_NAME_TML%>", pageName)
	JSONObject.put(logJSON, "<%=AttributeID.AD_BANNER_MSG%>", campaignID)
	JSONObject.put(logJSON, "<%=AttributeID.ACCOUNT_TYPE%>", accountType)
	JSONObject.put(logJSON, "<%=AttributeID.MESSAGE_NAME%>", message)
	
	if StatLogger.isStatEnabled(type)
		StatLogger.logEvent(type, logJSON)
	endif	        
endfunc

func AdJuggler_getAccoutType()
    String accountType = "<%=Constant.ACCOUNT_TYPE_PREM%>"
    if AccountTypeForSprint_IsLiteUser()
        accountType = "<%=Constant.ACCOUNT_TYPE_LITE%>"
    elsif AccountTypeForSprint_IsBundleUser()
        accountType = "<%=Constant.ACCOUNT_TYPE_BUNDLE%>"
    endif
    
    return accountType
endfunc

func AdJuggler_setDefaultJSON()
    JSONObject adJugglerJO
    JSONObject.put(adJugglerJO,"campaignID","Upgrade to premium")
    JSONObject text
    JSONObject.put(adJugglerJO,"en_US","Upgrade for additional features")
    JSONObject.put(adJugglerJO,"es_MX","Actualizar para obtener funciones adicionales")
    JSONObject.put(adJugglerJO,"text",text)
    JSONObject.put(adJugglerJO,"pageName","<%=Constant.KEY_ADJUGGLER_FROM_MAIN%>")
    JSONObject actionJO
    JSONObject.put(actionJO,"type","<%=Constant.KEY_ADJUGGLER_URL%>")
    JSONObject.put(actionJO,"url","<%=purchasePageUrl%>")
    JSONObject.put(adJugglerJO,"action",actionJO)
    
    return adJugglerJO
endfunc

func AdJuggler_saveLastTime(String key)
    int time = Time.get()
    TxNode lastTimeNode
    TxNode.addValue(lastTimeNode,time)
    Cache.saveCookie(key, lastTimeNode)
endfunc

func AdJuggler_getLastTime(String lastTimeKey)
    TxNode lastTimeNode = Cache.getCookie(lastTimeKey)
    int time = 0
    if NULL != lastTimeNode
       time = TxNode.valueAt(lastTimeNode, 0)
    endif
    
    return time
endfunc

func AdJuggler_checkExpire(JSONObject adJugglerJO, String lastTimeKey)
    if NULL == adJugglerJO
       return TRUE
    endif
    
    if !JSONObject.has(adJugglerJO,"expireTime")
       return TRUE
    endif
    
    int lastTime = AdJuggler_getLastTime(lastTimeKey)
    if 0 == lastTime
       return TRUE
    endif
    
    String expireTimeStr = JSONObject.get(adJugglerJO,"expireTime")
    if !String.isNumberString(expireTimeStr)
       return TRUE
    endif
    
    int expireTime = String.convertToNumber(expireTimeStr)
    int currentTime = Time.get()
    int time = currentTime - lastTime
    println("expireTime.............."+expireTime)
    println("currentTime.............."+currentTime)
    println("time.............."+time)
    
    if time >= expireTime
       return TRUE
    endif	
    
    return FALSE
endfunc

func AdJuggler_getTimeToExpire(JSONObject adJugglerJO, String lastTimeKey)
    if NULL == adJugglerJO
       return 0
    endif
    
    if !JSONObject.has(adJugglerJO,"expireTime")
       return 0
    endif
    
    int lastTime = AdJuggler_getLastTime(lastTimeKey)
    if 0 == lastTime
       return 0
    endif
    
    String expireTimeStr = JSONObject.get(adJugglerJO,"expireTime")
    if !String.isNumberString(expireTimeStr)
       return 0
    endif
    
    int expireTime = String.convertToNumber(expireTimeStr)
    int currentTime = Time.get()
    int time = currentTime - lastTime
    int remainingTime = expireTime - time
    return remainingTime
endfunc


func AdJuggler_getLocale()
    String locale = ""
    TxNode node = Preference.getPreferenceValue(9)
	if node != NULL
		if TxNode.getStringSize(node) > 0
			locale = TxNode.msgAt(node,0) 
		endif
	endif
	if locale == NULL || locale==""
		locale = "en_US"
	endif
	
	println("locale..................."+locale)
	return locale
endfunc

func AdJuggler_setJSONForPurchase(String campaignId, String from)
    JSONObject logInfoJSON
    JSONObject.put(logInfoJSON,"campaignID",campaignId)
    JSONObject.put(logInfoJSON,"from",from)
    println("save................"+logInfoJSON)
    
    Cache.saveToTempCache("PURCHASE_JSON_INFO",logInfoJSON)
endfunc

 func AdJuggler_setRefreshAdState(String doRefresh)
 	TxNode node
 	TxNode.addMsg(node,doRefresh)
 	Cache.saveToTempCache("ADJUGGLER_DO_REFRESH",node)
 endfunc

 func stopRefreshAd()
    AdJuggler_setRefreshAdState("NO")
 endfunc

 func startRefreshAd()
    AdJuggler_setRefreshAdState("YES")
    int stopRefreshTime = 120
    String value = ""
   	TxNode node = System.getServerParam("STOP_REFRESH_IN_SEC")
   	if node != NULL
    	value = TxNode.msgAt(node,0)  
   	endif
    if NULL != value
		if String.isNumberString(value) == TRUE
			stopRefreshTime = String.convertToNumber(value)
		endif
	endif
    System.invokeWhenTimeout(stopRefreshTime*1000,"stopRefreshAd")
 endfunc

 func shouldRefreshAd()
   	TxNode node = Cache.getFromTempCache("ADJUGGLER_DO_REFRESH")
   	String value
	if NULL != node
		value=TxNode.msgAt(node,0)
		if NULL != value
			return value
		endif
	endif
	return "NO"
 endfunc

<%-- 
     This method provides app start value, which means that app has just started.
     This will be currently used to target specific ads when the app has just
     started.

     Return value: "1" means that app has just started.
                   "0" means that app was already started.
 --%>
func getAppStartValue()

    int lastTime = AdJuggler_getLastTime("LAST_TIME_AJUGGLER")
    int currentTime = Time.get()
    int deltaTime = currentTime - lastTime

    int thresholdTime = 120*60
    String value = ""
    TxNode node = System.getServerParam("APP_START_ADJ_SEC")
    if node != NULL
        value = TxNode.msgAt(node,0)  
    endif

    if NULL != value
        if String.isNumberString(value) == TRUE
            thresholdTime = String.convertToNumber(value)
        endif
    endif

    thresholdTime = thresholdTime*1000

    if deltaTime > thresholdTime
        return "1"
    else
        return "0"
    endif

endfunc
