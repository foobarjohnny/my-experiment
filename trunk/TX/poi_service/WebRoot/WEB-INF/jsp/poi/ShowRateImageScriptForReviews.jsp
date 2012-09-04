func showStarsForReviews(int reviewsIndex,int index)
    String urlForImage1 = ""
    String urlForImage2 = ""
    String urlForImage3 = ""
    String urlForImage4 = ""
    String urlForImage5 = ""
    String url1 = "reviews_starImage1_" + index
    String url2 = "reviews_starImage2_" + index
    String url3 = "reviews_starImage3_" + index
    String url4 = "reviews_starImage4_" + index
    String url5 = "reviews_starImage5_" + index
    if 1 == reviewsIndex
	    urlForImage1 = "<%=reviews_imageSolid%>"
	    urlForImage2 = "<%=reviews_imageUnSolid%>"
	    urlForImage3 = "<%=reviews_imageUnSolid%>"
	    urlForImage4 = "<%=reviews_imageUnSolid%>"
	    urlForImage5 = "<%=reviews_imageUnSolid%>"
	elsif reviewsIndex == 2
	    urlForImage1 = "<%=reviews_imageSolid%>"
	    urlForImage2 = "<%=reviews_imageSolid%>"
	    urlForImage3 = "<%=reviews_imageUnSolid%>"
	    urlForImage4 = "<%=reviews_imageUnSolid%>"
	    urlForImage5 = "<%=reviews_imageUnSolid%>"
	elsif reviewsIndex == 3
		urlForImage1 = "<%=reviews_imageSolid%>"
	    urlForImage2 = "<%=reviews_imageSolid%>"
	    urlForImage3 = "<%=reviews_imageSolid%>"
	    urlForImage4 = "<%=reviews_imageUnSolid%>"
	    urlForImage5 = "<%=reviews_imageUnSolid%>"
	elsif reviewsIndex == 4
		urlForImage1 = "<%=reviews_imageSolid%>"
	    urlForImage2 = "<%=reviews_imageSolid%>"
	    urlForImage3 = "<%=reviews_imageSolid%>"
	    urlForImage4 = "<%=reviews_imageSolid%>"
	    urlForImage5 = "<%=reviews_imageUnSolid%>"
	elsif reviewsIndex == 5
		urlForImage1 = "<%=reviews_imageSolid%>"
	    urlForImage2 = "<%=reviews_imageSolid%>"
	    urlForImage3 = "<%=reviews_imageSolid%>"
	    urlForImage4 = "<%=reviews_imageSolid%>"
	    urlForImage5 = "<%=reviews_imageSolid%>"
	endif
	
	Page.setControlProperty(url1,"image",urlForImage1)
	Page.setControlProperty(url2,"image",urlForImage2)
	Page.setControlProperty(url3,"image",urlForImage3)
	Page.setControlProperty(url4,"image",urlForImage4)
	Page.setControlProperty(url5,"image",urlForImage5)
endfunc
