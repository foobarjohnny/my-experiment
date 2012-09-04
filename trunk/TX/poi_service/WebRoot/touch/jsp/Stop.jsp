func STOP_getFirstLine(TxNode stop)
	String firstLine = ""
	if NULL != stop
		if 2 <= TxNode.getStringSize(stop)
			firstLine = firstLine + TxNode.msgAt(stop, 1)
		endif
	endif
	#firstLine = String.trim(firstLine)
	return firstLine
endfunc

func STOP_getZip(TxNode stop)
	String zip = ""
	if NULL != stop
		if 6 <= TxNode.getStringSize(stop)
			zip = zip + TxNode.msgAt(stop, 5)
		endif
	endif
	#zip = String.trim(zip)
	return zip
endfunc

func STOP_getCity(TxNode stop)
	String city = ""
	if NULL != stop
		if 3 <= TxNode.getStringSize(stop)
			city = city + TxNode.msgAt(stop, 2)
		endif
	endif
	#city = String.trim(city)
	return city
endfunc

func STOP_getState(TxNode stop)
	String state = ""
	if NULL != stop
		if 4 <= TxNode.getStringSize(stop)
			state = state + TxNode.msgAt(stop, 3)
		endif
	endif
	#state = String.trim(state)
	return state
endfunc

func STOP_getSecondLine_JSON(JSONObject stop)
	String secondLine = ""
	if NULL != stop
		String city = JSONObject.get(stop,"city")
		String state = JSONObject.get(stop,"state")
		if "" != city
			if "" != state
				secondLine = city + ", " + state
				return secondLine
			endif
		endif
		secondLine = city + state
		return secondLine
	endif
	return secondLine
endfunc

func STOP_getStopName(TxNode stop)
	if NULL == stop
		return ""
	endif
	
	String stopName = ""
	stopName = stopName + STOP_getLabel(stop)
	if "" != stopName
		return stopName
	endif
	stopName = stopName + STOP_getFirstLine(stop)
	if "" != stopName
		return stopName
	endif
	stopName = stopName + STOP_getCity(stop)
	if "" != stopName
		return stopName
	endif
	stopName = stopName + STOP_getState(stop)
	if "" != stopName
		return stopName
	endif
	return stopName
endfunc

func STOP_getLabel(TxNode stop)
	String label = ""
	if NULL != stop
		if 1 <= TxNode.getStringSize(stop)
			label = label + TxNode.msgAt(stop, 0)
		endif
	endif
	#label = String.trim(label)
	return label
endfunc