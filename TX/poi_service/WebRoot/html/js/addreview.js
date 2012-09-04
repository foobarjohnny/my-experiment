/**
 * steps:
 * 1. initialize: fill poi name, address and so on;
 * 2. fetch review options and display: if could get this from cache, skip;
 * 3. fetch user name, if user name is null, display user name input
 */
var GLOBAL_optons,Global_needSaveNickName2Client = true;
/**
 * 1. initialize
 */
function initAddReview(){
	//1
	var poiObj = getSimplePoiObject();
	$("#name").html("<b>"+poiObj.brand+"</b>");
	var address=getPoiAddressDisplay(poiObj.stop);
	$("#address").html(address);
	//2
	fetchReviewOptions();
}

function getSimplePoiObject(){
	return JSON.parse(CommonUtil.getFromCache(PoiCacheKeys.SIMPLEPOI));
}

function getPoiAddressDisplay(stop){
	//"3041 Mckee Rd San Jose, CA 95127"
	return stop.firstLine + " " + stop.city + ", " + stop.province + " " + stop.zip;
}
//3
function onDeviceReady() {
	SDKAPI.getPreference(afterGetNickNameFromClient,PreferenceConstants.NICKNAME);
}
/**
 * 2. fetch review options and display
 */
//2.1 fetch review options
function fetchReviewOptions(){
	GLOBAL_optons = JSON.parse(PoiCacheHelper.getPoiReviewOptionCache());
	if(GLOBAL_optons){
		displayReviewOptions(GLOBAL_optons);
	}else{
		var poiObj = getSimplePoiObject();;
		var reviewSearchCriteria = {
				"poiId":poiObj.poiId,
				"categoryId":poiObj.categoryId
				};

		var ajxUrl = GLOBAL_hostUrl + "poireview.do?operateType=getReviewOptions&jsonStr=" + JSON.stringify(reviewSearchCriteria) + "&" + CommonUtil.getClientInfo();
		var ajaxOptions = {
				url:ajxUrl,
				onSuccess:ajaxCallBackGetReviewOptions
		};
		
		CommonUtil.ajax(ajaxOptions);
	}
}

function ajaxCallBackGetReviewOptions(returnValue){
	GLOBAL_optons = returnValue;
   	displayReviewOptions(GLOBAL_optons); 	
}
//2.2 display review option
function displayReviewOptions(reviewOptionList){         
     //begin
   	var reviewOptionListSize = reviewOptionList.length;  
   	var pageText ="",css="div_cell reviewOptionGap"; 
   	for(var i=0;i<reviewOptionListSize;i++)
   	{
   		pageText+="<div class=\"div_row\"><div class=\""+css+"\"><span class='fs_veryLarge fc_gray fw_bold'>"+reviewOptionList[i].name+"</span><br>";
		pageText+="<input type='hidden' id='isLike"+reviewOptionList[i].id+"' value='-1'></div>";
		
		var down = getBadScoreText(reviewOptionList[i].id);	
		var up = getGoodScoreText(reviewOptionList[i].id);
		pageText+="<div class=\""+css+"\" style=\"width: 20%;\" id='"+reviewOptionList[i].id+"Down'>"+down+"</div><div class=\""+css+"\" style=\"width: 20%;\" id='"+reviewOptionList[i].id+"Up'>"+up+"</div></div>";	
   	}
   $("#reviewOptionList").html(pageText);
      //end
}

function getBadScoreText(type)
{
    var typeDownSelected=type+"DownSelected";
    var typeDownUnselected=type+"DownUnselected";
	
	var down="<a onclick='score("+type+",0)'>"
			+"<img id='"+typeDownUnselected+"' class='evaluation bad_button_unselected'/>"
			+"<img id='"+typeDownSelected+"' class='evaluation bad_button_selected' style='display:none;'/>"
			+"</a>";
	return down;
}

function getGoodScoreText(type)
{
    var typeUpSelected=type+"UpSelected";
    var typeUpUnselected=type+"UpUnselected";
    
	var up = "<a onclick='score("+type+",1)'>"
				+"<img id='"+typeUpUnselected+"' class='evaluation good_button_unselected'/>"
				+"<img id='"+typeUpSelected+"' class='evaluation good_button_selected'  style='display:none;' />"				
				+"</a>";
	return up;
}

function score(type,isLike)
{
        var typeDown=type+"Down";
	    var typeUp=type+"Up";
	    
		   if(isLike==0){//-1:default,0:dislike,1:like
			   document.getElementById(typeDown).childNodes[0].setAttribute("onClick", "score("+type+",-1)");
			   $("#"+type+"DownSelected").show();
			   $("#"+type+"DownUnselected").hide();
			   $("#" + typeUp).html(getGoodScoreText(type));
		   }else if(isLike==1){
			   	document.getElementById(typeUp).childNodes[0].setAttribute("onClick", "score("+type+",-1)");
			    $("#"+type+"UpSelected").show();
			    $("#"+type+"UpUnselected").hide();
			    $("#" + typeDown).html(getBadScoreText(type));
               
		   }else{
			   $("#" + typeUp).html(getGoodScoreText(type));
			   $("#" + typeDown).html(getBadScoreText(type));
		   }
		   
		   $("#isLike"+type).val(isLike);
}

/**
 * 3. fetch user name
 */
function afterGetNickNameFromClient(name){
	//check if nick name exist
	var nickName = CommonUtil.getValidString(name);
	if(nickName == ""){   
		$("#userNameInputDiv").show();
	}else{
		Global_needSaveNickName2Client = false;
		PoiCacheHelper.setNickName(nickName);
		$("#userNameInput").val(nickName);
	}		
}



function addReview(){
	var rating= $("#totalRating").val(); 
	if(rating == null || rating == "0"){
		CommonUtil.showAlert("", I18NHelper["addreview.no.rating"], I18NHelper["common.button.OK"] ,"closePopup");
		return false;
	}
	
	var nickName = $("#userNameInput").val();
	if(!nickName){
		$("#userNameInput").val(I18NHelper["nickname.title"]);
		cssHelper.replaceObjCSSById("userNameInput", "fc_black", "fc_red");
		cssHelper.replaceObjCSS("userNameInput", "fst_normal", "fst_italic");
		return false;
	}
	if($("#userNameInput").val() == I18NHelper["nickname.title"]) {
		return false;
	}
	if(Global_needSaveNickName2Client){
		PoiCacheHelper.setNickName(nickName);
		SDKAPI.setPreference(function(){},PreferenceConstants.NICKNAME,nickName);
	}
	SDKAPI.getSSOToken(processReview,false);
}

function processReview(ssoToken)
{
	var poiObj = getSimplePoiObject();
	var reviewerName = PoiCacheHelper.getNickName();
	reviewerName = CommonUtil.toJSONString(reviewerName);
	reviewerName = encodeURIComponent(reviewerName);
	var rating= $("#totalRating").val(); 
	var comment= $("#comment").val(); 
	comment = CommonUtil.toJSONString(comment);
	comment = encodeURIComponent(comment);
	
	var length = GLOBAL_optons.length;
	var ratingOptions = new Array(length);
	
	if(GLOBAL_optons!=null){
		for(var i=0;i<length;i++)
		{
			var item = GLOBAL_optons[i];
			var id = "isLike" + item.id; 
			var isLikeValue = document.getElementById(id).value;
			ratingOptions[i] = {	'id':	item.id,
					  			 	'name':	item.name,
					  			 	'value':isLikeValue
					  			 };
		}
	}  
    
    var ratingStr = JSON.stringify(ratingOptions);
	var addReview = {
				//"ssoToken":ssoToken,
				"reviewerName":reviewerName,
				"poiId":poiObj.poiId,
				"categoryId":poiObj.categoryId,
				"rating":rating,
				"comments":comment,
				"ratingProperties":ratingStr
				};
	//scrollPageToTop();
	var ajxUrl = GLOBAL_hostUrl + "poireview.do?operateType=submit&jsonStr=" + JSON.stringify(addReview) + "&" + CommonUtil.getClientInfo();
	var ajaxOptions = {
			data:0,
			loadingStyle:1,
			url:ajxUrl,
			onSuccess:createAjaxCallBackReview
	};
	if (navigator.onLine) {
		CommonUtil.ajax(ajaxOptions);
	}else{
		CommonUtil.noNetworkError();
	}
}

function createAjaxCallBackReview(responseText){
	var result =  JSON.parse(responseText);
	var poiObj = getSimplePoiObject();;
	PoiCacheHelper.setPoiReviewCacheForAddReview(responseText);
	PoiCacheHelper.removePoiReviewCacheKeyForAddReview();
	refreshPoiList();
}

function refreshPoiList()
{
	try
	{
		function onsuc(pre){	
			backToPoiDetail();
		}
		function onerr(){
			CommonUtil.debug("doPrivateService error");
			backToPoiDetail();
		}
		
		var refreshObj = {"isNeedRefresh":true};
		navigator.tnprivateservice.doPrivateService("Refresh",refreshObj,onsuc,onerr);
	}
	catch(e)
	{
		CommonUtil.debug(e);
		backToPoiDetail();
	}
}

function backToPoiDetail()
{	
	SDKAPI.goBack();
}

function cancelReview()
{
	backToPoiDetail();
}

function lightStar(stars){
	var starId;
   for(var i=1; i<=stars; i++){ 
	   starId = "#lightStar" + i;
	   $(starId).attr("class","full_huge_star_icon_unfocused");
	   } 
 
	   for(var i=stars+1; i<=5; i++){
		   starId = "#lightStar" + i;
      $(starId).attr("class","vacancy_huge_star_icon_unfocused");
   }
   $("#totalRating").val(stars);
}
function closePopup(){
	PopupUtil.hide();
}

function onclickUserNameInput(obj){
	if(Global_needSaveNickName2Client){
		if(obj.value == I18NHelper["nickname.title"]){
			obj.value = "";
			cssHelper.replaceObjCSS(obj, "fc_red", "fc_black");
		}
		cssHelper.replaceObjCSS(obj, "fst_italic", "fst_normal");
	}
}

function onBlurUserNameInput(obj){
	if(Global_needSaveNickName2Client){
		if(!obj.value){
			cssHelper.replaceObjCSS(obj, "fst_normal", "fst_italic");
		}
	}
}
/*
function closeNickNamePopup()
{
	PopupUtil.hide();
}

function onClickCancelNickName()
{
	window.scrollTo(0,0);
	closeNickNamePopup();
}

function afterSetNickNameFromClient()
{	
	SDKAPI.getSSOToken(processReview);
}

function onClickSaveNickName()
{
	var nickName = $("#nicknameinput").val();
	if(nickName!=null && nickName!=""){
		//if this nickName has exist
		PoiCacheHelper.setNickName(nickName);
		//SDKAPI.getSSOToken(checkNickName);comment by jiangxl 2012.05.31: TNCSERVER-5038 allow the user to enter any user name there.
		closeNickNamePopup();
		SDKAPI.setPreference(afterSetNickNameFromClient,PreferenceConstants.NICKNAME,nickName);
	}
}
*/