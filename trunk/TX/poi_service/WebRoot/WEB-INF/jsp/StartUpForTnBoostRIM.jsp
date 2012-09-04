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
    String purchasePageUrl = "{login.http}/getInterface.do?jsp=PurchaseInterface";
 	
	int gpsValidTime = 240;
	int cellIdValidTime=1860;
	int gpsTimeout=12;
%>

<tml:TML outputMode="TxNode">
	<%@ include file="poi/controller/SearchPoiController.jsp"%>
	<%@ include file="ac/controller/AddressCaptureTemplateController.jsp"%>
	<%@ include file="ac/controller/AddressCaptureController.jsp"%>
	<%@ include file="model/StartUpModel.jsp"%>
	<%@ include file="dsr/controller/DSRController.jsp"%>
	<%@ include file="/WEB-INF/jsp/model/PrefModel.jsp"%>
	<%@ include file="controller/ReferFriendController.jsp"%>
	<%@include file="/WEB-INF/jsp/ac/model/SetUpHomeModel.jsp"%>
	<jsp:include 
	    page="/WEB-INF/jsp/AccountTypeForBoost.jsp"/>
	<jsp:include
		page="/WEB-INF/jsp/ac/controller/SelectAddressController.jsp" />
	<jsp:include
		page="/WEB-INF/jsp/local_service/controller/MapWrapController.jsp" />
	<tml:script language="fscript" version="1">
		<![CDATA[
		    <%@ include file="GetServerDriven.jsp"%>
		    <%@ include file="AdJugglerScript.jsp"%>
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
			
			func componentVisiblecontrol()
				if isPremiumAccount()
					Page.setComponentAttribute("item4","visible","0")
					
	                Page.setComponentAttribute("imagePrem","visible","1")
	                Page.setComponentAttribute("imageTnLogo","visible","0")	
	                				     		   		
     		   		MenuItem.setItemValid("item1",4,0)
     		   		MenuItem.commitSetItemValid("item1")					
     		   		MenuItem.setItemValid("item2",4,0)
     		   		MenuItem.commitSetItemValid("item2")	
     		   		MenuItem.setItemValid("item3",4,0)
     		   		MenuItem.commitSetItemValid("item3")	
     		   		MenuItem.setItemValid("item4",4,0)
     		   		MenuItem.commitSetItemValid("item4")	
				else
					logAdBannerLog(<%=EventTypes.AD_BANNER_VIEW%>,"PREM001")
					Page.setComponentAttribute("item4","visible","1")						

	                Page.setComponentAttribute("imagePrem","visible","0")
	                Page.setComponentAttribute("imageTnLogo","visible","1")	

     		   		MenuItem.setItemValid("item1",4,1)
     		   		MenuItem.commitSetItemValid("item1")					
     		   		MenuItem.setItemValid("item2",4,1)
     		   		MenuItem.commitSetItemValid("item2")	
     		   		MenuItem.setItemValid("item3",4,1)
     		   		MenuItem.commitSetItemValid("item3")	
     		   		MenuItem.setItemValid("item4",4,1)
     		   		MenuItem.commitSetItemValid("item4")	
				endif			
			endfunc
			func preLoad()
				checkUpgrade()
				checkTraffic()
			    AddressCapture_C_getCityString()
				SetUpHome_M_syncHomeFromClient()
				
				componentVisiblecontrol()
			endfunc
			
			func onShow()
				checkServerDrivenForMenu()
				string shortCutKey = "-" + "<%=Constant.SHORTCUT_KEY_UP%>" + "-" + "<%=Constant.SHORTCUT_KEY_DOWN%>"
				shortCutKey =  shortCutKey + "-"
				System.setKeyEventListener(shortCutKey,"onKeyPressed")
				initTopImage()
				checkAppNew()
				checkMyFavorite()
				homePageStatLogger()
				
				componentVisiblecontrol()
			endfunc
			
			func onLoad()
				# Delete IN_LOGIN_FLOW flag
				# Set convienence key will check this flag.
				# If in, go to main screen.
				# If not in, exit browser shell.
				Cache.deleteFromTempCache("IN_LOGIN_FLOW")

				componentVisiblecontrol()				
				# Check Blue tooth GPS local service
				System.doAction("checkBlueTooth")
	        endfunc
	        
			func isPremiumAccount()
				int flag = TRUE
	            if AccountTypeForBoost_IsLiteUser() || AccountTypeForBoost_IsBundleUser() || AccountTypeForBoost_IsFreeTrial()
	               flag = FALSE
	            endif
	            
	            return flag			
			endfunc
				        	        
	        func checkServerDrivenForMenu()
	            int canTellFriends = ServerDriven_CanTellFriends()
	            MenuItem.setItemValid("item0",3,canTellFriends)
		    	MenuItem.commitSetItemValid("item0")
        		MenuItem.setItemValid("item1",3,canTellFriends)
        		MenuItem.commitSetItemValid("item1")
        		MenuItem.setItemValid("item2",3,canTellFriends)
        		MenuItem.commitSetItemValid("item2")
        		MenuItem.setItemValid("item3",3,canTellFriends)
        		MenuItem.commitSetItemValid("item3")
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
				Page.setComponentAttribute("itemLabelFav","visible",needShow+"")
		      	Page.setComponentAttribute("itemImageNewFav","visible",needShow+"")
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
		        	JSONObject.put(jo,"mask","10111111011")
		        	JSONObject.put(jo,"from","DriveTo")
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
						return
					#go to AC page and show warning
       	        	else
       	        		SelectAddress_C_SetNoCoverage(TRUE)
       					goToACPage()	
       					return
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
			        	        	        

	        
	        func initTopImage()
	        	int index = Page.getControlProperty("menuListBox","focusedIndex")
	        	visibleImage(index)
	        endfunc
			
			func visibleImage(int i)
				string imageUrl = "<%=imageUrl%>" + "banner_" + i + ".png"
				Page.setControlProperty("imageTop","image",imageUrl)
				
				string imageUrl_logo = "<%=imageUrl%>" + "TN_logo" + ".png"
				Page.setControlProperty("imageTnLogo","image",imageUrl_logo)
				
				string imageUrl_prem = "<%=imageUrl%>" + "prem" + ".png"
				Page.setControlProperty("imagePrem","image",imageUrl_prem)
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
	        		text = " <red> " + "<%=msg.get("startup.New")%>" + "</red>"
	        	endif
	        	Page.setComponentAttribute("new","text",text)
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
        		MenuItem.setItemValid("item4",1,flag)
        		MenuItem.commitSetItemValid("item4")
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
			
			func onClickUpgradeToPremium()
				logAdBannerLog(<%=EventTypes.AD_BANNER_CLICK%>,"PREMMENU001")
				AdJuggler_setJSONForPurchase("PREMMENU001", "<%=Constant.KEY_ADJUGGLER_FROM_MAIN%>")
			    Purchase_C_purchase(1)
		        return FAIL
			endfunc
						
			func onClickUpgradeToPremiumViaClick()
				logAdBannerLog(<%=EventTypes.AD_BANNER_CLICK%>,"PREM001")
				Purchase_C_purchase(1)
		        return FAIL		        
			endfunc
			
			func Purchase_C_purchase(int accountStatus)
				TxNode node
				TxNode.addValue(node, accountStatus)
				MenuItem.setBean("Purchase", "accountStatusNode", node)
				System.doAction("Purchase")
				return FAIL
			endfunc
			
	        func logAdBannerLog(int type,string message)
				if StatLogger.isStatEnabled(type)
					String accountType = AdJuggler_getAccoutType()
					JSONObject logJSON
					JSONObject.put(logJSON, "<%=AttributeID.AD_BANNER_MSG%>", message)
					JSONObject.put(logJSON, "<%=AttributeID.PAGE_NAME_TML%>", "<%=Constant.KEY_ADJUGGLER_FROM_MAIN%>")
					JSONObject.put(logJSON, "<%=AttributeID.ACCOUNT_TYPE%>", accountType)
					
					StatLogger.logEvent(type, logJSON)
				endif	        
	        endfunc	
	        								
		    func onFocus(string id)
		    	int index = Page.getControlProperty("menuListBox","focusedIndex")
		    	visibleImage(index)
		    endfunc
			
		]]>

	</tml:script>
	
	<tml:menuItem name="Purchase" pageURL="<%=purchasePageUrl%>"/>
	<tml:actionItem name="checkBlueTooth" action="CheckBlueTooth"></tml:actionItem>
	<tml:menuItem name="checkBlueTooth" actionRef="checkBlueTooth">
	</tml:menuItem>

	<tml:menuItem name="about" text="<%=msg.get("startup.menu.About")%>"
		trigger="KEY_MENU" onClick="exitFromHomePage" pageURL="<%=getPage + "AboutMenu"%>">
	</tml:menuItem>
	
	<tml:menuItem name="productTour" text="<%=msg.get("startup.menu.ProductTour")%>"
		trigger="KEY_MENU" onClick="exitFromHomePage" pageURL="<%=getPage + "ProductTour"%>">
	</tml:menuItem>
	
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
	<tml:menuItem name="upgradeToPremium" onClick="onClickUpgradeToPremiumViaClick" trigger="TRACKBALL_CLICK" />
	<tml:menuItem name="upgradeToPremiumMenu" onClick="onClickUpgradeToPremium" text="<%=msg.get("startup.upgradeBoost")%>" trigger="KEY_MENU" />
		
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
		showLeftArrow="true" showRightArrow="true" helpMsg="$//$main" groupId="<%=GROUP_ID_COMMOM%>" defaultSelectedMenu="system_64" >
		<%if(TnUtil.isRogersCarrier(carrier) || TnUtil.isBell_VMC(handlerGloble)){ %>
			<tml:title id="title" align="center|middle" fontWeight="bold|22" fontColor="white" backgroundImageURL="local:0020450">
				<%=PoiUtil.amend(msg.get("startup.title"))%>
			</tml:title>
		<%}else{ %>
			<tml:title id="title" align="center|middle" fontWeight="bold|22" fontColor="white">
				<%=PoiUtil.amend(msg.get("startup.title"))%>
			</tml:title>
		<%}%>
		<tml:image id="imageTop" url="<%=imageUrl + "banner_0.png"%>" />
		<tml:listBox id="menuListBox" name="pageListBox:settingsList"
			isFocusable="true" hotKeyEnable="false">
			<tml:compositeListItem id="item0" visible="true" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage="<%=imageUrl + "list_bg_highlight_45px.png"%>"
				blurBgImage="<%=imageUrl + "list_bg_45px.png"%>">
				<tml:param name="onFocus" value="onFocus"/>
				<tml:image id="itemImage0" url="<%=imageUrl + "button_driveto.png"%>" />
				<tml:label id="itemlabel0" focusFontColor="white" 
					fontWeight="bold|system_large" textWrap="ellipsis" align="left|middle">
					<%=msg.get("startup.DriveTo")%>
				</tml:label>
				
				<tml:image id="itemImageNewFav" url="<%=imageUrl+  "new_fav_bg.png"%>" />
				<tml:label id="itemLabelFav" fontColor="white"
					fontWeight="bold|system_small" 
					align="center|middle">
				</tml:label>
				
				<tml:menuRef name="driveToItemAction1" />
				<tml:menuRef name="upgrade" />
				<tml:menuRef name="about" />
				<tml:menuRef name="referFriend" />
				<tml:menuRef name="upgradeToPremiumMenu" />
			</tml:compositeListItem>
			<tml:compositeListItem id="item1" visible="true" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage="<%=imageUrl + "list_bg_highlight_45px.png"%>"
				blurBgImage="<%=imageUrl + "list_bg_45px.png"%>">
				<tml:param name="onFocus" value="onFocus"/>
				<tml:image id="itemImage1" url="<%=imageUrl + "button_search.png"%>" />
				<tml:label id="itemlabel1" focusFontColor="white" 
					fontWeight="bold|system_large" textWrap="ellipsis" align="left|middle">
					<%=msg.get("startup.Search")%>
				</tml:label>
				
				<tml:menuRef name="searchPOI" />
				<tml:menuRef name="upgrade" />
				<tml:menuRef name="about" />
				<tml:menuRef name="referFriend" />
				<tml:menuRef name="upgradeToPremiumMenu" />
			</tml:compositeListItem>
			<tml:compositeListItem id="item2" visible="true" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage="<%=imageUrl + "list_bg_highlight_45px.png"%>"
				blurBgImage="<%=imageUrl + "list_bg_45px.png"%>">
				<tml:param name="onFocus" value="onFocus"/>
				<tml:image id="itemImage2" url="<%=imageUrl + "button_mapstraffic.png"%>" />
				<tml:label id="itemlabel2" focusFontColor="white" 
					fontWeight="bold|system_large" textWrap="ellipsis" align="left|middle">
				</tml:label>
				
				<tml:menuRef name="showMap" />
				<tml:menuRef name="upgrade" />
				<tml:menuRef name="about" />
				<tml:menuRef name="referFriend" />
				<tml:menuRef name="upgradeToPremiumMenu" />
			</tml:compositeListItem>
			<tml:compositeListItem id="item3" visible="true" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage="<%=imageUrl + "list_bg_highlight_45px.png"%>"
				blurBgImage="<%=imageUrl + "list_bg_45px.png"%>">
				<tml:param name="onFocus" value="onFocus"/>
				<tml:image id="itemImage3" url="<%=imageUrl + "button_tools.png"%>" />
				<tml:label id="itemlabel3" focusFontColor="white" 
					fontWeight="bold|system_large" textWrap="ellipsis" align="left|middle">
					<%=msg.get("startup.Apps")%>
				</tml:label>
				<tml:label id="new" focusFontColor="white" 
					fontWeight="bold|system_small" textWrap="ellipsis" align="left|middle">
				</tml:label>
				
				
				<tml:menuRef name="toolsMenu" />
				<tml:menuRef name="upgrade" />
				<tml:menuRef name="about" />
				<tml:menuRef name="referFriend" />
				<tml:menuRef name="upgradeToPremiumMenu" />				
			</tml:compositeListItem>
			<tml:compositeListItem id="item4" visible="true" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage="<%=imageUrl + "list_bg_highlight_45px.png"%>"
				blurBgImage="<%=imageUrl + "list_bg_45px.png"%>">
				<tml:param name="onFocus" value="onFocus"/>
				<tml:image id="itemImage4" url="<%=imageUrl + "button_upgrade.png"%>" />
				<tml:label id="itemlabel4" focusFontColor="white" 
					fontWeight="bold|system_large" textWrap="ellipsis" align="left|middle">
					<%=msg.get("startup.upgradeBoost")%>
				</tml:label>			
			<tml:menuRef name="upgradeToPremium" />
			<tml:menuRef name="upgrade" />
			<tml:menuRef name="about" />
			<tml:menuRef name="referFriend" />
			<tml:menuRef name="upgradeToPremiumMenu" />
		</tml:compositeListItem>
		</tml:listBox>
		<tml:image id="imagePrem" url="" visible="false" align="left|top"/>
		<tml:image id="imageTnLogo" url="" visible="false" align="left|top"/>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>
