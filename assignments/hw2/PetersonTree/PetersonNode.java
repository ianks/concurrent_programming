class PetersonNode {
	// The following variables are used for implementing a lock using
	// Peterson's protocol.  We use to variables for flags instead of an
	// array, as there is now way to make array elements volatile.
	private static volatile int victim;
	private static volatile int flag0;
	private static volatile int flag1;
	int value;
	PetersonNode leftChild;
	PetersonNode rightChild;
	PetersonNode parent;

	public PetersonNode(PetersonNode node) {
		parent = node;
	}

	public synchronized PetersonNode getParent() {
		return parent;
	}

	public void lock(int me) {
		int j = 1 - me;

		if (me == 0) {
			flag0 = 1;
			victim = 0;
			while ((flag1 == 1) && (victim == 0)) {}
		} else {
			flag1 = 1;
			victim = 1;
			while ((flag0 == 1) && (victim == 1)) {}
		}
	}

	public synchronized void unlock(int me) {
		if (me == 0) {
			flag0 = 0;
		} else {
			flag1 = 0;
		}
	}
}
