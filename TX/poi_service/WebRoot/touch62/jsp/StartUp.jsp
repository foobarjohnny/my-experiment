<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="Header.jsp"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>
<%@page import="com.telenav.j2me.datatypes.TxNode"%>
<%@page import="org.json.me.JSONObject"%>
<%@ page import="com.telenav.tnbrowser.util.*"%>
<%@page import="com.telenav.cserver.stat.*"%>

<%
    String pageURL = host + "/startUp.do?pageRegion=" + region;
    TxNode listNode = (TxNode) request.getAttribute("node");
	String strCarrier = handlerGloble.getClientInfo(handlerGloble.KEY_CARRIER);
%>

<tml:TML outputMode="TxNode">
	<%@ include file="poi/controller/SearchPoiController.jsp"%>
	<%@ include file="ac/controller/AddressCaptureController.jsp"%>
	<%@ include file="model/StartUpModel.jsp"%>
	<%@ include file="dsr/controller/DSRController.jsp"%>
	<%@ include file="/touch62/jsp/model/PrefModel.jsp"%>
	<%@ include file="controller/ReferFriendController.jsp"%>
	<%@ include file="/touch62/jsp/ac/model/SetUpHomeModel.jsp"%>
	<jsp:include page="/touch62/jsp/ac/controller/SelectAddressController.jsp" />
	<jsp:include page="/touch62/jsp/local_service/controller/MapWrapController.jsp" />
	<%@ include file="/touch62/jsp/model/OneBoxModel.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
			<%@ include file="GetServerDriven.jsp"%>

			func pageCallBack(int type) 
				if 3 == type 
					checkMyFavorite()
				elsif 5 == type
					onClickGoToOneBox()
				endif
				return FAIL
			endfunc

			func onBack()
				System.exit()
				return FAIL
			endfunc
			
			func checkTraffic()
				string text = "<%=msg.get("startup.Maps")%>"
				if StartUp_M_isTurnOnTraffic()
					text = "<%=msg.get("startup.MapsAndTraffic")%>"
				endif
				Page.setComponentAttribute("itemlabel2","text",text)
			endfunc

			func displayButtonImage()
				string locale = Pref_M_getLocale()
				string imageUrl = "<%=imageUrl%>" + "icon-for-new_" + locale + ".png"
				
				Page.setControlProperty("itemImageNew","image",imageUrl)
			endfunc
			
			func preLoad()
				checkAppNew()
				checkUpgrade()
				checkTraffic()
				<% if( TnUtil.isUSCC62(handlerGloble) ) { %>
					displayButtonImage()
				<% } %>
			    AddressCapture_C_getCityString()
			    TxNode hotBrandNode = ParameterSet.getParam("hotBrandlist")
			    OneBox_M_setHotBrandNode(hotBrandNode)
				SetUpHome_M_syncHomeFromClient()
				checkDSRAvail()				
			
			    if 0 == ServerDriven_CanOneBoxSearch()
				    println("hideonebox")
				    Page.setComponentAttribute("oneBoxSearchButton","visible","0")	
					Page.setComponentAttribute("titleShadow1","visible","0")	
			    endif
				<%if(!PoiUtil.isWarrior(handlerGloble)){%>
				   Page.addGenericMenu(64)
				<%}%>
			endfunc
			
			func onShow()
				checkServerDrivenForMenu()
				string shortCutKey = "-" + "<%=Constant.SHORTCUT_KEY_UP%>" + "-" + "<%=Constant.SHORTCUT_KEY_DOWN%>"
				shortCutKey =  shortCutKey + "-"
				System.setKeyEventListener(shortCutKey,"onKeyPressed")
				homePageStatLogger()
				println("---System.delPages begin")
				System.delPages(1)
				Uitility.clearLocalRelatedData()
				println("---Uitility.clearLocalRelatedData end")
				<%if(!PoiUtil.isWarrior(handlerGloble)){%>
				 	Page.preLoadPageToCache("<%=host + "/selectAddress.do?pageRegion=" + region + "&jsp=SelectAddress"%>")
					Page.preLoadPageToCache("<%=getPageCallBack+"MapWrap"%>")
				<%}%>
				checkMyFavorite()
				System.setKeyEventListener("-up-down-","onKeyPressed")
			endfunc
			
			func onKeyPressed(string s)
				if s == "down"
					int focusIdx = getFocusButton()
					if focusIdx >= 0 
						int nextFocusIdx = getNextFocusButton(focusIdx)
						if nextFocusIdx >= 0
							Page.setControlProperty("item"+nextFocusIdx,"focused","true")
							return TRUE
						else
							Page.setControlProperty("dsrButton","focused","true")
							return TRUE
						endif
					endif
				elsif s == "up"
					int focusIdx = getFocusButton()
					if focusIdx >= 0 
					int preFocusIdx = getPreFocusButton(focusIdx)
						if preFocusIdx >= 0
							Page.setControlProperty("item"+preFocusIdx,"focused","true")
							return TRUE
						else
							Page.setControlProperty("oneBoxSearchButton","focused","true")
							return TRUE
						endif
					endif
				endif
			endfunc
			
			func getFocusButton()
				int i =0
				int size = 4
				while i < size
					if TRUE == Page.getControlProperty("item"+i,"isFocused")
		        			return i
					endif
					i = i+1
				endwhile
				return -1
			endfunc
		        
			func getNextFocusButton(int orginIdx)
				int i = orginIdx + 1
		        int size = 4
		        int orginx = Page.getControlProperty("item"+orginIdx,"x")
		        while i < size
		        	if orginx == Page.getControlProperty("item"+i,"x")
		        		return i
		        	endif
		        	i = i+1
		        endwhile
		        return -1
			endfunc
		        
			func getPreFocusButton(int orginIdx)
		        int i = orginIdx - 1
		        int orginx = Page.getControlProperty("item"+orginIdx,"x")
		        while i >= 0
		        	if orginx == Page.getControlProperty("item"+i,"x")
		        		return i
		        	endif
		        	i = i-1
		        endwhile
		        return -1
			endfunc
			
			func onLoad()
				# Delete IN_LOGIN_FLOW flag
				# Set convienence key will check this flag.
				# If in, go to main screen.
				# If not in, exit browser shell.
				Cache.deleteFromTempCache("IN_LOGIN_FLOW")
				# Check Blue tooth GPS local service
				System.doAction("checkBlueTooth")
				return FAIL
	        endfunc
	        
	        func checkServerDrivenForMenu()
	            int canTellFriends = ServerDriven_CanTellFriends()
	            MenuItem.setItemValid("page",2,canTellFriends)
		    	MenuItem.commitSetItemValid("page")
	            MenuItem.setItemValid("item0",3,canTellFriends)
		    	MenuItem.commitSetItemValid("item0")
        		MenuItem.setItemValid("item1",3,canTellFriends)
        		MenuItem.commitSetItemValid("item1")
        		MenuItem.setItemValid("item2",3,canTellFriends)
        		MenuItem.commitSetItemValid("item2")
        		MenuItem.setItemValid("item3",3,canTellFriends)
        		MenuItem.commitSetItemValid("item3")
        		MenuItem.setItemValid("dsrButton",3,canTellFriends)
        		MenuItem.commitSetItemValid("dsrButton")
        		MenuItem.setItemValid("oneBoxSearchButton",3,canTellFriends)
        		MenuItem.commitSetItemValid("oneBoxSearchButton")
	        endfunc

	        func getReceivedAddress()
	        	int nReceivedAddress = 0
	        	TxNode node = Startup.getFlagInfos()
		    	if node != NULL
		    		int size = TxNode.getStringSize(node)
		    		if size > 2
		    			string receivedAddress = checkNULL(TxNode.msgAt(node,2))
		    			if String.isNumberString(receivedAddress)
		    				nReceivedAddress = String.convertToNumber(receivedAddress)
		    			endif
		    		endif 
		    	endif
		    	
		    	return nReceivedAddress
	        endfunc
	        
	        func checkMyFavorite()
		       	if 0 == ServerDriven_ShowNewAddressFlagOnMainPage()
		       	   showNewFav(FALSE)
		       	   return FAIL
		       	endif
		       	int receivedAddress = getReceivedAddress()
		       	if receivedAddress > 0
		       		showNewFav(TRUE)
		       		Page.setComponentAttribute("itemLabelFav","text",receivedAddress+"")
		       	else
		       		showNewFav(FALSE)
		       	endif
		    endfunc
			
			func showNewFav(int needShow)
			<% if (TnUtil.isCanadianCarrier(handlerGloble)) { %>
				Page.setComponentAttribute("itemLabelFav","visible","0")
		      	Page.setComponentAttribute("itemImageNewFav","visible","0")
			<% } else { %>
				Page.setComponentAttribute("itemLabelFav","visible",needShow+"")
				Page.setComponentAttribute("itemImageNewFav","visible",needShow+"")					
			<% } %>
			endfunc
			
			func checkNULL(string s)
	        	if s== NULL
	        		return ""
	        	else
	        		return s	
	        	endif
	        endfunc
	        	        
			func checkPoiLog()
	        	TxNode node = Cache.getCookie("POILogCookie")
	        	Cache.deleteCookie("POILogCookie")
	        	if NULL!=node
	        		MenuItem.setBean("searchPOI", "log", node)
	        	endif
	        	
				return TRUE
	        endfunc
	        
	        func homePageStatLogger()
				JSONObject empty
				if StatLogger.isStatEnabled(<%=EventTypes.CLICKSTREAM%>)
					StatLogger.clickStream()
				endif
				# time from startup till home screen
				if StatLogger.isStatEnabled(<%=EventTypes.HOME_SCREEN_TIME%>)
					int time = TempCache.getInt("LOGGIN_CHECK_ACCOUNT_TIME", -1)
					if time > 0
						JSONObject.put(empty, "<%=AttributeID.TIME_1%>", time)
						TempCache.deleteInt("LOGGIN_CHECK_ACCOUNT_TIME")
					endif
					StatLogger.eventEnds(<%=EventTypes.HOME_SCREEN_TIME%>, empty)
				endif
	        endfunc
	        
	        func onClickDriveTo1()
	        	#syn home again in case client does not finish the syn.
	        	SetUpHome_M_syncHomeFromClient()
	        	if Cell.isCoverage()
		        	goToACPage()
				else
					ignoreCheck("False")
				endif
				return FAIL
	        endfunc
			
			func goToACPage()
					JSONObject jo
		        	JSONObject.put(jo,"title","<%=msg.get("selectaddress.title.driveto")%>")
		        	JSONObject.put(jo,"mask","10111111111")
		        	JSONObject.put(jo,"from","DriveTo")
		        	JSONObject.put(jo,"returnAsIs","1")
		        	JSONObject.put(jo,"callbackfunction","CallBack_SelectAddress")
					JSONObject.put(jo,"callbackpageurl","<%=getPageCallBack + "DriveToWrap"%>")
					SelectAddress_C_SelectAddress(jo)
			endfunc
	        func checkCover()
	        	TxNode node 
       	        node = ParameterSet.getParam("checkCoverageBack")
       	        if TxNode.getStringSize(node)!=0
       	        	#check callback message
       	        	JSONObject msg = JSONObject.fromString(TxNode.msgAt(node,0))
       	        	String errorType
       	        	if JSONObject.has(msg,"Error")
 	      	       		errorType= JSONObject.get(msg,"Error")
 	      	       	else
 	      	       		errorType="back"
       	        	 endif
       	        	if errorType=="RadioIsOff"
						retry()
						return FAIL
					#go to AC page and show warning
       	        	else
       	        		SelectAddress_C_SetNoCoverage(TRUE)
       					goToACPage()	
       					return FAILWITHOUTREFRESH
    				endif
    			endif
	        endfunc
	        
	        
	        func ignoreCheck(String needCheck)
	        		JSONObject ignore
					JSONObject.put(ignore,"IgnoreRadioOff",needCheck)
					String ignoreStr = JSONObject.toString(ignore)
					TxNode node
	        		TxNode.addMsg(node,ignoreStr)
	        		MenuItem.setBean("checkCoverage","IgnoreRadioOff",node)
					System.doAction("checkCoverage")
					return FAIL
	        endfunc

	        func retry()
	        	System.showConfirm("<%=msg.get("common.radio.off")%>","<%=msg.get("common.button.retry")%>","<%=msg.get("common.button.Ignore")%>","selectRetry")
	        	return FAIL
	        endfunc
	        
	        func selectRetry(int selected)
	        	if selected==1
	        		onClickDriveTo1()
	        	elsif selected==0
	        		ignoreCheck("True")
	        	else
	        		
	        	endif
	        endfunc
			
			func visibleImage(int i)
				string imageUrl = "$imageTopTemp" + i
				Page.setControlProperty("imageTop","image",imageUrl)
			endfunc
			
			func mapOnClick()
				MapWrap_C_showCurrent()
				return FAIL
			endfunc
			
			func isSearchSpeakInput()
	 			return FALSE	       	
	        endfunc

	        func onClickSearch()
	        	checkPoiLog()
	        	if isSearchSpeakInput()
	        		JSONObject jo
					invokeSpeakSearch(jo)
	        	else
					SearchPoi_C_initial(5)
					SearchPoi_C_showSearch()        
	        	endif
	        	return FAIL	        	
	        endfunc
	        
	        func onClickApps()
	        	StartUp_M_clearAppNewFlag()
	        	checkAppNew()
	        endfunc
	        
	        func checkAppNew()
	        	string text = ""
	        	int isNew = StartUp_M_getAppNewFlag()
	        	if isNew
	        		text = "<%=msg.get("startup.New")%>"
	        	else	
	        	endif
	        	Page.setComponentAttribute("new","text",text)
	        	Page.setComponentAttribute("itemImageNew","visible",isNew + "")
	        	if isNew
	        		Page.setComponentAttribute("itemlabel3","visible",1 + "")
	        		Page.setComponentAttribute("itemlabel3_1","visible",0 + "")
	        	else	
	        		Page.setComponentAttribute("itemlabel3","visible",0 + "")
	        		Page.setComponentAttribute("itemlabel3_1","visible",1 + "")
	        	endif	
	        endfunc

		    func StartUp_M_getListItemSize()
		    	int size = 4
		    	return size
		    endfunc
		    
		    func checkUpgrade()
		    	int flag = StartUp_M_getUpgradeFlag()
		    	MenuItem.setItemValid("item0",1,flag)
		    	MenuItem.commitSetItemValid("item0")
        		MenuItem.setItemValid("item1",1,flag)
        		MenuItem.commitSetItemValid("item1")
        		MenuItem.setItemValid("item2",1,flag)
        		MenuItem.commitSetItemValid("item2")
        		MenuItem.setItemValid("item3",1,flag)
        		MenuItem.commitSetItemValid("item3")
		    	MenuItem.setItemValid("dsrButton",1,flag)
		    	MenuItem.commitSetItemValid("dsrButton")
		    	MenuItem.setItemValid("oneBoxSearchButton",1,flag)
		    	MenuItem.commitSetItemValid("oneBoxSearchButton")
		    	MenuItem.setItemValid("page",0,flag)
		    	MenuItem.commitSetItemValid("page")
		    endfunc	

			
		    func goToReferFriends()
		        ReferFriend_C_initalAndGoTo()
		        return FAIL
		    endfunc

			func onClickUpgrade()
				string upgradeUrl = StartUp_M_getUpgradeUrl()
				if "" != upgradeUrl
					TxNode node
					TxNode.addMsg(node,upgradeUrl)
					TxNode.addMsg(node,"TRUE")
					MenuItem.setBean("callLocalBrowserMenu", "url", node)
					System.doAction("callLocalBrowserMenu")
				endif
				return FAIL
			endfunc
    			
		    func onFocus(string id)
		    	int index = getItemIndex(id)
		    	visibleImage(index)
		    endfunc
		    
		    func getItemIndex(string id)
		    	int index = 0
		    	if "item0" == id
		    		index= 0
		    	elsif "item1" == id
		    		index= 1
		    	elsif "item2" == id
		    		index= 2
		    	elsif "item3" == id
		    		index= 3
		    	endif
		    			
		    	return index
		    endfunc
		    
			func checkDSRAvail()
				if DSR_M_isDSRSupportedForDisable() == 1
					Page.setComponentAttribute("dsrButton","visible","1")
				else
					Page.setComponentAttribute("dsrButton","visible","0")				
				endif
			endfunc
			
			func onClickSayCommand()
				JSONObject jo
				invokeSpeakCommand(jo)
				return FAIL	
			endfunc
					    
			func getOneBoxParam()
				JSONObject jo
				JSONObject.put(jo,"callbackfunction","addrCallBack")
				JSONObject.put(jo,"callbackpageurl","<%=getPageCallBack+"OneBoxWrap#search"%>")
				JSONObject.put(jo,"from","Common")
				println("AC Parameters " +jo)
	        	return jo
      		endfunc
      		
		    func onClickGoToOneBox()
		    	OneBox_M_saveAcParam(getOneBoxParam())
		    	OneBox_M_resetSearchType()
		    	System.doAction("oneBoxSearchMenu")
		    	return FAIL	
	    	endfunc
		]]>

	</tml:script>

	<tml:menuItem name="oneBoxSearchMenu" pageURL="<%=getPage + "OneBoxSearch#Common"%>" trigger="TRACKBALL_CLICK">
	</tml:menuItem>
	
	<tml:actionItem name="checkBlueTooth" action="CheckBlueTooth"></tml:actionItem>
	<tml:menuItem name="checkBlueTooth" actionRef="checkBlueTooth">
	</tml:menuItem>
	<tml:menuItem name="doSearch" onClick="onClickGoToOneBox"  trigger="TRACKBALL_CLICK|KEY_MENU"/>
	
	<tml:menuItem name="about" text="<%=msg.get("startup.menu.About")%>"
		trigger="KEY_MENU" onClick="exitFromHomePage" pageURL="<%=getPage + "AboutMenu"%>">
	</tml:menuItem>
	
	<%if(TnUtil.isRogersCarrier(strCarrier) || TnUtil.isBell_VMCUser(strCarrier)) {%>
	<tml:menuItem name="help" text="<%=msg.get("startup.menu.help")%>"
		trigger="KEY_MENU" onClick="exitFromHomePage" pageURL="<%=getPage + "AboutSupport"%>">
	</tml:menuItem>
	<%}%>
	
	<tml:menuItem name="productTour" text="<%=msg.get("startup.menu.ProductTour")%>"
		trigger="KEY_MENU" onClick="exitFromHomePage" pageURL="<%=getPage + "ProductTour"%>">
	</tml:menuItem>
	<tml:menuItem name="saycommand"  onClick="onClickSayCommand"  trigger="TRACKBALL_CLICK"/>
	

	<tml:block feature="<%=FeatureConstant.REFER_FRIEND%>">
		<tml:menuItem name="referFriend" text="<%=msg.get("startup.menu.ReferFriends")%>"
			trigger="KEY_MENU" onClick="goToReferFriends">
		</tml:menuItem>
	</tml:block>

	
	<tml:actionItem name="callLocalBrowser" action="<%=Constant.LOCALSERVICE_INVOKEPHONEBROWSER%>">
		<tml:input name = "url"/>
	</tml:actionItem>
	<tml:menuItem name="callLocalBrowserMenu" actionRef="callLocalBrowser" trigger="TRACKBALL_CLICK" />
	
	<tml:menuItem name="upgrade" onClick="onClickUpgrade" text="<%=msg.get("startup.menu.Upgrade")%>" trigger="KEY_MENU" />
		
	<tml:actionItem name="doCheckCoverage" action="DriveToOffCoverageCheck">
		<tml:input name="IgnoreRadioOff"/>
		<tml:output name="checkCoverageBack" />
	</tml:actionItem>
	<tml:menuItem name="checkCoverage" actionRef="doCheckCoverage"
		onClick="checkCover" />
		
	<tml:menuItem name="showMap" onClick="mapOnClick"
		trigger="TRACKBALL_CLICK">
	</tml:menuItem>
	<tml:menuItem name="home" pageURL="<%=pageURL%>">
	</tml:menuItem>

	<tml:menuItem name="toolsMenu" onClick="onClickApps" pageURL="<%=getPage + "ToolsMain"%>"> trigger="TRACKBALL_CLICK">
	</tml:menuItem>
	
	<tml:menuItem name="searchPOI" onClick="onClickSearch" trigger="TRACKBALL_CLICK"/>
	<tml:menuItem name="driveToItemAction1" onClick="onClickDriveTo1" trigger="TRACKBALL_CLICK"/>								
	<tml:actionItem name="SyncResource"
		action="<%=Constant.LOCALSERVICE_SYNCRESOURCE%>">
	</tml:actionItem>
	<tml:menuItem name="syncResouceMenu" actionRef="SyncResource"
		trigger="KEY_RIGHT | TRACKBALL_CLICK" />
		
	<tml:page id="StartUp" url="<%=pageURL%>" type="<%=pageType%>" genericMenu="16"
		showLeftArrow="true" showRightArrow="true" helpMsg="$//$main" groupId="<%=GROUP_ID_COMMOM%>" defaultSelectedMenu="system_64" scriptCallback="pageCallBack">
		<tml:bean name="hotBrandlist" valueType="TxNode"
			value="<%=Utility.TxNode2Base64(listNode)%>"></tml:bean>
		<tml:menuRef name="upgrade" />
		<tml:menuRef name="about" />
		<tml:menuRef name="referFriend" />
		<%if(TnUtil.isRogersCarrier(strCarrier) || TnUtil.isBell_VMCUser(strCarrier)) {%>
		<tml:menuRef name="help" />
		<%}%>
		<tml:image id="oneBoxBgImg" visible="true" align="left|top"/>
		<tml:image id="HomeTopBgImg" visible="true" align="left|top"/>
		<tml:label id="titleLabel" align="center|middle" fontWeight="bold" fontColor="white">
			<%=PoiUtil.amend(msg.get("startup.title"))%>
		</tml:label>
		<tml:image id="imageTop" url="$imageTopTemp0" align="left|top"/>
		<% if(TnUtil.isATTRIM623(handlerGloble) || TnUtil.isUSCCRIM62(handlerGloble)){ %>
		<tml:compositeListItem id="oneBoxSearchButton" >
			<tml:label id ="oneBoxSearchLabel" fontWeight="system_large">
				<%=TnUtil.amend(msg.get("onebox.what.prompt"))%>
			</tml:label>
			<tml:menuRef name="doSearch"/>
			<tml:menuRef name="upgrade" />
			<tml:menuRef name="about" />
			<tml:menuRef name="referFriend" />
		</tml:compositeListItem>
		<% } else { %>
		<tml:button id="oneBoxSearchButton" fontWeight="system_large"  text="<%=msg.get("onebox.what.prompt")%>">
				<tml:menuRef name="doSearch"/>
				<tml:menuRef name="upgrade" />
				<tml:menuRef name="about" />
				<tml:menuRef name="referFriend" />
		</tml:button>
		<% } %>
		<tml:compositeListItem id="item0" visible="true" bgColor="#FFFFFF" isFocusable="true"
			transparent="false" focusBgImage=""
			blurBgImage="">
			<tml:param name="onFocus" value="onFocus"/>
			<tml:image id="itemImage0" url="" align="left|top"/>
			<tml:label id="itemlabel0" focusFontColor="white" 
				 textWrap="ellipsis" align="left|middle">
				<%=msg.get("startup.DriveTo")%>
			</tml:label>
			<tml:image id="itemImageNewFav" visible="false"/>
			<tml:label id="itemLabelFav" fontColor="white"
			fontWeight="bold|system_small" 
			align="center|middle">
			</tml:label>
			<tml:menuRef name="driveToItemAction1" />
			<tml:menuRef name="upgrade" />
			<tml:menuRef name="about" />
			<tml:menuRef name="referFriend" />
			<%if(TnUtil.isRogersCarrier(strCarrier) || TnUtil.isBell_VMCUser(strCarrier)) {%>
			<tml:menuRef name="help" />
			<%}%>
		</tml:compositeListItem>
		<tml:compositeListItem id="item1" visible="true" bgColor="#FFFFFF" isFocusable="true"
			transparent="false" focusBgImage=""
			blurBgImage="">
			<tml:param name="onFocus" value="onFocus"/>
			<tml:image id="itemImage1" url=""  align="left|top"/>
			<tml:label id="itemlabel1" focusFontColor="white" 
				textWrap="ellipsis" align="left|middle">
				<%=msg.get("startup.Search")%>
			</tml:label>
			<tml:menuRef name="searchPOI" />
			<tml:menuRef name="upgrade" />
			<tml:menuRef name="about" />
			<tml:menuRef name="referFriend" />
			<%if(TnUtil.isRogersCarrier(strCarrier) || TnUtil.isBell_VMCUser(strCarrier)) {%>
			<tml:menuRef name="help" />
			<%}%>
		</tml:compositeListItem>
		<tml:compositeListItem id="item2" visible="true" bgColor="#FFFFFF" isFocusable="true"
			transparent="false" focusBgImage=""
			blurBgImage="">
			<tml:param name="onFocus" value="onFocus"/>
			<tml:image id="itemImage2" url="" align="left|top"/>
			<tml:label id="itemlabel2" focusFontColor="white" 
				textWrap="ellipsis" align="left|middle">
			</tml:label>
			<tml:menuRef name="showMap" />
			<tml:menuRef name="upgrade" />
			<tml:menuRef name="about" />
			<tml:menuRef name="referFriend" />
			<%if(TnUtil.isRogersCarrier(strCarrier) || TnUtil.isBell_VMCUser(strCarrier)) {%>
			<tml:menuRef name="help" />
			<%}%>
		</tml:compositeListItem>
		<tml:compositeListItem id="item3" visible="true" bgColor="#FFFFFF" isFocusable="true"
			transparent="false" focusBgImage=""
			blurBgImage="">
			<tml:param name="onFocus" value="onFocus"/>
			<tml:image id="itemImage3" url="" align="left|top"/>
			<tml:label id="itemlabel3" focusFontColor="white" 
				textWrap="ellipsis" align="left|middle">
				<%=msg.get("startup.Apps")%>
			</tml:label>
			<tml:label id="itemlabel3_1" focusFontColor="white" 
				textWrap="ellipsis" align="left|middle">
				<%=msg.get("startup.Apps")%>
			</tml:label>
			<tml:image id="itemImageNew" url="" align="left|top" visible="false"/>
			<tml:label id="new" fontColor="white"
				fontWeight="system" textWrap="ellipsis" align="center|middle">
			</tml:label>
			<tml:menuRef name="toolsMenu" />
			<tml:menuRef name="upgrade" />
			<tml:menuRef name="about" />
			<tml:menuRef name="referFriend" />
			<%if(TnUtil.isRogersCarrier(strCarrier) || TnUtil.isBell_VMCUser(strCarrier) ) {%>
			<tml:menuRef name="help" />
			<%}%>
		</tml:compositeListItem>
		<tml:image id="titleShadow1" visible="true" align="left|top"/>
		<tml:image id="bottomBgImg" visible="true" align="left|top"/>
		<tml:button id="dsrButton" text="  ">
				<tml:menuRef name="saycommand"/>
				<tml:menuRef name="upgrade" />
				<tml:menuRef name="about" />
				<tml:menuRef name="referFriend" />
		</tml:button>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>