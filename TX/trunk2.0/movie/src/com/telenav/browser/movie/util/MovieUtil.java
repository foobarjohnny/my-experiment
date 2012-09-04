/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Dec 2, 2008
 * File name: MovieUtil.java
 * Package name: com.telenav.j2me.browser.util
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 10:40:12 AM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.telenav.browser.movie.Constants;
import com.telenav.browser.movie.datatype.Movie;
import com.telenav.browser.movie.datatype.MovieImage;
import com.telenav.browser.movie.datatype.Schedule;
import com.telenav.browser.movie.datatype.Theater;
import com.telenav.browser.movie.service.MoviesManager;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * @author dysong (dysong@telenav.cn) 10:40:12 AM, Dec 2, 2008
 */
public class MovieUtil {

    private static Logger log = Logger.getLogger(MovieUtil.class);
    private static final PropertyResourceBundle serverBundle = (PropertyResourceBundle) PropertyResourceBundle
	.getBundle("config.movie");

    // TODO MOVE BROWSERUTIL
    /**
     * Get image data from file
     * 
     * @param filePath
     * @return
     */
    public static byte[] fileToImageData(String filePath) {
        FileInputStream in = null;
        byte[] imageData = null;
        try {
            in = new FileInputStream(filePath);
            if (in.available() == 0) {
                return null;
            }
            imageData = new byte[in.available()];
            in.read(imageData);
            in.close();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (in != null)
                    in.close();

            } catch (IOException e) {
                log.error("IO error ", e);
            }
        }
        return imageData;
    }

    // TODO MOVE BROWSERUTIL
    /**
     * Create image file with image data
     * 
     * @param filePath
     * @param imageData
     */
    public static void imageDataToFile(String filePath, byte[] imageData) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            out.write(imageData);

            out.close();
        } catch (FileNotFoundException e) {

            log.error("Error: Failed to put image into file", e);
        } catch (IOException e) {
            log.error("IO error ", e);
        } finally {
            try {
                if (out != null)
                    out.close();

            } catch (IOException e) {
                log.error("IO error ", e);
            }
        }

    }

    public static MovieImage resizeImage(MovieImage movieImage, int maxHeight,
            int maxWidth) {

        if (movieImage == null) {
            return null;
        }
        // Get original size and content.
        try {
            BufferedImage src = ImageIO.read(new ByteArrayInputStream(
                    movieImage.getData()));
            if(src == null)
            {
                return null;
            }
            int width = src.getWidth();
            int height = src.getHeight();

            // Get new size.
            if (width / height > maxWidth / maxHeight) {
                height = (int) (((float) height) / ((float) width) * maxWidth);
                width = maxWidth;
            } else {
                width = (int) (((float) width) / ((float) height) * maxHeight);
                height = maxHeight;
            }

            // Draw.
            Image image = src
                    .getScaledInstance(width, height, Image.SCALE_FAST);
            BufferedImage tag = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();

            // Get byte array.
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(tag, "PNG", os);
            byte[] byteArray = os.toByteArray();
            os.close();

            // Result
            MovieImage result = new MovieImage();
            result.setData(byteArray);
            result.setKey(movieImage.getKey());
            result.setHeight(height);
            result.setWidth(width);

            return result;
        } catch (Throwable e) {
            log.error("Failed to convert image", e);
            return null;
        }
    }

    /**
     * 
     * 115->01H55M
     * 
     * @param runTimeString
     * @return
     */
    public static String formatRunTime(String runTimeString) {
        int runTime = Integer.parseInt(runTimeString);
        int hour = runTime / 60;
        int min = runTime % 60;
        String hourString;
        String minString;

        if (hour < 10) {
            hourString = "0" + hour;
        } else {
            hourString = "" + hour;
        }
        if (min < 10) {
            minString = "0" + min;
        } else {
            minString = "" + min;
        }
        return hourString + "H" + minString + "M";

    }

    /**
     * Sort movies list by name or rating
     * 
     * @param movies
     * @param sortType 1. SORT_BY_NAME(SORT_BY_DEFAULT) 2.SORT_BY_RATING
     * @return
     */
    public static List sortMovies(List movies, int sortType) {
        if (sortType == MoviesManager.SORT_BY_NAME) {
            Collections.sort(movies, new Comparator() {
                public int compare(Object o0, Object o1) {
                    Movie m0 = (Movie) o0;
                    Movie m1 = (Movie) o1;
                    return m0.getName().compareTo(m1.getName());
                }
            });
        } else if (sortType == MoviesManager.SORT_BY_RATING) {
            Collections.sort(movies, new Comparator() {
                public int compare(Object o0, Object o1) {
                    Movie m0 = (Movie) o0;
                    Movie m1 = (Movie) o1;
                    if (m0.getRating() > m1.getRating()) {
                        return -1;
                    } else if (m0.getRating() == m1.getRating()) {
                        return m0.getName().compareTo(m1.getName());
                    }
                    return 1;
                }
            });
        }
	    else if (sortType == MoviesManager.SORT_BY_NEWOPENING) {
	        Collections.sort(movies, new Comparator() {
	            public int compare(Object o0, Object o1) {
	                Movie m0 = (Movie) o0;
	                Movie m1 = (Movie) o1;
	                
	                int result = m1.getReleaseDate().compareTo(m0.getReleaseDate()); 
	                if(result == 0)
	                {
	                	result = m0.getName().compareTo(m1.getName());
	                }
	                
	                return result;
	            }
	        });
	    }
        return movies;
    }

    /**
     * Sort theater list by theater's distance from certain point
     * 
     * @param theaters
     * @return
     */
    public static List sortTheaters(List theaters) {
        Collections.sort(theaters, new Comparator() {
            public int compare(Object o0, Object o1) {
                Theater t0 = (Theater) o0;
                Theater t1 = (Theater) o1;
                if (t0.getDistance() > t1.getDistance()) {
                    return 1;
                } else if (t0.getDistance() == t1.getDistance()) {
                    return 0;
                }
                return -1;
            }
        });

        return theaters;
    }

    /**
     * Sort raw schedule list by schedule's quals
     * 
     * @param schedules
     * @return
     */
    public static List sortRawSchedules(List schedules) {
        Collections.sort(schedules, new Comparator() {
            public int compare(Object o0, Object o1) {
                Schedule s0 = (Schedule) o0;
                Schedule s1 = (Schedule) o1;
                String q0 = s0.getQuals();
                String q1 = s1.getQuals();
                if (q0 == null) {
                    q0 = "";
                }
                if (q1 == null) {
                    q1 = "";
                }
                return q0.compareTo(q1);

            }
        });

        return schedules;
    }

    /**
     * Sort schedule list by schedule's quals and showTime
     * 
     * @param schedules
     * @return
     */
    public static List sortSchedules(List schedules) {
        Collections.sort(schedules, new Comparator() {
            public int compare(Object o0, Object o1) {
                Schedule s0 = (Schedule) o0;
                Schedule s1 = (Schedule) o1;
                String q0 = s0.getQuals();
                String q1 = s1.getQuals();
                if (q0 == null) {
                    q0 = "";
                }
                if (q1 == null) {
                    q1 = "";
                }
                if (q0.compareTo(q1) == 0) {
                    String showTime0 = s0.getShowTime();
                    String showTime1 = s1.getShowTime();
                    int minutes0 = MovieUtil.getMinutes(showTime0);
                    int minutes1 = MovieUtil.getMinutes(showTime1);
                    if (minutes0 > minutes1) {
                        return 1;
                    } else if (minutes0 == minutes1) {
                        return 0;
                    } else {
                        return -1;
                    }

                } else {
                    return q0.compareTo(q1);
                }

            }
        });

        return schedules;
    }

    // TODO MOVE BROWSERUTIL
    /**
     * Get margin day from show time <br>
     * 24:15 -> 1 48:01-> 2 just consider hour >= 0
     * 
     * @param showTime
     * @return
     */
    public static int getMarginDay(String showTime) {
        if (showTime == null || showTime.length() < 1)
            return 0;
        String[] times = showTime.split(":");
        int hour = Integer.parseInt(times[0]);
        // Just for hour >= 0

        return hour / 24;

    }

    /**
     * Format show time to 12-hour, but just add suffix AM
     * <p>
     * e.g. 12:50->12:50, 9:50-> 9:50 am, 17:50->5:50, 24:50->12:50 am,
     * 0:10->12:10 am (only consider hour >= 0)
     * </p>
     * 
     * @param showTime
     * @return
     */
    public static String formatShowTimeAm(String showTime) {
        if (showTime == null || showTime.length() < 1)
            return "";
        String[] times = showTime.split(":");
        int hour = Integer.parseInt(times[0]);
        String min = times[1];
        // Just for hour >= 0
        hour %= 24;

        if (hour == 12)
            return showTime;
        if (hour > 12) {
            return (hour - 12) + ":" + min;
        } else if (hour == 0) {
            return 12 + ":" + min + " am";
        } else {
            return hour + ":" + min + " am";
        }

    }

    /**
     * Format show time to 12-hour, add suffix AM/PM
     * <p>
     * e.g. 12:50->12:50 pm, 9:50-> 9:50 am, 17:50->5:50 pm, 24:50->12:50 am,
     * 0:10->12:10 am (only consider hour >= 0)
     * </p>
     * 
     * @param showTime
     * @return
     */
    public static String formatShowTimeAll(String showTime) {
        if (showTime == null || showTime.length() < 1)
            return "";
        String[] times = showTime.split(":");
        int hour = Integer.parseInt(times[0]);
        String min = times[1];
        // Just for hour >= 0
        hour %= 24;
        if (hour == 12)
            return showTime + " pm";
        if (hour > 12) {
            return (hour - 12) + ":" + min + " pm";
        } else if (hour == 0) {
            return 12 + ":" + min + " am";
        } else {
            return hour + ":" + min + " am";
        }

    }

    /**
     * /** Format show time to 24-hours with no suffix
     * <p>
     * e.g. 12:50->12:50, 24:50-> 0:50 (only consider hour >= 0)
     * </p>
     * 
     * @param showTime
     * @return
     */
    public static String formatShowTime(String showTime) {
        if (showTime == null || showTime.length() < 1)
            return "";
        String[] times = showTime.split(":");
        int hour = Integer.parseInt(times[0]);
        String min = times[1];
        // Just for hour >= 0
        hour %= 24;

        return hour + ":" + min;
    }

    /**
     * Sort 24 hours time by minutes
     * 
     * @param timeList
     * @return
     */
    public static ArrayList sortTime(ArrayList timeList) {

        Collections.sort(timeList, new Comparator() {
            public int compare(Object o0, Object o1) {
                String showTime1 = (String) o0;
                String showTime2 = (String) o1;
                int minutes1 = MovieUtil.getMinutes(showTime1);
                int minutes2 = MovieUtil.getMinutes(showTime2);
                if (minutes1 > minutes2) {
                    return 1;
                } else if (minutes1 == minutes2) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        return timeList;
    }

    // TODO MOVE BROWSERUTIL
    /**
     * Compute the amount of page
     * 
     * @param totalSize
     * @param pageSize
     * @return
     */
    public static int pageAmount(int totalSize, int pageSize) {

        int amount = 0;
        amount = totalSize / pageSize;
        if (totalSize % pageSize > 0) {
            amount++;
        }
        return amount;

    }

    // TODO MOVE BROWSERUTIL
    /**
     * Judge whether string is numeric
     * 
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (str == null || str.length() < 1) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[0-9]*$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    // TODO MOVE BROWSERUTIL

    /**
     * Get minutes of 24 hours time <br>
     * 11:05-->665
     * 
     * 
     * @param time
     * @return
     */
    public static int getMinutes(String time) {
        String[] times = time.split(":");
        if (times.length != 2) {
            return 0;
        }
        int hour = Integer.parseInt(times[0]);
        int minute = Integer.parseInt(times[1]);
        return hour * 60 + minute;

    }

    // TODO MOVE BROWSERUTIL
    /**
     * Sort file list by name
     * 
     * @param files
     * @return
     */
    public static List sortFiles(List files) {

        Collections.sort(files, new Comparator() {
            public int compare(Object o0, Object o1) {
                String s0 = (String) o0;
                String s1 = (String) o1;
                return (-1) * s0.compareTo(s1);
            }
        });

        return files;
    }

    /**
     * Get ticket wap link according
     * 
     * @param vendorName
     * @param showTime
     * @param ticketURI
     * @param dateString
     * @param theaterVendorId
     * @param marginDay
     * @return
     */
    public static String getWapLink(String vendorName, String showTime,
            String ticketURI, String dateString, String theaterVendorId) {
        String wapLink = null;
        String[] s;
        if ("TMS".equalsIgnoreCase(vendorName)) {
            String altFilmId = MovieUtil.getAltFilmId(ticketURI);
            if (isNumeric(altFilmId)) {
                s = new String[] { altFilmId, theaterVendorId, dateString,
                        showTime };
                String address = "http://mobile.fandango.com/tms.asp?a=11872&m={0}&t={1}&d={2}+{3}";
                wapLink = MessageFormat.format(address, s);
                return wapLink;
            }
        } else if ("MT".equalsIgnoreCase(vendorName)) {
            // MT is no longer supported.
        }

        return wapLink;

    }

    /**
     * @param date
     * @param showTime
     * @return
     */
    public static Date getTrueDate(Date date, String showTime) {
        GregorianCalendar now = new GregorianCalendar();
        now.setTime(date);
        now.add(Calendar.DAY_OF_MONTH, getMarginDay(showTime));
        return now.getTime();
    }

    /**
     * Get movie altFilmId form ticketURI
     * <p>
     * e.g. http://www.fandango.com/tms.asp?t=AAVAC&m=69138&d=2008-11-24 -->
     * 69138
     * </p>
     * 
     * @param ticketURI
     * @return
     */

    private static String getAltFilmId(String ticketURI) {
        if (ticketURI == null || ticketURI.length() < 1) {
            return null;
        }

        String[] strs = ticketURI.toLowerCase().split("&m=");
        if (strs.length != 2) {
            return null;
        }
        strs = strs[1].toLowerCase().split("&d=");
        if (strs.length != 2) {
            return null;
        }
        return strs[0];
    }

    /**
     * Get detail address of theater<br>
     * street, city, state postalcode
     * 
     * @param theater
     * @return
     */
    public static String getDetailAddress(Theater theater) {
        StringBuffer detail = new StringBuffer();
        if (theater != null) {
            if (theater.getStreet() != null && theater.getStreet().length() > 0) {
                detail.append(theater.getStreet());
            }
            if (theater.getCity() != null && theater.getCity().length() > 0) {
                if (detail.length() > 0) {
                    detail.append(", ");
                }
                detail.append(theater.getCity());
            }
            if (theater.getState() != null && theater.getState().length() > 0) {
                if (detail.length() > 0) {
                    detail.append(", ");
                }
                detail.append(theater.getState());
            }
            if (theater.getPostalcode() != null
                    && theater.getPostalcode().length() > 0) {
                if (detail.length() > 0) {
                    detail.append(" ");
                }
                detail.append(theater.getPostalcode());
            }
        }
        return detail.toString();
    }

    // TODO MOVE BROWSERUTIL
    /**
     * Add prefix CDATA
     * 
     * @param s
     * @return
     */
    public static String amend(String s) {

        if (s == null)
            return "";
        if (s.indexOf(">") > 0 || s.indexOf("<") > 0 || s.indexOf("&") > 0
                || s.indexOf(",") > 0 || s.indexOf("\"") > 0) {
            s = "<![CDATA[" + s + "]]>";
        }
        return s;
    }

    // TODO MOVE BROWSERUTIL
    /**
     * Replace & with &amp;
     * 
     * @param s
     * @return
     */
    public static String revise(String s) {

        return s.replaceAll("&", "&amp;");
    }

    /**
     * Transform format of time<br>
     * 1H02M-->1 hr 2 min
     * 
     * @param s
     * @return
     */
    public static String timeFormat(String s) {
        if (s == null || s.length() < 1)
            return "";
        String[] times = s.split("H|M");
        String hour = Integer.parseInt(times[0]) + "";
        String min = Integer.parseInt(times[1]) + "";
        //return hour + " hr " + min + " min";
        return hour + " : " + min ;
    }

    /**
     * Check the ticket is available according the any existence of schedules'
     * ticketURI
     * 
     * @param scheduleMap <<code>Theater</code>,<code>Schedule</code> List>
     * @return
     */
    public static boolean checkTicketAvailabe(Map scheduleMap) {
        if (scheduleMap == null) {
            return false;
        }
        Set keySet = scheduleMap.keySet();
        if (keySet.isEmpty()) {
            return false;
        }
        Iterator it = keySet.iterator();
        while (it.hasNext()) {
            Theater t = ((Theater) it.next());
            List scheduleList = (List) scheduleMap.get(t);
            if (checkTheaterTicketing(scheduleList)) {
                return true;
            }

        }
        return false;

    }

    /**
     * Check the theater is available for ticketing with a certain movie
     * according the any existence of schedules' ticketURI
     * 
     * @param scheduleList <List><code>Schedule</code><List>
     * @return
     */
    public static boolean checkTheaterTicketing(List scheduleList) {
        if (scheduleList != null && scheduleList.size() > 0) {
            Schedule s;
            for (Iterator it = scheduleList.iterator(); it.hasNext();) {
                s = (Schedule) it.next();
                if (s != null && s.getTicketURI() != null
                        && s.getTicketURI().length() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Remove unticketable theaters and related schedules
     * 
     * @param theaterList List<<code>Theater</code>>
     * @param scheduleListMap Map<List<<code>Schedule</code>>>
     * 
     */
    public static void removeUnticket(List theaterList, Map scheduleListMap) {
        Theater t;
        Schedule s;
        List sl;
        for (int i = 0; i < theaterList.size(); i++) {
            t = (Theater) theaterList.get(i);
            sl = (List) scheduleListMap.get(t);
            if (sl == null || sl.size() < 1) {
                theaterList.remove(i);
                scheduleListMap.remove(t);
                i--;
                continue;
            }
            s = (Schedule) sl.get(0);
            if (s == null || s.getTicketURI() == null
                    || s.getTicketURI().length() < 1) {
                theaterList.remove(i);
                scheduleListMap.remove(t);
                i--;
                continue;
            }
        }
    }

    /**
     * Compose show time string
     * 
     * @param schedules
     * @return
     */
    public static String composeShowTimeAm(List schedules) {
        StringBuffer sb = new StringBuffer();

        if (schedules != null) {
            schedules = MovieUtil.sortSchedules(schedules);
            Schedule schedule;
            String previousQuals = "";
            String quals;
            for (int i = 0; i < schedules.size(); i++) {
                schedule = (Schedule) schedules.get(i);
                quals = schedule.getQuals();
                if (quals == null || quals.length() < 1) {
                    quals = "";
                }

                if (!previousQuals.equals(quals)) {
                    sb.append(" (" + quals + ") ");
                } else {
                    if (sb.length() > 0) {
                        sb.append(" | ");
                    }
                }
                sb.append(MovieUtil.formatShowTimeAm(schedule.getShowTime()));
                previousQuals = quals;
            }
        }
        return sb.toString();
    }

    /**
     * @param showTime
     * @param marginHours
     * @return
     */
    public static String addMarginHours(String showTime, int marginHours) {
        if (showTime == null || showTime.length() < 1) {
            return "";
        }
        if (marginHours == 0) {
            return showTime;
        }
        String[] times = showTime.split(":");
        int hour = Integer.parseInt(times[0]);
        String min = times[1];
        hour += marginHours;
        return hour + ":" + min;
    }

    // TODO MOVE BROWSERUTIL
    /**
     * Get a properties map from properties file
     * 
     * @param filePath File path of properties file
     * @return
     */
    public static HashMap getPropertiesMap(String filePath) {

        HashMap map = new HashMap();

        Properties prop = new Properties();
        FileInputStream stream = null;
        try {

            File propertiesFile = new File(filePath);
            // Get layout properties
            if (propertiesFile.isFile()) {
                stream = new FileInputStream(propertiesFile);
                prop.load(stream);
                map.putAll(prop);
                return map;
            } else {

                log.info("Can not find the matching layout properties file: "
                        + filePath + ".");
                return null;
            }

        } catch (FileNotFoundException e) {

            log.error("Layout properties file " + " [" + filePath
                    + "] not found. " + e.getLocalizedMessage());
        } catch (IOException e) {

            log.error("Layout properties file " + " [" + filePath
                    + "] read error. " + e.getLocalizedMessage());
        } finally {

            if (null != stream) {
                try {
                    stream.close();
                } catch (IOException e) {
                    log.error("IO error in getting layout properties", e);
                }
            }
        }
        // Return null if no file was found.
        return null;
    }
    
    /**
     * 
     * @param handler
     * @return
     */
    public static int getMoviePageSize(DataHandler handler)
    {
    	if(MovieThunderUtil.isThunder(handler))
    	{
    		return 30;
    	}
    	else
    	{
    		return Constants.PAGE_SIZE_MOVIE;
    	}
    }
    
    /**
     * 
     * @param s
     * @return
     */
    public static String filterLastPara(String s)
    {
    	String[] list = s.split(";");
    	if(list != null)
    	{
    		return list[0];
    	}
    	else
    	{
    		return "";
    	}
    }
    
    /**
     * 
     * @param handler
     * @return
     */
    public static boolean needNearestTheater(DataHandler handler)
    {
    	boolean need = false;
    	if(MovieThunderUtil.isThunder(handler))
    	{
    		need = true;
    	}

    	return need;
    }
    
    public static TxNode TheaterToTxNode(Theater theater){
    	Stop stop = new Stop();
    	stop.city=theater.getCity();
    	stop.country=theater.getCountry();
    	stop.firstLine=theater.getName();
    	stop.state=theater.getState();
    	stop.zip=theater.getPostalcode();
    	stop.lat=(int)(theater.getLat()*100000);
    	stop.lon=(int)(theater.getLon()*100000);
    	stop.label=theater.getName();
    
    	return stop.toTxNode();
    }
    
    public static TxNode TheaterToTxNodeNew(Theater theater){
    	Stop stop = new Stop();
    	stop.city=theater.getCity();
    	stop.country=theater.getCountry();
    	stop.firstLine=theater.getStreet();
    	stop.state=theater.getState();
    	stop.zip=theater.getPostalcode();
    	stop.lat=(int)(theater.getLat()*100000);
    	stop.lon=(int)(theater.getLon()*100000);
    	stop.label=theater.getName();
    
    	return stop.toTxNode();
    }
    
	public static boolean isDisableLinkDevice(DataHandler handler) {
		String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
		String device = handler.getClientInfo(DataHandler.KEY_DEVICEMODEL);
		String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
		String version = handler.getClientInfo(DataHandler.KEY_VERSION);
		if (version != null) {
			version = version.replace(".", "_");
		}
		String key = platform + "_" + version + "_" + carrier + "_" + device;
		key = key.toUpperCase();
		String value = "false";
		try {
			value = serverBundle.getString(key);
		} catch (Exception e) {
			value = "false";
		}
		boolean disable = false;
		if (value != null && "true".equalsIgnoreCase(value)) {
			disable = true;
		}
		return disable;
	}
}
