package concurrent.sync;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTaskDemo
{
	public static void main(String[] args)
	{
		// 初始化一个Callable对象和FutureTask对象；
		Callable pAccount = new PrivateAccount();
		FutureTask futureTask = new FutureTask(pAccount);
		// 使用FutureTask创建一个线程
		Thread pAccountThread = new Thread(futureTask);
		System.out.println("future task starts at " + System.nanoTime());
		// 启动线程
		pAccountThread.start();
		// 主线程执行自己的任务
		System.out.println("main thread doing something else here. ");
		// 从其他帐户获取总金额
		int totalMoney = new Random().nextInt(100000);
		System.out.println(" You have " + totalMoney + " in your other Accounts. ");
		System.out.println(" Waiting for data from Private Account ");
		// 测试后台的就计算线程是否完成，如果未完成，等待
		while (!futureTask.isDone()) {
			try {
				Thread.sleep(5);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("future task ends at " + System.nanoTime());
		Integer privataAccountMoney = null;
		// 如果后台的FutureTask计算完成，则返回计算结果
		try {
			privataAccountMoney = (Integer) futureTask.get();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println(" The total moeny you have is " + (totalMoney + privataAccountMoney.intValue()));
	}
}

// 创建一个Callable类，模拟计算一个私有帐户中的金额
class PrivateAccount implements Callable
{
	Integer	totalMoney;

	@Override
	public Integer call() throws Exception
	{
		// 为了延长计算时间，这里暂停几秒
		Thread.sleep(5000);
		totalMoney = new Integer(new Random().nextInt(10000));
		System.out.println(" You have " + totalMoney + " in your private Account. ");
		return totalMoney;
	}
}
