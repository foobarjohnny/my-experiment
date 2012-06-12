package graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/** 最小生成树的Kruskal算法(以无向图为例) */
/** 最小生成树是最小权值生成树，并不是边最小化，因为生成树的边都固定为V-1 */
public class MaxFlow
{
	public static Map<Ve, Integer>		vertexMap	= new HashMap<Ve, Integer>();
	public static List<E>				EdgeQueue	= new ArrayList<E>();
	public static List<E>				EdgeList	= new ArrayList<E>();
	public static int					MAX			= Integer.MAX_VALUE;
	public static int					edgeCnt;
	public static int					vertexCnt	= edgeCnt = 0;					// 点数，边数

	public static int					MAXN		= 10;
	public static int[][]				capacity;
	public static int[][]				flow;
	public static int[]					minCapacity;
	public static int[]					father;
	public static int					maxFlow;
	public static boolean[]				visit;
	public static int[]					gap;
	public static int[]					nextV;
	public static int[]					H;											// H==Height
	public static int[]					E;											// E=Excess
	public static LinkedList<Integer>[]	activeHLPP;

	// Ford-Fullkerson方法 Method
	// 使用BFS来实现Ford-Fullkerson方法中的找增广路径的算法称为Edmonds-Karp算法。
	// Edmonds-Karp算法是最短增广路算法，因为实用BFS找到的增广路径是所有可能的增广路径中最短的路径。它的复杂度是O(VE^2)，其中V是结点数，E是有向边数。
	// 如果用使用DFS代替BFS，则Ford-Fullkerson方法退化成一般增广路算法。其复杂度是O(E| f* |)。其中f*是算法找出的最大流。
	// 最大流的Edmonds-Karp算法 Algorithm
	// S: Start T: Terminal
	public static int Edmonds_Karp(int S, int T)
	{
		LinkedList<Integer> queue = new LinkedList<Integer>();
		int u, v;
		maxFlow = 0;// 最大流初始化
		while (true) {
			minCapacity = new int[vertexCnt + 1];// 每次寻找增广路径都将每个点的流入容量置为0
			visit = new boolean[vertexCnt + 1];// 标记一个点是否已经压入队列
			minCapacity[S] = MAX;// 源点的容量置为正无穷
			queue.offer(S);// 将源点加入队列
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
				// 找到增广路径(augmenting father)[残留网络中S到T的简单路径]
				if (minCapacity[T] > 0) {// 如果找到了汇点并且汇点容量不为0则清空队列。
					while (!queue.isEmpty()) {
						queue.poll();
					}
					break;
				}
			}
			if (minCapacity[T] == 0) {
				// 经过BFS 不能达到终点T 即找不到增广路径(augmenting father)[残留网络中S到T的简单路径]
				break;
			}
			int aug = minCapacity[T];
			for (int i = T; i != S; i = father[i]) {
				capacity[father[i]][i] -= aug;// 正向更新
				capacity[i][father[i]] += aug;// 反向更新
				flow[father[i]][i] += aug; // 更新FLOW表
				minCapacity[i] -= aug;
			}
			maxFlow += aug;// 更新最大流
			// 如果Maxflow 可以认为没有流
		}
		return maxFlow;
	}

	public static void main(String[] args)
	{
		int S = 2, T = 9;
		buildCap();
		System.out.println(Edmonds_Karp(S, T));
		printFlow();
		buildCap();
		System.out.println(Dinic(S, T));
		buildCap();
		System.out.println(Dinic2(S, T));
		printFlow();
		buildCap();
		System.out.println(ISAP(S, T));
		buildCap();
		System.out.println(ISAP2(S, T));
		buildCap();
		System.out.println(Push_Relabel(S, T));
		buildCap();
		System.out.println(Relabel_To_Front(S, T));
		buildCap();
		System.out.println(FIFO_Preflow_Push(S, T));
		buildCap();
		// System.out.println(HLPP(S, T));
		// buildCap();
		// System.out.println(Hungary());
	}

	private static void printFlow()
	{
		for (int i = 0; i < flow.length; i++) {
			for (int j = 0; j < flow[i].length; j++) {
				if (flow[i][j] > 0) {
					System.out.println(i + " -> " + j + " : " + (flow[i][j] - flow[j][i]));
				}
			}
		}
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

	// Dinic是在Edmond_Karp上的优化，就是传说中的分层；分层听起来难理解，其实就是分级即给节点具有一定规则的标记，看一下实现就懂了！
	// 算法的实现步骤：
	// 1.分层：利用BFS搜索方式给每个节点给予标记H[i]；
	// 2.判断分层是否成功即汇点是否被分到级别H[sink]!=0；
	// 3.在分层的基础上即H[i]上寻找所有的增广路、累计流量，回到步骤1；
	// No-Recursive
	// O(V^2E)
	public static int Dinic(int S, int T)
	{
		LinkedList<Integer> queue = new LinkedList<Integer>();
		int u, v;
		maxFlow = 0;// 最大流初始化
		// 如果BFS为FALSE 说明已经没有到T的路径了
		while (BFS4Dinic(S, T)) {
			nextV = new int[vertexCnt + 1];// 保存U的下一个对应端点V,不作重复遍历V
			for (int i = 0; i <= vertexCnt; i++) { // nextV里初始化是第一个节点哈 FOR循环
				nextV[i] = 1; // DFS中，如果需要回溯，就回溯到nextV中的节点。
			}
			father = new int[vertexCnt + 1];
			minCapacity = new int[vertexCnt + 1];// 每次寻找增广路径都将每个点的流入容量置为0
			minCapacity[S] = MAX;// 源点的容量置为正无穷
			queue.offer(S);// 将源点加入队列
			// 当队列为空时，说明已经回到S了 可以重新BFS了
			while (!queue.isEmpty()) {
				u = queue.peek();
				for (v = nextV[u]; v <= vertexCnt; v++) {
					if ((capacity[u][v] > 0) && (H[u] + 1 == H[v])) {
						father[v] = u;
						queue.push(v);
						minCapacity[v] = (minCapacity[u] < capacity[u][v] ? minCapacity[u] : capacity[u][v]);
						nextV[u] = v + 1;
						break;
					}
				}
				// 如果这个新搜到的点是T
				if (v == T) {
					int aug = minCapacity[T];
					for (int i = T; i != S; i = father[i]) {
						capacity[father[i]][i] -= aug;// 正向更新
						capacity[i][father[i]] += aug;// 反向更新
						flow[father[i]][i] += aug; // 更新FLOW表
						minCapacity[i] -= aug; // 设S永远为MAX
						if (capacity[father[i]][i] == 0) {
							// 把stack弹到前一个饱和压入的点 因为他没能力继续压入了 后面的PRUNE
							// 设成u=S也行 但这样是 剪枝 阻塞优化＝＝多路增广 连续增广
							// 设成u=S时 不用minCapacity用一个aug变量更安全
							while (queue.peek() != father[i]) {
								queue.poll();
							}
						}
					}
					// 如果Maxflow==0 可以认为没有流
					maxFlow += aug;// 更新最大流
				}
				// 说明U这点的子孙遍历完了 可以设置为BLACK
				// 其实不设也没有关系 但避免重复
				else if (v > vertexCnt) {
					H[u] = MAX;
					queue.poll();
				}
			}

		}
		return maxFlow;
	}

	// Recursive
	public static int Dinic_Recurisive(int S, int T)
	{
		maxFlow = 0;// 最大流初始化
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
				father[v] = u;
				capacity[father[v]][v] -= branchFlow;// 正向更新
				capacity[v][father[v]] += branchFlow;
				flow[father[v]][v] += branchFlow; // 更新FLOW表
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

	private static void buildCap()
	{
		// gap 设成vertexCnt*2，因为有可能有V+1次提升高度，原来最大高度是V V+V-1=2V-1
		vertexCnt = 10;
		capacity = new int[vertexCnt + 1][vertexCnt + 1];
		flow = new int[vertexCnt + 1][vertexCnt + 1];
		minCapacity = new int[vertexCnt + 1];
		father = new int[vertexCnt + 1];
		visit = new boolean[vertexCnt + 1];
		gap = new int[vertexCnt * 2];
		nextV = new int[vertexCnt + 1];
		H = new int[vertexCnt + 1];// H==Height
		E = new int[vertexCnt + 1];// E=Excess
		activeHLPP = new LinkedList[vertexCnt * 2];
		for (int i = 0; i < activeHLPP.length; ++i) {
			activeHLPP[i] = new LinkedList<Integer>();
		}
		// addCap(1, 2, 40);
		// addCap(1, 4, 20);
		// addCap(2, 3, 30);
		// addCap(2, 4, 20);
		// addCap(3, 4, 10);

		// addCap(1, 6, 1);
		// addCap(2, 6, 1);
		// addCap(2, 8, 1);
		// addCap(3, 7, 1);
		// addCap(3, 8, 1);
		// addCap(3, 9, 1);
		// addCap(4, 8, 1);
		// addCap(5, 8, 1);

//		addCap(7, 1, 3);
//		addCap(7, 2, 1);
//		addCap(7, 3, 10);
		int maxint = MAX;
//		addCap(1, 4, maxint);
//		addCap(1, 5, maxint);
//		addCap(2, 4, maxint);
//		addCap(2, 6, maxint);
//		addCap(3, 5, maxint);
//		addCap(4, 5, maxint);
//		addCap(4, 6, maxint);
//
//		addCap(4, 8, 2);
//		addCap(5, 8, 3);
//		addCap(6, 8, 6);
		
		addCap(1, 2, 1);
		addCap(3, 4, 4);
		addCap(5, 6, 4);
		addCap(7, 8, 11);
		addCap(9, 10, 1);
//		addCap(2, 1, 1);
//		addCap(4, 3, 6);
//		addCap(6, 5, 6);
//		addCap(8, 7, 11);
//		addCap(10, 9, 1);
		
		addCap(4, 1, maxint);
		addCap(6, 1, maxint);
		addCap(2, 3, maxint);
		addCap(2, 5, maxint);
		addCap(4, 7, maxint);
		addCap(6, 7, maxint);
		addCap(8, 3, maxint);
		addCap(8, 5, maxint);
		addCap(8, 9, maxint);
		addCap(10, 7, maxint);

	}

	private static void addCap(int u, int v, int cap)
	{
		capacity[u][v] = cap;
		// capacity[v][u] = cap;
	}

	public static int Dinic2(int S, int T)
	{
		int[] cur = new int[vertexCnt + 1];
		int flow = 0;
		int i, u, flag, v, ag, k;
		while (BFS4Dinic(S, T)) {
			for (i = 0; i <= vertexCnt; i++) { // cur里初始化是第一个节点哈
				cur[i] = 0; // DFS中，如果需要回溯，就回溯到cur中的节点。
				minCapacity[i] = MAX; // a里面存的是剩余流量
			}
			u = S;
			while (true) {
				flag = 0;
				for (v = cur[u]; v <= vertexCnt; v++) {
					if (capacity[u][v] > 0 && H[u] + 1 == H[v]) {
						flag = 1;
						break;
					}
				}
				if (flag == 1) {
					cur[u] = v + 1;
					father[v] = u;
					minCapacity[v] = (minCapacity[u] < capacity[u][v] ? minCapacity[u] : capacity[u][v]);
					u = v; // 从找到的节点后开始在层次图里找路
					if (u == T) // 找到t后，增广
					{
						ag = minCapacity[T];
						flow += ag;
						for (v = T; v != S; v = father[v]) {
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
				else if (u != S) // 如果u ！= s 那么这条路走不通的话，从u的上一个节点继续找。
				{
					H[u] = MAX; // 相当于从层次图里删除这个节点。
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

	// Improved Shortest Augmenting Path
	// 1. Gap 优化 2. 主动标号技术
	// ISAP和DINIC 原理都是一样的 都借鉴了Preflow里的标号
	// ISAP是动态改变标号＝＝主动标号，DINIC是调用BFS来重建标号＝＝被动标号
	// ISAP里还加入了GAP 优化，和DINIC里都有阻塞优化 详细可见下面的描述
	public static int ISAP(int S, int T)
	{
		maxFlow = 0;
		for (int i = 0; i <= vertexCnt; i++) {
			H[i] = gap[i] = 0;
			nextV[i] = 1;
		}
		int u = S, v;
		gap[0] = vertexCnt;
		minCapacity[S] = MAX;
		while (H[S] <= vertexCnt) {
			for (v = nextV[u]; v <= vertexCnt; v++) {
				if ((capacity[u][v] > 0) && (H[v] + 1 == H[u])) {
					father[v] = u;
					minCapacity[v] = (minCapacity[u] < capacity[u][v] ? minCapacity[u] : capacity[u][v]);
					nextV[u] = v;
					// u=v 与DINIC的区别所在 因为是它自己建BFS生成的标号树，超过了VERTEXCNT后
					// 他自己循环重建而DINIC是出去重建BFS
					u = v;
					break;
				}
			}
			// 如果这个新搜到的点是T，找到了增广路径
			if (v == T) {
				// 设成u=S时 不用 minCapacity用一个aug变量更安全
				int aug = minCapacity[T];
				for (int i = T; i != S; i = father[i]) {
					capacity[father[i]][i] -= aug;// 正向更新
					capacity[i][father[i]] += aug;// 反向更新
					flow[father[i]][i] += aug; // 更新FLOW表
					minCapacity[i] -= aug;
					// 设成u=S也行 但这样是 剪枝 阻塞优化＝＝多路增广 连续增广
					if (capacity[father[i]][i] == 0) {
						u = father[i];
					}
				}
				maxFlow += aug;// 更新最大流

			}
			// U点没容许弧了，增大标号 dis 或者Level
			else if (v > vertexCnt) {
				int minLev = vertexCnt;
				for (v = 1; v <= vertexCnt; v++) {
					if (capacity[u][v] > 0 && H[v] < minLev) {
						minLev = H[v];
					}
				}
				// Gap 优化，如果出现断层，直接中断
				if ((--gap[H[u]]) == 0) {
					break;
				}
				gap[H[u] = minLev + 1]++;
				// U点重新遍历
				nextV[u] = 1;
				// 距离标号改变后，从父结点重新搜索，S的例外
				if (u != S) {
					u = father[u];
				}
			}

		}
		return maxFlow;
	}

	public static int ISAP2(int s, int t)
	{
		int v, maxFlow = 0;
		nextV = new int[vertexCnt + 1];
		for (int i = 0; i <= vertexCnt; i++) {
			H[i] = gap[i] = 0;
			nextV[i] = 1;
		}
		int u = s, aug = MAX;
		gap[0] = vertexCnt;

		while (H[s] <= vertexCnt) {
			for (v = nextV[u]; v <= vertexCnt; v++) {
				if (capacity[u][v] > 0 && H[v] + 1 == H[u]) {
					aug = Math.min(aug, capacity[u][v]);
					father[v] = u;
					nextV[u] = v;
					u = v;
					if (v == t) {
						maxFlow += aug;
						while (u != s) {
							capacity[father[u]][u] -= aug;
							capacity[u][father[u]] += aug;
							u = father[u];
						}
						father = new int[vertexCnt + 1];
						aug = MAX;
					}
					v = nextV[u] - 1;// 为什么－1呢?
				}
			}
			// 不能找到U点的
			int mindis = vertexCnt;
			for (v = 1; v <= vertexCnt; v++) {
				if (capacity[u][v] > 0 && H[v] < mindis) {
					mindis = H[v];
				}
			}
			if ((--gap[H[u]]) == 0) {
				break;
			}
			gap[H[u] = mindis + 1]++;
			nextV[u] = 1;
			if (u != s) {
				u = father[u];
			}
		}
		return maxFlow;
	}

	// O(V^2E)
	public static int Push_Relabel(int S, int T)
	{
		int u, v;
		boolean done, needRelabel;
		// Initialize the pre-flow
		initPreflow(S);
		while (true) {
			done = true;
			for (u = 1; u <= vertexCnt; u++) {
				// 不检查S,T,因为定义里不是溢出顶点
				// 如果检查了T 则全为0
				if (u == S || u == T) {
					continue;
				}
				if (E[u] > 0) {
					// 可以把IF改成WHILE，但没有改，是为了体现GENERIC-PUSH-FLOW寻找PUSH和RELABEL操作为随机（这里是循环）
					// 而不是专注于一点，专注于一点就是溢出点的排除了 这里就有点优化的作用了
					done = false;
					// 先假设顶点u需要relabel
					needRelabel = true;
					for (v = 1; v <= vertexCnt; v++) { /* 检查能push的顶点 */
						if (capacity[u][v] > 0) {
							// 3 cases exist: E[u]>0 && Capacity[u][v]>0 &&
							// H[u]<=H[v]+1
							// 高度函数定义
							// System.out.println("capacity[" + u + "][" + v +
							// "] > 0");
							if (H[u] == H[v] + 1) {
								// System.out.println("H[" + u + "] == H[" + v +
								// "] + 1");
								// push
								needRelabel = false;
								push(u, v);
							}
							// else if (H[u] > H[v] + 1) {
							// System.out.println("H[" + u + "] > H[" + v +
							// "] + 1");
							// System.out.println("There should be no this case;");
							// }
							// else if (H[u] < H[v] + 1) {
							// System.out.println("H[" + u + "] < H[" + v +
							// "] + 1");
							// }
							// System.out.println();
						}
						// else if (capacity[u][v] == 0) {
						// // 3 cases exist: E[u]>0 && Capacity[u][v]==0 &&
						// // H[u]<=>H[v]+1
						// System.out.println("capacity[" + u + "][" + v +
						// "] == 0");
						// if (H[u] == H[v] + 1) {
						// System.out.println("H[" + u + "] == H[" + v +
						// "] + 1");
						// }
						// else if (H[u] > H[v] + 1) {
						// System.out.println("H[" + u + "] > H[" + v +
						// "] + 1");
						// }
						// else if (H[u] < H[v] + 1) {
						// System.out.println("H[" + u + "] < H[" + v +
						// "] + 1");
						// }
						// System.out.println();
						// }
						// else {
						// // no this case E[u]>0 && Capacity[u][v]>0
						// System.out.println("There should be no this 3 cases;");
						// System.out.println("capacity[" + u + "][" + v +
						// "] < 0");
						// if (H[u] == H[v] + 1) {
						// System.out.println("H[" + u + "] == H[" + v +
						// "] + 1");
						// System.out.println("There should be no this case;");
						// }
						// else if (H[u] > H[v] + 1) {
						// System.out.println("H[" + u + "] > H[" + v +
						// "] + 1");
						// }
						// else if (H[u] < H[v] + 1) {
						// System.out.println("H[" + u + "] < H[" + v +
						// "] + 1");
						// }
						// System.out.println();
						// }
					}
					// 没有可以push的顶点,执行relabel
					if (needRelabel) {
						// relabel
						relabel(u);
					}
				}
			}
			if (done) { /* 除源点和汇点外,每个顶点的e[i]都为0 */
				maxFlow = 0;
				for (u = 1; u <= vertexCnt; u++) {
					if (flow[u][T] > 0) {
						maxFlow += flow[u][T];
					}
				}
				break;
			}
		}
		// E[u] or -E[s]
		return maxFlow;
	}

	// O(V^3)
	public static int Relabel_To_Front(int S, int T)
	{
		int u;
		LinkedList<Integer> list = new LinkedList<Integer>();
		int oldHeight;
		// Initialize the pre-flow
		initPreflow(S);
		for (u = 1; u <= vertexCnt; u++) {
			// 不检查S,T,因为定义里不是溢出顶点
			// 如果检查了T 则全为0
			if (u == S || u == T) {
				continue;
			}
			list.offer(u);
		}
		for (Iterator<Integer> it = list.iterator(); it.hasNext();) {
			u = it.next();
			oldHeight = H[u];
			// 把U的余量EXCESS消掉
			Discharge(u);
			if (H[u] > oldHeight) {
				list.remove(new Integer(u));
				list.push(u);
				it = list.listIterator(1);// index 为1 表明是第二个元素
			}
		}
		/* 除源点和汇点外,每个顶点的e[i]都为0 */
		maxFlow = 0;
		for (u = 1; u <= vertexCnt; u++) {
			if (flow[u][T] > 0) {
				maxFlow += flow[u][T];
			}
		}
		// E[T] or -E[S] 如果把E[S]和E[T]设成0,才有意义,
		return maxFlow;
	}

	// O(V^3)
	// FIFO preflow-push algorithm 是Relabel_To_Front的优化 FIFO HEURISTIC
	// 如果该算法的Q是标准的FIFO队列，则时间复杂度为(V^2E)，
	// 如果是优先队列，并且标号最高的点优先的话：我们就得到了最高标号预流推进算法，其时间复杂度仅为(V^2sqrt(E))，算是比较快的最大流算法了。
	public static int FIFO_Preflow_Push(int S, int T)
	{
		int u, pushFlow;
		LinkedList<Integer> queue = new LinkedList<Integer>();
		// Initialize the pre-flow
		// initPreflow(S); 这里没有INIT
		queue.push(S);
		H[S] = vertexCnt;
		E[S] = MAX;
		// E[T] = -MAX;
		while (!queue.isEmpty()) {
			u = queue.peek();
			queue.pop();
			// 没用 while(E[u]>0)是考虑到把S加入了这个WHILE大循环，效果其实是一样的
			// 用WHILE(E[U]>0),便于理解DISCHARGE

			// push
			for (int v = 1; v <= vertexCnt; v++) {
				if (u == S || H[u] == H[v] + 1) {
					pushFlow = push(u, v);
					if (v != S && v != T && pushFlow > 0) {
						queue.push(v);
					}
				}
			}
			// relabel
			if (u != S && u != T && E[u] > 0) {
				relabel(u);
				queue.push(u);
			}
		}
		maxFlow = 0;
		for (u = 1; u <= vertexCnt; u++) {
			if (flow[u][T] > 0) {
				maxFlow += flow[u][T];
			}
		}
		// E[T] or -E[S]
		return maxFlow;
	}

	// Highest-Label Preflow-Push
	// O(V^2*sqrt(E))
	// Relabel-to-Front算法的时间复杂度是O(V^3)，还有一个叫Highest Label Preflow
	// Push的算法复杂度据说是O(V^2*E^0.5)。我研究了一下HLPP，感觉它和Relabel-to-Front本质上没有区别，因为
	// Relabel-to-Front每次前移的都是高度最高的顶点，所以也相当于每次选择最高的标号进行更新。还有一个感觉也会很好实现的算法是使用队列维护溢出顶点，每次对pop出来的顶点discharge，出现了
	// 新的溢出顶点时入队。
	// Push-Relabel类的算法有一个名为gap heuristic的优化，就是当存在一个整数0<k<2V-1，
	// 没有任何顶点满足h[v]=k时，对所有h[v]>k的顶点v做更新，若它小于2V-1就置为2V-1,最小高度为1，等于0时未设高度
	public static int HLPP(int S, int T)
	{
		int u, pushFlow;
		// Initialize the pre-flow
		// initPreflow(S); 这里没有INIT
		// 分层
		BFS4HLPP(S, T);
		// 把S特殊化一下
		E[S] = MAX;
		gap[H[S]]--;
		H[S] = vertexCnt + 1;
		gap[H[S]]++;
		int maxHeight = H[S];

		activeHLPP[H[S]].push(S);
		// E[T] = -MAX;
		while (maxHeight > 0) {
			if (activeHLPP[maxHeight].isEmpty()) {
				maxHeight--;
				continue;
			}
			System.out.println("maxHeight:" + maxHeight);
			u = (Integer) activeHLPP[maxHeight].peek();
			activeHLPP[maxHeight].poll();
			// 没用 while(E[u]>0)是考虑到把S加入了这个WHILE大循环，效果其实是一样的
			// 用WHILE(E[U]>0),便于理解DISCHARGE
			// push
			for (int v = 1; v <= vertexCnt; v++) {
				if (u == S || H[u] == H[v] + 1) {
					pushFlow = push(u, v);
					if (v != S && v != T && pushFlow > 0) {
						activeHLPP[H[v]].push(v);
					}
				}
			}
			// relabel
			if (u != S && u != T && E[u] > 0) {
				int oldHeight = H[u];
				gap[H[u]]--;
				relabel(u);
				gap[H[u]]++;
				activeHLPP[H[u]].push(u);
				// Gap heuristic 优化
				if (gap[oldHeight] == 0) {
					for (int i = 1; i <= vertexCnt; ++i)
						if (H[i] > oldHeight && H[i] < 2 * vertexCnt - 1) {
							gap[H[i]]--;
							gap[2 * vertexCnt - 1]++;
							H[i] = 2 * vertexCnt - 1;
						}
				}
				if (H[u] > maxHeight) {
					maxHeight = H[u];
				}
			}
		}
		maxFlow = 0;
		for (u = 1; u <= vertexCnt; u++) {
			if (flow[u][T] > 0) {
				maxFlow += flow[u][T];
			}
		}
		// E[T] or -E[S]
		return maxFlow;
	}

	public static void BFS4HLPP(int S, int T)
	{
		LinkedList<Integer> queue = new LinkedList<Integer>();
		int u, v;
		queue.offer(T);
		H[T] = 1;
		gap[H[T]]++;
		while (!queue.isEmpty()) {
			u = queue.peek();
			queue.poll();
			for (v = 1; v <= vertexCnt; v++) {
				if (capacity[v][u] > 0 && H[v] == 0) {
					H[v] = H[u] + 1;
					gap[H[v]]++;
					queue.offer(v);
				}
			}
		}
	}

	private static void Discharge(int u)
	{
		boolean needRelabel = true;
		while (E[u] > 0) {
			// 先假设顶点u需要relabel
			needRelabel = true;
			for (int v = 1; v <= vertexCnt; v++) { /* 检查能push的顶点 */
				if (capacity[u][v] > 0) {
					// 2 cases exist:
					// E[u]>0 && Capacity[u][v]>0 && H[u]<=H[v]+1
					// 高度函数定义
					if (H[u] == H[v] + 1) {
						// push
						needRelabel = false;
						push(u, v);
					}
				}
			}
			// 没有可以push的顶点,执行relabel
			// relabel
			if (needRelabel) {
				relabel(u);
			}
		}
	}

	private static void relabel(int u)
	{
		int minH = MAX;
		for (int v = 1; v <= vertexCnt; v++) {
			if (capacity[u][v] > 0 && H[v] < minH) {
				minH = H[v];
			}
		}
		// System.out.println("need Relabel H[" + u + "] from " + H[u] + " to "
		// + (1 + minH));
		// System.out.println();
		H[u] = 1 + minH;
	}

	private static int push(int u, int v)
	{
		int d = Math.min(capacity[u][v], E[u]);
		E[u] -= d;
		E[v] += d;
		capacity[u][v] -= d;
		capacity[v][u] += d;
		flow[u][v] += d;
		flow[v][u] = -flow[u][v];
		// System.out.println("Push " + u + "->" + v + " " + d);
		// System.out.println();
		return d;
		// flow[v][u] -=d; 这样其实可以保证的
	}

	private static void initPreflow(int S)

	{
		H = new int[vertexCnt + 1];// H==Height
		E = new int[vertexCnt + 1];// E=Excess
		flow = new int[vertexCnt + 1][vertexCnt + 1];
		H[S] = vertexCnt;
		for (int u = 1; u <= vertexCnt; u++) {
			if (capacity[S][u] > 0) {
				E[u] = capacity[S][u];
				E[S] -= capacity[S][u];
				flow[S][u] = capacity[S][u];
				flow[u][S] = -flow[S][u];
				capacity[u][S] = capacity[u][S] - flow[u][S];
				capacity[S][u] = capacity[S][u] - flow[S][u];
				// capacity[u][S] = capacity[S][u];
				// capacity[S][u] = 0;
			}
		}
	}

	public static boolean dfs(int u)
	{
		for (int v = 1; v <= vertexCnt; v++) {
			if (capacity[u][v] > 0) {
				if (!visit[v]) {
					visit[v] = true;
					if (father[v] == 0 || dfs(father[v])) {
						// father[i] == -1 保证了第一条边和最后一条边为未匹配边
						// dfs(father[i])实现了交替路径;
						father[v] = u;
						return true;
					}
				}
			}
		}
		return false;
	}

	public static int Hungary()
	{
		int maxMatch = 0;
		father = new int[vertexCnt + 1];
		for (int i = 1; i <= vertexCnt; ++i) {
			visit = new boolean[vertexCnt + 1];
			// 每找到一条增广径 最大匹配加1 M+1
			if (dfs(i)) {
				maxMatch++;
			}
		}
		// 已经把X,Y都遍历了 DOUBLE了 所以要除以2
		return maxMatch / 2;
	}

	/**
	 * 网络流ISAP算法的简单介绍
	 * 
	 * http://blog.csdn.net/nomad2/article/details/7422527
	 * 
	 * 这几天由于种种原因经常接触到网络流的题目，这一类型的题给人的感觉，就是要非常使劲的YY才能出来点比较正常的模型。
	 * 尤其是看了Amber最小割应用的文章， 里面的题目思路真是充满了绵绵不绝的YD思想
	 * 。然而比赛中，当你YD到了这一层后，您不得不花比较多的时间去纠结于大量细节的实现，而冗长的代码难免会使敲错版后的调试显得异常悲伤
	 * ，因此一些巧妙简短高效的网络流算法在此时便显得犹为重要了
	 * 。本文力求以最简短的描述，对比较流行的网络流算法作一定的总结，并借之向读者强烈推荐一种效率与编程复杂度相适应的算法。
	 * 
	 * 　　众所周知，在网络流的世界里，存在2类截然不同的求解思想，就是比较著名的预流推进与增广路，两者都需要反向边的小技巧。
	 * 
	 * 　　其中预流推进的算法思想是以边为单元进行推流操作。具体流程如下:置初始点邻接边满流并用一次反向bfs对每个结点计算反向距离标号，
	 * 定义除汇点外存量大于出量的结点为活动结点
	 * ，每次对活动结点按允许边（u->v:d[u]=d[v]+1）进行推流操作，直到无法推流或者该点存量为0，若u点此时仍为活动结点
	 * ，则进行重标号，使之等于原图中进行推操作后的邻接结点的最小标号
	 * +1，并将u点入队。当队列为空时，算法结束，只有s点和t点存量非0，网络中各顶点无存量，无法找到增广路继续增广，则t点存量为最大流。
	 * 
	 * 　　而增广路的思想在于每次从源点搜索出一条前往汇点的增广路，并改变路上的边权，直到无法再进行增广，此时汇点的增广量即为最大流。
	 * 两者最后的理论基础依然是增广路定理
	 * ，而在理论复杂度上预流推进要显得比较优秀。其中的HLPP高标预流推进的理论复杂度已经达到了另人发指的O（sqrt(m)*n
	 * *n），但是其编程复杂度也是同样的令人发指- -
	 * 
	 * 　　于是我们能否在编程复杂度和算法复杂度上找到一个平衡呢，答案是肯定的。我们使用增广路的思想，而且必须进行优化。因为原始的增广路算法（例如EK）
	 * 是非常悲剧的。于是有人注意到了预流推进中的标号法，在增广路算法中引入允许弧概念，每次反搜残留网络得到结点标号，在正向增广中利用递归进行连续增广，
	 * 于是产生了基于分层图的Dinic算法
	 * 。一些人更不满足于常规Dinic所带来的提升，进而加入了多路分流增广的概念，即对同一顶点的流量，分多路同时推进，再加上比较复杂的手工递归
	 * ，使得Dinic已经满足大部分题目的需要。
	 * 
	 * 　　然而这样做就是增广路算法优化的极限么？答案永远是不。人们在Dinic中只类比了预流推进的标号技术，而重标号操作却没有发挥得淋漓尽致。
	 * 于是人们在Dinic的基础上重新引入了重标号的概念
	 * ，使得算法无须在每次增广后再进行BFS每个顶点进行距离标号，这种主动标号技术使得修正后算法的速度有了不少提高
	 * 。但这点提高是不足称道的，人们又发现当某个标号的值没有对应的顶点后
	 * ，即增广路被截断了，于是算法便可以提前结束，这种启发式的优化称为Gap优化。最后人们结合了连续增广
	 * ，分层图，多路增广，Gap优化，主动标号等穷凶极恶的优化，更甚者在此之上狂搞个手动递归，于是产生了增广路算法的高效算法–ISAP算法。
	 * 
	 * 　　虽然ISAP算法的理论复杂度仍然不可超越高标预流推进，但其编程复杂度已经简化到发指，如此优化，加上不逊于Dinic的速率（
	 * 在效率上手工Dinic有时甚至不如递归ISAP），我们没有不选择它的理由。
	 */
}
// 最小割的算法 Algorithm for Minimum Cut
// S Gf 中与s 连通的点集； T = V - S 。所以在问题的实现上分两步，先求得
// 最大流（在1.5 节有简要介绍）；再在得到最大流 f 后的残留网络Gf 中，从s 开始深度优先遍
// 历（DFS），所有被遍历到的点，即构成点集
// 注意，虽然最小割中的边都是满流边，但满流边不一定都是最小割中的边。在某些
// 特殊图中，很多初学者容易犯错，认为不用 DFS，就可以直接得出割。下面举一个二分图的例子。

// 【无向图的最大独立数】: 从V个顶点中选出k个顶，使得这k个顶互不相邻。 那么最大的k就是这个图的最大独立数。
// 【无向图的最大团】: 从V个顶点选出k个顶，使得这k个顶构成一个完全图，即该子图任意两个顶都有直接的边。
// 【最小路径覆盖(原图不一定是二分图，但必须是有向图，拆点构造二分图)】：在图中找一些路径，使之覆盖了图中的所有顶点，且任何一个顶点有且只有一条路径与之关联。最小路径覆盖
// = |V| - 最大匹配数
// 【最小边覆盖(原图是二分图)】：在图中找一些边，使之覆盖了图中所有顶点，且任何一个顶点有且只有一条边与之关联。最小边覆盖 = 最大独立集 = |V| -
// 最大匹配数
// 【最小顶点覆盖】：用最少的点（左右两边集合的点）让每条边都至少和其中一个点关联。
//
// PS: 原来学二分匹配时就整理过这些数字，他们之间关系是很多。如: 最小覆盖数+最大独立数 = 顶点数。
// 虽然求出他们都是np问题。但对于特殊的图还是有好的算法的，如:
// 在二分图中，最小覆盖数 等于 最大匹配数，而最大独立数又等于顶点数减去最小覆盖数(=最大匹配数)，所以可以利用匈牙利求出最大独立数等等。
// a.点覆盖集：无向图G的一个点集，使得该图中所有边都至少有一点端点在该集合内。
// b.点独立集：无向图G的一个点集，使得任两个在该集合中的点在原图中不相邻。
// c.最小点覆盖集：无向图G中点数最少的点覆盖集
// d.最大点独立集：无向图G中，点数最多的点独立集
// e.最小点权覆盖集：带点权的无向图中，点权之和最小的点覆盖集
// f.最大点权独立集：实在带点权无向图中，点权之和最大的点独立集
// 性质：
// 最大团 = 补图的最大独立集
// 最小边覆盖 = 二分图最大独立集 = |V| - 最小路径覆盖
// 最小路径覆盖 = |V| - 最大匹配数
// 最小顶点覆盖 = 最大匹配数
// 最小顶点覆盖 + 最大独立数 = |V|
// 最小割 = 最小点权覆盖集 = 点权和 - 最大点权独立集
// 覆盖集与独立集互补，但也可能是相同的
// 例 1-3
// 如 | |
// 如2-4 中1,4既是覆盖集，也是独立集