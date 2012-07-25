package concurrent.conditionlock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockedBuffer
{
	// 可重入锁
	final Lock		lock		= new ReentrantLock();
	// 两个条件对象
	final Condition	notFull		= lock.newCondition();
	final Condition	notEmpty	= lock.newCondition();
	// 缓冲区
	final Object[]	items		= new Object[10];
	int				putptr, takeptr, count;			// 计数器

	// 放数据操作，生产者调用该方法

	public void put(Object x) throws InterruptedException
	{
		lock.lock();
		try {
			// 如果缓冲区满了，则线程等待
			while (count == items.length)
				notFull.await();
			items[putptr] = x;
			if (++putptr == items.length)
				putptr = 0;
			++count;
			// 向消费者线程发送通知
			notEmpty.signal();
		}
		finally {
			lock.unlock();
		}
	}

	// 消费者线程调用该方法
	public Object take() throws InterruptedException
	{
		lock.lock();
		try {
			// 如果缓冲区空，则等待
			while (count == 0)
				notEmpty.await();// 釋放鎖的意思
			Object x = items[takeptr];
			if (++takeptr == items.length)
				takeptr = 0;
			--count;
			// 通知其他生产者线程
			notFull.signal();
			return x;
		}
		finally {
			lock.unlock();
		}
	}
}
