package com.telenav.cserver.backend.datatypes.addresssharing;

public class ContactInfo {
    public static final String NAME_SPLITOR = ",";
    
	private String ptn;
	private String name;

   public String toString() {
       StringBuilder sb = new StringBuilder();
       sb.append("ContactInfo=[");
       sb.append(", ptn=").append(this.ptn);
       sb.append(", name=").append(this.name);
       sb.append(", firstName=").append(this.getFirstName());
       sb.append(", lastName=").append(this.getLastName());
       return sb.toString();
   }
	   
	public String getPtn() {
		return ptn;
	}
	public void setPtn(String ptn) {
		this.ptn = ptn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getFirstName()
	{
	    if( name == null)
	        return "";
	    String firstName = name;
	    int splitIndex = name.indexOf(NAME_SPLITOR);
	    if( splitIndex != -1)
	    {
	        firstName = name.substring(0,splitIndex);
	    }
	    return firstName;
	}
	
	public String getLastName()
	{
	    if( name == null )
	        return "";
	    String lastName = "";
	    int splitIndex = name.indexOf(NAME_SPLITOR);
        if( splitIndex != -1)
        {
            lastName = name.substring(splitIndex+NAME_SPLITOR.length());
        }
        return lastName;
	}
}
