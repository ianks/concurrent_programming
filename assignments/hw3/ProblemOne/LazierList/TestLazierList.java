public class TestLazierList implements Runnable {
	public final int me;
	public final String type;
	public static final int countToThis = 1000;
	public static final int threadCount = 16;
	public static LazierList<Integer> list;

	public TestLazierList(int newMe, String newType) {
		me = newMe;
		type = newType;
	}

	public void run() {
		if (type == "add") {
			for (int i = 0; i < countToThis; i++) {
				list.add(i + countToThis*me);
			}
		}

		if (type == "remove") {
			for (int i = 0; i < countToThis; i++) {
				if (list.add(i + countToThis*me))
					list.remove(i + countToThis*me);
			}
		}
	}

	public static void spawnThreads(String type) {
		// We put the threads in an array for easy storage/access
		Thread[] threads = new Thread[threadCount];

		for (int i = 0; i < threadCount; i++) {
			threads[i] = new Thread(new TestLazierList(i, type));
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

	public static void main(String args[]) {
		runTests();
	}

	private static boolean runTests() {
		long startTime = System.nanoTime();
		boolean testsPassed = testAdd() && testRemove();
		long totalTime = (System.nanoTime() - startTime)/1000000;

		System.out.println("\nLazier list add: " + totalTime + "ms");

		return testsPassed;
	}

	private static boolean testAdd() {
		System.out.println("\nTesting add()...");

		list = new LazierList<Integer>();
		spawnThreads("add");
		return expect(list.getSize(), countToThis * threadCount);
	}

	private static boolean testRemove() {
		System.out.println("\nTesting remove()...");

		list = new LazierList<Integer>();

		spawnThreads("remove");
		return expect(list.getSize(), 0);
	}

	private static boolean testContains() {
		System.out.println("\nTesting contains()...");

		list = new LazierList<Integer>();

		spawnThreads("contains");
		for (int i = 0; i < 1000; i++) {
			expect(list.contains(i) ? 1 : 0, 1);
		}

		return true;
	}

	private static boolean expect(int actual, int expected) {
		if (actual == expected) {
			pass("Test passed. List is correct size.");
		} else {
			fail("Test failed. List is incorrect size");
			fail("Expected " + expected + ", got " + actual);
		}

		return actual == expected;
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
}
