package test;

import static test.TestImport.staticMethod;
//“静态导入”或者“静态成员导入”
//Static Import机制常常被直译成“静态导入”。
//但是从含义上看，“静态成员导入”是更为贴切的译法。
//不过考虑到“静态导入”这说法比较简短，估计还是会有强大的生命力。
//
//换而言之，对于不属于任何包的类和接口，是既不能用import导入它本身，也不能用import static导入它的静态成员的。
//import static 包名.类或接口名.静态成员名; enum也可以引入,他算比较特殊的类
//变量、常量、方法和内部类都可以导入
//import与import static 要与访问权限挂钩的 不然不能导入
//
//Static Import机制也支持一种不必逐一指出静态成员名称的导入方式。这时，要采用这样的语法：
//import static 包名.类或接口名.*;
//注意这种方式只是指出遇到来历不明的成员时，可以到这个类或接口里来查找，并不是把这个类或接口里的所有静态成员全部导入。

public class TestImportStatic
{

	public static void a()
	{
		staticMethod();
	}

	public static void main(String[] args)
	{
	}

}
