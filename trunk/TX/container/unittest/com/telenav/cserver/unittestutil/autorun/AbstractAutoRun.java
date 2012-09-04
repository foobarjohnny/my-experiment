/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.unittestutil.autorun;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AbstractAutoRun.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-20
 */
public abstract class AbstractAutoRun {
	protected List<String> classList = new ArrayList<String>();
	protected Map<String,List<String>> avoidMap = new HashMap<String,List<String>>();//not to be called method
	protected Map<String,List<String>> callMap = new HashMap<String,List<String>>();//specific method to be called 
	protected Map<String, Object> instanceMap = new HashMap<String, Object>();
	
	private ENUM_CallerType callerType;//the caller/executor type. There are only 2 types by now: call setter/getter and constructor method
	/**
	 * @TODO return the class list which contains all the classes registered
	 * @return List<String>
	 */
	public List<String> getClassList(){
		return classList;
	}
	/**
	 * @TODO  register a class whose method will be invoked.
	 * @param clazz  A string represent the class whole name
	 */
	public void registerClass(String clazz){
		classList.add(clazz);
		if(!avoidMap.containsKey(clazz)){
			avoidMap.put(clazz, new ArrayList<String>());
		}
		if(!callMap.containsKey(clazz)){
			callMap.put(clazz, new ArrayList<String>());
		}
		if(!instanceMap.containsKey(clazz)){
			instanceMap.put(clazz, null);
		}
	}
	/**
	 * @TODO  Specify which method of the class you do not want to call
	 * @param clazz
	 * @param methodName
	 * @throws Exception
	 */
	public void registerAvoidMethod(String clazz, String methodName) throws Exception{
		List<String> list = avoidMap.get(clazz);
		if(list != null){
			list.add(methodName);
		}
	}
	/**
	 * @TODO  Specify which method of the class you do want to call exactly
	 * @param clazz
	 * @param methodName
	 * @throws Exception
	 */
	public void registerCallMethod(String clazz, String methodName) throws Exception{
		List<String> list = callMap.get(clazz);
		if(list != null){
			list.add(methodName);
		}
	}
	public void registerInstance(String clazz, Object instance){
		instanceMap.put(clazz, instance);
	}
	/**
	 * @TODO  Get the caller type
	 * @return
	 */
	public ENUM_CallerType getCallerType(){
		return this.callerType;
	}
	/**
	 * @TODO  Set the caller type
	 * @return
	 */
	public void setCallerType(ENUM_CallerType t){
		this.callerType = t;
	}
	
	
	/**
	 * @TODO	call all the methods of all the class registered
	 * @throws Exception
	 */
	
	public void call()throws Exception{
		for(String clazz : classList){
			if(callerType == ENUM_CallerType.CONSTRUCTORTYPE){
				executeConstructorMethod(clazz);
			}else if(callerType == ENUM_CallerType.GSETTERTYPE){
				executeMethod(clazz);
			}
		}
		
	}
	
	
	/***
	 * @TODO	invoke all the Constructor Method of a class. The method name must pass the filter which defined in subclass of AbstractAutoRun.java
	 * @param clazz
	 */
	private void executeConstructorMethod(String clazz){
		Class<?> c;
		Object[] parameterTypes=null;
		try {
			c = ClassLoader.getSystemClassLoader().loadClass(clazz);
			
			Constructor<?>[] cms = c.getDeclaredConstructors();
			for(Constructor<?> cm : cms){
				cm.setAccessible(true);
				parameterTypes = cm.getParameterTypes();
				cm.newInstance( getParameters(parameterTypes));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			
		} catch (IllegalArgumentException e) {
			System.err.println("can not call constructor of "+clazz+" with parameter"+Arrays.asList(parameterTypes)+" ==>:"+e.getMessage()+e.getCause());
		} catch (InstantiationException e) {
			System.err.println("can not call constructor of "+clazz+" with parameter"+Arrays.asList(parameterTypes)+" ==>:"+e.getMessage()+e.getCause());
		} catch (IllegalAccessException e) {
			System.err.println("can not call constructor of "+clazz+" with parameter"+Arrays.asList(parameterTypes)+" ==>:"+e.getMessage()+e.getCause());
		} catch (InvocationTargetException e) {
			System.err.println("can not call constructor of "+clazz+" with parameter"+Arrays.asList(parameterTypes)+" ==>:"+e.getMessage()+e.getCause());
		}
		
	}
	/***
	 * @TODO	invoke all the Method of a class. The method name must pass the filter which defined in subclass of AbstractAutoRun.java
	 * @param clazz
	 */
	private void executeMethod(String clazz){
		Object instance = null;
		try {
			instance = getInstance(clazz);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Method[] ms = instance.getClass().getDeclaredMethods();
		Object[] parameterTypes;
		String methodName;
		for(Method m : ms){
			m.setAccessible(true);
			parameterTypes = m.getParameterTypes();
			try {
				methodName = m.getName();
				
				//filter method
				if(shouldInvoked(clazz, m)){
					m.invoke(instance, getParameters(parameterTypes));
				}
			} catch (IllegalArgumentException e) {
				System.err.println("can not call "+clazz+"."+m.getName()+"() ==>:"+e.getMessage()+e.getCause());
			} catch (IllegalAccessException e) {
				System.err.println("can not call "+clazz+"."+m.getName()+"() ==>:"+e.getMessage()+e.getCause());
			} catch (InvocationTargetException e) {
				System.err.println("can not call "+clazz+"."+m.getName()+"() ==>:"+e.getMessage()+e.getCause());
			}
		}
	}
	private boolean shouldInvoked(String clazz,Method m){
		String methodName = m.getName();
		//1. if in avoidMap, return false
		if(existsInAvoidMap(clazz, methodName)) return false;
		//2. if in callMap, return true
		if(existsInCallMap(clazz, methodName)) return true;
		return filter(m);
	}
	
	
	/**
	 * @TODO  judge a method of a class whether need to call
	 * @param clazz
	 * @param methodName
	 * @return
	 */
	private boolean existsInAvoidMap(String clazz, String methodName){
		List<String> list = avoidMap.get(clazz);
		if(list == null) return false;
		
		if(list.contains(methodName)) return true;
		else return false;
	}
	/**
	 * @TODO  judge a method of a class whether need to call specially
	 * @param clazz
	 * @param methodName
	 * @return
	 */
	private boolean existsInCallMap(String clazz, String methodName){
		List<String> list = callMap.get(clazz);
		if(list == null) return false;
		
		if(list.contains(methodName)) return true;
		else return false;
	}
	/**
	 * @TODO  return a instance of a class
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	protected Object getInstance(String clazz) throws Exception{
		if(instanceMap.containsKey(clazz)){
			Object obj = instanceMap.get(clazz);
			if(obj != null) return obj;
		}
		
		Object obj = null;
		Class<?> c = ClassLoader.getSystemClassLoader().loadClass(clazz);
		Object[] parameterTypes;
		Constructor<?>[] cms = c.getDeclaredConstructors();
		for(Constructor<?> cm : cms){
			cm.setAccessible(true);
			parameterTypes = cm.getParameterTypes();
			obj = cm.newInstance( getParameters(parameterTypes));
			if(obj != null) return obj;
		}
		return obj;
	}
	/**
	 * @TODO  We must offer an array contains correct parameter for invoking a method
	 * @param parameterTypes
	 * @return
	 */
	protected Object[] getParameters(Object[] parameterTypes){
		Object[] ret = new Object[parameterTypes.length];
		Object type;
		for(int i=0;i<parameterTypes.length;i++){
			type = parameterTypes[i];
			if(type == byte.class){
				ret[i] = (byte)65;
			}else if(type == char.class){
				ret[i] = 'a';
			}else if(type == boolean.class){
				ret[i] = true;
			}else if(type == int.class){
				ret[i] = (int)1;
			}else if(type == short.class){
				ret[i] = (short)1;
			}else if(type == long.class){
				ret[i] = (long)3L;
			}else if(type == double.class){
				ret[i] = (double)1.0;
			}else if(type == float.class){
				ret[i] = (float)1.0;
			}else if(type == String.class){
				ret[i] = "a string";
			}else{
				if(((Class)type).isLocalClass())
					ret[i] = getObjectParameter((Class)type);
				else
					ret[i] = null;
			}
		}
		
		return ret;
	}
	
	/**
	 * @TODO  the subclass must realize this method. This method decide what the parameter should be if it is not a basic type.
	 *        generally, we return a null for simple and convenient.
	 * @param c
	 * @return
	 */
	protected abstract Object getObjectParameter(Class c);
	/**
	 * @TODO  the subclass must realize this method. It determine whether a method should be invoked.
	 * @param name
	 * @return
	 */
	protected abstract boolean filter(Method m);

}
