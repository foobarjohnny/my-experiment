/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.telenav.cserver.backend.datatypes.contents.ReviewOption;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.html.datatypes.PoiReview;
import com.telenav.cserver.html.datatypes.TripAdvisor;
import com.telenav.cserver.html.datatypes.YelpReview;

/**
 * @TODO	Define the response Object
 * @author	jhjin@telenav.cn
 * @version 1.0 Jan 26, 2011
 */
public class HtmlPoiReviewResponse extends ExecutorResponse
{
	private String operateType;
    private List<PoiReview> poiReviewList = new ArrayList<PoiReview>();
    private String poiId;
    private int rating;
    private int rateNumber;
    
    //for add review screen
    private List<ReviewOption> reviewOptions;

    private YelpReview yelpReview;
    private TripAdvisor tripAdvisor;

    public List<PoiReview> getPoiReviewList()
    {
        return poiReviewList;
    }

    public void addPoiReview(PoiReview poiView)
    {
        this.poiReviewList.add(poiView);
    }
    
    public String getPoiId()
    {
        return poiId;
    }

    public void setPoiId(String poiId)
    {
        this.poiId = poiId;
    }

    public void setPoiReviewList(List<PoiReview> poiReviewList)
    {
        this.poiReviewList = poiReviewList;
    }

//    public int getRateNumber()
//    {
//        return this.poiReviewList.size();
//    }
//    
//    public double getRating()
//    {
//        double total = 0;
//        Iterator<PoiReview> iterator = poiReviewList.iterator();
//        while(iterator.hasNext())
//        {
//            PoiReview poiReview = iterator.next();
//            total += poiReview.getRating();
//        }
//        return total/getRateNumber();
//    }
    
    public Map<ReviewOption,Integer> getReviewOptionsStatistic()
    {
        Map<String,Integer> statistic = new LinkedHashMap<String,Integer>();
        Iterator<PoiReview> reviews = poiReviewList.iterator();
        while(reviews.hasNext())
        {
            PoiReview poiReview = reviews.next();
            Map<String, String> reviewOptions = poiReview.getReviewOptions();
            Map<String, String> newReviewOptions = new LinkedHashMap<String,String>();
            Iterator<String> keys = reviewOptions.keySet().iterator();
            while(keys.hasNext())
            {
                String key = keys.next();
                String value = reviewOptions.get(key);
                String keyWithSuffix = "";
                if( "1".equals(value) ){
                    keyWithSuffix = key + "-1";
                }else{
                    keyWithSuffix = key + "-0";
                }
                if( statistic.get(keyWithSuffix) == null )
                {
                    statistic.put(keyWithSuffix, 1);
                }
                else
                {
                    statistic.put(keyWithSuffix, statistic.get(keyWithSuffix)+1);
                }
                
                newReviewOptions.put(getReviewOptionNameById(key), value);
            }
            poiReview.setReviewOptions(newReviewOptions);
        }
        //transform structure
        Iterator<Entry<String,Integer>> statisticIterator = statistic.entrySet().iterator();
        Map<ReviewOption,Integer> newStatistic = new LinkedHashMap<ReviewOption,Integer>(statistic.size());
        while(statisticIterator.hasNext()){
            Entry<String,Integer> entry = statisticIterator.next();
            String keyWithSuffix = entry.getKey();
            String key = keyWithSuffix.substring(0, keyWithSuffix.length()-2);
            
            ReviewOption reviewOption = new ReviewOption();
            reviewOption.setName(getReviewOptionNameById(key));
            if(keyWithSuffix.endsWith("-1")){
                reviewOption.setValue("1");
            }else{
                reviewOption.setValue("0");
            }
            newStatistic.put(reviewOption, statistic.get(keyWithSuffix));
        }
        
        //get the top 3
        Map<ReviewOption,Integer> sortedStatistic = new LinkedHashMap<ReviewOption,Integer>(newStatistic.size());
        int length = newStatistic.size();
        for(int i=0; i<length && i<3; i++){
            Iterator<Entry<ReviewOption,Integer>> newStatisticIterator = newStatistic.entrySet().iterator();
            ReviewOption biggest = null;
            while(newStatisticIterator.hasNext()){
                Entry<ReviewOption,Integer> entry = newStatisticIterator.next();
                ReviewOption key = entry.getKey();
                if( biggest == null ){
                    biggest = key;
                    continue;
                }else{
                    if( entry.getValue() > newStatistic.get(biggest)){
                        biggest = key;
                    }
                }
            }
            sortedStatistic.put(biggest, newStatistic.get(biggest));
            newStatistic.remove(biggest);
        }
           
        return sortedStatistic;
    }
    
    private String getReviewOptionNameById(String optionId){
    	String optionName = "";
    	for(ReviewOption option : reviewOptions){
    		if((option.getId()+"").equalsIgnoreCase(optionId)){
    			optionName = option.getName();
    			break;
    		}
    	}
    	return optionName;
    }

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getRateNumber() {
		return rateNumber;
	}

	public void setRateNumber(int rateNumber) {
		this.rateNumber = rateNumber;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public List<ReviewOption> getReviewOptions() {
		return reviewOptions;
	}

	public void setReviewOptions(List<ReviewOption> reviewOptions) {
		this.reviewOptions = reviewOptions;
	}

    public void setYelpReview(YelpReview yelpReview)
    {
        this.yelpReview = yelpReview;
    }
    
    public YelpReview getYelpReview()
    {
        return this.yelpReview;
    }

	public TripAdvisor getTripAdvisor() {
		return tripAdvisor;
	}

	public void setTripAdvisor(TripAdvisor tripAdvisor) {
		this.tripAdvisor = tripAdvisor;
	}
}
