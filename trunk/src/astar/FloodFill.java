package astar;

import java.util.LinkedList;

public class FloodFill
{

	private static int						H				= 0;
	private static int[][]					screenBuffer	= null;
	private static int						W				= 0;
	private static LinkedList<Accordinate>	STACK			= new LinkedList<Accordinate>();

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		H = 8;
		W = 9;
		screenBuffer = new int[W][H];
		floodFill8Stack(1, 1, 1, 0);
		printScreen();
		floodFill8(0, 0, 0, 1);
		printScreen();

		floodFillScanline(3, 2, 1, 0);
		printScreen();

		floodFillScanlineStack(4, 4, 0, 1);
		printScreen();
	}

	private static void printScreen()
	{
		for (int i = 0; i < screenBuffer.length; i++) {
			for (int j = 0; j < screenBuffer[i].length; j++) {
				System.out.print(screenBuffer[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println();
	}

	// typedef int ElemT;
	// const int SIZE = 9;
	// const int RANG = 9;
	//
	// int _fac[SIZE];
	//
	// void N2P2N_Init() {
	// int i; for (_fac[0] = 1, i = 1; i < SIZE; ++i) _fac[i] = _fac[i - 1] * i;
	// }
	//
	// void N2P(int n, ElemT x[]) {
	// ElemT t[SIZE];
	// int i, j, a[SIZE], m[SIZE] = {0};
	// for (i = 0; i < SIZE; ++i)
	// a[i] = n / _fac[SIZE - 1 - i], n %= _fac[SIZE - 1 - i];
	// for (i = 0; i < SIZE; ++i) {
	// for (j = -1; j < a[i]; ++j)
	// if (m[j + 1]) ++a[i];
	// t[i] = x[j], m[j] = 1;
	// }
	// copy(t, t + SIZE, x);
	// }
	//
	// int P2N(ElemT f[], ElemT x[]) {
	// ElemT t[SIZE];
	// int i, j, res, r[RANG];
	// for (i = 0; i < SIZE; ++i) r[f[i]] = i;
	// copy(x, x + SIZE, t);
	// for (i = 0, res = 0; i < SIZE; ++i) {
	// for (j = i + 1; j < SIZE; ++j)
	// if (r[t[i]] < r[t[j]]) --r[t[j]];
	// res += r[t[i]] * _fac[SIZE - 1 - i];
	// }
	// return res;
	// }
	// 一、4-Way Recursive Method（FloodFill4）
	// Recursive 4-way floodfill, crashes if recursion stack is full
	public static void floodFill4(int x, int y, int newColor, int oldColor)
	{
		if (x >= 0 && x < W && y >= 0 && y < H && screenBuffer[x][y] == oldColor && screenBuffer[x][y] != newColor) {
			screenBuffer[x][y] = newColor;
			// set color before starting recursion

			floodFill4(x + 1, y, newColor, oldColor);
			floodFill4(x - 1, y, newColor, oldColor);
			floodFill4(x, y + 1, newColor, oldColor);
			floodFill4(x, y - 1, newColor, oldColor);
		}
	}

	// 二、8-Way Recursive Method（FloodFill8）
	// Recursive 8-way floodfill, crashes if recursion stack is full
	public static void floodFill8(int x, int y, int newColor, int oldColor)
	{
		if (x >= 0 && x < W && y >= 0 && y < H && screenBuffer[x][y] == oldColor && screenBuffer[x][y] != newColor) {
			screenBuffer[x][y] = newColor;
			// set color before starting recursion!

			floodFill8(x + 1, y, newColor, oldColor);
			floodFill8(x - 1, y, newColor, oldColor);
			floodFill8(x, y + 1, newColor, oldColor);
			floodFill8(x, y - 1, newColor, oldColor);
			floodFill8(x + 1, y + 1, newColor, oldColor);
			floodFill8(x - 1, y - 1, newColor, oldColor);
			floodFill8(x - 1, y + 1, newColor, oldColor);
			floodFill8(x + 1, y - 1, newColor, oldColor);
		}
	}

	// 三、4-Way Method With Stack（FloodFill4Stack）
	// 4-way floodfill using our own stack routines
	public static void floodFill4Stack(int x, int y, int newColor, int oldColor)
	{
		if (newColor == oldColor) {
			return; // avoid infinite loop
		}
		emptyStack();

		if (!push(x, y)) {
			return;
		}
		Accordinate a = new Accordinate();
		while (pop(a)) {
			x = a.x;
			y = a.y;
			screenBuffer[x][y] = newColor;
			if (x + 1 < W && screenBuffer[x + 1][y] == oldColor) {
				if (!push(x + 1, y))
					return;
			}
			if (x - 1 >= 0 && screenBuffer[x - 1][y] == oldColor) {
				if (!push(x - 1, y))
					return;
			}
			if (y + 1 < H && screenBuffer[x][y + 1] == oldColor) {
				if (!push(x, y + 1))
					return;
			}
			if (y - 1 >= 0 && screenBuffer[x][y - 1] == oldColor) {
				if (!push(x, y - 1))
					return;
			}
		}
	}

	private static void emptyStack()
	{

		while (!STACK.isEmpty()) {
			STACK.pop();
		}
	}

	// 四、8-Way Method With Stack（FloodFill8Stack）
	// 4-way floodfill using our own stack routines
	public static void floodFill8Stack(int x, int y, int newColor, int oldColor)
	{
		if (newColor == oldColor)
			return; // avoid infinite loop
		emptyStack();

		if (!push(x, y)) {
			return;
		}
		Accordinate a = new Accordinate();
		while (pop(a)) {
			x = a.x;
			y = a.y;
			screenBuffer[x][y] = newColor;
			if (x + 1 < W && screenBuffer[x + 1][y] == oldColor) {
				if (!push(x + 1, y))
					return;
			}
			if (x - 1 >= 0 && screenBuffer[x - 1][y] == oldColor) {
				if (!push(x - 1, y))
					return;
			}
			if (y + 1 < H && screenBuffer[x][y + 1] == oldColor) {
				if (!push(x, y + 1))
					return;
			}
			if (y - 1 >= 0 && screenBuffer[x][y - 1] == oldColor) {
				if (!push(x, y - 1))
					return;
			}
			if (x + 1 < W && y + 1 < H && screenBuffer[x + 1][y + 1] == oldColor) {
				if (!push(x + 1, y + 1))
					return;
			}
			if (x + 1 < W && y - 1 >= 0 && screenBuffer[x + 1][y - 1] == oldColor) {
				if (!push(x + 1, y - 1))
					return;
			}
			if (x - 1 >= 0 && y + 1 < H && screenBuffer[x - 1][y + 1] == oldColor) {
				if (!push(x - 1, y + 1))
					return;
			}
			if (x - 1 >= 0 && y - 1 >= 0 && screenBuffer[x - 1][y - 1] == oldColor) {
				if (!push(x - 1, y - 1))
					return;
			}
		}
	}

	// 五、 Recursive Scanline Floodfill Algorithm(floodFillScanline)
	// stack friendly and fast floodfill algorithm
	public static void floodFillScanline(int x, int y, int newColor, int oldColor)
	{
		if (oldColor == newColor)
			return;
		if (screenBuffer[x][y] != oldColor)
			return;

		int y1;

		// draw current scanline from start position to the top
		y1 = y;
		while (y1 < H && screenBuffer[x][y1] == oldColor) {
			screenBuffer[x][y1] = newColor;
			y1++;
		}

		// draw current scanline from start position to the bottom
		y1 = y - 1;
		while (y1 >= 0 && screenBuffer[x][y1] == oldColor) {
			screenBuffer[x][y1] = newColor;
			y1--;
		}

		// test for new scanlines to the left
		y1 = y;
		while (y1 < H && screenBuffer[x][y1] == newColor) {
			if (x > 0 && screenBuffer[x - 1][y1] == oldColor) {
				floodFillScanline(x - 1, y1, newColor, oldColor);
			}
			y1++;
		}
		y1 = y - 1;
		while (y1 >= 0 && screenBuffer[x][y1] == newColor) {
			if (x > 0 && screenBuffer[x - 1][y1] == oldColor) {
				floodFillScanline(x - 1, y1, newColor, oldColor);
			}
			y1--;
		}

		// test for new scanlines to the right
		y1 = y;
		while (y1 < H && screenBuffer[x][y1] == newColor) {
			if (x < W - 1 && screenBuffer[x + 1][y1] == oldColor) {
				floodFillScanline(x + 1, y1, newColor, oldColor);
			}
			y1++;
		}
		y1 = y - 1;
		while (y1 >= 0 && screenBuffer[x][y1] == newColor) {
			if (x < W - 1 && screenBuffer[x + 1][y1] == oldColor) {
				floodFillScanline(x + 1, y1, newColor, oldColor);
			}
			y1--;
		}
	}

	// 六、Scanline Floodfill Algorithm With Stack(floodFillScanlineStack)
	// The scanline floodfill algorithm using our own stack routines, faster
	public static void floodFillScanlineStack(int x, int y, int newColor, int oldColor)
	{
		if (oldColor == newColor) {
			return;
		}
		emptyStack();

		int y1;
		boolean spanLeft, spanRight;

		if (!push(x, y)) {
			return;
		}
		Accordinate a = new Accordinate();
		while (pop(a)) {
			x = a.x;
			y = a.y;
			y1 = y;
			while (y1 >= 0 && screenBuffer[x][y1] == oldColor)
				y1--;
			y1++;
			spanLeft = spanRight = false;
			while (y1 < H && screenBuffer[x][y1] == oldColor) {
				screenBuffer[x][y1] = newColor;
				if (!spanLeft && x > 0 && screenBuffer[x - 1][y1] == oldColor) {
					if (!push(x - 1, y1))
						return;
					spanLeft = true;
				}
				// 写这一部分是防止因为同一列上有断开的段而造成的可能的“没填充”
				else if (spanLeft && x > 0 && screenBuffer[x - 1][y1] != oldColor) {
					spanLeft = false;
				}
				if (!spanRight && x < W - 1 && screenBuffer[x + 1][y1] == oldColor) {
					if (!push(x + 1, y1))
						return;
					spanRight = true;
				}
				// 写这一部分是防止因为同一列上有断开的段而造成的可能的“没填充”
				else if (spanRight && x < W - 1 && screenBuffer[x + 1][y1] != oldColor) {
					spanRight = false;
				}
				y1++;
			}
		}
	}

	private static boolean pop(Accordinate a)
	{
		if (!STACK.isEmpty()) {
			Accordinate b = STACK.poll();
			a.x = b.x;
			a.y = b.y;
			return true;
		}
		else {
			return false;

		}
	}

	private static boolean push(int x, int y)
	{
		STACK.push(new Accordinate(x, y));
		return true;
	}

	public static void clearScreenBuffer(int color)
	{
		for (int x = 0; x < W; x++) {
			for (int y = 0; y < H; y++) {
				screenBuffer[x][y] = color;
			}
		}
	}

}

class Accordinate
{
	int	x;
	int	y;

	public Accordinate(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public Accordinate()
	{
	}
}