<div id="topPartDiv" class="clsMainDiv">
	<div id="firstLineDiv" class="clsDivFirst">
		<div class="div_table clsFixTable">
			<div class="tr">
				<div class="td fs_large fw_bold clsFontColor_blue"><div class="clsEllipsis" id="name"></div></div>
				<div align="right"  class="td clsPoiDistance fc_gray"><label class="text_cutoff fs_middle" id="distance"></label>&nbsp;</div>
				<div align="right" class="td clsFavTD" id="favHref" onClick="" ><img id="favIndicator" /></div>
			</div>
		</div>
	</div>
	<div id="secondLineDiv" class="clsDivSecond">
		<div class="div_table">
			<div class="tr" style="height:100%">
				<div class="td" style="width:22%;vertical-align:top;" align="center"><div align="center" id="logImageDiv" class="clsLogoImageDiv"></div></div>
				<div class="td" style="width:4%;">&nbsp;</div>
				<div class="td" style="vertical-align:top;" align="center">
					<div id="navButton" class="div_table clsNavButtonStyle clsNavFontColor clsLargeRadius clsNavButtonBgNormal clsFixTable" 
						onClick="PoiCommonHelper.onClickDrive(this)" ontouchstart="PoiCommonHelper.highLightNavButton(this)"
						ontouchend="PoiCommonHelper.dishighLightNavButton(this)" ontouchmove="PoiCommonHelper.dishighLightNavButton(this)">
						<div class="tr" style="height:100%;width:100%;" >
							<div class="td clsBtnPicDiv" width="17%" align="left" valign="middle"><img id="navImg" class="poi_details_driveto_icon_unfocused"/></div>
							<div class="td clsNavTextDiv clsAddressLine" align="left" valign="middle" id="address"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="thirdLineDiv" class="clsDivThird">
		<div class="div_table">
			<div class="tr">
				<div id="topReviewNo" class="td clsTdRating" onClick="clickReviewNo()">
					<div class="div_table clsFixTable" align="center">
						<div class="tr"><div class="td" align="center" style="vertical-align:top;" id="ratingIcons"></div></div>
						<div class="tr"><div align="center" style="vertical-align:bottom;" id="ratingNumber" class="td clsReviewNo clsReviewColor"></div></div>
					</div>
				</div>
				<div class="td" style="width:4%;">&nbsp;</div>
				<div class="td clsTdPhone" >
					<div id="phoneButton" ontouchstart="PoiCommonHelper.highLightPhone(this)"
							ontouchend="PoiCommonHelper.dishighLightPhone(this)" ontouchmove="PoiCommonHelper.dishighLightPhone(this)" style="width:100%;height:100%;" class="table clsBigButtonStyle clsPhoneNoColor clsLargeRadius clsPhoneBtnBgNormal" onClick="PoiCommonHelper.onClickPhoneNo()">
						<div class="tr" style="height:100%;width:100%" >
							<div class="td clsBtnPicDiv"><img id="phonePic" class="poi_details_call_icon_unfocused"/></div>
							<div id="phoneContainer" align="left" class="td clsPhoneBtnTextDiv fs_middle fw_bold"><b id="phone"></b></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="thirdLineDivLandscape" class="clsDivThirdLandscape" style="display:none">
		<div class="div_table">
			<div class="tr">
				<div class="td" style="width:6%;">&nbsp;</div>
				<div class="td clsTdPhone" >
					<div class="clsPhoneWrapper">
						<div id="phoneButtonLandscape" ontouchstart="PoiCommonHelper.highLightPhone(this)"
									ontouchend="PoiCommonHelper.dishighLightPhone(this)" ontouchmove="PoiCommonHelper.dishighLightPhone(this)" class="table clsBigButtonStyle fc_gray clsLargeRadius clsPhoneBtnBgNormal" onClick="PoiCommonHelper.onClickPhoneNo()">
							<div class="tr" id="phoneDivLandscape">
								<div class="td clsPhonePicContainerLandscape" align="center"><img id="phonePicLandscape" class="clsImgCallTo poi_details_call_icon_unfocused"/></div>
							</div>
						</div>
					</div>
				</div>
				<div class="td" style="width:4%;">&nbsp;</div>
				<div id="topReviewNoLandscape" class="td clsTdRatingLandscape" onClick="clickReviewNo()">
					<div class="div_table clsFixTable" align="center">
						<div class="tr"><div class="td" align="center" style="vertical-align:top;" id="ratingIconsLandscape"></div></div>
						<div class="tr"><div class="td clsReviewNo clsReviewColor" align="center" style="vertical-align:bottom;" id="ratingNumberLandscape"></div></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="middlePartDiv" class="clsPageMiddleDiv">
	<div class="table" style="width:100%;height:100%;">
		<div class="tr clsTabBackground clsBackground_tab" id="tabShow"></div>
	</div>
</div>