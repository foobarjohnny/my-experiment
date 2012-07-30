package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 最小生成树的Kruskal算法(以无向图为例) */
/** 最小生成树是最小权值生成树，并不是边最小化，因为生成树的边都固定为V-1 */
// http://www.byvoid.com/blog/tag/%E6%AC%A1%E5%B0%8F%E7%94%9F%E6%88%90%E6%A0%91/
// 次短路径 次小生成树 枚举减掉
public class MinimumSpanningTree
{
	public static Map<Ve, Integer>	vertexMap	= new HashMap<Ve, Integer>();
	public static List<E>			EdgeQueue	= new ArrayList<E>();
	public static List<E>			EdgeList	= new ArrayList<E>();
	public static int				MAX			= 100;
	public static int[]				p			= new int[MAX + 1];
	public static int[]				rank		= new int[MAX + 1];
	public static int				edgeCnt;
	public static int				vertexCnt	= edgeCnt = 0;					// 点数，边数

	// O(ElgV) 详细看书吧
	// 克鲁斯卡尔
	public static boolean Kruskal()
	{
		// 把边按权值非递减的排序
		Collections.sort(EdgeQueue, new Comparator<E>()
		{
			@Override
			public int compare(E e1, E e2)
			{
				return e1.weight - e2.weight;
			}
		});
		// 贪心的构造生成树
		int i = 0;
		makeSet();
		for (E edge : EdgeQueue) {
			// i用来标记顶点是否属于同一个树
			// 关键是排除那些会构成回路的边，并且合并两个不相连的树
			if (i >= vertexCnt - 1) {
				break;
			}
			if (findSet(edge.a.key) != findSet(edge.b.key)) {
				EdgeList.add(edge);
				Union(edge.a.key, edge.b.key);
				i++;
			}
		}
		if (i != vertexCnt - 1) {
			System.out.println("no spanning tree and no minimum spanning tree");
			return false;
		}
		else {
			System.out.println("there is minimum spanning tree");
			return true;
		}
	}

	public static void addEdge(Ve a, Ve b, int w)
	{
		E e = new E(a, b, w);
		EdgeQueue.add(e);
	}

	private static void buildGraph()
	{
		Ve v1 = new Ve(1);
		Ve v2 = new Ve(2);
		Ve v3 = new Ve(3);
		Ve v4 = new Ve(4);
		Ve v5 = new Ve(5);
		Ve v6 = new Ve(6);
		addEdge(v1, v2, 5);
		addEdge(v1, v3, 8);
		addEdge(v1, v4, 1);
		addEdge(v2, v3, 4);
		addEdge(v2, v5, 2);
		addEdge(v3, v4, 7);
		addEdge(v4, v5, 6);
		addEdge(v5, v6, 5);
	}

	// dist是距离 已经生成的 最小生成树 子图(?子树)的 距离 不是Dijkstra里距离源点的距离
	// 用优先队列 二叉最小堆最大堆O（VlgV+ElgV)=O(ElgV) 如果采用斐波那契堆 能改进为O(VlgV+E)
	//
	// EXTRACT-MIN : 二叉 lgV 斐 lgV
	// DECREASE-KEY: lgV 1
	// 普利姆
	private static boolean prim()
	{
		int start = 1;
		int[] dist = new int[vertexCnt + 1];
		int[] pre = new int[vertexCnt + 1];
		boolean[] mask = new boolean[vertexCnt + 1];
		// 这一步是关键
		initPrim(dist, pre, start);
		int count = vertexCnt + 1;
		while (count-- >= 0) {
			int t = extraMin(dist, mask);
			mask[t] = true;
			for (E edge : EdgeQueue) {
				if (edge.a.key == pre[t] && edge.b.key == t) {
					EdgeList.add(edge);
				}
				if (edge.a.key == t) {
					if (edge.weight <= dist[edge.b.key]) {
						dist[edge.b.key] = edge.weight;
						pre[edge.b.key] = edge.a.key;
					}
				}
			}
		}
		return true;
	}

	private static void initPrim(int[] dist, int[] pre, int n)
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

	public static void main(String[] args)
	{
		buildGraph();
		Kruskal();
		System.out.println("Kruskal");
		for (E edge : EdgeList) {
			System.out.println("[" + edge.a.key + "," + edge.b.key + "]" + edge.weight);
		}
		System.out.println();
		EdgeList.clear();
		prim();
		System.out.println("prim");
		for (E edge : EdgeList) {
			System.out.println("[" + edge.a.key + "," + edge.b.key + "]" + edge.weight);
		}
		System.out.println();
	}

	public static void makeSet()
	{
		for (int i = 1; i <= MAX; i++) {
			p[i] = i;
			rank[i] = 1;
		}
	}

	public static int findSet(int x)
	{
		if (x != p[x]) {
			p[x] = findSet(p[x]);
		}
		return p[x];
	}

	public static void Union(int x, int y)
	{
		int i = findSet(x);
		int j = findSet(y);
		if (rank[i] > rank[j]) {
			p[j] = i;
			rank[i] = rank[i] + rank[j];
		}
		else {
			p[i] = j;
			rank[j] = rank[i] + rank[j];
		}
	}
}

class Ve
{
	int	key;

	public Ve(int k)
	{
		key = k;
		MinimumSpanningTree.vertexCnt++;
	}

	@Override
	public String toString()
	{
		return "Ve [key=" + key + "]";
	}

}

class E
{
	int	weight;
	Ve	a;
	Ve	b;

	public E(Ve a2, Ve b2, int w)
	{
		a = a2;
		b = b2;
		weight = w;
		MinimumSpanningTree.edgeCnt++;
	}

	@Override
	public String toString()
	{
		return "E [weight=" + weight + ", a=" + a + ", b=" + b + "]";
	}
}
