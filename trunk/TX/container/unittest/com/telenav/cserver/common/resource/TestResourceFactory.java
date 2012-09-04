/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.reflect.Whitebox;

import com.telenav.cserver.class4test.SampleBean;
import com.telenav.cserver.common.resource.ResourceFactory.ResourceLoadMeta;
import com.telenav.cserver.common.resource.ext.BarIterationResourceLoader;
import com.telenav.cserver.common.resource.ext.BinDataResourceLoader;
import com.telenav.cserver.common.resource.ext.NativeResourecBundleLoader;
import com.telenav.cserver.common.resource.ext.ResourceBundleLoader;
import com.telenav.cserver.common.resource.ext.SpringResourceLoader;
import com.telenav.cserver.common.resource.ext.XmlResourceLoader;
import com.telenav.cserver.common.resource.holder.impl.BillingConfHolder;
import com.telenav.cserver.common.resource.orders.AccountTypeLoadOrder;
import com.telenav.cserver.common.resource.orders.CarrierLoadOrder;
import com.telenav.cserver.common.resource.orders.DeviceLoadOrder;
import com.telenav.cserver.common.resource.orders.LocaleLoadOrder;
import com.telenav.cserver.common.resource.orders.OrLoadOrder;
import com.telenav.cserver.common.resource.orders.PlatformLoadOrder;
import com.telenav.cserver.common.resource.orders.ProductLoadOrder;
import com.telenav.cserver.common.resource.orders.ProgramCodeLoadOrder;
import com.telenav.cserver.common.resource.orders.ResolutionLoadOrder;
import com.telenav.cserver.common.resource.orders.VersionLoadOrder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.unittestutil.UTConstant;
import com.telenav.cserver.unittestutil.UnittestUtil;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * TestResourceFactory.java
 * 
 * xljiang@telenav.cn
 * 
 * @version 1.0 2011-4-29
 */
//@RunWith(PowerMockRunner.class)
@PrepareForTest({ com.telenav.cserver.common.resource.ResourceFactory.class,
		com.telenav.cserver.framework.configuration.Configurator.class })
@SuppressStaticInitializationFor({
		"com.telenav.cserver.common.resource.ResourceFactory",
		"com.telenav.cserver.framework.configuration.Configurator" })
public class TestResourceFactory extends TestCase {
	private ResourceFactory resourceFactory = ResourceFactory.getInstance();
	private ResourceLoadMeta meta = new ResourceLoadMeta();
	private UserProfile userProfile = UnittestUtil.createUserProfile();
	private TnContext tnContext = UnittestUtil.createTnContext();

	public void testCreateObject() {
		String configPath = "device/test.xml";
		meta.type = "xml";
		meta.objectName = "test";
		Object obj = resourceFactory.createObject(configPath, meta);
		assertNotNull(obj);
	}

	public void testCreateResource() {
		String configPath = "resource/test";
		Object obj;
		obj = ResourceFactory.createResource(configPath, null);
		assertNotNull(obj);
		
		try{
			obj = ResourceFactory.createResource(new BillingConfHolder());
			assertNull(obj);
		}catch(Exception e){
			UnittestUtil.printExceptionMsg(e);
		}
		
	}
	
	
	public void testCreateResource_holderAddUserProfileKeyMapping()
	{
		//test when loading test.properties file successfully
		MockTestHolder holder=UnittestUtil.createMockTestHolder();
		UserProfile user=UnittestUtil.createATT70UserProfile();
		user.setDevice("9100");
		Map map=(Map)ResourceFactory.createResource(holder, user, null);
		verifyTestPropertyFileContent(map);
		assertEquals("device//ATTNAVPROG/IPHONE/7_0_01//ATT_NAV/default//test", holder.getLoadingPath(user, tnContext));
		
		
		//test 71 version which still can load the test.properties file successfully
		user.setVersion("7.1.0");
		map=(Map)ResourceFactory.createResource(holder, user, null);
		verifyTestPropertyFileContent(map);
		assertEquals("device//ATTNAVPROG/IPHONE/7_1//ATT_NAV//test", holder.getLoadingPath(user, tnContext));
		
		//test when fail to load test.properties file 
		user.setPlatform("NonExistentPlatForm");
		map=(Map)ResourceFactory.createResource(holder, user, null);
		assertNull(map);
		assertNull(holder.getLoadingPath(user, tnContext));
	}

	public void testCreateResource1(){
		String configPath = "device/test.xml";
		BillingConfHolder billingConfHolder = new BillingConfHolder();
		billingConfHolder.setConfigPath(configPath);
		billingConfHolder.setType("spring");
		billingConfHolder.setFloatingStructureOrders(new LoadOrders());
		billingConfHolder.setPrefixStructureOrders(new LoadOrders());
		billingConfHolder.setSuffixStructureOrders(new LoadOrders());
		ResourceFactory.SEPARATOR = "";
		TestResourceFactory.setResourcePathToNull();
		Object result = ResourceFactory.createResource(billingConfHolder,userProfile, null);
		assertNotNull(result);
		
	}
	
	
	
	
	private void verifyTestPropertyFileContent(Map map)
	{
		assertNotNull(map);
		assertEquals(3, map.size());
		assertEquals("value1", map.get("key1"));
		assertEquals("value2", map.get("key2"));
		assertEquals("value3", map.get("key3")); 
	}
	
	
	
	private static void setResourcePathToNull(){
		try{
			Class[] cArray = ResourceFactory.class.getDeclaredClasses();
			for(Class c : cArray){
				if(c.getSimpleName().equals("ResourceLoaderConfig")){
					Field f = c.getDeclaredField("RESOURCE_PATH");
					f.setAccessible(true);
					f.set(f, "");
					break;
				}
			}
		}catch(Exception ex){
			UnittestUtil.printExceptionMsg(ex);
		}
		
	}
	
	private static void recoverResourcePathValue(){
		try{
			Class[] cArray = ResourceFactory.class.getDeclaredClasses();
			for(Class c : cArray){
				if(c.getSimpleName().equals("ResourceLoaderConfig")){
					Field f = c.getDeclaredField("RESOURCE_PATH");
					f.setAccessible(true);
					f.set(f, "device/");
					break;
				}
			}
		}catch(Exception ex){
			UnittestUtil.printExceptionMsg(ex);
		}
	}

	public void testGetConfigSuffix() {
		List<LoadOrder> orders = new ArrayList<LoadOrder>();
		orders.add(new AccountTypeLoadOrder());
		orders.add(new CarrierLoadOrder());
		orders.add(new DeviceLoadOrder());
		ResourceFactory.SEPARATOR = "/";
		String result = ResourceFactory.getConfigSuffix(orders,
				orders.size() - 1, userProfile, tnContext);
		
		assertEquals("/"+UserProfile.DEFAULT_ACCOUNT_TYPE + "/"
				+ UTConstant.USERPROFILE_CARRIER + "/"
				+ UTConstant.USERPROFILE_DEVICE, result);
		
		//test fail
		List<LoadOrder> argList = new ArrayList<LoadOrder>();
		ResourceFactory.getConfigSuffix(argList, -1, null, null);
		ResourceFactory.getConfigSuffix(argList, 3, null, null);
	}
	public void testGetConfigSuffixForKey() {
		List<LoadOrder> orders = new ArrayList<LoadOrder>();
		orders.add(new AccountTypeLoadOrder());
		orders.add(new CarrierLoadOrder());
		orders.add(new DeviceLoadOrder());
		orders.add(new OrLoadOrder());

		String result = ResourceFactory.getConfigSuffixForKey(orders,
				orders.size() - 1, userProfile, tnContext);
		assertEquals("/"+UserProfile.DEFAULT_ACCOUNT_TYPE + "/"
				+ UTConstant.USERPROFILE_CARRIER + "/"
				+ UTConstant.USERPROFILE_DEVICE+"/", result);
		
		//test fail
		List<LoadOrder> argList = new ArrayList<LoadOrder>();
		ResourceFactory.getConfigSuffixForKey(argList, -1, null, null);
		ResourceFactory.getConfigSuffixForKey(argList, 3, null, null);
	}
	public void testGetVersion(){
		
		String result = ResourceFactory.getVersion(userProfile);
		assertEquals(UTConstant.USERPROFILE_VERSION, result);
		userProfile.setVersion("111.d000");
		result = ResourceFactory.getVersion(userProfile);
		assertEquals("111", result);
		
		userProfile.setVersion("222.t111.d000");
		result = ResourceFactory.getVersion(userProfile);
		assertEquals("222", result);
	}
	public void testGetConfigSuffixList(){
		List<LoadOrder> argList = new ArrayList<LoadOrder>();
		List<String> list = ResourceFactory.getConfigSuffixList(argList, 0, null, null);
		assertEquals(1,list.size());
		assertEquals("",list.get(0));
		
		ResourceFactory.getConfigSuffixList(argList, -1, null, null);
		ResourceFactory.getConfigSuffixList(argList, 3, null, null);
	}
	public void testGetResourceLoader(){
		assertTrue(ResourceFactory.getResourceLoader(ResourceFactory.TYPE_RESOURCE_BUNDLE) 
				instanceof ResourceBundleLoader);
		assertTrue(ResourceFactory.getResourceLoader(ResourceFactory.TYPE_XML) 
				instanceof XmlResourceLoader);
		assertTrue(ResourceFactory.getResourceLoader(ResourceFactory.TYPE_BIN) 
				instanceof BinDataResourceLoader);
		assertTrue(ResourceFactory.getResourceLoader(ResourceFactory.NATIVE_TYPE_RESOURCE_BUNDLE) 
				instanceof NativeResourecBundleLoader);
		assertTrue(ResourceFactory.getResourceLoader(ResourceFactory.TYPE_SPRING) 
				instanceof SpringResourceLoader);
		assertTrue(ResourceFactory.getResourceLoader(ResourceFactory.TYPE_BAR_ITERATION) 
				instanceof BarIterationResourceLoader);
	}
	//=====================================================================================
	//=====================================================================================
	//=====================================================================================
	//=====================================================================================
	public void testAppend() throws Exception{
		List<StringBuffer> list = new ArrayList<StringBuffer>();
		list.add(new StringBuffer("a"));
		list.add(new StringBuffer("b"));
		list.add(new StringBuffer("c"));
		
		Whitebox.invokeMethod(ResourceFactory.class, "append", list,".html");
		assertEquals("a.html",list.get(0).toString());
		assertEquals("b.html",list.get(1).toString());
		assertEquals("c.html",list.get(2).toString());
	}
	public void testMakeCopy() throws Exception{
		List<StringBuffer> list = new ArrayList<StringBuffer>();
		list.add(new StringBuffer("a"));
		list.add(new StringBuffer("b"));
		list.add(new StringBuffer("c"));
		
		List<String> strList = new ArrayList<String>();
		strList.add(".html");
		strList.add(".xml");
		Whitebox.invokeMethod(ResourceFactory.class, "makeCopy", list,strList);
		
		assertEquals("new size of list should be its origial size multiplied by sreList's size",6,list.size());//
		assertEquals("a.html",list.get(0).toString());
		assertEquals("a.xml",list.get(1).toString());
		assertEquals("b.html",list.get(2).toString());
		assertEquals("b.xml",list.get(3).toString());
		assertEquals("c.html",list.get(4).toString());
		assertEquals("c.xml",list.get(5).toString());
	}
	public void testCreateResourceMore() {
		ResourceHolder holder = prepare4TestCreateResourceMore();
		UserProfile userProfile = UnittestUtil.createUserProfile();
		TnContext tnContext = UnittestUtil.createTnContext();
		Object result;
		//[1] floatingOrders.size > 0
		result = ResourceFactory.createResource(holder,userProfile,tnContext);
		assertTrue(result instanceof SampleBean);
		assertEquals("tel\u00E9fon",((SampleBean)result).getValue());
		//[2] floatingOrders.size = 0
		holder.setFloatingStructureOrders(new LoadOrders());
		result = ResourceFactory.createResource(holder,userProfile,tnContext);
		assertTrue(result instanceof SampleBean);
		assertEquals("tel\u00E9fon",((SampleBean)result).getValue());
	}
	private ResourceHolder prepare4TestCreateResourceMore(){
		TestResourceFactory.recoverResourcePathValue();
		ResourceFactory.SEPARATOR = "/";
		BillingConfHolder holder = new BillingConfHolder();
		//[1] add prefix orders
		LoadOrders preOrders = new LoadOrders();
		preOrders.addOrder(new CarrierLoadOrder());
		preOrders.addOrder(new PlatformLoadOrder());
		preOrders.addOrder(new VersionLoadOrder());
		//prefixConfigPath = device/ATT/RIM
		holder.setPrefixStructureOrders(preOrders);
		
		//[2] add suffix orders
		LoadOrders sufOrders = new LoadOrders();
		sufOrders.addOrder(new LocaleLoadOrder());
		//suffixConfigPath = /6_0_01/ATT
		holder.setSuffixStructureOrders(sufOrders);
		
		//[3] add floating orders
		LoadOrders floatingOrders = new LoadOrders();
		floatingOrders.addOrder(new ProductLoadOrder());
		floatingOrders.addOrder(new ProgramCodeLoadOrder());
		
		OrLoadOrder olo = new OrLoadOrder();
		List<LoadOrder> orderList = new ArrayList<LoadOrder>();
		orderList.add(new DeviceLoadOrder());
		orderList.add(new ResolutionLoadOrder());
		olo.setLoadOrderList(orderList);
		
		floatingOrders.addOrder(olo);
		holder.setFloatingStructureOrders(floatingOrders);
		
		//[4] other infomation
		holder.setType("spring");//avoid Null Pointer Exception
		holder.setConfigPath("test.xml");
		return holder;
	}
}
