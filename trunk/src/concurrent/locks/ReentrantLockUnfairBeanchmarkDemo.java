package concurrent.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockUnfairBeanchmarkDemo implements Counter
{
	private volatile long	count	= 0;
	private Lock			lock;

	public ReentrantLockUnfairBeanchmarkDemo()
	{
		// ʹ�÷ǹ�ƽ����true���ǹ�ƽ��
		lock = new ReentrantLock(false);
	}

	public long getValue()
	{
		return count;
	}

	public void increment()
	{
		lock.lock();
		try {
			count++;
		}
		finally {
			lock.unlock();
		}
	}
}
