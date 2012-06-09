//������ʹ���̳߳ش����Ķ��̳߳���100 ���߳�Ŀ������� 2 ���̡߳� 
package concurrent.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import concurrent.jdkapidemo.InterruptTest2;

public class TestThreadPool
{
	public static void main(String args[]) throws InterruptedException
	{
		// ���̳߳��д��� 2 ���߳�
		ExecutorService exec = Executors.newFixedThreadPool(2);
		// ���� 100 ���߳�Ŀ�����
		for (int index = 0; index < 100; index++) {
			Runnable run = new Runner(index);
			// ִ���߳�Ŀ�����
			exec.execute(run);
			Future<?> a = exec.submit(run);
			while (a.isDone()) {
				System.out.println("done");
			}
		}
		// shutdown
		exec.shutdown();
	}
}

// �߳�Ŀ�����
class Runner implements Runnable
{
	int	index	= 0;

	public Runner(int index)
	{
		this.index = index;
	}

	@Override
	public void run()
	{
		long time = (long) (Math.random() * 1000);
		// ����̵߳����ֺ�ʹ��Ŀ��������ߵ�ʱ��
		System.out.println("�̣߳�" + Thread.currentThread().getName() + "(Ŀ�����" + index + ")" + ":Sleeping " + time + "ms");
		try {
			Thread.sleep(time);
			System.out.println("�̣߳�" + Thread.currentThread().getName() + "(Ŀ�����" + index + ")" + "OVER  :Sleeping " + time + "ms");
		}
		catch (InterruptedException e) {
		}
	}

}
