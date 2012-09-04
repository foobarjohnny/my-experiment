<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.telenav.tnbrowser.util.*"%>
<%@page import="com.telenav.j2me.datatypes.*"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>	
<%@ include file="../Header.jsp"%>

<tml:TML outputMode="TxNode">
	<%@ include file="AddressCommon.jsp"%>
	<%@ include file="model/SetUpHomeModel.jsp"%>
	
	<tml:script language="fscript" version="1">
		<![CDATA[
			func onLoad()
				Page.setComponentAttribute("countryButton","id",1122334455)
				AddressCapture_M_deleteInputAddress()
				AddressCapture_M_deleteCacheAddress()
				Page.setControlProperty("firstLine","focused","true")
				initAddress()
				initHome()
			endfunc
			
			func preLoad()
			    Page.setComponentAttribute("countryButton","visible","0")
			endfunc
			
			func initHome()
				JSONObject jo = SetUpHome_M_getHome()
				if jo != NULL && SetUpHome_M_isFromHome()
					string firstLine = JSONObject.getString(jo,"firstLine")
					string city = JSONObject.getString(jo,"city")
					string state = JSONObject.getString(jo,"state")
					string zipcode = JSONObject.getString(jo,"zip")
					String lastLine=""

					#check city and state
					if NULL!=city && ""!=city
					    lastLine=city
					    if NULL!=state && ""!=state
					        lastLine=city+","+state
					    endif
					else
					   lastLine=state
					endif
					
					#check zip
					if NULL!=zipcode && ""!=zipcode
					   lastLine=lastLine+" "+zipcode
					endif
					
					Page.setComponentAttribute("firstLine","text",firstLine)
					Page.setComponentAttribute("lastLine","text",lastLine)					
				endif
			endfunc
			
			func onClickSubmit()
				validateAddressOnClick()
				return FAIL
			endfunc

			func onClickCountry()
				SetUpHome_M_fromHome(0)
				AddressCapture_M_country("<%=getPageCallBack + "HomeAddress#Home"%>")
				return FAIL
			endfunc						
			]]>
	</tml:script>

	<tml:menuItem name="validateAddress" onClick="onClickSubmit">
	</tml:menuItem>

	<tml:menuItem name="showAddress" pageURL="">
		<tml:bean name="callFunction" valueType="String" value="loadAddress">
		</tml:bean>
	</tml:menuItem>

	<tml:menuItem name="selectCountry" onClick="onClickCountry">
	</tml:menuItem>

    <tml:menuItem name="autoFillForStreet" onClick="fillBoxForStreet"  trigger="TRACKBALL_CLICK|KEY_MENU"/>
	
	<tml:menuItem name="autoFillForCity" onClick="fillBoxForCity"  trigger="TRACKBALL_CLICK|KEY_MENU"/>
	<tml:menuItem name="submitMenu" onClick="validateAddressOnClick" text="<%=msg.get("common.button.Submit")%>" trigger="KEY_MENU"/>
	
	<tml:page id="typeAddressPage" url="<%=getPage + "HomeAddress"%>" groupId="<%=GROUP_ID_AC%>"
		type="<%=pageType%>" helpMsg="$//$drivehome">
		<tml:title id="title" align="center|middle" fontWeight="bold|system_large"  fontColor="white">
			<%=msg.get("home.title")%>
		</tml:title>
		<tml:menuRef name="submitMenu" />
		<tml:button id="countryButton" text="<%=msg.get("home.button.USA")%>" textVisible="true"
			isFocusable="true" fontWeight="system_median"
			imageClick="<%=imageUrl + "titlebutton2_2.png"%>" 
			imageUnclick="<%=imageUrl + "titlebutton2_1.png"%>">
			<tml:menuRef name="selectCountry" />
			<tml:menuRef name="submitMenu" />
		</tml:button>
		<tml:menuItem name="autoFill" onClick="fillBox" />
		
		<tml:inputBox id="firstLine" prompt="<%=msg.get("home.firstLine")%>" style="address" 
			isAlwaysShowPrompt="true" fontWeight="system_large" type="dropdownfilterfield">
			<tml:menuRef name="autoFillForStreet"></tml:menuRef>
			<tml:menuRef name="submitMenu" />
		</tml:inputBox>
		<tml:inputBox id="lastLine" prompt="<%=msg.get("home.lastLine")%>"
			isAlwaysShowPrompt="true" fontWeight="system_large" type="dropdownfilterfield">
		    <tml:menuRef name="autoFillForCity"></tml:menuRef>
		    <tml:menuRef name="submitMenu" />
		</tml:inputBox>
		
		<tml:button id="submitButton" text="<%=msg.get("common.button.Submit")%>"
			fontWeight="system_large"
			imageClick="<%=imageUrl + "button_small_on.png"%>"
			imageUnclick="<%=imageUrl + "button_small_off.png"%>">
			<tml:menuRef name="validateAddress" />
			<tml:menuRef name="submitMenu" />
		</tml:button>
	</tml:page>
	<cserver:outputLayout/>			
</tml:TML>