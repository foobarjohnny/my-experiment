/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.guidetone;

import java.util.Date;

/**
 * BackendGuideToneOrder.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-9-3
 *
 */
public class BackendGuideToneOrder
{
    private long userId;
    private long id;
    private String name;
    private boolean defaultGuideToneOrder;
    private Date createTime;
    private Date updateTime;
    private double price;
    private String desc;
    
    
    public long getUserId()
    {
        return userId;
    }
    public void setUserId(long userId)
    {
        this.userId = userId;
    }
    public long getId()
    {
        return id;
    }
    public void setId(long id)
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public boolean isDefaultGuideToneOrder()
    {
        return defaultGuideToneOrder;
    }
    public void setDefaultGuideToneOrder(boolean defaultGuideToneOrder)
    {
        this.defaultGuideToneOrder = defaultGuideToneOrder;
    }
    public Date getCreateTime()
    {
        return createTime;
    }
    public void setCreateTime(Date creatTime)
    {
        this.createTime = creatTime;
    }
    public Date getUpdateTime()
    {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }
    public double getPrice()
    {
        return price;
    }
    public void setPrice(double price)
    {
        this.price = price;
    }
    
    public String getDesc()
    {
        return desc;
    }
    public void setDesc(String desc)
    {
        this.desc = desc;
    }
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("userId="+userId+",");
        sb.append("id="+id+",");
        sb.append("name="+name+",");
        sb.append("defaultGuideToneOrder="+defaultGuideToneOrder+",");
        sb.append("price="+price+",");
        sb.append("desc="+desc+",");
        sb.append("createTime="+createTime+",");
        sb.append("updateTime="+updateTime);
        return sb.toString();
    }
    
}
