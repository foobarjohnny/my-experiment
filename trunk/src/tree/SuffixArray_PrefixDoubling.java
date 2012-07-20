package tree;

/**
 * http://www.cnblogs.com/staginner/archive/2012/02/02/2335600.html
 * http://tieba.baidu.com/f?kz=754580296
 * http://blog.csdn.net/lwl_ls/article/details/1903166
 * http://hi.baidu.com/fhnstephen/blog/item/4b20757c37245d0429388a76.html
 * 
 * Build Suffix Array using Prefix Doubling Algorithm see also: Udi Manber and
 * Gene Myers' seminal paper(1991):
 * "Suffix arrays: A new method for on-line string searches"
 * 
 * @author dysong
 *         http://www.cnblogs.com/ljsspace/archive/2011/07/18/2109196.html
 * 
 *         RANK-K 中间是允许有相等值的 如果没有相等值 那就意味着RANK不用再排了
 */
public class SuffixArray_PrefixDoubling
{
	public static final char	MAX_CHAR	= '\u00FF';

	class Suffix
	{
		int[]	sa;
		// Note: the p-th suffix in sa: SA[rank[p]-1]];
		// p is the index of the array "rank", start with 0;
		// a text S's p-th suffix is S[p..n], n=S.length-1.
		int[]	rank;
		boolean	done;
	}

	// a prefix of suffix[isuffix] represented with digits
	class Tuple
	{
		int		isuffix;	// the p-th suffix
		int[]	digits;

		public Tuple(int suffix, int[] digits)
		{
			this.isuffix = suffix;
			this.digits = digits;
		}

		public String toString()
		{
			StringBuffer sb = new StringBuffer();
			sb.append(isuffix);
			sb.append("(");
			for (int i = 0; i < digits.length; i++) {
				sb.append(digits[i]);
				if (i < digits.length - 1)
					sb.append("-");
			}
			sb.append(")");
			return sb.toString();
		}
	}

	// the plain counting sort algorithm for comparison
	// A: input array
	// B: output array (sorted)
	// max: A value's range is 0...max
	public void countingSort(int[] A, int[] B, int max)
	{
		// init the counter array
		int[] C = new int[max + 1];
		for (int i = 0; i <= max; i++) {
			C[i] = 0;
		}
		// stat the count in A
		for (int j = 0; j < A.length; j++) {
			C[A[j]]++;
		}
		// process the counter array C
		for (int i = 1; i <= max; i++) {
			C[i] += C[i - 1];
		}
		// distribute the values in A to array B
		for (int j = A.length - 1; j >= 0; j--) {
			// C[A[j]] <= A.length
			B[--C[A[j]]] = A[j];
		}
	}

	// d: the digit to do countingsort
	// max: A value's range is 0...max
	private void countingSort(int d, Tuple[] tA, Tuple[] tB, int max)
	{
		// init the counter array
		int[] C = new int[max + 1];
		for (int i = 0; i <= max; i++) {
			C[i] = 0;
		}
		// stat the count
		for (int j = 0; j < tA.length; j++) {
			C[tA[j].digits[d]]++;
		}
		// process the counter array C
		for (int i = 1; i <= max; i++) {
			C[i] += C[i - 1];
		}
		// distribute the values
		for (int j = tA.length - 1; j >= 0; j--) {
			// C[A[j]] <= A.length
			tB[--C[tA[j].digits[d]]] = tA[j];
		}
	}

	// tA: input
	// tB: output for rank caculation
	private void radixSort(Tuple[] tA, Tuple[] tB, int max, int digitsLen)
	{
		int len = tA.length;
		int digitsTotalLen = tA[0].digits.length;

		// 基数排序是先排次关键字 再排主关键字
		for (int d = digitsTotalLen - 1, j = 0; j < digitsLen; d--, j++) {
			this.countingSort(d, tA, tB, max);
			// assign tB to tA
			if (j < digitsLen - 1) {
				for (int i = 0; i < len; i++) {
					tA[i] = tB[i];
				}
			}
		}
	}

	// max is the maximum value in any digit of TA.digits[], used for counting
	// sort
	// tA: input
	// tB: the place holder, reused between iterations
	private Suffix rank(Tuple[] tA, Tuple[] tB, int max, int digitsLen)
	{
		int len = tA.length;
		radixSort(tA, tB, max, digitsLen);

		int digitsTotalLen = tA[0].digits.length;

		// caculate rank and sa
		int[] sa = new int[len];
		sa[0] = tB[0].isuffix;

		int[] rank = new int[len];
		int r = 1; // rank starts with 1
		rank[tB[0].isuffix] = r;
		for (int i = 1; i < len; i++) {
			sa[i] = tB[i].isuffix;

			// 这是什么意思 去掉重复的？
			// K前缀
			// 的确要这样做 不然有些RANK-K相等的却因为排序關系有前后 影響下次加倍運算
			boolean equalLast = true;
			for (int j = digitsTotalLen - digitsLen; j < digitsTotalLen; j++) {
				if (tB[i].digits[j] != tB[i - 1].digits[j]) {
					equalLast = false;
					break;
				}
			}
			if (!equalLast) {
				r++;
			}
			rank[tB[i].isuffix] = r;
		}

		Suffix suffix = new Suffix();
		suffix.rank = rank;
		suffix.sa = sa;
		// judge if we are done
		// R = LEN
		// 说明RANK-K里没有相等的了
		// 那就代表了RANK-2K也不用排了
		// 因为前K已经不相等 就不用对比后K了
		if (r == len) {
			suffix.done = true;
		}
		else {
			suffix.done = false;
		}
		return suffix;

	}

	// Precondition: the last char in text must be less than other chars.
	public Suffix solve(String text)
	{
		if (text == null)
			return null;
		int len = text.length();
		if (len == 0)
			return null;

		int k = 1;
		char base = text.charAt(len - 1); // the smallest char
		Tuple[] tA = new Tuple[len];
		Tuple[] tB = new Tuple[len]; // placeholder
		for (int i = 0; i < len; i++) {
			tA[i] = new Tuple(i, new int[] { 0, text.charAt(i) - base });
		}
		System.out.println("base" + (base - '0'));
		System.out.println("base" + ('a' - '0'));
		System.out.println("base" + ('A' - '0'));
		Suffix suffix = rank(tA, tB, MAX_CHAR - base, 1);
		while (!suffix.done) { // no need to decide if: k<=len
			k <<= 1;
			int offset = k >> 1;
			for (int i = 0, j = i + offset; i < len; i++, j++) {
				tA[i].isuffix = i;
				tA[i].digits = new int[] { suffix.rank[i], (j < len) ? suffix.rank[i + offset] : 0 };
			}
			int max = suffix.rank[suffix.sa[len - 1]];
			suffix = rank(tA, tB, max, 2);
		}
		return suffix;
	}

	public void report(Suffix suffix)
	{
		int[] sa = suffix.sa;
		int[] rank = suffix.rank;
		int len = sa.length;

		System.out.println("suffix array:");
		for (int i = 0; i < len; i++) {
			System.out.format(" %s", sa[i]);
		}
		System.out.println();
		System.out.println("rank array:");
		for (int i = 0; i < len; i++) {
			System.out.format(" %s", rank[i]);
		}
		System.out.println();
	}

	// h[i]=height[Rank[i]]，即height[i]=h[SA[i]], h[i-1]=
	// height[Rank[i-1]]=LCP(Suffix(SA[Rank[i-1]-1]),Suffix(SA[Rank[i-1]]);
	// height[Rank[SA[i]-1]]=h[SA[i]-1]
	// 令height[i]=LCP(i-1,i)==lcp(Suffix(SA[i-1]),Suffix(SA[i])，1<i≤n，并设height[1]=0。
	// RMQ看另一個SuffixArrayC.java
	public int[] getHeight(String text, int[] sa, int[] rank)
	{
		if (text == null)
			return null;
		int len = text.length();
		if (len == 0) {
			return null;
		}
		int[] H = new int[len];

		// base case: p=0
		// caculate LCP of suffix[0]
		int lcp = 0;
		int r = rank[0] - 1;
		if (r > 0) {
			int q = sa[r - 1];
			// caculate LCP by definition
			for (int i = 0, j = q; i < len && j < len; i++, j++) {
				if (text.charAt(i) != text.charAt(j)) {
					lcp = i;
					break;
				}
			}
		}
		H[0] = lcp;

		// other cases: p>=1
		// ignore p == sa[0] because LCP=0 for suffix[p] where rank[p]=0
		for (int p = 1; p < len && p != sa[0]; p++) {
			int h = H[p - 1];
			int q = sa[rank[p] - 2];
			lcp = 0;
			if (h > 1) { // for h<=1, caculate LCP by definition (i.e. start
							// with lcp=0)
				// jump h-1 chars for suffix[p] and suffix[q]
				lcp = h - 1;
			}
			for (int i = p + lcp, j = q + lcp, k = 0; i < len && j < len; i++, j++, k++) {
				if (text.charAt(i) != text.charAt(j)) {
					lcp += k;
					break;
				}
			}
			H[p] = lcp;
		}

		// caculate LCP
		int[] Height = new int[len];
		for (int i = 0; i < len; i++) {
			Height[i] = H[sa[i]];
		}
		return Height;
	}

	public static void main(String[] args)
	{
		/*
		 * //plain counting sort test:
		 * 
		 * int[] A= {2,5,3,0,2,3,0,3}; PrefixDoubling pd = new PrefixDoubling();
		 * int[] B = new int[A.length]; pd.countingSort(A,B,5); for(int
		 * i=0;i<B.length;i++) System.out.format(" %d", B[i]);
		 * System.out.println();
		 */
		System.out.println(MAX_CHAR);
		System.out.println(Character.MAX_VALUE);
		System.out.println(Character.MIN_VALUE);
		String text = "GACCCACCACC#";
		text = "abcd#";
		SuffixArray_PrefixDoubling pd = new SuffixArray_PrefixDoubling();
		Suffix suffix = pd.solve(text);
		System.out.format("Text: %s%n", text);
		pd.report(suffix);

		System.out.println("********************************");
		text = "mississippi#";
		pd = new SuffixArray_PrefixDoubling();
		suffix = pd.solve(text);
		System.out.format("Text: %s%n", text);
		pd.report(suffix);

		System.out.println("********************************");
		text = "abcdefghijklmmnopqrstuvwxyz#";
		pd = new SuffixArray_PrefixDoubling();
		suffix = pd.solve(text);
		System.out.format("Text: %s%n", text);
		pd.report(suffix);

		System.out.println("********************************");
		text = "yabbadabbado#";
		pd = new SuffixArray_PrefixDoubling();
		suffix = pd.solve(text);
		System.out.format("Text: %s%n", text);
		pd.report(suffix);

		System.out.println("********************************");
		text = "DFDLKJLJldfasdlfjasdfkldjasfldafjdajfdsfjalkdsfaewefsdafdsfa#";
		pd = new SuffixArray_PrefixDoubling();
		suffix = pd.solve(text);
		System.out.format("Text: %s%n", text);
		pd.report(suffix);

	}
}