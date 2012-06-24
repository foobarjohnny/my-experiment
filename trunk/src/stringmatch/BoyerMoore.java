package stringmatch;

import java.util.Arrays;

/**
 * http://blog.csdn.net/sealyao/article/details/4568167
 * http://mindlee.net/2011/11/25/string-matching/
 * http://www.cnblogs.com/a180285/archive/2011/12/15/BM_algorithm.html
 */
/**
 * http://blog.focus-linux.net/?p=162
 * �������ֲ���ΪBM��Ч��ԭ�����кú�׺�Ĳ�����KMP����(��ʵӦ���ǰ���������KMP������ô������ǽ����ַ��Ĳ������뵽KMP���Ƿ�Ҳ������Ч�أ�
 * abcdefgbcabc���� abcdefabcabc
 * �������ƥ�䣬����7λd��a��ƥ��ʱ��g��Ϊ���ַ�������KMP�㷨��˵��Ҳֻ���ƶ�7λ�������ԭ����KMP�㷨���������ƶ���һλ��
 * Ч�����ޡ���ԭ�������BM�Ǵ��ҿ�ʼƥ�䣬��KMP�Ǵ���ʼƥ�䣬��һ��һ�ҵ����𣬾�ʹBM���Ի��������λ����F
 */
// �����ڲ������ַ������㷨���У�BM(Boyer-Moore)�㷨��Ŀǰ�൱��Ч����������һ�֣�һ������£���KMP�㷨��3-5����
// BM�㷨���ƶ�ģʽ����ʱ���Ǵ����ң������бȽϵ�ʱ���Ǵ��ҵ���ġ�
// �����ƥ���㷨�ƶ�ģʽ����ʱ���Ǵ����ң������бȽϵ�ʱ��Ҳ���Ǵ����ҵģ���������ǣ� illustration()

// BM�㷨ʵ���ϰ����������е��㷨�����ַ��㷨�ͺú�׺�㷨��
// �������㷨��Ŀ�ľ�����ģʽ��ÿ�������ƶ������ܴ�ľ���(j+=x��x�����ܵĴ�)��

// BoyerMoore BM�㷨
// parStr TEXT ���� S �Ӵ� PATTERN
// BM�㷨Ԥ����ʱ�临�Ӷ�ΪO��m+s�����ռ临�Ӷ�ΪO(s)��s����P, T��ص������ַ������ȣ������׶�ʱ�临�Ӷ�ΪO(m��n)��
// �������µ�ʱ�临�Ӷ�ΪO(n/m)��������ʱ�临�Ӷ�ΪO(m��n)��
public class BoyerMoore
{
	private static int	times	= 10;

	public static void test(String parStr, String subStr)
	{
		long start = 0;
		long end = 0;
		System.out.println("����: " + parStr);
		System.out.println("�Ӵ�: " + subStr);
		start = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			System.out.println(boyerMoore(parStr, subStr));
		}
		end = System.currentTimeMillis();
		System.out.println("Time for BoyerMoore: " + (end - start));
	}

	private static void illustration()
	{
		int j = 0, i = 0;
		char[] parStr = new char[11];
		char[] subStr = new char[2];
		// �����ƥ���㷨�ƶ�ģʽ����ʱ���Ǵ����ң������бȽϵ�ʱ��Ҳ���Ǵ����ҵ�
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
		// ��BM�㷨���ƶ�subStr��ʱ���Ǵ����ң������бȽϵ�ʱ���Ǵ��ҵ���ģ���������ǣ�
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
		//��0��M-1 ��֤�� Ϊ����
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

	// ������
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
			// ���ܴ�m-2��һ
			// m-1-i����֤��bmGs[i]Ϊ������С,M-1-iΪ�ݼ�
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
		//�ɲ⺺��
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
				//System.out.println(j);
				count++;
				j += bmGs[0];
				// j++;
			}
			else {
				j += Math.max(bmGs[i], bmBc[par[i + j]] - (m - 1 - i));
			}
		}
		return count;
	}

	public static void main(String[] args)
	{
		// ����λ������ΪP[0, 0, 0, 0, 0, 0]
//		System.out.println("abcdeg, abcdeh, abcdef!�����ƥ��1�� ,  abcdef");
//		test("abcdegabcdehabcdef", "abcdef");
//		// ����λ������ΪP[0, 0, 1, 2, 3, 4]
//		System.out.println("Test ititi ititit! Test ititit!�����ƥ��2�� , ititit");
//		
//		test("testititiititittestititit", "ititit");
//		test("testititiititittestititit", "ititit");
//
//		test("ushers", "her");
		
		
		test("Test ititi ititit! Test ititit!�����ƥ��2��","ititit");
	}
}
