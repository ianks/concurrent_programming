import java.util.concurrent.locks.Lock;

public class TestBakery implements Runnable {

    public int me;
    public static final int countToThis = 1000;
    public static final int noOfExperiments = 10000;
    public static final int threadCount = 8;
    public static volatile int count = 0;
    public static Bakery lock = new Bakery(threadCount);

    public TestBakery(int newMe) {
        me = newMe;
    }

    public void run() {
        for (int i = 0; i < countToThis; i++) {
	    lock.lock(me);
            count = count + 1;
	    lock.unlock(me);
        }
    }

    public static void spawnThreads() {
        // We put the threads in an array for easy storage/access
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new TestBakery(i));
            threads[i].start();
        }

        for (Thread t : threads) {
            // Join each thread with the main thread to make sure the program
            // does not terminate before threads finish
            try {
                t.join();
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
