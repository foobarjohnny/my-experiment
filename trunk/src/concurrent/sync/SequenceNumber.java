package concurrent.sync;

//http://hck.iteye.com/blog/1596441
//ThreadLocal��ʲô�أ���ʵThreadLocal������һ���̵߳ı���ʵ�ְ汾����������һ��Thread��
//����thread local variable���ֲ߳̾���������Ҳ���������ΪThreadLocalVar���Ӻ��ʡ�
//�ֲ߳̾�������ThreadLocal����ʵ�Ĺ��÷ǳ��򵥣�����Ϊÿһ��ʹ�øñ������̶߳��ṩһ������ֵ�ĸ�����
//��ÿһ���̶߳����Զ����ظı��Լ��ĸ�����������������̵߳ĸ�����ͻ��
//���̵߳ĽǶȿ����ͺ���ÿһ���̶߳���ȫӵ�иñ�����
//�ֲ߳̾�����������Java���·�������������һЩ���Ա�����ʵ�֣���IBM XL FORTRAN���У�
//�������ԵĲ���ṩ��ֱ�ӵ�֧�֡���ΪJava��û���ṩ�����Բ�ε�ֱ��֧�֣�
//�����ṩ��һ��ThreadLocal�������ṩ֧�֣����ԣ���Java�б�д�ֲ߳̾������Ĵ�����ԱȽϱ�׾��
//��Ҳ�����ֲ߳̾�����û����Java�еõ��ܺõ��ռ���һ��ԭ��ɡ� 

public class SequenceNumber
{
	// �����������ഴ��ThreadLocal�ı���
	private static ThreadLocal<Integer>	seqNum	= new ThreadLocal<Integer>()
												{
													// ���ǳ�ʼ������
													public Integer initialValue()
													{
														return 0;
													}
												};

	// ��һ�����к�
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

		// �̲߳������к�
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
		// �����̲߳������Ե����к�
		TestClient t1 = new TestClient(sn);
		TestClient t2 = new TestClient(sn);
		TestClient t3 = new TestClient(sn);
		t1.start();
		t2.start();
		t3.start();
	}
}
