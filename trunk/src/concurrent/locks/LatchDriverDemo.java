package concurrent.locks;

import java.util.concurrent.CountDownLatch;

public class LatchDriverDemo
{
	public static final int	N	= 5;

	public static void main(String[] args) throws InterruptedException
	{
		// ���������̷߳��������ź�
		CountDownLatch startSignal = new CountDownLatch(1);
		// ���ڵȴ������̵߳Ľ����ź�
		CountDownLatch doneSignal = new CountDownLatch(N);
		for (int i = 0; i < N; ++i) {
			// ���������߳�
			new Thread(new LatchWorker(startSignal, doneSignal), "t" + i).start();
		}
		// �õ��߳̿�ʼ������ʱ��
		long start = System.nanoTime();
		// ���̣߳��ݼ���ʼ���������������߳̿�ʼ����
		startSignal.countDown();

		// ���߳��������ȴ������߳����
		doneSignal.await();
		long end = System.nanoTime();
		System.out.println("all worker take time��ms��:" + (end - start) / 1000000);
	}
}

class LatchWorker implements Runnable
{
	// ���ڵȴ������ź�
	private final CountDownLatch	startSignal;
	// ���ڷ��ͽ����ź�
	private final CountDownLatch	doneSignal;

	LatchWorker(CountDownLatch startSignal, CountDownLatch doneSignal)
	{
		this.startSignal = startSignal;
		this.doneSignal = doneSignal;
	}

	public void run()
	{
		try {
			startSignal.await();// �������ȴ���ʼ���ź�
			doWork();
			doneSignal.countDown();// ��������ź�
		}
		catch (InterruptedException ex) {
		}
	}

	void doWork()
	{
		System.out.println(Thread.currentThread().getName() + " is working...");
		int sum = 0;
		for (int i = 0; i < 10000000; i++) {
			sum += i;
		}
	}
}
