package concurrent.sync;

//http://hck.iteye.com/blog/1596441
//ThreadLocal是什么呢？其实ThreadLocal并非是一个线程的本地实现版本，它并不是一个Thread，
//而是thread local variable（线程局部变量）。也许把它命名为ThreadLocalVar更加合适。
//线程局部变量（ThreadLocal）其实的功用非常简单，就是为每一个使用该变量的线程都提供一个变量值的副本，
//是每一个线程都可以独立地改变自己的副本，而不会和其它线程的副本冲突。
//从线程的角度看，就好像每一个线程都完全拥有该变量。
//线程局部变量并不是Java的新发明，在其它的一些语言编译器实现（如IBM XL FORTRAN）中，
//它在语言的层次提供了直接的支持。因为Java中没有提供在语言层次的直接支持，
//而是提供了一个ThreadLocal的类来提供支持，所以，在Java中编写线程局部变量的代码相对比较笨拙，
//这也许是线程局部变量没有在Java中得到很好的普及的一个原因吧。 

public class SequenceNumber
{
	// 定义匿名子类创建ThreadLocal的变量
	private static ThreadLocal<Integer>	seqNum	= new ThreadLocal<Integer>()
												{
													// 覆盖初始化方法
													public Integer initialValue()
													{
														return 0;
													}
												};

	// 下一个序列号
	public int getNextNum()
	{
		seqNum.set(seqNum.get() + 1);
		return seqNum.get();
	}

	private static class TestClient extends Thread
	{
		private SequenceNumber	sn;

		public TestClient(SequenceNumber sn)
		{
			this.sn = sn;
		}

		// 线程产生序列号
		public void run()
		{
			for (int i = 0; i < 3; i++) {
				System.out.println("thread[" + Thread.currentThread().getName() + "] sn[" + sn.getNextNum() + "]");
			}
		}
	}

	/** * @param args */
	public static void main(String[] args)
	{
		SequenceNumber sn = new SequenceNumber();
		// 三个线程产生各自的序列号
		TestClient t1 = new TestClient(sn);
		TestClient t2 = new TestClient(sn);
		TestClient t3 = new TestClient(sn);
		t1.start();
		t2.start();
		t3.start();
	}
}
