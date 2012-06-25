package geometry;

public class Point
{
	double	x;
	double	y;

	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public static void main(String[] args)
	{

	}

	@Override
	public String toString()
	{
		// return "Point [x=" + x + ", y=" + y + "]";
		return "[" + x + ", " + y + "]";
	}

}
