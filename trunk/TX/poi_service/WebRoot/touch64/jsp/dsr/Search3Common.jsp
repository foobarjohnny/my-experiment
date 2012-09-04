<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@page import="com.telenav.cserver.dsr.struts.util.AudioConstants"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>

	<%@ include file="DSRCommon.jsp"%>
	<%@ include file="/touch64/jsp/dsr/model/Search3CommonModel.jsp"%>
	<%@ include file="/touch64/jsp/dsr/SpeakNumber.jsp"%>
	<%@ include file="SpeakShowDetail.jsp"%>
	<tml:script language="fscript" version="1" feature="<%=FeatureConstant.DSR%>">
		<![CDATA[
        func preLoad()
        	SpeakNumber_M_setSayNumberAvailable(1)
        	Search3Common_M_createIndex(0)
        	showList()
        	Page.setControlProperty("item0","focused","true")
        	
        	<%if(!TnUtil.isATTRIM623(handlerGloble)){%>
        	if getDataAudio() != NULL
        		int audioListSize = TxNode.getChildSize(getDataAudio()) 
        		if audioListSize >0
	        		TxNode node = ParameterSet.getParam("audio8")
	        		Handset.playStaticAudio(node,"CallBack_playStaticAudio")
        		endif
        	endif
        	<%}%>
        endfunc
        
        func onShow()
        	Search3Common_M_setContinueFlag(1)
        endfunc
		
        func CallBack_playStaticAudio()
        	if Search3Common_M_getContinueFlag()
	            int audioListSize = TxNode.getChildSize(getDataAudio()) 
	       		int index = Search3Common_M_getIndex()
				if index < audioListSize
	        		Handset.playSpecNode(index,index,getDataAudio(),"playNextAudio")
	        	endif
        	endif
        endfunc
        
        func playNextAudio()
        	if Search3Common_M_getContinueFlag()
	        	Search3Common_M_increaseIndex()
	        	Page.setControlProperty("item" + Search3Common_M_getIndex(),"focused","true")
				CallBack_playStaticAudio()
			endif
        endfunc
		        
		func onBack()
			#save the first AC address
			Search3Common_M_setContinueFlag(1)
			if needDefaultAdddress()
				TxNode stop = getDataStop(0)
				if stop != NULL
					JSONObject joAddress = convertStopToJSON(stop)
					DSRCommon_M_saveDefaultAddress(joAddress)
				endif
			endif
			
			string logText = "<%=AudioConstants.LOG_TYPE_DSR%>" + "|" + DSRCommon_M_getTrxnId() + "|-1| |0"
			
			if DSRCommon_M_getTrxnId() != (-1)
				System.log(logText)
			endif
			
			System.doAction("cancel")
			return FAIL
		endfunc
		
		func needDefaultAdddress()
			int need = 0
			string type = DSRCommon_M_getDSRType()
			if type == "<%=AudioConstants.DSR_RECOGNIZE_ONE_SHOT_ADDRESS%>"
				need = 1
			elsif type == "<%=AudioConstants.DSR_RECOGNIZE_CITY_STATE%>"
				need = 1
			elsif type == "<%=AudioConstants.DSR_RECOGNIZE_ONE_SHOT_INTERSECTION%>"
				need = 1	
			endif
			
			return need
		endfunc
		
		func cancelAction()
			if Search3Common_M_getContinueFlag()
				releaseResource()
        		System.back()
				return FAIL	
        	endif
        endfunc
        
        func showList()
       		TxNode node = getDataText()

			string itemId
			string itemText
			int count,i
			count = TxNode.getStringSize(node)
			while count>i
				itemId = "item" + i
				itemText = "<bold>" + TxNode.msgAt(node,i) + "</bold>"
				Page.setComponentAttribute(itemId,"text",itemText)
				Page.setComponentAttribute(itemId,"visible","1")
				i = i+1
			endwhile

			# Hide the others
			while i < <%=AudioConstants.PAGE_SIZE%>
				itemId = "item" + i
				Page.setComponentAttribute(itemId,"visible","0")
				i = i + 1
			endwhile
			#System.repaint()
        endfunc
        
		func onKeyPressedNotUsed(string s)
		  SpeakNumber_M_saveMaxSize(getDataTextSize())
		  if s =="1" || s =="2" || s =="3" || s =="4" || s =="5"
			if isNumberValid(s)
				int i = String.convertToNumber(s)
				i = i-1
				Search3Common_M_setContinueFlag(0)
				DSR_showDetail(i)
				return TRUE
			endif
		  elsif s =="dial"
		  	if SpeakNumber_M_isSayNumberAvailable()
		  		releaseResource() 
		  		Search3Common_M_setContinueFlag(0)
		  		NumberDSR_Start()
		  	endif
		  	return TRUE		      
		  endif 
		endfunc
		
		func onClickDetail()
			TxNode node = ParameterSet.getParam("indexClicked")
			int indexClicked = TxNode.valueAt(node,0)
			Search3Common_M_setContinueFlag(0)
			DSR_showDetail(indexClicked)
		endfunc
		]]>
	</tml:script>
