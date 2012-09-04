/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.util;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import com.telenav.cserver.ac.holder.CountryMappingHolder;
import com.telenav.cserver.ac.holder.CountryMessages;
import com.telenav.cserver.ac.protocol.ValidateAddressResponseFormatterACEWS;
import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.datatypes.locale.v10.Country;
import com.telenav.kernel.i18n.LocaleHelper;

/**
 * CommonUtil.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-21
 */
public class CommonUtil
{
	private static final String COUNTRY_MAPPING = "countryMapping";
	
	private static final String REGION_NA = "NA";
	
	private static final String REGION_EU = "EU";
	
	private static final String DEFAULT_LANGUAGE = "en";
	
	public static Locale getLocale(String locale)
	{
		if (locale != null)
		{
			String[] seq = locale.split("_");
	        String language = seq.length>0?seq[0]:"";
	        String country = seq.length>1?seq[1]:"";
	        String variant = seq.length>2?seq[2]:"";
	        
	        return new Locale(language,country,variant);
		}
		
		return new Locale("en", "US");
			
	}
	
	public static com.telenav.datatypes.locale.v10.Locale getTnLocal(
			String locale) {
//		TODO Chengbiao, one box search only work for US carriers now, so hardcode it for en_US. Or we can't find US address by es_MX
		Locale loc = getLocale("en_US");
		if (loc != null) {
			com.telenav.datatypes.locale.v10.Locale tnLocale = new com.telenav.datatypes.locale.v10.Locale();
			Country country = Country.Factory.fromValue(loc.getCountry());
			tnLocale.setCountry(country);

			tnLocale.setLanguage(com.telenav.datatypes.locale.v10.Language.ENG);
			return tnLocale;
		}
		return null;
	}
	
    
	/**
	 *  Returns Array for country name in region
	 * @param region
	 * @return
	 */
	public static String[] getI18NDisplayCountryArray( String region , String clientLocale )
	{
		
		Locale defaultLocale = Locale.getDefault();
		Locale.setDefault( LocaleHelper.getLocale( clientLocale ) );
		
		List<String> countryList = getRegionMap().get( region );
		if( countryList == null )
			return new String[0];
		String[] countryName = new String[countryList.size()];
		for( int i =0 ; i < countryList.size() ; i++ )
		{
			countryName[i] = new Locale( DEFAULT_LANGUAGE , countryList.get( i ) ).getDisplayCountry();
		}
		Locale.setDefault( defaultLocale );
		return countryName;
	}
	/**
	 *  Returns Array for country 3-letters in region
	 * @param region
	 * @return
	 */
	public static String[] getI18NISO3CountryArray( String region )
	{
		List<String> countryList = getRegionMap().get( region );
		if( countryList == null )
			return new String[0];
		String[] countryAlias = new String[countryList.size()];
		for( int i =0 ; i < countryList.size() ; i++ )
		{
			countryAlias[i] =new Locale(DEFAULT_LANGUAGE ,  countryList.get( i ) ).getISO3Country();
		}
		return countryAlias;
	}
	
	private static Map<String, List<String>> getRegionMap()
	{
		 UserProfile userProfile = new UserProfile();
		 CountryMappingHolder countryMappingHolder = ResourceHolderManager.getResourceHolder(COUNTRY_MAPPING);
	     return countryMappingHolder.getCountryMapping( userProfile , null).getRegionMap();
	}
	/**
	 * Returns default country 3-letters code for the region
	 * @param region 2-letters region code
	 */
	public static String getDefaultCountry(String region){
		if( !getRegionMap().containsKey( region ) )
			region = REGION_NA;
		List<String> countryList = getRegionMap().get( region );
		Locale locale = new Locale( DEFAULT_LANGUAGE , countryList.get( 0 ) );
		return locale.getISO3Country();
	}
	
	
	public static List<String> getI18NCountryList( String region )
	{
		List<String> countryList = getRegionMap().get( region );
		return countryList;
	}
	
	public static String getCountryCode( String regoin , String country  , String clientLocale )
	{
		String countryCode = "";
		Locale defaultLocale = Locale.getDefault();
		Locale.setDefault(	LocaleHelper.getLocale( clientLocale ) );
		List<String> countryList = getRegionMap().get( regoin );
		Iterator<String> it = countryList.iterator();
		String _country ;
		Locale _locale;
		while( it.hasNext() )
		{
			_country = it.next();
			_locale = new Locale(	LocaleHelper.getLocale( clientLocale ).getLanguage()  ,_country);
			if( _locale.getCountry().equalsIgnoreCase( country ) || _locale.getDisplayCountry().equalsIgnoreCase( country ) || _locale.getISO3Country().equalsIgnoreCase( country ) ) 
			{
				countryCode = _locale.getCountry();
			    break;
			}
		}
		Locale.setDefault( defaultLocale );
		return countryCode;
	}
	
    public static String convertNull(String s) {
        if (null == s) {
            s = "";
        }
        return s;
    }

    public static int convertToDM5(double degree) {
        return (int) (degree * ValidateAddressResponseFormatterACEWS.DEGREE_MULTIPLIER);
    }
}
