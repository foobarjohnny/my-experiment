function TimePicker(config) {
	this.init(config);
}

TimePicker.prototype = {
	time : new Date(),
	hour : 0,
	minute : 0,
	zone : "AM",
	timeDisplay : "06:30PM",
	containerDiv : "timePickerDiv",
	timeContainerOutSide : "openTableCheckInTime",
	backGroundDiv: "slowBackgroundPopup",
	intervalId : null,
	INTERVAL : 300,

	buildWidget : function() {
		var html ='<div class="popGreyWindowBg" width="100%">' 
				+'	<table class="timePickerMainBoard " cellpadding="0" cellspacing="0" border="0">'
				+ '		<tr>'
				+ '			<td colspan="3" align="center" class="pickerTitle">'
				+ '								Set Time'					
				+ '			</td>'
				+ '		</tr>'
				+ '		<tr>'
				+ '			<td>'
				+ '				<table align="center"  cellpadding="0" cellspacing="5" border="0" width="90%">'
				+ '					<tr>'
				+ '						<td align="center" width="33%">'
				+ '							<div class="clsPickerBtnContainer">'
				+ '								<table width="100%" height="100%">'
				+ '									<tr width="100%" height="100%">'
				+ '										<td id="hourPlus" class="pickerBtnSkeleton clsPickerBtnBgNormal pickerBtnRadiusTop fc_gray"'
				+ ' 										onTouchStart="Global_timePicker.continousHourPlus();Global_timePicker.highlight(this)" '
				+'                                            onclick="Global_timePicker.hourPlus()" onTouchEnd="Global_timePicker.stopContiusOperation(this);Global_timePicker.dishighlight(this)">+</td>'
				+ '									</tr>		'
				+ '								</table>'
				+ '							</div>'
				+ '							<table  class="valueArea">'
				+ '								<tr width="100%" height="100%">'
				+ '									<td valign="middle" align="center">'
				+ '										<span id="hourValueDisplay"></span>'
				+ '									</td>'
				+ '								</tr>'
				+ '							</table>'
				+ '							<div class="clsPickerBtnContainer">'
				+ '								<table width="100%" height="100%">'
				+ '									<tr width="100%" height="100%">'
				+ '										<td id="hourMinus" class="pickerBtnSkeleton clsPickerBtnBgNormal pickerBtnRadiusBottom fc_gray" onTouchStart="Global_timePicker.continousHourMinus(this);Global_timePicker.highlight(this)" onclick="Global_timePicker.hourMinus()" onTouchEnd="Global_timePicker.stopContiusOperation(this);Global_timePicker.dishighlight(this)">-</td>'
				+ '									</tr>'
				+ '								</table>'
				+ '							</div>'
				+ '						</td>'
				+ '						<td align="center" width="33%">'
				+ '							<div class="clsPickerBtnContainer">'
				+ '								<table width="100%" height="100%">'
				+ '									<tr width="100%" height="100%">'
				+ '										<td id="minPlus" class="pickerBtnSkeleton clsPickerBtnBgNormal pickerBtnRadiusTop fc_gray" onTouchStart="Global_timePicker.continousMinutePlus(this);Global_timePicker.highlight(this)" onclick="Global_timePicker.minPlus()" onTouchEnd="Global_timePicker.stopContiusOperation(this);Global_timePicker.dishighlight(this)">+</td>'
				+ '									</tr>		'
				+ '								</table>'
				+ '							</div>'
				+ '							<table  class="valueArea">'
				+ '								<tr width="100%" height="100%">'
				+ '									<td valign="middle" align="center">'
				+ '										<span id="minuteValueDisplay"></span>'
				+ '									</td>'
				+ '								</tr>'
				+ '							</table>'
				+ '							<div class="clsPickerBtnContainer">'
				+ '								<table width="100%" height="100%">'
				+ '									<tr width="100%" height="100%">'
				+ '										<td id="minMinus" class="pickerBtnSkeleton clsPickerBtnBgNormal pickerBtnRadiusBottom fc_gray" onTouchStart="Global_timePicker.continousMinuteMinus(this);Global_timePicker.highlight(this)" onclick="Global_timePicker.minMinus()" onTouchEnd="Global_timePicker.stopContiusOperation(this);Global_timePicker.dishighlight(this)">-</td>'
				+ '									</tr>'
				+ '								</table>'
				+ '							</div>'
				+ '						</td>'
				+ '						<td align="center" width="33%">'
				+ '							<div class="clsPickerBtnContainer">'
				+ '								<table width="100%" height="100%">'
				+ '									<tr width="100%" height="100%">'
				+ '										<td id="zoneValue" class="pickerBtnSkeleton clsPickerBtnBgNormal pickerBtnRadius fc_gray" onClick="Global_timePicker.changeZone()" onTouchStart="Global_timePicker.highlight(this)" onTouchEnd="Global_timePicker.dishighlight(this)">PM</td>'
				+ '									</tr>'
				+ '								</table>'
				+ '							</div>'
				+ '						</td>'
				+ '				</tr>'
				+ '			</table>'
				+ '		</td>'
				+ '	</tr>'
				+ '</table>'
				+ '</div>'
				+ '<table class="popGreyWindowBottomRowBg pickerBottomBar" >'
				+ '	<tr >'
				+ '		<td width="100%" align="center">'
				+ '			<table width="90%" height="80%">'
				+ '				<tr>'
				+ '					<td width="50%">'
				+ '								<input type="button" id="set" class="pickerBtnSkeleton clsPickerBtnBgNormal pickerBtnRadius fc_gray" onclick="Global_timePicker.done()" value="Set" onTouchStart="Global_timePicker.highlight(this)" onTouchEnd="Global_timePicker.dishighlight(this)">'
				+ '					</td>'

				+ '					<td width="50%">'
				+ '								<input type="button" id="close" class="pickerBtnSkeleton clsPickerBtnBgNormal pickerBtnRadius fc_gray" onClick="Global_timePicker.cancel()" value="Cancel" onTouchStart="Global_timePicker.highlight(this)" onTouchEnd="Global_timePicker.dishighlight(this)" />'
				+'					</td>' 
				+ '				</tr>' 
				+ '			</table>' 
				+ '		</td>' 
				+ '	</tr>'
				+ '	</table>';
		document.getElementById("timePickerDiv").innerHTML = html;
	},

	init : function(config) {
		if(config){
			this.containerDiv = config["containerDiv"];
			this.timeContainerOutSide = config["timeContainerOutSide"];
			this.backGroundDiv = config["backGroundDiv"];
		}

		this.buildWidget();

		this.time = new Date();
		this.hour = this.time.getHours();
		this.minute = this.time.getMinutes();
		this.hour > 12 ? this.zone = "PM" : this.zone = "AM";

		this.flush();
	},

	flush : function() {
		this.hour = this.time.getHours();
		this.minute = this.time.getMinutes();
		this.hour > 11 ? this.zone = "PM" : this.zone = "AM";


		$("#hourValueDisplay").html(this.hour > 12 ? this.addZero(this.hour - 12) : this.addZero(this.hour));
		$("#minuteValueDisplay").html(this.addZero(this.minute));
		
		$("#zoneValue").html(this.zone);
		$("#" + this.timeContainerOutSide).html(
				(this.hour > 12 ? this.addZero(this.hour - 12) : this.addZero(this.hour)) + ":"
						+ this.addZero(this.minute) + "  " + this.zone);
	},
	addZero : function(data) {
		if (data < 10) {
			return "0" + data;
		} else {
			return data;
		}
	},
	hourPlus : function() {
		console.log("hourPlus");
		var newTime = this.time.getTime() + 60 * 60 * 1000;
		this.time.setTime(newTime);

		this.flush();
	},
	hourMinus : function() {
		console.log("hourMinus");
		this.time.setTime(this.time.getTime() - 60 * 60 * 1000);
		this.flush();
	},
	minPlus : function() {
		console.log("minPlus");
		this.time.setTime(this.time.getTime() + 60 * 1000);
		this.flush();
	},
	minMinus : function() {
		console.log("minPlus");
		this.time.setTime(this.time.getTime() - 60 * 1000);
		this.flush();
	},

	continousHourPlus : function() {
		this.stopContiusOperation();
		console.log("continousHourPlus");
		var self = this;
		this.intervalId = window.setInterval(function() {
			self.hourPlus();
		}, this.INTERVAL);
	},

	continousMinutePlus : function() {
		
		this.stopContiusOperation();
		console.log("continousMinutePlus");
		var self = this;
		this.intervalId = window.setInterval(function() {
			self.minPlus();
		}, this.INTERVAL);
	},

	continousHourMinus : function() {
		
		this.stopContiusOperation();
		console.log("continousHourMinus");
		var self = this;
		this.intervalId = window.setInterval(function() {
			self.hourMinus();
		}, this.INTERVAL);
	},

	continousMinuteMinus : function() {
		this.stopContiusOperation();
		console.log("continousMinuteMinus");
		var self = this;
		this.intervalId = window.setInterval(function() {
			self.minMinus();
		}, this.INTERVAL);
	},

	stopContiusOperation : function() {
		console.log("stopContiusOperation");
		clearInterval(this.intervalId);
		this.intervalId = null;
	},
	
	getTime: function(){
		return this.getHourWithZero() + ":" + this.getMinuteWithZero();
	},

	getHour : function() {
		return this.hour;
	},

	getMinute : function() {
		return this.minute;
	},

	getHourWithZero : function() {
		return this.addZero(this.hour);
	},
	getMinuteWithZero : function() {
		return this.addZero(this.minute);
	},
	changeZone : function() {
		var newTime = this.time.getTime() + 12 * 60 * 60 * 1000;
		this.time.setTime(newTime);
		this.flush();
	},
	cancel : function() {
		$("#" + this.containerDiv).hide();
		$("#" + this.backGroundDiv).hide();
	},
	show : function() {
		$("#" + this.containerDiv).attr("class", "timepickerBody");
		$("#"+ this.backGroundDiv).attr("class", "slowBackgroundPopup");
		
		$("#" + this.containerDiv).show();
		$("#"+ this.backGroundDiv).show();
	},
	done : function() {
		$("#" + this.containerDiv).hide();
		$("#" + this.backGroundDiv).hide();
	},
	highlight:function(element){
		var oldClassName = element.className;
		oldClassName = oldClassName.replace(/clsPickerBtnBgNormal/g,"clsPickerBtnBgHL");
		oldClassName = oldClassName.replace(/fc_gray/g,"fc_white");
		element.className=oldClassName;
	},
	dishighlight:function(element){
		var oldClassName = element.className;
		oldClassName = oldClassName.replace(/clsPickerBtnBgHL/g,"clsPickerBtnBgNormal");
		oldClassName = oldClassName.replace(/fc_white/g,"fc_gray");
		element.className=oldClassName;
	}
};

var Global_timePicker = null;
