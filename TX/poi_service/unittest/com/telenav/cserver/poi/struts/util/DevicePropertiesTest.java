package com.telenav.cserver.poi.struts.util;

import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.TestUtil;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;
import com.telenav.kernel.util.datatypes.TnContext;

public class DevicePropertiesTest {
	
	DeviceProperties deviceProperties = new DeviceProperties(new HashMap());
	private DataSource dataSource = new DataSource();
	
	@Before
	public void setUp() throws Exception {
		
		UserProfile profile = TestUtil.getUserProfile();
		TnContext tnContext = new TnContext();
		tnContext.addProperty(TnContext.PROP_CARRIER, profile.getCarrier());
		tnContext.addProperty(TnContext.PROP_DEVICE, profile.getDevice());
		tnContext.addProperty(TnContext.PROP_PRODUCT, profile.getProduct());
		tnContext.addProperty(TnContext.PROP_VERSION, profile.getVersion());
		tnContext.addProperty("application", profile.getPlatform());
		tnContext.addProperty("login", profile.getMin());
		tnContext.addProperty("userid", profile.getUserId());

		dataSource.addData(int.class.getName(), Integer.valueOf("1"));
		dataSource.addData(String.class.getName(), String.valueOf("1"));
		dataSource.addData(boolean.class.getName(), Boolean.valueOf("TRUE"));
		
	}

	@Test
	public void testSuite(){
		GenericTest.getInstance().startTest(deviceProperties, dataSource);
	}

}
