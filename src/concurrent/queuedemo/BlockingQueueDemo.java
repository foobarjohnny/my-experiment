package concurrent.queuedemo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueDemo
{
	public static void main(String[] args)
	{
		BlockingQueue<String> queue = new ArrayBlockingQueue<String>(5);
		BlockingQueue<String> queue2 = new LinkedBlockingQueue<String>();

		Producer p = new Producer(queue);
		Consumer c1 = new Consumer(queue);
		Consumer c2 = new Consumer(queue);
		new Thread(p).start();
		new Thread(c1).start();
		new Thread(c2).start();
	}
}

class Producer implements Runnable
{
	private final BlockingQueue<String>	queue;

	Producer(BlockingQueue<String> q)
	{
		queue = q;
	}

	public void run()
	{
		try {
			for (int i = 0; i < 100; i++) {
				queue.put(produce());
			}
		}
		catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	String produce()
	{
		String temp = "" + (char) ('A' + (int) (Math.random() * 26));
		System.out.println("produce " + temp);
		return temp;
	}
}

class Consumer implements Runnable
{
	private final BlockingQueue<String>	queue;

	Consumer(BlockingQueue<String> q)
	{
		queue = q;
	}

	public void run()
	{
		try {
			for (int i = 0; i < 100; i++) {
				consume(queue.take());
			}
		}
		catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	void consume(String x)
	{
		System.out.println("consume " + x);
	}
}