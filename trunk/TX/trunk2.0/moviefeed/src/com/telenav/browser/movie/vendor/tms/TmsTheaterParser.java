/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on 2008-10-28
 * File name: TmsTheaterParser.java
 * Package name: com.telenav.j2me.browser.vendor.TMS
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 10:57:00 AM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.vendor.tms;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.telenav.browser.movie.datatype.Theater;

/**
 * The class TmsTheaterParser takes charge of parsing a xml file which contains
 * data about theaters,and return a ArrayList<Theaters>.
 * 
 * 
 * @author dysong (dysong@telenav.cn) 10:57:00 AM
 */
public class TmsTheaterParser extends DefaultHandler {

    /**
     * The stack is for storing tags(qNames)
     */
    private Stack tags = new Stack();

    /**
     * To store text of a xml item
     */
    private StringBuffer textStringBuffer = new StringBuffer();

    private String textString;

    /**
     * Result of parsing:ArrayList<Theater>
     */
    private ArrayList theaterList = null;

    private static Logger log = Logger.getLogger(TmsTheaterParser.class);

    /**
     * Temporary store units
     */
    private Theater theater = null;

    /**
     * Constants: xml tag name
     */
    private final static String THEATRES = "theatres";

    private final static String THEATRE = "theatre";

    private final static String AACODE = "aaCode";

    private final static String NAME = "name";

    private final static String STREET = "street1";

    private final static String CITY = "city";

    private final static String STATE = "state";

    private final static String POSTAL_CODE = "postalcode";

    private final static String COUNTRY = "country";

    private final static String TELEPHONE = "telephone";

    private final static String LONGITUDE = "longitude";

    private final static String LATITUDE = "latitude";

    private final static String ACTIVE = "active";

    /**
     * Get parsing result:ArrayList<Theater>
     * 
     * @return the <code>Theater</code> List
     */

    public List getTheaters(String xmlFileName) {
        parse(xmlFileName);
        return theaterList;
    }

    /**
     * Parse a xml file according xmlName to get theaters information
     * 
     * @param xmlFileName name of xml file to parse
     */

    public void parse(String xmlFileName) {
        InputStream is = null;
        try {
            SAXParserFactory saxFac = SAXParserFactory.newInstance();

            SAXParser parser = saxFac.newSAXParser();

            is = new FileInputStream(new File(xmlFileName));

            parser.parse(is, this);

        } catch (Exception e) {
            log.error("Parsing error", e);
        } finally {
            try {
                if (is != null)
                    is.close();

            } catch (IOException e) {
                log.error("IO error in parsing", e);
            }
        }

    }

    public void startElement(String uri, String localName, String qName,
            Attributes attr) {

        textString = null;
        if (THEATRES.equalsIgnoreCase(qName)) {
            theaterList = new ArrayList();
            tags.push(qName);
        } else if (THEATRE.equalsIgnoreCase(qName)) {
            theater = new Theater();
            theater.setVendorId(attr.getValue(AACODE));
            tags.push(qName);

        } else if (NAME.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (STREET.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (CITY.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (STATE.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (POSTAL_CODE.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (COUNTRY.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (TELEPHONE.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (LONGITUDE.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (LATITUDE.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (ACTIVE.equalsIgnoreCase(qName)) {
            tags.push(qName);
        }
        // #if debug
        // Don't delete.It's important for debug
        // System.out
        // .println("*************************************************************");
        // System.out.println("Element Start uri: " + uri + "| localName: "
        // + localName + "| qName: " + qName);
        // if (attr.getLength() > 0)
        // {
        // for (int i = 0; i < attr.getLength(); i++)
        // {
        // System.out.println(attr.getQName(i) + " = " + attr.getValue(i));
        // }
        // }
        // System.out.println("START===:" + tags);
        // #endif
    }

    public void characters(char ch[], int start, int length)
            throws SAXException {
        textStringBuffer.append(ch, start, length);

        textString = textStringBuffer.toString().trim();

    }

    public void endElement(String uri, String localName, String qName) {
        textStringBuffer.setLength(0);
        if (tags.size() < 1)
            return;
        String topTag = (String) tags.peek();
        if (THEATRES.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                tags.pop();
            }
        } else if (THEATRE.equalsIgnoreCase(qName)) {

            if (qName.equalsIgnoreCase(topTag)) {
                theaterList.add(theater);
                tags.pop();
            }
        } else if (NAME.equalsIgnoreCase(qName)) {

            if (qName.equalsIgnoreCase(topTag)) {
                theater.setName(textString);
                tags.pop();
            }
        } else if (STREET.equalsIgnoreCase(qName)) {

            if (qName.equalsIgnoreCase(topTag)) {
                theater.setStreet(textString);
                tags.pop();
            }
        } else if (CITY.equalsIgnoreCase(qName)) {

            if (qName.equalsIgnoreCase(topTag)) {
                theater.setCity(textString);
                tags.pop();
            }
        } else if (STATE.equalsIgnoreCase(qName)) {

            if (qName.equalsIgnoreCase(topTag)) {
                theater.setState(textString);
                tags.pop();
            }
        } else if (POSTAL_CODE.equalsIgnoreCase(qName)) {

            if (qName.equalsIgnoreCase(topTag)) {
                theater.setPostalcode(textString);
                tags.pop();
            }
        } else if (COUNTRY.equalsIgnoreCase(qName)) {

            if (qName.equalsIgnoreCase(topTag)) {
                theater.setCountry(textString);
                tags.pop();
            }
        } else if (TELEPHONE.equalsIgnoreCase(qName)) {

            if (qName.equalsIgnoreCase(topTag)) {
                theater.setTelephone(textString);
                tags.pop();
            }
        } else if (LONGITUDE.equalsIgnoreCase(qName)) {

            if (qName.equalsIgnoreCase(topTag)) {
                if (textString != null && textString.length() > 0) {
                    theater.setLon(Double.parseDouble(textString));
                }
                tags.pop();
            }
        } else if (LATITUDE.equalsIgnoreCase(qName)) {

            if (qName.equalsIgnoreCase(topTag)) {
                if (textString != null && textString.length() > 0) {
                    theater.setLat(Double.parseDouble(textString));
                }
                tags.pop();
            }
        } else if (ACTIVE.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {

            	if (textString != null && textString.length() > 0) {
                	if ("true".equalsIgnoreCase(textString)) {
                		theater.setActive(1);
                	} else {
                		theater.setActive(0) ;
                	}
                    
                }
                tags.pop();
            }
        }
        textString = null;
        // #if debug
        // Don't delete.It's important for debug
        // System.out.println("Element End uri: " + uri + "localName: " +
        // localName
        // + "qName: " + qName);
        // System.out.println("END====:" + tags);
        // System.out
        // .println("=============================================================");
        // #endif
    }

}
