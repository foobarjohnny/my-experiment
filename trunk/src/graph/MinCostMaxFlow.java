package graph;

import java.util.ArrayDeque;
import java.util.LinkedList;

/*http://kody-lau.blog.hexun.com/p5/default.aspx
 *
 1�� �������·�㷨��Successive Shortest Path��--����-->3��
 2�� ��Ȧ�㷨��Cycle Canceling��--����-->4��
 3�� ԭʼ��ż�㷨��Primal Dual����
 4�� ���絥���Σ�Network Simplex��
 mincost-maxflow
 mincost-maxflow��һ�����÷�Χ�ܹ��ģ�͡����ֻ�������������Ƿ��ã�����maxflowģ�ͣ�
 ���ֻ���Ƿ��ò��������������� shortest pathģ�ͣ������ۺϣ�����mincost-maxflowģ�͡�
 ��������������ĺ��ģ�ʵ������Ҫ���о�mincost-maxflow��
 (1)������˼�룺���������������·
 ��maxflow���ƣ�mincost-maxflowҲ�����ֻ���˼�롣
 һ�־��Ǳ�֤�����ǰ���²�����ȥ���ø�����Ҳ�����������㷨��
 ����һ�־��Ǳ�֤û�и������������ʹ���������Ҳ������С����·�㷨���ֳ��������·�㷨��
 �������൱��LPԭʼ���⣬���������·���Ƕ�ż���⡣
 (2)���Ľ��㷨��
 �����������㷨��һ����Ҫ�Ľ����������絥�����㷨��
 ���絥�����㷨�����Թ滮�������㷨��������ȫ��ͬ����������һ�������ž��˵�ָ�������ӶȺ�ͬ�����˵ļ�������Ч�ʡ�
 ���絥�����㷨ͨ������һ�ÿ�������Ȼ��Ѱ��һ������ߺͳ����������ٵ�Ѱ�Ҹ�����
 ����㷨ͬ���������˻��ͳ�ʼ������ѡ�������⣬�����������Թ滮�������㷨�Ľ��������ȵ����Խ����
 ���������·�㷨����չ����ԭʼ��ż�㷨��
 ͨ���������Է��֣�ʵ����Ѱ����С����·�Ĺ��̿�����ȫ�򻯳�ΪѰ�ҵ�Դ���·���Ĺ��̣������ϵ�Ȩֵ��Ϊ�ñߵķ��á�
 ���߶���ͬ��һ�������ƺ�����ȷ�����бߣ�Ѱ�����·����������������㷨��
 ԭʼ��ż�㷨������Ѱ�����·֮�󣬽��ж�·ͬʱ���������ٵ���������
 �ݳ��������·��ԭʼ��ż�㷨��O(V^3)ʵ�֣����Ǻ��ź����Ҳ�֪��Ӧ�����ʵ�֣�ϣ���������֪���Ļ����Խ̽��ҡ�
 */
public class MinCostMaxFlow
{
	public static int[][]	flow;

	public static int[][]	capacity;
	public static int[][]	cost;
	public static int[]		costDist;
	public static int[]		path;
	public static int[]		H;
	public static int		vertexCnt	= 0;		// ����������
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
		// ����ֻ�����˸�Ȩ�ߣ���ΪCOST������Ϊ��ֵͬ�������ܳ��ָ���Ȩ·
		// �����ж���û��START-END�����COST·�� ���������������
		// ���COST��˫��ֵ����ȣ��Ǿ�Ҫ���Ǹ���Ȩ·
		// ��BELLMAN_FORD����SPFA ����ֵ����0,1,2���� �ж���û��START-END��û�����·��
		// �����������ʽ������Ҫ�ò��鼯�ҳ��и���Ȩ·�ߵĵ������ӵ��ĵ� �Ƿ����END�� ����ͱȽϸ�����
		return costDist[end] != MAX;
	}

	// �������·�㷨��Successive Shortest Path����С����·�㷨���ֳ��������·�㷨
	// http://www.cnblogs.com/zhuangli/archive/2008/08/01/1258434.html
	// ��С��������� ����capacityͼ�ı����£���costͼS-T�����·�� ��ϸ��PPT
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
				// �����ǱȽ������ �����COST��һ���� ����ǲ�һ���� �ǳ�ʼ��ʱ��Ҫָ����
				cost[cur][pre] = -cost[pre][cur];
				minCost += cost[pre][cur] * aug;
				cur = pre;
			}
		}
		System.out.println("maxFlow:" + maxFlow);
		System.out.println("minCost:" + minCost);
		return minCost;
	}

	// x Ϊ��С�������ĳ�Ҫ�����ǲ����ڸ���������Ȧ
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
		// ��ζ��û��Ȧ
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
					if (v == t) { // �ҵ�T������ ������ͬ��εĿ��Բ��ù���
						return H[t] != 0; // ��ʵ����ֱ�ӷ���TRUE
					}
				}
			}
		}
		return H[t] != 0;
	}

	public static int Dinic_Recurisive(int S, int T)
	{
		int maxFlow = 0;// �������ʼ��
		// ���BFSΪFALSE ˵���Ѿ�û�е�T��·����
		while (BFS4Dinic(S, T)) {
			maxFlow += outFlow(S, MAX, T);
		}
		return maxFlow;
	}

	public static int outFlow(int u, int beforeMin, int T)
	{
		int out = 0, branchFlow;
		// u�ǻ�㣬���߲��ɴ��� �򷵻�
		if (u == T || beforeMin == 0) {
			return beforeMin;
		}

		for (int v = 1; v <= vertexCnt; v++) {
			if ((capacity[u][v] > 0) && (H[u] + 1 == H[v])) {
				branchFlow = outFlow(v, Math.min(beforeMin, capacity[u][v]), T);
				path[v] = u;
				capacity[path[v]][v] -= branchFlow;// �������
				capacity[v][path[v]] += branchFlow;
				flow[path[v]][v] += branchFlow; // ����FLOW��
				flow[v][path[v]] = -flow[path[v]][v];
				out += branchFlow;
				beforeMin -= branchFlow;
				// ǰ��ѹ����Ѿ�û�� ���Բ�����ѹ��ȥ��
				if (beforeMin == 0) {
					break;
				}
			}
		}
		// ˵��U��������������� ��������ΪBLACK
		H[u] = MAX;
		return out;
	}

	// ��Ȧ�㷨��Cycle Canceling��
	// �����������ֵ����һ����������ȥ����ĸ�����Ȧ
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
				// �����ǱȽ������ �����COST��һ���� ����ǲ�һ���� �ǳ�ʼ��ʱ��Ҫָ����
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