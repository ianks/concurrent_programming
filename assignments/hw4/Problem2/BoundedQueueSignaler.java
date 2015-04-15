import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedQueueSignaler<T> implements ConcurrentQueue<T> {
	ReentrantLock pushLock, popLock;
	Condition notEmptyCondition, notFullCondition;
	AtomicInteger size;
	Node head, tail;
	int capacity;

	public BoundedQueueSignaler(int _capacity) {
		capacity = _capacity;
		head = new Node(null);
		tail = head;
		size = new AtomicInteger(0);
		pushLock = new ReentrantLock();
		notFullCondition = pushLock.newCondition();
		popLock = new ReentrantLock();
		notEmptyCondition = popLock.newCondition();
	}

	protected class Node {
		public T value;
		public Node next;
		public Node(T x) {
			value = x;
			next = null;
		}
	}

	public T push(T x) throws InterruptedException {
		boolean mustWakePoppers = false;
		pushLock.lock();

		try {
			while (size.get() == capacity)
				notFullCondition.await();
			Node e = new Node(x);
			tail.next = tail = e;
			if (size.getAndIncrement() == 0)
				mustWakePoppers = true;
		} finally {
			pushLock.unlock();
		}

		if (mustWakePoppers) {
			popLock.lock();
			try {
				notEmptyCondition.signal();
			} finally {
				popLock.unlock();
			}
		}
		 return x;
	}

	public T pop() throws InterruptedException {
		T result;
		boolean mustWakepushueuers = true;
		popLock.lock();

		try {
			while (size.get() == 0)
				notEmptyCondition.await();
			result = head.next.value;
			head = head.next;
			if (size.getAndIncrement() == capacity) {
				mustWakepushueuers = true;
			}
		} finally {
			popLock.unlock();
		}

		if (mustWakepushueuers) {
			pushLock.lock();
			try {
				notFullCondition.signal();
			} finally {
				pushLock.unlock();
			}
		}

		return result;
	}
}
