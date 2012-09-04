package com.telenav.browser.movie.executor;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.j2me.datatypes.Stop;

public class ShareMovieRequest extends ExecutorRequest{
	private String movieName;
	private String theaterName;
	private Stop address;
	private String[] recipients;
	private long userId;
	private String ptn;
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String[] getRecipients() {
		return recipients;
	}
	public void setRecipients(String[] recipients) {
		this.recipients = recipients;
	}
	public String getMovieName() {
		return movieName;
	}
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
	public String getTheaterName() {
		return theaterName;
	}
	public void setTheaterName(String theaterName) {
		this.theaterName = theaterName;
	}
	
	public String toString() {
		String addressStr = address != null ? address.toString() : "";
		StringBuffer recipientsBuffer = new StringBuffer("");
		if (recipients != null && recipients.length != 0) {
			for (String recipient : recipients) {
				if ("".equals(recipientsBuffer.toString())) {
					recipientsBuffer.append(recipient);
				} else {
					recipientsBuffer.append(", " + recipient);
				}
			}
		}

		return "[movieName] = " + movieName + "; [theaterName] = "
				+ theaterName + "; [address] = " + addressStr
				+ "; [recipients] = " + recipients.toString()
				+ "; [userId] = " + userId;
	}
	public Stop getAddress() {
		return address;
	}
	public void setAddress(Stop address) {
		this.address = address;
	}
	public String getPtn() {
		return ptn;
	}
	public void setPtn(String ptn) {
		this.ptn = ptn;
	}
}
