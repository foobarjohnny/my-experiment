package com.telenav.cserver.util;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import junit.framework.TestCase;


public class TestCommonUtil extends TestCase{

	public void testGetCountryNameArray()
	{
		String[] country = CommonUtil.getI18NDisplayCountryArray( "NA" , "en_US" );
		assertTrue( country[0].equals( "United States" ) );
		assertTrue( country[1].equals( "Canada" ) );
		assertTrue( country[2].equals( "Mexico" ) );
		assertTrue( country.length == 3 );
		country = CommonUtil.getI18NDisplayCountryArray( "NN" , "en_IN" );
		assertTrue( country.length == 0  );
	}
	
	public void testGetCountryAliasArray()
	{
		String[] country = CommonUtil.getI18NISO3CountryArray( "NA" );
		assertTrue( country.length == 3 );
		assertTrue( country[0].equals( "USA" ) );
		assertTrue( country[1].equals( "CAN" ) );
		assertTrue( country[2].equals( "MEX" ) );
		country = CommonUtil.getI18NISO3CountryArray( "NN" );
		assertTrue( country.length == 0  );
	}
	
	public void testGetDefaultCountry()
	{
		String country = CommonUtil.getDefaultCountry( "NA" );
		assertTrue( country.equals( "USA" ) );
		country = CommonUtil.getDefaultCountry( "EU" );
		assertTrue( country.equals( "GBR" ) );
		country = CommonUtil.getDefaultCountry( null );
		assertTrue( country.equals( "USA" ) );
	}
	
	public void testGetI18NCountryList()
	{
		List<String> countryList = CommonUtil.getI18NCountryList( "NA" );
		assertTrue( countryList.get( 0 ).equals( "US" ) );
	}
	
	public void testGetCountryCode()
	{
		String code = CommonUtil.getCountryCode(  "NA" , "USA" , "en_US" );
		assertTrue( code.equals( "US" ));
		code = CommonUtil.getCountryCode(  "IN" , "India" , "en_US" );
		assertTrue( code.equals( "IN" ));
	 }
}
