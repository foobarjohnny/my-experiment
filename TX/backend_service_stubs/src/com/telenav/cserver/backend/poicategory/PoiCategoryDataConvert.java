/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.poicategory;

import com.telenav.cserver.backend.datatypes.poicategory.POICategory;
import com.telenav.datatypes.category.v10.Category;
import com.telenav.datatypes.category.v10.PoiCategory;
import com.telenav.datatypes.category.v10.PoiCategoryHierarchy;
import com.telenav.kernel.ws.axis2.Axis2Helper;
import com.telenav.services.xnav.v10.PoiCategoriesResponseDTO;
import com.telenav.services.xnav.v10.PoiCategoryRequestDTO;

/**
 * Tool utility
 * @author zhjdou
 * 2010-4-27
 */
public class PoiCategoryDataConvert
{   
    /**
     * convert the customise request to backend type  
     * @param proxy
     * @return
     */
    public static PoiCategoryRequestDTO convertGetPoiCategoryRequestProxy2Request(PoiCategoryRequest proxy){
        PoiCategoryRequestDTO request=new PoiCategoryRequestDTO();
        request.setCategoryId(proxy.getCategoryId());
        request.setContextString(proxy.getStrContext());
        request.setKeyword(proxy.getKeyWord());
        request.setVersion(proxy.getVersion());
        if (proxy.getPoiHierarchyId() == 2) {
            request.setHierarchy(PoiCategoryHierarchy.HOTLIST);
        } else {
            request.setHierarchy(PoiCategoryHierarchy.FULL);
        }
        request.setClientName(PoiCategoryServiceProxy.clientName);
        request.setClientVersion(PoiCategoryServiceProxy.clientVersion);
        request.setTransactionId(PoiCategoryServiceProxy.transactionId);
        return request;
    }
    
    /**
     * convert the backend response to customise type  
     * @param response
     * @return
     */
    public static PoiCategoryResponse convertGetPoiCategoryResponse2Proxy(PoiCategoriesResponseDTO response){
        PoiCategoryResponse proxy=new PoiCategoryResponse();
        proxy.setStatusCode(response.getResponseStatus().getStatusCode());
        proxy.setStatusMsg(response.getResponseStatus().getStatusMessage());
        proxy.setCategories(convertCategoryArray2Proxy(response.getCategories()));
        return proxy;
    }
    
    /**
     * Transform the data to POICategory(Array)
     * 
     * @param proxy []
     * @return
     */
    protected static POICategory[] convertCategoryArray2Proxy(PoiCategory[] arrCategory)
    {
        if (arrCategory != null && Axis2Helper.isNonEmptyArray(arrCategory))
        {
            int length = arrCategory.length;
            POICategory[] proxy = new POICategory[length];
            for (int index = 0; index < length; index++)
            {
                proxy[index] = convertCategory2Proxy(arrCategory[index]);
            }
            return proxy;
        }
        return null;
    }

    /**
     * Transform the data to POICategory backed service can understand it
     * @param proxy
     * @return
     */
    protected static POICategory convertCategory2Proxy(PoiCategory category)
    {
        if (category == null)
        {
            return null;
        }
        POICategory proxy = new POICategory();
        proxy.setDisplaySequence(category.getDisplaySequence());
        proxy.setHierarchy(category.getHierarchy().getValue());
        proxy.setNavigable(category.getIsNavigable());
        proxy.setPopularSearch(category.getIsPopularSearch());
        proxy.setVerision(category.getVersion());
        proxy.setCategoryPath(category.getCategoryPath());
        proxy.setId(category.getId());
        proxy.setLabel(category.getLabel());
        proxy.setParentId(category.getParentId());
        proxy.setSubCategories(convertBasicCategoryArray2Proxy(category.getSubCategories()));
        return proxy;
    }

    
    /**
     * Transform the data to Category(Array)
     * @param proxy []
     * @return
     */
    protected static com.telenav.cserver.backend.datatypes.poicategory.Category[] convertBasicCategoryArray2Proxy(Category[] arrCategory)
    {
        if (arrCategory != null && Axis2Helper.isNonEmptyArray(arrCategory))
        {
            int length = arrCategory.length;
            com.telenav.cserver.backend.datatypes.poicategory.Category[] proxy = new com.telenav.cserver.backend.datatypes.poicategory.Category[length];
            for (int index = 0; index < length; index++)
            {   
                if(arrCategory[index] instanceof PoiCategory) {
                    proxy[index] = convertCategory2Proxy((PoiCategory)arrCategory[index]);
                } else if(arrCategory[index] instanceof Category){
                    proxy[index] = convertBasicCategory2Proxy(arrCategory[index]);
                }
            }
            return proxy;
        }
        return null;
    }

    /**
     * Transform the data to Category backed service can understand it
     * @param proxy
     * @return
     */
    protected static com.telenav.cserver.backend.datatypes.poicategory.Category convertBasicCategory2Proxy(Category category)
    {
        if (category == null)
        {
            return null;
        }
        com.telenav.cserver.backend.datatypes.poicategory.Category proxy = new com.telenav.cserver.backend.datatypes.poicategory.Category();
        proxy.setCategoryPath(category.getCategoryPath());
        proxy.setParentId(category.getParentId());
        proxy.setId(category.getId());
        proxy.setLabel(category.getLabel());
        proxy.setSubCategories(convertBasicCategoryArray2Proxy(category.getSubCategories()));
        return proxy;
    }

}
