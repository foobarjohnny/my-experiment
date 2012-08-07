package graph;

//1. �㷨����"���������"ʱ���µĲ�����ͨ�����ϵ�����, ������֮�����ıߵ�Ȩֵ��, �����б߶��ǵ�λȨֵʱ�����ۼƶ�.
//2. "������A�������Ϊs��t", ���϶�s�����ֽ���, һ����t֮ǰһ���ӽ�ȥ�ĵ�, ����t��ǰ���ڵ�, Ҳ�������ѡ��������ߵ���һ��. �����ǵ�һ��!
//3. ���ڳ���ͼ, ��������, ���ö�, ӳ����ֶ�, ����STL�����ȶ��ж���TLE, ����������ʵʵO(n^3).

//��С�� Stoer-Wagner �㷨 
//Etrnls 2007-4-15 
//Stoer-Wagner �㷨����������ͼ G=(V, E)��ȫ����С� 
//
//�㷨��������һ��������������s, t   V �� ��ȫ����С����ߵ���ԭͼ��s-t ��С����ߵ��ڽ�ԭͼ���� Contract(s, 
//t)�������õ�ͼ��ȫ����С� 
//
//�㷨��ܣ� 
//1. �赱ǰ�ҵ�����С��MinCut Ϊ+��  
//2. �� G��������� s-t ��С�� c��MinCut = min(MinCut, c)   
//3. �� G�� Contract(s, t)�������õ� G'=(V', E')����|V'| > 1����G=G'��ת 2������MinCut Ϊԭͼ��ȫ����
//С��  
//
//Contract �������壺 
//�������ڱ�(p, q)�������(p, q)Ȩֵw(p, q) = 0 
//Contract(a, b): ɾ���� a, b ����(a, b)�������½ڵ� c���������� v  V �� ��w(v, c) = w(c, v) = w(a, v) + w(b, 
//v) 
//
//�� G=(V, E)������ s-t ��С����㷨�� 
//����w(A, x) = ��w(v[i], x)��v[i] ��   A 
//���� Ax Ϊ��x ǰ���� A �����е�ļ��ϣ������� x��  
//1. ��� A={a}��aΪ V�������  
//2. ѡȡ V - A�е� w(A, x)���ĵ� x���뼯�� A  
//3. ��|A|=|V|������ 
//����ڶ������� A�ĵ�Ϊ s�����һ������ A�ĵ�Ϊ t����s-t ��С��Ϊ w(At, t)

//http://acm.nudt.edu.cn/~twcourse/Cut.html
//��ϸ��PPT��PDF
//���PPT �Լ����� ÿ�����һ�� ���������s-t��С��,Ȼ���ٰ�s,t�ϲ���һ����,�ú�contract����,�ٵ�������С�ĵ������s-t��,�Ƚ���С��ֵ
public class MinCut
{
	public static int		edgeCnt;
	public static int		vertexCnt	= edgeCnt = 0;	// ����������

	public static int		MAXN		= 10;
	public static boolean[]	visit;
	public static int[]		weight;
	public static boolean[]	combine;
	public static int[][]	map;

	public static int		s, t, minCut;
	public static int		S, T;						// Global S,T

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		edgeCnt = 4;
		vertexCnt = 4;
		combine = new boolean[vertexCnt + 1];
		map = new int[vertexCnt + 1][vertexCnt + 1];
		input();
		System.out.println("min cut:" + Stoer_Wagner());
		System.out.println("S===" + S);
		System.out.println("T===" + T);
	}

	public static void input()
	{
		addEdge(1, 2, 1);
		addEdge(2, 3, 3);
		addEdge(4, 1, 2);
		addEdge(3, 4, 1);
		// int count = 0;
		// for (int i = 1; i <= edgeCnt; i++) {
		// int a = new Random().nextInt(vertexCnt - 1) + 1;// 1-n
		// int b = new Random().nextInt(vertexCnt - 1) + 1;// 1-n
		// int weight = new Random().nextInt(vertexCnt - 1) + 1;
		// if (a != b) {
		// if (map[a][b] == 0 || map[b][a] == 0) {
		// addEdge(a, b, weight);
		// count++;
		// }
		// }
		// }
		// // ȥ�����ظ��˵�edge
		// edgeCnt = count;

	}

	public static void addEdge(int a, int b, int weight)
	{
		map[a][b] = weight;
		map[b][a] = weight;
	}

	/* pku 2914 */

	// O(e + vlog(v)) E���ߵ�InCreaseKey,V�ε�ExtraMax ��Fibo
	//
	public static void maximumAdjacencySearch()
	{
		int i, j, x = 0;
		visit = new boolean[vertexCnt + 1];
		weight = new int[vertexCnt + 1];
		s = t = -1;
		for (i = 1; i <= vertexCnt; i++) {
			int MAX = Integer.MIN_VALUE;
			// �ҳ�һ����δ����A���С���w(A, x)����x�c��
			for (j = 1; j <= vertexCnt; j++) {
				if (!combine[j] && !visit[j] && weight[j] > MAX) {
					x = j;
					MAX = weight[j];
				}
			}
			if (t == x) {
				System.out.println("s===" + s);
				System.out.println("t===" + t);
				System.out.println("minCut===" + minCut);
				return;
			}
			// ����o�Ŀǰ��Cutλ��
			s = t;
			t = x;
			minCut = MAX;

			visit[x] = true; // ����x�㵽A����
			// ����a�c��A�����ᣬ����w(A, x)��ֵ��
			for (j = 1; j <= vertexCnt; j++) {
				if (!combine[j] && !visit[j]) {
					weight[j] += map[x][j];
				}
			}
		}
		System.out.println("s===" + s);
		System.out.println("t===" + t);
		System.out.println("minCut===" + minCut);
	}

	// O(VE + V^2lg(V))
	// V*O(e + vlog(v)) ����F�� ÿ��E���ߵ�INCREASE O(1),V�ε�EXTRACTMAX O(lgV)
	// ���������O(V*(E+V^2)) ��O(V^3) �����һ��Ƚ�
	// ���Ѿ���O(V*(Elg(E)+Vlg(V))
	public static int Stoer_Wagner()
	{
		int i, j;
		combine = new boolean[vertexCnt + 1];
		int ans = Integer.MAX_VALUE;
		for (i = 1; i <= vertexCnt - 1; i++) {
			printMap(map);
			maximumAdjacencySearch();
			if (minCut < ans) {

				T = t;
				S = s;
				ans = minCut;
			}
			if (ans == 0) {
				return 0;
			}
			// contract(s, t);
			combine[s] = true;// ��t�c��ӛ�鱻�ρ��^�ˣ�׃�ɲ����ڡ�
			// �ϲ�S��T���ǵ�Ч��,
			// �����ַ���������Global S,T ��Ϊ�ϲ��ĵ�,�����ֳܷ���,ֻ����minCut
			for (j = 1; j <= vertexCnt; j++) {
				if (!combine[j]) {
					map[t][j] += map[s][j];
					map[j][t] += map[j][s];
					// map[s][j] += map[t][j];
					// map[j][s] += map[j][t];
				}
			}

		}
		return ans;
	}

	private static void printMap(int[][] map)
	{
		for (int x = 1; x <= vertexCnt; x++) {
			for (int y = 1; y <= vertexCnt; y++) {
				System.out.print(map[x][y] + " ");
			}
			System.out.println();
		}
	}
}
