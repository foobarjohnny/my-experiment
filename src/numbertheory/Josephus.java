package numbertheory;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * http://blog.csdn.net/solofancy/article/details/4211770
 */
public class Josephus
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
		}
		end = System.currentTimeMillis();
		System.out.println("Time for ahoCorasick: " + (end - start));
	}

	// 从0开始
	// O(n)
	public static int getWinner(int step, int n)
	{
		int i, s;
		s = 0;
		for (i = 2; i <= n; i++) {
			s = (s + step) % i;
		}
		System.out.println(s + 1);
		return s + 1;
	}

	// 从1开始
	// O(n)
	public static int getWinner2(int step, int n)
	{
		int i, s;
		s = 1;
		for (i = 2; i <= n; i++) {
			s = (s + step) % i;
			if (s == 0) {
				s = i;
			}
		}
		System.out.println(s);
		return s;
	}

	// O(m) = O(step)
	int Josephus(int n, int m, int k) // 分别为：人数，出圈步长，起使报数位置,
	{
		int x = 0;
		if (m == 1) {
			k = k == 1 ? n : (k + n - 1) % n;
			// why not
			// k = k == 1 ? n : k - 1;
		}
		else {
			for (int i = 1; i <= n; i++) {
				if ((k + m) < i) {
					x = (i - k + 1) / (m - 1) - 1;
					if (i + x < n) {
						i = i + x;
						k = (k + m * x);
					}
					else {
						k = k + m * (n - i);// x = n-i;
						i = n;
					}
				}
				k = (k + m - 1) % i + 1;
			}
		}
		return k; // 返回最后一人的位置
	}

	public static ArrayList<Integer> getLosers(int step, int n)
	{
		ArrayList<Integer> a = new ArrayList<Integer>();
		int i = 0;
		int p = 0;
		while (++i <= n) {
			p = i * step;
			while (p > n) {
				p = p - n + (p - n - 1) / (step - 1);
			}
			System.out.println(p);
			a.add(p);
		}
		return a;
	}

	public static void main(String[] args)
	{

	}
}
