package concurrent.sync;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
//http://blog.csdn.net/historyasamirror/article/details/5961368
//ThreadPoolExecutor
// corePoolSize �� maxPoolSize

//������������ʵ��threadpool�ĵ��Ȳ���������أ�
//���poolsizeС��coresize����ôֻҪ����һ��request�����´���һ��thread��ִ�У�
//���poolsize�Ѿ����ڻ����coresize����ô����һ��request�󣬾ͷŽ�queue�У������߳�ִ�У�
//һ����ֻ��queue���ˣ��Ż��ִ����µ�thread��ִ�У�
//��Ȼ��coresize��maxpoolsize����������ʱͨ��set��������̬�ĵ��ڣ�
//��queue�����һ��ȷ��size�Ķ��У���ô���п��ܷ���reject request�����飨��Ϊ�������ˣ���
//�ܶ��˻���Ϊ������ϵͳ���á�����ʵ��reject request�ܶ�ʱ���Ǹ����£�
//��Ϊ�����ش���ϵͳ��capacity��ʱ�������reject request��ϵͳ�������ġ���
//
// 
//
//ThreadFactory 
//����ͨ������Ĭ�ϵ�ThreadFactory���ı�threadpool��δ���thread
//
//keep-alive time 
//���ʵ�ʵ��߳�������coresize����ô��Щ�����thread����keep-alive��ʱ��֮�󣬾ͻᱻkill����
//���ʱ���ǿ��Զ�̬�趨�ģ�
//
//queue 
//�κ�һ��BlockingQueue��������Ϊthreadpool�еĶ��У��ֿ��Է�Ϊ���֣�
//AsynchronousQueue����������queue���κε�task�ᱻֱ�ӽ���thread���У�
//queue���������κε�task������������е��߳���æ�Ļ����½����task�ǻᱻ�ܾ��ģ�
//LinkedBlockingQueue��queue��size�����޵ģ�����ǰ��ĵ��Ȳ��Կ�֪��thread��size��ԶҲ�������coresize��
//ArrayBlockingQueue������ʵ����Ҫ��ϸ����������һ�ַ�ʽ��
//��Ϊͨ���趨maxsize��queuesize����ʵ�����趨���threadpool����ʹ�õ�resource��Ȼ����ͼ�ﵽһ�����ܵ����ţ�
//
//���⣬��������beforeExecute��afterExecute�ȷ������Ա���д��
//���ϵ���Щ������ʵ��������ThreadPoolExecutor��javadoc���ҵ���Ӧ��˵��ThreadPoolExecutor�ǿ��Էǳ����ı����õģ�ֻ����һ�㣬��û�취�ı����ĵ��Ȳ��ԡ�
public class FutureTaskDemo
{
	public static void main(String[] args)
	{
		// ��ʼ��һ��Callable�����FutureTask����
		Callable pAccount = new PrivateAccount();
		FutureTask futureTask = new FutureTask(pAccount);
		// ʹ��FutureTask����һ���߳�
		Thread pAccountThread = new Thread(futureTask);
		System.out.println("future task starts at " + System.nanoTime());
		// �����߳�
		pAccountThread.start();
		// ���߳�ִ���Լ�������
		System.out.println("main thread doing something else here. ");
		// �������ʻ���ȡ�ܽ��
		int totalMoney = new Random().nextInt(100000);
		System.out.println(" You have " + totalMoney + " in your other Accounts. ");
		System.out.println(" Waiting for data from Private Account ");
		// ���Ժ�̨�ľͼ����߳��Ƿ���ɣ����δ��ɣ��ȴ�
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
		// �����̨��FutureTask������ɣ��򷵻ؼ�����
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

// ����һ��Callable�࣬ģ�����һ��˽���ʻ��еĽ��
class PrivateAccount implements Callable
{
	Integer	totalMoney;

	@Override
	public Integer call() throws Exception
	{
		// Ϊ���ӳ�����ʱ�䣬������ͣ����
		Thread.sleep(5000);
		totalMoney = new Integer(new Random().nextInt(10000));
		System.out.println(" You have " + totalMoney + " in your private Account. ");
		return totalMoney;
	}
}
