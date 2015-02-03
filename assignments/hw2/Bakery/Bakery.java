import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

class Bakery {
	AtomicLongArray flag;
	AtomicLongArray label;
	AtomicLong max = new AtomicLong(0);

	public Bakery(int threadCount) {
		// Initial zero'd out array to create AtomicLongArray with
		long[] zeros = new long[threadCount];

		// flag signifies interest in critical section
		flag = new AtomicLongArray(zeros);

		// label signifies the order in which one recieved the lock
		label = new AtomicLongArray(zeros);
	}

	public void lock(int me) {
		flag.set(me, 1);
		label.set(me, max.incrementAndGet());

		while (flagIsUpAndlabelIsSmallest(me)) {}
	}

	public void unlock(int me) {
		flag.set(me, 0);
	}

	// Check to see that the flag is up and that the label is smaller than
	// current thread's ID
	private boolean flagIsUpAndlabelIsSmallest(int me) {
		for (int k = 0; k < flag.length(); k++) {
			if (flagIsUp(k) && isMinimumlabel(k, me)) {
				return true;
			}
		}

		return false;
	}

	private boolean flagIsUp(int k) {
		return flag.get(k) == 1;
	}

	private boolean isMinimumlabel(int k, int me) {
		return label.get(k) < label.get(me);
	}
}
