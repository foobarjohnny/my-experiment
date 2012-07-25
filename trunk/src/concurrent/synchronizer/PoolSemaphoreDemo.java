package concurrent.synchronizer;

import java.util.concurrent.Semaphore;
//	http://blog.csdn.net/arkblue/article/details/6138598
//为了比较一下ReentrantLock和synchronized的性能，做了一下性能测试：
//
//参考http://zzhonghe.javaeye.com/blog/826162
//
//
//得出结论：
//
//（1）使用Lock的性能比使用synchronized关键字要提高4~5倍；
//
//（2）使用信号量实现同步的速度大约比synchronized要慢10~20%；
//
//（3）使用atomic包的AtomicInter速度是比Lock要快1一个数量级。
//
//ReentrantLock 类 
//java.util.concurrent.lock 中的 Lock 框架是锁定的一个抽象，
//它允许把锁定的实现作为 Java 类，而不是作为语言的特性来实现。
//这就为 Lock 的多种实现留下了空间，各种实现可能有不同的调度算法、性能特性或者锁定语义。
//ReentrantLock 类实现了 Lock，它拥有与 synchronized 相同的并发性和内存语义，
//但是添加了类似锁投票、定时锁等候和可中断锁等候的一些特性。
//此外，它还提供了在激烈争用情况下更佳的性能。
//（换句话说，当许多线程都想访问共享资源时，JVM 可以花更少的时候来调度线程，把更多时间用在执行线程上。）
//
//reentrant 锁意味着什么呢？简单来说，它有一个与锁相关的获取计数器，如果拥有锁的某个线程再次得到锁，
//那么获取计数器就加1，然后锁需要被释放两次才能获得真正释放。这模仿了 synchronized 的语义；
//如果线程进入由线程已经拥有的监控器保护的 synchronized 块，就允许线程继续进行，
//当线程退出第二个（或者后续）synchronized 块的时候，不释放锁，
//只有线程退出它进入的监控器保护的第一个 synchronized 块时，才释放锁。
//
//在查看清单 1 中的代码示例时，可以看到 Lock 和 synchronized 
//有一点明显的区别 —— lock 必须在 finally 块中释放。
//否则，如果受保护的代码将抛出异常，锁就有可能永远得不到释放！
//这一点区别看起来可能没什么，但是实际上，它极为重要。
//忘记在 finally 块中释放锁，可能会在程序中留下一个定时bomb，
//当有一天bomb爆炸时，您要花费很大力气才有找到源头在哪。而使用同步，JVM 将确保锁会获得自动释放。

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
		// 构造 10 个线程
		{
			Thread t = new Thread(runner, "t" + i);
			t.start();
		}
	}

	// 获取数据，需要得到许可
	public Object getItem() throws InterruptedException
	{
		available.acquire();
		return getNextAvailableItem();
	}

	// 放回数据，释放许可
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
