public interface ConcurrentQueue<T> {
	int getSize();
	T push(T item);
	T pop();
	boolean contains(T item);
}
