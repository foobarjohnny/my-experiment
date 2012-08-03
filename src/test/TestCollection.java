package test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Queue;

public class TestCollection
{
	// list set 等 针对Integer, Double等数据类型时比较特殊
	// 可以对比TestObject
	public static void main(String[] args)
	{
		ArrayList a = new ArrayList();
		HashSet s = new HashSet();
		Integer a1 = new Integer(1);
		Integer b1 = new Integer(1);
		for (int i = 1; i < 11; i++) {
			a.add(new Integer(i));
			// s.add(new Integer(i));
		}
		System.out.println(a1 == b1);
		System.out.println(a.equals(b1));
		s.add(a1);
		s.add(b1);
		// a.remove(new Integer(1));
		// s.remove(new Integer(1));
		a.remove(new Double(1));
		s.add(new Double(1));
		s.remove(new Double(1));
		s.add(new TestObject(1));
		s.remove(new TestObject(1));
		System.out.println(a);
		System.out.println(s);

	}
}

class TestObject
{
	int	i	= 3;

	TestObject(int i)
	{
		this.i = i;
	}

	@Override
	public String toString()
	{
		return "testObject [i=" + i + "]";
	}
}