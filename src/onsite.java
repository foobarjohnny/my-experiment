import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//Binary Search Tree
public class onsite
{

	class MyInvocationHandler implements InvocationHandler
	{
		private Object	target;

		public MyInvocationHandler(Object target)
		{
			this.target = target;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
		{
			// 1.记录日志2.时间统计开始3.安全检查
			Object retVal = method.invoke(target, args);
			// 4.时间统计结束
			return retVal;
		}

		public Object proxy()
		{
			return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new MyInvocationHandler(target));
		}
	}

	interface InterfaceA
	{
		int	len	= 1;

		void output();
	}

	interface InterfaceB
	{
		void output();
	}

	interface InterfaceSub extends InterfaceA, InterfaceB
	{
	}

	class Xyz implements InterfaceA, InterfaceB
	{
		public void output()
		{
			System.out.println("output in class Xyz." + len);
		}

		public void outputLen(int type)
		{

			switch (type) {
				case InterfaceA.len:
					System.out.println("len of InterfaceA=." + type);
					break;
			}
		}

	}

	class parent
	{
		protected String	a	= "222";

		public void output()
		{
			System.out.println("output in class parent.");
		}

		protected void abc()
		{
			System.out.println("output in class abc.");
		}

	}

	class child extends parent
	{
		public void output()
		{
			System.out.println("output in class child." + a);
			abc();
		}

	}

	public static void main(String[] args)
	{

		int N = 2;
		int count = 1;
		for (int i = 1; i < N; i++) {
			count = (count + 1) * 2;
		}
		System.out.println(count);
		onsite a = new onsite();
		Xyz xyz = a.new Xyz();
		xyz.output();
		xyz.outputLen(2);

		child c = a.new child();
		c.output();
	}

}