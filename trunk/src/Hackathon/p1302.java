package Hackathon;

/**
 * ZOJ Problem Set - 1180 :Self Numbers
 * http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1180
 * 
 * É¸Ñ¡·¨
 * 
 * Solutions: http://hi.baidu.com/lgzhilian_001/item/215f66cef7837ecd984aa0b0
 * 
 * @author dysong
 * 
 */

public class p1302
{
	public static void main(String[] args) throws Exception
	{
		int N = 1000000;
		boolean[] flag = new boolean[N + 1];
		for (int j = 1; j <= N; j++) {
			flag[j] = true;
		}
		int index = 1;
		while (index <= N) {
			int i = index;
			if (flag[i] == true) {
				int z = gen(i);
				while (z <= N && flag[z] == true) {
					flag[z] = false;
					z = gen(z);
				}
			}
			index++;
		}
		for (int j = 1; j <= N; j++) {
			if (flag[j] == true) {
				System.out.println(j);
			}
		}
	}

	private static int gen(int i)
	{
		int ret = i;
		if (i < 10) {
			return ret += i;
		}
		else {
			while (i > 0) {
				int digit = i % 10;
				ret += digit;
				i = i / 10;
			}
			return ret;
		}
	}
}