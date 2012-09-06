package graph.com.javaeye.rsrt;

public class GraphTest
{

	public static void main(String[] args)
	{
		Graph graph = new Graph(6);
		graph.addEdge(1, 0);
		graph.addEdge(2, 0);
		graph.addEdge(3, 0);
		graph.addEdge(1, 3);
		graph.addEdge(2, 3);
		graph.addEdge(3, 5);
		graph.addEdge(4, 2);
		graph.addEdge(4, 3);
		graph.addEdge(4, 5);

		graph.TopSort();
		int[] list = graph.getResult();
		System.out.println("拓扑排序的结果为：");
		for (int i : list) {

			System.out.print(i + "        ");
		}
	}

}