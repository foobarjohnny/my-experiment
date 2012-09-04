package com.telenav.cserver.movie.html.datatypes;

import java.util.List;

public class BookingInfoItem {

	private List<TicketItem> tickets;
	private String theaterId;
	private String movieId;
	private String showDate;
	private String showTime;
	private String confirmEmail;
	private double totalAmout;

	public List<TicketItem> getTickets() {
		return tickets;
	}

	public void setTickets(List<TicketItem> tickets) {
		this.tickets = tickets;
	}

	public String getTheaterId() {
		return theaterId;
	}

	public void setTheaterId(String theaterId) {
		this.theaterId = theaterId;
	}

	public String getMovieId() {
		return movieId;
	}

	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	public String getShowDate() {
		return showDate;
	}

	public void setShowDate(String showDate) {
		this.showDate = showDate;
	}

	public String getShowTime() {
		return showTime;
	}

	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}

	public String getConfirmEmail() {
		return confirmEmail;
	}

	public void setConfirmEmail(String confirmEmail) {
		this.confirmEmail = confirmEmail;
	}

	public double getTotalAmout() {
		return totalAmout;
	}

	public void setTotalAmout(double totalAmout) {
		this.totalAmout = totalAmout;
	}
}
