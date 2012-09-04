package com.telenav.cserver.backend.ace;

import com.telenav.datatypes.address.v20.Country;

import java.util.HashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.telenav.datatypes.address.v20.Language;

/**
 * @author: rapu
 * @date: 2009-5-19
 * @time: 16:06:38
 * TeleNav Inc.
 */

public final class CountryUtils
{
    public static final Country BRAZIL = newInstance("BR", "BRA", Language.PORTUGUESE);
    public static final Country CANADA = newInstance("CA", "CAN", Language.ENGLISH);
    public static final Country US = newInstance("US", "USA", Language.ENGLISH);
    public static final Country MEXICO = newInstance("MX", "MEX", Language.SPANISH);

    public static final Country CHINA = newInstance("CN", "CHN", Language.CHINESE);
    public static final Country TAIWAN = newInstance("TW", "TWN", Language.TAI);

    public static final Country ANDORRA = newInstance("AD", "AND", Language.CATALAN);
    public static final Country AUSTRIA = newInstance("AT", "AUT", Language.GERMAN);
    public static final Country BELGIUM = newInstance("BE", "BEL", Language.DUTCH);
    public static final Country SWITZERLAND = newInstance("CH", "CHE", Language.GERMAN);
    public static final Country GERMANY = newInstance("DE", "DEU", Language.GERMAN);
    public static final Country DENMARK = newInstance("DK", "DNK", Language.DANISH);
    public static final Country SPAIN = newInstance("ES", "ESP", Language.SPANISH);
    public static final Country FINDLAND = newInstance("FI", "FIN", Language.FINISH);
    public static final Country FRANCE = newInstance("FR", "FRA", Language.FRENCH);
    public static final Country GB = newInstance("GB", "GBR", Language.ENGLISH);
    public static final Country GIBRALTAR = newInstance("GI", "GIB", Language.ENGLISH);
    public static final Country GREECE = newInstance("GR", "GRC", Language.GERMAN);
    public static final Country IRELAND = newInstance("IE", "IRL", Language.ENGLISH);
    public static final Country ISLE_OF_MAN = newInstance("IM", "IMN", Language.ENGLISH);
    public static final Country ITALY = newInstance("IT", "ITA", Language.ITALIAN);
    public static final Country LIECHTENSTEIN = newInstance("LI", "LIE", Language.GERMAN);
    public static final Country LUXEMBOURG = newInstance("LU", "LUX", Language.FRENCH);
    public static final Country MONACO = newInstance("MC", "MCO", Language.FRENCH);
    public static final Country NETHERLANDS = newInstance("NL", "NLD", Language.DUTCH);
    public static final Country NORWAY = newInstance("NO", "NOR", Language.NORWEGEAN);
    public static final Country PORTUGAL = newInstance("PT", "PRT", Language.PORTUGUESE);
    public static final Country SWEDEN = newInstance("SE", "SWE", Language.SWEDISH);
    public static final Country SAN_MARINO = newInstance("SM", "SMR", Language.ITALIAN);
    public static final Country VATICAN = newInstance("VA", "VAT", Language.ITALIAN);
    public static final Country JAPAN = newInstance("JP", "JPN", Language.JAPANESE);
    public static final Country SOUTH_KOREA = newInstance("KR", "KOR", Language.KOREAN);

    private static HashMap<String, Country> countryHM = null;
    private static HashMap<String,Country> StringCountryHM =null;

    static
    {
        countryHM = new HashMap<String, Country>();
        Field[] fields = CountryUtils.class.getFields();
        try
        {
            if (null != fields)
            {
                for (int i = 0; i < fields.length; ++i)
                {
                    int modifiers = fields[i].getModifiers();

                    // check if it's a defined country field
                    if (Modifier.isStatic(modifiers) &&
                            Modifier.isPublic(modifiers) &&
                            Modifier.isFinal(modifiers) &&
                            fields[i].getType().equals(Country.class))
                    {
                        Country country = (Country) fields[i].get(null);
                        countryHM.put(country.getIso2Name(), country);
                        countryHM.put(country.getIso3Name(), country);
                    } // if
                } // for
            } // if
        } // try
        catch (Exception ex)
        {
        }

        StringCountryHM =new HashMap<String,Country>(500);
        // add String-Country-Map
        StringCountryHM.put("US",US);
        StringCountryHM.put("USA",US);
        StringCountryHM.put("AMERICA",US);
        StringCountryHM.put("U.S.",US);
        StringCountryHM.put("U.S.A.",US);
        StringCountryHM.put("UNITED STATES OF AMERICA",US);

        StringCountryHM.put("CA",CANADA);
        StringCountryHM.put("CAN",CANADA);
        StringCountryHM.put("CANADA",CANADA);

        StringCountryHM.put("BR",BRAZIL);
        StringCountryHM.put("BRA",BRAZIL);
        StringCountryHM.put("BRAZIL",BRAZIL);

        StringCountryHM.put("CN",CHINA);
        StringCountryHM.put("CHN",CHINA);
        StringCountryHM.put("CHINA",CHINA);

        StringCountryHM.put("FR",FRANCE);
        StringCountryHM.put("FRA",FRANCE);
        StringCountryHM.put("FRANCE",FRANCE);

        StringCountryHM.put("DE",GERMANY);
        StringCountryHM.put("DEU",GERMANY);
        StringCountryHM.put("GERMANY",GERMANY);
        StringCountryHM.put("DEUTSCHLAND",GERMANY);

        StringCountryHM.put("IE",IRELAND);
        StringCountryHM.put("IRL",IRELAND);
        StringCountryHM.put("IRELAND",IRELAND);

        StringCountryHM.put("IT",ITALY);
        StringCountryHM.put("ITA",ITALY);
        StringCountryHM.put("ITALY",ITALY);
        StringCountryHM.put("ITALIA",ITALY);

        StringCountryHM.put("MX",MEXICO);
        StringCountryHM.put("MEX",MEXICO);
        StringCountryHM.put("MEXICO",MEXICO);

        StringCountryHM.put("ES",SPAIN);
        StringCountryHM.put("ESP",SPAIN);
        StringCountryHM.put("SPAIN",SPAIN);
        StringCountryHM.put("ESPANA",SPAIN);

        StringCountryHM.put("TW",TAIWAN);
        StringCountryHM.put("TWN",TAIWAN);
        StringCountryHM.put("TAIWAN",TAIWAN);

        StringCountryHM.put("UK",GB);
        StringCountryHM.put("ENGLAND",GB);
        StringCountryHM.put("GB",GB);
        StringCountryHM.put("UNITED KINGDOM",GB);
        StringCountryHM.put("GREAT BRITAIN",GB);
        StringCountryHM.put("BRITAIN",GB);

        StringCountryHM.put("CH",SWITZERLAND);
        StringCountryHM.put("CHE",SWITZERLAND);
        StringCountryHM.put("SWITZERLAND",SWITZERLAND);

        StringCountryHM.put("AT",AUSTRIA);
        StringCountryHM.put("AUT",AUSTRIA);
        StringCountryHM.put("AUSTRIA",AUSTRIA);

        StringCountryHM.put("PT",PORTUGAL);
        StringCountryHM.put("PRT",PORTUGAL);
        StringCountryHM.put("PORTUGAL",PORTUGAL);

        StringCountryHM.put("BE",BELGIUM);
        StringCountryHM.put("BEL",BELGIUM);
        StringCountryHM.put("BELGIUM",BELGIUM);
        StringCountryHM.put("BELGIQUE",BELGIUM);

        StringCountryHM.put("NL",NETHERLANDS);
        StringCountryHM.put("NKD",NETHERLANDS);
        StringCountryHM.put("NETHERLANDS",NETHERLANDS);

        StringCountryHM.put("LU",LUXEMBOURG);
        StringCountryHM.put("LUX",LUXEMBOURG);
        StringCountryHM.put("LUXEMBOURG",LUXEMBOURG);

        StringCountryHM.put("DK",DENMARK);
        StringCountryHM.put("DNK",DENMARK);
        StringCountryHM.put("DENMARK",DENMARK);

        StringCountryHM.put("NO",NORWAY);
        StringCountryHM.put("NOR",NORWAY);
        StringCountryHM.put("NORWAY",NORWAY);
        StringCountryHM.put("NORGE",NORWAY);

        StringCountryHM.put("SE",SWEDEN);
        StringCountryHM.put("SWE",SWEDEN);
        StringCountryHM.put("SWEDEN",SWEDEN);
        StringCountryHM.put("SVERIGE",SWEDEN);

        StringCountryHM.put("FI",FINDLAND);
        StringCountryHM.put("FIN",FINDLAND);
        StringCountryHM.put("FINDLAND",FINDLAND);
        StringCountryHM.put("SWOMI",FINDLAND);

        StringCountryHM.put("GR",GREECE);
        StringCountryHM.put("GRC",GREECE);
        StringCountryHM.put("GREECE",GREECE);

        StringCountryHM.put("MC",MONACO);
        StringCountryHM.put("MCO",MONACO);
        StringCountryHM.put("MONACO",MONACO);

        StringCountryHM.put("SM",SAN_MARINO);
        StringCountryHM.put("SMR",SAN_MARINO);
        StringCountryHM.put("SAN MARINO",SAN_MARINO);

        StringCountryHM.put("AD",ANDORRA);
        StringCountryHM.put("AND",ANDORRA);
        StringCountryHM.put("ANDORRA",ANDORRA);

        StringCountryHM.put("LI",LIECHTENSTEIN);
        StringCountryHM.put("LIE",LIECHTENSTEIN);
        StringCountryHM.put("LIECHTENSTEIN",LIECHTENSTEIN);

        StringCountryHM.put("VA",VATICAN);
        StringCountryHM.put("VAT",VATICAN);
        StringCountryHM.put("VATICAN",VATICAN);
        StringCountryHM.put("VATICAN CITY",VATICAN);
        StringCountryHM.put("THE HOLY SEE",VATICAN);

        StringCountryHM.put("GI",GIBRALTAR);
        StringCountryHM.put("GIB",GIBRALTAR);
        StringCountryHM.put("GIBRALTAR",GIBRALTAR);

        StringCountryHM.put("IM",ISLE_OF_MAN);
        StringCountryHM.put("IMN",ISLE_OF_MAN);
        StringCountryHM.put("ISLE OF MAN",ISLE_OF_MAN);

        StringCountryHM.put("JP",JAPAN);
        StringCountryHM.put("JPN",JAPAN);
        StringCountryHM.put("JAPAN",JAPAN);

        StringCountryHM.put("KR",SOUTH_KOREA);
        StringCountryHM.put("KOR",SOUTH_KOREA);
        StringCountryHM.put("SOUTH KOREA",SOUTH_KOREA);
        StringCountryHM.put("REPUBLIC OF KOREA",SOUTH_KOREA);
    } // static

    public static Country newInstance(String iso2, String iso3, Language lang)
    {
        Country country = new Country();
        country.setIso2Name(iso2);
        country.setIso3Name(iso3);
        country.setPrimaryLanguage(lang);
        return country;
    }

    public static Country parse(String c)
    {
        if (c == null || c.length() == 0)
            return null;

        String n = c.trim().toUpperCase();
        return StringCountryHM.get(n);
    }

    /**
     * returns the Country corresponding with the specified ISO name.
     *
     * @param isoName [in] ISO 3166-1-alpha-2 country code or
     *                ISO 3166-1-alpha-3
     * @return instance corresponding to the country code, or null
     *         if no match can be found
     */
    public static Country getByIsoName(String isoName)
    {
    	if (isoName == null) {
    		return null;
    	}
        return countryHM.get(isoName.toUpperCase());
    } 
}
