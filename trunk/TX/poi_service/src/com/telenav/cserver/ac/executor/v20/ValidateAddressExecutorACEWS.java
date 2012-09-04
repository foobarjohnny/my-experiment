/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.ac.executor.v20;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.telenav.cserver.backend.ace.GeoCodeRequestV40;
import com.telenav.cserver.backend.ace.GeoCodeResponseV40;
import com.telenav.cserver.backend.ace.GeoCodingProxyV40;
import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.AddressFormatConstants;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.reporting.ReportType;
import com.telenav.cserver.framework.reporting.ReportingRequest;
import com.telenav.cserver.framework.reporting.ReportingUtil;
import com.telenav.cserver.framework.reporting.impl.ServerMISReportor;
import com.telenav.cserver.util.TnUtil;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * ValidateAddressExecutorACEWS.java
 * 
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-7
 * copy and update by xfliu at 2011/12/6
 */
public class ValidateAddressExecutorACEWS extends AbstractExecutor
{
    protected static Logger logger = Logger.getLogger(ValidateAddressExecutorACEWS.class);

    public void doExecute(ExecutorRequest req, ExecutorResponse resp, ExecutorContext context) throws ExecutorException
    {
        TnContext tc = context.getTnContext();
        UserProfile userProfile = req.getUserProfile();
        ReportingRequest misLog = new ReportingRequest(ReportType.SERVER_MIS_LOG_REPORT, userProfile, tc);

        misLog.addServerMisLogField(ServerMISReportor.SERVLET_NAME, ServerMISReportor.POI_SERVLET_NAME);
        misLog.addServerMisLogField(ServerMISReportor.ACTION_ID, this.getClass().getSimpleName());
        misLog.addServerMisLogField(ServerMISReportor.LOGTYPE_ID, ServerMISReportor.VALIDATE_ADDRESS_LOGTYPE);
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM16, tc.getProperty(TnContext.PROP_MAP_DATASET));

        ValidateAddressRequestACEWS request = (ValidateAddressRequestACEWS) req;
        ValidateAddressResponseACEWS response = (ValidateAddressResponseACEWS) resp;

        String street1 = request.getStreet1();
        String street2 = request.getStreet2();
        String firstLine = request.getFirstLine();
        String lastLine = request.getLastLine();
        String country = request.getCountry();
        String cityName = request.getCity();
        String county = request.getCounty();
        String state = request.getState();
        String postalCode = request.getZip();
        String door = request.getDoor();
        String neighborhood = request.getNeighborhood();
        String cityCountyOrPostalCode = request.getCityCountyOrPostalCode();
        String suite = request.getSuite();
    	String sublocality = request.getSublocality();
    	String locality = request.getLocality();
    	String locale = request.getLocale();
    	String subStreet = request.getSubStreet();
    	String buildingName = request.getBuildingName();
    	String addressId = request.getAddressId();
        response.setLabel(request.getLabel());
        response.setMaitai(request.isMaitai());
        if ("".equals(country))
        {
            country = TnUtil.getDefaultCountry(tc, userProfile);
            request.setCountry( country );
        }

        misLog.addServerMisLogField(ServerMISReportor.CUSTOM00, request.getCity());
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM01, request.getState());
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM02, country);
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM10, userProfile.getUserId());
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM04, StringUtils.isEmpty(street1) ? firstLine : street1);
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM05, street2);
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM06, lastLine);
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM07, request.getZip());
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM08, request.getAddressSearchId());
        
        if (logger.isDebugEnabled())
        {
            logger.debug("ValidateAddressExecutorACEWS >>> param: street1=" + street1 + ", street2=" + street2 + ", firstLine=" + firstLine + ", lastLine=" + lastLine
                    + ", country=" + country + ",cityName=" + cityName + ",county=" + county + ",state="+state + ",postalCode=" + postalCode + ",door="+door + ",neighborhood="+neighborhood
                    + ",cityCountyOrPostalCode" + cityCountyOrPostalCode + ",suite="+suite+ ",sublocality="+sublocality+ ",locality="+locality+ ",locale="+locale+ ",subStreet="+subStreet+ ",buildingName="+buildingName+ ",addressId="+addressId);
        }

        response.setStatus(ExecutorResponse.STATUS_OK);
        
        try
        {
        	HashMap<String,String> map = new HashMap<String,String>();        
    		map.put( AddressFormatConstants.FIRST_LINE, request.getFirstLine() );
    		map.put( AddressFormatConstants.LAST_LINE , request.getLastLine() );
    		map.put( AddressFormatConstants.STREET , request.getStreet1() );
    		map.put( AddressFormatConstants.CROSS_STREET , request.getStreet2() );
    		map.put( AddressFormatConstants.CITY , request.getCity() );
    		map.put( AddressFormatConstants.COUNTY , request.getCounty() );
    		map.put( AddressFormatConstants.POSTAL_CODE , request.getZip() );
    		map.put( AddressFormatConstants.STATE , request.getState() );
            map.put( AddressFormatConstants.LOCALITY , request.getNeighborhood() );
            map.put(AddressFormatConstants.CIYT_COUNTY_OR_POSTAL_CODE, request.getCityCountyOrPostalCode());
			map.put(AddressFormatConstants.ALL, request.getFirstLine() + "," + request.getLastLine() );	    		
            //door map.put( com.telenav.cserver.backend.ace.Constants., req.getDoor() );
    	    //all
    		Address address = new Address();
    		
    		//2. set SearchArea related attributes;
    		logger.debug( "GeoCodeRequestV40 map : " + map.toString() );
    		address.setCountry(request.getCountry().toUpperCase());
    		address.setLines( map );
    		address.setGeoCodingCountryList(TnUtil.getGeoCodingCountryList(tc, userProfile));
    		
    		GeoCodeRequestV40 geoCodeReq =  new GeoCodeRequestV40();
    		logger.debug( "GeoCodeRequestV40 lines : " + address.getLines().toString() );
    		logger.debug( "GeoCodeRequestV40 country : " + request.getCountry() + "," +address.getCountry()  );
    		geoCodeReq.setAddress(address);
    		geoCodeReq.setTransactionID( request.getTransactionId() );
    		
    		GeoCodingProxyV40 proxy = GeoCodingProxyV40.getInstance(context.getTnContext());
    		GeoCodeResponseV40 geoCodeResponse = proxy.geoCode(geoCodeReq);
    		///----------------------------
    		response.setGeoCodeStatusCode( geoCodeResponse.getStatus().getStatusCode() );
            String status =  "" + geoCodeResponse.getStatus().getStatusCode();
            logger.debug( "GeoCodeRequestV40 status : " + status );
            if (geoCodeResponse != null && geoCodeResponse.getStatus().isSuccessful())
            {
            	response.setAddressList(geoCodeResponse.getMatches());
            	response.setTotalCount(geoCodeResponse.getMatchCount());
            }
            misLog.addServerMisLogField(ServerMISReportor.CUSTOM03, status);
            misLog.addServerMisLogField(ServerMISReportor.COMPLETED_FLAG, ServerMISReportor.COMPLETE_SUCCEED);
        }
        catch (Exception e)
        {
            misLog.addServerMisLogField(ServerMISReportor.COMPLETED_FLAG, ServerMISReportor.COMPLETE_FAIL);
        }
        finally
        {
            ReportingUtil.report(misLog);
        }
    }

}
