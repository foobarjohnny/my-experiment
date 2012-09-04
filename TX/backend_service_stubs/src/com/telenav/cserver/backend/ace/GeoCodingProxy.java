package com.telenav.cserver.backend.ace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.framework.throttling.ThrottlingManager;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.kernel.ws.axis2.Axis2Helper;
import com.telenav.services.geocoding.v20.GeoCodingServiceRequestDTO;
import com.telenav.services.geocoding.v20.GeoCodingServiceResponseDTO;
import com.telenav.services.geocoding.v20.GeoCodingServiceStub;

/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */

/**
 * GeoCodingProxy.java
 * 
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-3
 */

final class ServiceConfig
{
    public String service = "ACE";

    public int timeout = 30000;

    public String serviceUrl = "http://localhost:8080/tnws/services/AceService";

    public String dataset = "NT";

    public int poolSizePerHost = 5;

    public int maximumPoolSize = 20;
}

public class GeoCodingProxy
{  
    private static final Logger logger=Logger.getLogger(GeoCodingProxy.class);

    private static final String clientName = "ace-ws-client";

    private static final String clientVersion = "1.0.0.3";

    public static final String GET_VERSION_SERVER_KEY = "server_Version";

    public static final String GET_VERSION_CLIENT_KEY = "client_Version";

    public static final String GET_VERSION_DATA_KEY = "data_Version";

    public static final String GET_VERSION_DATASET_KEY = TnContext.PROP_MAP_DATASET;

    public static final String GET_VERSION_COUNTRY_KEY = "country";

    public static final String GET_VERSION_CONTINENT_KEY = "continent";

    public static final String VENDOR_NT = "Navteq";

    public static final String VENDOR_TA = "TeleAtlas";

    private static final String CLIENT_CFG = "config/aceClient.xml";

    // serviceConfigMap: key:dataset_country, value:url
    private static final HashMap serviceConfigMap = new HashMap();

    // defaultCountryOfContinent: key: continent, value:default country
    private static final HashMap defaultCountryOfContinent = new HashMap();

    private final static String SERVICE_ACESERVER = "ACESERVER";
    //
    private static HashMap hMapdatasets = new HashMap();

    private static com.telenav.datatypes.address.v20.Country defaultCountry = CountryUtils.US;

    static
    {
        hMapdatasets.put(VENDOR_NT.toUpperCase(), "NT");
        hMapdatasets.put("NT", "NT");
        hMapdatasets.put(VENDOR_TA.toUpperCase(), "TA");
        hMapdatasets.put("TA", "TA");
    }

    static
    {
        getConfigInfo(CLIENT_CFG);

        defaultCountryOfContinent.put("EU", CountryUtils.GB);
        defaultCountryOfContinent.put("NA", CountryUtils.US);
    }

    public static String getVendorCode(String vendor)
    {
        if (null == vendor)
        {
            return null;
        }

        vendor = vendor.toUpperCase();
        String result = (String) hMapdatasets.get(vendor);
        return result == null ? vendor : result;
    }

    private static void getConfigInfo(String strPath)
    {
        try
        {
            ClassLoader classLoader = (com.telenav.cserver.backend.ace.GeoCodingProxy.class).getClassLoader();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            DocumentBuilder doc_builder = dbf.newDocumentBuilder();
            Document doc = doc_builder.parse(classLoader.getResourceAsStream(strPath), "UTF-8");

            // Get root node
            Element rootElement = doc.getDocumentElement();
            if (rootElement == null)
            {
                return;
            }

            NodeList nodeLists = doc.getElementsByTagName("Service");
            int iNodeCount = nodeLists.getLength();

            String strCountry;

            for (int i = 0; i < iNodeCount; i++)
            {
                ServiceConfig serviceConfig = new ServiceConfig();
                serviceConfig.service = nodeLists.item(i).getAttributes().getNamedItem("name").getNodeValue();
                serviceConfig.serviceUrl = nodeLists.item(i).getAttributes().getNamedItem("url").getNodeValue();
                serviceConfig.dataset = nodeLists.item(i).getAttributes().getNamedItem("dataset").getNodeValue();
                serviceConfig.dataset = (serviceConfig.dataset == null) ? "NT" : serviceConfig.dataset.toUpperCase();

                NodeList serviceNodeLists = nodeLists.item(i).getChildNodes();
                String name;
                for (int j = 0; j < serviceNodeLists.getLength(); j++)
                {
                    name = serviceNodeLists.item(j).getNodeName();
                    if (name.equalsIgnoreCase("servicepool"))
                    {
                        serviceConfig.timeout = Integer.parseInt(serviceNodeLists.item(j).getAttributes().getNamedItem("aceServiceTimeout")
                                .getNodeValue());
                        serviceConfig.poolSizePerHost = Integer.parseInt(serviceNodeLists.item(j).getAttributes().getNamedItem(
                            "aceServicePoolSizePerHost").getNodeValue());
                        serviceConfig.maximumPoolSize = Integer.parseInt(serviceNodeLists.item(j).getAttributes().getNamedItem(
                            "aceServiceMaximumPoolSize").getNodeValue());
                    }
                }
                for (int j = 0; j < serviceNodeLists.getLength(); j++)
                {
                    name = serviceNodeLists.item(j).getNodeName();
                    if (name.equalsIgnoreCase("country"))
                    {
                        NodeList countryNodeLists = serviceNodeLists.item(j).getChildNodes();
                        for (int k = 0; k < countryNodeLists.getLength(); k++)
                        {
                            strCountry = countryNodeLists.item(k).getNodeName();

                            if (strCountry != null)
                            {
                                strCountry = strCountry.trim();
                            }

                            com.telenav.datatypes.address.v20.Country country = CountryUtils.parse(strCountry);
                            if (country != null)
                            {
                                serviceConfigMap.put(serviceConfig.dataset + "_" + country.getIso2Name(), serviceConfig);
                            }
                        } // for
                    } // if
                } // for
            } // for
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static ConfigurationContext configContext = null;

    // end
    // singleton
    private GeoCodingProxy()
    {
    }

   // private String contextString = null;
    private TnContext tnContext;
    
    public static GeoCodingProxy getInstance(TnContext tc)
    {
        GeoCodingProxy instance = new GeoCodingProxy();
        instance.setContextString(tc);

        return instance;
    }

    private enum RequestType
    {
        Geocode, ExactGeocode, Standardize
    }


    public GeoCodeResponse geoCode(Address address) throws ThrottlingException
    {   
    	CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
		cli.setFunctionName("GeoCoding");
        if(address!=null) {//record geocode address
        	cli.addData("address", address.toString());
        	if(logger.isDebugEnabled()) {
               logger.debug("Geo coding Address======>" + address.toString());
        	}
        }
        boolean startAPICall = false;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_ACESERVER, this.tnContext);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
        GeoCodeRequest geoCodeReq =new GeoCodeRequest();
        geoCodeReq.setAddress(address);
        return geoCode(geoCodeReq, RequestType.Geocode,cli);
        }
        finally
        {    
        	cli.complete();
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(SERVICE_ACESERVER, this.tnContext);
            }
        }
    }
    
    
    public GeoCodeResponse geoCode(GeoCodeRequest geoCodeReq) throws ThrottlingException
    {   
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("GeoCoding");
        if(geoCodeReq.getAddress()!=null) {//record geocode address
            cli.addData("address", geoCodeReq.getAddress().toString());
            if(logger.isDebugEnabled()) {
               logger.debug("Geo coding Address======>" + geoCodeReq.getAddress().toString());
            }
        }
        boolean startAPICall = false;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_ACESERVER, this.tnContext);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
        return geoCode(geoCodeReq, RequestType.Geocode,cli);
        }
        finally
        {    
            cli.complete();
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(SERVICE_ACESERVER, this.tnContext);
            }
        }
    }
    
    


    private GeoCodeResponse geoCode(GeoCodeRequest geoCodeReq, RequestType requestType ,CliTransaction cli)
    {
        if(requestType!=null&& logger.isDebugEnabled()) {//record geocode address
            logger.debug("Geo coding Arequest type======>" + requestType.toString());
        }
        GeoCodeResponse response = null;
        GeoCodingServiceStub stub = null;
        
        com.telenav.datatypes.address.v20.Address wsAddress=geoCodeReq.convertAddress2WsAddress();
        
        com.telenav.datatypes.address.v20.Country country = wsAddress.getCountry();
        if (country == null)
        {
            country = defaultCountry;
        }

        String dataset = tnContext.getProperty(TnContext.PROP_MAP_DATASET);
        ServiceConfig serviceConfig = (ServiceConfig) serviceConfigMap.get(getVendorCode(dataset) + "_" + country.getIso2Name());

        if (serviceConfig == null)
        {
            return GeoCodeResponse.NULL_RESPONSE;
        }

        try
        {
            stub = createStub(serviceConfig);
            GeoCodingServiceRequestDTO dto = createDefaultAceServiceRequestDTO();
            if(geoCodeReq.getTransactionId()!=null)
                dto.setTransactionId(geoCodeReq.getTransactionId());
            cli.addData("transactionId", geoCodeReq.getTransactionId());
            dto.setAddress(wsAddress);
            
            GeoCodingServiceResponseDTO resp;
            switch (requestType)
            {
                case ExactGeocode:
                    resp = stub.exactGeoCode(dto);
                    break;
                case Geocode:
                    resp = stub.geoCode(dto);
                    break;
                case Standardize:
                    resp = stub.standardize(dto);
                    break;
                default:
                    return GeoCodeResponse.NULL_RESPONSE;
            }
            List<GeoCodedAddress> geoCodedAddresses = new ArrayList<GeoCodedAddress>();
            if (resp.getAddresses() != null)
            {
                for (int i = 0; i < resp.getAddresses().length; i++)
                {
                    com.telenav.services.geocoding.v20.GeoCodedAddress geoAddr = resp.getAddresses()[i];
                    GeoCodedAddress geoCodedAddress = AceDataConverter.wsGeoCodedAddressToCSGeoCodedAddress(geoAddr);
                    geoCodedAddresses.add(geoCodedAddress);
                }
            }
            
            cli.addData("Response",cliGeoCodingResponse(resp));
            if(resp != null)
            {
                response = new GeoCodeResponse(geoCodedAddresses, resp.getResponseStatus());
            }
            else
            {
                return GeoCodeResponse.NULL_RESPONSE;
            }
            if(response!=null && logger.isDebugEnabled()) {//record the geocode response
                logger.debug("Geo Code Response====> "+response.toString());
            }
        }
        catch (Exception ex)
        {
            logger.error("GeoCode Exception====> "+ex.getStackTrace());
            return GeoCodeResponse.NULL_RESPONSE;
        }
        finally
        {         	
            try
            {
                Axis2Helper.close(stub);
            }
            catch (Exception e)
            {

            }
        }
        return response;
    }
     
    /**
     * build up the grocoding for cli log
     * @param resp
     * @return
     */
    public String cliGeoCodingResponse(GeoCodingServiceResponseDTO resp) {
        StringBuilder sb=new StringBuilder();
        sb.append("ResponseDTO==>[");
        sb.append("status=");
        sb.append(resp.getResponseStatus().getStatusCode());
        sb.append("; message=");
        sb.append(resp.getResponseStatus().getStatusMessage());
        sb.append(",count addr=");
        if(resp.getAddresses()!=null) {
        	sb.append(resp.getAddresses().length);
        }
        sb.append("]");
        return sb.toString();
    }
    
    
    private GeoCodingServiceStub createStub(ServiceConfig serviceConfig) throws AxisFault
    {
        GeoCodingServiceStub stub = new GeoCodingServiceStub(getConfigContext(serviceConfig), serviceConfig.serviceUrl);
        Options optionsStub = stub._getServiceClient().getOptions();
        MultiThreadedHttpConnectionManager conmgr = new MultiThreadedHttpConnectionManager();
        conmgr.getParams().setDefaultMaxConnectionsPerHost(serviceConfig.poolSizePerHost);
        // override timeouts
        conmgr.getParams().setConnectionTimeout(serviceConfig.timeout);
        conmgr.getParams().setSoTimeout(serviceConfig.timeout);
        HttpClient client = new HttpClient(conmgr);
        // Reuse the client
        optionsStub.setProperty(HTTPConstants.REUSE_HTTP_CLIENT, "true");
        // Cache the client
        optionsStub.setProperty(HTTPConstants.CACHED_HTTP_CLIENT, client);
        optionsStub.setTimeOutInMilliSeconds(serviceConfig.timeout);
        stub._getServiceClient().setOptions(optionsStub);
        return stub;
    }

    public TnContext getPrimaryEnv(TnContext tnc)
    {
        if ((tnc == null) || ((tnc.getProperty(GET_VERSION_COUNTRY_KEY) == null) && (tnc.getProperty(GET_VERSION_CONTINENT_KEY) == null))
                || (tnc.getProperty(GET_VERSION_DATASET_KEY) == null))
        {
            return null;
        }

        GeoCodingServiceStub stub = null;

        com.telenav.datatypes.address.v20.Country country = CountryUtils.parse(tnc.getProperty(GET_VERSION_COUNTRY_KEY));
        if (country == null)
        {
            country = (com.telenav.datatypes.address.v20.Country) defaultCountryOfContinent.get(tnc.getProperty(GET_VERSION_CONTINENT_KEY)
                    .toUpperCase());
            country = (country == null) ? defaultCountry : country;
        }

        String dataset = tnc.getProperty(TnContext.PROP_MAP_DATASET);
        ServiceConfig serviceConfig = (ServiceConfig) serviceConfigMap.get(getVendorCode(dataset) + "_" + country);

        if (serviceConfig == null)
        {
            return null;
        }

        try
        {
            stub = createStub(serviceConfig);
            GeoCodingServiceRequestDTO dto = createDefaultAceServiceRequestDTO();
            dto.setContextString(tnc.toContextString());
            GeoCodingServiceResponseDTO resp = stub.getPrimaryEnv(dto);
            TnContext retTnc = new TnContext(resp.getContextString());
            retTnc.addProperty(GET_VERSION_CLIENT_KEY, clientVersion);
            retTnc.addProperty(GET_VERSION_COUNTRY_KEY, tnc.getProperty(GET_VERSION_COUNTRY_KEY));
            retTnc.addProperty(GET_VERSION_DATASET_KEY, tnc.getProperty(GET_VERSION_DATASET_KEY));
            return retTnc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                Axis2Helper.close(stub);
            }
            catch (Exception e)
            {

            }
        }
        return null;
    }

    public static void main(String[] args)
    {
        long start = System.currentTimeMillis();
        Address address = new Address();
        address.setFirstLine("1130 kifer rd");
        address.setCityName("sannyvale");
        address.setState("ca");
 
        TnContext tc =new TnContext();
        tc.addProperty(TnContext.PROP_MAP_DATASET, "NavTeq");
        GeoCodeResponse response;
        try
        {
            response = GeoCodingProxy.getInstance(tc).geoCode(address);
            if (response.getStatus().isSuccessful())
            {
                for (GeoCodedAddress match : response.getMatches())
                {
                    System.out.println(match.getStreetName());
                    System.out.println(match.getFirstLine());
                    System.out.println(match.getCityName());
                    System.out.println(match.getState());
                    System.out.println(match.getPostalCode());
                    System.out.println(match.getLatitude());
                    System.out.println(match.getLongitude());
                }
            }
        }
        catch (ThrottlingException e)
        {
        }
        System.out.println("Time elapsed = " + (System.currentTimeMillis() - start) + " ms");

        /*
         * Test getPrimaryEnv API
         */
        /*
         * TnContext tncIn = new TnContext(); tncIn.addProperty(GET_VERSION_DATASET_KEY,"Navteq");
         * tncIn.addProperty(GET_VERSION_COUNTRY_KEY, "US"); //tncIn.addProperty(GET_VERSION_CONTINENT_KEY, "NA");
         * TnContext tncOut = GeoCodingServiceProxy.getInstance("dataset=Navteq").getPrimaryEnv(tncIn);
         * System.out.println(tncOut.toContextString());
         */
    }

    public TnContext getContextString()
    {
        return tnContext;
    }

    public void setContextString(TnContext tc)
    {
        this.tnContext = tc;
    }

    private GeoCodingServiceRequestDTO createDefaultAceServiceRequestDTO()
    {
        GeoCodingServiceRequestDTO dto = new GeoCodingServiceRequestDTO();
        dto.setClientName(clientName);
        dto.setClientVersion(clientVersion);
        dto.setContextString(tnContext.toContextString());
        dto.setTransactionId("");
        return dto;
    }

    private static ConfigurationContext getConfigContext(ServiceConfig serviceConfig) throws AxisFault
    {

        synchronized (GeoCodingProxy.class)
        {
            if (configContext == null)
            {
                configContext = Axis2Helper.createNewContext(serviceConfig.poolSizePerHost, serviceConfig.poolSizePerHost,
                    serviceConfig.maximumPoolSize);
            }
        }

        return configContext;

    }

}
