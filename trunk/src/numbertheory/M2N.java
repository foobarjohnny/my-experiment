package numbertheory;

//http://www.cnblogs.com/pkuoliver/archive/2010/10/27/Convert-m-number-to-n-number.html
/**
 * ���ܣ���һ������M����ת����N���� MValue��M���������ַ�����ʾ���� Shang�������м������� M��M���� N��N���� :m->10->n
 */
public class M2N
{
	// ����������븳ֵ
	public static String	MValue	= "14";
	public static String	Shang	= null;
	public static int		M		= 10;
	public static int		N		= 8;

	// ���������޵е��Դ������ṩ��һ����Javaʵ�ֵ�ͨ�õĽ���ת����������ʹWindows�ļ�����Ҳת���˵Ĵ�����
	// ����㷨Ҳ����ת�����������㷨��ȣ����Ļ���˼�벻�䣬����շת�������������ַ��������������
	// �ܲ���Ĵ��µ㣬��һ�����������£�
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
	 * ���ܣ��Ը�����M�����ַ�����n���ࡣ
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
			result = "-1";// ��������
		}
		return result;
	}

	// void m2n(int m, char* mNum, int n, char* nNum)
	// {
	// int i = 0;
	// char c, *p = nNum;
	//
	// //����һ������ط����Ƿ��������ٳ˷�������
	// while (*mNum != '\0')
	// i = i*m + *mNum++ - '0';
	//
	// //շתȡ��
	// while (i) {
	// *p++ = i % n + '0';
	// i /= n;
	// }
	// *p-- = '\0';
	//
	// //������������
	// while (p > nNum) {
	// c = *p;
	// *p-- = *nNum;
	// *nNum++ = c;
	// }
	// }
}