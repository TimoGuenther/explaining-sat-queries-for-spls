package de.ovgu.featureide.fm.core.explanations.evaluation;

import java.util.Map;

/**
 * A test case that can be run to obtain its results.
 * 
 * @author Timo G&uuml;nther
 */
public interface Test extends Runnable {

	/**
	 * Returns the name identifying the test case.
	 * 
	 * @return the name; not null
	 */
	public String getName();

	/**
	 * Returns the test results.
	 * 
	 * @return the test results; not null
	 */
	public Map<String, Object> getTestResults();

	/**
	 * Executes the test case.
	 */
	@Override
	public void run();
}
