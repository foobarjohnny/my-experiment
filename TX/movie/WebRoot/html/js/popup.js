function PopupUtil() {}
PopupUtil.prototype = {
		contentDiv: "",
		contentList: [],
		topOfBk : 0,
		heightofBk : 0,
		supportMultiple : 0,
		
		supportMultiplePopup : function()
		{
			this.supportMultiple = 1;
		},
		
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
			this.init(contentDiv, bkDiv);
			this.centerInternal(contentDiv);
			this.specialForScout(contentDiv);
			
		},
		
		hasPopup : function()
		{
			if(this.contentList.length > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		},
		
		init : function (contentDiv, bkDiv)
		{
			if(!this.supportMultiple && this.hasPopup())
			{
				return;
			}
			
			var contentDivId = "#" + contentDiv;
			var bkDivId = "#" + bkDiv;
			this.contentList.push(contentDiv);
			$(contentDivId).show();
			
			if("" != bkDiv)
			{
				$(bkDivId).show();
				$(bkDivId).css({
					"opacity": "0.7"
				});
				$(bkDivId).fadeIn("slow");
			}
			$(contentDivId).fadeIn("slow");
			this.contentDiv= contentDiv;
		},
		
		center : function()
		{
			if(!this.hasPopup()){
				return;
			}
			
			for(var i=0;i<this.contentList.length;i++)
			{
				var tempContentDiv = this.contentList[i];
				this.centerInternal(tempContentDiv);
			}
		},
		
		centerInternal : function (contentDiv)
		{
			var contentDivId = "#" + contentDiv;
				windowWidth = CommonUtil.getScreenWidth(),
			 	windowHeight = CommonUtil.getScreenHeight(),
				cssName = contentDiv,
				position = "fixed";
			//change css base on portait or landscape
			if(CommonUtil.isLandscape())
			{
				cssName = contentDiv + "Landscape";
			}
			if("loadingPopup" == contentDiv)
			{
				cssName += " clsLoadingPopupBk";
				position = "absolute";
			}
			document.getElementById(contentDiv).className = cssName;
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
			
			var zIndex = this.contentList.length;
			$("#backgroundPopup").css({
				"height": CommonUtil.getContentHeight(),
				"z-index": zIndex
			});

			//centering
			$(contentDivId).css({
				"position": position,
				"top": topPosition,
				"left": (windowWidth-popupWidth)/2,
				"z-index": zIndex+1
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
			
			var bkDivId = "#backgroundPopup";
			
			var currentContent = this.contentList.pop();
			if(this.hasPopup())
			{
				this.contentDiv = this.contentList[this.contentList.length-1];
			}
			var contentDivId = "#" + currentContent;
			//disables popup only if it is enabled
			$(bkDivId).fadeOut("slow");
			$(contentDivId).fadeOut("slow");
		}
};
var PopupUtil = new PopupUtil();
