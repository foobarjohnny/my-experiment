<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@page import="java.util.List"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@ include file="../Header.jsp"%>
<% 
%>
<tml:TML outputMode="TxNode">
<%@include file="/WEB-INF/jsp/ac/model/RecordLocationModel.jsp"%>
<%@ include file="/WEB-INF/jsp/controller/DriveToController.jsp"%>
<%@ include file="/WEB-INF/jsp/ac/controller/ShareAddressController.jsp"%>
<%@ include file="/WEB-INF/jsp/ac/controller/CreateFavoritesController.jsp"%>
<%@ include file="/WEB-INF/jsp/StopUtil.jsp"%>	
	<tml:script language="fscript" version="1">
		<![CDATA[
        func preLoad()
            JSONObject jo = RecordLocation_M_getAddress()
            
        	string firstLine = JSONObject.getString(jo,"firstLine")
        	string city = JSONObject.getString(jo,"city")
        	string state = JSONObject.getString(jo,"state")
        	string zip = JSONObject.getString(jo,"zip")
        	string text = ""
        	if firstLine != ""
        		text = firstLine + "\n"
        		MenuItem.setItemValid("page",1,1)
        	else
        		MenuItem.setItemValid("page",1,0)
        	endif
        	MenuItem.commitSetItemValid("page")
        	
        	if city != ""
        		text = text + city + ","
        	endif

        	if state != ""
        		text = text  + state + "," 
        	endif

        	if zip != ""
        		text = text  + zip + "," 
        	endif
        	        	
        	if text != ""
        		text = String.at(text,0,String.getLength(text)-1)
        	endif
        	
        	Page.setComponentAttribute("text1","text",text)
        	int lat = JSONObject.getInt(jo,"lat")
        	int lon = JSONObject.getInt(jo,"lon")
        	Page.setControlProperty("map","lat",lat)
        	Page.setControlProperty("map","lon",lon)
        	
        	RecentPlace_saveAddress(jo)
        endfunc
        
        func onClickDriveTo()
        	JSONObject jo = RecordLocation_M_getAddress()
        	TxNode node
        	TxNode.addMsg(node,JSONObject.toString(jo)) 
        	DriveTo_C_doNav(node,"Record Location","")
        	return FAIL
        endfunc
		
		func onClickSaveFav()
			JSONObject locationJo = RecordLocation_M_getAddress()
			JSONObject jo
        	JSONObject.put(jo,"from","Common")
			JSONObject.put(jo,"callbackpageurl","<%=getPageCallBack+ "RecordLocation"%>")
			CreateFavorites_C_onShow(locationJo,jo,NULL)
			return FAIL		
		endfunc	
		]]>
	</tml:script>

	<tml:script language="fscript" version="1"
		feature="<%=FeatureConstant.SHARE_ADDRESS%>">
		<![CDATA[
		func onClickShareAddress()
            JSONObject jo
        	JSONObject.put(jo,"callbackfunction","CallBack_ShareAddress")
			JSONObject.put(jo,"callbackpageurl","<%=getPageCallBack+ "RecordLocation"%>")
			JSONObject.put(jo,"address",RecordLocation_M_getAddress())
						
			ShareAddress_C_show(jo)	
			return FAIL	
		endfunc
		
		func CallBack_ShareAddress()
		endfunc		
		]]>
	</tml:script>
			
	<tml:menuItem name="driveToMenu" onClick="onClickDriveTo" text="<%=msg.get("recordlocation.menu.driveTo")%>" trigger="KEY_MENU"/>
	<tml:block feature="<%=FeatureConstant.SHARE_ADDRESS%>">
	<tml:menuItem name="shareAddressMenu" onClick="onClickShareAddress" text="<%=msg.get("recordlocation.menu.shareAddress")%>" trigger="KEY_MENU"/>
	</tml:block>
	<tml:menuItem name="saveFavMenu" onClick="onClickSaveFav" text="<%=msg.get("recordlocation.menu.saveFav")%>" trigger="KEY_MENU"/>
	<tml:page id="RecordLocation" url="<%=getPage + "RecordLocation"%>" type="<%=pageType%>"  background="<%=imageUrl + "backgroud.png"%>" groupId="<%=GROUP_ID_MISC%>">
		<tml:menuRef name="driveToMenu" />
		<tml:menuRef name="shareAddressMenu" />
		<tml:menuRef name="saveFavMenu" />
		<tml:title id="title" align="center|middle" fontWeight="bold|system_large"  fontColor="white">
			<%=msg.get("recordlocation.title")%>
		</tml:title>
		<tml:label id="text1" fontWeight="system_medium" align="center"/>
		<tml:map id="map" lon="" lat=""/>
	</tml:page>
	<cserver:outputLayout/>		
</tml:TML>