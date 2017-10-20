package de.ovgu.featureide.fm.core.explanations.evaluation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

/**
 * Executes {@link Test tests}.
 * 
 * @author Timo G&uuml;nther
 * @see {@link Test}
 */
public class TestRunner {

	/** Whether to print output. */
	private static final boolean VERBOSE = true;

	/** Whether to run tests in parallel instead of sequence. */
	private boolean multiThreaded = false;

	/**
	 * Returns true iff the tests are executed in parallel.
	 * @return if this is multi-threaded
	 */
	public boolean isMultiThreaded() {
		return multiThreaded;
	}

	/**
	 * Sets whether tests are executed in parallel.
	 * @param multiThreaded whether to run tests in parallel instead of sequence
	 */
	public void setMultiThreaded(boolean multiThreaded) {
		this.multiThreaded = multiThreaded;
	}

	/**
	 * Executes the given tests.
	 * 
	 * @param tests tests to execute; not null
	 */
	public void run(Collection<? extends Test> tests) {
		run(tests.toArray(new Test[tests.size()]));
	}

	/**
	 * Executes the given tests.
	 * 
	 * @param tests tests to execute; not null
	 */
	public void run(Test... tests) {
		log("Running %d test%s.", tests.length, tests.length == 1 ? "" : "s");
		int longestNameLength = 0;
		final List<Thread> ts = new ArrayList<>(tests.length);
		for (final Test test : tests) {
			final String name = test.getName();
			longestNameLength = Math.max(longestNameLength, name.length());
			final Thread t = new Thread(test, name);
			ts.add(t);
			log("Started %s.", name);
			t.start();
			if (!isMultiThreaded()) {
				try {
					t.join();
				} catch (InterruptedException e) {}
			}
		}
		for (final Thread t : ts) {
			try {
				t.join();
			} catch (InterruptedException e) {}
		}
		log("Finished all tests.");
		if (VERBOSE) {
			System.out.println();
			final List<Map<String, Object>> results = new ArrayList<>(tests.length);
			for (final Test test : tests) {
				results.add(test.getTestResults());
			}
			System.out.println(tabulate(results));
		}
	}

	/**
	 * Logs the given message.
	 * 
	 * @param msg message to log; not null
	 * @param params parameters of the message
	 */
	protected void log(String msg, Object... params) {
		if (!VERBOSE) {
			return;
		}
		msg = String.format(msg, params);
		final String now = String.format("%tFT%<tTZ.%<tL", Calendar.getInstance(TimeZone.getTimeZone("Z")));
		msg = String.format("%s: %s", now, msg);
		System.out.println(msg);
	}

	/**
	 * Tabulates the given test results.
	 * 
	 * @param results test results to tabulate; not null
	 * @return tabulated test results; not null
	 */
	private String tabulate(Collection<? extends Map<?, ?>> results) {
		final Map<Object, Integer> widths = new LinkedHashMap<>();
		for (final Map<?, ?> result : results) {
			for (final Entry<?, ?> e : result.entrySet()) {
				int width;
				if (widths.containsKey(e.getKey())) {
					width = widths.get(e.getKey());
				} else {
					width = asString(e.getKey()).length();
				}
				width = Math.max(width, asString(e.getValue()).length());
				widths.put(e.getKey(), width);
			}
		}

		String s = "";
		final Map<Object, Object> header = new LinkedHashMap<>();
		for (final Object key : widths.keySet()) {
			header.put(key, key);
		}
		s += writeResult(header, widths);
		s += System.lineSeparator();
		for (final Map<?, ?> result : results) {
			s += writeResult(result, widths);
			s += System.lineSeparator();
		}
		return s;
	}

	/**
	 * Writes the results of a single test case.
	 * 
	 * @param result results of a single test case
	 * @param widths widths of each column
	 * @return one row of results
	 */
	private String writeResult(Map<?, ?> result, Map<?, Integer> widths) {
		String s = "";
		boolean first = true;
		for (final Entry<?, Integer> e : widths.entrySet()) {
			if (first) {
				first = false;
			} else {
				s += " | ";
			}
			final Object key = e.getKey();
			Object value;
			int width = e.getValue();
			if (result.containsKey(key)) {
				value = result.get(key);
				if (!(value instanceof Number || value instanceof Boolean)) {
					width = -width; // align everything but numbers and booleans on the left
				}
				value = asString(value);
			} else {
				value = "";
			}
			s += String.format("%" + width + "s", value);
		}
		return s;
	}

	/**
	 * Returns the given object as a string for use in tables.
	 * 
	 * @param obj object to transform
	 * @return string that can be used in tables
	 */
	private static String asString(Object obj) {
		if (obj instanceof Double || obj instanceof Float) {
			return String.format("%.3f", obj);
		}
		return String.valueOf(obj);
	}
}
