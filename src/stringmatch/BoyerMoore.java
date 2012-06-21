package stringmatch;

import java.util.Arrays;

/**
 * http://blog.csdn.net/sealyao/article/details/4568167
 * http://mindlee.net/2011/11/25/string-matching/
 */
// 在用于查找子字符串的算法当中，BM(Boyer-Moore)算法是目前相当有效又容易理解的一种，一般情况下，比KMP算法快3-5倍。
// BM算法在移动模式串的时候是从左到右，而进行比较的时候是从右到左的。
// 常规的匹配算法移动模式串的时候是从左到右，而进行比较的时候也是是从左到右的，基本框架是： illustration()

// BM算法实际上包含两个并行的算法，坏字符算法和好后缀算法。
// 这两种算法的目的就是让模式串每次向右移动尽可能大的距离(j+=x，x尽可能的大)。

// BoyerMoore BM算法
// 预处理时间 O(m|gama|) O(n)时间 用于多模式匹配
// parStr TEXT 或者 S 子串 PATTERN
public class BoyerMoore
{
	private static final int	ASIZE	= 0;
	private static final int	XSIZE	= 0;
	private static int			times	= 10;

	public static void test(String parStr, String subStr)
	{
		long start = 0;
		long end = 0;
		System.out.println("父串: " + parStr);
		System.out.println("子串: " + subStr);
		start = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			System.out.println(boyerMoore(parStr, subStr));
		}
		end = System.currentTimeMillis();
		System.out.println("Time for ahoCorasick: " + (end - start));
	}

	private static void illustration()
	{
		int j = 0, i = 0;
		char[] parStr = new char[11];
		char[] subStr = new char[2];
		// 常规的匹配算法移动模式串的时候是从左到右，而进行比较的时候也是是从左到右的
		while (j <= parStr.length - subStr.length) {
			for (i = 0; i < subStr.length && subStr[i] == parStr[i + j]; ++i) {
				;
			}
			if (i == subStr.length) {
				// Match;
			}
			else {
				++j;
			}
		}
		// 而BM算法在移动subStr的时候是从左到右，而进行比较的时候是从右到左的，基本框架是：
		j = 0;
		while (j <= parStr.length - subStr.length) {
			for (i = subStr.length - 1; i >= 0 && subStr[i] == parStr[i + j]; --i) {
				;
			}
			if (i < 0) {
				// match;
			}
			else {
				++j;
			}
		}
	}

	// bad character
	public static void preBmBc(char[] subStr, int m, int[] bmBc)
	{
		int i;
		for (i = 0; i < Character.MAX_VALUE; ++i) {
			bmBc[i] = m;
		}
		for (i = 0; i < m - 1; ++i) {
			bmBc[subStr[i]] = m - i - 1;
		}

	}

	public static void suffixes(char[] sub, int m, int[] suff)
	{
		int f, q, i;
		f = m - 1;
		suff[m - 1] = m;
		q = m - 1;
		for (i = m - 2; i >= 0; --i) {
			if (i > q && suff[i + m - 1 - f] < i - q) {
				suff[i] = suff[i + m - 1 - f];
			}
			else {
				if (i < q) {
					q = i;
				}
				f = i;
				while (q >= 0 && sub[q] == sub[q + m - 1 - f]) {
					--q;
				}
				suff[i] = f - q;
			}
		}
	}

	// 暴力法
	public static void suffixes2(char[] sub, int m, int[] suff)
	{
		suff[m - 1] = m;
		int i = 0;
		int q = 0;
		for (i = m - 2; i >= 0; --i) {
			q = i;
			while (q >= 0 && sub[q] == sub[m - 1 - i + q]) {
				--q;
			}
			suff[i] = i - q;
		}
	}

	// GOOD suffix
	public static void preBmGs(char[] subStr, int m, int bmGs[])
	{
		int i, j;
		int[] suff = new int[m];
		suffixes(subStr, m, suff);
		for (i = 0; i < m; ++i) {
			bmGs[i] = m;
		}
		j = 0;
		for (i = m - 1; i >= 0; --i) {
			if (suff[i] == i + 1) {
				for (; j < m - 1 - i; ++j) {
					if (bmGs[j] == m) {
						bmGs[j] = m - 1 - i;
					}
				}
			}
		}
		for (i = 0; i <= m - 2; ++i) {
			bmGs[m - 1 - suff[i]] = m - 1 - i;
		}
	}

	public static int boyerMoore(String parStr, String subStr)
	{
		if (subStr == null || parStr == null) {
			return -1;
		}
		int i, j;
		char[] sub;
		char[] par;
		sub = subStr.toCharArray();
		par = parStr.toCharArray();
		int m = sub.length;
		int n = par.length;
		int[] bmGs = new int[n];

		int[] bmBc = new int[Character.MAX_VALUE];
		/* Preprocessing */
		preBmGs(sub, m, bmGs);
		preBmBc(sub, m, bmBc);

		/* Searching */
		int count = 0;
		j = 0;
		while (j <= n - m) {
			for (i = m - 1; i >= 0 && sub[i] == par[i + j]; --i) {
				;
			}
			if (i < 0) {
				System.out.println(j);
				count++;
				j += bmGs[0];
				// j++;
			}
			else {
				j += Math.max(bmGs[i], bmBc[par[i + j]] - m + 1 + i);
			}
		}
		return count;
	}

	public static void main(String[] args)
	{
		// 回退位置数组为P[0, 0, 0, 0, 0, 0]
		System.out.println("abcdeg, abcdeh, abcdef!这个会匹配1次 ,  abcdef");
		test("abcdegabcdehabcdef", "abcdef");
		// 回退位置数组为P[0, 0, 1, 2, 3, 4]
		System.out.println("Test ititi ititit! Test ititit!这个会匹配2次 , ititit");
		test("testititiititittestititit", "ititit");

		test("ushers", "her");
	}
}
