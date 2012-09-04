<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@page import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%@page import="java.util.List"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%@ include file="../Header.jsp"%>
<% 
	String from = (String)request.getAttribute("from");
	String pageUrl = host + "/selectAddress.do?pageRegion=" + region + "&amp;jsp=SelectAddress";
	String callBackPageUrl = host + "/selectAddress.do?pageRegion=" + region + "&jsp=SelectAddress";
	String platform = handlerGloble.getClientInfo(DataHandler.KEY_PLATFORM);
	String carrier = handlerGloble.getClientInfo(DataHandler.KEY_CARRIER);
	String device = handlerGloble.getClientInfo(DataHandler.KEY_DEVICEMODEL);
%>
<tml:TML outputMode="TxNode">
	<%@include file="model/SelectAddressModel.jsp"%>
	<%@include file="/touch/jsp/model/PrefModel.jsp"%>
    <%@ include file="/touch/jsp/ac/controller/AddressCaptureController.jsp"%>
	<%@ include file="/touch/jsp/dsr/controller/DSRController.jsp"%>
	<%@ include file="controller/SetUpHomeController.jsp"%>
	<jsp:include page="controller/SelectContactController.jsp"/>
	<%@ include file="../poi/controller/SearchPoiController.jsp"%>
	<%@ include file="/touch/jsp/StopUtil.jsp"%>
	<%@ include file="AddressAjax.jsp"%>
	<%@ include file="/touch/jsp/model/DriveToModel.jsp"%>
	<%@ include file="/touch/jsp/local_service/GetGps.jsp"%>
	
	<tml:script language="fscript" version="1">
		<![CDATA[
		<%@ include file="../GetServerDriven.jsp"%>
 
		func CommonAPI_SetFocus(String it)
			<% if (!PoiUtil.isWarrior(handlerGloble)){ %>
			<%}else{%>
				Page.setComponentAttribute(it,"focused","true")		
			<%}%>
		endfunc
		
		
        func isAddressSpeakInput()
 			return FALSE	       	
        endfunc
        
        func isSearchSpeakInput()
 			return FALSE	       	
        endfunc
        
        func getAcInputParameter()
            JSONObject jo
			JSONObject.put(jo,"callbackfunction",SelectAddress_M_getCallbackfunction())
			JSONObject.put(jo,"callbackpageurl",SelectAddress_M_getCallbackpageurl())
			if SelectAddress_M_isReturnAsIs()
				JSONObject.put(jo,"returnAsIs","1")
			endif
			JSONObject.put(jo,"from",SelectAddress_M_getFrom())
        	return jo
        endfunc
        
        func onClickAddress()
        	CommonAPI_SetFocus("item6")    	
			DriveTo_M_saveStopType(<%=Constant.STOP_GENERIC%>)
			if isAddressSpeakInput()
				invokeSpeakAddress(getAcInputParameter())
			else
				AddressCapture_C_address(getAcInputParameter())				
			endif
			return FAIL
        endfunc
        
        func onClickCity()
      		CommonAPI_SetFocus("item7")    	
            DriveTo_M_saveStopType(<%=Constant.STOP_CITY%>)
			if isAddressSpeakInput()
				invokeSpeakCity(getAcInputParameter())
			else
				AddressCapture_C_city(getAcInputParameter())
			endif
			return FAIL	     	
        endfunc

        func onClickAirport()
        	CommonAPI_SetFocus("item9")
            DriveTo_M_saveStopType(<%=Constant.STOP_AIRPORT%>)
			if isAddressSpeakInput()
				invokeSpeakAirport(getAcInputParameter())     	
			else
				AddressCapture_C_airport(getAcInputParameter())
			endif
			return FAIL	
        endfunc

        func onClickIntersection()
        	CommonAPI_SetFocus("item10")
			DriveTo_M_saveStopType(<%=Constant.STOP_GENERIC%>)
			if isAddressSpeakInput()
				invokeSpeakIntersection(getAcInputParameter())     	
			else
				AddressCapture_C_intersection(getAcInputParameter())
			endif
			return FAIL	
        endfunc
        
        func onClickSetupHome()
        	CommonAPI_SetFocus("item2")
        	DriveTo_M_saveStopType(<%=Constant.STOP_GENERIC%>)
        	JSONObject jo
			JSONObject.put(jo,"callbackfunction",SelectAddress_M_getCallbackfunction())
			JSONObject.put(jo,"callbackpageurl",SelectAddress_M_getCallbackpageurl())
			JSONObject.put(jo,"edit","N")
			JSONObject.put(jo,"from",SelectAddress_M_getFrom())
        	SetUpHome_C_show(jo)
        	return FAIL
        endfunc
        
        func onClickEditHome()
        	CommonAPI_SetFocus("item2")
        	JSONObject jo
			JSONObject.put(jo,"callbackfunction",SelectAddress_M_getCallbackfunction())
			JSONObject.put(jo,"callbackpageurl",SelectAddress_M_getCallbackpageurl())
			JSONObject.put(jo,"from",SelectAddress_M_getFrom())
			JSONObject.put(jo,"edit","Y")
			
        	SetUpHome_C_show(jo)
        	return FAIL
        endfunc

        func onClickDeleteHome()
        	CommonAPI_SetFocus("item2")
        	System.showGeneralMsg(NULL,"<%=msg.get("selectaddress.deleteaddress")%>","<%=msg.get("common.button.Yes")%>","<%=msg.get("common.button.No")%>",NULL,"onDeleteHomeAction")
			return TRUE
        endfunc
        
		func onDeleteHomeAction(int selected)
			if 1 == selected
				SetUpHome_M_clearHome()
				checkHomeAddress()
				checkResumeTrip()
			endif
		endfunc
		                        
        func onClickCurrentLocation()
        	CommonAPI_SetFocus("item1")
        	DriveTo_M_saveStopType(<%=Constant.STOP_CURRENT_LOCATION%>)
        	
        	if SelectAddress_M_needGetGPS()
        		getCurrentLocation(<%=Constant.CurrentLocation.CURRENT_LOCATION%>,60,180,5)
        	else
	        	JSONObject jo
	        	JSONObject.put(jo,"type",6)
	        	JSONObject.put(jo,"lat",0)
				JSONObject.put(jo,"lon",0)
				JSONObject.put(jo,"label","")
				JSONObject.put(jo,"firstLine","")
				JSONObject.put(jo,"city","")
				JSONObject.put(jo,"state","")
				JSONObject.put(jo,"country","")	
				
	        	TxNode node
	        	TxNode.addMsg(node,JSONObject.toString(jo)) 
				backToCaller(node)
			endif
			return FAIL
        endfunc
   
   		func CallBack_GPS_Error(int param)
   		
		endfunc
                
        func setCurrentLocation(JSONObject jo)
       		TxNode node
       		TxNode.addMsg(node,JSONObject.toString(jo)) 
			
			backToCaller(node)   
        endfunc

		func CallBack_Popup(int param)
		
		endfunc
			        
        func backToCaller(TxNode node)
        	MenuItem.setAttribute("callback","url",SelectAddress_M_getCallbackpageurl())
			
			TxNode node1
        	TxNode.addMsg(node1,SelectAddress_M_getCallbackfunction()) 
        	MenuItem.setBean("callback", "callFunction", node1)
        	
        	MenuItem.setBean("callback", "returnAddress", node)
        	System.doAction("callback")
        	return FAIL
        endfunc
        
        func getMask()
        	return SelectAddress_M_getMask()
        endfunc
        
        func preLoad()
        	string title = SelectAddress_M_getTitle()
        	Page.setComponentAttribute("title","text",title)
        	
        	String from =  SelectAddress_M_getFrom()
        	if "DriveTo" == from
        	   Page.setHelpMsg("$//$driveto")
        	else
        	   Page.setHelpMsg("$//$locations")
        	endif
        	
        	string mask = getMask()
        	int i=0
        	string itemId
        	string temp
        	while i < String.getLength(mask)
				itemId = "item" + i
				temp = String.at(mask,i,1)
				if temp == "0"
					Page.setComponentAttribute(itemId,"visible","0")
				else
					Page.setComponentAttribute(itemId,"visible","1")
				endif
				i = i + 1
			endwhile
			
			if "0" == String.at(mask,1,1)
				Page.setComponentAttribute("nullField2","visible","0")
				Page.setComponentAttribute("nullField2","height","1")
			else
				Page.setComponentAttribute("nullField2","visible","1")
			endif
			
			if "1" == String.at(mask,3,1)
				checkMyFavorite()
			endif
            		
            DriveTo_M_saveStopType(<%=Constant.STOP_GENERIC%>)
            if(Account.isTnMaps()==1)
            	Page.setComponentAttribute("item2","visible","0")
            endif
        endfunc
        
        func onShow()
			string mask=getMask()
			
			if "1" == String.at(mask,0,1)
				checkResumeTrip()
			else
				Page.setComponentAttribute("nullField1","visible","0")
				Page.setComponentAttribute("nullField1","height","1")
			endif
				
			if "1" == String.at(mask,2,1)
				checkHomeAddress()
			endif        	

			if 0 == ServerDriven_CanResumeTrip()
        	   		Page.setComponentAttribute("item0","visible","0")
        	endif
				
			if 1 == ServerDriven_CanResumeIcon()
					Page.setComponentAttribute("itemImage0","visible","1")
			else
					Page.setComponentAttribute("itemImage0","visible","0")
			endif
					
			#duplicate of check my favorite to deal with back issue.  
			if "1" == String.at(mask,3,1)
				checkMyFavorite()
			endif
			if SelectContact_M_getFromContactFlag()
				SelectContact_M_setFromContactFlag(0)
				CommonAPI_SetFocus("item5")
				checkContact()
			endif
			if SelectAddress_M_getNoCoverage()==1
        		SelectAddress_M_deleteNoCoverage()
        		System.showErrorMsg("<%=msg.get("common.nocell.warning")%>")
        		return FAIL
        	endif
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
		        
        
        func checkHomeAddress()
        	JSONObject joAddress = SetUpHome_M_getHome()
        	string homeLabel = "<%=msg.get("selectaddress.home")%>"
        	if joAddress == NULL
        		homeLabel = "<%=msg.get("selectaddress.setuphome")%>"
      	
        	endif
        	
        	if joAddress == NULL
        		MenuItem.setItemValid("item2",0,1)
        		MenuItem.setItemValid("item2",1,0)
        		MenuItem.setItemValid("item2",2,0)
        		MenuItem.setItemValid("item2",3,0)
        		MenuItem.setItemValid("item2",4,0)
        		MenuItem.setItemValid("item2",5,0)
        	else
        		MenuItem.setItemValid("item2",0,0)
        		MenuItem.setItemValid("item2",1,1)
        		MenuItem.setItemValid("item2",2,1)
        		MenuItem.setItemValid("item2",3,1)
        		MenuItem.setItemValid("item2",4,1)
        		MenuItem.setItemValid("item2",5,1)
        	endif
        	MenuItem.commitSetItemValid("item2")
        	Page.setComponentAttribute("itemlabel2","text",homeLabel)  
        endfunc
        
        func checkResumeTrip()
        	JSONObject jo = SelectAddress_M_getResumeAddress()
        	if jo == NULL
        		Page.setComponentAttribute("item0","visible","0")
        		Page.setComponentAttribute("nullField1","visible","0")
        		Page.setComponentAttribute("nullField1","height","1")
        		return FAIL
        	else
        		Page.setComponentAttribute("item0","visible","1")
        		Page.setComponentAttribute("nullField1","visible","1")
        	endif
			
			if isJsonPoi(jo)
				jo = JSONObject.get(jo,"stop")
			endif
			
			string label = JSONObject.getString(jo,"label")
        	string firstLine = JSONObject.getString(jo,"firstLine")
        	string city = JSONObject.getString(jo,"city")
        	string state = JSONObject.getString(jo,"state")
        	string zip = JSONObject.getString(jo,"zip")
        	string text = ""
        	
        	if	firstLine != ""
        		text = firstLine + ","
        	elsif label != ""
        		text = label + ","
        	endif
        	
        	if city != ""
        		text = text  + " " + city + ","
        	endif

        	if state != ""
        		text = text + " " + state + "," 
        	endif
        	
        	if text != ""
        		text = String.at(text,0,String.getLength(text)-1)
        	endif
        	
        	Page.setComponentAttribute("resumeAddress","text",text)      	
        	#check if Home is same with Resume Trip
        	JSONObject joAddress = SetUpHome_M_getHome()
        	if joAddress != NULL
        		if JSONObject.getInt(jo,"lon") == JSONObject.getInt(joAddress,"lon")
        			if JSONObject.getInt(jo,"lat") == JSONObject.getInt(joAddress,"lat")
        				println("--resume trip is same with home address")
		        		Page.setComponentAttribute("item0","visible","0")
		        		Page.setComponentAttribute("nullField1","visible","0")
		        		Page.setComponentAttribute("nullField1","height","1")
        				return FAIL
        			endif
        		endif
        	endif           	        	
        endfunc
        
        func onClickClearTrip()
        	SelectAddress_M_clearResumeAddress()
        	Page.setComponentAttribute("item0","visible","0")
        	Page.setComponentAttribute("nullField1","visible","0")
        	Page.setComponentAttribute("nullField1","height","1")
        endfunc
        
        func onClickResumeTrip()
        	CommonAPI_SetFocus("item0")
        	JSONObject jo = SelectAddress_M_getResumeAddress()
        	JSONObject.put(jo, "isResumeTrip", "true")
        	TxNode node
        	TxNode.addMsg(node,JSONObject.toString(jo)) 
        	
			if isJsonPoi(jo)
				jo = JSONObject.get(jo,"stop")
			endif
        	int stopType = <%=Constant.STOP_GENERIC%>
        	if NULL != JSONObject.get(jo,"type")
        	   stopType = JSONObject.get(jo,"type")
        	endif
            DriveTo_M_saveStopType(stopType)
			backToCaller(node)
			return FAIL        	
        endfunc
        
        func onClickContact()	
        	CommonAPI_SetFocus("item5")
            DriveTo_M_saveStopType(<%=Constant.STOP_GENERIC%>)
			JSONObject joContact
        	JSONObject.put(joContact,"callbackpageurl","<%=callBackPageUrl%>" + "#" + SelectAddress_M_getFrom())
        	JSONObject.put(joContact,"callbackfunction","CallBack_SelectContact")
        	JSONObject.put(joContact,"type","1")
			SelectContact_C_show(joContact)
			return FAIL        	
        endfunc
        
        func onClickBusiness()
        	CommonAPI_SetFocus("item8")
        	if isSearchSpeakInput()
        		JSONObject jo
	        	JSONObject.put(jo,"poiSearchType",100)
	        	JSONObject.put(jo,"callbackpageurl",SelectAddress_M_getCallbackpageurl())
	        	JSONObject.put(jo,"callbackfunction",SelectAddress_M_getCallbackfunction())
				invokeSpeakSearch(jo)
        	else
				SearchPoi_C_searchForBusinessInitial(SelectAddress_M_getCallbackpageurl(),SelectAddress_M_getCallbackfunction())        
        	endif
        	return FAIL
        endfunc
        
        func CallBack_SelectContact()
        	SelectContact_M_setFromContactFlag(1)
        endfunc
        
        func checkContact()
        	TxNode node
			node=SelectContact_M_getContact()
			if node != NULL 
				TxNode firstNode = TxNode.childAt(node,0)
				if firstNode != NULL
					TxNode stopNode = TxNode.childAt(firstNode,0)
					if stopNode != NULL
					    String firstLine = checkNULL(TxNode.msgAt(stopNode,1))
					    String lastLine = checkNULL(TxNode.msgAt(stopNode,2)) + "," + checkNULL(TxNode.msgAt(stopNode,3)) + " " + checkNULL(TxNode.msgAt(stopNode,5))
				 		if ", " == lastLine
					       lastLine = ""
					    endif
				 		JSONObject jo
				        JSONObject.put(jo,"firstLine",firstLine)
				        JSONObject.put(jo,"lastLine",lastLine)
						JSONObject.put(jo,"country",checkNULL(TxNode.msgAt(stopNode,6)))
						
						String s = JSONObject.toString(jo)
						AddressCapture_M_init(SelectAddress_M_getCallbackpageurl(),SelectAddress_M_getCallbackfunction(),SelectAddress_M_getFrom())			
						doRequest(s)				
					endif
				endif
        	endif    
        endfunc
        
        func checkNULL(string s)
        	if s== NULL
        		return ""
        	else
        		return s	
        	endif
        endfunc
        
        func recentClick()
        	CommonAPI_SetFocus("item4")
            AddressCapture_M_recentPlace(getAcInputParameter())
            return FAIL
        endfunc
        
        func favoritesClick()
        	CommonAPI_SetFocus("item3")    	
        	SelectAddress_M_saveMaskForFavorite(getAcInputParameter())
            AddressCapture_M_favorites(getAcInputParameter())
            return FAIL
        endfunc
		]]>
	</tml:script>
	
	<tml:menuItem name="showAddress" pageURL="">
		<tml:bean name="callFunction" valueType="String" value="loadAddress">
		</tml:bean>
	</tml:menuItem>
	
    <tml:menuItem name="goBack"/>
	<tml:menuItem name="callback" pageURL="" trigger="TRACKBALL_CLICK">
		<tml:bean name="callFunction" valueType="string" value=""/>
	</tml:menuItem>
	<tml:menuItem name="address" trigger="TRACKBALL_CLICK" onClick="onClickAddress"/>
	<tml:menuItem name="city" trigger="TRACKBALL_CLICK" onClick="onClickCity"/>
	<tml:menuItem name="airport" trigger="TRACKBALL_CLICK" onClick="onClickAirport"/>
	<tml:menuItem name="intersection" trigger="TRACKBALL_CLICK" onClick="onClickIntersection"/>
	<tml:menuItem name="currentlocation"  trigger="TRACKBALL_CLICK" onClick="onClickCurrentLocation"/>
	<tml:menuItem name="rateThis" pageURL="<%=getPage + "RatePoi"%>"/>
	<tml:menuItem name="clearTripMenu" onClick="onClickClearTrip" text="<%=msg.get("selectaddress.menu.cleartrip")%>" trigger="KEY_MENU|KEY_CONTEXT_MENU"/>
	<tml:menuItem name="resumeTripMenu" onClick="onClickResumeTrip" trigger="TRACKBALL_CLICK"/>
	<tml:menuItem name="setupHomeMenu" onClick="onClickSetupHome" text="<%=msg.get("selectaddress.menu.setuphome")%>" trigger="KEY_MENU|TRACKBALL_CLICK"/>
	<tml:menuItem name="driveToHomeMenu" onClick="onClickSetupHome" trigger="TRACKBALL_CLICK"/>
	<tml:menuItem name="editHomeMenu" onClick="onClickEditHome" text="<%=msg.get("selectaddress.menu.edithome")%>" trigger="KEY_MENU"/>
	<tml:menuItem name="deleteHomeMenu" onClick="onClickDeleteHome" text="<%=msg.get("selectaddress.menu.delete")%>" trigger="KEY_MENU"/>
	<tml:menuItem name="editHomeContextMenu" onClick="onClickEditHome" text="<%=msg.get("selectaddress.menu.edithome")%>" trigger="KEY_CONTEXT_MENU"/>
	<tml:menuItem name="deleteContextMenu" onClick="onClickDeleteHome" text="<%=msg.get("selectaddress.menu.delete")%>" trigger="KEY_CONTEXT_MENU"/>
	<tml:menuItem name="contact" onClick="onClickContact" trigger="TRACKBALL_CLICK"/>
	<tml:menuItem name="business" onClick="onClickBusiness" trigger="TRACKBALL_CLICK"/>
	
	<tml:menuItem name="recentClick" onClick="recentClick"/>
	<tml:menuItem name="favoritesClick" onClick="favoritesClick"/>
	<tml:page id="SelectAddress" url="<%=pageUrl%>" type="<%=pageType%>" helpMsg="$//$driveto" groupId="<%=GROUP_ID_AC%>">
		<tml:title id="title" align="center|middle" fontWeight="bold|system_large" fontColor="white">
		</tml:title>		
		<tml:image id="background" url="" align="left|top"/>
		<tml:panel id="menuListBox" name="pageListBox:settingsList"
			layout="vertical" >
			<tml:nullField id="nullField1" />
			<tml:compositeListItem id="item0" visible="true" bgColor="#FFFFFF"
				isFocusable="true" transparent="false">
				<!--if you want to show resume trip icon(itemImage0) on the resume trip item , add an item in sdp_common.xml, the item shoud be : <entry key="PRODUCT_TYPE...RESUME_ICON" value="1"/> -->
				<tml:image id="itemImage0" url="" align="left|top"/>
				<tml:label id="resumeAddress" focusFontColor="white"
					fontWeight="system_large" textWrap="ellipsis"
					align="left">
				</tml:label>
				<tml:menuRef name="clearTripMenu" />
				<tml:menuRef name="resumeTripMenu" />
			</tml:compositeListItem>
			<tml:nullField id="nullField2" />
			<tml:compositeListItem id="item1" visible="true" bgColor="#FFFFFF"
				isFocusable="true" transparent="false">
				<tml:image id="itemImage1" url="" align="left|top"/>
				<tml:label id="itemlabel1" focusFontColor="white"
					fontWeight="system_large" textWrap="ellipsis"
					align="left|middle">
					<%=msg.get("selectaddress.currentlocation")%>
				</tml:label>
				<tml:menuRef name="currentlocation" />
			</tml:compositeListItem>
			<tml:nullField id="nullField3" />
			<tml:compositeListItem id="item2" visible="true" bgColor="#FFFFFF"
				isFocusable="true" transparent="false">
				<tml:image id="itemImage2" url="" align="left|top"/>
				<tml:label id="itemlabel2" focusFontColor="white"
					fontWeight="system_large" textWrap="ellipsis"
					align="left|middle"></tml:label>
				<tml:menuRef name="setupHomeMenu" />
				<tml:menuRef name="editHomeMenu" />
				<tml:menuRef name="deleteHomeMenu" />
				<tml:menuRef name="driveToHomeMenu" />
				<tml:menuRef name="editHomeContextMenu" />
				<tml:menuRef name="deleteContextMenu" />
			</tml:compositeListItem>
				<tml:compositeListItem id="item3" visible="true" bgColor="#FFFFFF"
				isFocusable="true" transparent="false">
				<tml:image id="itemImage3" url="" align="left|top"/>
				<tml:label id="itemlabel3" focusFontColor="white"
					fontWeight="system_large" textWrap="ellipsis"
					align="left|middle"><%=msg.get("selectaddress.favorite")%>
					</tml:label>
				<tml:image id="itemImageNewFav" url="" align="left|top"/>
					<tml:label id="itemLabelFav" fontColor="white"
					fontWeight="bold|system_small" 
					align="center|middle">
					</tml:label>

				<tml:menuRef name="favoritesClick" />
				
			</tml:compositeListItem>
		<tml:compositeListItem id="item4" visible="true" bgColor="#FFFFFF"
				isFocusable="true" transparent="false">
				<tml:image id="itemImage4" url="" align="left|top"/>
				<tml:label id="itemlabel4" focusFontColor="white"
					fontWeight="system_large" textWrap="ellipsis"
					align="left|middle"><%=msg.get("selectaddress.recentplace")%></tml:label>
				<tml:menuRef name="recentClick" />
				
			</tml:compositeListItem>
			<tml:compositeListItem id="item5" visible="true" bgColor="#FFFFFF"
				isFocusable="true" transparent="false">
				<tml:image id="itemImage5" url="" align="left|top"/>
				<tml:label id="itemlabel5" focusFontColor="white"
					fontWeight="system_large" textWrap="ellipsis"
					align="left|middle">
				<%=msg.get("selectaddress.contact")%></tml:label>
				<tml:menuRef name="contact" />
			</tml:compositeListItem>
			<tml:nullField id="nullField4" />
			
			<tml:compositeListItem id="item6" visible="true" bgColor="#FFFFFF"
				isFocusable="true" transparent="false">
				<tml:image id="itemImage6" url="" align="left|top"/>
				<tml:label id="itemlabel6" focusFontColor="white"
					fontWeight="system_large" textWrap="ellipsis"
					align="left|middle">
				<%=msg.get("selectaddress.addressLabel")%>
				</tml:label>
				<tml:menuRef name="address" />
			</tml:compositeListItem>
			<tml:compositeListItem id="item7" visible="true" bgColor="#FFFFFF"
				isFocusable="true" transparent="false">
				<tml:image id="itemImage7" url="" align="left|top"/>
				<tml:label id="itemlabel7" focusFontColor="white"
					fontWeight="system_large" textWrap="ellipsis"
					align="left|middle">
				<%=msg.get("selectaddress.city")%>
				</tml:label>
				<tml:menuRef name="city" />
			</tml:compositeListItem>
			<tml:compositeListItem id="item8" visible="true" bgColor="#FFFFFF"
				isFocusable="true" transparent="false">
				<tml:image id="itemImage8" url="" align="left|top"/>
				<tml:label id="itemlabel8" focusFontColor="white"
					fontWeight="system_large" textWrap="ellipsis"
					align="left|middle">
				<%=msg.get("selectaddress.business")%></tml:label>
				<tml:menuRef name="business" />
			</tml:compositeListItem>
			<tml:compositeListItem id="item9" visible="true" bgColor="#FFFFFF"
				isFocusable="true" transparent="false">
				<tml:image id="itemImage9" url="" align="left|top"/>
				<tml:label id="itemlabel9" focusFontColor="white"
					fontWeight="system_large" textWrap="ellipsis"
					align="left|middle">
				<%=msg.get("selectaddress.airport")%></tml:label>
				<tml:menuRef name="airport" />
			</tml:compositeListItem>
			<tml:compositeListItem id="item10" visible="true" bgColor="#FFFFFF"
				isFocusable="true" transparent="false">
				<tml:image id="itemImage10" url="" align="left|top"/>
				<tml:label id="itemlabel10" focusFontColor="white"
					fontWeight="system_large" textWrap="ellipsis"
					align="left|middle">
				<%=msg.get("selectaddress.intersection")%></tml:label>
				<tml:menuRef name="intersection" />
			</tml:compositeListItem>
			<tml:nullField id="nullField5" />
		</tml:panel>
		<tml:image id="titleShadow" url=""   visible="true" align="left|top"></tml:image>
	</tml:page>
	<cserver:outputLayout />
</tml:TML>
