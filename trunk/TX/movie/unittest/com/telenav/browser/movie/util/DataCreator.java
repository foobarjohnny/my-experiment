package com.telenav.browser.movie.util;

import com.telenav.browser.movie.datatypes.Address;
import com.telenav.browser.movie.executor.MovieSearchRequest;

public class DataCreator {
	public static MovieSearchRequest getMovieSearchRequest(){
		
		MovieSearchRequest movieSearchRequest = new MovieSearchRequest();
		BasicDataFactory basicDataFactory = new BasicDataFactory();
		try {
			int batchNumber = (Integer) basicDataFactory.getObjectFromConfig(FileNameConstant.MOVIE_SEARCH_REQUEST,"batchNumber", Integer.class);
			int batchSize =  (Integer) basicDataFactory.getObjectFromConfig(FileNameConstant.MOVIE_SEARCH_REQUEST, "batchSize", Integer.class);
			int distanceUnit = (Integer) basicDataFactory.getObjectFromConfig(FileNameConstant.MOVIE_SEARCH_REQUEST, "distanceUnit", Integer.class);
			boolean sortByName = (Boolean) basicDataFactory.getObjectFromConfig(FileNameConstant.MOVIE_SEARCH_REQUEST, "sortByName", Boolean.class);
			String searchString = (String) basicDataFactory.getObjectFromConfig(FileNameConstant.MOVIE_SEARCH_REQUEST, "searchString", String.class);
			String newSortBy = (String) basicDataFactory.getObjectFromConfig(FileNameConstant.MOVIE_SEARCH_REQUEST, "newSortBy", String.class);
			String searchDate = (String) basicDataFactory.getObjectFromConfig(FileNameConstant.MOVIE_SEARCH_REQUEST, "searchDate", String.class);
			Address address = (Address) basicDataFactory.getObjectFromConfig(FileNameConstant.COMMON_JSON, "address", Address.class);
			
			
			movieSearchRequest.setBatchNumber(batchNumber);
			movieSearchRequest.setBatchSize(batchSize);
			movieSearchRequest.setDistanceUnit(distanceUnit);
			movieSearchRequest.setSortByName(sortByName);
			movieSearchRequest.setSearchString(searchString);
			movieSearchRequest.setNewSortBy(newSortBy);
			movieSearchRequest.setSearchDate(searchDate);
			movieSearchRequest.setAddress(address);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return movieSearchRequest;
	}
}
