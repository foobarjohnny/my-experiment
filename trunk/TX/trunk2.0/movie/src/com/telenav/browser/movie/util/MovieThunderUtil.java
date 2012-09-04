/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.browser.movie.util;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import com.telenav.browser.movie.Constants;
import com.telenav.browser.movie.datatype.MessageWrap;
import com.telenav.browser.movie.datatype.Movie;
import com.telenav.browser.movie.datatype.Schedule;
import com.telenav.browser.movie.datatype.Theater;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * @author pzhang
 *
 * @version 1.0, 2009-3-18
 */
public class MovieThunderUtil {
	
	public static int NEW_MOVIE_RANGE = 7;
	public static String APPLICATION_MESSAGE = "com.telenav.browser.movie.resources.ApplicationResources";
	public static final long   DAY_MILLSECONDS   =   24   *   3600   *   1000;
	
	/**
	 * 
	 * @param handler
	 * @return
	 */
	public static boolean isThunder(DataHandler handler)
	{
		if(handler == null) return false;
		
		boolean isThunder = false;
		String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
		String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
		String clientVersion= handler.getClientInfo(DataHandler.KEY_VERSION);
		String device= handler.getClientInfo(DataHandler.KEY_DEVICEMODEL);
		if ("Verizon".equals(carrier) && "RIM".equals(platform)) {
			if (("5.8.01").equals(clientVersion)
					&& (("essex_Mp3").equals(device) || "9650_Mp3"
							.equals(device))) {
				isThunder = false;
			} else if (("5.8.02").equals(clientVersion)
					&& (("essex_Mp3").equals(device) || "9650_Mp3"
							.equals(device))) {
				isThunder = false;
			} else {
				isThunder = true;
			}
		}
		return isThunder;
	}
	
	/**
	 * 
	 * @param distance
	 * @return
	 */
	public static String getDistanceDisplay(long distance)
	{
        return MovieThunderUtil.flagAsBlue(getDistanceDisplayWithoutColor(distance));
	}
	
	/**
	 * 
	 * @param distance
	 * @return
	 */
	public static String getDistanceDisplayWithoutColor(long distance)
	{
		NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(1);
        String distanceDisplay = nf.format(distance/ Constants.MILE_METER);
        distanceDisplay = "(" + distanceDisplay + " mi)";
        
        return distanceDisplay;
	}
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String getString(String string)
	{
		if(string == null) return "";
		
		return string.trim();
	}
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String getGradeDisplay(String grade)
	{
		String gradeDisplay = MovieThunderUtil.getString(grade);
        if (!"".equals(gradeDisplay)) {
        	gradeDisplay = "(" + gradeDisplay + ")";
        }
		return gradeDisplay;
	}
	
	/**
	 * 
	 * @param releaseDate
	 * @return
	 */
	public static String getMovieNewFlag(Date releaseDate)
	{
		if(releaseDate == null) return "";
		
		String flag = "";
		Date currentDate = new Date();
		
		int dateDifferent = daysOfTwo(currentDate,releaseDate);

		if(dateDifferent <= NEW_MOVIE_RANGE)
		{
			flag = MovieThunderUtil.flagAsRed(" New!");
		}
		return flag;
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String flagAsRed(String str)
	{
		return "<red>" + str + "</red>";
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String flagAsBlue(String str)
	{
		return "<blue>" + str + "</blue>";
	}
	
	/**
	 * 
	 * @param schedules
	 * @param timeZone
	 * @return
	 */
	public static String composeShowTimeAm(List schedules,String timeZone) {
        StringBuffer sb = new StringBuffer();
        boolean showRed = false;
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
                    //if (sb.length() > 0) {
                        sb.append(" | ");
                    //}
                }
                
                //compare time
                if(!showRed)
                {
                	if(isFutureTime(schedule.getShowTime(),timeZone))
                	{
                		showRed = true;
                	}
                }
                //
                if(showRed)
                {
                	sb.append(MovieThunderUtil.flagAsRed(MovieUtil.formatShowTimeAm(schedule.getShowTime())));
                }
                else
                {
                    sb.append(MovieUtil.formatShowTimeAm(schedule.getShowTime()));
                }
                previousQuals = quals;
            }
        }
        return sb.toString();
	}
	
	/**
	 * 
	 * @param schedules
	 * @param timeZone
	 * @return
	 */
	public static List filterShowTime(List schedules,String timeZone) {

		List list = new ArrayList();
        boolean showRed = false;
        if (schedules != null) {
            schedules = MovieUtil.sortSchedules(schedules);
            Schedule schedule;
            for (int i = 0; i < schedules.size(); i++) {
                schedule = (Schedule) schedules.get(i);

                //compare time
                if(!showRed)
                {
                	if(isFutureTime(schedule.getShowTime(),timeZone))
                	{
                		showRed = true;
                	}
                }
                //
                if(showRed)
                {
                	list.add(schedule);
                }
            }
        }
        return list;
	}
	
	/**
	 * 
	 * @param showTime
	 * @param timeZone
	 * @return
	 */
	public static boolean isFutureTime(String showTime, String timeZone)
	{
		//testing timeZone
		timeZone = "GMT+8";
		boolean isFuture = false;
		//get theater current time
		Calendar calendarTheater = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
		int hour = calendarTheater.get(Calendar.HOUR_OF_DAY);
		int minute = calendarTheater.get(Calendar.MINUTE);
		int showMin = MovieUtil.getMinutes(showTime);
		int currentMin = hour * 60 + minute;
		//
		if(showMin > currentMin)
		{
			isFuture = true;
		}
		
		return isFuture;
	}
	
	/*
    public static String getApplicationString(String key)
    {
    	String value = "";
    	
    	try
        {
	        PropertyResourceBundle bundle = (PropertyResourceBundle) PropertyResourceBundle
	        .getBundle(APPLICATION_MESSAGE);
	        
	        value = bundle.getString(key);
        }
        catch(Exception e)
        {
        }
        
    	if(value.equals(""))
    	{
    		value = key;
    	}
    	
    	return value;
    }*/
    
    public static String getRatingImage(float rate)
    {
    	return "/line_"+ ((int) (rate * 2)) + ".png";
    }
    
    
    /**
     * 
     * @param date
     * @return
     */
    public static String getShortDate(Date date)
    {
    	String dateShow = Constants.SHORT_DATE_FORMAT.format(date);

    	if(isToday(date))
    	{
    		dateShow = "Today " + dateShow;
    	}
    	//check if it's today
    	return dateShow;
    }
    
    /**
     * This method only compare the days in same Year
     * @param fDate
     * @param oDate
     * @return
     */
    public static int daysOfTwo(Date fDate, Date oDate) {

        Calendar fCalendar = Calendar.getInstance();
        fCalendar.setTime(fDate);
        fCalendar.set(fCalendar.get(Calendar.YEAR),fCalendar.get(Calendar.MONTH),fCalendar.get(Calendar.DATE),0,0,0); 
        fCalendar.set(Calendar.MILLISECOND,0); 

        Calendar oCalendar = Calendar.getInstance();
        oCalendar.setTime(oDate);
        oCalendar.set(oCalendar.get(Calendar.YEAR),oCalendar.get(Calendar.MONTH),oCalendar.get(Calendar.DATE),0,0,0); 
        oCalendar.set(Calendar.MILLISECOND,0); 
        
        int daysDifferent = (int)((fCalendar.getTimeInMillis() - oCalendar.getTimeInMillis())/DAY_MILLSECONDS); 
        return Math.abs(daysDifferent);
     }

    
    private static boolean isToday(Date date)
    {
    	Date today = new Date();
    	int dateDifferent = daysOfTwo(today,date);
    	return dateDifferent < 1;
    }
    
    /**
     * 
     * @param date
     * @return
     */
    public static String getLongDate(Date date)
    {
    	String dateShow = "Today";

    	if(!isToday(date))
    	{
    		dateShow = Constants.DATE_FORMAT.format(date);
    	}
    	//check if it's today
    	return dateShow;
    }
    
    /**
     * 
     * @param origin
     * @return
     */
    /*
	public static String getStopText(Stop origin)
	{
		String text = MovieThunderUtil.getApplicationString("movies.current.location");
		if(origin == null) return text;
		
	    if(!"".equals(MovieThunderUtil.getString(origin.firstLine)))
	    	text =  origin.firstLine;
	    else if(!"".equals(MovieThunderUtil.getString(origin.city)))
	    	text =  origin.city; 
	    else if(!"".equals(MovieThunderUtil.getString(origin.label)))
	    	text =  origin.label; 
	    else if(origin.lat != 0)
	    	text = MovieThunderUtil.getApplicationString("movies.current.location");
	    
	    return text;
	}*/
	
	/**
	 * 
	 * @param movie
	 * @return
	 */
	public static String getMovieInfo(Movie m,HttpServletRequest request)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<bold>");
		sb.append(m.getName());
		sb.append("</bold>");
		sb.append(MovieThunderUtil.getMovieNewFlag(m.getReleaseDate()));
		sb.append("\n");
		sb.append(MovieThunderUtil.getGradeDisplay(m.getGrade()));
		sb.append("\n");
		sb.append(MovieThunderUtil.getApplicationString("movies.genres",request));
		sb.append(MovieThunderUtil.getString(m.getGenres()));
		sb.append("\n");
		sb.append(MovieUtil.timeFormat(m.getRunTime()));
		
		return MovieUtil.amend(sb.toString());
	}
	
	/**
	 * 
	 * @param theater
	 * @return
	 */
	public static String getTheaterInfo(Theater theater)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<bold>");
		sb.append(theater.getName());
		sb.append("</bold>");
		sb.append("\n");
		sb.append(MovieUtil.getDetailAddress(theater));

		return MovieUtil.amend(sb.toString());
	}
    
	/**
	 * 
	 * @param phone
	 * @return
	 */
	public static String getPhoneInfo(String phone)
	{
		String temp = getString(phone);
		
		if(temp.equals(""))
		{
			return temp;
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("<url>");
		sb.append(phone);
		sb.append("</url>");
		
		return sb.toString();
	}
	
	/**
	 *  
	 &      & amp;   
     '      & apos;   
     "      & quot;   
     >      & gt;   
     <      & lt;
	 * @param s
	 * @return
	 */
	public static String getXMLString(String s)
	{
		String temp =s;
		temp = temp.replaceAll("&", "&amp;");
		temp = temp.replaceAll("'", "&apos;");
		temp = temp.replaceAll("\"", "&quot;");
		temp = temp.replaceAll(">", "&gt;");
		temp = temp.replaceAll("<", "&lt;");
		
		return temp;
	}
	
	/**
	 +   %2B 
	space  %20 
	/   %2F  
	?   %3F  
	%  %25  
	#  %23  
	&  %26  
	= %3D 
	 * @param s
	 * @return
	 */
	public static String geURLString(String s)
	{
		String temp = getString(s);
		temp = temp.replaceAll(" ", "%20");
		
		return temp;
	}
	
	
    /**
     * 
     * @param s
     * @return
     */
    public static String flagAsBold(String s)
    {
    	if("".equals(MovieThunderUtil.getString(s)))
    	{
    		return "";
    	}
    	
    	return "<bold>" + s + "</bold>";
    }
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	protected static String getApplicationString(String key,HttpServletRequest request)
	{
		MessageWrap message = (MessageWrap)request.getAttribute("message");
		
		return (String)message.get(key);
	}
}
