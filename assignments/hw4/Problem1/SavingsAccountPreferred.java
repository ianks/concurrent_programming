import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.atomic.AtomicInteger;

public class SavingsAccountPreferred {
	public volatile Integer balance;
	private final Lock depositLock, depositPreferredLock;
	private Condition depositCondition, depositPreferredCondition;
	private AtomicInteger preferredWaiting;


	public SavingsAccountPreferred() {
		preferredWaiting = new AtomicInteger();
		balance = 0;
		depositLock = new ReentrantLock();
		depositPreferredLock = new ReentrantLock();
		depositCondition = depositLock.newCondition();
		depositPreferredCondition = depositPreferredLock.newCondition();
	}

	public Integer deposit(Integer k) throws InterruptedException {
		if (preferredWaiting.get() > 0) {
			depositLock.lock();

			try {
				balance += k;
				depositCondition.signalAll();

				return k;
			} finally {
				depositLock.unlock();
			}

		} else {
			depositPreferredLock.lock();

			try {
				balance += k;
				depositPreferredCondition.signalAll();

				return k;
			} finally {
				depositPreferredLock.unlock();
			}

		}
	}

	public Integer withdrawPreferred(Integer k) throws InterruptedException {
			preferredWaiting.getAndIncrement();
			depositPreferredLock.lock();

			try {
				while (balance < k) depositPreferredCondition.await();
				balance -= k;
				preferredWaiting.getAndDecrement();
				return k;
			} finally {
				depositPreferredLock.unlock();
			}
	}

	public Integer withdraw(Integer k) throws InterruptedException {
			depositLock.lock();

			try {
				while (balance < k) depositCondition.await();
				balance -= k;

				return k;
			} finally {
				depositLock.unlock();
			}
	}

	public Integer getBalance() {
		return balance;
	}
}
