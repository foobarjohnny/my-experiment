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
 * ֻ��Ҫ�ò��鼯������ Ȼ���õ�·��ѹ�� ����QUERY��INDEXĬ���ǵ����� ������ǵ����ģ��ǿ������������������Բ����ظ�������ͼ
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
	}// ���Ҹ��ڵ㣬·��ѹ����ͬʱ��Ϊ�ϲ����ϵ�ʱ��ֻ�ǽ�ĳһ�����ϵĸ��ڵ�ϲ�����һ�����ϣ���ֻ�޸��˸��ڵ���������
		// ���Բ��Ҹ��ڵ�Ĺ��̣��޸Ľڵ��������꣬ע���������ݹ�Ĺ���

	private static void Union(int u, int v, int w, String s)
	{
		int a = find(u);
		int b = find(v);
		houses[b].parent = a;// ������b���뼯��a
		int tempX = houses[v].x, tempY = houses[v].y;
		char ch = s.charAt(0);
		switch (ch)// ����ȽϺ����
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
		houses[b].x -= tempX;// �����U ���ڼ��ϸ��ڵ������v������
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
			while (s < j)// ������Ϊ��ѯλ���ǵ����ģ�����ֻҪs�Ȳ�ѯλ��С��������ϲ�
			{
				s++;
				Union(paths[s].s, paths[s].t, paths[s].L, paths[s].D);
			}
			if (find(a) != find(b))// ����ͬһ�����ϣ���ĳһ���ڵ�ľ������껹��֪��
			{
				System.out.println("-1");
			}
			else {
				System.out.println(Math.abs(houses[a].x - houses[b].x) + Math.abs(houses[a].y - houses[b].y));// �������پ���
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
