package graph;

import java.util.ArrayList;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
//��������С�и����������
//1.min=MAXINT,ȷ��һ��Դ�� Ϊʲô��ö��Դ�����أ�����
//2.ö�ٻ��
//3.�������������ȷ����ǰԴ�����С�������minС����min
//4.ת��2ֱ��ö�����
//5.min��Ϊ�������min
//    ���ѿ������ӶȺܸߣ�ö�ٻ��ҪO(n)���������·������㷨���������O((n^2)m)���Ӷȣ��ڸ���������O(m)=O(n^2)���㷨�ܸ��ӶȾ���O(n^5)�����²�����߱��Ԥ�����㷨�������O((n^2)(m^0.5))���㷨�ܸ��Ӷ�ҲҪO(n^4)
//    �������������㷨�����С����ӶȲ������O(n^4)��
    
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
//����w(A, x) = ��w(v[i], x)��v[i]  A ��  
//���� Ax Ϊ��x ǰ���� A �����е�ļ��ϣ������� x��  
//1. ��� A={a}��aΪ V�������  
//2. ѡȡ V - A�е� w(A, x)���ĵ� x���뼯�� A  
//3. ��|A|=|V|������ 
//����ڶ������� A�ĵ�Ϊ s�����һ������ A�ĵ�Ϊ t����s-t ��С��Ϊ w(At, t)
public class MinCut
{
	public static Map<Ve, Integer>		vertexMap	= new HashMap<Ve, Integer>();
	public static List<E>				EdgeQueue	= new ArrayList<E>();
	public static List<E>				EdgeList	= new ArrayList<E>();
	public static int					MAX			= Integer.MIN_VALUE;
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
	public static int[]					weight;
	public static boolean[]				combine;
	public static int[][]				map;
	public static LinkedList<Integer>[]	activeHLPP;

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// int a, b, c, M;
		// while(scanf("%d%d", &N, &M) != EOF){
		// memset(map, 0, sizeof(map));
		// while(M--){
		// scanf("%d%d%d", &a, &b, &c);
		// map[a][b] += c;
		// map[b][a] += c;
		// }
		// printf("%d\n", Stoer_Wagner());
		// }
	}

	/* pku 2914 */

	public static int	S, T, minCut, N;

	public static void maximumAdjacencySearch()
	{
		int i, j, x = 0;
		visit = new boolean[vertexCnt + 1];
		weight = new int[vertexCnt + 1];
		S = T = -1;
		for (i = 1; i <= vertexCnt; i++) {
			MAX = Integer.MIN_VALUE;
			// �ҳ�һ����δ����A���С���w(A, x)����x�c��
			for (j = 1; j <= vertexCnt; j++) {
				if (!combine[j] && !visit[j] && weight[j] > MAX) {
					x = j;
					MAX = weight[j];
				}
			}
			if (T == x) {
				return;
			}
			// ����o�Ŀǰ��Cutλ��
			S = T;
			T = x;
			minCut = MAX;
			visit[x] = true; // ����x�㵽A����
			// ����a�c��A�����ᣬ����w(A, x)��ֵ��
			for (j = 1; j <= vertexCnt; j++) {
				if (!combine[j] && !visit[j]) {
					weight[j] += map[x][j];
				}
			}
		}
	}

	public static int Stoer_Wagner()
	{
		int i, j;
		combine = new boolean[vertexCnt + 1];
		int ans = MAX;
		for (i = 1; i <= vertexCnt; i++) {
			maximumAdjacencySearch();
			// s�c��t�c��Cut��ͬ��
			if (minCut < ans) {
				ans = minCut;
			}
			if (ans == 0) {
				return 0;
			}
			// s�c��t�c��Cutͬ��
			combine[T] = true;// ��t�c��ӛ�鱻�ρ��^�ˣ�׃�ɲ����ڡ�
			for (j = 0; j < N; j++) {
				if (!combine[j]) {
					map[S][j] += map[T][j];
					map[j][S] += map[j][T];
				}
			}
		}
		return ans;
	}
}
