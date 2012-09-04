function AcTemplate() {
	this.home = {
		"postalCode" : "94128",
		"county" : "",
		"state" : "CA",
		"firstLine" : "SAN FRANCISCO INTERNATIONAL AIRPORT",
		"country" : "US",
		"city" : "BURLINGAME"
	};

	this.work = {
		"postalCode" : "94089",
		"county" : "",
		"state" : "CA",
		"firstLine" : "Baylands Park",
		"country" : "US",
		"city" : "Sunnyvale"
	};

	this.returnJSON2 = {
		"region" : "US",
		"addressType" : "recentandfav",
		"addressList" : [ {
			"postalCode" : "94128",
			"index" : 0,
			"source" : "recent",
			"county" : "",
			"state" : "CA",
			"firstLine" : "San Francisco Hotels",
			"country" : "US",
			"city" : "BURLINGAME"
		},{
			"postalCode" : "94128",
			"index" : 0,
			"source" : "recent",
			"county" : "",
			"state" : "CA",
			"firstLine" : "SAN FRANCISCO INTERNATIONAL AIRPORT",
			"country" : "US",
			"city" : "BURLINGAME"
		}, {
			"postalCode" : "94089",
			"index" : 1,
			"source" : "favorite",
			"county" : "",
			"state" : "CA",
			"firstLine" : "Baylands Park",
			"country" : "US",
			"city" : "Sunnyvale"
		},{
			"postalCode" : "94128",
			"index" : 0,
			"source" : "recent",
			"county" : "",
			"state" : "CA",
			"firstLine" : "SAN FRANCISCO INTERNATIONAL AIRPORT AIRPORT AIRPORT AIRPORT AIRPORT AIRPORT AIRPORT AIRPORT",
			"country" : "US",
			"city" : "BURLINGAME"
		} ]
	};

	this.returnJSON3 = {
		"region" : "US",
		"addressType" : "addressCache",
		"addressList" : [ {
			"state" : "CA",
			"postalCode" : "",
			"county" : "",
			"country" : "US",
			"firstLine" : "",
			"city" : "SUNNYVALE SAN FRANCISCO INTERNATIONAL AIRPORT"
		}, {
			"state" : "CA",
			"postalCode" : "",
			"county" : "",
			"country" : "US",
			"firstLine" : "",
			"city" : "SAN FRANCISCO INTERNATIONAL AIRPORT AIRPORT AIRPORT AIRPORT AIRPORT"
		}, {
			"state" : "CA",
			"postalCode" : "",
			"county" : "",
			"country" : "US",
			"firstLine" : "",
			"city" : "SANTA CLARA, CA"
		}, {
			"state" : "CA",
			"postalCode" : "",
			"county" : "",
			"country" : "US",
			"firstLine" : "",
			"city" : "SAN FRANCISCO INTERNATIONAL AIRPORT"
		}, {
			"state" : "CA",
			"postalCode" : "",
			"county" : "",
			"country" : "US",
			"firstLine" : "",
			"city" : "MOUNTAIN VIEW SAN FRANCISCO INTERNATIONAL AIRPORT"
		} ]
	};
}

AcTemplate.prototype = {
	constructor : AcTemplate,

	getAddressCacheList : function(type) {
		return sessionStorage.getItem("SESSION_STORAGE_ADDRESS_CACHE_LIST_" + type);
	},

	setAddressCacheList : function(data, type) {
		sessionStorage.setItem("SESSION_STORAGE_ADDRESS_CACHE_LIST_" + type, data);
	},

	clearAddressCacheList : function() {
		sessionStorage.removeItem("SESSION_STORAGE_ADDRESS_CACHE_LIST_addressCache");
		sessionStorage.removeItem("SESSION_STORAGE_ADDRESS_CACHE_LIST_recentAndFav");
	},

	getAddressObj : function(keys, values) {
		var s = "", msg = "";
		for ( var i = 0, length = keys.length; i < length; i++) {
			s += "<" + keys[i] + ">" + "=" + values[i];
			msg += values[i];
			if (i != length - 1) {
				s += ";";
				msg += ",";
			}
		}
		var addrObj = {
			"region" : $("#region").val(),
			"aceAddress" : s,
			"uiAddress" : msg
		};
		return addrObj;
	},

	retrieveHomeWork : function(addrType) {
		var self = this;
		CommonUtil.debug("SDK_API_getAddress ===========start " + addrType);
		if (dummyData == "true") {
			this.fakeGetHomeWork(function() {
				self.getHomeWorkAddressCallback.apply(self, arguments);
			}, addrType);
		} else {
			SDK_API_getAddress(function() {
				self.getHomeWorkAddressCallback.apply(self, arguments);
			}, addrType);
		}
		CommonUtil.debug("SDK_API_getAddress ===========over " + addrType);
	},

	fakeGetHomeWork : function(callback, addrType) {
		CommonUtil.debug("fakeGetHomeWork===== start");
		CommonUtil.debug("addrType=====" + addrType);
		var result = this.work;
		if (addrType == "home") {
			result = this.home;
		}
		callback(JSON.stringify(result));
		CommonUtil.debug("fakeGetHomeWork===== over");
	},

	getHomeWorkAddressCallback : function(resultString) {
		CommonUtil.debug("getHomeWorkAddressCallback resultString==========" + resultString);
		if (!!resultString) {
			CommonUtil.debug("getHomeWorkAddressCallback JSON.parse(resultString)==========" + JSON.parse(resultString));
			var result = JSON.parse(resultString);
			var inputs = document.getElementsByName('addressInput'), stopField = "", node = null;
			for ( var i = 0, length = inputs.length; i < length; i++) {
				node = $(inputs[i]);
				if (node.length <= 0) {
					CommonUtil.debug("node==null");
				} else {
					stopField = node.attr("stopField");
					CommonUtil.debug("stopField==========" + stopField);
					var mappings = stopField.split("|"), s = "", msg = "";
					for ( var j = 0, mapLength = mappings.length; j < mapLength; j++) {
						s = result[this.trim(mappings[j])];
						if (!!s) {
							if (msg.length > 0) {
								msg += ", ";
							}
							msg += s;
						}
					}
					if (!!msg) {
						inputs[i].value = msg;
					}
				}
			}
		}
	},

	retrieveAutoCompleteData : function() {
		var region = $("#region").val();
		CommonUtil.debug("region====" + region);

		for ( var suggestType in regionSuggestObj) {
			var addressFilter = {
				"region" : region,
				"fields" : regionSuggestObj[suggestType]
			};
			var self = this;
			CommonUtil.debug("SDKAPI.getAddressAutocompleteData ===========start " + suggestType + "||" + JSON.stringify(addressFilter));
			if (dummyData == "true") {
				this.fakeGetAddressAutocompleteData(suggestType, addressFilter, function() {
					self.getAddressAutoCompleteDataCallback.apply(self, arguments);
				}, suggestType);
			} else {
				SDKAPI.getAddressAutocompleteData(suggestType, addressFilter, function() {
					self.getAddressAutoCompleteDataCallback.apply(self, arguments);
				}, suggestType);
			}
			CommonUtil.debug("SDKAPI.getAddressAutocompleteData ===========over " + suggestType + "||" + JSON.stringify(addressFilter));
		}
	},

	fakeGetAddressAutocompleteData : function(addressType, addressFilter, callback, autoSuggestType) {
		CommonUtil.debug("fakeGetAddressAutocompleteData===== start");
		CommonUtil.debug("autoSuggestType=====" + autoSuggestType);
		CommonUtil.debug("addressType=====" + addressType);
		CommonUtil.debug("addressFilter.region=====" + addressFilter.region);
		var result = this.returnJSON2;
		if (addressType == "addressCache") {
			result = this.returnJSON3;
		}
		result.region = addressFilter.region;
		result.addressType = addressType;
		callback(result, autoSuggestType);
		CommonUtil.debug("fakeGetAddressAutocompleteData===== over");
	},

	getAddressAutoCompleteDataCallback : function(result, autoSuggestType) {
		CommonUtil.debug("getAddressAutoCompleteDataCallback autoSuggestType==========" + autoSuggestType);
		CommonUtil.debug("getAddressAutoCompleteDataCallback result==========" + result);
		CommonUtil.debug("JSON.stringify(result)==========" + JSON.stringify(result));
		if (!!result) {
			var addressStr = JSON.stringify(result.addressList);
			this.setAddressCacheList(addressStr, result.addressType);
			this.loadAutoCompleteData(autoSuggestType);
		}
	},

	loadAutoCompleteData : function(autoSuggestType) {
		CommonUtil.debug("loadAutoCompleteData autoSuggestType==" + autoSuggestType);
		var inputs = document.getElementsByName('addressInput'), autoSuggest = "", node = null, addressCacheList = null;
		for ( var i = 0, length = inputs.length; i < length; i++) {
			node = $(inputs[i]);
			if (node.length <= 0) {
				CommonUtil.debug("node==null");
			} else {
				autoSuggest = node.attr("autoSuggest");
				CommonUtil.debug("loadAutoCompleteData input autoSuggest==" + autoSuggest);
				if (autoSuggest == autoSuggestType) {
					addressCacheList = this.getAddressCacheList(autoSuggest);
					CommonUtil.debug("loadAutoCompleteData addressCacheList==" + addressCacheList);
					if (!!addressCacheList) {
						this.bindInputs(addressCacheList, inputs[i]);
					}
				}
			}
		}
	},

	bindInputs : function(addressStr, input) {
		var node = $(input);
		CommonUtil.debug("bindInputs node id===" + node.attr("id"));
		var sugArray = this.convertBindingData(addressStr, node.attr("autoSuggest"), node.attr("autoSuggestFormat"));
		if (!sugArray || sugArray.length < 1) {
			return;
		}
		this.bindAutoComplete(input, node.attr("triggerChar"), node.attr("optionsSize"), sugArray);
	},

	convertBindingData : function(oriObjStr, type, autoSuggestFormat) {
		if (autoSuggestFormat && oriObjStr) {
			var addrObjArray = oriObjStr ? JSON.parse(oriObjStr) : [], mappings = autoSuggestFormat.split("|");
			var obj = null, s = "", addrObj = null, msg = "", msgArray = [], hash = [], index = -1, source = "";
			for ( var i = 0, addrLength = addrObjArray.length; i < addrLength; i++) {
				addrObj = addrObjArray[i];
				msg = "";
				s = "";
				index = addrObj["index"];
				source = addrObj["source"];
				for ( var j = 0, mapLength = mappings.length; j < mapLength; j++) {
					s = addrObj[this.trim(mappings[j])];
					if (!!s) {
						if (msg.length > 0) {
							msg += ", ";
						}
						msg += s;
					}
				}
				if (msg.length > 0) {
					if (type == "addressCache") {
						if (hash[msg] == true) {
							continue;
						} else {
							hash[msg] = true;
						}
					}
					obj = {
						"msg" : msg,
						"index" : index,
						"source" : source,
						"addressType" : type
					};
					msgArray.push(obj);
				}
			}
			CommonUtil.debug("msgArray=======" + JSON.stringify(msgArray));
			return msgArray;
		}
		return null;
	},

	bindAutoComplete : function(input, min_char, max_len, suggestArr) {
		var options = {
			"min_char" : min_char,
			"max_len" : max_len,
			"pop_css" : "autoDis",
			"li_normal" : "fs_large normal suggestLiCss",
			"li_highlight" : "fs_large highlight suggestLiCss"
		};
		// cities is the data source
		CommonUtil.debug("options===========" + JSON.stringify(options));
		CommonUtil.debug("suggestArr===========\n" + JSON.stringify(suggestArr));
		var autoComplete = new AutoComplete(suggestArr, options, this.autoSuggestCallBack);
		autoComplete.init().bind(input);
	},

	autoSuggestCallBack : function(result, input) {
		CommonUtil.debug("autoSuggestCallBack result====" + JSON.stringify(result));
		if (!!result) {
			if (result["index"] != null && result["source"] != null) {
				var addressObj = {
					"region" : $("#region").val(),
					"index" : result["index"],
					"source" : result["source"]
				};
				addressType = result.addressType;

				CommonUtil.debug("SDKAPI.setValidationAddress  ===========start " + addressType + "||" + JSON.stringify(addressObj));
				SDKAPI.setValidationAddress(addressType, addressObj);
				CommonUtil.debug("SDKAPI.setValidationAddress  ===========over " + addressType + "||" + JSON.stringify(addressObj));
			} else {
				input.value = result["msg"];
			}
		}

	},

	findAddress : function() {
		var node = null, minLength = 0, maxLength = -1, text = "", hasInput = false;
		var inputs = document.getElementsByName('addressInput'), length = inputs.length, values = new Array(length), keys = new Array(length);
		for ( var i = 0; i < length; i++) {
			values[i] = "";
			keys[i] = "";
			node = $(inputs[i]);
			if (node.length <= 0) {
				CommonUtil.debug("node==null");
			} else {
				text = node.val();
				CommonUtil.debug("text==" + text);
				keys[i] = node.attr("id");
				CommonUtil.debug("key,id==" + keys[i]);
				maxLength = node.attr("maxLength");
				CommonUtil.debug("maxLength==" + maxLength);
				minLength = node.attr("minLen");
				CommonUtil.debug("minLen==" + minLength);
				// check input
				if (!!text) {
					text = this.trim(text);
					if (text.length > 0) {
						hasInput = true;
					}
					if (text.length < minLength) {
						CommonUtil.showAlert("", node.attr("placeholder") + " inputbox should >= " + minLength, I18NHelper["common.button.OK"]);
						return;
					} else if (maxLength != "-1" && maxLength >= minLength && text.length > maxLength) {
						CommonUtil.showAlert("", node.attr("placeholder") + " inputbox should <= " + minLength, I18NHelper["common.button.OK"]);
						return;
					}
					values[i] = text;
				} else {
					if (minLength > 0) {
						CommonUtil.showAlert("", node.attr("placeholder") + " inputbox should >= " + minLength, I18NHelper["common.button.OK"]);
						return;
					}
				}
			}
		}
		if (hasInput == false) {
			CommonUtil.showAlert("", I18NHelper["please.enter.address"], I18NHelper["common.button.OK"]);
			return;
		}
		var addressObj = this.getAddressObj(keys, values);
		var addressType = "userEntered";

		CommonUtil.debug("SDKAPI.setValidationAddress  ===========start " + addressType + "||" + JSON.stringify(addressObj));
		SDKAPI.setValidationAddress(addressType, addressObj);
		CommonUtil.debug("SDKAPI.setValidationAddress  ===========over " + addressType + "||" + JSON.stringify(addressObj));
	},

	trim : function(str) {
		return str.replace(/(^\s*)|(\s*$)/g, "");
	}
};

var acTemplate = new AcTemplate();
