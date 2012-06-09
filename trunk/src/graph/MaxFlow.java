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
	public static int[]				gap;
	public static int[]				nextV;

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
			minCapacity = new int[MAXN];// ÿ��Ѱ������·������ÿ���������������Ϊ0
			visit = new boolean[MAXN];// ���һ�����Ƿ��Ѿ�ѹ�����
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
				// �ҵ�����·��(augmenting path)[����������S��T�ļ�·��]
				if (minCapacity[T] > 0) {// ����ҵ��˻�㲢�һ��������Ϊ0����ն��С�
					while (!queue.isEmpty()) {
						queue.poll();
					}
					break;
				}
			}
			if (minCapacity[T] == 0) {
				// ����BFS ���ܴﵽ�յ�T ���Ҳ�������·��(augmenting path)[����������S��T�ļ�·��]
				break;
			}
			for (int i = T; i != 1; i = father[i]) {
				capacity[father[i]][i] -= minCapacity[T];// �������
				capacity[i][father[i]] += minCapacity[T];// �������
				flow[father[i]][i] += minCapacity[T]; // ����FLOW��
			}
			maxFlow += minCapacity[T];// ���������
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
		buildCap();
		System.out.println(ISAP2(1, 4));
	}

	public static boolean BFS4Dinic(int s, int t)
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
	// No-Recursive
	public static int Dinic(int S, int T)
	{
		LinkedList<Integer> queue = new LinkedList<Integer>();
		int u, v;
		maxFlow = 0;// �������ʼ��
		// ���BFSΪFALSE ˵���Ѿ�û�е�T��·����
		while (BFS4Dinic(S, T)) {
			int[] nextV = new int[vertexCnt + 1];// ����U����һ����Ӧ�˵�V,�����ظ�����V
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
					if ((capacity[u][v] > 0) && (level[u] + 1 == level[v])) {
						father[v] = u;
						queue.push(v);
						minCapacity[v] = (minCapacity[u] < capacity[u][v] ? minCapacity[u] : capacity[u][v]);
						nextV[u] = v + 1;
						break;
					}
				}
				// ���������ѵ��ĵ���T
				if (v == T) {
					for (int i = T; i != S; i = father[i]) {
						capacity[father[i]][i] -= minCapacity[T];// �������
						capacity[i][father[i]] += minCapacity[T];// �������
						flow[father[i]][i] += minCapacity[T]; // ����FLOW��
						if (capacity[father[i]][i] == 0) {
							// ��stack����ǰһ������ѹ��ĵ� ��Ϊ��û��������ѹ���� �����PRUNE
							//���u=SҲ�� �������� ��֦  �����Ż�������·���� �������� 
							//���u=Sʱ ����˵Ӧ������minCapacity����,��������Ҳʵ��û���� ����Ϊʲô
							//����minCapacity��һ��aug��������ȫ
							while (queue.peek() != father[i]) {
								queue.poll();
							}
						}
					}
					// ���Maxflow==0 ������Ϊû����
					maxFlow += minCapacity[T];// ���������
				}
				// ˵��U��������������� ��������ΪBLACK
				// ��ʵ����Ҳû�й�ϵ �������ظ�
				else if (v > vertexCnt) {
					level[u] = MAX;
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
			if ((capacity[u][v] > 0) && (level[u] + 1 == level[v])) {
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
		level[u] = MAX;
		return out;
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
		gap = new int[vertexCnt + 2];
		nextV = new int[vertexCnt + 1];
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

	// Improved Shortest Augmenting Path
	// 1. Gap �Ż� 2. ������ż���
	// ISAP��DINIC ԭ����һ���� �������Preflow��ı�� ISAP�Ƕ�̬�ı��ţ���������ţ�DINIC�ǵ���BFS���ؽ���ţ����������
	// ISAP�ﻹ������GAP �Ż�����DINIC�ﶼ�������Ż� ��ϸ�ɼ����������
	public static int ISAP(int S, int T)
	{
		maxFlow = 0;
		for (int i = 0; i <= vertexCnt; i++) {
			level[i] = gap[i] = 0;
			nextV[i] = 1;
		}
		int u = S, v;
		gap[0] = vertexCnt;
		minCapacity[S] = MAX;
		while (level[S] <= vertexCnt) {
			for (v = nextV[u]; v <= vertexCnt; v++) {
				if ((capacity[u][v] > 0) && (level[v] + 1 == level[u])) {
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
				for (int i = T; i != S; i = father[i]) {
					capacity[father[i]][i] -= minCapacity[T];// �������
					capacity[i][father[i]] += minCapacity[T];// �������
					flow[father[i]][i] += minCapacity[T]; // ����FLOW��
				}
				maxFlow += minCapacity[T];// ���������
				//���u=SҲ�� �������� ��֦  �����Ż�������·���� �������� 
				//���u=Sʱ ����˵Ӧ������minCapacity����,��������Ҳʵ��û���� ����Ϊʲô
				//����minCapacity��һ��aug��������ȫ
				if (capacity[father[v]][v] == 0) {
					u = father[v];
				}
			}
			// U��û�����ˣ������� dis ����Level
			else if (v > vertexCnt) {
				int minLev = vertexCnt;
				for (v = 1; v <= vertexCnt; v++) {
					if (capacity[u][v] > 0 && level[v] < minLev) {
						minLev = level[v];
					}
				}
				// Gap �Ż���������ֶϲ㣬ֱ���ж�
				if ((--gap[level[u]]) == 0) {
					break;
				}
				gap[level[u] = minLev + 1]++;
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
		int v, maxflow = 0;
		int[] cur = new int[vertexCnt + 1];
		for (int i = 0; i <= vertexCnt; i++) {
			level[i] = gap[i] = 0;
			cur[i] = 1;
		}
		int u = s, aug = MAX;
		gap[0] = vertexCnt;

		while (level[s] <= vertexCnt) {
			for (v = cur[u]; v <= vertexCnt; v++) {
				if (capacity[u][v] > 0 && level[v] + 1 == level[u]) {
					aug = Math.min(aug, capacity[u][v]);
					father[v] = u;
					cur[u] = v;
					u = v;
					if (v == t) {
						maxflow += aug;
						while (u != s) {
							capacity[father[u]][u] -= aug;
							capacity[u][father[u]] += aug;
							u = father[u];
						}
						father = new int[vertexCnt + 1];
						v = cur[u] - 1;
						continue;
					}
				}
			}
			// �����ҵ�U���
			int mindis = vertexCnt;
			for (v = 1; v <= vertexCnt; v++) {
				if (capacity[u][v] > 0 && level[v] < mindis) {
					mindis = level[v];
				}
			}
			if ((--gap[level[u]]) == 0) {
				break;
			}
			gap[level[u] = mindis + 1]++;
			cur[u] = 1;
			if (u != s) {
				u = father[u];
			}
		}
		return maxflow;
	}
}
/**
 * ������ISAP�㷨�ļ򵥽���
 * 
 * http://blog.csdn.net/nomad2/article/details/7422527
 * 
 * �⼸����������ԭ�򾭳��Ӵ�������������Ŀ����һ���͵�����˵ĸо�������Ҫ�ǳ�ʹ����YY���ܳ�����Ƚ�������ģ�͡������ǿ���Amber��С��Ӧ�õ����£�
 * �������Ŀ˼·���ǳ��������಻����YD˼��
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
