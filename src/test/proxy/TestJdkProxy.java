package test.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
//http://www.cnblogs.com/jqyp/archive/2010/08/20/1805041.html
//http://blog.csdn.net/magicianliu/article/details/4107497
//http://mrzhangtufu.iteye.com/blog/240698

//JDK的动态代理依靠接口实现，如果有些类并没有实现接口，则不能使用JDK代理，这就要使用cglib动态代理了。 
//Cglib动态代理 
//JDK的动态代理机制只能代理实现了接口的类，而不能实现接口的类就不能实现JDK的动态代理，
//cglib是针对类来实现代理的，他的原理是对指定的目标类生成一个子类，并覆盖其中方法实现增强，
//但因为采用的是继承，所以不能对final修饰的类进行代理。 

interface ForumService
{
	public void removeTopic(int topicId);

	public void removeForum(int forumId);
}

class ForumServiceImpl implements ForumService
{
	public void removeTopic(int topicId)
	{
		System.out.println("模拟删除记录" + topicId);
		try {
			Thread.currentThread().sleep(20);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void removeForum(int forumId)
	{
		System.out.println("模拟删除记录" + forumId);
		try {
			Thread.currentThread().sleep(20);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

// 1、创建一个实现java.lang.reflect.InvocationHandler 接口的代理类，如：
class PerformanceHandler implements InvocationHandler
{
	private Object	target; // 要进行代理的业务类的实例

	public PerformanceHandler(Object target)
	{
		this.target = target;
	}

	// 覆盖java.lang.reflect.InvocationHandler的方法invoke()进行织入（增强）的操作
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		System.out.println("Object target proxy:" + target);
		System.out.println("模拟代理加强的方法...");
		Object obj = method.invoke(target, args); // 调用目标业务类的方法
		System.out.println("模拟代理加强的方法执行完毕...");
		return obj;
	}
}

// 2、用java.lang.reflect.Proxy.newProxyInstance()方法创建动态实例来调用代理实例的方法：
public class TestJdkProxy
{
	public static void main(String args[])
	{
		ForumService target = new ForumServiceImpl();// 要进行代理的目标业务类
		PerformanceHandler handler = new PerformanceHandler(target);// 用代理类把目标业务类进行编织

		// 创建代理实例，它可以看作是要代理的目标业务类的加多了横切代码(方法)的一个子类
		ForumService proxy = (ForumService) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), handler);
		proxy.removeForum(10);
		proxy.removeTopic(20);
	}
}
