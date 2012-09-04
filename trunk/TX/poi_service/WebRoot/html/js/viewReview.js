
function initReviewerPage(reviewerTemp) {
	var reviewResults = reviewerTemp.ratingProperties;
	var rName = CommonUtil.convertChar(reviewerTemp.reviewerName);
	if(!rName){
		rName = I18NHelper["review.anonymous"];
	}
	$("#reviewerName").html(rName);
	diplayRatingIcon("ratingImg", reviewerTemp.rating, "big");
	var comments = CommonUtil.convertChar(reviewerTemp.comments);
	comments = comments? comments.replace(/\n/g,"<br>"):comments;
	if(!comments){
		comments = I18NHelper["review.no.Comment"];
	}
	$("#reviewComment").html(comments);
	
	if (reviewResults != null) {
		var reviewOptionsText = "<div class='div_table clsFixTable'>";
		var length = 0;
		$.each(reviewResults, function (name, value) {
			length++;
		});
		var everyWidth = '100%';
		if(length != 0)
		{
			everyWidth = (100/length - 1) + "%";
		}
		var index = 0;
		$.each(reviewResults, function (name, value) {
		
		reviewOptionsText += "<div class='div_cell'  style='width:" + everyWidth + "' >" + "<div class='div_table align_center'>" +                "<div class='div_row'><div class='div_cell fs_small fc_gray fw_bold'>" + name + "</div></div>" + "<div class='div_row'><div class='div_cell' id='valueImage'>";

			reviewOptionsText += reviewNum2ReviewImg(value);
			reviewOptionsText += "</div></div>";
			reviewOptionsText += "<div class='div_row'><div class='div_cell align_center fs_small fc_gray' id='reviewOptionValue'>" + reviewNum2ReviewStr(value) + "</div></div>";
			reviewOptionsText += "</div>" + "</div>";
			if (index != length-1) {
				reviewOptionsText += "<div class='div_cell halvingLine'></div>";
			}
			index++;
		});
		reviewOptionsText += "</div>";
		$("#reviewOptions").html(reviewOptionsText);
	}else{
		$("#reviewOptions").css("height",0);
	}
}


function reviewNum2ReviewStr(value) {
	if (value > 0) {
		return I18NHelper["review.like"];
	} else {
		return I18NHelper["review.dislike"];
	}
}
function reviewNum2ReviewImg(value) {
	if (value > 0) {
		return "<img class='evaluation good_icon_unfocused'  />";
	} else {
		return "<img class='evaluation bad_icon_unfocused'  />";
	}
}
function initReviewerData() {
	var reviewerTemp = JSON.parse(PoiCacheHelper.getCurrentReviewer());
	if (reviewerTemp != null) {
		initReviewerPage(reviewerTemp);
	}
}
