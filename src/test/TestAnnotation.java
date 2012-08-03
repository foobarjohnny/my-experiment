package test;
//http://blog.csdn.net/softwave/article/details/6991913
public class TestAnnotation

{
	public static void main(String[] args)
	{
		DeprecatedClass.DeprecatedMethod();
	}

	// TODO 静态方法似乎不能继承，似乎是能 但不能重写 重写了父类就隐藏了 但不支持多态
	// 隐藏是根据引用的类型来进行调用.
	// 重写是根据对象的类型来进行调用.
	// http://jarg.iteye.com/blog/984234
	public void override()
	{
		System.out.println(TestClassLoader.class.getClassLoader());
	}

}

class TestOverride extends TestAnnotation
{
	@Override
	public void override()
	{
		System.out.println("=TestClassLoader.class.getClassLoader()");
	}
}

class DeprecatedClass
{

	@Deprecated
	// TODO Eclipse怎么查看编译信息呀
	public static void DeprecatedMethod()
	{
		// TODO
		System.out.println("sfasdf DeprecatedClass.DeprecatedMethod()");
	}
}
