package graph;

import java.util.ArrayList;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
//可以用最小切割最大流定理：
//1.min=MAXINT,确定一个源点 为什么不枚举源点了呢？？？
//2.枚举汇点
//3.计算最大流，并确定当前源汇的最小割集，若比min小更新min
//4.转到2直到枚举完毕
//5.min即为所求输出min
//    不难看出复杂度很高：枚举汇点要O(n)，最短增广路最大流算法求最大流是O((n^2)m)复杂度，在复杂网络中O(m)=O(n^2)，算法总复杂度就是O(n^5)；哪怕采用最高标号预进流算法求最大流O((n^2)(m^0.5))，算法总复杂度也要O(n^4)
//    所以用网络流算法求解最小割集复杂度不会低于O(n^4)。
    
//1. 算法在做"最大生成树"时更新的不是普通意义上的最大边, 而是与之相连的边的权值和, 当所有边都是单位权值时就是累计度.
//2. "最后进入A的两点记为s和t", 网上对s有两种解释, 一是在t之前一个加进去的点, 二是t的前趋节点, 也就是最后选择的那条边的另一端. 正解是第一种!
//3. 对于稠密图, 比如这题, 我用堆, 映射二分堆, 或者STL的优先队列都会TLE, 还不如老老实实O(n^3).

//最小割 Stoer-Wagner 算法 
//Etrnls 2007-4-15 
//Stoer-Wagner 算法用来求无向图 G=(V, E)的全局最小割。 
//
//算法基于这样一个定理：对于任意s, t   V ∈ ，全局最小割或者等于原图的s-t 最小割，或者等于将原图进行 Contract(s, 
//t)操作所得的图的全局最小割。 
//
//算法框架： 
//1. 设当前找到的最小割MinCut 为+∞  
//2. 在 G中求出任意 s-t 最小割 c，MinCut = min(MinCut, c)   
//3. 对 G作 Contract(s, t)操作，得到 G'=(V', E')，若|V'| > 1，则G=G'并转 2，否则MinCut 为原图的全局最
//小割  
//
//Contract 操作定义： 
//若不存在边(p, q)，则定义边(p, q)权值w(p, q) = 0 
//Contract(a, b): 删掉点 a, b 及边(a, b)，加入新节点 c，对于任意 v  V ∈ ，w(v, c) = w(c, v) = w(a, v) + w(b, 
//v) 
//
//求 G=(V, E)中任意 s-t 最小割的算法： 
//定义w(A, x) = ∑w(v[i], x)，v[i]  A ∈  
//定义 Ax 为在x 前加入 A 的所有点的集合（不包括 x）  
//1. 令集合 A={a}，a为 V中任意点  
//2. 选取 V - A中的 w(A, x)最大的点 x加入集合 A  
//3. 若|A|=|V|，结束 
//令倒数第二个加入 A的点为 s，最后一个加入 A的点为 t，则s-t 最小割为 w(At, t)
public class MinCut
{
	public static Map<Ve, Integer>		vertexMap	= new HashMap<Ve, Integer>();
	public static List<E>				EdgeQueue	= new ArrayList<E>();
	public static List<E>				EdgeList	= new ArrayList<E>();
	public static int					MAX			= Integer.MIN_VALUE;
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
			// 找出一個尚未加入A當中、且w(A, x)最大的x點。
			for (j = 1; j <= vertexCnt; j++) {
				if (!combine[j] && !visit[j] && weight[j] > MAX) {
					x = j;
					MAX = weight[j];
				}
			}
			if (T == x) {
				return;
			}
			// 不斷紀錄目前的Cut位置
			S = T;
			T = x;
			minCut = MAX;
			visit[x] = true; // 加入x点到A集合
			// 加入a點到A集合後，更新w(A, x)的值。
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
			// s點和t點在Cut不同側
			if (minCut < ans) {
				ans = minCut;
			}
			if (ans == 0) {
				return 0;
			}
			// s點和t點在Cut同側
			combine[T] = true;// 把t點標記為被合併過了，變成不存在。
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
