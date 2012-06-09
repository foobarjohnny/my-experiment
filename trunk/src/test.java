import java.util.Scanner;

public class test
{
	int[][]	dp;
	int		n;

	public test()
	{
	}

	public void setN(int n)
	{
		this.n = n;
		dp = new int[n + 1][n + 1];
		dp[1][1] = dp[0][0] = 1;

		for (int i = 2; i <= n; i++) {
			for (int j = 1; j <= i; j++) {
				for (int k = 0; k <=(i-j>j?j:i-j); k++) {
					dp[i][j] += dp[i - j][k];
				}
			}
		}
	}

	public int solve()
	{
		int result = 0;
		for (int i = 1; i <= n; i++) {
			result += dp[n][i];
		}
		return result;
	}

	public void show()
	{
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j <= n; j++) {
				System.out.print(dp[i][j] + " ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		test a = new test();
		while (true) {
			a.setN(scanner.nextInt());
			a.show();
			System.out.println(a.solve());
		}
	}
}