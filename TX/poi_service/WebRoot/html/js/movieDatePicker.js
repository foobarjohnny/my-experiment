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
		if(daysDiffer == 0)
		{
			changeDatePreviousStyle(false);
		}
		else if(daysDiffer == GLOBAL_dateMaxRange)
		{
			changeDateNextStyle(false);
		}
		else
		{
			changeDatePreviousStyle(true);
			changeDateNextStyle(true);
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

	function getDaysDiff(d1,d2)
	{
		var s1 = new Date(d1);
        var s2 = new Date(d2);
        s1.setHours(0, 0, 1, 0);
        s2.setHours(0, 0, 1, 0);

        var time= s2.getTime() - s1.getTime(); 
        var days = parseInt(time / (1000 * 60 * 60 * 24));

        //alert("days diff:" + days);
        return days;
	}
	
	function changeDatePreviousStyle(enableFlag)
	{
		if(enableFlag)
		{
			document.getElementById("datePrevious").href = "javascript:onClickDatePrevious()";	
			$("#datePreviousIcon").attr("class","date_previous_button_unfocused");
		}
		else
		{
			document.getElementById("datePrevious").href = "javascript:void(0)";	
			$("#datePreviousIcon").attr("class","date_previous_button_disabled");
		}
	}

	function changeDateNextStyle(enableFlag)
	{
		if(enableFlag)
		{
			document.getElementById("dateNext").href = "javascript:onClickDateNext()";	
			//document.getElementById("dateNext").className = "clsFontBlue";	
			$("#dateNextIcon").attr("class","date_next_button_unfocused");
		}
		else
		{
			document.getElementById("dateNext").href = "javascript:void(0)";	
			//document.getElementById("dateNext").className = "clsFontGray";
			$("#dateNextIcon").attr("class","date_next_button_disabled");
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
				$("#datePreviousIcon").attr("class","date_previous_button_focused");
			}
			else
			{
				$("#datePreviousIcon").attr("class","date_previous_button_unfocused");
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
				$("#dateNextIcon").attr("class","date_next_button_focused");
			}
			else
			{
				$("#dateNextIcon").attr("class","date_next_button_unfocused");
			}
		}
	}
	
	/**
	 * End of the code for date picker
	 */
