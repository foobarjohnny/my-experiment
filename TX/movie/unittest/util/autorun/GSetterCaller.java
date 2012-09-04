package util.autorun;

import java.lang.reflect.Method;


public class GSetterCaller extends AbstractAutoRun{
	public GSetterCaller() {
		setCallerType(ENUM_CallerType.GSETTERTYPE);
	}
	@Override
	protected boolean filter(Method m){
		String name = m.getName();
		if(name == null || name.equals("")){
			return false;
		}else if(name.startsWith("set") || name.startsWith("get") || name.equalsIgnoreCase("toString")){
			return true;
		}
		return false;
	}
	@Override
	protected Object getObjectParameter(Class c) {
		return null;
	}
}
