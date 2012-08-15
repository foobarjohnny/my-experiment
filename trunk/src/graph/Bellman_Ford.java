package graph;

import java.util.ArrayDeque;
import java.util.LinkedList;
/*
 * http://www.cnblogs.com/jffifa/archive/2012/05/12/2497072.html
 * [ת��][�о��ܽ�]���·���·����Լ�������⡢��С��
 */
//������������
public class Bellman_Ford
{
	private static final int	MAX	= 1000;

	public static void main(String[] args)
	{

		Edge a1 = new Edge(0, 1, 1);
		// Edge a1 = new Edge(0, 1, -1);
		Edge a2 = new Edge(0, 2, 4);
		Edge a3 = new Edge(1, 2, 3);
		// Edge a4 = new Edge(3, 1, 1);
		Edge a5 = new Edge(1, 3, 2);
		Edge a6 = new Edge(3, 2, 5);
		Edge a7 = new Edge(1, 4, 2);
		// Edge a8 = new Edge(4, 3, 3);
		// ���ɸ���ȥ�� BELLMAN_FORD��SPFA
		Edge a8 = new Edge(4, 3, -1);

		Edge[] edges = new Edge[] { a1, a2, a3, a5, a6, a7, a8 };

		int[] dist = new int[5];
		int[] pre = new int[5];
		// ��һ���ǹؼ�
		int start = 1;
		boolean b;
		// b = bellman_Ford(edges, dist, pre, start);
		// b = spfa(edges, dist, pre, start);
		// int[] h = new int[dist.length];
		// b = Dijkstra(edges, dist, pre, start, h);
		// printPath(dist, pre, start, b);
		// b = Johnson(edges, dist, pre);
		b = DAG_shortestPath(edges, dist, pre, start);
		printPath(dist, pre, start, b);
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
	 * ������ʱ�临�Ӷ�ΪO(VE),��Ϊÿһ����Ҫ�Ա߽����ɳڣ��������ǲ��ñ���ߵķ�ʽ���洢ͼ�ĵ���Ϣ��
	 * p�������ǰ���ڵ㣬d�������Դ�㵽ÿһ�������̾��롣�������������һ���жϣ�������п����ɳ�
	 * �ıߣ���ô���Ա�֤����ͼ���и�Ȩ�Ļ����ڣ������Ļ���û�����·��ֻ˵�ˣ���������С��
	 * http://hi.baidu.com/rangemq/blog/item/0e96cad3977cdcdea9ec9ac6.html
	 * http://hi.baidu.com/jffifa/item/ef628c50d37345dcd58bac44
	 */
	// BELLMAN_FORD���·��һ��
	// 1.���е�Wȡ��,�����
	// 2.MAX��MIN,>����<
	// BELLMAN_FORD��S��ĳ�����и�(��)Ȩ��·,��S��ĳ���û�����(��)·��,ע���и�(��)��Ȩ·������S��ȫ���ĵ㶼û�����(��)·��
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

	// �����޻�ͼDAG(Directed Acyclic Graph)�����·��, DAGͼ�����,��Ȩ�߿�����,DI��������
	// ��TOPO����,�ٰ�TOPO����������ɳ�
	// �����޻�·(dagͼ)���������,���������б���һ�μ������Դ���·��(Ҳ�����޸ĺ�����ؼ�·��(���))
	// ��ؼ�·��,���·����BELLMAN_FORD���һ��
	// 1.���е�Wȡ��,�����
	// 2.MAX��MIN,>����<
	private static boolean DAG_shortestPath(Edge[] edges, int[] dist, int[] pre, int start)
	{
		boolean isDAG = true;
		initDist(dist, pre, start);
		LinkedList<Integer> topoList = new LinkedList<Integer>();
		isDAG = topSort(dist.length, edges, topoList);// ��ͼ�����������򣬽���洢��arrayList[]��
		System.out.println(topoList);
		if (isDAG == false) {
			return false;
		}
		// ����һ���������У���ɵ�Դ���·������
		for (int i = 0; i < dist.length; i++) {
			int t = topoList.get(i);
			for (int j = 0; j < edges.length; j++) {
				if (edges[j].getU() == t) {
					if (dist[edges[j].getU()] != MAX && dist[edges[j].getV()] > dist[edges[j].getU()] + edges[j].getW()) {
						dist[edges[j].getV()] = dist[edges[j].getU()] + edges[j].getW();
						pre[edges[j].getV()] = edges[j].getU();
					}
				}
			}
		}
		return isDAG;
	}

	private static boolean topSort(int vetexCnt, Edge[] edges, LinkedList<Integer> topoList)
	{
		boolean isDAG = true;
		int[] visited = new int[vetexCnt];
		for (int i = 0; i < vetexCnt; i++) {
			if (visited[i] == 0) {
				isDAG = topOlogicalSort(i, edges, visited, topoList);// ��ÿ���������������������
				if (isDAG == false) {
					return false;
				}
			}
		}
		return true;
	}

	// ������������㷨
	private static boolean topOlogicalSort(int u, Edge[] edges, int[] visited, LinkedList<Integer> topoList)
	{
		visited[u] = 1;// ���ڷ����ܱ߶���
		boolean isDAG = true;
		for (int j = 0; j < edges.length; j++) {// ������ȱ���
			if (edges[j].getU() == u) {
				int v = edges[j].getV();
				if (visited[v] == 1) {
					// �����GREY ���������޻�ͼ ûTOPO
					return false;
				}
				if (visited[v] == 0) {
					isDAG = topOlogicalSort(v, edges, visited, topoList);
					if (isDAG == false) {
						return false;
					}
				}
			}
		}
		visited[u] = 2;// �ö�����Χ���ѱ�������
		// ���뵽����������
		topoList.push(u);
		return isDAG;
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
	 * SPFA(Shortest Path Faster Algorithm)��Bellman-Ford�㷨��һ�ֶ���ʵ�֣������˲���Ҫ��������㡣
	 * ��������O(kE)��ʱ�临�Ӷ������Դ�㵽�������е�����·�������Դ����ߡ� �㷨����
	 * SPFA��Bellman-Ford�㷨�Ż��Ĺؼ�֮��������ʶ��
	 * ֻ����Щ��ǰһ���ɳ��иı��˾������ֵ�ĵ㣬�ſ����������ǵ��ڽӵ�ľ������ֵ�ĸı䡣
	 * �жϸ�Ȩ��·�ķ����ܶ࣬����������㡢�Ƚ�����ʵ�ֲ��Ҹ�Ч�ķ������Ǽ�¼ÿ�������Ӵ���������|V|�α�ʾ�и�Ȩ��
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

	// Լ��ɭ
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
		// ��bellman_Ford���ɵ�DIST����Ȩ �������Ƕ��������ɵ�W>=0
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
	 * ʱ�临�Ӷ���O(V^2+E) ������С�� ����O((V+E)lgV) ����쳲�����������O(VlgV+E)
	 */
	// �Ͻ�˹����
	// �������·��
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