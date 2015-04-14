import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.List;
import java.util.Arrays;

public class Broadcaster<T> implements ConcurrentQueue<T> {
	private final Node head, tail;
	private final ReentrantLock popLock, pushLock;
	private final Integer capacity;
	private AtomicInteger size;
	private Condition notFullCondition, notEmptyCondition;

	private class Node {
		public T item;
		public Node next;
		public Node prev;

		public Node(T data) {
			item = data;
		}
	}

	public Broadcaster(int _capacity) {
		capacity = _capacity;

		head = new Node(null);
		tail = new Node(null);

		head.prev = tail;
		tail.next = head;

		pushLock = new ReentrantLock();
		popLock = new ReentrantLock();

		notFullCondition = pushLock.newCondition();
		notEmptyCondition = popLock.newCondition();

		size = new AtomicInteger(0);
	}

	public T push(T item) {
		boolean mustAwakeDequeuers = false;
		pushLock.lock();

		try {
			while (isFull()) notFullCondition.awaitUninterruptibly();

			Node newNode = new Node(item);
			newNode.next = tail.next;

			if (isEmpty()) head.prev = newNode;

			tail.next.prev = newNode;
			tail.next = newNode;

			if (size.incrementAndGet() == 0) mustAwakeDequeuers = true;
		} finally {
			pushLock.unlock();
		}

		if (mustAwakeDequeuers) {
			popLock.lock();

			try {
				notEmptyCondition.signalAll();
			} finally {
				popLock.unlock();
			}
		}

		return item;
	}

	public T pop() {
		boolean mustAwakeEnqueuers = false;
		T poppedData;
		popLock.lock();

		try {
			while(isEmpty()) notEmptyCondition.awaitUninterruptibly();

			poppedData = head.prev.item;
			head.prev = head.prev.prev;

			if (size.decrementAndGet() == capacity)
				mustAwakeEnqueuers = true;
		} finally {
			popLock.unlock();
		}

		if (mustAwakeEnqueuers) {
			pushLock.lock();

			try {
				notFullCondition.signalAll();
			} finally {
				pushLock.unlock();
			}
		}

		return poppedData;
	}

	public int getSize() {
		return size.get();
	}

	private boolean isEmpty() {
		return size.get() == 0;
	}

	private boolean isFull() {
		return size.get() == capacity;
	}
}
