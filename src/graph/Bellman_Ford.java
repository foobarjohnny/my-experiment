package graph;

import java.util.ArrayDeque;

//贝尔曼—福德
public class Bellman_Ford
{
	private static final int	MAX	= 1000;

	public static void main(String[] args)
	{

		// Edge a1 = new Edge(0, 1, 1);
		Edge a1 = new Edge(0, 1, -1);
		Edge a2 = new Edge(0, 2, 4);
		Edge a3 = new Edge(1, 2, 3);
		Edge a4 = new Edge(3, 1, 1);
		Edge a5 = new Edge(1, 3, 2);
		Edge a6 = new Edge(3, 2, 5);
		Edge a7 = new Edge(1, 4, 2);
		// Edge a8 = new Edge(4, 3, 3);
		// 换成负数去测 BELLMAN_FORD和SPFA
		Edge a8 = new Edge(4, 3, -1);

		Edge[] edges = new Edge[] { a1, a2, a3, a4, a5, a6, a7, a8 };

		int[] dist = new int[5];
		int[] pre = new int[5];
		// 这一步是关键
		int start = 4;
		boolean b;
		// b = bellman_Ford(edges, dist, pre, start);
		// b = spfa(edges, dist, pre, start);
		// int[] h = new int[dist.length];
		// b = Dijkstra(edges, dist, pre, start, h);
		// printPath(dist, pre, start, b);
		b = Johnson(edges, dist, pre);
	}

	private static void printPath(int[] dist, int[] pre, int start, boolean b)
	{
		if (!b) {
			System.out.println("negative loop");
			for (int i = 0; i < dist.length; i++) {
				System.out.println(start + "--" + i + "==" + dist[i] + " : " + pre[i]);
			}
		}
		else {
			for (int i = 0; i < dist.length; i++) {
				System.out.print(start + "--" + i + "==" + dist[i] + " : ");
				outpath(i, pre, start);
				System.out.println();
			}
		}
	}

	private static void outpath(int i, int[] pre, int start)
	{
		if (i == start) {
			System.out.print(start + "->");
		}
		else {
			if (pre[i] == -1) {
				System.out.print("no path ->");
			}
			else {
				outpath(pre[i], pre, start);
				System.out.print(i + "->");
			}
		}
	}

	/**
	 * 很明显时间复杂度为O(VE),因为每一次需要对边进行松弛，所以我们采用保存边的方式来存储图的的信息。
	 * p保存的是前驱节点，d保存的是源点到每一个点的最短距离。我们最后又做了一次判断，如果还有可以松弛
	 * 的边，那么可以保证的是图中有负权的环存在，这样的话就没有最短路径只说了，可以无限小。
	 * 
	 * 
	 */
	private static boolean bellman_Ford(Edge[] edges, int[] dist, int[] pre, int n)
	{
		initDist(dist, pre, n);
		for (int i = 1; i < dist.length; i++) {
			for (int j = 0; j < edges.length; j++) {
				// System.out.println("U" + edges[j].getU());
				// System.out.println("V" + edges[j].getV());
				// System.out.println("dist" + dist.length);
				if (dist[edges[j].getU()] != MAX && dist[edges[j].getV()] > dist[edges[j].getU()] + edges[j].getW()) {
					dist[edges[j].getV()] = dist[edges[j].getU()] + edges[j].getW();
					pre[edges[j].getV()] = edges[j].getU();
				}
			}
		}
		for (int j = 0; j < edges.length; j++) {
			if (dist[edges[j].getU()] != MAX && dist[edges[j].getV()] > dist[edges[j].getU()] + edges[j].getW()) {
				return false;
			}
		}
		return true;

	}

	private static void initDist(int[] dist, int[] pre, int n)
	{
		for (int i = 0; i < dist.length; i++) {
			if (i == n) {
				dist[n] = 0;
				pre[n] = 0;
			}
			else {
				dist[i] = MAX;
				pre[i] = -1;
			}
		}
	}

	/**
	 * SPFA(Shortest Path Faster Algorithm)是Bellman-Ford算法的一种队列实现，减少了不必要的冗余计算。
	 * 它可以在O(kE)的时间复杂度内求出源点到其他所有点的最短路径，可以处理负边。 算法流程
	 * SPFA对Bellman-Ford算法优化的关键之处在于意识到
	 * 只有那些在前一遍松弛中改变了距离估计值的点，才可能引起他们的邻接点的距离估计值的改变。
	 * 判断负权回路的方案很多，世间流传最广、比较容易实现并且高效的方法的是记录每个结点进队次数，超过|V|次表示有负权。
	 */
	// ELEMENT--PEEK, ADD--OFFER, REMOVE--POLL--POP
	private static boolean spfa(Edge[] edges, int[] dist, int[] pre, int n)
	{
		initDist(dist, pre, n);
		ArrayDeque<Integer> Q = new ArrayDeque<Integer>(dist.length);
		int[] count = new int[dist.length];
		boolean[] mask = new boolean[dist.length];
		Q.offer(n);
		count[n] = 1;
		mask[n] = true;
		while (!Q.isEmpty()) {
			int t = Q.poll();
			mask[t] = false;
			if (count[t] > dist.length) {
				return false;
			}
			for (int j = 0; j < edges.length; j++) {
				if (edges[j].getU() == t) {
					if (dist[edges[j].getU()] != MAX && dist[edges[j].getV()] > dist[edges[j].getU()] + edges[j].getW()) {
						dist[edges[j].getV()] = dist[edges[j].getU()] + edges[j].getW();
						pre[edges[j].getV()] = edges[j].getU();
						if (!mask[edges[j].getV()]) {
							Q.offer(edges[j].getV());
							count[edges[j].getV()]++;
							mask[edges[j].getV()] = true;
						}
					}
				}
			}
		}
		return true;
	}

	// 约翰森
	private static boolean Johnson(Edge[] edges, int[] dist, int[] pre)
	{
		int[] dist2 = new int[dist.length + 1];
		int[] pre2 = new int[pre.length + 1];
		Edge[] edges2 = new Edge[edges.length + dist.length];
		for (int i = 0; i < edges2.length; i++) {
			if (i < edges.length) {
				edges2[i] = edges[i];
			}
			else {
				edges2[i] = new Edge(dist.length, i - edges.length, 0);
			}
		}

		int start = dist2.length - 1;

		boolean b = bellman_Ford(edges2, dist2, pre2, start);
		// 用bellman_Ford生成的DIST来赋权 是用三角定理保定生成的W>=0
		printPath(dist2, pre2, start, b);
		if (b) {
			start = dist.length - 1;
			while (start >= 0) {
				boolean bool = Dijkstra(edges, dist, pre, start, dist2);
				for (int j = 0; j < dist.length; j++) {
					if (dist[j] != MAX) {
						dist[j] = dist[j] + dist2[j] - dist2[start];
					}
				}
				printPath(dist, pre, start, bool);
				start--;
			}
		}
		else {
			return false;
		}
		return true;
	}

	/**
	 * 时间复杂度是O(V^2+E) 换成最小堆 则变成O((V+E)lgV) 换成斐波那契堆则变成O(VlgV+E)
	 */
	// 迪杰斯特拉
	private static boolean Dijkstra(Edge[] edges, int[] dist, int[] pre, int n, int[] h)
	{
		initDist(dist, pre, n);
		boolean[] mask = new boolean[dist.length];
		int count = dist.length;
		while (count-- >= 0) {
			int t = extraMin(dist, mask);
			mask[t] = true;
			for (int j = 0; j < edges.length; j++) {
				if (edges[j].getU() == t) {
					if (dist[edges[j].getU()] != MAX && dist[edges[j].getV()] > dist[edges[j].getU()] + edges[j].getW() + h[edges[j].getU()] - h[edges[j].getV()]) {
						dist[edges[j].getV()] = dist[edges[j].getU()] + edges[j].getW() + h[edges[j].getU()] - h[edges[j].getV()];
						pre[edges[j].getV()] = edges[j].getU();
					}
				}
			}
		}
		return true;
	}

	private static int extraMin(int[] dist, boolean[] mask)
	{
		int min = MAX;
		int index = 0;
		for (int i = 0; i < dist.length; i++) {
			if (mask[i] == false) {
				if (min > dist[i]) {
					min = dist[i];
					index = i;
				}
			}
		}
		return index;
	}

}

class Edge
{
	private int	u;
	private int	v;
	private int	w;

	public Edge(int u, int v, int w)
	{
		this.u = u;
		this.v = v;
		this.w = w;
	}

	public int getU()
	{
		return u;
	}

	public void setU(int u)
	{
		this.u = u;
	}

	public int getV()
	{
		return v;
	}

	public void setV(int v)
	{
		this.v = v;
	}

	public int getW()
	{
		return w;
	}

	public void setW(int w)
	{
		this.w = w;
	}

}