package Hackathon;

import java.util.Scanner;

/**
 * Navigation Nightmare : Origin problem of USACO:
 * http://ace.delos.com/FEB04.htm Try it online at:
 * http://poj.org/problem?id=1984
 * 
 * solutions:
 * 
 * http://www.cnblogs.com/nanke/archive/2011/05/08/2040419.html
 * 
 * 只需要用并查集就行了 然后用到路径压缩 这里QUERY的INDEX默认是递增的 如果不是递增的，那可以用先排序，这样可以不用重复并集或构图
 * 
 * @author dysong
 */
public class p1304
{
	static int[]		order;
	static int			tag;
	static boolean[]	visit;
	static int[][]		disX;
	static int[][]		disY;
	static int			N;
	static int			M;

	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		N = scanner.nextInt();
		M = scanner.nextInt();
		int[] infoH1 = new int[M + 1];
		int[] infoH2 = new int[M + 1];
		int[] infoL = new int[M + 1];
		String[] infoD = new String[M + 1];
		int H1, H2, L;
		String D;
		for (int i = 1; i < M + 1; ++i) {
			H1 = scanner.nextInt();
			H2 = scanner.nextInt();
			L = scanner.nextInt();
			D = scanner.next();
			infoH1[i] = H1;
			infoH2[i] = H2;
			infoL[i] = L;
			infoD[i] = D;
		}
		int K = scanner.nextInt();
		// System.out.println("k:" + K);
		int index = -1;
		int s, t;
		try {
			for (int i = 1; i < K + 1; ++i) {
				s = scanner.nextInt();
				// System.out.println("s:" + s);
				t = scanner.nextInt();
				// System.out.println("t:" + t);
				index = scanner.nextInt();
				// System.out.println("index:" + index);
				if (isConnected(s, t, index, infoH1, infoH2)) {
					makeGraph(index, infoH1, infoH2, infoL, infoD);
					order = new int[N + 1];
					tag = 1;
					visit[s] = true;
					dfsa(s, t);
					order[tag] = s;
					int dis = calDis(order);
					System.out.println(dis + "");
				}
				else {
					System.out.println("-1");
				}
			}
		}
		catch (Throwable z) {
			z.printStackTrace();
			System.out.println(z.getMessage());
		}
	}

	private static int calDis(int[] order2)
	{
		int dx = 0;
		int dy = 0;
		for (int i = 1; i < tag; i++) {
			int x = order[i];
			int y = order[i + 1];
			if (disX[x][y] != 0) {
				dx += disX[x][y];
			}
			else {
				dy += disY[x][y];
			}
		}
		return Math.abs(dx) + Math.abs(dy);
	}

	private static boolean isConnected(int h1, int h2, int index, int[] infoH1, int[] infoH2)
	{
		DisjointSets.makeSetFrom1(N);
		for (int i = 1; i <= index; i++) {
			DisjointSets.Union(infoH1[i], infoH2[i]);
		}
		if (DisjointSets.findSet(h1) == DisjointSets.findSet(h2)) {
			return true;
		}
		else {
			return false;
		}
	}

	private static void makeGraph(int index, int[] infoH1, int[] infoH2, int[] infoL, String[] infoD)
	{
		// TODO Auto-generated method stub
		disX = new int[N + 1][N + 1];
		disY = new int[N + 1][N + 1];
		visit = new boolean[N + 1];

		int H1 = 0, H2 = 0, L;
		String D;
		for (int i = 1; i <= index; i++) {
			H1 = infoH1[i];
			H2 = infoH2[i];
			L = infoL[i];
			D = infoD[i];
			if ("E".equalsIgnoreCase(D)) {
				disX[H1][H2] = L;
				disX[H2][H1] = -1 * L;
			}
			else if ("W".equalsIgnoreCase(D)) {
				disX[H1][H2] = -1 * L;
				disX[H2][H1] = L;
			}
			else if ("S".equalsIgnoreCase(D)) {
				disY[H1][H2] = -1 * L;
				disY[H2][H1] = L;
			}
			else if ("N".equalsIgnoreCase(D)) {
				disY[H1][H2] = L;
				disY[H2][H1] = -1 * L;
			}
		}

	}

	private static boolean dfsa(int S, int T)
	{
		for (int i = 1; i < N + 1; i++) {
			if (disX[S][i] != 0 || disY[S][i] != 0) {
				int next = i;
				if (next == T) {
					order[tag++] = T;
					return true;
				}
				if (visit[next] == false) {
					visit[next] = true;
					if (dfsa(next, T) == true) {
						order[tag++] = next;
						return true;
					}
				}
			}
		}
		return false;
	}
}

class DisjointSets
{
	public static int	count;	// 总数
	public static int[]	parent;
	public static int[]	rank;

	// 忽略INDEX 0
	public static void makeSetFrom1(int cnt)
	{
		count = cnt;
		parent = new int[count + 1];
		rank = new int[count + 1];
		for (int i = 1; i <= count; i++) {
			parent[i] = i;
			rank[i] = 1;
		}
	}

	public static void makeSetFrom0(int cnt)
	{
		count = cnt;
		parent = new int[count];
		rank = new int[count];
		for (int i = 0; i < count; i++) {
			parent[i] = i;
			rank[i] = 1;
		}
	}

	public static int findSet(int x)
	{
		if (x != parent[x]) {
			parent[x] = findSet(parent[x]);
		}
		return parent[x];
	}

	public static void Union(int x, int y)
	{
		int i = findSet(x);
		int j = findSet(y);
		if (rank[i] > rank[j]) {
			parent[j] = i;
			rank[i] = rank[i] + rank[j];
		}
		else {
			parent[i] = j;
			rank[j] = rank[i] + rank[j];
		}
	}
}
