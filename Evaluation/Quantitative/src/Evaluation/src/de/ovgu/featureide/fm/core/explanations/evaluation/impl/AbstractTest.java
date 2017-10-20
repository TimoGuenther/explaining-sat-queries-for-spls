package de.ovgu.featureide.fm.core.explanations.evaluation.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import de.ovgu.featureide.fm.core.explanations.evaluation.Test;

/**
 * Abstract implementation of {@link Test}.
 * A test composes any number of iterations.
 * Each iteration in turn composes any number of steps.
 * Subclasses may hook into the methods called at the start and the end of each scope to prepare the state.
 * 
 * @param <R> the type of the results of each step
 * @author Timo G&uuml;nther
 */
public abstract class AbstractTest<R> implements Test {

	/** The default number of iterations to run. */
	public static final int ITERATIONS = 10;

	/** The number of iterations to run. */
	private final int iterations;
	
	/** The current iteration. */
	private int iteration;
	/** The current step. */
	private int step;
	/** The number of steps taken since the start of the test. */
	private int steps;
	/** The result of the most recent step. */
	private R result;

	/**
	 * Constructs a new instance of this class.
	 * 
	 * @param iterations number of iterations to run
	 */
	protected AbstractTest(int iterations) {
		this.iterations = iterations;
	}

	@Override
	public void run() {
		runBeforeTest();
		try {
			runTest();
		} finally {
			runAfterTest();
		}
	}

	/**
	 * Returns the number of iterations to run.
	 * 
	 * @return the number of iterations to run
	 */
	public int getIterations() {
		return iterations;
	}

	/**
	 * Returns the current iteration.
	 * That is the amount of iterations finished since the start of the test.
	 * 
	 * @return the current iteration
	 */
	protected int getIteration() {
		return iteration;
	}

	/**
	 * Returns the number of steps taken since the start of the test.
	 * 
	 * @return the number of steps
	 */
	public int getSteps() {
		return steps;
	}

	/**
	 * Returns the current step.
	 * That is the amount of steps finished since the start of the iteration.
	 * 
	 * @return the current step
	 */
	protected int getStep() {
		return step;
	}

	/**
	 * Returns the result of the most recent step.
	 * 
	 * @return the step result
	 */
	protected R getResult() {
		return result;
	}

	/**
	 * Called before the test starts.
	 * Can be used to set fields that would normally be set in the constructor.
	 */
	protected void runBeforeTest() {}

	/**
	 * Runs the test with the set number of iterations.
	 */
	protected void runTest() {
		runTest(getIterations());
	}

	/**
	 * Runs the test with the given number of iterations.
	 * 
	 * @param iterations the number of iterations to run
	 */
	protected void runTest(int iterations) {
		steps = 0;
		runBeforeIterations();
		for (iteration = 0; iteration < iterations; iteration++) {
			runBeforeIteration();
			step = 0;
			while (hasNextStep()) {
				runBeforeStep();
				result = runStep();
				runAfterStep();
				step++;
				steps++;
			}
			runAfterIteration();
		}
		runAfterIterations();
	}

	/**
	 * Called before the iterations start.
	 */
	protected void runBeforeIterations() {}

	/**
	 * Called before an iteration starts.
	 */
	protected void runBeforeIteration() {}

	/**
	 * Determines whether another step needs to be taken.
	 * 
	 * @return true to take another step
	 */
	protected boolean hasNextStep() {
		return iteration == 0;
	}

	/**
	 * Called before a step starts.
	 */
	protected void runBeforeStep() {}

	/**
	 * Takes a single step.
	 * This is typically the most important part of the test.
	 * 
	 * @return the result of the step
	 */
	protected abstract R runStep();

	/**
	 * Called after a step ends.
	 */
	protected void runAfterStep() {}

	/**
	 * Called after an iteration ends.
	 */
	protected void runAfterIteration() {}

	/**
	 * Called after the iterations end.
	 */
	protected void runAfterIterations() {}

	/**
	 * Called after the test ends.
	 * Can be used to clean up fields.
	 */
	protected void runAfterTest() {}

	@Override
	public Map<String, Object> getTestResults() {
		final Map<String, Object> results = new LinkedHashMap<>();
		results.put("Name", getName());
		results.put("Iterations", getIteration());
		results.put("Steps", getSteps());
		return results;
	}

	@Override
	public String toString() {
		return String.format("%s[%s]", getClass().getSimpleName(), getName());
	}
}
