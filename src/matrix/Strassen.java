package matrix;

import java.util.Random;
import java.util.Scanner;

/*
 * http://www.codeforge.cn/read/113654/strassen.cpp__html
 * http://riddickbryant.iteye.com/blog/546463
 * http://hi.baidu.com/%C8%F4%CB%AE%C0%BC%D0%F9/blog/item/a10f807071563b048601b0e6.html
 * http://zh.wikipedia.org/zh-cn/Strassen%E6%BC%94%E7%AE%97%E6%B3%95
 * 施特拉森算法
 */
//一般矩阵乘法的时间复杂度为O(n^lg8)，Strassen算法则是O(n^lg7)=O(n^2.807)。有得必有失，Strassen算法的数值稳定性较差。
//现时时间复杂度最低的矩阵乘法算法Coppersmith-Winograd方法是O(n^2.376)。
public class Strassen
{
	public static int log2(int value) // 非递归判断一个数是2的多少次方
	{
		int x = 0;
		while (value > 1) {
			value >>= 1;
			x++;
		}
		return x;
	}

	// 对矩阵进行划分，将矩阵等分成四个。
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

	// 对矩阵进行合并，将四个小矩阵合并成一个大矩阵，即：将小C合并成整体C。
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

	// 计算两个矩阵的加法
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

	// 计算两个矩阵的减法
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

	// 计算阶数为2的矩阵乘法
	public static int[][] twoMatrix(int[][] A, int[][] B)
	{
		int m1, m2, m3, m4, m5, m6, m7;
		int[][] C = new int[3][3];
		// 矩阵的7次乘法运算
		m1 = A[1][1] * (B[1][2] - B[2][2]);
		m2 = (A[1][1] + A[1][2]) * B[2][2];
		m3 = (A[2][1] + A[2][2]) * B[1][1];
		m4 = A[2][2] * (B[2][1] - B[1][1]);
		m5 = (A[1][1] + A[2][2]) * (B[1][1] + B[2][2]);
		m6 = (A[1][2] - A[2][2]) * (B[2][1] + B[2][2]);
		m7 = (A[1][1] - A[2][1]) * (B[1][1] + B[1][2]);
		// 若干次加法计算最终矩阵乘法结果
		C[1][1] = m5 + m4 - m2 + m6;
		C[1][2] = m1 + m2;
		C[2][1] = m3 + m4;
		C[2][2] = m5 + m1 - m3 - m7;
		return C;
	}

	// 计算阶数为n的strassen矩阵乘法，含有递归调用
	public static int[][] strassenMatrix(int[][] a, int[][] b, int n)
	{
		if (a == null || b == null) {
			System.out.println("输入无效，其中有一个为NULL");
			return null;
		}
		int[][] c; // 计算结果的c矩阵
		int size = n;
		// n为1时
		if (size == 1) {
			c = new int[size + 1][size + 1];
			c[1][1] = a[1][1] * b[1][1];
			return c;
		}
		// size不是2的幂
		if ((size & size - 1) != 0) {
			size = 1 << log2(size) + 1; // size扩充为最小的大于它的2的幂
			a = extendMatrix(a, n, size);
			b = extendMatrix(b, n, size);
			c = strassenMatrix(a, b, size);
			c = shrinkMatrix(c, size, n);
			return c;
		}
		// size 为2
		if (size == 2) {
			c = twoMatrix(a, b);
			return c;
		}
		else // 分割矩阵
		{
			size = size / 2;
			// 定义四个对A进行划分的矩阵
			int[][] A11 = new int[size + 1][size + 1];
			int[][] A12 = new int[size + 1][size + 1];
			int[][] A21 = new int[size + 1][size + 1];
			int[][] A22 = new int[size + 1][size + 1];
			// 定义四个对B进行划分的矩阵
			int[][] B11 = new int[size + 1][size + 1];
			int[][] B12 = new int[size + 1][size + 1];
			int[][] B21 = new int[size + 1][size + 1];
			int[][] B22 = new int[size + 1][size + 1];
			// 定义四个对c进行划分的矩阵
			int[][] c11;
			int[][] c12;
			int[][] c21;
			int[][] c22;
			// 定义七个矩阵放置7次乘法运算的结果
			int[][] m1;
			int[][] m2;
			int[][] m3;
			int[][] m4;
			int[][] m5;
			int[][] m6;
			int[][] m7;
			divMatrix(a, A11, A12, A21, A22, size); // 对矩阵a进行划分
			divMatrix(b, B11, B12, B21, B22, size); // 对矩阵b进行划分
			// divmatrix(c, c11, c12, c21, c22, size); // 对矩阵c进行划分

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

	// 普通的方法计算矩阵乘法
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
		System.out.println("矩阵的阶数:" + n);
		System.out.println("输入矩阵A:");
		for (i = 1; i <= n; i++) {
			for (j = 1; j <= n; j++) {
				A[i][j] = r.nextInt(100);
			}
		}
		System.out.println("输入矩阵B:");
		for (i = 1; i <= n; i++) {
			for (j = 1; j <= n; j++) {
				B[i][j] = r.nextInt(100);
			}
		}
		C = strassenMatrix(A, B, n);
		D = multiMatrix(A, B, n);
		System.out.println("相乘后的Strassen矩阵为：");
		for (i = 1; i <= n; i++) {
			for (j = 1; j <= n; j++) {
				System.out.print(C[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println("普通相乘后的矩阵为：");
		for (i = 1; i <= n; i++) {
			for (j = 1; j <= n; j++) {
				System.out.print(D[i][j] + "\t");
			}
			System.out.println();
		}
	}

}