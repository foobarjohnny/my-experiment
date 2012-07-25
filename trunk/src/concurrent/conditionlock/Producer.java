package concurrent.conditionlock;

//������
public class Producer implements Runnable
{
	LockedBuffer	buffer;

	public Producer(LockedBuffer buf)
	{
		buffer = buf;
	}

	public void run()
	{
		char c;
		for (int i = 0; i < 20; i++) {
			c = (char) (Math.random() * 26 + 'A');
			try {
				// �򻺳�����������
				buffer.put(c);
			}
			catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			System.out.println("Produced: " + c);
			try {
				Thread.sleep((int) (Math.random() * 100));
			}
			catch (InterruptedException e) {
			}
		}
	}
}