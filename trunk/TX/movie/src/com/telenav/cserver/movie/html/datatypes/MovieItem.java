package com.telenav.cserver.movie.html.datatypes;

import java.util.ArrayList;
import java.util.List;

public class MovieItem {

	private String id;
	private String name;
	private String grade;
	private double rating;
	private String[] cast;
	private String runtime;
	private String description;
	private String director;
	private String genres;
	private String vendorId;
	private String tomatoRating;
	private String tomatoRatingUrl;
	private int tomatoRatingStatus;
	private String image;
	private int imageHeight;
	private int imageWidth;
	private String theaterId;
	
	//schedule and theaterList is only used when search from theater
	private ScheduleItem scheduleItem;
	private List<TheaterItem> theaterList = new ArrayList<TheaterItem>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getRuntime() {
		return runtime;
	}
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
	public String[] getCast() {
		return cast;
	}
	public void setCast(String[] cast) {
		this.cast = cast;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getGenres() {
		return genres;
	}
	public void setGenres(String genres) {
		this.genres = genres;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getTomatoRating() {
		return tomatoRating;
	}
	public void setTomatoRating(String tomatoRating) {
		this.tomatoRating = tomatoRating;
	}

	public List<TheaterItem> getTheaterList() {
		return theaterList;
	}
	public void setTheaterList(List<TheaterItem> theaterList) {
		this.theaterList = theaterList;
	}
	
	public long[] getTheaterIds()
	{
		long[] theaterIds = new long[theaterList.size()];
		for(int i=0; i< theaterList.size() ; i++)
		{
			theaterIds[i] = theaterList.get(i).getId();
		}
		
		return theaterIds;
	}
	public ScheduleItem getScheduleItem() {
		return scheduleItem;
	}
	public void setScheduleItem(ScheduleItem scheduleItem) {
		this.scheduleItem = scheduleItem;
	}
	
    public String getImage()
    {
        return image;
    }
    public void setImage(String image)
    {
        this.image = image;
    }
    public int getImageHeight()
    {
        return imageHeight;
    }
    public void setImageHeight(int imageHeight)
    {
        this.imageHeight = imageHeight;
    }
    public int getImageWidth()
    {
        return imageWidth;
    }
    public void setImageWidth(int imageWidth)
    {
        this.imageWidth = imageWidth;
    }
    
	public String getTheaterId() {
		return theaterId;
	}
	public void setTheaterId(String theaterId) {
		this.theaterId = theaterId;
	}
	public String getTomatoRatingUrl() {
		return tomatoRatingUrl;
	}
	public void setTomatoRatingUrl(String tomatoRatingUrl) {
		this.tomatoRatingUrl = tomatoRatingUrl;
	}

	public int getTomatoRatingStatus() {
		return tomatoRatingStatus;
	}
	public void setTomatoRatingStatus(int tomatoRatingStatus) {
		this.tomatoRatingStatus = tomatoRatingStatus;
	}
    
    
}
