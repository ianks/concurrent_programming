public class SavingsAccountTest implements Runnable {
	public static final int countToThis = 2000;
	public static final int threadCount = 16;
	public SavingsAccount acct;

	public SavingsAccountTest() {
		acct = new SavingsAccount();
	}

	public void run() {
		testWithdraw();
	}

	public void testWithdraw() {
		try {
			acct.deposit(10);
			acct.withdraw(10);
		} catch (InterruptedException ex) {
			System.out.println(ex);
		}

		pass("Withdraw succeeded.");
	}

	public static void spawnThreads() {
		Thread[] threads = new Thread[threadCount];

		for (int i = 0; i < threadCount; i++) {
			threads[i] = new Thread(new SavingsAccountTest());
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
		spawnThreads();
	}
}
