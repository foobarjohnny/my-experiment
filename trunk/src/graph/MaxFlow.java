package graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/** ��С��������Kruskal�㷨(������ͼΪ��) */
/** ��С����������СȨֵ�������������Ǳ���С������Ϊ�������ı߶��̶�ΪV-1 */
public class MaxFlow
{
	public static Map<Ve, Integer>		vertexMap	= new HashMap<Ve, Integer>();
	public static List<E>				EdgeQueue	= new ArrayList<E>();
	public static List<E>				EdgeList	= new ArrayList<E>();
	public static int					MAX			= Integer.MAX_VALUE;
	public static int					edgeCnt;
	public static int					vertexCnt	= edgeCnt = 0;					// ����������

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

	// Ford-Fullkerson���� Method
	// ʹ��BFS��ʵ��Ford-Fullkerson�����е�������·�����㷨��ΪEdmonds-Karp�㷨��
	// Edmonds-Karp�㷨���������·�㷨����Ϊʵ��BFS�ҵ�������·�������п��ܵ�����·������̵�·�������ĸ��Ӷ���O(VE^2)������V�ǽ������E�����������
	// �����ʹ��DFS����BFS����Ford-Fullkerson�����˻���һ������·�㷨���临�Ӷ���O(E| f* |)������f*���㷨�ҳ����������
	// �������Edmonds-Karp�㷨 Algorithm
	// S: Start T: Terminal
	public static int Edmonds_Karp(int S, int T)
	{
		LinkedList<Integer> queue = new LinkedList<Integer>();
		int u, v;
		maxFlow = 0;// �������ʼ��
		while (true) {
			minCapacity = new int[vertexCnt + 1];// ÿ��Ѱ������·������ÿ���������������Ϊ0
			visit = new boolean[vertexCnt + 1];// ���һ�����Ƿ��Ѿ�ѹ�����
			minCapacity[S] = MAX;// Դ���������Ϊ������
			queue.offer(S);// ��Դ��������
			while (!queue.isEmpty()) { // �����в�Ϊ��
				u = queue.peek();
				queue.poll();
				for (v = 1; v <= vertexCnt; v++) {
					if (!visit[v] && capacity[u][v] > 0) {
						visit[v] = true;
						father[v] = u;// ��¼�����ĸ��׷�����������������
						queue.offer(v);
						// ��ǰ�������Ϊ���׵�������������Ľ�С��
						// һ�������·�� һ�߸�������·���ϵ���С��������(min residual capacity)
						// ͨ�����鴫��MIN CAPACITY NICE
						minCapacity[v] = (minCapacity[u] < capacity[u][v] ? minCapacity[u] : capacity[u][v]);
					}
				}
				// �ҵ�����·��(augmenting father)[����������S��T�ļ�·��]
				if (minCapacity[T] > 0) {// ����ҵ��˻�㲢�һ��������Ϊ0����ն��С�
					while (!queue.isEmpty()) {
						queue.poll();
					}
					break;
				}
			}
			if (minCapacity[T] == 0) {
				// ����BFS ���ܴﵽ�յ�T ���Ҳ�������·��(augmenting father)[����������S��T�ļ�·��]
				break;
			}
			int aug = minCapacity[T];
			for (int i = T; i != S; i = father[i]) {
				capacity[father[i]][i] -= aug;// �������
				capacity[i][father[i]] += aug;// �������
				flow[father[i]][i] += aug; // ����FLOW��
				minCapacity[i] -= aug;
			}
			maxFlow += aug;// ���������
			// ���Maxflow ������Ϊû����
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
					if (v == t) { // �ҵ�T������ ������ͬ��εĿ��Բ��ù���
						return H[t] != 0; // ��ʵ����ֱ�ӷ���TRUE
					}
				}
			}
		}
		return H[t] != 0;
	}

	// Dinic����Edmond_Karp�ϵ��Ż������Ǵ�˵�еķֲ㣻�ֲ�����������⣬��ʵ���Ƿּ������ڵ����һ������ı�ǣ���һ��ʵ�־Ͷ��ˣ�
	// �㷨��ʵ�ֲ��裺
	// 1.�ֲ㣺����BFS������ʽ��ÿ���ڵ������H[i]��
	// 2.�жϷֲ��Ƿ�ɹ�������Ƿ񱻷ֵ�����H[sink]!=0��
	// 3.�ڷֲ�Ļ����ϼ�H[i]��Ѱ�����е�����·���ۼ��������ص�����1��
	// No-Recursive
	// O(V^2E)
	public static int Dinic(int S, int T)
	{
		LinkedList<Integer> queue = new LinkedList<Integer>();
		int u, v;
		maxFlow = 0;// �������ʼ��
		// ���BFSΪFALSE ˵���Ѿ�û�е�T��·����
		while (BFS4Dinic(S, T)) {
			nextV = new int[vertexCnt + 1];// ����U����һ����Ӧ�˵�V,�����ظ�����V
			for (int i = 0; i <= vertexCnt; i++) { // nextV���ʼ���ǵ�һ���ڵ�� FORѭ��
				nextV[i] = 1; // DFS�У������Ҫ���ݣ��ͻ��ݵ�nextV�еĽڵ㡣
			}
			father = new int[vertexCnt + 1];
			minCapacity = new int[vertexCnt + 1];// ÿ��Ѱ������·������ÿ���������������Ϊ0
			minCapacity[S] = MAX;// Դ���������Ϊ������
			queue.offer(S);// ��Դ��������
			// ������Ϊ��ʱ��˵���Ѿ��ص�S�� ��������BFS��
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
				// ���������ѵ��ĵ���T
				if (v == T) {
					int aug = minCapacity[T];
					for (int i = T; i != S; i = father[i]) {
						capacity[father[i]][i] -= aug;// �������
						capacity[i][father[i]] += aug;// �������
						flow[father[i]][i] += aug; // ����FLOW��
						minCapacity[i] -= aug; // ��S��ԶΪMAX
						if (capacity[father[i]][i] == 0) {
							// ��stack����ǰһ������ѹ��ĵ� ��Ϊ��û��������ѹ���� �����PRUNE
							// ���u=SҲ�� �������� ��֦ �����Ż�������·���� ��������
							// ���u=Sʱ ����minCapacity��һ��aug��������ȫ
							while (queue.peek() != father[i]) {
								queue.poll();
							}
						}
					}
					// ���Maxflow==0 ������Ϊû����
					maxFlow += aug;// ���������
				}
				// ˵��U��������������� ��������ΪBLACK
				// ��ʵ����Ҳû�й�ϵ �������ظ�
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
		maxFlow = 0;// �������ʼ��
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
				father[v] = u;
				capacity[father[v]][v] -= branchFlow;// �������
				capacity[v][father[v]] += branchFlow;
				flow[father[v]][v] += branchFlow; // ����FLOW��
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

	private static void buildCap()
	{
		// gap ���vertexCnt*2����Ϊ�п�����V+1�������߶ȣ�ԭ�����߶���V V+V-1=2V-1
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
			for (i = 0; i <= vertexCnt; i++) { // cur���ʼ���ǵ�һ���ڵ��
				cur[i] = 0; // DFS�У������Ҫ���ݣ��ͻ��ݵ�cur�еĽڵ㡣
				minCapacity[i] = MAX; // a��������ʣ������
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
					u = v; // ���ҵ��Ľڵ��ʼ�ڲ��ͼ����·
					if (u == T) // �ҵ�t������
					{
						ag = minCapacity[T];
						flow += ag;
						for (v = T; v != S; v = father[v]) {
							cur[father[v]] = v; // �˻���һ������ �о��������
							capacity[father[v]][v] -= ag;
							capacity[v][father[v]] += ag;
							minCapacity[v] -= ag;
							if (capacity[father[v]][v] == 0) {
								u = father[v];
							}
						}
					}
				}
				else if (u != S) // ���u ��= s ��ô����·�߲�ͨ�Ļ�����u����һ���ڵ�����ҡ�
				{
					H[u] = MAX; // �൱�ڴӲ��ͼ��ɾ������ڵ㡣
					u = father[u];
				}
				else {
					// �����Դ���Ҳ�������·���ͽ����֡�
					break;
				}
			}
		}
		return flow;
	}

	// Improved Shortest Augmenting Path
	// 1. Gap �Ż� 2. ������ż���
	// ISAP��DINIC ԭ����һ���� �������Preflow��ı��
	// ISAP�Ƕ�̬�ı��ţ���������ţ�DINIC�ǵ���BFS���ؽ���ţ����������
	// ISAP�ﻹ������GAP �Ż�����DINIC�ﶼ�������Ż� ��ϸ�ɼ����������
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
					// u=v ��DINIC���������� ��Ϊ�����Լ���BFS���ɵı������������VERTEXCNT��
					// ���Լ�ѭ���ؽ���DINIC�ǳ�ȥ�ؽ�BFS
					u = v;
					break;
				}
			}
			// ���������ѵ��ĵ���T���ҵ�������·��
			if (v == T) {
				// ���u=Sʱ ���� minCapacity��һ��aug��������ȫ
				int aug = minCapacity[T];
				for (int i = T; i != S; i = father[i]) {
					capacity[father[i]][i] -= aug;// �������
					capacity[i][father[i]] += aug;// �������
					flow[father[i]][i] += aug; // ����FLOW��
					minCapacity[i] -= aug;
					// ���u=SҲ�� �������� ��֦ �����Ż�������·���� ��������
					if (capacity[father[i]][i] == 0) {
						u = father[i];
					}
				}
				maxFlow += aug;// ���������

			}
			// U��û�����ˣ������� dis ����Level
			else if (v > vertexCnt) {
				int minLev = vertexCnt;
				for (v = 1; v <= vertexCnt; v++) {
					if (capacity[u][v] > 0 && H[v] < minLev) {
						minLev = H[v];
					}
				}
				// Gap �Ż���������ֶϲ㣬ֱ���ж�
				if ((--gap[H[u]]) == 0) {
					break;
				}
				gap[H[u] = minLev + 1]++;
				// U�����±���
				nextV[u] = 1;
				// �����Ÿı�󣬴Ӹ��������������S������
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
					v = nextV[u] - 1;// Ϊʲô��1��?
				}
			}
			// �����ҵ�U���
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
				// �����S,T,��Ϊ�����ﲻ���������
				// ��������T ��ȫΪ0
				if (u == S || u == T) {
					continue;
				}
				if (E[u] > 0) {
					// ���԰�IF�ĳ�WHILE����û�иģ���Ϊ������GENERIC-PUSH-FLOWѰ��PUSH��RELABEL����Ϊ�����������ѭ����
					// ������רע��һ�㣬רע��һ������������ų��� ������е��Ż���������
					done = false;
					// �ȼ��趥��u��Ҫrelabel
					needRelabel = true;
					for (v = 1; v <= vertexCnt; v++) { /* �����push�Ķ��� */
						if (capacity[u][v] > 0) {
							// 3 cases exist: E[u]>0 && Capacity[u][v]>0 &&
							// H[u]<=H[v]+1
							// �߶Ⱥ�������
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
					// û�п���push�Ķ���,ִ��relabel
					if (needRelabel) {
						// relabel
						relabel(u);
					}
				}
			}
			if (done) { /* ��Դ��ͻ����,ÿ�������e[i]��Ϊ0 */
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
			// �����S,T,��Ϊ�����ﲻ���������
			// ��������T ��ȫΪ0
			if (u == S || u == T) {
				continue;
			}
			list.offer(u);
		}
		for (Iterator<Integer> it = list.iterator(); it.hasNext();) {
			u = it.next();
			oldHeight = H[u];
			// ��U������EXCESS����
			Discharge(u);
			if (H[u] > oldHeight) {
				list.remove(new Integer(u));
				list.push(u);
				it = list.listIterator(1);// index Ϊ1 �����ǵڶ���Ԫ��
			}
		}
		/* ��Դ��ͻ����,ÿ�������e[i]��Ϊ0 */
		maxFlow = 0;
		for (u = 1; u <= vertexCnt; u++) {
			if (flow[u][T] > 0) {
				maxFlow += flow[u][T];
			}
		}
		// E[T] or -E[S] �����E[S]��E[T]���0,��������,
		return maxFlow;
	}

	// O(V^3)
	// FIFO preflow-push algorithm ��Relabel_To_Front���Ż� FIFO HEURISTIC
	// ������㷨��Q�Ǳ�׼��FIFO���У���ʱ�临�Ӷ�Ϊ(V^2E)��
	// ��������ȶ��У����ұ����ߵĵ����ȵĻ������Ǿ͵õ�����߱��Ԥ���ƽ��㷨����ʱ�临�ӶȽ�Ϊ(V^2sqrt(E))�����ǱȽϿ��������㷨�ˡ�
	public static int FIFO_Preflow_Push(int S, int T)
	{
		int u, pushFlow;
		LinkedList<Integer> queue = new LinkedList<Integer>();
		// Initialize the pre-flow
		// initPreflow(S); ����û��INIT
		queue.push(S);
		H[S] = vertexCnt;
		E[S] = MAX;
		// E[T] = -MAX;
		while (!queue.isEmpty()) {
			u = queue.peek();
			queue.pop();
			// û�� while(E[u]>0)�ǿ��ǵ���S���������WHILE��ѭ����Ч����ʵ��һ����
			// ��WHILE(E[U]>0),�������DISCHARGE

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
	// Relabel-to-Front�㷨��ʱ�临�Ӷ���O(V^3)������һ����Highest Label Preflow
	// Push���㷨���ӶȾ�˵��O(V^2*E^0.5)�����о���һ��HLPP���о�����Relabel-to-Front������û��������Ϊ
	// Relabel-to-Frontÿ��ǰ�ƵĶ��Ǹ߶���ߵĶ��㣬����Ҳ�൱��ÿ��ѡ����ߵı�Ž��и��¡�����һ���о�Ҳ��ܺ�ʵ�ֵ��㷨��ʹ�ö���ά��������㣬ÿ�ζ�pop�����Ķ���discharge��������
	// �µ��������ʱ��ӡ�
	// Push-Relabel����㷨��һ����Ϊgap heuristic���Ż������ǵ�����һ������0<k<2V-1��
	// û���κζ�������h[v]=kʱ��������h[v]>k�Ķ���v�����£�����С��2V-1����Ϊ2V-1,��С�߶�Ϊ1������0ʱδ��߶�
	public static int HLPP(int S, int T)
	{
		int u, pushFlow;
		// Initialize the pre-flow
		// initPreflow(S); ����û��INIT
		// �ֲ�
		BFS4HLPP(S, T);
		// ��S���⻯һ��
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
			// û�� while(E[u]>0)�ǿ��ǵ���S���������WHILE��ѭ����Ч����ʵ��һ����
			// ��WHILE(E[U]>0),�������DISCHARGE
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
				// Gap heuristic �Ż�
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
			// �ȼ��趥��u��Ҫrelabel
			needRelabel = true;
			for (int v = 1; v <= vertexCnt; v++) { /* �����push�Ķ��� */
				if (capacity[u][v] > 0) {
					// 2 cases exist:
					// E[u]>0 && Capacity[u][v]>0 && H[u]<=H[v]+1
					// �߶Ⱥ�������
					if (H[u] == H[v] + 1) {
						// push
						needRelabel = false;
						push(u, v);
					}
				}
			}
			// û�п���push�Ķ���,ִ��relabel
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
		// flow[v][u] -=d; ������ʵ���Ա�֤��
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
						// father[i] == -1 ��֤�˵�һ���ߺ����һ����Ϊδƥ���
						// dfs(father[i])ʵ���˽���·��;
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
			// ÿ�ҵ�һ�����㾶 ���ƥ���1 M+1
			if (dfs(i)) {
				maxMatch++;
			}
		}
		// �Ѿ���X,Y�������� DOUBLE�� ����Ҫ����2
		return maxMatch / 2;
	}

	/**
	 * ������ISAP�㷨�ļ򵥽���
	 * 
	 * http://blog.csdn.net/nomad2/article/details/7422527
	 * 
	 * �⼸����������ԭ�򾭳��Ӵ�������������Ŀ����һ���͵�����˵ĸо�������Ҫ�ǳ�ʹ����YY���ܳ�����Ƚ�������ģ�͡�
	 * �����ǿ���Amber��С��Ӧ�õ����£� �������Ŀ˼·���ǳ��������಻����YD˼��
	 * ��Ȼ�������У�����YD������һ��������ò����Ƚ϶��ʱ��ȥ�����ڴ���ϸ�ڵ�ʵ�֣����߳��Ĵ��������ʹ�ô���ĵ����Ե��쳣����
	 * �����һЩ�����̸�Ч���������㷨�ڴ�ʱ���Ե���Ϊ��Ҫ��
	 * ���������������̵��������ԱȽ����е��������㷨��һ�����ܽᣬ����֮�����ǿ���Ƽ�һ��Ч�����̸��Ӷ�����Ӧ���㷨��
	 * 
	 * ����������֪���������������������2���Ȼ��ͬ�����˼�룬���ǱȽ�������Ԥ���ƽ�������·�����߶���Ҫ����ߵ�С���ɡ�
	 * 
	 * ��������Ԥ���ƽ����㷨˼�����Ա�Ϊ��Ԫ��������������������������:�ó�ʼ���ڽӱ���������һ�η���bfs��ÿ�������㷴������ţ�
	 * ����������������ڳ����Ľ��Ϊ����
	 * ��ÿ�ζԻ��㰴����ߣ�u->v:d[u]=d[v]+1����������������ֱ���޷��������߸õ����Ϊ0����u���ʱ��Ϊ����
	 * ��������ر�ţ�ʹ֮����ԭͼ�н����Ʋ�������ڽӽ�����С���
	 * +1������u����ӡ�������Ϊ��ʱ���㷨������ֻ��s���t�������0�������и������޴������޷��ҵ�����·�������㣬��t�����Ϊ�������
	 * 
	 * ����������·��˼������ÿ�δ�Դ��������һ��ǰ����������·�����ı�·�ϵı�Ȩ��ֱ���޷��ٽ������㣬��ʱ������������Ϊ�������
	 * �����������ۻ�����Ȼ������·����
	 * ���������۸��Ӷ���Ԥ���ƽ�Ҫ�ԵñȽ����㡣���е�HLPP�߱�Ԥ���ƽ������۸��Ӷ��Ѿ��ﵽ�����˷�ָ��O��sqrt(m)*n
	 * *n�����������̸��Ӷ�Ҳ��ͬ�������˷�ָ- -
	 * 
	 * �������������ܷ��ڱ�̸��ӶȺ��㷨���Ӷ����ҵ�һ��ƽ���أ����ǿ϶��ġ�����ʹ������·��˼�룬���ұ�������Ż�����Ϊԭʼ������·�㷨������EK��
	 * �Ƿǳ�����ġ���������ע�⵽��Ԥ���ƽ��еı�ŷ���������·�㷨�������������ÿ�η��Ѳ�������õ�����ţ����������������õݹ�����������㣬
	 * ���ǲ����˻��ڷֲ�ͼ��Dinic�㷨
	 * ��һЩ�˸��������ڳ���Dinic�����������������������˶�·��������ĸ������ͬһ������������ֶ�·ͬʱ�ƽ����ټ��ϱȽϸ��ӵ��ֹ��ݹ�
	 * ��ʹ��Dinic�Ѿ�����󲿷���Ŀ����Ҫ��
	 * 
	 * ����Ȼ����������������·�㷨�Ż��ļ���ô������Զ�ǲ���������Dinic��ֻ�����Ԥ���ƽ��ı�ż��������ر�Ų���ȴû�з��ӵ����쾡�¡�
	 * ����������Dinic�Ļ����������������ر�ŵĸ���
	 * ��ʹ���㷨������ÿ��������ٽ���BFSÿ��������о����ţ�����������ż���ʹ���������㷨���ٶ����˲������
	 * �����������ǲ���Ƶ��ģ������ַ��ֵ�ĳ����ŵ�ֵû�ж�Ӧ�Ķ����
	 * ��������·���ض��ˣ������㷨�������ǰ��������������ʽ���Ż���ΪGap�Ż���������ǽ������������
	 * ���ֲ�ͼ����·���㣬Gap�Ż���������ŵ����׼�����Ż����������ڴ�֮�Ͽ����ֶ��ݹ飬���ǲ���������·�㷨�ĸ�Ч�㷨�CISAP�㷨��
	 * 
	 * ������ȻISAP�㷨�����۸��Ӷ���Ȼ���ɳ�Խ�߱�Ԥ���ƽ��������̸��Ӷ��Ѿ��򻯵���ָ������Ż������ϲ�ѷ��Dinic�����ʣ�
	 * ��Ч�����ֹ�Dinic��ʱ��������ݹ�ISAP��������û�в�ѡ���������ɡ�
	 */
}
// ��С����㷨 Algorithm for Minimum Cut
// S Gf ����s ��ͨ�ĵ㼯�� T = V - S �������������ʵ���Ϸ������������
// ���������1.5 ���м�Ҫ���ܣ������ڵõ������ f ��Ĳ�������Gf �У���s ��ʼ������ȱ�
// ����DFS�������б��������ĵ㣬�����ɵ㼯
// ע�⣬��Ȼ��С���еı߶��������ߣ��������߲�һ��������С���еıߡ���ĳЩ
// ����ͼ�У��ܶ��ѧ�����׷�����Ϊ���� DFS���Ϳ���ֱ�ӵó�������һ������ͼ�����ӡ�

// ������ͼ������������: ��V��������ѡ��k������ʹ����k�����������ڡ� ��ô����k�������ͼ������������
// ������ͼ������š�: ��V������ѡ��k������ʹ����k��������һ����ȫͼ��������ͼ��������������ֱ�ӵıߡ�
// ����С·������(ԭͼ��һ���Ƕ���ͼ��������������ͼ����㹹�����ͼ)������ͼ����һЩ·����ʹ֮������ͼ�е����ж��㣬���κ�һ����������ֻ��һ��·����֮��������С·������
// = |V| - ���ƥ����
// ����С�߸���(ԭͼ�Ƕ���ͼ)������ͼ����һЩ�ߣ�ʹ֮������ͼ�����ж��㣬���κ�һ����������ֻ��һ������֮��������С�߸��� = �������� = |V| -
// ���ƥ����
// ����С���㸲�ǡ��������ٵĵ㣨�������߼��ϵĵ㣩��ÿ���߶����ٺ�����һ���������
//
// PS: ԭ��ѧ����ƥ��ʱ���������Щ���֣�����֮���ϵ�Ǻܶࡣ��: ��С������+�������� = ��������
// ��Ȼ������Ƕ���np���⡣�����������ͼ�����кõ��㷨�ģ���:
// �ڶ���ͼ�У���С������ ���� ���ƥ�����������������ֵ��ڶ�������ȥ��С������(=���ƥ����)�����Կ�������������������������ȵȡ�
// a.�㸲�Ǽ�������ͼG��һ���㼯��ʹ�ø�ͼ�����б߶�������һ��˵��ڸü����ڡ�
// b.�������������ͼG��һ���㼯��ʹ���������ڸü����еĵ���ԭͼ�в����ڡ�
// c.��С�㸲�Ǽ�������ͼG�е������ٵĵ㸲�Ǽ�
// d.���������������ͼG�У��������ĵ������
// e.��С��Ȩ���Ǽ�������Ȩ������ͼ�У���Ȩ֮����С�ĵ㸲�Ǽ�
// f.����Ȩ��������ʵ�ڴ���Ȩ����ͼ�У���Ȩ֮�����ĵ������
// ���ʣ�
// ����� = ��ͼ����������
// ��С�߸��� = ����ͼ�������� = |V| - ��С·������
// ��С·������ = |V| - ���ƥ����
// ��С���㸲�� = ���ƥ����
// ��С���㸲�� + �������� = |V|
// ��С�� = ��С��Ȩ���Ǽ� = ��Ȩ�� - ����Ȩ������
// ���Ǽ����������������Ҳ��������ͬ��
// �� 1-3
// �� | |
// ��2-4 ��1,4���Ǹ��Ǽ���Ҳ�Ƕ�����