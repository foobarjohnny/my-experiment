/**
 * Construct a new TNService object
 * @class
 * @description This class provides access to telenav services.
 * @constructor
 */
var TNPrivateService= function(){
		this.onSuccess = null;
		this.onFail = null;
};

/**
* Add tnprivateservice object so that can be used by JavaScript
*/
PhoneGap.addConstructor(function() {
    if (typeof navigator.tnprivateservice == "undefined") navigator.tnprivateservice = new TNPrivateService();
});

/**
*This function is to invoke TN Private Service 
* @param {String} serviceName It indicates the TNService  Name
* @param {Object} serviceData  This provides paramters to the TNService
* @param {function} successCallback The callback that is called after invoking private private service successfully.
* @param {function} errorCallback The callback that is called if there was an error.
* all call from js to object c are async, so sync call will be deprived.
TNPrivateService.prototype.doPrivateService = function (serviceName, serviceData, successCallback,errorCallback){
		return PhoneGap.exec(successCallback, errorCallback, "TNPrivateService", "callApp", [serviceName,serviceData]);
}
*/

/**
*This function is to invoke TN Private Service 
* @param {String} serviceName It indicates the TNService  Name
* @param {Object} serviceData  This provides paramters to the TNService
* @param {function} successCallback The callback that is called after invoking private private service successfully.
* @param {function} errorCallback The callback that is called if there was an error.
*/
TNPrivateService.prototype.doPrivateServiceAsync = function(serviceName, serviceData, successCallback,errorCallback){
	this.onSuccess = successCallback;
	this.onFail = errorCallback;
	PhoneGap.exec("TNPrivateService.callAppAsync",successCallback, errorCallback,[serviceName,serviceData]);
}