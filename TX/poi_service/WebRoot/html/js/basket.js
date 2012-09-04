/*! basket.js - v0.2.0 - 3/9/2012
* http://addyosmani.github.com/basket.js
* Copyright (c) 2012 Addy Osmani; Licensed MIT, GPL
* Credits: Addy Osmani, Ironsjp, Mathias Bynens, Rick Waldron, Sindre Sorhus, AndrÃ©e Hansson 
* */

/*
 * Modified by Jianyu Zhou to use "TN-" as storage prefix
 * Add version extraction from URI. store version info in additional to Javascript content.
 * Update the local storage store according to version comparion
 */
(function ( window, document ) {
	"use strict";

	var
	storagePrefix = "TN-",
	scripts = [],
	scriptsExecuted = 0,
	waitCount = 0,
	waitCallbacks = [],
	versionPattern = /(\d+_)+\d+/g,

	getUrl = function( url, callback ) {
		var xhr = new XMLHttpRequest();
		xhr.open( "GET", url, true );

		xhr.onreadystatechange = function() {
			if ( xhr.readyState === 4 ) {
				callback( xhr.responseText );
			}
		};

		xhr.send();
	},

	saveUrl = function( url, key, callback ) {
		getUrl( url, function( text ) {
			var version = extractVersion(url);			
			cacheObj = new Object();
			cacheObj.version = version;
			cacheObj.content = text;
			localStorage.setItem( key, text );

			if ( isFunc(callback) ) {
				callback();
			}
		});
	},

	isFunc = function( func ) {
		return Object.prototype.toString.call( func ) === "[object Function]";
	},

	injectScript = function( text ) {
		var
		script = document.createElement("script"),
		body = document.getElementsByTagName("body")[0];

		//script.defer = true;
		// Have to use .text, since we support IE8,
		// which won't allow appending to a script
		script.text = text;

		body.insertBefore( script, body.lastChild );
	},

	queueExec = function( waitCount ) {
		var
		i,
		j,
		script,
		callback;

		if ( scriptsExecuted >= waitCount ) {
			for ( i = 0; i < scripts.length; i++ ) {
				script = scripts[ i ];

				if ( !script ) {
					// loading/executed
					continue;
				}

				scripts[ i ] = null;
				injectScript( script );
				scriptsExecuted++;

				for ( j = i; j < scriptsExecuted; j++ ) {
					callback = waitCallbacks[ j ];

					if ( isFunc(callback) ) {
						waitCallbacks[ j ] = null;
						callback();
					}
				}
			}
		}
	};

	function isVersionEqual(version, cacheObj){
		console.log("[BROWER SERVER] --Request version [" + version + "]");
		if(cacheObj){
			console.log("[BROWER SERVER] --Stored version [" + cacheObj.version + "]");
		}
		return cacheObj && (cacheObj.version == version);
	};
	
	function extractVersion(uri){
		var version = "";
		var parts =uri.split("/");
		var i;
		for(i in parts){
			if(parts[i].match(versionPattern)){
				version = parts[i];
				break;
			}
		}
		return version;
	}
	
	window.basket = {
		require: function ( uri, options ) {
			options = options || {};

			var
			version = extractVersion(uri),
			cacheObj,
			localWaitCount = waitCount,
			scriptIndex = scripts.length,
			key = storagePrefix + ( options.key || uri ),
			source = localStorage.getItem( key );
			
			scripts[ scriptIndex ] = null;
			console.log("[BROWER SERVER] --Request Object with key [" + key + "] + uri + [" + uri + "]");
			
			if(source){
				cacheObj = JSON.parse(source);
			}
			
			if ( source && isVersionEqual(version, cacheObj)) {
				console.log("[BROWER SERVER] --Request version matches stored version");
				scripts[ scriptIndex ] = cacheObj.content;
				queueExec( localWaitCount );
				
			} else {
				console.log("[BROWER SERVER] --Request version NOT matches store version. Get it from URI");
				getUrl( uri, function( text ) {
					cacheObj = new Object();
					cacheObj.version = version;
					cacheObj.content = text;
					localStorage.setItem( key, JSON.stringify(cacheObj) );
					scripts[ scriptIndex ] = text;
					queueExec( localWaitCount );
				});
			}

			return this;
		},

		add: function( uri, options, callback ) {
			options = options || {};

			var key = storagePrefix + ( options.key || uri );

			// default is to overwrite
			if ( typeof options.overwrite === "undefined" ) {
				options.overwrite = true;
			}

			// if they key exists and overwrite true, overwrite
			if ( localStorage.getItem(key) ) {
				if( options.overwrite ) {
					saveUrl( uri, key, callback );
				}
			} else {
				//key doesnt exist, add key as new entry
				saveUrl( uri, key, callback );
			}

			return this;
		},

		remove: function( key ) {
			localStorage.removeItem( storagePrefix + key );

			return this;
		},

		wait: function( callback ) {
			waitCount = scripts.length - 1;
			if ( callback ) {
				if ( scriptsExecuted > waitCount ) {
					callback();
				} else {
					waitCallbacks[ waitCount ] = callback;
				}
			}

			return this;
		},

		get: function( key ) {
			var cacheObj = localStorage.getItem( storagePrefix + key ) || null;
			return cacheObj.content;
		}
	};

}( this, document ));
