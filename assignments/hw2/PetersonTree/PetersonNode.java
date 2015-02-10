import java.util.concurrent.atomic.AtomicInteger;

class PetersonNode {
	// We use an array of flags here because we have more than 2 threads
	private boolean[] flags;
	private AtomicInteger victim = new AtomicInteger();

	// Properties for binary tree node
	PetersonNode leftChild;
	PetersonNode rightChild;
	PetersonNode parent;

	public PetersonNode(PetersonNode node, int threadCount) {
	        flags = new boolean[threadCount];

		parent = node;
	}

	public void lock(int me) {
		flags[me] = true;
		victim.set(me);

		// Spin until my flag is unset and I am not victim
		while(multipleFlagsSet() && isTheVictim(me)) {};
	}

	public void unlock(int me) {
		flags[me] = false;
	}

	private boolean isTheVictim(int me) {
		return victim.get() == me;
	}

	// Check to see if more than 1 flags are set. If they are, that means
	// that someone has the lock.
	private boolean multipleFlagsSet() {
		int numFlagsSet = 0;

		for (int i = 0; i < flags.length; i++) {
			if (flags[i])
				numFlagsSet++;
			if (numFlagsSet > 1)
				return true;

		}

		return false;
	}
}
