import java.util.Arrays;

public class BoolMRSWRegister implements Register<Boolean> {
	private Boolean[] arr;

	public BoolMRSWRegister(int size) {
		arr = new Boolean[size];
	}

	public Boolean read(int tid) {
		return arr[tid];
	}

	public void write(Boolean data) {
		Arrays.fill(arr, data);
	}
}
