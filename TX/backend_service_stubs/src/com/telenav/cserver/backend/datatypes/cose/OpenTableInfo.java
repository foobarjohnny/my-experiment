package com.telenav.cserver.backend.datatypes.cose;

public class OpenTableInfo
{
	private String partner;
	
    private boolean isReservable;
    
	private String partnerPoiId;
    
    public String getPartner() 
    {
		return partner;
	}
	public void setPartner(String partner) 
	{
		this.partner = partner;
	}
    public boolean isReservable() 
    {
		return isReservable;
	}
	public void setReservable(boolean isReservable) 
	{
		this.isReservable = isReservable;
	}
	public String getPartnerPoiId()
	{
		return partnerPoiId;
	}
	public void setPartnerPoiId(String partnerPoiId)
	{
		this.partnerPoiId = partnerPoiId;
	}
}
