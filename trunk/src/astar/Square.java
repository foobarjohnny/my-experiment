package astar;

import java.util.HashSet;
import java.util.Set;
//http://blog.csdn.net/em_num_cool/article/details/6748718
//F(N) = G(N) + H(N)
//���У�G(N)����ʼ״̬S����ǰ״̬��ʵ�ʴ���G(N)���������·���Ĵ���G*(N)��
//H(N)�Ǵӵ�ǰ״̬��Ŀ��״̬T��ʵ�ʴ��۵Ĺ���ֵH*(N)������ʵ�ʴ���H(N)��
//����һ����ʼ״̬����ǰ״̬��ʵ��·����G(N)���Ѿ������ġ�H(N)��ѡ����������Ҫ�����������ƣ�
//1. H(N)һ��ҪС�ڵ��ڵ�ǰ���N��Ŀ����T��ʵ�ʴ��ۡ�
//2. �������Fֵ��������丸��״̬��Fֵ����F����������
//�����ۺ���F����������������������������ʽ�����㷨һ���ܵó���������Ž⡣

//IDA*����
//A*�㷨���ٴ洢�������򵥵ķ������ǽ����������˼����������ʽ�����ϣ��γɵ�������A*�㷨����IDA*��
//ʹ��BFS������Ҫʵ��һ�����У���������������Ϳ��ܳ����ڴ����ơ���DFS����BFS�����Լ��ٴ洢����
//IDA*�㷨ʵ�������ڵ��������������������Ļ����Ͻ��е�һ�ָĽ���
//��������������������ʹ�õ�ǰ����������ж��Ƿ�ֹͣ������
//��IDA*�㷨��ʹ��A*�㷨�Ĺ��ۺ���F(N) = G(N) + H(N)���ж��Ƿ�ֹͣ������
//����֪������ʱ��G(N)���ǵ�ǰ������ȣ�H(N)���Ƕ��ڴӵ�ǰ���N��Ŀ����T��ʵ�ʴ���һ���ֹ۹��ơ�
//˵H(N)�ֹۣ�����Ϊ����ƹ��ۺ�����ʱ��H(N)��Լ��ΪС�ڵ���ʵ�ʴ��ۡ�
//�ɴ˿�֪����������صĵ���������������������IDA*�㷨ʡȥ��һЩ����Ҫ��������

// A*�㷨���ȡ�������Ⱥ�Dijkstra�㷨����ϵ�����ڣ�
// ��G(n)=0ʱ�����㷨������DFS��
// ��H(n)=0ʱ�����㷨������BFS��
// ��ͬʱ�����H(n)Ϊ0��ֻ�����G(n)���������㵽���ⶥ��n�����·��  ����ת��Ϊ��Դ���·�����⣬��Dijkstra�㷨��
// ��һ�㣬����ͨ�������A*�������ľ�������н�H(n)��Ϊ0��G(n)��Ϊ0���õ���

//BFS��Breadth-First-Search �������������
//���Ƚ���ʼ������OPEN��CLOSE���ÿգ��㷨��ʼʱ��
//  1�����OPEN��Ϊ�գ��ӱ��п�ʼȡһ�����S�����Ϊ���㷨ʧ��
//  2��S��Ŀ������ǣ��ҵ�һ���⣨����Ѱ�ң�����ֹ�㷨�������ǵ�3
//  3����S�����к�̽��չ�������Ǵ�S����ֱ�ӹ����Ľ�㣨�ӽ�㣩���������CLOSE���У��ͽ����Ƿ���OPEN��[[[ĩβ]]]������S����CLOSE���ظ��㷨��1��

//DFS��Depth-First-Search �������������
//���Ƚ���ʼ������OPEN��CLOSE���ÿգ��㷨��ʼʱ��
//  1�����OPEN��Ϊ�գ��ӱ��п�ʼȡһ�����S�����Ϊ���㷨ʧ��
//  2��S��Ŀ������ǣ��ҵ�һ���⣨����Ѱ�ң�����ֹ�㷨�������ǵ�3
//  3����S�����к�̽��չ�������Ǵ�S����ֱ�ӹ����Ľ�㣨�ӽ�㣩���������CLOSE���У��ͽ����Ƿ���OPEN��[[[��ʼ]]]������S����CLOSE���ظ��㷨��1��

//�Ƿ��п�����������BFS��DFS��ʲô��ͬ��
//  ��ϸ�۲�OPEN���д����ʵĽ�����֯��ʽ��BFS�Ǵӱ�ͷȡ��㣬�ӱ�β��ӽ�㣬Ҳ����˵OPEN����һ�����У��ǵģ�BFS���������뵽�����С�����DFS�����Ǵ�OPEN��ͷȡ��㣬Ҳ�ӱ�ͷ��ӽ�㣬Ҳ����˵OPEN����һ��ջ��
//  DFS�õ���ջ��������һ���ܺõ�ʵ�ַ������Ǿ��ǵݹ飬ϵͳջ�Ǽ���������м���Ҫ�Ĳ���֮һ���õݹ�Ҳ�и��ô����ǣ���ϵͳջ��ֻ��Ҫ������������ô��Ŀռ䣬Ҳ������չ��һ�����ĺ������ʱ���Բ���һ��ȫ��չ������һЩ����������¼��ǰ��״̬���ڵݹ���ý��������չ����
/**
 * ��������Ƕ�OPEN�������������Ҳ�����ǶԺ���չ�����ӽ�����������Ŀ�ľ���Ҫʹ�����������ԣ�������ѳ�Ŀ��⡣
 * 
 * �����ֵ����ֻ���ǽ���ĳ�������ϵļ�ֵ������������ȣ��Ƚ������ľ�������������Ordered-Search���������ؿ����ܷ��ҳ��⣬
 * ������������ʼ���ľ��루��ȣ���
 * �����ֵ������������ȣ������Ǵ�Ȩ���루����ʼ��㵽Ŀ����ľ����Ȩ�ͣ����Ǿ���A*���ٸ��������ӣ����������⣬������������
 * ������˵��Ҫ�����ٲ������ƶ�һ�����൱������չ��һ���㣬��ȶ���һ�㣬���Ҫ�����ٲ������Ǿ���Ҫ��A*��
 * �򵥵���˵A*���ǽ���ֵ�����ֳ��������֣�һ��������·����ֵ����һ��������һ����������ֵ������һ������������ļ�ֵ��
 * 
 * ��A*�ĽǶȿ�ǰ����������������·����ֵΪ0�����������������·����ֵ�������ڽ�㵽��ʼ���ľ��루��ȣ���ʾ��������ֵΪ0���Ǿ���BFS����DFS��
 * �������պ��Ǹ����ģ�BFS�Ǵ�OPEN����ѡһ�������С�Ľ���չ����
 * ��DFS�Ǵ�OPEN����ѡһ��������Ľ���չ������Ȼֻ��BFS�����������A*������BFS������Ҫ��·����̵����⣬ֻ��û���κ������ԡ�
 * �����Ժ󣬻����̸A*��Ѱ�㷨˼�롣
 * 
 * BFS��DFS��Kruskal��Prim��Dijkstra�㷨ʱ�临�Ӷ�
 * ���棬��Ȼ�ᵽ��A*�㷨���ȡ�������������㷨����ϵ����ô�����棬Ҳ˳���ٱȽ���BFS
 * ��DFS��Kruskal��Prim��Dijkstra�㷨ʱ�临�ӶȰɣ� һ��˵��������֪����BFS��DFS�㷨��ʱ�临�Ӷ�ΪO��V+E����
 * ��С�������㷨Kruskal��Prim�㷨��ʱ�临�Ӷ�ΪO��E*lgV����
 * ��Prim�㷨������쳲�������ʵ�ֵĻ����㷨ʱ�临�Ӷ�ΪO��E+V*lgV������|V|<<|E|ʱ��E+V*lgV��һ���ϴ�ĸĽ���
 * //|V|<<|E|��=>O��E+V*lgV�� << O��E*lgV�����԰ɡ�:D Dijkstra
 * �㷨��쳲��������������ȶ���ʱ���㷨ʱ�临�Ӷ�ΪO��V*lgV + E���� //�����˰ɣ���Prim�㷨����쳲�������ʵ��ʱ�����㷨ʱ�临�Ӷ���һ���ġ�
 * 
 * �������ǣ�˵��BFS��Prime��Dijkstra �㷨��������֮���ģ����Ӹ��㷨��ʱ�临�ӶȱȽϿ����Ϳɿ�֮һ���� 
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

	// ����ʵ��H(N)
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

		// ��ʵPARENTCOST��G(N)
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
