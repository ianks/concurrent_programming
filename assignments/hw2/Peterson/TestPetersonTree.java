import java.util.concurrent.locks.Lock;

public class TestPetersonTree implements Runnable {
	public int me;
	public static final int countToThis = 1000;
	public static final int noOfExperiments = 1000;
	public static volatile int count = 0;
	public static int threadCount = 64;
	public static PetersonTree lock = new PetersonTree(threadCount);

	public TestPetersonTree(int newMe) {
		me = newMe;
	}

	public void run() {
		int i = 0;

		while (i < countToThis) {
			lock.lock(me);
			count = count + 1;
			i = i + 1;
			lock.unlock(me);
		}
	}

	public static void spawnThreads() {
		// We put the threads in an array for easy storage/access
		Thread[] threads = new Thread[threadCount];

		for (int i = 0; i < threadCount; i++) {
			threads[i] = new Thread(new TestPetersonTree(i));
			threads[i].start();
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String args[]) {
		int wrong = 0;
		long startTime = System.nanoTime();

		for (int i = 0; i < noOfExperiments; i++) {
			count = 0;

			spawnThreads();

			// Check to see that count is correct value
			if (count != threadCount*countToThis) {
				System.out.println("Wrong : " + count);
				wrong++;
			}
		}

		long endTime = System.nanoTime();
		System.out.println("That took " + (endTime - startTime)/1000000 + " milliseconds");
		System.out.println("Mistakes:  " + wrong + "/" + noOfExperiments);
	}
}
