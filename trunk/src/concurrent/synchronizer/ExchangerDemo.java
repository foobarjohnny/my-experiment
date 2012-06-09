package concurrent.synchronizer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Exchanger;

public class ExchangerDemo
{
	private static final Exchanger	ex	= new Exchanger();

	class DataProducer implements Runnable
	{
		private List	list	= new ArrayList();

		public void run()
		{
			for (int i = 0; i < 5; i++) {
				System.out.println("������һ�����ݣ���ʱ1��");
				list.add(new Date());
				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				// ������׼�����ڽ����������������ߵ�����
				list = (List) ex.exchange(list);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				System.out.println("Producer " + iterator.next());
			}
		}
	}

	class DataConsumer implements Runnable
	{
		private List	list	= new ArrayList();

		public void run()
		{
			for (int i = 0; i < 5; i++) {
				// �����߲������ݣ����潻����ʱ���������
				list.add("����һ��������");
			}
			try {
				// ���н������ݣ����������ߵ�����
				list = (List) ex.exchange(list);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Date d = (Date) iterator.next();
				System.out.println("consumer:" + d);
			}
		}
	}

	public static void main(String args[])
	{
		ExchangerDemo et = new ExchangerDemo();
		new Thread(et.new DataProducer()).start();
		new Thread(et.new DataConsumer()).start();
	}
}
