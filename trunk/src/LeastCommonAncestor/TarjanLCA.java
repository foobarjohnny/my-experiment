package LeastCommonAncestor;

//LeastCommonAncestor
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import disjointsets.DisjointSets;

public class TarjanLCA
{
	public static int					vertexCnt, edgeCnt; // ����������
	public static LinkedList<Integer>[]	graph;
	public static LinkedList<Integer>[]	quest;
	public static int[]					InStack;			// ����Ƿ���ջ��(2Ϊ��ջ��GREY��1Ϊ�ѷ���BLACK���Ҳ���ջ�У�0Ϊ����)
	public static int[]					ancestor;
	private static int[]				levels;
	private static int[]				times;
	private static int					index;
	private static int					level;
	private static ArrayList<Question>	questList;

	// ��1������㷨��Ȼ�����ص�ֱ�Ӳ�ѯ ���ش�ÿ��������O(n)�ġ�
	//
	// ��2��һ���㷨�ǲ����߶���
	// �������߶�����ÿ���ڵ㱣�����������ֵ����Сֵ��O(n)��Ԥ����ʱ�䣨��Ҫ�Ե����Ϲ�����������O(logn)�ػش�ÿ�����⡣�������ֽⷨ�������߶���ר�����ڱ�����ǵ��⼴�ɣ���
	//
	// ��3����һ���㷨���������ST�㷨��Sparse Table��
	// ���������ֵΪ������v[n][f]��ʾ[n,n+2^f)��������ڵ����ֵ����ô��ѯ�ʵ�[a,b)��������ֵʱ�𰸾���max(v[a][f],v[b-2^f][f])������f������2^f<=b-a������f����������ϡ��������õ��Ƶķ�����O(nlogn)��Ҳ���Ǳ��Ԫ��������ʱ���ڹ�����Ҳ����˵v[n][f]=max(v[n][f-1],v[n+2^(f-1)][f])��
	//
	// ���⣬RMQ������LCA��Least Common
	// Ancestors������������ȣ�������Ի���ת����LCA������һ������������㷨Tarjan�㷨���Ժ��ҽ�����ܡ�
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

	// �����㷨 ����Ҫ��������QUERY�ռ���
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
			// ����Ѿ�����������ڵ�,�Ϳ��Է��ؽ����.
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

	// ���ڽ�����һ�ֱȽϸ�Ч�������㷨��ST�㷨�����������⡣��ν�����㷨����ָ�û�ÿ����һ����ѯ�����ϴ���һ����ѯ��
	// ���㷨һ���ýϳ���ʱ����Ԥ��������Ϣ�����Ժ������ý��ٵ�ʱ��ش�ÿ����ѯ��
	// ST��Sparse Table���㷨��һ���ǳ����������ߴ���RMQ������㷨����������O(nlogn)ʱ���ڽ���Ԥ����Ȼ����O(1)ʱ���ڻش�ÿ����ѯ��
	private static void rmq_LCA(int root)
	{
		// Ϊʲô��2N-1��
		// ������N�� ������N-1��
		// ÿ������2�Σ��൱��2N-2�ߣ�Ҫ��2N-2���ߣ���2N-1����
		levels = new int[vertexCnt * 2 - 1];
		times = new int[vertexCnt * 2 - 1];
		index = 0;
		// ��DFS ��ST
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