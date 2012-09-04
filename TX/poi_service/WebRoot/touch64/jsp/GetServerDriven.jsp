func ServerDriven_CanCancel()
    return serverDriven("CAN_CANCEL_SUBSCRIPTION")
endfunc

#invisible post location for OS5.0 ATT2.3
func ServerDriven_CanPostLocation()
    int canPostLocation = serverDrivenDefaultAsFalse("POSTLOCATION")
    TxNode OSNode = ShareData.get("OS_VERSION")
    if NULL != OSNode
       if TxNode.getStringSize(OSNode) > 0
	       String OSVersion = TxNode.msgAt(OSNode, 0)
	       if String.find(OSVersion,0,"5.0.") > -1 && 0 == serverDriven("POSTLOCATION_V50")
	          return 0
	       endif
       endif
    endif
    return canPostLocation
endfunc

func ServerDriven_CanManageSubscription()
	return serverDrivenDefaultAsFalse("CAN_MANAGE_SUBSCRIPTION")
endfunc

func ServerDriven_HavePIN()
	return serverDriven("HAVE_PIN")
endfunc

func ServerDriven_CanDiagnostic()
	return serverDriven("DIAGNOSTIC")
endfunc

func ServerDriven_CanCommuteAlert()
    return serverDriven("COMMUTEALERT")
endfunc

func ServerDriven_CanWeather()
    return serverDriven("WEATHER")
endfunc

func ServerDriven_CanShowTraffic()
    return serverDriven("TRAFFIC")
endfunc

func ServerDriven_CanResumeTrip()
    return serverDriven("RESUME_TRIP")
endfunc

func ServerDriven_ShowTOS()
	return serverDriven("TOS")
endfunc

func ServerDriven_CanDisplayAds()
    return serverDriven("ADSPOI")
endfunc

func ServerDriven_CanSponsor()
    return serverDriven("NEEDSPONSOR")
endfunc

func ServerDriven_CanDynamicNav()
    return serverDriven("DYNAMIC_NAV")
endfunc

func ServerDriven_CanRestaurant()
	return serverDrivenDefaultAsFalse("RESTAURANT")
endfunc

func ServerDriven_CanMyMileage()
	return serverDrivenDefaultAsFalse("LOCAL_APP_MYMILEAGE")
endfunc

func ServerDriven_CanShareAddress()
	return serverDriven("SHARE_ADDRESS")
endfunc

func ServerDriven_CanLocalAppRestaurant()
	return serverDriven("LOCAL_APP_RESTAURANT")
endfunc

func ServerDriven_CanFeedBack()
	return serverDriven("FEED_BACK")
endfunc

func ServerDriven_ShowNewAddressFlagOnMainPage()
    return serverDrivenDefaultAsFalse("RECEIVE_ADDRESS_FLAG_ON_MAINPAGE")
endfunc

func ServerDriven_CanShowMovie()
    return serverDriven("MOVIE")
endfunc

func ServerDriven_CanPhoneNumber()
    return serverDriven("PHONE_NUMBER")
endfunc

func ServerDriven_CanLocationStatement()
    return serverDrivenDefaultAsFalse("LOCATION_STATEMENT")
endfunc

func ServerDriven_CanTellFriends()
	return serverDriven("TELL_FRIENDS")
endfunc

func ServerDriven_CanOneBoxSearch()
	return serverDriven("ONE_BOX_SEARCH")
endfunc

func ServerDriven_CanGasByPrice()
	return serverDriven("GAS_BY_PRICE")
endfunc

func ServerDriven_IsCallOneBox()
	return serverDriven("IS_CALL_ONE_BOX")
endfunc

func ServerDriven_GetUpgradePhone()
	String phoneNumber="611"
	TxNode node = System.getServerParam("CALL_PHONE_NUMBER_FREEMAP")
	if node != NULL
		phoneNumber = TxNode.msgAt(node,0)  
	endif
	return phoneNumber 
endfunc

func serverDriven(String key)
    int isSupport = 1
   	TxNode node = System.getServerParam(key)
   	if node != NULL
    	if "0" == TxNode.msgAt(node,0)  
    		isSupport = 0
        elsif "2" == TxNode.msgAt(node,0)  
			isSupport = 2
    	endif
   	endif
   	
   	println(key + ".................."+isSupport)
   	return isSupport
endfunc

func serverDrivenDefaultAsFalse(String key)
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