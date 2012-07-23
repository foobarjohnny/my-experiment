package matrix;

import java.util.Random;
import java.util.Scanner;

/*
 * http://www.codeforge.cn/read/113654/strassen.cpp__html
 * http://riddickbryant.iteye.com/blog/546463
 * http://hi.baidu.com/%C8%F4%CB%AE%C0%BC%D0%F9/blog/item/a10f807071563b048601b0e6.html
 * http://zh.wikipedia.org/zh-cn/Strassen%E6%BC%94%E7%AE%97%E6%B3%95
 * ʩ����ɭ�㷨
 */
//һ�����˷���ʱ�临�Ӷ�ΪO(n^lg8)��Strassen�㷨����O(n^lg7)=O(n^2.807)���еñ���ʧ��Strassen�㷨����ֵ�ȶ��Խϲ
//��ʱʱ�临�Ӷ���͵ľ���˷��㷨Coppersmith-Winograd������O(n^2.376)��
public class Strassen
{
	public static int log2(int value) // �ǵݹ��ж�һ������2�Ķ��ٴη�
	{
		int x = 0;
		while (value > 1) {
			value >>= 1;
			x++;
		}
		return x;
	}

	// �Ծ�����л��֣�������ȷֳ��ĸ���
	public static void divMatrix(int[][] a, int[][] A11, int[][] A12, int[][] A21, int[][] A22, int size)
	{
		int i, j;
		for (i = 1; i <= size; i++) {
			for (j = 1; j <= size; j++) {
				A11[i][j] = a[i][j];
				A12[i][j] = a[i][j + size];
				A21[i][j] = a[i + size][j];
				A22[i][j] = a[i + size][j + size];
			}
		}
	}

	// �Ծ�����кϲ������ĸ�С����ϲ���һ������󣬼�����СC�ϲ�������C��
	public static int[][] mergeMatrix(int[][] C11, int[][] C12, int[][] C21, int[][] C22, int size)
	{
		int[][] C = new int[2 * size + 1][2 * size + 1];
		int i, j;
		for (i = 1; i <= size; i++) {
			for (j = 1; j <= size; j++) {
				C[i][j] = C11[i][j];
				C[i][j + size] = C12[i][j];
				C[i + size][j] = C21[i][j];
				C[i + size][j + size] = C22[i][j];
			}
		}
		return C;
	}

	// ������������ļӷ�
	public static int[][] addMatrix(int[][] a, int[][] b, int n)
	{
		int[][] C = new int[n + 1][n + 1];
		int i, j;
		for (i = 1; i <= n; i++) {
			for (j = 1; j <= n; j++) {
				C[i][j] = a[i][j] + b[i][j];
			}
		}
		return C;
	}

	// ������������ļ���
	public static int[][] subMatrix(int[][] a, int[][] b, int n)
	{
		int[][] C = new int[n + 1][n + 1];
		int i, j;
		for (i = 1; i <= n; i++) {
			for (j = 1; j <= n; j++) {
				C[i][j] = a[i][j] - b[i][j];
			}
		}
		return C;
	}

	// �������Ϊ2�ľ���˷�
	public static int[][] twoMatrix(int[][] A, int[][] B)
	{
		int m1, m2, m3, m4, m5, m6, m7;
		int[][] C = new int[3][3];
		// �����7�γ˷�����
		m1 = A[1][1] * (B[1][2] - B[2][2]);
		m2 = (A[1][1] + A[1][2]) * B[2][2];
		m3 = (A[2][1] + A[2][2]) * B[1][1];
		m4 = A[2][2] * (B[2][1] - B[1][1]);
		m5 = (A[1][1] + A[2][2]) * (B[1][1] + B[2][2]);
		m6 = (A[1][2] - A[2][2]) * (B[2][1] + B[2][2]);
		m7 = (A[1][1] - A[2][1]) * (B[1][1] + B[1][2]);
		// ���ɴμӷ��������վ���˷����
		C[1][1] = m5 + m4 - m2 + m6;
		C[1][2] = m1 + m2;
		C[2][1] = m3 + m4;
		C[2][2] = m5 + m1 - m3 - m7;
		return C;
	}

	// �������Ϊn��strassen����˷������еݹ����
	public static int[][] strassenMatrix(int[][] a, int[][] b, int n)
	{
		if (a == null || b == null) {
			System.out.println("������Ч��������һ��ΪNULL");
			return null;
		}
		int[][] c; // ��������c����
		int size = n;
		// nΪ1ʱ
		if (size == 1) {
			c = new int[size + 1][size + 1];
			c[1][1] = a[1][1] * b[1][1];
			return c;
		}
		// size����2����
		if ((size & size - 1) != 0) {
			size = 1 << log2(size) + 1; // size����Ϊ��С�Ĵ�������2����
			a = extendMatrix(a, n, size);
			b = extendMatrix(b, n, size);
			c = strassenMatrix(a, b, size);
			c = shrinkMatrix(c, size, n);
			return c;
		}
		// size Ϊ2
		if (size == 2) {
			c = twoMatrix(a, b);
			return c;
		}
		else // �ָ����
		{
			size = size / 2;
			// �����ĸ���A���л��ֵľ���
			int[][] A11 = new int[size + 1][size + 1];
			int[][] A12 = new int[size + 1][size + 1];
			int[][] A21 = new int[size + 1][size + 1];
			int[][] A22 = new int[size + 1][size + 1];
			// �����ĸ���B���л��ֵľ���
			int[][] B11 = new int[size + 1][size + 1];
			int[][] B12 = new int[size + 1][size + 1];
			int[][] B21 = new int[size + 1][size + 1];
			int[][] B22 = new int[size + 1][size + 1];
			// �����ĸ���c���л��ֵľ���
			int[][] c11;
			int[][] c12;
			int[][] c21;
			int[][] c22;
			// �����߸��������7�γ˷�����Ľ��
			int[][] m1;
			int[][] m2;
			int[][] m3;
			int[][] m4;
			int[][] m5;
			int[][] m6;
			int[][] m7;
			divMatrix(a, A11, A12, A21, A22, size); // �Ծ���a���л���
			divMatrix(b, B11, B12, B21, B22, size); // �Ծ���b���л���
			// divmatrix(c, c11, c12, c21, c22, size); // �Ծ���c���л���

			m1 = strassenMatrix(A11, subMatrix(B12, B22, size), size);
			m2 = strassenMatrix(addMatrix(A11, A12, size), B22, size);
			m3 = strassenMatrix(addMatrix(A21, A22, size), B11, size);
			m4 = strassenMatrix(A22, subMatrix(B21, B11, size), size);
			m5 = strassenMatrix(addMatrix(A11, A22, size), addMatrix(B11, B22, size), size);
			m6 = strassenMatrix(subMatrix(A12, A22, size), addMatrix(B21, B22, size), size);
			m7 = strassenMatrix(subMatrix(A11, A21, size), addMatrix(B11, B12, size), size);

			c11 = addMatrix(subMatrix(addMatrix(m5, m4, size), m2, size), m6, size);
			c12 = addMatrix(m1, m2, size);
			c21 = addMatrix(m3, m4, size);
			c22 = subMatrix(subMatrix(addMatrix(m5, m1, size), m3, size), m7, size);

			c = mergeMatrix(c11, c12, c21, c22, size);
			return c;
		}
	}

	public static int[][] extendMatrix(int[][] src, int oldSize, int newSize)
	{
		// padding with 0
		if (oldSize > newSize) {
			return null;
		}
		if (oldSize == newSize) {
			return src;
		}
		int[][] dest = new int[newSize + 1][newSize + 1];
		int i, j;
		for (i = 1; i <= oldSize; i++) {
			for (j = 1; j <= oldSize; j++) {
				dest[i][j] = src[i][j];
			}
		}
		return dest;
	}

	public static int[][] shrinkMatrix(int[][] src, int oldSize, int newSize)
	{
		if (oldSize < newSize) {
			return null;
		}
		if (oldSize == newSize) {
			return src;
		}
		int[][] dest = new int[newSize + 1][newSize + 1];
		int i, j;
		for (i = 1; i <= newSize; i++) {
			for (j = 1; j <= newSize; j++) {
				dest[i][j] = src[i][j];
			}
		}
		return dest;
	}

	// ��ͨ�ķ����������˷�
	// O(n^3)
	public static int[][] multiMatrix(int[][] a, int[][] b, int n)
	{
		int c[][] = new int[n + 1][n + 1];
		int i, j, k;
		for (i = 1; i <= n; i++) {
			for (j = 1; j <= n; j++) {
				for (k = 1; k <= n; k++) {
					c[i][j] += a[i][k] * b[k][j];
				}
			}
		}
		return c;
	}

	public static void main(String[] args)
	{
		Random r = new Random();
		int n = r.nextInt(100);
		n = 5;
		int m = 100;
		int[][] A = new int[n + 1][n + 1];
		int[][] B = new int[n + 1][n + 1];
		int[][] C = new int[n + 1][n + 1];
		int[][] D = new int[n + 1][n + 1];
		int i, j;
		System.out.println("����Ľ���:" + n);
		System.out.println("�������A:");
		for (i = 1; i <= n; i++) {
			for (j = 1; j <= n; j++) {
				A[i][j] = r.nextInt(100);
			}
		}
		System.out.println("�������B:");
		for (i = 1; i <= n; i++) {
			for (j = 1; j <= n; j++) {
				B[i][j] = r.nextInt(100);
			}
		}
		C = strassenMatrix(A, B, n);
		D = multiMatrix(A, B, n);
		System.out.println("��˺��Strassen����Ϊ��");
		for (i = 1; i <= n; i++) {
			for (j = 1; j <= n; j++) {
				System.out.print(C[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println("��ͨ��˺�ľ���Ϊ��");
		for (i = 1; i <= n; i++) {
			for (j = 1; j <= n; j++) {
				System.out.print(D[i][j] + "\t");
			}
			System.out.println();
		}
	}

}