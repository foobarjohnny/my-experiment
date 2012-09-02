package graph;

import java.util.Random;
//http://www.cnblogs.com/zhexue/archive/2011/12/27/2303859.html
//最短路径系列【最短路径、哈密顿路等】
//http://www.cnblogs.com/pandy/archive/2009/05/07/1451692.html [A STAR +dijkstra搜索 解决k短路径问题]
//http://blog.csdn.net/neupioneer/article/details/369737
//如果给定无孤立结点图G，若存在一条路，经过图中每边一次且仅一次，这条条路称为欧拉路；若存在一条回路，经过图中每边一次且仅一次，那么该回路称为欧拉回路。存在欧拉回路的图，称为欧拉图。对于无向图G，具有一条欧拉路，当且仅当G是连通的，且有零个或两个奇数度结点。
//有向图G具有一条单向欧拉路，当且仅当是可达的，且每个结点入度等于出度。一个有向图G具有单向欧拉路，当且仅当是可达的，而且除两个结点外，每个结点的入度等于出度，而这两个结点中，一个结点的入度比出度大1，另一个结点的入度比出度小1。
//汉密尔顿路 给定图G，若存在一条路，经过图中每个结点恰好一次，这条路称作汉密尔顿路。
//汉密尔顿回路 给定图G，若存在一条回路，经过图中每个结点恰好一次，这条回路称作汉密尔顿回路。
//汉密尔顿图 具有汉密尔顿回路的图，称作汉密尔顿图。
//定理 5．4．3 若图G=<V，E>具有汉密尔顿回路，则对于结点集V的每个非空子集S，均有 成立。
//其中W(G-S)是G-S 的连通分支数。
//定理 5．4．4 设G为具有n个结点的简单图，如果G中每一对结点度数之和大于等于n-1，则在G中存在一条汉密尔顿路。
//定理 5．4．5 设G是具有n个结点的简单图，如果G中每一对结点度数之和大于等于n，则在G中存在一条汉密尔顿回路。

//http://www.cnblogs.com/youxin/archive/2012/07/12/2588519.html
//哈密顿回路分支限界遍历法
public class Hamilton
{
	// http://kukumayas.iteye.com/blog/776301
	// public LinkedList<Integer> elem = new LinkedList<Integer>();//
	// 存放结点（结点均为int型从0开始）
	public int[]	elem;

	// 经测试,当节点数小于19时,数组的效率要快于链表

	public int		depth			= Matrix.data.length;	// 数组（放图的邻接矩阵）的长度即将要遍历的总深度

	public int		currentDepth	= 0;					// 放当前正在遍历的深度（层次）

	public int		count			= 0;					// 记录总共计算过的 从根到叶子的
															// 所有路径数

	// public LinkedList<Integer> best = null;// 存放当前最优路径
	public int[]	best;

	public int		leastLength		= 0x7fffffff;			// 存放当前最优路径（回路）的长度
															// 缺省为一个最大的int型

	private int		currentLength	= 0;					// 用于存放从根节点到当前结点的长度，用于同leastLength比较以实现分支限界法

	/**
	 * 构造函数 根据节点个数,建立一条链表,按照用自然数标记每个节点的序号,依次增1 在把所有节点放入链表后,最终要添加一个 0
	 * 节点,此处假设哈密顿回路从 0开始,这样便于计算最后一个节点到出发节点的长度
	 */
	public Hamilton()
	{
		elem = new int[Matrix.data.length + 1];
		best = new int[Matrix.data.length + 1];
		for (int i = 0; i < Matrix.data.length; i++) {
			elem[i] = i;
		}
		elem[elem.length - 1] = 0;
		replace();
	}

	/**
	 * 核心递归方法 没有形参,但是通过各个实例变量 控制递归的层次以及判断结束的条件
	 * 
	 * 主要思路: 成员变量thisLength暂存父节点到自身节点的距离,加到currentLength上, 如果currentLength已经
	 * 大于最优路径长度,返回; 如果小于则判断是否是最深层,即是否将所有点都遍历了一遍,如果是,则把最后一个
	 * 节点到出发节点的长度finalLength加到currentLength,之后判断currentLength和 leastLength的关系.
	 * 如果不属于以上两种情况,那就是普通的节点,记录好各个变量,进行循环
	 */
	public void show()
	{
		currentDepth++;// 进入方法,首先标记层次加1
		int thisLength = 0;
		if (currentLength >= leastLength) {// 分支限界,如果当前

		}
		else if (currentDepth == depth) {// 如果已经遍历过所有节点,并且当前路径还没有超过最优,则进行最后的计算
			count++;
			int finalLength = lengthOf(elem, currentDepth - 1, currentDepth);
			currentLength += finalLength;

			if (currentLength < leastLength) {// 替换掉当前最优路径
				leastLength = currentLength;
				replace();
				// displayArray(elem);
				// System.out.print('\t' + currentLength + "\n");
			}
			currentLength -= finalLength;
		}
		for (int i = currentDepth; i < depth; i++) {
			// elem.add(depth - 1, elem.remove(currentDepth));
			curl(elem, currentDepth);
			thisLength = lengthOf(elem, currentDepth - 1, currentDepth);
			currentLength += thisLength;// 同currentDepth的+-
			show();
			currentLength -= thisLength;
		}
		currentDepth--;// 退出方法,要标记层次减1
	}

	/**
	 * 计算链表中 第offset到第end之间,节点的距离
	 * 
	 * @param ll
	 *            要计算的链表对象
	 * @param offset
	 *            进行计算的起始位置
	 * @param end
	 *            进行计算的结束位置
	 * @return 查Matrix.data 返回结果
	 */
	public int lengthOf(int[] array, int offset, int end)
	{
		int length = 0;
		for (int i = offset; i < end; i++) {
			length += Matrix.data[array[i]][array[i + 1]];
		}
		return length;
	}

	public void replace()
	{
		for (int i = 0; i < elem.length; i++) {
			best[i] = elem[i];
		}
	}

	void curl(int[] array, int index)
	{
		int a = array[index];
		for (int i = index + 1; i < array.length - 1; i++) {
			array[i - 1] = array[i];
		}
		array[array.length - 2] = a;
	}

	void displayArray(int[] array)
	{
		System.out.print('[');
		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i] + ((i == array.length - 1) ? "" : ","));
		}
		System.out.print(']');
	}
}// /:~

class Matrix
{
	private static Random	random		= new Random();

	public static int		data[][]	= { { 0x7fffffff, 15, 30, 20, 25, 60 }, { 15, 0x7fffffff, 10, 35, 45, 60 }, { 30, 10, 0x7fffffff, 40, 55, 60 }, { 20, 35, 40, 0x7fffffff, 50, 60 },
			{ 25, 45, 55, 50, 0x7fffffff, 60 }, { 60, 60, 60, 60, 60, 0x7fffffff } };

	static class Point
	{
		public int	x;

		public int	y;

		Point()
		{
			x = (random.nextInt(19) + 1) * 5;
			y = (random.nextInt(19) + 1) * 5;
		}
	}

	private static int distanceBetween(Point a, Point b)
	{
		return (int) Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
	}

	public static void generate(int size)
	{

		data = new int[size][size];
		Point points[] = new Point[size];
		for (int i = 0; i < size; i++) {
			points[i] = new Point();
		}
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (j == i) {
					data[i][j] = 0x7fffffff;
				}
				else if (j > i) {
					data[i][j] = distanceBetween(points[i], points[j]);
				}
				else if (j < i) {
					data[i][j] = data[j][i];
				}
			}
		}
		// for (int i = 0; i < size; i++) {
		// for (int j = 0; j < size; j++) {
		//
		// System.out.print(data[i][j] == 0x7fffffff ? "MAX\t" : data[i][j]
		// + "\t");
		// }
		// System.out.println();
		// }
	}

	public static void generate()
	{
		int size = 10;
		// int size = Integer.parseInt(JOptionPane.showInputDialog("输入数组大小"));
		generate(size);

	}

}// /:~  