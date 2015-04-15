import java.util.concurrent.atomic.AtomicIntegerArray;

public class BarrierArray implements Runnable{
	private static int threadCount = 16;
	private static AtomicIntegerArray A = new AtomicIntegerArray(threadCount);
	private int id;

	public BarrierArray(int _id) {
		id = _id;
	}

	public void run() {
		foo();

		if (id == 0) {
			A.set(0, 1);

			while (A.get(threadCount - 1) != 1) {}
		} else {
			while (A.get(id - 1) != 1) {}

			A.set(id, 1);
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
