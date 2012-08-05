package test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
//http://blog.csdn.net/softwave/article/details/6991913
//http://blog.csdn.net/foamflower/article/details/3990198
//http://blog.csdn.net/foamflower/article/details/3990198
//http://blog.csdn.net/janeky/article/details/4570541
//http://javaopen.iteye.com/blog/645915
//http://hejiangtao.iteye.com/blog/1381225
//http://blog.csdn.net/janeky/article/details/4570541

@MyClassAnnotation(uri = "test.TestAnnotation", desc = "The class name")
public class TestAnnotation
// Ԫ����Meta Data��ʡ������Ķ���Ϊ��data about data (�������ݵ�����)��
// @Override @Deprecated @SuppressWarnings ��������ע�� ��java.lang����
{

	@MyFieldAnnotation(uri = "test.TestAnnotation#id", desc = "The class field")
	public String	id;

	/**
	 * Description: default constructor
	 */
	@MyConstructorAnnotation(uri = "test.TestAnnotation#TestAnnotation", desc = "The default constuctor")
	public TestAnnotation()
	{
	}

	/**
	 * Description: normal method
	 */
	@MyMethodAnnotation(uri = "test.TestAnnotation#setId", desc = "The class method")
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * Description: MyAnnotation test
	 * 
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public static void main(String[] args) throws SecurityException, NoSuchMethodException, NoSuchFieldException
	{
		TestDeprecated.DeprecatedMethod();
		TestAnnotation oTestAnnotation = new TestAnnotation();
		// get class annotation
		MyClassAnnotation oMyAnnotation = TestAnnotation.class.getAnnotation(MyClassAnnotation.class);
		System.out.println("Class's uri: " + oMyAnnotation.uri() + "; desc: " + oMyAnnotation.desc());

		// get constructor annotation
		Constructor oConstructor = oTestAnnotation.getClass().getConstructor();
		MyConstructorAnnotation oMyConstructorAnnotation = (MyConstructorAnnotation) oConstructor.getAnnotation(MyConstructorAnnotation.class);
		System.out.println("Constructor's uri: " + oMyConstructorAnnotation.uri() + "; desc: " + oMyConstructorAnnotation.desc());

		// get method annotation
		Method oMethod = oTestAnnotation.getClass().getDeclaredMethod("setId", String.class);
		MyMethodAnnotation oMyMethodAnnotation = oMethod.getAnnotation(MyMethodAnnotation.class);
		System.out.println("Method's uri: " + oMyMethodAnnotation.uri() + "; desc: " + oMyMethodAnnotation.desc());

		// get field annotation
		Field oField = oTestAnnotation.getClass().getDeclaredField("id");
		MyFieldAnnotation oMyFieldAnnotation = oField.getAnnotation(MyFieldAnnotation.class);
		System.out.println("Field's uri: " + oMyFieldAnnotation.uri() + "; desc: " + oMyFieldAnnotation.desc());
		// output:
		// Class's uri: test.TestAnnotation; desc: The class name
		// Constructor's uri: test.TestAnnotation#TestAnnotation; desc: The
		// default constuctor
		// Method's uri: test.TestAnnotation#setId; desc: The class method
		// Field's uri: test.TestAnnotation#id; desc: The class field
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

class TestDeprecated
{

	// TODO Eclipse��ô�鿴������Ϣѽ
	// ע����������λ��
	public @Deprecated
	static void DeprecatedMethod()
	{
		// TODO
		System.out.println("sfasdf TestDeprecated.DeprecatedMethod()");
	}

	@Deprecated
	public static void DeprecatedMethod2()
	{
		// TODO
		System.out.println("TestDeprecated.DeprecatedMethod2()");
	}
}

class TestSuppressWarnings
{

	// TODO Eclipse��ô�鿴������Ϣѽ
	// ע����������λ��
	public @SuppressWarnings(value = { "a" })
	static void DeprecatedMethod()
	{
		// TODO
		System.out.println("sfasdf TestSuppressWarnings.DeprecatedMethod()");
	}

	@SuppressWarnings(value = { "a" })
	public static void DeprecatedMethod2()
	{
		// TODO
		System.out.println("TestSuppressWarnings.DeprecatedMethod2()");
	}
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
@interface MyConstructorAnnotation
{
	String uri();

	String desc();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface MyMethodAnnotation
{
	String uri();

	String desc();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MyFieldAnnotation
{
	String uri();

	String desc();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface MyClassAnnotation
{
	String uri();

	String desc();
}