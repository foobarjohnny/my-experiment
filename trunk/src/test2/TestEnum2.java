package test2;

public enum TestEnum2
{
	A(), B(1), C;
	// 不能用protected,public 修饰, 可以用private,也可以不用 效果一样的
	// 数字从0开始
	TestEnum2()
	{
		System.out.println("TestEnum2.TestEnum2()");
	}

	TestEnum2(int a)
	{
		System.out.println("TestEnum2.TestEnum2()");
	}

	public static void main(String[] args)
	{
		System.out.println("TestEnum2.main");
	}

	public static void cc(String[] args)
	{
		System.out.println("TestEnum2.cc");
	}
}