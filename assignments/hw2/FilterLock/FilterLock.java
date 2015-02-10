import java.util.concurrent.atomic.AtomicIntegerArray;

public class FilterLock {
	AtomicIntegerArray level;
	AtomicIntegerArray victim;

	public FilterLock(int n) {
		level = new AtomicIntegerArray(n);
		victim = new AtomicIntegerArray(n);
	}

	public void lock(int me) {
		for (int i = 1; i < level.length(); i++) {
			level.set(me, i);
			victim.set(i, me);

			while (existsLevelsToGain(i, me)) {}
		}
	}

	public void unlock(int me) {
		level.set(me, 0);
	}

	// while ((∃k != me) (level[k] >= i && victim[i] == me)) {};
	private boolean existsLevelsToGain(int i, int me) {
		for (int k = 0; k < level.length(); k++) {
			if (k != me && level.get(k) >= i && victim.get(i) == me) {
				return true;
			}
		}

		return false;
	}
}
