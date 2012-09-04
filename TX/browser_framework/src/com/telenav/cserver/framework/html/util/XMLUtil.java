package com.telenav.cserver.framework.html.util;

public class XMLUtil {

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
}
