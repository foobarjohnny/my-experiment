package concurrent.synchronizer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//基于ReentrantLock
public class CyclicBarrierDemo
{
	// 徒步需要的时间: Shenzhen, Guangzhou, Chongqing
	private static int[]	timeForWalk	= { 5, 8, 15 };
	// 自驾游
	private static int[]	timeForSelf	= { 1, 3, 4 };
	// 旅游大巴
	private static int[]	timeForBus	= { 2, 4, 6 };

	static String nowTime()
	{
		// 时间格式化
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(new Date()) + ": ";
	}

	static class Tour implements Runnable
	{
		private int[]			timeForUse;
		private CyclicBarrier	barrier;
		private String			tourName;

		public Tour(CyclicBarrier barrier, String tourName, int[] timeForUse)
		{
			this.timeForUse = timeForUse;
			this.tourName = tourName;
			this.barrier = barrier;
		}

		public void run()
		{
			try {
				Thread.sleep(timeForUse[0] * 1000);
				System.out.println(nowTime() + tourName + " Reached Shen zhen  ");
				barrier.await();
				// 到达中转站后等待其他旅行团
				Thread.sleep(timeForUse[1] * 1000);
				System.out.println(nowTime() + tourName + " Reached Guangzhou");
				barrier.await();
				// 到达中转站后等待其他旅行团
				Thread.sleep(timeForUse[2] * 1000);
				System.out.println(nowTime() + tourName + " Reached Chon in gq g");
				barrier.await();
				// 到达中转站后等待其他旅行团
			}
			catch (InterruptedException e) {
			}
			catch (BrokenBarrierException e) {
			}
		}
	}

	public static void main(String[] args)
	{
		// 三个旅行团都到到达某一个站点后，执行下面的操作，表示都到齐了。
		Runnable runner = new Runnable()
		{
			@Override
			public void run()
			{
				System.out.println("we all are here.");
			}
		};
		CyclicBarrier barrier = new CyclicBarrier(3, runner);
		// 使用线程池
		ExecutorService exec = Executors.newFixedThreadPool(3);
		exec.submit(new Tour(barrier, "WalkTour", timeForWalk));
		exec.submit(new Tour(barrier, "SelfTour", timeForSelf));
		exec.submit(new Tour(barrier, "BusTour", timeForBus));
		exec.shutdown();
	}

}