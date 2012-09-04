package com.telenav.cserver.misc.struts.action;

public class AlertsDTOComparator implements java.util.Comparator
{
    public int compare(Object o1, Object o2)
    {
        TrafficAlert alert1 = (TrafficAlert) o1;
        TrafficAlert alert2 = (TrafficAlert) o2;
        
        int distance1 = TrafficIncidentsAction.calDistanceInMilesDM5(alert1.getLat(), alert1.getLon(), alert1.getAlertDTO().getLat(), alert1.getAlertDTO().getLon());
        int distance2 = TrafficIncidentsAction.calDistanceInMilesDM5(alert2.getLat(), alert2.getLon(), alert2.getAlertDTO().getLat(), alert2.getAlertDTO().getLon());
        
        if(distance1 < distance2)
        {
            return -1;
        }
        else if(distance1 == distance2)
        {
            return 0;
        }
        else
        {
            return 1;
        }
    } 
}
