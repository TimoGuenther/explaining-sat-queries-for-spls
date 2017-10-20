package de.ovgu.featureide.fm.core.explanations.evaluation.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A test that takes measurements.
 * 
 * @param <R> the type of the results of each step
 * @author Timo G&uuml;nther
 */
public abstract class MeasuringTest<R> extends AbstractTest<R> {

	/** Whether to write the measurements to a file. */
	private static final boolean WRITE_TO_FILE = true;
	/** The file to write the measurements to. */
	private static final File OUT_DIR = new File("results");

	/** The output stream. */
	private PrintWriter out;

	/**
	 * Constructs a new instance of this class.
	 * 
	 * @param iterations number of iterations to run
	 */
	protected MeasuringTest(int iterations) {
		super(iterations);
	}

	/**
	 * Returns true iff this is taking measurements.
	 * @return true iff this is taking measurements
	 */
	protected boolean isMeasuring() {
		return true;
	}

	@Override
	protected void runBeforeTest() {
		super.runBeforeTest();
		if (!isMeasuring()) {
			return;
		}
		final Collection<?> keys = getStepMeasurement().keySet();
		if (WRITE_TO_FILE) {
			out = openOut();
			out.println(asCsv(keys));
		}
	}

	@Override
	protected void runAfterStep() {
		super.runAfterStep();
		if (!isMeasuring()) {
			return;
		}
		final Collection<?> values = getStepMeasurement().values();
		if (WRITE_TO_FILE) {
			out.println(asCsv(values));
		}
	}

	@Override
	protected void runAfterTest() {
		super.runAfterTest();
		if (out != null) {
			out.close();
		}
	}

	/**
	 * Returns a measurement of the most recently taken step.
	 * 
	 * @return a step measurement
	 */
	protected Map<String, Object> getStepMeasurement() {
		final Map<String, Object> measurement = new LinkedHashMap<>();
		measurement.put("Iteration", getIteration());
		measurement.put("Step", getStep());
		measurement.put("Result", getResult());
		return measurement;
	}

	/**
	 * Returns the output directory containing the output file.
	 * 
	 * @return the output directory
	 * @see {@link #getOutFile()}
	 */
	public File getOutDir() {
		return OUT_DIR;
	}

	/**
	 * Returns the output file to write the measurements to.
	 * 
	 * @return the output file
	 */
	public File getOutFile() {
		return new File(getOutDir(), String.format("%s/%s.csv", getClass().getSimpleName(), getName()));
	}

	/**
	 * Opens the output stream.
	 * 
	 * @return the output stream
	 */
	private PrintWriter openOut() {
		final File outFile = getOutFile();
		try {
			outFile.getParentFile().mkdirs();
			outFile.createNewFile();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		try {
			return new PrintWriter(outFile);
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Returns the given elements as comma-separated values
	 * 
	 * @param elements elements to transform
	 * @return the given elements as comma-separated values
	 */
	private static String asCsv(Collection<?> elements) {
		String s = "";
		boolean first = true;
		for (final Object element : elements) {
			if (first) {
				first = false;
			} else {
				s += ",";
			}
			s += '"' + String.valueOf(element).replace("\\", "\\\\").replace("\"", "\\\"") + '"';
		}
		return s;
	}
}
