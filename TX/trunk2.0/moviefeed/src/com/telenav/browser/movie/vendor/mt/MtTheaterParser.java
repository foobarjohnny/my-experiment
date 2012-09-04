/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Nov 25, 2008
 * File name: MtTheaterParser.java
 * Package name: com.telenav.j2me.browser.vendor.mt
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 1:28:30 PM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.vendor.mt;

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
 * @author dysong (dysong@telenav.cn) 1:28:30 PM, Nov 25, 2008
 */
public class MtTheaterParser extends DefaultHandler {

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

    private static Logger log = Logger.getLogger(MtTheaterParser.class);

    /**
     * Temporary store units
     */
    private Theater theater = null;

    /**
     * Constants: xml tag name
     */
    private final static String THEATERS = "houses";

    private final static String THEATER = "theater";

    private final static String ID = "theater_id";

    private final static String NAME = "theater_name";

    private final static String ADDRESS = "theater_address";

    private final static String CITY = "theater_city";

    private final static String STATE = "theater_state";

    private final static String POSTAL_CODE = "theater_zip";

    private final static String TICKETING = "theater_ticketing";

    private final static String TELEPHONE = "theater_phone";

    private final static String LONGITUDE = "theater_lon";

    private final static String LATITUDE = "theater_lat";

    // #if debug
    /**
     * A test example
     */
    public static void main(String[] args) {
        MtTheaterParser p = new MtTheaterParser();

        ArrayList list = (ArrayList) p.getTheaters("C:\\THEATER.XML");
        // System.out.println(list);
        for (int i = 1; i <= list.size(); i++) {
            if (((Theater) list.get(i - 1)).getActive() == 1) {
                System.out.println(i + ":");
                System.out.println((Theater) list.get(i - 1));
            }
        }
    }

    // #endif

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
            log.error("Parse error", e);
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
        if (THEATERS.equalsIgnoreCase(qName)) {
            theaterList = new ArrayList();
            tags.push(qName);
        } else if (THEATER.equalsIgnoreCase(qName)) {
            theater = new Theater();
            tags.push(qName);

        } else if (NAME.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (ID.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (ADDRESS.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (CITY.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (STATE.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (POSTAL_CODE.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (TICKETING.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (TELEPHONE.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (LATITUDE.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (LONGITUDE.equalsIgnoreCase(qName)) {
            tags.push(qName);
        }
        // #if debug
        // Don't delete.It's important for debug
        // System.out
        // .println("*************************************************************");
        // System.out.println("Element Start uri: " + uri + "| localName: "
        // + localName + "| qName: " + qName);
        // if (attr.getLength() > 0) {
        // for (int i = 0; i < attr.getLength(); i++) {
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
        if (THEATERS.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                tags.pop();
            }
        } else if (THEATER.equalsIgnoreCase(qName)) {

            if (qName.equalsIgnoreCase(topTag)) {
                theaterList.add(theater);
                tags.pop();
            }
        } else if (ID.equalsIgnoreCase(qName)) {

            if (qName.equalsIgnoreCase(topTag)) {
                theater.setVendorId(textString);
                tags.pop();
            }
        } else if (NAME.equalsIgnoreCase(qName)) {

            if (qName.equalsIgnoreCase(topTag)) {
                theater.setName(textString);
                tags.pop();
            }
        } else if (ADDRESS.equalsIgnoreCase(qName)) {

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
        } else if (TICKETING.equalsIgnoreCase(qName)) {

            if (qName.equalsIgnoreCase(topTag)) {
                if ("N".equalsIgnoreCase(textString)) {
                    theater.setActive(0);
                } else {
                    theater.setActive(1);
                }

                tags.pop();
            }
        } else if (TELEPHONE.equalsIgnoreCase(qName)) {

            if (qName.equalsIgnoreCase(topTag)) {
                theater.setTelephone(textString);
                tags.pop();
            }
        } else if (LATITUDE.equalsIgnoreCase(qName)) {

            if (qName.equalsIgnoreCase(topTag)) {
                if (textString != null && textString.length() > 0) {
                    theater.setLat(Double.parseDouble(textString));
                }
                tags.pop();
            }
        } else if (LONGITUDE.equalsIgnoreCase(qName)) {

            if (qName.equalsIgnoreCase(topTag)) {
                if (textString != null && textString.length() > 0) {
                    theater.setLon(Double.parseDouble(textString));
                }
                tags.pop();
            }
        }
        textString = null;
        // #if debug
        // Don't delete.It's important for debug
        // System.out.println("Element End uri: " + uri + "localName: "
        // + localName + "qName: " + qName);
        // System.out.println("END====:" + tags);
        // System.out
        // .println("=============================================================");
        // #endif
    }

}
