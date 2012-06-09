package concurrent.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockFairBeanchmarkDemo implements Counter
{
	private volatile long	count	= 0;
	private Lock			lock;

	public ReentrantLockFairBeanchmarkDemo()
	{
		// true 就是公平锁
		lock = new ReentrantLock(true);
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