package numbertheory;

import java.math.BigDecimal;

// ����/�߾��ȼӼ��˳�ȡģ[�ղ�] http://blog.csdn.net/subo86/article/details/4206287
// �߾��ȿ��ٽ׳�
// http://www.freewebs.com/maths/florilegium/factorial.htm
// http://www.emath.ac.cn/algorithm/factorial.htm

// �׳ˣ�����һ�����еĳ˻�����Ч�ʵĸߵͣ�һ����ȡ���ڸ߾��ȳ˷��㷨����������Խ׳������ص��㷨���Ż���
// ǰ�ߣ���һ�ֹ�Ϊϰ֪�ļ�������Ȼ��ͬ�㷨Ч�ʿ�����֮�𣬵��վ�����ͨ��ѧϰ��ã���������³Ѹ�����ġ��������塱��
// ���ߣ��������ķ�չ�ռ䣬�����ҽ�����һ��������ȫ������һ�ַ�������ӭ������ۣ������𵽡���ש���񡱵�Ч�����Ǿ������ﵽ��Ŀ�ġ�
//
// ���ڿ������׳ˡ����㷨ʱ��ʼ����ѭ����ԭ��
// ����߾��ȳ˷��㷨����������СӦ�����ܵ������
// �����ܽ��˷�ת��Ϊ�˷���
// �����ܵ����ȵ���ƽ����
//
// �Թ���ת�������Ծ�ȷ���� 1000! Ϊ�����������㷨��
//
// �� F1(n) = n*(n-1)*...*1��
// F2(n) = n*(n-2)*...*(2 or 1)��
// Pow2(r) = 2^r��
// �� F1(2k+1) = F2(2k+1) * F2(2k)
// = Pow2(k) * F2(2k+1) * F1(k),
// F1(2k) = Pow2(k) * F2(2k-1) * F1(k),
// �� Pow2(u) * Pow2(v) = Pow2(u+v),
//
// �� 1000! = F1(1000)
// = Pow2(500)*F2(999)*F1(500)
// = Pow2(750)*F2(999)*F2(499)*F1(250)
// = Pow2(875)*F2(999)*F2(499)*F2(249)*F1(125)
// = Pow2(937)*F2(999)*F2(499)*F2(249)*F2(125)*F1(62)
// = Pow2(968)*F2(999)*F2(499)*F2(249)*F2(125)*F2(61)*F1(31)
// = Pow2(983)*F2(999)*F2(499)*F2(249)*F2(125)*F2(61)*F2(31)*F1(15)
// = ...
//
// �����Ԥ����ĳЩС�����׳ˣ���������ġ�F1(15)�����������ǰ��ֹ�ֽ⣬����ֱ���ұ����һ��Ϊ��F1(1)��Ϊֹ�����������ǽ��׳�ת��Ϊ2������������һЩ���������Ļ������ٳ���һ��С�����Ľ׳ˣ���
//
// �ٶ��壺F2(e,f) = e*(e-2)*...*(f+2)���������á�F2�����͵��ǡ��������ء�����:)��
// �� F2(e) = F2(e,-1) = F2(e,f)*F2(f,-1) ��e��fΪ������0��f��e��
//
// �� F2(999) = F2(999,499)*F2(499,249)*F2(249,125)*F2(125,61)*F2(61,31)*F2(31),
// F2(499) = ____________F2(499,249)*F2(249,125)*F2(125,61)*F2(61,31)*F2(31),
// F2(249) = ________________________F2(249,125)*F2(125,61)*F2(61,31)*F2(31),
// F2(125) = ____________________________________F2(125,61)*F2(61,31)*F2(31),
// F2( 61) = _______________________________________________F2(61,31)*F2(31),
// F2( 31) = _________________________________________________________F2(31),
// �� F1(1000) = F1(15) * Pow2(983) * F2(999,499) \
// * [F2(499,249)^2] * [F2(249,125)^3] \
// * [F2(61,31)^4] * [F2(31)^5]
// �����������ֽ��׳�ת��Ϊ�˳˷����㡣
//
// ��ʽʵ�����Ǹ����� a * b^2 * c^3 * d^4 * e^5 ��ʽ�ӣ������ٽ�ָ��ת��Ϊ�����ƣ��ɵõ����¹�ʽ��
// a * b^2 * c^3 * d^4 * e^5 = (a*c*e)*[(b*c)^2]*[(d*e)^4]
// = (((e*d)^2)*(c*b))^2*(e*c*a)��
// ����ת�����˿ɳ�����ø�Ч��ƽ���㷨��
//
//
// ��������ṩ���һ��ȷ����С�����۳˲�������ļ��ɣ��������Դ��ģ����Ż�Դ���н�����ã�
// Ӧ���á����򡱣��ȷ�˵��Ӧ�� 999 * 997 * ... ��˳��
// ��Ԥ���趨һ�����飬��¼�����ǰ��ʼ��������ֵ������ĳ��ֵ�������˶��ٸ������������
// �����á�����ƽ��ֵ������ƽ��ֵ���������Ƶ����� k
// �����������˲�����ĳ����ֵ��������������Ͳ�����ĳ���ٽ�ֵ��������ֻ��Ҫ���ȼ�������ǵĶ�Ӧ��ϵ������������
//
// �����㷨�� HugeCalc Ver1.2.0.1 ���㷨�ؼ��㣬��Ч�����Ը���liangbch(����)�ĸ߼��㷨3.0�档
//
// �����µ� HugeCalc Ver2.1.0.1
// �У����õ��Ǹ����׵ķֽ���������ķ����������Ե������������ǿ����ȴ��Ҫ�и�Ч������ɸ������������ĺܶ�˼������������

public class BigNumber
{

	public static int compare(String str1, String str2)
	{
		if (str1.length() > str2.length()) {// ���ȳ����������ڳ���С������
			return 1;
		}
		else if (str1.length() < str2.length()) {
			return -1;
		}
		else {
			return str1.compareTo(str2);
		} // ��������ȣ���ͷ��β��λ�Ƚϣ�compare��������ȷ���0�����ڷ���1��С�ڷ��أ�1
	}

	// �߾��ȼӷ�
	public static String ADD_INT(String str1, String str2)
	{
		// String MINUS_INT(String str1, String str2);
		int sign = 1; // sign Ϊ����λ
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
				// �������������룬������ǰ���0����
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
				int int1 = 0, int2 = 0; // int2 ��¼��λ
				for (i = str1.length() - 1; i >= 0; i--) {
					// 48 Ϊ '0' ��ASCII ��
					int1 = ((int) (str1.charAt(i)) - '0' + (int) (str2.charAt(i)) - '0' + int2) % 10;
					int2 = ((int) (str1.charAt(i)) - '0' + (int) (str2.charAt(i)) - '0' + int2) / 10;
					str = (char) (int1 + '0') + str;
				}
				if (int2 != 0) {
					str = (char) (int2 + '0') + str;
				}
			}
		}
		// ����������λ
		if ((sign == -1) && (str.charAt(0) != '0')) {
			str = "-" + str;
		}
		return str;
	}

	// �߾��ȼ���
	public static String MINUS_INT(String str1, String str2)
	{
		// String MULTIPLY_INT(String str1, String str2);
		int sign = 1; // sign Ϊ����λ
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
		// ȥ������ж����ǰ��0
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

	// �߾��ȳ˷�
	public static String MULTIPLY_INT(String str1, String str2)
	{
		int sign = 1; // sign Ϊ����λ
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
		for (i = l2 - 1; i >= 0; i--) { // ʵ���ֹ��˷�
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
		// ȥ������е�ǰ��0
		str = str.substring(find_first_not_of(str, '0'));
		if (str.isEmpty()) {
			str = "0";
		}
		if ((sign == -1) && (str.charAt(0) != '0')) {
			str = "-" + str;
		}
		return str;
	}

	// �߾��ȳ���
	public static String DIVIDE_INT(String str1, String str2, int flag)
	{
		// flag = 1ʱ,������; flag = 0ʱ,��������
		String quotient = "", residue = ""; // �����̺�����
		int sign1 = 1, sign2 = 1;
		if (str2 == "0") { // �жϳ����Ƿ�Ϊ0
			quotient = "ERROR!";
			residue = "ERROR!";
			if (flag == 1) {
				return quotient;
			}
			else {
				return residue;
			}
		}
		if (str1 == "0") { // �жϱ������Ƿ�Ϊ0
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

			// ģ���ֹ�����
			for (int i = l2 - 1; i < l1; i++) {
				tempstr = tempstr + str1.charAt(i);
				for (char ch = '9'; ch >= '0'; ch--) { // ����
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
		// ȥ������е�ǰ��0
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

	// �߾��ȳ���,������
	public static String DIV_INT(String str1, String str2)
	{
		return DIVIDE_INT(str1, str2, 1);
	}

	// �߾��ȳ���,��������
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
				s[i + j + 1] += (a[i] - '0') * (b[j] - '0');// i+j+1��Ŀ�ľ���Ϊ�˷�ֹ���λ��λ����������
		for (i = ca + cb - 1; i >= 0; i--)
			if (s[i] >= 10) {
				s[i - 1] += s[i] / 10;
				s[i] %= 10;
			}
		i = 0;
		while (i < s.length && s[i] == 0) {
			i++;// ȥ��ǰ��0
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
				break; // �߾��ȼӷ�
			case '-':
				res = MINUS_INT(s1, s2);
				break; // �߾��ȼ���
			case '*':
				res = MULTIPLY_INT(s1, s2);
				break; // �߾��ȳ˷�
			case '/':
				res = DIV_INT(s1, s2);
				break; // �߾��ȳ���, ������
			case 'm':
				res = MOD_INT(s1, s2);
				break; // �߾��ȳ���, ��������
			default:
				break;
		}
		System.out.println(res);

		System.out.println("�ӷ����㣺" + round(add(10.345, 3.333), 1));
		System.out.println("�������㣺" + round(sub(10.345, 3.333), 3));
		System.out.println("�˷����㣺" + round(mul(10.345, 3.333), 2));
		System.out.println("�������㣺" + div(10.345, 3.333, 3));

	}

	// http://tianya23.blog.51cto.com/1081650/412105
	// JAVA��������
	public static double add(double d1, double d2)
	{ // ���мӷ�����
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.add(b2).doubleValue();
	}

	public static double sub(double d1, double d2)
	{ // ���м�������
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.subtract(b2).doubleValue();
	}

	public static double mul(double d1, double d2)
	{ // ���г˷�����
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.multiply(b2).doubleValue();
	}

	public static double div(double d1, double d2, int len)
	{ // ���г˷�����
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static double round(double d, int len)
	{ // ������������
		BigDecimal b1 = new BigDecimal(d);
		BigDecimal b2 = new BigDecimal(1);
		return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

}
