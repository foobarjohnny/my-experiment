package concurrent.locks;

import java.util.concurrent.CyclicBarrier;

// ReentrantLock不公平锁 先ACQUIRE后ENQUEUE 直接插队了 但不用PARK还是什么的 所以效率更高
public class BenchmarkTest
{
	private Counter			counter;	;
	private CyclicBarrier	barrier;
	private int				threadNum;
	private int				loopNum;
	private String			testName;

	private BenchmarkTest(Counter counter, int threadNum, int loopNum, String testName)
	{
		this.counter = counter;
		this.barrier = new CyclicBarrier(threadNum + 1);
		// 关卡计数=线程数 +1
		this.threadNum = threadNum;
		this.loopNum = loopNum;
		this.testName = testName;
	}

	public static void main(String args[]) throws Exception
	{
		int threadNum = 5000;
		int loopNum = 100;
		new BenchmarkTest(new SynchronizedBenchmarkDemo(), threadNum, loopNum, "内部锁").test();
		new BenchmarkTest(new ReentrantLockUnfairBeanchmarkDemo(), threadNum, loopNum, "不公平重入锁").test();
		new BenchmarkTest(new ReentrantLockFairBeanchmarkDemo(), threadNum, loopNum, "公平重入锁").test();
	}

	public void test() throws Exception
	{
		try {
			for (int i = 0; i < threadNum; i++) {
				new TestThread(counter, loopNum).start();
			}
			long start = System.currentTimeMillis();
			barrier.await();
			// 等待所有任务线程创建,然后通过关卡，统一执行 所有线程
			barrier.await();
			// 等待所有任务计算完成
			long end = System.currentTimeMillis();
			System.out.println(this.testName + " count value:" + counter.getValue());
			System.out.println(this.testName + " 花费时间:" + (end - start) + "毫秒");
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	class TestThread extends Thread
	{
		int				loopNum	= 100;
		private Counter	counter;

		public TestThread(final Counter counter, int loopNum)
		{
			this.counter = counter;
			this.loopNum = loopNum;
		}

		public void run()
		{
			try {
				barrier.await();
				// 等待所有的线程开始
				for (int i = 0; i < this.loopNum; i++) {
					counter.increment();
				}
				barrier.await();
				// 等待所有的线程完成
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
