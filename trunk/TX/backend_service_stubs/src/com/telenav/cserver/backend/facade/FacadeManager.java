/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.facade;

import com.telenav.cserver.backend.facade.billing.BillingFacade;
import com.telenav.cserver.backend.facade.billing.BillingFacadeImpl;

/**
 * FacadeManager
 * @author kwwang
 *
 */
public class FacadeManager {
	private static BillingFacade billingFacade = new BillingFacadeImpl();

	public static BillingFacade getBillingFacade() {
		return billingFacade;
	}

}
