				<%-- when there is no data --%>
				<%
					if(locArr1.length() == 0)
					{
				%>
				<tml:compositeListItem id="ti_0" getFocus="false" visible="true" bgColor="#FFFFFF" transparent="false" focusBgImage="" blurBgImage="" isFocusable="true">
					<tml:label id="location_0" textWrap="wrap" fontWeight="system_medium|bold" focusFontColor="white" align="left|top">
					</tml:label>
					<tml:label id="desc_0" textWrap="ellipsis" fontWeight="system_medium" focusFontColor="white" align="left|middle">
						<%= msg.get("No incidents nearby") %>
					</tml:label>
					<tml:label id="line2_0" textWrap="ellipsis" fontWeight="system_medium" focusFontColor="white" align="left|middle">
					</tml:label>
				</tml:compositeListItem>
				<%
					}
				%>
				
			<%
				for(int i = 0, k = 0; i < locArr1.length(); i++)
				{
				    JSONObject locJson = locArr1.optJSONObject(i);
				    
				    String location = (String) locJson.optString("name");
				    JSONArray detailsArr = (JSONArray) locJson.optJSONArray("details");
				    if(detailsArr != null)
				    {
					    for(int j = 0; j < detailsArr.length(); j++, k++)
					    {
					        JSONObject detailJson = (JSONObject) detailsArr.optJSONObject(j);
					    	String alertType = (String) detailJson.optString("type");
					    	String displayAlertType = (String) detailJson.optString("displayType");
					    	String desc = (String) detailJson.optString("desc");
					    	String crossStreet = (String) detailJson.optString("crossStreet");
					    	int distance = detailJson.optInt("distance");
					    	int averageSpeed = detailJson.optInt("averageSpeed");
					    	String averageSpeedStr = "";
					    	if(detailJson.has("averageSpeed"))
					    	{
					    	    averageSpeedStr = averageSpeed + " mph";
					    	}
					    	
					    	int speedColor = detailJson.optInt("speedColor");
					    	String speedImgURL = "greenbox.png";
					    	switch(speedColor)
					    	{
					    	case 0: 
					    		speedImgURL = "redbox.png";
					    		break;
					    	case 1: 
					    		speedImgURL = "orangebox.png";
					    		break;
					    	case 2: 
					    		speedImgURL = "yellowbox.png";
					    		break;
					    	default: 
					    		speedImgURL = "greenbox.png";
					    		break;
					    	}
					    	
					    	
					    	
					    
					    	
					    	String uiText1 = "";
					    	String uiText2 = "";
					    	uiText1 = displayAlertType + " in " + distance + " miles";
					    	
					    	if(crossStreet != null && !crossStreet.equals(""))
					    	{
					    	    uiText1 = uiText1 + " at";
					    		uiText2 = crossStreet;     
					    	}

			%>
					
					
				<tml:compositeListItem id="<%= "ti_" + k %>" getFocus="false" visible="true" bgColor="#FFFFFF" transparent="false" focusBgImage="<%=imageUrl + "3line_list_blue.png"%>" blurBgImage="" isFocusable="true">
					<% 
						if(detailJson.has("hasFlowData")&&detailJson.getBoolean("hasFlowData"))
								{
					%>
					<tml:image id="<%= "speedBgImg_" + k %>" url="<%=imageUrl + speedImgURL %>" />
					<tml:label id="<%= "flowSpeed_" + k %>" textWrap="ellipsis" fontWeight="system_large|bold" focusFontColor="white" fontColor="white" align="center|middle">
						<%= averageSpeedStr %>
					</tml:label>
					<%
						}
					 %>
					<tml:label id="<%= "location_" + k %>" textWrap="wrap" fontWeight="system_medium|bold" focusFontColor="white" align="left|top">
						<%= location %>
					</tml:label>
					<tml:label id="<%= "desc_" + k %>" textWrap="ellipsis" fontWeight="system_medium" focusFontColor="white" align="left|middle">
						<%= uiText1 %>
					</tml:label>
					<tml:label id="<%= "line2_" + k %>" textWrap="ellipsis" fontWeight="system_medium" focusFontColor="white" align="left|middle">
						<%= uiText2 %>
					</tml:label>
					<tml:image id="lineSeparator2" url="<%=imageUrl + "separator1.png"%>"/>
				</tml:compositeListItem>
				

			<%
					    }
					    
					}
				}
			%>
