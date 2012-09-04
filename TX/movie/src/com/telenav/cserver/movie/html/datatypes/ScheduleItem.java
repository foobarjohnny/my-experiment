package com.telenav.cserver.movie.html.datatypes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleItem
{
    private String id;

    private String movieId;

    private long theaterId;

    private String vendorName;

    private String vendorTheaterId;

    private String ticketURI;

    private List<String> showTimes;

    private String showTimeString;

    private List<String> showPageTimes;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getMovieId()
    {
        return movieId;
    }

    public void setMovieId(String movieId)
    {
        this.movieId = movieId;
    }

    public long getTheaterId()
    {
        return theaterId;
    }

    public void setTheaterId(long theaterId)
    {
        this.theaterId = theaterId;
    }

    public String getVendorName()
    {
        return vendorName;
    }

    public void setVendorName(String vendorName)
    {
        this.vendorName = vendorName;
    }

    public String getVendorTheaterId()
    {
        return vendorTheaterId;
    }

    public void setVendorTheaterId(String vendorTheaterId)
    {
        this.vendorTheaterId = vendorTheaterId;
    }

    public List<String> getShowTimes()
    {
        return showTimes;
    }

    public void setShowTimes(List<String> showTimes)
    {
        this.showTimes = showTimes;
    }

    public String getTicketURI()
    {
        return ticketURI;
    }

    public void setTicketURI(String ticketURI)
    {
        this.ticketURI = ticketURI;
    }

    public String formatShowTimes()
    {
        StringBuffer showTimesText = new StringBuffer();
        if (showTimes == null)
        {
            showTimes = new ArrayList<String>();
        }

        for (String showTime : showTimes)
        {
            showTimesText.append(showTime + ";");
        }

        int length = showTimesText.length();
        if (length > 0)
        {
            showTimesText.deleteCharAt(length - 1);
        }

        return showTimesText.toString();
    }

    public String getShowTimeString()
    {
        return showTimeString;
    }

    public void setShowTimeString(String showTimeString)
    {
        this.showTimeString = showTimeString;
    }

    /**
     * @return the showPageTimes
     */
    public List<String> getShowPageTimes()
    {
        return showPageTimes;
    }
    
    public String getLastestShowTime()
    {
        String result = "";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        String now = simpleDateFormat.format(calendar.getTime());
        int minutesOfNow = getMinutes(now);
        int interval = Integer.MAX_VALUE;
        for (String showTime : showTimes)
        {
            int minutes = getMinutes(showTime);
            if( minutesOfNow < minutes && (minutes-minutesOfNow) < interval ){
                interval = minutes-minutesOfNow;
                result = showTime;
            }
        }
        return convertShowTime(result);
    }
    
    private int getMinutes(String showTime){
        int result = 0;
        String[] times = showTime.split(":");
        if(times != null)
        {
            int hour = Integer.valueOf(times[0]);
            result += hour * 60;
            result += Integer.valueOf(times[1]);
        }
        return result;
    }

    /**
     * @param showPageTimes the showPageTimes to set
     */
    public void setShowPageTimes(List<String> showTimes)
    {
        this.showPageTimes=new ArrayList<String>(); 
        if (showTimes == null)
        {
           this.showTimes = new ArrayList<String>();
        }
        for (String showTime : showTimes)
        {
            showPageTimes.add(convertShowTime(showTime));
        }
    }
    
    private String convertShowTime(String showTime)
    {
        String result = "";
        String[] times = showTime.split(":");
        int hour = Integer.valueOf(times[0]);
        if (hour < 12)
        {
            result = hour + ":" + times[1] + "am";
        }
        else
        {
            result = hour-12 + ":" + times[1] + "pm";
        }
        return result;
    }
}
