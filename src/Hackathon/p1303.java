package Hackathon;

import java.math.BigInteger;
import java.util.Scanner;

/**
 * Game of Connections:http://poj.org/problem?id=2084
 * 
 * Solutions:
 * 
 * http://blog.csdn.net/wings_of_liberty/article/details/7459175
 * http://bobten2008.iteye.com/blog/675396
 * 
 * 卡特兰数 + 大数运算，一开始自己推公式得到的结论是: f(2 * n) = f(0) * f(2 * n - 2) + f(2) * f(2 * n -
 * 4) + f(4) * f(2 * n - 6) + ... + f(2 * n - 2) * f(0); 然后纯粹基于递推来算,
 * 发现结果是对的，但是速度太慢，超时了。后来想了想，这不就是卡特兰数吗？呵呵，问题解决了 F(n) = F(0) * F(n - 1) + F(1) *
 * F(n - 2) + ... + F(n - 1) * F(0) = C(2 * n, n) / (n + 1), F(0) = 1
 * 代码很长，大部分是自己写的大数运算工具模板, 哈哈
 * 
 * 怎么从2N推到N,没想明白????
 * 
 * Catalan Number 扩展资料：
 * 
 * http://www.cppblog.com/abilitytao/archive/2010/04/12/112378.html
 * 
 * http://blog.csdn.net/famousdt/article/details/7378023
 * SIGMA序列得要想想呀 m<n
 * http://blog.csdn.net/wangzhewang/article/details/6842844
 * http://en.wikipedia.org/wiki/Catalan_number
 * 这个最详细:
 * http://blog.csdn.net/wuzhekai1985/article/details/6764858
 * 
 * 鉴于JAVA C++没带阶乘组合排列的API,还不如用DP呢,以空间换时间,不然每次算一大堆超时呀
 * 
 * @author dysong
 * 
 */
public class p1303
{
	public static void main(String[] args)
	{
		int n, i, j;
		int N = 30;
		BigInteger[] dp = new BigInteger[N + 1];
		Scanner sc = new Scanner(System.in);
		dp[0] = BigInteger.ONE;
		for (i = 1; i < N + 1; i++) {
			dp[i] = BigInteger.ZERO;
			for (j = 0; j < i; j++) {
				dp[i] = dp[i].add(dp[j].multiply(dp[i - 1 - j]));
			}
		}
		while (sc.hasNext()) {
			n = sc.nextInt();
			if (n == -1) {
				break;
			}
			System.out.println(dp[n]);
		}
	}
}
