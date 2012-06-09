package concurrent.sync;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTaskDemo
{
	public static void main(String[] args)
	{
		// ��ʼ��һ��Callable�����FutureTask����
		Callable pAccount = new PrivateAccount();
		FutureTask futureTask = new FutureTask(pAccount);
		// ʹ��FutureTask����һ���߳�
		Thread pAccountThread = new Thread(futureTask);
		System.out.println("future task starts at " + System.nanoTime());
		// �����߳�
		pAccountThread.start();
		// ���߳�ִ���Լ�������
		System.out.println("main thread doing something else here. ");
		// �������ʻ���ȡ�ܽ��
		int totalMoney = new Random().nextInt(100000);
		System.out.println(" You have " + totalMoney + " in your other Accounts. ");
		System.out.println(" Waiting for data from Private Account ");
		// ���Ժ�̨�ľͼ����߳��Ƿ���ɣ����δ��ɣ��ȴ�
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
		// �����̨��FutureTask������ɣ��򷵻ؼ�����
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

// ����һ��Callable�࣬ģ�����һ��˽���ʻ��еĽ��
class PrivateAccount implements Callable
{
	Integer	totalMoney;

	@Override
	public Integer call() throws Exception
	{
		// Ϊ���ӳ�����ʱ�䣬������ͣ����
		Thread.sleep(5000);
		totalMoney = new Integer(new Random().nextInt(10000));
		System.out.println(" You have " + totalMoney + " in your private Account. ");
		return totalMoney;
	}
}
