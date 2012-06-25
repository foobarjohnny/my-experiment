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
public class GranhamScan
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

	// O(nlogn) 比较极角大小 初始化
	public static ArrayList<Point> grahamScan(Point[] pts)
	{
		int i, j, k, top;
		int n = pts.length;
		Point[] ch = new Point[n];
		int len = 0;

		Point tmp;

		// 找到最下且偏左的那个点
		k = 0;
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
				if ((Vector.direction(pts[0], pts[j], pts[i]) < 0) || ((Vector.direction(pts[0], pts[j], pts[i]) == 0) && (Vector.dis(pts[0], pts[j]) < Vector.dis(pts[0], pts[i])))) {
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
		top = 1;
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
		return new ArrayList<Point>(Arrays.asList(Arrays.copyOf(ch, len)));
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
