<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<% 
    String host = "http://172.16.10.87:8080/poi_service";
    String pageType = "net";
    String getPage = host + "/goToJsp.do?jsp=";
    String pageURL = host + "/index.jsp";
    String getDsrPage = host + "/runDSR.do?action=";
%>
<tml:TML outputMode="TxNode">

	<tml:page id="mainPageMapPage"
		url="<%=pageURL%>" type="<%=pageType%>" x="0"
		y="0" width="480" height="320" showLeftArrow="true"
		showRightArrow="true" helpMsg="">

		<tml:listBox id="pageListBox" name="pageListBox:settingsList" x="0"
			y="20" width="480" height="320" isFocusable="true"
			hotKeyEnable="false">
			
			<tml:menuItem name="mainPage"
				pageURL="<%=host+ "/startUp.do"%>">
			</tml:menuItem>
			<tml:urlImageLabel id="mainPageLabel" fontWeight="bold|system_large"
				x="0" y="20" width="480" height="30" align="left|middle"
				showArrow="true">
				<tml:menuRef name="mainPage" />
				MainPage
			</tml:urlImageLabel>
			
			
			<tml:menuItem name="searchPoi"
				pageURL="<%=getPage + "SearchPoi"%>">
			</tml:menuItem>
			<tml:urlImageLabel id="searchPoiLabel" fontWeight="bold|system_large"
				x="0" y="20" width="480" height="30" align="left|middle"
				showArrow="true">
				<tml:menuRef name="searchPoi" />
				Search Poi
			</tml:urlImageLabel>
			
			<tml:menuItem name="dsrForPoi"
				pageURL="<%=getDsrPage + "SpeakSearch1"%>">
			</tml:menuItem>
			<tml:urlImageLabel id="dsrPoiLabel" fontWeight="bold|system_large"
				x="0" y="20" width="480" height="30" align="left|middle"
				showArrow="true">
				<tml:menuRef name="dsrForPoi" />
				DSR for Poi
			</tml:urlImageLabel>
			
			<tml:menuItem name="command"
				pageURL="<%=getDsrPage + "SpeakCommand1"%>">
			</tml:menuItem>
			<tml:urlImageLabel id="commandLabel" fontWeight="bold|system_large"
				x="0" y="20" width="480" height="30" align="left|middle"
				showArrow="true">
				<tml:menuRef name="command" />
				Say a command
			</tml:urlImageLabel>
		</tml:listBox>

	</tml:page>
</tml:TML>
