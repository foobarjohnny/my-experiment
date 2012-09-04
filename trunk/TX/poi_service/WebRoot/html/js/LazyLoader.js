function ResourceLazyLoader() {

}

ResourceLazyLoader.prototype = {
	jsNo : 0,
	cssNo : 0,
	currentJSLoaded : 0,
	isCssAllLoaded : true,
	callBackAfterResourceLoaded : null,

	lazyLoadResource : function(jsPathArray, cssPathArray, callBack) {

		this.callBackAfterResourceLoaded = callBack;
		var oHead = document.getElementsByTagName('HEAD').item(0);
		var that = this;

		// add all the js
		if (jsPathArray) {
			this.jsNo = jsPathArray.length;
			for ( var i = 0; i < this.jsNo; i++) {
				var oneJS = document.createElement("script");
				oneJS.type = "text/javascript";
				oneJS.src = GLOBAL_contextPath + jsPathArray[i];

				var jsPath = jsPathArray[i];// don't use jsPathArray[i] instead.
				oneJS.onload = function() {
					that.onOneJSLoaded(jsPath);
				};
				oHead.appendChild(oneJS);
			}
		}

		// add all the css
		if (cssPathArray) {
			this.cssNo = cssPathArray.length;
			for ( var i = 0; i < this.cssNo; i++) {

				var hotelCss = document.createElement("link");
				hotelCss.type = "text/css";
				hotelCss.href = GLOBAL_cssUrl + cssPathArray[i];
				hotelCss.rel = "stylesheet";
				oHead.appendChild(hotelCss);
			}
			// set the interval to check css

			var oldStyleSheetSize = document.styleSheets.length;
			var ti = setInterval(function() {
				if (document.styleSheets.length >= oldStyleSheetSize + that.cssNo) {
					console.log("Hotel CSS loaded");
					that.onAllCssLoaded();
					clearInterval(ti);
				}
			}, 300);
		}
	},

	onOneJSLoaded : function(jsPath) {
		this.currentJSLoaded++;
		CommonUtil.debug("javaScript: [ " + jsPath + " ] successfully loaded");
		this.checkAllResource();
	},

	onAllCssLoaded : function() {
		this.isCssAllLoaded = true;
		console.log("All CSS successfully loaded");
		this.checkAllResource();
	},
	checkAllResource : function() {
		if (this.isCssAllLoaded && this.currentJSLoaded >= this.jsNo) {
			this.callBackAfterResourceLoaded.call(this);
		}
	}
};

var LazyLoader = new ResourceLazyLoader();
