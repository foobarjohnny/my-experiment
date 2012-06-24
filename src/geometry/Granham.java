package geometry;

/**
 * http://softsurfer.com/Archive/algorithm_0109/algorithm_0109.htm
 * http://www.cnblogs.com/Booble/archive/2011/03/10/1980089.html
 * 
 * @author SDY
 * 
 */
public class Granham
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
	// AMC与GRAHAM算法区别 在于在POINT排序 不需要极角排序
	// O(nlogn)
	public static int andrewMonotoneChain(Point[] P, int n, Point[] H)
	{
		// 按X从小到大,相等时Y从小大到 排序
		Point tmp;
		for (int i = 1; i < n - 1; i++) {
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
			return top + 1;
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
			if (Vector.direction(P[minmin], P[maxmin], P[i]) >= 0 && i < maxmin) {
				continue; // ignore P[i] above or on the lower line
			}
			while (top > 0) // there are at least 2 points on the stack
			{
				// test if P[i] is left of the line at the stack top
				if (Vector.direction(H[top - 1], H[top], P[i]) > 0) {
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
			if (Vector.direction(P[maxmax], P[minmax], P[i]) >= 0 && i > minmax) {
				continue; // ignore P[i] below or on the upper line
			}
			while (top > bot) // at least 2 points on the upper stack
			{
				// test if P[i] is left of the line at the stack top
				if (Vector.direction(H[top - 1], H[top], P[i]) > 0) {
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
		return top + 1;
	}

	// O(nlogn)
	public static void graham(Point[] pts, Point[] ch, int n, int len)
	{
		int i, j, k = 0, top = 2;
		Point tmp;

		// 找到最下且偏左的那个点
		for (i = 1; i < n; i++) {
			if ((pts[i].y < pts[k].y) || ((pts[i].y == pts[k].y) && (pts[i].x < pts[k].x))) {
				k = i;
			}
		}
		// 将这个点指定为PointSet[0]
		tmp = pts[0];
		pts[0] = pts[k];
		pts[k] = tmp;

		// 按极角从小到大,距离偏短进行排序
		for (i = 1; i < n - 1; i++) {
			for (j = i + 1; j < n; j++) {
				if ((Vector.direction(pts[0], pts[j], pts[i]) > 0) || ((Vector.direction(pts[0], pts[j], pts[i]) == 0) && (Vector.dis(pts[0], pts[j]) < Vector.dis(pts[0], pts[i])))) {
					// k保存极角最小的那个点,或者相同距离原点最近
					tmp = pts[i];
					pts[i] = pts[j];
					pts[j] = tmp;
				}
			}
		}
		// 第三个点先入栈
		ch[0] = pts[0];
		ch[1] = pts[1];
		// ch[2] = pts[2];
		// 判断与其余所有点的关系
		// 因为没有去掉极角相等 长度更小的点 所以要从第二点开始 不是算法导论上说的从第三点
		for (i = 2; i < n; i++) {
			// 不满足向左转的关系,栈顶元素出栈
			while (top >= 1 && Vector.direction(ch[top - 1], ch[top], pts[i]) >= 0) {
				top--;
			}
			// 当前点与栈内所有点满足向左关系,因此入栈.
			ch[++top] = pts[i];
		}
		len = top + 1;
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
	}
}
