package stringmatch;

import java.util.Arrays;

/**
 http://blog.csdn.net/hairetz/article/details/4729397
 http://blog.csdn.net/zhanglizhe_cool/article/details/5576037
 */
/**
 * Sunday算法是Daniel M.Sunday于1990年提出的一种比BM算法搜索速度更快的算法。其核心思想是：在匹配过程中，
 * 模式串并不被要求一定要按从左向右进行比较还是从右向左进行比较，它在发现不匹配时，算法能跳过尽可能多的字符以进行下一步的匹配，从而提高了匹配效率。
 * 
 * Sunday算法思想跟BM算法很相似，在匹配失败时关注的是文本串中参加匹配的最末位字符的下一位字符。如果该字符没有在匹配串中出现则直接跳过，即移动步长=
 * 匹配串长度+1；否则，同BM算法一样其移动步长=匹配串中最右端的该字符到末尾的距离+1。
 */
// Sunday算法
// 时间最坏O(N*M)
public class Sunday
{
	private static int	times	= 10;

	public static void test(String parStr, String subStr)
	{
		long start = 0;
		long end = 0;
		System.out.println("父串: " + parStr);
		System.out.println("子串: " + subStr);
		start = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			System.out.println(sunday(parStr, subStr));
		}
		end = System.currentTimeMillis();
		System.out.println("Time for sunday: " + (end - start));
	}

	// bad character 请和BM中的BC对比
	public static void nextChar(char[] subStr, int m, int[] bmBc)
	{
		int i;
		for (i = 0; i < Character.MAX_VALUE; ++i) {
			bmBc[i] = m;
		}
		// 从0－M-1 保证了 为最右
		// 和BM有何不同 他要加1;
		// 从左往右 重新匹配的不是不同的字符 而是模式串下一个字符
		for (i = 0; i <= m - 1; ++i) {
			bmBc[subStr[i]] = m - i;
		}

	}

	public static int sunday(String parStr, String subStr)
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
		int[] bmBc = new int[Character.MAX_VALUE];
		// 可测汉字
		/* Preprocessing */
		nextChar(sub, m, bmBc);

		/* Searching */
		int count = 0;
		int pos = 0;
		while (pos < (n - m + 1)) // 遍历原串 仔细对比一下和BM有何不同
		{
			i = pos;
			for (j = 0; j < m; ++j, ++i) // 比较
			{
				if (par[i] != sub[j]) // 一旦不匹配，原串就按照next跳转
				{
					pos += bmBc[par[pos + m]];
					break;
				}
			}
			if (j == m) {
				pos += 1;
				count++;
			}
		}
		return count; // 无子串则返回-1
	}

	// public static int sunday2( char[] src, char[] des)
	// {
	// int i,j,pos=0;
	// int len_s,len_d;
	// int next[26]={0}; //next数组，预处理初始化
	// len_s=strlen(src);
	// len_d=strlen(des);
	// for(j=0;j<26;++j) //初始化next数组
	// next[j]=len_d;
	// for(j=0;j<len_d;++j) //设置next数组
	// next[des[j]-'a']=len_d-j;
	// while( pos<(len_s-len_d+1) ) //遍历原串
	// {
	// i=pos;
	// for(j=0;j<len_d;++j,++i) //比较
	// {
	// if(src[i]!=des[j]) //一旦不匹配，原串就按照next跳转
	// {
	// pos+=next[src[pos+len_d]-'a'];
	// break;
	// }
	// }
	// if(j==len_d)
	// return pos;
	// }
	// return -1; //无子串则返回-1
	// }

	public static void main(String[] args)
	{
		// 回退位置数组为P[0, 0, 0, 0, 0, 0]
		// System.out.println("abcdeg, abcdeh, abcdef!这个会匹配1次 ,  abcdef");
		// test("abcdegabcdehabcdef", "abcdef");
		// // 回退位置数组为P[0, 0, 1, 2, 3, 4]
		// System.out.println("Test ititi ititit! Test ititit!这个会匹配2次 , ititit");
		//
		// test("testititiititittestititit", "ititit");
		// test("testititiititittestititit", "ititit");
		//
		// test("ushers", "her");

		test("Test ititi ititit! Test ititit!这个会匹配2次", "ititit");
	}
}
