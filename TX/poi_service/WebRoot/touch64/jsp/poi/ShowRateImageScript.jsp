	func showRateStarImage(int rating,int index)
	    String urlForImage1 = ""
	    String urlForImage2 = ""
	    String urlForImage3 = ""
	    String urlForImage4 = ""
	    String urlForImage5 = ""
	    String url1 = "starImage1_" + index
	    String url2 = "starImage2_" + index
	    String url3 = "starImage3_" + index
	    String url4 = "starImage4_" + index
	    String url5 = "starImage5_" + index
	    if 0 == rating
		    urlForImage1 = "$imageUnSolid"
		    urlForImage2 = "$imageUnSolid"
		    urlForImage3 = "$imageUnSolid"
		    urlForImage4 = "$imageUnSolid"
		    urlForImage5 = "$imageUnSolid"
		elsif rating <= 5
		    urlForImage1 = "$imageHalf"
		    urlForImage2 = "$imageUnSolid"
		    urlForImage3 = "$imageUnSolid"
		    urlForImage4 = "$imageUnSolid"
		    urlForImage5 = "$imageUnSolid"
		elsif rating <= 10
		    urlForImage1 = "$imageSolid"
		    urlForImage2 = "$imageUnSolid"
		    urlForImage3 = "$imageUnSolid"
		    urlForImage4 = "$imageUnSolid"
		    urlForImage5 = "$imageUnSolid"
		elsif rating <= 15
		    urlForImage1 = "$imageSolid"
		    urlForImage2 = "$imageHalf"
		    urlForImage3 = "$imageUnSolid"
		    urlForImage4 = "$imageUnSolid"
		    urlForImage5 = "$imageUnSolid"
		elsif rating <= 20
		    urlForImage1 = "$imageSolid"
		    urlForImage2 = "$imageSolid"
		    urlForImage3 = "$imageUnSolid"
		    urlForImage4 = "$imageUnSolid"
		    urlForImage5 = "$imageUnSolid"
		elsif rating <= 25
		    urlForImage1 = "$imageSolid"
		    urlForImage2 = "$imageSolid"
		    urlForImage3 = "$imageHalf"
		    urlForImage4 = "$imageUnSolid"
		    urlForImage5 = "$imageUnSolid"
		elsif rating <= 30
			urlForImage1 = "$imageSolid"
		    urlForImage2 = "$imageSolid"
		    urlForImage3 = "$imageSolid"
		    urlForImage4 = "$imageUnSolid"
		    urlForImage5 = "$imageUnSolid"
		elsif rating <= 35
			urlForImage1 = "$imageSolid"
		    urlForImage2 = "$imageSolid"
		    urlForImage3 = "$imageSolid"
		    urlForImage4 = "$imageHalf"
		    urlForImage5 = "$imageUnSolid"
		elsif rating <= 40
			urlForImage1 = "$imageSolid"
		    urlForImage2 = "$imageSolid"
		    urlForImage3 = "$imageSolid"
		    urlForImage4 = "$imageSolid"
		    urlForImage5 = "$imageUnSolid"
		elsif rating <= 45
			urlForImage1 = "$imageSolid"
		    urlForImage2 = "$imageSolid"
		    urlForImage3 = "$imageSolid"
		    urlForImage4 = "$imageSolid"
		    urlForImage5 = "$imageHalf"
		else
			urlForImage1 = "$imageSolid"
		    urlForImage2 = "$imageSolid"
		    urlForImage3 = "$imageSolid"
		    urlForImage4 = "$imageSolid"
		    urlForImage5 = "$imageSolid"
		endif
		Page.setControlProperty(url1,"image",urlForImage1)
		Page.setControlProperty(url2,"image",urlForImage2)
		Page.setControlProperty(url3,"image",urlForImage3)
		Page.setControlProperty(url4,"image",urlForImage4)
		Page.setControlProperty(url5,"image",urlForImage5)
	endfunc
	
	func hideStarIcons(int index)
	    String url1 = "starImage1_" + index
	    String url2 = "starImage2_" + index
	    String url3 = "starImage3_" + index
	    String url4 = "starImage4_" + index
	    String url5 = "starImage5_" + index
	    
        Page.setComponentAttribute(url1,"visible","0")
        Page.setComponentAttribute(url2,"visible","0")
        Page.setComponentAttribute(url3,"visible","0")
        Page.setComponentAttribute(url4,"visible","0")
        Page.setComponentAttribute(url5,"visible","0")
	endfunc
	
	func showStarIcons(int index)
	    String url1 = "starImage1_" + index
	    String url2 = "starImage2_" + index
	    String url3 = "starImage3_" + index
	    String url4 = "starImage4_" + index
	    String url5 = "starImage5_" + index
	    
        Page.setComponentAttribute(url1,"visible","1")
        Page.setComponentAttribute(url2,"visible","1")
        Page.setComponentAttribute(url3,"visible","1")
        Page.setComponentAttribute(url4,"visible","1")
        Page.setComponentAttribute(url5,"visible","1")
	endfunc
