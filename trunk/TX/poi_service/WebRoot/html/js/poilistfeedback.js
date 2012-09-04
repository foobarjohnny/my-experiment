var GLOBAL_previewHighLightItemId = "";

function init(){
	setQuestionContent();
    $("#saveButton").click(saveFeedbackWithAjax); 
}

function setQuestionContent(){
	//set question content
	if(GLOBAL_parameter["searchKeyword"] == ""){
		$("#questionContent").html(I18NHelper["poi.feedback.listQuestion"]);
	}else{
		if(CommonUtil.isLandscape()){
			$("#questionContent").html(I18NHelper["poi.feedback.listQuestionWithKeyword"]+" <span class='clsQuestionKey'>"+GLOBAL_parameter["searchKeyword"] + " ?</span>");
			$("#questionKey").html("");
		}else{
			$("#questionContent").html(I18NHelper["poi.feedback.listQuestionWithKeyword"]);
			$("#questionKey").html(GLOBAL_parameter["searchKeyword"] + " ?");
		}
	}
}

function getFeedBacks(){
	var feedbacks = new Array();
	var j=0;
	var options = document.getElementsByName("feedbacks");
	for (var i=0; i < options.length; i++)
	{
		if(options[i].checked)
		{
			feedbacks[j] = $("#op"+i).html();
			j++;
		}
	}
	return feedbacks;
	}
  
function saveFeedbackWithAjax(){
	var feedbacks = getFeedBacks(); 
	var length = feedbacks.length;
	
	var comments = CommonUtil.getValidString($('#comment').val());
	if (length ==0 && ""==comments){
		CommonUtil.showAlert("", I18NHelper["poi.feedback.selectFeedback"], I18NHelper["common.button.OK"]);
		return false;
	}
	var feedbackData = {
		"searchCatName": CommonUtil.getValidString(GLOBAL_parameter["searchCatName"]),
		"searchKeyword": CommonUtil.getValidString(GLOBAL_parameter["searchKeyword"]),
		"searchLocation": CommonUtil.getValidString(GLOBAL_parameter["searchLocation"]),
		"feedbackPage": CommonUtil.getValidString(GLOBAL_parameter["feedbackPage"]),
		"feedbackQuestion": CommonUtil.getValidString($('#questionContent').html()),
		"feedbacks": JSON.stringify(feedbacks),
		"comment": comments
	};
	
	console.log(feedbackData);
	var ajxUrl = GLOBAL_hostUrl + "poiListFeedbackSave.do?jsonStr=" + JSON.stringify(feedbackData) + "&" + CommonUtil.getClientInfo();
	console.log("url................."+ajxUrl);
	var ajaxOptions = {
			data:0,
			loadingStyle:1,
			url:ajxUrl,
			onSuccess:ajaxCallBackFeedback
	};
	CommonUtil.ajax(ajaxOptions);
}

function ajaxCallBackFeedback(text)
{
	CommonUtil.showAlert(I18NHelper["common.thankyou"], I18NHelper["poi.feedback.submitSuccess"], I18NHelper["common.button.OK"], "onClickOK");
}

function changeImg(id){
	var optionId = "option"+id;
	var option =  document.getElementById(optionId);
	if(option.checked){
		document.getElementById("image"+id).className = "check_box_unfocused";
		option.checked = false;
	}else{
		document.getElementById("image"+id).className = "check_box_focused";
		option.checked = true;
	}
}

function onClickOK()
{
	PopupUtil.hide();
	//go back
	SDKAPI.goBack(true);
}


function highLightOptionItem(id){
	dishighLightOptionItem(GLOBAL_previewHighLightItemId);
	GLOBAL_previewHighLightItemId = id;
	var elementId = "optionDiv"+id;
	var element = document.getElementById(elementId);
	highLightItem(element);
}

function dishighLightOptionItem(id){
	var elementId = "optionDiv"+id;
	var element = document.getElementById(elementId);
	dishighLightItem(element);
}