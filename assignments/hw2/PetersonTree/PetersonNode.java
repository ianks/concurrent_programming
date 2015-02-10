import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicInteger;

class PetersonNode {
	// We use an array of flags here because we have more than 2 threads
	AtomicIntegerArray flag;
	AtomicInteger victim = new AtomicInteger();

	// Properties for binary tree node
	PetersonNode leftChild;
	PetersonNode rightChild;
	PetersonNode parent;

	public PetersonNode(PetersonNode node, int threadCount) {
		// Create zero'd out array for flags
	        flag = new AtomicIntegerArray(threadCount);

		parent = node;
	}

	public void lock(int me) {
		flag.set(me, 1);
		victim.set(me);

		// Spin until my flag is unset and I am not victim
		while(multipleFlagsSet() && (victim.get() == me)) {};
	}

	public void unlock(int me) {
		flag.set(me, 0);
	}

	// Check to see if more than 1 flags are set. Ig they are, that means
	// that someone has the lock.
	private boolean multipleFlagsSet() {
		int numFlagsSet = 0;

		for (int i = 0; i < flag.length(); i++) {
			if (flag.get(i) == 1)
				numFlagsSet++;
			if (numFlagsSet > 1)
				return true;

		}

		return false;
	}
}
