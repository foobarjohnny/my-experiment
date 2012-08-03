package test2;

public class TestInnerClass
{
	public static void main(String[] args)
	{
		ThreadA a = new TestInnerClass.ThreadA();
		ThreadC c = new TestInnerClass().new ThreadC();
		a.start();
	}

	static class ThreadA extends Thread
	{
		public ThreadA()
		{
		}

		public void run()
		{
			System.out.println("A run");
			try {
				Thread.sleep(6000);
			}
			catch (Exception e) {
				System.out.println("A Exception");
			}
			System.out.println("A done");
		}

	}

	public class ThreadB extends Thread
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

	class ThreadC extends Thread
	{
		public void run()
		{
			System.out.println("C run");
			try {
				Thread.sleep(6000);
			}
			catch (Exception e) {
				System.out.println("C Exception");
			}
			System.out.println("C done");
		}
	}
}


class outThread2 extends Thread
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
