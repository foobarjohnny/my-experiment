package stringmatch;

import java.util.Arrays;

/**
 http://blog.csdn.net/hairetz/article/details/4729397
 http://blog.csdn.net/zhanglizhe_cool/article/details/5576037
 */
/**
 * Sunday�㷨��Daniel M.Sunday��1990�������һ�ֱ�BM�㷨�����ٶȸ�����㷨�������˼���ǣ���ƥ������У�
 * ģʽ��������Ҫ��һ��Ҫ���������ҽ��бȽϻ��Ǵ���������бȽϣ����ڷ��ֲ�ƥ��ʱ���㷨�����������ܶ���ַ��Խ�����һ����ƥ�䣬�Ӷ������ƥ��Ч�ʡ�
 * 
 * Sunday�㷨˼���BM�㷨�����ƣ���ƥ��ʧ��ʱ��ע�����ı����вμ�ƥ�����ĩλ�ַ�����һλ�ַ���������ַ�û����ƥ�䴮�г�����ֱ�����������ƶ�����=
 * ƥ�䴮����+1������ͬBM�㷨һ�����ƶ�����=ƥ�䴮�����Ҷ˵ĸ��ַ���ĩβ�ľ���+1��
 */
// Sunday�㷨
// ʱ���O(N*M)
public class Sunday
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
			System.out.println(sunday(parStr, subStr));
		}
		end = System.currentTimeMillis();
		System.out.println("Time for sunday: " + (end - start));
	}

	// bad character ���BM�е�BC�Ա�
	public static void nextChar(char[] subStr, int m, int[] bmBc)
	{
		int i;
		for (i = 0; i < Character.MAX_VALUE; ++i) {
			bmBc[i] = m;
		}
		// ��0��M-1 ��֤�� Ϊ����
		// ��BM�кβ�ͬ ��Ҫ��1;
		// �������� ����ƥ��Ĳ��ǲ�ͬ���ַ� ����ģʽ����һ���ַ�
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
		// �ɲ⺺��
		/* Preprocessing */
		nextChar(sub, m, bmBc);

		/* Searching */
		int count = 0;
		int pos = 0;
		while (pos < (n - m + 1)) // ����ԭ�� ��ϸ�Ա�һ�º�BM�кβ�ͬ
		{
			i = pos;
			for (j = 0; j < m; ++j, ++i) // �Ƚ�
			{
				if (par[i] != sub[j]) // һ����ƥ�䣬ԭ���Ͱ���next��ת
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
		return count; // ���Ӵ��򷵻�-1
	}

	// public static int sunday2( char[] src, char[] des)
	// {
	// int i,j,pos=0;
	// int len_s,len_d;
	// int next[26]={0}; //next���飬Ԥ�����ʼ��
	// len_s=strlen(src);
	// len_d=strlen(des);
	// for(j=0;j<26;++j) //��ʼ��next����
	// next[j]=len_d;
	// for(j=0;j<len_d;++j) //����next����
	// next[des[j]-'a']=len_d-j;
	// while( pos<(len_s-len_d+1) ) //����ԭ��
	// {
	// i=pos;
	// for(j=0;j<len_d;++j,++i) //�Ƚ�
	// {
	// if(src[i]!=des[j]) //һ����ƥ�䣬ԭ���Ͱ���next��ת
	// {
	// pos+=next[src[pos+len_d]-'a'];
	// break;
	// }
	// }
	// if(j==len_d)
	// return pos;
	// }
	// return -1; //���Ӵ��򷵻�-1
	// }

	public static void main(String[] args)
	{
		// ����λ������ΪP[0, 0, 0, 0, 0, 0]
		// System.out.println("abcdeg, abcdeh, abcdef!�����ƥ��1�� ,  abcdef");
		// test("abcdegabcdehabcdef", "abcdef");
		// // ����λ������ΪP[0, 0, 1, 2, 3, 4]
		// System.out.println("Test ititi ititit! Test ititit!�����ƥ��2�� , ititit");
		//
		// test("testititiititittestititit", "ititit");
		// test("testititiititittestititit", "ititit");
		//
		// test("ushers", "her");

		test("Test ititi ititit! Test ititit!�����ƥ��2��", "ititit");
	}
}
