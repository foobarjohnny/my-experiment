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
public class BruteForce
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

	// O(n^3)

	// Naive Bruteforce
	// The bruteforce approach is use the definition. A point is on the convex
	// hull if and only if it is not inside a triangle of any other three
	// points. Since there are O(n^3) triangles for each of the O(n) points, this
	// algorithm is O(n^4).
	// [edit] Better Bruteforce
	// Slightly better bruteforce is to realize that an edge is on the convex
	// hull if and only if it is an extreme edge - all the other points lie on
	// one side of it. Since there are O(n^2) edges, and each check requires
	// going through the O(n) points, this algorithm is O(n^3).
	public static ArrayList<Point> bruteForce(Point[] pts)
	{
		ArrayList<Point> ch = new ArrayList<Point>();
		int n = pts.length;
		int sign = 0;
		int positive = 0;
		int negatvie = 0;
		int i, j, k;
		for (i = 0; i < n; ++i) {
			for (j = i + 1; j < n; ++j) {
				sign = 0;
				positive = 0;
				negatvie = 0;
				for (k = 0; k < n; ++k) {
					if ((k == j) || (k == i)) {
						continue;
					}
					/*
					 * 
					 * * 如果有两个以上的点共线，不处理
					 */
					if (Vector.direction(pts[i], pts[j], pts[k]) > 0) {
						++sign;
						++positive;
					}
					if (Vector.direction(pts[i], pts[j], pts[k]) < 0) {
						--sign;
						--negatvie;
					}
					if (positive * negatvie != 0) {
						break;
					}
				}/* end inside for */

				// 没仔细考虑共线的情况 此算法 不完善 以后有空再搞
				if ((sign == (n - 2)) || ((sign == (2 - n)))) {
					if (!ch.contains(pts[i])) {
						ch.add(pts[i]);
					}
					if (!ch.contains(pts[j])) {
						ch.add(pts[j]);
					}
				}
			}
		}
		return ch;
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
