package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/** ��С��������Kruskal�㷨(������ͼΪ��) */
/** ��С����������СȨֵ�������������Ǳ���С������Ϊ�������ı߶��̶�ΪV-1 */
public class MaxFlow
{
	public static Map<Ve, Integer>	vertexMap	= new HashMap<Ve, Integer>();
	public static List<E>			EdgeQueue	= new ArrayList<E>();
	public static List<E>			EdgeList	= new ArrayList<E>();
	public static int				MAX			= Integer.MAX_VALUE;
	public static int				edgeCnt;
	public static int				vertexCnt	= edgeCnt = 0;					// ����������

	public static int				MAXN		= 10;
	public static int[][]			capacity;
	public static int[][]			flow;
	public static int[]				minCapacity;
	public static int[]				father;
	public static int				maxFlow;
	public static boolean[]			visit;
	public static int[]				level;

	// Ford-Fullkerson���� Method
	// ʹ��BFS��ʵ��Ford-Fullkerson�����е�������·�����㷨��ΪEdmonds-Karp�㷨��
	// Edmonds-Karp�㷨���������·�㷨����Ϊʵ��BFS�ҵ�������·�������п��ܵ�����·������̵�·�������ĸ��Ӷ���O(VE^2)������V�ǽ������E�����������
	// �����ʹ��DFS����BFS����Ford-Fullkerson�����˻���һ������·�㷨���临�Ӷ���O(E| f* |)������f*���㷨�ҳ����������
	// �������Edmonds-Karp�㷨 Algorithm
	public static int Edmonds_Karp(int start, int terminal)
	{
		LinkedList<Integer> queue = new LinkedList<Integer>();
		int u, v;
		maxFlow = 0;// �������ʼ��
		while (true) {
			minCapacity = new int[MAXN];// ÿ��Ѱ������·������ÿ���������������Ϊ0
			visit = new boolean[MAXN];// ���һ�����Ƿ��Ѿ�ѹ�����
			minCapacity[start] = MAX;// Դ���������Ϊ������
			queue.offer(start);// ��Դ��������
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
				// �ҵ�����·��(augmenting path)[����������S��T�ļ�·��]
				if (minCapacity[terminal] > 0) {// ����ҵ��˻�㲢�һ��������Ϊ0����ն��С�
					while (!queue.isEmpty()) {
						queue.poll();
					}
					break;
				}
			}
			if (minCapacity[terminal] == 0) {
				// ����BFS ���ܴﵽ�յ�T ���Ҳ�������·��(augmenting path)[����������S��T�ļ�·��]
				break;
			}
			for (int i = terminal; i != 1; i = father[i]) {
				capacity[father[i]][i] -= minCapacity[terminal];// �������
				capacity[i][father[i]] += minCapacity[terminal];// �������
				flow[father[i]][i] += minCapacity[terminal]; // ����FLOW��
			}
			maxFlow += minCapacity[terminal];// ���������
			// ���Maxflow ������Ϊû����
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
					if (v == t) { // �ҵ�T������ ������ͬ��εĿ��Բ��ù���
						return level[t] != 0; // ��ʵ����ֱ�ӷ���TRUE
					}
				}
			}
		}
		return level[t] != 0;
	}

	// Dinic����Edmond_Karp�ϵ��Ż������Ǵ�˵�еķֲ㣻�ֲ�����������⣬��ʵ���Ƿּ������ڵ����һ������ı�ǣ���һ��ʵ�־Ͷ��ˣ�
	// �㷨��ʵ�ֲ��裺
	// 1.�ֲ㣺����BFS������ʽ��ÿ���ڵ������level[i]��
	// 2.�жϷֲ��Ƿ�ɹ�������Ƿ񱻷ֵ�����level[sink]!=0��
	// 3.�ڷֲ�Ļ����ϼ�level[i]��Ѱ�����е�����·���ۼ��������ص�����1��
	//
	public static int Dinic(int start, int terminal)
	{
		LinkedList<Integer> queue = new LinkedList<Integer>();
		int u, v;
		maxFlow = 0;// �������ʼ��
		// ���BFSΪFALSE ˵���Ѿ�û�е�T��·����
		while (BFS(start, terminal)) {
			int[] nextV = new int[vertexCnt + 1];// ����U����һ����Ӧ�˵�V,�����ظ�����V
			for (int i = 0; i <= vertexCnt; i++) { // nextV���ʼ���ǵ�һ���ڵ�� FORѭ��
				nextV[i] = 1; // DFS�У������Ҫ���ݣ��ͻ��ݵ�nextV�еĽڵ㡣
			}
			father = new int[vertexCnt + 1];
			minCapacity = new int[vertexCnt + 1];// ÿ��Ѱ������·������ÿ���������������Ϊ0
			minCapacity[start] = MAX;// Դ���������Ϊ������
			queue.offer(start);// ��Դ��������
			// ������Ϊ��ʱ��˵���Ѿ��ص�START�� ��������BFS��
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
				// ���������ѵ��ĵ���T
				if (v == terminal) {
					for (int i = terminal; i != start; i = father[i]) {
						capacity[father[i]][i] -= minCapacity[terminal];// �������
						capacity[i][father[i]] += minCapacity[terminal];// �������
						flow[father[i]][i] += minCapacity[terminal]; // ����FLOW��
						if (capacity[father[i]][i] == 0) {
							// ��stack����ǰһ������ѹ��ĵ� ��Ϊ��û��������ѹ���� �����PRUNE
							while (queue.peek() != father[i]) {
								queue.poll();
							}
						}
					}
					// ���Maxflow==0 ������Ϊû����
					maxFlow += minCapacity[terminal];// ���������
				}
				// ˵��U��������������� ��������ΪBLACK
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
			for (i = 0; i <= vertexCnt; i++) { // cur���ʼ���ǵ�һ���ڵ��
				cur[i] = 0; // DFS�У������Ҫ���ݣ��ͻ��ݵ�cur�еĽڵ㡣
				minCapacity[i] = MAX; // a��������ʣ������
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
					u = v; // ���ҵ��Ľڵ��ʼ�ڲ��ͼ����·
					if (u == terminal) // �ҵ�t������
					{
						ag = minCapacity[terminal];
						flow += ag;
						for (v = terminal; v != start; v = father[v]) {
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
				else if (u != start) // ���u ��= s ��ô����·�߲�ͨ�Ļ�����u����һ���ڵ�����ҡ�
				{
					level[u] = MAX; // �൱�ڴӲ��ͼ��ɾ������ڵ㡣
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

}