package concurrent.simplethread;

public class MyThread1 extends Thread
{
	public MyThread1(String name)
	{
	//	super(name);
		// �����̵߳�����
	}

	public static void main(String[] args)
	{

		// TODO Auto-generated method stub
		for (int i = 0; i < 5; i++) {
			// ����5���߳�
			new MyThread1("thread" + i).start();
			System.out.println(Thread.currentThread().getName());
		}
	}
	@Override
	public void run()
	{
		for (int i = 0; i < 20; i++) {
			// ����߳����ֺ�i
			System.out.println(this.getName() + ":" + i);
			System.out.println(Thread.currentThread().getName() + ":" + i);
		}
	}
}
