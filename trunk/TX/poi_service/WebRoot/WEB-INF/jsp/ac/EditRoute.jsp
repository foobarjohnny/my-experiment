<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@page
	import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.j2me.datatypes.TxNode"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%@ include file="../Header.jsp"%>
<%@page import="java.util.List"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String pageURL = getPage + "EditRoute";
	String backURL = getPageCallBack + "EditRoute";
	String threeLinesImageFocus = imageUrl + "list_bg_highlight_45px.png";
	String threeLinesImageBlur = imageUrl + "list_bg_45px.png";
%>

<tml:TML outputMode="TxNode">
	<%@ include file="/WEB-INF/jsp/StopUtil.jsp"%>
	<%@ include file="model/EditRouteModel.jsp"%>
	<jsp:include
		page="/WEB-INF/jsp/ac/controller/SelectAddressController.jsp" />
	<%@ include file="../local_service/GetGps.jsp"%>
	<%@ include file="../GetClientInfo.jsp"%>

	<tml:script language="fscript" version="1">
		<![CDATA[
    	    <%@ include file="../GetServerDriven.jsp"%>
			func onLoad()
				<% if(!TnUtil.isUsccUser(product)) { %>
					Page.setComponentAttribute("upgradeLabel","visible","0")
				<% } %>
				# Check login flag
				TxNode node = UserInfo.getAccountInfo()	  
				if node == NULL
					return FAIL
				endif
			
				if TxNode.getValueSize(node) == 0
					return FAIL
				endif      

	        	int status = TxNode.valueAt(node, 0)
	        	if status != 10
					return FAIL
	        	endif	


				if TxNode.getStringSize(node) == 0
					return FAIL
				endif      

	        	String type = TxNode.msgAt(node, 0)
				if "NAV" != type
					return FAIL
				endif
				
				Page.setComponentAttribute("upgradeLink","visible","0")
				Page.setComponentAttribute("upgradeLabel","visible","0")
			endfunc
			
           func chooseLocation()
				TxNode node = ParameterSet.getParam("type")
				String type= TxNode.msgAt(node,0)
	            JSONObject jo
	        	JSONObject.put(jo,"title","<%=msg.get("common.chooseLocation")%>")
	        	JSONObject.put(jo,"mask","01011111011")
	        	JSONObject.put(jo,"from","EditRoute")
	        	JSONObject.put(jo,"callbackfunction","CallBack_SelectAddress_"+type)
				JSONObject.put(jo,"callbackpageurl","<%=backURL%>")
				SelectAddress_C_SelectAddress(jo)
		   endfunc
		   
		   func CallBack_SelectAddress_original()
		   		CallBack_SelectAddress("original")
		   endfunc
		   
		   func CallBack_SelectAddress_dest()
		   		CallBack_SelectAddress("dest")
		   endfunc    
		       
		   func CallBack_SelectAddress(String id)
	           TxNode addressNode
			   addressNode=ParameterSet.getParam("returnAddress")
			   String joString = TxNode.msgAt(addressNode,0)
			   JSONObject jo = JSONObject.fromString(joString)
				
			   int type = JSONObject.getInt(jo,"type")
			   JSONObject route= getJSONRoute()
			   JSONObject.put(route,id,jo)
			   setStopDisplay(jo,id)
			endfunc

 		  func DriveToControl_driveToAction(TxNode poiNode)
			MenuItem.setBean("driveToMenu", "inputName", poiNode)
			System.doAction("driveToMenu")
      	  endfunc
        
          func DriveToControl_driveToActionViaJSON(JSONObject jo)
        	JSONObject originStop =JSONObject.get(jo,"original")
      		TxNode originNode 
      		originNode= convertToStop(originStop)
      		
      		TxNode stopNode = convertToStop(JSONObject.get(jo,"dest"))
      		
      		TxNode node
      		TxNode.addChild(node,stopNode)
      		TxNode.addChild(node,originNode)

			JSONObject style=JSONObject.get(jo,"style")
			String index = JSONObject.get(style,"index")+""
      		TxNode routeStyle
      		TxNode.addMsg(routeStyle,index)
      		TxNode.addChild(node,routeStyle)
			
      		JSONObject avoid = JSONObject.get(jo,"avoid")
      		int avoidValues = JSONObject.get(avoid,"index")
      		TxNode avoidNode 
      		TxNode.addMsg(avoidNode,avoidValues+"")
      		TxNode.addChild(node,avoidNode)
      		
    		DriveToControl_driveToAction(node)   
        endfunc
        
    
        
		func setStopDisplay(JSONObject stop, String id)
			String firstLine = ""
			String lastLine = ""
			
			if(JSONObject.getInt(stop,"type")==6)
				firstLine = "<%=msg.get("common.currentLocation")%>"
		    else
		        firstLine = JSONObject.get(stop,"firstLine")
				if firstLine == ""
					firstLine= JSONObject.get(stop,"city")+", "+JSONObject.get(stop,"state")
				else
					if(JSONObject.has(stop,"city"))
						firstLine = firstLine + ", " + JSONObject.get(stop,"city")+", "+JSONObject.get(stop,"state")
					endif
				endif
			endif
			
			String firstLineId = "firstLine_dest"
			String lastLineId = "lastLine_dest"
			if id == "original"
				firstLineId = "firstLine_original"
			    lastLineId = "lastLine_original"
			endif
			
			Page.setComponentAttribute(firstLineId,"text",firstLine)
		    #Page.setComponentAttribute(lastLineId,"text",lastLine)
		endfunc
		
        func getRouteSelected()
        	JSONObject route=getJSONRoute()
        	if(JSONObject.has(route,"dest")==0)
        		System.showErrorMsg("<%=msg.get("maps.select.dest")%>")
        		return FAIL
        	endif
        	if JSONObject.has(route,"style")!=1
        		JSONObject.put(route,"style",defaultStyleJSON())
        	endif
        	if JSONObject.has(route,"avoid")!=1
        		JSONObject.put(route,"avoid",defaultAvoidJSON())
        	endif
        	if(JSONObject.has(route,"original")==0)
        		getCurrentLocation(<%=Constant.CurrentLocation.GPS%>,60,180,5)
        	endif
        	JSONObject origin = JSONObject.get(route,"original")
        	JSONObject dest = JSONObject.get(route,"dest")
        	
        	if(origin==NULL|| JSONObject.get(origin,"lat")==0 || JSONObject.get(dest,"lat")==0)
       			getCurrentLocation(<%=Constant.CurrentLocation.GPS%>,60,180,5)
       		else
       			DriveToControl_driveToActionViaJSON(getJSONRoute())
        	endif
        endfunc
    
        func setCurrentLocation(JSONObject stop)
     		JSONObject route= getJSONRoute()
     		if(JSONObject.has(route,"original")==0)
     		 	JSONObject.put(route,"original",stop)
     		else
     		 	JSONObject origin = JSONObject.get(route,"original")
     		 	
	     		if(JSONObject.get(origin,"lat")==0)
	     			JSONObject.put(route,"original",stop)
	     		endif
     		endif
		    JSONObject dest = JSONObject.get(route,"dest")
		 	if(JSONObject.get(dest,"lat")==0)
		 		JSONObject.put(route,"dest",stop)
		 	endif
			DriveToControl_driveToActionViaJSON(route)
	    endfunc
        
	func CallBack_GPS_Error(int param)
	endfunc 
	  
        func defaultStyleJSON()
        	JSONObject style
        	JSONObject.put(style,"style","1")
        	return style
        endfunc
        
        func defaultAvoidJSON()
        	JSONObject avoid
        	JSONObject.put(avoid,"avoid","1")
        	return avoid
        endfunc
        
        func preLoad()
        	JSONObject route=getJSONRoute()
        	if(JSONObject.has(route,"original")==0)
        		Page.setComponentAttribute("firstLine_original","text","<%=msg.get("common.currentLocation")%>")
		    	#Page.setComponentAttribute("lastLine_original","text","")
        	endif
        	
        	if(JSONObject.has(route,"dest"))
		       	JSONObject dest =JSONObject.get(route,"dest")
		       	#if coming from favorite, need to get destination stop infromation 
		       	if(JSONObject.has(dest,"stop"))
					dest = JSONObject.get(dest,"stop")
					JSONObject.put(route,"dest",dest)
				endif
		       	setStopDisplay(dest,"dest")
		    else
		    	String destLabel = "<%=msg.get("maps.select.dest")%>"
		    	Page.setComponentAttribute("firstLine_dest","text",destLabel)
		    	#Page.setComponentAttribute("lastLine_dest","text","")
		    endif
        	
        	JSONObject routeStyle = JSONObject.get(route,"style")
        	String routeStyle_pref=JSONObject.get(routeStyle,"value")
        	JSONObject avoidJSON= JSONObject.get(route,"avoid")
        	String avoidStr=JSONObject.get(avoidJSON,"value")
	        int styleIndex = JSONObject.get(routeStyle,"index")
        	String avoid_pref = ""
        	
        	
        	if(styleIndex!=7)
        		avoid_pref = "<%=msg.get("maps.route.avoid")%>: " 
	        	if(avoidStr!="")
	        		avoid_pref = avoid_pref + avoidStr
	        	else
	        		avoid_pref = avoid_pref + "<%=msg.get("maps.route.none")%>"	
	        	endif
        	endif
        	
        	String routeStyleStr = routeStyle_pref
        	if 1 == ServerDriven_CanAvoid()
        	   routeStyleStr = routeStyle_pref + "; " + avoid_pref
        	endif
        	
       		Page.setComponentAttribute("routeStyle","text",routeStyleStr)
       		#Page.setComponentAttribute("avoid","text",avoid_pref)
        endfunc
        
        func upgradeOnClick()
	        <%if(TnUtil.isUsccUser(product)){%>
			Handset.makeCall()
			return FAIL
	        <%}%>
        	TxNode empty
        	ShareData.Save("UPGRADE",empty)
        	

        endfunc
        
        func onBack()
		    String backAction = EditRoute_M_getBackAction()
            if "" != backAction
               if "<%=Constant.BACK_ACTION_MAIN_SCREEN%>" == backAction
                  EditRoute_M_deleteBackAction()
                  System.doAction("home")
                  return FAIL
               else
                  System.quit()
                  return FAIL
               endif
            endif
		endfunc
        
		]]>
	</tml:script>


	<tml:menuItem name="upgrade" pageURL="<%="{login.http}/startup.do"%>"
		onClick="upgradeOnClick"></tml:menuItem>
	<tml:menuItem name="originalAddressSelect" onClick="chooseLocation">
		<tml:bean name="type" value="original" valueType="String" />
	</tml:menuItem>

	<tml:menuItem name="destAddressSelect" onClick="chooseLocation">
		<tml:bean name="type" value="dest" valueType="String" />
	</tml:menuItem>
	<tml:menuItem name="pref" pageURL="<%=getPage + "RouteStyle"%>" />
	<tml:menuItem name="getRoute" onClick="getRouteSelected">
	</tml:menuItem>

	<tml:actionItem name="driveTo"
		action="<%=Constant.LOCALSERVICE_DRIVETO%>">
		<tml:output name="nextAction" />
		<tml:input name="inputName" />
	</tml:actionItem>
	<tml:menuItem name="driveToMenu" actionRef="driveTo"
		trigger="KEY_RIGHT | TRACKBALL_CLICK" />


	<tml:page id="EditRoute" url="<%=pageURL%>" type="<%=pageType%>"
		genericMenu="4" groupId="<%=GROUP_ID_COMMOM%>">
		<tml:title id="title" align="center|middle"
			fontWeight="bold|system_large" fontColor="white">
			<%=msg.get("maps.direction")%>
		</tml:title>

		<tml:listBox id="directions">
			<tml:compositeListItem id="original" visible="true" bgColor="#FFFFFF"
				transparent="false" focusBgImage="<%=threeLinesImageFocus%>"
				blurBgImage="<%=threeLinesImageBlur%>" isFocusable="true">
				<tml:label id="origin" focusFontColor="white"
					fontWeight="bold|system_large" textWrap="ellipsis" align="left|middle">
					<%=msg.get("ac.origin")%>
				</tml:label>
				<tml:label id="firstLine_original" textWrap="ellipsis"
					fontWeight="system_large" focusFontColor="white" align="left|middle">
				</tml:label>
				<tml:menuRef name="originalAddressSelect" />
			</tml:compositeListItem>

			<tml:compositeListItem id="dest" visible="true" bgColor="#FFFFFF"
				transparent="false" focusBgImage="<%=threeLinesImageFocus%>"
				blurBgImage="<%=threeLinesImageBlur%>" isFocusable="true">
				<tml:label id="destination" focusFontColor="white"
					fontWeight="bold|system_large" textWrap="ellipsis" align="left|middle">
					<%=msg.get("ac.dest")%>
				</tml:label>
				<tml:label id="firstLine_dest" textWrap="ellipsis"
					fontWeight="system_large" focusFontColor="white" align="left|middle">
				</tml:label>
				<tml:menuRef name="destAddressSelect" />
			</tml:compositeListItem>

			<tml:compositeListItem id="pref" visible="true" bgColor="#FFFFFF"
				transparent="false" focusBgImage="<%=threeLinesImageFocus%>"
				blurBgImage="<%=threeLinesImageBlur%>" isFocusable="true">
				<tml:label id="routeSettings" focusFontColor="white"
					fontWeight="bold|system_large" textWrap="ellipsis" align="left|middle">
					<%=msg.get("maps.preference")%>:
				</tml:label>
				<tml:label id="routeStyle" textWrap="ellipsis" fontWeight="system_large"
					focusFontColor="white" align="left|middle">
				</tml:label>
				<tml:menuRef name="pref" />
			</tml:compositeListItem>
		</tml:listBox>

		<tml:button fontWeight="system_large"
			text="<%=msg.get("maps.get.route")%>" id="button"
			imageClick="<%=imageUrl + "button_small_on.png"%>"
			imageUnclick="<%=imageUrl + "button_small_off.png"%>">
			<tml:menuRef name="getRoute"></tml:menuRef>
		</tml:button>
		<tml:urlLabel id="upgradeLink" isFocusable="true" fontSize="18"
			align="center" fontColor="blue">
			<%=msg.get("maps.upgrade")%>
			<tml:menuRef name="upgrade"></tml:menuRef>
		</tml:urlLabel>
		<tml:multiline id="upgradeLabel" align="left">
			<![CDATA[<%=msg.get("maps.upgrade2")%>]]>
		</tml:multiline>
	</tml:page>
	<cserver:outputLayout />
</tml:TML>
