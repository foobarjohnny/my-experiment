package graph;

import java.util.ArrayDeque;
import java.util.LinkedList;

/*http://kody-lau.blog.hexun.com/p5/default.aspx
 *
 1、 连续最短路算法（Successive Shortest Path）--升级-->3；
 2、 消圈算法（Cycle Canceling）--升级-->4；
 3、 原始对偶算法（Primal Dual）；
 4、 网络单纯形（Network Simplex）
 mincost-maxflow
 mincost-maxflow是一种适用范围很广的模型。如果只考虑流量不考虑费用，则是maxflow模型；
 如果只考虑费用不考虑流量，就是 shortest path模型；二者综合，就是mincost-maxflow模型。
 所以网络流问题的核心，实际上主要是研究mincost-maxflow。
 (1)　基本思想：消负环、连续最短路
 与maxflow类似，mincost-maxflow也有两种基本思想。
 一种就是保证最大流前提下不断消去费用负环，也就是消负环算法；
 还有一种就是保证没有负环的情况下逐步使得流量最大，也就是最小费用路算法，又称连续最短路算法。
 消负环相当于LP原始问题，而连续最短路则是对偶问题。
 (2)　改进算法：
 朴素消负环算法的一个重要改进，就是网络单纯型算法。
 网络单纯型算法和线性规划单纯型算法本质上完全相同，所以它们一样，有着惊人的指数级复杂度和同样惊人的极高运行效率。
 网络单纯型算法通过构建一棵可行树，然后寻找一组进树边和出树边来快速的寻找负环。
 这个算法同样的有着退化和初始可行树选定的问题，可以依据线性规划单纯型算法的解决方法类比的予以解决。
 而连续最短路算法的拓展就是原始对偶算法。
 通过分析可以发现，实际上寻找最小费用路的过程可以完全简化成为寻找单源最短路径的过程，而边上的权值则为该边的费用。
 二者都是同过一个顶点势函数来确定可行边，寻找最短路径，并按其增广的算法。
 原始对偶算法则是在寻找最短路之后，进行多路同时增广来减少迭代次数。
 据称连续最短路＋原始对偶算法有O(V^3)实现，但是很遗憾，我不知道应该如何实现，希望如果有人知道的话可以教教我。
 */
public class MinCostMaxFlow
{
	public static int[][]	flow;

	public static int[][]	capacity;
	public static int[][]	cost;
	public static int[]		costDist;
	public static int[]		path;
	public static int[]		H;
	public static int		vertexCnt	= 0;		// 点数，边数
	public static int		MAX			= 10000;

	public static void buildNet()
	{

		capacity = new int[vertexCnt + 1][vertexCnt + 1];
		cost = new int[vertexCnt + 1][vertexCnt + 1];
		flow = new int[vertexCnt + 1][vertexCnt + 1];
		costDist = new int[vertexCnt + 1];
		path = new int[vertexCnt + 1];

		initCapAndCos(1, 2, 1, 1);
		initCapAndCos(1, 3, 1, 2);
		initCapAndCos(2, 4, 3, 3);
		initCapAndCos(3, 4, 3, 1);
		initCapAndCos(4, 5, 1, 1);
		int maxint = MAX;
	}

	private static void initCapAndCos(int u, int v, int cap, int cos)
	{
		capacity[u][v] = cap;
		cost[u][v] = cos;
		cost[v][u] = -cos;
	}

	private static void initEcost(int[] ecost, int[] pre, int S)
	{
		for (int i = 0; i < ecost.length; i++) {
			if (i == S) {
				ecost[S] = 0;
				pre[S] = 0;
			}
			else {
				ecost[i] = MAX;
				pre[i] = -1;
			}
		}
	}

	public static boolean bellmanFord4SSP(int start, int end)
	{
		int i, j;
		initEcost(costDist, path, start);
		boolean flag = true;
		while (flag) {
			flag = false;
			for (i = 1; i <= vertexCnt; i++) {
				if (costDist[i] == MAX) {
					continue;
				}
				for (j = 1; j <= vertexCnt; j++) {
					if (capacity[i][j] > 0 && costDist[i] + cost[i][j] < costDist[j]) {
						flag = true;
						costDist[j] = costDist[i] + cost[i][j];
						path[j] = i;
					}
				}
			}
		}
		// 这里只考虑了负权边，因为COST正反向都为相同值，不可能出现负回权路
		// 所以判断有没有START-END的最短COST路径 用这个条件就行了
		// 如果COST的双向值不相等，那就要考虑负回权路
		// 用BELLMAN_FORD或者SPFA 返回值就有0,1,2三种 判断有没有START-END有没有最短路径
		// 除了这个不等式，还需要用并查集找出有负回权路边的点能连接到的点 是否包含END点 这个就比较复杂了
		return costDist[end] != MAX;
	}

	// 连续最短路算法（Successive Shortest Path）最小费用路算法，又称连续最短路算法
	// http://www.cnblogs.com/zhuangli/archive/2008/08/01/1258434.html
	// 最小费用最大流 是在capacity图的保障下，求cost图S-T的最短路径 详细见PPT
	public static int successiveShortestPath(int start, int end)
	{
		int minCost = 0, maxFlow = 0;
		while (bellmanFord4SSP(start, end)) {
			int cur = end;
			int aug = MAX;
			while (cur != start) {
				int pre = path[cur];
				aug = Math.min(aug, capacity[pre][cur]);
				cur = pre;
			}
			maxFlow += aug;
			cur = end;
			while (cur != start) {
				int pre = path[cur];
				capacity[pre][cur] -= aug;
				capacity[cur][pre] += aug;
				flow[pre][cur] += aug;
				// flow[cur][pre] = -flow[pre][cur];
				// 这里是比较特殊的 反向的COST是一样的 如果是不一样的 那初始化时就要指定好
				cost[cur][pre] = -cost[pre][cur];
				minCost += cost[pre][cur] * aug;
				cur = pre;
			}
		}
		System.out.println("maxFlow:" + maxFlow);
		System.out.println("minCost:" + minCost);
		return minCost;
	}

	// x 为最小费用流的充要条件是不存在负费用增广圈
	private static int spfa(int S, int T)
	{
		int i, j;
		initEcost(costDist, path, S);
		ArrayDeque<Integer> Q = new ArrayDeque<Integer>(costDist.length);
		int[] count = new int[costDist.length];
		boolean[] mask = new boolean[costDist.length];
		Q.offer(S);
		count[S] = 1;
		mask[S] = true;
		while (!Q.isEmpty()) {
			i = Q.poll();
			mask[i] = false;
			for (j = 1; j <= vertexCnt; j++) {
				if (capacity[i][j] > 0 && costDist[i] + cost[i][j] < costDist[j]) {
					costDist[j] = costDist[i] + cost[i][j];
					path[j] = i;
					if (!mask[j]) {
						Q.offer(j);
						count[j]++;
						mask[j] = true;
						if (count[j] > costDist.length) {
							return j;
						}
					}
				}
			}
		}
		// 意味着没负圈
		return -1;
	}

	public static boolean BFS4Dinic(int s, int t)
	{
		LinkedList<Integer> queue = new LinkedList<Integer>();
		int u, v;
		H = new int[vertexCnt + 1];
		queue.offer(s);
		H[s] = 1;
		while (!queue.isEmpty()) {
			u = queue.peek();
			queue.poll();
			for (v = 1; v <= vertexCnt; v++) {
				if (capacity[u][v] > 0 && H[v] == 0) {
					H[v] = H[u] + 1;
					queue.offer(v);
					if (v == t) { // 找到T就行了 其他相同层次的可以不用管了
						return H[t] != 0; // 其实可以直接返回TRUE
					}
				}
			}
		}
		return H[t] != 0;
	}

	public static int Dinic_Recurisive(int S, int T)
	{
		int maxFlow = 0;// 最大流初始化
		// 如果BFS为FALSE 说明已经没有到T的路径了
		while (BFS4Dinic(S, T)) {
			maxFlow += outFlow(S, MAX, T);
		}
		return maxFlow;
	}

	public static int outFlow(int u, int beforeMin, int T)
	{
		int out = 0, branchFlow;
		// u是汇点，或者不可达了 则返回
		if (u == T || beforeMin == 0) {
			return beforeMin;
		}

		for (int v = 1; v <= vertexCnt; v++) {
			if ((capacity[u][v] > 0) && (H[u] + 1 == H[v])) {
				branchFlow = outFlow(v, Math.min(beforeMin, capacity[u][v]), T);
				path[v] = u;
				capacity[path[v]][v] -= branchFlow;// 正向更新
				capacity[v][path[v]] += branchFlow;
				flow[path[v]][v] += branchFlow; // 更新FLOW表
				flow[v][path[v]] = -flow[path[v]][v];
				out += branchFlow;
				beforeMin -= branchFlow;
				// 前面压入的已经没了 所以不能再压出去了
				if (beforeMin == 0) {
					break;
				}
			}
		}
		// 说明U这点的子孙遍历完了 可以设置为BLACK
		H[u] = MAX;
		return out;
	}

	// 消圈算法（Cycle Canceling）
	// 先求最大流或定值流的一个流，再消去里面的负费用圈
	// http://blog.sina.com.cn/s/blog_64675f540100sbx0.html
	public static int cycleCanceling(int S, int T)
	{
		int minCost = 0;
		int maxFlow = Dinic_Recurisive(S, T);
		System.out.println("Dinic_Recurisive:" + maxFlow);
		for (int i = 0; i < flow.length; i++) {
			for (int j = 0; j < flow[i].length; j++) {
				if (flow[i][j] > 0) {
					System.out.println(i + " -> " + j + " : " + flow[i][j]);
					minCost += flow[i][j] * cost[i][j];
				}
			}
		}
		System.out.println("minCost:" + minCost);
		int cur, pre, aug;
		int start;
		while ((start = spfa(S, T)) != -1) {
			aug = MAX;
			cur = start;
			do {
				pre = path[cur];
				aug = Math.min(aug, capacity[pre][cur]);
				cur = pre;
			} while (cur != start);
			cur = start;
			do {
				pre = path[cur];
				capacity[pre][cur] -= aug;
				capacity[cur][pre] += aug;
				flow[pre][cur] += aug;
				flow[cur][pre] = -flow[pre][cur];
				// 这里是比较特殊的 反向的COST是一样的 如果是不一样的 那初始化时就要指定好
				cost[cur][pre] = -cost[pre][cur];
				minCost -= cost[cur][pre] * aug;
				cur = pre;
			} while (cur != start);
		}
		maxFlow = 0;
		for (int i = 1; i <= vertexCnt; i++) {
			if (flow[i][T] > 0) {
				maxFlow += flow[i][T];
			}
		}
		System.out.println("maxFlow:" + maxFlow);
		System.out.println("minCost:" + minCost);
		return minCost;
	}

	public static void main(String[] args)
	{
		vertexCnt = 5;
		int S = 1, T = 5;
		buildNet();
		System.out.println(successiveShortestPath(S, T));
		printFlow();

		buildNet();
		System.out.println(cycleCanceling(S, T));
		printFlow();
	}

	private static void printFlow()
	{
		for (int i = 0; i < flow.length; i++) {
			for (int j = 0; j < flow[i].length; j++) {
				if (flow[i][j] > 0) {
					System.out.println(i + " -> " + j + " : " + flow[i][j]);
				}
			}
		}
	}
}