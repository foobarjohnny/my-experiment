package stringmatch;

/**
 * KMP算法的Java实现例子与测试、分析 http://www.isnowfy.com/kmp-and-extend-kmp/
 * http://my.oschina.net/YeanXu/blog/15612s
 * http://3214668848.blog.163.com/blog/static/48764919200910152452182/
 */
public class RabinKarp
{
	private static int	times	= 10;

	public static void test(String parStr, String subStr)
	{
		long start = 0;
		long end = 0;
		System.out.println("父串: " + parStr);
		System.out.println("子串: " + subStr);
		start = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			System.out.println(rabinKarp(parStr, subStr));
		}
		end = System.currentTimeMillis();
		System.out.println("Time for rabinKarp: " + (end - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			System.out.println(naiveSearch(parStr, subStr));
		}
		end = System.currentTimeMillis();
		System.out.println("Time for naiveSearch: " + (end - start));
		System.out.println("-------------------------------------");
	}

	// Rabin–Karp，即hash检索法：
	// 这个算法的核心思想是，通过hash值，我们可以一次匹配一整条字串，速度上要快很多。
	// 关键： 选择这样一种hash算法，使得从前一个hash值到后一个hash值仅需要常量的步骤。
	// 我这里实现的hash算法可以做到这点，但是有效性并不高，应该还有其他的hash算法可以更好了减少冲突的发生。
	// 预处理O(m),匹配时间最坏为O((n-m+1)m)
	// 期望匹配时间 O((n-m+1)+cm)=O(n+m) m<=n 则O(m)
	public static int rabinKarp(String content, String sub)
	{
		long hcontent = rshash(content.substring(0, sub.length()));
		long hsub = rshash(sub);
		boolean found = false;
		int count = 0;
		for (int i = 0; i < (content.length() - sub.length()); i++) {
			// hcontent = rshash(content.substring(i, sub.length() + i));
			if (hsub == hcontent) {
				if (sub.equals(content.substring(i, i + sub.length()))) {
					found = true;
					count++;
				}
			}
			hcontent = newhash(content, hcontent, i + 1, sub.length());
		}
		if (found) {
			return count;
		}
		return -1;
	}

	private static long rshash(String str)
	{
		int a = 63689;
		long hash = 0;
		for (int i = 0; i < str.length(); i++) {
			hash += a * str.charAt(i);
		}
		return hash;
	}

	private static long newhash(String str, long previous, int i, int length)
	{
		int a = 63689;
		long minHash = str.charAt(i - 1) * a;
		long plusHash = str.charAt(i + length - 1) * a;
		return (previous - minHash + plusHash);
	}

	// 朴素算法：时间复杂度为 O((n-m+1) m)
	public static int naiveSearch(String content, String sub)
	{
		int count = 0;
		boolean found = true;
		for (int i = 0; i < (content.length() - sub.length() + 1); i++) {
			for (int j = 0; j < sub.length(); j++) {
				if (content.charAt(i + j) != sub.charAt(j)) {
					break;
				}
				found = true;
				count++;
			}

		}
		if (found) {
			return count;
		}
		return -1;
	}

	public static void main(String[] args)
	{
		// 回退位置数组为P[0, 0, 0, 0, 0, 0]
		test("abcdeg, abcdeh, abcdef!这个会匹配1次", "abcdef");
		// 回退位置数组为P[0, 0, 1, 2, 3, 4]
		test("Test ititi ititit! Test ititit!这个会匹配2次", "ititit");
	}
}
