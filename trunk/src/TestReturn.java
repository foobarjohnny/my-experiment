import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Queue;

public class TestReturn
{
	public String message()
	{
		System.out.println("returned start");
		return "return";
	}
	
	private TestReturn(){
		
	}

	public static int testFinal()
	{
		int a = 1;
		try {
			System.out.println("a=" + a);
			return a;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 3;
		}
		finally {
			a = 2;
			System.out.println("do finally");
			// return "finally";
		}
	}

	public static void main(String[] args)
	{
		int i = 0;
		int a = -2, b = -3;
		a = (int) (a + (b - a) * 1);
		System.out.println(a);
		while (i++ < 5) {
			System.out.print("A");
		}
		// String a="abc"; String b=new String("abc");
		// System.out.println(a==b);;

		TestReturn test = new TestReturn();
		System.out.println(test.testFinal());
		// int a = 0;
		// int c = 0;
		// do {
		// --c;
		// a = a - 1;
		// } while (a > 0);
		// System.out.println(c);
		String[] z = "|DF|A".split(".");

		for (i = 0; i < z.length; i++) {
			System.out.println(z[i]);
		}
		//
		// Hashtable c11;
		// c11=new Hashtable();
		// c11.put("d", null);
	}
}