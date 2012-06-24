package geometry;

/**
 * http://softsurfer.com/Archive/algorithm_0109/algorithm_0109.htm
 * http://www.cnblogs.com/Booble/archive/2011/03/10/1980089.html
 * ��ס���������й�ϵ���д�ֱ�߶��й�ϵ
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
	public static void quickHull(int l, int r, Point a, Point b, Point[] pts, double[] area)
	{
		int x = l, i = l - 1, j = r + 1, k;
		for (k = l; k <= r; k++) {
			double temp = sgn(area[x] - area[k]);
			if (temp < 0 || temp == 0 && cmp(pts[x], pts[k])) {
				x = k;
			}
		}
		// ���������Զ���Ǹ��㣬�ò�� �� ��� ���� ��ֱ�߶�
		Point y = pts[x];
		for (k = l; k <= r; k++) {
			area[++i] = Vector.direction(pts[k], a, y);
			if (sgn(area[i]) > 0) {
				swap(pts, j, k);
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
			quickHull(l, i, a, y, pts, area);
		}
		System.out.println(y.x + ":" + y.y);
		if (j <= r) {
			quickHull(j, r, y, b, pts, area);
		}
	}

	private static void swap(Point[] a, int index1, int index2)
	{
		Point tmp;
		tmp = a[index1];
		a[index1] = a[index2];
		a[index2] = tmp;
	}

	private static boolean cmp(Point a, Point b)
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
		Point[] p = new Point[11];
		double[] s = new double[p.length];
		int n = p.length - 1;
		int i = 0;
		int x = 0;
		for (i = 1; i <= n; i++) {
			if (x == 0 || cmp(p[i], p[x]))
				x = i;
		}
		swap(p, 1, x);
		System.out.println(p[1].x + ":" + p[1].y);
		quickHull(2, n, p[1], p[1], p, s);
	}
}
