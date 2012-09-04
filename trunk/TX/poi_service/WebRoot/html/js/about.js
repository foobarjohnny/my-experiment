$(document).ready(function() {
	CommonUtil.fetchClientInfoFromUrl();
	document.addEventListener("deviceready", onDeviceReady, false);
	if($("#dataSet").html() == "TomTom"){
		$("#copyRight").show();
	}
	if(CommonUtil.isIphone()||ClientInfo.version.match("^7.2")){
		$("#versionNo").html("");
	}
	if(ProgramConstants.ATTNAVPROG==ClientInfo.programCode){
		$("#poweredBy").hide();
	}
});

function onDeviceReady() {
	if(CommonUtil.isIphone()||ClientInfo.version.match("^7.2")){
		SDKAPI.getPreference(setVersion,"internalVersion");
		//SDKAPI.getSSOToken(getDataSet);
		//getDataSet();
	}
}

function setVersion(version){
	if(version){
		$("#versionNo").html("["+version+"]");
	}
}