function OpenTableJS() {

}

OpenTableJS.prototype = {
	checkData : function() {

	},
	getPartnerPoiId : function(){
		return 32;
		//return JSON.parse(CommonUtil.getFromCache(appendPoiKey(PoiCacheKeys.POIDETAIL))).openTable.partnerPoiId;
	},
	fetchTimePeriod : function() {
		var checkInDate = Global_calendar.getCheckInDate();
		var dateStr = checkInDate.getFullYear() + "-" + (checkInDate.getMonth() + 1) + "-" + checkInDate.getDate();
		var checkInTime = Global_timePicker.getHourWithZero() + ":" + Global_timePicker.getMinuteWithZero();

		var criteria = {
			"PartySize" : Global_partySizePicker.getSize(),
			"SearchRequestTime" : checkInTime,
			"SearchRequestDate" : dateStr,
			"lafObjectId" : this.getPartnerPoiId()
		};

		var url = GLOBAL_hostUrl + "getTableAvailable.do?jsonStr=" + JSON.stringify(criteria) + "&"
				+ CommonUtil.getClientInfo();
		CommonUtil.debug(url);

		loadPopup_poidetail();

		GLOBAL_xmlhttpforAjax.open("GET", url, true);
		GLOBAL_xmlhttpforAjax.onreadystatechange = function() {
			if (handleCommonAjaxError()) {
				if (GLOBAL_xmlhttpforAjax.responseText) {
					var json = JSON.parse(GLOBAL_xmlhttpforAjax.responseText);

					var time1 = json.SearchOfferTime1;
					var time2 = json.SearchOfferTime2;
					var time3 = json.SearchOfferTime3;

					if ((!time1 || time1 == "") && (!time2 || time2 == "") && (!time3 || time3 == "")) {
						CommonUtil.showAlert("",I18NHelper["openTable.noTableAvailable"],I18NHelper["common.button.OK"]);
					} else {
						$("#openTableTime1").html(time1);
						$("#openTableTime2").html(time2);
						$("#openTableTime3").html(time3);
						$("#restaurant_openTableTime1").val(time1);
						$("#restaurant_openTableTime2").val(time2);
						$("#restaurant_openTableTime3").val(time3);

						if (!time1 || time1 == "") {
							$("#openTableTime1").hide();
						}
						if (!time2 || time2 == "") {
							$("#openTableTime2").hide();
						}
						if (!time3 || time3 == "") {
							$("#openTableTime3").hide();
						}

						$("#restaurant_availalbeTimeTable").show();
						$("#restaurant_findTablesBtn").hide();
						$("#restaurant_pickTimeTable").hide();
						$("#restaurant_partySizeTable").attr("class",
								"clsConfigRow clsMiddleItemRadius fs_small clsFontColor_gray");

						$("#restaurant_date_td").hide();
						$("#restaurant_partySize_td").hide();

					}
				} else {
					CommonUtil.showAlert("",I18NHelper["openTable.noTableAvailable"],I18NHelper["common.button.OK"]);
				}

			}
		};
		GLOBAL_xmlhttpforAjax.send(null);
	},

	showMap : function() {
		var poiDetailObj = JSON.parse(CommonUtil.getFromCache(appendPoiKey(PoiCacheKeys.POIDETAIL)));
		var lat = poiDetailObj.poi.stop.lat;
		var lon = poiDetailObj.poi.stop.lon;
		if (lat != 0 && lon != 0) {
			lat = lat / JSConstants.DEGREE_MULTIPLIER;
			lon = lon / JSConstants.DEGREE_MULTIPLIER;
			var addressParameter = lat + ',' + lon;
			var widthParameter = $("#mapImageDivWrapper").outerWidth();
			var heightParameter = $("#mapImageDivWrapper").outerHeight();

			// Get Image with customized center
			var searchCriteria = {
				"imageName" : '',
				"width" : widthParameter,
				"height" : heightParameter,
				"center" : addressParameter,
				"markers" : 'color:blue|' + addressParameter
			};

			var serverIp = "http://mapapicdn.telenav.com/maps/staticmap?";
			var apiKey = "AQAAAS+jF+1of/////////8AAAABAAAAAQEAAAAQZUmvC1JKzdtXA+1AkO2ZqwEAAAAOAwAAAGsAAACYAAAAAgA=";
			var staticMapUrl = serverIp;
			staticMapUrl += "width=" + searchCriteria.width + "&height=" + searchCriteria.height + "&zoom=1&center=";
			staticMapUrl += searchCriteria.center + "&markers=" + encodeURI(searchCriteria.markers) + "&apiKey="
					+ encodeURI(apiKey);
			$("#restaurantMapImg")[0].onerror = function() {
				PopupUtil.hide();
			};
			$("#restaurantMapImg")[0].onload = function() {
				PopupUtil.hide();
				$("#restaurantMapDiv").show();
				$("#restaurantMapImageDivWrapper").show();
			};

			CommonUtil.debug(staticMapUrl);
			var oldSrc = $("#restaurantMapImg")[0].src;
			$("#restaurantMapImg")[0].src = staticMapUrl;

			if (oldSrc == staticMapUrl) {
				$("#restaurantMapDiv").show();
				$("#restaurantMapImageDivWrapper").show();
			} else {
				loadPopup_poidetail();
			}
		}

		GLOBAL_isRestaurantDetailLoaded = true;

	},

	showOpenTableMain : function() {
		if (GLOBAL_isRestaurantDetailLoaded) {

		} else {
			$("#restaurantDetailDiv").hide();
			
			partnerId = this.getPartnerPoiId();
			loadPopup_poidetail();
			var ajxUrl = GLOBAL_hostUrl + "getRestaurantDetail.do?partnerId=" + partnerId + "&" + CommonUtil.getClientInfo();
			var ajaxOptions = {
					data:0,
					url:ajxUrl,
					onSuccess:this.ajaxCBOpenTableMain
			};
			CommonUtil.ajax(ajaxOptions);

			var that = this;

			GLOBAL_xmlhttpforAjax.onreadystatechange = function() {
				if (handleCommonAjaxError()) {}
			};
			GLOBAL_xmlhttpforAjax.send(null);

		}

	},
	
	ajaxCBOpenTableMain : function(responseText){
		if (responseText) {
			$("#restaurantDetailDiv").show();
			var json = JSON.parse(responseText);
			CommonUtil.debug(json);

			$("#restaurant_desc").html(json.RestaurantDescription);
			$("#restaurant_foodType").html(json.SearchFoodType);
			$("#restaurant_parking").html(json.Parking);
			$("#restaurant_url").html(json.RestaurantUrl);
			$("#restaurant_url").attr("href", "http://" + json.RestaurantUrl);
			$("#restaurant_location_desc").html(json.RestaurantLocationDesc);
			$("#restaurant_payment").html(json.RestaurantPaymentType);

			var starNum = 0;
			starNum = json.SearchPriceSign;//add by jiangxl 2011.08.29

			$("#restaurant_priceRange1").attr("class", getPriceClass(starNum));
			$("#restaurant_priceRange2").attr("class", getPriceClass(starNum - 1));
			$("#restaurant_priceRange3").attr("class", getPriceClass(starNum - 2));
			$("#restaurant_priceRange4").attr("class", getPriceClass(starNum - 3));
			$("#restaurant_hiddenOpenHour").hide();

			if (json.HoursOfOperation && json.HoursOfOperation != null) {
				var temp = json.HoursOfOperation;
				CommonUtil.debug(temp);
				CommonUtil.debug(temp.replace(/<BR>/g, "").replace(/\n/g, ""));
				$("#restaurant_hiddenOpenHour").html(temp.replace(/<BR><BR>/g, "<BR>"));
			}

			GLOBAL_isRestaurantDetailLoaded = true;
			that.fetchDeals();
			$("#restaurantMapDiv").show();//add by jiangxl

		} else {
			that.showMap();
		}
	},
	
	fetchDeals : function() {
		var poiDetailObj = JSON.parse(CommonUtil.getFromCache(appendPoiKey(PoiCacheKeys.POIDETAIL)));

		if (shouldCallAdsServer(poiDetailObj)) {
			this.fetchOrganicAdsForOpenTable();
		}
	},

	fetchOrganicAdsForOpenTable : function() {
		var ajxUrl = GLOBAL_hostUrl + "getPoiDetailData.do?operateType=adsPoi&jsonStr="
				+ JSON.stringify(getPoiSearchKey()) + "&" + CommonUtil.getClientInfo();
		var ajaxOptions = {
				data:0,
				loadingStyle:1,
				url:ajxUrl
		};
		var self = this;
		ajaxOptions.onSuccess = function(responseText) {
			var result = CommonUtil.getValidString(responseText);
			if ("" != result) {
				var resultObj = JSON.parse(result);
				if (resultObj.success) {
					self.displayOpenTableDeal(resultObj.dealTab);
				}
			}
		};
		
		CommonUtil.ajax(ajaxOptions);
	},

	displayOpenTableDeal : function(data) {
		CommonUtil.showAlert("",data,I18NHelper["common.button.OK"]);
	},

	showOrHide : function() {
		var display = $("#restaurant_hiddenOpenHour").css("display");
		CommonUtil.debug(display);
		if (display == "block") {
			$("#restaurant_hiddenOpenHour").hide();
		} else {
			$("#restaurant_hiddenOpenHour").show();
		}
	},
	goToReservationDetail : function(index) {

		var checkInDate = Global_calendar.getCheckInDate();
		var dateStr = checkInDate.getFullYear() + "-" + (checkInDate.getMonth() + 1) + "-" + checkInDate.getDate();
		var checkInTime = $("#restaurant_openTableTime" + index).val();

		var dateTime = dateStr + " " + checkInTime;
		var poiObj = JSON.parse(CommonUtil.getFromCache(appendPoiKey(PoiCacheKeys.POIDETAIL))).poi;
		var poiId = poiObj.bizPoi.poiId;
		var poiName = poiObj.bizPoi.brand;

		var criteria = {
			"name" : poiName,
			"partySize" : Global_partySizePicker.getSize(),
			"dateTime" : dateTime,
			"date" : dateStr,
			"time" : checkInTime,
			"partnerPoiId" : this.getPartnerPoiId(),
			"poiId" : poiId
		};
		
		this.saveInfo(JSON.stringify(criteria), 
					  CommonUtil.getFromCache(appendPoiKey(PoiCacheKeys.POIDETAIL)), 
					  document.getElementById("logImageDiv").getElementsByTagName("IMG")[0].src);

		var hostUrlOfOpenTable = LocalAppsURLHelper["RESTAURANT"] + "&" + CommonUtil.getClientInfo();
		CommonUtil.debug(hostUrlOfOpenTable);
		SDKAPI.launchLocalApp(hostUrlOfOpenTable);

	},
	/**
	 * TODO save necessary information to client(mobile phone)
	 * cause: the localStorage cannot be visited cross domain. So we must save the data into client side
	 * 		  for localApps project visiting.
	 */
	saveInfo : function(reserveConfig, poiDetail, logo){
		navigator.tnservice.deletePersistentData("FOR_LOCALAPPS_DATA",function(){},function(){});
		
		var openTableInfo = '{"RESERVATION_CONFIG":'+reserveConfig+', "RESTAURANT_POI_DETAIL":'+poiDetail+',"RESTAURANT_LOGO":"'+logo+'"}';
		var localAppsData = '{"OPENTABLE":'+openTableInfo+'}';
		navigator.tnservice.setPersistentData("FOR_LOCALAPPS_DATA",localAppsData,"",function(){},function(){});
	},
	
	/**
	 * generate open table main tab and reserve tab
	 */
	writeHTML : function(){
		var mainTabObj = document.getElementById("poiopentablemain");
		var reserveTabObj = document.getElementById("poiopentablereserve");
		var mainTabHtml="";
		var reserveTabHtml="";
		//1. generate open table main tab html
		mainTabHtml  = '<div width="100%" id="restaurantDetailDiv" display="none">'+
		 			'	<table class="bg_orange fs_verySmall ot_deals fc_white">'+
					 '		<tr>'+
					 '			<td width="80%" class="left_pad">2 Deals Available</td>'+
					 '			<td width="20%" align="right"><img class="hotel_next_arrow_icon_unfocused.png"></td>'+
					 '		</tr>'+
					 '	</table>'+
					 '	<div class="ot_allPadding">'+
					 '		<table width="100%">'+
					 '			<tr>'+
					 '				<td width="60%" >'+
					 '					<table class="fc_gray fs_middle">'+
					 '						<tr onclick="OpenTable.showOrHide()">'+
					 '							<td class="ot_middleHeightLine" valign="top">'+
					 '									<span class="fc_green bold">'+ I18NHelper["openTable.open"] + '</span>'+
					 '									<span class="fc_gray">'+ I18NHelper["openTable.hoursAvailable"] + '</span>'+
					 '							</td>'+
					 '						</tr>'+
					 '					</table>'+
					 '				</td>'+
					 '				<td width="40%" align="right" valign="top" class="fc_gray fs_verySmall">'+
					 '					<div id="restaurant_location_desc"></div>'+
					 '					<div id="restaurant_parking"></div>'+
					 '				</td>'+
					 '			</tr>'+
					 '		</table>'+
					 '		<table width="100%">'+
					 '			<tr>'+
					 '				<td id="restaurant_hiddenOpenHour"  class="fc_gray fs_verySmall">	</td>'+
					 '			</tr>'+
					 '			<tr>'+
					 '				<td>'+
					 '					<table>'+
					 '						<tr>'+
					 '							<td id="restaurant_priceRange1" class="clsDollarGrey" align="center">$</td><td width="2px"></td>'+
					 '					 		<td id="restaurant_priceRange2" class="clsDollarGrey" align="center">$</td><td width="2px"></td>'+
					 '							<td id="restaurant_priceRange3" class="clsDollarGrey" align="center">$</td><td width="2px"></td>'+
					 '							<td id="restaurant_priceRange4" class="clsDollarGrey" align="center">$</td><td width="2px"></td>'+
					 '						</tr>'+
					 '					</table>'+
					 '				</td>'+
					 '			</tr>'+
					 '		</table>'+
					 '		<!-- detail info -->'+
					 '		<table width="100%" class="fc_gray fs_middle">'+
					 '			<tr>'+
					 '				<td class="fc_gray"> <span class="bold">'+ I18NHelper["openTable.cuisine"]+ ':</span> <span id="restaurant_foodType"></span></td>'+
					 '			</tr>'+
					 '			<tr>'+
					 '				<td class="fc_gray"> <span class="bold">'+ I18NHelper["openTable.webSite"]+ ':</span> <span><a id="restaurant_url" href="#" class="fc_blue" onclick=""></a></span></td>'+
					 '			</tr>'+
					 '			<tr>'+
					 '				<td class="fs_verySmall" id="restaurant_desc"></td>'+
					 '			</tr>'+
					 '		</table>'+
					 '	</div>'+
					 '  </div>'+
					 '   <div id="restaurantMapDiv" style="display:none;" width="100%" align="center">'+
					 '   		<div id="restaurantMapImageDivWrapper" class="clsRestaurantMapImageDiv" style="display:none;">'+
					 '   		 	<img id="restaurantMapImg" class="clsRestaurantMapImg" src=""></img>'+
					 '   		</div>'+
					 '   		<table class="clsRestaurantMapButtonTable">'+
					 '   		  <tr>'+
					 '		 	 <td id="mainButtonsBar" class="clsMainBottom" >'+
					 '				<table border="0" cellpadding="0" cellspacing="0" width="99%" align="center" class="clsFixTable">'+
					 '				  <tr>'+
					 '				    <td width="20%" height="100%" align="left" valign="middle"><div class="clsBigButtonStyle fs_veryLarge fc_black clsLargeRadius clsButtonBgNormal" align="center" onClick="showMapOfClient()"'+
					 '				    	ontouchstart="highlightBtnAll(this,\'fc_black\',\'fc_white\')" ontouchend="disHighlightBtnAll(this,\'fc_white\',\'fc_black\')">'+
					 '				    	<p class="clsBigButtonLabel">'+ I18NHelper["poidetail.button.map"] + '</p></div></td>'+
					 '					<td width="5%"></td>'+
					 '					<td width="20%" align="left" valign="middle" ><div class="clsBigButtonStyle fs_veryLarge fc_black clsLargeRadius clsButtonBgNormal" align="center" onClick="shareAddress()"'+
					 '						ontouchstart="highlightBtnAll(this,\'fc_black\',\'fc_white\')" ontouchend="disHighlightBtnAll(this,\'fc_white\',\'fc_black\')">'+
					 '						<p  class="clsBigButtonLabel">'+ I18NHelper["poidetail.button.share"] + '</p></div></td>'+
					 '					<td width="5%"></td>'+
					 '					<td width="45%" align="left" valign="middle" ><div class="clsBigButtonStyle fs_veryLarge fc_black clsLargeRadius clsButtonBgNormal" align="center"  onClick="searchNearBy()"'+
					 '						ontouchstart="highlightBtnAll(this,\'fc_black\',\'fc_white\')" ontouchend="disHighlightBtnAll(this,\'fc_white\',\'fc_black\')">'+
					 '						<p  class="clsBigButtonLabel" >'+ I18NHelper["poidetail.button.searchnearby"]+ '</p></div></td>'+
					 '				  </tr>	  '+
					 '				</table>'+
					 '	 	 	</td>'+
					 '  		</tr>'+
					 '  	  </table>'+
					 ' </div>';
		mainTabObj.innerHTML = mainTabHtml;

		// 2. generate open table reserve tab html
		reserveTabHtml = '<div class="clsOTRserveConfigBoard">'+
						'	<table class="clsConfigRow clsTopItemRadius fs_small clsFontColor_gray">'+
						'		<tr width="100%" onclick="Global_calendar.showForCheckIn();">'+
						'			<td width="30%" align="left"><span style="padding-left: 15px" class="bold">'+ I18NHelper["openTable.date"] + '</span></td>'+
						'			<td  align="right" class="fc_blue" style="padding-right: 15px"><span id="openTableCheckInDate" ></span></td>'+
						'			<td id="restaurant_date_td" align="right" class="clsOpenTableImgTd"  align="center"><img src="'	+ GLOBAL_sharedImageUrl	+ 'hotel_calendar_icon_unfocused.png"></td>'+
						'		</tr>'+
						'	</table>'+
						'	<table class="clsConfigRow clsMiddleItemRadius fs_small clsFontColor_gray" id="restaurant_pickTimeTable">'+
						'		<tr width="100%"  onClick="Global_timePicker.show();">'+
						'			<td width="30%" align="left"><span style="padding-left: 15px" class="bold">'+ I18NHelper["openTable.time"] + '</span></td>'+
						'			<td  align="right" class="fc_blue" style="padding-right: 15px">'+ I18NHelper["openTable.around"]+ ' &nbsp;<span id="openTableCheckInTime"></span></td>'+
						'			<td id="restaurant_time_td" class="clsOpenTableImgTd"  align="center"><img src="'+ GLOBAL_sharedImageUrl+ 'ac_main_recent_icon_unfocused.png"></td>'+
						'		</tr>'+
						'	</table>'+
						'	<table class="clsConfigRow clsBottomItemRadius fs_small clsFontColor_gray" id="restaurant_partySizeTable">'+
						'		<tr onclick="Global_partySizePicker.show()">'+
						'			<td width="40%" align="left"><span style="padding-left: 15px" class="bold">'+ I18NHelper["openTable.partySize"] + '</span></td>'+
						'			<td  align="right" class="fc_blue" style="padding-right: 15px"><span id="openTablePartySize">'+ I18NHelper["openTable.onePerson"] + '</span></td>'+
						'			<td id="restaurant_partySize_td" class="clsOpenTableImgTd" align="center"><img src="'+ GLOBAL_sharedImageUrl + 'down_blue_arrow.png" ></td>'+
						'		</tr>'+
						'	</table>'+
						'	<table id="restaurant_availalbeTimeTable" class="clsConfigRow clsBottomItemRadius fs_small clsFontColor_gray " style="display: none;">'+
						'		<tr>'+
						'			<td width="33.3%" align="middle">'+
						'				<button style="width:90%" onTouchStart="changeCSS(this,\'clsBigButtonStyle fs_veryLarge fc_white clsLargeRadius clsButtonBgRedHover\')"'+
						'				 	 ontouchend="changeCSS(this,\'clsBigButtonStyle fs_veryLarge fc_white clsLargeRadius clsButtonBgRed\')" '+
						'				 	 onclick="OpenTable.goToReservationDetail(1)" '+
						'				 	 class="clsBigButtonStyle fs_veryLarge fc_white clsLargeRadius clsButtonBgRed" id="openTableTime1">'+
						'				</button>'+
						'			</td> '+
						'			<td width="33.3%"  align="middle">'+
						'				<button style="width:90%" onTouchStart="changeCSS(this,\'clsBigButtonStyle fs_veryLarge fc_white clsLargeRadius clsButtonBgRedHover\')"'+
						'					 ontouchend="changeCSS(this,\'clsBigButtonStyle fs_veryLarge fc_white clsLargeRadius clsButtonBgRed\')" '+
						'					 onclick="OpenTable.goToReservationDetail(2)" '+
						'					 class="clsBigButtonStyle fs_veryLarge fc_white clsLargeRadius clsButtonBgRed"'+ 
						'					 id="openTableTime2"></button>'+
						'			</td>'+
						'			<td width="33.3%" align="middle">'+
						'				<button style="width:90%" onTouchStart="changeCSS(this,\'clsBigButtonStyle fs_veryLarge fc_white clsLargeRadius clsButtonBgRedHover\')"'+  
						'					ontouchend="changeCSS(this,\'clsBigButtonStyle fs_veryLarge fc_white clsLargeRadius clsButtonBgRed\')" '+
						'					onclick="OpenTable.goToReservationDetail(3)" '+
						'					class="clsBigButtonStyle fs_veryLarge fc_white clsLargeRadius clsButtonBgRed" id="openTableTime3"></button>'+
						'			</td>'+
						'		</tr>'+
						'	</table>'+
						'	<input id="restaurant_openTableTime1" value="" type="hidden" />'+
						'	<input id="restaurant_openTableTime2" value="" type="hidden" />'+
						'	<input id="restaurant_openTableTime3" value="" type="hidden" />'+
						'	<table class="clsOpenTableButtonRow" id="restaurant_findTablesBtn">'+
						'		<tr>'+
						'			<td width="100%">'+
						'				<button onclick="OpenTable.fetchTimePeriod()" class="clsBigButtonStyle fs_veryLarge fc_white clsLargeRadius clsButtonBgRed"'+ 
						'						onTouchStart="changeCSS(this,\'clsBigButtonStyle fs_veryLarge fc_white clsLargeRadius clsButtonBgRedHover\')"  '+
						'						ontouchend="changeCSS(this,\'clsBigButtonStyle fs_veryLarge fc_white clsLargeRadius clsButtonBgRed\')">'+I18NHelper["openTable.findATableButton"]+
						'				</button>'+
						'			</td>'+
						'		</tr>'+
						'	</table>'+
						'	<table class="clsOpenTableButtonRow">'+
						'		<tr><td width="100%" align="right"><img alt="openTable.com" src="'+GLOBAL_sharedImageUrl+'opentable_logo.png"></td></tr>'+
						'	</table>'+
						'</div>';
		reserveTabObj.innerHTML = reserveTabHtml;
	}
	//Temporarily useless
	/*
	 * go to restaurant web site
	 * @param url
	go2WebSite : function(url){
		if(url&&url != ""){
			SDKAPI.launchLocalApp(url);
		}
	},
	setRestaurantDealCache : function(data) {
		localStorage.setItem("LS_RESTAURANT_DEALS", data);
	},
	getRestaurantDealCache : function() {
		return localStorage.getItem("LS_RESTAURANT_DEALS");
	}*/
};

var OpenTable = new OpenTableJS();
OpenTable.writeHTML();
var GLOBAL_isRestaurantDetailLoaded = false;