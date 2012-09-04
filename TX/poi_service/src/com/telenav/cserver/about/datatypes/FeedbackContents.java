package com.telenav.cserver.about.datatypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.telenav.cserver.util.TnConstants;

public class FeedbackContents {
   
	private Logger logger = Logger.getLogger(FeedbackContents.class);
	//private Vector feedbackTopics;
	private String feedbackVersion;
	private HashMap feedbackMaps;
	
	private static FeedbackContents instance = new FeedbackContents();
	
	private FeedbackContents()
	{
		loadFeedback();
	}
	public static FeedbackContents getInstance()
	{
		return instance; 
	}
	
	//about feedback function have been removed, so this method is no use. not be dizzy with this method :)
	private void loadFeedback()
	{
		feedbackVersion = "2.0";
		
		String supportedLocaleStr = TnConstants.SUPPORTED_LOCALE;
		StringTokenizer token = new StringTokenizer(supportedLocaleStr, ",");
		ArrayList supportedLocales = new ArrayList();
        while (token.hasMoreTokens())
        {
            String locale = token.nextToken();
            supportedLocales.add(locale.trim());
        }
        feedbackMaps = new HashMap();
        for(int k=0; k<supportedLocales.size(); k++)
        {
        	 String strLocale = (String)supportedLocales.get(k);
             Vector feecbackTopicVec = null;
            try
            {
                feecbackTopicVec = new Vector();
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
             if (feecbackTopicVec != null && feecbackTopicVec.size() > 0)
             {
            	 feedbackMaps.put(strLocale, feecbackTopicVec);
                 logger.info("feedback topic locale=" + strLocale + "topic size=" + feecbackTopicVec.size());
             }
        }
		
		logger.info("load feedback..");
	}
	
	public Vector getFeedbackTopics(String locale)
	{
		Vector topics = null;
		if(feedbackMaps != null)
		{
			topics = (Vector)feedbackMaps.get(locale);
		}
		if(topics == null || topics.size() == 0)
		{
			topics = (Vector)feedbackMaps.get(TnConstants.DEFAULT_LOCALE);
		}
		return topics;
	}
	
}
