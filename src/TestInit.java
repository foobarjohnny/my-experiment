//   	    a      父类静态成员和静态初始化块 
//          b      子类静态成员和静态初始化块 
//          c      父类实例成员和实例初始化块 
//          d      父类构造方法 
//          e      子类实例成员和实例初始化块 
//          f      子类构造方法   
public class TestInit
{

	public static void main(String[] args)
	{
		Base t = new Other();
		t.test();
	}

}

class Other extends Extend
{
	public Other()
	{
		System.out.println("Other:<init>");
	}

	void test()
	{
		super.test();
		System.out.println("Other:test");
	}

	static {
		System.out.println("Other:<cinit>");
	}
}

class Extend extends Base
{
	public Extend()
	{
		System.out.println("Extend:<init>");
	}

	void test()
	{
		System.out.println("Extend:test");
	}

	static {
		System.out.println("Extend:<cinit>");
	}
}

class Base
{
	public Base()
	{
		System.out.println("Base:<init>");
	}

	void test()
	{
		System.out.println("Base:test");
	}
}
