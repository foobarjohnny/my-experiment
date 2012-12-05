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
public class p1304_correct
{
	private static int		N, M, K;
	private static House[]	houses;
	private static Path[]	paths;

	private static int find(int x)
	{
		if (x == houses[x].parent) {
			return houses[x].parent;
		}
		int t = find(houses[x].parent);
		houses[x].x += houses[houses[x].parent].x;
		houses[x].y += houses[houses[x].parent].y;
		houses[x].parent = t;
		return houses[x].parent;
	}// 查找根节点，路径压缩，同时因为合并集合的时候，只是将某一个集合的根节点合并到另一个集合，即只修改了根节点的相对坐标
		// 所以查找根节点的过程，修改节点的相对坐标，注意体会这个递归的过程

	private static void Union(int u, int v, int w, String s)
	{
		int a = find(u);
		int b = find(v);
		houses[b].parent = a;// 将集合b并入集合a
		int tempX = houses[v].x, tempY = houses[v].y;
		char ch = s.charAt(0);
		switch (ch)// 这个比较好理解
		{
			case 'E':
				houses[b].x = houses[u].x + w;
				houses[b].y = houses[u].y;
				break;
			case 'W':
				houses[b].x = houses[u].x - w;
				houses[b].y = houses[u].y;
				break;
			case 'N':
				houses[b].y = houses[u].y + w;
				houses[b].x = houses[u].x;
				break;
			case 'S':
				houses[b].y = houses[u].y - w;
				houses[b].x = houses[u].x;
				break;
		}
		houses[b].x -= tempX;// 求的是U 所在集合根节点相对于v的坐标
		houses[b].y -= tempY;
	}

	public static void main(String[] args)
	{
		int i, j, s, a, b;
		Scanner scanner = new Scanner(System.in);
		N = scanner.nextInt();
		M = scanner.nextInt();
		houses = new House[N + 1];
		for (i = 1; i <= N; i++) {
			houses[i] = new House();
			houses[i].parent = i;
			houses[i].x = houses[i].y = 0;
		}
		paths = new Path[M + 1];
		for (i = 1; i <= M; i++) {
			paths[i] = new Path();
			paths[i].s = scanner.nextInt();
			paths[i].t = scanner.nextInt();
			paths[i].L = scanner.nextInt();
			paths[i].D = scanner.next();
		}
		s = 0;
		K = scanner.nextInt();
		while (K-- > 0) {
			a = scanner.nextInt();
			b = scanner.nextInt();
			j = scanner.nextInt();
			while (s < j)// 这里因为查询位置是递增的，所以只要s比查询位置小，则继续合并
			{
				s++;
				Union(paths[s].s, paths[s].t, paths[s].L, paths[s].D);
			}
			if (find(a) != find(b))// 不在同一个集合，即某一个节点的具体坐标还不知道
			{
				System.out.println("-1");
			}
			else {
				System.out.println(Math.abs(houses[a].x - houses[b].x) + Math.abs(houses[a].y - houses[b].y));// 求曼哈顿距离
			}
		}
	}
}

class House
{
	int	x, y, parent;
}

class Path
{
	int		s, t, L;
	// length and direction
	String	D;
}
