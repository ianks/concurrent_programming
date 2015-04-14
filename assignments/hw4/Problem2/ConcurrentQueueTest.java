import java.util.List;
import java.util.Arrays;

public class ConcurrentQueueTest {
    private static Broadcaster<String> broadcaster;
    private static List<String> list;

    public static void testPush(ConcurrentQueue<String> q) {
        for (String item : list)
            q.push("");

        if (q.getSize() == 3)
            pass("testPop() passed.");
        else
            fail("testPop() failed.");
    }

    public static void testPop(ConcurrentQueue<String> q) {
        for (String item : list)
            q.push(item);

        boolean passed = (q.pop() == "Cheerios") &&
                         (q.pop() == "Feta cheese") &&
                         (q.pop() == "Salmon smoothie");

        if (passed)
            pass("testPop() passed");
        else
            fail("testPop() failed.");
    }

    public static void testContains(ConcurrentQueue<String> q) {
        for (String item : list)
            q.push(item);

        if (q.contains("Salmon smoothie"))
            pass("testContains() passed");
        else
            fail("testContains() failed.");
    }

    private static void pass(String msg) {
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_RESET = "\u001B[0m";

        System.out.println(ANSI_GREEN + msg + ANSI_RESET);
    }

    private static void fail(String msg) {
        String ANSI_RED = "\u001B[31m";
        String ANSI_RESET = "\u001B[0m";

        System.out.println(ANSI_RED + msg + ANSI_RESET);
    }

    public static void main(String[] args) {
        list = Arrays.asList("Cheerios", "Feta cheese", "Salmon smoothie");

        testPop(new Broadcaster<String>(32));
        testPush(new Broadcaster<String>(32));
        testContains(new Broadcaster<String>(32));
    }
}
