public class HelloThreadOneA implements Runnable {
    // Create a counter that is volatile, and accessible by all threads
    private static volatile int counter = 0;

    // Implement the run method so the thread has an entry-point
    public void run() {
        // Get ID of the current thread and print
        long threadId = Thread.currentThread().getId();
        System.out.println("Hello from thread " + threadId);
    }

    public static void main(String args[]) {
        // We put the threads in an array for easy storage/access
        Thread[] threads = new Thread[128];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new HelloThreadOneA());
            threads[i].start();

            // Join each thread with the main thread to make sure the program
            // does not terminate before threads finish
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
