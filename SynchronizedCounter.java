
public class SynchronizedCounter {
	
    private static int threadCount = 0;

    public static synchronized void increment() {
    	threadCount++;
    }

    public static synchronized  void decrement() {
    	threadCount--;
    }

    public static synchronized int getValue() {
        return threadCount;
    }
}