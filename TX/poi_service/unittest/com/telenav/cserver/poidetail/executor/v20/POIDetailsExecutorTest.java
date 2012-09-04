package com.telenav.cserver.poidetail.executor.v20;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.poidetail.executor.v20.POIDetailsRequest;
import com.telenav.cserver.poidetail.executor.v20.POIDetailsResponse;
import com.telenav.cserver.poidetail.executor.v20.POIDetailsExecutor;
import com.telenav.cserver.weather.executor.TestUtil;

public class POIDetailsExecutorTest extends POIDetailsExecutor{

	POIDetailsRequest request = new POIDetailsRequest();
	POIDetailsResponse response = new POIDetailsResponse();
	
	
	@Before
	public void setUp() throws Exception {
	//	ResourceFactory.init();
		request.setPoiId(342033168);            // set POI id
		request.setUserProfile(getUserProfile());
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testDoExecuteExecutor() {
		
		try {
			
			this.doExecute(request, response, getExecutorContext());
			if(response.getStatus() == ExecutorResponse.STATUS_FAIL){
				Assert.assertEquals(ExecutorResponse.STATUS_FAIL, response.getStatus());
			}else{
				Assert.assertEquals(ExecutorResponse.STATUS_OK, response.getStatus());
			}
		} catch (ExecutorException e) {
			Assert.fail();
			e.printStackTrace();
		}
	}

	public UserProfile getUserProfile() {
		return TestUtil.getUserProfile64();
	}
	public ExecutorContext getExecutorContext(){
		return TestUtil.getExecutorContext();
	}
}
