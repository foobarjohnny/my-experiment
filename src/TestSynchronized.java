import java.util.concurrent.locks.ReentrantLock;

public class TestSynchronized

{
	static synchronized void X()
	{
		System.out.println("static synchronized X");
	}

	synchronized void X1()
	{
		System.out.println("synchronized X1");
	}

	synchronized void X2()
	{
		synchronized (this) {
			System.out.println("synchronized X2");
		}
	}

	void Y()
	{
		ReentrantLock lock = new ReentrantLock();
		synchronized (A.class) {
			System.out.println("synchronized (A.class) ");
		}
	}

	void Z(A a)
	{

		synchronized (a.getClass()) {
			System.out.println("synchronized (a.getClass()) ");
		}
	}

}