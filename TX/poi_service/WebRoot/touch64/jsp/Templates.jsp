<tml:inputBox id="firstLine" fontWeight="system_large" 
			isAlwaysShowPrompt="true" prompt="<%=msg.get("home.firstLine")%>"
			type="dropdownfilterfield" visible="false">
			<tml:menuRef name="autoFillForStreet" />
			<tml:menuRef name="submitMenu" />
		</tml:inputBox>
		
		<tml:inputBox id="door" fontWeight="system_large" 
			isAlwaysShowPrompt="true" prompt="<%=msg.get("home.door")%>"
			type="dropdownfilterfield" visible="false">
			<tml:menuRef name="submitMenu" />
		</tml:inputBox>
		
		<tml:inputBox id="neighborhood" prompt="<%=msg.get("home.neighborhood")%>"
			isAlwaysShowPrompt="true" fontWeight="system_large"
			type="dropdownfilterfield" visible="false">
			<tml:menuRef name="submitMenu" />
		</tml:inputBox>
		
		<tml:inputBox id="streetName" fontWeight="system_large" 
			isAlwaysShowPrompt="true" prompt="<%=msg.get("home.streetName")%>"
			type="dropdownfilterfield" visible="false">
			<tml:menuRef name="submitMenu" />
		</tml:inputBox>
		
		<tml:inputBox id="crossStreetName" fontWeight="system_large" 
			isAlwaysShowPrompt="true" prompt="<%=msg.get("home.crossStreetName")%>"
			type="dropdownfilterfield" visible="false">
			<tml:menuRef name="submitMenu" />
		</tml:inputBox>

		<tml:inputBox id="cityName" fontWeight="system_large" 
			isAlwaysShowPrompt="true" prompt="<%=msg.get("home.cityName")%>"
			type="dropdownfilterfield" visible="false">
			<tml:menuRef name="autoFillForCity" />
			<tml:menuRef name="submitMenu" />
		</tml:inputBox>
		
		<tml:inputBox id="county" fontWeight="system_large" 
			isAlwaysShowPrompt="true" prompt="<%=msg.get("home.county")%>"
			type="dropdownfilterfield" visible="false">
			<tml:menuRef name="submitMenu" />
		</tml:inputBox>
		
		<tml:inputBox id="cityCountyOrPostalCode" fontWeight="system_large" 
			isAlwaysShowPrompt="true" prompt=""
			type="dropdownfilterfield" visible="false">
			<tml:menuRef name="autoFillForCityCountyOrPostalCode" />
			<tml:menuRef name="submitMenu" />
		</tml:inputBox>
		
		<tml:inputBox id="state" fontWeight="system_large" 
			isAlwaysShowPrompt="true" prompt="<%=msg.get("home.state")%>"
			type="dropdownfilterfield" visible="false">
			<tml:menuRef name="autoFillForState" />			
			<tml:menuRef name="submitMenu" />
		</tml:inputBox>

		<tml:inputBox id="postalCode" fontWeight="system_large" 
			isAlwaysShowPrompt="true" prompt="<%=msg.get("home.postalCode")%>"
			type="dropdownfilterfield" visible="false">
			<tml:menuRef name="submitMenu" />
		</tml:inputBox>
		
				
		<tml:inputBox id="lastLine" prompt="<%=msg.get("ac.tips.lastLine.other")%>"
			isAlwaysShowPrompt="true" fontWeight="system_large"
			type="dropdownfilterfield" visible="false">
			<tml:menuRef name="submitMenu" />
		</tml:inputBox>
		
	    <tml:dropDownBox  id="countryLine" isFocusable="true" fontWeight="system_large" visible="false">
			<tml:menuRef name="refresh" />
		</tml:dropDownBox>	
		



<tml:label id="lbSeperator"></tml:label>
<tml:panel id="buttonLabel">
	<tml:button id="submitButton" text="<%=msg.get("onebox.searchButton")%>"
		fontWeight="system_medium"
		imageClick=""	imageUnclick="">
		<tml:menuRef name="validateAddress" />
		<tml:menuRef name="submitMenu" />
	</tml:button>
</tml:panel>
<tml:label id="lbSeperatorBottom"></tml:label>