package concurrent.conditionlock;

//������
public class Consumer implements Runnable
{
	LockedBuffer	buffer;

	public Consumer(LockedBuffer buf)
	{
		buffer = buf;
	}

	public void run()
	{
		Object c = null;
		for (int i = 0; i < 20; i++) {
			try {
				// ȡ����
				c = buffer.take();
			}
			catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			System.out.println("Consumed: " + c);
			try {
				Thread.sleep((int) (Math.random() * 1000));
			}
			catch (InterruptedException e) {
			}
		}
	}
}