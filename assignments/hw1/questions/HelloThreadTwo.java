public class HelloThreadTwo implements Runnable {
    // Create a counter that is volatile, and accessible by all threads
    private static volatile int counter = 0;

    // Create a lock for our counter, this is a basic  Object
    private static final Object counterLock = new Object();

    // Implement the run method so the thread has an entry-point
    public void run() {
        synchronizedIncrement();
    }

    public void synchronizedIncrement() {
        // We use a synchronized closure/block which grabs increments our
        // counter. No two threads can access this block at a single time,
        // thus, incrementing the counter is thread-safe. This does not,
        // however, ensure that reading the counter variable is thread-safe.
        // However, we only read once in the main thread. So in this case it
        // does not matter.
        synchronized (counterLock) {
            counter++;
        }
    }

    public static void main(String args[]) {
        // We put the threads in an array for easy storage/access
        Thread[] threads = new Thread[128];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new HelloThreadTwo());
            threads[i].start();

            // Join each thread with the main thread to make sure the program
            // does not terminate before threads finish
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Counter: " + counter);
    }
}
