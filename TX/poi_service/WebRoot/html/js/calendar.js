function TNDatePicker(config) {
	this.setConfig(config);
	this.flushCal();
}

TNDatePicker.prototype = {
	type: "double",
	container : null,
	checkInDateTarget : null,
	checkOutDateTarget : null,
	todayDate : null,
	checkInDate : new Date(),
	checkOutDate : null,
	mArr : null,
	wArr : null,
	month : null,
	year : null,
	hasPreMonth : false,
	hasNextMonth : true,
	currentOperation : null, // 1 for checkIn , 0 for checkOut

	setConfig : function(config) {
		if (!config || !config["type"]) {
			alert("Please indicate the type of calendar, it should be 'single' or 'double'");
		}
		
		this.type = config["type"];
		
		if (!config || !config["container"]) {
			alert("Please indicate the id of the container for the calendar");
		}
		if (!config || !config["checkInDateTarget"]) {
			alert("Please indicate the id of the check In Date Container");
		}
		if (config["type"]=="double"&&(!config || !config["checkOutDateTarget"])) {
			alert("Please indicate the id of the check Out Date Container");
		}
		
		
		this.container = config["container"];
		this.checkInDateTarget = config["checkInDateTarget"];
		this.checkOutDateTarget = config["checkOutDateTarget"];
		this.todayDate = new Date();
		this.mArr = (config["mArr"] ? config["mArr"] : [ 'January', 'February',
				'March', 'April', 'May', 'June', 'July', 'August', 'September',
				'October', 'November', 'December' ]);
		this.wArr = (config["wArr"] ? config["wArr"] : [ "Sun", "Mon", "Tue",
				"Wed", "Tur", "Fri", "Sat" ]);
		this.month = (config["month"] ? config["month"] : this.todayDate
				.getMonth() + 1);
		this.year = (config["year"] ? config["year"] : this.todayDate
				.getFullYear());
		
		this.checkInDate.setHours(1, 1, 1, 1);

		$("#" + this.checkInDateTarget).html(this.dateToStr(this.checkInDate));
	},
	
	isDouble: function(){
		return this.type == "double";
	},

	flushCal : function() {
		$("#" + this.container).html(
				this.buildCal(this.mArr, this.wArr, this.month, this.year));
	},

	calChangeCSS : function(element, newCssName) {
		element.className = newCssName;
	},

	nextMonth : function() {
		if (this.month < 12) {
			this.month += 1;
		} else {
			this.month = 1;
			this.year += 1;
		}
		this.hasPreMonth = true;
		this.hasNextMonth = true;
		this.flushCal();
	},

	strToDate : function(str) {// like 2001-06-30
		var arr = str.split('-');
		var date = new Date();
		date.setFullYear(arr[0], arr[1] - 1, arr[2]);
		date.setHours(1, 1, 1, 1);
		return date;
	},

	dateToStr : function(date) {
		var dateStr = "";
		if (this.dayDiff(date, this.todayDate) == 0) {
			//dateStr = "Today, ";
			dateStr = "";
		}

		dateStr += this.addZero(date.getMonth() + 1) + "/"
				+ this.addZero(date.getDate()) + "/"
				+ (date.getFullYear() + "").substring(2, 4);
		return dateStr;
	},

	addZero : function(num) {
		if (num < 10) {
			return "0" + num;
		} else {
			return num;
		}

	},

	getTomorrow : function() {
		var tomorrow = new Date();
		tomorrow.setDate(tomorrow.getTime() + 1000 * 60 * 60 * 24);
		return tomorrow;
	},

	getDaySpan : function(checkIn, checkOut) {
		var diffMs = checkOut.getTime() - checkIn.getTime();
		console.log("DiffMs:" + diffMs / (1000 * 60 * 60 * 24));
		var diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));
		return diffDays;
	},

	preMonth : function() {
		if (!this.hasPreMonth) {
			return;
		}

		if (this.year == this.todayDate.getFullYear()
				&& this.month <= this.todayDate.getMonth() + 1) {
			this.hasPreMonth = false;
		} else {
			if (this.month == 1) {
				this.month = 12;
				this.year -= 1;
			} else {
				this.month -= 1;
			}
			this.hasPreMonth = true;
		}
		this.hasNextMonth = true;
		this.flushCal();
	},

	highlightPreIcon : function() {
		$("#preImg")
				.attr("class","hotel_previous_arrow_icon_focused");
	},

	dishighlightPreIcon : function() {
		$("#preImg").attr("class","hotel_previous_arrow_icon_unfocused");
	},

	highlightNextIcon : function() {
		$("#nextImg").attr("class","hotel_next_arrow_icon_focused");
	},

	dishighlightNextIcon : function() {
		$("#nextImg").attr("class","hotel_next_arrow_icon_unfocused");
	},

	showForCheckIn : function() {
		if (this.checkInDate) {
			console.log("Change  check in Month" + this.month);

			if (this.month != this.checkInDate.getMonth() + 1) {
				this.month = this.checkInDate.getMonth() + 1;
				if (this.month == this.todayDate.getMonth() + 1) {
					this.hasPreMonth = false;
				} else {
					this.hasPreMonth = true;
				}

				this.flushCal();
			}
		}

		$("#" + this.container).show();
		//$("#" + this.container).attr("class","popOut fullScreen");
		this.currentOperation = 1;

	},

	showForCheckOut : function() {
		// show the month of checkOutdate
		if (this.checkOutDate) {
			if (this.month != this.checkOutDate.getMonth() + 1) {
				console.log("Change Month:" + this.month);
				this.month = this.checkOutDate.getMonth() + 1;
				if (this.month == this.todayDate.getMonth() + 1) {
					this.hasPreMonth = false;
				} else {
					this.hasPreMonth = true;
				}

				this.flushCal();
			}
		}

		//$("#" + this.container).attr("class","fullScreen");
		$("#" + this.container).show();
		this.currentOperation = 0;

	},

	setCheckOutDate : function(newCheckOutDate) {
		this.checkOutDate = newCheckOutDate;
		if (!newCheckOutDate) {
			$("#" + this.checkOutDateTarget).html("&nbsp;");
		} else {
			$("#" + this.checkOutDateTarget).html(
					this.dateToStr(this.checkOutDate));
		}

		this.flushCal();
	},

	getCheckInDate : function() {
		return this.checkInDate;
	},

	getCheckOutDate : function() {
		return this.checkOutDate;
	},

	jumpToDate : function(date, n) {
		if (!date) {
			return;
		}
		var newDate = new Date();
		newDate.setTime(date.getTime() + n * 1000 * 60 * 60 * 24);

		console.log(newDate.toLocaleString());
		return newDate;
	},

	syncNightsQuantity : function() {
		if (this.checkInDate && this.checkOutDate) {
			Global_nightQuantity = this.getDaySpan(this.checkInDate,
					this.checkOutDate);
			console.log("Sync:" + Global_nightQuantity);
			$("#nightQuantity").html(Global_nightQuantity);

			$("#nightSubImg").attr("src",
					GLOBAL_sharedImageUrl + "subtract_icon_unfocused.png");
		} else {
			return;
		}
	},

	onSelectDate : function(element, dateStr) {
		var tempDate = this.strToDate(dateStr);
		console.log(dateStr);
		console.log(tempDate.toDateString());

		if (this.currentOperation == null) {
			alert("Unknown operation");
		}

		if (this.currentOperation == 1) {
			if (this.dayDiff(tempDate, this.todayDate) < 0) {
				alert("Invalid Date, You can't choose the day past.");
				return;
			}
			
			if(this.isDouble()){
				if (this.checkOutDate
						&& this.dayDiff(tempDate, this.checkOutDate) >= 0) {
					alert("Invalid Date, Check In Date Should Be Earlier Than Check Out Date");
					return;
				}
			}
			


			if (this.checkInDate) {
				$(".clsSelectedCheckInBg").attr("class", "clsDayTd gray");
			}
			element.className = "clsDayTd clsSelectedCheckInBg white";
			this.checkInDate = tempDate;
			$("#" + this.checkInDateTarget).html(
					this.dateToStr(this.checkInDate));
			CommonUtil.debug("set the check in date:"
					+ this.checkInDate.toLocaleString());

		} else {

			if (this.dayDiff(tempDate, this.todayDate) < 0) {
				alert("Invalid Date, You can't choose the day past.");
				return;
			}
			if (this.checkInDate
					&& this.dayDiff(tempDate, this.checkInDate) <= 0) {
				alert("Invalid Date, Check Out Date Should Be Later Than Check Out Date");
				return;
			}

			if (this.checkOutDate) {
				$(".clsSelectedCheckOutBg").attr("class", "clsDayTd gray");
			}

			element.className = "clsDayTd clsSelectedCheckOutBg white";
			this.checkOutDate = tempDate;
			$("#" + this.checkOutDateTarget).html(
					this.dateToStr(this.checkOutDate));
			CommonUtil.debug("set the check out date:" + this.checkOutDate);
		}
		this.syncNightsQuantity();
	},

	dayDiff : function(date1, date2) {
		var dateValue1 = date1.getFullYear() * 12 * 31 + date1.getMonth() * 31
				+ date1.getDate();
		var dateValue2 = date2.getFullYear() * 12 * 31 + date2.getMonth() * 31
				+ date2.getDate();
		return dateValue1 - dateValue2;
	},

	selectDone : function() {
		$("#" + this.container).hide();
	},

	/**
	 * 
	 * @param mArr
	 *            names of 12 months
	 * @param wArr
	 *            names of 7 days
	 * @param month
	 *            The month you wish to display, where 1=January, and
	 *            12=December.
	 * @param year
	 *            The year you wish to display.
	 * @returns {String}
	 */

	buildCal : function(mArr, wArr, month, year) {
		var dim = [ 31, 0, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 ];
		// DD replaced line to fix date bug when current day is 31st
		var oD = new Date(year, month - 1, 1);
		oD.od = oD.getDay() + 1;

		var todaydate = new Date();
		var scanfortoday = (year == todaydate.getFullYear() && month == todaydate
				.getMonth() + 1) ? todaydate.getDate() : 0;
		// calculate the days for Feb
		dim[1] = (((oD.getFullYear() % 100 != 0) && (oD.getFullYear() % 4 == 0)) || (oD
				.getFullYear() % 400 == 0)) ? 29 : 28;
		var t = '<table class="bg_title clsCalendarToolBar"><tr width="100%" height="100%"><td width="10%" ontouchstart="Global_calendar.highlightPreIcon()" ontouchend="Global_calendar.dishighlightPreIcon()" onClick="Global_calendar.preMonth();">'
				+'<img id="preImg" class="hotel_previous_arrow_icon_unfocused"/>'
				+'</td><td class="white" width="80%" height="100%">'
				+ mArr[month - 1]
				+ ' - '
				+ year
				+ '</td><td onclick="Global_calendar.nextMonth();" onTouchStart="Global_calendar.highlightNextIcon()" onTouchEnd="Global_calendar.dishighlightNextIcon()"><img id="nextImg" class="hotel_next_arrow_icon_unfocused" /></td></tr></table>'
				+ '<div class="clsCalendarMainBody clsPanelBg">'
				+ '<div class="clsShadowBar"></div>';
		t += '<table class="clsCanlendarPanel clsAllRadius" cols="7" cellpadding="0" cellspacing="0">';

		t += '<tr class="clsWeekDayRow">';
		for (s = 0; s < 7; s++) {
			t += '<td class="clsWeekDayTd">' + wArr[s] + '</td>';
		}

		t += '</tr><tr align="center" class="clsDayRow">';

		for (i = 1; i <= 42; i++) {
			var x = ((i - oD.od >= 0) && (i - oD.od < dim[month - 1])) ? i
					- oD.od + 1 : '&nbsp;';

			var isValidDate = false;
			if (x == '&nbsp;') {
				isValidDate = false;
			} else {
				var tempDate = new Date();
				tempDate.setFullYear(year, month - 1, x);
				if (this.dayDiff(tempDate, this.todayDate) >= 0) {
					isValidDate = true;
				}
			}

			var temp;
			if (x == scanfortoday) {
				temp = '<span id="today">' + x + '</span>';
			} else {
				temp = x;
			}

			var style = "";
			if (isValidDate && this.checkInDate
					&& this.dayDiff(tempDate, this.checkInDate) == 0) {
				style = "clsDayTd clsSelectedCheckInBg white";
			} else if (isValidDate && this.checkOutDate
					&& this.dayDiff(tempDate, this.checkOutDate) == 0) {
				style = "clsDayTd clsSelectedCheckOutBg white";
			} else {
				style = "clsDayTd gray";
			}

			var onClickStr = null;
			if (isValidDate) {
				onClickStr = ' onClick="Global_calendar.onSelectDate(this,\'' + this.year
						+ '-' + this.month + '-' + x + '\')"';
			} else {
				onClickStr = '';
			}

			t += '<td class="' + style + '"' + onClickStr + '>' + temp
					+ '</td>';
			if (((i) % 7 == 0) && (i < 36)) {
				if (i < 29) {
					t += '</tr><tr align="center" class="clsDayRow">';
				} else {
					t += '</tr><tr align="center" class="clsDayRow">';
				}
			}
		}

		t += '</tr></table>';
		t += '<table class="clsConfirmBar"><tr class="clsFullFilled"><td class="clsFullFilled" align="center">'
				+ '<div class="clsButtonContainer"  onclick="Global_calendar.selectDone();"><table class="clsDoneBtnSkeleton clsBtnRadius fc_gray veryLarge clsDontBtnBgNormal"  '
				+ 'onTouchStart=\'Global_calendar.calChangeCSS(this,"clsDoneBtnSkeleton clsBtnRadius white veryLarge clsDontBtnBgHL")\'  '
				+ 'onTouchEnd=\'Global_calendar.calChangeCSS(this,"clsDoneBtnSkeleton clsBtnRadius gray veryLarge clsDontBtnBgNormal")\'><tr><td class="veryLarge">'
				+ 'Done'
				+ '</td></tr></table></div>'
				+ '</td></tr></table></div></div>';
		return t;
	}
};

var Global_calendar = null;//for hotel
