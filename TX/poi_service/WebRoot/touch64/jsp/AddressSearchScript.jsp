<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.telenav.tnbrowser.util.*"%>
<%@page import="com.telenav.j2me.datatypes.*"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>

<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="Header.jsp"%>
<%
	String pageUrl = getAcPage+ "TypeAddress";
	String callBackPageUrl = getAcPageCallBack + "TypeAddress";
	String strCarrier = handlerGloble.getClientInfo(handlerGloble.KEY_CARRIER);
%>

	<%@ include file="ac/model/AddressCaptureModel.jsp"%>
	<%@ include file="dsr/controller/DSRController.jsp"%>
	<%@ include file="ac/AddressCommon.jsp"%>
	<%@ include file="StopUtil.jsp"%>
	<%@ include file="model/PrefModel.jsp"%>
	<%@ include file="ac/AceTemplateCommon.jsp"%>
	
	<tml:script language="fscript" version="1">
		<![CDATA[
		    <%@ include file="Stop.jsp"%>

			func checkDefaultAddress()
				JSONObject jo = AddressCapture_M_getDefaultAddress()
				#println( "??????????????????????" )
				if jo != NULL
					string zip = JSONObject.getString(jo,"zip")
					string city = JSONObject.getString(jo,"city")
					string state = JSONObject.getString(jo,"state")
					string firstLine = JSONObject.getString(jo,"firstLine")
					#println( "????????????????????????????????????" )
					if city == NULL
						city = ""
					endif
					
					if state == NULL
						state = ""
					endif

					if zip == NULL
						zip = ""
					endif

					if firstLine == NULL
						firstLine = ""
					endif
					
					String lastLine = ""
					if city!="" && state!=""
						lastLine = city + "," + state + " " + zip
					endif
					#println( "???????????????????????????????????????????????????????" )
					if isVisiblePageComponentAttribute( "firstLine" )
					    Page.setComponentAttribute("firstLine","text",firstLine)
					endif 
					if isVisiblePageComponentAttribute( "lastLine" )
					    Page.setComponentAttribute("lastLine","text",lastLine)
					else 
					    if isVisiblePageComponentAttribute( "cityName" )
					         Page.setComponentAttribute("cityName","text",city )
					    endif
					    if isVisiblePageComponentAttribute( "streetName" )
					         Page.setComponentAttribute("streetName","text",state )
					    endif
					    if isVisiblePageComponentAttribute( "postalCode" )
					         Page.setComponentAttribute("postalCode","text",zip )
					    endif
					endif 
					Page.setControlProperty(getSubmitButtonId(),"focused","true")
				endif
			endfunc
	        
	        
	        func speakInClick()
	        	onClickSpeakIn()
	            return FAIL
	        endfunc
			
	        
			func onClickCountry()
				AddressCapture_M_country("<%=callBackPageUrl%>" + "#" + AddressCapture_M_getFrom())
			endfunc
			
			func getSubmitButtonId()
				string id = "submitButton1"
				if DSR_M_isDSRSupportedForDisable() == 1
					id = "submitButton"
				endif
				return id
			endfunc
			
			func fillBoxForStreet_Template()
					fillBoxForStreet()
			endfunc
			]]>
	</tml:script>
	
    <tml:script language="fscript" version="1" feature="<%=FeatureConstant.DSR%>">
		<![CDATA[
			func preLoad()
				Page.setComponentAttribute("countryButton","visible","0")
				checkDSRAvail_TypeAddress()
			endfunc
			
			func onClickSpeakIn()
				JSONObject jo
	        	JSONObject.put(jo,"callbackfunction",AddressCapture_M_getCallbackFunc())
				JSONObject.put(jo,"callbackpageurl",AddressCapture_M_getInvoker())
				JSONObject.put(jo,"from",AddressCapture_M_getFrom())
				invokeSpeakAddress(jo)
				return FAIL
			endfunc	
			
			func checkDSRAvail_TypeAddress()
				if DSR_M_isDSRSupportedForDisable() == 1
					<%if(TnUtil.isTMOAndroidUser(handlerGloble)){%>
						Page.setComponentAttribute("speakInButton","visible","1")
						Page.setComponentAttribute("speakInButton","imageUnclick","$availableOffButton")
						Page.setComponentAttribute("speakInButton","imageClick","$availableOnButton")
					<%} else {%>
						Page.setComponentAttribute("speakInButton","visible","1")
					<%}%>
				elsif DSR_M_isDSRSupportedForDisable() == 2
					Page.setComponentAttribute("speakInButton","imageUnclick","$disableOffButton")
					Page.setComponentAttribute("speakInButton","imageClick","$disableOnButton")
				else
					Page.setComponentAttribute("speakInButton","visible","0")
				endif
			endfunc
			]]>
	</tml:script>