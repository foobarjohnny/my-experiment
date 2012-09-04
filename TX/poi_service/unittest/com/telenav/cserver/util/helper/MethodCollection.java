package com.telenav.cserver.util.helper;

import java.lang.reflect.Method;
import java.util.ArrayList;

public final class MethodCollection {

	ArrayList<Method> common = new ArrayList();
	ArrayList<Method[]> coupled = new ArrayList();
	
	public void setCommon(ArrayList common){
		this.common = common;
	}
	
	public ArrayList getCommon(){
		return this.common;
	}
	
	public void setCoupled(ArrayList coupled){
		this.coupled = coupled;
	}
	
	public ArrayList getCoupled(){
		return this.coupled;
	}
	
}
