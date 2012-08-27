package numbertheory;

//http://www.cnblogs.com/pkuoliver/archive/2010/10/27/Convert-m-number-to-n-number.html
/**
 * 功能：将一个数从M进制转换成N进制 MValue：M进制数的字符串表示方法 Shang：保存中间运算结果 M：M进制 N：N进制 :m->10->n
 */
public class M2N
{
	// 在这里对输入赋值
	public static String	MValue	= "14";
	public static String	Shang	= null;
	public static int		M		= 10;
	public static int		N		= 8;

	// 我们霹雳无敌的赵大叔又提供了一种用Java实现的通用的进制转换方法，即使Windows的计算器也转不了的大数，
	// 这个算法也可以转。算和上面的算法相比，他的基本思想不变，还是辗转除，但是用了字符串做大数相除，
	// 很不错的创新点，赞一个。代码如下：
	public static void main(String[] args)
	{
		String nValue = "";
		Shang = MValue;
		while (Shang.length() > 0) {
			nValue = qiuyu(Shang) + nValue;
		}
		System.out.println(nValue);
	}

	/**
	 * 功能：对给定的M进制字符串对n求余。
	 * 
	 * @param MTempValue
	 * @param m
	 * @param n
	 * @return
	 */
	public static String qiuyu(String MTempValue)
	{
		Shang = "";
		int temp = 0;
		while (MTempValue.length() > 0) {
			int t = getIntFromStr(MTempValue.substring(0, 1));
			MTempValue = MTempValue.substring(1);
			temp = temp * M + t;
			Shang += getStrFromInt(temp / N);
			temp = temp % N;
		}
		while (Shang.length() > 0 && Shang.charAt(0) == '0') {
			Shang = Shang.substring(1);
		}
		return getStrFromInt(temp);
	}

	public static int getIntFromStr(String str)
	{
		return str.charAt(0) <= '9' && str.charAt(0) >= '0' ? str.charAt(0) - '0' : str.charAt(0) - 'a' + 10;
	}

	public static String getStrFromInt(int value)
	{
		String result = null;
		if (value >= 0 && value <= 9)
			result = String.valueOf((char) ('0' + value));
		else if (value > 9 && value < 36) {
			result = String.valueOf((char) ('a' + value - 10));
		}
		else {
			result = "-1";// 出错误了
		}
		return result;
	}

	// void m2n(int m, char* mNum, int n, char* nNum)
	// {
	// int i = 0;
	// char c, *p = nNum;
	//
	// //这是一个考察地方，是否能用最少乘法次数。
	// while (*mNum != '\0')
	// i = i*m + *mNum++ - '0';
	//
	// //辗转取余
	// while (i) {
	// *p++ = i % n + '0';
	// i /= n;
	// }
	// *p-- = '\0';
	//
	// //逆置余数序列
	// while (p > nNum) {
	// c = *p;
	// *p-- = *nNum;
	// *nNum++ = c;
	// }
	// }
}