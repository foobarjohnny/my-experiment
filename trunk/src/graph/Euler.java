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
 * ŷ����·��ͼG��������һ��·������G��ÿ�������ҽ���һ�Σ�������·Ϊŷ��·��
 �������һ����·����Gÿ�������ҽ���һ�Σ�
 ��������·Ϊŷ����·������ŷ����·��ͼ��Ϊŷ��ͼ��
 �ж�ŷ��·�Ƿ���ڵķ���
 ����ͼ��ͼ��ͨ����һ��������ȴ����1����һ��������ȴ����1�����඼�ǳ���=��ȡ�
 ����ͼ��ͼ��ͨ��ֻ�����������������ȣ����඼��ż���ȵġ�
 �ж�ŷ����·�Ƿ���ڵķ���
 ����ͼ��ͼ��ͨ�����еĶ������=��ȡ�
 ����ͼ��ͼ��ͨ�����ж��㶼��ż���ȡ�
 ����ͼ��ͨ��������ͼ��ǿ��ͨ ����ͼ�Ļ�
 http://zyk.thss.tsinghua.edu.cn/73/longtime/part4/chapter14/14_03_04_01.htm
 ����14.22 ��D=<V,E>Ϊһ������ͼ����D�Ļ�ͼ����ͨͼ�����D������ͨͼ�����Ϊ��ͨͼ����vi,vj��V ��vi��vj��vj��vi���ٳ�����һ�����D�ǵ�����ͨͼ��������vivj�����D��ǿ��ͨͼ��

 ŷ��· 0����2����������� 0������¾��ǻ�·
 *
 */
//CCָ����ͼ SCCָ����ͼ
//http://www.graph-magics.com/articles/euler.php ���
//http://coco-young.iteye.com/blog/1437699 ���
/*Procedure Euler-circuit (start);
 Begin
 For ����start��ÿ���ڽӵ�v Do
 If ��(start,v)δ����� Then Begin
 ����(start,v)���ϱ��;
 ����(v,start)���ϱ��;                   //1 ����ͼע�͵�
 Euler-circuit (v);
 ���߼���ջ;
 End;
 End;
 */
/**
 * http://ideone.com/3lfbC
 * http://hi.baidu.com/ybq0460/blog/item/9f5e2272a445a7ea0bd18725.html
 * ���ŷ����·��poj1637�� 2012-01-19 10:10 ��������㷨�����ȹ�ͼ��
 * 
 * �Ѹ�ͼ���������㶨�򣬼���ÿ�������Ⱥͳ��ȡ������ĳ��������֮��Ϊ��������ô�϶�������ŷ����·�� ��Ϊŷ����·Ҫ��ÿ����� =
 * ���ȣ�Ҳ�����ܶ���Ϊż�������������ȵ�ز�����ŷ����·��
 * 
 * ���ˣ�����ÿ������Ⱥͳ���֮���Ϊż������ô�����ż������2����x��Ҳ����˵������ÿһ���㣬ֻҪ��x���߸ı䷽����>�����Ǳ��룬��>����Ǳ������
 * ���ܱ�֤�� = �롣���ÿ���㶼�ǳ� = �룬��ô�����ԣ���ͼ�ʹ���ŷ����·��
 * 
 * ���ڵ�����ͱ���ˣ��Ҹøı���Щ�ߣ�������ÿ����� = �룿
 * 
 * ���ȣ�������ǲ��ܸı䷽��ģ�Ҫ֮���ã�ɾ��һ��ʼ���ǰ�����߶������𣿶�����ʲô�򣬾Ͱ����繹����ʲô�����߳���������1�����½�s��t�������� >
 * ���ĵ�u�����ӱ�(u, t)������Ϊx�����ڳ� > ��ĵ�v�����ӱ�(s, v)������Ϊx��ע��Բ�ͬ�ĵ�x��ͬ����
 * ֮�󣬲쿴��S���������б��Ƿ��������о�������ŷ����·��û�о���û�С�ŷ����·���ĸ����쿴��ֵ���䣬������������
 * 0��������1����ֵ����0����1���ı߷��򣬾��ܵõ�ÿ����� = ���ȵ�ŷ��ͼ�� ����������������ÿ���� >
 * ���ĵ㣬����x���߽���������Щ�����ı߷���OK���� = ���ˡ����ڳ� > ��ĵ���Ȼ����ô��û��s��t���ӵĵ���ô�죿��s���ӵ������ǳ� >
 * �룬��t���ӵ��������� > ������ô�����û��sҲû��t���ӵĵ㣬��Ȼ���ڿ�ʼ���Ѿ������� =
 * ���ˡ���ô�������������У���Щ�����ڡ��м�㡱������֪���м���������������ۻ��ģ���������ȥ���پͳ������٣�����֮����Ȼ�Ա���ƽ�⡣
 * ���ԣ������������ͼŷ����·���⣬���ˡ�
 */
public class Euler extends Tarjan
{

	public static boolean brigde4CC(int i, int j)
	{
		// ���������1������ֻ��I,J������ �Ǳ�Ȼ������
		if (degree[i] == 1) {
			return true;
		}
		else {
			// ΪʲôҪ��Integer��ǿ��ת����ΪʲôInteger�ܹ�ȥ�����������j? ��ͬ����ѽ������Ϊʲô
			gra[i].remove((Integer) j);
			gra[j].remove((Integer) i);
			if (connectivity4CC(i)) {
				// ��Ϊ��
				gra[i].push((Integer) j);// Ϊʲô��PUSH������ADD����Ϊ֮ǰ�Ѿ���������
				gra[j].push((Integer) i);
				System.out.println("brigde4CC false");
				return false;
			}
			// Ϊ��
			gra[i].push((Integer) j);
			gra[j].push((Integer) i);
			System.out.println("brigde4CC true");
			return true;
		}
	}

	// ����ͼ��ʵû������һ������￴�����Ļ�ͼ��˵
	public static boolean brigde4SCC(int i, int j)
	{
		// ���������1������ֻ��I,J������ �Ǳ�Ȼ������
		if (outDegree[i] == 1) {
			return true;
		}
		else {
			// ΪʲôҪ��Integer��ǿ��ת����ΪʲôInteger�ܹ�ȥ�����������j? ��ͬ����ѽ������Ϊʲô
			gra[i].remove((Integer) j);
			if (connectivity()) {
				// ��Ϊ��
				gra[i].push((Integer) j);// Ϊʲô��PUSH������ADD����Ϊ֮ǰ�Ѿ���������
				System.out.println("brigde4SCC false");
				return false;
			}
			// Ϊ��
			gra[i].push((Integer) j);
			System.out.println("brigde4SCC true");
			return true;
		}
	}

	// ͬ�������ò��鼯������ͨ�� ������õ�BFS DFS
	public static boolean connectivity4CC(int cur)
	{
		int[] visit = new int[vertexCnt + 1];
		LinkedList<Integer> queue = new LinkedList<Integer>();
		visit[cur] = 1;
		queue.push(cur);
		int i = 0, next = 0;
		// BFS DFS���ܲ�CC����ͨ��
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
			// �ų��������� ��Ҫ������ͼ��ʱ�����ȥ������ж�
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

	// SCC ��Kosaraju��ǿ��ͨ�Ի���Tarjan
	// ����ֻ��Ҫ����ͨ�� ����ͼ����ͼ��������
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
			// ���������
			if ((outDegree[i] > 0 || inDegree[i] > 0) && p[i] == i)
				cnt++;
		}
		return cnt == 1;
	}

	// ��������Fleury���㷨˼�룭���ŷ����·
	// Fleury�㷨��
	// ��ȡv0��V(G)����P0=v0��
	// ��Pi=v0e1v1e2��ei vi�Ѿ��б飬�����淽������ѡȡei+1��
	// ��a��ei+1��vi�������
	// ��b�������ޱ�ı߿ɹ��б飬����ei+1��Ӧ��ΪGi=G-{e1,e2, ��,
	// ei}�е��ţ���ν����һ��ɾ����ʹ��ͨͼ������ͨ�ıߣ�����ͼת��Ϊ��ͼ���ò��鼯��ã���
	// ��c������b�������ٽ���ʱ���㷨ֹͣ��
	// ����֤��,���㷨ֹͣʱ���õļ򵥻�·Wm=v0e1v1e2��.emvm(vm=v0)ΪG�е�һ��ŷ����·�����Ӷ�ΪO(e*e)��
	// http://www.austincc.edu/powens/+Topics/HTML/05-6/05-6.html
	// ֻ����������ͼ������չ������ͼ
	public static void Fleury4CC(LinkedList<Integer> eulerPath, int cur) // Fleury�㷨
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

	public static void Fleury4SCC(LinkedList<Integer> eulerPath, int cur) // Fleury�㷨
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

	// �ȿ�����CIRCUIT�ֿ�����PATH,����PATH��ʱ��Ҫѡ��START,�����ں�����push
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
				// �⴨���е� Ϊʲô��Ҫ�ں�����PUSH��׼ȷ�� �Լ������
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
		// && connectivity()) // �ж�ͼ��ͨ,�ж�������ȵ�
		{
			LinkedList<Integer> eulerPath = new LinkedList<Integer>();
			int[][] visitEdge = new int[vertexCnt + 1][vertexCnt + 1];
			int start = 1;
			Hierholzer(eulerPath, start, visitEdge, directedGraph);
			System.out.println("\n\t HierholzerSlove ��ͼ��һ��ŷ����·��");
			System.out.println(eulerPath);
		}
		else {
			System.out.println("\n\t HierholzerSlove ��ͼ������ŷ����·!\n");
		}
	}

	public static void FleurySlove4CC()
	{

		if (!odd4CC() && connectivity4CC(1)) // �ж�ͼ��ͨ,�ж�������ȵ�
		{
			LinkedList<Integer> eulerPath = new LinkedList<Integer>();
			Fleury4CC(eulerPath, 1);
			System.out.println("\n\tFleurySlove4CC ��ͼ��һ��ŷ����·��");
			System.out.println(eulerPath);
		}
		else {
			System.out.println("\n\tFleurySlove4CC ��ͼ������ŷ����·!\n");
		}
	}

	public static void FleurySlove4SCC()
	{

		if (!odd4SCC() && connectivity()) // �ж�ͼ��ͨ,�ж�������ȵ�
		{
			LinkedList<Integer> eulerPath = new LinkedList<Integer>();
			Fleury4SCC(eulerPath, 1);
			System.out.println("\n\tFleurySlove4SCC ��ͼ��һ��ŷ����·��");
			System.out.println(eulerPath);
		}
		else {
			System.out.println("\n\tFleurySlove4SCC ��ͼ������ŷ����·!\n");
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
		// ȥ�����ظ��˵�edge
		edgeCnt = count;
	}

}
