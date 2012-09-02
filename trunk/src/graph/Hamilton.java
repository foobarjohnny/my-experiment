package graph;

import java.util.Random;
//http://www.cnblogs.com/zhexue/archive/2011/12/27/2303859.html
//���·��ϵ�С����·�������ܶ�·�ȡ�
//http://www.cnblogs.com/pandy/archive/2009/05/07/1451692.html [A STAR +dijkstra���� ���k��·������]
//http://blog.csdn.net/neupioneer/article/details/369737
//��������޹������ͼG��������һ��·������ͼ��ÿ��һ���ҽ�һ�Σ�������·��Ϊŷ��·��������һ����·������ͼ��ÿ��һ���ҽ�һ�Σ���ô�û�·��Ϊŷ����·������ŷ����·��ͼ����Ϊŷ��ͼ����������ͼG������һ��ŷ��·�����ҽ���G����ͨ�ģ�������������������Ƚ�㡣
//����ͼG����һ������ŷ��·�����ҽ����ǿɴ�ģ���ÿ�������ȵ��ڳ��ȡ�һ������ͼG���е���ŷ��·�����ҽ����ǿɴ�ģ����ҳ���������⣬ÿ��������ȵ��ڳ��ȣ�������������У�һ��������ȱȳ��ȴ�1����һ��������ȱȳ���С1��
//���ܶ���· ����ͼG��������һ��·������ͼ��ÿ�����ǡ��һ�Σ�����·�������ܶ���·��
//���ܶ��ٻ�· ����ͼG��������һ����·������ͼ��ÿ�����ǡ��һ�Σ�������·�������ܶ��ٻ�·��
//���ܶ���ͼ ���к��ܶ��ٻ�·��ͼ���������ܶ���ͼ��
//���� 5��4��3 ��ͼG=<V��E>���к��ܶ��ٻ�·������ڽ�㼯V��ÿ���ǿ��Ӽ�S������ ������
//����W(G-S)��G-S ����ͨ��֧����
//���� 5��4��4 ��GΪ����n�����ļ�ͼ�����G��ÿһ�Խ�����֮�ʹ��ڵ���n-1������G�д���һ�����ܶ���·��
//���� 5��4��5 ��G�Ǿ���n�����ļ�ͼ�����G��ÿһ�Խ�����֮�ʹ��ڵ���n������G�д���һ�����ܶ��ٻ�·��

//http://www.cnblogs.com/youxin/archive/2012/07/12/2588519.html
//���ܶٻ�·��֧�޽������
public class Hamilton
{
	// http://kukumayas.iteye.com/blog/776301
	// public LinkedList<Integer> elem = new LinkedList<Integer>();//
	// ��Ž�㣨����Ϊint�ʹ�0��ʼ��
	public int[]	elem;

	// ������,���ڵ���С��19ʱ,�����Ч��Ҫ��������

	public int		depth			= Matrix.data.length;	// ���飨��ͼ���ڽӾ��󣩵ĳ��ȼ���Ҫ�����������

	public int		currentDepth	= 0;					// �ŵ�ǰ���ڱ�������ȣ���Σ�

	public int		count			= 0;					// ��¼�ܹ�������� �Ӹ���Ҷ�ӵ�
															// ����·����

	// public LinkedList<Integer> best = null;// ��ŵ�ǰ����·��
	public int[]	best;

	public int		leastLength		= 0x7fffffff;			// ��ŵ�ǰ����·������·���ĳ���
															// ȱʡΪһ������int��

	private int		currentLength	= 0;					// ���ڴ�ŴӸ��ڵ㵽��ǰ���ĳ��ȣ�����ͬleastLength�Ƚ���ʵ�ַ�֧�޽編

	/**
	 * ���캯�� ���ݽڵ����,����һ������,��������Ȼ�����ÿ���ڵ�����,������1 �ڰ����нڵ���������,����Ҫ���һ�� 0
	 * �ڵ�,�˴�������ܶٻ�·�� 0��ʼ,�������ڼ������һ���ڵ㵽�����ڵ�ĳ���
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
	 * ���ĵݹ鷽�� û���β�,����ͨ������ʵ������ ���Ƶݹ�Ĳ���Լ��жϽ���������
	 * 
	 * ��Ҫ˼·: ��Ա����thisLength�ݴ游�ڵ㵽����ڵ�ľ���,�ӵ�currentLength��, ���currentLength�Ѿ�
	 * ��������·������,����; ���С�����ж��Ƿ��������,���Ƿ����е㶼������һ��,�����,������һ��
	 * �ڵ㵽�����ڵ�ĳ���finalLength�ӵ�currentLength,֮���ж�currentLength�� leastLength�Ĺ�ϵ.
	 * ��������������������,�Ǿ�����ͨ�Ľڵ�,��¼�ø�������,����ѭ��
	 */
	public void show()
	{
		currentDepth++;// ���뷽��,���ȱ�ǲ�μ�1
		int thisLength = 0;
		if (currentLength >= leastLength) {// ��֧�޽�,�����ǰ

		}
		else if (currentDepth == depth) {// ����Ѿ����������нڵ�,���ҵ�ǰ·����û�г�������,��������ļ���
			count++;
			int finalLength = lengthOf(elem, currentDepth - 1, currentDepth);
			currentLength += finalLength;

			if (currentLength < leastLength) {// �滻����ǰ����·��
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
			currentLength += thisLength;// ͬcurrentDepth��+-
			show();
			currentLength -= thisLength;
		}
		currentDepth--;// �˳�����,Ҫ��ǲ�μ�1
	}

	/**
	 * ���������� ��offset����end֮��,�ڵ�ľ���
	 * 
	 * @param ll
	 *            Ҫ������������
	 * @param offset
	 *            ���м������ʼλ��
	 * @param end
	 *            ���м���Ľ���λ��
	 * @return ��Matrix.data ���ؽ��
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
		// int size = Integer.parseInt(JOptionPane.showInputDialog("���������С"));
		generate(size);

	}

}// /:~  