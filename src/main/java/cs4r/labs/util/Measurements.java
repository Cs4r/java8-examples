package cs4r.labs.util;

import java.util.function.Supplier;

/**
 * Utility class to measure times
 * 
 * @author cs4r
 *
 */
public class Measurements {
	/** How many times to repeat the test. 5 seems to give reasonable results */
	private static final int RUN_COUNT = 1;

	/**
	 * Repeatedly generate results using a Supplier to eliminate some of the
	 * issues of running a micro-benchmark.
	 *
	 * @param <T>
	 *            The type of result generated by the Supplier
	 * @param label
	 *            Description of what's being measured
	 * @param supplier
	 *            The Supplier to measure execution time of
	 * @return The last execution time of the Supplier code
	 */
	public static <T> T measure(String label, Supplier<T> supplier) {
		T result = null;

		for (int i = 0; i < RUN_COUNT; i++)
			result = measureOneRun(label, supplier);

		return result;
	}

	/**
	 * Used by the measure method to determine how long a Supplier takes to
	 * return a result.
	 *
	 * @param <T>
	 *            The type of the result provided by the Supplier
	 * @param label
	 *            Description of what's being measured
	 * @param supplier
	 *            The Supplier to measure execution time of
	 * @return
	 */
	private static <T> T measureOneRun(String label, Supplier<T> supplier) {
		long startTime = System.nanoTime();
		T result = supplier.get();
		long endTime = System.nanoTime();
		System.out.printf("%s took %dms%n", label, (endTime - startTime + 500_000L) / 1_000_000L);
		return result;
	}

}