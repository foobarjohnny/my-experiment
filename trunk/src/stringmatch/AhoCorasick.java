package stringmatch;

import java.util.Arrays;

/**
 * http://www.zhiwenweb.cn/Category/Security/1274.htm
 * http://mindlee.net/2011/11/25/string-matching/
 */
// AC�Զ����㷨ȫ��Aho-Corasick�㷨����һ���ַ�����ģʽƥ���㷨�����㷨��1975������ڱ���ʵ���ң��������Ķ�ģƥ���㷨֮һ��
// AC�㷨������һ���ı��в��Ҷ��ģʽ�ַ�����������ܶ��ַ������ٸ���һ���ı����������ı�������Щ���Ƿ���ֹ������ֹ����ٴΣ��ֱ���������֡�
// ���㷨Ӧ�������Զ�������ؽ��ַ��Ƚ�ת��Ϊ��״̬ת�ơ�
// ���㷨�������ص㣬һ����ɨ���ı�ʱ��ȫ����Ҫ���ݣ���һ����ʱ�临�Ӷ�ΪO(n)��ʱ�临�Ӷ���ؼ��ֵ���Ŀ�ͳ����޹�
// ��������ʱ����ı������Լ����йؼ��ֵ��ܳ��ȳ����ȡ�
// AC�㷨��������Ҫ���裬һ�����ֵ���trie�Ĺ��죬һ��������·����ȷ����������ʧ��ָ�룩�����о���ģʽƥ����̡�
// ѧϰAC�Զ����㷨֮ǰ���������ϤKMP�㷨����ΪKMP�㷨���ֵ���tire�Ĺ���������ơ�KMP�㷨��һ�־���ĵ��ַ���ƥ���㷨��

// Ҳ�������Զ����㷨 FINITE-AUTOMATION
// Ԥ����ʱ�� O(m|gama|) O(n)ʱ�� ���ڶ�ģʽƥ��
// ���� TEXT ���� S �Ӵ� PATTERN
public class AhoCorasick
{
	private static int	times	= 10;

	public static void test(String parStr, String[] subStr)
	{
		long start = 0;
		long end = 0;
		System.out.println("����: " + parStr);
		System.out.println("�Ӵ�: " + Arrays.toString(subStr));
		start = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			System.out.println(ahoCorasick(parStr, subStr));
		}
		end = System.currentTimeMillis();
		System.out.println("Time for ahoCorasick: " + (end - start));
	}

	public static int ahoCorasick(String content, String[] subStr)
	{
		if (subStr == null) {
			return -1;
		}
		Trie finiteStateMachine = new Trie();
		for (int i = 0; i < subStr.length; i++) {
			finiteStateMachine.insert(subStr[i]);
		}
		finiteStateMachine.Build_AC_Automation();
		return finiteStateMachine.AC_Search(content);
	}

	public static void main(String[] args)
	{
		// ����λ������ΪP[0, 0, 0, 0, 0, 0]
		System.out.println("abcdeg, abcdeh, abcdef!�����ƥ��1�� ,  abcdef");
		test("abcdegabcdehabcdef", new String[] { "abcdef",""});
		// ����λ������ΪP[0, 0, 1, 2, 3, 4]
		System.out.println("Test ititi ititit! Test ititit!�����ƥ��2�� , ititit");
		test("testititiititittestititit", new String[] { "ititit" });

		test("ushers", new String[] { "her", "she", "he", "his" });
	}
}
