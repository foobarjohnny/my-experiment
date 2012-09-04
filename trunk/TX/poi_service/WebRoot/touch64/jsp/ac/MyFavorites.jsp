<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="../Header.jsp"%>
<%@page import="java.util.List"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>

<%
    String pageURL = getPage + "MyFavorites";
    String pageURLCallBack = getPageCallBack + "MyFavorites";
    String addressReturnURL = getPageCallBack + "CreateFavorites";
%>
<tml:TML outputMode="TxNode">
	<%@ include file="../poi/controller/RatePoiController.jsp"%>
	<%@ include file="../poi/controller/ShowDetailController.jsp"%>
	<%@ include file="controller/ShareAddressController.jsp"%>
	<%@ include file="model/AddressCaptureModel.jsp"%>
	<%@ include file="../poi/controller/SearchPoiController.jsp"%>
	<%@ include file="../misc/controller/SendUpdateController.jsp"%>
	<%@ include file="../StopUtil.jsp"%>
	<jsp:include page="/touch64/jsp/controller/DriveToController.jsp"/>
	<%@ include file="/touch64/jsp/ac/controller/CreateFavoritesController.jsp"%>
	<jsp:include page="/touch64/jsp/ac/controller/SelectAddressController.jsp" />
	<jsp:include page="/touch64/jsp/local_service/controller/MapWrapController.jsp" />
	<tml:script language="fscript" version="1">
		<![CDATA[
		     func onShow()
		         int status
				 status = AddressCapture_M_getFavoritesStatus()
				 
		         if 1 == status
		            System.doAction("returnToMyFavorites")
		         else
					showMyFavorite()	
		         endif
		     endfunc
		     
		     func onResume()
	        	System.doAction("returnToMyFavorites")
	        	return FAIL
	         endfunc 
	        
	        func showMyFavorite()
	            string type = AddressCapture_M_getFavoriteType()
	            TxNode inputNode
	            TxNode.addMsg(inputNode,type)
	            MenuItem.setBean("favoritesClick", "inputName", inputNode)
	            System.doAction("favoritesClick")	        
	        endfunc
        		     
		     func onClickFavorites()
	            TxNode nextAction
				nextAction = ParameterSet.getParam("nextAction")
				println("************************"+nextAction)
				if NULL == nextAction
				    AddressCapture_M_deleteFavoritesStatus()
					#TODO need back?
					System.back()
				else
				   String nextActionStr = TxNode.msgAt(nextAction,0)
				   if nextActionStr == NULL
				   	nextActionStr = ""
				   endif
				   if "Drive To" == nextActionStr
					  TxNode poiNode = TxNode.childAt(nextAction,0)
					  if NULL != poiNode
					      #int childSize = TxNode.getChildSize(poiNode)
					      int nodeType = TxNode.valueAt(poiNode,0)
					      JSONObject jo
					      #Stop
					      if 28 == nodeType
							 println("************************Stop Node")
					         jo = convertStopToJSON(poiNode)
					         JSONObject.put(jo,"poiOrStop","stop")
					      else
							 println("************************Poi Node")
					         jo = convertPoiToJSON(poiNode)
					         JSONObject.put(jo,"poiOrStop","poi")
					      endif
					      String joStr = JSONObject.toString(jo)
					      TxNode node
					      TxNode.addMsg(node,joStr)
					  	  DriveTo_C_saveStopType(<%=Constant.STOP_FAVORITE%>)
					      DriveTo_C_doNav(node,"","MYFAVORITES")
					      #System.doAction("driveToFav")
					  endif
				   elsif "Map It" == nextActionStr
					  TxNode poiNode = TxNode.childAt(nextAction,0)
					  if NULL != poiNode
					      #int childSize = TxNode.getChildSize(poiNode)
					      int nodeType = TxNode.valueAt(poiNode,0)
					      JSONObject jo
					      #Stop
					      if 28 == nodeType
					         jo = convertStopToJSON(poiNode)
					      else
					         TxNode locationNode = TxNode.childAt(poiNode,0)
					         TxNode stopNode = TxNode.childAt(locationNode,0)
			    			 jo = convertStopToJSON(stopNode)
					      endif
					      MapWrap_C_showSingleAddress(jo)	
					  endif  
				   elsif "Rate This"  == nextActionStr
				      AddressCapture_M_saveFavoritesStatus(1)
				      
				      TxNode poiNode = TxNode.childAt(nextAction,0)
				      
				      JSONObject poiJo = convertPoiToJSON(poiNode)
				      RatePoi_C_savePoiToDo(poiJo)
				      
				      RatePoi_C_saveBackUrl("<%=pageURLCallBack%>" + "#" + AddressCapture_M_getFrom())
				      RatePoi_C_showRatePoi()
				   elsif "View Resviews" == nextActionStr
				      AddressCapture_M_saveFavoritesStatus(1)
				      TxNode poiNode = TxNode.childAt(nextAction,0)
				      JSONObject poiJo = convertPoiToJSON(poiNode)
				      TxNode node
				      TxNode.addMsg(node,JSONObject.toString(poiJo))
				      
				      TxRequest req
					  String url="<%=host + "/reviewPoi.do"%>"
					  String scriptName="getReviewsCallBack"
					  TxRequest.open(req,url)
					  TxRequest.setRequestData(req,node)
					  TxRequest.onStateChange(req,scriptName)
					  TxRequest.setProgressTitle(req,"Geting reviews......")
					  TxRequest.send(req)
		<%if (featureMgr.isEnabled(FeatureConstant.SHARE_ADDRESS)){%>
				   elsif "Share Address" == nextActionStr
				      AddressCapture_M_saveFavoritesStatus(1)
				   
				   	  JSONObject jo
			          JSONObject.put(jo,"callbackfunction","CallBack_ShareAddress")
					  JSONObject.put(jo,"callbackpageurl","<%=pageURLCallBack%>" + "#" + AddressCapture_M_getFrom())
					  #TODO send TxNode to share address
					  TxNode poiNode = TxNode.childAt(nextAction,0)
					  #int childeSize = TxNode.getChildSize(poiNode)
					  int nodeType = TxNode.valueAt(poiNode,0)
					  JSONObject addressJO
					  JSONObject poiJO
					  if 196 == nodeType
					     poiJO = convertPoiToJSON(poiNode)
					     String name = JSONObject.getString(poiJO,"name")
						 if name == NULL || name ==""
						    JSONObject.put(poiJO,"name","Favorite")
						 endif 
						 JSONObject.put(jo,"poi",poiJO)
						 poiNode = TxNode.childAt(poiNode,0)
						 poiNode = TxNode.childAt(poiNode,0)
					  endif
					  
					  addressJO = convertStopToJSON(poiNode)
					  String label = JSONObject.getString(addressJO,"label")
					  if label == NULL || label ==""
						 JSONObject.put(addressJO,"label","Favorite")
					  endif
					  JSONObject.put(jo,"address",addressJO)
					  ShareAddress_C_show(jo)
		<%}%>
				   elsif "Create New Favorite" == nextActionStr
				      AddressCapture_M_saveFavoritesStatus(1)
				      JSONObject joCreateFav
				      JSONObject.put(joCreateFav,"from",AddressCapture_M_getFrom())
				      JSONObject.put(joCreateFav,"flag","create")
				      JSONObject.put(joCreateFav,"callbackpageurl","<%=pageURLCallBack%>" + "#" + AddressCapture_M_getFrom())
				   	  CreateFavorites_C_init(joCreateFav)
				   	  
				      JSONObject jo
		        	  JSONObject.put(jo,"title","<%=msg.get("common.chooseLocation")%>")
		        	  JSONObject.put(jo,"mask","01101111111")
		        	  JSONObject.put(jo,"from","MyFavorite")
		        	  JSONObject.put(jo,"returnAsIs","1")
		        	  JSONObject.put(jo,"getGPS",1)
		        	  JSONObject.put(jo,"callbackfunction","CallBack_SelectAddress")
					  JSONObject.put(jo,"callbackpageurl","<%=addressReturnURL%>" + "#" + AddressCapture_M_getFrom())
					  SelectAddress_C_SelectAddress(jo)
				   elsif "Edit Favorites" == nextActionStr
				      AddressCapture_M_saveFavoritesStatus(1)
				      TxNode addressNode = TxNode.childAt(nextAction,0)
				      if NULL == addressNode
				         return FAIL
				      endif
					  
					  String favName = TxNode.msgAt(addressNode, 0)
					  println("Edit Favorites favName=" + favName)

					  TxNode favNameNode
					  TxNode.addMsg(favNameNode, favName)
					  Cache.saveToTempCache("<%=Constant.StorageKey.FAVORITES_NAME_LABEL%>" , favNameNode)

				      JSONObject jo
			          JSONObject.put(jo,"from",AddressCapture_M_getFrom())
					  JSONObject.put(jo,"callbackpageurl","<%=pageURLCallBack%>" + "#" + AddressCapture_M_getFrom())
					  JSONObject.put(jo,"flag","edit")				      
					  CreateFavorites_C_onEdit(addressNode,jo)
				   elsif "Address Back" == nextActionStr
				      TxNode poiNode = TxNode.childAt(nextAction,0)
				      #int childSize = TxNode.getChildSize(poiNode)
				      int nodeType = TxNode.valueAt(poiNode,0)
					  println("************************nodeType="+nodeType)
				      JSONObject jo
				      #Stop
				      if 196 == nodeType
				         jo = convertPoiToJSON(poiNode)
				         if SelectAddress_C_isReturnAsIs()
					        JSONObject.put(jo,"poiOrStop","poi")
				         else
				         	jo = JSONObject.get(jo, "stop")
				         	JSONObject.put(jo,"poiOrStop","stop")
				         endif
				      else
				         jo = convertStopToJSON(poiNode)
				         JSONObject.put(jo,"poiOrStop","stop")
				      endif
					  JSONObject joParameter = SelectAddress_C_getMaskForFavorite()
					  String invokerPageURL = ""
					  String callbackFunction = ""
					  if joParameter != NULL
					  	invokerPageURL = JSONObject.getString(joParameter,"callbackpageurl")
				      	callbackFunction = JSONObject.getString(joParameter,"callbackfunction")
				      endif
				      DriveTo_C_saveStopType(<%=Constant.STOP_FAVORITE%>)
				      AddressCapture_M_init(invokerPageURL,callbackFunction,AddressCapture_M_getFrom())
				      AddressCapture_M_returnAddressToInvokerPage(jo)
				   elsif "Select Address" == nextActionStr
				       AddressCapture_M_saveFavoritesStatus(1)
				       AddressCapture_M_saveAddressForCreateOrEdit("edit")
				       JSONObject jo
		        	   JSONObject.put(jo,"title","<%=msg.get("common.chooseLocation")%>")
		        	   JSONObject.put(jo,"mask","01001111111")
		        	   JSONObject.put(jo,"from","MyFavorite")
		        	   JSONObject.put(jo,"returnAsIs","1")
		        	   JSONObject.put(jo,"callbackfunction","CallBack_SelectAddress")
					   JSONObject.put(jo,"callbackpageurl","<%=addressReturnURL%>" + "#" + AddressCapture_M_getFrom())
					   SelectAddress_C_SelectAddress(jo)
				   elsif "" == nextActionStr
				      AddressCapture_M_deleteFavoritesStatus()
				      JSONObject joParameter = SelectAddress_C_getMaskForFavorite()
				      if(joParameter!=NULL)
				     	 SelectAddress_C_reSaveParameterWhenBack(joParameter)
				      endif
				      System.back()
				   else
				   	  System.back()
				   endif
				endif
				return FAIL
	        endfunc
	        
	        func getReviewsCallBack(TxNode node,int status)
	            if status == 0
	                System.showErrorMsg("Cannot connect to server,\n please try again later.")
	                PoiList_M_backToPageWhenNoFound()
				    return FAIL
				elseif status == 1
			        String poiStr = TxNode.msgAt(node,0)
			        JSONObject poiJo = JSONObject.fromString(poiStr)
			        JSONArray sponsorPoiJsonArray
			        JSONArray poiJsonArray
			        JSONArray.put(poiJsonArray,poiJo)
			        ShowDetail_C_saveShowTabIndex(1)
			        ShowDetail_C_saveForDetail(poiJsonArray,0,sponsorPoiJsonArray,-1)
			        ShowDetail_C_showDetail()
				endif
	        endfunc
	        
	        func onBack()
	            String url = AddressCapture_M_getFavoritesBackURL()
	            if "" != url
	               MenuItem.setAttribute("goBack","url",url)
	               AddressCapture_M_deleteFavoritesBackURL()
	               System.doAction("goBack")
	            else
	               System.back()   
	            endif
	           	return FAIL
	        endfunc
		]]>
	</tml:script>
	
	<tml:menuItem name="EditFavorite" pageURL="" />

	<tml:page id="MyFavorites" url="<%=pageURL%>" type="<%=pageType%>" groupId="<%=GROUP_ID_AC%>">
		<tml:actionItem name="showFavorites" action="myFavorites">
			<tml:input name="inputName" />
			<tml:output name="nextAction" />
		</tml:actionItem>
		<tml:menuItem name="favoritesClick" actionRef="showFavorites"
			onClick="onClickFavorites" />
		<tml:menuItem name="driveToFav" pageURL="<%=getPage + "DriveToWrap"%>">
			<tml:bean name="callFunction" valueType="string"
				value="CallBack_SelectAddress" />
		</tml:menuItem>

		<tml:actionItem name="myFavorites" action="RestoreMyFavorites">
			<tml:output name="nextAction" />
		</tml:actionItem>
		<tml:menuItem name="returnToMyFavorites" actionRef="myFavorites"
			onClick="onClickFavorites" />

		<tml:menuItem name="goBack" pageURL="<%=host + "/startUp.do"%>" />
	</tml:page>
</tml:TML>