/*
 * Date Format 1.2.3
 * (c) 2007-2009 Steven Levithan <stevenlevithan.com>
 * MIT license
 *
 * Includes enhancements by Scott Trenda <scott.trenda.net>
 * and Kris Kowal <cixar.com/~kris.kowal/>
 *
 * Accepts a date, a mask, or a date and a mask.
 * Returns a formatted version of the given date.
 * The date defaults to the current date/time.
 * The mask defaults to dateFormat.masks.default.
 */

var dateFormat = function () {
	var	token = /d{1,4}|m{1,4}|yy(?:yy)?|([HhMsTt])\1?|[LloSZ]|"[^"]*"|'[^']*'/g,
		timezone = /\b(?:[PMCEA][SDP]T|(?:Pacific|Mountain|Central|Eastern|Atlantic) (?:Standard|Daylight|Prevailing) Time|(?:GMT|UTC)(?:[-+]\d{4})?)\b/g,
		timezoneClip = /[^-+\dA-Z]/g,
		pad = function (val, len) {
			val = String(val);
			len = len || 2;
			while (val.length < len) val = "0" + val;
			return val;
		};

	// Regexes and supporting functions are cached through closure
	return function (date, mask, utc) {
		
		var locale = $("#locale").val();
		
		if(locale == "de_DE"|| locale == "en_GB"||locale == "en_US"||locale == "ne_ES"||locale == "es_MX"||locale == "fr_FR"||locale == "it_IT"||locale == "nl_NL"||locale == "pt_BR"){
			
		}else{
			locale = "en_US";
		}
		
		var dF = dateFormat;

		// You can't provide utc if you skip other args (use the "UTC:" mask prefix)
		if (arguments.length == 1 && Object.prototype.toString.call(date) == "[object String]" && !/\d/.test(date)) {
			mask = date;
			date = undefined;
		}

		// Passing date through Date applies Date.parse, if necessary
		date = date ? new Date(date) : new Date;
		if (isNaN(date)) throw SyntaxError("invalid date");

		mask = String(dF.masks[mask] || mask || dF.masks["default"]);

		// Allow setting the utc argument via the mask
		if (mask.slice(0, 4) == "UTC:") {
			mask = mask.slice(4);
			utc = true;
		}

		var	_ = utc ? "getUTC" : "get",
			d = date[_ + "Date"](),
			D = date[_ + "Day"](),
			m = date[_ + "Month"](),
			y = date[_ + "FullYear"](),
			H = date[_ + "Hours"](),
			M = date[_ + "Minutes"](),
			s = date[_ + "Seconds"](),
			L = date[_ + "Milliseconds"](),
			o = utc ? 0 : date.getTimezoneOffset(),
			flags = {
				d:    d,
				dd:   pad(d),
				ddd:  dF.i18n["dayNames_"+locale][D],
				dddd: dF.i18n["dayNames_"+locale][D + 7],
				m:    m + 1,
				mm:   pad(m + 1),
				mmm:  dF.i18n["monthNames_"+locale][m],
				mmmm: dF.i18n["monthNames_"+locale][m + 12],
				yy:   String(y).slice(2),
				yyyy: y,
				h:    H % 12 || 12,
				hh:   pad(H % 12 || 12),
				H:    H,
				HH:   pad(H),
				M:    M,
				MM:   pad(M),
				s:    s,
				ss:   pad(s),
				l:    pad(L, 3),
				L:    pad(L > 99 ? Math.round(L / 10) : L),
				t:    H < 12 ? "a"  : "p",
				tt:   H < 12 ? "am" : "pm",
				T:    H < 12 ? "A"  : "P",
				TT:   H < 12 ? "AM" : "PM",
				Z:    utc ? "UTC" : (String(date).match(timezone) || [""]).pop().replace(timezoneClip, ""),
				o:    (o > 0 ? "-" : "+") + pad(Math.floor(Math.abs(o) / 60) * 100 + Math.abs(o) % 60, 4),
				S:    ["th", "st", "nd", "rd"][d % 10 > 3 ? 0 : (d % 100 - d % 10 != 10) * d % 10]
			};

		return mask.replace(token, function ($0) {
			return $0 in flags ? flags[$0] : $0.slice(1, $0.length - 1);
		});
	};
}();

// Some common format strings
dateFormat.masks = {
	"default":      "ddd mmm dd yyyy HH:MM:ss",
	shortDate:      "m/d/yy",
	mediumDate:     "mmm d, yyyy",
	longDate:       "mmmm d, yyyy",
	fullDate:       "dddd, mmmm d, yyyy",
	shortTime:      "h:MM TT",
	mediumTime:     "h:MM:ss TT",
	longTime:       "h:MM:ss TT Z",
	isoDate:        "yyyy-mm-dd",
	isoTime:        "HH:MM:ss",
	isoDateTime:    "yyyy-mm-dd'T'HH:MM:ss",
	isoUtcDateTime: "UTC:yyyy-mm-dd'T'HH:MM:ss'Z'"
};

dateFormat.i18n = {
		
		dayNames_en_US: [
			"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
			"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
		],
		monthNames_en_US: [
			"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
			"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
		],
		
		dayNames_es_ES: [
		     "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
		     "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
		],
		monthNames_es_ES: [
		     "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
		     "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
		],	
		
		dayNames_de_DE: [
		     "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
		      "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
		],
		monthNames_de_DE: [
		      "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
		      "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
		],
		
		dayNames_en_GB: [
		       "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
		   		"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
		],
		monthNames_en_GB: [
		     "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
		     "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
	    ],
		
		dayNames_es_MX: [
		    "Do", "Lu", "Ma", "Mi", "Ju", "Vi", "S\u00E1",
		    "Domingo", "Lunes", "Martes", "Mi\u00E9rcoles", "Jueves", "Viernes", "S\u00E1bado"
		],
	    monthNames_es_MX: [
		      "Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic",
		      "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
		],
		
		dayNames_fr_FR: [
		      "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
		       "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
		],
		monthNames_fr_FR: [
		      "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
		      "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
		],
		
		
		dayNames_it_IT: [
		   "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
		   "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
		],
		monthNames_it_IT: [
		   	"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
		    "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
		],
		
		dayNames_nl_NL: [
		    "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
		    "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
		],
		monthNames_nl_NL: [
		    "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
		    "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
		],
		
		dayNames_pt_BR: [
		     "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
		     "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
	     ],
        monthNames_pt_BR: [
		    "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
		    "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
		]
	};

// For convenience...
Date.prototype.format = function (mask, utc) {
	return dateFormat(this,mask, utc); 
};

