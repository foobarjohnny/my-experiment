package numbertheory;

/**
 */
public class GreatestCommonDivisor
{

	// 辗转相除法
	public static int gcd(int x, int y)
	{
		if (x < y) {
			return gcd(y, x);
		}
		if (y == 0) {
			return x;
		}
		else {
			return gcd(x - y, y);
		}
	}

	// 最原始
	public static int gcd1(int x, int y)
	{
		return (y == 0) ? x : gcd1(y, x % y);
	}

	// 2
	public static int gcd3(int x, int y)
	{
		if (x < y) {
			return gcd3(y, x);
		}
		if (y == 0) {
			return x;
		}
		else {
			if (isEven(x)) {
				if (isEven(y)) {
					return (gcd3(x >> 1, y >> 1) << 1);
				}
				else {
					return gcd3(x >> 1, y);
				}
			}
			else {
				if (isEven(y)) {
					return (gcd3(x, y >> 1));
				}
				else {
					return gcd3(y, x - y);
				}
			}
		}
	}

	private static boolean isEven(int x)
	{
		if (x % 2 != 0) {
			return false;
		}
		return true;
	}

	public static void main(String[] args)
	{

	}
}
