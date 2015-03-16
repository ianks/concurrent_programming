import java.util.Arrays;

public class AcmeRegister implements Register<Integer> {
	private final int n;
	private volatile BoolMRSWRegister[] b;

	public AcmeRegister(int threadCount) {
		n = threadCount;
		b = new BoolMRSWRegister[3 * n];

		for (int i = 0; i < b.length; i++)
			b[i] = new BoolMRSWRegister(n);
	}

	public void write(Integer x) {
		boolean[] v = intToBooleanArray(x);

		for (int i = 0; i < n; i++) {
			b[i].write(v[i]);
		}

		for (int i = 0; i < n; i++)
			b[n + i].write(v[i]);

		for (int i = 0; i < n; i++)
			b[2 * n + i].write(v[i]);
	}

	public Integer read(int tid) {
		int n2 = booleanArrayToInt(2, tid);
		int n1 = booleanArrayToInt(1, tid);
		int n0 = booleanArrayToInt(0, tid);

		return (n2 == n1) ? n2 : n0;
	}

	private boolean[] intToBooleanArray(int x) {
		boolean[] bitArray = new boolean[n];

		for (int i = n - 1; i >= 0; i--)
			bitArray[i] = (x & (1 << i)) != 0;

		return bitArray;
	}

	private int booleanArrayToInt(int offset, int tid) {
		int start = offset * n;
		int length = start + n - 1;
		int num = 0;

		for (int i = start; i < length; i++)
			num = (num << 1) + ((b[i].read(tid)) ? 1 : 0);

		return num;
	}

	public static void main(String [] args) {
		AcmeRegister reg = new AcmeRegister(8);
		reg.write(8);

		for (int i = 0; i < 8; i++) {
			System.out.println(String.format("Read %d from register %d", reg.read(i), i));
		}
	}
}
