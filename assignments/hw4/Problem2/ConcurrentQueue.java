public interface ConcurrentQueue<T> {
	T push(T item) throws InterruptedException;
	T pop() throws InterruptedException;
}
