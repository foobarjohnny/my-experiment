package geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * http://softsurfer.com/Archive/algorithm_0109/algorithm_0109.htm
 * http://www.cnblogs.com/Booble/archive/2011/03/10/1980089.html
 * 记住叉积与面积有关系，有垂直高度有关系
 * 
 * http://www.csie.ntnu.edu.tw/~u91029/PointLinePlane2.html
 * 
 * @author SDY
 */
public class RotatingCaliper
{

	public static void rotatingCaliper(Point[] P)
	{

		int n = P.length;

		Point[] L = new Point[P.length + 1];
		Point[] U = new Point[P.length]; // 下凸包、上凸包
		/* 尋找凸包，Andrew's Monotone Chain。 */

		// 先排X座標、再排Y座標
		// 按X从小到大,相等时Y从小大到 排序
		Point tmp;
		for (int i = 0; i < n - 1; i++) {
			for (int j = i + 1; j < n; j++) {
				if ((P[i].x > P[j].x) || ((P[i].x == P[j].x) && (P[i].y > P[j].y))) {
					tmp = P[i];
					P[i] = P[j];
					P[j] = tmp;
				}
			}
		}

		int l = 0, u = 0; // 上凸包的點數、下凸包的點數
		for (int i = 0; i < 10; ++i) {
			while (l >= 2 && Vector.direction(L[l - 2], L[l - 1], P[i]) <= 0)
				l--;
			while (u >= 2 && Vector.direction(U[u - 2], U[u - 1], P[i]) >= 0)
				u--;
			L[l++] = P[i];
			U[u++] = P[i];
		}

		/* 旋轉卡尺 */

		// 下凸包額外補上一個凸包頂點，以便操作。
		if (u - 2 >= 0)
			L[l] = U[u - 2];
		System.out.println("l:" + l + " u:" + u);
		for (int i = 0, j = u - 1; i < l && j > 0;) {
			System.out.println("夾到了頂點L[" + i + "]" + L[i] + "與頂點 U[" + j + "] " + U[j]);

			// 下方邊與上方邊的張開角度：
			// 小於180°，則上方前進。想像成下方很平、上方變凸。
			// 大於180°，則下方前進。想像成上方很平、下方變凸。
			// 等於180°，則同時前進，亦可擇一前進。
			if (Vector.crossProduct(new Vector(L[i + 1], L[i]), new Vector(U[j - 1], U[j])) < 0)
				i++; // 下方前進
			else
				j--; // 上方前進
		}
	}

}
