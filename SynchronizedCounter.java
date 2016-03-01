/**
 * @file: SynchronizedCounter.java
 * 
 * @author: Satvik Dhandhania <sdhandha@andrew.cmu.edu>
 * 			Gobinath Iyyanan Vellaiappan <giyyanan@andrew.cmu.edu> 
 * 
 * @date: Mar 1, 2016 1:13:37 AM EST
 * 
 */

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