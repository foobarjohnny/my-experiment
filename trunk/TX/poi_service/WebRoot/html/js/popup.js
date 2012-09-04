function PopupUtil() {}
PopupUtil.prototype = {
		contentDiv: "",
		topOfBk : 0,
		heightofBk : 0,
		
		showLoading : function()
		{
			this.show("loadingPopup", "backgroundPopup");
		},
		
		showPoiLoading : function(topOfBk,heightofBk)
		{
			this.topOfBk = topOfBk;
			this.heightofBk = heightofBk;
			this.show("loadingPopup", "");
		},

		show : function (contentDiv, bkDiv)
		{
			this.osDiffer();
			this.init(contentDiv, bkDiv);
			this.centerInternal(contentDiv);
			this.specialForScout(contentDiv);
			//disable touchmove event
			//document.body.addEventListener("touchmove", this.stopMove, false);
		},
		
		stopMove : function()
		{
			event.preventDefault();
			event.stopPropagation();
			return false;
		},
		
		hasPopup : function()
		{
			return (this.contentDiv != "");
		},
		
		init : function (contentDiv, bkDiv)
		{
			if(this.hasPopup())
			{
				this.hide();
			}
			
			var bk = $("#" + bkDiv),
			 	ct = $("#" + contentDiv);
			ct.show();
			
			if("" != bkDiv)
			{
				bk.show();
				bk.css({
					"opacity": "0.7"
				});
				bk.fadeIn("slow");
			}
			ct.fadeIn("slow");
			this.contentDiv= contentDiv;
		},
		
		center : function()
		{
			if(!this.hasPopup()){
				return;
			}

			this.centerInternal(this.contentDiv);
		},
		
		centerInternal : function (contentDiv)
		{
			var contentDivId = "#" + contentDiv;
				windowWidth = CommonUtil.getScreenWidth(),
			 	windowHeight = CommonUtil.getScreenHeight(),
				cssName = contentDiv,
				position = "fixed";
				
			if("loadingPopup" == contentDiv)
			{
				cssName += " clsLoadingPopupBk";
				position = "absolute";
			}
			if(contentDiv){
				document.getElementById(contentDiv).className = cssName;
			}
			
			//centering
			var popupHeight = $(contentDivId).height(),
				popupWidth = $(contentDivId).width(),
				topPosition = 0;
			if("nickNameInputPopup" == contentDiv)
			{
				topPosition = 10;
			}
			else
			{
				if(this.topOfBk != 0)
				{
					topPosition = this.topOfBk + (this.heightofBk-popupHeight)/2;
				}
				else
				{
					topPosition = (windowHeight-popupHeight)/2;
				}
			}
			
			$("#backgroundPopup").css({
				"height": CommonUtil.getContentHeight(),
				"z-index": 1
			});

			//centering
			$(contentDivId).css({
				"position": position,
				"top": topPosition,
				"left": (windowWidth-popupWidth)/2,
				"z-index": 2
			});
		},
		
		specialForScout : function(contentDiv)
		{
			//add by jiangxl 2011.11.01 for inside point img
			if(CommonUtil.isScoutStyle() && contentDiv == "loadingPopup"){
				var insideBKHtml = '<div class="clsLoadingPopupInsideBk"></div>';
				$("#"+contentDiv).html(insideBKHtml);
			}
		},
		
		hide : function ()
		{
			if(!this.hasPopup())
			{
				return;
			}
			//disables popup only if it is enabled
			$("#backgroundPopup").fadeOut("slow");
			$("#" + this.contentDiv).fadeOut("slow");
			document.body.removeEventListener("touchmove", this.stopMove, false);
			this.contentDiv = "";
		},
		
		osDiffer : function ()
		{
			try{
				if(device){
					var version = parseInt(device.version.charAt(0));
					if(CommonUtil.isIphone() && version >= 5){
						//do nothing
					}else{
						window.scrollTo(0,0);
					}
				}
			}catch(e){}
		}
};
var PopupUtil = new PopupUtil();
