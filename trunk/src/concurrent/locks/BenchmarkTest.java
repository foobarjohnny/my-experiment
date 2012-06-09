package concurrent.locks;

import java.util.concurrent.CyclicBarrier;

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
		// �ؿ�����=�߳��� +1
		this.threadNum = threadNum;
		this.loopNum = loopNum;
		this.testName = testName;
	}

	public static void main(String args[]) throws Exception
	{
		int threadNum = 5000;
		int loopNum = 100;
		new BenchmarkTest(new SynchronizedBenchmarkDemo(), threadNum, loopNum, "�ڲ���").test();
		new BenchmarkTest(new ReentrantLockUnfairBeanchmarkDemo(), threadNum, loopNum, "����ƽ������").test();
		new BenchmarkTest(new ReentrantLockFairBeanchmarkDemo(), threadNum, loopNum, "��ƽ������").test();
	}

	public void test() throws Exception
	{
		try {
			for (int i = 0; i < threadNum; i++) {
				new TestThread(counter, loopNum).start();
			}
			long start = System.currentTimeMillis();
			barrier.await();
			// �ȴ����������̴߳���,Ȼ��ͨ���ؿ���ͳһִ�� �����߳�
			barrier.await();
			// �ȴ���������������
			long end = System.currentTimeMillis();
			System.out.println(this.testName + " count value:" + counter.getValue());
			System.out.println(this.testName + " ����ʱ��:" + (end - start) + "����");
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
				// �ȴ����е��߳̿�ʼ
				for (int i = 0; i < this.loopNum; i++) {
					counter.increment();
				}
				barrier.await();
				// �ȴ����е��߳����
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
