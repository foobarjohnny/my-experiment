/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package util.autorun;

import java.lang.reflect.Method;



/**
 * ConstructorCaller.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-20
 */
public class ConstructorCaller extends AbstractAutoRun{
	
	public ConstructorCaller() {
		setCallerType(ENUM_CallerType.CONSTRUCTORTYPE);
	}
	
	@Override
	protected boolean filter(Method m){
		return true;
	}
	@Override
	protected Object getObjectParameter(Class c) {
		return null;
	}

}
