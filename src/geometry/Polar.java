package geometry;

/**
 */
public class Polar
{
	// 极角大小比较
	// P1 < P2 true
	// p1 >= p2 false
	public static boolean cmpAngle2(Vector v1, Vector v2)
	{
		double c = Vector.crossProduct(v1, v2);
		if (v1.y * v2.y == 0) {
			if (v1.y == 0 && v2.y == 0) {
				if (v1.x * v2.x <= 0) {
					// 这里定义的是 X>=0&&Y==0 极角==0
					// 两点X,Y都为0 极角相等
					// 某点X为0，意味着有可能极角差为0或180
					return v1.x > v2.x;
				}
				else {
					return false; // 极角相等
				}
			}
			// c不可能为0，因为p1.y和p2.y不同时为零 即极角差不可能为0或者180
			if (v1.y == 0 && v2.y != 0) {
				if (v1.x >= 0) {
					return true;// p1<p2
				}
				else {
					return c > 0;
				}
			}
			// c不可能为0，因为p1.y和p2.y不同时为零 即极角差不可能为0或者180
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
			// 极角差可能为180 不可能为0
			return v1.y > v2.y;
		}
		// 同Y 表明 C==0时 不可能相差180度，只能为相同
		if (v1.y * v2.y > 0) {
			return c > 0;
		}
		// 不可能走到这里来
		return false;
	}

	// 极角大小且距离比较，先极角再距离
	// P1 < P2 true
	// p1 >= p2 false
	// 绝对值不可去 因为比较的距离 即使相同符号
	public static boolean cmpAngleAndDis2(Vector v1, Vector v2)
	{
		double c = Vector.crossProduct(v1, v2);
		if (v1.y * v2.y == 0) {
			if (v1.y == 0 && v2.y == 0) {
				if (v1.x * v2.x <= 0) {
					// 这里定义的是 X>=0&&Y==0 极角==0
					// 两点X,Y都为0 极角相等
					// 某点X为0，意味着有可能极角差为0或180
					return v1.x > v2.x;
				}
				else {
					return Math.abs(v1.x) < Math.abs(v2.x); // 极角相等
				}
			}
			// c不可能为0，因为p1.y和p2.y不同时为零 即极角差不可能为0或者180
			if (v1.y == 0 && v2.y != 0) {
				if (v1.x >= 0) {
					return true;// p1<p2
				}
				else {
					return c > 0;
				}
			}
			// c不可能为0，因为p1.y和p2.y不同时为零 即极角差不可能为0或者180
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
			// 极角差可能为180 不可能为0
			return v1.y > v2.y;
		}
		// 同Y 表明 C==0时 不可能相差180度，只能为相同角度
		if (v1.y * v2.y > 0) {
			return c > 0 || c == 0 && Math.abs(v1.x) < Math.abs(v2.x);
		}
		// 不可能走到这里来
		return false;
	}

	// 极角大小且距离比较，先极角再距离
	// P1 < P2 true
	// p1 >= p2 false
	// 绝对值不可去 因为比较的距离 即使相同符号
	// cmpAngleAndDis2的精简版
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
		// 结合完整版，想想C==0时为啥这没有180的情况 以及为啥没删除绝对值
		return c > 0 || c == 0 && Math.abs(v1.x) < Math.abs(v2.x);
	}

	// 极角大小比较
	// P1 < P2 true
	// p1 >= p2 false
	// cmpAngle的精简版
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
		// 结合完整版，想想C==0时为啥这没有180的情况
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

	// 求角了
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

// SubStrFind.java（枚举算法的实现）

