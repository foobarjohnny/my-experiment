<%@ page language="java" pageEncoding="UTF-8"%>

<tml:script language="fscript" version="1">
		<![CDATA[
func getTemplate()
   #println( "............................................................................................" )
   String from = Page.getControlProperty("page","url_flag")
   #println( "Template : " + from )
   int addressTemplate = 1
   int weatherTemplate = 2
   if from == NULL || from == ""
       return FAIL
   endif
   if from == "Address" || from == "address"
       return addressTemplate
   elseif from == "Weather" || from == "weather"
       return weatherTemplate
	else
	    #println( "###################" )
	   return addressTemplate
   endif 
endfunc

func getTemplateAttribute( String key )
    int id = getTemplate()
	#println( id )
	String prefixForCountry = AddressCapture_M_getCountry_ForAcTemplate()
	if EditHome_M_isFromHome()
		String countryFromEditHome = EditHome_M_getCountry()
		if NULL != countryFromEditHome && "" != countryFromEditHome
			prefixForCountry = countryFromEditHome
		endif
	endif
							
	if "" != prefixForCountry
		prefixForCountry = prefixForCountry + "_"
	endif
	#println("~~~~~~~~~~for debug~~~~~~~~~~~getTemplateAttribute, key=" + prefixForCountry + key)
	
   	TxNode node = System.getAceTemplate( id , prefixForCountry + key)
   	if node != NULL
   	    String str = TxNode.msgAt(node,0)  
		if str == NULL || str == ""
		    return NULL
		endif
   	 #   println(key + " "+ id + "=" + str )
		if str == "FALSE" || str == "false"
		    str = "0"
	   elseif str == "TRUE" || str == "true"
	       str = "1"
	    endif
    	return str
    else
		node = System.getAceTemplate( id , key)
		if node != NULL
			String str = TxNode.msgAt(node,0)  
			if str == NULL || str == ""
				return NULL
			endif
		 #   println(key + " "+ id + "=" + str )
			if str == "FALSE" || str == "false"
				str = "0"
		   elseif str == "TRUE" || str == "true"
			   str = "1"
			endif
			return str
		else
			return NULL
		endif
   	endif
endfunc



func getTemplateAttributeFromCountry( String key )
	return getTemplateAttribute( key )
endfunc

func getTemplateComponentAttribute( String componentId , String componentAttribute )
   String key = componentId + "_" + componentAttribute
   #println( "........................................key : " + key )
   return getTemplateAttributeFromCountry( key )
endfunc

func setPageComponentAttribute( String componentId , String componentAttribute , String defaultValue )
  # println( "template " + componentId + ":" +componentAttribute  )
   String value = getTemplateComponentAttribute( componentId ,componentAttribute )
  # println( "----------------------------------------------------------------------------------------------------------------------template " + componentId + ":" +componentAttribute + ":" + value )
   if value == NULL || value == ""
       value = defaultValue
   endif
   Page.setComponentAttribute( componentId ,componentAttribute ,value )
endfunc

func minLenValidate( String value , String minLen )
   if value == NULL || value == ""
      return TRUE
   endif
   if minLen == NULL || minLen == ""
       return TRUE
   endif
   if minLen != NULL && minLen != "" && String.isNumberString(minLen) == TRUE
      int len = String.convertToNumber(minLen)
	  if String.getLength(value) < len 
		 return FALSE
	  endif
	  return TRUE
   else
       return TRUE
   endif
endfunc

func validateMinLen( String componentId )
     if isVisiblePageComponentAttribute( componentId ) == FALSE
	      return TRUE
	 endif
     String minLen = getTemplateComponentAttribute( componentId , "minLen" )
	 #println( "validateMinLen >>>>> " + componentId + ":" + minLen )
     String value = getComponentValue(componentId)
     if minLenValidate( value , minLen ) == FALSE
		  return FALSE
	 endif
	 return TRUE
endfunc

func getComponentValue( String componentId )
    TxNode inputNode = ParameterSet.getParam(componentId) 
    if NULL != inputNode  
        String value = TxNode.msgAt(inputNode, 0)
        return value
    else
        return NULL
    endif
endfunc

func setPageComponentAttribute_inputbox( String componentId )
   #println( ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>template " + componentId  )
   setPageComponentAttribute( componentId , "visible" , "1" )
   setPageComponentAttribute( componentId , "style" , "all" )
   setPageComponentAttribute( componentId , "length" , "-1" )
endfunc

func setPageComponentAttribute_dropDownBox( String componentId )
    setPageComponentAttribute( componentId , "visible" , "1" )
endfunc

func setPageComponentAttributeFromComponentId( String componentId )
   
     String type = getTemplateComponentAttribute( componentId , "type" )
	# println( "setPageComponentAttributeFromComponentId " + componentId + ":" + type )
	 if type == NULL 
		setPageComponentAttribute( componentId , "visible" , "0" )
	     return FAIL
     endif
	 if type == "inputbox"
	     setPageComponentAttribute_inputbox( componentId )
	 elseif type == "dropDownBox"
	     setPageComponentAttribute_dropDownBox( componentId )
     else
	     return FAIL
	 endif
endfunc

func isVisible( String visibleValue )
    if visibleValue == NULL || visibleValue == "" || visibleValue == "1"
        return TRUE
    else
        return FALSE
    endif
endfunc

func isVisiblePageComponentAttribute( String componentId )
    String visible = getTemplateComponentAttribute( componentId , "visible" )
	#println( componentId + ">>>>>>>>>>>>>>>>>>>>>>>>>" + visible )
    return isVisible( visible )
endfunc

func validateComponentAttributeMinLen( String componentId )
    if isVisiblePageComponentAttribute( componentId , "visible" )
        return validateMinLen(  componentId )
    endif
    return TRUE
endfunc

func initPageComponent()
               #println( ".............................initPageComponent...................................." )
               setPageComponentAttributeFromComponentId(  "firstLine" )
		       setPageComponentAttributeFromComponentId( "cityName" )
		       setPageComponentAttributeFromComponentId( "streetName" )
		       setPageComponentAttributeFromComponentId(  "crossStreetName" )
		       setPageComponentAttributeFromComponentId(  "county" )
		       setPageComponentAttributeFromComponentId(  "state" )
		       setPageComponentAttributeFromComponentId(  "postalCode" )
		       setPageComponentAttributeFromComponentId( "door" )
		       setPageComponentAttributeFromComponentId(  "countryLine" )
		       setPageComponentAttributeFromComponentId(  "neighborhood" )
			   setPageComponentAttributeFromComponentId("cityCountyOrPostalCode")
		       setPageComponentAttributeFromComponentId( "lastLine" )	

				# Set country button text
				String countryStr = AddressCapture_M_getCountry_ForAcTemplate()
				if "" == countryStr
					countryStr = "<%=country%>"
				endif	

				println("[browser debug log]: country name is: " + countryStr)
				String from = Page.getControlProperty("page","url_flag")
				if from == "Weather"
					String hintPromptForWeather = "<%=msg.get("ac.weather.prompt")%>"
					String key = "ac.weather.prompt." + countryStr
					String hintPromptForWeatherFromMsg = System.parseI18n("$("+key+")")
					if hintPromptForWeatherFromMsg != NULL && hintPromptForWeatherFromMsg != key
						hintPromptForWeather = hintPromptForWeatherFromMsg
					endif
					if "<%= Constant.CountryForAC.MX%>" == countryStr
						Page.setComponentAttribute("cityCountyOrPostalCode","hint",hintPromptForWeather)
					else
						Page.setComponentAttribute("lastLine","hint",hintPromptForWeather)
					endif
				else
						String hintPrompt = "<%=msg.get("ac.tips.lastLine.other")%>"
						String key = "ac.tips.lastLine.other." + countryStr
						String hintPromptFromMsg = System.parseI18n("$("+key+")")
						if hintPromptFromMsg != NULL && hintPromptFromMsg != key
							hintPrompt = hintPromptFromMsg
						endif
						Page.setComponentAttribute("lastLine","hint",hintPrompt)

						if isSupportCountrySelect()
							if EditHome_M_isFromHome()
								String countryFromEditHome = EditHome_M_getCountry()
								if NULL != countryFromEditHome && "" != countryFromEditHome
									countryStr = countryFromEditHome
									EditHome_M_DeleteFromHome()
								endif
							endif
							String countryKey = "ac.country." + countryStr
							String i18nKey = System.parseI18n("$(" + countryKey + ")")
							String value = countryStr

							JSONObject dataItemJson
							JSONObject.put(dataItemJson, value, i18nKey)
							
							TxNode countryListNode
							countryListNode = AddressCapture_M_getCountryList() 
			
							if NULL != countryListNode
								#println("countryListNode:\n" + countryListNode)
								String countryNameInNode					
								int size = TxNode.getStringSize(countryListNode)
								int j = 0
								while j < size
									countryNameInNode = TxNode.msgAt(countryListNode,j)
									if countryNameInNode != countryStr
										key = "ac.country." + countryNameInNode
										i18nKey = System.parseI18n("$(" + key + ")")
										value = countryNameInNode
										JSONObject.put(dataItemJson, value, i18nKey)
									endif
									j = j + 1
								endwhile
							endif
							Page.setComponentAttribute("countryLine","visible","1")					
							Page.setComponentAttribute("countryLine","dataItemList",JSONObject.toString(dataItemJson))
							
						else
							Page.setComponentAttribute("countryLine","visible","0")						
						endif
				endif
				Page.setComponentAttribute("countryButton","text",countryStr)
				
		
endfunc

func getCountryCode( String index )
       JSONObject countryJo
       <%
		     for( int i = 0 ; i < countryList.size() ; i++ )
	         {
	    %>
	              JSONObject.put(countryJo,"<%=(i+1)%>",<%=countryList.get(i)%> )
	    <%
	         }
	    %>
	    String countryCode = JSONObject.getString( countryJo , index )
	    println( "...........................................countryCode ...... " + countryCode )
	    return countryCode
endfunc

func submitValidate()
		TxNode firstLineNode
		TxNode cityNameNode
        TxNode streetNameNode
        TxNode crossStreetNameNode
        TxNode countyNode
        TxNode stateNode
        TxNode postalCodeNode
        TxNode doorNode
		TxNode countryLineNode
		TxNode neighborhoodNode
		TxNode lastLineNode
		TxNode cityCountyOrPostalCode
		       
		#Street
		firstLineNode = ParameterSet.getParam("firstLine")
		cityNameNode = ParameterSet.getParam("cityName")
		streetNameNode = ParameterSet.getParam("streetName")
		crossStreetNameNode = ParameterSet.getParam("crossStreetName")
		countyNode = ParameterSet.getParam("county")
		stateNode = ParameterSet.getParam("state")
		postalCodeNode = ParameterSet.getParam("postalCode")
		doorNode = ParameterSet.getParam("door")
		lastLineNode = ParameterSet.getParam("lastLine")
		countryLineNode = ParameterSet.getParam("countryLine")
        neighborhoodNode = ParameterSet.getParam("neighborhood")
		cityCountyOrPostalCode = ParameterSet.getParam("cityCountyOrPostalCode")
        #println( "validateAddressOnClick................................................................." )
		if NULL == firstLineNode && NULL == lastLineNode && cityNameNode == NULL && streetNameNode == NULL && crossStreetNameNode == NULL  && countyNode == NULL && stateNode == NULL && postalCodeNode == NULL && doorNode == NULL && countryLineNode == NULL && neighborhoodNode == NULL && cityCountyOrPostalCode == NULL
		    System.showErrorMsg("<%=msg.get("ac.enter.address")%>")
		    Page.setControlProperty("firstLine","focused","true")
            return FALSE
		else
			String addressStr = ""
			if firstLineNode != NULL
					addressStr = addressStr + checkNULL(TxNode.msgAt(firstLineNode, 0))
			endif
			if cityNameNode != NULL
					addressStr = addressStr + checkNULL(TxNode.msgAt(cityNameNode, 0))
			endif
			if streetNameNode != NULL
					addressStr = addressStr + checkNULL(TxNode.msgAt(streetNameNode, 0))
			endif			
			if crossStreetNameNode != NULL
					addressStr = addressStr + checkNULL(TxNode.msgAt(crossStreetNameNode, 0))
			endif			
			if countyNode != NULL
					addressStr = addressStr + checkNULL(TxNode.msgAt(countyNode, 0))
			endif			
			if stateNode != NULL
					addressStr = addressStr + checkNULL(TxNode.msgAt(stateNode, 0))
			endif			
			if postalCodeNode != NULL
					addressStr = addressStr + checkNULL(TxNode.msgAt(postalCodeNode, 0))
			endif			
			if doorNode != NULL
					addressStr = addressStr + checkNULL(TxNode.msgAt(doorNode, 0))
			endif				
			if lastLineNode != NULL
					addressStr = addressStr + checkNULL(TxNode.msgAt(lastLineNode, 0))
			endif				
			if countryLineNode != NULL
					addressStr = addressStr + checkNULL(TxNode.msgAt(countryLineNode, 0))
			endif				
			if neighborhoodNode != NULL
					addressStr = addressStr + checkNULL(TxNode.msgAt(neighborhoodNode, 0))
			endif
			if cityCountyOrPostalCode != NULL
					addressStr = addressStr + checkNULL(TxNode.msgAt(cityCountyOrPostalCode, 0))
			endif

			if "" == addressStr
			    System.showErrorMsg("<%=msg.get("ac.enter.address")%>")
			    Page.setControlProperty("firstLine","focused","true")
                return FALSE
			endif
			
			String lastLine = ""
			if lastLineNode != NULL
			    lastLine = checkNULL(TxNode.msgAt(lastLineNode, 0))
			endif	
			if  isVisiblePageComponentAttribute( "lastLine" ) && lastLineNode != NULL && "" == lastLine
			    System.showErrorMsg("<%=msg.get("ac.enter.city")%>")
			    Page.setControlProperty("lastLine","focused","true")
                return FALSE
			endif
			if validateMinLen( "firstLine" ) == FALSE
		            System.showErrorMsg("<%=msg.get("ac.enter.firstLine.minLen")%>")
		           Page.setControlProperty("firstLine","focused","true")
		            return FALSE
		    endif
			
			if validateMinLen(  "cityName" ) == FALSE
		            System.showErrorMsg("<%=msg.get("ac.enter.cityName.minLen")%>")
		            Page.setControlProperty("cityName","focused","true")
		            return FALSE
		    endif
		    
		    if validateMinLen(  "streetName" ) == FALSE
		            System.showErrorMsg("<%=msg.get("ac.enter.streetName.minLen")%>")
		            Page.setControlProperty("streetName","focused","true")
		            return FALSE
		    endif
		    
		    if validateMinLen(  "crossStreetName" ) == FALSE
		            System.showErrorMsg("<%=msg.get("ac.enter.crossStreetName.minLen")%>")
		            Page.setControlProperty("crossStreetName","focused","true")
		            return FALSE
		    endif
		    
		    if validateMinLen(  "county" ) == FALSE
		            System.showErrorMsg("<%=msg.get("ac.enter.county.minLen")%>")
		            Page.setControlProperty("county","focused","true")
		            return FALSE
		    endif
		    
		    if validateMinLen(  "state" ) == FALSE
		            System.showErrorMsg("<%=msg.get("ac.enter.state.minLen")%>")
		            Page.setControlProperty("state","focused","true")
		            return FALSE
		    endif
		    
		    if validateMinLen(  "postalCode" ) == FALSE
		            System.showErrorMsg("<%=msg.get("ac.enter.postalCode.minLen")%>")
		            Page.setControlProperty("postalCode","focused","true")
		            return FALSE
		    endif
		    
		    if validateMinLen(  "door" ) == FALSE
		            System.showErrorMsg("<%=msg.get("ac.enter.door.minLen")%>")
		            Page.setControlProperty("door","focused","true")
		            return FALSE
		    endif
		    
		    if validateMinLen(  "neighborhood" ) == FALSE
		            System.showErrorMsg("<%=msg.get("ac.enter.neighborhood.minLen")%>")
		            Page.setControlProperty("neighborhood","focused","true")
		            return FALSE
		    endif
		    
		     if validateMinLen(  "lastLine" ) == FALSE
		            System.showErrorMsg("<%=msg.get("ac.enter.lastLine.minLen")%>")
		            Page.setControlProperty("lastLine","focused","true")
		            return FALSE
		    endif
		endif
		return TRUE
endfunc

func getSourceResourceAndSetRequestJSON(  JSONObject jo )
            String firstLine = ""
            String cityName = ""
            String streetName = ""
            String crossStreetName = ""
            String county = ""
            String state = ""
            String postalCode = ""
            String door = ""
            String country = ""
            String neighborhood = ""
		    String lastLine = ""
			String cityCountyOrPostalCode = ""
		    int countryIndex
            if isVisiblePageComponentAttribute( "firstLine" )
		        firstLine = getComponentValue("firstLine")
			else
			    firstLine = ""
			endif
			if isVisiblePageComponentAttribute( "cityName" )
		        cityName = getComponentValue("cityName")
		    else
		        cityName = ""
		    endif
			if isVisiblePageComponentAttribute( "streetName" )
		        streetName = getComponentValue("streetName")
		    else
		        streetName = ""
		    endif
        	if isVisiblePageComponentAttribute( "crossStreetName" )
		        crossStreetName =getComponentValue("crossStreetName")
			else
			    crossStreetName = ""
			endif
            if isVisiblePageComponentAttribute( "county" )
		        county = getComponentValue("county")
		    else
		        county = ""
			endif
            if isVisiblePageComponentAttribute( "state" )
		          state = getComponentValue("state")
		    else
		         state = ""
		    endif
            if isVisiblePageComponentAttribute( "postalCode" )
		        postalCode = getComponentValue("postalCode")
		    else
		        postalCode = ""
			endif
			
			if  isVisiblePageComponentAttribute( "door" )
		        door = getComponentValue("door")
		    else
		        door = ""
			endif
			
			if  isVisiblePageComponentAttribute( "neighborhood" )
		        neighborhood = getComponentValue("neighborhood")
		    else
		        neighborhood = ""
			endif
			
			if  isVisiblePageComponentAttribute( "cityCountyOrPostalCode" )
		        cityCountyOrPostalCode = getComponentValue("cityCountyOrPostalCode")
			endif
			
			if  isVisiblePageComponentAttribute( "lastLine" )
		        lastLine = getComponentValue("lastLine")
		    else
		        lastLine = ""
			endif
			if  isVisiblePageComponentAttribute( "countryLine" )
		        country = getComponentValue("countryLine")
				TxNode  countryLineNode = ParameterSet.getParam("countryLine")
				countryIndex = TxNode.valueAt(countryLineNode,0)
		    else
		        country = ""
                countryIndex = -1
			endif
		
		    if country == NULL || country == "" || countryIndex <= 0
		           country = AddressCapture_M_getCountry_ForAcTemplate()		       
				   if "" == country || NULL == country
		               country =  "<%=country%>"
		           endif
				  # println( "~~~~~~~~~~~~~~~~~~~~[" + country + "]" )
		    else
			      country = getCountryCode( "" + countryIndex )
		    endif
		    
	        if firstLine != NULL && firstLine != ""
	            JSONObject.put(jo,"firstLine",firstLine)
		    endif
			
	        if lastLine != NULL && lastLine != ""
					JSONObject.put(jo,"lastLine",lastLine)
					
					#String tmp = String.trim(lastLine)
					#int length = String.getLength(tmp)
					#if String.isNumberString(tmp)
					#	JSONObject.put(jo,"zip",tmp)
					#elseif String.find(tmp, 0, ",") != -1
					#	int index = String.find(tmp, 0, ",")
					#	JSONObject.put(jo,"city", String.trim(String.at(tmp, 0, index)))
					#	JSONObject.put(jo,"county", String.trim(String.at(tmp, index+1, length-index-1))) 
					#else
					#	JSONObject.put(jo,"city", tmp)
					#endif
			endif
			if country != NULL && country != "" && country != "-1"
			    JSONObject.put(jo,"country",country )			    
			endif
			if cityName != NULL && cityName != ""
			    JSONObject.put(jo,"city",cityName)
			endif
			
			if streetName != NULL && streetName != ""
			    JSONObject.put(jo,"street1",streetName)
			endif
			if crossStreetName != NULL && crossStreetName != ""
			    JSONObject.put(jo,"street2",crossStreetName)
			endif
			if county != NULL && county != ""
			    JSONObject.put(jo,"county",county)     
            endif
			if state != NULL && state != ""
			    JSONObject.put(jo,"state",state)
			endif
			if postalCode != NULL && postalCode != ""
			    JSONObject.put(jo,"zip",postalCode)  
			endif
			if door != NULL && door != ""
			    JSONObject.put(jo,"door",door)   
		    endif
		    if neighborhood != NULL && neighborhood != ""
			    JSONObject.put( jo,"neighborhood",neighborhood )   
		    endif
			if cityCountyOrPostalCode != NULL && cityCountyOrPostalCode != ""
			    JSONObject.put( jo,"cityCountyOrPostalCode",cityCountyOrPostalCode )   
		    endif
		    
		    String sourceResource = "firstLine:" +  firstLine + "lastLine:" + lastLine + "country:" + country  + "cityName:" + cityName + "state:" + state + "postalCode:" + postalCode + "streetName:" + streetName + "crossStreetName:" + crossStreetName + "county:" + county + "door:" + door + "neighborhood:"+neighborhood
            return sourceResource
 endfunc
 
 func getCacheResource( JSONObject inputAddressJo  )
               String cacheFirstLine
		       String cacheLastLine 
		       String cacheCountry
		       String cacheCityName
		       String cacheState
		       String cachePostalCode
		       String cacheStreetName
		       String cacheCrossStreetName
               String cacheCounty
               String cacheDoor
               String cacheNeighborhood
		       cacheFirstLine = JSONObject.get(inputAddressJo,"firstLine")
		       if cacheFirstLine == NULL
		           cacheFirstLine = ""
		       endif
		       cacheLastLine = JSONObject.get(inputAddressJo,"lastLine")
		       if cacheLastLine == NULL
			       cacheLastLine = ""
			   endif
			   cacheCountry = JSONObject.get(inputAddressJo,"country")
			   if cacheCountry == NULL
			       cacheCountry = ""
			   endif
			   cacheCityName = JSONObject.get(inputAddressJo,"city")
			   if cacheCityName == NULL
			       cacheCityName = ""
			   endif
			   cacheState = JSONObject.get(inputAddressJo,"state")
			   if cacheState == NULL
			       cacheState = ""
			   endif
			   cachePostalCode = JSONObject.get(inputAddressJo,"zip")
			   if cachePostalCode == NULL
			       cachePostalCode = ""
			   endif
               cacheStreetName = JSONObject.get(inputAddressJo,"street1")
               if cacheStreetName == NULL
			       cacheStreetName = ""
			   endif
               cacheCrossStreetName = JSONObject.get(inputAddressJo,"street2")
               if cacheCrossStreetName == NULL
			       cacheCrossStreetName = ""
			   endif
               cacheCounty = JSONObject.get(inputAddressJo,"county")
               if cacheCounty == NULL
			       cacheCounty = ""
			   endif
               cacheDoor = JSONObject.get(inputAddressJo,"door")
               if cacheDoor == NULL
			       cacheDoor = ""
			   endif
			   
			   cacheNeighborhood = JSONObject.get(inputAddressJo,"neighborhood")
			   if cacheNeighborhood == NULL
			       cacheNeighborhood = ""
			   endif
			   
			   String cacheResource = "firstLine:" +  cacheFirstLine + "lastLine:" + cacheLastLine + "country:" + cacheCountry + "cityName:" + cacheCityName + "state:" + cacheState + "postalCode:" + cachePostalCode + "streetName:" + cacheStreetName + "crossStreetName:" + cacheCrossStreetName + "county:" + cacheCounty + "door:" + cacheDoor+ "neighborhood:" + cacheNeighborhood
               return cacheResource
 endfunc
 
func isSupportCountrySelect()
	<%if(isSupportCountrySelect) {%>
		return TRUE
	<%}%>
	return FALSE
 endfunc
	]]>
</tml:script>