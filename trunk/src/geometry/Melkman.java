package geometry;

import java.util.ArrayList;

/**
 * http://hi.baidu.com/35661327/blog/item/701641c207351a5bb319a851.html
 * http://suanyu.blog.163.com/blog/static/191362225201171410413701/
 * http://cgm.cs.mcgill.ca/~athens/cs601/Melkman.html
 * 
 * @author SDY
 * 
 */

// ������melkman�㷨��͹��ǰ����Ҫ�������ĵ㼯�γɼ򵥶���Ρ�
// �����Ƚ��㼯���а��������������������Ȼ��ǰ�����㰴��˳ʱ���˳����ʱ��Ҳ�ǿ��Եģ�ֻ����������ж�����Ҫ������Ӧ�ĸ��ģ�����һ��˫������У�����ͷ����Ϊhead������β����Ϊtail��
public class Melkman
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

	// Melkman�����ߵ��㷨 O(n) Ҫ��֤�����Ǽ򵥶���� ��Ȼ�Լ�Ҫ���� �˻���һ�����
	public static ArrayList<Point> melkman(Point[] P)
	{
		ArrayList<Point> ch = new ArrayList<Point>();
		int n = P.length;
		// ��X��С����,���ʱY��С�� ����
		// ��֤���Ǽ򵥶������ ���������Ѿ�����Ҫ��nlgn ��MONOTONE CHAINһ����
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
		int bot = n - 1, top = n, i;
		int temp;
		int[] deque = new int[2 * n];
		deque[top++] = 0;
		deque[top++] = 1;
		for (i = 2; i < n; i++) {
			if (Vector.direction(P[deque[top - 2]], P[deque[top - 1]], P[i]) != 0) {
				break;
			}
			deque[top - 1] = i;
		}
		deque[bot--] = i;
		deque[top++] = i;
		if (Vector.direction(P[deque[n]], P[deque[n + 1]], P[deque[n + 2]]) > 0) {
			temp = deque[n];
			deque[n] = deque[n + 1];
			deque[n + 1] = temp;
		}
		for (i++; i < n; i++) {
			if (Vector.direction(P[deque[bot + 1]], P[deque[bot + 2]], P[i]) < 0 && Vector.direction(P[deque[top - 2]], P[deque[top - 1]], P[i]) < 0) {
				continue;
			}
			while (Vector.direction(P[deque[bot + 1]], P[deque[bot + 2]], P[i]) >= 0) {
				bot++;
			}
			deque[bot--] = i;
			while (Vector.direction(P[deque[top - 2]], P[deque[top - 1]], P[i]) >= 0) {
				top--;
			}
			deque[top++] = i;

		}
		for (i = bot + 1; i < top - 1; i++) {
			ch.add(P[deque[i]]);
		}
		return ch;
	}
}
