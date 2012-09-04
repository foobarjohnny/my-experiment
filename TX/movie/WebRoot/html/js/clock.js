function logManifest()
{
	if (window.applicationCache) {
		//console.log("this browser supports offline application");
	} else {
		//console.log("this browser does not support offline application");
	}
	
	window.applicationCache.onchecking = function(event)
	{
		//console.log("cache event: checking");
	},
	
	window.applicationCache.onnoupdate = function(event)
	{
		console.log("cache event: no update");
	},
	
	window.applicationCache.ondownloading = function(event)
	{
		//console.log("cache event: downloading");
	},

	window.applicationCache.onprogress = function(event)
	{
		//console.log("cache event: progress");
	},
	
	window.applicationCache.oncached = function(event)
	{
		//console.log("cache event: cached");
	},
	
	window.applicationCache.onupdateready = function(event)
	{
		console.log("cache event: update ready");
	},

	window.applicationCache.onobsolete = function(event)
	{
		//console.log("cache event: obsolete");
	},
	
	window.applicationCache.onerror = function(event)
	{
		//console.log("cache event: error");
	};
}
