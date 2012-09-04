/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on 2008-10-28
 * File name: TmsScheduleParser.java
 * Package name: com.telenav.j2me.browser.vendor.TMS
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 10:53:06 AM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.vendor.tms;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.telenav.browser.movie.datatype.Schedule;
import com.telenav.browser.movie.util.MovieUtil;

/**
 * The class TmsScheduleParser takes charge of parsing a xml file which contains
 * data about schedules,and return a ArrayList<Schedule>.
 * 
 * @author dysong (dysong@telenav.cn) 10:53:06 AM
 */
public class TmsScheduleParser extends DefaultHandler {
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
     * The date format yyyy-MM-dd
     */
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Flag schedule information is correct wanted type
     */
    private boolean isScheduleFlag;

    /**
     * Result of parsing:ArrayList<Schedule>
     */
    private ArrayList scheduleList = null;

    private static Logger log = Logger.getLogger(TmsScheduleParser.class);

    /**
     * Temporary store units
     */

    private Schedule schedule;

    private String movieTmsId;

    private String theaterTmsId;

    private String dateString;

    private String ticketURI;

    private HashSet scheduleSet;

    private StringBuffer showTimeBuffer = new StringBuffer();

    private String quals;

    private String tempDateString;

    private int marginHours = 0;

    /**
     * Constants: xml tag name
     */
    private final static String SCHEDULES = "Schedules";

    private final static String SCHEDULE = "schedule";

    private final static String EVENT = "event";

    private final static String TIMES = "times";

    private final static String TIME = "time";

    private final static String TICKET_URI = "ticketURI";

    private final static String THEATER = "theater";

    private final static String AACODE = "aaCode";

    private final static String TMSID = "TMSId";

    private final static String DATE = "date";

    private final static String TYPE = "type";

    private final static String QUALS = "quals";

    private final static String TEMP_DATE = "date";

    /**
     * Get parsing result:ArrayList<Schedule>
     * 
     * @return the <code>Schedule</code> List
     */
    public List getSchedules(String xmlFileName) {
        parse(xmlFileName);
        return scheduleList;
    }

    /**
     * Parse a xml file according xmlName to get schedules information
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
        if (SCHEDULES.equalsIgnoreCase(qName)) {
            // Judge whether type of schedules is wanted
            if (THEATER.equalsIgnoreCase(attr.getValue(TYPE))) {
                scheduleList = new ArrayList();
                scheduleSet = new HashSet();
                isScheduleFlag = true;
                tags.push(qName);
            }

        } else if (isScheduleFlag) {
            // Get tms_id (aaCode) of theater
            if (SCHEDULE.equalsIgnoreCase(qName)) {
                theaterTmsId = attr.getValue(AACODE);
                tags.push(qName);
            } else if (EVENT.equalsIgnoreCase(qName)) {
                schedule = new Schedule();
                schedule.setTheaterVendorId(theaterTmsId);

                movieTmsId = attr.getValue(TMSID);
                dateString = attr.getValue(DATE);

                schedule.setMovieVendorId(movieTmsId);
                try {
                    schedule.setDate(format.parse(dateString));
                } catch (ParseException e) {
                    log.error("Parsing date string error", e);
                }
                tags.push(qName);

            } else if (TIMES.equalsIgnoreCase(qName)) {
                tags.push(qName);
            } else if (TIME.equalsIgnoreCase(qName)) {

                tempDateString = attr.getValue(TEMP_DATE);
                if (tempDateString != null && tempDateString.length() > 0) {
                    try {
                        marginHours = 24 * format.parse(tempDateString)
                                .compareTo(format.parse(dateString));
                    } catch (ParseException e) {
                        log.error("Parsing date string error", e);
                    }
                }
                tags.push(qName);
            } else if (TICKET_URI.equalsIgnoreCase(qName)) {
                tags.push(qName);
            } else if (QUALS.equalsIgnoreCase(qName)) {
                tags.push(qName);
            }

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
    }

    public void endElement(String uri, String localName, String qName) {

        textString = textStringBuffer.toString().trim();
        textStringBuffer.setLength(0);
        if (tags.size() < 1)
            return;
        String topTag = (String) tags.peek();
        if (SCHEDULES.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                isScheduleFlag = false;
                tags.pop();
            }

        } else if (isScheduleFlag) {
            if (SCHEDULE.equalsIgnoreCase(qName)) {
                if (qName.equalsIgnoreCase(topTag)) {
                    theaterTmsId = null;
                    tags.pop();
                }
            } else if (EVENT.equalsIgnoreCase(qName)) {
                if (qName.equalsIgnoreCase(topTag)) {
                    if (showTimeBuffer.length() > 0) {
                        schedule.setShowTime(showTimeBuffer.toString());
                        if (!scheduleSet.contains(theaterTmsId + "_"
                                + movieTmsId + "_" + dateString + "_" + quals)) {
                            scheduleList.add(schedule);
                            scheduleSet.add(theaterTmsId + "_" + movieTmsId
                                    + "_" + dateString + "_" + quals);

                        }
                    }
                    reset();
                    tags.pop();
                }
            } else if (TIMES.equalsIgnoreCase(qName)) {
                if (qName.equalsIgnoreCase(topTag)) {
                    tags.pop();
                }

            } else if (TIME.equalsIgnoreCase(qName)) {
                // compose a showtime string
                if (qName.equalsIgnoreCase(topTag)) {
                    if (textString != null && textString.length() > 0) {
                        if (showTimeBuffer.length() > 0) {
                            showTimeBuffer.append("_");
                        }
                        showTimeBuffer.append(MovieUtil.addMarginHours(
                                textString, marginHours));
                    }
                    tags.pop();
                }
                marginHours = 0;
            } else if (TICKET_URI.equalsIgnoreCase(qName)) {
                if (qName.equalsIgnoreCase(topTag)) {
                    if (textString != null && textString.length() > 0) {
                        ticketURI = textString;
                        schedule.setTicketURI(ticketURI);
                    }
                    tags.pop();
                }
            } else if (QUALS.equalsIgnoreCase(qName)) {
                if (qName.equalsIgnoreCase(topTag)) {
                    if (textString != null && textString.length() > 0) {
                        quals = textString;
                        schedule.setQuals(quals);
                    }
                    tags.pop();
                }
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

    private void reset() {

        textString = null;

        dateString = null;

        ticketURI = null;

        quals = null;

        schedule = null;

        movieTmsId = null;

        marginHours = 0;

        showTimeBuffer.setLength(0);

    }

}
