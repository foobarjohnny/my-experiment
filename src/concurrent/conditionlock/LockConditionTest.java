package concurrent.conditionlock;

public class LockConditionTest
{
	public static void main(String args[])
	{
		LockedBuffer stack = new LockedBuffer();
		// 创建生产者，消费者
		int count = 3;
		Producer[] producers = new Producer[count];
		Consumer[] consumers = new Consumer[count];
		for (int i = 0; i < count; i++) {
			producers[i] = new Producer(stack);
			consumers[i] = new Consumer(stack);
		}
		for (int i = 0; i < count; i++) {
			new Thread(producers[i]).start();
			new Thread(consumers[i]).start();
		}
	}
}