package com.telenav.cserver.common.resource.loader.framework;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.telenav.cserver.common.resource.loader.framework.bean.DevicePropertiesBean;
import com.telenav.cserver.common.resource.loader.framework.bean.DsmRulesBean;
import com.telenav.cserver.common.resource.loader.framework.bean.UserProfileListBean;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.configuration.ConfigurationException;
import com.telenav.cserver.framework.configuration.Configurator;
import com.telenav.cserver.unittestutil.UnittestUtil;

/**
 * Currently, we have configured the resource loading path/orders by
 * resource_loader.xml. If we want to change it for one resource, it is easy to
 * cause regression. So we need a unittest frame work to check it.
 * 
 * Generate UserPerfile using Spring IoC form ValidateConfig.xml output the
 * result to system.out
 * 
 * @author gwwang
 * 
 */
public class LoaderValidate
{
	public static void main(String[] args)
	{
		LoaderValidate validate = new LoaderValidate();

		String path = validate.getConfigFilePath();

		try
		{
			List<UserProfile> userProfiles = validate.getUserProfileList(path);

			Map<String, DevicePropertiesBean> devicePropertiesResults = validate.validateDevicePropertiesLoaderOrder(userProfiles);

			Map<String, DsmRulesBean> dsmRulesResults = validate.validateDsmRulesLoaderOrder(userProfiles);

			validate.outputDevicePropertiesValidateResults(devicePropertiesResults);

			validate.outputDsmRulesValidateResults(dsmRulesResults);

		} catch (ConfigurationException e)
		{
			e.printStackTrace();
		}
	}

	private void outputDsmRulesValidateResults(Map<String, DsmRulesBean> dsmRulesResults)
	{
		System.out.println("dsmRulesResults");
		Set<String> set = dsmRulesResults.keySet();
		for (String key : set)
		{
			System.out.printf("Key:%s \n\t\tResult:%s\n", key, dsmRulesResults.get(key));
		}
	}

	private void outputDevicePropertiesValidateResults(Map<String, DevicePropertiesBean> devicePropertiesResults)
	{
		System.out.println("devicePropertiesResults");
		Set<String> set = devicePropertiesResults.keySet();
		for (String key : set)
		{
			System.out.printf("Key:%s \n\t\tResult:%s\n", key, devicePropertiesResults.get(key));
		}
	}

	private List<UserProfile> getUserProfileList(String path) throws ConfigurationException
	{
		UserProfileListBean bean = (UserProfileListBean) Configurator.getObject(path, "UserProfileList");
		List<UserProfile> userProfiles = bean.getUserProfiles();
		return userProfiles;
	}

	private Map<String, DsmRulesBean> validateDsmRulesLoaderOrder(List<UserProfile> userProfiles)
	{
		Map<String, DsmRulesBean> resultMap = new LinkedHashMap<String, DsmRulesBean>();
		for (UserProfile userProfile : userProfiles)
		{
			DsmRulesLoaderValidate dsmRulesLoaderValidate = new DsmRulesLoaderValidate();
			DsmRulesBean result = dsmRulesLoaderValidate.validate(userProfile, null);
			String resultKey = generateKey(userProfile);

			resultMap.put(resultKey, result);
		}
		return resultMap;
	}

	private Map<String, DevicePropertiesBean> validateDevicePropertiesLoaderOrder(List<UserProfile> userProfiles)
	{
		Map<String, DevicePropertiesBean> resultMap = new LinkedHashMap<String, DevicePropertiesBean>();
		for (UserProfile userProfile : userProfiles)
		{
			DevicePropertiesLoaderValidate devicePropertiesLoaderValidate = new DevicePropertiesLoaderValidate();
			DevicePropertiesBean result = devicePropertiesLoaderValidate.validate(userProfile, null);
			String resultKey = generateKey(userProfile);

			resultMap.put(resultKey, result);
		}
		return resultMap;
	}

	private String generateKey(UserProfile userProfile)
	{
		return userProfile.getCarrier() + "_" + userProfile.getPlatform() + "_" + userProfile.getVersion();
	}

	private String getConfigFilePath()
	{
		String path = UnittestUtil.getAbsURLPath("./configurator/ValidateConfig.xml");

		// ClassPathXmlApplicationContext needs added "file:" before abs path
		if (!path.startsWith("file:"))
		{
			path = "file:" + path;
		}
		return path;
	}
}
