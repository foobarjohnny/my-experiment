package concurrent.simplethread;

public class MyThreadTarget implements Runnable
{
	public static void main(String[] args)
	{
		for (int i = 0; i < 5; i++) {
			// �����߳�Ŀ�����
			Runnable r = new MyThreadTarget();
			// ��Ŀ����󴫵ݸ�Thread���������cpu
			new Thread(r, "thread" + i).start();
		}
	}

	@Override
	public void run()
	{
		for (int i = 0; i < 20; i++) {
			System.out.println(Thread.currentThread().getName() + ":" + i);
		}
	}
}