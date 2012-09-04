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
import com.telenav.cserver.backend.datatypes.AddressDataConverter;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.framework.throttling.ThrottlingManager;
import com.telenav.datatypes.locale.v10.Country;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.kernel.ws.axis2.Axis2Helper;
import com.telenav.services.geocoding.v40.GeoCodedAddress;
import com.telenav.services.geocoding.v40.GeoCodingServiceRequestDTO;
import com.telenav.services.geocoding.v40.GeoCodingServiceResponseDTO;
import com.telenav.services.geocoding.v40.GeoCodingServiceStub;



public class GeoCodingProxyV40
{  
	private static GeoCodingProxyV40 instance = new GeoCodingProxyV40();
	
    private static final Logger logger=Logger.getLogger(GeoCodingProxyV40.class);

    private static final String clientName = "ace-ws-client";

    private static final String clientVersion = "1.0.0.3";
    
    private static final String componentName = "POI";

    public static final String GET_VERSION_SERVER_KEY = "server_Version";

    public static final String GET_VERSION_CLIENT_KEY = "client_Version";

    public static final String GET_VERSION_DATA_KEY = "data_Version";

    public static final String GET_VERSION_DATASET_KEY = TnContext.PROP_MAP_DATASET;

    public static final String GET_VERSION_COUNTRY_KEY = "country";

    public static final String GET_VERSION_CONTINENT_KEY = "continent";

    public static final String VENDOR_NT = "Navteq";

    public static final String VENDOR_TA = "TeleAtlas";

    private static final String CLIENT_CFG = "config/aceClientV40.xml";

    // serviceConfigMap: key:dataset_country, value:url
    private static final HashMap<String,ServiceConfig> serviceConfigMap = new HashMap<String,ServiceConfig>();

    // defaultCountryOfContinent: key: continent, value:default country
   // private static final HashMap<String, Country> defaultCountryOfContinent = new HashMap<String, Country>();

    private final static String SERVICE_ACESERVER = "ACESERVER";
    //
    private static HashMap<String,String> hMapdatasets = new HashMap<String,String>();

//    private static Country defaultCountry = Country.US;

    // private String contextString = null;
    private TnContext tnContext;

    private static ConfigurationContext configContext = null;
    
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
       // defaultCountryOfContinent.put("EU", Country.GB);
        //defaultCountryOfContinent.put("NA", Country.US);
    }


    // singleton
    private GeoCodingProxyV40(){}

    public static GeoCodingProxyV40 getInstance(TnContext tc)
    {
        if(instance == null)
        {
        	synchronized (GeoCodingProxyV40.class)
			{
				if(instance == null)
				{
					instance = new GeoCodingProxyV40();
				}
			}
        }	
        instance.setContextString(tc);
        return instance;
    }
    
	public TnContext getContextString()
    {
    	return tnContext;
    }
    
    public void setContextString(TnContext tc)
    {
    	this.tnContext = tc;
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

    public GeoCodeResponseV40 geoCode(Address address) throws ThrottlingException
    {   
    	CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
		cli.setFunctionName("GeoCoding");
        if(address!=null) 
        {//record geocode address
        	cli.addData("Input Address", address.toString());
        	if(logger.isDebugEnabled()) 
        	{
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
        GeoCodeRequestV40 geoCodeReq =new GeoCodeRequestV40();
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
    
    
    public GeoCodeResponseV40 geoCode(GeoCodeRequestV40 geoCodeReq) throws ThrottlingException
    {   
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("GeoCoding");
        if(geoCodeReq.getAddress()!=null) 
        {//record geocode address
            cli.addData("Input Address", geoCodeReq.getAddress().toString());
            if(logger.isDebugEnabled()) 
            {
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

    
    /**
     * build up the grocoding for cli log
     * @param resp
     * @return
     */
    public String cliGeoCodingResponse(GeoCodingServiceResponseDTO resp) {
        StringBuilder sb=new StringBuilder();
        sb.append("ResponseDTO==>[");
        sb.append("status=");
        sb.append(resp.getStatus().getValue());
        sb.append("; message=");
        sb.append(resp.getStatus().getValue());
        sb.append(",count addr=");
        if(resp.getAddresses()!=null) 
        {
        	sb.append(resp.getAddresses().length);
        }
        sb.append("]");
        return sb.toString();
    }
    
    private static void getConfigInfo(String strPath)
    {
        try
        {
            ClassLoader classLoader = (GeoCodingProxyV40.class).getClassLoader();

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

                            Country country = getCountryFromString(strCountry);
                            if (country != null)
                            {
                                serviceConfigMap.put(serviceConfig.dataset + "_" + country.getValue(), serviceConfig);
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

    private static Country getCountryFromString(String countryName)
    {
    	if("#TEXT".equalsIgnoreCase(countryName))
    		return null;

    	Country country = null;
    	try
		{
    		country = Country.Factory.fromValue(countryName);
		} 
    	catch (Exception e)
		{
			logger.debug("Unsupportted country name: " + countryName);
		}
    	return country;
    }

    // end
    private GeoCodeResponseV40 geoCode(GeoCodeRequestV40 geoCodeReq, RequestType requestType ,CliTransaction cli)
    {
        if(requestType!=null&& logger.isDebugEnabled()) {//record geocode address
            logger.debug("Geo coding Arequest type======>" + requestType.toString());
        }
        GeoCodeResponseV40 response = null;
        GeoCodingServiceStub stub = null;
        
        String countryName = geoCodeReq.getAddress().getCountry();
        Country country = Country.Factory.fromValue(countryName);
        if (country == null)
        {
            country = Country.US;
        }

        String dataset = tnContext.getProperty(TnContext.PROP_MAP_DATASET);
        ServiceConfig serviceConfig = (ServiceConfig) serviceConfigMap.get(getVendorCode(dataset) + "_" + country.getValue());

        if (serviceConfig == null)
        {
            return GeoCodeResponseV40.NULL_RESPONSE;
        }

        try
        {
            stub = createStub(serviceConfig);
            GeoCodingServiceRequestDTO dto = createDefaultAceServiceRequestDTO();
            if(geoCodeReq.getTransactionID()!=null)
                dto.setTransactionId(geoCodeReq.getTransactionID());
           
            cli.addData("transactionId", geoCodeReq.getTransactionID());
            
            dto.setAddress(geoCodeReq.getAddressString());
            dto.setSearchArea(geoCodeReq.getSearchArea());
            
            GeoCodingServiceResponseDTO resp;
            switch (requestType)
            {
                case Geocode:
                    resp = stub.geoCode(dto);
                    break;
                case Standardize:
                    resp = stub.standardize(dto);
                    break;
                default:
                    return GeoCodeResponseV40.NULL_RESPONSE;
            }
            List<com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress> geoCodedAddresses = new ArrayList<com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress>();
            if (resp.getAddresses() != null)
            {
                for (int i = 0; i < resp.getAddresses().length; i++)
                {
                    GeoCodedAddress geoAddr = resp.getAddresses()[i];
                    com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress geoCodedAddress = AddressDataConverter.wsGeoCodedAddressToCSGeoCodedAddress(geoAddr);
                    geoCodedAddresses.add(geoCodedAddress);
                }
            }
            
            cli.addData("Response",cliGeoCodingResponse(resp));
            if(resp != null)
            {
                response = new GeoCodeResponseV40(geoCodedAddresses, resp.getStatus().getValue());
            }
            else
            {
                return GeoCodeResponseV40.NULL_RESPONSE;
            }
            if(response!=null && logger.isDebugEnabled()) {//record the geocode response
                logger.debug("Geo Code Response====> "+response.toString());
            }
        }
        catch (Exception ex)
        {
            logger.error("GeoCode Exception====> "+ex.getStackTrace());
            return GeoCodeResponseV40.NULL_RESPONSE;
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

    /**
     * @Description: create a default WS request
     * @return GeoCodingServiceRequestDTO
     * 
     */
    private GeoCodingServiceRequestDTO createDefaultAceServiceRequestDTO()
    {
        GeoCodingServiceRequestDTO dto = new GeoCodingServiceRequestDTO();
        dto.setComponentName(componentName);
        dto.setClientName(clientName);
        dto.setClientVersion(clientVersion);
        dto.setContextString(tnContext.toContextString());
        dto.setTransactionId("");
        return dto;
    }

    private static ConfigurationContext getConfigContext(ServiceConfig serviceConfig) throws AxisFault
    {
    	if( null == configContext)
    	{
    		synchronized (GeoCodingProxyV40.class)
    		{
    			if (configContext == null)
    			{
    				configContext = Axis2Helper.createNewContext(serviceConfig.poolSizePerHost, serviceConfig.poolSizePerHost,
    						serviceConfig.maximumPoolSize);
    			}
    		}    		
    	}
        return configContext;
    }
    
    private enum RequestType
    {
        Geocode, Standardize
    }
}