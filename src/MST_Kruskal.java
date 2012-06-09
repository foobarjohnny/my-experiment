
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** 最小生成树的Kruskal算法(以无向图为例) */
public class MST_Kruskal
{
	public Map<V, Integer>	vertexMap	= new HashMap<V, Integer>();
	public List<E>			EQueue		= new ArrayList<E>();
	public static List<E>	EList		= new ArrayList<E>();

	public MST_Kruskal()
	{
		buildGraph();
		// 把边按权值非递减的排序
		Collections.sort(EQueue, new Comparator<E>()
		{
			@Override
			public int compare(E e1, E e2)
			{
				return e1.weight - e2.weight;
			}
		});
		// 贪心的构造生成树
		int i = 1;
		for (E edge : EQueue) {
			// i用来标记顶点是否属于同一个树
			i = i + 1;
			Set<V> vertexSet = vertexMap.keySet();
			// 关键是排除那些会构成回路的边，并且合并两个不相连的树
			if (!vertexSet.contains(edge.a) && !vertexSet.contains(edge.b)) {
				EList.add(edge);
				vertexMap.put(edge.a, i);
				vertexMap.put(edge.b, i);
			}
			else if (vertexSet.contains(edge.a) && vertexSet.contains(edge.b)) {
				if (vertexMap.get(edge.a) != vertexMap.get(edge.b)) {
					EList.add(edge);
					int n = vertexMap.get(edge.a);
					int m = vertexMap.get(edge.b);
					for (V v : vertexSet) {
						if (vertexMap.get(v) == m) {
							vertexMap.put(v, n);
						}
					}
					vertexMap.put(edge.b, n);
				}
			}
			else if (vertexSet.contains(edge.a) && !vertexSet.contains(edge.b)) {
				int n = vertexMap.get(edge.a);
				EList.add(edge);
				vertexMap.put(edge.b, n);
			}
			else if (vertexSet.contains(edge.b) && !vertexSet.contains(edge.a)) {
				int n = vertexMap.get(edge.b);
				EList.add(edge);
				vertexMap.put(edge.a, n);
			}
		}
	}

	public void addE(V a, V b, int w)
	{
		E e = new E(a, b, w);
		EQueue.add(e);
	}

	private void buildGraph()
	{
		V v1 = new V(1);
		V v2 = new V(2);
		V v3 = new V(3);
		V v4 = new V(4);
		V v5 = new V(5);
		V v6 = new V(6);
		addE(v1, v2, 5);
		addE(v1, v3, 8);
		addE(v1, v4, 1);
		addE(v2, v3, 4);
		addE(v2, v5, 2);
		addE(v3, v4, 7);
		addE(v4, v5, 6);
		addE(v5, v6, 5);
	}

	public static void main(String[] args)
	{
		new MST_Kruskal();
		for (E edge : EList) {
			System.out.println("[" + edge.a.key + "," + edge.b.key + "]" + edge.weight);
		}
	}
}

class V
{
	int	key;

	public V(int k)
	{
		key = k;
	}
}

class E
{
	int	weight;
	V	a;
	V	b;

	public E(V a2, V b2, int w)
	{
		a = a2;
		b = b2;
		weight = w;
	}
}