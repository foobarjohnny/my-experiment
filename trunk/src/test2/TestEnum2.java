package test2;

public enum TestEnum2
{
	A(), B(1), C;
	// ������protected,public ����, ������private,Ҳ���Բ��� Ч��һ����
	// ���ִ�0��ʼ
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