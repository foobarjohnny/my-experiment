package astar;

import java.util.HashSet;
import java.util.Set;
//http://blog.csdn.net/em_num_cool/article/details/6748718
//F(N) = G(N) + H(N)
//其中，G(N)是起始状态S到当前状态的实际代价G(N)，它比最佳路径的代价G*(N)大。
//H(N)是从当前状态到目标状态T的实际代价的估算值H*(N)，它比实际代价H(N)大。
//对于一个起始状态到当前状态的实际路径，G(N)是已经决定的。H(N)的选择则至关重要。有两个限制：
//1. H(N)一定要小于等于当前结点N至目标结点T的实际代价。
//2. 任意结点的F值必须大于其父辈状态的F值，即F单调递增。
//当估价函数F满足以上两个限制条件，则启发式搜索算法一定能得出问题的最优解。

//IDA*搜索
//A*算法减少存储需求的最简单的方法就是将迭代深入的思想用在启发式搜索上，形成迭代深入A*算法，即IDA*。
//使用BFS搜索需要实用一个队列，这个队列如果过大就可能超出内存限制。用DFS代替BFS，可以减少存储需求。
//IDA*算法实际上是在迭代加深的深度优先搜索的基础上进行的一种改进。
//迭代加深的深度优先搜索使用当前搜索深度来判断是否停止搜索，
//而IDA*算法则使用A*算法的估价函数F(N) = G(N) + H(N)来判断是否停止搜索。
//可以知道，此时的G(N)就是当前搜索深度，H(N)则是对于从当前结点N至目标结点T的实际代价一种乐观估计。
//说H(N)乐观，是因为在设计估价函数的时候，H(N)被约束为小于等于实际代价。
//由此可知，相对于朴素的迭代加深的深度优先搜索，IDA*算法省去了一些不必要的搜索。

// A*算法与广度、深度优先和Dijkstra算法的联系就在于：
// 当G(n)=0时，该算法类似于DFS，
// 当H(n)=0时，该算法类似于BFS。
// 且同时，如果H(n)为0，只需求出G(n)，即求出起点到任意顶点n的最短路径  ，则转化为单源最短路径问题，即Dijkstra算法。
// 这一点，可以通过上面的A*搜索树的具体过程中将H(n)设为0或将G(n)设为0而得到。

//BFS（Breadth-First-Search 宽度优先搜索）
//首先将起始结点放入OPEN表，CLOSE表置空，算法开始时：
//  1、如果OPEN表不为空，从表中开始取一个结点S，如果为空算法失败
//  2、S是目标解吗？是，找到一个解（继续寻找，或终止算法）；不是到3
//  3、将S的所有后继结点展开，就是从S可以直接关联的结点（子结点），如果不在CLOSE表中，就将它们放入OPEN表[[[末尾]]]，而把S放入CLOSE表，重复算法到1。

//DFS（Depth-First-Search 深度优先搜索）
//首先将起始结点放入OPEN表，CLOSE表置空，算法开始时：
//  1、如果OPEN表不为空，从表中开始取一个结点S，如果为空算法失败
//  2、S是目标解吗？是，找到一个解（继续寻找，或终止算法）；不是到3
//  3、将S的所有后继结点展开，就是从S可以直接关联的结点（子结点），如果不在CLOSE表中，就将它们放入OPEN表[[[开始]]]，而把S放入CLOSE表，重复算法到1。

//是否有看出：上述的BFS和DFS有什么不同？
//  仔细观察OPEN表中待访问的结点的组织形式，BFS是从表头取结点，从表尾添加结点，也就是说OPEN表是一个队列，是的，BFS首先让你想到‘队列’；而DFS，它是从OPEN表头取结点，也从表头添加结点，也就是说OPEN表是一个栈！
//  DFS用到了栈，所以有一个很好的实现方法，那就是递归，系统栈是计算机程序中极重要的部分之一。用递归也有个好处就是，在系统栈中只需要存结点最大深度那么大的空间，也就是在展开一个结点的后续结点时可以不用一次全部展开，用一些环境变量记录当前的状态，在递归调用结束后继续展开。
/**
 * 排序可能是对OPEN表整体进行排序，也可以是对后续展开的子结点排序，排序的目的就是要使程序有启发性，更快的搜出目标解。
 * 
 * 如果估值函数只考虑结点的某种性能上的价值，而不考虑深度，比较有名的就是有序搜索（Ordered-Search），它着重看好能否找出解，
 * 而不看解离起始结点的距离（深度）。
 * 如果估值函数考虑了深度，或者是带权距离（从起始结点到目标结点的距离加权和），那就是A*，举个问题例子，八数码问题，如果不考虑深度
 * ，就是说不要求最少步数，移动一步就相当于向后多展开一层结点，深度多算一层，如果要求最少步数，那就需要用A*。
 * 简单的来说A*就是将估值函数分成两个部分，一个部分是路径价值，另一个部分是一般性启发价值，合在一起算估整个结点的价值。
 * 
 * 从A*的角度看前面的搜索方法，如果路径价值为0就是有序搜索，如果路径价值就用所在结点到起始结点的距离（深度）表示，而启发值为0，那就是BFS或者DFS，
 * 它们两刚好是个反的，BFS是从OPEN表中选一个深度最小的进行展开，
 * 而DFS是从OPEN表中选一个深度最大的进行展开。当然只有BFS才算是特殊的A*，所以BFS可以求要求路径最短的问题，只是没有任何启发性。
 * 下文稍后，会具体谈A*搜寻算法思想。
 * 
 * BFS、DFS、Kruskal、Prim、Dijkstra算法时间复杂度
 * 上面，既然提到了A*算法与广度、深度优先搜索算法的联系，那么，下面，也顺便再比较下BFS
 * 、DFS、Kruskal、Prim、Dijkstra算法时间复杂度吧： 一般说来，我们知道，BFS，DFS算法的时间复杂度为O（V+E），
 * 最小生成树算法Kruskal、Prim算法的时间复杂度为O（E*lgV）。
 * 而Prim算法若采用斐波那契堆实现的话，算法时间复杂度为O（E+V*lgV），当|V|<<|E|时，E+V*lgV是一个较大的改进。
 * //|V|<<|E|，=>O（E+V*lgV） << O（E*lgV），对吧。:D Dijkstra
 * 算法，斐波纳契堆用作优先队列时，算法时间复杂度为O（V*lgV + E）。 //看到了吧，与Prim算法采用斐波那契堆实现时，的算法时间复杂度是一样的。
 * 
 * 所以我们，说，BFS、Prime、Dijkstra 算法是有相似之处的，单从各算法的时间复杂度比较看，就可窥之一二。 
 * 
 */
public class Square
{

	private int			x;
	private int			y;
	private boolean		start;
	private boolean		end;

	// cost of getting from this square to goal
	private double		localCost;
	// cost of getting from parent square to this node
	private double		parentCost;
	// cost of getting from the start to the goal through this square
	private double		passThroughCost;
	private Maze		maze;
	private Set<Square>	adjacencies	= new HashSet<Square>();

	private Square		parent;

	public Square(int x, int y, Maze maze)
	{

		this.x = x;
		this.y = y;
		this.maze = maze;
	}

	public int getX()
	{

		return x;
	}

	public void setX(int x)
	{

		this.x = x;
	}

	public int getY()
	{

		return y;
	}

	public void setY(int y)
	{

		this.y = y;
	}

	public boolean isStart()
	{

		return start;
	}

	public void setStart(boolean start)
	{

		this.start = start;
	}

	public boolean isEnd()
	{

		return end;
	}

	public void setEnd(boolean end)
	{

		this.end = end;
	}

	public Set<Square> getAdjacencies()
	{

		return adjacencies;
	}

	public void setAdjacencies(Set<Square> adjacencies)
	{

		this.adjacencies = adjacencies;
	}

	public Square getParent()
	{

		return parent;
	}

	public void setParent(Square parent)
	{

		this.parent = parent;
	}

	public void calculateAdjacencies()
	{

		int top = x - 1;
		int bottom = x + 1;
		int left = y - 1;
		int right = y + 1;

		if (bottom < maze.getRows()) {
			if (isAdjacent()) {
				maze.getSquare(bottom, y).addAdjacency(this);
				this.addAdjacency(maze.getSquare(bottom, y));
			}
		}

		if (right < maze.getColumns()) {
			if (isAdjacent()) {
				maze.getSquare(x, right).addAdjacency(this);
				this.addAdjacency(maze.getSquare(x, right));
			}
		}
	}

	public void addAdjacency(Square square)
	{

		adjacencies.add(square);
	}

	public void removeAdjacency(Square square)
	{
		adjacencies.remove(square);
	}

	// F(N) = G(N) + H(N)
	public double getPassThrough(Square goal)
	{

		if (isStart()) {
			return 0.0;
		}

		return getLocalCost(goal) + getParentCost();
	}

	// 这其实是H(N)
	public double getLocalCost(Square goal)
	{

		if (isStart()) {
			return 0.0;
		}

		localCost = 1.0 * (Math.abs(x - goal.getX()) + Math.abs(y - goal.getY()));
		return localCost;
	}

	public double getParentCost()
	{

		// 其实PARENTCOST是G(N)
		if (isStart()) {
			return 0.0;
		}

		if (parentCost == 0.0) {
			parentCost = 1.0 + parent.getParentCost();
			// parentCost = 1.0 + .5 * (parent.getParentCost() - 1.0);
		}

		return parentCost;
	}

	public boolean isAdjacent()
	{

		if (Math.random() > .5) {
			return true;
		}
		return false;
	}

}
