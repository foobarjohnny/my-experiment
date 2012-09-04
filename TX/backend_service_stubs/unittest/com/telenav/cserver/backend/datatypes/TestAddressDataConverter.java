package com.telenav.cserver.backend.datatypes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.telenav.datatypes.address.v40.Street;
import com.telenav.datatypes.locale.v10.Country;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.services.geocoding.v40.GeoCodedAddress;

public class TestAddressDataConverter {

	static
	{
		Logger.getLogger(AddressDataConverter.class).setLevel(Level.DEBUG);
	}
	JSONObject stopJo = null;

	static Stop stop = null;

	@Before
	public void init() {
		stopJo = new JSONObject();

		try {
			stopJo.put("firstLine", "firstLine");
			stopJo.put("city", "city");
			stopJo.put("state", "state");
			stopJo.put("country", "country");
			stopJo.put("lon", 12.1212);
			stopJo.put("zip", "90551");
			stopJo.put("lat", 13.1313);
			stopJo.put("label", "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testJSONObject2Stop() {
		Stop stop = AddressDataConverter.JSONObject2Stop(stopJo);
		System.out.println(stop.toString());
	}
	
	@Test
	public void testInputAddressFormat()
	{
		Address address = new Address();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(AddressFormatConstants.ALL, "T_All");
		map.put(AddressFormatConstants.CITY, "T_City");
		map.put(AddressFormatConstants.COUNTY, "T_County");
		map.put(AddressFormatConstants.CROSS_STREET, "T_Cross_Street");
		map.put(AddressFormatConstants.FIRST_LINE, "T_First_Line");
		map.put(AddressFormatConstants.LAST_LINE, "T_Last_Line");
		map.put(AddressFormatConstants.LOCALITY, "T_Locality");
		map.put(AddressFormatConstants.POSTAL_CODE, "T_Postal_Code");
		map.put(AddressFormatConstants.STATE, "T_State");
		map.put(AddressFormatConstants.STREET, "T_Street");
		address.setLines(map);	
		
		address.setCountry("US");
		Assert.assertNotSame("", AddressDataConverter.getAddressString(address));
		
		address.setCountry("CA");
		Assert.assertNotSame("", AddressDataConverter.getAddressString(address));

		address.setCountry("MX");
		Assert.assertNotSame("", AddressDataConverter.getAddressString(address));
		
		address.setCountry("BR");
		Assert.assertNotSame("", AddressDataConverter.getAddressString(address));
		
		address.setCountry("IN");
		Assert.assertNotSame("", AddressDataConverter.getAddressString(address));
		
		address.setCountry("EU");
		Assert.assertNotSame("", AddressDataConverter.getAddressString(address));
	}
	
	@Test
	public void testOutputAddressFormat()
	{
		GeoCodedAddress wsAddress = new GeoCodedAddress();
		wsAddress.setHouseNumber("T_HouseNumber");
		Street s = new Street();
		s.setName("T_Street");
		wsAddress.setStreet(s);
		wsAddress.setSubLocality("T_SubLocality");
		wsAddress.setLocality("T_Locality");
		wsAddress.setCity("T_CityName");
		wsAddress.setCounty("T_County");
		wsAddress.setState("T_State");
		wsAddress.setPostalCode("T_PostalCode");
		
		wsAddress.setCountry(Country.US);
		Assert.assertNotNull(AddressDataConverter.wsGeoCodedAddressToCSGeoCodedAddress(wsAddress));
		
		wsAddress.setCountry(Country.CA);
		Assert.assertNotNull(AddressDataConverter.wsGeoCodedAddressToCSGeoCodedAddress(wsAddress));

		wsAddress.setCountry(Country.MX);
		Assert.assertNotNull(AddressDataConverter.wsGeoCodedAddressToCSGeoCodedAddress(wsAddress));
		
		wsAddress.setCountry(Country.BR);
		Assert.assertNotNull(AddressDataConverter.wsGeoCodedAddressToCSGeoCodedAddress(wsAddress));
		
		wsAddress.setCountry(Country.IN);
		Assert.assertNotNull(AddressDataConverter.wsGeoCodedAddressToCSGeoCodedAddress(wsAddress));
		
		wsAddress.setCountry(Country.AD);
		Assert.assertNotNull(AddressDataConverter.wsGeoCodedAddressToCSGeoCodedAddress(wsAddress));
	}
	
	@Test
	public void testSetLines() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		AddressDataConverter instance = new AddressDataConverter();		
		Class<?>[] paramArrayOfClass = {Address.class, com.telenav.datatypes.address.v40.Address.class};
		Address csAddress = new Address();
		com.telenav.datatypes.address.v40.Address address = new com.telenav.datatypes.address.v40.Address();
		address.setCountry(Country.BR);
		address.setState("_state");
		Street street = new Street();
		street.setName("_street");
		address.setStreet(street);
		address.setHouseNumber("_houseNumber");
		address.setCity("_city");
		address.setPostalCode("_postalCode");
		Object[] paramArrayOfObject = {csAddress, address};
		
		String expected = address.getStreet().getName() + " " + address.getHouseNumber();

		
		String methodName = String.valueOf("setLines");
		Method method = instance.getClass().getDeclaredMethod(methodName, paramArrayOfClass);
		method.setAccessible(true);
		method.invoke(instance, paramArrayOfObject);
		
		Assert.assertEquals(expected, csAddress.getFirstLine());	
		
		address.setCountry(Country.MX);
		address.setState("_state");
		street.setName("_street");
		address.setStreet(street);
		address.setHouseNumber("_houseNumber");
		address.setCity("_city");
		address.setPostalCode("_postalCode");
		
		expected = address.getStreet().getName() + " " + address.getHouseNumber();

		
		methodName = String.valueOf("setLines");
		method = instance.getClass().getDeclaredMethod(methodName, paramArrayOfClass);
		method.setAccessible(true);
		method.invoke(instance, paramArrayOfObject);
		
		Assert.assertEquals(expected, csAddress.getFirstLine());
	}
}
