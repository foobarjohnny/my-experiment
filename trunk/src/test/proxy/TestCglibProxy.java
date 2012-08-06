package test.proxy;

//CGLib动态代理示例：
//1、创建一个实现net.sf.cglib.proxy.MethodInterceptor接口的实例来为目标业务类加入进行代理时要进行的操作或增强：
import java.lang.reflect.Method;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * CGlib采用非常底层的字节码技术，可以为一个类创建子类， 并在子类中采用方法拦截技术拦截父类方法的调用， 并顺势进行增强，即是织入横切逻辑
 * 
 */
class CglibProxy implements MethodInterceptor
{
	private Enhancer	enhancer	= new Enhancer();

	// 覆盖MethodInterceptor接口的getProxy()方法，设置
	public Object getProxy(Class clazz)
	{
		enhancer.setSuperclass(clazz); // 设者要创建子类的类
		enhancer.setCallback(this); // 设置回调的对象
		return enhancer.create(); // 通过字节码技术动态创建子类实例,
	}

	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable
	{
		System.out.println("模拟代理增强方法");
		// 通过代理类实例调用父类的方法，即是目标业务类方法的调用
		Object result = proxy.invokeSuper(obj, args);
		System.out.println("模拟代理增强方法结束");
		return result;
	}
}

// 2、通过java.lang.reflect.Proxy的getProxy()动态生成目标业务类的子类，即是代理类，再由此得到代理实例：
public class TestCglibProxy
{
	public static void main(String args[])
	{
		CglibProxy proxy = new CglibProxy();
		// 动态生成子类的方法创建代理类
		ForumServiceImpl fsi = (ForumServiceImpl) proxy.getProxy(ForumServiceImpl.class);
		fsi.removeForum(10);
		fsi.removeTopic(2);
	}
}