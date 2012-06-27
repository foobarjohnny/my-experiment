package geometry;

import java.util.*;

/**
 * http://softsurfer.com/Archive/algorithm_0109/algorithm_0109.htm
 * http://www.cnblogs.com/Booble/archive/2011/03/10/1980089.html.
 * 
 * 
 * @author SDY
 */
// Gift wrapping ==
// In the two-dimensional case the algorithm is also known as Jarvis's march
public class JarvisMarch
{

	// Algorithm Speed Discovered By
	// Brute Force O(n4) [Anon, the dark ages]
	// Gift Wrapping O(nh) [Chand & Kapur, 1970]
	// Graham Scan O(n log n) [Graham, 1972]
	// Jarvis March O(nh) [Jarvis, 1973]
	// QuickHull O(nh) [Eddy, 1977], [Bykat, 1978]
	// Divide-and-Conquer O(n log n) [Preparata & Hong, 1977]
	// Monotone Chain O(n log n) [Andrew, 1979]
	// Incremental O(n log n) [Kallay, 1984]
	// Marriage-before-Conquest O(n log h) [Kirkpatrick & Seidel, 1986]

	// O(nh)
	private static final double	MAX_ANGLE	= 4;

	public static ArrayList<Point> jarvisMarch(Point[] pts)
	{
		// initializeHull();
		ArrayList<Point> ch = new ArrayList<Point>();

		Point startingPoint = getStartingPoint(pts);
		ch.add(startingPoint);

		double currentAngle = 0;
		Point p = startingPoint;
		Point next;
		currentAngle = 0;
		while ((next = getNextPoint(pts, p, startingPoint, currentAngle)) != startingPoint) {
			currentAngle = relativeAngle(next, p);
			p = next;
			ch.add(p);
		}
		return ch;
	}

	/**
	 * The Jarvis March, sometimes known as the Gift Wrap Algorithm. The next
	 * point is the point with the next largest angle.
	 * <p/>
	 * Imagine wrapping a string around a set of nails in a board. Tie the
	 * string to the leftmost nail and hold the string vertical. Now move the
	 * string clockwise until you hit the next, then the next, then the next.
	 * When the string is vertical again, you will have found the hull.
	 */
	public static Point getStartingPoint(Point[] pts)
	{
		int x = 0;
		for (int i = 0; i <= pts.length - 1; i++) {
			if (QuickHull.cmp(pts[i], pts[x])) {
				x = i;
			}
		}
		return pts[x];
	}

	private static Point getNextPoint(Point[] pts, Point p, Point startingPoint, double currentAngle)
	{
		double minAngle = MAX_ANGLE;
		Point minP = startingPoint;
		for (int i = 0; i < pts.length; i++) {
			if (pts[i] != p) {
				double thisAngle = relativeAngle(pts[i], p);
				if (thisAngle >= currentAngle && thisAngle <= minAngle) {
					minP = pts[i];
					minAngle = thisAngle;
				}
			}
		}
		return minP;
	}

	private static double relativeAngle(Point next, Point p)
	{
		return pseudoAngle(next.x - p.x, next.y - p.y);
	}

	/**
	 * The PseudoAngle is a number that increases as the angle from vertical
	 * increases. The current implementation has the maximum pseudo angle < 4.
	 * The pseudo angle for each quadrant is 1. The algorithm is very simple. It
	 * just finds where the angle intesects a square and measures the perimeter
	 * of the square at that point. The math is in my Sept '06 notebook.
	 * UncleBob.
	 */
	// CLOCK WISE
	public static double pseudoAngle2(double dx, double dy)
	{
		if (dx >= 0 && dy >= 0)
			return quadrantOnePseudoAngle(dx, dy);
		if (dx >= 0 && dy < 0)
			return 1 + quadrantOnePseudoAngle(Math.abs(dy), dx);
		if (dx < 0 && dy < 0)
			return 2 + quadrantOnePseudoAngle(Math.abs(dx), Math.abs(dy));
		if (dx < 0 && dy >= 0)
			return 3 + quadrantOnePseudoAngle(dy, Math.abs(dx));
		throw new Error("Impossible");
	}

	// COUNTER CLOCK WISE
	public static double pseudoAngle(double dx, double dy)
	{
		if (dx >= 0 && dy >= 0)
			return 1 + quadrantOnePseudoAngle(dy, dx);
		if (dx >= 0 && dy < 0)
			return quadrantOnePseudoAngle(dx, Math.abs(dy));
		if (dx < 0 && dy < 0)
			return 3 + quadrantOnePseudoAngle(Math.abs(dy), Math.abs(dx));
		if (dx < 0 && dy >= 0)
			return 2 + quadrantOnePseudoAngle(Math.abs(dx), dy);
		throw new Error("Impossible");
	}

	public static double quadrantOnePseudoAngle(double dx, double dy)
	{
		return dx / (dy + dx);
		// tan dy+dxÆäÊµ¸údy^2+dx^2 monotone
	}

	public static void main(String[] args)
	{
	}
}
