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

func STOP_getCity(TxNode stop)
	String city = ""
	if NULL != stop
		if 3 <= TxNode.getStringSize(stop)
			city = city + TxNode.msgAt(stop, 2)
		endif
	endif
	return city
endfunc

func STOP_getState(TxNode stop)
	String state = ""
	if NULL != stop
		if 4 <= TxNode.getStringSize(stop)
			state = state + TxNode.msgAt(stop, 3)
		endif
	endif
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
