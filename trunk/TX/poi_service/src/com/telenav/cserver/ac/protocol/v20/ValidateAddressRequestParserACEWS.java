package com.telenav.cserver.ac.protocol.v20;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.me.JSONObject;

import com.telenav.cserver.ac.executor.v20.ValidateAddressRequestACEWS;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolRequestParser;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.util.CommonUtil;
import com.telenav.cserver.util.JsonUtil;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * 
 * @author chbzhang
 * 2009-07-09
 * copy and update by xfliu at 2011/12/6
 *
 */

public class ValidateAddressRequestParserACEWS  extends BrowserProtocolRequestParser {

	
	 private static final Logger log = Logger.getLogger( ValidateAddressRequestParserACEWS.class );
	 
    @Override
    public String getExecutorType() {
        return "ValidateAddress20";
    }

    private boolean isNumber( String s )
    {
    	Pattern pattern = Pattern.compile( "[0-9]*" );
    	return pattern.matcher( s ).matches();
    }
    
    public ExecutorRequest parseBrowserRequest(HttpServletRequest object)
            throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) object;
        ValidateAddressRequestACEWS request = new ValidateAddressRequestACEWS();

        // Get the JSON request.
        DataHandler handler = (DataHandler) httpRequest
                .getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
        
        String region  = handler.getClientInfo(DataHandler.KEY_REGION);
        
        TxNode body = handler.getAJAXBody();

        String jsonStr = body.msgAt(0);
        JSONObject jo = new JSONObject(jsonStr);
        
        //streetName
        String street1 = JsonUtil.getString(jo, "street1");
        //crossStreetName
        String street2 = JsonUtil.getString(jo, "street2");
        //firstLine
        String firstLine = JsonUtil.getString(jo, "firstLine");
        //lastLine
        String lastLine = JsonUtil.getString(jo, "lastLine");
        //country
        String country = JsonUtil.getString(jo, "country");
        
        //lable
        String label = JsonUtil.getString(jo, "label");
        boolean maitai = JsonUtil.getBoolean(jo, "maitai");

        String cityName = JsonUtil.getString(jo, "city");
        String county =  JsonUtil.getString(jo, "county");
        String state =   JsonUtil.getString(jo, "state");
        String postalCode =  JsonUtil.getString(jo, "zip");
        String door = JsonUtil.getString(jo, "door");
 		String neighborhood = JsonUtil.getString(jo,  "neighborhood" );
 		String cityCountyOrPostalCode = JsonUtil.getString(jo, "cityCountyOrPostalCode");

 		String suite = JsonUtil.getString(jo,  "suite" );
 		String sublocality = JsonUtil.getString(jo,  "sublocality" );
 		String locality = JsonUtil.getString(jo,  "locality" );
 		String locale = JsonUtil.getString(jo,  "locale" );
 		String subStreet = JsonUtil.getString(jo,  "subStreet" );
 		String buildingName = JsonUtil.getString(jo,  "buildingName" );
 		String addressId = JsonUtil.getString(jo,  "addressId" );
 		
        // TODO MAPPING XML for country AC TEMPLATE
 		if( country != null && !country.equals( "" ) )
 		{
 			if( isNumber( country ) == false )
 	        {
 	        	country = CommonUtil.getCountryCode( region , country , locale );
 	        }
 	        else
 	        {
 	        	int index = Integer.parseInt( country );
 	        	country = CommonUtil.getI18NCountryList(region).get( index-1 );
 	        }
 		}
 		else
 			country = "";
       
        log.debug( "tn64 country = " + country );
        request.setStreet1(street1);
        request.setStreet2(street2);
        request.setFirstLine(firstLine);
        request.setLastLine(lastLine);
        request.setCountry(country);
        request.setLabel(label);
        request.setMaitai(maitai);

        request.setCity( cityName );
        request.setCounty( county );
        request.setState( state );
        request.setZip( postalCode );
        request.setDoor( door );
        
        request.setNeighborhood( neighborhood );
        request.setCityCountyOrPostalCode(cityCountyOrPostalCode);
        
        request.setSuite(suite);
        request.setAddressId(addressId);
        request.setBuildingName(buildingName);
        request.setLocale(locale);
        request.setLocality(locality);
        request.setSublocality(sublocality);
        request.setSubStreet(subStreet);
        return request;
    }
}
