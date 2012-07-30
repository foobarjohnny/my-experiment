package graph;

import java.util.LinkedList;
import java.util.Random;

/*
 * http://blog.163.com/zhoumhan_0351/blog/static/39954227200982051154725/
 * http://www.cppblog.com/RyanWang/archive/2009/02/04/73021.html
 * http://www.cnblogs.com/buptLizer/archive/2012/04/15/2450297.html
 * http://hi.baidu.com/ybq0460/blog/item/9f5e2272a445a7ea0bd18725.html
 * http://ideone.com/3lfbC
 */
/*
 * 欧拉回路：图G，若存在一条路，经过G中每条边有且仅有一次，称这条路为欧拉路，
 如果存在一条回路经过G每条边有且仅有一次，
 称这条回路为欧拉回路。具有欧拉回路的图成为欧拉图。
 判断欧拉路是否存在的方法
 有向图：图连通，有一个顶点出度大入度1，有一个顶点入度大出度1，其余都是出度=入度。
 无向图：图连通，只有两个顶点是奇数度，其余都是偶数度的。
 判断欧拉回路是否存在的方法
 有向图：图连通，所有的顶点出度=入度。
 无向图：图连通，所有顶点都是偶数度。
 这里图连通并不代表图是强连通 有向图的话
 http://zyk.thss.tsinghua.edu.cn/73/longtime/part4/chapter14/14_03_04_01.htm
 定义14.22 设D=<V,E>为一个有向图。若D的基图是连通图，则称D是弱连通图，简称为连通图。若vi,vj∈V ，vi→vj与vj→vi至少成立其一，则称D是单向连通图。若均有vivj，则称D是强连通图。

 欧拉路 0或者2个点是特殊的 0的情况下就是回路
 *
 */
//CC指无向图 SCC指有向图
//http://www.graph-magics.com/articles/euler.php 最经典
//http://coco-young.iteye.com/blog/1437699 最不错
/*Procedure Euler-circuit (start);
 Begin
 For 顶点start的每个邻接点v Do
 If 边(start,v)未被标记 Then Begin
 将边(start,v)作上标记;
 将边(v,start)作上标记;                   //1 有向图注释掉
 Euler-circuit (v);
 将边加入栈;
 End;
 End;
 */
/**
 * http://ideone.com/3lfbC
 * http://hi.baidu.com/ybq0460/blog/item/9f5e2272a445a7ea0bd18725.html
 * 混合欧拉回路（poj1637） 2012-01-19 10:10 用最大流算法，首先构图：
 * 
 * 把该图的无向边随便定向，计算每个点的入度和出度。如果有某个点出入度之差为奇数，那么肯定不存在欧拉回路。 因为欧拉回路要求每点入度 =
 * 出度，也就是总度数为偶数，存在奇数度点必不能有欧拉回路。
 * 
 * 好了，现在每个点入度和出度之差均为偶数。那么将这个偶数除以2，得x。也就是说，对于每一个点，只要将x条边改变方向（入>出就是变入，出>入就是变出），
 * 就能保证出 = 入。如果每个点都是出 = 入，那么很明显，该图就存在欧拉回路。
 * 
 * 现在的问题就变成了：我该改变哪些边，可以让每个点出 = 入？
 * 
 * 首先，有向边是不能改变方向的，要之无用，删。一开始不是把无向边定向了吗？定的是什么向，就把网络构建成什么样，边长容量上限1。另新建s和t。对于入 >
 * 出的点u，连接边(u, t)、容量为x，对于出 > 入的点v，连接边(s, v)，容量为x（注意对不同的点x不同）。
 * 之后，察看从S发出的所有边是否满流。有就是能有欧拉回路，没有就是没有。欧拉回路是哪个？察看流值分配，将所有流量非
 * 0（上限是1，流值不是0就是1）的边反向，就能得到每点入度 = 出度的欧拉图。 由于是满流，所以每个入 >
 * 出的点，都有x条边进来，将这些进来的边反向，OK，入 = 出了。对于出 > 入的点亦然。那么，没和s、t连接的点怎么办？和s连接的条件是出 >
 * 入，和t连接的条件是入 > 出，那么这个既没和s也没和t连接的点，自然早在开始就已经满足入 =
 * 出了。那么在网络流过程中，这些点属于“中间点”。我们知道中间点流量不允许有累积的，这样，进去多少就出来多少，反向之后，自然仍保持平衡。
 * 所以，就这样，混合图欧拉回路问题，解了。
 */
public class Euler extends Tarjan
{

	public static boolean brigde4CC(int i, int j)
	{
		// 如果度数是1表明就只有I,J这条边 那必然是桥了
		if (degree[i] == 1) {
			return true;
		}
		else {
			// 为什么要加Integer的强制转换，为什么Integer能够去掉里面的数字j? 不同的类呀，想想为什么
			gra[i].remove((Integer) j);
			gra[j].remove((Integer) i);
			if (connectivity4CC(i)) {
				// 不为桥
				gra[i].push((Integer) j);// 为什么用PUSH而不是ADD，因为之前已经遍历过了
				gra[j].push((Integer) i);
				System.out.println("brigde4CC false");
				return false;
			}
			// 为桥
			gra[i].push((Integer) j);
			gra[j].push((Integer) i);
			System.out.println("brigde4CC true");
			return true;
		}
	}

	// 有向图其实没有桥这一概念，这里看成他的基图来说
	public static boolean brigde4SCC(int i, int j)
	{
		// 如果度数是1表明就只有I,J这条边 那必然是桥了
		if (outDegree[i] == 1) {
			return true;
		}
		else {
			// 为什么要加Integer的强制转换，为什么Integer能够去掉里面的数字j? 不同的类呀，想想为什么
			gra[i].remove((Integer) j);
			if (connectivity()) {
				// 不为桥
				gra[i].push((Integer) j);// 为什么用PUSH而不是ADD，因为之前已经遍历过了
				System.out.println("brigde4SCC false");
				return false;
			}
			// 为桥
			gra[i].push((Integer) j);
			System.out.println("brigde4SCC true");
			return true;
		}
	}

	// 同样可以用并查集来测连通性 这里采用的BFS DFS
	public static boolean connectivity4CC(int cur)
	{
		int[] visit = new int[vertexCnt + 1];
		LinkedList<Integer> queue = new LinkedList<Integer>();
		visit[cur] = 1;
		queue.push(cur);
		int i = 0, next = 0;
		// BFS DFS都能测CC的连通性
		while (!queue.isEmpty()) {
			cur = queue.pop();
			for (i = 0; i < gra[cur].size(); ++i) {
				next = gra[cur].get(i);
				if (visit[next] == 0) {
					visit[next] = 1;
					queue.push(next);
				}
			}
		}

		for (i = 1; i <= vertexCnt; i++) {
			// 排除掉孤立点 当要算整个图的时候可以去掉这个判断
			if (degree[i] > 0) {
				if (visit[i] == 0) {
					System.out.println("connectivity4CC false");
					return false;
				}
			}
		}
		System.out.println("connectivity4CC true");
		return true;
	}

	// SCC 用Kosaraju测强连通性或者Tarjan
	// 这里只需要测连通性 有向图无向图都可以用
	public static boolean connectivity()
	{
		makeSet();
		int next, cur;
		for (cur = 1; cur <= vertexCnt; cur++) {
			for (int idx = 0; idx < gra[cur].size(); idx++) {
				next = gra[cur].get(idx);
				union(cur, next);
			}
		}
		int cnt = 0;
		for (int i = 1; i <= vertexCnt; i++) {
			// 必须加括号
			if ((outDegree[i] > 0 || inDegree[i] > 0) && p[i] == i)
				cnt++;
		}
		return cnt == 1;
	}

	// 弗罗莱（Fleury）算法思想－解决欧拉回路
	// Fleury算法：
	// 任取v0∈V(G)，令P0=v0；
	// 设Pi=v0e1v1e2…ei vi已经行遍，按下面方法从中选取ei+1：
	// （a）ei+1与vi相关联；
	// （b）除非无别的边可供行遍，否则ei+1不应该为Gi=G-{e1,e2, …,
	// ei}中的桥（所谓桥是一条删除后使连通图不再连通的边，有向图转化为其图，用并查集最好）；
	// （c）当（b）不能再进行时，算法停止。
	// 可以证明,当算法停止时所得的简单回路Wm=v0e1v1e2….emvm(vm=v0)为G中的一条欧拉回路，复杂度为O(e*e)。
	// http://www.austincc.edu/powens/+Topics/HTML/05-6/05-6.html
	// 只能用于无向图，可拓展到有向图
	public static void Fleury4CC(LinkedList<Integer> eulerPath, int cur) // Fleury算法
	{
		int next = 0, b = 0;
		if (eulerPath.size() <= edgeCnt + 1) {
			eulerPath.add(cur);
			for (int idx = 0; idx < gra[cur].size(); idx++) {
				next = gra[cur].get(idx);
				if (degree[cur] == 1 || brigde4CC(cur, next) == false) {
					b = 1;
					break;
				}
			}
		}
		if (b == 1) {
			gra[cur].remove((Integer) next);
			gra[next].remove((Integer) cur);
			degree[cur]--;
			degree[next]--;
			Fleury4CC(eulerPath, next);
		}
	}

	public static void Fleury4SCC(LinkedList<Integer> eulerPath, int cur) // Fleury算法
	{
		int next = 0, b = 0;
		if (eulerPath.size() <= edgeCnt + 1) {
			eulerPath.add(cur);
			for (int idx = 0; idx < gra[cur].size(); idx++) {
				next = gra[cur].get(idx);
				if (outDegree[cur] == 1 || brigde4SCC(cur, next) == false) {
					b = 1;
					break;
				}
			}
		}
		if (b == 1) {
			gra[cur].remove((Integer) next);
			outDegree[cur]--;
			inDegree[next]--;
			Fleury4SCC(eulerPath, next);
		}
	}

	// 既可以求CIRCUIT又可以求PATH,不过PATH的时候要选好START,必须在后面用push
	// O(E)
	public static void Hierholzer(LinkedList<Integer> eulerPath, int cur, int[][] visitEdge, boolean directedGraph)
	{
		int next;
		for (int idx = 0; idx < gra[cur].size(); idx++) {
			next = gra[cur].get(idx);
			if (visitEdge[cur][next] == 0) {
				visitEdge[cur][next] = 1;
				if (!directedGraph) {
					visitEdge[next][cur] = 1;
				}

				// if (eulerPath.size() == 0) {
				// eulerPath.add(cur);
				// }
				// eulerPath.add(next);
				// 这川不行的 为什么需要在后面用PUSH更准确呢 自己想想吧
				Hierholzer(eulerPath, next, visitEdge, directedGraph);
				if (eulerPath.size() == 0) {
					eulerPath.push(next);
				}
				eulerPath.push(cur);
				// eulerPath.push(edge);
			}
		}
	}

	public static void HierholzerSlove(boolean directedGraph)
	{

		if (true)
		// if (((directedGraph && !odd4SCC()) || (!directedGraph && !odd4CC()))
		// && connectivity()) // 判断图连通,判断有无奇度点
		{
			LinkedList<Integer> eulerPath = new LinkedList<Integer>();
			int[][] visitEdge = new int[vertexCnt + 1][vertexCnt + 1];
			int start = 1;
			Hierholzer(eulerPath, start, visitEdge, directedGraph);
			System.out.println("\n\t HierholzerSlove 该图的一条欧拉回路：");
			System.out.println(eulerPath);
		}
		else {
			System.out.println("\n\t HierholzerSlove 该图不存在欧拉回路!\n");
		}
	}

	public static void FleurySlove4CC()
	{

		if (!odd4CC() && connectivity4CC(1)) // 判断图连通,判断有无奇度点
		{
			LinkedList<Integer> eulerPath = new LinkedList<Integer>();
			Fleury4CC(eulerPath, 1);
			System.out.println("\n\tFleurySlove4CC 该图的一条欧拉回路：");
			System.out.println(eulerPath);
		}
		else {
			System.out.println("\n\tFleurySlove4CC 该图不存在欧拉回路!\n");
		}
	}

	public static void FleurySlove4SCC()
	{

		if (!odd4SCC() && connectivity()) // 判断图连通,判断有无奇度点
		{
			LinkedList<Integer> eulerPath = new LinkedList<Integer>();
			Fleury4SCC(eulerPath, 1);
			System.out.println("\n\tFleurySlove4SCC 该图的一条欧拉回路：");
			System.out.println(eulerPath);
		}
		else {
			System.out.println("\n\tFleurySlove4SCC 该图不存在欧拉回路!\n");
		}
	}

	public static boolean odd4CC()
	{
		for (int i = 1; i <= vertexCnt; i++) {
			if (gra[i].size() % 2 == 1) {
				System.out.println("odd4CC true");
				return true;
			}
		}
		System.out.println("odd4CC false");
		return false;
	}

	public static boolean odd4SCC()
	{
		for (int i = 1; i <= vertexCnt; i++) {
			if (outDegree[i] != inDegree[i]) {
				System.out.println("odd4SCC true");
				return true;
			}
		}
		System.out.println("odd4SCC false");
		return false;
	}

	public static void main(String[] args)
	{
		edgeCnt = 22;
		vertexCnt = 5;
		boolean directedGraph = true;
		init();
		input3(directedGraph);
		// FleurySlove4CC();
		// FleurySlove4SCC();
		HierholzerSlove(directedGraph);
	}

	public static void input3(boolean directedGraph)
	{
		// for cc euler
		// String[] s = { "1-2", "1-3", "2-5", "4-2", "3-2", "4-5" };
		// for scc euler
		// String[] s = { "2-1","4-2", "1-3", "2-5", "3-2", "5-4" };
		String[] s = { "1-3", "1-4", "4-1" };
		int a, b;
		int count = 0;
		for (int i = 0; i < s.length; i++) {
			a = Integer.valueOf(s[i].split("-")[0]);// 1-n
			b = Integer.valueOf(s[i].split("-")[1]);// 1-n
			if (!gra[a].contains(b) && a != b) {
				System.out.println(a + "-" + b);
				gra[a].push(b);
				graT[b].push(a);
				outDegree[a]++;
				inDegree[b]++;
				if (!directedGraph) {
					gra[b].push(a);
					graT[a].push(b);
					outDegree[b]++;
					inDegree[a]++;
					degree[a]++;
					degree[b]++;
				}
				count++;
			}
		}
		// 去掉了重复了的edge
		edgeCnt = count;
	}

}
