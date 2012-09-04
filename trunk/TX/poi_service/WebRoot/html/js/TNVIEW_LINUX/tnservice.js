/**
*@fileoverview This file describes  a list of TN services that can be used by JavaScript.
* For Navigation and Map display, you need to define an object with specified format.
*/
/**
 * Construct a new TNService object
 * @class This is the basic TNService class
 * @description This class provides access to telenav services, such as map,navigation,etc.
 * @constructor
 * @see Poi 
 * @see Address
 * @see Stop 
 */
var TNService= function(){
	//this.onSuccess = new Object();
	//this.onFail = new Object();
};
/**
 * @class Contains Address data.
 * @description This object is returned by an TNService method.
 * @property {Number} id 
 * @property {Number} type 
 * @property {Number} phoneNumber 
 * @property {String} label 
 * @property {Number} status
 * @property {String} sharedFromPTN
 * @property {String} sharedFromUser
 * @property {Array} category This parameter must be an Array of Number values 
 * @property {Object} stop
 * @property {Object} poi
 */
function Address(id,selectedIndex,type,phoneNumber,label,status,sharedFromPTN,sharedFromUser,category,stop,poi){
	this.id = id;
	this.selectedIndex = selectedIndex;
	this.type = type ;
	this.phoneNumber = phoneNumber;
	this.label = label;
	this.status = status;
	this.sharedFromPTN = sharedFromPTN;
	this.sharedFromUser = sharedFromUser;
	this.stop = stop;
	this.category = category;
	this.poi = poi;

}

/**
* @class Contains Stop data.
* @property {Number} type
* @property {Number} lat
* @property {Number} lon
* @property {String} firstLine
* @property {String} lastLine
* @property {Number} status
* @property {Number} stopId
* @property {String} city
* @property {String} zip
* @property {String} country
* @property {Boolean} isGeocoded
* @property {String} province
* @property {String} streetName
* @property {String} streetNumber
* @property {String} label
* @property {String} crossStreet
*/
function Stop(type,lat,lon,firstLine,lastLine,status,stopId,city,zip,country,isGeocoded,province,streetName,streetNumber,label,crossStreet){
	this.type = type;
	this.lat = lat;
	this.lon = lon;
	this.firstLine = firstLine;
	this.lastLine = lastLine;
	this.status = status;
	this.stopId = stopId;
	this.city = city;
	this.zip = zip;
	this.country = country;
	this.isGeocoded = isGeocoded;
	this.province = province;
	this.streetName = streetName;
	this.streetNumber = streetNumber;
	this.label = label;
	this.crossStreet = crossStreet;	
}
/**
* @class Contains poi data.
* @property {Boolean} rated
* @property {Number} rating
* @property {Number} rateNumber
* @property {Number} popularity
* @property {Number} type
*/
function Poi(rated,rating,rateNumber,popularity,type){
	this.rated = rated;
	this.rating = rating;
	this.rateNumber = rateNumber;
	this.popularity = popularity; 
	this.type = type;
}

/**
 * This funtion provides access for local apps to capture address using native client user interface. 
 * The resulting objects are passed to the successCallback callback function specified by the successCallback parameter. 
 * @param {String} callbackurl This specifies the identifier for returning back to your application 
 * 	<h4>callbackurl is in the following format, for example</h4>
 * 	<p>url?lat&lon&isSharedAddress&label&firstLine&city&province&id&postalCode&country</p>
 * @param {function} successCallback The callback that is called with the captured address.
 * @param {function} errorCallback The callback that is called if there was an error
 */
TNService.prototype.captureAddress = function(callbackurl,successCallback, errorCallback){
	//this.onSuccess = successCallback;
	//this.onFail = errorCallback;
	PhoneGap.exec("AddressService.getAddress", successCallback, errorCallback, [callbackurl]);	
};

/**
 * This funtion provides access for local apps to capture address book phone number/name,etc via native client user interface.
 * The resulting objects are passed to the successCallback callback function specified by the successCallback parameter. 
 * @param {String} callbackurl This specifies the identifier for returning back to your application
 * <h4>callbackurl is in the following format, for example</h4>
 * <p>url?displayName&phoneNumber&emails&address</p>
 * @param {function} successCallback The callback that is called with the address selected from AddressBook 
 * @param {function} errorCallback The callback that is called if there was an error
 */
TNService.prototype.getAddressBook = function(callbackurl,successCallback, errorCallback){
	//this.onSuccess = successCallback;
	//this.onFail = errorCallback;
	PhoneGap.exec("AddressService.getAddressBook",successCallback, errorCallback, [callbackurl]);		
};

/**
 * This funtion allows web users to set address value of special type.
 * @param {String} addressType This specifies the address type,there are two candidates now, that is [work, home]
 * @param {String} address The callback that is called with the address selected from AddressBook 
 * @param {function} successCallback The callback that is called with the address selected from AddressBook 
 * @param {function} errorCallback The callback that is called if there was an error 
 */
TNService.prototype.setAddress = function(addressType,address,successCallback, errorCallback){
	//this.onSuccess = successCallback;
	//this.onFail = errorCallback;
	PhoneGap.exec("AddressService.setAddressByType",successCallback, errorCallback,[addressType,address]);		
};
/**
 * This funtion provides access for local apps to get address of special type and results are passed to the successCallback callback function specified by the successCallback parameter. 
 * @param {String} addressType This specifies the address type,there are two candidates now, that is [work, home]
 * @param {function} successCallback The callback that is called with the address selected from AddressBook 
 * @param {function} errorCallback The callback that is called if there was an error
 */
TNService.prototype.getAddress = function(addressType,successCallback, errorCallback){
	//this.onSuccess = successCallback;
	//this.onFail = errorCallback;
	PhoneGap.exec("AddressService.getAddressByType",successCallback, errorCallback,  addressType);		
};
/**
* @description This function is to get home address specified by users.
* @param {function} successCallback The callback that is called with home address
* @param {function} errorCallback The callback that is called if there was an error
*/
TNService.prototype.getHomeAddress = function(successCallback, errorCallback) {
	//this.onSuccess = successCallback;
	//this.onFail = errorCallback;
	PhoneGap.exec("AddressService.getHomeAddress", successCallback, errorCallback);
}
/**
*@description This function is to save log information through MIS logging system .
*@param {Number} LogEventType 
*@param {Object} LogEventValue
*@param {function} successCallback The callback is invoked after a TNService object has invoked log event.
*@param {function} errorCallback It provides an error message
*
*/
TNService.prototype.logEvent = function(LogEventType,LogEventValue, successCallback, errorCallback) {
	//this.onSuccess = successCallback;
	//this.onFail = errorCallback;
	PhoneGap.exec("LogEventService.logEvent",successCallback, errorCallback,[LogEventType,LogEventValue]);
}
/** 
 * This function is used  to display map and specified address
 * @param {object} address It specifies the place that will be displayed on the map
 * @param {String} callbackurl This specifies the identifier for returning back to your application
 * @param {function} successCallback It will be invoked if map has been displayed
 * @param {function} errorCallback If there is an error this function will be called
 * @example
 * var oriaddr = "";
 * var stop1 = new Stop(0,34.19568,-118.34892);
 * var category1 = new Array("bmw");
 * var desaddr = new Address(0,"","","","","","",category1,stop1);
 * var callbackurl = ""
 * function onsuccess(){
 * }
 * function onerror(){
 * }
 * navigator.tnservice.displayMap(address, callbackurl, onsuccess, onerror);
 */
TNService.prototype.displayMap = function(address,callbackurl,successCallback, errorCallback){
	//this.onDispSuccess = successCallback;
	//this.onFail = errorCallback;
	PhoneGap.exec("MapService.doMap", successCallback, errorCallback,[address,callbackurl]);
};
/** 
 * This function is to start navigation
 * @param {object} origAddr The start address of navigation {defaultValue:""}
 * @param {object} destAddr Destination of the navigation
 * @param {String} cburl  This specifies the identifier for returning back to your application
 * @param {function} successCallback The callback is invoked after a TNService object has invoked navigation .
 * @param {function} errorCallback It provides an error message 
 * @example
 * var oriaddr = "";
 * var stop1 = new Stop(0,34.19568,-118.34892);
 * var category1 = new Array("bmw");
 * var desaddr = new Address(0,"","","","","","",category1,stop1);
 * var callbackurl = ""
 * function onsuccess(){
 * }
 * function onerror(){
 * }
 * navigator.tnservice.navTo(oriaddr, desaddr, callbackurl, onsuccess, onerror);
 */
TNService.prototype.navTo = function(origAddr,destAddr,callbackurl,successCallback, errorCallback){
	//this.onSuccess = successCallback;
	//this.onFail = errorCallback;
	PhoneGap.exec("NavService.doNav",successCallback, errorCallback, [origAddr,destAddr,callbackurl]);
};
/**
 * This function is to get persistent data according to the infoID(Key) from store. As a synchronous function 
 * it returns PersistentData .
 * @param {String} infoID This key indicates the persistent data that you want to retrieve
 * @param {function} successCallback The callback that is called with returned PersistentData(Optional).
 * @param {function} errorCallback The callback that is called if there was an error (Optional). 
 * @return The persistent data corresponding to the infoID that you entered 
 */
TNService.prototype.getPersistentData = function(infoID,successCallback,errorCallback){
	//this.onSuccess = successCallback;
	//this.onFail = errorCallback;
	PhoneGap.exec("PersistentDataService.getPersistentData", successCallback, errorCallback,[infoID]);
};

/** 
 * This function is to set persistent data according to the infoID(Key)
 * @param {String} infoID 
 * @param {String} persistentData
 * @param {String} callbackurl  This specifies the identifier for returning back to your application  
 * @param {function} successCallback The callback that is called if setting persistent data succeed.
 * @param {function} errorCallback The callback that is called if there was an error.  
 */
TNService.prototype.setPersistentData = function(infoID,persistentData,callbackurl,successCallback,errorCallback){
	//this.onSuccess = successCallback;
	//this.onFail = errorCallback;
	PhoneGap.exec("PersistentDataService.setPersistentData", successCallback, errorCallback, [infoID,persistentData,callbackurl]);
};
/** 
 * This function is to delete persistent data according to the ID
 * @param {String} infoID 
 * @param {function} successCallback The callback that is invoked after persistentData has been deleted. 
 * @param {function} errorCallback The callback that is called if there was an error
 */
TNService.prototype.deletePersistentData = function(infoID,successCallback,errorCallback){
	//this.onSuccess = successCallback;
	//this.onFail = errorCallback;
	PhoneGap.exec("PersistentDataService.deletePersistentData",successCallback, errorCallback,[infoID]);
};


/**
 * This funtion is to let client only synch certain resources, otherwise synch anything server says has changed/updated.
 * @param {function} successCallback The callback that is called with returned preference value (Optional).
 * @param {function} errorCallback The callback that is called if there was an error (Optional).
 * @param {String} preferenceType It can be email or disUnit
 * @return The preference value that is corresponding  to the preference type designated by the parameter-preferenceType.
 */
TNService.prototype.getPreference = function(preferenceType,successCallback, errorCallback){
	//this.onSuccess.getPreference = successCallback;
	//this.onFail.getPreference = errorCallback;
	alert("enter phonegap exec");
	PhoneGap.exec("PreferenceService.getPreference",successCallback, errorCallback,[preferenceType]);
};
/**
iphone implementation
 PhoneGap.exec("Contacts.chooseContact", GetFunctionName(successCallback), options);
*/


/**
 * This funtion is to let client only synch certain resources, otherwise synch anything server says has changed/updated.
 * @param {String} preferenceType It can be email or something else 
 * @param {String} preferenceValue
 * @param {String} callbackurl  This specifies the identifier for returning back to your application 
 * @param {function} successCallback The callback that is called if setting preference succeed.
 * @param {function} errorCallback The callback that is called if there was an error
 */
TNService.prototype.setPreference = function(preferenceType,preferenceValue,callbackurl,successCallback, errorCallback){
	//this.onSuccess = successCallback;
	//this.onFail = errorCallback;
	var opt = {};
	opt.url = callbackurl;
	opt.type = preferenceType;
	opt.value = preferenceValue;
	PhoneGap.exec("PreferenceService.setPreference",successCallback,errorCallback,[opt]);
};

/**
 * This funtion is to let client only synch preference, otherwise synch anything server says has changed/updated.
 * @param {String}   purchaseType indicates type of purchase
 * @param {function} successCallback The callback that is called if resources synchronization succeed .
 * @param {function} errorCallback The callback that is called if there was an error
 */
TNService.prototype.syncPurchase = function(purchaseType,successCallback, errorCallback){
	//this.onSuccess = successCallback;
	//this.onFail = errorCallback;
	var opt = {};
	opt.type = purchaseType;
	PhoneGap.exec("PreferenceService.syncPurchase",successCallback, errorCallback,opt);
};
/**
 * This funtion is to get Status of syncPurchase. 
 * @param {String} purchaseType Indicates the type of purchase
 * @param {function} successCallback The callback that is called if resources synchronization succeed .
 * @param {function} errorCallback The callback that is called if there was an error
 */
TNService.prototype.getSyncPurchaseStatus = function(purchaseType,successCallback, errorCallback){
	//this.onSuccess = successCallback;
	//this.onFail = errorCallback;
	var opt = {};
	opt.type = purchaseType;
	PhoneGap.exec("PreferenceService.getSyncPurchaseStatus",successCallback, errorCallback, [opt]);
};

/**
 * This funtion is to get token from client side and then passes through to server API that needs it.
 * @param {function} successCallback The callback that is called with TelenavToken (Optional).
 * @param {function} errorCallback The callback that is called if there was an error (Optional).
 * @return The TelenavToken needed by the server APIs. 
 */
TNService.prototype.getTeleNavToken= function(successCallback,errorCallback){
	//this.onSuccess = successCallback;
	//this.onFail = errorCallback;
	PhoneGap.exec("TNTokenService.getTelenavToken",successCallback, errorCallback,[]);
};

/** 
 * This function is to get TN Product Information
 * @param {function} successCallback The callback that that provides TN Product Information (Optional).
 * @param {function} errorCallback The callback that is called if there was an error (Optional).
 * @return TN Product Information
 */
TNService.prototype.getTNInfo = function(successCallback, errorCallback){
	//this.onSuccess = successCallback;
	//this.onFail = errorCallback;
  PhoneGap.exec("TNInfoService.getTNInfo",successCallback, errorCallback,[]);
};

/** 
 * @description This function is to set the mode of the SDK window
 * @param {String} windowMode there are two candidate values now: appstore, app. The window with appstore mode will have the nav bar,
 * app mode indicates the full screen mode.
 * @param {function} successCallback The callback that is invoked after a TNService object has set window mode successfully.
 * @param {function} errorCallback The callback that is called if there was an error.
 * @example
 * navigator.tnservice.setWindowMode("appstore");
 * navigator.tnservice.setWindowMode("app");
 */
TNService.prototype.setWindowMode = function(windowMode, successCallback, errorCallback) {
	//this.onSuccess = successCallback;
	//this.onFail = errorCallback;
	var opt = {};
	opt.windowMode = windowMode;
	PhoneGap.exec("TNWindowModeService.setWindowMode", successCallback, errorCallback,[opt]);
}

/**
 * @description This function is used to exit the browser
 * @param {Boolean} toHomeScreen after whether back to home screen after exit the browser 
 * @param {function} successCallback The user's callback function that is called if existing browser is successful.
 * @param {function} errorCallback It provides an error message
 */
TNService.prototype.exitBrowser = function(toHomeScreen, successCallback, errorCallback) {
	//this.onSuccess = successCallback;
	//this.onFail = errorCallback;

	PhoneGap.exec("TNUtilService.exitBrowser",successCallback, errorCallback, [toHomeScreen]);
}

/**
 * @description Client native code will be called and passed the url.
 * It is up to the client native code to close any open views and then launch the local app in the browser of it's choice.
 * Note: if client wants to launch a local app in the same browser window, it just loads the URL. 
 * @param {function} successCallback The callback is invoked after a TNService object has launched a local app.
 * @param {function} errorCallback It provides an error message
 * @param {String} url the local app url to launch
 */
TNService.prototype.launchLocalApp = function(url, successCallback, errorCallback) {
	//this.onSuccess = successCallback;
	//this.onFail = errorCallback;
	var opt = {};
	opt.utilAction = url;
	PhoneGap.exec("TNUtilService.launchLocalApp",successCallback, errorCallback,opt);
}
/**
 * @description Client native code will be called 
 * It is up to the client native code to close any open views and then launch the local app in the browser of it's choice.
 * Note: if client wants to launch a local app in the same browser window, it just loads the URL. 
 * @param {function} successCallback The callback is invoked after a TNService object has launched Local Settings
 * @param {function} errorCallback It provides an error message
 */
TNService.prototype.launchSettings = function(successCallback, errorCallback) {
	//this.onSuccess = successCallback;
	//this.onFail = errorCallback;
	var opt = {};
	PhoneGap.exec("TNUtilService.launchSettings",successCallback, errorCallback);
}


/**
*@class This class provides access to get geolocation, it is an attribute of tnservice.
*@description The Geolocation object is used by scripts to programmatically determine the location information associated with the hosting device. 
*   The location information is acquired by applying a user-agent specific algorithm, creating a Position object, and populating that 
*   object with appropriate data accordingly. It should be used like this: navigator.tnservice.geolocation
*
*/
Geolocation = function(){};

var STOREDPOSITION = "currentGeolocation";
var ACHIEVEDTIME = "currentTime";
var TIMEINTERVAL = 60000;
//var INITIAL_GEOLOCATION = {"coords":{"speed":null,"accuracy":60,"altitudeAccuracy":null,"altitude":null,"longitude":-122.01921,"heading":null,"latitude":37.37646},"timestamp":1298505601000};
var INITIAL_GEOLOCATION = {"coords":{"longitude":-118.24532,"latitude":34.05348}};
/**
*@description This function takes one, two or three arguments. When called, it must immediately return and then asynchronously attempt 
* to obtain the current location of the device. If the attempt is successful, the successCallback must be invoked (i.e. the handleEvent operation must be 
* called on the callback object) with a new Position object, reflecting the current location of the device. If the attempt fails, the errorCallback must 
* be invoked with a new PositionError object, reflecting the reason for the failure.
* @param {function} successCallback The callback provides current location of the device.
* @param {function} errorCallback(optional) The callback that is called if there was an error
* @param {object} options(optional) Optional parameters to customize the geolocation settings.
* <h>Options</h>
* <p>enableHighAccuracy: provides a hint that the application would like to receive the best possible results.
*	timeOut: denotes the maximum length of time
*	maximumAge: indicates that the application is willing to accept a cached position whose age is no greater than the specified time in milliseconds.
* </p>
*/
Geolocation.prototype.getCurrentPosition = function(successCallback, errorCallback,options){
	/*var maximumAge = (options != undefined)? options.maximumAge : 0;
    // successCallback required
  if (typeof successCallback != "function") {
        console.log("Geolocation Error: successCallback is not a function");
        return;
    }
	var storedPos = localStorage.getItem(STOREDPOSITION);
	var storedTime = localStorage.getItem(ACHIEVEDTIME);
	
	var curTime = new Date().getTime();
	var difTime = curTime - storedTime;
	
	var suc = function(pos){
		var thisTime = new Date().getTime();
		localStorage.setItem(STOREDPOSITION,JSON.stringify(pos));
		localStorage.setItem(ACHIEVEDTIME, thisTime);
		console.log("Get current position successfully");
		
		successCallback(pos);
	};
	var fail = function(error){
		if(storedPos != null && storedPos != "" ){
			successCallback(JSON.parse(storedPos));		
		}else{
			successCallback(INITIAL_GEOLOCATION);		
		}
		
		errorCallback(error);
	};
	
	if (storedPos != null && storedPos != "" && difTime <= maximumAge){
		console.log("Stored position here!");	
		successCallback(JSON.parse(storedPos));
	}else {
		navigator.geolocation.getCurrentPosition(suc,fail,options);
	}*/
	PhoneGap.exec( "Geolocation.getCurrentPosition",successCallback, errorCallback,[options]);
}
/**
*@description The watchPosition()  takes one, two or three arguments. When called, it must immediately return a long value that uniquely
* identifies a watch operation and then asynchronously start the watch operation. This operation must first attempt to obtain the current location
* of the device. If the attempt is successful, the successCallback must be invoked (i.e. the handleEvent operation must be called on the callback
* object) with a new Position object, reflecting the current location of the device. If the attempt fails, the errorCallback must be invoked with
* a new PositionError object, reflecting the reason for the failure.
*@param
*@param {function} errorCallback(optional) The callback that is called if there was an error
*@param {object} options(optional) Optional parameters to customize the geolocation settings.
* <h>Options</h>
* <p>enableHighAccuracy: provides a hint that the application would like to receive the best possible results.
*	timeOut: denotes the maximum length of time
*	maximumAge: indicates that the application is willing to accept a cached position whose age is no greater than the specified time in milliseconds.
* </p>
*@return A unique long value that identifies a watch operation
*/
Geolocation.prototype.watchPosition = function(successCallback, errorCallback, options){
	/*
	var maximumAge = (options != undefined)? options.maximumAge : 0;
	// successCallback required
	if (typeof successCallback != "function") {
        console.log("Geolocation Error: successCallback is not a function");
        return;
    }
	var suc = function(pos){
		var storedTime = localStorage.getItem(ACHIEVEDTIME);
		var thisTime = new Date().getTime();	
		var difTime = thisTime - storedTime;
		if(difTime >= 60000){
			localStorage.setItem(STOREDPOSITION,JSON.stringify(pos));
			localStorage.setItem(ACHIEVEDTIME, thisTime);
			console.log("");
		}
		console.log("watch position successfully");
		successCallback(pos);
	};
	var fail = function(error){
		var storedPos = localStorage.getItem(STOREDPOSITION);
		var storedTime = localStorage.getItem(ACHIEVEDTIME);
	
		var curTime = new Date().getTime();
		var difTime = curTime - storedTime;
		if (storedPos != null && storedPos != ""){
			console.log("Watch position unsuccessfully and used stored position");
			successCallback(JSON.parse(storedPos));
		}else {
			successCallback(INITIAL_GEOLOCATION);
//			errorCallback(error);
		}
	};
	navigator.geolocation.watchPosition(suc,fail,options);		*/
	PhoneGap.exec( "Geolocation.watchPosition",successCallback, errorCallback,[options]);
}
/**
*@description The clearWatch() method takes one argument. When called, it must first check the value of the given watchId argument.
* If this value does not correspond to any previously started watch process, then the method must return immediately without taking
* any further action. Otherwise, the watch process identified by the watchId argument must be immediately stopped and no further callbacks must be invoked.
*@param {Number} watchId  A unique long value that identifies a watch operation
*/
Geolocation.prototype.clearWatch = function(watchId){
	navigator.geolocation.clearWatch(watchId);
}

TNService.prototype.geolocation = new Geolocation();
/**
* Add tnservice object so that can be used by JavaScript
*/
PhoneGap.addConstructor(function() {
    if (typeof navigator.tnservice == "undefined") navigator.tnservice = new TNService();
});

