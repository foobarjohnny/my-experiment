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
 * �������� + �������㣬һ��ʼ�Լ��ƹ�ʽ�õ��Ľ�����: f(2 * n) = f(0) * f(2 * n - 2) + f(2) * f(2 * n -
 * 4) + f(4) * f(2 * n - 6) + ... + f(2 * n - 2) * f(0); Ȼ�󴿴���ڵ�������,
 * ���ֽ���ǶԵģ������ٶ�̫������ʱ�ˡ����������룬�ⲻ���ǿ��������𣿺Ǻǣ��������� F(n) = F(0) * F(n - 1) + F(1) *
 * F(n - 2) + ... + F(n - 1) * F(0) = C(2 * n, n) / (n + 1), F(0) = 1
 * ����ܳ����󲿷����Լ�д�Ĵ������㹤��ģ��, ����
 * 
 * ��ô��2N�Ƶ�N,û������????
 * 
 * Catalan Number ��չ���ϣ�
 * 
 * http://www.cppblog.com/abilitytao/archive/2010/04/12/112378.html
 * 
 * http://blog.csdn.net/famousdt/article/details/7378023
 * SIGMA���е�Ҫ����ѽ m<n
 * http://blog.csdn.net/wangzhewang/article/details/6842844
 * http://en.wikipedia.org/wiki/Catalan_number
 * �������ϸ:
 * http://blog.csdn.net/wuzhekai1985/article/details/6764858
 * 
 * ����JAVA C++û���׳�������е�API,��������DP��,�Կռ任ʱ��,��Ȼÿ����һ��ѳ�ʱѽ
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
