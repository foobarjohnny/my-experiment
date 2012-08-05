package test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * 元数据，就是“关于数据的数据”。功能也有很多啦。
 * 你可能用过Javadoc的注释自动生成文档。这就是元数据功能的一种。
 * 总的来说，元数据可以用来创建文档，跟踪代码的依赖性，执行编译时格式检查，
 * 代替已有的配置文件（如Hibernate也提供了注释配置）
 注释有3中基本类型
 a.标记注释      --没有变量，只有名称标识。例如 @annotation
 b.单一值注释    --在标记注释的基础上提供一段数据。如 @annotation（“data”）
 c.完整注释      --可以包括多个数据成员，每个数据成员由名称和值构成。
 @annotation(val1="data1",val2="data2")
 */
/* 
 * 
 * 元注解@Target,@Retention,@Documented,@Inherited 
 *  
 *     @Target 表示该注解用于什么地方，可能的 ElemenetType 参数包括： 
 *         ElemenetType.CONSTRUCTOR 构造器声明 
 *         ElemenetType.FIELD 域声明（包括 enum 实例） 
 *         ElemenetType.LOCAL_VARIABLE 局部变量声明 
 *         ElemenetType.METHOD 方法声明 
 *         ElemenetType.PACKAGE 包声明 
 *         ElemenetType.PARAMETER 参数声明 
 *         ElemenetType.TYPE 类，接口（包括注解类型）或enum声明 
 *          
 *     @Retention 表示在什么级别保存该注解信息。可选的 RetentionPolicy 参数包括： 
 *         RetentionPolicy.SOURCE 注解将被编译器丢弃 
 *         RetentionPolicy.CLASS 注解在class文件中可用，但会被VM丢弃 
 *         RetentionPolicy.RUNTIME VM将在运行期也保留注释，因此可以通过反射机制读取注解的信息。 
 *          
 *     @Documented 将此注解包含在 javadoc 中 
 *      
 *     @Inherited 允许子类继承父类中的注解 
 *    
 */
//@Targe @Retention @Documented @Inherited 四种元注解 Meta Annotation
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation1
{
	public String myMethod() default "xyz";
	// 下面两个都不行
	// public String myMethod2()default "xyz" {};
	// public String myMethod3(){ }default "xyz" ;
}
