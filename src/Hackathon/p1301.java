package Hackathon;

import java.util.Scanner;

/**
 * Candy Sharing Game:
 * http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1814
 * solutions:http://www.cnblogs.com/cnnbboy/archive/2008/08/28/1278891.html
 * 
 * @author dysong
 * 
 */
public class p1301
{
	public static void main(String[] args) throws Exception
	{
		Scanner scanner = new Scanner(System.in);
		while (true) {
			int count = scanner.nextInt();
			if (count == 0) {
				break;
			}
			int[] A = new int[count];
			int[] B = new int[count];
			for (int i = 0; i < count; ++i) {
				A[i] = scanner.nextInt();
			}
			int[] ret = giveCandy(A, B, count);
			System.out.print(ret[0] + " " + ret[1]);
			System.out.print("\n");
		}
	}

	private static int[] giveCandy(int[] a, int[] b, int count)
	{
		int p = 0;
		int[] ret = new int[2];
		while (true) {
			for (int i = 0; i < count; i++) {
				if (i == count - 1) {
					b[i] = a[0] / 2 + a[i] / 2;
				}
				else {
					b[i] = a[i + 1] / 2 + a[i] / 2;
				}
				if (b[i] % 2 != 0) {
					b[i]++;
				}
			}
			p++;
			int[] temp = null;
			int num = b[0];
			boolean flag = true;
			for (int i = 0; i < count; i++) {
				if (b[i] != num) {
					flag = false;
				}
			}
			if (flag == true) {
				ret[0] = p;
				ret[1] = b[0];
				return ret;
			}
			else {
				temp = a;
				a = b;
				b = temp;
			}
		}

	}

	private static void printArray(int[] a)
	{
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i] + " ");
		}
		System.out.println();
	}
}