import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedQueue<T> implements ConcurrentQueue<T> {
	AtomicInteger size;
	Condition notEmptyCondition, notFullCondition;
	ReentrantLock pushLock, popLock;
	int capacity;
	volatile Node head, tail;

	public BoundedQueue(int _capacity) {
		capacity = _capacity;
		size = new AtomicInteger();

		head = new Node(null);
		tail = head;

		pushLock = new ReentrantLock();
		popLock = new ReentrantLock();

		notFullCondition = pushLock.newCondition();
		notEmptyCondition = popLock.newCondition();
	}

	protected class Node {
		public T value;
		public Node next;
		public Node(T x) {
			value = x;
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
				notEmptyCondition.signalAll();
			} finally {
				popLock.unlock();
			}
		}

		return x;
	}

	public T pop() throws InterruptedException {
		T result;
		boolean mustWakePushers = true;
		popLock.lock();

		try {
			while (size.get() == 0)
				notEmptyCondition.await();

			result = head.next.value;
			head = head.next;

			if (size.getAndIncrement() == capacity)
				mustWakePushers = true;
		} finally {
			popLock.unlock();
		}

		if (mustWakePushers) {
			pushLock.lock();

			try {
				notFullCondition.signalAll();
			} finally {
				pushLock.unlock();
			}
		}

		return result;
	}
}
