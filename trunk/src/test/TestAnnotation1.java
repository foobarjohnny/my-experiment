package test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * Ԫ���ݣ����ǡ��������ݵ����ݡ�������Ҳ�кܶ�����
 * ������ù�Javadoc��ע���Զ������ĵ��������Ԫ���ݹ��ܵ�һ�֡�
 * �ܵ���˵��Ԫ���ݿ������������ĵ������ٴ���������ԣ�ִ�б���ʱ��ʽ��飬
 * �������е������ļ�����HibernateҲ�ṩ��ע�����ã�
 ע����3�л�������
 a.���ע��      --û�б�����ֻ�����Ʊ�ʶ������ @annotation
 b.��һֵע��    --�ڱ��ע�͵Ļ������ṩһ�����ݡ��� @annotation����data����
 c.����ע��      --���԰���������ݳ�Ա��ÿ�����ݳ�Ա�����ƺ�ֵ���ɡ�
 @annotation(val1="data1",val2="data2")
 */
/* 
 * 
 * Ԫע��@Target,@Retention,@Documented,@Inherited 
 *  
 *     @Target ��ʾ��ע������ʲô�ط������ܵ� ElemenetType ���������� 
 *         ElemenetType.CONSTRUCTOR ���������� 
 *         ElemenetType.FIELD ������������ enum ʵ���� 
 *         ElemenetType.LOCAL_VARIABLE �ֲ��������� 
 *         ElemenetType.METHOD �������� 
 *         ElemenetType.PACKAGE ������ 
 *         ElemenetType.PARAMETER �������� 
 *         ElemenetType.TYPE �࣬�ӿڣ�����ע�����ͣ���enum���� 
 *          
 *     @Retention ��ʾ��ʲô���𱣴��ע����Ϣ����ѡ�� RetentionPolicy ���������� 
 *         RetentionPolicy.SOURCE ע�⽫������������ 
 *         RetentionPolicy.CLASS ע����class�ļ��п��ã����ᱻVM���� 
 *         RetentionPolicy.RUNTIME VM����������Ҳ����ע�ͣ���˿���ͨ��������ƶ�ȡע�����Ϣ�� 
 *          
 *     @Documented ����ע������� javadoc �� 
 *      
 *     @Inherited ��������̳и����е�ע�� 
 *    
 */
//@Targe @Retention @Documented @Inherited ����Ԫע�� Meta Annotation
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation1
{
	public String myMethod() default "xyz";
	// ��������������
	// public String myMethod2()default "xyz" {};
	// public String myMethod3(){ }default "xyz" ;
}
