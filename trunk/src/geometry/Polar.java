package geometry;

/**
 */
public class Polar
{
	// ���Ǵ�С�Ƚ�
	// P1 < P2 true
	// p1 >= p2 false
	public static boolean cmpAngle2(Vector v1, Vector v2)
	{
		double c = Vector.crossProduct(v1, v2);
		if (v1.y * v2.y == 0) {
			if (v1.y == 0 && v2.y == 0) {
				if (v1.x * v2.x <= 0) {
					// ���ﶨ����� X>=0&&Y==0 ����==0
					// ����X,Y��Ϊ0 �������
					// ĳ��XΪ0����ζ���п��ܼ��ǲ�Ϊ0��180
					return v1.x > v2.x;
				}
				else {
					return false; // �������
				}
			}
			// c������Ϊ0����Ϊp1.y��p2.y��ͬʱΪ�� �����ǲ����Ϊ0����180
			if (v1.y == 0 && v2.y != 0) {
				if (v1.x >= 0) {
					return true;// p1<p2
				}
				else {
					return c > 0;
				}
			}
			// c������Ϊ0����Ϊp1.y��p2.y��ͬʱΪ�� �����ǲ����Ϊ0����180
			if (v2.y == 0 && v1.y != 0) {
				if (v2.x >= 0) {
					return false;// p1>p2
				}
				else {
					return c > 0;
				}
			}
		}
		if (v1.y * v2.y < 0) {
			// ���ǲ����Ϊ180 ������Ϊ0
			return v1.y > v2.y;
		}
		// ͬY ���� C==0ʱ ���������180�ȣ�ֻ��Ϊ��ͬ
		if (v1.y * v2.y > 0) {
			return c > 0;
		}
		// �������ߵ�������
		return false;
	}

	// ���Ǵ�С�Ҿ���Ƚϣ��ȼ����پ���
	// P1 < P2 true
	// p1 >= p2 false
	// ����ֵ����ȥ ��Ϊ�Ƚϵľ��� ��ʹ��ͬ����
	public static boolean cmpAngleAndDis2(Vector v1, Vector v2)
	{
		double c = Vector.crossProduct(v1, v2);
		if (v1.y * v2.y == 0) {
			if (v1.y == 0 && v2.y == 0) {
				if (v1.x * v2.x <= 0) {
					// ���ﶨ����� X>=0&&Y==0 ����==0
					// ����X,Y��Ϊ0 �������
					// ĳ��XΪ0����ζ���п��ܼ��ǲ�Ϊ0��180
					return v1.x > v2.x;
				}
				else {
					return Math.abs(v1.x) < Math.abs(v2.x); // �������
				}
			}
			// c������Ϊ0����Ϊp1.y��p2.y��ͬʱΪ�� �����ǲ����Ϊ0����180
			if (v1.y == 0 && v2.y != 0) {
				if (v1.x >= 0) {
					return true;// p1<p2
				}
				else {
					return c > 0;
				}
			}
			// c������Ϊ0����Ϊp1.y��p2.y��ͬʱΪ�� �����ǲ����Ϊ0����180
			if (v2.y == 0 && v1.y != 0) {
				if (v2.x >= 0) {
					return false;// p1>p2
				}
				else {
					return c > 0;
				}
			}
		}
		else if (v1.y * v2.y < 0) {
			// ���ǲ����Ϊ180 ������Ϊ0
			return v1.y > v2.y;
		}
		// ͬY ���� C==0ʱ ���������180�ȣ�ֻ��Ϊ��ͬ�Ƕ�
		if (v1.y * v2.y > 0) {
			return c > 0 || c == 0 && Math.abs(v1.x) < Math.abs(v2.x);
		}
		// �������ߵ�������
		return false;
	}

	// ���Ǵ�С�Ҿ���Ƚϣ��ȼ����پ���
	// P1 < P2 true
	// p1 >= p2 false
	// ����ֵ����ȥ ��Ϊ�Ƚϵľ��� ��ʹ��ͬ����
	// cmpAngleAndDis2�ľ����
	public static boolean cmpAngleAndDis(Vector v1, Vector v2)
	{
		if (v1.y == 0 && v2.y == 0 && v1.x * v2.x <= 0) {
			return v1.x > v2.x;
		}
		if (v1.y == 0 && v1.x >= 0 && v2.y != 0) {
			return true;
		}
		if (v2.y == 0 && v2.x >= 0 && v1.y != 0) {
			return false;
		}
		if (v1.y * v2.y < 0) {
			return v1.y > v2.y;
		}
		double c = Vector.crossProduct(v1, v2);
		// ��������棬����C==0ʱΪɶ��û��180����� �Լ�Ϊɶûɾ������ֵ
		return c > 0 || c == 0 && Math.abs(v1.x) < Math.abs(v2.x);
	}

	// ���Ǵ�С�Ƚ�
	// P1 < P2 true
	// p1 >= p2 false
	// cmpAngle�ľ����
	public static boolean cmpAngle(Vector v1, Vector v2)
	{
		if (v1.y == 0 && v2.y == 0 && v1.x * v2.x <= 0) {
			return v1.x > v2.x;
		}
		if (v1.y == 0 && v1.x >= 0 && v2.y != 0) {
			return true;
		}
		if (v2.y == 0 && v2.x >= 0 && v1.y != 0) {
			return false;
		}
		if (v1.y * v2.y < 0) {
			return v1.y > v2.y;
		}
		double c = Vector.crossProduct(v1, v2);
		// ��������棬����C==0ʱΪɶ��û��180�����
		return c > 0;
	}

	public static boolean cmpAngle(Point p1, Point p2)
	{
		Point o = new Point(0, 0);
		return cmpAngle(p1, p2, o);
	}

	private static boolean cmpAngle(Point p1, Point p2, Point o)
	{
		Vector v1 = new Vector(p1.x - o.x, p1.y - o.y);
		Vector v2 = new Vector(p2.x - o.x, p2.y - o.y);
		return cmpAngle(v1, v2);
	}

	public static boolean cmpAngleAndDis(Point p1, Point p2)
	{
		Point o = new Point(0, 0);
		return cmpAngleAndDis(p1, p2, o);
	}

	private static boolean cmpAngleAndDis(Point p1, Point p2, Point o)
	{
		Vector v1 = new Vector(p1.x - o.x, p1.y - o.y);
		Vector v2 = new Vector(p2.x - o.x, p2.y - o.y);
		return cmpAngleAndDis(v1, v2);
	}

	// �����
	public static boolean cmpAnglebyArctan(Vector v1, Vector v2)
	{
		return Math.atan2(v1.y, v1.x) < Math.atan2(v2.y, v2.x);
	}

	public static boolean cmpAngleAndDisbyArctan(Vector v1, Vector v2)
	{
		return Math.atan2(v1.y, v1.x) < Math.atan2(v2.y, v2.x) || Math.atan2(v1.y, v1.x) == Math.atan2(v2.y, v2.x) && Math.abs(v1.x) < Math.abs(v2.x);
	}

	public static boolean cmpAngle3(Vector v1, Vector v2)
	{
		// p1.y * p2.y < 0
		if (v1.y * v2.y < 0) {
			return v2.x * v1.y < v2.y * v1.x;
		}
		// p1.y * p2.y = 0
		else if (v1.y == 0) {
			if (v1.x > 0) {
				return true;
			}
			else {
				return v2.y < 0;
			}
		}
		else if (v2.y == 0) {
			if (v2.x > 0) {
				return false;
			}
			else {
				return v1.y > 0;
			}
		}
		// p1.y * p2.y > 0
		else {
			return v1.y > 0;
		}
	}

	public static void main(String[] args)
	{

	}

}

// SubStrFind.java��ö���㷨��ʵ�֣�

