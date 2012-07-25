package concurrent.conditionlock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockedBuffer
{
	// ��������
	final Lock		lock		= new ReentrantLock();
	// ������������
	final Condition	notFull		= lock.newCondition();
	final Condition	notEmpty	= lock.newCondition();
	// ������
	final Object[]	items		= new Object[10];
	int				putptr, takeptr, count;			// ������

	// �����ݲ����������ߵ��ø÷���

	public void put(Object x) throws InterruptedException
	{
		lock.lock();
		try {
			// ������������ˣ����̵߳ȴ�
			while (count == items.length)
				notFull.await();
			items[putptr] = x;
			if (++putptr == items.length)
				putptr = 0;
			++count;
			// ���������̷߳���֪ͨ
			notEmpty.signal();
		}
		finally {
			lock.unlock();
		}
	}

	// �������̵߳��ø÷���
	public Object take() throws InterruptedException
	{
		lock.lock();
		try {
			// ����������գ���ȴ�
			while (count == 0)
				notEmpty.await();// ጷ��i����˼
			Object x = items[takeptr];
			if (++takeptr == items.length)
				takeptr = 0;
			--count;
			// ֪ͨ�����������߳�
			notFull.signal();
			return x;
		}
		finally {
			lock.unlock();
		}
	}
}
