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

	// void 快速凸包(P:点集 , S:向量 /*S.p,S.q:点)*/ ){
	// 2 　　/* P 在 S 左侧*/
	// 3 　　选取 P 中距离 S 最远的 点 Y ;
	// 4 　　向量 A <- { S.p , Y } ; 向量 B <- { Y , S.q } ;
	// 5 　　点集 Q <- 在 P 中 且在 A 左侧的点 ;
	// 6 　　点集 R <- 在 P 中 且在 B 左侧的点 ; /* 划分 */
	// 7 　　快速凸包 ( Q , A ) ; /* 分治 */
	// 8 　　输出 (点 Y) ; /* 按中序输出 保证顺序*/
	// 9 　　快速凸包 ( R , B ) ; /* 分治 */
	// 10 }
	//
	// 初始化就选取 最左下和最右上的点 划分好 然后调用两次快速凸包函数分别求上下凸包
	// 其中 选取和划分都需要用到向量的叉乘 注意方向
	// 另外 存储点集可以用数组里的连续一段 传参的时候就传左右下标
	// 划分的时候就是给数组交换元素 使新的点集 变成连续的一段
	// 这里有一个很好的演示
	// http://www.cs.princeton.edu/courses/archive/spr10/cos226/demo/ah/QuickHull.html
	// 还要补充说明一下
	// 快速凸包在所有点都在圆周上的时候还是O(Nlog2N) 不过会比Graham扫描算法慢一些
	// 可以说 这种数据是Graham扫描法的最好情况 一遍走完就行了
	// 构造快速凸包的最坏情况就是使划分不均等 和构造快速排序最坏情况一样
	// 跟QUICKSORT有一样的性质
	public static void quickHull(int l, int r, Point a, Point b, Point[] pts, double[] area, ArrayList<Point> ch)
	{
		int x = l, i = l - 1, j = r + 1, k;
		// 求AREA 或者求距离点远的 这里面要去掉重点的点
		for (k = l; k <= r; k++) {
			double temp = sgn(area[x] - area[k]);
			if (temp < 0 || temp == 0 && cmp(pts[x], pts[k])) {
				x = k;
			}
		}
		// 求出距离最远的那个点，用叉积 推 面积 再推 垂直高度
		Point y = pts[x];
		if (a == null || b == null) {
			a = y;
			b = y;
			ch.add(y);
			// 想想为什么 要把这个交换出去
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
		// ans+=2*d*acos(-1.0); //等价于圆形周长
		// cout<<(int)(ans+0.5)<<endl; //四舍五入
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
