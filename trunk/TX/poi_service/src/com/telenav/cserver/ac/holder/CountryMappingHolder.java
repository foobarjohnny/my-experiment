package com.telenav.cserver.ac.holder;

import java.util.Map;

import com.telenav.cserver.common.resource.AbstractResourceHolder;
import com.telenav.cserver.common.resource.ResourceContent;
import com.telenav.cserver.common.resource.ResourceFactory;
import com.telenav.cserver.common.resource.SpringObjectNameAware;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

public class CountryMappingHolder extends AbstractResourceHolder implements SpringObjectNameAware{

	public static String CONTENT_KEY = "country_mapping";
	@Override
	public ResourceContent createObject(String key, UserProfile profile, TnContext tnContext) {
		// TODO Auto-generated method stub
		CountryMessages countryMessages = (CountryMessages) ResourceFactory.createResource(this, profile, tnContext);
		ResourceContent resourceContent = null;
		if( countryMessages == null )
		{
			countryMessages = new CountryMessages();
		}
		resourceContent = new ResourceContent();
		resourceContent.setProperty( CONTENT_KEY, countryMessages );
		return resourceContent;
	}

	@Override
	public String getObjectName() {
		// TODO Auto-generated method stub
		return "CountryMappingParameters";
	}

	public CountryMessages getCountryMapping( UserProfile profile, TnContext tnContext )
	{
		ResourceContent content = getResourceContent(profile, tnContext);
		return (CountryMessages) content.getProperty( CONTENT_KEY );
	}
}
