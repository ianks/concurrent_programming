public interface ConcurrentQueue<T> {
	int getSize();
	T push(T item);
	T pop();
}
