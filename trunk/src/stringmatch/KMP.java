package stringmatch;

import java.util.Arrays;

/**
 * KMP�㷨��Javaʵ����������ԡ����� http://www.isnowfy.com/kmp-and-extend-kmp/
 * http://my.oschina.net/YeanXu/blog/15612s
 * http://3214668848.blog.163.com/blog/static/48764919200910152452182/
 */
// O(m+n)
// KMP�㷨��Ԥ�����Ӷ�O(n)��ƥ�临�Ӷ�O(m)���ܸ��Ӷ�O(n + m);
public class KMP
{
	static int[]		Next;
	private static int	times	= 10;

	public static void test(String parStr, String subStr)
	{
		long start = 0;
		long end = 0;
		System.out.println("����: " + parStr);
		System.out.println("�Ӵ�: " + subStr);
		start = System.currentTimeMillis();
		KMP.Next = KMP.preProcess(subStr.toCharArray());
		System.out.println(Arrays.toString(KMP.Next));
		for (int i = 0; i < times; i++) {
			System.out.println(KMP.kmp(parStr, subStr));
		}
		end = System.currentTimeMillis();
		System.out.println("Time for KMP: " + (end - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			System.out.println(SubStrFind.strFind(parStr, subStr));
		}
		end = System.currentTimeMillis();
		System.out.println("Time for Enumeration: " + (end - start));
		System.out.println("-------------------------------------");
	}

	/**
	 * ���Ӵ�����Ԥ�����Ӷ��ҵ�ƥ��ʧ��ʱ�Ӵ����˵�λ��
	 * 
	 * @param P
	 *            ���������Ӵ���char����
	 * @return
	 */
	public static int[] preProcess(char[] P)
	{
		int size = P.length;
		int[] Next = new int[size];
		Next[0] = 0;
		int j = 0;
		// j=0 i=1�ĳ�ʼ�� ��֤�� ��������B[j]==B[i]&& i==j
		// next[j] = max{k | 0 < k < j+1 �� 'p[0]...p[k-1]' =
		// 'p[j-k+1]...p[j]'}��Ӧ�ò������Ӵ�Ϊ�������� ��Ϊ����������β�ظ��Ӵ�����
		for (int i = 1; i < size; i++) {
			while (j > 0 && P[j] != P[i]) {
				j = Next[j];
			}
			if (P[j] == P[i]) {
				j++; // ��Ϊ�Ǹ��� ΪINDEX+1
			}
			// ��������� ��j=0
			Next[i] = j;
		}
		return Next;
	}

	/**
	 * KMPʵ��
	 * 
	 * @param parStr
	 * @param subStr
	 * @return
	 */
	public static int kmp(String parStr, String subStr)
	{
		int subSize = subStr.length();
		int parSize = parStr.length();
		char[] sub = subStr.toCharArray();
		char[] par = parStr.toCharArray();
		int[] B = new int[parSize];
		int j = 0;
		int count = 0;
		for (int i = 0; i < parSize; i++) {
			while (j > 0 && sub[j] != par[i]) {
				j = Next[j];
			}
			if (sub[j] == par[i]) {
				j++;
			}
			B[i] = j;

			if (j == subSize) {
				// �������´�
				// NEXT���INDEX SUBSIZE-1
				j = Next[j - 1];
				count++;
			}
		}
		return count;
	}

	// A��T��T�Լ���ƥ�伴NEXT
	// B��T��Sƥ��õ�����
	public static int extendKmp(String parStr, String subStr)
	{
		int subSize = subStr.length();
		int parSize = parStr.length();
		char[] sub = subStr.toCharArray();
		char[] par = parStr.toCharArray();
		int[] B = new int[parSize];
		int[] A = new int[subSize];
		int j = 0;
		int k = 0;
		j = 0;
		while (1 + j < subSize && sub[0 + j] == sub[1 + j]) {
			j++;
		}
		A[1] = j;
		k = 1;
		for (int i = 2; i < subSize; i++) {
			int Len = k + A[k] - 1, L = A[i - k];
			if (L < Len - i + 1) {
				A[i] = L;
			}
			else {
				j = Math.max(0, Len - i + 1);
				while (i + j < subSize && sub[i + j] == sub[0 + j]) {
					j = j + 1;
				}
				A[i] = j;
				k = i;
			}
		}
		while (j < parSize && j < subSize && sub[0 + j] == par[0 + j]) {
			j = j + 1;
		}
		B[0] = j;
		k = 0;
		for (int i = 1; i < parSize; i++) {
			int Len = k + B[k] - 1, L = A[i - k];
			if (L < Len - i + 1) {
				B[i] = L;
			}
			else {
				j = Math.max(0, Len - i + 1);
				while (i + j < parSize && j < subSize && par[i + j] == sub[0 + j]) {
					j = j + 1;
				}
				B[i] = j;
				k = i;
			}
		}
		return k;
	}

	// ��С��ʾ���ֳ�����С�����У���˵��һ���ַ�������һ��������n�����Ա�ʾ������˵�ѻ��п��� ����������С�ľ�����С�����У�����
	int MCP(String str)
	{
		// ע�⵽��С������ĸ�϶�����С�ģ�
		char[] s = str.toCharArray();
		int i = 0, j = 1, count = 0, t = 0, len = str.length();
		while (i < len && j < len && count < len) {
			t++;
			int x = i + count;
			int y = j + count;
			if (x >= len) {
				x -= len;
			}
			if (y >= len) {
				y -= len;
			}
			if (s[x] == s[y]) {
				count++;
			}
			else {
				if (s[x] > s[y])
					i = i + count + 1;
				else
					j = j + count + 1;
				count = 0;
			}
			if (j == i) {
				j = i + 1;
			}
		}
		return i;
	}

	public static void main(String[] args)
	{
		// ����λ������ΪP[0, 0, 0, 0, 0, 0]
		test("abcdeg, abcdeh, abcdef!�����ƥ��1��", "abcdef");
		// ����λ������ΪP[0, 0, 1, 2, 3, 4]
		test("Test ititi ititit! Test ititit!�����ƥ��2��", "ititit");
		
		test("aaaaat!�����ƥ��2��", "a");

	}

	// ��չ��KMP���⣺
	// ����ĸ��S�����Ӵ�T��
	// ����n=|S|, m=|T|, extend[i]=S[i..n]��T�������ǰ׺���ȡ�
	// �������Ե�ʱ�临�Ӷ��ڣ�������е�extend[1..n]��

	// ���׷��֣������ĳ��λ��i����extend[i]=m����ôT�Ϳ϶���S�г��ֹ������ҽ�һ��֪��������λ����i�����������Ǿ����KMP���⡣
	// ��˿ɼ�����չ��KMP���⡱�ǶԾ���KMP�����һ������ͼ��ѡ�

	// KMP�õ�����S[i-B[i]+1]..S[i]=T[0]..T[B[i]-1]������չKMP����˵S[i]..S[i+B[i]-1]=T[0]..T[B[i]-1]
}

// SubStrFind.java��ö���㷨��ʵ�֣�

/**
 * �ַ������ң�ö�ٷ������ң� ��һ���ַ����в����Ƿ����ĳһ�Ӵ�
 */
class SubStrFind
{
	/**
	 * �ַ������ң�ö�ٷ�����
	 * 
	 * @param parStr
	 * @param subStr
	 * @return
	 */
	public static int strFind(String parStr, String subStr)
	{
		int parSize = parStr.length();
		int subSize = subStr.length();

		char[] B = subStr.toCharArray();
		char[] A = parStr.toCharArray();

		boolean flag = true;
		int times = 0;
		int j = 0;
		int k = 0;// k��¼����ƥ����ȷ��λ�û���ƥ�䲻��ȷ�Ļ���λ��
		// i��¼�����ĵ�ǰ�Ƚ��ַ���λ��
		for (int i = 0; i < parSize; i++) {
			if (B[j] == A[i]) {
				j++;
				// ��һ��ʱ��¼��������λ��
				if (flag) {
					k = i;
					flag = false;
				}
			}
			else {
				// ��ƥ��ʱ����λ�����ã��Ƚϼ�������
				i = ++k;
				j = 0;
				flag = true;
			}
			if (j == subSize) {
				j = 0;// ƥ��ʱֻ����Ӵ�����λ�����ã��Ƚϼ�������
				flag = true;
				times++;
			}
		}
		return times;
	}
}
