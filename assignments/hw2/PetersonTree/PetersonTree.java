import java.util.ArrayList;
import java.util.List;

public class PetersonTree {
	int numOfThreads;
	public static volatile int size;
	public static PetersonNode root;
	public static List<PetersonNode> leaves;

	public PetersonTree(int threads) {
		validateArgs(threads);

		numOfThreads = threads;
		root = new PetersonNode(null);
		size++;

		List<PetersonNode> initList = new ArrayList<>();
		initList.add(root);

		leaves = growLockTree(initList);
	}

	// Attempt to lock all nodes starting from the threads corresponding
	// leaf, all the way to the root of the tree.
	public void lock(int me) {
		PetersonNode currentNode = leafLockForThread(me);

		while (currentNode.parent != null) {
			currentNode.lock(me);
			currentNode = currentNode.parent;
		}
	}

	public void unlock(int me) {
		PetersonNode currentNode = leafLockForThread(me);

		while (currentNode.parent != null) {
			currentNode.unlock(me);
			currentNode = currentNode.parent;
		}
	}

	// Helper method which returns the correct leaf node for a thread.
	private PetersonNode leafLockForThread(int num) {

		// We divide by 2 becauase each lock shares two threads.
		//   Thread 0 => leafLock 0
		//   Thread 1 => leafLock 0
		//   Thread 2 => leafLock 1
		//   Thread 3 => leafLock 1
		return leaves.get(num / 2);
	}

	// Since out number of threads will be a power of two, we can safely
	// assume that our binary tree will always have leaves such that the
	// cardinality of the leaves will always be a power of 2. Thus, we grow
	// the tree row-by-row, per-se. This algorithm recursively fills out
	// the leaves for a binary tree until the tree has a number of leaves
	// equal to the number of total threads/2.
	private List<PetersonNode> growLockTree(List<PetersonNode> nodes) {
		// Base case:
		// 	When the number of leaves we have is == 1/2 *
		// 	number of threads
		if (nodes.size() == numOfThreads / 2)
			return nodes;

		// This array will be the current leaves in the iteration for
		// creating the PetersonTree
		List<PetersonNode> currentLeaves = new ArrayList<>();

		// Loop through each node, add two childent and store them as
		// the current leaves
		for (PetersonNode node : new ArrayList<PetersonNode>(nodes)) {
			node.leftChild = new PetersonNode(node);
			node.rightChild = new PetersonNode(node);

			currentLeaves.add(node.leftChild);
			currentLeaves.add(node.rightChild);

			size += 2;
		}

		// Recurse, passing our current leaves back to the function
		return growLockTree(currentLeaves);
	}

	@Override public String toString() {
		return    "TotalSize: " + size + "\n"
	       		+ "Leaves size: " + leaves.size();
	}

	private void validateArgs(int threads) {
		if ((threads & -threads) != threads) {
			throw new IllegalArgumentException("Thread count must be power of 2.");
		}
	}
}
