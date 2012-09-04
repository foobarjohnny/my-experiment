/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.image;

/**
 * ImageRequest.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Apr 12, 2011
 *
 */
public class ImageRequest
{
    private String layers;
    private String xCoordinate;
    private String yCoordinate;
    private String zoom;
    private String version;
    private String style;
    private String extendParams = "";
    
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("layers="+layers)
          .append(",xCoordinate="+xCoordinate)
          .append(",yCoordinate="+yCoordinate)
          .append(",zoom="+zoom)
          .append(",version="+version)
          .append(",style="+style)
          .append(",extendParams="+extendParams);
        return sb.toString();
    }
    
    
    public String getLayers()
    {
        return layers;
    }
    public void setLayers(String layers)
    {
        this.layers = layers;
    }
    public String getXCoordinate()
    {
        return xCoordinate;
    }
    public void setXCoordinate(String coordinate)
    {
        xCoordinate = coordinate;
    }
    public String getYCoordinate()
    {
        return yCoordinate;
    }
    public void setYCoordinate(String coordinate)
    {
        yCoordinate = coordinate;
    }
    public String getZoom()
    {
        return zoom;
    }
    public void setZoom(String zoom)
    {
        this.zoom = zoom;
    }
    public String getVersion()
    {
        return version;
    }
    public void setVersion(String version)
    {
        this.version = version;
    }
    public String getStyle()
    {
        return style;
    }
    public void setStyle(String style)
    {
        this.style = style;
    }
    public String getExtendParams()
    {
        return extendParams;
    }
    public void setExtendParams(String extendParams)
    {
        this.extendParams = extendParams;
    }
    
   
}
