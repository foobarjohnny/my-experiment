package com.telenav.cserver.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.TnPoi;

/*
 * 	mmli@telenavsoftware.com, 2011/12/25, telenav
 * 	unit test for AirportPoi 
 */

public class AirportPoiTest {
	
	public static final String parameterOfDouble = "double";
	public static final String parameterOfTnpoi = "tnpoi";
	public static final String parameterOfAirportPoi = "airportpoi";
	
	public static final String unknowError = "unknown reason from %s";	
	
	AirportPoi instanceForTest;
	HashMap<String, Object> dataCenter = new HashMap();
	TnPoi tnpoi;
	Address addr;
	@Before
	public void setUp() throws Exception {
		// setup data before testing
		
		addr = new Address();
		tnpoi = new TnPoi();
		tnpoi.setAddress(addr);
		instanceForTest = new AirportPoi(tnpoi);
		
		addr.setAddressId("id");
		addr.setFirstLine("just for test");
		addr.setLabel("label");
		tnpoi.setAddress(addr);
		
		dataCenter.put(AirportPoiTest.parameterOfDouble, Double.valueOf(1.0));
		dataCenter.put(AirportPoiTest.parameterOfTnpoi, tnpoi);
		dataCenter.put(AirportPoiTest.parameterOfAirportPoi, instanceForTest);
		
	}

	@After
	public void tearDown() throws Exception {
		// clear work
		dataCenter.clear();
	}

	@Test
	public void testAirportPoiTnPoi() {
		try{
			Class<?>[] parameter = {TnPoi.class};
			Object[] parameterObj = {dataCenter.get(AirportPoiTest.parameterOfTnpoi)};
			Constructor<?> constructor = instanceForTest.getClass().getConstructor(parameter);
			constructor.newInstance(parameterObj);
		}catch(NoSuchMethodException e){
			fail("the constructor doesn't exist or has been removed!");
		}catch(Throwable e){
			fail((e.getMessage() != null) ? e.getMessage() : AirportPoiTest.unknowError.replace("%s", e.getClass().getName()));
		}
	}

	@Test
	public void testAirportPoiTnPoiDoubleDouble() {
		try{
			Class<?>[] parameter = {TnPoi.class, double.class, double.class};
			Object[] parameterObj = {
										dataCenter.get(AirportPoiTest.parameterOfTnpoi), 
										dataCenter.get(AirportPoiTest.parameterOfDouble),
										dataCenter.get(AirportPoiTest.parameterOfDouble), 										
									};
			Constructor<?> constructor = instanceForTest.getClass().getConstructor(parameter);
			constructor.newInstance(parameterObj);
		}catch(NoSuchMethodException e){
			fail("the constructor doesn't exist or has been removed!");
		}catch(Throwable e){
			fail((e.getMessage() != null) ? e.getMessage() : AirportPoiTest.unknowError.replace("%s", e.getClass().getName()));
		}
	}

	@Test
	public void testCompareToObject() {
		String methodName = "compareTo";
		try{
			Class<?>[] parameter = {Object.class};
			Object[] parameterObj = {dataCenter.get(AirportPoiTest.parameterOfAirportPoi)};
			Method m = instanceForTest.getClass().getMethod(methodName, parameter);
			Object o = m.invoke(instanceForTest, parameterObj);
			
		}catch(NoSuchMethodException e){
			fail("couldn't find method " + methodName + " or it has been removed!");
		}catch(InvocationTargetException e){
			assertTrue(true);
		}catch(ClassCastException e){
			assertTrue(true);
		}catch(Throwable e){
			fail((e.getMessage() != null) ? e.getMessage() : AirportPoiTest.unknowError.replace("%s", e.getClass().getName()));
		}
	}

	@Test
	public void testCompareToAirportPoi() {
		int expect = 0;
		String methodName = "compareTo";
		try{
			Class<?>[] parameter = {AirportPoi.class};
			Object[] parameterObj = {dataCenter.get(AirportPoiTest.parameterOfAirportPoi)};
			Method m = instanceForTest.getClass().getMethod(methodName, parameter);
			Object o = m.invoke(instanceForTest, parameterObj);
			
			assertEquals(Integer.valueOf(expect), o);
			
		}catch(NoSuchMethodException e){
			fail("couldn't find method " + methodName + " or it has been removed!");
		}catch(Throwable e){
			fail((e.getMessage() != null) ? e.getMessage() : AirportPoiTest.unknowError.replace("%s", e.getClass().getName()));
		}
	}

	@Test
	public void testGetPoi() {
		String methodName = "getPoi";
		try{
			Class<?>[] parameter = {};
			Object[] parameterObj = {};
			Method m = instanceForTest.getClass().getMethod(methodName, parameter);
			Object o = m.invoke(instanceForTest, parameterObj);
			
			assertEquals(dataCenter.get(AirportPoiTest.parameterOfTnpoi).getClass(), o.getClass());
			
		}catch(NoSuchMethodException e){
			fail("couldn't find method " + methodName + " or it has been removed!");
		}catch(Throwable e){
			fail((e.getMessage() != null) ? e.getMessage() : AirportPoiTest.unknowError.replace("%s", e.getClass().getName()));
		}
	}

	@Test
	public void testGetAddress() {
		String methodName = "getAddress";
		try{
			Class<?>[] parameter = {};
			Object[] parameterObj = {};
			Method m = instanceForTest.getClass().getMethod(methodName, parameter);
			Object o = m.invoke(instanceForTest, parameterObj);
			
			assertTrue( ((TnPoi)dataCenter.get(AirportPoiTest.parameterOfTnpoi)).getAddress().equals(o) );
			
		}catch(NoSuchMethodException e){
			fail("couldn't find method " + methodName + " or it has been removed!");
		}catch(Throwable e){
			fail((e.getMessage() != null) ? e.getMessage() : AirportPoiTest.unknowError.replace("%s", e.getClass().getName()));
		}
	}

}
