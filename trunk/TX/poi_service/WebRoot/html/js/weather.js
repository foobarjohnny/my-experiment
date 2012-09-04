function Weather(daylight){
	this.daylight = daylight; 
};

Weather.prototype = {
		//add touch and click event listener for changeLocationIcon
		init : function(){
			var changeLocationIcon = document.getElementById("changeLocationIcon");
			changeLocationIcon.addEventListener("touchstart", function(e){
				this.className = "change_location_button_focused";
			}, false);
			changeLocationIcon.addEventListener("touchend", function(e){
				this.className = "change_location_button_unfocused";
			}, false);
			$("#changeLocationIcon").click(SDKAPI.captureAddress);
		},
		
		getSearchLocation : function(){
			var addressStr = sessionStorage.getItem("SESSION_STORAGE_WEATHER_SEARCH_LOCATION");
			return addressStr;
		},
		
		setSearchLocation : function(data){
			sessionStorage.setItem("SESSION_STORAGE_WEATHER_SEARCH_LOCATION",data);
		},
		
		refreshData : function(){
			SDKAPI.getCurrentLocation(this.getGPSBack);
		},
		
		getGPSBack : function(position){
			CommonUtil.debug("captureAddress: "+JSON.stringify(position));
			var address = CommonUtil.processGpsLocation(position);
			Weather.preprocessWeather(address);
		},
		
		preprocessWeather : function(address){
			Weather.setSearchLocation(JSON.stringify(address));
			Weather.setDaylight(address);
			SDKAPI.getPreference(this.afterGetDistnaceUnit,PreferenceConstants.DISUNIT);
		},
		
		afterGetDistnaceUnit : function(distanceUnit){
			CommonUtil.debug("distanceUnit get from client: "+distanceUnit);
			Weather.processWeather(distanceUnit);
		},
		
		processWeather : function(distanceUnit){
			var addressStr = this.getSearchLocation();
			if (ClientInfo.platform == "TNVIEW_LINUX") {
				PopupUtil.showLoading();
			}else {
				PopupUtil.show("loadingPopup", "");
			}
			var ajxUrl = GLOBAL_hostUrl + "WeatherFetch.do?addressString=" + addressStr + "&distanceUnit=" + distanceUnit +"&" + CommonUtil.getClientInfo();
			var ajaxOptions = {
					url:ajxUrl,
					loadingStyle:1,
					onSuccess:this.ajaxCallBack,
					onError:this.ajaxErrCallBack,
					timeout:7000
			};
			if (navigator.onLine) {
				CommonUtil.ajax(ajaxOptions);
			}else{
				CommonUtil.noNetworkError();
			}
		},
		
		formateCityName : function(address){
			var cityStr = address.city.toLowerCase(),
				reg  = /\b(\w)|\s(\w)/g;
			cityStr = cityStr.replace(reg,function(m){return m.toUpperCase();});
			return cityStr;
		},
		
		ajaxCallBack : function(result){
	        Weather.displayData(result); 
		},
		ajaxErrCallBack : function(){
			CommonUtil.noNetworkError();
		},
		
		displayData : function(result){
			var today =  JSON.parse(result.today);
			var weekList = JSON.parse(result.weekList); 
			var FUnit = today.unit;
			var noFUnit = FUnit.charAt(0);
			
			$("#title").html(today.titleWithoutState);
			$("#temp").html(today.temp+ FUnit);
			$("#todayDate").html("<b>" +today.todayDate+"</b>");
			$("#status").html(today.status);
			
			var bigImageString = today.imageWeatherBig.toString();
			var bigImage = bigImageString.replace(".png","");
			if(hasNightImage&&!this.daylight){
				bigImage += "_night"; 
			}
			
			var bigImageClass = "weatherBig " + bigImage;
			$("#imageWeatherBig").html("<img class='"+bigImageClass+"' />");
			$("#feel").html(today.feel);
			$("#wind").html(today.wind);
			$("#humidity").html(today.humidity+"%");
			 
			var offsetFlag = 0;
			var weekListSize = weekList.length-1;
			
			if(today.shortWeekDesc == weekList[0].shortWeekDesc){
				$("#highLabel").html("<b>"+weekList[0].high+ noFUnit +"</b> ");
				$("#lowLabel").html(weekList[0].low+noFUnit);
				offsetFlag = 1;
				weekListSize = weekList.length;
			}
		
			var pageText = "<div class='div_table clsWeekly'>";
			var j=0;
			for(var i=offsetFlag;i<weekListSize;i++){
				var smallImageString = weekList[i].imageWeatherSmall.toString();
				var smallImage = smallImageString.replace(".png","");
				if(hasNightImage&&!this.daylight){
					smallImage += "_night"; 
				}
				var smallImageClass = "weatherSmall " + smallImage;
				
				var rowClass = "clsWeatherItem" + j;
				j++;
				pageText +="<div class='div_row weatherBarDiv' ><div class='div_cell weatherBar'></div><div class='div_cell weatherBar'></div><div class='div_cell weatherBar'></div></div>" + 
							"<div class='" +'div_row ' + rowClass + "'>"+
								"<div class='div_cell align_left weekAlign'><b class='fs_veryLarge'>"+weekList[i].shortWeekDesc+"</b></div>"+
								"<div class='div_cell align_center'><img class='"+smallImageClass+"' /></div>"+
								"<div class='div_cell align_center'>" +
									"<span><b class='fs_veryLarge'> "+weekList[i].high+ noFUnit+"</b></span>" +
									"<span><b class='clsScoutGray fs_veryLarge'> "+weekList[i].low+ noFUnit+"</b></span>" +
								"</div>"+
							"</div>";
			}
			pageText +="</div>";
			$("#weeklyWeatherDiv").html(pageText);
		},
		
		getExitFlag : function(){
			var exit = true;
			var Request= CommonUtil.getRequest();   
			var extraFlag = Request["animation"];
			if("true" == extraFlag){
				exit = false;
			}
			CommonUtil.debug("exitWebView:" + exit);
			return exit;
		},
		
		isDaylight : function(lat, lon, date ){
		    if(!!lat && !!lon && !!date){
		        var ss = new SunriseSunset( date.getFullYear(), date.getMonth() + 1, date.getDate(), lat, lon);
		        var UTCHours = date. getUTCHours() + ((date. getUTCMinutes() * 60 + date. getUTCSeconds())/3600);
		        return ss.isDaylight(UTCHours);
		    }
		    return true;
		},
		
		setDaylight : function(address){
			var lat = address["lat"] / 1.e5;
			var lon = address["lon"] / 1.e5;
			this.daylight = this.isDaylight(lat, lon, new Date());
		}
};

var Weather = new Weather();

function SDK_API_captureAddressCallBack(address){	
	//the city name get form client is upper case.
	if(address.city){
		address.city = Weather.formateCityName(address);
	}
	Weather.preprocessWeather(address);
}

function SunriseSunset( utcFullYear, utcMonth, utcDay, latitude, longitude ) {
    this.zenith = 90 + 50/60; //   offical      = 90 degrees 50'
                              //   civil        = 96 degrees
                              //   nautical     = 102 degrees
                              //   astronomical = 108 degrees

    this.utcFullYear = utcFullYear;
    this.utcMonth = utcMonth;
    this.utcDay = utcDay;
    this.latitude = latitude;
    this.longitude = longitude;

    this.rising = true; // set to true for sunrise, false for sunset
    this.lngHour = this.longitude / 15;

    this.sin = function( deg ) { return Math.sin( deg * Math.PI / 180 ); };
    this.cos = function( deg ) { return Math.cos( deg * Math.PI / 180 ); };
    this.tan = function( deg ) { return Math.tan( deg * Math.PI / 180 ); };
    this.asin = function( x ) { return (180/Math.PI) * Math.asin(x); };
    this.acos = function( x ) { return (180/Math.PI) * Math.acos(x); };
    this.atan = function( x ) { return (180/Math.PI) * Math.atan(x); };

    this.getDOY = function() {
        var month = this.utcMonth;
        var year = this.utcFullYear;
        var day = this.utcDay;

        var N1 = Math.floor( 275 * month / 9 );
        var N2 = Math.floor( (month + 9) / 12 );
        var N3 = (1 + Math.floor((year - 4 * Math.floor(year / 4 ) + 2) / 3));
        var N = N1 - (N2 * N3) + day - 30;
        return N;
    };

    this.approximateTime = function() {
        var doy = this.getDOY();
        if ( this.rising ) {
            return doy + ((6 - this.lngHour) / 24);
        } else {
            return doy + ((18 - this.lngHour) / 24);
        }
    };

    this.meanAnomaly = function() {
        var t = this.approximateTime();
        return (0.9856 * t) - 3.289;
    };

    this.trueLongitude = function() {
        var M = this.meanAnomaly();
        var L = M + (1.916 * this.sin(M)) + (0.020 * this.sin(2 * M)) + 282.634;
        return L % 360;
    };

    this.rightAscension = function() {
        var L = this.trueLongitude();
        var RA = this.atan(0.91764 * this.tan(L));
        RA %= 360;

        var Lquadrant  = (Math.floor( L/90)) * 90;
        var RAquadrant = (Math.floor(RA/90)) * 90;
        RA = RA + (Lquadrant - RAquadrant);
        RA /= 15;

        return RA;
    };

    this.sinDec = function() {
        var L = this.trueLongitude();
        var sinDec = 0.39782 * this.sin(L);
        return sinDec;
    };

    this.cosDec = function() {
        return this.cos(this.asin(this.sinDec()));
    };

    this.localMeanTime = function() {
        var cosH = (this.cos(this.zenith) - (this.sinDec() * this.sin(this.latitude)))
            / (this.cosDec() * this.cos(this.latitude));

        if (cosH >  1) {
            return "the sun never rises on this location (on the specified date)";
        } else if (cosH < -1) {
            return "the sun never sets on this location (on the specified date)";
        } else {
            var H = this.rising ? 360 - this.acos(cosH) : this.acos(cosH);
            H /= 15;
            var RA = this.rightAscension();
            var t = this.approximateTime();
            var T = H + RA - (0.06571 * t) - 6.622;
            return T;
        }
    };

    this.UTCTime = function() {
        var T = this.localMeanTime();
        var UT = T - this.lngHour;
        return UT % 24;
    };

    this.sunriseUtcHours = function() {
        this.rising = true;
        return this.UTCTime();
    };

    this.sunsetUtcHours = function() {
        this.rising = false;
        return this.UTCTime();
    };

    this.hoursRange = function( h ) {
        if ( h >= 24 ) {
            return h - 24;
        } else if ( h < 0 ) {
            return h + 24;
        } else {
            return h;
        }
    };

    this.sunriseLocalHours = function(gmt) {
        return this.hoursRange( gmt + this.sunriseUtcHours() );
    };

    this.sunsetLocalHours = function(gmt) {
        return this.hoursRange( gmt + this.sunsetUtcHours() );
    };

    this.isDaylight = function( utcCurrentHours ) {
        var sunriseHours = this.hoursRange(this.sunriseUtcHours());
        var sunsetHours =this.hoursRange(this.sunsetUtcHours());

        //print( "rise", sunriseHours );
        //print( "set", sunsetHours );

        if ( utcCurrentHours < sunriseHours &&
             utcCurrentHours < sunsetHours && sunriseHours > sunsetHours) {
            return true;
        }

        if ( utcCurrentHours >= sunriseHours ) {
            if ( sunsetHours < sunriseHours ) {
                // Sunrise time we have is from yesterday in UTC
                return true;
            }

            return utcCurrentHours < sunsetHours;
        }

        return false;
    };
}
