/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.poi;

import java.util.Locale;

import junit.framework.TestCase;

import com.telenav.datatypes.locale.v10.Country;
import com.telenav.datatypes.services.v20.UserInformation;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.search.onebox.v10.ClientNameEnum;
import com.telenav.services.search.onebox.v10.OneboxSearchRequest;
import com.telenav.services.search.onebox.v10.OneboxSearchResponse;
import com.telenav.ws.datatypes.address.GeoCode;

/**
 * TestOneBoxSearchProxy
 * 
 * @author kwwang
 * 
 */
public class TestOneBoxSearchProxy extends TestCase
{
    private OneboxSearchRequest obReq;

    private TnContext tc;

    public void setUp()
    {
        tc = new TnContext();

        obReq = new OneboxSearchRequest();

        // set client info
        obReq.setClientName(ClientNameEnum._MOBILE);
        obReq.setClientVersion("1");
        obReq.setTransactionId("8099");

        UserInformation user = new UserInformation();
        user.setUserId("");

        // set user info
        obReq.setUserInfo(user);

        // set context
        obReq.setContextString(tc.toContextString());

        GeoCode gcode = new GeoCode();
        gcode.setLatitude(37.12546);
        gcode.setLongitude(-121.12385);
        // set anchor
        obReq.setAnchor(gcode);

        // set query
        obReq.setQuery("SFO");
        obReq.setStartIndex(0);
        obReq.setResultCount(20);
        // check sponsor number
        obReq.setSponsorStartIndex(0);
        obReq.setSponsorResultCount(1);

        Locale loc = new Locale("en", "US");
        com.telenav.datatypes.locale.v10.Locale tnLocale = new com.telenav.datatypes.locale.v10.Locale();
        Country country = Country.Factory.fromValue(loc.getCountry());
        tnLocale.setCountry(country);

        tnLocale.setLanguage(com.telenav.datatypes.locale.v10.Language.ENG);
        obReq.setLocale(tnLocale);
    }

    public void testOneboxSearch()
    {
        OneboxSearchResponse obResp = OneBoxSearchProxy.getInstance().oneBoxSearch(obReq, tc);
        assertNotNull(obResp);
    }
}
