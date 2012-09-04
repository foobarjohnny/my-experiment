package com.telenav.cserver.util.helper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.telenav.cserver.util.helper.MethodCollection;

import junit.framework.Assert;


public class CommonTest{
	
	public static final int publicModifier = 1;
	public static final int publicStaticModifier = 9;
	
	private static final String debugLevel = "debug";
	private static final String releaseLevel = "release";
	
	private static final int AssertClassLevel = 0;
	private static final int AssertObjectLevel = 1;
	
	private static final String GET = "get";
	private static final String SET = "set";
	
	private static CommonTest instance;
	private CommonTest(){
		
	}
	
	
	public static CommonTest getInstance(){
		if(null == instance){
			instance = new CommonTest();
		}
		return instance;
	}
	
	
	public void handleCommonMethod(Object o, ArrayList<Method> common, DataSource dataCenter, int modifier){
		if(common == null){
			return ;
		}
		
		for(Method m: common){

			if(m.getModifiers() != modifier){
				continue;
			}
			
			Class<?>[] para = m.getParameterTypes();
			Class<?> returnType = m.getReturnType();			
					
			Object returnInstance = dataCenter.lookup(returnType);
			Object[] paraInstance = new Object[para.length];
			paraInstance = dataCenter.lookup(para, paraInstance);			
			
			try {
				
				if(dataCenter.getLogLevel() == debugLevel){
					StringBuffer stringBuf = new StringBuffer();
					stringBuf.append("[Unit Test]: " + m.getName() + "(");
					for(int i = 0; i < para.length; i++){
						stringBuf.append(para[i].getName() + ",");
					}
					stringBuf.append(")");
					int lastComma = stringBuf.lastIndexOf(",");
					if(lastComma > 0 && lastComma < stringBuf.length()){
						stringBuf.replace(lastComma, lastComma+1, "");
					}
					
					System.out.println(stringBuf.toString());
				}
				
				System.out.println("method name : " + m.getName());
				Object actual = m.invoke(o, paraInstance);
				switch(dataCenter.getAssertLevel()){
					case AssertClassLevel:
						if(actual != null && returnInstance != null){
							Assert.assertEquals(returnInstance.getClass(), actual.getClass());
						}else if(actual == null){
							Assert.assertNull(actual);
						}
						
						break;
					case AssertObjectLevel:
						Assert.assertEquals(returnInstance, actual);
						break;
				}
				
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/*
	 *  handle coupled methods
	 *  @para Object o, ArrayList<Method[]> coupled, dataCenter
	 *  @return AnalysisResult
	 */
	public void handleCoupledMethod(Object o, ArrayList<Method[]> copuled, DataSource dataCenter){
		if(copuled == null){
			return ;
		}
		
		Method getMethod = null;
		Method setMethod = null;
		
		for(int i = 0; i < copuled.size(); i++){
			if(copuled.get(i)[0] == null || copuled.get(i)[1] == null){
				System.out.println("get method or set method is missing!");
			}else{
				if(copuled.get(i)[0].getName().toLowerCase().startsWith(CommonTest.SET)){
					setMethod = copuled.get(i)[0];
					getMethod = copuled.get(i)[1];
				}else if(copuled.get(i)[0].getName().toLowerCase().startsWith(CommonTest.GET)){
					setMethod = copuled.get(i)[1];
					getMethod = copuled.get(i)[0];
				}
			}
			
			if((getMethod.getModifiers() != publicModifier) || (setMethod.getModifiers() != publicModifier)){
				continue;
			}
			
			Class<?>[] paraSet = setMethod.getParameterTypes();
			Class<?>[] paraGet = getMethod.getParameterTypes();
			
			Object[] paraInstanceSet = new Object[paraSet.length];
			Object[] paraInstanceGet = new Object[paraGet.length]; 
			paraInstanceSet = dataCenter.lookup(paraSet, paraInstanceSet);
			paraInstanceGet = dataCenter.lookup(paraGet, paraInstanceGet);
			
			
			try {
				
				if(dataCenter.getLogLevel() == releaseLevel){
					StringBuffer stringBuf = new StringBuffer();
					stringBuf.append("[Unit Test]: " + setMethod.getName() + "(");
					for(int l = 0; l < paraSet.length; l++){
						stringBuf.append(paraSet[l].getName() + ",");
					}
					stringBuf.append(")");
					int lastComma = stringBuf.lastIndexOf(",");
					if(lastComma > 0 && lastComma < stringBuf.length()){
						stringBuf.replace(lastComma, lastComma+1, "");
					}
					
					System.out.println(stringBuf.toString());
					
					stringBuf = new StringBuffer();
					stringBuf.append("[Unit Test]: " + getMethod.getName() + "(");
					for(int m = 0; m < paraGet.length; m++){
						stringBuf.append(paraGet[i].getName() + ",");
					}
					stringBuf.append(")");
					lastComma = stringBuf.lastIndexOf(",");
					if(lastComma > 0 && lastComma < stringBuf.length()){
						stringBuf.replace(lastComma, lastComma+1, "");
					}
					
					System.out.println(stringBuf.toString());
					
				}

				setMethod.invoke(o, paraInstanceSet);
				Object actual = getMethod.invoke(o, paraInstanceGet);
				
				switch(dataCenter.getAssertLevel()){
				case AssertClassLevel:
					if(null != actual && null != paraInstanceSet[0]){
						if(!primitiveType(paraInstanceSet[0].getClass()) && !primitiveType(actual.getClass()))
						{
							Assert.assertEquals(paraInstanceSet[0].getClass(), actual.getClass());
						}
					}else{
						Assert.assertNull(actual);
					}
					
					break;
					
				case AssertObjectLevel:
					Assert.assertEquals(paraInstanceGet, actual);
					break;
				}
				
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	
	/*
	 *  handle static coupled methods
	 *  @para Object o, ArrayList<Method[]> coupled, dataCenter
	 *  @return AnalysisResult
	 */
	public void handleCoupledStaticMethod(Object o, ArrayList<Method[]> copuled, DataSource dataCenter){
		if(copuled == null){
			return ;
		}
		
		Method getMethod = null;
		Method setMethod = null;
		
		for(int i = 0; i < copuled.size(); i++){
			if(copuled.get(i)[0] == null || copuled.get(i)[1] == null){
				System.out.println("get method or set method is missing!");
			}else{
				if(copuled.get(i)[0].getName().toLowerCase().startsWith(CommonTest.SET)){
					setMethod = copuled.get(i)[0];
					getMethod = copuled.get(i)[1];
				}else if(copuled.get(i)[0].getName().toLowerCase().startsWith(CommonTest.GET)){
					setMethod = copuled.get(i)[1];
					getMethod = copuled.get(i)[0];
				}
			}
			
			if((getMethod.getModifiers() != publicStaticModifier) || (setMethod.getModifiers() != publicStaticModifier)){
				continue;
			}
			
			Class<?>[] paraSet = setMethod.getParameterTypes();
			Class<?>[] paraGet = getMethod.getParameterTypes();
			
			Object[] paraInstanceSet = new Object[paraSet.length];
			Object[] paraInstanceGet = new Object[paraGet.length]; 
			paraInstanceSet = dataCenter.lookup(paraSet, paraInstanceSet);
			paraInstanceGet = dataCenter.lookup(paraGet, paraInstanceGet);
			
			
			try {
				
				if(dataCenter.getLogLevel() == releaseLevel){
					StringBuffer stringBuf = new StringBuffer();
					stringBuf.append("[Unit Test]: " + setMethod.getName() + "(");
					for(int l = 0; l < paraSet.length; l++){
						stringBuf.append(paraSet[l].getName() + ",");
					}
					stringBuf.append(")");
					int lastComma = stringBuf.lastIndexOf(",");
					if(lastComma > 0 && lastComma < stringBuf.length()){
						stringBuf.replace(lastComma, lastComma+1, "");
					}
					
					System.out.println(stringBuf.toString());
					
					stringBuf = new StringBuffer();
					stringBuf.append("[Unit Test]: " + getMethod.getName() + "(");
					for(int m = 0; m < paraGet.length; m++){
						stringBuf.append(paraGet[i].getName() + ",");
					}
					stringBuf.append(")");
					lastComma = stringBuf.lastIndexOf(",");
					if(lastComma > 0 && lastComma < stringBuf.length()){
						stringBuf.replace(lastComma, lastComma+1, "");
					}
					
					System.out.println(stringBuf.toString());
					
				}

				setMethod.invoke(o, paraInstanceSet);
				Object actual = getMethod.invoke(o, paraInstanceGet);
				
				switch(dataCenter.getAssertLevel()){
				case AssertClassLevel:
					if(null != actual && null != paraInstanceSet && paraInstanceSet.length > 0){
						Assert.assertEquals(paraInstanceSet[0].getClass(), actual.getClass());
					}else if(paraInstanceSet != null && paraInstanceSet.length == 0){
						System.out.println("set function : " + setMethod.getName() + " get function : " + getMethod.getName());
					}else					{
						Assert.assertNull(actual);
					}
					
					break;
					
				case AssertObjectLevel:
					Assert.assertEquals(paraInstanceGet, actual);
					break;
				}
				
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	
	/*
	 * get the class's method information 
	 * 	@para Object o
	 * 	@return AnalysisResult
	 */
	
	public MethodCollection handle(Object o){
		if(o == null){
			return null;
		}
		
		Class<?> clazz = o.getClass();
		Method[] methods = clazz.getDeclaredMethods();
		
		ArrayList<Method> common = new ArrayList();
		ArrayList<Method[]> coupled = new ArrayList();
		ArrayList<Method> container = new ArrayList();
		
		for(Method m: methods){
			if(m.getName().toLowerCase().contains(CommonTest.SET) || m.getName().toLowerCase().contains(CommonTest.GET)){
				container.add(m);
			}else{
				common.add(m);
			}
		}
		
		while(!container.isEmpty()){
			boolean flag = true;
			Method m = container.get(0);
			String name = m.getName().contains(CommonTest.SET) ? m.getName().replace(CommonTest.SET, CommonTest.GET) : m.getName().replace(CommonTest.GET, CommonTest.SET);
			
			for(int i = 1; i < container.size(); i++){
				if(container.get(i).getName().equals(name)){
					Method[] couple = new Method[2];
					couple[0] = m;
					couple[1] = container.get(i);
					coupled.add(couple);
					container.remove(i);
					container.remove(0);

					flag = false;
					break;
				}
			}
			
			if(flag){
				common.add(container.get(0));
				container.remove(0);
			}
		}
		
		MethodCollection analysisResult = new MethodCollection();
		analysisResult.setCommon(common);
		analysisResult.setCoupled(coupled);
		
		return analysisResult;
	}
	
	public MethodCollection handle(Object o, Method m){
		return new MethodCollection();
	}
	
	public MethodCollection handle(Object o, Method[] m){
		
		return new MethodCollection();
	}
	
	public MethodCollection handle(Object o, Field f){
		
		return new MethodCollection();
	}
	
	public MethodCollection handle(Object o, Field[] f){
		
		return new MethodCollection();
	}
	
	
	
	public boolean primitiveType(Class c)
	{
		if(c.equals(long.class) || c.equals(int.class) || c.equals(char.class) || c.equals(short.class)
				|| c.equals(float.class) || c.equals(double.class) || c.equals(boolean.class) || c.equals(byte.class)
				||c.equals(Long.class) || c.equals(Integer.class) || c.equals(Character.class) || c.equals(Short.class)
				|| c.equals(Float.class) || c.equals(Double.class) || c.equals(Boolean.class) || c.equals(Byte.class))
		{
			return true;
		}
		return false;
	}
	
	
	

}
