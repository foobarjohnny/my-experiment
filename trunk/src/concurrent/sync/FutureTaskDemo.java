package concurrent.sync;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
//http://blog.csdn.net/historyasamirror/article/details/5961368
//ThreadPoolExecutor
// corePoolSize 和 maxPoolSize

//这两个参数其实和threadpool的调度策略密切相关：
//如果poolsize小于coresize，那么只要来了一个request，就新创建一个thread来执行；
//如果poolsize已经大于或等于coresize，那么来了一个request后，就放进queue中，等来线程执行；
//一旦且只有queue满了，才会又创建新的thread来执行；
//当然，coresize和maxpoolsize可以在运行时通过set方法来动态的调节；
//（queue如果是一个确定size的队列，那么很有可能发生reject request的事情（因为队列满了）。
//很多人会认为这样的系统不好。但其实，reject request很多时候是个好事，
//因为当负载大于系统的capacity的时候，如果不reject request，系统会出问题的。）
//
// 
//
//ThreadFactory 
//可以通过设置默认的ThreadFactory来改变threadpool如何创建thread
//
//keep-alive time 
//如果实际的线程数大于coresize，那么这些超额的thread过了keep-alive的时间之后，就会被kill掉。
//这个时间是可以动态设定的；
//
//queue 
//任何一个BlockingQueue都可以做为threadpool中的队列，又可以分为三种：
//AsynchronousQueue，采用这种queue，任何的task会被直接交到thread手中，
//queue本身不缓存任何的task，所以如果所有的线程在忙的话，新进入的task是会被拒绝的；
//LinkedBlockingQueue，queue的size是无限的，根据前面的调度策略可知，thread的size永远也不会大于coresize；
//ArrayBlockingQueue，这其实是需要仔细调整参数的一种方式。
//因为通过设定maxsize和queuesize，其实就是设定这个threadpool所能使用的resource，然后试图达到一种性能的最优；
//
//此外，还有诸如beforeExecute，afterExecute等方法可以被重写。
//以上的这些内容其实都可以在ThreadPoolExecutor的javadoc中找到。应该说，ThreadPoolExecutor是可以非常灵活的被设置的，只除了一点，你没办法改变它的调度策略。
public class FutureTaskDemo
{
	public static void main(String[] args)
	{
		// 初始化一个Callable对象和FutureTask对象；
		Callable pAccount = new PrivateAccount();
		FutureTask futureTask = new FutureTask(pAccount);
		// 使用FutureTask创建一个线程
		Thread pAccountThread = new Thread(futureTask);
		System.out.println("future task starts at " + System.nanoTime());
		// 启动线程
		pAccountThread.start();
		// 主线程执行自己的任务
		System.out.println("main thread doing something else here. ");
		// 从其他帐户获取总金额
		int totalMoney = new Random().nextInt(100000);
		System.out.println(" You have " + totalMoney + " in your other Accounts. ");
		System.out.println(" Waiting for data from Private Account ");
		// 测试后台的就计算线程是否完成，如果未完成，等待
		while (!futureTask.isDone()) {
			try {
				Thread.sleep(5);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("future task ends at " + System.nanoTime());
		Integer privataAccountMoney = null;
		// 如果后台的FutureTask计算完成，则返回计算结果
		try {
			privataAccountMoney = (Integer) futureTask.get();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println(" The total moeny you have is " + (totalMoney + privataAccountMoney.intValue()));
	}
}

// 创建一个Callable类，模拟计算一个私有帐户中的金额
class PrivateAccount implements Callable
{
	Integer	totalMoney;

	@Override
	public Integer call() throws Exception
	{
		// 为了延长计算时间，这里暂停几秒
		Thread.sleep(5000);
		totalMoney = new Integer(new Random().nextInt(10000));
		System.out.println(" You have " + totalMoney + " in your private Account. ");
		return totalMoney;
	}
}
