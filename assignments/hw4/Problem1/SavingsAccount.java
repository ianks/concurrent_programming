import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class SavingsAccount {
	public volatile Integer balance;
	private final ReentrantLock depositLock, withdrawLock;
	private Condition deposited;


	public SavingsAccount() {
		depositLock = new ReentrantLock();
		withdrawLock = new ReentrantLock();
		deposited = depositLock.newCondition();
		balance = 0;
	}

	public Integer deposit(Integer k) {
		depositLock.lock();

		try {
			balance += k;
		} finally {
			depositLock.unlock();
			deposited.signalAll();
		}

		return k;
	}

	public Integer withdraw(Integer k) {
		withdrawLock.lock();
		try {
			while (balance < k) deposited.awaitUninterruptibly();
			balance -= k;
		} finally {
			withdrawLock.unlock();
		}

		return k;
	}

	public Integer getBalance() {
		return balance;
	}
}
