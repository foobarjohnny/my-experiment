package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** ��С��������Kruskal�㷨(������ͼΪ��) */
//(��³˹�˶�)
public class MST_Kruskal
{
	public Map<V, Integer>	vertexMap	= new HashMap<V, Integer>();
	public List<E1>			EQueue		= new ArrayList<E1>();
	public static List<E1>	EList		= new ArrayList<E1>();

	public MST_Kruskal()
	{
		buildGraph();
		// �ѱ߰�Ȩֵ�ǵݼ�������
		Collections.sort(EQueue, new Comparator<E1>()
		{
			@Override
			public int compare(E1 e1, E1 e2)
			{
				return e1.weight - e2.weight;
			}
		});
		// ̰�ĵĹ���������
		int i = 1;
		// ���鼯
		for (E1 edge : EQueue) {
			// i������Ƕ����Ƿ�����ͬһ����
			i = i + 1;
			Set<V> vertexSet = vertexMap.keySet();
			// �ؼ����ų���Щ�ṹ�ɻ�·�ıߣ����Һϲ���������������
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
		E1 e = new E1(a, b, w);
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
		for (E1 edge : EList) {
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

class E1
{
	int	weight;
	V	a;
	V	b;

	public E1(V a2, V b2, int w)
	{
		a = a2;
		b = b2;
		weight = w;
	}
}