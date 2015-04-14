import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class SavingsAccount {
	public volatile Integer balance;
	private final Lock depositLock;
	private Condition withdrawCondition;


	public SavingsAccount() {
		balance = 0;
		depositLock = new ReentrantLock();
		withdrawCondition = depositLock.newCondition();
	}

	public Integer deposit(Integer k) throws InterruptedException {
		depositLock.lock();

		try {
			balance += k;
			withdrawCondition.signalAll();

			return k;
		} finally {
			depositLock.unlock();
		}
	}

	public Integer withdraw(Integer k) throws InterruptedException {
		depositLock.lock();

		try {
			while (balance < k) withdrawCondition.await();
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
