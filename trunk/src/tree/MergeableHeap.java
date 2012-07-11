//http://blog.csdn.net/acceptedxukai/article/details/6921334
package tree;

import java.util.ArrayList;
import java.util.LinkedList;

/*
 * http://mindlee.net/2011/09/26/binomial-heaps/
 * http://mindlee.net/2011/09/29/fibonacci-heaps/
 * 
 * ������Bk��һ�ֵݹ鶨�����������������B0ֻ����һ����㡣������Bk����������Bk-1���Ӷ��ɣ�����һ�����ĸ�����һ�����ĸ��������ӡ��������������У�
 1)����2^k����㡣
 2)���ĸ߶�Ϊk����0��ʼ
 3)�����i��ǡ��Cik����㡣i��K��0��ʼ
 4)���Ķ�������Ů�ĸ�����Ϊk���������κ��������Ķ��������������Ů�����ҵı����Ϊk-1, k-2, ��, 0����Ůi������Bi�ĸ���
 ��ͼ��ͼ(b)��ʾ������B0��B4��ʾ���˸�������ȡ�ͼ(c)��������һ�ַ�ʽ����������Bk��
 PS : ��һ�ð���n�����Ķ������У��������������Ϊlgn. ��������Ϊk

 ����                    �����(���             ����� (���                        쳲�������(ƽ̯)
 MAKE-HEAP    ��(1)           ��(1)                     ��(1)
 INSERT       ��(lg n)        O(lg n)                  ��(1)
 MINIMUM      ��(1)           O(lg n)                  ��(1)
 EXTRACT-MIN  ��(lg n)        ��(lg n)                  O(lg n)
 UNION        ��(n)           O(lg n)                  ��(1)
 DECREASE-KEY ��(lg n)        ��(lg n)                  ��(1)
 DELETE       ��(lg n)        ��(lg n)                  O(lg n)
 */

// �Ӷ����֪ ���� ���� ���ڻ���ڸ���㣬�е��ڣ���Ϊ����û����ת�������漰��תҪ���⴦��
public class MergeableHeap
{
	public static int combinDigit(int m, int n)
	{
		int t = 0;
		if (m < n) {
			t = m;
			m = n;
			n = t;
		}
		int ret = 0;
		if (n == 0) {
			ret = 1;
		}
		else if (n == 1) {
			ret = m;
		}
		else {
			if (n > m / 2) {
				ret = combinDigit(m, m - n);
			}
			else {
				ret = combinDigit(m - 1, n) + combinDigit(m - 1, n - 1);
			}
		}
		return ret;
	}

	public static String printBlank(int space, int length)
	{
		StringBuilder s = new StringBuilder();
		for (int j = 0; j < space * length; j++) {
			s.append(" ");
		}
		return s.toString();
	}

	public static String printKey(String k, int length, String c)
	{
		if (k.length() < length) {
			StringBuilder s = new StringBuilder();
			for (int i = 0; i < length; i++) {
				s.append(c);
			}
			s.replace((length - k.length()) / 2, (length - k.length()) / 2 + k.length(), k);
			k = s.toString();
		}
		return k;
	}

}