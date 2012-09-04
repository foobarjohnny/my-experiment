
/**
* To make phonegap available, the Device object should be set by device info,this function is to obtain device info from server.
*/
//TODO
function DeviceInfoNativeCallRequest(){
	var params = {
		callbackId:"deviceinfo11111",
		Service:"Device.getDeviceInfo",
		Parameters:"[]"
	};
	var strParams = JSON.stringify(params);
	//var _url_ = "http://172.16.202.245:8080/testiframe/deviceinfo.js";
	var _url_ = "http://localhost:8088/services/localapp?parameters="+strParams+"&callback=?";
	console.log(_url_);
	$.getJSON(_url_,function(args){	
		if (args.status == PhoneGap.callbackStatus.OK) {	
			extraIconClickResponse(args.message);
		}else{
			alert("Cannot get device info now!");
		}
	//	extraIconClickResponse(args);		
	});

}
//TODO
//this function will be called after receving response about device info from server  
function extraIconClickResponse(JSONinfo){
	DeviceInfo.available = true;
	DeviceInfo.platform = JSONinfo.platform;
	DeviceInfo.version = JSONinfo.version;
    DeviceInfo.uuid = JSONinfo.uuid;
	DeviceInfo.phonegap =JSONinfo.phonegap;
	
	PhoneGap.queue.ready = true;
	PhoneGap.available = true;
}

/**
* This function is to send an ajax request of function call
* @param {string} cbid indicate the id of callback function that should be invoked 
* @param {string} tnservice indicate the name of tnservice
* @param {Object} tnopt indicate parameters passed to the service
*/
function localAppNativeCallRequest(cbid,tnservice,tnopt){
	var params = {
		CallbackId:cbid,
		Service:tnservice,
		Parameters:tnopt
	};
	var strParams = JSON.stringify(params);
	var url = "http://localhost:8088/services/localapp?parameters="+strParams+"&callback=?";
	console.log(url);
	try{
		$.getJSON(url,function(args){
			if (PhoneGap.callbacks[params.CallbackId]) {
				if (args.status == PhoneGap.callbackStatus.OK) {	
					PhoneGap.callbackSuccess(params.CallbackId,args);
				}else {
					PhoneGap.callbackError(params.CallbackId, args);
				}
		/*		if (!args.keepCallback) {
					delete PhoneGap.callbacks[params.CallbackId];
				}*/
			}
		});
	}catch(e){
		console.log("Error:" + e);
	}
	PhoneGap.queue.ready = true;

}