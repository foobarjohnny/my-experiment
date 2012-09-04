package com.telenav.cserver.movie.html.executor;

public class MovieListRequest extends MovieCommonRequest{

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\naddress:");
		sb.append(this.getAddress().toString());
		sb.append("\nsearchDate:");
		sb.append(this.getSearchDate());
		sb.append("\ndistanceUnit:");
		sb.append(this.getDistanceUnit());
		
		return sb.toString();
	}
}
