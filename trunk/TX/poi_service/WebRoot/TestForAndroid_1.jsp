<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%
	String host = "http://172.16.10.87:8080/poi_service";
	String pageType = "net";
	String getPage = host + "/goToJsp.do?jsp=";
	String pageURL = host + "/TestForAndroid_1.jsp";

	String imageUrl = "http://172.16.10.87:8080/poi_service/ATT/RIM/6_0_01/ATT_NAV/9000/480x320/image/";
%>
<tml:TML outputMode="TxNode">

	<tml:page id="mainPageMapPage" url="<%=pageURL%>" type="<%=pageType%>"
		x="0" y="0" width="200" height="320" showLeftArrow="true"
		showRightArrow="true" helpMsg="">
		<tml:tabContainer id="showDetailContainer" style="horizontal" x="0"
			y="50" width="200" height="300" defaultFocus="0">
			<tml:tab id="Details" label="Details">
				<tml:panel id="testPanel" layout="vertical" x="0" y="10" width="200"
					height="300">
					<%
						for (int i = 0; i < 2; ++i) {
					%>
					<tml:multiline id="<%="couponDesc" + i%>" fontSize="18"
						isFocusable="true" align="left|top" width="200" height="10">
					    Hi, this is a multiline in the panel.
					</tml:multiline>
					<%
						}
					%>

					<tml:button id="reviewButton" text="testButton" fontWeight="system"
						x="0" y="50" width="100" height="50"
						imageClick="<%=imageUrl
											+ "button_small_on.png"%>"
						imageUnclick="<%=imageUrl
											+ "button_small_off.png"%>">
					</tml:button>
				</tml:panel>
				
				
			</tml:tab>

			<tml:tab id="Reviews" label="Reviews">
				<tml:checkBox id="currentLocation" name="currentLocation"
					fontWeight="system_huge" isFocusable="true" x="<%=10%>" y="<%=10%>"
					width="<%=480%>" height="<%=50%>">
					<tml:checkBoxItem selected="false" value="0" text="Reviews">
					</tml:checkBoxItem>
				</tml:checkBox>
			</tml:tab>

			<tml:tab id="Coupons" label="Coupons">
				<tml:checkBox id="currentLocation" name="currentLocation"
					fontWeight="system_huge" isFocusable="true" x="<%=10%>" y="<%=10%>"
					width="<%=480%>" height="<%=50%>">
					<tml:checkBoxItem selected="false" value="0" text="Coupons">
					</tml:checkBoxItem>
				</tml:checkBox>
			</tml:tab>

			<tml:tab id="hoursAndMenu" label="HoursMenu">
				<tml:checkBox id="currentLocation" name="currentLocation"
					fontWeight="system_huge" isFocusable="true" x="<%=10%>" y="<%=10%>"
					width="<%=480%>" height="<%=50%>">
					<tml:checkBoxItem selected="false" value="0" text="HoursMenu">
					</tml:checkBoxItem>
				</tml:checkBox>
			</tml:tab>
		</tml:tabContainer>

	</tml:page>
</tml:TML>
