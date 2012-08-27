package numbertheory;

import java.math.BigDecimal;

// 大数/高精度加减乘除取模[收藏] http://blog.csdn.net/subo86/article/details/4206287
// 高精度快速阶乘
// http://www.freewebs.com/maths/florilegium/factorial.htm
// http://www.emath.ac.cn/algorithm/factorial.htm

// 阶乘，是求一组数列的乘积，其效率的高低，一、是取决于高精度乘法算法，二、是针对阶乘自身特点算法的优化。
// 前者，是一种广为习知的技术，虽然不同算法效率可天壤之别，但终究可以通过学习获得，甚至可用鲁迅先生的“拿来主义”；
// 后者，则有许多的发展空间，这里我将介绍一种由我完全独创的一种方法，欢迎大家讨论，如能起到“抛砖引玉”的效果，那就真正达到了目的。
//
// 我在开发“阶乘”类算法时，始终遵循如下原则：
// 参与高精度乘法算法的两数，大小应尽可能地相近；
// 尽可能将乘法转化为乘方；
// 尽可能地优先调用平方；
//
// 言归正转，下面以精确计算 1000! 为例，阐述该算法：
//
// 记 F1(n) = n*(n-1)*...*1；
// F2(n) = n*(n-2)*...*(2 or 1)；
// Pow2(r) = 2^r；
// 有 F1(2k+1) = F2(2k+1) * F2(2k)
// = Pow2(k) * F2(2k+1) * F1(k),
// F1(2k) = Pow2(k) * F2(2k-1) * F1(k),
// 及 Pow2(u) * Pow2(v) = Pow2(u+v),
//
// ∴ 1000! = F1(1000)
// = Pow2(500)*F2(999)*F1(500)
// = Pow2(750)*F2(999)*F2(499)*F1(250)
// = Pow2(875)*F2(999)*F2(499)*F2(249)*F1(125)
// = Pow2(937)*F2(999)*F2(499)*F2(249)*F2(125)*F1(62)
// = Pow2(968)*F2(999)*F2(499)*F2(249)*F2(125)*F2(61)*F1(31)
// = Pow2(983)*F2(999)*F2(499)*F2(249)*F2(125)*F2(61)*F2(31)*F1(15)
// = ...
//
// 如果你预存了某些小整数阶乘（比如这里的“F1(15)”），则可提前终止分解，否则直至右边最后一项为“F1(1)”为止；这样，我们将阶乘转化为2的整数次幂与一些连续奇数的积（或再乘以一个小整数的阶乘）；
//
// 再定义：F2(e,f) = e*(e-2)*...*(f+2)，这里仍用“F2”，就当是“函数重载”好了:)，
// 则 F2(e) = F2(e,-1) = F2(e,f)*F2(f,-1) （e、f为奇数，0≤f≤e）
//
// ∴ F2(999) = F2(999,499)*F2(499,249)*F2(249,125)*F2(125,61)*F2(61,31)*F2(31),
// F2(499) = ____________F2(499,249)*F2(249,125)*F2(125,61)*F2(61,31)*F2(31),
// F2(249) = ________________________F2(249,125)*F2(125,61)*F2(61,31)*F2(31),
// F2(125) = ____________________________________F2(125,61)*F2(61,31)*F2(31),
// F2( 61) = _______________________________________________F2(61,31)*F2(31),
// F2( 31) = _________________________________________________________F2(31),
// ∴ F1(1000) = F1(15) * Pow2(983) * F2(999,499) \
// * [F2(499,249)^2] * [F2(249,125)^3] \
// * [F2(61,31)^4] * [F2(31)^5]
// 这样，我们又将阶乘转化为了乘方运算。
//
// 上式实际上是个形如 a * b^2 * c^3 * d^4 * e^5 的式子；我们再将指数转化为二进制，可得到如下公式：
// a * b^2 * c^3 * d^4 * e^5 = (a*c*e)*[(b*c)^2]*[(d*e)^4]
// = (((e*d)^2)*(c*b))^2*(e*c*a)，
// 即可转化成了可充分利用高效的平方算法。
//
//
// 最后，我再提供大家一个确保“小整数累乘不溢出”的技巧，这是我自创的，相信会对大家有借鉴作用：
// 应采用“倒序”，比方说，应按 999 * 997 * ... 的顺序；
// 可预先设定一个数组，记录如果当前起始数组的最大值不大于某个值，则连乘多少个数不会溢出；
// 可利用“几何平均值≤算术平均值”定理，可推导出“ k
// 个正整数连乘不大于某个定值，其充分条件是其和不大于某个临界值”，我们只需要事先计算出它们的对应关系并保存下来。
//
// 上述算法是 HugeCalc Ver1.2.0.1 的算法关键点，其效率已略高于liangbch(宝宝)的高级算法3.0版。
//
// 在最新的 HugeCalc Ver2.1.0.1
// 中，采用的是更彻底的分解成质因数的方法，技巧性倒反不如上面的强（但却需要有高效的素数筛法），但里面的很多思想仍在延续。

public class BigNumber
{

	public static int compare(String str1, String str2)
	{
		if (str1.length() > str2.length()) {// 长度长的整数大于长度小的整数
			return 1;
		}
		else if (str1.length() < str2.length()) {
			return -1;
		}
		else {
			return str1.compareTo(str2);
		} // 若长度相等，从头到尾按位比较，compare函数：相等返回0，大于返回1，小于返回－1
	}

	// 高精度加法
	public static String ADD_INT(String str1, String str2)
	{
		// String MINUS_INT(String str1, String str2);
		int sign = 1; // sign 为符号位
		String str = "";
		if (str1.charAt(0) == '-') {
			if (str2.charAt(0) == '-') {
				sign = -1;
				str = ADD_INT(str1.substring(1), str2.substring(1));
			}
			else {
				str = MINUS_INT(str2, str1.substring(1));
			}
		}
		else {
			if (str2.charAt(0) == '-') {
				str = MINUS_INT(str1, str2.substring(1));
			}
			else {
				// 把两个整数对齐，短整数前面加0补齐
				int l1, l2;
				int i;
				l1 = str1.length();
				l2 = str2.length();
				if (l1 < l2) {
					for (i = 1; i <= l2 - l1; i++) {
						str1 = "0" + str1;
					}
				}
				else {
					for (i = 1; i <= l1 - l2; i++) {
						str2 = "0" + str2;
					}
				}
				int int1 = 0, int2 = 0; // int2 记录进位
				for (i = str1.length() - 1; i >= 0; i--) {
					// 48 为 '0' 的ASCII 码
					int1 = ((int) (str1.charAt(i)) - '0' + (int) (str2.charAt(i)) - '0' + int2) % 10;
					int2 = ((int) (str1.charAt(i)) - '0' + (int) (str2.charAt(i)) - '0' + int2) / 10;
					str = (char) (int1 + '0') + str;
				}
				if (int2 != 0) {
					str = (char) (int2 + '0') + str;
				}
			}
		}
		// 运算后处理符号位
		if ((sign == -1) && (str.charAt(0) != '0')) {
			str = "-" + str;
		}
		return str;
	}

	// 高精度减法
	public static String MINUS_INT(String str1, String str2)
	{
		// String MULTIPLY_INT(String str1, String str2);
		int sign = 1; // sign 为符号位
		String str = "";
		if (str2.charAt(0) == '-') {
			str = ADD_INT(str1, str2.substring(1));
		}
		else {
			int res = compare(str1, str2);
			if (res == 0)
				return "0";
			if (res < 0) {
				sign = -1;
				String temp = str1;
				str1 = str2;
				str2 = temp;
			}
			int tempint;
			char[] s1 = str1.toCharArray();
			char[] s2 = str2.toCharArray();
			tempint = s1.length - s2.length;
			for (int i = s2.length - 1; i >= 0; i--) {
				if (s1[i + tempint] < s2[i]) {
					s1[i + tempint - 1] = (char) ((int) (s1[i + tempint - 1]) - 1);
					str = (char) (s1[i + tempint] - s2[i] + 10 + '0') + str;
				}
				else {
					str = (char) (s1[i + tempint] - s2[i] + '0') + str;
				}
			}
			for (int i = tempint - 1; i >= 0; i--) {
				str = s1[i] + str;
			}
		}
		// 去除结果中多余的前导0
		str = str.substring(find_first_not_of(str, '0'));
		if (str.isEmpty()) {
			str = "0";
		}
		if ((sign == -1) && (str.charAt(0) != '0')) {
			str = "-" + str;
		}
		return str;
	}

	public static int find_first_not_of(String str1, char x)
	{
		if (str1 == null) {
			return -1;
		}
		for (int i = 0; i < str1.length(); i++) {
			if (str1.charAt(i) != x) {
				return i;
			}
		}
		return str1.length() - 1;
	}

	// 高精度乘法
	public static String MULTIPLY_INT(String str1, String str2)
	{
		int sign = 1; // sign 为符号位
		String str = "";
		if (str1.charAt(0) == '-') {
			sign *= -1;
			str1 = str1.substring(1);
		}
		if (str2.charAt(0) == '-') {
			sign *= -1;
			str2 = str2.substring(1);
		}
		int i, j;
		int l1, l2;
		l1 = str1.length();
		l2 = str2.length();
		for (i = l2 - 1; i >= 0; i--) { // 实现手工乘法
			String tempstr = "";
			int int1 = 0, int2 = 0, int3 = (int) (str2.charAt(i)) - '0';
			if (int3 != 0) {
				for (j = 1; j <= (int) (l2 - 1 - i); j++) {
					tempstr = "0" + tempstr;
				}
				for (j = l1 - 1; j >= 0; j--) {
					int1 = (int3 * ((int) (str1.charAt(j)) - '0') + int2) % 10;
					int2 = (int3 * ((int) (str1.charAt(j)) - '0') + int2) / 10;
					tempstr = (char) (int1 + '0') + tempstr;
				}
				if (int2 != 0) {
					tempstr = (char) (int2 + '0') + tempstr;
				}
			}
			if (str == "") {
				str = "0";
			}
			if (tempstr == "") {
				tempstr = "0";
			}
			str = ADD_INT(str, tempstr);
		}
		// 去除结果中的前导0
		str = str.substring(find_first_not_of(str, '0'));
		if (str.isEmpty()) {
			str = "0";
		}
		if ((sign == -1) && (str.charAt(0) != '0')) {
			str = "-" + str;
		}
		return str;
	}

	// 高精度除法
	public static String DIVIDE_INT(String str1, String str2, int flag)
	{
		// flag = 1时,返回商; flag = 0时,返回余数
		String quotient = "", residue = ""; // 定义商和余数
		int sign1 = 1, sign2 = 1;
		if (str2 == "0") { // 判断除数是否为0
			quotient = "ERROR!";
			residue = "ERROR!";
			if (flag == 1) {
				return quotient;
			}
			else {
				return residue;
			}
		}
		if (str1 == "0") { // 判断被除数是否为0
			quotient = "0";
			residue = "0";
		}
		if (str1.charAt(0) == '-') {
			str1 = str1.substring(1);
			sign1 *= -1;
			sign2 = -1;
		}
		if (str2.charAt(0) == '-') {
			str2 = str2.substring(1);
			sign1 *= -1;
		}
		int res = compare(str1, str2);
		if (res < 0) {
			quotient = "0";
			residue = str1;
		}
		else if (res == 0) {
			quotient = "1";
			residue = "0";
		}
		else {
			int l1, l2;
			l1 = str1.length();
			l2 = str2.length();
			String tempstr = "";
			tempstr += str1.substring(0, l2 - 1);

			// 模拟手工除法
			for (int i = l2 - 1; i < l1; i++) {
				tempstr = tempstr + str1.charAt(i);
				for (char ch = '9'; ch >= '0'; ch--) { // 试商
					String str = "";
					str = str + ch;
					if (compare(MULTIPLY_INT(str2, str), tempstr) <= 0) {
						quotient = quotient + ch;
						tempstr = MINUS_INT(tempstr, MULTIPLY_INT(str2, str));
						break;
					}
				}
			}
			residue = tempstr;
		}
		// 去除结果中的前导0
		quotient = quotient.substring(find_first_not_of(quotient, '0'));
		if (quotient.isEmpty()) {
			quotient = "0";
		}
		if ((sign1 == -1) && (quotient.charAt(0) != '0')) {
			quotient = "-" + quotient;
		}
		if ((sign2 == -1) && (residue.charAt(0) != '0')) {
			residue = "-" + residue;
		}
		if (flag == 1) {
			return quotient;
		}
		else {
			return residue;
		}
	}

	// 高精度除法,返回商
	public static String DIV_INT(String str1, String str2)
	{
		return DIVIDE_INT(str1, str2, 1);
	}

	// 高精度除法,返回余数
	public static String MOD_INT(String str1, String str2)
	{
		return DIVIDE_INT(str1, str2, 0);
	}

	// http://blog.csdn.net/yming0221/article/details/6169839
	public static String MULTIPLY(String str1, String str2)
	{
		int i, j, ca, cb;
		ca = str1.length();
		cb = str2.length();
		char[] a = str1.toCharArray();
		char[] b = str2.toCharArray();
		int[] s = new int[ca + cb];
		for (i = 0; i < ca + cb; i++)
			s[i] = 0;
		for (i = 0; i < ca; i++)
			for (j = 0; j < cb; j++)
				s[i + j + 1] += (a[i] - '0') * (b[j] - '0');// i+j+1的目的就是为了防止最高位进位而产生错误
		for (i = ca + cb - 1; i >= 0; i--)
			if (s[i] >= 10) {
				s[i - 1] += s[i] / 10;
				s[i] %= 10;
			}
		i = 0;
		while (i < s.length && s[i] == 0) {
			i++;// 去除前导0
		}
		String c = "";
		for (j = 0; i < ca + cb; i++, j++) {
			c += s[i];
		}
		if (c == "") {
			c = "0";
		}
		return c;
	}

	public static void main(String[] args)
	{
		char ch = 'm';
		String s1, s2, res;
		s1 = "235";
		s2 = "23";
		res = "";
		switch (ch) {
			case '+':
				res = ADD_INT(s1, s2);
				break; // 高精度加法
			case '-':
				res = MINUS_INT(s1, s2);
				break; // 高精度减法
			case '*':
				res = MULTIPLY_INT(s1, s2);
				break; // 高精度乘法
			case '/':
				res = DIV_INT(s1, s2);
				break; // 高精度除法, 返回商
			case 'm':
				res = MOD_INT(s1, s2);
				break; // 高精度除法, 返回余数
			default:
				break;
		}
		System.out.println(res);

		System.out.println("加法运算：" + round(add(10.345, 3.333), 1));
		System.out.println("减法运算：" + round(sub(10.345, 3.333), 3));
		System.out.println("乘法运算：" + round(mul(10.345, 3.333), 2));
		System.out.println("除法运算：" + div(10.345, 3.333, 3));

	}

	// http://tianya23.blog.51cto.com/1081650/412105
	// JAVA大数运算
	public static double add(double d1, double d2)
	{ // 进行加法计算
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.add(b2).doubleValue();
	}

	public static double sub(double d1, double d2)
	{ // 进行减法计算
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.subtract(b2).doubleValue();
	}

	public static double mul(double d1, double d2)
	{ // 进行乘法计算
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.multiply(b2).doubleValue();
	}

	public static double div(double d1, double d2, int len)
	{ // 进行乘法计算
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static double round(double d, int len)
	{ // 进行四舍五入
		BigDecimal b1 = new BigDecimal(d);
		BigDecimal b2 = new BigDecimal(1);
		return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

}
