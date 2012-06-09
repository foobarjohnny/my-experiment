package concurrent.locks;

public class SynchronizedBenchmarkDemo implements Counter
{
	private long	count	= 0;

	public long getValue()
	{
		return count;
	}

	public synchronized void increment()
	{
		count++;
	}
}