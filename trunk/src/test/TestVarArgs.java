package test;

public class TestVarArgs
{

	public static void f(Object... a)
	{
		System.out.println("f1...");
	}

	public static void f()
	{
		System.out.println("f2...");
	}

	public static void c(char b, Object... a)
	{
		System.out.println("c1...");
	}

	public static void c(Object... a)
	{
		System.out.println("c2...");
	}

	public static void d(Object... a)
	{
		System.out.println("d1...");
	}

	public static void d(Integer... a)
	{
		System.out.println("d2...");
	}

	public static void d(Object a)
	{
		System.out.println("d1");
	}

	public static void d(Integer a)
	{
		System.out.println("d2");
	}

	public static void d(Float a)
	{
		System.out.println("d3");
	}

	public static void e(Long... a)
	{
		System.out.println("e1...");
	}

	public static void e(Integer... a)
	{
		System.out.println("e2...");
	}

	public static void g(long b, Long... a)
	{
		System.out.println("g1...");
	}

	public static void g(Integer... a)
	{
		System.out.println("g2...");
	}

	public static void main(String[] args)
	{
		f();
		c();
		d();
		// e(); e()会报错 对比d()
		g();
		d(1);
		d(1.2);
		d(1.2f);
		// output 慢慢体会结果吧
		// f2...
		// c2...
		// d2...
		// g2...
		// d2
		// d1
		// d3
	}

}