<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>	
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@ include file="../model/DSRModel.jsp"%>
<%@ include file="/touch64/jsp/ac/model/AddressCaptureModel.jsp"%>
<tml:script language="fscript" version="1" feature="<%=FeatureConstant.DSR%>">
	<![CDATA[
	        func invokeSpeakSearchAlong(TxNode searchInformationNode)
	        	if DSR_M_isDSRSupportedForDisable() ==1
					JSONObject jo
					JSONObject.put(jo,"poiSearchType",7)
					JSONObject.put(jo,"callbackpageurl","")
			        JSONObject.put(jo,"callbackfunction","")
					DSR_M_saveParameter(jo)
					DSR_M_saveSearchAlongData(searchInformationNode)
	             	System.doAction("DSR_ACTION_speakSearchAlong")
	            elsif DSR_M_isDSRSupportedForDisable() ==2
	            	System.doAction("Purchase")
             	endif
	            return FAIL	
	        endfunc
	        
	        func needSearchInst()
	        	return DSR_M_getSearchInstFlag()
	        endfunc
	        
	        func invokeSpeakSearch(JSONObject jo)
	        	if DSR_M_isDSRSupportedForDisable() ==1
					DSR_M_saveParameter(jo)
					if needSearchInst()
						System.doAction("DSR_ACTION_speakSearchInst")
					else
	             		System.doAction("DSR_ACTION_speakSearch")
	             	endif
	            elsif DSR_M_isDSRSupportedForDisable() ==2
	            	System.doAction("Purchase")
             	endif
		        return FAIL
	        endfunc

	        func invokeSpeakCity(JSONObject jo)
	        	if DSR_M_isDSRSupportedForDisable() ==1
					DSR_M_saveParameter(jo)
             		System.doAction("DSR_ACTION_speakCity")
             	endif
             	return FAIL
	        endfunc
	        
	        func invokeSpeakAirport(JSONObject jo)
	        	if DSR_M_isDSRSupportedForDisable() ==1
					DSR_M_saveParameter(jo)
             		System.doAction("DSR_ACTION_speakAirport")
	            elsif DSR_M_isDSRSupportedForDisable() ==2
	            	System.doAction("Purchase")
             	endif
             	return FAIL
	        endfunc
	        
	        func invokeSpeakAddress(JSONObject jo)
	        	if DSR_M_isDSRSupportedForDisable() ==1
					DSR_M_saveParameter(jo)
             		System.doAction("DSR_ACTION_speakAddress")
	            elsif DSR_M_isDSRSupportedForDisable() ==2
	            	System.doAction("Purchase")
             	endif
             	return FAIL	
	        endfunc

	        func invokeSpeakIntersection(JSONObject jo)
	        	if DSR_M_isDSRSupportedForDisable() ==1
					DSR_M_saveParameter(jo)
             		System.doAction("DSR_ACTION_speakIntersection")
             	endif
             	return FAIL	
	        endfunc	        	        

	        func invokeSpeakCommand(JSONObject jo)
	        	if DSR_M_isDSRSupportedForDisable() ==1
					DSR_M_saveParameter(jo)
             		System.doAction("DSR_ACTION_speakCommand")
	            elsif DSR_M_isDSRSupportedForDisable() ==2
	            	System.doAction("Purchase")
             	endif
             	return FAIL
	        endfunc
		]]>
</tml:script>

<tml:block feature="<%=FeatureConstant.DSR%>">
<tml:menuItem name="Purchase" pageURL="<%=purchasePageUrl%>"/>
<tml:menuItem name="DSR_ACTION_speakCity"
	pageURL="<%=getDsrPage + "SpeakCity1"%>" />
<tml:menuItem name="DSR_ACTION_speakAirport"
	pageURL="<%=getDsrPage + "SpeakAirport1"%>" />
<tml:menuItem name="DSR_ACTION_speakAddress"
	pageURL="<%=getDsrPage + "SpeakAddress1"%>" />
<tml:menuItem name="DSR_ACTION_speakIntersection"
	pageURL="<%=getDsrPage + "SpeakIntersection1"%>" />
<tml:menuItem name="DSR_ACTION_speakSearch"
	pageURL="<%=getDsrPage + "SpeakSearch1"%>" />
<tml:menuItem name="DSR_ACTION_speakSearchAlong"
	pageURL="<%=getDsrPage + "SpeakSearchAlong1"%>" />	
<tml:menuItem name="DSR_ACTION_speakCommand"
	pageURL="<%=getDsrPage + "SpeakCommand1"%>" />
<tml:menuItem name="DSR_ACTION_speakSearchInst"
	pageURL="<%=getPage + "SpeakSearchInstruction"%>" />	
<tml:actionItem name="makePhoneCallAction" action="<%=Constant.LOCALSERVICE_MAKEPHONECALL%>"> 
	<tml:input name="phonenumber" />
</tml:actionItem>
<tml:menuItem name="ivrMenu" actionRef="makePhoneCallAction" trigger="TRACKBALL_CLICK" onClick="ivrServiceReturned"/>
<tml:actionItem name="syncAddress"
action="<%=Constant.LOCALSERVICE_SYNCADDRESS%>">
</tml:actionItem>
<tml:menuItem name="syncAddressMenu" actionRef="syncAddress" onClick="syncAddressReturned"
	trigger="TRACKBALL_CLICK" />
	
</tml:block>	
