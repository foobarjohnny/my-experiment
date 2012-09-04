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
	<%@ include file="/touch/jsp/ac/model/AddressCaptureModel.jsp"%>
	<%@ include file="/touch/jsp/ac/model/CreateFavoritesModel.jsp"%>
	<%@ include file="../StopUtil.jsp"%>
	<jsp:include page="/touch/jsp/ac/controller/SelectAddressController.jsp" />
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
					#TODO need back?
					System.back()
				 else
				    String nextActionStr = TxNode.msgAt(nextAction,0)
				    if "Create Success" == nextActionStr 
				       getBackToInitialEnterPage()
				    elsif "" == nextActionStr
				       System.back()
				    elsif "Select Address" == nextActionStr
				       AddressCapture_M_saveFavoritesStatus(1)
				       JSONObject jo
		        	   JSONObject.put(jo,"title","<%=msg.get("common.chooseLocation")%>")
		        	   JSONObject.put(jo,"mask","01001111111")
		        	   JSONObject.put(jo,"from","CreateFavorite")
		        	   JSONObject.put(jo,"returnAsIs","1")
		        	   JSONObject.put(jo,"getGPS",1)
		        	   JSONObject.put(jo,"callbackfunction","CallBack_SelectAddress")
					   JSONObject.put(jo,"callbackpageurl","<%=pageURLCallBack%>" + "#" + AddressCapture_M_getFrom())
					   SelectAddress_C_SelectAddress(jo)
				    else
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
	        
	         func getCurrentLocationCallback(TxNode node,int status)
	            if status == 0
	              System.showErrorMsg("<%=msg.get("common.internal.error")%>")
				elseif status == 1
				  String createOrEdit = CreateFavorites_M_getAddressForCreateOrEdit()
				  
				  TxNode addressNode
				  TxNode.addMsg(addressNode,"stop")
				  TxNode.addMsg(addressNode,createOrEdit)
				  TxNode.addChild(addressNode,TxNode.childAt(node,0)) 
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
                       node = CreateFav_convertJSONToPoi(joPoi)
                    else
                       if isJsonPoi(jo)
						jo = JSONObject.get(jo,"stop")
					   endif	
                       node = CreateFav_convertJSONToStop(jo)
                    endif
                    String createOrEdit = CreateFavorites_M_getAddressForCreateOrEdit()
				    TxNode.addMsg(node,createOrEdit)
                    MenuItem.setBean("editFavorites", "pSrc", node)
                    System.doAction("editFavorites")
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
