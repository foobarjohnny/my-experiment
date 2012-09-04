func log(String logCat,String logText)
	TxNode logNode = Cache.getCookie(logCat)
	if NULL!=logNode
	TxNode.addMsg(logNode,logText)
		Cache.saveCookie(logCat,logNode)
	else
		TxNode tempNode
		TxNode.addMsg(tempNode,logText)
		Cache.saveCookie(logCat,tempNode)
	endif
endfunc

func logPoi(String logText)
	log("POILogCookie",logText);
endfunc

func UTIL_formatPhoneNumber(String phoneNumber)
	if NULL == phoneNumber
		phoneNumber = ""
		return phoneNumber
	endif
	#phoneNumber = String.trim(phoneNumber)
	if "" == phoneNumber
		return phoneNumber
	endif
	int length
	length = String.getLength(phoneNumber)
	if 10 > length
		return phoneNumber
	endif
	if 11 == length
		String s1 = String.at(phoneNumber,0,1)
		if "1" == s1
			String s2 = String.at(phoneNumber, 1, 2)	
			String s3 = String.at(phoneNumber, 3, 3)
			String s4 = String.at(phoneNumber, 6, 5)
			phoneNumber = s1 + "-" + s2 + "-" + s3 + "-" + s4
			return phoneNumber
		else
			int lastLength = length - 6
			s1 = String.at(phoneNumber, 0, 3)	
			String s2 = String.at(phoneNumber, 3, 3)
			String s3 = String.at(phoneNumber, 6, lastLength)
			phoneNumber = "(" + s1 + ") " + s2 + "-" + s3
			return phoneNumber
		endif
	else
		int lastLength = length - 6
		String s1 = String.at(phoneNumber, 0, 3)	
		String s2 = String.at(phoneNumber, 3, 3)
		String s3 = String.at(phoneNumber, 6, lastLength)
		phoneNumber = "(" + s1 + ") " + s2 + "-" + s3
		return phoneNumber
	endif
endfunc

func UTIL_checkEmptyJSON(JSONObject json)
    if NULL == json
       return TRUE
    endif
    
    String jsonStr = JSONObject.toString(json)
    if "{}" == jsonStr
       return TRUE
    endif
    
    return FALSE
endfunc
