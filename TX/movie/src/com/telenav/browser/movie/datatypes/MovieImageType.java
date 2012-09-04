package com.telenav.browser.movie.datatypes;

import com.telenav.datatypes.content.movie.v10.MovieImageTypeEnum;
import com.telenav.tnbrowser.util.DataHandler;


public enum MovieImageType {
/**
 *  movie search service(backend) provide "getMovieImage" method to get images, 
 *  when client send the request, they should pass movieId, width, height, ImageType(small or big),
 *  however, the paremeters for these are fixed, there're 4 types currently, which are: 
 *
 *  height:150 width:225 type:BIG_IMAGE 
 *  height:150 width:100 type:BIG_IMAGE  
 *  height:120 width:180 type:SMALL_IMAGE 
 *  height:120 width:80 type:SMALL_IMAGE
**/
	LandscapeBigImage(MovieImageTypeEnum.BIG_IMAGE,150,225),
	PortraitBigImage(MovieImageTypeEnum.BIG_IMAGE,150,100), 
	LandscapeSmallImage(MovieImageTypeEnum.SMALL_IMAGE,120,180),
	PortraitSmallImage(MovieImageTypeEnum.SMALL_IMAGE,120,80);

	MovieImageTypeEnum type;
	int height;
	int width;
	
	MovieImageType(MovieImageTypeEnum type, int height, int width) {
		this.type = type;
		this.height = height;
		this.width = width;
	}

	public MovieImageTypeEnum getType() {
		return type;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
	
	public static MovieImageType selectImage(DataHandler handler) {
		int width = Integer.parseInt(handler.getClientInfo(DataHandler.KEY_WIDTH));
		int height = Integer.parseInt(handler.getClientInfo(DataHandler.KEY_HEIGHT));
		
		if((width <= 480) && (height < 640)) {
			return PortraitSmallImage;
		} else {
			return PortraitBigImage;
		}
	}
}
