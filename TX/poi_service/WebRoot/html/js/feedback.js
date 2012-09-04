function onDeviceReady() {
	Feedback.fetchData();
	SDKAPI.getPreference(Feedback.setEmail,PreferenceConstants.EMAIL);
	SDKAPI.getSSOToken(Feedback.setSsoToken,false);
	var pageName = Feedback.pageName;
	if(pageName){
		var addressObj = {
			"type" : pageName 
		};
		if(!Feedback.dummyData){
			SDKAPI.invokePrivateService("getPoiDataForFeedback",addressObj,Feedback.setPoiData);
		}else{
			Feedback.setMockPoiData();
		}
		
		Feedback.showQuestionKey();
	}
	if(CommonUtil.isIphone()||ClientInfo.version.match("^7.2")){
		SDKAPI.getPreference(Feedback.setClientVersion,"internalVersion");
	}
}

function Feedback(){
	this.dummyData = false;
	this.pageName = "";
	this.email = "";
	this.ssoToken = "";
	this.clientVersion = "";
	this.currentLocation = "";
	
	this.preItemId = "";//for dishighlight pre item
	
	this.questionID = "";
	this.questionPre = "";
	this.questionKey = "";
	
	this.poiData = {};
	this.feedbackData = {};
}

Feedback.prototype = {
		constructor : Feedback,
		
		//bind click event and feedback page judgement
		init : function(){
			$("#saveButton").click(this.collectData); 
			$("#togglerButton").click(this.clickToggleButton);
			var Request = CommonUtil.getRequest();
			var page = Request["jsp"];
			this.pageName = page ? page : "";
			var dummy = Request["dummyData"];
			if(dummy){
				Feedback.dummyData = true;
				onDeviceReady();
				$("#title").click(function(){
					location.reload(true);
				});
			}
		},
		
		//as callback
		setSsoToken : function(ssoToken){
			Feedback.ssoToken = ssoToken ? ssoToken : "";
			CommonUtil.debug("ssoToken: " + ssoToken);
		},
		
		//show email if exits in userProfile
		//as callback
		setEmail : function(email){
			//email = "zhhyan@telenav.cn";
			Feedback.email = email ? email : "";
			document.getElementById("emailAddress").value = email;
			CommonUtil.debug("email: " + Feedback.email);
		},
		
		//as callback
		setClientVersion : function(version){
			Feedback.clientVersion = version ? version : "";
			CommonUtil.debug("clientVersion: " + version);
		},
		
		//only for general feedback
		//as callback
		setLocation : function(position){
			if(position){
				Feedback.currentLocation = CommonUtil.processGpsLocation(position);
				CommonUtil.debug("currentLocation: " + JSON.stringify(Feedback.currentLocation));
			}
		},
		
		//only for poilist/poidetail feedback
		//as callback
		setPoiData : function(data){
			CommonUtil.debug("poi data :  "+data);
			if(data){
				data = JSON.parse(data);
				var poiData = {};
				if(Feedback.pageName=="poilist"){
					//to handle ' in the string, client encode it.
					poiData["searchKeyword"] = decodeURI(data["keyword"]);
					poiData["searchCatName"] = decodeURI(data["categoryName"]);
				}else{
					poiData["poiId"] = data["poiId"];
					poiData["poiName"] = decodeURI(data["poiName"]);
					poiData["poiPhoneNumber"] = data["poiPhoneNumber"];
				}
				poiData["location"] = data["stop"];
				Feedback.poiData = poiData;
			}
		},
		
		setMockPoiData : function(){
			var poiData = {};
			if(this.pageName=="poilist"){
				poiData["searchKeyword"] = "KFC";
				poiData["searchCatName"] = "Food";
			}else{
				poiData["poiId"] = 1;
				poiData["poiName"] = "Starbuck Cofee";
				poiData["poiPhoneNumber"] = "1234567890";
			}
			var stop = {
					"lat":3761386,
					"lon":-12239382
			};
			poiData["location"] = JSON.stringify(stop);
			this.poiData = poiData;
		},
		
		//get Feedback Options
		fetchData : function(){
			var ajaxUrl = GLOBAL_hostUrl + "ajaxFeedback.do?jsp=" + this.pageName + "&" + CommonUtil.getClientInfo();
			CommonUtil.debug("request: " + ajaxUrl);
			var ajaxOptions = {
					loadingStyle : 1,
					url : ajaxUrl,
					onSuccess : this.show
			};
			
			$('#loading-overlay').hide(); 
			navigator.onLine ? CommonUtil.ajax(ajaxOptions) : CommonUtil.noNetworkError();
		},
		
		//as callback
		show : function(result){
			CommonUtil.debug("response: "+JSON.stringify(result));
			Feedback.questionID = result["questionID"];
			Feedback.questionPre = result["question"];
			var choices = result["choices"];
			
			$("#questionContent").html(Feedback.questionPre);
			
			Feedback.pageName ? Feedback.showPoiFB(choices) : Feedback.showGeneralFB(choices);
			
		},
		
		//general feedback
		showGeneralFB : function(choices){
			var mainChoiceHtml = '',choiceHtml = '';
			for(var i = 0; i < 2; i++){
					var optionDivClass = "div_table clsFixTable clsOptionBgNormal clsFontColor_gray ";
					switch(i){
						case 0: 
							optionDivClass += "clsFeedbackBgTop";
							break;
						case 1:
							optionDivClass += "clsFeedbackBgBottom";
							break;
				}
					mainChoiceHtml += 
						'		<div  id="optionDiv'+ i +'" class="' + optionDivClass + '" onclick="Feedback.changeImg('+i+')" ontouchstart="Feedback.highLightOptionItem('+i+')" ontouchend="Feedback.dishighLightOptionItem('+i+')" ontouchmove="Feedback.dishighLightOptionItem('+i+')">'
						+'			<div class="div_cell checkboxContent">'
				    	+'				<label class="fs_middle" id="op' + i +'">'+choices[i]+'</label>'
				    	+'			</div>'
						+'			<div class="div_cell align_right checkboxImg">'
						+'				<input class="clsCheckBox" type="checkbox" id="option'+ i +'" name="feedbacks" />'
						+'				<img class="check_box_unfocused" id="image' + i + '"/>'
				    	+'			</div>'
						+'		</div>';
				}
				for(var i = 2; i < choices.length; i++){
					var optionDivClass = "div_table clsFixTable clsOptionBgNormal clsFontColor_gray ";
					switch(i){
						case 2: 
							optionDivClass += "clsFeedbackBgTop";
							break;
						case (choices.length-1):
							optionDivClass += "clsFeedbackBgBottom";
							break;
						default :
							optionDivClass += "clsFeedbackBgMiddle";
					}
					choiceHtml +=
				    	'		<div  id="optionDiv'+ i +'" class="' + optionDivClass + '" onclick="Feedback.changeImg('+i+')" ontouchstart="Feedback.highLightOptionItem('+i+')" ontouchend="Feedback.dishighLightOptionItem('+i+')" ontouchmove="Feedback.dishighLightOptionItem('+i+')">'
				    	+'			<div class="div_cell checkboxContent">'
				    	+'				<label class="fs_middle" id="op' + i +'">'+choices[i]+'</label>'
				    	+'			</div>'
				    	+'			<div class="div_cell align_right checkboxImg">'
						+'				<input class="clsCheckBox" type="checkbox" id="option'+ i +'" name="feedbacks" />'
						+'				<img class="check_box_unfocused" id="image' + i + '"/>'
				    	+'			</div>'
						+'		</div>';
				}
				
				$("#mainOptionsDiv").html(mainChoiceHtml);
				$("#optionListDiv").html(choiceHtml);
				
				SDKAPI.getCurrentLocation(this.setLocation);
		},
		
		//poilist and poidetail feedback
		showPoiFB : function(choices){
			var choiceHtml = '';
			for(var i = 0; i < choices.length; i++){
				var optionDivClass = "div_table clsFixTable clsOptionBgNormal clsFontColor_gray ";
				switch(i){
				case 0: 
					optionDivClass += "clsFeedbackBgTop";
					break;
				case (choices.length-1):
					optionDivClass += "clsFeedbackBgBottom";
					break;
				default :
					optionDivClass += "clsFeedbackBgMiddle";
			}
				choiceHtml += 
					'		<div  id="optionDiv'+ i +'" class="' + optionDivClass + '" onclick="Feedback.changeImg('+i+')" ontouchstart="Feedback.highLightOptionItem('+i+')" ontouchend="Feedback.dishighLightOptionItem('+i+')" ontouchmove="Feedback.dishighLightOptionItem('+i+')">'
					+'			<div class="div_cell checkboxContent">'
			    	+'				<label class="fs_middle" id="op' + i +'">'+choices[i]+'</label>'
			    	+'			</div>'
					+'			<div class="div_cell align_right checkboxImg">'
					+'				<input class="clsCheckBox" type="checkbox" id="option'+ i +'" name="feedbacks" />'
					+'				<img class="check_box_unfocused" id="image' + i + '"/>'
			    	+'			</div>'
					+'		</div>';
			}
			
			$("#optionListDiv").html(choiceHtml);
		},
		
		//show question key, only for poilist and poidetail feedback
		showQuestionKey : function(){
			var questionKey = "";
			if (this.pageName == "poilist"){
				questionKey = this.poiData["searchKeyword"] + " ?";
			} else {
				questionKey = this.poiData["poiName"] + " ?";
			}
			
			$("#questionKey").html(questionKey);
			this.questionKey = questionKey;
		},
		
		//switch toggler button image
		clickToggleButton : function(){
			var toggler = document.getElementById("togglerCheckBox");
			if(toggler.checked){
				$("#togglerButton").attr("class","toggler_button_OFF");
				toggler.checked = false;
				$("#emailDiv").hide();
				$("#toggleDiv").attr("class","div_table clsFeedbackBg clsOptionBgNormal");
			}else{
				$("#togglerButton").attr("class","toggler_button_ON");
				toggler.checked = true;
				$("#emailDiv").css({
					"display": "table"
				});
				$("#toggleDiv").attr("class","div_table clsFeedbackBgTop clsOptionBgNormal");
			}
		},
		
		collectData : function(){
			var self = Feedback;
			var question = "", pageName = self.pageName;
			if(pageName){
				question = self.questionPre + " " + self.questionKey;
			}else{
				question = self.questionPre;
			}
			
			//get comment
			var comments = CommonUtil.getValidString($('#comment').val());
			var feedbacks = new Array();
			var j=0;
			var options = document.getElementsByName("feedbacks");
			for (var i=0; i < options.length; i++){
				if(options[i].checked){
					//get selected options
					feedbacks[j] = $("#op"+i).html();
					j++;
				}
			}
			var length = feedbacks.length;
			// no option selected and no comment
			if(length==0&&comments==""){
				CommonUtil.showAlert("", I18NHelper["poi.feedback.selectFeedback"], I18NHelper["common.button.OK"]);
				return false;
			}
			
			var feedbackData = {
				"pageName": self.pageName,
				"questionID": self.questionID,
				//"ssoToken": self.ssoToken,
				"clientVersion": self.clientVersion,
				"email": self.email,
				"question": question,
				"feedbacks": JSON.stringify(feedbacks),
				"comments":comments,
				"userAgent": navigator.userAgent
			};
			
			var poiData = self.poiData;
			var currentLocation = self.currentLocation;
			if(pageName){
				feedbackData["location"] = poiData["location"];
				if(pageName=="poilist"){
					feedbackData["searchCatName"] = poiData["searchCatName"];
					feedbackData["searchKeyword"] = poiData["searchKeyword"];
				}else if(pageName=="poidetail"){
					feedbackData["poiName"] = poiData["poiName"];
					feedbackData["poiPhoneNumber"] = poiData["poiPhoneNumber"];
					feedbackData["poiID"] = poiData["poiID"];
				}
			}else{
				feedbackData["currentLocation"] = currentLocation;
			}
			
			//get email
			var email = CommonUtil.getValidString($('#emailAddress').val());
			CommonUtil.debug("email inputed: "+email);
			feedbackData["email"] = email;
			
			self.feedbackData = feedbackData;
			CommonUtil.debug("feedbackData: " + JSON.stringify(self.feedbackData));
			
			if(document.getElementById("togglerCheckBox").checked){
				if(email){
					if(!CommonUtil.emailAddressCheck(email)){
						//invalid email address
						CommonUtil.showAlert("",I18NHelper["poi.feedback.invalidEmail"], I18NHelper["common.button.OK"]);
						return;
					}
					Feedback.save();
				}else{//no contact email
					Feedback.showConfirmPopup(I18NHelper["poi.feedback.hearBack"],"Feedback.onClickSkip",I18NHelper["common.button.Skip"],"Feedback.onClickBack",I18NHelper["common.button.OK"]);
				}
			 }else{
				 Feedback.save();
			 }
		},
		
		save : function(){
			if(this.getSdCardLogFlag()){
				this.showConfirmPopup(I18NHelper["poi.feedback.submitLog"], "Feedback.onClickNo", I18NHelper["common.No"], "Feedback.onClickYes", I18NHelper["common.Yes"]);
			}else{
				this.submit();
			}
		},
		
		submit : function(){
			var jsonStr = encodeURIComponent(JSON.stringify(this.feedbackData));
			var ajaxUrl = GLOBAL_hostUrl + "feedbackSave.do?jsonStr=" + jsonStr + "&" + CommonUtil.getClientInfo();
			CommonUtil.debug("ajaxUrl: "+decodeURIComponent(ajaxUrl));
			var ajaxOptions = {
					data:0,
					loadingStyle:1,
					url:ajaxUrl,
					onSuccess:this.submitSuccess
			};
			
			if(navigator.onLine){
				CommonUtil.ajax(ajaxOptions);
			}
		},
		
		submitSuccess : function(){
			var divContent = 
				'<div class="div_table confirmContent">' + 
						'<div class="div_cell fs_large fc_gray align_center">'+ I18NHelper["poi.feedback.submitSuccess"] + '</div>' + 
				'</div>' ;
			$("#submitSuccessPopup").html(divContent);
			PopupUtil.show("submitSuccessPopup","backgroundPopup");
			//close popup in 3 seconds
			setTimeout(Feedback.onClickOK,3000);
		},
		
		//switch checkbox image
		changeImg : function(id){
			var optionId = "option"+id;
			var option =  document.getElementById(optionId);
			if(option.checked){
				document.getElementById("image"+id).className = "check_box_unfocused";
				option.checked = false;
			}else{
				document.getElementById("image"+id).className = "check_box_focused";
				option.checked = true;
			}
		},
		
		//show confirm popup
		showConfirmPopup : function(confirmMsg,a_fun,a_msg,b_fun,b_msg){
			var divContent = '<div class="fdpopupdiv align_center">'+
				'<div class="div_table confirmContent">' + 
					'<div class="div_cell fs_large fc_gray">'+ confirmMsg + '</div>' + 
					'</div>' + 
				'</div>' + 
				'<div class="popbottomdivstyle">' + 
					'<div class="div_table align_center">' + 
						'<div class="div_cell" style="width:50%"> ' +
							'<button class="confirmButton clsMiddleRadius clsButtonColorNormal clsButtonBgNormal"'+ 
				 			' ontouchstart="highlightBtnAll(this,\'clsButtonColorNormal\',\'clsButtonColorHighlight\')" ontouchend="disHighlightBtnAll(this,\'clsButtonColorHighlight\',\'clsButtonColorNormal\')" ontouchmove="disHighlightBtnAll(this,\'clsButtonColorHighlight\',\'clsButtonColorNormal\')"' +  
				 			' onClick="'+a_fun+'()"><span class="fs_large">'+a_msg+'</span></button>'+
						'</div>' + 
						'<div class="div_cell" style="width:50%"> ' +
							'<button class="confirmButton clsMiddleRadius clsButtonColorNormal clsButtonBgNormal"'+ 
				 			' ontouchstart="highlightBtnAll(this,\'clsButtonColorNormal\',\'clsButtonColorHighlight\')" ontouchend="disHighlightBtnAll(this,\'clsButtonColorHighlight\',\'clsButtonColorNormal\')" ontouchmove="disHighlightBtnAll(this,\'clsButtonColorHighlight\',\'clsButtonColorNormal\')"' +  
				 			' onClick="'+b_fun+'()"><span class="fs_large">'+b_msg+'</span></button>'+
				 		'</div>' + 
					'</div>' + 			 
				'</div>';
			$("#emailConfirmPopup").html(divContent);
			PopupUtil.show("emailConfirmPopup","backgroundPopup");
		},
		
		//close popup and go back
		onClickOK : function(){
			PopupUtil.hide();
			SDKAPI.goBack(true);
		},
		
		onClickSkip : function(){
			PopupUtil.hide();
			this.save();
		},
		
		onClickBack : function(){
			PopupUtil.hide();
			var e = document.getElementById("emailAddress"),y = e.offsetTop;
			while(e = e.offsetParent){
				y += e.offsetTop;
			}
			window.scrollTo(0,y);
		},
		
		onClickYes : function(){
			PopupUtil.hide();
			SDKAPI.invokePrivateService("submitSdCardLog");
			this.submit();
		},
		
		onClickNo : function(){
			PopupUtil.hide();
			this.submit();
		},
		
		getSdCardLogFlag : function(){
			var Request = CommonUtil.getRequest();
			return ("true" == Request["needSdCardLog"])? true: false;
		},
		
		highLightOptionItem : function(id){
			this.dishighLightOptionItem(this.preItemId);
			this.preItemId = id;
			var elementId = "optionDiv"+id;
			var element = document.getElementById(elementId);
			switchHightlight(element,"clsOptionBgNormal","clsButtonBgHighlight");
			switchHightlight(element,"clsFontColor_gray","clsFontColor_white");
		},
		
		dishighLightOptionItem : function(id){
			var elementId = "optionDiv"+id;
			var element = document.getElementById(elementId);
			switchHightlight(element,"clsButtonBgHighlight","clsOptionBgNormal");
			switchHightlight(element,"clsFontColor_white","clsFontColor_gray");
		}
};

var Feedback = new Feedback();
