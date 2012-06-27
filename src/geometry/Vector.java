package geometry;

public class Vector
{
	@Override
	public String toString()
	{
		// return "Vector [x=" + x + ", y=" + y + "]";
		return "v[" + x + ", " + y + "]";
	}

	double	x;
	double	y;

	public Vector(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	// p = point o =origin point
	public Vector(Point p, Point o)
	{
		this.x = p.x - o.x;
		this.y = p.y - o.y;
	}

	public static void main(String[] args)
	{

	}

	public static double crossProduct(Vector v1, Vector v2)
	{
		return (v1.x * v2.y - v2.x * v1.y);
	}

	public static double direction2(Point p, Point p1, Point p2)
	{
		return crossProduct(new Vector(p2, p), new Vector(p1, p));
	}

	// >0 p2是p1的顺时针 即 pp1p2为右转
	// AREA
	// CROSS
	// MULIFY
	public static double direction(Point p, Point p1, Point p2)
	{
		return (p2.x - p.x) * (p1.y - p.y) - (p2.y - p.y) * (p1.x - p.x);
	}

	public static boolean onSegment(Point p1, Point p2, Point p)
	{
		if (Math.min(p1.x, p2.x) <= p.x && p.x <= Math.max(p1.x, p2.x)) {
			if (Math.min(p1.y, p2.y) <= p.y && p.y <= Math.max(p1.y, p2.y)) {
				return true;
			}
		}
		return false;
	}

	public static boolean segmentIntersect(Point p1, Point p2, Point p3, Point p4)
	{
		double d1 = direction(p3, p4, p1);
		double d2 = direction(p3, p4, p2);
		double d3 = direction(p1, p2, p3);
		double d4 = direction(p1, p2, p4);
		if (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) && ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0))) {
			return true;
		}
		else if (d1 == 0 && onSegment(p3, p4, p1)) {
			return true;
		}
		else if (d2 == 0 && onSegment(p3, p4, p2)) {
			return true;
		}
		else if (d3 == 0 && onSegment(p1, p2, p3)) {
			return true;
		}
		else if (d4 == 0 && onSegment(p1, p2, p4)) {
			return true;
		}
		return false;
	}

	public static double dis(Point p1, Point p2)
	{
		return (Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y)));
	}
}
