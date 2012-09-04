<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@include file="../model/AddressCaptureModel.jsp"%>
<tml:script language="fscript" version="1">
		<![CDATA[
			func AddressCapture_C_city(JSONObject jo)
				AddressCapture_M_clearDefaultAddress()
				JSONObject joDefaultAddress = JSONObject.get(jo,"address")
				if joDefaultAddress!= NULL
					AddressCapture_M_saveDefaultAddress(joDefaultAddress)
				endif
				AddressCapture_M_city(jo)
			endfunc
			
			func AddressCapture_C_address(JSONObject jo)
				AddressCapture_M_clearDefaultAddress()
				JSONObject joDefaultAddress = JSONObject.get(jo,"address")
				if joDefaultAddress!= NULL
					AddressCapture_M_saveDefaultAddress(joDefaultAddress)
				endif
				AddressCapture_M_address(jo)
			endfunc

			func AddressCapture_C_airport(JSONObject jo)
				AddressCapture_M_airport(jo)
			endfunc
			
			func AddressCapture_C_intersection(JSONObject jo)
				AddressCapture_M_clearDefaultAddress()
				JSONObject joDefaultAddress = JSONObject.get(jo,"address")
				if joDefaultAddress!= NULL
					AddressCapture_M_saveDefaultAddress(joDefaultAddress)
				endif
				AddressCapture_M_intersection(jo)
			endfunc
			
			func AddressCapture_C_initType(String s)
			    AddressCapture_M_initType(s)
			endfunc
			
			func AddressCapture_C_getCountry()
		        return AddressCapture_M_getCountry()
		    endfunc
		    
		    func AddressCapture_C_saveCountry(String s)
			    AddressCapture_M_saveCountry(s)
			endfunc
			
			func AddressCapture_C_saveReturnToLocalService(String serviceName)
			    AddressCapture_M_saveReturnToLocalService(serviceName)
			endfunc
			
			func AddressCapture_C_getReturnToLocalService()
			    return AddressCapture_M_getReturnToLocalService()
			endfunc
			
			func AddressCapture_C_saveCreateFavoriteReturnUrl(String url)
			    AddressCapture_M_saveCreateFavoriteReturnUrl(url)
			endfunc
			
			func AddressCapture_C_getCityString()
			    return AddressCapture_M_getCityString()
			endfunc
			
			func AddressCapture_C_saveCacheCity(TxNode cacheCityNode)
			    AddressCapture_M_saveCacheCity(cacheCityNode)
			endfunc
			
			func AddressCapture_C_getCacheCity()
			    return AddressCapture_M_getCacheCity()
			endfunc
			
			func AddressCapture_C_saveAddressListTitleForMaiTai(TxNode node)
			    AddressCapture_M_saveAddressListTitleForMaiTai(node)
			endfunc
			
			func AddressCapture_C_deleteAddressListTitleForMaiTai()
			    AddressCapture_M_deleteAddressListTitleForMaiTai()
			endfunc
		]]>
</tml:script>
