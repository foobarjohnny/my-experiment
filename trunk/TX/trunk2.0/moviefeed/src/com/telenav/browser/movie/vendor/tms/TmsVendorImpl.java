/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Nov 28, 2008
 * File name: TMSVendorImpl.java
 * Package name: com.telenav.j2me.browser.vendor.TMS
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 11:19:03 AM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.vendor.tms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import com.telenav.browser.movie.datatype.Movie;
import com.telenav.browser.movie.datatype.MovieImage;
import com.telenav.browser.movie.util.MovieUtil;
import com.telenav.browser.movie.util.TimeoutSocketFactory;
import com.telenav.browser.movie.vendor.Vendor;
import com.televigation.log.TVCategory;

/**
 * @author lwei (lwei@telenav.cn) 11:19:03 AM
 */
public class TmsVendorImpl implements Vendor {
    private static final Logger misLogger = (Logger) TVCategory
            .getInstance("movie.feed.mis");

    /** Feed finished successfully. */
    private boolean feedFinished = false;

    /** Initialized successful. */
    private boolean initialized = false;

    /** Configuration of TMS. */
    private TmsConfig config = null;

    /** XML file name list. */
    private List xmlFileNameList;

    /** Movie list. */
    private List movieList;

    /** Theater list. */
    private List theaterList;

    /** Schedule list. */
    private List scheduleList;
    
    public static final String GZ_SUFFIX = ".gz";

    /*
     * (non-Javadoc)
     * 
     * @see com.telenav.j2me.browser.vendor.Vendor#downloadImage(java.util.List)
     */
    public List downloadImage(List movieList) throws IllegalAccessException {
        List result = new ArrayList();

        if (movieList != null && movieList.size() > 0) {
            misLogger.info("Start downloading missing pictures from server.");
        } else {
            misLogger.info("No missing pictures.");
            return result;
        }

        // Start download.
        TimeoutSocketFactory timeoutSocketFactory = new TimeoutSocketFactory(
                config.getEstablishTimeout());
        FTPClient ftp = new FTPClient();
        ftp.setSocketFactory(timeoutSocketFactory);
        ftp.setDefaultTimeout(config.getDefaultTimeout());
        ftp.setReaderThread(false);

        try {
            // Connect.
            ftp.connect(config.getHost());

            ftp.setSoTimeout(config.getDefaultTimeout());

            int replyCode = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                clearImageInfo(movieList);
                return result;
            }

            // Login.
            ftp.setTcpNoDelay(true);
            boolean login = ftp
                    .login(config.getAccount(), config.getPassword());
            if (!login) {
                clearImageInfo(movieList);
                return result;
            }

            // Change the ftp settings.
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            // Get remote files.
            for (int i = movieList.size() - 1; i >= 0; i--) {
                // Get file name.
                Movie movie = (Movie) movieList.get(i);
                misLogger.info("Downloading pictures for movie "
                        + movie.getName());
                String bigRemote = config.getImageRemotePath() + "/"
                        + movie.getBigImage();
                String smallRemote = config.getImageRemotePath() + "/"
                        + movie.getSmallImage();
                String bigLocal = config.getDataLocalPath() + "/"
                        + this.getVenderName() + "/images/"
                        + movie.getBigImage();
                String smallLocal = config.getDataLocalPath() + "/"
                        + this.getVenderName() + "/images/"
                        + movie.getSmallImage();

                // Download big.
                if (movie.getBigImage() != null) {

                    // Check the temp folder first.
                    byte[] bigData = MovieUtil.fileToImageData(bigLocal);
                    if (bigData != null) {
                        FTPFile[] listFiles = ftp.listFiles(bigRemote);
                        if (listFiles.length > 0) {
                            long remoteSize = listFiles[0].getSize();
                            if (remoteSize != bigData.length) {
                                bigData = null;
                            } else {
                                misLogger.info("Use image downloaded before");
                            }
                        }
                    }

                    // Down load.
                    if (bigData == null) {
                        OutputStream bigOut = getFileOutputStream(bigLocal);
                        ftp.retrieveFile(bigRemote, bigOut);
                        bigOut.close();
                        bigData = MovieUtil.fileToImageData(bigLocal);
                    }

                    // Save the result.
                    if (bigData != null) {
                        MovieImage mi = new MovieImage();
                        mi.setKey(movie.getBigImage());
                        mi.setData(bigData);
                        mi.setBig(true);
                        result.add(mi);
                    } else {
                        movie.setBigImage(null);
                    }
                }

                // Download small.
                if (movie.getSmallImage() != null) {

                    // Check the temp folder first.
                    byte[] smallData = MovieUtil.fileToImageData(smallLocal);
                    if (smallData != null) {
                        FTPFile[] listFiles = ftp.listFiles(smallRemote);
                        if (listFiles.length > 0) {
                            long remoteSize = listFiles[0].getSize();
                            if (remoteSize != smallData.length) {
                                smallData = null;
                            } else {
                                misLogger.info("Use image downloaded before");
                            }
                        }
                    }

                    // Down load.
                    if (smallData == null) {
                        OutputStream smallOut = getFileOutputStream(smallLocal);
                        ftp.retrieveFile(smallRemote, smallOut);
                        smallOut.close();
                        smallData = MovieUtil.fileToImageData(smallLocal);
                    }

                    // Save the result.
                    if (smallData != null) {
                        MovieImage mi = new MovieImage();
                        mi.setKey(movie.getSmallImage());
                        mi.setData(smallData);
                        mi.setBig(false);
                        result.add(mi);
                    } else {
                        movie.setSmallImage(null);
                    }
                }

                // Remove from the list.
                movieList.remove(i);
            }

            misLogger.info("Downloading picture finished.");
            ftp.logout();
            ftp.disconnect();
        } catch (Throwable e) {
            misLogger
                    .error(
                            "Exception while downloading pic file. This will not block the feed.",
                            e);
            clearImageInfo(movieList);
        } finally {
            if (ftp != null && ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    // do nothing
                }
            }
        }

        return result;
    }

    private void clearImageInfo(List movieList) {
        for (Iterator iterator = movieList.iterator(); iterator.hasNext();) {
            Movie movie = (Movie) iterator.next();
            movie.setBigImage(null);
            movie.setSmallImage(null);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.telenav.j2me.browser.vendor.Vendor#generateWAPLink()
     */
    public String generateWAPLink() throws IllegalAccessException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.telenav.j2me.browser.vendor.Vendor#getMovieList()
     */
    public List getMovieList() throws IllegalAccessException {
        if (!feedFinished) {
            throw new IllegalAccessException("Feed is not finished yet!");
        }
        if (movieList != null) {
            return movieList;
        }

        for (Iterator iterator = xmlFileNameList.iterator(); iterator.hasNext();) {
            String name = (String) iterator.next();

            if (name != null
                    && name.indexOf(config.getProgramFileInitial()) >= 0) {
                misLogger.info("Now start to parse " + name);
                TmsMovieParser p = new TmsMovieParser();
                movieList = p.getMovies(name);
                misLogger.info("Now finish parsing " + name);
                return movieList;
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.telenav.j2me.browser.vendor.Vendor#getScheduleList()
     */
    public List getScheduleList() throws IllegalAccessException {
        if (!feedFinished) {
            throw new IllegalAccessException("Feed is not finished yet!");
        }
        if (scheduleList != null) {
            return scheduleList;
        }

        for (Iterator iterator = xmlFileNameList.iterator(); iterator.hasNext();) {
            String name = (String) iterator.next();

            if (name != null
                    && name.indexOf(config.getScheduleFileInitial()) > 0) {
                misLogger.info("Now start to parse " + name);
                TmsScheduleParser p = new TmsScheduleParser();
                scheduleList = p.getSchedules(name);
                misLogger.info("Now finish parsing " + name);
                return scheduleList;
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.telenav.j2me.browser.vendor.Vendor#getTheaterList()
     */
    public List getTheaterList() throws IllegalAccessException {
        if (!feedFinished) {
            throw new IllegalAccessException("Feed is not finished yet!");
        }
        if (theaterList != null) {
            return theaterList;
        }

        for (Iterator iterator = xmlFileNameList.iterator(); iterator.hasNext();) {
            String name = (String) iterator.next();

            if (name != null
                    && name.indexOf(config.getTheaterFileInitial()) > 0) {
                misLogger.info("Now start to parse " + name);
                TmsTheaterParser p = new TmsTheaterParser();
                theaterList = p.getTheaters(name);
                misLogger.info("Now finish parsing " + name);
                return theaterList;
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.telenav.j2me.browser.vendor.Vendor#initialize()
     */
    public boolean initialize() {
        misLogger.info("Initializing TMS vendor.");
        TmsConfig config = new TmsConfig();
        boolean loadConfig = config.loadConfig();
        if (loadConfig) {
            this.config = config;
            this.initialized = true;
            return true;
        } else {
            this.config = null;
            this.initialized = false;
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.telenav.j2me.browser.vendor.Vendor#loadFeed()
     */
    public boolean loadFeed() throws IllegalAccessException {
        if (!initialized) {
            throw new IllegalAccessException(
                    "Please initialize the vendor first.");
        }

        misLogger.info("Start getting the feed from remote server.");
        TimeoutSocketFactory timeoutSocketFactory = new TimeoutSocketFactory(
                config.getEstablishTimeout());
        FTPClient ftp = new FTPClient();
        ftp.setSocketFactory(timeoutSocketFactory);
        ftp.setDefaultTimeout(config.getDefaultTimeout());
        ftp.setReaderThread(false);

        try {
            // Connect.
            ftp.connect(config.getHost());
            ftp.setSoTimeout(config.getDefaultTimeout());

            // Check the reply code.
            int replyCode = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                return false;
            }

            // Login.
            ftp.setTcpNoDelay(true);
            boolean login = ftp
                    .login(config.getAccount(), config.getPassword());
            if (!login) {
                return false;
            }

            // Change the ftp settings.
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            // Get remote files.
            List localFileNameList = new ArrayList();
            ftp.changeWorkingDirectory(config.getDataRemotePath());
            FTPFile[] listFiles = ftp.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                FTPFile file = listFiles[i];
				if (file == null || file.isDirectory() || !isGzFile(file.getName())) {
                    continue;
                }

                String localFileName = config.getDataLocalPath() + "/"
                        + this.getVenderName() + "/" + file.getName();

                // Skip existing files.
                File localFile = new File(localFileName);
                if (localFile.exists()) {
                    misLogger.info("File has existed : "
                            + localFile.getAbsolutePath());

                    misLogger.info("Delete  file : "
                            + localFile.getAbsolutePath());

                    localFile.delete();
                    misLogger.info("Delete successfully, now update file");
                }

                // Download.
                misLogger.info("Downloading file: " + localFileName);
                localFileNameList.add(localFileName);
                OutputStream local = getFileOutputStream(localFileName);
                boolean retrieveFile = ftp.retrieveFile(file.getName(), local);
                local.flush();
                local.close();
                if (!retrieveFile) {
                    return false;
                }
            }

            ftp.logout();
            ftp.disconnect();

            // Sleep for 2 sec before unzip.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {		
            }

            // Unzip.
            List result = new ArrayList();
            xmlFileNameList = new ArrayList();
            for (Iterator iterator = localFileNameList.iterator(); iterator
                    .hasNext();) {
                String name = (String) iterator.next();
                GZIPInputStream gz = new GZIPInputStream(new FileInputStream(
                        name));
                String xmlFileName = name.substring(0, name.length() - 3);
                result.add(xmlFileName);

                // Unzip
                OutputStream o = new FileOutputStream(xmlFileName);
                copyInputStream(gz, o);

                xmlFileNameList.add(xmlFileName);
            }
            // Sort files by name to guarantee to load newest data file.
            xmlFileNameList = MovieUtil.sortFiles(xmlFileNameList);

            feedFinished = true;
            return true;
        } catch (IOException e) {
            misLogger.error("Failed to download feed", e);
        } finally {
            if (ftp != null && ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    // do nothing
                }
            }
        }

        xmlFileNameList = null;
        feedFinished = false;
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.telenav.j2me.browser.vendor.Vendor#getVenderName()
     */
    public String getVenderName() {
        return "TMS";
    }

    private OutputStream getFileOutputStream(String file) throws IOException {
        File f = new File(file);
        File folder = new File(f.getParent());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return new FileOutputStream(file);
    }
    
    public static boolean isGzFile(String zipFile) {
        return zipFile.endsWith(GZ_SUFFIX);
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
