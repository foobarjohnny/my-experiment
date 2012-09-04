package com.telenav.cserver.adjuggler.util;


import java.util.HashMap;
import java.util.Map;

import com.telenav.cserver.billing.BillingException;
import com.telenav.cserver.billing.BillingManagerInterface;
import com.telenav.cserver.billing.CSUserProduct;
import com.telenav.cserver.billing.GetUpgradePathResp;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.poi.struts.Constant;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * Free Trial checker class that keeps the upgrade rules for premium 30 day free trials and
 * helps check if an existing account can be upgraded to take the 30 day free trial advantage.
 * 
 * @author jianyuz
 *
 */
public class FreeTrialChecker {
	
	//hash map that keeps the rules that which level's product can be upgraded to which 30 day free trial offer code.
	private static final Map<Integer, String> upgradeMapping = new HashMap<Integer, String>(2);
	
	private BillingManagerInterface billingManager;
	
	//initialize the upgrade rule map.
	static{
		upgradeMapping.put(Constant.SERVICE_LEVEL_BOUNDLE, Constant.PRODUCT_CODE_SPRINT_PREM_WITH_30_FREE_TRIAL_FROM_BUNDLE);
		upgradeMapping.put(Constant.SERVICE_LEVEL_LITE, Constant.PRODUCT_CODE_SPRINT_PREM_WITH_30_FREE_TRIAL_FROM_LITE);
	}

	
	public FreeTrialChecker(BillingManagerInterface billingManager) {
		super();
		this.billingManager = billingManager;
	}


	/**
	 * Check if an existing account with particular service level and product code 
	 * can be upgraded take the advantage of premium 30 free trial.
	 * 
	 * @param profile
	 * @param tnContext
	 * @param serviceLevel
	 * @param productCode
	 * @return
	 * @throws BillingException
	 */
	public int isFreeTrialUpgradable(UserProfile profile, TnContext tnContext, 
			int serviceLevel, String productCode)
			throws BillingException {
		int freeTrialUpgradable = 0;
		if (upgradeMapping.containsKey(serviceLevel)) {
			GetUpgradePathResp upgradePathResp = billingManager
					.getUpgradeProducts(profile, new String[] { productCode },
							tnContext);
			if (upgradePathResp != null) {
				CSUserProduct[] upgradeProducts = upgradePathResp
						.getUpgradePath();
				if (null != upgradeProducts) {
					Object freeTrialProductCode = upgradeMapping.get(serviceLevel);
					
					for (CSUserProduct upgradeProduct : upgradeProducts) {
						if (null != freeTrialProductCode && 
								((String)freeTrialProductCode).equalsIgnoreCase(upgradeProduct
										.getProductCode())) {
							freeTrialUpgradable = 1;
							break;
						}
					}
				}
			}

		}
		return freeTrialUpgradable;
	}
	
}
