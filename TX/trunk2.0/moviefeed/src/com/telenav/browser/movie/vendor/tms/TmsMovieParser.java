/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on 2008-10-28
 * File name: TmsMovieParser.java
 * Package name: com.telenav.j2me.browser.vendor.TMS
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 10:55:46 AM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.vendor.tms;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.telenav.browser.movie.datatype.Movie;

/**
 * 
 * The class TmsMovieParser takes charge of parsing a xml file which contains
 * data about movies,and return a ArrayList<Movie>.
 * 
 * @author dysong (dysong@telenav.cn) 10:55:46 AM
 */

public class TmsMovieParser extends DefaultHandler {
    /**
     * Blank (", ") length
     */
    private final static int BLANK_LENGTH = 2;
    /**
     * Cast limit length
     */
    private final static int CAST_LENGTH = 200;
    /**
     * Description limit length
     */
    private final static int DESCRIPTION_LENGTH = 250;
    /**
     * Name limit length
     */
    private final static int NAME_LENGTH = 40;

    /**
     * The stack is for storing tags(qNames)
     */
    private Stack tags = new Stack();

    /**
     * To store text of a Xml item
     */
    private StringBuffer textStringBuffer = new StringBuffer();

    private String textString;

    /**
     * To flag that value of text is director name
     */
    private boolean isDirectorNameFlag;

    /**
     * To flag that value of text is cast name
     */
    private boolean isCastNameFlag;

    /**
     * To choose the description and title which has max size
     */

    private int maxSize;

    /**
     * To choose the description and title which has max size.It's for comparing
     */
    private int currentSize;

    /**
     * Result of parsing:ArrayList<Movie>
     */
    private ArrayList movieList = null;

    /**
     * Temporary store units
     */
    private static Logger log = Logger.getLogger(TmsMovieParser.class);

    private Movie movie = null;

    private String tmsId;

    private String altFilmId;

    private StringBuffer personNameBuffer = new StringBuffer();

    private StringBuffer castBuffer = new StringBuffer();

    private StringBuffer directorBuffer = new StringBuffer();

    private StringBuffer genresBuffer = new StringBuffer();
    
    private StringBuffer releaseDatesBuffer = new StringBuffer();

    private String ratingString;

    private String description;

    private String smallImage;

    private String bigImage;

    private String name;

    /**
     * Constants: xml tag name
     */
    private final static String PROGRAMS = "programs";

    private final static String PROGRAM = "program";

    private final static String TITLES = "titles";

    private final static String TITLE = "title";

    private final static String DESCRIPTIONS = "descriptions";

    private final static String DESCRIPTION = "desc";

    private final static String CAST = "cast";

    private final static String CREW = "crew";

    private final static String FIRST_NAME = "first";

    private final static String LAST_NAME = "last";

    private final static String RUN_TIME = "runTime";

    private final static String GENRES = "genres";

    private final static String GENRE = "genre";

    private final static String RATING = "rating";

    private final static String QUALITY_RATING = "qualityRating";

    private final static String IMAGE = "image";

    private final static String FILE_NAME = "URI";

    private final static String ALTFILMID = "altFilmId";

    private final static String SIZE = "size";

    private final static String TMSID = "TMSId";

    private final static String AREA = "area";

    private final static String UNITED_STATES = "United States";

    private final static String CODE = "code";

    private final static String TYPE = "type";

    private final static String RATINGS_BODY = "ratingsBody";

    private final static String JPEG = "image/jpg";

    private final static String TMS = "TMS";

    private final static String VALUE = "value";

    private final static String THUMBTAIL_IMAGE_SUFFIX = "_t.jpg";

    private final static String RELEASEDATES = "releaseDates";

    private final static String RELEASEDATE = "releaseDate";
    
    /**
     * Get parsing result:ArrayList<Movie>
     * 
     * @return the <code>Movie</code> List
     */
    public List getMovies(String xmlFileName) {
        parse(xmlFileName);
        return movieList;
    }

    /**
     * Parse a xml file according xmlName to get movies information
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

        if (PROGRAMS.equalsIgnoreCase(qName)) {
            movieList = new ArrayList();
            tags.push(qName);
        }
        // Get the tms_id of movie
        else if (PROGRAM.equalsIgnoreCase(qName)) {
            movie = new Movie();
            tmsId = attr.getValue(TMSID);
            altFilmId = attr.getValue(ALTFILMID);
            movie.setVendorId(tmsId);
            movie.setAltFilmId(altFilmId);
            tags.push(qName);
        } else if (TITLES.equalsIgnoreCase(qName)) {
            maxSize = 0;
            currentSize = 0;
            tags.push(qName);

        } else if (TITLE.equalsIgnoreCase(qName)) {
            // choose full title

            tags.push(qName);

        } else if (DESCRIPTIONS.equalsIgnoreCase(qName)) {
            maxSize = 0;
            currentSize = 0;
            tags.push(qName);

        } else if (DESCRIPTION.equalsIgnoreCase(qName)) {
            // Choose description of max size
            currentSize = Integer.parseInt(attr.getValue(SIZE));
            if (currentSize > maxSize && currentSize <= DESCRIPTION_LENGTH) {
                maxSize = currentSize;
                tags.push(qName);
            }

        } else if (CAST.equalsIgnoreCase(qName)) {
            castBuffer.setLength(0);
            isCastNameFlag = true;
            tags.push(qName);
        } else if (CREW.equalsIgnoreCase(qName)) {
            directorBuffer.setLength(0);
            tags.push(qName);
        } else if (FIRST_NAME.equalsIgnoreCase(qName)) {
            if (isCastNameFlag || isDirectorNameFlag) {
                tags.push(qName);
            }
        } else if (LAST_NAME.equalsIgnoreCase(qName)) {
            if (isCastNameFlag || isDirectorNameFlag) {
                tags.push(qName);
            }
        } else if (RUN_TIME.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (GENRES.equalsIgnoreCase(qName)) {
            genresBuffer.setLength(0);
            tags.push(qName);
        } else if (GENRE.equalsIgnoreCase(qName)) {
            tags.push(qName);
        }
        // Choose rating code of United States
        else if (RATING.equalsIgnoreCase(qName)) {
            if (UNITED_STATES.equalsIgnoreCase(attr.getValue(AREA))) {
                movie.setGrade(attr.getValue(CODE));
            }

        }
        // Get qualityRating of United TMS
        else if (QUALITY_RATING.equalsIgnoreCase(qName)) {
            if (TMS.equalsIgnoreCase(attr.getValue(RATINGS_BODY))) {
                ratingString = attr.getValue(VALUE);
                if (ratingString != null && ratingString.length() > 0) {
                    movie.setRating(new Float(Float.parseFloat(ratingString)));
                }
            }
        }
        // choose the first two jpeg image for bigimage and smallimage
        else if (IMAGE.equalsIgnoreCase(qName)
                && JPEG.equalsIgnoreCase(attr.getValue(TYPE))) {
            tags.push(qName);
        } else if (FILE_NAME.equalsIgnoreCase(qName)) {
            if (tags.size() < 1)
                return;
            if (IMAGE.equalsIgnoreCase(((String) tags.peek()))) {
                tags.push(qName);
            }
        }else if (RELEASEDATES.equalsIgnoreCase(qName)) {
        	releaseDatesBuffer.setLength(0);
	        tags.push(qName);
	    } else if (RELEASEDATE.equalsIgnoreCase(qName)) {
	    	tags.push(qName);
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
        // endif
    }

    public void characters(char ch[], int start, int length)
            throws SAXException {

        textStringBuffer.append(ch, start, length);

        textString = textStringBuffer.toString().trim();

        // According the text value of role item to mark isDirectorFlag
        if (textString != null && "Director".equalsIgnoreCase(textString)) {
            isDirectorNameFlag = true;
        }

    }

    public void endElement(String uri, String localName, String qName) {

        textStringBuffer.setLength(0);
        if (tags.size() < 1)
            return;
        String topTag = (String) tags.peek();
        if (PROGRAMS.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                reset();
                tags.pop();
            }
        } else if (PROGRAM.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                // If either is null , both null
                if (smallImage == null || bigImage == null) {
                    movie.setSmallImage(null);
                    movie.setBigImage(null);
                }
                movieList.add(movie);
                reset();
                tags.pop();
            }
        } else if (TITLES.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                maxSize = 0;
                currentSize = 0;
                movie.setName(name);
                tags.pop();
            }

        }

        else if (TITLE.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                currentSize = textString.length();
                if (currentSize > maxSize && currentSize <= NAME_LENGTH) {
                    maxSize = currentSize;
                    name = textString;
                }
                tags.pop();
            }

        } else if (DESCRIPTIONS.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                maxSize = 0;
                currentSize = 0;
                movie.setDescription(description);
                tags.pop();
            }

        } else if (DESCRIPTION.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                description = textString;
                tags.pop();
            }
        } else if (CAST.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                movie.setCast(castBuffer.toString());
                isCastNameFlag = false;
                castBuffer.setLength(0);
                tags.pop();
            }

        } else if (CREW.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                movie.setDirector(directorBuffer.toString());
                isDirectorNameFlag = false;
                directorBuffer.setLength(0);
                tags.pop();
            }
        } else if (FIRST_NAME.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                personNameBuffer.setLength(0);
                personNameBuffer.append(textString);
                tags.pop();
            }

        } else if (LAST_NAME.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {

                if (isCastNameFlag) {
                    personNameBuffer.append(" ");
                    personNameBuffer.append(textString);

                    if (castBuffer.length() < 1) {
                        if (personNameBuffer.length() <= CAST_LENGTH) {
                            castBuffer.append(personNameBuffer.toString());
                        }
                    } else {
                        if (castBuffer.length() + personNameBuffer.length()
                                + BLANK_LENGTH <= CAST_LENGTH) {
                            castBuffer.append(", ");
                            castBuffer.append(personNameBuffer.toString());
                        } else {
                            isCastNameFlag = false;
                        }

                    }
                } else if (isDirectorNameFlag) {
                    personNameBuffer.append(" ");
                    personNameBuffer.append(textString);
                    if (directorBuffer.length() > 0) {
                        directorBuffer.append(", ");
                    }
                    // Append a new director name
                    directorBuffer.append(personNameBuffer.toString());
                    isDirectorNameFlag = false;
                }
                personNameBuffer.setLength(0);
                tags.pop();
            }
        }

        else if (RUN_TIME.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {

                if (textString != null && textString.length() > 2) {
                    // remove prefix "PT" PT01H25M->01H25M
                    movie.setRunTime(textString.substring(2));
                }
                tags.pop();
            }

        } else if (GENRES.equalsIgnoreCase(qName)) {
        	if (qName.equalsIgnoreCase(topTag)) {
                movie.setGenres(genresBuffer.toString());
                genresBuffer.setLength(0);
                tags.pop();
            }
        } else if (GENRE.equalsIgnoreCase(qName)) {
        	if (qName.equalsIgnoreCase(topTag)) {

                if (genresBuffer.length() > 0) {
                    genresBuffer.append(", ");
                }
                // Append a new genre
                genresBuffer.append(textString);
                tags.pop();
            }
        }
        else if (RELEASEDATES.equalsIgnoreCase(qName)) {
        	if (qName.equalsIgnoreCase(topTag)) {
                movie.setReleaseDate(getReleaseDate(releaseDatesBuffer.toString()));
                releaseDatesBuffer.setLength(0);
                tags.pop();
            }
        } else if (RELEASEDATE.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                if (releaseDatesBuffer.length() > 0) {
                	releaseDatesBuffer.append(",");
                }
                // Append a new genre
                releaseDatesBuffer.append(textString);
                tags.pop();
            }
        }
        else if (IMAGE.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                tags.pop();
            }
        } else if (FILE_NAME.equalsIgnoreCase(qName)) {

            if (qName.equalsIgnoreCase(topTag)) {
                // Image with behindhand name in alphametic is prior
                if (textString != null && textString.length() > 0) {
                    // suffix "_t" of filename is a thumbnail(small) image flag
                    if (textString.endsWith(THUMBTAIL_IMAGE_SUFFIX)) {
                        if (smallImage != null && smallImage.length() > 0) {
                            if (smallImage.compareTo(textString) >= 1) {
                                smallImage = textString;
                                movie.setSmallImage(smallImage);
                            }
                        } else {
                            smallImage = textString;
                            movie.setSmallImage(smallImage);
                        }

                    } else {
                        if (bigImage != null && bigImage.length() > 0) {
                            if (bigImage.compareTo(textString) >= 1) {
                                bigImage = textString;
                                movie.setBigImage(bigImage);
                            }
                        } else {
                            bigImage = textString;
                            movie.setBigImage(bigImage);
                        }
                    }

                }
                tags.pop();
            }
        }

        textString = null;
        // #if debug
        // Don't delete.It's important for debug
        // System.out.println("Element End uri: " + uri + "localName: " +
        // localName
        // + "qName: " + qName);
        // System.out.println("END====:" + tags);
        // System.out
        // .println("=============================================================");
        // #endif
    }

    private Date getReleaseDate(String str)
    {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
    	Date releaseDate = new Date();
		try
		{
			//default release date as "1980-01-01"
	    	releaseDate = dateFormat.parse("1980-01-01");
	    	
	    	if(str != null && !str.equals(""))
	    	{
	    		String[] dates = str.split(",");
	    		if(dates.length >= 1)
	    		{
	    			String realDate = dates[dates.length-1];
	    			if(realDate.length() == 10)
	    			{
	
	    					releaseDate = dateFormat.parse(realDate);
	
	    			}
	    		}
	    	}
		}
		catch(Exception e)
		{
			log.error("getReleaseDate format error", e);
		}
    	return releaseDate;
    }
    /**
     * Clear temporary store units
     */

    private void reset() {
        textString = null;

        textStringBuffer.setLength(0);

        maxSize = 0;

        currentSize = 0;

        isDirectorNameFlag = false;

        isCastNameFlag = false;

        movie = null;

        tmsId = null;

        altFilmId = null;

        ratingString = null;

        castBuffer.setLength(0);

        directorBuffer.setLength(0);

        personNameBuffer.setLength(0);

        genresBuffer.setLength(0);

        description = null;

        smallImage = null;

        bigImage = null;

        name = null;
        
        releaseDatesBuffer.setLength(0);

    }

}
