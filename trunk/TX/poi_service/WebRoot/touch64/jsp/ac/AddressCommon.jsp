<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ include file="model/AddressCaptureModel.jsp"%>
<%@ include file="controller/SetUpHomeController.jsp"%>
<%@ include file="AddressAjax.jsp"%>
<%@include file="/touch64/jsp/StopUtil.jsp"%>

	<tml:script language="fscript" version="1">
		<![CDATA[
	func initAddress()
		#Here, addressNode is used to display drop down list content for firstline input box, it consists of two sub node, 
		# One is common node, store favorite stop and home or office address
		# The other is recent stop node, stop last ten recent stop 
		# It is consistent with client code when create the filter for fristline drop down, filter = new Filter(commonListNode,recentyListNode);
		# Now, find a bug in client, client only read handle recentyListNode when user choose, so add commonListNode to recentyListNode
        TxNode addressNode
		TxNode commonListNode 
		TxNode recentyListNode
		
		# Why we add country here?
		# In RIM6.4 Telcel, user change switch country before search addresses, after user switching from one country to another,
		# first, refresh the ac template in type address page first, then filter out the stop node that not belong to choosing country before creating filter for firstline input box
		# Thus, we user first search address in Mexico, then switch to US, the drop list won't display Mexico address but US address.
		String country
		String countryInStop
		country = AddressCapture_M_getCountry_ForAcTemplate()
		if country == ""
			country = "unknow"
		endif
		#println("[debug log in AddressCommon.jsp], cached country is : " + country)
		
		# 1. add favorite address to recentyListNode
        TxNode favoriteNode
		favoriteNode = Favorite.getAddresses()
		if favoriteNode != NULL
			String favorite
			TxNode favoriteStop
			int favoriteSize = TxNode.getStringSize(favoriteNode)
			
			int i = 0
			while i < favoriteSize
				favorite = TxNode.msgAt(favoriteNode,i)
				favoriteStop = TxNode.childAt(favoriteNode,i)
			
				if favoriteStop != NULL
					countryInStop = checkNULL(TxNode.msgAt(favoriteStop,6))
					#println("[debug log in AddressCommon.jsp],  favorite stop is : \n" + favoriteStop)
					#println("[debug log in AddressCommon.jsp], country in favorite stop is : \n" + countryInStop)					
					if countryInStop == "" || country == "unknow" || countryInStop == country
						#println("add favorite: " + favorite + "(" + countryInStop + "," + country + ")")
						TxNode.addMsg(recentyListNode,favorite)
						TxNode.addChild(recentyListNode,favoriteStop)
					endif
				endif
				i = i + 1
			endwhile				
		endif

		# 2. add home or office address to recentyListNode
		TxNode homeOrOfficeAddressNode
        JSONObject homeOrOfficeAddress = SetUpHome_C_getHome()
        if NULL != homeOrOfficeAddress
			TxNode addressStop = convertToStop(homeOrOfficeAddress)
			String address = ""
			if JSONObject.get(homeOrOfficeAddress,"firstLine") != NULL
              address = JSONObject.get(homeOrOfficeAddress,"firstLine")
           endif
           String zipCode = JSONObject.get(homeOrOfficeAddress,"zip")
           String neighborhood =  JSONObject.get(homeOrOfficeAddress,"locality" )
            if  neighborhood != NULL && neighborhood != ""
				if address != NULL && address != ""
					address = address + "," + neighborhood 
				else
				    address = neighborhood
				endif
			endif
               
           if "" != address
              address = address + ", " + JSONObject.get(homeOrOfficeAddress,"city") + ", " + JSONObject.get(homeOrOfficeAddress,"state")
           else
              address = JSONObject.get(homeOrOfficeAddress,"city") + ", " + JSONObject.get(homeOrOfficeAddress,"state")
           endif
           if ""!= zipCode
              address = address + " " + zipCode
           endif
		   
           if addressStop != NULL
				countryInStop = checkNULL(TxNode.msgAt(addressStop,6))
				#println("[debug log in AddressCommon.jsp],  home or office stop is : \n" + addressStop)				
				#println("[debug log in AddressCommon.jsp], country in home or office is : \n" + countryInStop)
				if countryInStop == "" || country == "unknow" || countryInStop == country
					#println("add home: " + address + "(" + countryInStop + "," + country + ")")
					TxNode.addMsg(recentyListNode,address)
					TxNode.addChild(recentyListNode,addressStop)
				endif	
			endif
        endif

		# 3. add last ten recent stop address to lastTenRecentPlaceNode
        TxNode recentPlaceNode = RecentPlaces.getAddresses()
        int size = TxNode.getStringSize(recentPlaceNode)
        int recentPlacesSize = <%=Constant.RECENT_PLACES_SIZE%>
        if size > recentPlacesSize
           size = recentPlacesSize
        endif

        String recentPlace
		TxNode recentPlaceStop
        int j = 0
        while j < size
			recentPlace = TxNode.msgAt(recentPlaceNode,j)
		   recentPlaceStop = TxNode.childAt(recentPlaceNode,j)
		   
		   if recentPlaceStop != NULL
				countryInStop = checkNULL(TxNode.msgAt(recentPlaceStop,6))
				#println("[debug log in AddressCommon.jsp],  recent place stop is : \n" + recentPlaceStop)					
				#println("[debug log in AddressCommon.jsp], country in recent place is : \n" + countryInStop)			
				if countryInStop == "" || country == "unknow" || countryInStop == country
					#println("add recent: " + recentPlace + "(" + countryInStop + "," + country + ")")	
					TxNode.addMsg(recentyListNode,recentPlace)
					TxNode.addChild(recentyListNode,recentPlaceStop)
				endif
			endif
           j = j + 1
        endwhile
	#  add commonListNode and recentyListNode to addressNode
	
		# why adding following code, strange?
		# Because client has bug, client code use whether the size of lastTenRecentPlaceNode is big than zero to decide whether display arrow in the right corner of input box
		# That is not right, it should use the total size of commonListNode and lastTenRecentPlaceNode to make judgement
		# We fix it in server side
		#if TxNode.getStringSize(lastTenRecentPlaceNode) < 1 && TxNode.getStringSize(commonListNode) > 0
		#		TxNode.addMsg(lastTenRecentPlaceNode,TxNode.msgAt(commonListNode, 0))
		#		TxNode.addChild(lastTenRecentPlaceNode,TxNode.childAt(commonListNode,0))
		#endif
		
		TxNode.addChild(addressNode,commonListNode)		
        TxNode.addChild(addressNode,recentyListNode)
        Page.setFieldFilter("firstLine",addressNode)
			
		TxNode nearCityNode = AddressCapture_M_setCacheCity()
		if NULL != nearCityNode
			Page.setFieldFilter("cityName", nearCityNode)
			Page.setFieldFilter("state", nearCityNode)
		   Page.setFieldFilter("cityCountyOrPostalCode",nearCityNode)			
		
			String cityInOneLine
			TxNode cityStop
			TxNode filterNearCityNode
			#println("[debug log in AddressCommon.jsp],  nearby city : \n" + nearCityNode)	
			int nearCitySize = TxNode.getStringSize(nearCityNode)
			int k = 0
			while k < nearCitySize
				cityInOneLine = TxNode.msgAt(nearCityNode,k)
				cityStop = TxNode.childAt(nearCityNode,k)
			   
			   if cityStop != NULL
					countryInStop = checkNULL(TxNode.msgAt(cityStop,6))
					#println("[debug log in AddressCommon.jsp],  near city stop is : \n" + cityStop)					
					#println("[debug log in AddressCommon.jsp], country in near city stop is : \n" + countryInStop)				
					if countryInStop == "" || country == "unknow" || countryInStop == country
						TxNode.addMsg(filterNearCityNode,cityInOneLine)
						TxNode.addChild(filterNearCityNode,cityStop)
					endif
				endif
			   k = k + 1
			endwhile
		   Page.setFieldFilter("lastLine",filterNearCityNode)
		endif
		AddressCapture_M_initType("TypeAddress")
		
		String errorMsgStr = AddressCapture_M_getErrorMsg()
		if "" != errorMsgStr
			System.showErrorMsg(errorMsgStr)
		endif			
	endfunc
	
	func fillBoxForStreet()
		   String firstLine = ""
		   String city = ""
		   String county = ""
		   String state =  ""
		   String postalCode = ""
		   String lastLine = ""
		   
			TxNode inputNode = ParameterSet.getParam("firstLine")
			TxNode  lastNode = ParameterSet.getParam("lastLine")

			if NULL != inputNode
			   String  address = TxNode.msgAt(inputNode,0)
			   int length = String.getLength(address)
			   int index = String.find(address,0,",")
			   int nextIndex = String.find(address,index,",")
			   if nextIndex != -1
				   firstLine = String.at(address,0, index)
				   int lastLineIndex = index + 1
				   int lastLineLength = length - index - 1
				   lastLine = String.at(address,lastLineIndex, lastLineLength)
			   else
				   firstLine = address
				   lastLine = ""
			   endif

			   # the protocol of stop TxNode
			   # msg 0: label
			   # msg 1: firstline
			   # msg 2: city
			   # msg 3: state
			   # msg 4: stopId
			   # msg 5: zip
			   # msg 6: country
			   # msg 7: county

			   TxNode stop = TxNode.childAt(inputNode, 0)
			   if stop != NULL
					String firstLineInNode = checkNULL(TxNode.msgAt(stop,1))
					if "" != firstLineInNode
						firstLine = firstLineInNode
					endif
					city = checkNULL(TxNode.msgAt(stop,2))
					state = checkNULL(TxNode.msgAt(stop,3))
					postalCode = checkNULL(TxNode.msgAt(stop,5))
					county = checkNULL(TxNode.msgAt(stop,7))
			   endif

				Page.setComponentAttribute("firstLine","text",firstLine)
				Page.setComponentAttribute("firstLine","cursorIndex","0")
				
				if lastLine != ""
					Page.setComponentAttribute("lastLine","text",lastLine)
					Page.setComponentAttribute("lastLine","cursorIndex","0")
				endif
				
				# for international address,
				if city != ""
					Page.setComponentAttribute("cityName","text",city)
				endif
				if state != ""
					Page.setComponentAttribute("state","text",state)		
				endif
				if postalCode != ""
					Page.setComponentAttribute("postalCode","text",postalCode)	
				endif
				if county != ""
					Page.setComponentAttribute("county","text",county)
				endif


			   Page.setControlProperty("submitButton","focused","true")
			endif
	endfunc
	
	func fillBoxForCity()
			TxNode cityNameNode = ParameterSet.getParam("cityName")
			if cityNameNode != NULL
				String city	= checkNULL(TxNode.msgAt(cityNameNode,0))
				Page.setComponentAttribute("cityName","text",city)
			endif
			if isVisiblePageComponentAttribute( "county" )
				Page.setControlProperty("county","focused","true")
			elseif isVisiblePageComponentAttribute( "state" )
				Page.setControlProperty("state","focused","true")
			elseif isVisiblePageComponentAttribute( "lastLine" )
				Page.setControlProperty("lastLine","focused","true")
			else
				Page.setControlProperty("submitButton","focused","true")
			endif
	endfunc
	
	func fillBoxForState()
			TxNode stateNameNode = ParameterSet.getParam("state")
			if stateNameNode != NULL
				String state = checkNULL(TxNode.msgAt(stateNameNode,0))
				Page.setComponentAttribute("state","text",state)
			endif
			if isVisiblePageComponentAttribute( "lastLine" )
				Page.setControlProperty("lastLine","focused","true")
			elseif isVisiblePageComponentAttribute( "postalCode" )
				Page.setControlProperty("postalCode","focused","true")	
			else
				Page.setControlProperty("submitButton","focused","true")	
			endif
	endfunc
	
	func fillBoxForCityCountyOrPostalCode()
			TxNode cityNameNode = ParameterSet.getParam("cityName")
			if cityNameNode != NULL
				String city	= checkNULL(TxNode.msgAt(cityNameNode,0))
				Page.setComponentAttribute("cityOrPostalCode","text",city)
			endif
				Page.setControlProperty("submitButton","focused","true")
	endfunc

	func validateAddressOnClick()

		if submitValidate() == FALSE
		      return FAIL
		  endif
		      
		   
            
            JSONObject requestJo
            String sourceResource = getSourceResourceAndSetRequestJSON( requestJo )
		     
     
		if !Cell.isCoverage()
			System.showErrorMsg("<%=msg.get("common.nocell.error")%>")
            return FAIL
		endif

		JSONObject cacheJo = AddressCapture_M_getCacheAddress()
		
		if NULL != cacheJo
		   JSONObject inputAddressJo = AddressCapture_M_getInputAddress()
		   if NULL != inputAddressJo
		   
		       String cacheResource = getCacheResource( cacheJo )
			   
			   if cacheResource == sourceResource
			       AddressCapture_M_returnAddressToInvokerPage(cacheJo)
			       return FAIL
			   endif
			   #if isVisiblePageComponentAttribute( "lastLine" ) 
			   #    if NULL != cacheFirstLine && NULL != cacheLastLine && NULL != cacheCountry && firstLine == cacheFirstLine && lastLine == cacheLastLine && country == cacheCountry
			   #       AddressCapture_M_returnAddressToInvokerPage(cacheJo)
			   #      return FAIL
			   #    endif
			   #else
			   #   if NULL != cacheFirstLine && cacheCity != NULL && cacheState != NULL && cachePostalCode != NULL && NULL != cacheCountry && firstLine == cacheFirstLine && cacheCity == cityName && cacheState == state && cachePostalCode == postalCode && country == cacheCountry
			   #       AddressCapture_M_returnAddressToInvokerPage(cacheJo)
			   #	      return FAIL
			   #    endif
			   #endif
			   
			   
		   endif
		endif
		
		#check if address exist in My Favorite or Recent Place
		string inputAddress = ""
		String lastLine = getComponentValue("lastLine")
		String firstLine = getComponentValue("firstLine")
		if lastLine == ""
			inputAddress = lastLine
		else
			inputAddress = firstLine + ", " + lastLine	
		endif
	
		#if address exist in MyFavorite or Recent Place, does not do validation
		TxNode cacheAddressNode = AC.getStopByDetailLine(inputAddress)
		if cacheAddressNode != NULL && TxNode.getValueSize(cacheAddressNode) > 2
			cacheJo = convertStopToJSON(cacheAddressNode)
			AddressCapture_M_returnAddressToInvokerPage(cacheJo)	
			return FAIL
		else
		    AddressCapture_M_saveInputAddress(requestJo)
	    	String s = JSONObject.toString(requestJo)
		    println( ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>submit s : " + s )
		    doRequest(s)			
		endif
    endfunc
	
	func refreshOnClick()
		TxNode countryNode
		countryNode = ParameterSet.getParam("countryLine")
		AddressCapture_M_saveCountry_ForAcTemplate(countryNode)
		initPageComponent()
		initAddress()
		clearInputHistory()
		Page.setControlProperty("firstLine","focused","true")
	endfunc
	
	func clearInputHistory()
		if isVisiblePageComponentAttribute("firstLine")
			Page.setComponentAttribute("firstLine","text","")
		endif
		if isVisiblePageComponentAttribute("door")
			Page.setComponentAttribute("door","text","")
		endif
		if isVisiblePageComponentAttribute("neighborhood")
			Page.setComponentAttribute("neighborhood","text","")
		endif
		if isVisiblePageComponentAttribute("streetName")
			Page.setComponentAttribute("streetName","text","")
		endif
		if isVisiblePageComponentAttribute("crossStreetName")
			Page.setComponentAttribute("crossStreetName","text","")
		endif
		if isVisiblePageComponentAttribute("county")
			Page.setComponentAttribute("county","text","")
		endif
		if isVisiblePageComponentAttribute("cityName")
			Page.setComponentAttribute("cityName","text","")
		endif
		if isVisiblePageComponentAttribute("state")
			Page.setComponentAttribute("state","text","")
		endif
		if isVisiblePageComponentAttribute("postalCode")
			Page.setComponentAttribute("postalCode","text","")
		endif
		if isVisiblePageComponentAttribute("lastLine")
			Page.setComponentAttribute("lastLine","text","")
		endif
	endfunc
		
			
	]]>
</tml:script>
