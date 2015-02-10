import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicInteger;

class PetersonNode {
	private final AtomicIntegerArray flag = new AtomicIntegerArray(2);
	AtomicInteger victim = new AtomicInteger();

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
		flag.set(me, 1);
		victim.set(me);
		while(flag.get(j) == 1 && (victim.get() == me)) {};
	}

	public void unlock(int me) {
		flag.set(me, 0);
	}
}
