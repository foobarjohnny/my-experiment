function saveBuyTicketUsedHeight(data)
{
	sessionStorage.setItem("SESSION_STORAGE_USED_HEIGHT",data);
}

function getBuyTicketUsedHeight()
{
	return sessionStorage.getItem("SESSION_STORAGE_USED_HEIGHT");
}

function changeBuyButtonCSS(afterGetTicket){
	var usedHeight = 0;
	if(!afterGetTicket)
	{
		usedHeight = getBuyTicketUsedHeight();
		if(null == usedHeight)
		{
			usedHeight = 0;
		}
	}
	
	if(usedHeight == 0 || afterGetTicket)
	{
		var bottomPartHeight = $("#bottomPartDiv").outerHeight();
		var topHeightHeight = $("#titleBarDiv").outerHeight() + $("#infoBarDiv").outerHeight() + $("#blankBarDiv").outerHeight();
		var ticketQuantityDivHeight;
		if($("#ticketQuantityGroup").css("display") == "block"){
			ticketQuantityDivHeight = $("#ticketQuantityGroup").outerHeight();
		}else{
			ticketQuantityDivHeight = 0;
		}
	
		usedHeight = topHeightHeight + bottomPartHeight + ticketQuantityDivHeight;
		saveBuyTicketUsedHeight(usedHeight);
	}
	var windowHeight = document.body.clientHeight;
	var gap = "0";
	if(windowHeight > usedHeight){
		gap = windowHeight-usedHeight;
	}
	$("#bottomPartDiv").css("top",gap + "px");
}

function placeAnOrder(){
	SDKAPI.getSSOToken(getOrderId);
}

function fetchPriceInfo(){
	SDKAPI.getSSOToken(fetchPrices);
}

function getOrderId(ssoToken){
	//check Email
	var email = $("#email").val();
	if(!email||email==""){
		CommonUtil.showAlert("",I18NHelper["email.empty"],I18NHelper["button.ok"]);
		return;
	}
	if(!CommonUtil.emailAddressCheck(email)){
		CommonUtil.showAlert("",I18NHelper["email.invalid"],I18NHelper["button.ok"]);
		return;
	}
	setConfirmEmailCache(email);

	var showTime = trunkShowTime(getMovieTime());
	var showDate = getSearchDateTextColon();
	var ticketURI = JSON.parse(getCurrentSchedule()).ticketURI;
	//var json = {"movieId":"136699","theaterId":"AACFQ","showDate":"2011:4:7","showTime":"21:40"};
	var json = getSearchQueryForGetTickets(ticketURI, showDate,showTime);

	var movieId = json.movieId;
	var theaterId = json.theaterId;
	var totalAmount = $("#totalAmountHidden").val();
	var bookingInfo = new BookingTicketInfoItem(ticketList,theaterId,movieId,showDate,showTime, email,totalAmount);

	var jsonStr = JSON.stringify(bookingInfo);
	var ajxUrl = GLOBAL_hostUrl + "BookTicket.do?jsonStr=" + JSON.stringify(bookingInfo) + "&ssoToken=" + ssoToken;
	var ajaxOptions = {
			loadingStyle:1,
			url:ajxUrl,
			onSuccess:ajaxCallBackOrderId
	};
	CommonUtil.ajax(ajaxOptions);
}

function ajaxCallBackOrderId(result){
    if(CommonUtil.isEmptyStr(result)){
    	CommonUtil.showAlert("",I18NHelper["buyticket.bookingFailed"],I18NHelper["button.ok"]);
    	return;
    }

    CommonUtil.debug("bookingId:"+JSON.stringify(result));
    fireCheckout(result);
}

function fetchPrices(ssoToken){
	var ticketURI = JSON.parse(getCurrentSchedule()).ticketURI;
	if(CommonUtil.isEmptyStr(ticketURI)){
		CommonUtil.showAlert("", I18NHelper["buyticket.noticket"], I18NHelper["button.ok"], "SDKAPI.goBack(false)");
		return;
	}

	var showTime = trunkShowTime(getMovieTime());
	var showDate = getSearchDateTextColon();
	//var json = {"movieId":"136699","theaterId":"AACFQ","showDate":"2011:4:7","showTime":"21:40"};
	var json = getSearchQueryForGetTickets(ticketURI, showDate,showTime);

	var ajxUrl = GLOBAL_hostUrl + "GetTicketQuantity.do?jsonStr=" + JSON.stringify(json) + "&ssoToken=" + ssoToken;
	CommonUtil.debug("[get Ticket URL] "+ajxUrl);
	var ajaxOptions = {
			loadingStyle:1,
			url:ajxUrl,
			onSuccess:ajaxCallBackPrices
	};
	CommonUtil.ajax(ajaxOptions);
}

function ajaxCallBackPrices(result)
{
    $("#convenienceFeeHidden").val(result.convenienceCharge);
    $("#surchargeHidden").val(result.surcharge);
    $("#surcharge").html("$"+formatNumber(result.surcharge,2));

    $("#convenienceFee").html("$"+formatNumber(0,2));
    $("#surcharge").html("$"+formatNumber(0,2));

    var tickets = result.ticketList;
    var count=0;
    for(var i in tickets){

	   var ticket =new TicketItem(tickets[i].ticketId, tickets[i].type, tickets[i].price, tickets[i].currency,tickets[i].quantity);
	   ticketList[count]=ticket;
	   totalList[count]= 0;
	   //alert("id: "+tickets[i].id+" type:"+tickets[i].type+" price:"+ tickets[i].price+" currency:"+ tickets[i].currency+"  quantity:"+tickets[i].quantity)
	   count+=1;
    }
    displayTicketData();
	changeHtml();
	changeBuyButtonCSS(true);
}

function add(index){
	ticketList[index].quantity += 1;	//quantity

	if(ticketList[index].quantity>0){
		$("#subImg"+index).attr("class", "clsPlusSubIcon clsSubtractIconUnfocused");
	}

	totalList[index] = ticketList[index].quantity * (ticketList[index].price*100)/100;//total money of some type of ticket
	changeHtml();
}

function sub(index){
	if(ticketList[index].quantity>0){
		ticketList[index].quantity-=1;	//quantity

		if(ticketList[index].quantity<=0){
			$("#subImg"+index).attr("class", "clsPlusSubIcon clsSubtractIconDisabled");
		}
		 
		totalList[index] = ticketList[index].quantity * (ticketList[index].price*100)/100;//total money of some type of ticket
		changeHtml();
	}
}

function changeHtml(){
	var totalMoney = 0;

	var totalSurcharge = 0;
	var totalConvenienceFee = 0;
	var totalTicketQuantity = 0;

	for(var i=0; i<ticketList.length;i++ ){

		document.getElementById("quantityOf_"+i).innerHTML = ticketList[i].quantity;
		document.getElementById("totalOf_"+i).innerHTML = "$"+formatNumber(totalList[i],2);
		totalMoney += totalList[i];

		totalTicketQuantity += ticketList[i].quantity;
	}

	//when total is 0.00, then disable buy button
	if(formatNumber(totalMoney,2) == formatNumber(0,2)){
		$("#buyButton").attr("disabled","disabled");
		$("#buyButton").attr("class","ticketBuyButton ticketBuyButtonDisabledBg");
		$("#buyButton").attr("ontouchstart","");
		$("#buyButton").attr("ontouchend","");
		$("#buyButton").attr("ontouchmove","");
	}
	else{
		buyButton.disabled=false;
		$("#buyButton").attr("class","ticketBuyButton clsFontColor_unfocusedBuyBT clsSmallRadius clsButtonBgNormal");
		$("#buyButton").attr("ontouchstart","hightLightBuyButton(this)");
		$("#buyButton").attr("ontouchend","disHightLightBuyButton(this)");
		$("#buyButton").attr("ontouchmove","disHightLightBuyButton(this)");
	}

	totalSurcharge = $("#surchargeHidden").val()*100*totalTicketQuantity/100;
	totalConvenienceFee = $("#convenienceFeeHidden").val()*100*totalTicketQuantity/100;

	totalMoney += totalSurcharge*100/100;
	totalMoney += totalConvenienceFee*100/100;
	document.getElementById("totalMoney").innerHTML = "$"+formatNumber(totalMoney,2);
	document.getElementById("convenienceFee").innerHTML = "$"+formatNumber(totalConvenienceFee,2);
	document.getElementById("surcharge").innerHTML = "$"+formatNumber(totalSurcharge,2);
	$("#totalAmountHidden").val(totalMoney);

}

function hightLightBuyButton(element)
{
	highlightBtnAll(element,"clsFontColor_unfocusedBuyBT","clsFontColor_focusedBuyBT");
}

function disHightLightBuyButton(element)
{
	disHighlightBtnAll(element,"clsFontColor_focusedBuyBT","clsFontColor_unfocusedBuyBT");
}

function formatNumber(num,exponent) {
  	if (exponent<1){
  		return num;
  	}
  	var str = num.toString();
  	if (str.indexOf(".") != -1) {
  		var strs = str.split(".");
    	if (strs[1].length >= exponent) {
     		return str;
     	} else {
     		return formatNumber(str+"0",exponent);
    	}
  	} else {
    	return formatNumber(str+".0",exponent);
 	}
}

function showMovieInfo(){
	var movieItem = JSON.parse(getCurrentMovie());
	document.getElementById("movieName").innerText = movieItem.name;

	var theaterItem = JSON.parse(getCurrentTheater());
	document.getElementById("address").innerHTML = theaterItem.addressDisplay;

	document.getElementById("theaterName").innerText = theaterItem.name;

	var scheduleItem = JSON.parse(getCurrentSchedule());

	var movieTime = getMovieTime();
	document.getElementById("time").innerHTML = "&nbsp;&nbsp;&nbsp;"+convertTimeFormatToDisplay(movieTime);

	var scheduleDate = getScheduleDate();
	document.getElementById("date").innerHTML = scheduleDate.format("mmm d, yyyy"); 
	// set the Email user used last time
	var confirmEmail = getConfirmEmailCache();
	if(confirmEmail){
		$("#email").val(confirmEmail);
	}

}

function getRequest()
{
	var url = location.search;
	var theRequest = new Object();
	if(url.indexOf("?") != -1)
	{
		var str = url.substr(1);
		strs = str.split("&");
		for(var i = 0; i < strs.length; i ++)
		{
			theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
		}
	}
	return theRequest;
}

function TicketItem(id, type, price, currency,quantity)
{
	this.id  =  id;
	this.type = type;
	this.price = price;
	this.currency = currency;
	this.quantity =  0;
}

function BookingTicketInfoItem(ticketArray,theaterId,movieId,showDate,showTime,confirmEmail,totalAmount ){
	this.ticketArray = ticketArray;
	this.theaterId = theaterId;
	this.movieId = movieId;
	this.showDate = showDate;
	this.showTime = showTime;
	this.confirmEmail = confirmEmail;
	this.totalAmount = totalAmount;
}

function trunkShowTime(time){
	var timeList = time.split(":");
	var result = timeList[0]+":"+timeList[1];
	return result;
}

//ticketURI  https://mobile.fandango.com/tms.asp?a=11872&m=101041&t=AAPOT&d=2011-04-04
function getSearchQueryForGetTickets(ticketURI,showDate, showTime){
	var movieId;
	var theaterId;

	if(!ticketURI||ticketURI==""){
		return null;
	}

    if(ticketURI.indexOf('?') == -1)
    	return null;

    var temp = ticketURI.substring(ticketURI.indexOf('?')+1, ticketURI.length);
    var paraArray = new Array(3);
    var pairs = temp.split("&");
    if(pairs.length!=3)
    	return null;

     for(i=0;i<pairs.length;i++){
    	var keyAndValue = pairs[i].split("=");
    	if(keyAndValue[0]=="m")
    		movieId = keyAndValue[1];
    	else if(keyAndValue[0]=="t"){
    		theaterId = keyAndValue[1];
    	}
    	else{
        }
    }

	var json = {"movieId":movieId,"theaterId":theaterId,"showDate":showDate,"showTime":showTime};
	return json;
}

function isFlip(){
	var newWidth = document.documentElement.clientWidth;
	if(GLOBAL_currentWidth == newWidth){
		return false;
	}else{
		GLOBAL_currentWidth = newWidth;
		return true;
	}
}

function fireCheckout(orderId){

	CommonUtil.debug("orderId:"+orderId);
	PassThrough.checkout(
			orderId, // order id, BK-2c90a8cf-761ce55a-0000
			I18NHelper["checkout.purchaseFor"] + " " +$("#movieName").html(), // order description
			function(data){
				displayConfirmation(data);
			}); // callback
}

function displayConfirmation(data){
	//var result = data.
	$("#ticketQuantityGroup").hide();
	$("#bottom").hide();
	var innerHtml = data.bookingResult;
	$("#bookingInfoDiv").show();
	$("#bookingInfoDiv").html(innerHtml);
}

function displayTicketData(){
	var pageText = "";
	if(ticketList.length<=0){
		CommonUtil.showAlert("",I18NHelper["buyticket.noticket.schedule"],I18NHelper["button.ok"], "SDKAPI.goBack(false)");
	}else{
		for(var i=0; i<ticketList.length;i++ ){

			var style = "";
			if(ticketList.length==1){
			    style = "clsticketitembg";
			}else if(i==0){
			    style = "clsticketitembgtop";
			}else if(ticketList.length==1+i){
			    style = "clsticketitembgbottom";
			}else{
			    style = "clsticketitembgmiddle";
			}
			pageText+="<div id=\"ticketQuantityResult\" class='div_table "+style+" '>"
				+"<div style='width: 5%;' class='div_cell ticketquantityrightstyle' onClick=\"sub('"+i+"')\"><div id='subImg"+i+"' class='clsPlusSubIcon clsSubtractIconDisabled'></div></div>"
				+"<div style='width: 8%;' id='quantityOf_"+i+"' class='div_cell ticketquantitycenterstyle fs_middle' ></div>"
				+"<div style='width: 10%;' class='div_cell' onClick=\"add(" + i + ")\"><div class='clsPlusSubIcon clsPlusIconUnfocused'></div></div>"
				+"<div style='width: 40%; text-align: left;' class='div_cell ticketquantitystyle fs_middle'>"+ticketList[i].type+"<br/>"+"<span class='clsPrompt fs_small'>($"+ticketList[i].price+")"+"</span></div>"
				+"<div style='width: 25%; text-align: right;'  id='totalOf_"+i+"' class='div_cell ticketquantitystyle fs_middle'></div>"
				+"</div>";

		}
		//$("#ticketQuantitySubDiv").prepend(pageText);
		var newText = pageText + $("#ticketQuantitySubDiv").html();
		$("#ticketQuantitySubDiv").html(newText);
		$("#ticketQuantityGroup").css("display","block");
	}
}

function APPSTORE_API_lockScreen(){
	PopupUtil.showLoading();
}

function APPSTORE_API_unLockScreen(){
	PopupUtil.hide();
}
