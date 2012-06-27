package geometry;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * http://softsurfer.com/Archive/algorithm_0109/algorithm_0109.htm
 * http://www.cnblogs.com/Booble/archive/2011/03/10/1980089.html
 * 
 * @author SDY
 * 
 */
public class AndrewMonotoneChain
{

	// Algorithm Speed Discovered By
	// Brute Force O(n4) [Anon, the dark ages]
	// Gift Wrapping O(nh) [Chand & Kapur, 1970]
	// Graham Scan O(n log n) [Graham, 1972]
	// Jarvis March O(nh) [Jarvis, 1973]
	// QuickHull O(nh) [Eddy, 1977], [Bykat, 1978]
	// Divide-and-Conquer O(n log n) [Preparata & Hong, 1977]
	// Monotone Chain O(n log n) [Andrew, 1979]
	// Incremental O(n log n) [Kallay, 1984]
	// Marriage-before-Conquest O(n log h) [Kirkpatrick & Seidel, 1986]

	// Andrew's Monotone Chain Algorithm
	// Sort S by increasing x- and then y-coordinate.
	// AMC��GRAHAM�㷨���� ������POINT���� ����Ҫ��������
	// O(nlogn)
	public static ArrayList<Point> andrewMonotoneChain(Point[] P)
	{
		int n = P.length;
		Point[] H = new Point[n];
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
		// the output array H[] will be used as the stack
		int bot = 0, top = (-1); // indices for bottom and top of the stack
		int i; // array scan index

		// Get the indices of points with min x-coord and min|max y-coord
		int minmin = 0, minmax;
		double xmin = P[0].x;
		for (i = 1; i < n; i++) {
			if (P[i].x != xmin) {
				break;
			}
		}
		minmax = i - 1;
		if (minmax == n - 1) { // degenerate case: all x-coords == xmin
			H[++top] = P[minmin];
			if (P[minmax].y != P[minmin].y) { // a nontrivial segment
				H[++top] = P[minmax];
			}
			H[++top] = P[minmin]; // add polygon endpoint
			// return top + 1;
			return new ArrayList<Point>(Arrays.asList(Arrays.copyOf(H, top + 1)));
		}

		// Get the indices of points with max x-coord and min|max y-coord
		int maxmin, maxmax = n - 1;
		double xmax = P[n - 1].x;
		for (i = n - 2; i >= 0; i--) {
			if (P[i].x != xmax) {
				break;
			}
		}
		maxmin = i + 1;

		// Compute the lower hull on the stack H
		H[++top] = P[minmin]; // push minmin point onto stack
		i = minmax;
		while (++i <= maxmin) {
			// the lower line joins P[minmin] with P[maxmin]
			if (Vector.direction(P[minmin], P[maxmin], P[i]) <= 0 && i < maxmin) {
				continue; // ignore P[i] above or on the lower line
			}
			while (top > 0) // there are at least 2 points on the stack
			{
				// test if P[i] is left of the line at the stack top
				if (Vector.direction(H[top - 1], H[top], P[i]) < 0) {
					break; // P[i] is a new hull vertex
				}
				else {
					top--; // pop top point off stack
				}
			}
			H[++top] = P[i]; // push P[i] onto stack
		}

		// Next, compute the upper hull on the stack H above the bottom hull
		if (maxmax != maxmin) { // if distinct xmax points
			H[++top] = P[maxmax]; // push maxmax point onto stack
		}
		bot = top; // the bottom point of the upper hull stack
		i = maxmin;
		while (--i >= minmax) {
			// the upper line joins P[maxmax] with P[minmax]
			if (Vector.direction(P[maxmax], P[minmax], P[i]) <= 0 && i > minmax) {
				continue; // ignore P[i] below or on the upper line
			}
			while (top > bot) // at least 2 points on the upper stack
			{
				// test if P[i] is left of the line at the stack top
				if (Vector.direction(H[top - 1], H[top], P[i]) < 0) {
					break; // P[i] is a new hull vertex
				}
				else {
					top--; // pop top point off stack
				}
			}
			H[++top] = P[i]; // push P[i] onto stack
		}
		if (minmax != minmin) {
			H[++top] = P[minmin]; // push joining endpoint onto stack
		}
		// return top + 1;
		return new ArrayList<Point>(Arrays.asList(Arrays.copyOf(H, top + 1)));
	}

	public static ArrayList<Point> andrewMonotoneChain2(Point[] P)
	{
		// �������c�������˴�С����
		int n = P.length;
		Point[] CH = new Point[n];
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

		int m = 0; // m ��͹����c��Ŀ

		// ���°벿
		for (int i = 0; i < 10; ++i) {
			while (m >= 2 && Vector.direction(CH[m - 2], CH[m - 1], P[i]) >= 0)
				m--;
			CH[m++] = P[i];
		}

		// ���ϰ벿�������ٰ��뷽�Ű��^�ĽK�c�������ٰ�һ�����c
		for (int i = 10 - 2, t = m + 1; i >= 0; --i) {
			while (m >= t && Vector.direction(CH[m - 2], CH[m - 1], P[i]) >= 0)
				m--;
			CH[m++] = P[i];
		}

		m--; // ����һ���c�����}���F�ɴε����c����Ҫ�pһ��
		return new ArrayList<Point>(Arrays.asList(Arrays.copyOf(CH, m)));
	}

}
