package geometry;

/**
 * http://softsurfer.com/Archive/algorithm_0109/algorithm_0109.htm
 * http://www.cnblogs.com/Booble/archive/2011/03/10/1980089.html
 * 记住叉积与面积有关系，有垂直高度有关系
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
	public static void quickHull(int l, int r, Point a, Point b, Point[] pts, double[] area)
	{
		int x = l, i = l - 1, j = r + 1, k;
		for (k = l; k <= r; k++) {
			double temp = sgn(area[x] - area[k]);
			if (temp < 0 || temp == 0 && cmp(pts[x], pts[k])) {
				x = k;
			}
		}
		// 求出距离最远的那个点，用叉积 推 面积 再推 垂直高度
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
		// ans+=2*d*acos(-1.0); //等价于圆形周长
		// cout<<(int)(ans+0.5)<<endl; //四舍五入
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
