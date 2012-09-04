package com.telenav.cserver.dsr.util;

import com.telenav.j2me.datatypes.Stop;
import com.telenav.resource.data.TtsItem;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: llzhang
 * Date: 2009-7-22
 * Time: 17:54:15
 */
public class MatchingUtil {
	
	private static final String STREET_TYPES_CONFIG = "/street_types.properties" ;
	private static final String HIGHWAY_PREFIXES_CONFIG = "/highway_prefixes.properties" ;
	private static final String DIRECTIONS_CONFIG = "/directions.properties" ;
	
	private static Set<String> streetTypes = new HashSet<String>() ;
	private static Set<String> highwayPrefixes = new HashSet<String>() ;
	private static Set<String> directions = new HashSet<String>() ;
	
	static
	{
		loadDataSet(STREET_TYPES_CONFIG, streetTypes) ;
		loadDataSet(HIGHWAY_PREFIXES_CONFIG, highwayPrefixes) ;
		loadDataSet(DIRECTIONS_CONFIG, directions) ;
	}
	
	public static void loadDataSet(String configFile, Set<String> set)
	{
		Logger logger = Logger.getLogger(DsrConfigure.class.getName());
        
		BufferedReader in = null ;
		
		try
		{
			// load the properties files
			InputStream is = MatchingUtil.class.getResourceAsStream(configFile);
			Reader r= new InputStreamReader(is,"UTF-8") ;
			in = new BufferedReader(r) ;
			
			String line = null ;
			while ((line = in.readLine()) != null)
				set.add(line) ;
			
	    }
		catch(Exception e)
		{
	        logger.log(Level.SEVERE, "Read Properties Failed: "+STREET_TYPES_CONFIG, e);
	    }
		finally
		{
	        if(in != null)
	        {
	            try
	            {
	                in.close();
	            }
	            catch(Exception ignored){}
	        }
	    }	
	}

    public static boolean equals(Stop lStop, Stop rStop) {
        if (lStop == null)
            return rStop == null;

        return rStop != null && (equalStr(lStop.firstLine, rStop.firstLine) &&
        		equalStr(lStop.label, rStop.label) &&
                equalStr(lStop.city, rStop.city) &&
                equalStr(lStop.zip, rStop.zip) &&
                equalStr(lStop.state, rStop.state) &&
                equalStr(lStop.county, rStop.county) &&
                equalStr(lStop.country, rStop.country));
    }

    public static boolean equals(com.telenav.audio.recognition.ds.Stop lStop, com.telenav.audio.recognition.ds.Stop rStop) {
        if (lStop == null)
            return rStop == null;

        return rStop != null && (equalStr(lStop.getFirstLine(), rStop.getFirstLine()) &&
        		equalStr(lStop.getLabel(), rStop.getLabel()) &&
                equalStr(lStop.getCity(), rStop.getCity()) &&
                equalStr(lStop.getZipcode(), rStop.getZipcode()) &&
                equalStr(lStop.getState(), rStop.getState()) &&
                equalStr(lStop.getLiteral(), rStop.getLiteral()) &&
                equalStr(lStop.getCountry(), rStop.getCountry()) &&
                equalStr(lStop.getCrossStreetName(), rStop.getCrossStreetName()));
    }
	
    public static boolean isDuplicateStop(Stop lStop, Stop rStop) {
        if (lStop == null)
            return rStop == null;

        return rStop != null && (equalStr(lStop.firstLine, rStop.firstLine) &&
        		equalStr(lStop.label, rStop.label) &&
                equalStr(lStop.city, rStop.city) &&
                equalStr(lStop.zip, rStop.zip) &&
                equalStr(lStop.state, rStop.state) &&
                equalStr(lStop.county, rStop.county) &&
                equalStr(lStop.country, rStop.country));
    }
    
    public static boolean isDuplicateStop(com.telenav.audio.recognition.ds.Stop lStop, com.telenav.audio.recognition.ds.Stop rStop) {
        if (lStop == null)
            return rStop == null;

        return rStop != null && (equalStr(lStop.getFirstLine(), rStop.getFirstLine()) &&
        		equalStr(lStop.getLabel(), rStop.getLabel()) &&
                equalStr(lStop.getCity(), rStop.getCity()) &&
                equalStr(lStop.getZipcode(), rStop.getZipcode()) &&
                equalStr(lStop.getState(), rStop.getState()) &&
                equalStr(lStop.getLiteral(), rStop.getLiteral()) &&
                equalStr(lStop.getCountry(), rStop.getCountry()) &&
                equalStr(lStop.getCrossStreetName(), rStop.getCrossStreetName()));
    }

    public static boolean equals(List lList, List rList) {
        return Arrays.equals(lList.toArray(new Object[lList.size()]), rList.toArray(new Object[lList.size()]));
    }

    public static boolean equalStr(String left, String right) {
        if (left == null)
            return right == null;
        return left.equalsIgnoreCase(right);
    }

    public static String cityStr(String city, String state) {
        if (city == null) {
            return null;
        }
        if (state == null) {
            return StrUtil.firstLetterUppercase(city);
        }

        return StrUtil.firstLetterUppercase(city) + ", " + state.toUpperCase();
    }

    public static String airportStr(String lable, String fullname, String preParse) {
        if (preParse == null) {
            return null;
        }
        String fmtResult = StrUtil.notBlank(lable) ? lable : preParse;
        if (StrUtil.notBlank(fullname)) {
            fmtResult += " - " + StrUtil.firstLetterUppercase(fullname);
        }
        return fmtResult;
    }

    public static Map<Integer, String> streetParams(String streetText) {
        Map<Integer, String> params = new HashMap<Integer, String>();
        if (!StrUtil.notBlank(streetText))
            return params;

        int atIndex = streetText.toLowerCase().indexOf(" at ");
        String doorNum = null;
        if (atIndex > 0) {
            params.put(TtsItem.KEY_STREET_MAIN, streetText.substring(0, atIndex));
            params.put(TtsItem.KEY_STREET_CROSS, streetText.substring(atIndex + 4));
        } else {
            params.put(TtsItem.KEY_STREET_MAIN, streetText.trim());
        }

        return params;
    }

    private static boolean isDigits(String text) {
        char[] chars = text.toCharArray();
        for (char aChar : chars) {
            if (!Character.isDigit(aChar))
                return false;
        }
        return true;

    }
    
    public static boolean looksLikeAddress(String street)
    {
    	// TODO This is a very basic way to do this. We should find a
    	// better way in the future
    	
    	// The string is an address in the following situations
    	// 1) Last token is a street type
    	// 2) Last token is a direction and penultimate is a street type
    	// 3) First is a highway prefix
    	// 4) First is a direction and second is a highway prefix
    	
    	String[] tokens = street.toUpperCase().split(" ") ;
    	
    	if (streetTypes.contains(tokens[tokens.length-1]))
    		return true ;  // rule 1
    	else if (directions.contains(tokens[tokens.length-1])
    			&& streetTypes.contains(tokens[tokens.length-2]))
    		return true ;  // rule 2 ;
    	else if (highwayPrefixes.contains(tokens[0]))
    		return true ;  // rule 3 ;
    	else if (directions.contains(tokens[0])
    			&& (highwayPrefixes.contains(tokens[2])))
    		return true ;  // rule 4 ;
    	else
    		return false ;
    	
    }
  
    public static Map<String,String> filterSlots(Map<String,String> slots)
    {
    	// Add all of the slots to the matching values
    	Map<String,String> filteredSlots = new HashMap<String,String>() ;
    	for (Entry<String,String> kv : slots.entrySet())
    	{
    		// filter out the following slots:
    		// 1. transactionId
    		// 2. address (if xstreet, street are present)
    		
    		if (ProcessConstants.SLOT_TRANSACTION_ID.equals(kv.getKey()))
    			continue ;
    		else if (ProcessConstants.SLOT_DIGIT_TEXT.equals(kv.getKey()))
    			continue ;
    		else if (ProcessConstants.SLOT_DIGITS.equals(kv.getKey()))
    			continue ;
    		else if (ProcessConstants.SLOT_ADDRESS.equals(kv.getKey())
    				&& slots.get(ProcessConstants.SLOT_STREET) != null)
    			continue ;
    		else if (ProcessConstants.SLOT_CURRENT_LOC.equals(kv.getKey()))
    			continue ;
    		else if ("dontParse".equals(kv.getKey()))
    			continue ;
    		
    		filteredSlots.put(kv.getKey(), kv.getValue());
    	}
    	
    	return filteredSlots ;
    }
    
    public static void main(String args[])
    {
    	String[] tests = new String[]{
    		"1130 kifer road",
    		"1130 kifer road north",
    		"lawrence expressway",
    		"north highway 101",
    		"highway 101",
    	} ;
    	
    	for (String test : tests)
    		System.out.println(test +": "+looksLikeAddress(test));
    	
    	String[] stopWordsTest = new String[]{
        		"the gas station and",
        		"a gas station",
        		"a pizza",
        		"pizza and",
        		"the and a",
        		"hotel of",
        		"gas station",
        		"gas and station",
        		"the taco bell of"
        	} ;
    	System.out.println("\n") ;
    	/**
    	for (String test : stopWordsTest)
    		System.out.println(test +": "+filterStopWords(test));
    	**/
    }
    
}
