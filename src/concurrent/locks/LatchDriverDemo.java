package concurrent.locks;

import java.util.concurrent.CountDownLatch;

public class LatchDriverDemo
{
	public static final int	N	= 5;

	public static void main(String[] args) throws InterruptedException
	{
		// 用于向工作线程发送启动信号
		CountDownLatch startSignal = new CountDownLatch(1);
		// 用于等待工作线程的结束信号
		CountDownLatch doneSignal = new CountDownLatch(N);
		for (int i = 0; i < N; ++i) {
			// 创建启动线程
			new Thread(new LatchWorker(startSignal, doneSignal), "t" + i).start();
		}
		// 得到线程开始工作的时间
		long start = System.nanoTime();
		// 主线程，递减开始计数器，让所有线程开始工作
		startSignal.countDown();

		// 主线程阻塞，等待所有线程完成
		doneSignal.await();
		long end = System.nanoTime();
		System.out.println("all worker take time（ms）:" + (end - start) / 1000000);
	}
}

class LatchWorker implements Runnable
{
	// 用于等待启动信号
	private final CountDownLatch	startSignal;
	// 用于发送结束信号
	private final CountDownLatch	doneSignal;

	LatchWorker(CountDownLatch startSignal, CountDownLatch doneSignal)
	{
		this.startSignal = startSignal;
		this.doneSignal = doneSignal;
	}

	public void run()
	{
		try {
			startSignal.await();// 阻塞，等待开始新信号
			doWork();
			doneSignal.countDown();// 发送完成信号
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
