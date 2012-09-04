/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Nov 25, 2008
 * File name: MtMovieParser.java
 * Package name: com.telenav.j2me.browser.vendor.mt
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 10:33:19 AM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.vendor.mt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.telenav.browser.movie.datatype.Movie;
import com.telenav.browser.movie.util.MovieUtil;

/**
 * @author dysong (dysong@telenav.cn) 10:33:19 AM, Nov 25, 2008
 */
public class MtMovieParser extends DefaultHandler {

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
     * Result of parsing:ArrayList<Movie>
     */
    private ArrayList movieList = null;

    /**
     * Temporary store units
     */
    private static Logger log = Logger.getLogger(MtMovieParser.class);

    private Movie movie = null;

    private String vendorId;

    private StringBuffer castBuffer = new StringBuffer();

    private StringBuffer directorBuffer = new StringBuffer();

    private StringBuffer genresBuffer = new StringBuffer();

    private StringBuffer gradesBuffer = new StringBuffer();

    private String description;

    private String smallImage;

    private String bigImage;

    private String name;
    /**
     * Constants: xml tag name
     */
    private final static String MOVIES = "movies";

    private final static String MOVIE = "movie";

    private final static String TITLE = "title";

    private final static String CAST = "cast";

    private final static String ID = "movie_id";

    private final static String RUNNING_TIME = "running_time";

    private final static String GENRE = "genre";

    private final static String RATING = "rating";

    private final static String PHOTOS = "photos";

    private final static String DIRECTOR = "director";

    private final static String SYNOPSIS = "synopsis";

    private final static String PARAGRAPH = "P";

    // #if debug
    /**
     * A test example
     */
    public static void main(String[] args) {

        MtMovieParser p = new MtMovieParser();

        ArrayList list = (ArrayList) p.getMovies("C:\\IMOVIES.XML");
        System.out.println(list);
        if (list != null) {

            HashSet hs = new HashSet();
            for (int i = 1; i <= list.size(); i++) {
                // System.out.println(i + ":");
                String vendorId = ((Movie) list.get(i - 1)).getVendorId();
                System.out.println(vendorId);
                if (hs.contains(vendorId)) {
                    System.out.println("Yes:" + vendorId);
                }
                hs.add(vendorId);
            }

        }

    }

    // #endif

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
            log.error("Parse error", e);
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

        if (MOVIES.equalsIgnoreCase(qName)) {
            movieList = new ArrayList();
            tags.push(qName);
        }
        // Get the vendor id of movie
        else if (MOVIE.equalsIgnoreCase(qName)) {
            movie = new Movie();
            tags.push(qName);
        } else if (TITLE.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (ID.equalsIgnoreCase(qName)) {
            tags.push(qName);
        }
        // choose the first two jpeg image for bigimage and smallimage
        else if (PHOTOS.equalsIgnoreCase(qName)) {
            if (movie.getBigImage() == null || movie.getBigImage().length() < 1
                    || movie.getSmallImage() == null
                    || movie.getSmallImage().length() < 1) {
                tags.push(qName);
            }
        }

        else if (qName != null && qName.toLowerCase().endsWith(RATING)) {
            tags.push(qName);
        } else if (CAST.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (DIRECTOR.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (RUNNING_TIME.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (GENRE.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (SYNOPSIS.equalsIgnoreCase(qName)) {
            tags.push(qName);
        } else if (PARAGRAPH.equalsIgnoreCase(qName)) {
            if (SYNOPSIS.equalsIgnoreCase(((String) tags.peek()))) {
                tags.push(qName);
            }
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
        // #endif
    }

    public void characters(char ch[], int start, int length)
            throws SAXException {
        textStringBuffer.append(ch, start, length);
        textString = textStringBuffer.toString().trim();
    }

    public void endElement(String uri, String localName, String qName) {

        textStringBuffer.setLength(0);
        if (tags.size() < 1)
            return;
        String topTag = (String) tags.peek();
        if (MOVIES.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                reset();
                tags.pop();
            }
        } else if (MOVIE.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                // movie.setAltFilmId(0+"");
                movie.setCast(castBuffer.toString());
                movie.setDirector(directorBuffer.toString());
                movie.setGenres(genresBuffer.toString());
                movie.setGrade(gradesBuffer.toString());
                if (smallImage == null && bigImage != null) {
                    smallImage = bigImage;
                    movie.setSmallImage(smallImage);
                }

                movieList.add(movie);
                reset();
                tags.pop();
            }
        } else if (TITLE.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                name = textString;
                movie.setName(name);
                tags.pop();
            }

        } else if (ID.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                vendorId = textString;
                movie.setVendorId(vendorId);
                tags.pop();
            }

        } else if (PHOTOS.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                if (bigImage == null) {
                    bigImage = textString;
                    movie.setBigImage(bigImage);
                    tags.pop();
                } else if (smallImage == null) {
                    smallImage = textString;
                    movie.setSmallImage(smallImage);
                    tags.pop();
                }
            }

        } else if (qName != null && qName.toLowerCase().endsWith(RATING)) {
            if (qName.equalsIgnoreCase(topTag)) {
                if (textString != null && textString.length() > 0
                        && !"NO RATING".equalsIgnoreCase(textString)) {
                    if (gradesBuffer.length() > 0) {
                        gradesBuffer.append(", ");
                    }
                    gradesBuffer.append(textString);
                }
                tags.pop();
            }
        } else if (CAST.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                if (castBuffer.length() > 0) {
                    castBuffer.append(", ");
                }
                castBuffer.append(textString);
                tags.pop();
            }

        } else if (DIRECTOR.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                if (directorBuffer.length() > 0) {
                    directorBuffer.append(", ");
                }
                directorBuffer.append(textString);
                tags.pop();
            }

        } else if (RUNNING_TIME.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {

                if (textString != null && textString.length() > 0) {
                    // 115->01H55M
                    movie.setRunTime(MovieUtil.formatRunTime(textString));
                }
                tags.pop();
            }

        } else if (GENRE.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                if (genresBuffer.length() > 0) {
                    genresBuffer.append(", ");
                }
                genresBuffer.append(textString);
                tags.pop();
            }
        } else if (SYNOPSIS.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                tags.pop();
            }
        } else if (PARAGRAPH.equalsIgnoreCase(qName)) {
            if (qName.equalsIgnoreCase(topTag)) {
                description = textString;
                movie.setDescription(description);
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

    /**
     * Clear temporary store units
     */

    private void reset() {
        textString = null;

        textStringBuffer.setLength(0);

        movie = null;

        vendorId = null;

        castBuffer.setLength(0);

        directorBuffer.setLength(0);

        gradesBuffer.setLength(0);

        genresBuffer.setLength(0);

        description = null;

        smallImage = null;

        bigImage = null;

        name = null;

    }

}
