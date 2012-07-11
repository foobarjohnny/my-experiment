//http://blog.csdn.net/acceptedxukai/article/details/6921334
package tree;

import java.util.ArrayList;
import java.util.LinkedList;

/*
 * http://mindlee.net/2011/09/26/binomial-heaps/
 * http://mindlee.net/2011/09/29/fibonacci-heaps/
 * 
 * 二项树Bk是一种递归定义的有序树。二项树B0只包含一个结点。二项树Bk由两个子树Bk-1连接而成：其中一棵树的根是另一棵树的根的最左孩子。二项树的性质有：
 1)共有2^k个结点。
 2)树的高度为k。从0开始
 3)在深度i处恰有Cik个结点。i与K从0开始
 4)根的度数（子女的个数）为k，它大于任何其他结点的度数；如果根的子女从左到右的编号设为k-1, k-2, …, 0，子女i是子树Bi的根。
 右图中图(b)表示二项树B0至B4中示出了各结点的深度。图(c)是以另外一种方式来看二项树Bk。
 PS : 在一棵包含n个结点的二项树中，任意结点的最大度数为lgn. 即结点度数为k

 操作                    二叉堆(最坏）             二项堆 (最坏）                        斐波那契堆(平摊)
 MAKE-HEAP    Θ(1)           Θ(1)                     Θ(1)
 INSERT       Θ(lg n)        O(lg n)                  Θ(1)
 MINIMUM      Θ(1)           O(lg n)                  Θ(1)
 EXTRACT-MIN  Θ(lg n)        Θ(lg n)                  O(lg n)
 UNION        Θ(n)           O(lg n)                  Θ(1)
 DECREASE-KEY Θ(lg n)        Θ(lg n)                  Θ(1)
 DELETE       Θ(lg n)        Θ(lg n)                  O(lg n)
 */

// 从定义可知 堆中 可以 大于或等于父结点，有等于，因为堆中没有旋转，树中涉及旋转要特殊处理
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