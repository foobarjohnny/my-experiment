package com.telenav.cserver.ugc.struts.action;

import java.rmi.ServerException;
import java.util.List;

import com.telenav.cserver.backend.xnav.FetchBrandNamesRequest;
import com.telenav.cserver.backend.xnav.FetchBrandNamesResponse;
import com.telenav.cserver.backend.xnav.XnavServiceProxy;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * 2009.06.04 Get business name list of poi when add poi
 * 2009.06.05 Add find airport for ac
 * 
 * @author chbzhang
 * 
 */

public class PoiFinderManager {

    public PoiFinderManager() {
    }

    @SuppressWarnings("unchecked")
    public List<String> searchPOIBrandNames(String requestCountry, int count,TnContext tnContext)
            throws ThrottlingException {
    	
    	FetchBrandNamesRequest request = new FetchBrandNamesRequest();
    	request.setCount(count);
    	request.setCountry(requestCountry);
    	
    	FetchBrandNamesResponse response = XnavServiceProxy.getInstance().fetchBrandNames(request,tnContext);
    	
    	//String statusCode = response.getStatusCode();
    	List<String> nameList = response.getBrandNames();
    	
        return nameList;
    }

    /**
     * Find airports for ac
     * chbzhang
     * @param requestCode
     * @param requestCity
     * @param region
     * @return
     * @throws ServerException
     */
    @SuppressWarnings("unchecked")
//    public Vector searchAirportPOIs(String requestCode, String requestCity,
//            String region) throws ServerException {
//        String code = this.toUpperCase(requestCode);
//        String city = this.toUpperCase(requestCity);
//
//        String para1 = code;
//        if (para1 == null || para1.length() == 0) {
//            para1 = city;
//        }
//        
//        NavFinderStatus status = null;
//        status = finder.tlPoiAirportWildSearch(para1, null, null,
//                AirportSearchParam.AIRPORT_DEEPTH_MAX_ALIAS, region);
//       
//        return status == null ? null : status.getPois();
//    }

    protected String toUpperCase(String str) {
        return str == null ? null : str.toUpperCase();
    }
}
