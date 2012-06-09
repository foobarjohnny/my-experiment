package concurrent.jdkapidemo;

public class SimulatedCAS
{
	private int	value;

	public synchronized int getValue()
	{
		return value;
	}

	public synchronized int compareAndSwap(int expectedValue, int newValue)
	{
		if (value == expectedValue)
			value = newValue;
		return value;
	}
}