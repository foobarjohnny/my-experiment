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
