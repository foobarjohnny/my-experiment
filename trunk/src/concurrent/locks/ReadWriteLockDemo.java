package concurrent.locks;

import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo
{
	// �������д��
	private ReentrantReadWriteLock	lock		= null;
	private Lock					readLock	= null;
	// ����
	private Lock					writeLock	= null;
	// д��
	public int						key			= 100;
	public int						index		= 100;
	public Map<Integer, String>		dataMap		= null;

	// �̹߳�������

	public ReadWriteLockDemo()
	{
		lock = new ReentrantReadWriteLock(true);
		readLock = lock.readLock();
		writeLock = lock.writeLock();
		dataMap = new TreeMap<Integer, String>();
	}

	public static void main(String[] args)
	{
		ReadWriteLockDemo tester = new ReadWriteLockDemo();
		// ��һ�λ�ȡ��
		tester.writeLock.lock();
		System.out.println(Thread.currentThread().getName() + " get writeLock.");
		// �ڶ��λ�ȡ����ӦΪ�ǿ�������
		tester.writeLock.lock();
		System.out.println(Thread.currentThread().getName() + " get writeLock.");
		tester.readLock.lock();
		System.out.println(Thread.currentThread().getName() + " get readLock");
		tester.readLock.lock();
		System.out.println(Thread.currentThread().getName() + " get readLock");
		tester.readLock.unlock();
		tester.readLock.unlock();
		tester.writeLock.unlock();
		tester.writeLock.unlock();
		tester.test();
	}

	public void test()
	{
		// ���̱߳�д�̶߳�
		for (int i = 0; i < 10; i++) {
			new Thread(new reader(this)).start();
		}
		for (int i = 0; i < 3; i++) {
			new Thread(new writer(this)).start();
		}
	}

	public void read()
	{

		// ��ȡ��
		readLock.lock();
		try {
			if (dataMap.isEmpty()) {
				Calendar now = Calendar.getInstance();
				System.out.println(now.getTime().getTime() + " R " + Thread.currentThread().getName() + " get key, but map is empty.");
			}
			String value = dataMap.get(index);
			Calendar now = Calendar.getInstance();
			System.out.println(now.getTime().getTime() + " R " + Thread.currentThread().getName() + " key = " + index + " value = " + value + " map size = " + dataMap.size());
			if (value != null) {
				index++;
			}
		}
		finally {
			// �ͷ���
			Calendar now = Calendar.getInstance();
			System.out.println(now.getTime().getTime() + " R " + Thread.currentThread().getName() + " readLock.unlock();");
			readLock.unlock();
		}

		try {
			Thread.sleep(3000);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void write()
	{
		writeLock.lock();
		try {
			String value = "value" + key;
			dataMap.put(new Integer(key), value);
			Calendar now = Calendar.getInstance();
			System.out.println(now.getTime().getTime() + " W " + Thread.currentThread().getName() + " key = " + key + " value = " + value + " map size = " + dataMap.size());
			key++;
			try {
				Thread.sleep(500);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		finally {
			Calendar now = Calendar.getInstance();
			System.out.println(now.getTime().getTime() + " W " + Thread.currentThread().getName() + " writeLock.unlock();");
			writeLock.unlock();
		}
	}
}

class reader implements Runnable
{
	private ReadWriteLockDemo	tester	= null;

	public reader(ReadWriteLockDemo tester)
	{
		this.tester = tester;
	}

	public void run()
	{
		Calendar now = Calendar.getInstance();
		System.out.println(now.getTime().getTime() + " R " + Thread.currentThread().getName() + " started");
		for (int i = 0; i < 10; i++) {
			tester.read();
		}
	}
}

class writer implements Runnable
{
	private ReadWriteLockDemo	tester	= null;

	public writer(ReadWriteLockDemo tester)
	{
		this.tester = tester;
	}

	public void run()
	{
		Calendar now = Calendar.getInstance();
		System.out.println(now.getTime().getTime() + " W " + Thread.currentThread().getName() + " started");
		for (int i = 0; i < 10; i++) {
			tester.write();
		}
	}
}