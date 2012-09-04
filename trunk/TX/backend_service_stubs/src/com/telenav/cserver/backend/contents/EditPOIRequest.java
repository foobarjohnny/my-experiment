/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.contents;

import com.telenav.cserver.backend.datatypes.contents.EditablePoi;

/**
 * The <code>EditPOIRequest</code> to simulate the request to backend service(ContentManager). c-server dev can use this
 * request freely, that only contain two main elements:<code>EditablePoi</code> and userId. <code>EditablePoi</code>
 * save all poi informations.
 * 
 * @author zhjdou 2009-7-16
 */
public class EditPOIRequest
{
    // the poi
    private EditablePoi editPOI;

    /** userId record the id of the user who want to edit the current POI */
    private long userId;

    /** the value from client request <code>ExecutorContext</code> */
    private String context = "dataset=TA";

    // expected value "TA" or "YPC" or "NT" data provider
    private String poiDataSet = "";

    // expected value "TeleAtlas" or "Navteq"
    private String mapDataSet = "";

    /**
     * @return the poiDataSet
     */
    public String getPoiDataSet()
    {
        return poiDataSet;
    }

    /**
     * @param poiDataSet the poiDataSet to set
     */
    public void setPoiDataSet(String poiDataSet)
    {
        this.poiDataSet = poiDataSet;
    }

    /**
     * @return the papDataSet
     */
    public String getMapDataSet()
    {
        return mapDataSet;
    }

    /**
     * @param papDataSet the papDataSet to set
     */
    public void setMapDataSet(String mapDataSet)
    {
        this.mapDataSet = mapDataSet;
    }

    /**
     * @return the context
     */
    public String getContext()
    {
        return context;
    }

    /**
     * @return the editPOI
     */
    public EditablePoi getEditPOI()
    {
        return editPOI;
    }

    /**
     * @param editPOI the editPOI to set
     */
    public void setEditPOI(EditablePoi editPOI)
    {
        this.editPOI = editPOI;
    }

    /**
     * @param context the context to set
     */
    public void setContext(String context)
    {
        this.context = context;
    }

    /**
     * @return the userId
     */
    public long getUserId()
    {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(long userId)
    {
        this.userId = userId;
    }
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("EditPOIRequest[");
        sb.append("userId=");
        sb.append(this.userId);
        sb.append(", editPOI=");
        sb.append(this.editPOI.toString());
        return sb.toString();

    }
}
