//弗洛伊德算法，插点法
//以无向图G为入口，得出任意两点之间的路径长度dist[i][j]，路径path[i][j][k]，
//途中无连接得点距离用0表示，点自身也用0表示
//O(V^3) V vetex E edge
//可以用来求传递闭包 把所有的权值赋为1
//也可以用布尔求值 来算闭包
//仔细回想一下矩阵算法重复平方 O(V^3*lnV)的区别 
//一个是增加边（点V在里面）然后循环(V-1)次增长边为(V-1)，一个是遍历点 V在外面
//矩阵算法可能在并行计算机中能被很好地优化，因为矩阵乘法在并行计算机中有很多优化办法
public class Floyd_Warshall
{
	int[][]		dist	= null; // 任意两点之间路径长度
	int[][][]	path	= null; // 任意两点之间的路径

	/**
	 * @param G
	 */
	public Floyd_Warshall(int[][] G)
	{
		int MAX = Integer.MAX_VALUE;
		int row = G.length;// 图G的行数
		int[][] spot = new int[row][row];// 定义任意两点之间经过的点
		int[] onePath = new int[row];// 记录一条路径
		dist = new int[row][row];
		path = new int[row][row][];
		for (int i = 0; i < row; i++)
			// 处理图两点之间的路径
			for (int j = 0; j < row; j++) {
				if (G[i][j] == 0) {
					G[i][j] = MAX;// 没有路径的两个点之间的路径为默认最大
				}
				if (i == j) {
					G[i][j] = 0;// 本身的路径长度为0
				}
			}
		for (int i = 0; i < row; i++) {
			// 初始化为任意两点之间没有路径
			for (int j = 0; j < row; j++) {
				spot[i][j] = -1;
			}
		}
		for (int i = 0; i < row; i++) {
			// 假设任意两点之间的没有路径
			onePath[i] = -1;
		}
		for (int v = 0; v < row; ++v) {
			for (int w = 0; w < row; ++w) {
				dist[v][w] = G[v][w];
			}
		}
		for (int u = 0; u < row; ++u) {
			for (int v = 0; v < row; ++v) {
				for (int w = 0; w < row; ++w) {
					// 防止溢出
					if (dist[v][u] != MAX && dist[u][w] != MAX && dist[v][w] > dist[v][u] + dist[u][w]) {
						dist[v][w] = dist[v][u] + dist[u][w];// 如果存在更短路径则取更短路径
						spot[v][w] = u;// 把经过的点加入
					}
				}
			}
		}

		/**
		 * 上面是精简形式，把空间从O(V^3)节省到O(V^2) int i, j, k; for (i = 1; i <= n; i++) {
		 * for (j = 1; j <= n; j++) { distance[i][j][0] = map[i][j]; } } for (k
		 * = 1; k <= n; k++) { for (i = 1; i <= n; i++) { for (j = 1; j <= n;
		 * j++) { distance[i][j][k] = distance[i][j][k - 1]; if
		 * (distance[i][k][k - 1] + distance[k][j][k - 1] < distance[i][j][k])
		 * distance[i][j][k] = distance[i][k][k - 1] + distance[k][j][k - 1]; }
		 * } }
		 */

		for (int i = 0; i < row; i++) {
			// 求出所有的路径
			int[] point = new int[1];
			for (int j = 0; j < row; j++) {
				point[0] = 0;
				onePath[point[0]++] = i;
				outputPath(spot, i, j, onePath, point);
				path[i][j] = new int[point[0]];
				for (int s = 0; s < point[0]; s++) {
					path[i][j][s] = onePath[s];
				}
			}
		}
	}

	void outputPath(int[][] spot, int i, int j, int[] onePath, int[] point)
	{
		// 输出i 到j 的路径的实际代码，point[]记录一条路径的长度
		if (i == j) {
			return;
		}
		if (spot[i][j] == -1) {
			onePath[point[0]++] = j;
		}
		// System.out.print(" "+j+" ");
		else {
			outputPath(spot, i, spot[i][j], onePath, point);
			outputPath(spot, spot[i][j], j, onePath, point);
		}
	}

	public static void main(String[] args)
	{
		int data[][] = { { 0, 27, 44, 17, 11, 27, 42, 0, 0, 0, 20, 25, 21, 21, 18, 27, 0 },// x1
				{ 27, 0, 31, 27, 49, 0, 0, 0, 0, 0, 0, 0, 52, 21, 41, 0, 0 },// 1
				{ 44, 31, 0, 19, 0, 27, 32, 0, 0, 0, 47, 0, 0, 0, 32, 0, 0 },// 2
				{ 17, 27, 19, 0, 14, 0, 0, 0, 0, 0, 30, 0, 0, 0, 31, 0, 0 },// 3
				{ 11, 49, 0, 14, 0, 13, 20, 0, 0, 28, 15, 0, 0, 0, 15, 25, 30 },// 4
				{ 27, 0, 27, 0, 13, 0, 9, 21, 0, 26, 26, 0, 0, 0, 28, 29, 0 },// 5
				{ 42, 0, 32, 0, 20, 9, 0, 13, 0, 32, 0, 0, 0, 0, 0, 33, 0 },// 6
				{ 0, 0, 0, 0, 0, 21, 13, 0, 19, 0, 0, 0, 0, 0, 0, 0, 0 },// 7
				{ 0, 0, 0, 0, 0, 0, 0, 19, 0, 11, 20, 0, 0, 0, 0, 33, 21 },// 8
				{ 0, 0, 0, 0, 28, 26, 32, 0, 11, 0, 10, 20, 0, 0, 29, 14, 13 },// 9
				{ 20, 0, 47, 30, 15, 26, 0, 0, 20, 10, 0, 18, 0, 0, 14, 9, 20 },// 10
				{ 25, 0, 0, 0, 0, 0, 0, 0, 0, 20, 18, 0, 23, 0, 0, 14, 0 },// 11
				{ 21, 52, 0, 0, 0, 0, 0, 0, 0, 0, 0, 23, 0, 27, 22, 0, 0 },// 12
				{ 21, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 27, 0, 0, 0, 0 },// 13
				{ 18, 41, 32, 31, 15, 28, 0, 0, 0, 29, 14, 0, 22, 0, 0, 11, 0 },// 14
				{ 27, 0, 0, 0, 25, 29, 33, 0, 33, 14, 9, 14, 0, 0, 11, 0, 9 },// 15
				{ 0, 0, 0, 0, 30, 0, 0, 0, 21, 13, 20, 0, 0, 0, 0, 9, 0 } // 16
		};
		for (int i = 0; i < data.length; i++) {
			for (int j = i; j < data.length; j++) {
				if (data[i][j] != data[j][i]) {
					return;
				}
			}
		}
		Floyd_Warshall test = new Floyd_Warshall(data);
		for (int i = 0; i < data.length; i++) {
			for (int j = i; j < data[i].length; j++) {
				System.out.println();
				System.out.print("From " + i + " to " + j + " path is: ");
				for (int k = 0; k < test.path[i][j].length; k++) {
					System.out.print(test.path[i][j][k] + " ");
				}
				System.out.println();
				System.out.println("From " + i + " to " + j + " length :" + test.dist[i][j]);
			}
		}
	}
}