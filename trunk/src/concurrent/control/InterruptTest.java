package concurrent.control;

public class InterruptTest extends Thread
{
	static int	result	= 0;
	
	protected InterruptTest(){
		System.out.println();
	}

	public InterruptTest(String name)
	{
		super(name);
	}

	public static void main(String[] args)
	{
		System.out.println("���߳�ִ��");
		Thread t = new InterruptTest("�����߳�");
		t.start();
		System.out.println("result��" + result);
		try {
			long start = System.nanoTime();
			t.join(2000);
			long end = System.nanoTime();
			t.interrupt();
			System.out.println((end - start) / 1000000 + "�����:" + result);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		System.out.println(this.getName() + "��ʼ����...");
		try {
			Thread.sleep(4000);
		}
		catch (InterruptedException e) {
			System.out.println(this.getName() + "���ж�,����");
			return;
		}
		result = (int) (Math.random() * 10000);
		System.out.println(this.getName() + "��������");
	}
}