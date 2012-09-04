<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@ include file="../Header.jsp"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<tml:TML outputMode="TxNode">
	<%@ include file="model/AddressCaptureModel.jsp"%>
	<%@ include file="/touch64/jsp/StopUtil.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
		<%@ include file="../Stop.jsp"%>
		        func preLoad()
		            int titleFlag = AddressCapture_M_getAddressListTitleForMaiTai()
		            if 0 ==  titleFlag
		               return FAIL
		            elsif 1 ==  titleFlag
		               String title = "<%=msg.get("ac.dest")%>" + "  " + "<%=msg.get("ac.address.list")%>"
		               Page.setComponentAttribute("title","text",title)
		            elsif 2 ==  titleFlag
		               String title = "<%=msg.get("ac.origin")%>" + "  " + "<%=msg.get("ac.address.list")%>"
		               Page.setComponentAttribute("title","text",title)
		            endif
		            AddressCapture_M_deleteAddressListTitleForMaiTai()
		        endfunc
		        
		        func loadAddress()
		        
		            TxNode node = ParameterSet.getParam("addressList")
		            if NULL != node
		            	String saveKey = "<%=Constant.StorageKey.MUTLI_MATCHES_ADDRESS_LIST%>"
		            	Cache.saveToTempCache(saveKey,node)
						int size = TxNode.getStringSize(node)
						if 0 < size
							String strJa = TxNode.msgAt(node,0)
							if NULL != strJa
								JSONArray ja
								ja = JSONArray.fromString(strJa)
								int addressSize = JSONArray.length(ja)
								if 10 < addressSize
									addressSize = 10
								endif
								int i = 0
								JSONObject jo
								String text
								int restype = TxNode.valueAt(node,1)
								println( ">>>>>>>>>>>>>>>>>>>>>  <<<<<<<<<"  + addressSize )
								while i < addressSize
								    println( "i = " + i )
									jo = JSONArray.get(ja,i)
									if(restype==<%=Constant.OneBox.DID_YOU_MEAN%>)
									    #println( "???????????????????????>>>>>>>>>>>><<<<<<<<<<<<<<<<<" )
										text = getSingleSuggestionFromJO(jo)
									else
									   # println( "?????????????????????" )
								    	#String s = JSONObject.toString(jo)
									    #println( ">>>>>>>>>>>  "  + s )
										text = getSingleAddressFromJO(jo)
									endif
									Page.setComponentAttribute("item"+i,"visible","1")
									println( "................................... : " + text )
									Page.setComponentAttribute("item"+i,"text",text)
									i = i + 1
								endwhile
								while i < 10
									Page.setComponentAttribute("item"+i,"visible","0")
									i = i + 1
								endwhile
							endif
						endif
					endif
		        endfunc
		        
		        func getSingleSuggestionFromJO(JSONObject jo)
		        	String display = JSONObject.getString(jo,"display")
		        	return display
		        endfunc
		        
		        func selectAddress()
		        
					TxNode indexClickNode = ParameterSet.getParam("indexClicked")
					int indexClicked = TxNode.valueAt(indexClickNode, 0)
					
		            String saveKey = "<%=Constant.StorageKey.MUTLI_MATCHES_ADDRESS_LIST%>"
		            Cache.getFromTempCache(saveKey)
					TxNode node = Cache.getFromTempCache(saveKey)
					
		            if NULL != node
						int size = TxNode.getStringSize(node)
						if 0 < size
							String strJa = TxNode.msgAt(node,0)
							if NULL != strJa
								JSONArray ja
								ja = JSONArray.fromString(strJa)
								int addressSize = JSONArray.length(ja)
								if indexClicked < addressSize
									JSONObject jo = JSONArray.get(ja,indexClicked)
									if NULL != jo
										AddressCapture_M_returnAddressToInvokerPage(jo)
									endif
								endif
							endif
						endif
					endif
					return FAIL
		        endfunc
				
				func getSingleAddressFromJO(JSONObject jo)
					String addressStr = ""
					if NULL != jo
					    String firstLine = JSONObject.getString(jo,"firstLine")
					    String city = JSONObject.getString(jo,"city")
						String state = JSONObject.getString(jo,"state")
						String zip =  JSONObject.getString(jo,"zip")
                        String neighborhood = JSONObject.getString(jo,"locality")
                        String county = JSONObject.getString( jo,"county" )
                        
						if  firstLine != NULL && firstLine != ""
						    addressStr = addressStr + firstLine
						endif

                        if  neighborhood != NULL && neighborhood != ""
						     if addressStr != NULL && addressStr != ""
						        addressStr = addressStr + "," + neighborhood 
						     else
						        addressStr = neighborhood
						     endif
						endif
						
                        if  city != NULL && city != ""
						   if addressStr != NULL && addressStr != ""
						      addressStr = addressStr + "," + city 
						   else
						      addressStr = city
						   endif
						endif

                        if  county != NULL && county != ""
						   if addressStr != NULL && addressStr != ""
						      addressStr = addressStr + "," + county 
						   else
						      addressStr = county
						   endif
						endif
						
                       if  state != NULL && state != ""
					        if addressStr != NULL && addressStr != ""
						        addressStr = addressStr + "," + state 
						    else
						       addressStr = state
							endif
						endif

                        if  zip != NULL && zip != ""
						   if addressStr != NULL && addressStr != ""
						        addressStr = addressStr + "," + zip 
						   else
						       addressStr = zip
						   endif
						endif

                       
					endif
					return addressStr
				endfunc
				
				func onBack()
				   String backAction = AddressCapture_M_getBackAction()
                   if "" != backAction
                   	  AddressCapture_M_deleteBackAction() 	
                      if "<%=Constant.BACK_ACTION_MAIN_SCREEN%>" == backAction
                         System.doAction("home")
		                 return FAIL
                      else
                         System.quit()
                         return FAIL
                      endif
                   else
                      System.back()
				   	  return FAIL
                   endif
				endfunc		
			]]>
	</tml:script>

	<tml:page id="AddressListPage" url="<%=getPage + "AddressList"%>" groupId="<%=GROUP_ID_AC%>"
		type="<%=pageType%>" showLeftArrow="true" showRightArrow="true"
		helpMsg="" supportback="false">

		<tml:title id="title" align="center|middle" fontColor="white"
			fontWeight="bold|system_large">
			<%=msg.get("ac.address.list")%>
		</tml:title>


		<tml:listBox id="addressList">

			<%
			    for (int i = 0; i < 10; i++) {
			%>
			<tml:menuItem name="<%="item_" + i + "_clicked"%>"
				onClick="selectAddress" trigger="TRACKBALL_CLICK">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:listItem id="<%="item" + i%>" align="left|middle" fontWeight="bold|system_large"
				isFocusable="true" showArrow="false" 
				heightAutoScale="true" textWrap="scroll">
				<tml:menuRef name="<%="item_" + i + "_clicked"%>"></tml:menuRef>
			</tml:listItem>
			<%
			    }
			%>

		</tml:listBox>
		<tml:image id="titleShadow" url="" align="left|top"/>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>