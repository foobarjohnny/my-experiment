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
 * http://blog.csdn.net/zhang20072844/article/category/893217
 * http://www.csie.ntnu.edu.tw/~u91029/ConvexHull.html
 * 
 * @author SDY
 */
public class QuickHull
{

	public static double	ZERO	= 1e-12;

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

	// void ����͹��(P:�㼯 , S:���� /*S.p,S.q:��)*/ ){
	// 2 ����/* P �� S ���*/
	// 3 ����ѡȡ P �о��� S ��Զ�� �� Y ;
	// 4 �������� A <- { S.p , Y } ; ���� B <- { Y , S.q } ;
	// 5 �����㼯 Q <- �� P �� ���� A ���ĵ� ;
	// 6 �����㼯 R <- �� P �� ���� B ���ĵ� ; /* ���� */
	// 7 ��������͹�� ( Q , A ) ; /* ���� */
	// 8 ������� (�� Y) ; /* ��������� ��֤˳��*/
	// 9 ��������͹�� ( R , B ) ; /* ���� */
	// 10 }
	//
	// ��ʼ����ѡȡ �����º������ϵĵ� ���ֺ� Ȼ��������ο���͹�������ֱ�������͹��
	// ���� ѡȡ�ͻ��ֶ���Ҫ�õ������Ĳ�� ע�ⷽ��
	// ���� �洢�㼯�����������������һ�� ���ε�ʱ��ʹ������±�
	// ���ֵ�ʱ����Ǹ����齻��Ԫ�� ʹ�µĵ㼯 ���������һ��
	// ������һ���ܺõ���ʾ
	// http://www.cs.princeton.edu/courses/archive/spr10/cos226/demo/ah/QuickHull.html
	// ��Ҫ����˵��һ��
	// ����͹�������е㶼��Բ���ϵ�ʱ����O(Nlog2N) �������Grahamɨ���㷨��һЩ
	// ����˵ ����������Grahamɨ�跨�������� һ�����������
	// �������͹������������ʹ���ֲ����� �͹��������������һ��
	// ��QUICKSORT��һ��������
	public static void quickHull(int l, int r, Point a, Point b, Point[] pts, double[] area, ArrayList<Point> ch)
	{
		int x = l, i = l - 1, j = r + 1, k;
		// ��AREA ����������Զ�� ������Ҫȥ���ص�ĵ�
		for (k = l; k <= r; k++) {
			double temp = sgn(area[x] - area[k]);
			if (temp < 0 || temp == 0 && cmp(pts[x], pts[k])) {
				x = k;
			}
		}
		// ���������Զ���Ǹ��㣬�ò�� �� ��� ���� ��ֱ�߶�
		Point y = pts[x];
		if (a == null || b == null) {
			a = y;
			b = y;
			ch.add(y);
			// ����Ϊʲô Ҫ�����������ȥ
			swap(pts, l, x);
			quickHull(l + 1, r, a, b, pts, area, ch);
		}
		else {
			for (k = l; k <= r; k++) {
				area[++i] = Vector.direction(pts[k], a, y);
				if (sgn(area[i]) > 0) {
					swap(pts, i, k);
				}
				else {
					i--;
				}
			}
			for (k = r; k >= l; k--) {
				area[--j] = Vector.direction(pts[k], y, b);
				if (sgn(area[j]) > 0) {
					swap(pts, j, k);
				}
				else {
					j++;
				}
			}
			if (l <= i) {
				quickHull(l, i, a, y, pts, area, ch);
			}
			ch.add(y);
			if (j <= r) {
				quickHull(j, r, y, b, pts, area, ch);
			}
		}
	}

	private static void swap(Point[] a, int index1, int index2)
	{
		Point tmp;
		tmp = a[index1];
		a[index1] = a[index2];
		a[index2] = tmp;
	}

	public static boolean cmp(Point a, Point b)
	{
		return (a.x < b.x || sgn(a.x - b.x) == 0 && a.y < b.y);
	}

	private static double sgn(double x)
	{
		return (Math.abs(x) < ZERO ? 0 : (x > 0 ? 1 : -1));
	}

	public static void main(String[] args)
	{
		// double ans=0;
		// int d;
		// cin>>n>>d;
		// for(int i=0;i<n;i++)
		// cin>>PointSet[i].x>>PointSet[i].y;//input the data;
		// graham(PointSet,ch,n,len);
		// for(int i=0;i<len;i++)
		// ans+=dis(ch[i],ch[(i+1)%len]);
		// ans+=2*d*acos(-1.0); //�ȼ���Բ���ܳ�
		// cout<<(int)(ans+0.5)<<endl; //��������
		// return 0;
		int pointCnt = 10;
		int max = 68;
		ArrayList<Point> p2 = getPoints(pointCnt, max);
		System.out.println(p2);
		Point[] p = new Point[p2.size()];
		p2.toArray(p);
		ArrayList<Point> ch = new ArrayList<Point>();
		quickHull(0, p.length - 1, null, null, p, new double[p.length], ch);
		System.out.println(ch);
		System.out.println(diameter(ch.toArray(new Point[ch.size()])));

		ch = (ArrayList<Point>) GranhamScan.grahamScan(p2.toArray(new Point[p2.size()]));
		System.out.println(ch);
		System.out.println(diameter(ch.toArray(new Point[ch.size()])));

		ch = (ArrayList<Point>) AndrewMonotoneChain.andrewMonotoneChain(p2.toArray(new Point[p2.size()]));
		System.out.println(ch);
		System.out.println(diameter(ch.toArray(new Point[ch.size()])));

		ch = (ArrayList<Point>) JarvisMarch.jarvisMarch(p2.toArray(new Point[p2.size()]));
		System.out.println(ch);
		System.out.println(diameter(ch.toArray(new Point[ch.size()])));

		ch = (ArrayList<Point>) BruteForce.bruteForce(p2.toArray(new Point[p2.size()]));
		System.out.println(ch);

		ch = (ArrayList<Point>) AndrewMonotoneChain.andrewMonotoneChain2(p2.toArray(new Point[p2.size()]));
		System.out.println(ch);
		System.out.println(diameter(ch.toArray(new Point[ch.size()])));
		
		ch = (ArrayList<Point>) Melkman.melkman(p2.toArray(new Point[p2.size()]));
		System.out.println(ch);
		System.out.println(diameter(ch.toArray(new Point[ch.size()])));

		// RotatingCaliper.rotatingCaliper(p2.toArray(new Point[p2.size()]));
		// System.out.println(ch);
	}

	private static ArrayList<Point> getPoints(int pointCnt, int max)
	{
		ArrayList<Point> pts = new ArrayList<Point>();
		Random r = new Random();
		for (int i = 1; i <= pointCnt; i++) {
			pts.add(new Point(r.nextInt(max) + 1, r.nextInt(max) + 1));
		}
		return pts;
	}

	private static double diameter(Point[] ch)
	{

		int n = ch.length;
		int q = 1;
		double ans = 0;
		for (int p = 0; p <= n - 1; p++) {
			while (Vector.direction(ch[p], ch[(q + 1) % n], ch[(p + 1) % n]) > Vector.direction(ch[p], ch[q], ch[(p + 1) % n])) {
				q = (q + 1) % n;
			}
			ans = Math.max(ans, Math.max(Vector.dis(ch[p], ch[q]), Vector.dis(ch[(p + 1) % n], ch[(q + 1) % n])));
		}
		return ans;
	}
}
