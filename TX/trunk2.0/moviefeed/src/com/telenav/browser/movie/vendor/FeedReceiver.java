/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Oct 23, 2008
 * File name: FeedReceiver.java
 * Package name: com.telenav.j2me.browser.TMS
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 1:25:25 PM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.vendor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.Set;

import org.apache.log4j.Logger;

import com.telenav.browser.movie.datatype.Movie;
import com.telenav.browser.movie.datatype.MovieImage;
import com.telenav.browser.movie.datatype.Schedule;
import com.telenav.browser.movie.datatype.Theater;
import com.telenav.browser.movie.db.DaoManager;
import com.telenav.browser.movie.db.MovieDao;
import com.telenav.browser.movie.db.MovieImageDao;
import com.telenav.browser.movie.db.ScheduleDao;
import com.telenav.browser.movie.db.TheaterDao;
import com.telenav.browser.movie.util.MovieUtil;
import com.telenav.browser.movie.vendor.tms.TmsVendorImpl;
import com.televigation.log.TVCategory;

/**
 * The Class FeedReceiver takes charge of executing entire procedure of data
 * feed(XML-->JAVA-->DATABASE)through parsing first movie and theater second
 * schedule.
 * 
 * @author lwei (lwei@telenav.cn) 1:25:25 PM, Oct 23, 2008
 */
public class FeedReceiver {
    private static final Logger misLogger = (Logger) TVCategory
            .getInstance("movie.feed.mis");
    
    private static final Logger trimLogger = (Logger) TVCategory
			.getInstance("movie.feed.trim");
    
    private static PropertyResourceBundle data_length_config = (PropertyResourceBundle) PropertyResourceBundle
			.getBundle("config.data_length");

    private Map screenSizeMap = new HashMap();

    private HashSet availQualsSet = new HashSet();

    public static void main(String[] args) {
        new FeedReceiver().catchFeed();
    }

    public boolean catchFeed() {
        boolean ok = false;
        ok = doCatch();
        return ok;
    }

    private boolean doCatch() {
        // Get the screen size we supported.
        boolean b = initial();
        if (!b) {
            misLogger.error("Feed initialization failed");
            return false;
        }

        // Get vendor list.
        List vendorList = new ArrayList();
        vendorList.add(new TmsVendorImpl());

        // Create data list.
        List movieList = new ArrayList();
        List theaterList = new ArrayList();
        List scheduleList = new ArrayList();

        // Get data from vendor.
        for (Iterator iterator = vendorList.iterator(); iterator.hasNext();) {
            Vendor v = (Vendor) iterator.next();

            // Initialize.
            misLogger.info("Initialize vendor [" + v.getVenderName() + "]");
            boolean initialize = v.initialize();
            if (!initialize) {
                misLogger.info("Initialize failed.");
                continue;
            }

            try {
                // Load feed.
                misLogger.info("Downloading feed from vendor ["
                        + v.getVenderName() + "]");
                boolean loadFeed = v.loadFeed();
                if (!loadFeed) {
                    misLogger.info("Download failed.");
                    continue;
                }

                List mList = v.getMovieList();
                List tList = v.getTheaterList();
                List sList = v.getScheduleList();
                if (mList == null || tList == null || sList == null
                        || mList.isEmpty() || tList.isEmpty()
                        || sList.isEmpty()) {
                    misLogger.info("Empty feed data.");
                    continue;
                }
                // Set vendor name
                for (Iterator it = sList.iterator(); it.hasNext();) {
                    Schedule s = (Schedule) it.next();
                    s.setVendorName(v.getVenderName());
                }
                // Validate the connection among the movie, theater and
                // schedule.
                removeUnused(mList, tList, sList);

                // Optimize quals of schedules
                optimizeQuals(sList);

                // Process the image.
                // If download failed, this should not break the feed.
                processImage(mList, v);

                // Specify movies and schedules info and their connections with
                // details such as meaning of showing movie:IMAX,Open Caption
                // specify(mList, sList);
                // After specification, some raw and valueless movie info need
                // to clear.
                // removeUnused(mList, tList, sList);

                // Add to list.
                movieList.addAll(mList);
                theaterList.addAll(tList);
                scheduleList.addAll(sList);
            } catch (IllegalAccessException e) {
                misLogger.error(e);
            }
        }

        if (movieList.isEmpty() || theaterList.isEmpty()
                || scheduleList.isEmpty()) {
            misLogger.error("No feed data for all vendors.");
            return false;
        }

        // Merge data.
        // TODO Rain MT feed will not be applied and merge data is not
        // necessary. If it is needed in the future, it should be implement
        // here.

        // Store
        try {
        	checkDataLengthForDB(movieList, theaterList, scheduleList);
            storeFeed(movieList, theaterList, scheduleList);
        } catch (SQLException e) {
            misLogger.fatal("Error operating DB when feed", e);
            return false;
        }
        return true;
    }

    private boolean initial() {

        // Get the property.
        PropertyResourceBundle config = (PropertyResourceBundle) PropertyResourceBundle
                .getBundle("config.feed");

        String availQuals = config.getString("movies.available.quals");
        // Get available quals
        if (availQuals == null || availQuals.length() < 1) {
            availQuals = "";
        }
        String[] qualsArray = availQuals.split(",");
        for (int i = 0; i < qualsArray.length; i++) {
            availQualsSet.add(qualsArray[i].trim());
        }
        // Get screen size
        String screenSize = config.getString("image.size");
        if (screenSize == null || screenSize.length() == 0) {
            return false;
        }

        // Get the image size.
        String[] devices = screenSize.split(";");
        for (int i = 0; i < devices.length; i++) {
            String device = devices[i];
            String[] split = device.split("\\|");
            if (split.length != 3) {
                continue;
            }

            // Create a image size.
            ImageSize imageSize = new ImageSize();
            imageSize.name = split[0];

            // Set small;
            String[] smallSize = split[1].split("x");
            if (smallSize.length == 2) {
                imageSize.smallWidth = Integer.parseInt(smallSize[0]);
                imageSize.smallHeight = Integer.parseInt(smallSize[1]);
            } else {
                continue;
            }

            // Set big
            // Set small;
            String[] bigSize = split[2].split("x");
            if (smallSize.length == 2) {
                imageSize.bigWidth = Integer.parseInt(bigSize[0]);
                imageSize.bigHeight = Integer.parseInt(bigSize[1]);
            } else {
                continue;
            }

            if (imageSize.isValid()) {
                screenSizeMap.put(imageSize.name, imageSize);
            }
        }

        if (screenSizeMap.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    class ImageSize {
        String name;
        int smallWidth;
        int smallHeight;
        int bigWidth;
        int bigHeight;

        public boolean isValid() {
            if (smallHeight > 0 && smallWidth > 0 && bigHeight > 0
                    && bigWidth > 0) {
                return true;
            }
            return false;
        }
    }

    /**
     * Optimize quals of schedules.
     * <p>
     * First sort schedules by feature (theaterVendorId_movieVendorId_date),
     * then format quals of each sorted schedules.
     * 
     * @param scheduleList <List><code>Schedule</code></List>
     * @return List schedule list optimized
     */
    private List optimizeQuals(List scheduleList) {
        HashMap map = new HashMap();
        String key;
        ArrayList schedules;
        Schedule s;
        // Sort by feature of schedule
        for (int i = 0; i < scheduleList.size(); i++) {
            s = (Schedule) scheduleList.get(i);
            key = s.getTheaterVendorId() + "_" + s.getMovieVendorId() + "_"
                    + s.getDate();
            if (map.containsKey(key)) {
                schedules = (ArrayList) map.get(key);
            } else {
                schedules = new ArrayList();
            }
            schedules.add(s);
            map.put(key, schedules);
        }
        // Format quals of each sorted schedules
        Set keys = map.keySet();
        for (Iterator it = keys.iterator(); it.hasNext();) {
            key = (String) it.next();
            schedules = (ArrayList) map.get(key);
            formatQuals(schedules);
        }
        return new ArrayList(map.values());
    }

    /**
     * Format quals of schedules.
     * <p>
     * Only one schedule, retain quals of this schedule which are in config
     * file. When there are many schedules, remove quals which are contained by
     * all schedules.
     * 
     * @param schedules
     */
    private void formatQuals(ArrayList schedules) {
        if (schedules == null || schedules.size() < 1) {
            return;
        }

        StringBuffer qualsBuffer = new StringBuffer();
        String[] qualArray = null;
        String quals = null;
        // Only one schedule, retain quals of this schedule which are in config
        // file.
        if (schedules.size() == 1) {

            // Condense quals of schedules
            Schedule s = (Schedule) schedules.get(0);
            quals = s.getQuals();
            // if quals is null or "" ,there is no action.
            if (quals != null && quals.length() > 0) {
                qualArray = quals.split("\\|");
                qualsBuffer.setLength(0);
                for (int j = 0; j < qualArray.length; j++) {
                    String qual = qualArray[j].trim();
                    if (availQualsSet.contains(qual)) {
                        if (qualsBuffer.length() > 0) {
                            qualsBuffer.append(" | ");
                        }
                        qualsBuffer.append(qual);
                    }
                }
                s.setQuals(qualsBuffer.toString());
            }

        }
        // When there are many schedules, remove quals which are contained by
        // all schedules.
        else {
            HashSet qualsSet;
            HashSet invalidQualsSet = new HashSet();
            // Get set of invalid quals that all schedules contains
            for (int i = 0; schedules != null && i < schedules.size(); i++) {
                Schedule s = (Schedule) schedules.get(i);
                quals = s.getQuals();
                qualsSet = new HashSet();
                if (quals != null && quals.length() > 0) {
                    qualArray = quals.split("\\|");
                    for (int j = 0; j < qualArray.length; j++) {
                        String qual = qualArray[j].trim();
                        qualsSet.add(qual);
                    }

                }
                if (i == 0) {
                    invalidQualsSet.addAll(qualsSet);
                } else {
                    invalidQualsSet.retainAll(qualsSet);
                }
            }
            // Condense quals of schedules
            for (Iterator it = schedules.iterator(); it.hasNext();) {
                Schedule s = (Schedule) it.next();
                quals = s.getQuals();
                // if quals is null or "" ,there is no action.
                if (quals != null && quals.length() > 0) {
                    qualArray = quals.split("\\|");
                    qualsBuffer.setLength(0);
                    for (int j = 0; j < qualArray.length; j++) {
                        String qual = qualArray[j].trim();
                        if (!invalidQualsSet.contains(qual)) {
                            if (qualsBuffer.length() > 0) {
                                qualsBuffer.append(" | ");
                            }
                            qualsBuffer.append(qual);
                        }
                    }
                    s.setQuals(qualsBuffer.toString());
                }
            }
        }

    }

    private void processImage(List movieList, Vendor v)
            throws IllegalAccessException {
        MovieImageDao imageDao = DaoManager.getMovieImageDao();
        try {
			// To meet sn 28 brew requirement, all posters should be stored in
			// PNG not JPG format.
			// Clearing movieimage table which stores JPG images is first step
			// of process.
			// clear_movie_image_table_flag.txt is a flag indicator. If it
			// exists, feed program will clear movieimage table in movies db and
			// delete it after feeding automatically.
        	File f = new File("config/clear_movie_image_table_flag.txt");
        	if(f.exists()){
        		misLogger.debug("clear moiveimage table in moviesdb");
        		imageDao.truncate();
        		boolean delete = f.delete();
        		misLogger.debug("delete config/clear_movie_image_table_flag.txt: "+ delete);
        	}
            // Get the download list.
            // The movie image key will be vendor name + original image path.
            Set allImageKeys = imageDao.getAllImageKeys();
            List downloadList = new ArrayList();
            for (Iterator iterator2 = movieList.iterator(); iterator2.hasNext();) {
                Movie m = (Movie) iterator2.next();

                // Movie with no image.
                if (m.getBigImage() == null && m.getSmallImage() == null) {
                    continue;
                }

                // Resized image available
                String bigName = v.getVenderName() + "_" + m.getBigImage();
                String smallName = v.getVenderName() + "_" + m.getSmallImage();
                Set keySet = screenSizeMap.keySet();
                for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
                    String screenSize = (String) iterator.next();
                    if (!allImageKeys.contains(screenSize + "_" + bigName)
                            || !allImageKeys.contains(screenSize + "_"
                                    + smallName)) {
                        downloadList.add(m);
                        break;
                    }
                }
            }

            // Download the images.
            List downloadImage = v.downloadImage(downloadList);

            // Set the name with the vendor id.
            for (Iterator iterator = movieList.iterator(); iterator.hasNext();) {
                Movie m = (Movie) iterator.next();
                if (m.getBigImage() != null) {
                    m.setBigImage(v.getVenderName() + "_" + m.getBigImage());
                }
                if (m.getSmallImage() != null) {
                    m
                            .setSmallImage(v.getVenderName() + "_"
                                    + m.getSmallImage());
                }
            }

            // Insert into db.
            MovieImageDao dao = DaoManager.getMovieImageDao();
            for (Iterator iterator = downloadImage.iterator(); iterator
                    .hasNext();) {
                MovieImage image = (MovieImage) iterator.next();
                misLogger.info("Process image" + image.getKey());
                for (Iterator iterator2 = screenSizeMap.values().iterator(); iterator2
                        .hasNext();) {
                    ImageSize imageSize = (ImageSize) iterator2.next();
                    String imageName = imageSize.name + "_" + v.getVenderName()
                            + "_" + image.getKey();
                    if (!allImageKeys.contains(imageName)) {
                        MovieImage toInsert;
                        if (image.isBig()) {
                            toInsert = MovieUtil.resizeImage(image,
                                    imageSize.bigHeight, imageSize.bigWidth);
                        } else {
                            toInsert = MovieUtil
                                    .resizeImage(image, imageSize.smallHeight,
                                            imageSize.smallWidth);
                        }

                        // Do nothing if resize failed.
                        if (toInsert != null) {
                            toInsert.setKey(imageName);
                            dao.insert(toInsert);
                        } 
                    }
                }

            }

        } catch (SQLException e) {
            misLogger.error("Fail when downloading the images", e);
        }
    }

    private void removeUnused(List movieList, List theaterList,
            List scheduleList) {
        // Clean schedule
        HashSet theaterIdSet = new HashSet();
        HashSet movieIdSet = new HashSet();
        for (Iterator iterator = theaterList.iterator(); iterator.hasNext();) {
            Theater t = (Theater) iterator.next();
            theaterIdSet.add(t.getVendorId());
        }
        for (Iterator iterator = movieList.iterator(); iterator.hasNext();) {
            Movie m = (Movie) iterator.next();
            movieIdSet.add(m.getVendorId());
        }
        for (Iterator iterator = scheduleList.iterator(); iterator.hasNext();) {
            Schedule s = (Schedule) iterator.next();
            if (!movieIdSet.contains(s.getMovieVendorId())
                    || !theaterIdSet.contains(s.getTheaterVendorId())) {
                iterator.remove();
            }
        }

        // Clean theater.
        theaterIdSet.clear();
        movieIdSet.clear();
        for (Iterator iterator = scheduleList.iterator(); iterator.hasNext();) {
            Schedule s = (Schedule) iterator.next();
            theaterIdSet.add(s.getTheaterVendorId());
            movieIdSet.add(s.getMovieVendorId());
        }
        for (Iterator iterator = theaterList.iterator(); iterator.hasNext();) {
            Theater t = (Theater) iterator.next();
            if (!theaterIdSet.contains(t.getVendorId())) {
                iterator.remove();
            }
        }

        // Clean movie
        for (Iterator iterator = movieList.iterator(); iterator.hasNext();) {
            Movie m = (Movie) iterator.next();
            if (!movieIdSet.contains(m.getVendorId())) {
                iterator.remove();
            }
        }
    }

    private boolean storeFeed(List movieList, List theaterList,
            List scheduleList) throws SQLException {
        misLogger.info("Start store feed");

        // Create temporary table.
        misLogger.info("Create temp table for new feed.");
        MovieDao movieDao = DaoManager.getMovieDao();
        TheaterDao theaterDao = DaoManager.getTheaterDao();
        ScheduleDao scheduleDao = DaoManager.getScheduleDao();
        createTemp(movieDao, theaterDao, scheduleDao);

        int movieCount = 0;
        int theaterCount = 0;
        int scheduleCount = 0;
        // Store the movie.
        misLogger.info("Insert movies.");
        Map movieMap = new HashMap();
        for (Iterator iterator = movieList.iterator(); iterator.hasNext();) {
        	Movie m = (Movie) iterator.next();
        	try{
        		movieDao.insert(m);
                movieCount++;
                movieMap.put(m.getVendorId(), m);
        	}catch (Exception e) {
        		trimLogger.info("Insert movie error:");
        		trimLogger.info(m.toString() + "\r\n");
			}
        }

        // Store theater.
        misLogger.info("Insert theaters.");
        Map theaterMap = new HashMap();
        for (Iterator iterator = theaterList.iterator(); iterator.hasNext();) {
            Theater t = (Theater) iterator.next();
        	try{
        		theaterDao.insert(t);
                theaterCount++;
                theaterMap.put(t.getVendorId(), t);
        	}catch (Exception e) {
        		trimLogger.info("Insert theater error:");
        		trimLogger.info(t.toString() + "\r\n");
			}
        }

        // Store schedules.
        misLogger.info("Insert schedules.");
        for (Iterator iterator = scheduleList.iterator(); iterator.hasNext();)

        {
            Schedule s = (Schedule) iterator.next();
            // Check theater.
            Theater theater = (Theater) theaterMap.get(s.getTheaterVendorId());
            if (theater == null) {
                continue;
            } else {
                s.setTheaterId(theater.getId());
            }

            // Check movie.
            Movie movie = (Movie) movieMap.get(s.getMovieVendorId());
            if (movie == null) {
                continue;
            } else {
                s.setMovieId(movie.getId());
            }

            // Check whether show time is not available.
            if (s.getShowTime() == null || s.getShowTime().trim().length() == 0) {
                continue;
            }
            try{
            	scheduleDao.insert(s);
                scheduleCount++;
        	}catch (Exception e) {
        		trimLogger.info("Insert schedule error:");
        		trimLogger.info(s.toString() + "\r\n");
			}
        }

        // backup
        // backup(movieDao, theaterDao, scheduleDao);
        // Apply new data
        misLogger.info("Apply new feed from temp table.");
        apply(movieDao, theaterDao, scheduleDao);

        misLogger.info("Feed is finished.");
        misLogger.info("There are " + movieList.size() + " movies in "
                + theaterList.size() + " theaters.");
        Set zipSet = new HashSet();
        for (Iterator iterator = theaterList.iterator(); iterator.hasNext();) {
            Theater t = (Theater) iterator.next();
            zipSet.add(t.getPostalcode());
        }
        misLogger.info("Zip codes of the theaters: " + zipSet);

        if (movieCount <= 0 || theaterCount <= 0 || scheduleCount <= 0) {
            return false;
        }
        return true;
    }
    
	private void checkDataLengthForDB(List movieList, List theaterList,
			List scheduleList) {
		for (Iterator iterator = movieList.iterator(); iterator.hasNext();) {
			checkDataLengthForMovie((Movie) iterator.next());
		}

		for (Iterator iterator = theaterList.iterator(); iterator.hasNext();) {
			checkDataLengthForTheater((Theater) iterator.next());
		}

		for (Iterator iterator = scheduleList.iterator(); iterator.hasNext();)

		{
			checkDataLengthForSchedule((Schedule) iterator.next());
		}
	}
    
    private void checkDataLengthForMovie(Movie movie) {
		int movieName = Integer.parseInt(data_length_config
				.getString("movie.name"));
		int movieCast = Integer.parseInt(data_length_config
				.getString("movie.cast"));
		int movieDirector = Integer.parseInt(data_length_config
				.getString("movie.director"));
		int movieGrade = Integer.parseInt(data_length_config
				.getString("movie.grade"));
		int movieGenres = Integer.parseInt(data_length_config
				.getString("movie.genres"));
		int movieDescription = Integer.parseInt(data_length_config
				.getString("movie.description"));
		int movieRunTime = Integer.parseInt(data_length_config
				.getString("movie.runTime"));
		int movieSmallImage = Integer.parseInt(data_length_config
				.getString("movie.smallImage"));
		int movieBigImage = Integer.parseInt(data_length_config
				.getString("movie.bigImage"));
		int movieAltFilmId = Integer.parseInt(data_length_config
				.getString("movie.altFilmId"));
		boolean flag = false;
		StringBuffer movieBuffer = new StringBuffer("Movie[vendorId = "
				+ movie.getVendorId() + "] data exceed limit:");
		if (movie.getName() != null && movie.getName().length() > movieName) {
			movieBuffer.append("\r\n");
			movieBuffer.append("[name]: " + movie.getName());
			movie.setName(movie.getName().substring(0, movieName - 1));
			flag = true;
		}
		if (movie.getCast() != null && movie.getCast().length() > movieCast) {
			movieBuffer.append("\r\n");
			movieBuffer.append("[cast]: " + movie.getCast());
			movie.setCast(movie.getCast().substring(0, movieCast - 1));
			flag = true;
		}
		if (movie.getDirector() != null && movie.getDirector().length() > movieDirector) {
			movieBuffer.append("\r\n");
			movieBuffer.append("[director]: " + movie.getDirector());
			movie.setDirector(movie.getDirector().substring(0,
					movieDirector - 1));
			flag = true;
		}
		if (movie.getGrade() != null && movie.getGrade().length() > movieGrade) {
			movieBuffer.append("\r\n");
			movieBuffer.append("[grade]: " + movie.getGrade());
			movie.setGrade(movie.getGrade().substring(0, movieGrade - 1));
			flag = true;
		}
		if (movie.getGenres() != null && movie.getGenres().length() > movieGenres) {
			movieBuffer.append("\r\n");
			movieBuffer.append("[genres]: " + movie.getGenres());
			movie.setGenres(movie.getGenres().substring(0, movieGenres - 1));
			flag = true;
		}
		if (movie.getDescription() != null && movie.getDescription().length() > movieDescription) {
			movieBuffer.append("\r\n");
			movieBuffer.append("[description]: " + movie.getDescription());
			movie.setDescription(movie.getDescription().substring(0,
					movieDescription - 1));
			flag = true;
		}
		if (movie.getRunTime() != null && movie.getRunTime().length() > movieRunTime) {
			movieBuffer.append("\r\n");
			movieBuffer.append("[runTime]: " + movie.getRunTime());
			movie.setRunTime(movie.getRunTime().substring(0, movieRunTime - 1));
			flag = true;
		}
		if (movie.getSmallImage() != null && movie.getSmallImage().length() > movieSmallImage) {
			movieBuffer.append("\r\n");
			movieBuffer.append("[smallImage]: " + movie.getSmallImage());
			movie.setSmallImage(movie.getSmallImage().substring(0,
					movieSmallImage - 1));
			flag = true;
		}
		if (movie.getBigImage() != null && movie.getBigImage().length() > movieBigImage) {
			movieBuffer.append("\r\n");
			movieBuffer.append("[bigImage]: " + movie.getBigImage());
			movie.setBigImage(movie.getBigImage().substring(0,
					movieBigImage - 1));
			flag = true;
		}
		if (movie.getAltFilmId() != null && movie.getAltFilmId().length() > movieAltFilmId) {
			movieBuffer.append("\r\n");
			movieBuffer.append("[altFilmId]: " + movie.getAltFilmId());
			movie.setAltFilmId(movie.getAltFilmId()
					.substring(0, movieAltFilmId));
			flag = true;
		}

		if (flag) {
			trimLogger.info(movieBuffer.toString() + "\r\n");
		}
	}

	private void checkDataLengthForTheater(Theater theater) {
		int theaterName = Integer.parseInt(data_length_config
				.getString("theater.name"));
		int theaterStreet = Integer.parseInt(data_length_config
				.getString("theater.street"));
		int theaterCity = Integer.parseInt(data_length_config
				.getString("theater.city"));
		int theaterState = Integer.parseInt(data_length_config
				.getString("theater.state"));
		int theaterCountry = Integer.parseInt(data_length_config
				.getString("theater.country"));
		int theaterPostalcode = Integer.parseInt(data_length_config
				.getString("theater.postalcode"));
		int theaterTelephone = Integer.parseInt(data_length_config
				.getString("theater.telephone"));
		boolean flag = false;
		StringBuffer theaterBuffer = new StringBuffer("Theater[vendorId = "
				+ theater.getVendorId() + "] data exceed limit:");
		if (theater.getName() != null && theater.getName().length() > theaterName) {
			theaterBuffer.append("\r\n");
			theaterBuffer.append("[name]: " + theater.getName());
			theater.setName(theater.getName().substring(0, theaterName));
			flag = true;
		}
		if (theater.getStreet() != null && theater.getStreet().length() > theaterStreet) {
			theaterBuffer.append("\r\n");
			theaterBuffer.append("[street]: " + theater.getStreet());
			theater.setStreet(theater.getStreet().substring(0,
					theaterStreet - 1));
			flag = true;
		}
		if (theater.getCity() != null && theater.getCity().length() > theaterCity) {
			theaterBuffer.append("\r\n");
			theaterBuffer.append("[city]: " + theater.getCity());
			theater.setCity(theater.getCity().substring(0, theaterCity - 1));
			flag = true;
		}
		if (theater.getState() != null && theater.getState().length() > theaterState) {
			theaterBuffer.append("\r\n");
			theaterBuffer.append("[state]: " + theater.getState());
			theater.setState(theater.getState().substring(0, theaterState - 1));
			flag = true;
		}
		if (theater.getCountry() != null && theater.getCountry().length() > theaterCountry) {
			theaterBuffer.append("\r\n");
			theaterBuffer.append("[country]: " + theater.getCountry());
			theater.setCountry(theater.getCountry().substring(0,
					theaterCountry - 1));
			flag = true;
		}
		if (theater.getPostalcode() != null && theater.getPostalcode().length() > theaterPostalcode) {
			theaterBuffer.append("\r\n");
			theaterBuffer.append("[postalcode]: " + theater.getPostalcode());
			theater.setPostalcode(theater.getPostalcode().substring(0,
					theaterPostalcode - 1));
			flag = true;
		}
		if (theater.getTelephone() != null && theater.getTelephone().length() > theaterTelephone) {
			theaterBuffer.append("\r\n");
			theaterBuffer.append("[telephone]: " + theater.getTelephone());
			theater.setTelephone(theater.getTelephone().substring(0,
					theaterTelephone - 1));
			flag = true;
		}

		if (flag) {
			trimLogger.info(theaterBuffer.toString() + "\r\n");
		}
	}

	private void checkDataLengthForSchedule(Schedule schedule) {
		int scheduleShowTime = Integer.parseInt(data_length_config
				.getString("schedule.showTime"));
		int scheduleVendorName = Integer.parseInt(data_length_config
				.getString("schedule.vendorName"));
		int scheduleQuals = Integer.parseInt(data_length_config
				.getString("schedule.quals"));
		int scheduleTicketURI = Integer.parseInt(data_length_config
				.getString("schedule.ticketURI"));
		boolean flag = false;
		StringBuffer scheduleBuffer = new StringBuffer("Schedule[vendorName = "
				+ schedule.getVendorName() + "] data exceed limit:");
		if (schedule.getShowTime() != null && schedule.getShowTime().length() > scheduleShowTime) {
			scheduleBuffer.append("\r\n");
			scheduleBuffer.append("[showTime]: " + schedule.getShowTime());
			schedule.setShowTime(schedule.getShowTime().substring(0,
					scheduleShowTime - 1));
			flag = true;
		}
		if (schedule.getVendorName() != null && schedule.getVendorName().length() > scheduleVendorName) {
			scheduleBuffer.append("\r\n");
			scheduleBuffer.append("[vendorName]: " + schedule.getVendorName());
			schedule.setVendorName(schedule.getVendorName().substring(0,
					scheduleVendorName - 1));
			flag = true;
		}
		if (schedule.getQuals() != null && schedule.getQuals().length() > scheduleQuals) {
			scheduleBuffer.append("\r\n");
			scheduleBuffer.append("[quals]: " + schedule.getQuals());
			schedule.setQuals(schedule.getQuals().substring(0,
					scheduleQuals - 1));
			flag = true;
		}
		if (schedule.getTicketURI() != null && schedule.getTicketURI().length() > scheduleTicketURI) {
			scheduleBuffer.append("\r\n");
			scheduleBuffer.append("[ticketURI]: " + schedule.getTicketURI());
			schedule.setTicketURI(schedule.getTicketURI().substring(0,
					scheduleTicketURI - 1));
			flag = true;
		}

		if (flag) {
			trimLogger.info(scheduleBuffer.toString() + "\r\n");
		}
	}

    // private void backup(MovieDao movieDao, TheaterDao theaterDao,
    // ScheduleDao scheduleDao) throws SQLException {
    // movieDao.backUp();
    // theaterDao.backUp();
    // scheduleDao.backUp();
    //
    // }

    private void apply(MovieDao movieDao, TheaterDao theaterDao,
            ScheduleDao scheduleDao) throws SQLException {
        movieDao.apply();
        theaterDao.apply();
        scheduleDao.apply();

    }

    private void createTemp(MovieDao movieDao, TheaterDao theaterDao,
            ScheduleDao scheduleDao) throws SQLException {
        movieDao.createTemp();
        scheduleDao.createTemp();
        theaterDao.createTemp();

    }

    public static final void copyInputStream(InputStream in, OutputStream out)
            throws IOException {
        byte[] buffer = new byte[1024];
        int len;

        while ((len = in.read(buffer)) >= 0) {
            out.write(buffer, 0, len);
        }

        in.close();
        out.close();
    }

}
