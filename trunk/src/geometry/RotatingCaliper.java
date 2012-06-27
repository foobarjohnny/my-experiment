package geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * http://softsurfer.com/Archive/algorithm_0109/algorithm_0109.htm
 * http://www.cnblogs.com/Booble/archive/2011/03/10/1980089.html
 * ��ס���������й�ϵ���д�ֱ�߶��й�ϵ
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
		Point[] U = new Point[P.length]; // ��͹������͹��
		/* ����͹����Andrew's Monotone Chain�� */

		// ����X���ˡ�����Y����
		// ��X��С����,���ʱY��С�� ����
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

		int l = 0, u = 0; // ��͹�����c������͹�����c��
		for (int i = 0; i < 10; ++i) {
			while (l >= 2 && Vector.direction(L[l - 2], L[l - 1], P[i]) <= 0)
				l--;
			while (u >= 2 && Vector.direction(U[u - 2], U[u - 1], P[i]) >= 0)
				u--;
			L[l++] = P[i];
			U[u++] = P[i];
		}

		/* ���D���� */

		// ��͹���~���a��һ��͹����c���Ա������
		if (u - 2 >= 0)
			L[l] = U[u - 2];
		System.out.println("l:" + l + " u:" + u);
		for (int i = 0, j = u - 1; i < l && j > 0;) {
			System.out.println("�A������cL[" + i + "]" + L[i] + "�c��c U[" + j + "] " + U[j]);

			// �·�߅�c�Ϸ�߅�ď��_�Ƕȣ�
			// С�180�㣬�t�Ϸ�ǰ�M��������·���ƽ���Ϸ�׃͹��
			// ���180�㣬�t�·�ǰ�M��������Ϸ���ƽ���·�׃͹��
			// ���180�㣬�tͬ�rǰ�M����ɓ�һǰ�M��
			if (Vector.crossProduct(new Vector(L[i + 1], L[i]), new Vector(U[j - 1], U[j])) < 0)
				i++; // �·�ǰ�M
			else
				j--; // �Ϸ�ǰ�M
		}
	}

}
