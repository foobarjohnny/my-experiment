package test.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
//http://www.cnblogs.com/jqyp/archive/2010/08/20/1805041.html
//http://blog.csdn.net/magicianliu/article/details/4107497
//http://mrzhangtufu.iteye.com/blog/240698

//JDK�Ķ�̬���������ӿ�ʵ�֣������Щ�ಢû��ʵ�ֽӿڣ�����ʹ��JDK�������Ҫʹ��cglib��̬�����ˡ� 
//Cglib��̬���� 
//JDK�Ķ�̬�������ֻ�ܴ���ʵ���˽ӿڵ��࣬������ʵ�ֽӿڵ���Ͳ���ʵ��JDK�Ķ�̬����
//cglib���������ʵ�ִ���ģ�����ԭ���Ƕ�ָ����Ŀ��������һ�����࣬���������з���ʵ����ǿ��
//����Ϊ���õ��Ǽ̳У����Բ��ܶ�final���ε�����д��� 

interface ForumService
{
	public void removeTopic(int topicId);

	public void removeForum(int forumId);
}

class ForumServiceImpl implements ForumService
{
	public void removeTopic(int topicId)
	{
		System.out.println("ģ��ɾ����¼" + topicId);
		try {
			Thread.currentThread().sleep(20);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void removeForum(int forumId)
	{
		System.out.println("ģ��ɾ����¼" + forumId);
		try {
			Thread.currentThread().sleep(20);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

// 1������һ��ʵ��java.lang.reflect.InvocationHandler �ӿڵĴ����࣬�磺
class PerformanceHandler implements InvocationHandler
{
	private Object	target; // Ҫ���д����ҵ�����ʵ��

	public PerformanceHandler(Object target)
	{
		this.target = target;
	}

	// ����java.lang.reflect.InvocationHandler�ķ���invoke()����֯�루��ǿ���Ĳ���
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		System.out.println("Object target proxy:" + target);
		System.out.println("ģ������ǿ�ķ���...");
		Object obj = method.invoke(target, args); // ����Ŀ��ҵ����ķ���
		System.out.println("ģ������ǿ�ķ���ִ�����...");
		return obj;
	}
}

// 2����java.lang.reflect.Proxy.newProxyInstance()����������̬ʵ�������ô���ʵ���ķ�����
public class TestJdkProxy
{
	public static void main(String args[])
	{
		ForumService target = new ForumServiceImpl();// Ҫ���д����Ŀ��ҵ����
		PerformanceHandler handler = new PerformanceHandler(target);// �ô������Ŀ��ҵ������б�֯

		// ��������ʵ���������Կ�����Ҫ�����Ŀ��ҵ����ļӶ��˺��д���(����)��һ������
		ForumService proxy = (ForumService) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), handler);
		proxy.removeForum(10);
		proxy.removeTopic(20);
	}
}
