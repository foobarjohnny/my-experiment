/**
 * (c) Copyright 2011 TeleNav.
 * 
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.ace;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.apontador.apirequest.proxy.AddressStatus;
import com.apontador.apirequest.proxy.AddressStatusTypeDef;
import com.telenav.cserver.backend.datatypes.ace.GeoCodeSubStatus;
import com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress;
import com.telenav.cserver.backend.proxy.HttpClientResponse;
import com.telenav.j2me.datatypes.Stop;

/**
 * ValidateAddressResponse
 * 
 * @author kwwang
 * @date 2011-6-28
 */
public class ValidateAddressResponse extends HttpClientResponse {
	private List<GeoCodedAddress> addresses = new ArrayList<GeoCodedAddress>();

	public List<GeoCodedAddress> getAddresses() {
		return addresses;
	}

	public void setAddresses(AddressStatus aceStatus) {

		if (aceStatus != null
				&& (aceStatus.getStatus() == AddressStatusTypeDef.EXACT_FOUND || aceStatus
						.getStatus() == AddressStatusTypeDef.MULTIPLE_MATCH)) {
			Stop[] matches = aceStatus.getAddresses();

			for (int i = 0; i < matches.length; i++) {
				if (matches[i] != null && matches[i].firstLine != null
						&& matches[i].firstLine.length() > 0) {
					String[] str = getStreetAndNumber(matches[i].firstLine,
							Character.toString(' '));
					matches[i].firstLine = str[0] + " " + str[1];
					this.addresses
							.add(convert2GeoCodedAddress(matches[i], str));
				}
			}
		}
	}

	protected GeoCodedAddress convert2GeoCodedAddress(Stop stop,
			String[] streetAndNumber) {
		GeoCodedAddress geoAddr = new GeoCodedAddress();
		geoAddr.setCityName(stop.city);
		geoAddr.setCountry(stop.country);
		geoAddr.setCounty(stop.county);
		geoAddr.setFirstLine(stop.firstLine);
		geoAddr.setLatitude(stop.lat / 100000.0);
		geoAddr.setLongitude(stop.lon / 100000.0);
		geoAddr.setLabel(stop.label);
		geoAddr.setPostalCode(stop.zip);
		geoAddr.setStreetName(streetAndNumber[0]);
		geoAddr.setDoor(streetAndNumber[1]);
		geoAddr.setState(stop.state);
		//dummy GeoCodeSubStatus
		geoAddr.setSubStatus(new GeoCodeSubStatus());
		return geoAddr;
	}

	public String[] getStreetAndNumber(String firstLine, String seprator) {
		String[] name = new String[2];
		if (firstLine.trim().indexOf(seprator) > -1) {
			String street = "";
			String number = "";
			StringTokenizer st = new StringTokenizer(firstLine, seprator);
			while (st.hasMoreElements()) {
				String temp = st.nextToken().trim();
				char[] ch = temp.toCharArray();
				boolean isNum = true;
				for (int i = 0; i < ch.length; i++) {
					if (!Character.isDigit(ch[i])) {
						isNum = false;
						break;
					}
				}
				if (!isNum) {
					street += (temp + " ");
				} else {
					number = temp;
				}
			}
			name[0] = street.trim();
			name[1] = number.trim();
		} else {
			name[0] = firstLine.trim();
			name[1] = "";
		}
		return name;
	}

}
