package cs4r.labs.oracle.mooc;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import cs4r.labs.util.Measurements;

/**
 * Oracle JDK 8 MOOC Lesson 3 homework
 * 
 * @author cs4r
 */
public class Lesson3 {

	/**
	 * Computes the Levenshtein distance between every pair of words in the
	 * subset, and returns a matrix of distances. This actually computes twice
	 * as much as it needs to, since for every word a, b it should be the case
	 * that lev(a,b) == lev(b,a) i.e., Levenshtein distance is commutative.
	 *
	 * @param wordList
	 *            The subset of words whose distances to compute
	 * @param parallel
	 *            Whether to run in parallel
	 * @return Matrix of Levenshtein distances
	 */
	static int[][] computeLevenshtein(List<String> wordList, final boolean parallel) {
		final int LIST_SIZE = wordList.size();
		int[][] distances = new int[LIST_SIZE][LIST_SIZE];

		Supplier<IntStream> intsSupplier = () -> {
			IntStream intStream = IntStream.range(0, LIST_SIZE).parallel();

			if (!parallel) {
				intStream = intStream.sequential();
			}

			return intStream;
		};

		intsSupplier.get().forEach(row -> {
			intsSupplier.get().forEach(col -> {
				distances[row][col] = Levenshtein.lev(wordList.get(row), wordList.get(col));
			});
		});

		return distances;
	}

	/**
	 * Process a list of random strings and return a modified list
	 * 
	 * @param wordList
	 *            The subset of words whose distances to compute
	 * @param parallel
	 *            Whether to run in parallel
	 * @return The list processed in whatever way you want
	 */
	static List<String> processWords(List<String> wordList, boolean parallel) {

		Stream<String> stream = wordList.parallelStream();

		if (!parallel) {
			stream = stream.sequential();
		}

		return stream.map(String::toLowerCase).sorted().distinct().collect(Collectors.toList());
	}

	/**
	 * Main entry point for application
	 *
	 * @param args
	 *            the command line arguments
	 * @throws IOException
	 *             If word file cannot be read
	 */
	public static void main(String[] args) throws IOException {
		RandomWords fullWordList = new RandomWords();
		List<String> wordList = fullWordList.createList(10000);

		Measurements.measure("Sequential", () -> computeLevenshtein(wordList, false));
		Measurements.measure("Parallel", () -> computeLevenshtein(wordList, true));

		Measurements.measure("Sequential", () -> processWords(wordList, false));
		Measurements.measure("Parallel", () -> processWords(wordList, true));
	}
}