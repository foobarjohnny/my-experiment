func ServerDriven_CanCommuteAlert()
    return serverDrivenForSN("COMMUTEALERT")
endfunc

func ServerDriven_CanWeather()
    return serverDrivenForSN("WEATHER")
endfunc

func ServerDriven_CanShowTraffic()
    return serverDrivenForSN("TRAFFIC")
endfunc

func ServerDriven_CanResumeTrip()
    return serverDrivenForSN("RESUME_TRIP")
endfunc

func ServerDriven_CanResumeIcon()
	return serverDrivenForSNReverse("RESUME_ICON")
endfunc

func ServerDriven_ShowTOS()
	return serverDrivenForSN("TOS")
endfunc

func ServerDriven_CanDynamicNav()
    return serverDrivenForSN("DYNAMIC_NAV")
endfunc

func ServerDriven_CanAvoid()
    return serverDrivenForSN("AVOID")
endfunc

func ServerDriven_CanRestaurant()
	return serverDrivenForSNReverse("RESTAURANT")
endfunc

func ServerDriven_CanLocalAppRestaurant()
	return serverDrivenForSNReverse("LOCAL_APP_RESTAURANT")
endfunc

func ServerDriven_ShowNewAddressFlagOnMainPage()
    return serverDrivenForSNReverse("RECEIVE_ADDRESS_FLAG_ON_MAINPAGE")
endfunc

func ServerDriven_CanTellFriends()
	return serverDrivenForSN("TELL_FRIENDS")
endfunc

func ServerDriven_CanShareAddress()
	return serverDrivenForSN("SHARE_ADDRESS")
endfunc

func serverDrivenForSNReverse(String key)
    int isSupport = 0
   	TxNode node = System.getServerParam(key)
   	if node != NULL
    	if "1" == TxNode.msgAt(node,0)  
    		isSupport = 1
    	endif
   	endif
   	
   	println(key + ".................."+isSupport)
   	return isSupport
endfunc

func serverDrivenForSN(String key)
    int isSupport = 1
   	TxNode node = System.getServerParam(key)
   	if node != NULL
    	if "0" == TxNode.msgAt(node,0)  
    		isSupport = 0
    	endif
   	endif
   	
   	println(key + ".................."+isSupport)
   	return isSupport
endfunc
