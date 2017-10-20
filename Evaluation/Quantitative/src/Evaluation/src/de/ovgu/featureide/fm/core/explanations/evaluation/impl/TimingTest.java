package de.ovgu.featureide.fm.core.explanations.evaluation.impl;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * A test that measures time.
 * All measurements are in nanoseconds.
 * </p>
 * 
 * <p>
 * Test execution is preceded by a warm-up phase.
 * During the warm-up phase, the test is repeated until a set duration has expired.
 * This is to avoid timing biases.
 * </p>
 * 
 * @param <R> the type of the results of each step
 * @author Timo G&uuml;nther
 */
public abstract class TimingTest<R> extends MeasuringTest<R> {

	/** The minimum duration of the warm-up phase. */
	private static final long WARM_UP_DURATION = TimeUnit.SECONDS.toNanos(5);

	/** The time when the most recent step started. */
	private long stepStartTime;
	/** The time when the most recent step ended. */
	private long stepStopTime;
	/** The duration of the most recent step. */
	private long stepDuration;
	/** The duration of the current iteration. */
	private long iterationDuration;
	/** The total duration of the test. */
	private long testDuration;
	/** The total duration of the warm up test. */
	private long warmUpDuration = -1L;

	/**
	 * Constructs a new instance of this class.
	 * 
	 * @param iterations number of iterations to run
	 */
	protected TimingTest(int iterations) {
		super(iterations);
	}

	/**
	 * Returns the time when the most recent step started.
	 * 
	 * @return the step start time
	 */
	protected long getStepStartTime() {
		return stepStartTime;
	}

	/**
	 * Returns the time when the most recent step ended.
	 * 
	 * @return the step end time
	 */
	protected long getStepStopTime() {
		return stepStopTime;
	}

	/**
	 * Returns the duration of the most recent step.
	 * 
	 * @return the step duration
	 */
	protected long getStepDuration() {
		return stepDuration;
	}

	/**
	 * Returns the average step duration throughout the test.
	 * 
	 * @return the average step duration
	 */
	public long getAverageStepDuration() {
		return getTestDuration() / Math.max(1, getSteps());
	}

	/**
	 * Returns the duration of the current iteration.
	 * 
	 * @return the iteration duration
	 */
	protected long getIterationDuration() {
		return iterationDuration;
	}

	/**
	 * Returns the average iteration duration throughout the test.
	 * 
	 * @return the average iteration duration
	 */
	public long getAverageIterationDuration() {
		return getTestDuration() / Math.max(1, getIterations());
	}

	/**
	 * Returns the total duration of the test.
	 * 
	 * @return the test duration
	 */
	public long getTestDuration() {
		return testDuration;
	}

	/**
	 * Returns true iff the test is currently in the warm-up phase.
	 * @return true iff warming up
	 */
	protected boolean isWarmingUp() {
		return warmUpDuration >= 0 && warmUpDuration < WARM_UP_DURATION;
	}

	@Override
	protected boolean isMeasuring() {
		return !isWarmingUp();
	}

	@Override
	public Map<String, Object> getTestResults() {
		final Map<String, Object> results = super.getTestResults();
		results.put("Duration", getTestDuration());
		results.put("Avg It Dur", getAverageIterationDuration());
		results.put("Avg St Dur", getAverageStepDuration());
		return results;
	}

	@Override
	protected Map<String, Object> getStepMeasurement() {
		final Map<String, Object> measurement = super.getStepMeasurement();
		measurement.put("Start", getStepStartTime());
		measurement.put("Stop", getStepStopTime());
		measurement.put("Duration", getStepDuration());
		return measurement;
	}

	@Override
	protected void runTest() {
		warmUpDuration = 0;
		while (isWarmingUp()) {
			runTest(1);
			warmUpDuration += getTestDuration();
		}
		super.runTest();
	}

	@Override
	protected void runBeforeIterations() {
		super.runBeforeIterations();
		stepDuration = 0L;
		iterationDuration = 0L;
		testDuration = 0L;
	}

	@Override
	protected void runBeforeStep() {
		super.runBeforeStep();
		stepStartTime = System.nanoTime();
	}

	@Override
	protected void runAfterStep() {
		stepStopTime = System.nanoTime();
		stepDuration = stepStopTime - stepStartTime;
		iterationDuration += stepDuration;
		testDuration += stepDuration;
		super.runAfterStep();
	}
}
