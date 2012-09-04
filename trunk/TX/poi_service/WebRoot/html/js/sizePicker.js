function sizePicker() {
	this.initSize = 20;
	this.pickedSize = 1;
	this.title = "Please enter a party size:";
	this.unit = "person";
	this.units = "people";
	this.titleImgSrc = "";
	this.titleClass = "sizePickerRow  bold fc_gray fs_veryLarge";
	this.cancelBarClass = "sizePickerCancelBar clsTitleBg";
	this.cancel = "Cancel"; 
	this.set = "Set"; 
	this.container = "partySizeWidget";
	this.target = "openTablePartySize";
}

sizePicker.prototype = {
	flush:function(){
		$("#"+this.target).html( this.pickedSize+" &nbsp;"+ (this.pickedSize>1?this.units: this.unit));
	},
	setSize: function(newSize){
		this.pickedSize = newSize;
	},
	
	getSize: function(){
		return this.pickedSize;
	},
		
	init : function() {
		var html =	'<div class="popGreyWindowBg" width="100%">' 
					+'<table id="sizePickerTitleBar" class="'+this.titleClass+'">'
					+'	<tr>'
					+'		<td class="sizePickerRow">'
					+			this.title
					+'		</td>'
					+'	</tr>'
					+'</table>';
		

		html += '<table class="sizePickerRow">'
				+'	<tr>'
				+'		<td class="sizePickerRow"><input type="number" min="1" max="20" value="1" class="fc_gray sizePickerInput fs_veryLarge" id="sizePickerInput"/></td>'
				+'	</tr>'
				+'</table>'
				+'<table>'
				+'	<tr>'
				+'		<td align="center" class="sizePickerRow bold fc_gray fs_middle">Note:Maximum Party Size is 20 people</td>'
				+'	</tr>'
				+'</table>'
				+'</div>';
					

					 
		html += '<table id="sizePickerCancelBar" class="sizePickerRow popGreyWindowBottomRowBg">'
			 +	' <tr>'
			 +	'	<td  width="50%" height="100%" align="center" valign="middle">'
			 +	'		<input type="button"  value="'+this.set+'" onclick="Global_partySizePicker.done()" ontouchend="changeCSS(this,\'sizePickerBtnSize fc_gray fs_veryLarge clsLargeRadius clsButtonBgNormal\')" ontouchStart="changeCSS(this,\'sizePickerBtnSize fc_white fs_veryLarge clsLargeRadius clsButtonBgHighlight\')" class="sizePickerBtnSize fc_gray fs_veryLarge clsLargeRadius clsButtonBgNormal">'
			 +	'	</td>'
			 
			 +	'	<td  width="50%" height="100%" align="center" valign="middle">'
			 +	'		<input type="button"  value="'+this.cancel+'" onclick="Global_partySizePicker.cancle()" ontouchend="changeCSS(this,\'sizePickerBtnSize fc_gray fs_veryLarge clsLargeRadius clsButtonBgNormal\')" ontouchStart="changeCSS(this,\'sizePickerBtnSize fc_white fs_veryLarge clsLargeRadius clsButtonBgHighlight\')" class="sizePickerBtnSize fc_gray fs_veryLarge clsLargeRadius  clsButtonBgNormal">'
			 +	'	</td>'
			 
			 +	' </tr>'
			 +	'</table>';

		
		$("#"+this.container).html(html);
	},
	show: function(){
		$("#"+this.container).show();
		$("#slowBackgroundPopup").show();
	},
	done: function(){
		this.setSize($("#sizePickerInput").val());
		this.flush();
		$("#"+this.container).hide();
		$("#slowBackgroundPopup").hide();
	},
	cancle: function(){
		$("#"+this.container).hide();
		$("#slowBackgroundPopup").hide();
	}
};

var Global_partySizePicker = null;
