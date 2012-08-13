package test;

import java.util.concurrent.locks.ReentrantLock;

public class TestSynchronized

{
	private static int	TIME	= 1500;

	static synchronized void X()
	{
		try {
			System.out.println("static synchronized X");
			Thread.sleep(TIME);
			System.out.println("static synchronized X over");
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	synchronized void X1()
	{
		try {
			System.out.println("synchronized X1");
			Thread.sleep(TIME);
			System.out.println("synchronized over X1");
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	synchronized void X2()
	{

		// 可以再加锁 不会死锁
		synchronized (this) {
			try {
				System.out.println("synchronized X2");
				Thread.sleep(TIME);
				System.out.println("synchronized over X2");
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void Y()
	{
		ReentrantLock lock = new ReentrantLock();
		synchronized (TestSynchronized.class) {
			try {
				System.out.println("synchronized Y");
				Thread.sleep(TIME);
				System.out.println("synchronized over Y");
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void Z(TestSynchronized a)
	{

		synchronized (a.getClass()) {
			try {
				System.out.println("synchronized Z");
				Thread.sleep(TIME);
				System.out.println("synchronized over Z");
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// X Y Z互斥 X1 X2互斥
	public static void main(String[] args)
	{
		TestSynchronized ts1 = new TestSynchronized();
		TestSynchronizedThread X = new TestSynchronizedThread(ts1, "X");
		TestSynchronizedThread Y = new TestSynchronizedThread(ts1, "Y");
		TestSynchronizedThread Z = new TestSynchronizedThread(ts1, "Z");
		TestSynchronizedThread X1 = new TestSynchronizedThread(ts1, "X1");
		TestSynchronizedThread X2 = new TestSynchronizedThread(ts1, "X2");
		// X.start();
		Y.start();
		// Z.start();
		// X1.start();
		X2.start();
	}
}

class TestSynchronizedThread extends Thread
{

	private TestSynchronized	ts;
	private String				s;

	public TestSynchronizedThread(TestSynchronized t, String s)
	{
		ts = t;
		this.s = s;
	}

	public void run()
	{
		System.out.println(s + " run");
		try {
			if (s == "X") {
				ts.X();
			}
			else if (s == "X1") {
				ts.X1();
			}
			else if (s == "X2") {
				ts.X2();
			}
			else if (s == "Y") {
				ts.Y();
			}
			else if (s == "Z") {
				ts.Z(ts);
			}
		}
		catch (Exception e) {
			System.out.println(s + " Exception");
		}
		System.out.println(s + " done");
	}

}
