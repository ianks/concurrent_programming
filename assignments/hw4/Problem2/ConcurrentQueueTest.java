import java.util.List;
import java.util.Arrays;

public class ConcurrentQueueTest implements Runnable {
    private static ConcurrentQueue<String> q;
    private static List<String> list;
    private static final int threadCount = 8;

    public ConcurrentQueueTest(ConcurrentQueue<String> _q) {
        q = _q;
    }

    public void run() {
        testPush(q);
        testPop(q);
    }

    public static void testPush(ConcurrentQueue<String> q) {
        q.push("test");

        if (q.getSize() > 0)
            pass("testPush() passed.");
        else
            fail("testPush() failed.");
    }

    public static void testPop(ConcurrentQueue<String> q) {
        if (q.pop() == "test")
            pass("testPop() passed");
        else
            fail("testPop() failed.");
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
            threads[i] = new Thread(new ConcurrentQueueTest(q));
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
        System.out.println("Starting tests for Broadcaster.java...");
        q = new Broadcaster<String>(threadCount);
        spawnThreads(q);
    }
}
