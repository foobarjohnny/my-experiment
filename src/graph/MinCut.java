package graph;

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
//定义w(A, x) = ∑w(v[i], x)，v[i] ∈   A 
//定义 Ax 为在x 前加入 A 的所有点的集合（不包括 x）  
//1. 令集合 A={a}，a为 V中任意点  
//2. 选取 V - A中的 w(A, x)最大的点 x加入集合 A  
//3. 若|A|=|V|，结束 
//令倒数第二个加入 A的点为 s，最后一个加入 A的点为 t，则s-t 最小割为 w(At, t)

//http://acm.nudt.edu.cn/~twcourse/Cut.html
//详细看PPT与PDF
//详见PPT 自己描述 每次求出一个 任意两点的s-t最小割,然后再把s,t合并成一个点,用好contract定理,再迭代求余小的点的任意s-t割,比较最小割值
public class MinCut
{
	public static int		edgeCnt;
	public static int		vertexCnt	= edgeCnt = 0;	// 点数，边数

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
		// // 去掉了重复了的edge
		// edgeCnt = count;

	}

	public static void addEdge(int a, int b, int weight)
	{
		map[a][b] = weight;
		map[b][a] = weight;
	}

	/* pku 2914 */

	// O(e + vlog(v)) E条边的InCreaseKey,V次的ExtraMax 用Fibo
	//
	public static void maximumAdjacencySearch()
	{
		int i, j, x = 0;
		visit = new boolean[vertexCnt + 1];
		weight = new int[vertexCnt + 1];
		s = t = -1;
		for (i = 1; i <= vertexCnt; i++) {
			int MAX = Integer.MIN_VALUE;
			// 找出一個尚未加入A當中、且w(A, x)最大的x點。
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
			// 不斷紀錄目前的Cut位置
			s = t;
			t = x;
			minCut = MAX;

			visit[x] = true; // 加入x点到A集合
			// 加入a點到A集合後，更新w(A, x)的值。
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
	// V*O(e + vlog(v)) 采用F堆 每次E条边的INCREASE O(1),V次的EXTRACTMAX O(lgV)
	// 如果不用是O(V*(E+V^2)) 共O(V^3) 这个是一般比较
	// 最大堆就是O(V*(Elg(E)+Vlg(V))
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
			combine[s] = true;// 把t點標記為被合併過了，變成不存在。
			// 合并S和T都是等效的,
			// 用这种方法不能求Global S,T 因为合并的点,并不能分出来,只能求minCut
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
