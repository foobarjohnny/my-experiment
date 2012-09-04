package com.telenav.cserver.poi.holder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.telenav.cserver.common.resource.AbstractResourceHolder;
import com.telenav.cserver.common.resource.ResourceContent;
import com.telenav.cserver.common.resource.ResourceFactory;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * 
 * @author qyuan@telenavsoftware.com
 * add a holder to Parse the file device/PoiLocalParameters.xml
 * get some POI category data from it
 * */

public class PoiLocalParametersHolder extends AbstractResourceHolder{

	public static final String CONTENT_KEY = "PoiLocalParameters";
	
	private static final String CARRIER = "carrier";
	private static final Logger log = Logger.getLogger(PoiLocalParametersHolder.class);
	private static final String ID = "id";
	private static final String IMAGE_ID = "imageId";
	private static final String INDICATOR = "indicator";
	private static final String MOST_POPULAR = "mostPopular";
	private static final String NAME = "name";
	private static final String POI_FIND_VERSION = "poifindversion";
	private static final String CATEGORY = "category";
	private static final String SUB_CATEGORY = "subcategory";
	private static final String WITH_SUB_CATEGORY = "1";
	
	@Override
	public ResourceContent createObject(String s, UserProfile userprofile,
			TnContext tncontext)
	{
		Element element = (Element)ResourceFactory.createResource(this, userprofile, tncontext);
		ResourceContent resourceContent = new ResourceContent();
		if( element == null )
		{
			log.debug( "Element is null" );
			HashMap<String, PoiLocalParametersCollection> map = new HashMap<String, PoiLocalParametersCollection>();
			resourceContent.setProperty(CONTENT_KEY, map);
		}
		else
		{
			HashMap<String, PoiLocalParametersCollection> map = parse(element);
			resourceContent.setProperty(CONTENT_KEY, map);
		}
		return resourceContent;
	}
	
	private HashMap<String, PoiLocalParametersCollection> parse(Element root)
    {
		//There are four levels in the file: root, carrier, category, sub category
		//carrier level:
		NodeList carrierNodeList = root.getElementsByTagName(CARRIER);
		HashMap<String, PoiLocalParametersCollection> map = new HashMap<String, PoiLocalParametersCollection>();
		for(int i = 0; i < carrierNodeList.getLength(); i ++)
		{
			PoiLocalParametersCollection plpc = new PoiLocalParametersCollection();
			Element carrierNode = (Element)carrierNodeList.item(i);
			//category level
			NodeList categoryList = carrierNode.getElementsByTagName(CATEGORY);
			for(int j = 0; j < categoryList.getLength(); j ++)
			{
				PoiCategoryItem pci = new PoiCategoryItem();
				Element category = (Element)categoryList.item(j);
				pci.setId(category.getAttribute(ID));
				pci.setImageId(category.getAttribute(IMAGE_ID));
				String indicator = category.getAttribute(INDICATOR);
				//sub category level, first to see whether the catogory has a sub category or not
				if(WITH_SUB_CATEGORY.equals(indicator))
				{
					NodeList subCategoryNodeList = category.getElementsByTagName(SUB_CATEGORY);
					List<PoiCategoryItem> subCategoryList = new ArrayList<PoiCategoryItem>();
					for(int k = 0; k < subCategoryNodeList.getLength(); k ++)
					{
						PoiCategoryItem subCategory = new PoiCategoryItem();
						Element concret = (Element)subCategoryNodeList.item(k);
						subCategory.setId(concret.getAttribute(ID));
						subCategory.setImageId(concret.getAttribute(IMAGE_ID));
						subCategory.setIndicator(concret.getAttribute(INDICATOR));
						subCategory.setMostPopular(concret.getAttribute(MOST_POPULAR));
						subCategory.setName(concret.getAttribute(NAME));
						subCategoryList.add(subCategory);
					}
					pci.setSubCategory(subCategoryList);
				}
				pci.setIndicator(category.getAttribute(INDICATOR));
				pci.setMostPopular(category.getAttribute(MOST_POPULAR));
				pci.setName(category.getAttribute(NAME));
				plpc.addSubCategory(pci);
			}
			
			plpc.setCarrierName(carrierNode.getAttribute(NAME));
			plpc.setPoiFindVersion(carrierNode.getAttribute(POI_FIND_VERSION));
			map.put(carrierNode.getAttribute(NAME), plpc);
		}
		
		return map;
    }
	
	public PoiLocalParametersCollection getPoiLocalParametersCollection(UserProfile userprofile)
	{
		String carrier = userprofile.getCarrier();
		String locale = userprofile.getLocale();
		ResourceContent content = getResourceContent(userprofile, null);
		HashMap<String, PoiLocalParametersCollection> map =  (HashMap<String, PoiLocalParametersCollection>)content.getProperty( CONTENT_KEY );
		return (PoiLocalParametersCollection)map.get(carrier+"_"+locale);
	}
}
