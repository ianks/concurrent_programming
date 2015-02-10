import java.util.concurrent.atomic.AtomicLong;

class Bakery {
	private boolean[] flag;
	private long[] label;
	private AtomicLong max = new AtomicLong(0);

	public Bakery(int threadCount) {
		// flag signifies interest in critical section
		flag = new boolean[threadCount];

		// label signifies the order in which one recieved the lock
		label = new long[threadCount];
	}

	public void lock(int me) {
		// Set our flag to true, meaning we are expressing interest in critical section
		flag[me] = true;

		// Here we set the label, with a max that is one larger
		// than the previous max
		label[me] = max.incrementAndGet();

		while (flagIsUpAndlabelIsSmallest(me)) {}
	}

	public void unlock(int me) {
		flag[me] = false;
	}

	// Check to see that the flag is up and that the label is smaller than
	// current thread's ID
	private boolean flagIsUpAndlabelIsSmallest(int me) {
		for (int k = 0; k < flag.length; k++) {
			if (flagIsUp(k) && isMinimumlabel(k, me)) {
				return true;
			}
		}

		return false;
	}

	private boolean flagIsUp(int k) {
		return flag[k] == true;
	}

	private boolean isMinimumlabel(int k, int me) {
		return label[k] < label[me];
	}
}
