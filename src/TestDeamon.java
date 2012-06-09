public class TestDeamon
{

	public static void main(String[] args)
	{
		A a = new TestDeamon().new A();
		a.start();
	}

	class A extends Thread
	{

		public A()
		{
			this.setDaemon(true);
		}

		public void run()
		{
			System.out.println("A run");
			try {
				B b = new B();
				Thread.sleep(6000);
			}
			catch (Exception e) {
				System.out.println("A Exception");
			}
			System.out.println("A done");
		}

	}

	class B extends Thread
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
