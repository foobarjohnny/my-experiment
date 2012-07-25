package concurrent.synchronizer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//����ReentrantLock
public class CyclicBarrierDemo
{
	// ͽ����Ҫ��ʱ��: Shenzhen, Guangzhou, Chongqing
	private static int[]	timeForWalk	= { 5, 8, 15 };
	// �Լ���
	private static int[]	timeForSelf	= { 1, 3, 4 };
	// ���δ��
	private static int[]	timeForBus	= { 2, 4, 6 };

	static String nowTime()
	{
		// ʱ���ʽ��
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
				// ������תվ��ȴ�����������
				Thread.sleep(timeForUse[1] * 1000);
				System.out.println(nowTime() + tourName + " Reached Guangzhou");
				barrier.await();
				// ������תվ��ȴ�����������
				Thread.sleep(timeForUse[2] * 1000);
				System.out.println(nowTime() + tourName + " Reached Chon in gq g");
				barrier.await();
				// ������תվ��ȴ�����������
			}
			catch (InterruptedException e) {
			}
			catch (BrokenBarrierException e) {
			}
		}
	}

	public static void main(String[] args)
	{
		// ���������Ŷ�������ĳһ��վ���ִ������Ĳ�������ʾ�������ˡ�
		Runnable runner = new Runnable()
		{
			@Override
			public void run()
			{
				System.out.println("we all are here.");
			}
		};
		CyclicBarrier barrier = new CyclicBarrier(3, runner);
		// ʹ���̳߳�
		ExecutorService exec = Executors.newFixedThreadPool(3);
		exec.submit(new Tour(barrier, "WalkTour", timeForWalk));
		exec.submit(new Tour(barrier, "SelfTour", timeForSelf));
		exec.submit(new Tour(barrier, "BusTour", timeForBus));
		exec.shutdown();
	}

}