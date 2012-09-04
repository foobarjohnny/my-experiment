<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../Header.jsp"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.util.CommonUtil"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%
    String pageURL = getPage + "SelectCountry";
    String[] country = CommonUtil.getI18NDisplayCountryArray( region , locale );
    String[] countryCodes = CommonUtil.getI18NISO3CountryArray( region );


    String checkmarkImage = imageUrl + "checkmark.png";
    String twoLinesImageFocus = imageUrl + "2line_list_highlight.png";
    String twoLinesImageBlur = imageUrl + "2line_list.png";
%>

<tml:TML outputMode="TxNode">
	<%@ include file="model/AddressCaptureModel.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
				func selectCountry()
					TxNode code = ParameterSet.getParam("cCode")
					String cCode = TxNode.msgAt(code,0)
					setCheckmarkImage(cCode)
					AddressCapture_M_saveCountry(cCode)
					AddressCapture_M_showAddressCapturePage()
					return FAIL
				endfunc
				
				func preLoad()
				    String countryStr = AddressCapture_M_getCountry()
					setCheckmarkImage(countryStr)
				endfunc
				
				func setCheckmarkImage(String countryStr)
				    if "CAN" == countryStr
				       Page.setControlProperty("i1","focused","true")
					   Page.setComponentAttribute("checkmarkImage0","visible","0")
					   Page.setComponentAttribute("checkmarkImage1","visible","1")
					   Page.setComponentAttribute("checkmarkImage2","visible","0")
					elsif "MEX" == countryStr
					   Page.setControlProperty("i2","focused","true")
					   Page.setComponentAttribute("checkmarkImage0","visible","0")
					   Page.setComponentAttribute("checkmarkImage1","visible","0")
					   Page.setComponentAttribute("checkmarkImage2","visible","1")
					else   
					   Page.setControlProperty("i0","focused","true")
					   Page.setComponentAttribute("checkmarkImage0","visible","1")
					   Page.setComponentAttribute("checkmarkImage1","visible","0")
					   Page.setComponentAttribute("checkmarkImage2","visible","0")
					endif
				endfunc
			]]>
	</tml:script>


	<tml:page id="selectCountryPage" url="<%=pageURL%>" groupId="<%=GROUP_ID_AC%>" 
		type="<%=pageType%>" showLeftArrow="true" showRightArrow="true"
		helpMsg="">
		<tml:title id="title" align="center|middle" fontColor="white"
			fontWeight="bold|system_large">
			<%=msg.get("ac.select.country")%>
		</tml:title>

       <tml:listBox id="contryList" isFocusable="true" hotKeyEnable="false">
			<%
			    for (int i = 0; i < country.length; i++) {
			                    String display = country[i] + " ("
			                            + countryCodes[i] + ")";
			                    String menuItem = "mi" + i;
			%>
			<tml:menuItem name="<%=menuItem%>" onClick="selectCountry">
				<tml:bean name="cCode" valueType="String"
					value="<%=countryCodes[i]%>" />
			</tml:menuItem>

			<tml:compositeListItem id="<%="i" + i%>" getFocus="false" height="45"
				width="480" visible="true" bgColor="#FFFFFF" transparent="false"
				focusBgImage="<%=twoLinesImageFocus%>"
				blurBgImage="<%=twoLinesImageBlur%>" isFocusable="true">
				<tml:label id="<%="countryMessage" + i%>" textWrap="ellipsis"
					fontWeight="bold|system_large" focusFontColor="white" align="left|middle">
					<%=display%>
				</tml:label>
				<tml:image id="<%="checkmarkImage" + i%>" url="<%=checkmarkImage%>" />
				<tml:menuRef name="<%=menuItem%>" />
			</tml:compositeListItem>
			<%
			    }
			%>
		</tml:listBox>

	</tml:page>
	<cserver:outputLayout />
</tml:TML>
