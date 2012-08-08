package numbertheory;

/**
 * http://blog.csdn.net/dellaserss/article/details/7751217 
 * 求素数 
 * 1、 * 试除法 用 n 除以 * 2-sqrt(n),有一个能除尽就不是素数，否则是素数。 时间复杂度：O(sqrt(n))
 * 
 * 2、素数判断法 这种方法是对上面方法的改进，上面方法是对 2-sqrt(n)之间的数进行判断是否能除尽，而 因为有如下算术基本定理，可以减少判断量。
 * 算术基本定理：又称为素数的唯一分解定理，即：每个大于 1 的自然数均可写为素数的积， 而且这些素因子按大小排列之后，写法仅有一种方式。 例如:6936 =
 * 2^3×3×17^2，1200 = 2^4×3×5^2。 由算术基本定理知，任何合数都可分解为一些素数的乘积，所以判断一个数能不能被
 * 2-sqrt(n)之间的素数整除即可。 但是必须知道 2-sqrt(n)之间的所有素数。
 * 
 * 3、筛选法 这种方法可以找出一定范围内的所有的素数。 思路是，要求 10000 以内的所有素数，把 1-10000 这些数都列出来，1 不是素数，划掉；2
 * 是素数，所有 2 的倍数都不是素数，划掉；取出下一个幸存的数，划掉它的所有倍数；直到 所有幸存的数的倍数都被坏掉为止。 要找出 10000
 * 以为的所有的素数，则需要一个大小为 10000 的数组,将其所有元素设置为未标记首先把 1 设置为标记，从 2 开始，标记所有是它
 * 倍数的数，然后对下一个没有标记的数进行标记它的倍数。当标记完成后，所有未标记的数 即为素数。
 */
public class Prime
{

	int prime2(int n)
	{
		int i, j;
		int[] prime = new int[1000000 + 1];
		for (i = 3; i <= 1000000; i += 2)
			prime[i] = 1;

		for (i = 3; i < 500000; i++)
			for (j = 1; j < 1000000 / i; j++)
				prime[i + j * i] = 0;
		while (n != 0) {
			for (i = 3; i <= n / 2; i += 2)
				if (prime[i] == 1 && prime[n - i] == 1)
					break;
			System.out.println(n + "=" + i + "+" + (n - i));
		}
		return 0;
	}

	// 升级版
	int prime(int n)
	{
		int[] prime = new int[1000000 + 1];
		int i;
		for (i = 3; i <= 1000000; i += 2)
			prime[i] = 1;

		for (i = 3; i < 1000; i++) {
			if (prime[i] == 1) {
				int i2 = i + i;
				int k = i * i;
				while (k < 1000000) {
					prime[k] = 0;
					k += i2;
				}
			}
		}
		System.out.println(prime[6]);
		while (n != 0) {
			for (i = 3; i <= n / 2; i += 2)
				if (prime[i] == 1 && prime[n - i] == 1)
					break;
			System.out.println(n + "=" + i + "+" + (n - i));
		}
		return 0;
	}

	public static void main(String[] args)
	{

	}
}
