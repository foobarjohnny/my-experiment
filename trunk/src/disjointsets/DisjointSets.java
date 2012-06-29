package disjointsets;

//并查集
public class DisjointSets
{
	public static int	count;	// 总数
	public static int[]	parent;
	public static int[]	rank;

	public static void main(String[] args)
	{
	}

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