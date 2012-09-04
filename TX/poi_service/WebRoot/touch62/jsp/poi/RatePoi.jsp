<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="../Header.jsp"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>

<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%
    String ShowDetailURL = getPage + "ShowDetail";
	String strCarrier = handlerGloble.getClientInfo(handlerGloble.KEY_CARRIER);
%>

<tml:TML outputMode="TxNode">
	<%@ include file="model/RatePoiModel.jsp"%>
	<%@ include file="../ac/controller/AddressCaptureController.jsp"%>
	<%@ include file="/touch62/jsp/StopUtil.jsp"%>
	<%@ include file="/touch62/jsp/model/DriveToModel.jsp"%>
	<%@ include file="../PoiUtil.jsp"%>
	<%@ include file="controller/ShowDetailController.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
		      func preLoad()
		        JSONObject poiJo = RatePoi_M_getPoiToDo()
		        int lastRateIndex = JSONObject.get(poiJo,"userPreviousRating")
		        if lastRateIndex == NULL
		        	lastRateIndex = 0
		        endif
		        showStars(lastRateIndex)
		        RatePoi_M_saveRateIndex(lastRateIndex)
		      endfunc
		      
		      func onShow()
		      	System.setKeyEventListener("-1-2-3-4-5-up-down-right-left-","keypress")
		      endfunc
		      
		      func keypress(String s)
			     int rateIndex = RatePoi_M_getRateIndex()
		         int index = 0
		         if "1" == s
		            index = 1
		         elsif "2" == s
		            index = 2
		         elsif "3" == s
		            index = 3
		         elsif "4" == s
		            index = 4
		         elsif "5" == s
		            index = 5
		         endif
		         
		         if "left" == s && TRUE == Page.getControlProperty("list","isFocused")	
		         	leftKeyClick()
		         	return TRUE
		         elsif "right" == s && TRUE == Page.getControlProperty("list","isFocused")
		            rightKeyClick()
		            return TRUE
		         endif
		         
		         
		         if TRUE == Page.getControlProperty("list","isFocused")
			         if 0 != index
	                    showStars(index)
	                 elsif 0 == index
	                 	#Page.setComponentAttribute("composite","selectRatings$visible","0")		         
			         endif
			     endif
			     
			     String focusId = "submitButton"
				<%if (featureMgr.isEnabled(FeatureConstant.UGC_EDIT)){%>
			         focusId = "rateReview"
			     <%}%>
			     
			     if "up" == s && TRUE == Page.getControlProperty(focusId,"isFocused")
			     	#Page.setComponentAttribute("composite","selectRatings$visible","1")
			     elsif "up"== s && TRUE == Page.getControlProperty("list","isFocused")	
			     	#Page.setComponentAttribute("composite","selectRatings$visible","1")
	             elsif FALSE == Page.getControlProperty("list","isFocused")	
	             	#Page.setComponentAttribute("composite","selectRatings$visible","0")		         
			     endif
		         
		         #return TRUE
		      endfunc
		      
		      func leftKeyClick()
		         int rateIndex = RatePoi_M_getRateIndex()
		         #if TRUE == Page.getControlProperty("selectRatings","isFocused")
					 if rateIndex != 1 && rateIndex != 0
					   rateIndex = rateIndex - 1
					 endif
					 showStars(rateIndex)
				 #endif
		      endfunc
		      
		      func rightKeyClick()
		         int rateIndex = RatePoi_M_getRateIndex()
		         
		         #if TRUE == Page.getControlProperty("selectRatings","isFocused")
					 if rateIndex != 5
					   rateIndex = rateIndex + 1
					 endif
					 showStars(rateIndex)
				 #endif
		      endfunc
		      
		      func showStars(int index)
		         RatePoi_M_saveRateIndex(index)
		         if index!=0
		            Page.setComponentAttribute("submitButton","isFocusable","1")
		            Page.setComponentAttribute("submitButton","fontColor","#000000")
		            Page.setComponentAttribute("submitButton","focusFontColor","#FFFFFF")
		            String StarsString = "<%=msg.get("poi.Stars")%>"
		            if 1 == index
		               StarsString = "<%=msg.get("poi.Star")%>"
		            endif
				 	String text = "<%=msg.get("poi.rating.num")%>" + " " + index + " " + StarsString
				 	#Page.setComponentAttribute("selectRatings","text",text)
				 else
				    Page.setComponentAttribute("submitButton","isFocusable","0")
				    Page.setComponentAttribute("submitButton","fontColor","#666666")
				    Page.setComponentAttribute("submitButton","focusFontColor","#666666")
				 endif
				    		      
		         if 1 == index
					 showStarImage("1","Solid")
					 showStarImage("2","UnSolid")
					 showStarImage("3","UnSolid")
					 showStarImage("4","UnSolid")
				     showStarImage("5","UnSolid")
				 elsif index == 2
					 showStarImage("1","Solid")
					 showStarImage("2","Solid")
					 showStarImage("3","UnSolid")
					 showStarImage("4","UnSolid")
					 showStarImage("5","UnSolid")
				 elsif index == 3
					 showStarImage("1","Solid")
					 showStarImage("2","Solid")
					 showStarImage("3","Solid")
					 showStarImage("4","UnSolid")
					 showStarImage("5","UnSolid")
				 elsif index == 4
					 showStarImage("1","Solid")
					 showStarImage("2","Solid")
					 showStarImage("3","Solid")
					 showStarImage("4","Solid")
					 showStarImage("5","UnSolid")
				 elsif index == 5
					 showStarImage("1","Solid")
					 showStarImage("2","Solid")
					 showStarImage("3","Solid")
					 showStarImage("4","Solid")
					 showStarImage("5","Solid")
				 endif
		      endfunc
		      
		      func showStarImage(String index,String starName)
				    String imageId = "starImage"+index+"_"+starName
				    String imageId1=""
				    if "UnSolid"==starName
				        imageId1 = "starImage"+index+"_Solid"
				    else
				        imageId1 = "starImage"+index+"_UnSolid"
				    endif
				    
				    Page.setComponentAttribute("composite",imageId+"$visible","1")
				    Page.setComponentAttribute("composite",imageId1+"$visible","0")
				endfunc
				
				func doSubmit()
				    int rateIndex = RatePoi_M_getRateIndex()
				    
				    if 0 == rateIndex
				       #Page.setComponentAttribute("composite","selectRatings$visible","1")
				       Page.setControlProperty("composite","focused","true")
				       System.showErrorMsg("<%=msg.get("poi.no.rating")%>")
				       return FAIL
				    endif
				
				    TxNode rateReviewNode
			        rateReviewNode = ParameterSet.getParam("rateReview")	
			        String rateReview = ""
			        if NULL != rateReviewNode
			           rateReview = TxNode.msgAt(rateReviewNode,0)
			        endif
				    
				    JSONObject jo
				    JSONObject.put(jo,"rateIndex",rateIndex)
				    JSONObject.put(jo,"rateReview",rateReview)
				   
				    JSONObject poiJo = RatePoi_M_getPoiToDo()
				    int poiId = JSONObject.get(poiJo,"poiId")
				    JSONObject.put(jo,"poiId",poiId)
				    
				    String categoryId = JSONObject.get(poiJo, "categoryId")
				    JSONObject.put(jo,"categoryId",categoryId)
				    
				    string joStr=JSONObject.toString(jo)
				    TxNode rateNode
				    TxNode.addMsg(rateNode,joStr)
				    TxRequest req
					String url="<%=host + "/editPOI.do"%>"
					String scriptName="ratePoiCallback"
					TxRequest.open(req,url)
					TxRequest.setRequestData(req,rateNode)
					TxRequest.onStateChange(req,scriptName)
					TxRequest.setProgressTitle(req,"<%=msg.get("poi.rating")%>")
					TxRequest.send(req)
				endfunc
				
				func ratePoiCallback(TxNode node,int status)
				    if status == 0
		                System.showErrorMsg("<%=msg.get("common.internal.error")%>")
					    return FAIL
					elseif status == 1
					    int rateIndex = RatePoi_M_getRateIndex()
					    if 0 < TxNode.getStringSize(node)
					       String poiStr = TxNode.msgAt(node,0)
					       JSONObject poiJOUpdated = JSONObject.fromString(poiStr)
					       JSONArray poiJsonArray = ShowDetail_C_getAddressList()
					       if poiJsonArray != NULL
							   int index=ShowDetail_C_getIndex()
							   if index != -1
								   JSONObject newPoiJo = JSONArray.get(poiJsonArray,index)
								   JSONObject.put(newPoiJo,"ratingNumber",JSONObject.get(poiJOUpdated,"ratingNumber"))
								   JSONObject.put(newPoiJo,"rating",JSONObject.get(poiJOUpdated,"rating"))
								   JSONObject.put(newPoiJo,"userPreviousRating",rateIndex)
								   JSONArray reviewDetailJo = JSONObject.get(poiJOUpdated,"reviewDetail")
								   JSONObject.put(newPoiJo,"reviewDetail",reviewDetailJo)
								   JSONArray.putAt(poiJsonArray,index,newPoiJo)
							   endif
					       endif
					    endif
					    
					    AddressCapture_C_saveReturnToLocalService("")
					    JSONObject poiJo = RatePoi_M_getPoiToDo()
					    TxNode newPlaceNode = PoiUtil_convertToNodeForResentSearch(poiJo)
					    TxNode.addMsg(newPlaceNode,"hasRated")
					    RecentPlaces.modifyAddress(newPlaceNode)
					    
					    String StarsString = "<%=msg.get("poi.stars")%>"
			            if 1 == rateIndex
			               StarsString = "<%=msg.get("poi.star")%>"
			            endif
					    String msg = rateIndex + "  " + StarsString + " " + "<%=msg.get("poi.rate.successfully")%>"
					    System.showGeneralMsg(NULL,msg,NULL,NULL,3,"Callback_PopopTimeOut")
					endif
				endfunc
				
				func Callback_PopopTimeOut(int param)
				    String url = RatePoi_M_getBackUrl()
				    if "" != url
				       MenuItem.setAttribute("goBack","url",url)
				    endif
				    System.doAction("goBack")
				endfunc
				
				func ratePoiBytouch62()
			 		TxNode paraNode = ShareData.get("GENERIC_ITEM_SUB_PART")
					String id = TxNode.msgAt(paraNode,0)
					if id != NULL && id != ""
				 		int index = -1
				 		println("=============****id" + id)
						if String.find(id,0,"1") > -1
							index = 1
						elsif String.find(id,0,"2") > -1
							index = 2
						elsif String.find(id,0,"3") > -1
							index = 3
						elsif String.find(id,0,"4") > -1
							index = 4
						elsif String.find(id,0,"5") > -1
							index = 5
						endif	
						println("=============****index" + index)
						if index > 0
							if index < 6
								println("=============****show stars" + index)
								showStars(index)
								println("=============****show stars over" + index)
							endif
						endif
						
						return FAIL
					else
					    #doSubmit()
					    return FAIL
					endif
				endfunc 
		]]>
	</tml:script>

	<tml:menuItem name="doSubmit" onClick="doSubmit">
	</tml:menuItem>
	<tml:menuItem name="doSubmitMenu" text="Submit" trigger="KEY_MENU" onClick="doSubmit">
	</tml:menuItem>

	<tml:menuItem name="goBack">
	</tml:menuItem>

	<tml:menuItem name="leftKeyDown" onClick="leftKeyClick"
		trigger="KEY_LEFT">
	</tml:menuItem>

	<tml:menuItem name="rightKeyDown" onClick="rightKeyClick"
		trigger="KEY_RIGHT">
	</tml:menuItem>

	<tml:page id="RatePoi" url="<%=getPage + "RatePoi"%>" groupId="<%=GROUP_ID_POI%>"
		type="<%=pageType%>" showLeftArrow="true" showRightArrow="true" supportback="false"
		helpMsg="">
		<tml:menuRef name="doSubmitMenu" />
		<tml:title id="title" align="center|middle" fontColor="white"
			fontWeight="bold|system_large">
			<%=msg.get("poi.rate.this")%>
		</tml:title>

		<tml:bean name="rateIndex" value="0" valueType="int" />
		<tml:menuItem name="ratePoiBytouch62" trigger="TRACKBALL_CLICK" onClick="ratePoiBytouch62"/>
		        <%if(TnUtil.isRogersCarrier(strCarrier)) {%>
				<tml:multiline id="selectRatings" fontWeight="system_large"
					align="center" >
					<%=msg.get("poi.select.ratings")%>
				</tml:multiline>
				<%}%>
		<tml:listBox id="list">
			<tml:compositeListItem id="composite"
				isFocusable="true" getFocus="false" visible="true" bgColor="#FFFFFF"
				transparent="false">
				<%if(!TnUtil.isRogersCarrier(strCarrier)) {%>
				<tml:label id="selectRatings" fontWeight="system_large"
					align="center" focusFontColor="black" fontColor="black" >
					<%=msg.get("poi.select.ratings")%>
				</tml:label>
				<%}%>
				
				<tml:menuRef name="ratePoiBytouch62"/>
				<tml:image id="starImage1_UnSolid" />
				<tml:image id="starImage1_Solid" visible="0" />

				<tml:image id="starImage2_UnSolid" />
				<tml:image id="starImage2_Solid" visible="0" />

				<tml:image id="starImage3_UnSolid"  />
				<tml:image id="starImage3_Solid" visible="0"  />

				<tml:image id="starImage4_UnSolid" />
				<tml:image id="starImage4_Solid" visible="0" />

				<tml:image id="starImage5_UnSolid" />
				<tml:image id="starImage5_Solid" visible="0" />
			</tml:compositeListItem>
		</tml:listBox>
		<tml:button id="submitButton"
			text="<%=msg.get("common.button.Submit")%>" fontWeight="system_large">
			<tml:menuRef name="doSubmit" />
			<tml:menuRef name="doSubmitMenu" />
		</tml:button>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>