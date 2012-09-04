<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>

<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.telenav.browser.movie.Constant"%>
<%@page import="com.telenav.browser.movie.Util"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>

<%
	String imageBg = imageUrl + "background_telenav.png";
    String pageURL = getPage + "SearchMovie";
    String callbackURL = getPageCallBack + "SearchMovie";
    String whereStr = msg.get("mSearch.where");
    String whenStr = msg.get("mSearch.when"); 
    String twoLinesImageFocus = imageUrl + "2line_list_highlight.png";
    String twoLinesImageBlur = imageUrl + "2line_list.png";
    String POI_SERVICE = "{poi.http}";
%>


<tml:TML outputMode="TxNode">
	<jsp:include page="model/SearchMovieModel.jsp" />
	<jsp:include page="controller/SelectDateController.jsp" />
	<jsp:include page="controller/MovieListController.jsp" />
	<jsp:include page="model/MovieListModel.jsp" />
	<jsp:include page="model/MovieModel.jsp" />
	<jsp:include page="/WEB-INF/jsp/common/ac/controller/SelectAddressController.jsp" />
	
	<tml:script language="fscript" version="1">
		<![CDATA[
		        func chooseWhen()
		            TxNode inputNode 
				    inputNode = ParameterSet.getParam("movieName")
				    SearchMovie_M_saveInput(inputNode)
				    SelectDate_C_showDateList()
		        endfunc
		        
				func onLoad()
					#set flag for New Search action to return to original search
					TxNode flag
			    	TxNode.addMsg(flag, "movie")
			        String saveKey="<%=Constant.StorageKey.MOVIE_FROM_SEARCH%>"
			    	Cache.saveToTempCache(saveKey, flag)
					setInformation()
				endfunc
				
				func setInformation()
				    #Set what
				    TxNode inputNode = SearchMovie_M_getInput()
				    
				    if inputNode != NULL 
				       String inputString=TxNode.msgAt(inputNode,0)
				       Page.setComponentAttribute("movieName","text",inputString)
				       SearchMovie_M_deleteInput()
				    endif
				    
				    #Set address
				    JSONObject addressJO = Movie_M_getAddress()
				    String textAddress = "<%=msg.get("mSearch.curr")%>"
				    if NULL!=addressJO
				       int type = JSONObject.getInt(addressJO,"type")
				       if type != 6
				           String addressString = JSONObject.get(addressJO,"firstLine")
					       if NULL==addressString || "" == addressString
					          
					          #check city and state
					          String cityString=JSONObject.get(addressJO,"city")
					          String stateString=JSONObject.get(addressJO,"state")
					          if NULL!=cityString && ""!=cityString
					             addressString=cityString
					             if NULL!=stateString && ""!=stateString
					                addressString =cityString+","+stateString
					             endif
					          else
					             addressString=stateString
					          endif
					          					          					          
					       endif
					       textAddress = addressString
					   else
					       textAddress = "<%=msg.get("mSearch.currSet")%>"
				       endif
				    endif
			        Page.setComponentAttribute("addressName","text",textAddress)
				    
				    #Set date
				    String label = SelectDate_C_getDateLabel()
				    String id = SelectDate_C_getDateID()
				    
				    String lString
				    if NULL!=label
				    	if (id == "0")
				    		lString = label + " <%=msg.get("mSearch.today")%>"
				    	else
				    		lString = label
				    	endif
				    	Page.setComponentAttribute("whenName","text",lString)
				   	else
				   		String currTime = Time.format("EEEE, MMM d", -1)
						lString = currTime + " <%=msg.get("mSearch.today")%>"				   		
				    	Page.setComponentAttribute("whenName","text",lString)
				    endif
				endfunc
		
		        func doSearchOnClick()
					JSONObject addressJO = Movie_M_getAddress()
					if NULL == addressJO
						searchLocation()
						return FAIL
					endif
				    doSearchWithAjax()
		        endfunc
		        
		        func searchLocation()
		        	JSONObject locParm
					JSONObject.put(locParm,"LocationType",1)
					JSONObject.put(locParm,"GpsLocationValidTime",240000)
					JSONObject.put(locParm,"NetworkLocationValidTime",1860000)
					JSONObject.put(locParm,"Timeout",12000)
					TxNode locParmNode
					TxNode.addMsg(locParmNode,""+locParm)
					MenuItem.setBean("doGetGps","locParam",locParmNode)
					System.doAction("doGetGps")
		        endfunc
		        
		        func getLocation()
		        	TxNode currentLocationNode = ParameterSet.getParam("currentLocation")

		        	JSONObject stop 
					String joStr =TxNode.msgAt(currentLocationNode,0)
					JSONObject joStop = JSONObject.fromString(joStr)
					String callbackId = JSONObject.get(joStop,"CallbackID")
					 
					if(callbackId!="Success")
						System.showErrorMsg("<%=msg.get("mSearch.gps.error")%>")
						return FAIL
					endif
					
					String location = JSONObject.getString(joStop,"Location")
					JSONObject gps= JSONObject.fromString(location)
				      
			        JSONObject jo
					JSONObject.put(jo,"lat",JSONObject.get(gps,"Lat"))
					JSONObject.put(jo,"lon",JSONObject.get(gps,"Lon"))
					JSONObject.put(jo,"type",6)
					Movie_M_saveCurrentLocation(jo)
				    String textAddress = "<%=msg.get("mSearch.currSet")%>"
				    Page.setComponentAttribute("addressName","text",textAddress)     
				    doSearchWithAjax()
		        endfunc
		        
		        func doSearchWithAjax()
		            TxNode inputNode 
				    inputNode = ParameterSet.getParam("movieName")
				    
				    SearchMovie_M_saveInput(inputNode)
					
					#TODO add sorting by name or rating
					int sortByName = Movie_M_getSortType()
					String newSortBy = MovieList_M_getNewSortBy()
				    MovieList_C_searchMovieWithAjax(sortByName, newSortBy)
		        endfunc
		        
		        func showSearchScreen()
		        	#setInformation()
		        	#System.repaint()
		        endfunc
		        
		        func chooseLocation()
		            JSONObject jo
		        	JSONObject.put(jo,"title","<%=msg.get("common.chooseLocation")%>")
		        	JSONObject.put(jo,"mask","01111111011")
		        	JSONObject.put(jo,"from","movie")
		        	JSONObject.put(jo,"callbackfunction","CallBack_SelectAddress")
					JSONObject.put(jo,"callbackpageurl","<%=callbackURL%>")
					SelectAddress_C_SelectAddress(jo)
		        endfunc
		        
		        #Back from select address
		        func CallBack_SelectAddress()
		           TxNode addressNode
				   addressNode=ParameterSet.getParam("returnAddress")
				   
				   String joString = TxNode.msgAt(addressNode,0)
				   JSONObject jo = JSONObject.fromString(joString)

				   int type = JSONObject.getInt(jo,"type")
				   
				   #type == 6 means current location
			       if type == 6
                       Movie_M_deleteLocation()
                   else
                       Movie_M_saveCurrentLocation(jo)
			       endif
				   setInformation()
		        endfunc

		        func inputClick()
		           Page.setControlProperty("searchButton","focused","true")
		        endfunc	
		        
		        func onBack()
				   String backAction = Movie_M_getBackAction()
	               if "" != backAction
	                  if "<%=Constant.BACK_ACTION_MAIN_SCREEN%>" == backAction
	                     Movie_M_deleteBackAction()
	                     System.doAction("home")
	                     return FAIL
	                  else
	                     System.quit()
	                     return FAIL
	                  endif
	               endif
				endfunc	        
			]]>
	</tml:script>
	<tml:menuItem name="home" pageURL="<%=POI_SERVICE + "/startUp.do?pageRegion=" + region%>">
	</tml:menuItem>

	<tml:menuItem name="autoFill" onClick="inputClick" trigger="TRACKBALL_CLICK|KEY_MENU"/>
	<tml:menuItem name="doSearch" onClick="doSearchOnClick"
		progressBarText="<%=msg.get("mSearch.bar.searching")%>">
	</tml:menuItem>
	<tml:menuItem name="addressSelect" onClick="chooseLocation">
	</tml:menuItem>
	<tml:menuItem name="whenSelect" onClick="chooseWhen">
	</tml:menuItem>
	
	<tml:actionItem name="getGPS" action="getGPS"
		progressBarText="<%=msg.get("mSearch.bar.gps")%>">
						<tml:input name="locParam"></tml:input>
						<tml:output name="currentLocation" />
	</tml:actionItem>
	<tml:menuItem actionRef="getGPS" name="doGetGps" onClick="getLocation"/>
	
	<tml:page id="searchMoviePage" url='<%=pageURL%>' type="<%=pageType%>" groupId="<%=MOVIE_GROUP_ID%>"
		background="<%=imageBg%>" showLeftArrow="true"
		showRightArrow="true" helpMsg="$//$searchmovies" >

		<tml:title id="searchLabel" align="center|middle"
			fontWeight="bold|system_large" fontColor="white">
			<%=msg.get("mSearch.title")%>
		</tml:title>

		<tml:inputBox id="movieName" titleAbove="false" titleFontWeight="system_large|bold"
			fontWeight="system_large" title="<%=msg.get("movie") + ":"%>"
			prompt="<%=msg.get("mSearch.input.prompt")%>">
			<tml:menuRef name="autoFill" />
		</tml:inputBox>

		<tml:compositeListItem id="address" getFocus="false"
			visible="true" bgColor="#FFFFFF" transparent="false"
			focusBgImage="<%=twoLinesImageFocus%>"
			blurBgImage="<%=twoLinesImageBlur%>" isFocusable="true">
			<tml:label id="addressTitle" textWrap="ellipsis"
				fontWeight="bold|system_large" focusFontColor="white" align="left|middle">
				<%=whereStr%>
			</tml:label>
			<tml:label id="addressName" textWrap="ellipsis"
				focusFontColor="white" fontWeight="system_large" align="left|middle">
				<%=msg.get("mSearch.curr")%>
			</tml:label>
			<tml:menuRef name="addressSelect" />
		</tml:compositeListItem>
		
<%
    Date currentDate = new Date();
    SimpleDateFormat sdf = (SimpleDateFormat)Constant.DATE_FORMAT.clone();
%>

		<tml:compositeListItem id="when" getFocus="false"
			visible="true" bgColor="#FFFFFF" transparent="false"
			focusBgImage="<%=twoLinesImageFocus%>"
			blurBgImage="<%=twoLinesImageBlur%>" isFocusable="true">
			<tml:label id="whenTitle" textWrap="ellipsis"
				fontWeight="bold|system_large" focusFontColor="white" align="left|middle">
				<%=whenStr%>
			</tml:label>
			<tml:label id="whenName" textWrap="ellipsis"
				focusFontColor="white" fontWeight="system_large" align="left|middle">
				<%=sdf.format(currentDate) + " " + msg.get("mSearch.today")%>
			</tml:label>
			<tml:menuRef name="whenSelect" />
		</tml:compositeListItem>
		
		<tml:button id="searchButton" text="<%=msg.get("movie.search")%>" fontWeight="system_large"
			imageClick='<%=imageUrl + "button_small_on.png"%>'
			imageUnclick='<%=imageUrl + "button_small_off.png"%>'>
			<tml:menuRef name="doSearch" />
		</tml:button>
		<cserver:outputLayout />
	</tml:page>
</tml:TML>