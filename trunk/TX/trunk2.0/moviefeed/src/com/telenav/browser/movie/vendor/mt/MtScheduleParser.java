/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Nov 25, 2008
 * File name: MtScheduleParser.java
 * Package name: com.telenav.j2me.browser.vendor.mt
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 1:51:17 PM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.vendor.mt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.telenav.browser.movie.datatype.Schedule;
import com.telenav.browser.movie.util.MovieUtil;

/**
 * @author dysong (dysong@telenav.cn) 1:51:17 PM, Nov 25, 2008
 */
public class MtScheduleParser extends DefaultHandler {

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
     * The date format yyyyMMdd
     */
    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    /**
     * Result of parsing:ArrayList<Schedule>
     */
    private ArrayList scheduleList = null;

    private static Logger log = Logger.getLogger(MtScheduleParser.class);

    /**
     * Temporary store units
     */
    private Schedule schedule = null;

    private String movieVendorId;

    private String theaterVendorId;

    /**
     * Constants: xml tag name
     */
    private final static String TIMES = "times";

    private final static String SHOW_TIME = "showtime";

    private final static String SHOW_TIMES = "showtimes";

    private final static String MOVIE_VENDOR_ID = "movie_id";

    private final static String THEATER_VENDOR_ID = "theater_id";

    private final static String SHOW_DATE = "show_date";

    private final static String DATE = "date";

    // #if debug
    /**
     * A test example
     */
    public static void main(String[] args) {

        MtScheduleParser sp = new MtScheduleParser();

        ArrayList list = (ArrayList) sp.getSchedules("C:\\SCREENS.XML");
        System.out.println(list.size());
        // for (int i = 1; i <= list.size(); i++) {
        // System.out.println(i + ":");
        // System.out.println((Schedule) list.get(i - 1));
        // }
    }

    // #endif

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
                log.info("IO error in parsing", e);
            }
        }

    }

    public void startElement(String uri, String localName, String qName,
            Attributes attr) {

        textString = null;
        if (TIMES.equalsIgnoreCase(qName)) {
            scheduleList = new ArrayList();
            tags.push(qName);
        } else if (SHOW_TIME.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (MOVIE_VENDOR_ID.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (THEATER_VENDOR_ID.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (SHOW_DATE.equalsIgnoreCase(qName)) {
            schedule = new Schedule();
            schedule.setMovieVendorId(movieVendorId);
            schedule.setTheaterVendorId(theaterVendorId);
            try {
                schedule.setDate(format.parse(attr.getValue(DATE)));
            } catch (ParseException e) {
                log.error("Parsing date error", e);
            }
            tags.push(qName);
        } else if (SHOW_TIMES.equalsIgnoreCase(qName)) {
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
    }

    public void endElement(String uri, String localName, String qName) {

        textString = textStringBuffer.toString().trim();
        textStringBuffer.setLength(0);
        if (tags.size() < 1)
            return;
        String topTag = (String) tags.peek();
        if (TIMES.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                tags.pop();
            }
        } else if (SHOW_TIME.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                reset();
                tags.pop();
            }
        } else if (MOVIE_VENDOR_ID.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                movieVendorId = textString;
                tags.pop();
            }
        } else if (THEATER_VENDOR_ID.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                theaterVendorId = textString;
                tags.pop();
            }
        } else if (SHOW_DATE.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {

                scheduleList.add(schedule);
                tags.pop();
            }
        } else if (SHOW_TIMES.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                schedule.setShowTime(showTimeFormat(textString));
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

    private String showTimeFormat(String showTimeString) {
        if (showTimeString == null || showTimeString.length() < 1)
            return "??:??";
        String temp = showTimeString.replaceAll("\\(|\\)", "");
        ArrayList times = new ArrayList();
        String[] temps = temp.split("[\\s,]+");
        for (int i = 0; i < temps.length; i++) {
            if (isTime(temps[i])) {
                insertTimeList(times, temps[i]);

                // System.out.println("tmeps[" + i + "]:" + temps[i]);
            }
        }
        // System.out.println("times:" + times);

        if (times.size() < 1) {
            return "??:??";
        }
        HashSet timeSet = new HashSet(times);
        HashSet newTimeSet = new HashSet();
        ArrayList newTimeList = new ArrayList();
        if (!timeSet.isEmpty()) {
            Iterator it = timeSet.iterator();
            while (it.hasNext()) {
                newTimeSet.add(timeFormat((String) it.next()));
            }

            newTimeList = MovieUtil.sortTime(new ArrayList(newTimeSet));
            StringBuffer showTime = new StringBuffer();
            String time;
            for (int i = 0; i < newTimeList.size(); i++) {
                time = ((String) newTimeList.get(i)).trim();

                if (showTime.length() > 0) {
                    showTime.append("_");
                }
                showTime.append(time);
            }
            // System.out.println("showTime:----" + showTime);
            return showTime.toString();
        }
        return "??:??";
    }

    private void insertTimeList(ArrayList times, String time) {

        if (times == null) {
            times = new ArrayList();
        }
        if (isNumberTime(time) || isNumberSignTime(time)) {
            times.add(time);
            return;
        }
        if (isSignTime(time)) {
            if (times.size() == 0) {
                return;
            }
            String tempTime = (String) times.get(times.size() - 1);
            if (isNumberTime(tempTime)) {
                times.add(times.size() - 1, tempTime + time);
            } else {
                return;
            }
        }
    }

    private String timeFormat(String time) {

        String[] value = time.split(":|[AaPp][Mm]");
        int hour = Integer.parseInt(value[0].trim());
        if (!time.toUpperCase().endsWith("AM")) {

            if (hour < 12) {
                hour += 12;
            }

        }
        return hour + ":" + value[1].trim();
    }

    private void reset() {

        movieVendorId = null;

        theaterVendorId = null;

    }

    /**
     * Judge whether string is time
     * 
     * @param str
     * @return
     */
    private boolean isTime(String str) {
        if (str == null || str.length() < 1) {
            return false;
        }
        Pattern pattern = Pattern
                .compile("^[0-2]?[0-9]:[0-5]?[0-9]([aApP][Mm])?|[aApP][Mm]$");
        Matcher isTime = pattern.matcher(str);
        if (!isTime.matches()) {
            return false;
        }
        return true;
    }

    /**
     * Judge whether string is number time
     * 
     * @param str
     * @return
     */
    private boolean isNumberTime(String str) {
        if (str == null || str.length() < 1) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[0-2]?[0-9]:[0-5]?[0-9]$");
        Matcher isTime = pattern.matcher(str);
        if (!isTime.matches()) {
            return false;
        }
        return true;
    }

    /**
     * Judge whether string is time sign
     * 
     * @param str
     * @return
     */
    private boolean isSignTime(String str) {
        if (str == null || str.length() < 1) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[aApP][Mm]$");
        Matcher isTime = pattern.matcher(str);
        if (!isTime.matches()) {
            return false;
        }
        return true;
    }

    /**
     * Judge whether string is number and sign time
     * 
     * @param str
     * @return
     */
    private boolean isNumberSignTime(String str) {
        if (str == null || str.length() < 1) {
            return false;
        }
        Pattern pattern = Pattern
                .compile("^[0-2]?[0-9]:[0-5]?[0-9][aApP][Mm]$");
        Matcher isTime = pattern.matcher(str);
        if (!isTime.matches()) {
            return false;
        }
        return true;
    }
}
