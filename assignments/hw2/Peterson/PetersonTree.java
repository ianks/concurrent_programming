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

		List<PetersonNode> initList = new ArrayList<>();
		initList.add(root);

		leaves = growLockTree(initList);
	}

	public List<PetersonNode> growLockTree(List<PetersonNode> nodes) {
		if (nodes.size() == numOfThreads)
			return nodes;

		List<PetersonNode> currentLeaves = new ArrayList<>();

		for (PetersonNode node : new ArrayList<PetersonNode>(nodes)) {
			node.leftChild = new PetersonNode(node);
			node.rightChild = new PetersonNode(node);

			currentLeaves.add(node.leftChild);
			currentLeaves.add(node.rightChild);

			size +=2;
		}

		return growLockTree(currentLeaves);
	}

	@Override public String toString() {
		return    "TotalSize: " + size + "\n"
	       		+ "Leaves size: " + leaves.size() + "\n";
	}

	private void validateArgs(int threads) {
		if ((threads & -threads) != threads) {
			throw new IllegalArgumentException("Thread count must be power of 2.");
		}
	}
}
