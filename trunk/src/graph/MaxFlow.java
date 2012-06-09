package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/** 最小生成树的Kruskal算法(以无向图为例) */
/** 最小生成树是最小权值生成树，并不是边最小化，因为生成树的边都固定为V-1 */
public class MaxFlow
{
	public static Map<Ve, Integer>	vertexMap	= new HashMap<Ve, Integer>();
	public static List<E>			EdgeQueue	= new ArrayList<E>();
	public static List<E>			EdgeList	= new ArrayList<E>();
	public static int				MAX			= Integer.MAX_VALUE;
	public static int				edgeCnt;
	public static int				vertexCnt	= edgeCnt = 0;					// 点数，边数

	public static int				MAXN		= 10;
	public static int[][]			capacity;
	public static int[][]			flow;
	public static int[]				minCapacity;
	public static int[]				father;
	public static int				maxFlow;
	public static boolean[]			visit;
	public static int[]				level;

	// Ford-Fullkerson方法 Method
	// 使用BFS来实现Ford-Fullkerson方法中的找增广路径的算法称为Edmonds-Karp算法。
	// Edmonds-Karp算法是最短增广路算法，因为实用BFS找到的增广路径是所有可能的增广路径中最短的路径。它的复杂度是O(VE^2)，其中V是结点数，E是有向边数。
	// 如果用使用DFS代替BFS，则Ford-Fullkerson方法退化成一般增广路算法。其复杂度是O(E| f* |)。其中f*是算法找出的最大流。
	// 最大流的Edmonds-Karp算法 Algorithm
	public static int Edmonds_Karp(int start, int terminal)
	{
		LinkedList<Integer> queue = new LinkedList<Integer>();
		int u, v;
		maxFlow = 0;// 最大流初始化
		while (true) {
			minCapacity = new int[MAXN];// 每次寻找增广路径都将每个点的流入容量置为0
			visit = new boolean[MAXN];// 标记一个点是否已经压入队列
			minCapacity[start] = MAX;// 源点的容量置为正无穷
			queue.offer(start);// 将源点加入队列
			while (!queue.isEmpty()) { // 当队列不为空
				u = queue.peek();
				queue.poll();
				for (v = 1; v <= vertexCnt; v++) {
					if (!visit[v] && capacity[u][v] > 0) {
						visit[v] = true;
						father[v] = u;// 记录下他的父亲方便往后的正反向更新
						queue.offer(v);
						// 当前点的容量为父亲点容量与边流量的较小者
						// 一边找最短路径 一边更新这条路径上的最小残余流量(min residual capacity)
						// 通过数组传递MIN CAPACITY NICE
						minCapacity[v] = (minCapacity[u] < capacity[u][v] ? minCapacity[u] : capacity[u][v]);
					}
				}
				// 找到增广路径(augmenting path)[残留网络中S到T的简单路径]
				if (minCapacity[terminal] > 0) {// 如果找到了汇点并且汇点容量不为0则清空队列。
					while (!queue.isEmpty()) {
						queue.poll();
					}
					break;
				}
			}
			if (minCapacity[terminal] == 0) {
				// 经过BFS 不能达到终点T 即找不到增广路径(augmenting path)[残留网络中S到T的简单路径]
				break;
			}
			for (int i = terminal; i != 1; i = father[i]) {
				capacity[father[i]][i] -= minCapacity[terminal];// 正向更新
				capacity[i][father[i]] += minCapacity[terminal];// 反向更新
				flow[father[i]][i] += minCapacity[terminal]; // 更新FLOW表
			}
			maxFlow += minCapacity[terminal];// 更新最大流
			// 如果Maxflow 可以认为没有流
		}
		return maxFlow;
	}

	public static void main(String[] args)
	{
		buildCap();
		System.out.println(Edmonds_Karp(1, 4));
		buildCap();
		System.out.println(Dinic(1, 4));
		buildCap();
		System.out.println(Dinic2(1, 4));
	}

	public static boolean BFS(int s, int t)
	{
		LinkedList<Integer> queue = new LinkedList<Integer>();
		int u, v;
		level = new int[vertexCnt + 1];
		queue.offer(s);
		level[s] = 1;
		while (!queue.isEmpty()) {
			u = queue.peek();
			queue.poll();
			for (v = 1; v <= vertexCnt; v++) {
				if (capacity[u][v] > 0 && level[v] == 0) {
					level[v] = level[u] + 1;
					queue.offer(v);
					if (v == t) { // 找到T就行了 其他相同层次的可以不用管了
						return level[t] != 0; // 其实可以直接返回TRUE
					}
				}
			}
		}
		return level[t] != 0;
	}

	// Dinic是在Edmond_Karp上的优化，就是传说中的分层；分层听起来难理解，其实就是分级即给节点具有一定规则的标记，看一下实现就懂了！
	// 算法的实现步骤：
	// 1.分层：利用BFS搜索方式给每个节点给予标记level[i]；
	// 2.判断分层是否成功即汇点是否被分到级别level[sink]!=0；
	// 3.在分层的基础上即level[i]上寻找所有的增广路、累计流量，回到步骤1；
	//
	public static int Dinic(int start, int terminal)
	{
		LinkedList<Integer> queue = new LinkedList<Integer>();
		int u, v;
		maxFlow = 0;// 最大流初始化
		// 如果BFS为FALSE 说明已经没有到T的路径了
		while (BFS(start, terminal)) {
			int[] nextV = new int[vertexCnt + 1];// 保存U的下一个对应端点V,不作重复遍历V
			for (int i = 0; i <= vertexCnt; i++) { // nextV里初始化是第一个节点哈 FOR循环
				nextV[i] = 1; // DFS中，如果需要回溯，就回溯到nextV中的节点。
			}
			father = new int[vertexCnt + 1];
			minCapacity = new int[vertexCnt + 1];// 每次寻找增广路径都将每个点的流入容量置为0
			minCapacity[start] = MAX;// 源点的容量置为正无穷
			queue.offer(start);// 将源点加入队列
			// 当队列为空时，说明已经回到START了 可以重新BFS了
			while (!queue.isEmpty()) {
				u = queue.peek();
				for (v = nextV[u]; v <= vertexCnt; v++) {
					if ((capacity[u][v] > 0) && (level[u] + 1 == level[v])) {
						father[v] = u;
						queue.push(v);
						minCapacity[v] = (minCapacity[u] < capacity[u][v] ? minCapacity[u] : capacity[u][v]);
						nextV[u] = v + 1;
						break;
					}
				}
				// 如果这个新搜到的点是T
				if (v == terminal) {
					for (int i = terminal; i != start; i = father[i]) {
						capacity[father[i]][i] -= minCapacity[terminal];// 正向更新
						capacity[i][father[i]] += minCapacity[terminal];// 反向更新
						flow[father[i]][i] += minCapacity[terminal]; // 更新FLOW表
						if (capacity[father[i]][i] == 0) {
							// 把stack弹到前一个饱和压入的点 因为他没能力继续压入了 后面的PRUNE
							while (queue.peek() != father[i]) {
								queue.poll();
							}
						}
					}
					// 如果Maxflow==0 可以认为没有流
					maxFlow += minCapacity[terminal];// 更新最大流
				}
				// 说明U这点的子孙遍历完了 可以设置为BLACK
				else if (v > vertexCnt) {
					level[u] = MAX;
					queue.poll();
				}
			}

		}
		return maxFlow;
	}

	private static void buildCap()
	{
		vertexCnt = 4;
		capacity = new int[vertexCnt + 1][vertexCnt + 1];
		flow = new int[vertexCnt + 1][vertexCnt + 1];
		minCapacity = new int[vertexCnt + 1];
		father = new int[vertexCnt + 1];
		visit = new boolean[vertexCnt + 1];
		level = new int[vertexCnt + 1];
		addCap(1, 2, 40);
		addCap(1, 4, 20);
		addCap(2, 3, 30);
		addCap(2, 4, 20);
		addCap(3, 4, 10);
	}

	private static void addCap(int u, int v, int cap)
	{
		capacity[u][v] = cap;
	}

	public static int Dinic2(int start, int terminal)
	{
		int[] cur = new int[vertexCnt + 1];
		int flow = 0;
		int i, u, flag, v, ag, k;
		while (BFS(start, terminal)) {
			for (i = 0; i <= vertexCnt; i++) { // cur里初始化是第一个节点哈
				cur[i] = 0; // DFS中，如果需要回溯，就回溯到cur中的节点。
				minCapacity[i] = MAX; // a里面存的是剩余流量
			}
			u = start;
			while (true) {
				flag = 0;
				for (v = cur[u]; v <= vertexCnt; v++) {
					if (capacity[u][v] > 0 && level[u] + 1 == level[v]) {
						flag = 1;
						break;
					}
				}
				if (flag == 1) {
					cur[u] = v + 1;
					father[v] = u;
					minCapacity[v] = (minCapacity[u] < capacity[u][v] ? minCapacity[u] : capacity[u][v]);
					u = v; // 从找到的节点后开始在层次图里找路
					if (u == terminal) // 找到t后，增广
					{
						ag = minCapacity[terminal];
						flow += ag;
						for (v = terminal; v != start; v = father[v]) {
							cur[father[v]] = v; // 退回上一步。。 感觉这个多了
							capacity[father[v]][v] -= ag;
							capacity[v][father[v]] += ag;
							minCapacity[v] -= ag;
							if (capacity[father[v]][v] == 0) {
								u = father[v];
							}
						}
					}
				}
				else if (u != start) // 如果u ！= s 那么这条路走不通的话，从u的上一个节点继续找。
				{
					level[u] = MAX; // 相当于从层次图里删除这个节点。
					u = father[u];
				}
				else {
					// 如果从源点找不到增广路，就结束咧。
					break;
				}
			}
		}
		return flow;
	}

}