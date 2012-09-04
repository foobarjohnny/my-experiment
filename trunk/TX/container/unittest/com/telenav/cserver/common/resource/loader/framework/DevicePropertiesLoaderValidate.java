package com.telenav.cserver.common.resource.loader.framework;

import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.common.resource.device.DeviceProperties;
import com.telenav.cserver.common.resource.device.DevicePropertiesHolder;
import com.telenav.cserver.common.resource.loader.framework.bean.DevicePropertiesBean;
import com.telenav.cserver.common.resource.loader.framework.constant.LoaderOrderValidateDeviceConstant;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.unittestutil.UnittestUtil;
import com.telenav.kernel.util.datatypes.TnContext;

public class DevicePropertiesLoaderValidate
{

	private static DevicePropertiesHolder devicePropertiesHolder = ResourceHolderManager.getResourceHolder("device");

	public DevicePropertiesBean validate(UserProfile profile, TnContext tnContext)
	{

		DeviceProperties dp = devicePropertiesHolder.getDeviceProperties(profile, tnContext);

		DevicePropertiesBean bean = new DevicePropertiesBean();
		bean.setAudioFormat(getProperty(dp, LoaderOrderValidateDeviceConstant.AUDIO_FORMAT));
		bean.setAudioProfileKey(getProperty(dp, LoaderOrderValidateDeviceConstant.AUDIO_PROFILE_KEY));
		bean.setImageType(getProperty(dp, LoaderOrderValidateDeviceConstant.IMAGE_TYPE));
		bean.setMaxPayloadSize(getProperty(dp, LoaderOrderValidateDeviceConstant.MAX_PAYLOAD_SIZE));
		bean.setNavEnableLaneAddist(getProperty(dp, LoaderOrderValidateDeviceConstant.NAV_ENABLE_LANE_ASSIST));
		bean.setNeedAudio(getProperty(dp, LoaderOrderValidateDeviceConstant.NEED_AUDIO));
		bean.setNeedBarCollection(getProperty(dp, LoaderOrderValidateDeviceConstant.NEED_BAR_COLLECTION));
		bean.setNeedServerDrivenParamters(getProperty(dp, LoaderOrderValidateDeviceConstant.NEED_SERVER_DRIVEN_PARAMETERS));
		bean.setNewAppVersion(getProperty(dp, LoaderOrderValidateDeviceConstant.NEW_APP_VERSION));
		bean.setParamsVersion(getProperty(dp, LoaderOrderValidateDeviceConstant.PARAMS_VERSION));
		bean.setSmallMemory(getProperty(dp, LoaderOrderValidateDeviceConstant.SMALL_MEMORY));
		bean.setUnsupportedMapIncident(getProperty(dp, LoaderOrderValidateDeviceConstant.UNSUPPORTED_MAP_INCIDENTS));

		System.out.println(bean);

		return bean;
	}

	private String getProperty(DeviceProperties dp, String key)
	{
		return dp.getString(key, key + "NOT_FOUND");
	}

	public static void main(String[] args)
	{
		UserProfile userProfile = UnittestUtil.createUserProfile();

		DevicePropertiesLoaderValidate validate = new DevicePropertiesLoaderValidate();
		validate.validate(userProfile, null);
	}
}
