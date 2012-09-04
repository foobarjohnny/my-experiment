package com.telenav.browser.movie.datatypes;

public enum RatingImage {
	
	ZeroStar("$imageUnSolid","$imageUnSolid","$imageUnSolid","$imageUnSolid"),
	HalfStar("$imageHalf","$imageUnSolid","$imageUnSolid","$imageUnSolid"),
	OneStar("$imageSolid","$imageUnSolid","$imageUnSolid","$imageUnSolid"),
	OneHalfStar("$imageSolid","$imageHalf","$imageUnSolid","$imageUnSolid"),
	TwoStar("$imageSolid","$imageSolid","$imageUnSolid","$imageUnSolid"),
	TwoHalfStar("$imageSolid","$imageSolid","$imageHalf","$imageUnSolid"),
	ThreeStar("$imageSolid","$imageSolid","$imageSolid","$imageUnSolid"),
	ThreeHalfStar("$imageSolid","$imageSolid","$imageSolid","$imageHalf"),
	FourStar("$imageSolid","$imageSolid","$imageSolid","$imageSolid");
	
	private String starImage1;
	private String starImage2;
	private String starImage3;
	private String starImage4;
	
	RatingImage(String starImage1, String starImage2, String starImage3, String starImage4) {
		this.starImage1 = starImage1;
		this.starImage2 = starImage2;
		this.starImage3 = starImage3;
		this.starImage4 = starImage4;
	}

	public String getStarImage1() {
		return starImage1;
	}

	public String getStarImage2() {
		return starImage2;
	}

	public String getStarImage3() {
		return starImage3;
	}

	public String getStarImage4() {
		return starImage4;
	}
	
	public static RatingImage selectImage(int ordinal) {
		for(RatingImage rImage: RatingImage.values()) {
			if(ordinal == rImage.ordinal()) {
				return rImage;
			}
		}
		return ZeroStar;
	}
}
