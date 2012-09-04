package com.telenav.browser.movie.executor;

import org.apache.log4j.Logger;

import com.telenav.browser.movie.Constant;
import com.telenav.browser.movie.Util;
import com.telenav.browser.movie.datatypes.Address;
import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.datatypes.content.movie.v10.Area;
import com.telenav.datatypes.content.movie.v10.MovieListingWithDetailTheaterInfo;
import com.telenav.datatypes.content.movie.v10.MovieSearchDate;
import com.telenav.datatypes.content.movie.v10.MovieSortTypeEnum;
import com.telenav.services.content.moviesearchservice.v10.MovieSearchRequestDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieSearchServiceStub;
import com.telenav.services.content.moviesearchservice.v10.MovieWithDetailTheaterInfoServiceResponseDTO;
import com.telenav.ws.datatypes.services.ResponseStatus;

public class MovieSearchExecutor extends AbstractExecutor{
	private static Logger logger = Logger.getLogger(MovieSearchExecutor.class);
	
    public void doExecute(ExecutorRequest req, ExecutorResponse resp,
            ExecutorContext context) throws ExecutorException {

        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("MovieSearchExecutor");
        // Get the request and response
        MovieSearchRequest movieRequest = (MovieSearchRequest) req;
        MovieSearchResponse movieResponse = (MovieSearchResponse) resp;
        
    	Address address = movieRequest.getAddress();
        
        MovieSearchRequestDTO sMovie = new MovieSearchRequestDTO();
        sMovie.setKeyword(movieRequest.getSearchString());
        MovieSearchDate searchDate = Util.getSearchDate(movieRequest.getSearchDate()); 
        sMovie.setDate(searchDate);
        Area area = Util.getArea(address.lat, address.lon, Constant.SEARCH_RADIUS);
        sMovie.setArea(area);
        sMovie.setPageLength(movieRequest.getBatchSize());
        sMovie.setPageNumber(movieRequest.getBatchNumber());
        
		String newSortBy = movieRequest.getNewSortBy();
		if (newSortBy != null && !newSortBy.equals("")) {
			if (newSortBy.equals(Constant.SORT_BY_NAME)) {
				sMovie.setSortType(MovieSortTypeEnum.ALPHABET);
			} else if (newSortBy.equals(Constant.SORT_BY_RANK)) {
				sMovie.setSortType(MovieSortTypeEnum.RANK);
			} else if (newSortBy.equals(Constant.SORT_BY_RELEASE_DATE)) {
				sMovie.setSortType(MovieSortTypeEnum.RELEASE_DATE);
			} else if (newSortBy.equals(Constant.SORT_BY_WEEKEND_GROSS)) {
				sMovie.setSortType(MovieSortTypeEnum.BOX_OFFICE_WEEKEND_GROSS);
			}
		} else {
			if (movieRequest.isSortByName())
				sMovie.setSortType(MovieSortTypeEnum.ALPHABET);
			else
				sMovie.setSortType(MovieSortTypeEnum.RANK);
		}
        
		//System.out.println("smovie name:" + sMovie.getKeyword());
		//System.out.println("smovie date:" + sMovie.getDate().getDay() + "-" + sMovie.getDate().getMonth() + "-" + sMovie.getDate().getYear() );
        //System.out.println("smovie Sort by :" + sMovie.getSortType());
        //System.out.println("smovie get page number:" + sMovie.getPageNumber());
        //System.out.println("smovie get page length:" + sMovie.getPageLength());
        //System.out.println("MovieSearchExecutor.. lat=" + address.lat + " lon=" + address.lon);
        Util.setClientProps(sMovie);
        MovieSearchServiceStub stub = null;
        try{
        	stub = Util.getService();
        	MovieWithDetailTheaterInfoServiceResponseDTO mResp = stub.searchMoviesWithDetailTheaterInfo(sMovie);
        	ResponseStatus status = mResp.getResponseStatus();
        	logger.debug("Status code:" + status.getStatusCode() + " Message:" + status.getStatusMessage());
        	MovieListingWithDetailTheaterInfo[] mList = mResp.getMovieListing();
        	if (mList != null){
	        	
	            movieResponse.movies = mList;
        	}else{
                movieResponse.setStatus(ExecutorResponse.STATUS_OK);
                return;
        	}
        	
	        movieResponse.lat = address.lat/Constant.DEGREE_MULTIPLIER;
	        movieResponse.lon = address.lon/Constant.DEGREE_MULTIPLIER;
	        movieResponse.dUnit = movieRequest.getDistanceUnit();
	        

        	
        }catch(Exception ex){
            cli.setState(CliTransaction.STATUS_FAIL);
            cli.setStatus(ex);
            logger.error("COSE is not available now");
        	logger.error("cause:" + ex.getCause());
        	logger.error("error message:" + ex.getMessage());
            movieResponse.setErrorMessage("COSE.notAvailable");
            movieResponse.setStatus(ExecutorResponse.STATUS_FAIL);
            return;
        }finally {
            cli.complete();
			WebServiceUtils.cleanupStub(stub);
        }
        
        
        // TODO remove when COSE is up and running
        //movieResponse.batchNumber = movieRequest.getBatchNumber();
        //movieResponse.batchSize = movieRequest.getBatchSize();
        movieResponse.setStatus(ExecutorResponse.STATUS_OK);
    }
}
