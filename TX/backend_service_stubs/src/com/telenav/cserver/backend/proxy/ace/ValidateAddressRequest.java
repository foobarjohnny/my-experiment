/**
 * (c) Copyright 2011 TeleNav.
 * 
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.ace;

import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
/**
 * ValidateAddressRequest
 * @author kwwang
 * @date 2011-6-29
 */
public class ValidateAddressRequest
{
    public final static int SEARCH_BY_NUMBER = 1;

    public final static int SEARCH_BY_XSTREET = 2;

    public final static int SEARCH_BY_CITYSTATE = 3;

    public static final String AMP = "&";

    public static final String QUESTIONMARK = "?";

    public static final String EQUALS = "=";
    
    public static final String LOGIN = "user";

    public static final String PASSWORD = "pass";

    public static final String FUNCTION = "function";

    public static final String GEOLANG = "geolang";

    public static final String SEARCH_TERM = "term";

    public static final String COUNTRY = "country";

    public static final String STATE = "state";

    public static final String CITY = "city";

    public static final String STREET = "street";

    public static final String STREET_NUMBER = "streetnum";

    public static final String COUNTY = "county";

    public static final String CATEGORY_ID = "type";

    public static final String MAX_LIMIT_SIZE = "maxlistsize";

    public static final String ZIPCODE = "code";

    public static final String DETAIL = "detail";

    public static final String NAME = "name";

    public static final String MODE = "mode";
    
    public static final String ENCODING = "encoding";

    private String city;

    private String state;

    private String zipCode;

    private String country;

    private String county;

    private String street;

    private String corssStreeOrNumber;

    private int searchType;
    
    
    public int getSearchType() {
		return searchType;
	}

	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCorssStreeOrNumber() {
		return corssStreeOrNumber;
	}

	public void setCorssStreeOrNumber(String corssStreeOrNumber) {
		this.corssStreeOrNumber = corssStreeOrNumber;
	}

	public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    public void parseFirstLine(String firstLine)
    {

        final String address = new String(firstLine);
        street = address;
        searchType = SEARCH_BY_CITYSTATE;
        int at = address.indexOf('@');

        if (at > -1)
        {
            street = address.substring(0, at);
            corssStreeOrNumber = address.substring(at + 1); // cross street
            searchType = SEARCH_BY_XSTREET;
        }
        // [ZY]:required by shaun, support "com" as the cross street separator, 20060109:
        else if ((at = address.indexOf(" com ")) > -1)
        {
            street = address.substring(0, at);
            corssStreeOrNumber = address.substring(at + 5); // cross street
            searchType = SEARCH_BY_XSTREET;
        }
        else if (((at = address.indexOf(" at ")) > -1) || ((at = address.indexOf(" AT ")) > -1))
        {
            street = address.substring(0, at);
            corssStreeOrNumber = address.substring(at + 4); // cross street
            searchType = SEARCH_BY_XSTREET;
        }
        else
        {
            searchType = SEARCH_BY_NUMBER;
            int comma = address.indexOf(',');
            if (comma > -1)
            {
                street = BRValidateAddressProxyHelper.getStreetAndNumber(address, Character.toString(','))[0];
                corssStreeOrNumber = BRValidateAddressProxyHelper.getStreetAndNumber(address, Character.toString(','))[1];
            }
            else
            {
                int space = address.lastIndexOf(' ');
                if (space > -1)
                {
                    street = BRValidateAddressProxyHelper.getStreetAndNumber(address, Character.toString(' '))[0];
                    corssStreeOrNumber = BRValidateAddressProxyHelper.getStreetAndNumber(address, Character.toString(' '))[1];
                }
            }

        }

        // upper case the following
        if (corssStreeOrNumber != null)
        {
            corssStreeOrNumber = corssStreeOrNumber.trim().toUpperCase();
        }
        if (state != null)
        {
            state = state.toUpperCase();
        }
        if (city != null)
        {
            city = city.toUpperCase();
        }
        if (street != null)
        {
            street = street.toUpperCase();
        }
    }

    public String toString()
    {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
