package concurrent.synchronizer;

import java.util.concurrent.Semaphore;
//	http://blog.csdn.net/arkblue/article/details/6138598
//Ϊ�˱Ƚ�һ��ReentrantLock��synchronized�����ܣ�����һ�����ܲ��ԣ�
//
//�ο�http://zzhonghe.javaeye.com/blog/826162
//
//
//�ó����ۣ�
//
//��1��ʹ��Lock�����ܱ�ʹ��synchronized�ؼ���Ҫ���4~5����
//
//��2��ʹ���ź���ʵ��ͬ�����ٶȴ�Լ��synchronizedҪ��10~20%��
//
//��3��ʹ��atomic����AtomicInter�ٶ��Ǳ�LockҪ��1һ����������
//
//ReentrantLock �� 
//java.util.concurrent.lock �е� Lock �����������һ������
//�������������ʵ����Ϊ Java �࣬��������Ϊ���Ե�������ʵ�֡�
//���Ϊ Lock �Ķ���ʵ�������˿ռ䣬����ʵ�ֿ����в�ͬ�ĵ����㷨���������Ի����������塣
//ReentrantLock ��ʵ���� Lock����ӵ���� synchronized ��ͬ�Ĳ����Ժ��ڴ����壬
//���������������ͶƱ����ʱ���Ⱥ�Ϳ��ж����Ⱥ��һЩ���ԡ�
//���⣬�����ṩ���ڼ�����������¸��ѵ����ܡ�
//�����仰˵��������̶߳�����ʹ�����Դʱ��JVM ���Ի����ٵ�ʱ���������̣߳��Ѹ���ʱ������ִ���߳��ϡ���
//
//reentrant ����ζ��ʲô�أ�����˵������һ��������صĻ�ȡ�����������ӵ������ĳ���߳��ٴεõ�����
//��ô��ȡ�������ͼ�1��Ȼ������Ҫ���ͷ����β��ܻ�������ͷš���ģ���� synchronized �����壻
//����߳̽������߳��Ѿ�ӵ�еļ���������� synchronized �飬�������̼߳������У�
//���߳��˳��ڶ��������ߺ�����synchronized ���ʱ�򣬲��ͷ�����
//ֻ���߳��˳�������ļ���������ĵ�һ�� synchronized ��ʱ�����ͷ�����
//
//�ڲ鿴�嵥 1 �еĴ���ʾ��ʱ�����Կ��� Lock �� synchronized 
//��һ�����Ե����� ���� lock ������ finally �����ͷš�
//��������ܱ����Ĵ��뽫�׳��쳣�������п�����Զ�ò����ͷţ�
//��һ��������������ûʲô������ʵ���ϣ�����Ϊ��Ҫ��
//������ finally �����ͷ��������ܻ��ڳ���������һ����ʱbomb��
//����һ��bomb��ըʱ����Ҫ���Ѻܴ����������ҵ�Դͷ���ġ���ʹ��ͬ����JVM ��ȷ���������Զ��ͷš�

public class PoolSemaphoreDemo
{
	private static final int	MAX_AVAILABLE	= 5;
	private final Semaphore		available		= new Semaphore(MAX_AVAILABLE, true);

	public static void main(String args[])
	{
		final PoolSemaphoreDemo pool = new PoolSemaphoreDemo();
		Runnable runner = new Runnable()
		{
			@Override
			public void run()
			{
				try {
					Object o;
					o = pool.getItem();
					System.out.println(Thread.currentThread().getName() + " acquire " + o);
					Thread.sleep(1000);
					pool.putItem(o);
					System.out.println(Thread.currentThread().getName() + " release " + o);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		for (int i = 0; i < 10; i++)
		// ���� 10 ���߳�
		{
			Thread t = new Thread(runner, "t" + i);
			t.start();
		}
	}

	// ��ȡ���ݣ���Ҫ�õ����
	public Object getItem() throws InterruptedException
	{
		available.acquire();
		return getNextAvailableItem();
	}

	// �Ż����ݣ��ͷ����
	public void putItem(Object x)
	{
		if (markAsUnused(x))
			available.release();
	}

	protected Object[]	items	= { "AAA", "BBB", "CCC", "DDD", "EEE" };
	protected boolean[]	used	= new boolean[MAX_AVAILABLE];

	protected synchronized Object getNextAvailableItem()
	{
		for (int i = 0; i < MAX_AVAILABLE; ++i) {
			if (!used[i]) {
				used[i] = true;
				return items[i];
			}
		}
		return null;
	}

	protected synchronized boolean markAsUnused(Object item)
	{
		for (int i = 0; i < MAX_AVAILABLE; ++i) {
			if (item == items[i]) {
				if (used[i]) {
					used[i] = false;
					return true;
				}
				else
					return false;
			}
		}
		return false;
	}
}
