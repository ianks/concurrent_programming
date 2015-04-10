public interface ConcurrentQueue<T> {
	T push(T item);
	T pop();
	boolean contains(T item);
}
