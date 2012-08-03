package test;
public class TestReference
{

	public void play(Object o)
	{

		System.out.println("Object");
	}

	public void play(String o)
	{

		System.out.println("String");
	}

	public static void main(String[] args)
	{
		TestReference t = new TestReference();
		Object test = "test";
		A tt = new TestReference().new B();
		t.play(test);
		t.play(tt);
	}

	public void play(A o)
	{

		System.out.println("A");
	}

	public void play(B o)
	{

		System.out.println("B");
	}

	class B extends A
	{

	}

	class A
	{
	}
}
