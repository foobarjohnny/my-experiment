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
<%@page import="com.telenav.cserver.browser.util.ClientHelper"%>
<%
	String poiModuleName = ClientHelper.getModuleNameForPoi(handlerGloble);
	String poiMainUrl = "{poi.http}/" + poiModuleName + "startUp.do?pageRegion=" + region;
	String versionStr = handlerGloble.getClientInfo(DataHandler.KEY_VERSION);
	String imageBg = imageUrl + "background_telenav.png";
    String pageURL = getPage + "SearchMovie";
    String callbackURL = getPageCallBack + "SearchMovie";
    String whereStr = msg.get("mSearch.where");
    String whenStr = msg.get("mSearch.when"); 
	String strCarrier = handlerGloble.getClientInfo(handlerGloble.KEY_CARRIER);
%>


<tml:TML outputMode="TxNode">
	<jsp:include page="model/SearchMovieModel.jsp" />
	<jsp:include page="controller/SelectDateController.jsp" />
	<jsp:include page="controller/MovieListController.jsp" />
	<jsp:include page="model/MovieListModel.jsp" />
	<jsp:include page="model/MovieModel.jsp" />
	<jsp:include page="/touch/jsp/common/ac/controller/SelectAddressController.jsp" />
	<%@ include file="/touch/jsp/local_service/GetGps.jsp"%>
	
	<tml:script language="fscript" version="1">
		<![CDATA[
		        func chooseWhen()
		            TxNode inputNode 
				    inputNode = ParameterSet.getParam("movieName")
				    SearchMovie_M_saveInput(inputNode)
				    SelectDate_C_showDateList()
				    return FAIL
		        endfunc
		        
		        func preLoad()
		            setInformation()
		        endfunc
				
				func onShow()
		            setInformation()
			    endfunc
		        
				func onLoad()
					#check if from say command-search movie
					string externalFlag = ""
					String saveKey1="<%=Constant.StorageKey.EXTERNAL_FROM_SAYCOMMAND%>"
					TxNode nodeExternalFlag = Cache.getFromTempCache(saveKey1)
					if nodeExternalFlag != NULL
						externalFlag = TxNode.msgAt(nodeExternalFlag,0)
					endif
					
					if "searchMovie" == externalFlag
						Cache.deleteFromTempCache(saveKey1)
						searchMovieForCurrentLocation()
						return FAIL
					endif
					
					#set flag for New Search action to return to original search
					TxNode flag
			    	TxNode.addMsg(flag, "movie")
			        String saveKey="<%=Constant.StorageKey.MOVIE_FROM_SEARCH%>"
			    	Cache.saveToTempCache(saveKey, flag)
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
					       if "" == addressString || addressString == NULL
						      String city =  JSONObject.get(addressJO,"city") 
							  if city != "" && city != NULL
									addressString = city + ", " + JSONObject.get(addressJO,"state")
							  else
									addressString = JSONObject.get(addressJO,"state")
							  endif
					       else
					       	  String city = JSONObject.get(addressJO,"city")
					       	  if "" != city || NULL != city
									addressString = addressString + ", " +city + ", " + JSONObject.get(addressJO,"state")
					          else
									addressString = addressString + ", " + JSONObject.get(addressJO,"state")
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
				   	    #PreferenceConstants.KEY_GENERAL_LANGUAGE=9
				   	    TxNode langNode = Preference.getPreferenceValue(9)
				   	    String language
				   	    if NULL != langNode
		          		    language = TxNode.msgAt(langNode,0)	
		          		endif		      		
			      		println("------------language="+language)
						if language == "fr_CA"
							String currTime = Time.format("EEE, d MMM", -1)
						else
							String currTime = Time.format("EEE, MMM d", -1)
						endif
				   		
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
		        	<%if("6.2.01".equals(versionStr)){%>
		        	getCurrentLocationWithoutBlocking(<%=BrowserFrameworkConstants.CurrentLocation.CURRENT_LOCATION%>,240,480,12)
		        	<%}else{%>
		        	getCurrentLocation(<%=BrowserFrameworkConstants.CurrentLocation.CURRENT_LOCATION%>,240,480,12)
		        	<%}%>
		        endfunc
		        
		        func setCurrentLocation(JSONObject jo)
       				Movie_M_saveCurrentLocation(jo)
				    String textAddress = "<%=msg.get("mSearch.currSet")%>"
				    Page.setComponentAttribute("addressName","text",textAddress)     
				    doSearchWithAjax()
        		endfunc

				func CallBack_GPS_Error(int param)
				endfunc
				        
		        func doSearchWithAjax()
		            TxNode inputNode 
				    inputNode = ParameterSet.getParam("movieName")
				    
				    SearchMovie_M_saveInput(inputNode)
					
					#TODO add sorting by name or rating
					int sortByName = Movie_M_getSortType()
				    MovieList_C_searchMovieWithAjax(sortByName)
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
					return FAIL
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
				
				func searchMovieForCurrentLocation()
				    searchLocation()
				endfunc        
			]]>
	</tml:script>
	<tml:menuItem name="home" pageURL="<%=poiMainUrl%>">
	</tml:menuItem>

	<tml:menuItem name="autoFill" onClick="inputClick" trigger="KEY_MENU"/>
	<tml:menuItem name="doSearch" onClick="doSearchOnClick"
		progressBarText="<%=msg.get("mSearch.bar.searching")%>">
	</tml:menuItem>
	<tml:menuItem name="addressSelect" onClick="chooseLocation">
	</tml:menuItem>
	<tml:menuItem name="whenSelect" onClick="chooseWhen">
	</tml:menuItem>
	<tml:page id="searchMoviePage" url='<%=pageURL%>' type="<%=pageType%>" groupId="<%=MOVIE_GROUP_ID%>"
		background="<%=imageBg%>" showLeftArrow="true"
		showRightArrow="true" helpMsg="$//$searchmovies" >
		
		<tml:title id="title" fontWeight="System_large|bold" fontColor="white" >
			<%=msg.get("mSearch.title")%>
		</tml:title>
		
		<tml:inputBox id="movieName" titleAbove="false" titleFontWeight="system_large|bold"
			fontWeight="system_large" title=""
			prompt="<%=msg.get("mSearch.input.prompt")%>" isAlwaysShowPrompt="true">
			<tml:menuRef name="autoFill" />
		</tml:inputBox>

		<tml:compositeListItem id="address" getFocus="false"
			visible="true" bgColor="#FFFFFF" transparent="false"
			focusBgImage=""
			blurBgImage="" isFocusable="true">
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
			focusBgImage=""
			blurBgImage="" isFocusable="true">
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
			imageClick=''
			imageUnclick=''>
			<tml:menuRef name="doSearch" />
		</tml:button>
		<cserver:outputLayout />
	</tml:page>
</tml:TML>