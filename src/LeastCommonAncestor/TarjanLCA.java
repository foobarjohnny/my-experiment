package LeastCommonAncestor;

//LeastCommonAncestor
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import disjointsets.DisjointSets;

public class TarjanLCA
{
	public static int					vertexCnt, edgeCnt; // 点数，边数
	public static LinkedList<Integer>[]	graph;
	public static LinkedList<Integer>[]	quest;
	public static int[]					InStack;			// 检查是否在栈中(2为在栈中GREY，1为已访问BLACK，且不在栈中，0为不在)
	public static int[]					ancestor;
	private static int[]				levels;
	private static int[]				times;
	private static int					index;
	private static int					level;
	private static ArrayList<Question>	questList;

	// （1）最笨的算法显然是朴素的直接查询 ，回答每个问题是O(n)的。
	//
	// （2）一种算法是采用线段树
	// ，即在线段树的每个节点保存该区间的最大值与最小值，O(n)的预处理时间（需要自底向上构建），可以O(logn)地回答每个问题。关于这种解法，见我线段树专题中炮兵阵地那道题即可：）
	//
	// （3）另一种算法就是神奇的ST算法（Sparse Table）
	// ，以求最大值为例，设v[n][f]表示[n,n+2^f)这个区间内的最大值，那么在询问到[a,b)区间的最大值时答案就是max(v[a][f],v[b-2^f][f])，其中f是满足2^f<=b-a的最大的f。至于那张稀疏表，可以用递推的方法在O(nlogn)（也就是表的元素数）的时间内构建。也就是说v[n][f]=max(v[n][f-1],v[n+2^(f-1)][f])。
	//
	// 另外，RMQ问题与LCA（Least Common
	// Ancestors，最近公共祖先）问题可以互相转化。LCA问题有一个经典的离线算法Tarjan算法，稍后我将会介绍。
	public static void init()
	{
		graph = new LinkedList[vertexCnt + 1];
		quest = new LinkedList[vertexCnt + 1];
		questList = new ArrayList<Question>();
		for (int i = 1; i <= vertexCnt; ++i) {
			graph[i] = new LinkedList<Integer>();
			quest[i] = new LinkedList<Integer>();
		}
		refresh();
	}

	public static void refresh()
	{
		InStack = new int[vertexCnt + 1];
		ancestor = new int[vertexCnt + 1];
		DisjointSets.makeSetFrom1(vertexCnt + 1);
	}

	// 离线算法 就是要把所有有QUERY收集完
	// O(V+E+Q)
	private static void tarjan_LCA(int cur)
	{
		ancestor[cur] = cur;
		for (int i = 0; i < graph[cur].size(); ++i) {
			int next = graph[cur].get(i);
			tarjan_LCA(next);
			DisjointSets.Union(cur, graph[cur].get(i));
			ancestor[DisjointSets.findSet(cur)] = cur;
		}
		InStack[cur] = 2; // BLACK
		for (int i = 0; i < quest[cur].size(); i++) {
			// 如果已经访问了问题节点,就可以返回结果了.
			if (InStack[quest[cur].get(i)] == 2) {
				System.out.println(cur + "-" + quest[cur].get(i) + ":" + ancestor[DisjointSets.findSet(quest[cur].get(i))]);
				return;
			}
		}
	}

	public static void input()
	{

		for (int i = 1; i <= edgeCnt; i++) {
			int a = new Random().nextInt(vertexCnt - 1) + 1;// 1-n
			int b = new Random().nextInt(vertexCnt - 1) + 1;// 1-n
			if (!graph[a].contains(b) && a != b) {
				System.out.println(a + "->" + b);
				graph[a].push(b);
				graph[b].push(a);
			}
		}

	}

	public static void input2()
	{
		// String[] s = { "4-3", "5-6", "5-2", "7-8", "5-7", "3-5" };
		// String[] s = { "7-8", "6-1", "8-1", "1-2", "3-2", "1-3", "4-5",
		// "6-7", "5-7", "5-6", "6-8", "7-1" };
		String[] s = { "1-2", "2-3", "1-4", "4-5", "5-6", "6-7", "1-8", "8-9" };

		int a, b;
		for (int i = 0; i < s.length; i++) {

			a = Integer.valueOf(s[i].split("-")[0]);// 1-n
			b = Integer.valueOf(s[i].split("-")[1]);// 1-n
			if (!graph[a].contains(b) && a != b) {
				System.out.println(a + "-" + b);
				graph[a].push(b);
				// graph[b].push(a);
			}
		}

		for (int i = 1; i <= 7; i++) {
			a = new Random().nextInt(vertexCnt - 1) + 1;// 1-n
			b = new Random().nextInt(vertexCnt - 1) + 1;// 1-n
			if (!quest[a].contains(b) && a != b) {
				System.out.println(a + "--" + b);
				quest[a].push(b);
				quest[b].push(a);
			}
			questList.add(new Question(a, b));
		}
		// gra[5].push(6);

	}

	public static void solve()
	{

		refresh();
		tarjan_LCA(1);
		rmq_LCA(1);
	}

	public static void main(String[] args)
	{
		edgeCnt = 22;
		vertexCnt = 9;
		init();
		input2();
		solve();
	}

	private static void DFS(int root, int level)
	{
		times[index] = root;
		levels[index++] = level;
		for (int i = 0; i < graph[root].size(); ++i) {
			int next = graph[root].get(i);
			DFS(next, level + 1);
			times[index] = root;
			levels[index++] = level;
		}
	}

	private static int getIndex(int vertex)
	{
		int index = -1;
		for (int i = 0; i < times.length; ++i) {
			if (vertex == times[i]) {
				index = i;
			}
		}
		return index;
	}

	// 本节介绍了一种比较高效的在线算法（ST算法）解决这个问题。所谓在线算法，是指用户每输入一个查询便马上处理一个查询。
	// 该算法一般用较长的时间做预处理，待信息充足以后便可以用较少的时间回答每个查询。
	// ST（Sparse Table）算法是一个非常有名的在线处理RMQ问题的算法，它可以在O(nlogn)时间内进行预处理，然后在O(1)时间内回答每个查询。
	private static void rmq_LCA(int root)
	{
		// 为什么是2N-1呢
		// 树中有N点 至多有N-1边
		// 每条边走2次，相当于2N-2边，要有2N-2条边，有2N-1个点
		levels = new int[vertexCnt * 2 - 1];
		times = new int[vertexCnt * 2 - 1];
		index = 0;
		// 先DFS 再ST
		DFS(root, 1);
		RMQ.preProcess(levels);
		for (Question q : questList) {
			int a = Math.min(getIndex(q.a), getIndex(q.b));
			int b = Math.max(getIndex(q.a), getIndex(q.b));
			if (a == -1 || b == -1) {
				System.out.println("LCA " + q.a + "-" + q.b + ": NONE");
				continue;
			}
			System.out.println("LCA " + q.a + "-" + q.b + ":" + times[RMQ.queryMin(a, b)]);
		}
	}
}

class Question
{
	int	a;
	int	b;

	Question(int a, int b)
	{
		this.a = a;
		this.b = b;
	}
}