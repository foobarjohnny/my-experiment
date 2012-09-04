(function(window, undefined) {

// Use the correct document accordingly with window argument (sandbox)
var document = window.document,
	navigator = window.navigator,
	location = window.location;
	// Used for trimming whitespace
var	trimLeft = /^\s+/,
	trimRight = /\s+$/,
	class2type ={},
	rdigit = /\d/,
	rupper = /([A-Z]|^ms)/g,
	fcamelCase = function( all, letter ) {
		return letter.toUpperCase();
	},
	rdashAlpha = /-([a-z])/ig,
	cssWidth = [ "left", "right" ],
	cssHeight = [ "top", "bottom" ],
	cssShow = { position: "absolute", visibility: "hidden", display: "block" },
	toString = Object.prototype.toString;
// Define a local copy of jQuery
var jQuery = function( selector) {
		// The jQuery object is actually just the init constructor 'enhanced'
		return new jQuery.fn.init( selector);
};

jQuery.fn = jQuery.prototype = {
	constructor: jQuery,
	length:0,
	init: function( selector) {
		var match, elem, ret, doc;

		// Handle $(""), $(null), or $(undefined)
		if ( !selector ) {
			return this;
		}
			//alert(selector);
		// Handle $(DOMElement)
		if ( selector.nodeType ) {
			this.context = this[0] = selector;
			this.length = 1;
			return this;
		}
		if ( selector===window ) {
			this.context = this[0] = selector;
			this.length = 1;
			return this;
		}
		// The body element only exists once, optimize finding it
		if ( selector === "body" && !context && document.body ) {
			this.context = document;
			this[0] = document.body;
			this.selector = selector;
			this.length = 1;
			return this;
		}

		// Handle HTML strings
		if ( typeof selector === "string" ) 
		{

			this.selector = selector;
			if(selector.substr(0,1)===".")
			{
			    var els = document.all,
			        elsLen = els.length;
			    var searchClass = selector.substr(1);
			    var  pattern = new RegExp("\\b"+searchClass+"\\b");
			    var j = 0;
			    for (var i = 0; i < elsLen; i++) {
			         if ( pattern.test(els[i].className) ) {
			             this[j++] = els[i];
			         }
			    }
				 this.length = j;
			}
			else
			{
				var strId = selector.substr(0,1)==="#"?selector.substr(1):selector;
				var currElement = document.getElementById(strId);
				if(currElement)
				{
					this[0] = currElement;
					this.length = 1;
				}
			}
		}

		return this;
	},
	each: function( callback, args ) {
		return jQuery.each(this, callback, args );
	},
	ready: function( fn ) {
		// Attach the listeners
		jQuery.bindReady();

		// Add the callback
		jQuery.done( fn );

		return this;
	},
	html:function(value)
	{
		if(value!==undefined) 
		{
			 this.each(function(){
			 	this.innerHTML = value;
			  });
		} else return this.length>0 ? this[0].innerHTML : "";
	},
	attr: function( name, value) {
		if(this.length <=0) return;
		var elem = this[0];
		var nType = elem.nodeType;
		// don't get/set attributes on text, comment and attribute nodes
		if ( !elem || nType === 3 || nType === 8 || nType === 2 ) {
			return undefined;
		}
		if ( value !== undefined ) 
		{
			this.each(function(){this.setAttribute( name, "" + value ); });
			return value;
		} else ret = elem.getAttribute( name );
		return ret;
	},
	val: function(value) {
		if(value!==undefined) 
		{
			 this.each(function(){this.value =value; });
		} else return this.length>0 ? this[0].value : "";
	},
	setVisible:function(value)
	{
		var tDisplay = !!value;
		this.each(
		 	function(){
				if(!this.getAttribute("oldDisplayStyle"))
				{
					this.setAttribute("oldDisplayStyle",this.style.display=="none"?"":this.style.display);
				}
				var dd = tDisplay?(this.getAttribute("oldDisplayStyle")?this.getAttribute("oldDisplayStyle"):"block"):"none"
				this.style.display = dd;
		 	}
		 );

	},
	show:function(){
		this.setVisible(true);
		return this;
	},
	hide:function(){
		this.setVisible(false);
		return this;
	},
	height:function(value){
		if(value!==undefined) 
		{
			this.each(
			 	function(){
					var h = jQuery.type(value)=="number"? value+"px" : value;
					this.style.height = h;
			 	}
			 );
		} else return this.getWH("height");
	},
	width:function(value){
		if(value!==undefined) 
		{
			this.each(
			 	function(){
					var h = jQuery.type(value)=="number"? value+"px":value;
					this.style.width=h;
			 	}
			 );
		} else return this.getWH("width");
	},
	getWH:function(name,extra)
	{
		var elem = this[0],val = 0;
		if(elem){
			val = jQuery.getWH(elem, name, extra );
			if(val===0) {
				jQuery.swap(elem, cssShow, function() {
						val = jQuery.getWH( elem, name, extra );
				});
			}

			if ( val <= 0 ) {
				val = jQuery.getCss( elem, name );
				val = parseInt(val);
			}
		}
		return val;
	},
	outerWidth:function(){return this.getWH("width","outer");},
	outerHeight:function(){return this.getWH("height","outer");},
	attachEvent:function(fn,name){
	
			this.each(
			 	function(){
					// Mozilla, Opera and webkit nightlies currently support this event
					if ( this.addEventListener ) {
						// A fallback to window.onload, that will always work
						this.addEventListener(name, fn, false );
					// If IE event model is used
					} else if ( this.attachEvent ) {
						// A fallback to window.onload, that will always work
						this.attachEvent( name, fn );
					}
			 	}
			 );

	},
	click:function(fn)
	{
		this.attachEvent(fn,"onclick");
		this.attachEvent(fn,"click");
		return this;
	},
	resize:function(fn)
	{
		this.attachEvent(fn,"onresize");
		this.attachEvent(fn,"resize");
		return this;
	},
	css:function(css,value)
	{
		if(value===undefined && jQuery.type(css)=="string")
	    {
	      	 return jQuery.getCss( this[0],css);
	    } else {
			var cssObj = {};
			if(jQuery.type(css)=="string")
			{
				cssObj[css] = value
			 } else cssObj = css;
			this.each(
			 	function(){
			 		for(var k in cssObj) {
						this.style[jQuery.camelCase(k)]=cssObj[k]+"";
					}
			 	}
			 );
	   }
	},
	fadeIn: function(){this.show();},
	fadeOut:  function(){this.hide();},
	push: [].push
};

// Give the init function the jQuery prototype for later instantiation
jQuery.fn.init.prototype = jQuery.fn;
//make the extend function simple
jQuery.extend = function() {
	var target = this;
	var src = arguments[0];
	if(arguments.length>=2) 
	{
		target = arguments[1];
		src = arguments[0]
	}
	if(target && src)
	{
		for(var k in src)
		{
			target[k]=src[k];
		}
	}

};

jQuery.extend({
	// Is the DOM ready to be used? Set to true once it occurs.
	isReady: false,
	callbacks:[],
	done:function(fun)
	{
		jQuery.callbacks.push(fun);
	},
	getCss: function(ele,cssName){
		var cssCamel = jQuery.camelCase(cssName);
		var val= ele.style[cssCamel] || ele.style[cssName];
		if(!val){ 
			if(ele.currentStyle) return ele.currentStyle[cssCamel] || ele.currentStyle[cssName];
	        var dv= document.defaultView || window; 
	        if(dv && dv.getComputedStyle){ 
	            return dv.getComputedStyle(ele,'').getPropertyValue(cssName) || dv.getComputedStyle(ele,'').getPropertyValue(cssCamel); 
	        } 
        }
    	return val;
	},
	getWH:function(elem,name,extra)
	{
		var which = name === "width" ? cssWidth : cssHeight,
		val = name === "width" ? elem.offsetWidth : elem.offsetHeight;
		jQuery.each( which, function() {
			if ( !extra ) {
				val -= parseFloat(jQuery.getCss( elem, "padding-" + this )) || 0;
			}

			if ( extra === "margin" ) {
				val += parseFloat(jQuery.getCss( elem, "margin-" + this )) || 0;

			} else {
				val -= parseFloat(jQuery.getCss( elem, "border-" + this + "-width" )) || 0;
			}
		});
		return val;
	},
	// Handle when the DOM is ready
	ready: function() {
		// Either a released hold or an DOMready/load event and not yet ready
		if (!jQuery.isReady ) {
			// Make sure body exists, at least, in case IE gets a little overzealous (ticket #5443).
			if ( !document.body ) {
				return setTimeout( jQuery.ready, 1 );
			}

			// Remember that the DOM is ready
			jQuery.isReady = true;
			while( jQuery.callbacks[ 0 ] ) 
			{
				jQuery.callbacks.shift().apply(document);
			}

		}
	},
	hasBind:false,
	bindReady: function() {
		if ( jQuery.hasBind ) {
			return;
		}
		jQuery.hasBind = true;
		// Catch cases where $(document).ready() is called after the
		// browser event has already occurred.
		if ( document.readyState === "complete" ) {
			// Handle it asynchronously to allow scripts the opportunity to delay ready
			return setTimeout( jQuery.ready, 1 );
		}

		// Mozilla, Opera and webkit nightlies currently support this event
		if ( document.addEventListener ) {
			document.addEventListener("DOMContentLoaded",DOMContentLoaded, false );
			// A fallback to window.onload, that will always work
			window.addEventListener( "load", jQuery.ready, false );
		// If IE event model is used
		} else if ( document.attachEvent ) {
			// A fallback to window.onload, that will always work
			window.attachEvent( "onload", jQuery.ready );
		}
	},
	isFunction: function( obj ) {
		return jQuery.type(obj) === "function";
	},
	swap: function( elem, options, callback ) {
		var old = {};

		// Remember the old values, and insert the new ones
		for ( var name in options ) {
			old[ name ] = elem.style[ name ];
			elem.style[ name ] = options[ name ];
		}

		callback.call( elem );

		// Revert the old values
		for ( name in options ) {
			elem.style[ name ] = old[ name ];
		}
	},
	isArray: Array.isArray || function( obj ) {
		return jQuery.type(obj) === "array";
	},
	// args is for internal usage only
	each: function( object, callback, args ) {
		var name, i = 0,
			length = object.length,
			isObj = length === undefined || jQuery.isFunction( object );

		if ( args ) {
			if ( isObj ) {
				for ( name in object ) {
					if ( callback.apply( object[ name ], args ) === false ) {
						break;
					}
				}
			} else {
				for ( ; i < length; ) {
					if ( callback.apply( object[ i++ ], args ) === false ) {
						break;
					}
				}
			}

		// A special, fast, case for the most common use of each
		} else {
			if ( isObj ) {
				for ( name in object ) {
					if ( callback.call( object[ name ], name, object[ name ] ) === false ) {
						break;
					}
				}
			} else {
				for ( ; i < length; ) {
					if ( callback.call( object[ i ], i, object[ i++ ] ) === false ) {
						break;
					}
				}
			}
		}

		return object;
	},
	// A crude way of determining if an object is a window
	isWindow: function( obj ) {
		return obj && typeof obj === "object" && "setInterval" in obj;
	},

	isNaN: function( obj ) {
		return obj == null || !rdigit.test( obj ) || isNaN( obj );
	},

	type: function( obj ) {
		return obj == null ?
			String( obj ) :
			class2type[ toString.call(obj) ] || "object";
	},

	isEmptyObject: function( obj ) {
		for ( var name in obj ) {
			return false;
		}
		return true;
	},

	// Simplify the function
	trim: function( text ) {
			return text == null ?
				"" :
				text.toString().replace( trimLeft, "" ).replace( trimRight, "" );
		},
	now: function() {
		return (new Date()).getTime();
	},
	camelCase: function( string ) {
		return string.replace( rdashAlpha, fcamelCase );
	}

});
DOMContentLoaded = function() {
	document.removeEventListener( "DOMContentLoaded", DOMContentLoaded, false );
	jQuery.ready();
};

jQuery.each("Boolean Number String Function Array Date RegExp Object".split(" "), function(i, name) {
	class2type[ "[object " + name + "]" ] = name.toLowerCase();
});

window.jQuery = window.$ = jQuery;
})(window);

