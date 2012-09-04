package com.telenav.browser.movie;
import java.util.Locale;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

public abstract class DateFormatCreater {
	private static final Logger log = Logger.getLogger(Util.class);
	
	public static String DATE_FORMAT_MONTH_DATE="MMM d";
	
	public static SimpleDateFormat getDateFormat(String format,String locale){
		SimpleDateFormat sdf = new SimpleDateFormat(format,transformStringToLocale(locale));
			return sdf;
	}
	public static Locale transformStringToLocale(String locale){
		if (locale==null||locale.equals("")){
			log.debug("locale is null, use en_US as locale");
			return new Locale("en", "US");
		}else{
			String language_locale[] = locale.split("_");
			return new Locale(language_locale[0],language_locale[1]);
		}	
	}
}
