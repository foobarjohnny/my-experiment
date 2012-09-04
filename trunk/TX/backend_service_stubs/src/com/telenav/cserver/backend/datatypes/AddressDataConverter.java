package com.telenav.cserver.backend.datatypes;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.backend.datatypes.ace.GeoCodeSubStatus;
import com.telenav.cserver.framework.util.ProtoUtil;
import com.telenav.datatypes.address.v40.AddressAttribute;
import com.telenav.datatypes.address.v40.GeoCoordinate;
import com.telenav.datatypes.address.v40.Street;
import com.telenav.datatypes.locale.v10.Country;
import com.telenav.datatypes.locale.v10.Locale;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.services.geocoding.v40.GeoCodedAddress;
import com.telenav.services.geocoding.v40.SearchArea;


/**
 * This is a common class for conversion in some Address related classes.
 * @author cguo
 * xfliu update 2011-11-22
 */
public class AddressDataConverter
{
	private static Logger log = Logger.getLogger(AddressDataConverter.class);
	/**
	 * @Description: convert backend_service_stub Address to v40.address or its subclass
	 * @param <T>
	 * @param srcAddr
	 * @param targetAddr
	 * @return
	 * 
	 */
	public static <T extends com.telenav.datatypes.address.v40.Address> T convertCSAddressToWSAddressV40(Address srcAddr, T targetAddr)
	{
		if(null == targetAddr)
		{
			return null;
		}
		
		if(null != srcAddr)
		{	
			targetAddr.setAddressId(srcAddr.getAddressId());
			targetAddr.setBuildingName(srcAddr.getBuildingName());
			targetAddr.setCity(srcAddr.getCityName());
			
			Country country = null;
			try
			{
				country = Country.Factory.fromValue(srcAddr.getCountry());
			} catch (Exception e)
			{
				log.debug("Invalid country >>>>>>>>>>>>>>>>>>>>>" + srcAddr.getCountry());
			}
			
			targetAddr.setCountry(country);
			targetAddr.setCounty(srcAddr.getCounty());
			
			Street crossStreet = new Street();
			crossStreet.setName(srcAddr.getCrossStreetName());
			
			targetAddr.setCrossStreet(crossStreet);
			GeoCoordinate coordinate = new GeoCoordinate();
			coordinate.setLatitude(srcAddr.getLatitude());
			coordinate.setLongitude(srcAddr.getLongitude());
			targetAddr.setGeoCoordinate(coordinate);
			targetAddr.setHouseNumber(srcAddr.getDoor());
			
			Locale locale = new Locale();
			locale.setCountry(country);
			
			targetAddr.setLocale(locale);
			targetAddr.setLocality(srcAddr.getLocality());
			targetAddr.setPostalCode(srcAddr.getPostalCode());
			targetAddr.setState(srcAddr.getState());
			
			Street streetName = new Street();
			streetName.setName(srcAddr.getStreetName());
			targetAddr.setStreet(streetName);
			targetAddr.setSubLocality(srcAddr.getSublocality());
			targetAddr.setSubStreet(srcAddr.getSubStreet());
			targetAddr.setSuite(srcAddr.getSuite());
			
			log.debug("Class Name =="+ targetAddr.getClass().getName() + " , " + targetAddr.toString());
		}
		
		return targetAddr;
	}
	/**
	 * @Description: get formatted address string
	 * @param address
	 * @return
	 * 
	 */
	public static String getAddressString(Address address)
	{
		if(null == address || null == address.getLines())
		{
			log.debug("setLines(): address or address.getLines() is null!!");
			return null;
		}
		String countryString = address.getCountry();
		
		log.debug("getAddressString():countryString >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + countryString);
		
		if(null== countryString || "".equals(countryString))
			return "";
		
		String formattedAddress = "";

		if("US".equalsIgnoreCase(countryString))//
		{
			formattedAddress = getUSAddress(address);
		}
		else if("CA".equalsIgnoreCase(countryString))//Canada
		{
			formattedAddress = getCanadaAddress(address);
		}
		else if("BR".equalsIgnoreCase(countryString))//Brazil
		{
			formattedAddress = getBrazilAddress(address);
		}
		else if("IN".equalsIgnoreCase(countryString))//India
		{
			formattedAddress = getIndiaAddress(address);
		}
		else if("MX".equalsIgnoreCase(countryString))//Mexico
		{
			formattedAddress = getMexicoAddress(address);
		}
		else//Europe
		{
			formattedAddress = getEuropeAddress(address);
		}
		
		log.debug("getAddressString():addressString >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + formattedAddress);
		
		return formattedAddress;
	}
	
	/**
	 * @Description: Get SearchArea
	 * @param address
	 * @return SearchArea
	 * 
	 */
	public static SearchArea getSearchArea(Address address)
	{
		if(address != null)
		{
			SearchArea searchArea = new SearchArea();
			searchArea.addCountryList(Country.Factory.fromValue(address.getCountry()));
			
			boolean addCountryToSearchArea = true;
			Country[] countryInSearchArea = null;
			ArrayList<Country> countryListInAddress = address.getGeoCodingCountryList();
			
			if(null != countryListInAddress)
			{
				for(Country countryListInAddressItem : countryListInAddress)
				{
					addCountryToSearchArea = true;
					countryInSearchArea = searchArea.getCountryList();
					if(countryInSearchArea != null)
					{
						for(Country countryInSearchAreaItem : countryInSearchArea)
						{
							
							if(countryInSearchAreaItem.getValue().equals(countryListInAddressItem.getValue()))
							{
								addCountryToSearchArea = false;
								break;
							}
						}
					}
					if(addCountryToSearchArea)
					{
						searchArea.addCountryList(countryListInAddressItem);
					}
				}
			}
			
			GeoCoordinate geo = new GeoCoordinate();
			geo.setLatitude(address.getLatitude());
			geo.setLongitude(address.getLongitude());
			searchArea.setAnchorPoint(geo);
			return searchArea;
		}
		return null;
	}
	
	/**
	 * @Description: convert WSAddress to backend_stub GeocodedAddress
	 * @param address
	 * @return AddressV40
	 * 
	 */
	public static com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress wsGeoCodedAddressToCSGeoCodedAddress(GeoCodedAddress address)
	{
		com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress geoCodedAddress = new com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress();
		
		if(null != address)
		{
			geoCodedAddress.setDoor(address.getHouseNumber());
			geoCodedAddress.setSuite(address.getSuite());
			geoCodedAddress.setStreetName(getStreetName(address.getStreet()));
			geoCodedAddress.setCrossStreetName(getStreetName(address.getCrossStreet()));
			geoCodedAddress.setCityName(address.getCity());
			geoCodedAddress.setCounty(address.getCounty());
			geoCodedAddress.setState(address.getState());
			geoCodedAddress.setCountry(address.getCountry() == null ? "" : address.getCountry().getValue());
			geoCodedAddress.setPostalCode(address.getPostalCode());
			if(address.getGeoCoordinate() != null)
			{
				geoCodedAddress.setLatitude(address.getGeoCoordinate().getLatitude());
				geoCodedAddress.setLongitude(address.getGeoCoordinate().getLongitude());
			}
			geoCodedAddress.setSublocality(address.getSubLocality());
			geoCodedAddress.setLocality(address.getLocality());
	
			geoCodedAddress.setSubStreet(address.getSubStreet());
			geoCodedAddress.setBuildingName(address.getBuildingName());
			geoCodedAddress.setAddressId(address.getAddressId());
			geoCodedAddress.setAddressScore(address.getScore()!=null?address.getScore().getTotalScore():-1.0f);
			setLines(geoCodedAddress,address);
			log.debug("wsGeoCodedAddressToCSGeoCodedAddress():geoCodedAddress >>>>>>>>>>>>>>>>>>>>>" + geoCodedAddress.toString());
			
		}else{
			log.debug("setLines(): GeoCodedAddress is null!!");
		}
		
		return geoCodedAddress;
	}

	private static String getUSAddress(Address address)
	{
		//i.e:<ALL>=1130 Kifer Road, Sunnyvale, CA, 94089 
		StringBuilder addressStr = new StringBuilder();
		addressStr.append("<")
			.append(AddressAttribute.ALL)
			.append(">=")
			.append(address.getLines().get(AddressFormatConstants.ALL));
		return addressStr.toString();
	}
	
	private static String getBrazilAddress(Address address)
	{
		//i.e:<HOUSE_NUMBER,STREET>=1130 Kifer Road;<LOCALITY>= ;<CITY,STATE,POSTAL_CODE>=Sunnyvale CA 94089 
		StringBuilder addressStr = new StringBuilder();
		addressStr.append("<")
			.append(AddressAttribute.HOUSE_NUMBER).append(",")
			.append(AddressAttribute.STREET)
			.append(">=")
			.append(address.getLines().get(AddressFormatConstants.FIRST_LINE)).append(";")
			.append("<")
			.append(AddressAttribute.LOCALITY)
			.append(">=")
			.append(address.getLines().get(AddressFormatConstants.LOCALITY)).append(";")
			.append("<")
			.append(AddressAttribute.CITY).append(",")
			.append(AddressAttribute.STATE).append(",")
			.append(AddressAttribute.POSTAL_CODE)
			.append(">=")
			.append(address.getLines().get(AddressFormatConstants.LAST_LINE));
		return addressStr.toString();
	}
	
	private static String getMexicoAddress(Address address)
	{
		//i.e:<HOUSE_NUMBER,STREET>=1130 Kifer Road;<CITY>=Sunnyvale;<COUNTY>=Santa Clara;<STATE>=CA;<POSTAL_CODE>=94089 
		StringBuilder addressStr = new StringBuilder();
		addressStr.append("<")
			.append(AddressAttribute.HOUSE_NUMBER).append(",")
			.append(AddressAttribute.STREET)
			.append(">=")
			.append(address.getLines().get(AddressFormatConstants.FIRST_LINE)).append(";")
			.append("<")
			.append(AddressAttribute.CITY)
			.append(">=")
			.append(address.getLines().get(AddressFormatConstants.CITY)).append(";")
			.append("<")
			.append(AddressAttribute.COUNTY)
			.append(">=")
			.append(address.getLines().get(AddressFormatConstants.COUNTY)).append(";")
			.append("<")
			.append(AddressAttribute.STATE)
			.append(">=")
			.append(address.getLines().get(AddressFormatConstants.STATE)).append(";")
			.append("<")
			.append(AddressAttribute.CITY).append(",")
			.append(AddressAttribute.COUNTY).append(",")
			.append(AddressAttribute.POSTAL_CODE).append(">=")
			.append(address.getLines().get(AddressFormatConstants.CIYT_COUNTY_OR_POSTAL_CODE)).append(";")
			.append("<")
			.append(AddressAttribute.POSTAL_CODE)
			.append(">=")
			.append(address.getLines().get(AddressFormatConstants.POSTAL_CODE));
		return addressStr.toString();
	}
	
	private static String getCanadaAddress(Address address)
	{
		//i.e:<HOUSE_NUMBER,STREET>=1130 Kifer Road;<CITY,STATE,POSTAL_CODE>=Sunnyvale CA 94089
		StringBuilder addressStr = new StringBuilder();
		addressStr.append("<")
			.append(AddressAttribute.HOUSE_NUMBER).append(",")
			.append(AddressAttribute.STREET)
			.append(">=")
			.append(address.getLines().get(AddressFormatConstants.FIRST_LINE)).append(";")
			.append("<")
			.append(AddressAttribute.CITY).append(",")
			.append(AddressAttribute.STATE).append(",")
			.append(AddressAttribute.POSTAL_CODE)
			.append(">=")
			.append(address.getLines().get(AddressFormatConstants.LAST_LINE));		
		return addressStr.toString();
	}
	
	private static String getEuropeAddress(Address address)
	{
		//i.e:<HOUSE_NUMBER,STREET>=1130, Kifer Road;<CITY>=Sunnyvale;<POSTAL_CODE>=94089 
		StringBuilder addressStr = new StringBuilder();
		addressStr.append("<")
			.append(AddressAttribute.HOUSE_NUMBER).append(",")
			.append(AddressAttribute.STREET)
			.append(">=")
			.append(address.getLines().get(AddressFormatConstants.FIRST_LINE)).append(";")
			.append("<")
			.append(AddressAttribute.CITY)
			.append(">=")
			.append(address.getLines().get(AddressFormatConstants.CITY)).append(";")
			.append("<")
			.append(AddressAttribute.POSTAL_CODE)
			.append(">=")
			.append(address.getLines().get(AddressFormatConstants.POSTAL_CODE));		
		return addressStr.toString();
	}
	
	private static String getIndiaAddress(Address address)
	{
		//i.e:<HOUSE_NUMBER,STREET, SUB_LOCALITY>=1130 Kifer Road;<LOCALITY,CITY,COUNTY,STATE,POSTAL_CODE>=Sunnyvale CA 94089
		StringBuilder addressStr = new StringBuilder();
		addressStr.append("<")
			.append(AddressAttribute.HOUSE_NUMBER).append(",")
			.append(AddressAttribute.STREET).append(",")
			.append(AddressAttribute.SUB_LOCALITY)
			.append(">=")
			.append(address.getLines().get(AddressFormatConstants.FIRST_LINE)).append(";")
			.append("<")
			.append(AddressAttribute.LOCALITY).append(",")
			.append(AddressAttribute.CITY).append(",")
			.append(AddressAttribute.COUNTY).append(",")
			.append(AddressAttribute.STATE).append(",")
			.append(AddressAttribute.POSTAL_CODE)
			.append(">=")
			.append(address.getLines().get(AddressFormatConstants.LAST_LINE));	
		return addressStr.toString();
	}
	
	private static void setLines(Address csAddress, com.telenav.datatypes.address.v40.Address address)
	{
		if(null == address)
		{
			return;
		}
		HashMap<String, String> tempMap = new HashMap<String, String>();
		
		String countryName = "US"; //defaultValue
		
		if (null != address.getCountry())
		{
			countryName = address.getCountry().getValue();
		}	
		
		String firstLine = "", lastLine = "";
		if(Constants.US_COUNTRY.equalsIgnoreCase(countryName))//
		{	
			firstLine = getFormattedString(true,address.getHouseNumber(),getStreetName(address.getStreet()));
			lastLine = getFormattedString(false,address.getCity(), address.getState(), address.getPostalCode());
			
			tempMap.put(AddressFormatConstants.ALL, getFormattedString(false,address.getHouseNumber(),getStreetName(address.getStreet()),address.getCity(), address.getState(), address.getPostalCode()));
			tempMap.put(AddressFormatConstants.FIRST_LINE, firstLine);
			tempMap.put(AddressFormatConstants.LAST_LINE, lastLine);
		}
		else if(Constants.CA_COUNTRY.equalsIgnoreCase(countryName))//Canada
		{
			firstLine = getFormattedString(true, address.getHouseNumber(),getStreetName(address.getStreet()));
			lastLine = getFormattedString(false, address.getCity(), address.getState(), address.getPostalCode());
			
			tempMap.put(AddressFormatConstants.FIRST_LINE, firstLine);
			tempMap.put(AddressFormatConstants.LAST_LINE, lastLine);
		}
		else if(Constants.BR_COUNTRY.equalsIgnoreCase(countryName))//Brazil
		{
			firstLine = getFormattedString(true, getStreetName(address.getStreet()), address.getHouseNumber());
			lastLine = getFormattedString(false, address.getCity(), address.getState(), address.getPostalCode());
			
			tempMap.put(AddressFormatConstants.FIRST_LINE, firstLine);
			tempMap.put(AddressFormatConstants.LOCALITY, getFormattedString(false, address.getLocality()));
			tempMap.put(AddressFormatConstants.LAST_LINE, lastLine);
		}
		else if(Constants.IN_COUNTRY.equalsIgnoreCase(countryName))//India
		{
			firstLine = getFormattedString(true, address.getHouseNumber(),getStreetName(address.getStreet()),address.getSubLocality());
			lastLine = getFormattedString(false, address.getLocality(), address.getCity(), address.getCounty(), address.getState(), address.getPostalCode());
			
			tempMap.put(AddressFormatConstants.FIRST_LINE, firstLine);
			tempMap.put(AddressFormatConstants.LAST_LINE, lastLine);
		}
		else if(Constants.MX_COUNTRY.equalsIgnoreCase(countryName))//Mexico
		{
			firstLine = getFormattedString(true, getStreetName(address.getStreet()), address.getHouseNumber());
			
			tempMap.put(AddressFormatConstants.FIRST_LINE, firstLine);
			tempMap.put(AddressFormatConstants.CITY, getFormattedString(false, address.getCity()));
			tempMap.put(AddressFormatConstants.COUNTY, getFormattedString(false, address.getCounty()));
			tempMap.put(AddressFormatConstants.STATE, getFormattedString(false, address.getState()));
			tempMap.put(AddressFormatConstants.POSTAL_CODE, getFormattedString(false, address.getPostalCode()));
		}
		else//Europe
		{
			firstLine = getFormattedString(true, address.getHouseNumber(),getStreetName(address.getStreet()));
			
			tempMap.put(AddressFormatConstants.FIRST_LINE, firstLine);
			tempMap.put(AddressFormatConstants.CITY, getFormattedString(false, address.getCity()));
			tempMap.put(AddressFormatConstants.POSTAL_CODE, getFormattedString(false, address.getPostalCode()));
		}
		csAddress.setFirstLine(firstLine);
		csAddress.setLastLine(lastLine);
		csAddress.setLines(tempMap);
		return ;
	}
	
	private static String getStreetName(Street street)
	{
		return street == null? "" : street.getName();
	}
	
	private static String getFormattedString(boolean isFirstLine, String... str)
	{
		if(null == str || str.length < 1)
		{
			return "";
		}
		StringBuilder strBuilder = new StringBuilder();
		for(int i = 0; i < str.length; i++)
		{
			if(null == str[i] || "".equals(str[i]))
			{
				continue;
			}
			if(isFirstLine)
			{
				strBuilder.append(str[i] + " ");
			}
			else
			{
				strBuilder.append(str[i] + ",");
			}
		}
		String result = strBuilder.toString();
		if("".equals(result))
			return "";
		return result.substring(0, result.length()-1);
	}

	/**
	 * @Description: convert webservice Address to backend_service_stub Address
	 * @param wsAddr
	 * @return
	 * 
	 */
	public static com.telenav.cserver.backend.datatypes.Address convertWSAddressToAddress(com.telenav.ws.datatypes.address.Address wsAddr)
	{
		com.telenav.cserver.backend.datatypes.Address address = new com.telenav.cserver.backend.datatypes.Address();
		
		if(wsAddr != null)
		{
			address.setCityName(wsAddr.getCity());
			address.setCountry(wsAddr.getCountry());
			address.setCounty(wsAddr.getCounty());
			address.setCrossStreetName(wsAddr.getCrossStreetName());
			address.setDoor(wsAddr.getStreetNumber());
			address.setFirstLine(wsAddr.getFirstLine());
			address.setLastLine(wsAddr.getLastLine());
			if(wsAddr.getGeoCode() != null)
			{
				address.setLatitude(wsAddr.getGeoCode().getLatitude());
				address.setLongitude(wsAddr.getGeoCode().getLongitude());
			}
			address.setPostalCode(wsAddr.getPostalCode());
			address.setState(wsAddr.getState());
			address.setStreetName(wsAddr.getStreetName());
			address.setLabel(wsAddr.getLabel());
			log.debug("convertWSAddressToAddress():address >>>>>>>>>>>>>>>>>>>>>" + address.toString());
		}
		
		return address;
	}
	
	/**
	 * @Description: Convert backend_service_stub Address or GeoCodedAddress to webservice Address
	 * @param addr
	 * @return
	 * 
	 */
	public static com.telenav.ws.datatypes.address.Address convertAddressToWSAddress(com.telenav.cserver.backend.datatypes.Address addr)
	{
		com.telenav.ws.datatypes.address.Address address = new com.telenav.ws.datatypes.address.Address();
		if(addr != null)
		{
			address.setCity(addr.getCityName());
			address.setCountry(addr.getCountry());
			address.setCounty(addr.getCounty());
			address.setFirstLine(addr.getFirstLine());
			address.setCrossStreetName(addr.getCrossStreetName());
			
			com.telenav.ws.datatypes.address.GeoCode geoCode = new com.telenav.ws.datatypes.address.GeoCode();
			geoCode.setLatitude(addr.getLatitude());
			geoCode.setLongitude(addr.getLongitude());
			address.setGeoCode(geoCode);
			
			address.setLastLine(addr.getLastLine());
			address.setPostalCode(addr.getPostalCode());
			address.setState(addr.getState());
			address.setStreetName(addr.getStreetName());
		}
		log.debug("convertWSAddressToAddress():address >>>>>>>>>>>>>>>>>>>>>" + address.toString());
		return address;
	}
	
	
	/**
	 * @Description: convert com.telenav.j2me.datatypes.Stop to GeoCodedAddress in backend_service_stub
	 * @param stop
	 * @return GeoCodedAddress
	 * 
	 */
	
	public static com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress convertStopToGeoCodedAddress(Stop stop) 
	{

		com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress geoAddr = new com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress();

		if(null != stop)
		{
			geoAddr = convertStopToCSAddress(stop, geoAddr);
			geoAddr.setSubStatus(new GeoCodeSubStatus());			//dummy GeoCodeSubStatus
			log.debug("convertStopToGeoCodedAddress: GeoCodedAddress: " + geoAddr);
			
		}
		else
		{
			log.debug("convertStopToGeoCodedAddress(): stop is null!!");
		}
		return geoAddr;
	}
	
	/**
	 * @Description: convert GeoCodedAddress to com.telenav.j2me.datatypes.Stop in backend_service_stub
	 * @param stop
	 * @return GeoCodedAddress
	 * 
	 */
	
	public static Stop convertGeoCodedAddressToStop(com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress geoCodeAddress) 
	{
		return convertCSAddressToStop(geoCodeAddress);		
	}
	
	/**
	 * @Description: convert Address in backend_service_stub to com.telenav.j2me.datatypes.Stop
	 * @param  Address
	 * @return
	 */

	public static <T extends com.telenav.cserver.backend.datatypes.Address> Stop convertCSAddressToStop(T addr)
	{
		log.debug("convertCSAddressToStop(): Address : " + addr);
		Stop stop = new Stop();
		
		if(addr != null)
		{		
			stop.firstLine = addr.getFirstLine();
			stop.city = addr.getCityName();
			stop.state = addr.getState();
			stop.country = addr.getCountry();
			stop.county = addr.getCounty();
			stop.label = addr.getLabel();
			stop.lat = convertToDM5(addr.getLatitude());
			stop.lon = convertToDM5(addr.getLongitude());
			stop.zip = addr.getPostalCode();
			stop.isGeocoded = true;		
			
			stop.setExtraProperty(Stop.KEY_ADDRESSID, addr.getAddressId());
	        stop.setExtraProperty(Stop.KEY_BUILDINGNAME, addr.getBuildingName());
	        stop.setExtraProperty(Stop.KEY_LOCALE, addr.getLocale());
	        stop.setExtraProperty(Stop.KEY_LOCALITY, addr.getLocality());
	        stop.setExtraProperty(Stop.KEY_SUBLOCALITY, addr.getSublocality());
	        stop.setExtraProperty(Stop.KEY_SUBSTREET, addr.getSubStreet());
	        stop.setExtraProperty(Stop.KEY_SUITE, addr.getSuite());
			stop.setExtraProperty( Stop.KEY_HOUSENUMBER , addr.getDoor() );
			stop.setExtraProperty( Stop.KEY_STREETNAME , addr.getStreetName() );
	        log.debug("convertCSAddressToStop(): stop >>>>>>>>>>>>>>>>>>>>>" + stop.toString());
		}
		
		return stop;
	}

	/**
	 * @Description: convert com.telenav.j2me.datatypes.Stop to Address in backend_service_stub 
	 * @param  Address
	 * @return
	 */

	public static <T extends com.telenav.cserver.backend.datatypes.Address> T convertStopToCSAddress(Stop stop, T targetAddr)
	{
		log.debug("convertStopToCSAddress(): Stop: " + stop);
		if(stop != null)
		{		
	        targetAddr.setPostalCode(stop.zip);
	        targetAddr.setCityName(stop.city);
	        targetAddr.setState(stop.state);
	        targetAddr.setFirstLine(stop.firstLine);
	        targetAddr.setCountry(stop.country);
	        targetAddr.setCounty(stop.county);
	        targetAddr.setLabel(stop.label);
	
	        targetAddr.setLatitude(convertToDegree(stop.lat));
	        targetAddr.setLongitude(convertToDegree(stop.lon));        
	        
	        targetAddr.setAddressId((String)stop.getExtraProperty(Stop.KEY_ADDRESSID));  
	        
	        targetAddr.setBuildingName((String)stop.getExtraProperty(Stop.KEY_BUILDINGNAME)); 
	      
	        targetAddr.setLocale((String)stop.getExtraProperty(Stop.KEY_LOCALE));
	                    
	        targetAddr.setLocality((String)stop.getExtraProperty(Stop.KEY_LOCALITY));
	        
	        targetAddr.setSublocality((String)stop.getExtraProperty(Stop.KEY_SUBLOCALITY));
	        
	        targetAddr.setSubStreet((String)stop.getExtraProperty(Stop.KEY_SUBSTREET));
	        
	        targetAddr.setSuite((String)stop.getExtraProperty(Stop.KEY_SUITE));        
			
	        targetAddr.setDoor( (String)stop.getExtraProperty( Stop.KEY_HOUSENUMBER ) );
	        targetAddr.setStreetName( (String)stop.getExtraProperty( Stop.KEY_STREETNAME )  );
	        log.debug("convertStopToCSAddress(): Address >>>>>>>>>>>>>>>>>>>>>" + targetAddr.toString());
		}
		
        return targetAddr;
	}
	/**
	 * @param lon
	 * @return
	 */
	public static double convertToDegree(int dm5)
	{
		return dm5 / Constants.DEGREE_MULTIPLIER;
	}
	
	/**
	 * @param latitude
	 * @return
	 */
	public static int convertToDM5(double degree)
	{
		return (int) (degree * Constants.DEGREE_MULTIPLIER);
	}

	/**
	 * 
	 * @Description: convert webservice Address to backend_service_stub Address
	 * @param wsAddr
	 * @return
	 * 
	 */
	public static <T extends com.telenav.cserver.backend.datatypes.Address> T convertWSAddressV40ToCSAddress(com.telenav.datatypes.address.v40.Address wsaddress, T targetAddress)
	{
		if(targetAddress == null)
		{
			return null;
		}
		if(null != wsaddress){

			targetAddress.setDoor(wsaddress.getHouseNumber());
			targetAddress.setSuite(wsaddress.getSuite());
			targetAddress.setStreetName(getStreetName(wsaddress.getStreet()));
			targetAddress.setCrossStreetName(getStreetName(wsaddress.getCrossStreet()));
			targetAddress.setCityName(wsaddress.getCity());
			targetAddress.setCounty(wsaddress.getCounty());
			targetAddress.setState(wsaddress.getState());
			targetAddress.setCountry(wsaddress.getCountry() == null ? "" : wsaddress.getCountry().getValue());
			targetAddress.setPostalCode(wsaddress.getPostalCode());
			if(wsaddress.getGeoCoordinate() != null)
			{
				targetAddress.setLatitude(wsaddress.getGeoCoordinate().getLatitude());
				targetAddress.setLongitude(wsaddress.getGeoCoordinate().getLongitude());
			}
			targetAddress.setSublocality(wsaddress.getSubLocality());
			targetAddress.setLocality(wsaddress.getLocality());
	
			targetAddress.setSubStreet(wsaddress.getSubStreet());
			targetAddress.setBuildingName(wsaddress.getBuildingName());
			targetAddress.setAddressId(wsaddress.getAddressId());
			if(wsaddress.getLocale()!=null && wsaddress.getLocale().getLanguage()!=null &&wsaddress.getLocale().getCountry()!=null){
				targetAddress.setLocale(wsaddress.getLocale().getLanguage().getValue()+"_"+wsaddress.getLocale().getCountry().getValue());
			}
			setLines(targetAddress,wsaddress);
			log.debug("convertWSAddressV40ToAddress():address >>>>>>>>>>>>>>>>>>>>>" + targetAddress.toString());
		}
		
		return targetAddress;
	}

	public static Stop JSONObject2Stop(JSONObject jsAddr) {
		Stop stop = new Stop();
        try {
        	if(jsAddr.has("lat"))
        	{
        		stop.lat = jsAddr.getInt("lat");
        	}
        	if(jsAddr.has("lon"))
        	{
        		stop.lon = jsAddr.getInt("lon");
        	}
        	if(jsAddr.has("label"))
        	{
            	stop.label = jsAddr.getString("label");
        	}
        	if(jsAddr.has("firstLine"))
        	{
        		stop.firstLine = jsAddr.getString("firstLine");
        	}
        	if(jsAddr.has("city"))
        	{
        		stop.city = jsAddr.getString("city");
        	}
        	if(jsAddr.has("state"))
        	{
            	stop.state = jsAddr.getString("state");
        	}
        	if(jsAddr.has("zip"))
        	{
            	stop.zip = jsAddr.getString("zip");
        	}
        	if(jsAddr.has("country"))
        	{
            	stop.country = jsAddr.getString("country");
        	}
        	if(jsAddr.has(Stop.KEY_LOCALITY))
        	{
            	stop.setExtraProperty(Stop.KEY_LOCALITY, jsAddr.get(Stop.KEY_LOCALITY));
        	}
        	if(jsAddr.has(Stop.KEY_SUITE))
        	{
            	stop.setExtraProperty(Stop.KEY_SUITE, jsAddr.get(Stop.KEY_SUITE));
            }
        	if(jsAddr.has(Stop.KEY_SUBLOCALITY))
        	{
            	stop.setExtraProperty(Stop.KEY_SUBLOCALITY, jsAddr.get(Stop.KEY_SUBLOCALITY));
        	}
        	if(jsAddr.has(Stop.KEY_LOCALE))
        	{
            	stop.setExtraProperty(Stop.KEY_LOCALE, jsAddr.get(Stop.KEY_LOCALE));
        	}
        	if(jsAddr.has(Stop.KEY_SUBSTREET))
        	{
            	stop.setExtraProperty(Stop.KEY_SUBSTREET, jsAddr.get(Stop.KEY_SUBSTREET));        		
        	}
        	if(jsAddr.has(Stop.KEY_BUILDINGNAME))
        	{
            	stop.setExtraProperty(Stop.KEY_BUILDINGNAME, jsAddr.get(Stop.KEY_BUILDINGNAME));
        	}
        	if(jsAddr.has(Stop.KEY_ADDRESSID))
        	{
        		stop.setExtraProperty(Stop.KEY_ADDRESSID, jsAddr.get(Stop.KEY_ADDRESSID));
        	}
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return stop;
	}

	public static JSONObject Stop2JSONObject(Stop stop) {
		
        JSONObject stopJo = new JSONObject();
        if(stop != null)
        {
	        try {
	            stopJo.put("firstLine", stop.firstLine);
	            stopJo.put("city", stop.city);
	            stopJo.put("state", stop.state);
	            stopJo.put("country", stop.country);
	            stopJo.put("zip", stop.zip);
	            stopJo.put("lat", stop.lat);
	            stopJo.put("lon", stop.lon);
	            stopJo.put("label", stop.label);
	            /**
	             *  add for Class com.telenav.cserver.commuteAlerts.protocol.CommuteAlertResponseFormatter
	             **/
	            stopJo.put("isGeocoded", stop.isGeocoded ? 1 : 0);
	            /**
	             *  add for Class com.telenav.cserver.commuteAlerts.html.protocol.HtmlAlertdataBuilder
	             **/
	            stopJo.put("province", stop.state);
	            
	        	stopJo.put("suite", convertNull((String) stop.getExtraProperty(Stop.KEY_SUITE)));
	        	stopJo.put("sublocality", convertNull((String) stop.getExtraProperty(Stop.KEY_SUBLOCALITY)));
	        	stopJo.put("locality", convertNull((String) stop.getExtraProperty(Stop.KEY_LOCALITY)));
	        	stopJo.put("locale", convertNull((String) stop.getExtraProperty(Stop.KEY_LOCALE)));
	        	stopJo.put("subStreet", convertNull((String) stop.getExtraProperty(Stop.KEY_SUBSTREET)));
	        	stopJo.put("buildingName", convertNull((String) stop.getExtraProperty(Stop.KEY_BUILDINGNAME)));
	        	stopJo.put("addressId", convertNull((String) stop.getExtraProperty(Stop.KEY_ADDRESSID)));
	        } catch (JSONException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
        }
        
		return stopJo;
	}

	public static Address JSONObject2Address(JSONObject jsAddr) {
		Address address = new Address(); 
		convertStopToCSAddress(JSONObject2Stop(jsAddr), address);
		return address;
	}

	public static JSONObject Address2JSONObject(Address address) {
		return Stop2JSONObject(convertCSAddressToStop(address));
	}
	
	public static Stop TnPoi2Stop(TnPoi tnPoi)
    {
        Stop stop = new Stop();
        
        if (tnPoi != null)
        { 
	        Address addr = tnPoi.getAddress();
	        stop.firstLine = tnPoi.getBrandName();
	        if (addr != null)
	        {
	            stop.city = addr.getCityName();
	            stop.state = addr.getState();
	            stop.zip = addr.getPostalCode();
	            stop.lat = convertToDM5(addr.getLatitude());
	            stop.lon = convertToDM5(addr.getLongitude());
	            stop.country=addr.getCountry();
	            stop.county=addr.getCounty();
	            stop.zip=addr.getPostalCode();
	            
	            stop.setExtraProperty(Stop.KEY_SUITE,addr.getSuite());
	            stop.setExtraProperty(Stop.KEY_SUBLOCALITY,addr.getSublocality());
	            stop.setExtraProperty(Stop.KEY_LOCALITY,addr.getLocality());
	            stop.setExtraProperty(Stop.KEY_LOCALE,addr.getLocale());
	            stop.setExtraProperty(Stop.KEY_SUBSTREET,addr.getSubStreet());
	            stop.setExtraProperty(Stop.KEY_BUILDINGNAME,addr.getBuildingName());
	            stop.setExtraProperty(Stop.KEY_ADDRESSID,addr.getAddressId());
	            stop.setExtraProperty(Stop.KEY_STREETNAME, addr.getStreetName());
	            stop.setExtraProperty(Stop.KEY_HOUSENUMBER, addr.getDoor());
	            stop.isGeocoded = true;
	        }
        }
        
        return stop;
    }
	
	public static String convertNull(String s) {
        if (null == s) {
            s = "";
        }
        return s;
    }
}
