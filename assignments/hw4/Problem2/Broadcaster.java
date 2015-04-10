import java.util.concurrent.locks.ReentrantLock;
import java.util.List;
import java.util.Arrays;

public class Broadcaster<T> implements ConcurrentQueue<T> {
	private final Node head, tail;
	private final ReentrantLock lock = new ReentrantLock();
	private volatile int size;

	private class Node {
		public T item;
		public Node next;
		public Node prev;

		public Node(T data) {
			item = data;
		}
	}

	public Broadcaster() {
		head = new Node(null);
		tail = new Node(null);

		head.prev = tail;
		tail.next = head;

		size = 0;
	}

	public T push(T item) {
		Node newNode = new Node(item);
		newNode.next = tail.next;

		if (isEmpty()) head.prev = newNode;

		tail.next.prev = newNode;
		tail.next = newNode;

		size++;

		return item;
	}

	public T pop() {
		if (isEmpty()) return null;

		T poppedData = head.prev.item;
		head.prev = head.prev.prev;

		size--;

		return poppedData;
	}

	public boolean contains(T item) {
		Node current = tail.next;

		for (int i = 0; i < size; i++) {
			if (current.item == item) return true;
			current = current.next;
		}

		return false;
	}

	private boolean isEmpty() {
		return size == 0;
	}

	public static void main(String[] args) {
		Broadcaster<String> queue;
		queue = new Broadcaster<String>();

		List<String> foods = Arrays.asList(
			"Cheerios",
			"Feta cheese",
			"Salmon smoothie"
		);

		for (String food : foods)
			queue.push(food);

		System.out.println(queue.contains("Cheerios"));
		System.out.println(queue.pop());
		System.out.println(queue.contains("Cheerios"));

		System.out.println(queue.pop());
		System.out.println(queue.pop());
		System.out.println(queue.pop());

	}
}
