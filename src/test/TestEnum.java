package test;

import test2.TestEnum2;
import test2.TestEnum3;
import static test2.TestEnum3.*;

// enum是非常特殊的类
public class TestEnum
{
	TestEnum2	a	= TestEnum2.A;
	TestEnum3	b	= X;
	TestEnum3	c	= TestEnum3.Z;

	public static void main(String[] args)
	{
		TestEnum t = new TestEnum();
		System.out.println(t.a.valueOf("B"));
		System.out.println(t.a.valueOf("A").ordinal());
		System.out.println(t.a.ordinal());
		TestEnum2.cc(args);
	}
}