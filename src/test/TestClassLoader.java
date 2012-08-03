package test;

import test2.TestInnerClass;
import test2.TestInnerClass.*;

public class TestClassLoader
// http://psupsuoooo.blog.163.com/blog/static/31841482012721475334/
{
	public static void main(String[] args)
	{
		System.out.println(TestClassLoader.class.getClassLoader());
		TestInnerClass.main(args);
	}

}