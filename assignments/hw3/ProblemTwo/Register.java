public interface Register<T> {
	void write(T data);
	T read(int tid);
}
