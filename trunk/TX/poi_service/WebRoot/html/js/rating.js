function getRatingIcons(rating, iconSize) {
	var ratingIcons = "";
	var imgCSS = "";

	ratingIcons += "<img class='"+ getRatingIconName(rating, iconSize) + "'/>";
	ratingIcons += "<img class='"+ getRatingIconName(rating - 10, iconSize) + "'/>";
	ratingIcons += "<img class='"+ getRatingIconName(rating - 20, iconSize) + "'/>";
	ratingIcons += "<img class='"+ getRatingIconName(rating - 30, iconSize) + "'/>";
	ratingIcons += "<img class='"+ getRatingIconName(rating - 40, iconSize) + "'/>";
	return ratingIcons;
}

function getRatingIconName(rating, iconSize) {
	var vacancyIcon = "vacancy_";
	var semiIcon = "semi_";
	var fullIcon = "full_";
	// small/medium/big
	vacancyIcon += iconSize + "_star_icon_unfocused";
	semiIcon += iconSize + "_star_icon_unfocused";
	fullIcon += iconSize + "_star_icon_unfocused";
	/*
	if (fullName) {
		vacancyIcon += ".png";
		semiIcon += ".png";
		fullIcon += ".png";

		vacancyIcon = GLOBAL_imageCommonUrl + vacancyIcon;
		semiIcon = GLOBAL_imageCommonUrl + semiIcon;
		fullIcon = GLOBAL_imageCommonUrl + fullIcon;
	}*/

	var iconName;

	if (0 >= rating) {
		iconName = vacancyIcon;
	} else if (rating < 10) {
		//do this in purpose to make it consistent with poi list
		//client does not support semi icon
		iconName = vacancyIcon;
	} else {
		iconName = fullIcon;
	}

	return iconName;
}
function diplayRatingIcon(iconId, rating, iconSize) {
	$("#" + iconId).html(getRatingIcons(rating, iconSize));
}