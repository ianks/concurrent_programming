import java.util.List;
import java.util.Arrays;

public class ConcurrentQueueTest implements Runnable {
    private static ConcurrentQueue<String> q;
    private static List<String> list;
    private static final int threadCount = 4;
    private final int id;

    public ConcurrentQueueTest(ConcurrentQueue<String> _q, int _id) {
        q = _q;
        id = _id;
    }

    public void run() {
        testMixed(q);
    }

    public void testMixed(ConcurrentQueue<String> q) {
        try {
            if (id == 1 || id == 2) {
                q.push("test");
                q.push("test");
            } else {
                Thread.sleep(200);
                String result = q.pop();

                if (result == "test")
                    pass("Tests passed.");
                else
                    fail("Tests failed.");
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void pass(String msg) {
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_RESET = "\u001B[0m";
        long id = Thread.currentThread().getId();
        msg = "Thread " + id + ": " + msg;

        System.out.println(ANSI_GREEN + msg + ANSI_RESET);
    }

    private static void fail(String msg) {
        String ANSI_RED = "\u001B[31m";
        String ANSI_RESET = "\u001B[0m";
        long id = Thread.currentThread().getId();
        msg = "Thread " + id + ": " + msg;

        System.out.println(ANSI_RED + msg + ANSI_RESET);
    }

    public static void spawnThreads(ConcurrentQueue<String> q) {
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new ConcurrentQueueTest(q, i));
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
        System.out.println("\nStarting tests for BoundedQueue.java...");
        q = new BoundedQueue<String>(64);
        spawnThreads(q);

        System.out.println("\n========================");

        System.out.println("\nStarting tests for BoundedQueueSignaler.java...");
        q = new BoundedQueueSignaler<String>(64);
        spawnThreads(q);
    }
}
