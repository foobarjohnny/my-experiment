package test;
//http://blog.csdn.net/softwave/article/details/6991913
public class TestAnnotation

{
	public static void main(String[] args)
	{
		DeprecatedClass.DeprecatedMethod();
	}

	// TODO ��̬�����ƺ����ܼ̳У��ƺ����� ��������д ��д�˸���������� ����֧�ֶ�̬
	// �����Ǹ������õ����������е���.
	// ��д�Ǹ��ݶ�������������е���.
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
	// TODO Eclipse��ô�鿴������Ϣѽ
	public static void DeprecatedMethod()
	{
		// TODO
		System.out.println("sfasdf DeprecatedClass.DeprecatedMethod()");
	}
}
