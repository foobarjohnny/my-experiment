package stringmatch;

import java.util.Arrays;

/**
 * http://www.zhiwenweb.cn/Category/Security/1274.htm
 * http://mindlee.net/2011/11/25/string-matching/
 */
// AC自动机算法全称Aho-Corasick算法，是一种字符串多模式匹配算法。该算法在1975年产生于贝尔实验室，是著名的多模匹配算法之一。
// AC算法用于在一段文本中查找多个模式字符串，即给你很多字符串，再给你一段文本，让你在文本中找这些串是否出现过，出现过多少次，分别在哪里出现。
// 该算法应用有限自动机巧妙地将字符比较转化为了状态转移。
// 此算法有两个特点，一个是扫描文本时完全不需要回溯，另一个是时间复杂度为O(n)，时间复杂度与关键字的数目和长度无关
// ，但所需时间和文本长度以及所有关键字的总长度成正比。
// AC算法有三个主要步骤，一个是字典树trie的构造，一个是搜索路径的确定（即构造失败指针），还有就是模式匹配过程。
// 学习AC自动机算法之前，最好先熟悉KMP算法，因为KMP算法与字典树tire的构造很是类似。KMP算法是一种经典的单字符串匹配算法。

// 也是有限自动机算法 FINITE-AUTOMATION
// 预处理时间 O(m|gama|) O(n)时间 用于多模式匹配
// 主串 TEXT 或者 S 子串 PATTERN
public class AhoCorasick
{
	private static int	times	= 10;

	public static void test(String parStr, String[] subStr)
	{
		long start = 0;
		long end = 0;
		System.out.println("父串: " + parStr);
		System.out.println("子串: " + Arrays.toString(subStr));
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
		// 回退位置数组为P[0, 0, 0, 0, 0, 0]
		System.out.println("abcdeg, abcdeh, abcdef!这个会匹配1次 ,  abcdef");
		test("abcdegabcdehabcdef", new String[] { "abcdef",""});
		// 回退位置数组为P[0, 0, 1, 2, 3, 4]
		System.out.println("Test ititi ititit! Test ititit!这个会匹配2次 , ititit");
		test("testititiititittestititit", new String[] { "ititit" });

		test("ushers", new String[] { "her", "she", "he", "his" });
	}
}
