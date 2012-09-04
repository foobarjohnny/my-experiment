package com.telenav.cserver.backend.datatypes.addresssharing;

public class ReceiverUserInfo {
	private String ptn;
	private String name;

   public String toString() {
       StringBuilder sb = new StringBuilder();
       sb.append("ReceiverUserInfo=[");
       sb.append(", ptn=").append(this.ptn);
       sb.append(", name=").append(this.name);
       sb.append("]");
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
}
