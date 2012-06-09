package graph;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
// DFS BFS TOPSORT
// 图类

public class Graph
{

	private final int			MAX_VERTS	= 20;
	private ArrayList<Vertex>	vertexList;
	private int					adjMat[][];
	private int					nVerts;
	private LinkedList<Integer>	theStack;
	private LinkedList<Integer>	theQueue;
	private int					TIME		= 0;
	private LinkedList<String>	topoList;
	private LinkedList<String>	topoList2;

	/** Creates a new instance of Graph */
	public Graph()
	{
		vertexList = new ArrayList<Vertex>();
		adjMat = new int[MAX_VERTS][MAX_VERTS];
		nVerts = 0;
		for (int j = 0; j < MAX_VERTS; j++) {
			for (int k = 0; k < MAX_VERTS; k++) {
				adjMat[j][k] = 0;
			}
		}
		theStack = new LinkedList<Integer>();
		theQueue = new LinkedList<Integer>();
	}

	// 增加一个顶点
	public void addVertex(String lab)
	{
		vertexList.add(new Vertex(lab));
		nVerts = vertexList.size();
	}

	private void delVertex(int index)
	{
		if (0 <= index && index < nVerts) {
			vertexList.remove(index);

			for (int row = index; row < nVerts - 1; row++) {
				moveRowUp(row, nVerts - 1);
			}
			for (int col = index; col < nVerts - 1; col++) {
				moveColLeft(col, nVerts - 1);
			}
			for (int i = 0; i <= nVerts - 1; i++) {
				adjMat[nVerts - 1][i] = 0;
				adjMat[i][nVerts - 1] = 0;
			}

			nVerts = vertexList.size();
		}
	}

	// 增加一条边
	public void addEdge(int start, int end)
	{
		adjMat[start][end] = 1;
		// adjMat[end][start] = 1;
	}

	public void displayVertex(int i, boolean dfs)
	{
		Vertex v = vertexList.get(i);
		if (dfs) {
			System.out.print(i + "=" + v.label + "[" + v.dist + ":" + v.pre + "]" + "->");
		}
		else {
			if (v.color == Color.grey) {
				System.out.print("(" + v.dTime + ":" + i + "=" + v.label + " ");
			}
			else if (v.color == Color.black) {
				System.out.print(i + "=" + v.label + ":" + v.fTime + ") ");
			}
		}
	}

	public void visit()
	{
		TIME = 0;
		topoList = new LinkedList<String>();
		topoList2 = new LinkedList<String>();
		for (int i = 0; i < vertexList.size(); i++) {
			if (vertexList.get(i).isVisted() == false) {
				dfs(i);
				// bfs(i);
				System.out.println();
			}
		}
		for (int j = 0; j < vertexList.size(); j++) {
			vertexList.get(j).color = Color.white;
		}
	}

	// 深度优先搜索
	// E(u,v)
	// v: white -> tree edge -> d[u]<d[v]<f[v]<f[u]
	// v: grey -> back edge -> d[v]<=d[u]<f[u]<=f[v]
	// v: black - > forward edge -> d[u]<d[v]<f[v]<f[u]
	// v: black - > cross edge -> d[v]<f[v]<d[u]<f[u]
	// 无向图G DFS时要么树边 要么反向边
	public void dfs(int start)
	{
		vertexList.get(start).color = Color.grey;
		vertexList.get(start).dTime = ++TIME;
		displayVertex(start, false);
		// topoList2 有问题 因为一出来就假设这个点没依赖了。。。
		topoList2.offer(vertexList.get(start).label);
		theStack.push(start);
		int v1, v2;
		while (!theStack.isEmpty()) {
			v1 = theStack.peek();
			v2 = getAdjUnvisitedVertex(theStack.peek());
			if (v2 == -1) {
				theStack.pop();
				vertexList.get(v1).color = Color.black;
				vertexList.get(v1).fTime = ++TIME;
				displayVertex(v1, false);
				topoList.offerFirst(vertexList.get(v1).label);
				// topoList没问题 是考虑到了交叉边 把后面生成的树放前面 就消掉了交叉边
			}
			else {
				vertexList.get(v2).color = Color.grey;
				vertexList.get(v2).dTime = ++TIME;
				topoList2.offer(vertexList.get(v2).label);
				displayVertex(v2, false);
				theStack.push(v2);
			}
		}
	}

	// 广度优先搜索
	public void bfs(int start)
	{
		vertexList.get(start).color = Color.grey;
		vertexList.get(start).dist = 0;
		displayVertex(start, true);
		theQueue.offer(start);
		int v1, v2;
		while (!theQueue.isEmpty()) {
			v1 = theQueue.poll();
			while ((v2 = getAdjUnvisitedVertex(v1)) != -1) {
				vertexList.get(v2).color = Color.grey;
				vertexList.get(v2).dist = vertexList.get(v1).dist + 1;
				vertexList.get(v2).pre = v1;
				displayVertex(v2, true);
				theQueue.offer(v2);
			}
			vertexList.get(v1).color = Color.black;
		}
	}

	// 得到与v顶点邻接且未访问过的顶点标号
	public int getAdjUnvisitedVertex(int v)
	{
		for (int j = 0; j < vertexList.size(); j++) {
			if (adjMat[v][j] == 1 && vertexList.get(j).color == Color.grey) {
				System.out.println("there is loop");
			}
			if (adjMat[v][j] == 1 && vertexList.get(j).isVisted() == false) {
				return j;
			}
		}
		return -1;
	}

	private int noPrecessors()
	{
		boolean isEdge;
		for (int col = 0; col < vertexList.size(); col++) {
			isEdge = false;
			for (int row = 0; row < vertexList.size(); row++) {
				if (adjMat[row][col] > 0) {
					isEdge = true;
					break;
				}
			}
			if (!isEdge) {
				return col;
			}
		}
		return -1;
	}

	/**
	 * 有向图拓扑
	 */
	public void poto()
	{
		int orig_nverts = vertexList.size();
		String[] sortedArray = new String[orig_nverts];
		while (vertexList.size() > 0) {
			int curIndex = noPrecessors();
			if (curIndex == -1) {
				System.out.println("Graph 有环");
				for (int j = orig_nverts - 1; j >= 0; j--) {
					System.out.print(sortedArray[j]);
				}
				return;
			}
			sortedArray[vertexList.size() - 1] = vertexList.get(curIndex).label;
			delVertex(curIndex);
		}
		for (int j = orig_nverts - 1; j >= 0; j--) {
			System.out.print(sortedArray[j]);
		}
	}

	public void topologySort_byDFS()
	{
		this.visit();
		System.out.println(this.topoList);
		System.out.println(this.topoList2);

	}

	private void moveRowUp(int row, int length)
	{
		for (int col = 0; col <= length; col++) {
			adjMat[row][col] = adjMat[row + 1][col];
		}
		// adjMat[row][length] = 0;
	}

	private void moveColLeft(int col, int length)
	{
		for (int row = 0; row <= length; row++) {
			adjMat[row][col] = adjMat[row][col + 1];
		}
		// adjMat[length][col] = 0;
	}

	public static void main(String[] args)
	{
		Graph g = new Graph();
		g.addVertex("A");
		g.addVertex("B");
		g.addVertex("C");
		g.addVertex("D");
		g.addVertex("E");
		g.addVertex("F");
		g.addVertex("G");
		g.addVertex("H");

		g.addEdge(7, 3);
		g.addEdge(7, 4);
		g.addEdge(1, 4);
		g.addEdge(2, 5);
		g.addEdge(3, 6);
		g.addEdge(4, 6);
		g.addEdge(5, 0);
		g.addEdge(6, 0);
		g.addEdge(0, 6);

		g.topologySort_byDFS();
		g.poto();
	}
}

// 顶点类
class Vertex
{
	public String	label;
	public Color	color;
	public int		dTime	= 0;
	public int		fTime	= 0;
	public int		dist	= Integer.MAX_VALUE;
	public int		pre		= -1;

	public Vertex(String label)
	{
		this.label = label;
		this.color = Color.white;
	}

	public boolean isVisted()
	{
		if (this.color == Color.white) {
			return false;
		}
		return true;
	}
}

enum Color
{
	white, grey, black
}
