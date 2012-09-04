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

func UTIL_checkPhoneNumber(String phoneNumber)
	if NULL == phoneNumber
		return 0
	endif
	phoneNumber = String.trim(phoneNumber)
	if "" == phoneNumber
		return 0
	endif
	int length
	length = String.getLength(phoneNumber)
	if 10 != length
	   if 12 != length
	       return 0
	   endif
	endif
	
	int isNumberString
	isNumberString = UTIL_isphoneString(phoneNumber,length)
	return isNumberString
endfunc

func UTIL_isphoneString(String s,int length)
    int isPhone = 1
	int i = 0
	String char
	int isNumberChar = 1
	if 10 == length
	     while i < length
			char = String.at(s,i,1)
			isNumberChar = UTIL_isNumberChar(char)
			if 0 == isNumberChar
				isPhone = 0
				return isPhone
			endif			
			i = i + 1
		endwhile
	else
	    while i < length
			char = String.at(s,i,1)
			if 3 == i
			   if "-" != char
			      return 0
			   endif
			elsif 7 == i
			   if "-" != char
			      return 0
			   endif
			else
			   isNumberChar = UTIL_isNumberChar(char)
			   if 0 == isNumberChar
					isPhone = 0
					return isPhone
				endif	
			endif
			i = i + 1
		endwhile
	endif
	return isPhone
endfunc

func UTIL_isNumberChar(String s)
	int isNumber = 0
	int i = 0
	String n = ""
	while i < 10
		n = "" + i
		if n == s
			isNumber = 1
			return isNumber
		endif
		i = i + 1
	endwhile
	return isNumber
endfunc
