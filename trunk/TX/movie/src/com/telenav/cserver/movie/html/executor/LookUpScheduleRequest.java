package com.telenav.cserver.movie.html.executor;

public class LookUpScheduleRequest extends MovieCommonRequest{

	private String[] movieIds;
	private long[] theaterIds;
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\nmovieIds:");
		sb.append(this.getMovieIds());
		sb.append("\nsearchDate:");
		sb.append(this.getSearchDate());
		sb.append("\ntheaterIds:");
		sb.append(this.getTheaterIds());
		
		return sb.toString();
	}

	public String[] getMovieIds() {
		return movieIds;
	}

	public void setMovieIds(String[] movieIds) {
		this.movieIds = movieIds;
	}

	public long[] getTheaterIds() {
		return theaterIds;
	}

	public void setTheaterIds(long[] theaterIds) {
		this.theaterIds = theaterIds;
	}
}
