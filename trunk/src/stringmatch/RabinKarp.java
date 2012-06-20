package stringmatch;

/**
 * KMP�㷨��Javaʵ����������ԡ����� http://www.isnowfy.com/kmp-and-extend-kmp/
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
		System.out.println("����: " + parStr);
		System.out.println("�Ӵ�: " + subStr);
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

	// Rabin�CKarp����hash��������
	// ����㷨�ĺ���˼���ǣ�ͨ��hashֵ�����ǿ���һ��ƥ��һ�����ִ����ٶ���Ҫ��ܶࡣ
	// �ؼ��� ѡ������һ��hash�㷨��ʹ�ô�ǰһ��hashֵ����һ��hashֵ����Ҫ�����Ĳ��衣
	// ������ʵ�ֵ�hash�㷨����������㣬������Ч�Բ����ߣ�Ӧ�û���������hash�㷨���Ը����˼��ٳ�ͻ�ķ�����
	// Ԥ����O(m),ƥ��ʱ���ΪO((n-m+1)m)
	// ����ƥ��ʱ�� O((n-m+1)+cm)=O(n+m) m<=n ��O(m)
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

	// �����㷨��ʱ�临�Ӷ�Ϊ O((n-m+1) m)
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
		// ����λ������ΪP[0, 0, 0, 0, 0, 0]
		test("abcdeg, abcdeh, abcdef!�����ƥ��1��", "abcdef");
		// ����λ������ΪP[0, 0, 1, 2, 3, 4]
		test("Test ititi ititit! Test ititit!�����ƥ��2��", "ititit");
	}
}
