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
		if (a != null) {
			System.out.println(a);
			if (a.length > 2) {
				System.out.println(a[1]);
			}
		}
	}

	public static void main(String[] args)
	{
		f();
		c();
		d();
		// e(); e()�ᱨ�� �Ա�d()
		d(1);
		d(1.2);
		d(1.2f);
		int[] a = { 1, 2, 3 };
		f(a);
		g();
		g(1, 2, 3);
		// output �����������
		// f2...
		// c2...
		// d2...
		// d2
		// d1
		// d3
		// f1...
		// g2...
		// [Ljava.lang.Integer;@190d11
		// g2...
		// [Ljava.lang.Integer;@a90653
		// 2

	}

}