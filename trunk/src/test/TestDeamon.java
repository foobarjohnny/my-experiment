package test;

public class TestDeamon
{
	public static void main(String[] args)
	{
		ThA a = new TestDeamon().new ThA();
		a.start();
	}

	class ThA extends Thread
	{

		public ThA()
		{
			this.setDaemon(true);
		}

		public void run()
		{
			System.out.println("A run");
			try {
				ThB b = new ThB();
				Thread.sleep(6000);
			}
			catch (Exception e) {
				System.out.println("A Exception");
			}
			System.out.println("A done");
		}

	}

	public class ThB extends Thread
	{
		public void run()
		{
			System.out.println("B run");
			try {
				Thread.sleep(6000);
			}
			catch (Exception e) {
				System.out.println("B Exception");
			}
			System.out.println("B done");
		}
	}
}

class outThread extends Thread
{
	public void run()
	{
		System.out.println("B run");
		try {
			Thread.sleep(6000);
		}
		catch (Exception e) {
			System.out.println("B Exception");
		}
		System.out.println("B done");
	}
}
