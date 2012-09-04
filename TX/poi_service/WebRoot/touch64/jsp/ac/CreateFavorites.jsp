<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="../Header.jsp"%>
<%@page import="java.util.List"%>

<%
    String pageURL = getPage + "CreateFavorites";
    String pageURLCallBack = getPageCallBack + "CreateFavorites";
%>
<tml:TML outputMode="TxNode">
	<%@ include file="/touch64/jsp/ac/model/AddressCaptureModel.jsp"%>
	<%@ include file="/touch64/jsp/ac/model/CreateFavoritesModel.jsp"%>
	<%@ include file="../StopUtil.jsp"%>
	<jsp:include page="/touch64/jsp/ac/controller/SelectAddressController.jsp" />
	<tml:script language="fscript" version="1">
		<![CDATA[
			func onShow()
				if CreateFavorites_M_isDoingCreateFavorites()
					int canReturnFlag = CreateFavorites_M_getCanReturnToEditFavorites()
					if 0 == canReturnFlag
					   CreateFavorites_M_deleteCanReturnToEditFavorites()
					else
						System.doAction("returnToEditFavorites")						   
					endif
				else
					CreateFavorites_M_setDoingCreateFavorites(1)	
				endif
				return FAIL
			endfunc
		     
		     func onClickEditFavorites()
		         TxNode nextAction
				 nextAction = ParameterSet.getParam("nextAction")
				 if NULL == nextAction
				    AddressCapture_M_deleteFavoritesStatus()
					Cache.deleteFromTempCache("<%=Constant.StorageKey.FAVORITES_NAME_LABEL%>")
					System.back()
				 else
				    String nextActionStr = TxNode.msgAt(nextAction,0)
				    if "Create Success" == nextActionStr
					   Cache.deleteFromTempCache("<%=Constant.StorageKey.FAVORITES_NAME_LABEL%>")
				       getBackToInitialEnterPage()
				    elsif "" == nextActionStr
					   Cache.deleteFromTempCache("<%=Constant.StorageKey.FAVORITES_NAME_LABEL%>")
				       System.back()
				    elsif "Select Address" == nextActionStr
				       AddressCapture_M_saveFavoritesStatus(1)
				       JSONObject jo
		        	   JSONObject.put(jo,"title","<%=msg.get("common.chooseLocation")%>")
		        	   JSONObject.put(jo,"mask","01101111111")
		        	   JSONObject.put(jo,"from","CreateFavorite")
		        	   JSONObject.put(jo,"returnAsIs","1")
		        	   JSONObject.put(jo,"getGPS",1)
		        	   JSONObject.put(jo,"callbackfunction","CallBack_SelectAddress")
					   JSONObject.put(jo,"callbackpageurl","<%=pageURLCallBack%>" + "#" + AddressCapture_M_getFrom())
					   SelectAddress_C_SelectAddress(jo)
				    else
					   Cache.deleteFromTempCache("<%=Constant.StorageKey.FAVORITES_NAME_LABEL%>")
				       System.back()
				    endif
				 endif
				 return FAIL
		     endfunc
		     
		     func getBackToInitialEnterPage()
			      CreateFavorites_M_setDoingCreateFavorites(0)
			      String url = CreateFavorites_M_getCreateFavoriteReturnUrl()
			      if url != ""
			      		MenuItem.setAttribute("goBack","url",url)
			      		System.doAction("goBack")
			      else
			      		System.back()
			      endif
			      return FAIL
		     endfunc
		     
		     func setCurrentLocation(JSONObject jo)
		          String joStr=JSONObject.toString(jo)
		          CreateFavorites_M_saveCanReturnToEditFavorites(0)
		          TxNode node
		          TxNode.addMsg(node,joStr)
		          TxRequest req
				  String url="<%=host + "/getCurrentLocation.do"%>"
				  String scriptName="getCurrentLocationCallback"
				  TxRequest.open(req,url)
				  TxRequest.setRequestData(req,node)
				  TxRequest.onStateChange(req,scriptName)
				  TxRequest.setProgressTitle(req,"<%=msg.get("rgc.loading")%>","Cancel_CurrentLocation_Ajax")
				  TxRequest.send(req)
	         endfunc
	         
	         func Cancel_CurrentLocation_Ajax()
	         	System.back()
			    return FAIL
	         endfunc
			 
			 func restoreFavoriteLabelNameForCurrentLocation(TxNode node)
				if TRUE = needRestoreFavoriteLabelName()
					TxNode stopNode
					stopNode = TxNode.childAt(node,0)

					JSONObject joStop
					joStop = convertStopToJSON(stopNode)
					restoreFavoriteLabelNameForStop(joStop)

					TxNode newStopNode
					newStopNode = CreateFav_convertJSONToStop(joStop)

					return TxNode.childAt(newStopNode, 0)
				else
					return TxNode.childAt(node, 0)
			 endfunc

			 func needRestoreFavoriteLabelName()
				TxNode favNameNode
				favNameNode = Cache.getFromTempCache("<%=Constant.StorageKey.FAVORITES_NAME_LABEL%>")
				if NULL != favNameNode
					return TRUE
				else
					return FALSE
			 endfunc

			 func getCachedFavoriteLabelName()
				TxNode favNameNode
				favNameNode = Cache.getFromTempCache("<%=Constant.StorageKey.FAVORITES_NAME_LABEL%>")

				if NULL != favNameNode
					String favName = TxNode.msgAt(favNameNode, 0)
					return favName
				else
					return ""
			 endfunc
	        
	         func getCurrentLocationCallback(TxNode node,int status)
	            if status == 0
	              System.showErrorMsg("<%=msg.get("common.internal.error")%>")
				elseif status == 1
				  String createOrEdit = CreateFavorites_M_getAddressForCreateOrEdit()

				  TxNode stopNode
				  stopNode = restoreFavoriteLabelNameForCurrentLocation(node)
				  
				  TxNode addressNode
				  TxNode.addMsg(addressNode,"stop")
				  TxNode.addMsg(addressNode,createOrEdit)
				  TxNode.addChild(addressNode, stopNode)
				  MenuItem.setBean("editFavorites", "pSrc", addressNode)
                  System.doAction("editFavorites")  
				endif
				return FAIL
	         endfunc
	        
	         func CallBack_SelectAddress()
	            TxNode addressNode
			    addressNode=ParameterSet.getParam("returnAddress")
			    JSONObject jo = JSONObject.fromString(TxNode.msgAt(addressNode,0))
		        TxNode node
		        if isCurrentStop(jo)
                    setCurrentLocation(jo)
                else
                	if TxNode.getStringSize(addressNode) > 1
                	   JSONObject joPoi = JSONObject.fromString(TxNode.msgAt(addressNode,1))
					   restoreFavoriteLabelNameForPoi(joPoi)
                       node = CreateFav_convertJSONToPoi(joPoi)
                    else
                       if isJsonPoi(jo)
						jo = JSONObject.get(jo,"stop")
					   endif
					   restoreFavoriteLabelNameForStop(jo)
                       node = CreateFav_convertJSONToStop(jo)
                    endif
                    String createOrEdit = CreateFavorites_M_getAddressForCreateOrEdit()
					println("node:" + node)
				    TxNode.addMsg(node,createOrEdit)
                    MenuItem.setBean("editFavorites", "pSrc", node)
                    System.doAction("editFavorites")
		        endif
	        endfunc

			# restore the label name from poi
			func restoreFavoriteLabelNameForPoi(JSONObject jo)
				if TRUE == needRestoreFavoriteLabelName()
					String favName = getCachedFavoriteLabelName()
					println("favName=" + favName)

					# for poi, the labelName key is name
					JSONObject.put(jo, "name", favName)
				endif
			endfunc

			# restore the label name from Stop
			func restoreFavoriteLabelNameForStop(JSONObject jo)
				if TRUE == needRestoreFavoriteLabelName()
					String favName = getCachedFavoriteLabelName()

					# for stop, the labelName key is label
					JSONObject.put(jo, "label", favName)
				endif
			endfunc

	        func goToEditFavorite()
	            TxNode addressNode
			    addressNode=ParameterSet.getParam("returnAddress")
			    MenuItem.setBean("editFavorites", "pSrc", addressNode)
			    
			    System.doAction("editFavorites")
	        endfunc
	        	        
	        func CreateFav_convertJSONToStop(JSONObject jo)
	        	TxNode node
	            TxNode.addMsg(node,"stop")
	            TxNode stopNode = convertToStop(jo)
	            TxNode.addChild(node,stopNode)
	            return node
	        endfunc
	        
	        func CreateFav_convertJSONToPoi(JSONObject jo)
	        	return convertToPoi(jo)
	        endfunc
		]]>
	</tml:script>
	<tml:page id="CreateFavorites" url="<%=pageURL%>" type="<%=pageType%>" groupId="<%=GROUP_ID_AC%>">
	    <tml:actionItem name="editFavoritesAction" action="EditFavorites">
	        <tml:input name="pSrc" />
			<tml:output name="nextAction" />
		</tml:actionItem>
		<tml:menuItem name="editFavorites"
			actionRef="editFavoritesAction" onClick="onClickEditFavorites" />
			
		<tml:actionItem name="returnToEditFavoritesAction" action="RestoreEditFavorites">
			<tml:output name="nextAction" />
		</tml:actionItem>
		<tml:menuItem name="returnToEditFavorites"
			actionRef="returnToEditFavoritesAction"
			onClick="onClickEditFavorites" />
		
		<tml:menuItem name="goBack" pageURL="" />
	</tml:page>
</tml:TML>
