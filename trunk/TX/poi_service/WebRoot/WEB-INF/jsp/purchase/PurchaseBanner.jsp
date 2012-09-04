
<%@page import="com.telenav.cserver.ExtraProperties"%>
<%@page import="com.telenav.cserver.stat.AttributeID"%>
<%@page import="com.telenav.cserver.stat.EventTypes"%>

	<jsp:include page="/WEB-INF/jsp/AccountTypeForSprint.jsp"/>
	<jsp:include page="/WEB-INF/jsp/FreeTrialSDParamScriptForSprint.jsp" />
		
	<%
	//String purchasePageUrl = "{login.http}/getInterface.do?jsp=PurchaseInterface";
	String banner_PurchasePageUrl = "{login.http}/getInterface.do?jsp=PurchaseInterface";
	String banner_PurchaseConfirmPageUrl = "{login.http}/getInterface.do?jsp=PurchaseConfirmInterface";

	// Pay per month without free trial.
	final String PRODUCT_CODE_LITE        = "tn_sn_wsip_mrc";
	final String PRODUCT_CODE_BUNDLE      = "tn_sn_wsip_mrc_pre";
	final String PRODUCT_CODE_PAY_PER_DAY = "tn_sn_wsip_ppd";
	
	final String PRODUCT_CODE_SPRINT_PREM_WITH_30_FREE_TRIAL_FROM_LITE = "tn_sn_wsip_mrc30";
	final String PRODUCT_CODE_SPRINT_PREM_WITH_30_FREE_TRIAL_FROM_BUNDLE = "tn_sn_wsip_mrc_pre30";
	
    final String USER_INFO_MANAGER_PTN    = "USER_INFO_MANAGER_PTN";
    final String USER_INFO_FREE_TRIAL = "USER_INFO_FREE_TRIAL";
	
    final String PURCHASE_JSON_INFO       = "PURCHASE_JSON_INFO";
    final String NODE_FOR_PURCHASE        = "NODE_FOR_PURCHASE";
    MessageWrap msgtemp = MessageHelper.getInstance(true).getMessageWrap(msgKey);
    
	%>
	<tml:script language="fscript" version="1">
		<![CDATA[

				func isFreeTrial()
			    	int freeTrial = UserInfoManager_getFreeTrial()
			        int freeTrialDays = FreeTrial_serverDriven_FreeTrialDays()
			        if 1 == freeTrial && 0 < freeTrialDays
			        	return 1
			        endif
		        	return 0
				endfunc 
				
				func displayPurchaseBanner()
					println("herea banner")
					
					int freeTrial = isFreeTrial()
					  TxNode codeNode
				   	
				    if AccountTypeForSprint_IsLiteUser()
						println("onload lite")
						Page.setComponentAttribute("bottomPanelForLite",  "visible","1")
						Page.setComponentAttribute("bottomPanelForBundle","visible","0")
						Page.setComponentAttribute("premiumBottom",       "visible","0")					
						if 1 == freeTrial
							TxNode.addMsg(codeNode,"<%=PRODUCT_CODE_SPRINT_PREM_WITH_30_FREE_TRIAL_FROM_LITE%>")	       
							Page.setComponentAttribute("mrcForLiteL","visible","1")
							Page.setComponentAttribute("mrcForLiteL","fontColor","green")	  
			            else
							TxNode.addMsg(codeNode,"<%=PRODUCT_CODE_LITE%>")
							Page.setComponentAttribute("mrcForLiteL","visible","0")	  
			            endif	
			             MenuItem.setBean("mrcForLite", "productCode", codeNode)	
				  	elsif AccountTypeForSprint_IsBundleUser()
						println("onload bundle")
						Page.setComponentAttribute("bottomPanelForLite",  "visible","0")
						Page.setComponentAttribute("bottomPanelForBundle","visible","1")
						Page.setComponentAttribute("premiumBottom",       "visible","0")					
						String msg = "<%=msg.get("login.purchase.period.mrc")%>"  
						if 1 == freeTrial
							TxNode.addMsg(codeNode,"<%=PRODUCT_CODE_SPRINT_PREM_WITH_30_FREE_TRIAL_FROM_BUNDLE%>")	       
							msg = "<%=msg.get("login.purchase.period.mrc30")%>"
							Page.setComponentAttribute("mrcForBundleL","fontColor","green")	  
			            else
							TxNode.addMsg(codeNode,"<%=PRODUCT_CODE_BUNDLE%>")
							Page.setComponentAttribute("mrcForBundleL","visible","0")	  
			            endif	
			             Page.setComponentAttribute("mrcForBundleL","text",msg)
		          		 MenuItem.setBean("mrcForBundle", "productCode", codeNode)	
		           
					else
						println("onload other - premium ")
						Page.setComponentAttribute("bottomPanelForLite",  "visible","0")
						Page.setComponentAttribute("bottomPanelForBundle","visible","0")
					endif
					Page.setComponentAttribute("footerImage","visible","0")
					
					JSONObject logJSON = compileLogJSON(NULL, freeTrial)
	            	Purchase_M_logAdBannerLog(<%=EventTypes.AD_BANNER_VIEW%>,logJSON)
				endfunc


		func isPremiumAccount()
			int flag = TRUE
			if AccountTypeForSprint_IsLiteUser() || AccountTypeForSprint_IsBundleUser() || AccountTypeForSprint_IsFreeTrial()
				flag = FALSE
			endif
			return flag			
		endfunc
		
		func purchaseBanner_Back()
			int freeTrial = UserInfoManager_getFreeTrial()
			JSONObject logJSON = compileLogJSON("back", freeTrial)
	        Purchase_M_logAdBannerLog(<%=EventTypes.AD_BANNER_CLICK%>,logJSON)
		endfunc
		
		func UserInfoManager_getPTN()
			TxNode node
			node = Cache.getCookie("<%=USER_INFO_MANAGER_PTN%>")
			if NULL == node
				return ""
			endif
	
			return TxNode.msgAt(node, 0)    

		endfunc

		func UserInfoManager_getFreeTrial()
			TxNode node
			node = Cache.getCookie("<%=USER_INFO_FREE_TRIAL%>")
			if NULL == node
				return 0
			endif
			return TxNode.valueAt(node, 0)  	
		endfunc


			func purchase()
				# Create account
				JSONObject billingReq
				int freeTrial = isFreeTrial()
				
				# Set PTN
				String ptn = UserInfoManager_getPTN()
				JSONObject.put(billingReq, "ptn", ptn)

				println("here2")

				# Set product code
				TxNode codeNode = ParameterSet.getParam("productCode")
				String productCode = TxNode.msgAt(codeNode, 0)
				JSONObject.put(billingReq, "productCode", productCode)
				JSONObject.put(billingReq, "<%=ExtraProperties.KEY_SALES_SOURCE%>", "Handset")
				if 1 == freeTrial
					JSONObject.put(billingReq, "<%=ExtraProperties.KEY_SALES_CAMPAIGN_ID%>", "FREETRIAL_POPUP_001")
					JSONObject.put(billingReq, "<%=ExtraProperties.KEY_PROMOTION_CODE%>", "30 Days 1st 30 Days Free")
				endif
				
				
				# Send request.				
				TxNode data
				TxNode.addMsg(data, JSONObject.toString(billingReq))
				Purchase_M_saveNodeForPurchase(data)
				String actionName = "BuyMRC-4.99"
				if "<%=PRODUCT_CODE_LITE%>" == productCode
				    actionName = "BuyMRC-9.99"
				elsif "<%=PRODUCT_CODE_PAY_PER_DAY%>" == productCode
				    actionName = "BuyRRD-2.99"
				elsif "<%=PRODUCT_CODE_SPRINT_PREM_WITH_30_FREE_TRIAL_FROM_LITE%>" == productCode
				    actionName = "BuyMRC-9.99"
				elsif "<%=PRODUCT_CODE_SPRINT_PREM_WITH_30_FREE_TRIAL_FROM_BUNDLE%>" == productCode
				    actionName = "BuyMRC-4.99"
				endif
		        JSONObject logJSON = compileLogJSON(actionName, freeTrial)
	            logAdBannerLog(<%=EventTypes.AD_BANNER_CLICK%>,logJSON)
	            System.doAction("purchaseConfirm")
				return FAIL
	        endfunc

	    func Purchase_M_saveNodeForPurchase(TxNode node)
	        Cache.saveCookie("<%=NODE_FOR_PURCHASE%>", node)
	    endfunc

			func compileLogJSON(String actionName, int freeTrial)
	           String accountType = AccountTypeForSprint_getAccoutType()
	           JSONObject logJSON
			   JSONObject.put(logJSON, "<%=AttributeID.CAMPAIGN_VERSION%>", "v2")
			   if NULL != actionName
			   	JSONObject.put(logJSON, "<%=AttributeID.ACTION_NAME%>", actionName)
			   endif
			   JSONObject.put(logJSON, "<%=AttributeID.PAGE_NAME_TML%>", "<%=TML_PAGE_FOR_LOG%>")
			   println("************** " + "<%=TML_PAGE_FOR_LOG%>")
			   JSONObject.put(logJSON, "<%=AttributeID.ACCOUNT_TYPE%>", accountType)
			   if 1 == freeTrial
			    	JSONObject.put(logJSON, "<%=AttributeID.THIRTYDAYS_FREETRIAL_ENABLED%>", "30 days free enabled" )
	           endif
	           return logJSON	
	        endfunc


	    func logAdBannerLog(int type,JSONObject logJSON)
			println("...........in func logAdBannerLog")
			String from = getFromForLog()
			String campaignID = getCampaignIDForLog()
			JSONObject.put(logJSON, "<%=AttributeID.AD_BANNER_MSG%>", campaignID)
			JSONObject.put(logJSON, "<%=AttributeID.CAMPAIGN_ENTRY_POIT%>", from)
			if StatLogger.isStatEnabled(type)
			    println("type...................."+type)
			    println("logJSON...................."+logJSON)
				StatLogger.logEvent(type, logJSON)
			endif	        
        endfunc
        
        func learnMore()
        	int freeTrial = UserInfoManager_getFreeTrial()
			JSONObject logJSON = compileLogJSON("learnMore", freeTrial)
	        Purchase_M_logAdBannerLog(<%=EventTypes.AD_BANNER_CLICK%>,logJSON)
        	System.doAction("learnMoreMenu")
        endfunc
        
        func getFromForLog()
            String key = "<%=PURCHASE_JSON_INFO%>"
            JSONObject result = Cache.getJSONObjectFromTempCache(key)
            String from = "Unknown"
            if NULL != result
               if 1 == JSONObject.has(result,"from")
                  from = JSONObject.get(result,"from")
               endif
            endif
            
            return from
        endfunc
        
        func getCampaignIDForLog()
            String key = "<%=PURCHASE_JSON_INFO%>"
            JSONObject result = Cache.getJSONObjectFromTempCache(key)
            String campaignID = "Unknown"
            if NULL != result
               if 1 == JSONObject.has(result,"campaignID")
                  campaignID = JSONObject.get(result,"campaignID")
               endif
            endif
            
            return campaignID
        endfunc
     		]]>
	</tml:script>
		
		<tml:menuItem name="mrcForLite" onClick="purchase">
			<tml:bean name="productCode" valueType="String"	value="<%=PRODUCT_CODE_LITE %>" />
		</tml:menuItem>

		<tml:menuItem name="mrcForBundle" onClick="purchase">
			<tml:bean name="productCode" valueType="String"	value="<%=PRODUCT_CODE_BUNDLE %>" />
		</tml:menuItem>

		<tml:menuItem name="ppdForLite" onClick="purchase">
			<tml:bean name="productCode" valueType="String"	value="<%=PRODUCT_CODE_PAY_PER_DAY %>" />
		</tml:menuItem>
		
		<%-- TODO - change interface to Purchase Confirm --%>
		<tml:menuItem name="purchaseConfirm" pageURL="<%=banner_PurchaseConfirmPageUrl%>" />

		<tml:menuItem name="learnMoreMenu" pageURL="<%=banner_PurchasePageUrl%>" />
		<tml:menuItem name="learnMoreFunc" onClick="learnMore"/>
		
		
		<%-- panel for bundle --%>
		<tml:panel id="bottomPanelForBundle">
			<tml:image id="bottomImage" url="<%=imageUrl + "footerbg.png"%>" />
			<tml:label id="bottomLabelForBundle" textWrap="wrap" fontWeight="system" align="left">
				<![CDATA[<%=msgtemp.get("traffic.purchase.messageBundle")%>]]>
			</tml:label>
			<tml:urlLabel id="bottomURLForBundle" fontWeight="system_median" align="left|middle" fontColor="#005AFF">
				<%=msgtemp.get("traffic.purchase.learnmore")%>
				<tml:menuRef name="learnMoreFunc" />
			</tml:urlLabel>
			<tml:button id="mrcForBundleB"
				text="<%=msg.get("login.purchase.price.mrcForBundle") + " / month"%>"
				fontWeight="system|bold"
				imageClick="<%=imageUrl + "button_buy_highlighted.png"%>"
				imageUnclick="<%=imageUrl + "button_buy.png"%>">
				<tml:menuRef name="mrcForBundle" />
			</tml:button>
			<tml:label id="mrcForBundleL" fontWeight="bold" align="center|middle">
				<%=msg.get("login.purchase.period.mrc")%>
			</tml:label>
		</tml:panel>
		
		<%-- panel for lite --%>
		<tml:panel id="bottomPanelForLite">
			<tml:image id="bottomImage" url="<%=imageUrl + "footerbg.png"%>" />
			<tml:label id="bottomLabelForLite" textWrap="wrap" fontWeight="system" align="left">
				<![CDATA[<%=msgtemp.get("traffic.purchase.messageLite")%>]]>
			</tml:label>
			<tml:urlLabel id="bottomURLForLite" fontWeight="system_median" align="left|middle" fontColor="#005AFF">
				<%=msgtemp.get("traffic.purchase.learnmore")%>
				<tml:menuRef name="learnMoreFunc" />
			</tml:urlLabel>
			<tml:button id="ppdForLiteB"
				text="<%=msg.get("login.purchase.price.ppdForLite")+" / 24 hrs"%>"
				fontWeight="system|bold"
				imageClick="<%=imageUrl + "button_buy_highlighted.png"%>"
				imageUnclick="<%=imageUrl + "button_buy.png"%>">
				<tml:menuRef name="ppdForLite" />
			</tml:button>
			<tml:button id="mrcForLiteB"
				text="<%=msg.get("login.purchase.price.mrcForLite")+ " / month"%>"
				fontWeight="system|bold"
				imageClick="<%=imageUrl + "button_buy_highlighted.png"%>"
				imageUnclick="<%=imageUrl + "button_buy.png"%>">
				<tml:menuRef name="mrcForLite" />
			</tml:button>
			<tml:label id="mrcForLiteL" fontColor="green" fontWeight="bold" align="right|middle">
				<%=msg.get("login.purchase.period.mrc30")%>
			</tml:label>
		</tml:panel>