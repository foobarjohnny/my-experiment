package concurrent.control;
public class SleepTest
{
	public static void main(String[] arg)
	{
		String[] args = { "one", "two", "three", "for" };
		long start = System.nanoTime();
		for (int i = 0; i < args.length; i++) {
			try {
				System.out.println(args[i]);
				// �������߳�
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		long end = System.nanoTime();
		System.out.println("�ܵ�ʱ�䣺" + (end - start) / 1000000);
	}
}
