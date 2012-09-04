package com.telenav.cserver.util.helper;

import java.lang.reflect.Method;
import java.util.ArrayList;

import com.telenav.cserver.util.helper.MethodCollection;

public class GenericTest {
	
	private static GenericTest instance;
	
	public static GenericTest getInstance(){
		
		if(instance == null){
			instance = new GenericTest();
		}
		return instance;
	}
	
	private GenericTest(){
		
	}
	
	
	/*
	 * 	invoke all the public method in Object instance
	 * 	@Para	Object, DataSource
	 * 	@Return	
	 */
	public void startTest(Object instance, DataSource dataSrc){
		
		MethodCollection methodCollection = CommonTest.getInstance().handle(instance);
		ArrayList<Method[]> coupled = methodCollection.getCoupled();	
		CommonTest.getInstance().handleCoupledMethod(instance, coupled, dataSrc);
	
		ArrayList<Method> common = methodCollection.getCommon();
		CommonTest.getInstance().handleCommonMethod(instance, common, dataSrc, CommonTest.publicModifier);
	}
	
	
	/*
	 * 	invoke all the public method in Object instance that don't contain key word of filter
	 * 	@Para	Object, DataSource, String[]
	 * 	@Return	
	 */
	public void startTest(Object instance, DataSource dataSrc, String[] filter){
		
		MethodCollection methodCollection = CommonTest.getInstance().handle(instance);
		ArrayList<Method[]> coupled = methodCollection.getCoupled();	
		if(filter != null && filter.length > 0)
		{
			for(String ft : filter)
			{
				for(int i = 0; i < coupled.size(); i++)
				{
					if(coupled.get(i)[0].getName().contains(ft) || coupled.get(i)[1].getName().contains(ft))
					{
						coupled.remove(i);
					}
				}
			}
		}
		
		CommonTest.getInstance().handleCoupledMethod(instance, coupled, dataSrc);
		ArrayList<Method> common = methodCollection.getCommon();
		if(filter != null && filter.length > 0)
		{
			for(String ft : filter)
			{
				for(int i = 0; i < common.size(); i++)
				{
					if(common.get(i).getName().contains(ft))
					{
						common.remove(i);
					}
				}
			}
		}
		CommonTest.getInstance().handleCommonMethod(instance, common, dataSrc, CommonTest.publicModifier);
	}
	
	
	/*
	 * 	invoke all the public static method in Object instance
	 * 	@Para	Object, DataSource
	 * 	@Return	
	 */
	public void startStaticTest(Object instance, DataSource dataSrc){
		
		MethodCollection methodCollection = CommonTest.getInstance().handle(instance);
		ArrayList<Method[]> coupled = methodCollection.getCoupled();	
		CommonTest.getInstance().handleCoupledStaticMethod(instance, coupled, dataSrc);
	
		ArrayList<Method> common = methodCollection.getCommon();
		CommonTest.getInstance().handleCommonMethod(instance, common, dataSrc, CommonTest.publicStaticModifier);
	}
	
	
	/*
	 * 	invoke all the public static method in Object instance that don't contain key word of filter
	 * 	@Para	Object, DataSource, String[]
	 * 	@Return	
	 */
	public void startStaticTest(Object instance, DataSource dataSrc, String[] filter){
		
		MethodCollection methodCollection = CommonTest.getInstance().handle(instance);
		ArrayList<Method[]> coupled = methodCollection.getCoupled();	
		if(filter != null && filter.length > 0)
		{
			for(String ft : filter)
			{
				for(int i = 0; i < coupled.size(); i++)
				{
					if(coupled.get(i)[0].getName().contains(ft) || coupled.get(i)[1].getName().contains(ft))
					{
						coupled.remove(i);
					}
				}
			}
		}
		
		CommonTest.getInstance().handleCoupledStaticMethod(instance, coupled, dataSrc);
		ArrayList<Method> common = methodCollection.getCommon();
		if(filter != null && filter.length > 0)
		{
			for(String ft : filter)
			{
				for(int i = 0; i < common.size(); i++)
				{
					if(common.get(i).getName().contains(ft))
					{
						common.remove(i);
					}
				}
			}
		}
		CommonTest.getInstance().handleCommonMethod(instance, common, dataSrc, CommonTest.publicStaticModifier);
	}

}
