/**
 * (c) Copyright 2008 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.throttling;

import java.util.List;

import org.apache.log4j.Logger;

import com.telenav.kernel.util.datatypes.TnContext;

/**
 * Throttling Manager, to apply throttling action on API level
 * 
 * @author yqchen@telenav.cn
 * @version 1.0 2008-7-24
 * 
 */
public class ThrottlingManager
{
	protected static Logger logger = Logger.getLogger(ThrottlingManager.class);

	
	static ThrottlingConfiguration tc = ThrottlingConfiguration.getInstance();
	
	/**
	 * service list
	 */
	static List serviceList = tc.getServiceList();

	/**
	 * return service list for management, i.e, web console, i.e.
	 * 
	 * @return
	 */
	public static List getServiceList()
	{
		return serviceList;
	}

	/**
	 * start a API call
	 * 
	 * @param serviceType
	 * @param tnContext
	 * @return
	 */
	public static boolean startAPICall(String serviceType, TnContext tnContext)
	{
		if (!tc.isEnabled())
		{
			return true;
		}
		if (logger.isDebugEnabled())
		{
//			logger.debug("=======================================");
//			logger.debug("starting startAPICall for service:"
//					+ serviceType);
//			logger.debug("tnContext:" + tnContext);
		}

		for (int i = 0, size = serviceList.size(); i < size; i++)
		{
			Service service = (Service) serviceList.get(i);
			boolean isInService = service.isInService(serviceType);
			if (isInService)
			{
				/**
				 * can we add one more online user? If so, increase the online
				 * number and add context
				 */
				boolean hasReachMaxOnlineNumber = false;
				synchronized (service)
				{
					hasReachMaxOnlineNumber = service.hasReachMaxOnlineNumber();

					if (!hasReachMaxOnlineNumber)
					{

						service.increaseOnlineUser();
						if (tnContext != null)
						{
							service.addOnlineContext(tnContext, serviceType);
						}

						if (logger.isDebugEnabled())
						{
							logger
									.debug("increaseOnlineUser, total online number: "
											+ service.getOnlineNumber()
											+ " for service:" + serviceType);
//							for (int jj = 0; jj < service
//									.getOnlineContextList().size(); jj++)
//							{
//								logger.debug("online context: "
//										+ service.getOnlineContextList()
//												.get(jj));
//							}
						}
					}
				}

				if (hasReachMaxOnlineNumber)
				{
					logger.fatal("hasReach MaxOnlineNumber: "
							+ service.getMaxAllowedOnlineNumber()
							+ " for service:" + serviceType);
					//TODO: send out alert email
				}
				if (logger.isDebugEnabled())
				{
//					logger.debug("hasReachMaxOnlineNumber: "
//							+ hasReachMaxOnlineNumber + " for service:"
//							+ serviceType);
//					logger.debug("ending startServiceCall for service:"
//							+ serviceType);
//					logger.debug("=======================================");
//					logger.debug("");
				}
				return !hasReachMaxOnlineNumber;

			}
		}
		if (logger.isDebugEnabled())
		{
			logger.debug("No matched service:" + serviceType);
			logger.debug("ending startServiceCall for service:" + serviceType);
		}
		return true;
	}

	/**
	 * end a API call
	 * 
	 * @param serviceType
	 * @param tnContext
	 */
	public static void endAPICall(String serviceType, TnContext tnContext)
	{
		if (!tc.isEnabled())
		{
			return;
		}

		if (logger.isDebugEnabled())
		{
//			logger.debug("starting finishServiceCall for service:"
//					+ serviceType);
//			logger.debug("tnContext:" + tnContext);
		}

		for (int i = 0, size = serviceList.size(); i < size; i++)
		{
			Service service = (Service) serviceList.get(i);
			boolean isInService = service.isInService(serviceType);
			if (isInService)
			{
				synchronized (service)
				{
					service.decreaseOnlineUser();
					if (tnContext != null)
					{
						service.removeOnlineContext(tnContext);
					}

					if (logger.isDebugEnabled())
					{
						logger
								.debug("decreaseOnlineUser, total online number: "
										+ service.getOnlineNumber()
										+ " for service:" + serviceType);
//						for (int jj = 0; jj < service.getOnlineContextList()
//								.size(); jj++)
//						{
//							logger.debug("online context: "
//									+ service.getOnlineContextList().get(jj));
//						}
					}
				}

//				if (logger.isDebugEnabled())
//				{
//					logger.debug("ending finishServiceCall for service:"
//							+ serviceType);
//				}
				return;
			}
		}
		if (logger.isDebugEnabled())
		{
			logger.debug("No matched service for service:" + serviceType);
			logger
					.debug("ending finishServiceCall for service:"
							+ serviceType);
		}
	}

//	public static void main(String[] args)
//	{
//		ThrottlingManager tm = new ThrottlingManager();
//		for (int i = 0; i < tm.serviceList.size(); i++)
//		{
//			Service service = (Service) serviceList.get(i);
//			System.out.println("service " + i + " " + service.getName() + " "
//					+ service.getMaxAllowedOnlineNumber());
//		}
//	}

}
