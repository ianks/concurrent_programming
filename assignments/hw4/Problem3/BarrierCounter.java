import java.util.concurrent.locks.ReentrantLock;

public class BarrierCounter implements Runnable{
	private static int threadCount = 16;
	private static volatile int counter = 0;
	private static ReentrantLock lock = new ReentrantLock();

	public void run() {
		foo();

		// Increment counter safely
		lock.lock();
		counter++;
		lock.unlock();

		// Spin until all threads have run foo()
		while (counter < threadCount) {}

		bar();
	}

	public static void foo() {
		System.out.println("foo()");

		try {
			Thread.sleep(20);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	public static void bar() {
		System.out.println("bar()");

		try {
			Thread.sleep(20);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	public static void spawnThreads() {
		Thread[] threads = new Thread[threadCount];

		for (int i = 0; i < threadCount; i++) {
			threads[i] = new Thread(new BarrierCounter());
			threads[i].start();
		}

		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		spawnThreads();
	}
}
