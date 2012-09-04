/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Oct 23, 2008
 * File name: Movie.java
 * Package name: com.telenav.j2me.browser.datatype
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 11:14:32 AM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.datatype;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author lwei (lwei@telenav.cn) 11:14:32 AM, Oct 23, 2008
 */
public class Movie implements Cloneable {

    /** Fields. */
    private long id;

    private String vendorId;

    private String altFilmId;

    private String name;

    private String cast;

    private String director;

    private float rating;

    private String grade;

    private String genres;

    private String description;

    private String runTime;

    private String smallImage;

    private String bigImage;
    
    private Date releaseDate;
    
    private Theater nearestTheater;
    
    private String nearestTheaterShowTime;
    
    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the vendorId
     */
    public String getVendorId() {
        return vendorId;
    }

    /**
     * @param vendorId the vendorId to set
     */
    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    /**
     * @return the altFileId
     */
    public String getAltFilmId() {
        return altFilmId;
    }

    /**
     * @param altFilmId the altFilmId to set
     */
    public void setAltFilmId(String altFilmId) {
        this.altFilmId = altFilmId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the cast
     */
    public String getCast() {
        return cast;
    }

    /**
     * @param cast the cast to set
     */
    public void setCast(String cast) {
        this.cast = cast;
    }

    /**
     * @return the director
     */
    public String getDirector() {
        return director;
    }

    /**
     * @param director the director to set
     */
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     * @return the rating
     */
    public float getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(Float rating) {
        if (rating == null) {
            this.rating = 0;
            return;
        }
        this.rating = rating.floatValue();
    }

    /**
     * @return the grade
     */
    public String getGrade() {
        return grade;
    }

    /**
     * @param grade the grade to set
     */
    public void setGrade(String grade) {
        this.grade = grade;
    }

    /**
     * @return the genres
     */
    public String getGenres() {
        return genres;
    }

    /**
     * @param genres the genres to set
     */
    public void setGenres(String genres) {
        this.genres = genres;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the runTime
     */
    public String getRunTime() {
        return runTime;
    }

    /**
     * @param runTime the runTime to set
     */
    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    /**
     * @return the smallImage
     */
    public String getSmallImage() {
        return smallImage;
    }

    /**
     * @param smallImage the smallImage to set
     */
    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    /**
     * @return the bigImage
     */
    public String getBigImage() {
        return bigImage;
    }

    /**
     * @param bigImage the bigImage to set
     */
    public void setBigImage(String bigImage) {
        this.bigImage = bigImage;
    }

    /**
     * Low clone because there're no need in MOVIES project.
     * 
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        Movie o = null;
        try {
            o = (Movie) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Movie================START\n");

        buffer.append("id:" + this.getId() + "\n");
        buffer.append("vendorId:" + this.getVendorId() + "\n");
        buffer.append("altFilmId:" + this.getAltFilmId() + "\n");
        buffer.append("name:" + this.getName() + "\n");
        buffer.append("cast:" + this.getCast() + "\n");
        buffer.append("director:" + this.getDirector() + "\n");
        buffer.append("description:" + this.getDescription() + "\n");
        buffer.append("genres:" + this.getGenres() + "\n");
        buffer.append("grade:" + this.getGrade() + "\n");
        buffer.append("rating:" + this.getRating() + "\n");
        buffer.append("runTime:" + this.getRunTime() + "\n");
        buffer.append("bigImage:" + this.getBigImage() + "\n");
        buffer.append("smallImage:" + this.getSmallImage() + "\n");
        buffer.append("releaseDate:" + this.getReleaseDate() + "\n");
        
        buffer.append("Movie================OVER\n");

        return buffer.toString();

    }

	public Theater getNearestTheater() {
		return nearestTheater;
	}

	public void setNearestTheater(Theater nearestTheater) {
		this.nearestTheater = nearestTheater;
	}

	public Date getReleaseDate() {
		//set one default realse date
		if(releaseDate == null)
		{
			try
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
				releaseDate = dateFormat.parse("1980-01-01");
			}
			catch(Exception e)
			{
			}
		}
		
		
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getNearestTheaterShowTime() {
		return nearestTheaterShowTime;
	}

	public void setNearestTheaterShowTime(String nearestTheaterShowTime) {
		this.nearestTheaterShowTime = nearestTheaterShowTime;
	}
}
