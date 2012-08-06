package test.proxy;

//CGLib��̬����ʾ����
//1������һ��ʵ��net.sf.cglib.proxy.MethodInterceptor�ӿڵ�ʵ����ΪĿ��ҵ���������д���ʱҪ���еĲ�������ǿ��
import java.lang.reflect.Method;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * CGlib���÷ǳ��ײ���ֽ��뼼��������Ϊһ���ഴ�����࣬ ���������в��÷������ؼ������ظ��෽���ĵ��ã� ��˳�ƽ�����ǿ������֯������߼�
 * 
 */
class CglibProxy implements MethodInterceptor
{
	private Enhancer	enhancer	= new Enhancer();

	// ����MethodInterceptor�ӿڵ�getProxy()����������
	public Object getProxy(Class clazz)
	{
		enhancer.setSuperclass(clazz); // ����Ҫ�����������
		enhancer.setCallback(this); // ���ûص��Ķ���
		return enhancer.create(); // ͨ���ֽ��뼼����̬��������ʵ��,
	}

	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable
	{
		System.out.println("ģ�������ǿ����");
		// ͨ��������ʵ�����ø���ķ���������Ŀ��ҵ���෽���ĵ���
		Object result = proxy.invokeSuper(obj, args);
		System.out.println("ģ�������ǿ��������");
		return result;
	}
}

// 2��ͨ��java.lang.reflect.Proxy��getProxy()��̬����Ŀ��ҵ��������࣬���Ǵ����࣬���ɴ˵õ�����ʵ����
public class TestCglibProxy
{
	public static void main(String args[])
	{
		CglibProxy proxy = new CglibProxy();
		// ��̬��������ķ�������������
		ForumServiceImpl fsi = (ForumServiceImpl) proxy.getProxy(ForumServiceImpl.class);
		fsi.removeForum(10);
		fsi.removeTopic(2);
	}
}