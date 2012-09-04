package com.telenav.cserver.backend.datatypes.contents;

public class ReviewOption {
	private long id;
	private String name;//display name
	//user choosed value
	//1=Thumb Up; 0:Thumb down.
	private String value;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
    @Override
    public String toString()
    {
       StringBuffer sb = new StringBuffer();
       sb.append("id=").append(id).append(", ");
       sb.append("name=").append(name).append(", ");
       sb.append("value=").append(value);
       return sb.toString();
    }
	
}
