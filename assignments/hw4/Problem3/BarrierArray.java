import java.util.concurrent.locks.ReentrantLock;

public class BarrierArray implements Runnable{
	private static int threadCount = 16;
	private static boolean[] A = new boolean[threadCount];
	private static ReentrantLock lock = new ReentrantLock();
	private int id;

	public BarrierArray(int _id) {
		id = _id;
	}

	public void run() {
		foo();

		if (id == 0) {
			A[0] = true;

			while (!A[threadCount - 1]) {}
		} else {
			while (!A[id - 1]) {}

			A[id] = true;
		}

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
			threads[i] = new Thread(new BarrierArray(i));
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
