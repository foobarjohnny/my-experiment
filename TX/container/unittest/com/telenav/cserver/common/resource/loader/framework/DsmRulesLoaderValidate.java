package com.telenav.cserver.common.resource.loader.framework;

import java.util.Map;

import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.common.resource.constant.HolderType;
import com.telenav.cserver.common.resource.holder.impl.DsmRuleHolder;
import com.telenav.cserver.common.resource.loader.framework.bean.DsmRulesBean;
import com.telenav.cserver.common.resource.loader.framework.constant.LoaderOrderValidateDsmRulesConstant;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.unittestutil.UnittestUtil;
import com.telenav.kernel.util.datatypes.TnContext;

public class DsmRulesLoaderValidate
{
	private static DsmRuleHolder dsmHolder = ResourceHolderManager.getResourceHolder(HolderType.DSM_TYPE);

	public DsmRulesBean validate(UserProfile profile, TnContext tnContext)
	{
		Map map = dsmHolder.getDsmResponses(profile, tnContext);
		System.out.println(map);

		DsmRulesBean bean = new DsmRulesBean();
		if (map != null)
		{
			bean.setAdEngine((String) map.get(LoaderOrderValidateDsmRulesConstant.AD_ENGINE));
			bean.setAdType((String) map.get(LoaderOrderValidateDsmRulesConstant.AD_TYPE));
			bean.setAlertDataSrc((String) map.get(LoaderOrderValidateDsmRulesConstant.ALERT_DATA_SRC));
			bean.setDefaultCountry((String) map.get(LoaderOrderValidateDsmRulesConstant.DEFAULT_COUNTRY));
			bean.setFlowDataSrc((String) map.get(LoaderOrderValidateDsmRulesConstant.FLOW_DATA_SRC));
			bean.setIsAndroid((String) map.get(LoaderOrderValidateDsmRulesConstant.IS_ANDROID));
			bean.setNeedReview((String) map.get(LoaderOrderValidateDsmRulesConstant.NEED_REVIEW));
			bean.setNeedSponsor((String) map.get(LoaderOrderValidateDsmRulesConstant.NEED_SPONSOR));
			bean.setPoiFinderVersion((String) map.get(LoaderOrderValidateDsmRulesConstant.POI_FINDER_VERSION));
			bean.setSupportTouch((String) map.get(LoaderOrderValidateDsmRulesConstant.SUPPORT_TOUCH));
		}

		System.out.println(bean);

		return bean;
	}

	public static void main(String[] args)
	{
		UserProfile userProfile = UnittestUtil.createUserProfile();

		DsmRulesLoaderValidate validate = new DsmRulesLoaderValidate();
		validate.validate(userProfile, null);
	}
}
