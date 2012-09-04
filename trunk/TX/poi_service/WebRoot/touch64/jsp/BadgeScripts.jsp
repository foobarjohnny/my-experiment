func getNewBadgeAppList()
	TxNode node = System.getServerParam("<%=Constant.BadgeFwkConstants.NEW_APP_LIST_KEY%>")
   	String appList
   	if node != NULL
   		appList = TxNode.msgAt(node,0) 
   	endif
   	if NULL == appList
   		appList=""
   	endif
 	return appList
endfunc
	
func saveBadgeStructure(JSONObject badge_Structure)
	TxNode node
	TxNode.addMsg(node, JSONObject.toString(badge_Structure))
	Cache.saveCookie("<%=Constant.BadgeFwkConstants.BADGE_STRUCTURE_STORAGE_KEY%>" ,node)
endfunc

func getBadgeStructure()
	TxNode node = Cache.getCookie("<%=Constant.BadgeFwkConstants.BADGE_STRUCTURE_STORAGE_KEY%>")
	String str = ""
	if NULL == node
		return NULL
	endif
	str = TxNode.msgAt(node, 0)
	JSONObject badgeStructure = JSONObject.fromString(str)
	return badgeStructure
endfunc

func GetNewIndicatorLabel()
	println("here")
	JSONObject badge_Structure = getBadgeStructure()
	println("here4")
	if NULL == badge_Structure
		println("null")
		badge_Structure = JSONObject.fromString("{}")
	endif
	String appListStr = getNewBadgeAppList()
	int newApps = 0
	String lastSeenNewApp = ""
	int index=0
	int len = String.getLength(appListStr)
	int tok=0
	String curApp = ""
	String appStatus = ""
	println("here")
	while index < len-1
		tok = String.find(appListStr, index, ";")
		if tok>0 
			curApp = String.At(appListStr,index,tok-index)
			index=tok+1
		else
			curApp = String.At(appListStr,index,len)
			index=len
		endif
		if !JSONObject.has(badge_Structure,curApp)
			newApps = newApps + 1
			JSONObject.put(badge_Structure, curApp, "TRUE")
			lastSeenNewApp = curApp
		else
			appStatus = JSONObject.get(badge_Structure,curApp)
			if "TRUE" == appStatus
				newApps = newApps + 1
				lastSeenNewApp = curApp
			endif
		endif
	endwhile
	saveBadgeStructure(badge_Structure)
	println(badge_Structure)
	String returnLabel = "" + newApps + " <%=msg.get("startup.NewApps")%>"
	println(returnLabel)
	if newApps == 0
		returnLabel = ""
	endif
	println(returnLabel)
	if newApps == 1
		<%  int screenWidth = new Integer(handlerGloble.getClientInfo(DataHandler.KEY_WIDTH)).intValue();
			if(screenWidth < Constant.BadgeFwkConstants.MIN_SCREEN_SIZE_TO_SHOW_APPNAME)
			{
		%>
			println("less")
		  returnLabel = "<%=msg.get("startup.NewApp")%>"
		  <% } else { %>
		  	println("more")
		    returnLabel = "<%=msg.get("startup.NewApp")%> - " + "$(newapps."+lastSeenNewApp+")"
		  <%} %>
	endif
	println(returnLabel)
	return returnLabel	
endfunc

func IsNewApp(String appName)
	JSONObject badge_Structure = getBadgeStructure()
	println(" is new " + badge_Structure)
	String appStatus = "FALSE"
	if NULL != badge_Structure
		if JSONObject.has(badge_Structure,appName)
			println("asd " + appName)
			appStatus = JSONObject.get(badge_Structure,appName)
			println(appStatus)
		endif
	endif
	println(appStatus)
	return appStatus
endfunc

func makeNotNew(String appName)
	JSONObject badge_Structure = getBadgeStructure()
	if NULL != badge_Structure
		JSONObject.put(badge_Structure, appName, "FALSE")
	endif
	saveBadgeStructure(badge_Structure)
endfunc