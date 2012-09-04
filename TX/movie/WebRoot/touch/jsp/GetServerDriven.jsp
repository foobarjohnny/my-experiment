func ServerDriven_CanShareAddress()
	return serverDriven_DefaultAble("SHARE_ADDRESS")
endfunc

func serverDriven_DefaultAble(String key)
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
