	/**
	 * Start of the code for date picker
	 */
	function onClickDatePrevious()
	{
		var daysDiffer = getDaysDiff(GLOBAL_todayDate,GLOBAL_searchDate);
		if(daysDiffer > 0 )
		{
			//Date - 1
			GLOBAL_searchDate.setDate(GLOBAL_searchDate.getDate()-1); 
			//disable previous button
			changeDatePickerStatus();
		}
	}
	
	function changeDatePickerStatus()
	{
		changeDateDisplay();
		datePickerOnDateChange();		
	}
	
	function changeDateDisplay()
	{
		var daysDiffer = getDaysDiff(GLOBAL_todayDate,GLOBAL_searchDate);
		changeDatePreviousStyle(true);
		changeDateNextStyle(true);
		if(daysDiffer == 0)
		{
			changeDatePreviousStyle(false);
		}
		if(daysDiffer == GLOBAL_dateMaxRange)
		{
			changeDateNextStyle(false);
		}
		setSearchDateDisplay(daysDiffer);	
	}

	function onClickDateNext()
	{
		var daysDiffer = getDaysDiff(GLOBAL_todayDate,GLOBAL_searchDate);
		if(daysDiffer < GLOBAL_dateMaxRange )
		{
			//Date + 1
			GLOBAL_searchDate.setDate(GLOBAL_searchDate.getDate()+1); 
			changeDatePickerStatus();
		}
	}

	function setSearchDateDisplay(daysDiffer)
	{
		var dateDisplay;
		if(daysDiffer==0)
		{
			dateDisplay = I18NHelper["mSearch.today"];
		}
		else
		{
			dateDisplay = GLOBAL_searchDate.format("ddd, mmm d");
		}
		document.getElementById("searchDate").innerText = dateDisplay;
	}
	
	function changeDatePreviousStyle(enableFlag)
	{
		if(enableFlag)
		{
			document.getElementById("datePrevious").href = "javascript:onClickDatePrevious()";	
			//document.getElementById("datePrevious").className = "clsFontBlue";
			$("#datePreviousIcon").attr("class", "clsDateIcon clsDatePreviousButtonUnfocused");
		}
		else
		{
			document.getElementById("datePrevious").href = "javascript:void(0)";	
			//document.getElementById("datePrevious").className = "clsFontGray";
			$("#datePreviousIcon").attr("class", "clsDateIcon clsDatePreviousButtonDisabled");
		}
	}

	function changeDateNextStyle(enableFlag)
	{
		if(enableFlag)
		{
			document.getElementById("dateNext").href = "javascript:onClickDateNext()";	
			//document.getElementById("dateNext").className = "clsFontBlue";	
			$("#dateNextIcon").attr("class", "clsDateIcon clsDateNextButtonUnfocused");
		}
		else
		{
			document.getElementById("dateNext").href = "javascript:void(0)";	
			//document.getElementById("dateNext").className = "clsFontGray";
			$("#dateNextIcon").attr("class", "clsDateIcon clsDateNextButtonDisabled");
		}
	}
	
	function switchDatePreviousIcon(isOn)
	{
		//first check if the button is enabled.
		var isEnabled = true;
		
		if($("#datePrevious").attr("href")=="javascript:void(0)")
		{
			isEnabled = false;
		}
		
		if(isEnabled)
		{
			if(isOn)
			{
				$("#datePreviousIcon").attr("class", "clsDateIcon clsDatePreviousButtonFocused");
			}
			else
			{
				$("#datePreviousIcon").attr("class", "clsDateIcon clsDatePreviousButtonUnfocused");
			}
		}
	}
	
	function switchDateNextIcon(isOn)
	{
		//first check if the button is enabled.
		var isEnabled = true;
		
		if($("#dateNext").attr("href")=="javascript:void(0)")
		{
			isEnabled = false;
		}
		if(isEnabled)
		{
			if(isOn)
			{
				$("#dateNextIcon").attr("class", "clsDateIcon clsDateNextButtonFocused");
			}
			else
			{
				$("#dateNextIcon").attr("class", "clsDateIcon clsDateNextButtonUnfocused");
			}
		}
	}
	
	/**
	 * End of the code for date picker
	 */