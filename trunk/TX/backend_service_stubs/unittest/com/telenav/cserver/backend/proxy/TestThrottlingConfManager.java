/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy;

import static org.easymock.EasyMock.expect;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.backend.proxy.annotation.ThrottlingConf;
import com.telenav.cserver.framework.throttling.Service;
import com.telenav.cserver.framework.throttling.ThrottlingConfiguration;
import com.telenav.cserver.framework.throttling.ThrottlingException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ThrottlingConfiguration.class })
@SuppressStaticInitializationFor("com.telenav.cserver.framework.throttling.ThrottlingConfiguration")
public class TestThrottlingConfManager extends TestCase {

	protected ThrottlingConfManager throttlingManager;

	public void init(List<Service> services) {
		// mock dependency
		PowerMock.mockStatic(ThrottlingConfiguration.class);
		ThrottlingConfiguration mockTc = PowerMock
				.createMock(ThrottlingConfiguration.class);
		expect(mockTc.getServiceList()).andReturn(services);
		expect(ThrottlingConfiguration.getInstance()).andReturn(mockTc);
		PowerMock.replayAll();
		// new manager
		throttlingManager = new ThrottlingConfManager();
	}

	@Test
	public void throttlingWithoutDefaultServiceSet() throws ThrottlingException {
		init(mockServices());

		ThrottlingConf conf = createThrottlingConf("nonExistentAndNoDefaultProxy");

		throttlingManager.incrementThrottling(conf);
		throttlingManager.incrementThrottling(conf);
		throttlingManager.incrementThrottling(conf);

		// Assert, there should be no corresponding
		// 'nonExistentAndNoDefaultProxy' ThrottlingControlItem there.
		Assert.assertNull(throttlingManager
				.getThrottlingControlItem("nonExistentAndNoDefaultProxy"));

		throttlingManager.decrementThrottling(conf);
		throttlingManager.decrementThrottling(conf);
		throttlingManager.decrementThrottling(conf);

		// Assert, there should be no corresponding
		// 'nonExistentAndNoDefaultProxy' ThrottlingControlItem there.
		Assert.assertNull(throttlingManager
				.getThrottlingControlItem("nonExistentAndNoDefaultProxy"));

		PowerMock.verifyAll();
	}

	/**
	 * This will test the sucessful case
	 * 
	 * @throws ThrottlingException
	 */
	@Test
	public void throttlingNormalCase() throws ThrottlingException {
		init(mockServicesWithDefaultService());
		ThrottlingConf conf = createThrottlingConf("dummyProxy2");

		throttlingManager.incrementThrottling(conf);
		throttlingManager.incrementThrottling(conf);

		// afert incrementing twice, online number for this proxy should 2
		Assert.assertEquals(2,
				throttlingManager.getThrottlingControlItem("dummyProxy2")
						.getCurNumOnLine());

		throttlingManager.decrementThrottling(conf);
		throttlingManager.decrementThrottling(conf);

		// back to 0 after decrementing twice.
		Assert.assertEquals(0,
				throttlingManager.getThrottlingControlItem("dummyProxy2")
						.getCurNumOnLine());

		PowerMock.verifyAll();
	}

	/**
	 * This will test the case when client requests more than max online number.
	 * 
	 * @throws ThrottlingException
	 */
	@Test(expected = ThrottlingException.class)
	public void throttlingExceedMaxOnlineNum() throws ThrottlingException {
		init(mockServicesWithDefaultService());

		ThrottlingConf conf = createThrottlingConf("dummyProxy2");

		throttlingManager.incrementThrottling(conf);
		throttlingManager.incrementThrottling(conf);
		throttlingManager.incrementThrottling(conf);
	}

	@Test
	public void throttlingWithDefaultServiceSet() throws ThrottlingException {
		init(mockServicesWithDefaultService());

		ThrottlingConf conf = createThrottlingConf("nonExistentProxy");

		ThrottlingConf anotherConf = createThrottlingConf("anotherNonExistentProxy");

		throttlingManager.incrementThrottling(conf);
		throttlingManager.incrementThrottling(conf);
		throttlingManager.incrementThrottling(anotherConf);
		throttlingManager.incrementThrottling(anotherConf);

		// check if there are two different ThrottlingControlItem for different
		// proxy.[default version]
		Assert.assertEquals(
				2,
				throttlingManager.getThrottlingControlItem(
						"nonExistentProxy").getCurNumOnLine());
		Assert.assertEquals(
				2,
				throttlingManager.getThrottlingControlItem(
						"anotherNonExistentProxy").getCurNumOnLine());

		throttlingManager.decrementThrottling(anotherConf);
		throttlingManager.decrementThrottling(anotherConf);
		throttlingManager.decrementThrottling(conf);
		throttlingManager.decrementThrottling(conf);

		// back to normal, after decrementing
		Assert.assertEquals(
				0,
				throttlingManager.getThrottlingControlItem(
						"nonExistentProxy").getCurNumOnLine());
		Assert.assertEquals(
				0,
				throttlingManager.getThrottlingControlItem(
						"anotherNonExistentProxy").getCurNumOnLine());
	}

	@Test
	public void throttlingWithDefaultServiceSetAndSameProxy()
			throws ThrottlingException, InterruptedException {
		init(mockServicesWithDefaultService());

		final ThrottlingConf conf = createThrottlingConf("nonExistentProxy");
		final ThrottlingConf anotherConf = createThrottlingConf("nonExistentProxy");

		final CountDownLatch latch = new CountDownLatch(4);
		final CountDownLatch finishFlag = new CountDownLatch(4);

		startNewThread(new ThreadOperations() {
			@Override
			public void execute() throws Exception {
				latch.await();
				throttlingManager.incrementThrottling(conf);

				Thread.sleep(1000);
				throttlingManager.decrementThrottling(conf);
				finishFlag.countDown();
			}
		});

		startNewThread(new ThreadOperations() {
			@Override
			public void execute() throws Exception {
				latch.await();
				throttlingManager.incrementThrottling(conf);

				Thread.sleep(1000);
				throttlingManager.decrementThrottling(conf);
				finishFlag.countDown();
			}
		});

		startNewThread(new ThreadOperations() {
			@Override
			public void execute() throws Exception {
				latch.await();
				throttlingManager.incrementThrottling(anotherConf);

				Thread.sleep(1000);
				throttlingManager.decrementThrottling(anotherConf);
				finishFlag.countDown();
			}
		});

		startNewThread(new ThreadOperations() {
			@Override
			public void execute() throws Exception {
				latch.await();
				throttlingManager.incrementThrottling(anotherConf);

				Thread.sleep(1000);
				throttlingManager.decrementThrottling(anotherConf);
				finishFlag.countDown();
			}
		});

		// wait for all thread ready
		Thread.sleep(1000);

		latch.countDown();
		latch.countDown();
		latch.countDown();
		latch.countDown();

		finishFlag.await();

		// back to normal, after decrementing
		Assert.assertEquals(
				0,
				throttlingManager.getThrottlingControlItem(
						"nonExistentProxy").getCurNumOnLine());
	}

	protected void startNewThread(final ThreadOperations to) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					to.execute();
				} catch (Exception e) {
				}
			}
		}).start();
	}

	protected ThrottlingConf createThrottlingConf(final String name) {
		ThrottlingConf conf = new ThrottlingConf() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return ThrottlingConf.class;
			}

			@Override
			public String value() {
				return name;
			}
		};
		return conf;
	}

	protected List<Service> mockServices() {
		List<Service> services = new ArrayList<Service>();
		for (int i = 1; i <= 5; i++) {
			Service service = new Service();
			service.setServiceTypes(new String[] { "dummyProxy" + i });
			service.setMaxAllowedOnlineNumber(i);
			services.add(service);
		}
		return services;
	}

	protected List<Service> mockServicesWithDefaultService() {
		List<Service> services = new ArrayList<Service>();
		services.addAll(mockServices());
		// add default
		Service defaultService = new Service();
		defaultService.setServiceTypes(new String[] { "default_service" });
		defaultService.setMaxAllowedOnlineNumber(9);
		services.add(defaultService);
		return services;
	}

	interface ThreadOperations {
		void execute() throws Exception;
	}
}
